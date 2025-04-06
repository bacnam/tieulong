/*     */ package org.apache.http.impl.auth;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import org.apache.commons.codec.binary.Base64;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpHost;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.annotation.NotThreadSafe;
/*     */ import org.apache.http.auth.AuthenticationException;
/*     */ import org.apache.http.auth.Credentials;
/*     */ import org.apache.http.auth.InvalidCredentialsException;
/*     */ import org.apache.http.auth.KerberosCredentials;
/*     */ import org.apache.http.auth.MalformedChallengeException;
/*     */ import org.apache.http.conn.routing.HttpRoute;
/*     */ import org.apache.http.message.BufferedHeader;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ import org.apache.http.util.Args;
/*     */ import org.apache.http.util.CharArrayBuffer;
/*     */ import org.ietf.jgss.GSSContext;
/*     */ import org.ietf.jgss.GSSCredential;
/*     */ import org.ietf.jgss.GSSException;
/*     */ import org.ietf.jgss.GSSManager;
/*     */ import org.ietf.jgss.GSSName;
/*     */ import org.ietf.jgss.Oid;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @NotThreadSafe
/*     */ public abstract class GGSSchemeBase
/*     */   extends AuthSchemeBase
/*     */ {
/*     */   enum State
/*     */   {
/*  65 */     UNINITIATED,
/*  66 */     CHALLENGE_RECEIVED,
/*  67 */     TOKEN_GENERATED,
/*  68 */     FAILED;
/*     */   }
/*     */   
/*  71 */   private final Log log = LogFactory.getLog(getClass());
/*     */   
/*     */   private final Base64 base64codec;
/*     */   
/*     */   private final boolean stripPort;
/*     */   
/*     */   private final boolean useCanonicalHostname;
/*     */   
/*     */   private State state;
/*     */   
/*     */   private byte[] token;
/*     */ 
/*     */   
/*     */   GGSSchemeBase(boolean stripPort, boolean useCanonicalHostname) {
/*  85 */     this.base64codec = new Base64(0);
/*  86 */     this.stripPort = stripPort;
/*  87 */     this.useCanonicalHostname = useCanonicalHostname;
/*  88 */     this.state = State.UNINITIATED;
/*     */   }
/*     */   
/*     */   GGSSchemeBase(boolean stripPort) {
/*  92 */     this(stripPort, true);
/*     */   }
/*     */   
/*     */   GGSSchemeBase() {
/*  96 */     this(true, true);
/*     */   }
/*     */   
/*     */   protected GSSManager getManager() {
/* 100 */     return GSSManager.getInstance();
/*     */   }
/*     */ 
/*     */   
/*     */   protected byte[] generateGSSToken(byte[] input, Oid oid, String authServer) throws GSSException {
/* 105 */     return generateGSSToken(input, oid, authServer, (Credentials)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected byte[] generateGSSToken(byte[] input, Oid oid, String authServer, Credentials credentials) throws GSSException {
/*     */     GSSCredential gssCredential;
/* 114 */     byte[] inputBuff = input;
/* 115 */     if (inputBuff == null) {
/* 116 */       inputBuff = new byte[0];
/*     */     }
/* 118 */     GSSManager manager = getManager();
/* 119 */     GSSName serverName = manager.createName("HTTP@" + authServer, GSSName.NT_HOSTBASED_SERVICE);
/*     */ 
/*     */     
/* 122 */     if (credentials instanceof KerberosCredentials) {
/* 123 */       gssCredential = ((KerberosCredentials)credentials).getGSSCredential();
/*     */     } else {
/* 125 */       gssCredential = null;
/*     */     } 
/*     */     
/* 128 */     GSSContext gssContext = manager.createContext(serverName.canonicalize(oid), oid, gssCredential, 0);
/*     */     
/* 130 */     gssContext.requestMutualAuth(true);
/* 131 */     gssContext.requestCredDeleg(true);
/* 132 */     return gssContext.initSecContext(inputBuff, 0, inputBuff.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   protected byte[] generateToken(byte[] input, String authServer) throws GSSException {
/* 140 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected byte[] generateToken(byte[] input, String authServer, Credentials credentials) throws GSSException {
/* 150 */     return generateToken(input, authServer);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isComplete() {
/* 155 */     return (this.state == State.TOKEN_GENERATED || this.state == State.FAILED);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public Header authenticate(Credentials credentials, HttpRequest request) throws AuthenticationException {
/* 167 */     return authenticate(credentials, request, (HttpContext)null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Header authenticate(Credentials credentials, HttpRequest request, HttpContext context) throws AuthenticationException {
/*     */     String tokenstr;
/*     */     CharArrayBuffer buffer;
/* 175 */     Args.notNull(request, "HTTP request");
/* 176 */     switch (this.state) {
/*     */       case UNINITIATED:
/* 178 */         throw new AuthenticationException(getSchemeName() + " authentication has not been initiated");
/*     */       case FAILED:
/* 180 */         throw new AuthenticationException(getSchemeName() + " authentication has failed");
/*     */       case CHALLENGE_RECEIVED:
/*     */         try {
/* 183 */           HttpHost host; String authServer; HttpRoute route = (HttpRoute)context.getAttribute("http.route");
/* 184 */           if (route == null) {
/* 185 */             throw new AuthenticationException("Connection route is not available");
/*     */           }
/*     */           
/* 188 */           if (isProxy()) {
/* 189 */             host = route.getProxyHost();
/* 190 */             if (host == null) {
/* 191 */               host = route.getTargetHost();
/*     */             }
/*     */           } else {
/* 194 */             host = route.getTargetHost();
/*     */           } 
/*     */           
/* 197 */           String hostname = host.getHostName();
/*     */           
/* 199 */           if (this.useCanonicalHostname) {
/*     */             
/*     */             try {
/*     */ 
/*     */ 
/*     */               
/* 205 */               hostname = resolveCanonicalHostname(hostname);
/* 206 */             } catch (UnknownHostException ignore) {}
/*     */           }
/*     */           
/* 209 */           if (this.stripPort) {
/* 210 */             authServer = hostname;
/*     */           } else {
/* 212 */             authServer = hostname + ":" + host.getPort();
/*     */           } 
/*     */           
/* 215 */           if (this.log.isDebugEnabled()) {
/* 216 */             this.log.debug("init " + authServer);
/*     */           }
/* 218 */           this.token = generateToken(this.token, authServer, credentials);
/* 219 */           this.state = State.TOKEN_GENERATED;
/* 220 */         } catch (GSSException gsse) {
/* 221 */           this.state = State.FAILED;
/* 222 */           if (gsse.getMajor() == 9 || gsse.getMajor() == 8)
/*     */           {
/* 224 */             throw new InvalidCredentialsException(gsse.getMessage(), gsse);
/*     */           }
/* 226 */           if (gsse.getMajor() == 13) {
/* 227 */             throw new InvalidCredentialsException(gsse.getMessage(), gsse);
/*     */           }
/* 229 */           if (gsse.getMajor() == 10 || gsse.getMajor() == 19 || gsse.getMajor() == 20)
/*     */           {
/*     */             
/* 232 */             throw new AuthenticationException(gsse.getMessage(), gsse);
/*     */           }
/*     */           
/* 235 */           throw new AuthenticationException(gsse.getMessage());
/*     */         } 
/*     */       case TOKEN_GENERATED:
/* 238 */         tokenstr = new String(this.base64codec.encode(this.token));
/* 239 */         if (this.log.isDebugEnabled()) {
/* 240 */           this.log.debug("Sending response '" + tokenstr + "' back to the auth server");
/*     */         }
/* 242 */         buffer = new CharArrayBuffer(32);
/* 243 */         if (isProxy()) {
/* 244 */           buffer.append("Proxy-Authorization");
/*     */         } else {
/* 246 */           buffer.append("Authorization");
/*     */         } 
/* 248 */         buffer.append(": Negotiate ");
/* 249 */         buffer.append(tokenstr);
/* 250 */         return (Header)new BufferedHeader(buffer);
/*     */     } 
/* 252 */     throw new IllegalStateException("Illegal state: " + this.state);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void parseChallenge(CharArrayBuffer buffer, int beginIndex, int endIndex) throws MalformedChallengeException {
/* 260 */     String challenge = buffer.substringTrimmed(beginIndex, endIndex);
/* 261 */     if (this.log.isDebugEnabled()) {
/* 262 */       this.log.debug("Received challenge '" + challenge + "' from the auth server");
/*     */     }
/* 264 */     if (this.state == State.UNINITIATED) {
/* 265 */       this.token = Base64.decodeBase64(challenge.getBytes());
/* 266 */       this.state = State.CHALLENGE_RECEIVED;
/*     */     } else {
/* 268 */       this.log.debug("Authentication already attempted");
/* 269 */       this.state = State.FAILED;
/*     */     } 
/*     */   }
/*     */   
/*     */   private String resolveCanonicalHostname(String host) throws UnknownHostException {
/* 274 */     InetAddress in = InetAddress.getByName(host);
/* 275 */     String canonicalServer = in.getCanonicalHostName();
/* 276 */     if (in.getHostAddress().contentEquals(canonicalServer)) {
/* 277 */       return host;
/*     */     }
/* 279 */     return canonicalServer;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/auth/GGSSchemeBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */