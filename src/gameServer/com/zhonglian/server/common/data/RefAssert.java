/*     */ package com.zhonglian.server.common.data;
/*     */ 
/*     */ import BaseCommon.CommLog;
/*     */ import com.zhonglian.server.common.data.ref.RefBase;
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.ArrayList;
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
/*     */ public class RefAssert
/*     */ {
/*     */   public static boolean listSize(List<?> lista, List<?> listb, List... lists) {
/*  25 */     if (lista == null) {
/*  26 */       CommLog.error("[RefAssert.listSize] list(1) 为空");
/*  27 */       return false;
/*     */     } 
/*  29 */     if (listb == null) {
/*  30 */       CommLog.error("[RefAssert.listSize] list(2) 为空");
/*  31 */       return false;
/*     */     } 
/*  33 */     if (lista.size() != listb.size()) {
/*  34 */       CommLog.error("[RefAssert.listSize] list(1) 长度:{}, list(2)长度:{}", Integer.valueOf(lista.size()), Integer.valueOf(listb.size()));
/*  35 */       return false;
/*     */     } 
/*     */     
/*  38 */     int size = lista.size();
/*  39 */     for (int i = 0; i < lists.length; i++) {
/*  40 */       List<?> list = lists[i];
/*  41 */       if (list == null) {
/*  42 */         CommLog.error("[RefAssert.listSize] list({}) 为空", Integer.valueOf(i + 3));
/*  43 */         return false;
/*     */       } 
/*  45 */       if (list.size() != size) {
/*  46 */         CommLog.error("[RefAssert.listSize] list({}) 长度:{}, 其他list长度:{}", new Object[] { Integer.valueOf(i + 3), Integer.valueOf(list.size()), Integer.valueOf(size) });
/*  47 */         return false;
/*     */       } 
/*     */     } 
/*  50 */     return true;
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
/*     */   public static boolean inList(Object value, Object option1, Object... options) {
/*  63 */     if (value.getClass().isArray() || List.class.isAssignableFrom(value.getClass())) {
/*  64 */       for (Object v : value) {
/*  65 */         if (!v.equals(option1) && !contained(options, v)) {
/*  66 */           return false;
/*     */         }
/*     */       } 
/*     */     } else {
/*  70 */       return !(!value.equals(option1) && !contained(options, value));
/*     */     } 
/*  72 */     return false;
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
/*     */   
/*     */   public static boolean inRef(Object value, Class<? extends RefBase> clazz, String fieldName, Object... option) {
/*  89 */     Field field = null;
/*     */     try {
/*  91 */       field = clazz.getField(fieldName);
/*  92 */     } catch (Exception e) {
/*  93 */       CommLog.error("校验配表数值失败：[{}]表没有字段[{}]", clazz.getSimpleName(), fieldName);
/*  94 */       return false;
/*     */     } 
/*  96 */     RefField refField = field.<RefField>getAnnotation(RefField.class);
/*  97 */     boolean iskey = (refField != null && refField.iskey());
/*  98 */     if (value.getClass().isArray() || List.class.isAssignableFrom(value.getClass())) {
/*  99 */       List<Object> list = iskey ? new ArrayList() : getRefValues(clazz, field);
/* 100 */       for (Object v : value) {
/* 101 */         if (contained(option, v)) {
/*     */           continue;
/*     */         }
/* 104 */         if (iskey && AbstractRefDataMgr.get(clazz, v) == null) {
/* 105 */           CommLog.error("校验配表数值失败, 配表数值[{}]不存在于[{}]表的[{}]字段中", new Object[] { v, clazz.getSimpleName(), fieldName });
/* 106 */           return false;
/* 107 */         }  if (!iskey && !list.contains(v)) {
/* 108 */           CommLog.error("校验配表数值失败, 配表数值[{}]不存在于[{}]表的[{}]字段中", new Object[] { v, clazz.getSimpleName(), fieldName });
/* 109 */           return false;
/*     */         } 
/*     */       } 
/*     */     } else {
/* 113 */       if (contained(option, value)) {
/* 114 */         return true;
/*     */       }
/* 116 */       if (iskey && AbstractRefDataMgr.get(clazz, value) == null) {
/* 117 */         CommLog.error("校验配表数值失败, 配表数值[{}]不存在于[{}]表的[{}]字段中", new Object[] { value, clazz.getSimpleName(), fieldName });
/* 118 */         return false;
/* 119 */       }  if (!iskey && !getRefValues(clazz, field).contains(value)) {
/* 120 */         CommLog.error("校验配表数值失败, 配表数值[{}]不存在于[{}]表的[{}]字段中", new Object[] { value, clazz.getSimpleName(), fieldName });
/* 121 */         return false;
/*     */       } 
/*     */     } 
/* 124 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean inRef(Object value, Class<? extends RefBase> clazz, Object... option) {
/* 128 */     return inRef(value, clazz, "id", option);
/*     */   }
/*     */   
/*     */   private static List<Object> getRefValues(Class<? extends RefBase> clazz, Field field) {
/* 132 */     RefContainer<?> container = AbstractRefDataMgr.getAll(clazz);
/* 133 */     List<Object> list = new ArrayList();
/* 134 */     for (Object ref : container.values()) {
/*     */       try {
/* 136 */         list.add(field.get(ref));
/* 137 */       } catch (Exception e) {
/* 138 */         CommLog.error("校验配表数值失败：[{}]表没有字段[{}]", clazz.getSimpleName(), field.getName());
/*     */       } 
/*     */     } 
/* 141 */     return list; } private static boolean contained(Object[] array, Object v) {
/*     */     byte b;
/*     */     int i;
/*     */     Object[] arrayOfObject;
/* 145 */     for (i = (arrayOfObject = array).length, b = 0; b < i; ) { Object a = arrayOfObject[b];
/* 146 */       if (v.equals(a))
/* 147 */         return true; 
/*     */       b++; }
/*     */     
/* 150 */     return false;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/data/RefAssert.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */