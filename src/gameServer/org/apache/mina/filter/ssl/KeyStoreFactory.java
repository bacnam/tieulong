/*     */ package org.apache.mina.filter.ssl;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.security.KeyStore;
/*     */ import java.security.KeyStoreException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.NoSuchProviderException;
/*     */ import java.security.cert.CertificateException;
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
/*     */ public class KeyStoreFactory
/*     */ {
/*  43 */   private String type = "JKS";
/*     */   
/*  45 */   private String provider = null;
/*     */   
/*  47 */   private char[] password = null;
/*     */   
/*  49 */   private byte[] data = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public KeyStore newInstance() throws KeyStoreException, NoSuchProviderException, NoSuchAlgorithmException, CertificateException, IOException {
/*     */     KeyStore ks;
/*  59 */     if (this.data == null) {
/*  60 */       throw new IllegalStateException("data property is not set.");
/*     */     }
/*     */ 
/*     */     
/*  64 */     if (this.provider == null) {
/*  65 */       ks = KeyStore.getInstance(this.type);
/*     */     } else {
/*  67 */       ks = KeyStore.getInstance(this.type, this.provider);
/*     */     } 
/*     */     
/*  70 */     InputStream is = new ByteArrayInputStream(this.data);
/*     */     try {
/*  72 */       ks.load(is, this.password);
/*     */     } finally {
/*     */       try {
/*  75 */         is.close();
/*  76 */       } catch (IOException ignored) {}
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/*  81 */     return ks;
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
/*     */   public void setType(String type) {
/*  93 */     if (type == null) {
/*  94 */       throw new IllegalArgumentException("type");
/*     */     }
/*  96 */     this.type = type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPassword(String password) {
/* 107 */     if (password != null) {
/* 108 */       this.password = password.toCharArray();
/*     */     } else {
/* 110 */       this.password = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProvider(String provider) {
/* 121 */     this.provider = provider;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setData(byte[] data) {
/* 130 */     byte[] copy = new byte[data.length];
/* 131 */     System.arraycopy(data, 0, copy, 0, data.length);
/* 132 */     this.data = copy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void setData(InputStream dataStream) throws IOException {
/* 141 */     ByteArrayOutputStream out = new ByteArrayOutputStream();
/*     */     try {
/*     */       while (true) {
/* 144 */         int data = dataStream.read();
/* 145 */         if (data < 0) {
/*     */           break;
/*     */         }
/* 148 */         out.write(data);
/*     */       } 
/* 150 */       setData(out.toByteArray());
/*     */     } finally {
/*     */       try {
/* 153 */         dataStream.close();
/* 154 */       } catch (IOException e) {}
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
/*     */   public void setDataFile(File dataFile) throws IOException {
/* 166 */     setData(new BufferedInputStream(new FileInputStream(dataFile)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDataUrl(URL dataUrl) throws IOException {
/* 175 */     setData(dataUrl.openStream());
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/ssl/KeyStoreFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */