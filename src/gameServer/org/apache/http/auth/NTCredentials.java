/*     */ package org.apache.http.auth;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.security.Principal;
/*     */ import java.util.Locale;
/*     */ import org.apache.http.annotation.Immutable;
/*     */ import org.apache.http.util.Args;
/*     */ import org.apache.http.util.LangUtils;
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
/*     */ @Immutable
/*     */ public class NTCredentials
/*     */   implements Credentials, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -7385699315228907265L;
/*     */   private final NTUserPrincipal principal;
/*     */   private final String password;
/*     */   private final String workstation;
/*     */   
/*     */   public NTCredentials(String usernamePassword) {
/*     */     String username;
/*  65 */     Args.notNull(usernamePassword, "Username:password string");
/*     */     
/*  67 */     int atColon = usernamePassword.indexOf(':');
/*  68 */     if (atColon >= 0) {
/*  69 */       username = usernamePassword.substring(0, atColon);
/*  70 */       this.password = usernamePassword.substring(atColon + 1);
/*     */     } else {
/*  72 */       username = usernamePassword;
/*  73 */       this.password = null;
/*     */     } 
/*  75 */     int atSlash = username.indexOf('/');
/*  76 */     if (atSlash >= 0) {
/*  77 */       this.principal = new NTUserPrincipal(username.substring(0, atSlash).toUpperCase(Locale.ROOT), username.substring(atSlash + 1));
/*     */     }
/*     */     else {
/*     */       
/*  81 */       this.principal = new NTUserPrincipal(null, username.substring(atSlash + 1));
/*     */     } 
/*     */ 
/*     */     
/*  85 */     this.workstation = null;
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
/*     */   public NTCredentials(String userName, String password, String workstation, String domain) {
/* 103 */     Args.notNull(userName, "User name");
/* 104 */     this.principal = new NTUserPrincipal(domain, userName);
/* 105 */     this.password = password;
/* 106 */     if (workstation != null) {
/* 107 */       this.workstation = workstation.toUpperCase(Locale.ROOT);
/*     */     } else {
/* 109 */       this.workstation = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Principal getUserPrincipal() {
/* 115 */     return this.principal;
/*     */   }
/*     */   
/*     */   public String getUserName() {
/* 119 */     return this.principal.getUsername();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPassword() {
/* 124 */     return this.password;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDomain() {
/* 133 */     return this.principal.getDomain();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getWorkstation() {
/* 142 */     return this.workstation;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 147 */     int hash = 17;
/* 148 */     hash = LangUtils.hashCode(hash, this.principal);
/* 149 */     hash = LangUtils.hashCode(hash, this.workstation);
/* 150 */     return hash;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 155 */     if (this == o) {
/* 156 */       return true;
/*     */     }
/* 158 */     if (o instanceof NTCredentials) {
/* 159 */       NTCredentials that = (NTCredentials)o;
/* 160 */       if (LangUtils.equals(this.principal, that.principal) && LangUtils.equals(this.workstation, that.workstation))
/*     */       {
/* 162 */         return true;
/*     */       }
/*     */     } 
/* 165 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 170 */     StringBuilder buffer = new StringBuilder();
/* 171 */     buffer.append("[principal: ");
/* 172 */     buffer.append(this.principal);
/* 173 */     buffer.append("][workstation: ");
/* 174 */     buffer.append(this.workstation);
/* 175 */     buffer.append("]");
/* 176 */     return buffer.toString();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/auth/NTCredentials.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */