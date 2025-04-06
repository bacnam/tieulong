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
/*     */ class LOHRecord
/*     */   extends LOHRecElem
/*     */ {
/*     */   LongObjectHash parent;
/* 132 */   int size = 0;
/*     */   
/*     */   LOHRecord(long paramLong) {
/* 135 */     super(paramLong, null, null);
/*     */   }
/*     */   
/*     */   LOHRecElem findLong(long paramLong) {
/* 139 */     for (LOHRecord lOHRecord = this; lOHRecord.next != null; lOHRecElem = lOHRecord.next) {
/* 140 */       LOHRecElem lOHRecElem; if (lOHRecord.next.num == paramLong) return lOHRecord; 
/* 141 */     }  return null;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean add(long paramLong, Object paramObject, boolean paramBoolean) {
/* 146 */     LOHRecElem lOHRecElem = findLong(paramLong);
/* 147 */     if (lOHRecElem != null) {
/*     */       
/* 149 */       if (paramBoolean)
/* 150 */         lOHRecElem.next = new LOHRecElem(paramLong, paramObject, lOHRecElem.next.next); 
/* 151 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 155 */     this.next = new LOHRecElem(paramLong, paramObject, this.next);
/* 156 */     this.size++;
/* 157 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   Object remove(long paramLong) {
/* 163 */     LOHRecElem lOHRecElem = findLong(paramLong);
/* 164 */     if (lOHRecElem == null) return null;
/*     */ 
/*     */     
/* 167 */     Object object = lOHRecElem.next.obj;
/* 168 */     lOHRecElem.next = lOHRecElem.next.next;
/* 169 */     this.size--;
/* 170 */     if (this.size == 0)
/* 171 */       this.parent.records[(int)this.num] = null; 
/* 172 */     return object;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   Object get(long paramLong) {
/* 178 */     LOHRecElem lOHRecElem = findLong(paramLong);
/* 179 */     if (lOHRecElem != null)
/* 180 */       return lOHRecElem.next.obj; 
/* 181 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   LOHRecord split(int paramInt) {
/* 186 */     LOHRecord lOHRecord1 = null;
/* 187 */     LOHRecord lOHRecord2 = null;
/* 188 */     for (LOHRecord lOHRecord3 = this; lOHRecord3.next != null; lOHRecElem = lOHRecord3.next) {
/*     */       LOHRecElem lOHRecElem;
/* 190 */       if (lOHRecord3.next.num % paramInt != this.num) {
/*     */         
/* 192 */         if (lOHRecord1 == null) {
/*     */           
/* 194 */           lOHRecord1 = new LOHRecord(this.num * 2L);
/* 195 */           lOHRecord2 = lOHRecord1;
/*     */         } 
/* 197 */         lOHRecord2.next = lOHRecord3.next;
/* 198 */         lOHRecord3.next = lOHRecord3.next.next;
/* 199 */         LOHRecElem lOHRecElem1 = lOHRecord2.next;
/* 200 */         lOHRecElem1.next = null;
/*     */       } 
/*     */     } 
/* 203 */     return lOHRecord1;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/util/impl/LOHRecord.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */