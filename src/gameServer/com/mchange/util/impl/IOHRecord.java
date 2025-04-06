/*     */ package com.mchange.util.impl;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class IOHRecord
/*     */   extends IOHRecElem
/*     */ {
/*     */   IntObjectHash parent;
/* 197 */   int size = 0;
/*     */   
/*     */   IOHRecord(int paramInt) {
/* 200 */     super(paramInt, null, null);
/*     */   }
/*     */   
/*     */   IOHRecElem findInt(int paramInt) {
/* 204 */     for (IOHRecord iOHRecord = this; iOHRecord.next != null; iOHRecElem = iOHRecord.next) {
/* 205 */       IOHRecElem iOHRecElem; if (iOHRecord.next.num == paramInt) return iOHRecord; 
/* 206 */     }  return null;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean add(int paramInt, Object paramObject, boolean paramBoolean) {
/* 211 */     IOHRecElem iOHRecElem = findInt(paramInt);
/* 212 */     if (iOHRecElem != null) {
/*     */       
/* 214 */       if (paramBoolean)
/* 215 */         iOHRecElem.next = new IOHRecElem(paramInt, paramObject, iOHRecElem.next.next); 
/* 216 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 220 */     this.next = new IOHRecElem(paramInt, paramObject, this.next);
/* 221 */     this.size++;
/* 222 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   Object remove(int paramInt) {
/* 228 */     IOHRecElem iOHRecElem = findInt(paramInt);
/* 229 */     if (iOHRecElem == null) return null;
/*     */ 
/*     */     
/* 232 */     Object object = iOHRecElem.next.obj;
/* 233 */     iOHRecElem.next = iOHRecElem.next.next;
/* 234 */     this.size--;
/* 235 */     if (this.size == 0)
/* 236 */       this.parent.records[this.num] = null; 
/* 237 */     return object;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   Object get(int paramInt) {
/* 243 */     IOHRecElem iOHRecElem = findInt(paramInt);
/* 244 */     if (iOHRecElem != null)
/* 245 */       return iOHRecElem.next.obj; 
/* 246 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   IOHRecord split(int paramInt) {
/* 251 */     IOHRecord iOHRecord1 = null;
/* 252 */     IOHRecord iOHRecord2 = null;
/* 253 */     for (IOHRecord iOHRecord3 = this; iOHRecord3.next != null; iOHRecElem = iOHRecord3.next) {
/*     */       IOHRecElem iOHRecElem;
/* 255 */       if (Math.abs(iOHRecord3.next.num % paramInt) != this.num) {
/*     */         
/* 257 */         if (iOHRecord1 == null) {
/*     */           
/* 259 */           iOHRecord1 = new IOHRecord(this.num * 2);
/* 260 */           iOHRecord2 = iOHRecord1;
/*     */         } 
/* 262 */         iOHRecord2.next = iOHRecord3.next;
/* 263 */         iOHRecord3.next = iOHRecord3.next.next;
/* 264 */         IOHRecElem iOHRecElem1 = iOHRecord2.next;
/* 265 */         iOHRecElem1.next = null;
/*     */       } 
/*     */     } 
/* 268 */     return iOHRecord1;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/util/impl/IOHRecord.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */