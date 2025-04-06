/*     */ package com.mchange.util.impl;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CircularList
/*     */   implements Cloneable
/*     */ {
/*  68 */   CircularListRecord firstRecord = null;
/*  69 */   int size = 0;
/*     */ 
/*     */ 
/*     */   
/*     */   private void addElement(Object paramObject, boolean paramBoolean) {
/*  74 */     if (this.firstRecord == null) {
/*  75 */       this.firstRecord = new CircularListRecord(paramObject);
/*     */     } else {
/*     */       
/*  78 */       CircularListRecord circularListRecord = new CircularListRecord(paramObject, this.firstRecord.prev, this.firstRecord);
/*  79 */       this.firstRecord.prev.next = circularListRecord;
/*  80 */       this.firstRecord.prev = circularListRecord;
/*  81 */       if (paramBoolean) this.firstRecord = circularListRecord; 
/*     */     } 
/*  83 */     this.size++;
/*     */   }
/*     */ 
/*     */   
/*     */   private void removeElement(boolean paramBoolean) {
/*  88 */     if (this.size == 1) {
/*  89 */       this.firstRecord = null;
/*     */     } else {
/*     */       
/*  92 */       if (paramBoolean) this.firstRecord = this.firstRecord.next; 
/*  93 */       zap(this.firstRecord.prev);
/*     */     } 
/*  95 */     this.size--;
/*     */   }
/*     */ 
/*     */   
/*     */   private void zap(CircularListRecord paramCircularListRecord) {
/* 100 */     paramCircularListRecord.next.prev = paramCircularListRecord.prev;
/* 101 */     paramCircularListRecord.prev.next = paramCircularListRecord.next;
/*     */   }
/*     */   
/*     */   public void appendElement(Object paramObject) {
/* 105 */     addElement(paramObject, false);
/*     */   }
/*     */   public void addElementToFront(Object paramObject) {
/* 108 */     addElement(paramObject, true);
/*     */   }
/*     */   public void removeFirstElement() {
/* 111 */     removeElement(true);
/*     */   }
/*     */   public void removeLastElement() {
/* 114 */     removeElement(false);
/*     */   }
/*     */   
/*     */   public void removeFromFront(int paramInt) {
/* 118 */     if (paramInt > this.size)
/* 119 */       throw new IndexOutOfBoundsException(paramInt + ">" + this.size); 
/* 120 */     for (byte b = 0; b < paramInt; ) { removeElement(true); b++; }
/*     */   
/*     */   }
/*     */   
/*     */   public void removeFromBack(int paramInt) {
/* 125 */     if (paramInt > this.size)
/* 126 */       throw new IndexOutOfBoundsException(paramInt + ">" + this.size); 
/* 127 */     for (byte b = 0; b < paramInt; ) { removeElement(false); b++; }
/*     */   
/*     */   }
/*     */   
/*     */   public void removeAllElements() {
/* 132 */     this.size = 0;
/* 133 */     this.firstRecord = null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getElementFromFront(int paramInt) {
/* 138 */     if (paramInt >= this.size) {
/* 139 */       throw new IndexOutOfBoundsException(paramInt + ">=" + this.size);
/*     */     }
/*     */     
/* 142 */     CircularListRecord circularListRecord = this.firstRecord;
/* 143 */     for (byte b = 0; b < paramInt; b++)
/* 144 */       circularListRecord = circularListRecord.next; 
/* 145 */     return circularListRecord.object;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getElementFromBack(int paramInt) {
/* 151 */     if (paramInt >= this.size) {
/* 152 */       throw new IndexOutOfBoundsException(paramInt + ">=" + this.size);
/*     */     }
/*     */     
/* 155 */     CircularListRecord circularListRecord = this.firstRecord.prev;
/* 156 */     for (byte b = 0; b < paramInt; b++)
/* 157 */       circularListRecord = circularListRecord.prev; 
/* 158 */     return circularListRecord.object;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getFirstElement() {
/*     */     try {
/* 164 */       return this.firstRecord.object;
/* 165 */     } catch (NullPointerException nullPointerException) {
/* 166 */       throw new IndexOutOfBoundsException("CircularList is empty.");
/*     */     } 
/*     */   }
/*     */   public Object getLastElement() {
/*     */     try {
/* 171 */       return this.firstRecord.prev.object;
/* 172 */     } catch (NullPointerException nullPointerException) {
/* 173 */       throw new IndexOutOfBoundsException("CircularList is empty.");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Enumeration elements(boolean paramBoolean1, boolean paramBoolean2) {
/* 179 */     return new CircularListEnumeration(this, paramBoolean1, paramBoolean2);
/*     */   }
/*     */   public Enumeration elements(boolean paramBoolean) {
/* 182 */     return elements(paramBoolean, true);
/*     */   }
/*     */   public Enumeration elements() {
/* 185 */     return elements(true, true);
/*     */   }
/*     */   public int size() {
/* 188 */     return this.size;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object clone() {
/* 197 */     CircularList circularList = new CircularList();
/* 198 */     int i = size();
/* 199 */     for (byte b = 0; b < i; b++)
/* 200 */       circularList.appendElement(getElementFromFront(b)); 
/* 201 */     return circularList;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void main(String[] paramArrayOfString) {
/* 206 */     CircularList circularList = new CircularList();
/* 207 */     circularList.appendElement("Hello");
/* 208 */     circularList.appendElement("There");
/* 209 */     circularList.appendElement("Joe.");
/* 210 */     for (Enumeration<String> enumeration = circularList.elements(); enumeration.hasMoreElements();)
/* 211 */       System.out.println("x " + enumeration.nextElement()); 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/util/impl/CircularList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */