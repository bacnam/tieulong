/*     */ package com.mchange.v1.util;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
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
/*     */ public abstract class WrapperIterator
/*     */   implements Iterator
/*     */ {
/*  47 */   protected static final Object SKIP_TOKEN = new Object();
/*     */   
/*     */   static final boolean DEBUG = true;
/*     */   
/*     */   Iterator inner;
/*     */   boolean supports_remove;
/*  53 */   Object lastOut = null;
/*  54 */   Object nextOut = SKIP_TOKEN;
/*     */ 
/*     */   
/*     */   public WrapperIterator(Iterator paramIterator, boolean paramBoolean) {
/*  58 */     this.inner = paramIterator;
/*  59 */     this.supports_remove = paramBoolean;
/*     */   }
/*     */   
/*     */   public WrapperIterator(Iterator paramIterator) {
/*  63 */     this(paramIterator, false);
/*     */   }
/*     */   
/*     */   public boolean hasNext() {
/*  67 */     findNext();
/*  68 */     return (this.nextOut != SKIP_TOKEN);
/*     */   }
/*     */ 
/*     */   
/*     */   private void findNext() {
/*  73 */     if (this.nextOut == SKIP_TOKEN)
/*     */     {
/*  75 */       while (this.inner.hasNext() && this.nextOut == SKIP_TOKEN) {
/*  76 */         this.nextOut = transformObject(this.inner.next());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public Object next() {
/*  82 */     findNext();
/*  83 */     if (this.nextOut != SKIP_TOKEN) {
/*     */       
/*  85 */       this.lastOut = this.nextOut;
/*  86 */       this.nextOut = SKIP_TOKEN;
/*     */     } else {
/*     */       
/*  89 */       throw new NoSuchElementException();
/*     */     } 
/*     */ 
/*     */     
/*  93 */     DebugUtils.myAssert((this.nextOut == SKIP_TOKEN && this.lastOut != SKIP_TOKEN));
/*  94 */     return this.lastOut;
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove() {
/*  99 */     if (this.supports_remove) {
/*     */       
/* 101 */       if (this.nextOut != SKIP_TOKEN) {
/* 102 */         throw new UnsupportedOperationException(getClass().getName() + " cannot support remove after" + " hasNext() has been called!");
/*     */       }
/*     */       
/* 105 */       if (this.lastOut != SKIP_TOKEN) {
/* 106 */         this.inner.remove();
/*     */       } else {
/* 108 */         throw new NoSuchElementException();
/*     */       } 
/*     */     } else {
/* 111 */       throw new UnsupportedOperationException();
/*     */     } 
/*     */   }
/*     */   
/*     */   protected abstract Object transformObject(Object paramObject);
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/util/WrapperIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */