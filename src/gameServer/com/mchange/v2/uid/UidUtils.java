/*     */ package com.mchange.v2.uid;
/*     */ 
/*     */ import com.mchange.v2.log.MLevel;
/*     */ import com.mchange.v2.log.MLog;
/*     */ import com.mchange.v2.log.MLogger;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.security.SecureRandom;
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
/*     */ public final class UidUtils
/*     */ {
/*  52 */   static final MLogger logger = MLog.getLogger(UidUtils.class);
/*     */   
/*  54 */   public static final String VM_ID = generateVmId();
/*     */ 
/*     */   
/*  57 */   private static long within_vm_seq_counter = 0L;
/*     */ 
/*     */   
/*     */   private static String generateVmId() {
/*  61 */     DataOutputStream dataOutputStream = null;
/*  62 */     DataInputStream dataInputStream = null;
/*     */     
/*     */     try {
/*  65 */       SecureRandom secureRandom = new SecureRandom();
/*  66 */       ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
/*  67 */       dataOutputStream = new DataOutputStream(byteArrayOutputStream);
/*     */       
/*     */       try {
/*  70 */         dataOutputStream.write(InetAddress.getLocalHost().getAddress());
/*     */       }
/*  72 */       catch (Exception exception) {
/*     */         
/*  74 */         if (logger.isLoggable(MLevel.INFO))
/*  75 */           logger.log(MLevel.INFO, "Failed to get local InetAddress for VMID. This is unlikely to matter. At all. We'll add some extra randomness", exception); 
/*  76 */         dataOutputStream.write(secureRandom.nextInt());
/*     */       } 
/*  78 */       dataOutputStream.writeLong(System.currentTimeMillis());
/*  79 */       dataOutputStream.write(secureRandom.nextInt());
/*     */       
/*  81 */       int i = byteArrayOutputStream.size() % 4;
/*  82 */       if (i > 0) {
/*     */         
/*  84 */         int k = 4 - i;
/*  85 */         byte[] arrayOfByte1 = new byte[k];
/*  86 */         secureRandom.nextBytes(arrayOfByte1);
/*  87 */         dataOutputStream.write(arrayOfByte1);
/*     */       } 
/*     */       
/*  90 */       StringBuffer stringBuffer = new StringBuffer(32);
/*  91 */       byte[] arrayOfByte = byteArrayOutputStream.toByteArray();
/*  92 */       dataInputStream = new DataInputStream(new ByteArrayInputStream(arrayOfByte)); byte b; int j;
/*  93 */       for (b = 0, j = arrayOfByte.length / 4; b < j; b++) {
/*     */         
/*  95 */         int k = dataInputStream.readInt();
/*  96 */         long l = k & 0xFFFFFFFFL;
/*  97 */         stringBuffer.append(Long.toString(l, 36));
/*     */       } 
/*  99 */       return stringBuffer.toString();
/*     */     }
/* 101 */     catch (IOException iOException) {
/*     */       
/* 103 */       if (logger.isLoggable(MLevel.WARNING)) {
/* 104 */         logger.log(MLevel.WARNING, "Bizarro! IOException while reading/writing from ByteArray-based streams? We're skipping the VMID thing. It almost certainly doesn't matter, but please report the error.", iOException);
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 109 */       return "";
/*     */     } finally {
/*     */ 
/*     */       
/*     */       try {
/*     */         
/* 115 */         if (dataOutputStream != null) dataOutputStream.close(); 
/* 116 */       } catch (IOException iOException) {
/* 117 */         logger.log(MLevel.WARNING, "Huh? Exception close()ing a byte-array bound OutputStream.", iOException);
/* 118 */       }  try { if (dataInputStream != null) dataInputStream.close();  }
/* 119 */       catch (IOException iOException)
/* 120 */       { logger.log(MLevel.WARNING, "Huh? Exception close()ing a byte-array bound IntputStream.", iOException); }
/*     */     
/*     */     } 
/*     */   }
/*     */   private static synchronized long nextWithinVmSeq() {
/* 125 */     return ++within_vm_seq_counter;
/*     */   }
/*     */   public static String allocateWithinVmSequential() {
/* 128 */     return VM_ID + "#" + nextWithinVmSeq();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/uid/UidUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */