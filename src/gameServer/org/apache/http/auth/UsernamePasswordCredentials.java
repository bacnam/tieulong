/*     */ package org.apache.http.auth;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.security.Principal;
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
/*     */ @Immutable
/*     */ public class UsernamePasswordCredentials
/*     */   implements Credentials, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 243343858802739403L;
/*     */   private final BasicUserPrincipal principal;
/*     */   private final String password;
/*     */   
/*     */   public UsernamePasswordCredentials(String usernamePassword) {
/*  58 */     Args.notNull(usernamePassword, "Username:password string");
/*  59 */     int atColon = usernamePassword.indexOf(':');
/*  60 */     if (atColon >= 0) {
/*  61 */       this.principal = new BasicUserPrincipal(usernamePassword.substring(0, atColon));
/*  62 */       this.password = usernamePassword.substring(atColon + 1);
/*     */     } else {
/*  64 */       this.principal = new BasicUserPrincipal(usernamePassword);
/*  65 */       this.password = null;
/*     */     } 
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
/*     */   public UsernamePasswordCredentials(String userName, String password) {
/*  78 */     Args.notNull(userName, "Username");
/*  79 */     this.principal = new BasicUserPrincipal(userName);
/*  80 */     this.password = password;
/*     */   }
/*     */ 
/*     */   
/*     */   public Principal getUserPrincipal() {
/*  85 */     return this.principal;
/*     */   }
/*     */   
/*     */   public String getUserName() {
/*  89 */     return this.principal.getName();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPassword() {
/*  94 */     return this.password;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  99 */     return this.principal.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 104 */     if (this == o) {
/* 105 */       return true;
/*     */     }
/* 107 */     if (o instanceof UsernamePasswordCredentials) {
/* 108 */       UsernamePasswordCredentials that = (UsernamePasswordCredentials)o;
/* 109 */       if (LangUtils.equals(this.principal, that.principal)) {
/* 110 */         return true;
/*     */       }
/*     */     } 
/* 113 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 118 */     return this.principal.toString();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/http/auth/UsernamePasswordCredentials.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */