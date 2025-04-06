/*     */ package com.zhonglian.server.common.utils.secure;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XPEncrypt
/*     */ {
/*     */   public static final int ALIGN = 8;
/*     */   public static final int MAGIC = 1164408404;
/*     */   private byte[] eData;
/*     */   private ByteBuffer eBuffer;
/*     */   private int eSize;
/*     */   private int key;
/*     */   
/*     */   public XPEncrypt(int key) {
/*  18 */     this.key = key;
/*     */   }
/*     */   
/*     */   public ByteBuffer encrypt(ByteBuffer data) {
/*  22 */     return encrypt(data.array());
/*     */   }
/*     */   
/*     */   public ByteBuffer encrypt(byte[] data) {
/*  26 */     if (data == null) {
/*  27 */       return null;
/*     */     }
/*     */     
/*  30 */     this.eData = data;
/*  31 */     paddingAndMakeBuffer();
/*  32 */     return doEncrypt();
/*     */   }
/*     */   
/*     */   public ByteBuffer decrypt(byte[] data) {
/*  36 */     ByteBuffer dst = ByteBuffer.allocate(data.length);
/*  37 */     ByteBuffer src = ByteBuffer.wrap(data);
/*  38 */     XPEncryptGroup group = new XPEncryptGroup(this.key);
/*     */     
/*  40 */     if (data.length % 8 != 0) {
/*  41 */       return null;
/*     */     }
/*     */     
/*  44 */     while (src.hasRemaining()) {
/*  45 */       dst.putLong(group.decode(src.getLong()));
/*     */     }
/*     */     
/*  48 */     int dataSize = dst.getInt(data.length - 12);
/*     */     
/*  50 */     int sum = 0;
/*  51 */     dst.rewind();
/*  52 */     while (dst.remaining() > 8) {
/*  53 */       sum += dst.getInt();
/*     */     }
/*     */     
/*  56 */     if (dst.getInt(data.length - 8) != 1164408404 || dst.getInt(data.length - 4) != sum)
/*     */     {
/*     */       
/*  59 */       return null;
/*     */     }
/*     */     
/*  62 */     dst.limit(dataSize);
/*  63 */     dst.rewind();
/*  64 */     return dst;
/*     */   }
/*     */   
/*     */   public ByteArrayInputStream decrypt(ByteArrayInputStream input) throws Exception {
/*  68 */     int length = input.available();
/*     */     
/*  70 */     if (length % 8 != 0) {
/*  71 */       throw new Exception("消息字节流错误，非刚好8位字节流");
/*     */     }
/*     */     
/*  74 */     ByteBuffer dst = ByteBuffer.allocate(length);
/*  75 */     XPEncryptGroup group = new XPEncryptGroup(this.key);
/*     */     
/*  77 */     byte[] ibuff = new byte[4];
/*  78 */     while (input.available() > 0) {
/*  79 */       input.read(ibuff);
/*  80 */       int hv = toInt(ibuff);
/*     */       
/*  82 */       input.read(ibuff);
/*  83 */       int lv = toInt(ibuff);
/*  84 */       dst.putLong(group.decode(hv, lv));
/*     */     } 
/*     */     
/*  87 */     int dataSize = dst.getInt(length - 12);
/*     */     
/*  89 */     int sum = 0;
/*  90 */     dst.rewind();
/*  91 */     while (dst.remaining() > 8) {
/*  92 */       sum += dst.getInt();
/*     */     }
/*     */     
/*  95 */     if (dst.getInt(length - 8) != 1164408404 || dst.getInt(length - 4) != sum) {
/*  96 */       throw new Exception("消息字节流错误，check sum错误");
/*     */     }
/*     */     
/*  99 */     dst.limit(dataSize);
/* 100 */     dst.rewind();
/* 101 */     return new ByteArrayInputStream(dst.array(), 0, dataSize);
/*     */   }
/*     */   
/*     */   private ByteBuffer doEncrypt() {
/* 105 */     ByteBuffer ret = ByteBuffer.allocate(this.eSize);
/* 106 */     XPEncryptGroup group = new XPEncryptGroup(this.key);
/* 107 */     this.eBuffer.rewind();
/* 108 */     while (this.eBuffer.hasRemaining()) {
/* 109 */       ret.putLong(group.encode(this.eBuffer.getLong()));
/*     */     }
/* 111 */     ret.rewind();
/* 112 */     return ret;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void paddingAndMakeBuffer() {
/* 118 */     int dataSize = this.eData.length;
/* 119 */     int append = 0;
/*     */ 
/*     */     
/* 122 */     append = 11 - ((dataSize & 0x7) + 3 & 0x7);
/* 123 */     int zeros = append - 4;
/*     */     
/* 125 */     this.eSize = dataSize + append + 8;
/* 126 */     this.eBuffer = ByteBuffer.allocate(this.eSize);
/* 127 */     this.eBuffer.put(this.eData);
/*     */     
/* 129 */     for (int i = 0; i < zeros; i++) {
/* 130 */       this.eBuffer.put((byte)0);
/*     */     }
/*     */     
/* 133 */     this.eBuffer.putInt(dataSize);
/*     */ 
/*     */     
/* 136 */     this.eBuffer.rewind();
/* 137 */     int checksum = 0;
/* 138 */     while (this.eBuffer.remaining() > 8) {
/* 139 */       checksum += this.eBuffer.getInt();
/*     */     }
/* 141 */     this.eBuffer.putInt(this.eSize - 8, 1164408404);
/* 142 */     this.eBuffer.putInt(this.eSize - 4, checksum);
/*     */   }
/*     */ 
/*     */   
/*     */   private int toInt(byte[] src) {
/* 147 */     int value = src[3] & 0xFF | (
/* 148 */       src[2] & 0xFF) << 8 | (
/* 149 */       src[1] & 0xFF) << 16 | (
/* 150 */       src[0] & 0xFF) << 24;
/* 151 */     return value;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/utils/secure/XPEncrypt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */