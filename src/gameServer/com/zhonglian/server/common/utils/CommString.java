/*     */ package com.zhonglian.server.common.utils;
/*     */ 
/*     */ import BaseCommon.CommLog;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CommString
/*     */ {
/*     */   public static <T> String join(String sep, List<T> iters) {
/*  15 */     StringBuilder ret = new StringBuilder();
/*  16 */     if (iters.size() != 0) {
/*  17 */       boolean hasFirst = false;
/*  18 */       for (int index = 0; index < iters.size(); index++) {
/*  19 */         String toadd = iters.get(index).toString();
/*  20 */         if (!toadd.equals(""))
/*     */         {
/*  22 */           if (hasFirst) {
/*  23 */             ret.append(String.valueOf(sep) + toadd);
/*     */           } else {
/*  25 */             hasFirst = true;
/*  26 */             ret.append(toadd);
/*     */           }  } 
/*     */       } 
/*     */     } 
/*  30 */     return ret.toString();
/*     */   }
/*     */   
/*     */   public static List<Integer> getIntegerList(String src, String sep) {
/*  34 */     String[] srcEx = src.split(sep);
/*  35 */     List<Integer> ret = new ArrayList<>();
/*  36 */     for (int index = 0; index < srcEx.length; index++) {
/*  37 */       if (!srcEx[index].equals("")) {
/*     */         try {
/*  39 */           ret.add(Integer.valueOf(srcEx[index]));
/*  40 */         } catch (Exception e) {
/*  41 */           CommLog.error("getIntegerList src:" + src);
/*     */         } 
/*     */       }
/*     */     } 
/*  45 */     return ret;
/*     */   }
/*     */   
/*     */   public static List<Double> getDoubleList(String src, String sep) {
/*  49 */     String[] srcEx = src.split(sep);
/*  50 */     List<Double> ret = new ArrayList<>();
/*  51 */     for (int index = 0; index < srcEx.length; index++) {
/*  52 */       if (!srcEx[index].equals("")) {
/*     */         try {
/*  54 */           ret.add(Double.valueOf(srcEx[index]));
/*  55 */         } catch (Exception e) {
/*  56 */           CommLog.error("getIntegerList src:" + src);
/*     */         } 
/*     */       }
/*     */     } 
/*  60 */     return ret;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int length(String value) {
/*  70 */     int valueLength = 0;
/*  71 */     String chinese = "[一-龥]";
/*  72 */     for (int i = 0; i < value.length(); i++) {
/*  73 */       String temp = value.substring(i, i + 1);
/*  74 */       if (temp.matches(chinese)) {
/*  75 */         valueLength += 2;
/*     */       } else {
/*  77 */         valueLength++;
/*     */       } 
/*     */     } 
/*  80 */     return valueLength;
/*     */   }
/*     */   
/*     */   public static String output_classField(Object t) {
/*  84 */     return output_class(t, false, true);
/*     */   }
/*     */   
/*     */   public static String output_class_line(Object t) {
/*  88 */     return output_class(t, true, false);
/*     */   }
/*     */   
/*     */   public static String output_class(Object t) {
/*  92 */     return output_class(t, false, false);
/*     */   }
/*     */   
/*     */   public static String output_class_deep(Object t, int deep) {
/*  96 */     return objectTosString(t, deep);
/*     */   }
/*     */   
/*     */   private static String output_class(Object t, boolean isInline, boolean isField) {
/* 100 */     StringBuilder sb = new StringBuilder();
/* 101 */     if (!isInline && !isField) {
/* 102 */       sb.append(String.format("===[%s] start...\n", new Object[] { t.getClass().getSimpleName() }));
/*     */     }
/* 104 */     Field[] fds = t.getClass().getDeclaredFields(); byte b; int i;
/*     */     Field[] arrayOfField1;
/* 106 */     for (i = (arrayOfField1 = fds).length, b = 0; b < i; ) { Field fd = arrayOfField1[b];
/*     */       
/* 108 */       Class<?> typeClass = fd.getType();
/*     */       
/* 110 */       Object fieldValue = null;
/*     */ 
/*     */ 
/*     */       
/* 114 */       List.class.isAssignableFrom(typeClass);
/*     */ 
/*     */ 
/*     */       
/* 118 */       if (!Map.class.isAssignableFrom(typeClass))
/*     */       {
/* 120 */         if (!typeClass.isArray())
/*     */           
/*     */           try {
/*     */             
/* 124 */             fd.setAccessible(true);
/* 125 */             fieldValue = fd.get(t);
/* 126 */           } catch (IllegalAccessException e) {
/* 127 */             CommLog.error(CommString.class.getName(), e);
/*     */           }  
/*     */       }
/* 130 */       if (isField) {
/* 131 */         sb.append(String.format("%s\t", new Object[] { fd.getName() }));
/* 132 */       } else if (fieldValue != null) {
/* 133 */         if (isInline) {
/* 134 */           sb.append(String.format("%s\t", new Object[] { fieldValue.toString() }));
/*     */         } else {
/* 136 */           sb.append(String.format("[%s]%s = %s", new Object[] { fd.getType().getSimpleName(), fd.getName(), fieldValue.toString() }));
/* 137 */           sb.append(System.lineSeparator());
/*     */         } 
/*     */       } else {
/* 140 */         sb.append(isInline ? "null" : "null\n");
/*     */       }  b++; }
/*     */     
/* 143 */     if (!isInline && !isField) {
/* 144 */       sb.append(String.format("===[%s] end!!!\n\n", new Object[] { t.getClass().getSimpleName() }));
/*     */     }
/* 146 */     return sb.toString();
/*     */   }
/*     */   
/*     */   private static String objectTosString(Object o, int deep) {
/* 150 */     ArrayList<Object> already = new ArrayList();
/* 151 */     StringBuffer s = new StringBuffer();
/* 152 */     s.append("{\n");
/* 153 */     printObject(o, s, "    ", deep, already);
/* 154 */     s.append("}\n");
/* 155 */     return s.toString();
/*     */   }
/*     */   
/*     */   private static void printObject(Object o, StringBuffer s, String blank, int deep, ArrayList<Object> already) {
/* 159 */     if (o == null) {
/* 160 */       s.append(blank).append("null\n");
/*     */       return;
/*     */     } 
/* 163 */     deep--;
/*     */     
/* 165 */     Class<?> clazz = o.getClass();
/* 166 */     Field[] fields = clazz.getDeclaredFields(); byte b; int i; Field[] arrayOfField1;
/* 167 */     for (i = (arrayOfField1 = fields).length, b = 0; b < i; ) { Field field = arrayOfField1[b];
/* 168 */       boolean isAccessible = field.isAccessible();
/* 169 */       field.setAccessible(true);
/*     */       
/*     */       try {
/* 172 */         String value = field.get(o).toString();
/* 173 */         if (value.contains("@"))
/* 174 */         { String typeName = "[" + field.getType().getSimpleName() + "]";
/*     */           
/* 176 */           if (already.contains(field.get(o))) {
/* 177 */             s.append(blank).append(field.getName()).append("{\n");
/* 178 */             s.append(blank).append("↑↑↑ ").append(field.getType().getSimpleName()).append("\n");
/* 179 */             s.append(blank).append("}\n");
/*     */           } else {
/*     */             
/* 182 */             already.add(field.get(o));
/* 183 */             if (value.startsWith("[L")) {
/* 184 */               s.append(blank).append(typeName).append(field.getName()).append("(size:").append(Array.getLength(field.get(o))).append(") [\n");
/* 185 */               for (int j = 0; j < Array.getLength(field.get(o)); j++) {
/* 186 */                 s.append(blank).append("    ").append(field.getName()).append("[").append(j).append("]{\n");
/* 187 */                 if (deep > 0) {
/* 188 */                   printObject(Array.get(field.get(o), j), s, String.valueOf(blank) + "        ", deep - 1, already);
/*     */                 } else {
/* 190 */                   s.append(blank).append("    ......\n");
/*     */                 } 
/* 192 */                 s.append(blank).append("    }\n");
/*     */               } 
/* 194 */               s.append(blank).append("]\n");
/* 195 */             } else if (List.class.isAssignableFrom(field.getType())) {
/*     */               
/* 197 */               List<Object> temp = (List<Object>)field.get(o);
/* 198 */               s.append(blank).append(typeName).append(field.getName()).append("(size:").append(temp.size()).append(") [\n");
/* 199 */               int index = 0;
/* 200 */               for (Object t : temp) {
/* 201 */                 s.append(blank).append("    ").append(field.getName()).append("[").append(index).append("]{\n");
/* 202 */                 printObject(t, s, String.valueOf(blank) + "        ", deep - 1, already);
/* 203 */                 s.append(blank).append("    }\n");
/* 204 */                 index++;
/*     */               } 
/* 206 */               s.append(blank).append("]\n");
/* 207 */             } else if (Map.class.isAssignableFrom(field.getType())) {
/*     */               
/* 209 */               Map<Object, Object> temp = (Map<Object, Object>)field.get(o);
/* 210 */               s.append(blank).append(typeName).append(field.getName()).append("(size:").append(temp.size()).append(") {\n");
/* 211 */               for (Map.Entry<Object, Object> entry : temp.entrySet()) {
/* 212 */                 s.append(blank).append("    key=").append(entry.getKey().toString()).append(", value={\n");
/* 213 */                 if (deep > 0) {
/* 214 */                   printObject(entry.getValue(), s, String.valueOf(blank) + "        ", deep - 1, already);
/*     */                 } else {
/* 216 */                   s.append(blank).append("    ......\n");
/*     */                 } 
/* 218 */                 s.append(blank).append("    }\n");
/*     */               } 
/* 220 */               s.append(blank).append("}\n");
/*     */             } else {
/* 222 */               s.append(blank).append(typeName).append(field.getName()).append("{\n");
/* 223 */               if (deep > 0) {
/* 224 */                 printObject(field.get(o), s, String.valueOf(blank) + "    ", deep - 1, already);
/*     */               } else {
/* 226 */                 s.append(blank).append("......\n");
/*     */               } 
/* 228 */               s.append(blank).append("}\n");
/*     */             } 
/* 230 */             already.remove(field.get(o));
/*     */           }  }
/* 232 */         else { s.append(blank).append(field.getName()).append(":");
/* 233 */           s.append(value).append("\n"); }
/*     */       
/* 235 */       } catch (IllegalArgumentException|IllegalAccessException|ArrayIndexOutOfBoundsException e) {
/* 236 */         if (!field.getName().startsWith("this")) {
/* 237 */           s.append(blank).append(field.getName()).append(":");
/* 238 */           s.append("null\n");
/*     */         } 
/*     */       } 
/*     */       
/* 242 */       field.setAccessible(isAccessible);
/*     */       b++; }
/*     */   
/*     */   }
/*     */   public static String format(String format, Object... arguments) {
/* 247 */     if (format == null) {
/* 248 */       return format;
/*     */     }
/*     */     
/* 251 */     for (int i = 0; i < arguments.length; i++) {
/* 252 */       String info = (arguments[i] == null) ? "[null]" : arguments[i].toString();
/* 253 */       format = format.replaceAll("\\{" + i + "\\}", info);
/*     */     } 
/* 255 */     return format;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/utils/CommString.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */