/*     */ package org.apache.mina.filter.codec.serialization;
/*     */ 
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.OutputStream;
/*     */ import org.apache.mina.core.buffer.IoBuffer;
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
/*     */ public class ObjectSerializationOutputStream
/*     */   extends OutputStream
/*     */   implements ObjectOutput
/*     */ {
/*     */   private final DataOutputStream out;
/*  39 */   private int maxObjectSize = Integer.MAX_VALUE;
/*     */   
/*     */   public ObjectSerializationOutputStream(OutputStream out) {
/*  42 */     if (out == null) {
/*  43 */       throw new IllegalArgumentException("out");
/*     */     }
/*     */     
/*  46 */     if (out instanceof DataOutputStream) {
/*  47 */       this.out = (DataOutputStream)out;
/*     */     } else {
/*  49 */       this.out = new DataOutputStream(out);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxObjectSize() {
/*  60 */     return this.maxObjectSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxObjectSize(int maxObjectSize) {
/*  70 */     if (maxObjectSize <= 0) {
/*  71 */       throw new IllegalArgumentException("maxObjectSize: " + maxObjectSize);
/*     */     }
/*     */     
/*  74 */     this.maxObjectSize = maxObjectSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/*  79 */     this.out.close();
/*     */   }
/*     */ 
/*     */   
/*     */   public void flush() throws IOException {
/*  84 */     this.out.flush();
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(int b) throws IOException {
/*  89 */     this.out.write(b);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(byte[] b) throws IOException {
/*  94 */     this.out.write(b);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(byte[] b, int off, int len) throws IOException {
/*  99 */     this.out.write(b, off, len);
/*     */   }
/*     */   
/*     */   public void writeObject(Object obj) throws IOException {
/* 103 */     IoBuffer buf = IoBuffer.allocate(64, false);
/* 104 */     buf.setAutoExpand(true);
/* 105 */     buf.putObject(obj);
/*     */     
/* 107 */     int objectSize = buf.position() - 4;
/* 108 */     if (objectSize > this.maxObjectSize) {
/* 109 */       throw new IllegalArgumentException("The encoded object is too big: " + objectSize + " (> " + this.maxObjectSize + ')');
/*     */     }
/*     */ 
/*     */     
/* 113 */     this.out.write(buf.array(), 0, buf.position());
/*     */   }
/*     */   
/*     */   public void writeBoolean(boolean v) throws IOException {
/* 117 */     this.out.writeBoolean(v);
/*     */   }
/*     */   
/*     */   public void writeByte(int v) throws IOException {
/* 121 */     this.out.writeByte(v);
/*     */   }
/*     */   
/*     */   public void writeBytes(String s) throws IOException {
/* 125 */     this.out.writeBytes(s);
/*     */   }
/*     */   
/*     */   public void writeChar(int v) throws IOException {
/* 129 */     this.out.writeChar(v);
/*     */   }
/*     */   
/*     */   public void writeChars(String s) throws IOException {
/* 133 */     this.out.writeChars(s);
/*     */   }
/*     */   
/*     */   public void writeDouble(double v) throws IOException {
/* 137 */     this.out.writeDouble(v);
/*     */   }
/*     */   
/*     */   public void writeFloat(float v) throws IOException {
/* 141 */     this.out.writeFloat(v);
/*     */   }
/*     */   
/*     */   public void writeInt(int v) throws IOException {
/* 145 */     this.out.writeInt(v);
/*     */   }
/*     */   
/*     */   public void writeLong(long v) throws IOException {
/* 149 */     this.out.writeLong(v);
/*     */   }
/*     */   
/*     */   public void writeShort(int v) throws IOException {
/* 153 */     this.out.writeShort(v);
/*     */   }
/*     */   
/*     */   public void writeUTF(String str) throws IOException {
/* 157 */     this.out.writeUTF(str);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/codec/serialization/ObjectSerializationOutputStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */