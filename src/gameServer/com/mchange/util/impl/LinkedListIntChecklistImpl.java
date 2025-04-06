/*     */ package com.mchange.util.impl;
/*     */ 
/*     */ import com.mchange.util.IntChecklist;
/*     */ import com.mchange.util.IntEnumeration;
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
/*     */ public class LinkedListIntChecklistImpl
/*     */   implements IntChecklist
/*     */ {
/*  45 */   private final LLICIRecord headRecord = new LLICIRecord();
/*  46 */   private int num_checked = 0;
/*     */ 
/*     */   
/*     */   public void check(int paramInt) {
/*  50 */     LLICIRecord lLICIRecord = findPrevious(paramInt);
/*  51 */     if (lLICIRecord.next == null || lLICIRecord.next.contained != paramInt) {
/*     */       
/*  53 */       LLICIRecord lLICIRecord1 = new LLICIRecord();
/*  54 */       lLICIRecord1.next = lLICIRecord.next;
/*  55 */       lLICIRecord1.contained = paramInt;
/*  56 */       lLICIRecord.next = lLICIRecord1;
/*  57 */       this.num_checked++;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void uncheck(int paramInt) {
/*  63 */     LLICIRecord lLICIRecord = findPrevious(paramInt);
/*  64 */     if (lLICIRecord.next != null && lLICIRecord.next.contained == paramInt) {
/*     */       
/*  66 */       lLICIRecord.next = lLICIRecord.next.next;
/*  67 */       this.num_checked--;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isChecked(int paramInt) {
/*  73 */     LLICIRecord lLICIRecord = findPrevious(paramInt);
/*  74 */     return (lLICIRecord.next != null && lLICIRecord.next.contained == paramInt);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/*  79 */     this.headRecord.next = null;
/*  80 */     this.num_checked = 0;
/*     */   }
/*     */   
/*     */   public int countChecked() {
/*  84 */     return this.num_checked;
/*     */   }
/*     */   
/*     */   public int[] getChecked() {
/*  88 */     LLICIRecord lLICIRecord = this.headRecord;
/*  89 */     int[] arrayOfInt = new int[this.num_checked];
/*  90 */     byte b = 0;
/*  91 */     while (lLICIRecord.next != null) {
/*     */       
/*  93 */       arrayOfInt[b++] = lLICIRecord.next.contained;
/*  94 */       lLICIRecord = lLICIRecord.next;
/*     */     } 
/*  96 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   
/*     */   public IntEnumeration checked() {
/* 101 */     return new IntEnumerationHelperBase()
/*     */       {
/* 103 */         LLICIRecord finger = LinkedListIntChecklistImpl.this.headRecord;
/*     */ 
/*     */ 
/*     */         
/*     */         public int nextInt() {
/*     */           try {
/* 109 */             this.finger = this.finger.next;
/* 110 */             return this.finger.contained;
/*     */           }
/* 112 */           catch (NullPointerException nullPointerException) {
/* 113 */             throw new NoSuchElementException();
/*     */           } 
/*     */         }
/*     */         public boolean hasMoreInts() {
/* 117 */           return (this.finger.next != null);
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private LLICIRecord findPrevious(int paramInt) {
/* 123 */     LLICIRecord lLICIRecord = this.headRecord;
/* 124 */     while (lLICIRecord.next != null && lLICIRecord.next.contained < paramInt)
/* 125 */       lLICIRecord = lLICIRecord.next; 
/* 126 */     return lLICIRecord;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/util/impl/LinkedListIntChecklistImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */