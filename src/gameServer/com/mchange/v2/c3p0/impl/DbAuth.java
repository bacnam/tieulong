/*     */ package com.mchange.v2.c3p0.impl;
/*     */ 
/*     */ import com.mchange.v2.lang.ObjectUtils;
/*     */ import com.mchange.v2.ser.UnsupportedVersionException;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
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
/*     */ public final class DbAuth
/*     */   implements Serializable
/*     */ {
/*     */   transient String username;
/*     */   transient String password;
/*     */   static final long serialVersionUID = 1L;
/*     */   private static final short VERSION = 1;
/*     */   
/*     */   public DbAuth(String username, String password) {
/*  49 */     this.username = username;
/*  50 */     this.password = password;
/*     */   }
/*     */   
/*     */   public String getUser() {
/*  54 */     return this.username;
/*     */   }
/*     */   public String getPassword() {
/*  57 */     return this.password;
/*     */   }
/*     */   public String getMaskedUserString() {
/*  60 */     return getMaskedUserString(2, 8);
/*     */   }
/*     */   
/*     */   private String getMaskedUserString(int chars_to_reveal, int total_chars) {
/*  64 */     if (this.username == null) return "null";
/*     */ 
/*     */     
/*  67 */     StringBuffer sb = new StringBuffer(32);
/*  68 */     if (this.username.length() >= chars_to_reveal) {
/*     */       
/*  70 */       sb.append(this.username.substring(0, chars_to_reveal));
/*  71 */       for (int i = 0, len = total_chars - chars_to_reveal; i < len; i++) {
/*  72 */         sb.append('*');
/*     */       }
/*     */     } else {
/*  75 */       sb.append(this.username);
/*  76 */     }  return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/*  82 */     if (this == o)
/*  83 */       return true; 
/*  84 */     if (o != null && getClass() == o.getClass()) {
/*     */       
/*  86 */       DbAuth other = (DbAuth)o;
/*  87 */       return (ObjectUtils.eqOrBothNull(this.username, other.username) && ObjectUtils.eqOrBothNull(this.password, other.password));
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  92 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/*  97 */     return ObjectUtils.hashOrZero(this.username) ^ ObjectUtils.hashOrZero(this.password);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream out) throws IOException {
/* 108 */     out.writeShort(1);
/* 109 */     out.writeObject(this.username);
/* 110 */     out.writeObject(this.password);
/*     */   }
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
/* 115 */     short version = in.readShort();
/* 116 */     switch (version) {
/*     */       
/*     */       case 1:
/* 119 */         this.username = (String)in.readObject();
/* 120 */         this.password = (String)in.readObject();
/*     */         return;
/*     */     } 
/* 123 */     throw new UnsupportedVersionException(this, version);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/impl/DbAuth.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */