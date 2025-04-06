/*     */ package com.mchange.v2.collection;
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
/*     */ public abstract class WrapperIterator
/*     */   implements Iterator
/*     */ {
/*  46 */   protected static final Object SKIP_TOKEN = new Object();
/*     */   
/*     */   static final boolean DEBUG = true;
/*     */   
/*     */   Iterator inner;
/*     */   boolean supports_remove;
/*  52 */   Object lastOut = null;
/*  53 */   Object nextOut = SKIP_TOKEN;
/*     */ 
/*     */   
/*     */   public WrapperIterator(Iterator paramIterator, boolean paramBoolean) {
/*  57 */     this.inner = paramIterator;
/*  58 */     this.supports_remove = paramBoolean;
/*     */   }
/*     */   
/*     */   public WrapperIterator(Iterator paramIterator) {
/*  62 */     this(paramIterator, false);
/*     */   }
/*     */   
/*     */   public boolean hasNext() {
/*  66 */     findNext();
/*  67 */     return (this.nextOut != SKIP_TOKEN);
/*     */   }
/*     */ 
/*     */   
/*     */   private void findNext() {
/*  72 */     if (this.nextOut == SKIP_TOKEN)
/*     */     {
/*  74 */       while (this.inner.hasNext() && this.nextOut == SKIP_TOKEN) {
/*  75 */         this.nextOut = transformObject(this.inner.next());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public Object next() {
/*  81 */     findNext();
/*  82 */     if (this.nextOut != SKIP_TOKEN) {
/*     */       
/*  84 */       this.lastOut = this.nextOut;
/*  85 */       this.nextOut = SKIP_TOKEN;
/*     */     } else {
/*     */       
/*  88 */       throw new NoSuchElementException();
/*     */     } 
/*     */     
/*  91 */     if (this.nextOut != SKIP_TOKEN || this.lastOut == SKIP_TOKEN) {
/*  92 */       throw new AssertionError("Better check out this weird WrapperIterator logic!");
/*     */     }
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


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/collection/WrapperIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */