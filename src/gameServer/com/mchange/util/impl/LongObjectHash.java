/*     */ package com.mchange.util.impl;
/*     */ 
/*     */ import com.mchange.util.LongObjectMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LongObjectHash
/*     */   implements LongObjectMap
/*     */ {
/*     */   LOHRecord[] records;
/*     */   float load_factor;
/*     */   long threshold;
/*     */   long size;
/*     */   
/*     */   public LongObjectHash(int paramInt, float paramFloat) {
/*  49 */     this.records = new LOHRecord[paramInt];
/*  50 */     this.load_factor = paramFloat;
/*  51 */     this.threshold = (long)(paramFloat * paramInt);
/*     */   }
/*     */   
/*     */   public LongObjectHash() {
/*  55 */     this(101, 0.75F);
/*     */   }
/*     */   
/*     */   public synchronized Object get(long paramLong) {
/*  59 */     int i = (int)(paramLong % this.records.length);
/*  60 */     Object object = null;
/*  61 */     if (this.records[i] != null)
/*  62 */       object = this.records[i].get(paramLong); 
/*  63 */     return object;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void put(long paramLong, Object paramObject) {
/*  68 */     int i = (int)(paramLong % this.records.length);
/*  69 */     if (this.records[i] == null)
/*  70 */       this.records[i] = new LOHRecord(i); 
/*  71 */     boolean bool = this.records[i].add(paramLong, paramObject, true);
/*  72 */     if (!bool) this.size++; 
/*  73 */     if (this.size > this.threshold) rehash();
/*     */   
/*     */   }
/*     */   
/*     */   public synchronized boolean putNoReplace(long paramLong, Object paramObject) {
/*  78 */     int i = (int)(paramLong % this.records.length);
/*  79 */     if (this.records[i] == null)
/*  80 */       this.records[i] = new LOHRecord(i); 
/*  81 */     boolean bool = this.records[i].add(paramLong, paramObject, false);
/*  82 */     if (bool) {
/*  83 */       return false;
/*     */     }
/*     */     
/*  86 */     this.size++;
/*  87 */     if (this.size > this.threshold) rehash(); 
/*  88 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getSize() {
/*  93 */     return this.size;
/*     */   }
/*     */   
/*     */   public synchronized boolean containsLong(long paramLong) {
/*  97 */     int i = (int)(paramLong % this.records.length);
/*  98 */     return (this.records[i] != null && this.records[i].findLong(paramLong) != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized Object remove(long paramLong) {
/* 103 */     LOHRecord lOHRecord = this.records[(int)(paramLong % this.records.length)];
/* 104 */     Object object = (lOHRecord == null) ? null : lOHRecord.remove(paramLong);
/* 105 */     if (object != null) this.size--; 
/* 106 */     return object;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void rehash() {
/* 112 */     if (this.records.length * 2L > 2147483647L) {
/* 113 */       throw new Error("Implementation of LongObjectHash allows a capacity of only 2147483647");
/*     */     }
/* 115 */     LOHRecord[] arrayOfLOHRecord = new LOHRecord[this.records.length * 2];
/* 116 */     for (byte b = 0; b < this.records.length; b++) {
/*     */       
/* 118 */       if (this.records[b] != null) {
/*     */         
/* 120 */         arrayOfLOHRecord[b] = this.records[b];
/* 121 */         arrayOfLOHRecord[b * 2] = this.records[b].split(arrayOfLOHRecord.length);
/*     */       } 
/*     */     } 
/* 124 */     this.records = arrayOfLOHRecord;
/* 125 */     this.threshold = (long)(this.load_factor * this.records.length);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/util/impl/LongObjectHash.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */