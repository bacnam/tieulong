/*     */ package org.apache.mina.util.byteaccess;
/*     */ 
/*     */ import java.nio.ByteOrder;
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
/*     */ 
/*     */ abstract class CompositeByteArrayRelativeBase
/*     */ {
/*     */   protected final CompositeByteArray cba;
/*     */   protected final ByteArray.Cursor cursor;
/*     */   
/*     */   public CompositeByteArrayRelativeBase(CompositeByteArray cba) {
/*  56 */     this.cba = cba;
/*  57 */     this.cursor = cba.cursor(cba.first(), new CompositeByteArray.CursorListener()
/*     */         {
/*     */           public void enteredFirstComponent(int componentIndex, ByteArray component) {}
/*     */ 
/*     */           
/*     */           public void enteredLastComponent(int componentIndex, ByteArray component) {
/*     */             assert false;
/*     */           }
/*     */ 
/*     */           
/*     */           public void enteredNextComponent(int componentIndex, ByteArray component) {
/*  68 */             CompositeByteArrayRelativeBase.this.cursorPassedFirstComponent();
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           public void enteredPreviousComponent(int componentIndex, ByteArray component) {
/*     */             assert false;
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getRemaining() {
/*  82 */     return this.cursor.getRemaining();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean hasRemaining() {
/*  89 */     return this.cursor.hasRemaining();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ByteOrder order() {
/*  96 */     return this.cba.order();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void append(ByteArray ba) {
/* 103 */     this.cba.addLast(ba);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void free() {
/* 110 */     this.cba.free();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int getIndex() {
/* 117 */     return this.cursor.getIndex();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final int last() {
/* 124 */     return this.cba.last();
/*     */   }
/*     */   
/*     */   protected abstract void cursorPassedFirstComponent();
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/util/byteaccess/CompositeByteArrayRelativeBase.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */