/*     */ package com.mchange.v2.management;
/*     */ 
/*     */ import java.util.Comparator;
/*     */ import javax.management.MBeanOperationInfo;
/*     */ import javax.management.MBeanParameterInfo;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ManagementUtils
/*     */ {
/*  43 */   public static final Comparator PARAM_INFO_COMPARATOR = new Comparator()
/*     */     {
/*     */       public int compare(Object param1Object1, Object param1Object2)
/*     */       {
/*  47 */         MBeanParameterInfo mBeanParameterInfo1 = (MBeanParameterInfo)param1Object1;
/*  48 */         MBeanParameterInfo mBeanParameterInfo2 = (MBeanParameterInfo)param1Object2;
/*  49 */         int i = mBeanParameterInfo1.getType().compareTo(mBeanParameterInfo2.getType());
/*  50 */         if (i == 0) {
/*     */           
/*  52 */           i = mBeanParameterInfo1.getName().compareTo(mBeanParameterInfo2.getName());
/*  53 */           if (i == 0) {
/*     */             
/*  55 */             String str1 = mBeanParameterInfo1.getDescription();
/*  56 */             String str2 = mBeanParameterInfo2.getDescription();
/*  57 */             if (str1 == null && str2 == null) {
/*  58 */               i = 0;
/*  59 */             } else if (str1 == null) {
/*  60 */               i = -1;
/*  61 */             } else if (str2 == null) {
/*  62 */               i = 1;
/*     */             } else {
/*  64 */               i = str1.compareTo(str2);
/*     */             } 
/*     */           } 
/*  67 */         }  return i;
/*     */       }
/*     */     };
/*     */   
/*  71 */   public static final Comparator OP_INFO_COMPARATOR = new Comparator()
/*     */     {
/*     */       public int compare(Object param1Object1, Object param1Object2)
/*     */       {
/*  75 */         MBeanOperationInfo mBeanOperationInfo1 = (MBeanOperationInfo)param1Object1;
/*  76 */         MBeanOperationInfo mBeanOperationInfo2 = (MBeanOperationInfo)param1Object2;
/*  77 */         String str1 = mBeanOperationInfo1.getName();
/*  78 */         String str2 = mBeanOperationInfo2.getName();
/*  79 */         int i = String.CASE_INSENSITIVE_ORDER.compare(str1, str2);
/*  80 */         if (i == 0)
/*     */         {
/*  82 */           if (str1.equals(str2)) {
/*     */             
/*  84 */             MBeanParameterInfo[] arrayOfMBeanParameterInfo1 = mBeanOperationInfo1.getSignature();
/*  85 */             MBeanParameterInfo[] arrayOfMBeanParameterInfo2 = mBeanOperationInfo2.getSignature();
/*  86 */             if (arrayOfMBeanParameterInfo1.length < arrayOfMBeanParameterInfo2.length) {
/*  87 */               i = -1;
/*  88 */             } else if (arrayOfMBeanParameterInfo1.length > arrayOfMBeanParameterInfo2.length) {
/*  89 */               i = 1;
/*     */             } else {
/*     */               byte b; int j;
/*  92 */               for (b = 0, j = arrayOfMBeanParameterInfo1.length; b < j; b++) {
/*     */                 
/*  94 */                 i = ManagementUtils.PARAM_INFO_COMPARATOR.compare(arrayOfMBeanParameterInfo1[b], arrayOfMBeanParameterInfo2[b]);
/*  95 */                 if (i != 0) {
/*     */                   break;
/*     */                 }
/*     */               } 
/*     */             } 
/*     */           } else {
/*     */             
/* 102 */             i = str1.compareTo(str2);
/*     */           } 
/*     */         }
/* 105 */         return i;
/*     */       }
/*     */     };
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/management/ManagementUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */