/*     */ package com.mchange.v1.util;
/*     */ 
/*     */ import java.util.AbstractList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Sublist
/*     */   extends AbstractList
/*     */ {
/*     */   List parent;
/*     */   int start_index;
/*     */   int end_index;
/*     */   
/*     */   public Sublist() {
/*  48 */     this(Collections.EMPTY_LIST, 0, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Sublist(List paramList, int paramInt1, int paramInt2) {
/*  55 */     setParent(paramList, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setParent(List paramList, int paramInt1, int paramInt2) {
/*  63 */     if (paramInt1 > paramInt2 || paramInt2 > paramList.size()) {
/*  64 */       throw new IndexOutOfBoundsException("start_index: " + paramInt1 + " end_index: " + paramInt2 + " parent.size(): " + paramList.size());
/*     */     }
/*     */     
/*  67 */     this.parent = paramList;
/*  68 */     this.start_index = paramInt2;
/*  69 */     this.end_index = paramInt2;
/*     */   }
/*     */   
/*     */   public Object get(int paramInt) {
/*  73 */     return this.parent.get(this.start_index + paramInt);
/*     */   }
/*     */   public int size() {
/*  76 */     return this.end_index - this.start_index;
/*     */   }
/*     */   
/*     */   public Object set(int paramInt, Object paramObject) {
/*  80 */     if (paramInt < size()) {
/*  81 */       return this.parent.set(this.start_index + paramInt, paramObject);
/*     */     }
/*  83 */     throw new IndexOutOfBoundsException(paramInt + " >= " + size());
/*     */   }
/*     */ 
/*     */   
/*     */   public void add(int paramInt, Object paramObject) {
/*  88 */     if (paramInt <= size()) {
/*     */       
/*  90 */       this.parent.add(this.start_index + paramInt, paramObject);
/*  91 */       this.end_index++;
/*     */     } else {
/*     */       
/*  94 */       throw new IndexOutOfBoundsException(paramInt + " > " + size());
/*     */     } 
/*     */   }
/*     */   
/*     */   public Object remove(int paramInt) {
/*  99 */     if (paramInt < size()) {
/*     */       
/* 101 */       this.end_index--;
/* 102 */       return this.parent.remove(this.start_index + paramInt);
/*     */     } 
/*     */     
/* 105 */     throw new IndexOutOfBoundsException(paramInt + " >= " + size());
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/util/Sublist.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */