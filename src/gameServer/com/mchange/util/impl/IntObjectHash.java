/*     */ package com.mchange.util.impl;
/*     */ 
/*     */ import com.mchange.util.IntEnumeration;
/*     */ import com.mchange.util.IntObjectMap;
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
/*     */ public class IntObjectHash
/*     */   implements IntObjectMap
/*     */ {
/*     */   IOHRecord[] records;
/*     */   int init_capacity;
/*     */   float load_factor;
/*     */   int threshold;
/*     */   int size;
/*     */   
/*     */   public IntObjectHash(int paramInt, float paramFloat) {
/*  52 */     this.init_capacity = paramInt;
/*  53 */     this.load_factor = paramFloat;
/*  54 */     clear();
/*     */   }
/*     */   
/*     */   public IntObjectHash() {
/*  58 */     this(101, 0.75F);
/*     */   }
/*     */   
/*     */   public synchronized Object get(int paramInt) {
/*  62 */     int i = getIndex(paramInt);
/*  63 */     Object object = null;
/*  64 */     if (this.records[i] != null)
/*  65 */       object = this.records[i].get(paramInt); 
/*  66 */     return object;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void put(int paramInt, Object paramObject) {
/*  71 */     if (paramObject == null)
/*  72 */       throw new NullPointerException("Null values not permitted."); 
/*  73 */     int i = getIndex(paramInt);
/*  74 */     if (this.records[i] == null)
/*  75 */       this.records[i] = new IOHRecord(i); 
/*  76 */     boolean bool = this.records[i].add(paramInt, paramObject, true);
/*  77 */     if (!bool) this.size++; 
/*  78 */     if (this.size > this.threshold) rehash();
/*     */   
/*     */   }
/*     */   
/*     */   public synchronized boolean putNoReplace(int paramInt, Object paramObject) {
/*  83 */     if (paramObject == null)
/*  84 */       throw new NullPointerException("Null values not permitted."); 
/*  85 */     int i = getIndex(paramInt);
/*  86 */     if (this.records[i] == null)
/*  87 */       this.records[i] = new IOHRecord(i); 
/*  88 */     boolean bool = this.records[i].add(paramInt, paramObject, false);
/*  89 */     if (bool) {
/*  90 */       return false;
/*     */     }
/*     */     
/*  93 */     this.size++;
/*  94 */     if (this.size > this.threshold) rehash(); 
/*  95 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSize() {
/* 100 */     return this.size;
/*     */   }
/*     */   
/*     */   public synchronized boolean containsInt(int paramInt) {
/* 104 */     int i = getIndex(paramInt);
/* 105 */     return (this.records[i] != null && this.records[i].findInt(paramInt) != null);
/*     */   }
/*     */   
/*     */   private int getIndex(int paramInt) {
/* 109 */     return Math.abs(paramInt % this.records.length);
/*     */   }
/*     */   
/*     */   public synchronized Object remove(int paramInt) {
/* 113 */     IOHRecord iOHRecord = this.records[getIndex(paramInt)];
/* 114 */     Object object = (iOHRecord == null) ? null : iOHRecord.remove(paramInt);
/* 115 */     if (object != null) this.size--; 
/* 116 */     return object;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void clear() {
/* 121 */     this.records = new IOHRecord[this.init_capacity];
/* 122 */     this.threshold = (int)(this.load_factor * this.init_capacity);
/* 123 */     this.size = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized IntEnumeration ints() {
/* 128 */     return new IntEnumerationHelperBase()
/*     */       {
/*     */         int index;
/*     */ 
/*     */ 
/*     */         
/*     */         IOHRecElem finger;
/*     */ 
/*     */ 
/*     */         
/*     */         public boolean hasMoreInts() {
/* 139 */           return (this.index < IntObjectHash.this.records.length);
/*     */         }
/*     */ 
/*     */         
/*     */         public int nextInt() {
/*     */           try {
/* 145 */             int i = this.finger.num;
/* 146 */             findNext();
/* 147 */             return i;
/*     */           }
/* 149 */           catch (NullPointerException nullPointerException) {
/* 150 */             throw new NoSuchElementException();
/*     */           } 
/*     */         }
/*     */         
/*     */         private void findNext() {
/* 155 */           if (this.finger.next != null) { this.finger = this.finger.next; }
/* 156 */           else { nextIndex(); }
/*     */         
/*     */         }
/*     */ 
/*     */         
/*     */         private void nextIndex() {
/*     */           try {
/* 163 */             int i = IntObjectHash.this.records.length; do {
/* 164 */               this.index++;
/* 165 */             } while (IntObjectHash.this.records[this.index] == null && this.index <= i);
/* 166 */             this.finger = (IntObjectHash.this.records[this.index]).next;
/*     */           }
/* 168 */           catch (ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException) {
/*     */ 
/*     */             
/* 171 */             this.finger = null;
/*     */           } 
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void rehash() {
/* 180 */     IOHRecord[] arrayOfIOHRecord = new IOHRecord[this.records.length * 2];
/* 181 */     for (byte b = 0; b < this.records.length; b++) {
/*     */       
/* 183 */       if (this.records[b] != null) {
/*     */         
/* 185 */         arrayOfIOHRecord[b] = this.records[b];
/* 186 */         arrayOfIOHRecord[b * 2] = this.records[b].split(arrayOfIOHRecord.length);
/*     */       } 
/*     */     } 
/* 189 */     this.records = arrayOfIOHRecord;
/* 190 */     this.threshold = (int)(this.load_factor * this.records.length);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/util/impl/IntObjectHash.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */