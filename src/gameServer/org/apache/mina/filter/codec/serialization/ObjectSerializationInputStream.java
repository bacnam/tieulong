/*     */ package org.apache.mina.filter.codec.serialization;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.StreamCorruptedException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ObjectSerializationInputStream
/*     */   extends InputStream
/*     */   implements ObjectInput
/*     */ {
/*     */   private final DataInputStream in;
/*     */   private final ClassLoader classLoader;
/*  44 */   private int maxObjectSize = 1048576;
/*     */   
/*     */   public ObjectSerializationInputStream(InputStream in) {
/*  47 */     this(in, null);
/*     */   }
/*     */   
/*     */   public ObjectSerializationInputStream(InputStream in, ClassLoader classLoader) {
/*  51 */     if (in == null) {
/*  52 */       throw new IllegalArgumentException("in");
/*     */     }
/*  54 */     if (classLoader == null) {
/*  55 */       classLoader = Thread.currentThread().getContextClassLoader();
/*     */     }
/*     */     
/*  58 */     if (in instanceof DataInputStream) {
/*  59 */       this.in = (DataInputStream)in;
/*     */     } else {
/*  61 */       this.in = new DataInputStream(in);
/*     */     } 
/*     */     
/*  64 */     this.classLoader = classLoader;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxObjectSize() {
/*  74 */     return this.maxObjectSize;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxObjectSize(int maxObjectSize) {
/*  84 */     if (maxObjectSize <= 0) {
/*  85 */       throw new IllegalArgumentException("maxObjectSize: " + maxObjectSize);
/*     */     }
/*     */     
/*  88 */     this.maxObjectSize = maxObjectSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public int read() throws IOException {
/*  93 */     return this.in.read();
/*     */   }
/*     */   
/*     */   public Object readObject() throws ClassNotFoundException, IOException {
/*  97 */     int objectSize = this.in.readInt();
/*  98 */     if (objectSize <= 0) {
/*  99 */       throw new StreamCorruptedException("Invalid objectSize: " + objectSize);
/*     */     }
/* 101 */     if (objectSize > this.maxObjectSize) {
/* 102 */       throw new StreamCorruptedException("ObjectSize too big: " + objectSize + " (expected: <= " + this.maxObjectSize + ')');
/*     */     }
/*     */ 
/*     */     
/* 106 */     IoBuffer buf = IoBuffer.allocate(objectSize + 4, false);
/* 107 */     buf.putInt(objectSize);
/* 108 */     this.in.readFully(buf.array(), 4, objectSize);
/* 109 */     buf.position(0);
/* 110 */     buf.limit(objectSize + 4);
/*     */     
/* 112 */     return buf.getObject(this.classLoader);
/*     */   }
/*     */   
/*     */   public boolean readBoolean() throws IOException {
/* 116 */     return this.in.readBoolean();
/*     */   }
/*     */   
/*     */   public byte readByte() throws IOException {
/* 120 */     return this.in.readByte();
/*     */   }
/*     */   
/*     */   public char readChar() throws IOException {
/* 124 */     return this.in.readChar();
/*     */   }
/*     */   
/*     */   public double readDouble() throws IOException {
/* 128 */     return this.in.readDouble();
/*     */   }
/*     */   
/*     */   public float readFloat() throws IOException {
/* 132 */     return this.in.readFloat();
/*     */   }
/*     */   
/*     */   public void readFully(byte[] b) throws IOException {
/* 136 */     this.in.readFully(b);
/*     */   }
/*     */   
/*     */   public void readFully(byte[] b, int off, int len) throws IOException {
/* 140 */     this.in.readFully(b, off, len);
/*     */   }
/*     */   
/*     */   public int readInt() throws IOException {
/* 144 */     return this.in.readInt();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated
/*     */   public String readLine() throws IOException {
/* 153 */     return this.in.readLine();
/*     */   }
/*     */   
/*     */   public long readLong() throws IOException {
/* 157 */     return this.in.readLong();
/*     */   }
/*     */   
/*     */   public short readShort() throws IOException {
/* 161 */     return this.in.readShort();
/*     */   }
/*     */   
/*     */   public String readUTF() throws IOException {
/* 165 */     return this.in.readUTF();
/*     */   }
/*     */   
/*     */   public int readUnsignedByte() throws IOException {
/* 169 */     return this.in.readUnsignedByte();
/*     */   }
/*     */   
/*     */   public int readUnsignedShort() throws IOException {
/* 173 */     return this.in.readUnsignedShort();
/*     */   }
/*     */   
/*     */   public int skipBytes(int n) throws IOException {
/* 177 */     return this.in.skipBytes(n);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/codec/serialization/ObjectSerializationInputStream.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */