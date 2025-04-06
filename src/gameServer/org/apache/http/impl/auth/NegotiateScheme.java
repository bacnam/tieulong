/*     */ package org.apache.http.impl.auth;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.http.Header;
/*     */ import org.apache.http.HttpRequest;
/*     */ import org.apache.http.auth.AuthenticationException;
/*     */ import org.apache.http.auth.Credentials;
/*     */ import org.apache.http.protocol.HttpContext;
/*     */ import org.apache.http.util.Args;
/*     */ import org.ietf.jgss.GSSException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public class NegotiateScheme
/*     */   extends GGSSchemeBase
/*     */ {
/*  53 */   private final Log log = LogFactory.getLog(getClass());
/*     */ 
/*     */   
/*     */   private static final String SPNEGO_OID = "1.3.6.1.5.5.2";
/*     */ 
/*     */   
/*     */   private static final String KERBEROS_OID = "1.2.840.113554.1.2.2";
/*     */   
/*     */   private final SpnegoTokenGenerator spengoGenerator;
/*     */ 
/*     */   
/*     */   public NegotiateScheme(SpnegoTokenGenerator spengoGenerator, boolean stripPort) {
/*  65 */     super(stripPort);
/*  66 */     this.spengoGenerator = spengoGenerator;
/*     */   }
/*     */   
/*     */   public NegotiateScheme(SpnegoTokenGenerator spengoGenerator) {
/*  70 */     this(spengoGenerator, false);
/*     */   }
/*     */   
/*     */   public NegotiateScheme() {
/*  74 */     this(null, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSchemeName() {
/*  83 */     return "Negotiate";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Header authenticate(Credentials credentials, HttpRequest request) throws AuthenticationException {
/*  90 */     return authenticate(credentials, request, null);
/*     */   }
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
/*     */   public Header authenticate(Credentials credentials, HttpRequest request, HttpContext context) throws AuthenticationException {
/* 111 */     return super.authenticate(credentials, request, context);
/*     */   }
/*     */ 
/*     */   
/*     */   protected byte[] generateToken(byte[] input, String authServer) throws GSSException {
/* 116 */     return super.generateToken(input, authServer);
/*     */   }
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
/*     */   protected byte[] generateToken(byte[] input, String authServer, Credentials credentials) throws GSSException {
/* 136 */     Oid negotiationOid = new Oid("1.3.6.1.5.5.2");
/*     */     
/* 138 */     byte[] token = input;
/* 139 */     boolean tryKerberos = false;
/*     */     try {
/* 141 */       token = generateGSSToken(token, negotiationOid, authServer, credentials);
/* 142 */     } catch (GSSException ex) {
/*     */ 
/*     */       
/* 145 */       if (ex.getMajor() == 2) {
/* 146 */         this.log.debug("GSSException BAD_MECH, retry with Kerberos MECH");
/* 147 */         tryKerberos = true;
/*     */       } else {
/* 149 */         throw ex;
/*     */       } 
/*     */     } 
/*     */     
/* 153 */     if (tryKerberos) {
/*     */       
/* 155 */       this.log.debug("Using Kerberos MECH 1.2.840.113554.1.2.2");
/* 156 */       negotiationOid = new Oid("1.2.840.113554.1.2.2");
/* 157 */       token = generateGSSToken(token, negotiationOid, authServer, credentials);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 163 */       if (token != null && this.spengoGenerator != null) {
/*     */         try {
/* 165 */           token = this.spengoGenerator.generateSpnegoDERObject(token);
/* 166 */         } catch (IOException ex) {
/* 167 */           this.log.error(ex.getMessage(), ex);
/*     */         } 
/*     */       }
/*     */     } 
/* 171 */     return token;
/*     */   }
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
/*     */   public String getParameter(String name) {
/* 185 */     Args.notNull(name, "Parameter name");
/* 186 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRealm() {
/* 196 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isConnectionBased() {
/* 206 */     return true;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/impl/auth/NegotiateScheme.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */