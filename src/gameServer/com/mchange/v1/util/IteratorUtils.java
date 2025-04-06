/*     */ package com.mchange.v1.util;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.ArrayList;
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
/*     */ public final class IteratorUtils
/*     */ {
/*  43 */   public static final Iterator EMPTY_ITERATOR = new Iterator()
/*     */     {
/*     */       public boolean hasNext() {
/*  46 */         return false;
/*     */       }
/*     */       public Object next() {
/*  49 */         throw new NoSuchElementException();
/*     */       }
/*     */       public void remove() {
/*  52 */         throw new IllegalStateException();
/*     */       }
/*     */     };
/*     */   
/*     */   public static Iterator oneElementUnmodifiableIterator(final Object elem) {
/*  57 */     return new Iterator() {
/*     */         boolean shot = false;
/*     */         
/*     */         public boolean hasNext() {
/*  61 */           return !this.shot;
/*     */         }
/*     */         
/*     */         public Object next() {
/*  65 */           if (this.shot) {
/*  66 */             throw new NoSuchElementException();
/*     */           }
/*     */           
/*  69 */           this.shot = true;
/*  70 */           return elem;
/*     */         }
/*     */ 
/*     */         
/*     */         public void remove() {
/*  75 */           throw new UnsupportedOperationException("remove() not supported.");
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean equivalent(Iterator<Object> paramIterator1, Iterator<Object> paramIterator2) {
/*     */     while (true) {
/*  83 */       boolean bool1 = paramIterator1.hasNext();
/*  84 */       boolean bool2 = paramIterator2.hasNext();
/*  85 */       if (bool1 ^ bool2)
/*  86 */         return false; 
/*  87 */       if (bool1) {
/*     */         
/*  89 */         Object object1 = paramIterator1.next();
/*  90 */         Object object2 = paramIterator2.next();
/*  91 */         if (object1 == object2)
/*     */           continue; 
/*  93 */         if (object1 == null)
/*  94 */           return false; 
/*  95 */         if (!object1.equals(object2))
/*  96 */           return false;  continue;
/*     */       }  break;
/*  98 */     }  return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static ArrayList toArrayList(Iterator paramIterator, int paramInt) {
/* 104 */     ArrayList arrayList = new ArrayList(paramInt);
/* 105 */     while (paramIterator.hasNext())
/* 106 */       arrayList.add(paramIterator.next()); 
/* 107 */     return arrayList;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void fillArray(Iterator paramIterator, Object[] paramArrayOfObject, boolean paramBoolean) {
/* 123 */     byte b = 0;
/* 124 */     int i = paramArrayOfObject.length;
/* 125 */     while (b < i && paramIterator.hasNext())
/* 126 */       paramArrayOfObject[b++] = paramIterator.next(); 
/* 127 */     if (paramBoolean && b < i)
/* 128 */       paramArrayOfObject[b] = null; 
/*     */   }
/*     */   
/*     */   public static void fillArray(Iterator paramIterator, Object[] paramArrayOfObject) {
/* 132 */     fillArray(paramIterator, paramArrayOfObject, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object[] toArray(Iterator paramIterator, int paramInt, Class<?> paramClass, boolean paramBoolean) {
/* 140 */     Object[] arrayOfObject = (Object[])Array.newInstance(paramClass, paramInt);
/* 141 */     fillArray(paramIterator, arrayOfObject, paramBoolean);
/* 142 */     return arrayOfObject;
/*     */   }
/*     */   
/*     */   public static Object[] toArray(Iterator paramIterator, int paramInt, Class paramClass) {
/* 146 */     return toArray(paramIterator, paramInt, paramClass, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object[] toArray(Iterator paramIterator, int paramInt, Object[] paramArrayOfObject) {
/* 155 */     if (paramArrayOfObject.length >= paramInt) {
/*     */       
/* 157 */       fillArray(paramIterator, paramArrayOfObject, true);
/* 158 */       return paramArrayOfObject;
/*     */     } 
/*     */ 
/*     */     
/* 162 */     Class<?> clazz = paramArrayOfObject.getClass().getComponentType();
/* 163 */     return toArray(paramIterator, paramInt, clazz);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/util/IteratorUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */