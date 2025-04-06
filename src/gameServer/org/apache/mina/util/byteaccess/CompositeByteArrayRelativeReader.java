/*     */ package org.apache.mina.util.byteaccess;
/*     */ 
/*     */ import java.nio.ByteOrder;
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
/*     */ public class CompositeByteArrayRelativeReader
/*     */   extends CompositeByteArrayRelativeBase
/*     */   implements IoRelativeReader
/*     */ {
/*     */   private final boolean autoFree;
/*     */   
/*     */   public CompositeByteArrayRelativeReader(CompositeByteArray cba, boolean autoFree) {
/*  52 */     super(cba);
/*  53 */     this.autoFree = autoFree;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void cursorPassedFirstComponent() {
/*  58 */     if (this.autoFree) {
/*  59 */       this.cba.removeFirst().free();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void skip(int length) {
/*  67 */     this.cursor.skip(length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteArray slice(int length) {
/*  74 */     return this.cursor.slice(length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte get() {
/*  82 */     return this.cursor.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void get(IoBuffer bb) {
/*  90 */     this.cursor.get(bb);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public short getShort() {
/*  97 */     return this.cursor.getShort();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getInt() {
/* 104 */     return this.cursor.getInt();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getLong() {
/* 111 */     return this.cursor.getLong();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getFloat() {
/* 118 */     return this.cursor.getFloat();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public double getDouble() {
/* 125 */     return this.cursor.getDouble();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char getChar() {
/* 132 */     return this.cursor.getChar();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/util/byteaccess/CompositeByteArrayRelativeReader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */