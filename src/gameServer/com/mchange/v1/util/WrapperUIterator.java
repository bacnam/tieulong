/*     */ package com.mchange.v1.util;
/*     */ 
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
/*     */ 
/*     */ public abstract class WrapperUIterator
/*     */   implements UIterator
/*     */ {
/*  47 */   protected static final Object SKIP_TOKEN = new Object();
/*     */   
/*     */   static final boolean DEBUG = true;
/*     */   
/*     */   UIterator inner;
/*     */   boolean supports_remove;
/*  53 */   Object lastOut = null;
/*  54 */   Object nextOut = SKIP_TOKEN;
/*     */ 
/*     */   
/*     */   public WrapperUIterator(UIterator paramUIterator, boolean paramBoolean) {
/*  58 */     this.inner = paramUIterator;
/*  59 */     this.supports_remove = paramBoolean;
/*     */   }
/*     */   
/*     */   public WrapperUIterator(UIterator paramUIterator) {
/*  63 */     this(paramUIterator, false);
/*     */   }
/*     */   
/*     */   public boolean hasNext() throws Exception {
/*  67 */     findNext();
/*  68 */     return (this.nextOut != SKIP_TOKEN);
/*     */   }
/*     */ 
/*     */   
/*     */   private void findNext() throws Exception {
/*  73 */     if (this.nextOut == SKIP_TOKEN)
/*     */     {
/*  75 */       while (this.inner.hasNext() && this.nextOut == SKIP_TOKEN) {
/*  76 */         this.nextOut = transformObject(this.inner.next());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public Object next() throws NoSuchElementException, Exception {
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
/*     */     
/*  95 */     assert this.nextOut == SKIP_TOKEN && this.lastOut != SKIP_TOKEN;
/*     */     
/*  97 */     return this.lastOut;
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove() throws Exception {
/* 102 */     if (this.supports_remove) {
/*     */       
/* 104 */       if (this.nextOut != SKIP_TOKEN) {
/* 105 */         throw new UnsupportedOperationException(getClass().getName() + " cannot support remove after" + " hasNext() has been called!");
/*     */       }
/*     */       
/* 108 */       if (this.lastOut != SKIP_TOKEN) {
/* 109 */         this.inner.remove();
/*     */       } else {
/* 111 */         throw new NoSuchElementException();
/*     */       } 
/*     */     } else {
/* 114 */       throw new UnsupportedOperationException();
/*     */     } 
/*     */   }
/*     */   public void close() throws Exception {
/* 118 */     this.inner.close();
/*     */   }
/*     */   
/*     */   protected abstract Object transformObject(Object paramObject) throws Exception;
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/util/WrapperUIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */