/*     */ package com.zhonglian.server.common.data;
/*     */ 
/*     */ import BaseCommon.CommClass;
/*     */ import BaseCommon.CommLog;
/*     */ import com.zhonglian.server.common.data.ref.RefBase;
/*     */ import com.zhonglian.server.common.data.ref.RefFactor;
/*     */ import com.zhonglian.server.common.data.ref.RefGeneral;
/*     */ import com.zhonglian.server.common.data.ref.matcher.NumberRange;
/*     */ import com.zhonglian.server.common.utils.CommFile;
/*     */ import com.zhonglian.server.common.utils.CommTime;
/*     */ import java.io.File;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractRefDataMgr
/*     */ {
/*  34 */   private static AbstractRefDataMgr instance = null;
/*     */   
/*     */   protected Map<Class<? extends RefBase>, RefContainer<?>> refs;
/*     */   
/*     */   private List<String> dulError;
/*     */   
/*     */   private boolean assertPassed;
/*     */ 
/*     */   
/*     */   protected AbstractRefDataMgr() {
/*  44 */     this.refs = new ConcurrentHashMap<>();
/*  45 */     this.dulError = new ArrayList<>();
/*  46 */     this.assertPassed = false;
/*     */     instance = this;
/*     */   } public static <T extends RefBase> T get(Class<T> clazz, Object key) {
/*     */     RefBase refBase;
/*  50 */     T ret = null;
/*  51 */     if (instance.refs.containsKey(clazz)) {
/*  52 */       refBase = ((RefContainer<RefBase>)instance.refs.get(clazz)).get(key);
/*     */     }
/*  54 */     if (refBase == null)
/*  55 */       CommLog.warn("BaseRefDataMgr get clazz:{}, key:{} failed!!", clazz.getSimpleName(), key.toString()); 
/*  56 */     return (T)refBase;
/*     */   } public static AbstractRefDataMgr getInstance() {
/*     */     return instance;
/*     */   } public static <T extends RefBase> T getOrLast(Class<T> clazz, Object key) {
/*     */     RefBase refBase;
/*  61 */     T ret = null;
/*  62 */     if (instance.refs.containsKey(clazz)) {
/*  63 */       refBase = ((RefContainer<RefBase>)instance.refs.get(clazz)).get(key);
/*     */     }
/*  65 */     if (refBase == null)
/*  66 */       return getAll(clazz).last(); 
/*  67 */     return (T)refBase;
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T extends RefBase> RefContainer<T> getAll(Class<T> clazz) {
/*  72 */     if (!instance.refs.containsKey(clazz)) {
/*  73 */       instance.refs.put(clazz, new RefContainer());
/*     */     }
/*  75 */     return (RefContainer<T>)instance.refs.get(clazz);
/*     */   }
/*     */   
/*     */   public static <T extends RefBase> int size(Class<T> clazz) {
/*  79 */     if (!instance.refs.containsKey(clazz)) {
/*  80 */       instance.refs.put(clazz, new RefContainer());
/*     */     }
/*  82 */     return ((RefContainer)instance.refs.get(clazz)).size();
/*     */   }
/*     */   
/*     */   public static int getFactor(String name) {
/*     */     try {
/*  87 */       RefFactor ref = get(RefFactor.class, name);
/*  88 */       if (ref != null) {
/*  89 */         return (int)ref.Value;
/*     */       }
/*  91 */     } catch (Exception e) {
/*  92 */       return 0;
/*     */     } 
/*  94 */     return 0;
/*     */   }
/*     */   
/*     */   public static int getFactor(String name, int defaultValue) {
/*     */     try {
/*  99 */       RefFactor ref = get(RefFactor.class, name);
/* 100 */       if (ref != null) {
/* 101 */         return (int)ref.Value;
/*     */       }
/* 103 */     } catch (Exception e) {
/* 104 */       return defaultValue;
/*     */     } 
/* 106 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public static double getFactor(String name, double defaultValue) {
/*     */     try {
/* 111 */       RefFactor ref = get(RefFactor.class, name);
/* 112 */       if (ref != null) {
/* 113 */         return ref.Value;
/*     */       }
/* 115 */     } catch (Exception e) {
/* 116 */       return defaultValue;
/*     */     } 
/* 118 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public static String getGeneral(String name, String defaultValue) {
/*     */     try {
/* 123 */       RefGeneral ref = get(RefGeneral.class, name);
/* 124 */       if (ref != null) {
/* 125 */         return ref.Value;
/*     */       }
/* 127 */     } catch (Exception e) {
/* 128 */       return defaultValue;
/*     */     } 
/* 130 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public static List<String> getGeneral(String name) {
/* 134 */     List<String> list = new ArrayList<>();
/*     */     
/* 136 */     RefGeneral ref = get(RefGeneral.class, name);
/*     */     try {
/* 138 */       if (ref != null) {
/* 139 */         String[] lists = ref.Value.split(";");
/* 140 */         list = Arrays.asList(lists);
/* 141 */         return list;
/*     */       } 
/* 143 */     } catch (Exception e) {
/* 144 */       if (ref == null) {
/* 145 */         CommLog.error("getGeneral name:{}, null", name);
/*     */       } else {
/* 147 */         CommLog.error("getGeneral name:{}, value:{} parse List failed", name, ref.Value);
/*     */       } 
/* 149 */       return list;
/*     */     } 
/* 151 */     return list;
/*     */   }
/*     */   
/*     */   public void reload() {
/* 155 */     CommLog.info("=======开始加载配表");
/* 156 */     this.dulError.clear();
/*     */     
/* 158 */     checkDirects();
/* 159 */     renameRefFilesToLowcase();
/*     */ 
/*     */     
/* 162 */     load(RefBase.class);
/* 163 */     onCustomLoad();
/*     */ 
/*     */     
/* 166 */     this.assertPassed = assertRefs();
/* 167 */     CommLog.info("=======配表加载结束");
/*     */   }
/*     */   
/*     */   public boolean assertRefs() {
/* 171 */     long begin = CommTime.nowMS();
/* 172 */     CommLog.info("进行配表检测...");
/* 173 */     boolean rtn = true;
/* 174 */     for (Map.Entry<Class<? extends RefBase>, RefContainer<?>> pair : this.refs.entrySet()) {
/* 175 */       for (RefBase ref : ((RefContainer)pair.getValue()).values()) {
/*     */         try {
/* 177 */           if (!ref.Assert()) {
/* 178 */             CommLog.error("[{}] 配表自校验发现问题:{}", ref.getClass().getSimpleName(), CommClass.getClassPropertyInfos(ref));
/* 179 */             rtn = false;
/*     */           } 
/* 181 */         } catch (Throwable e) {
/* 182 */           CommLog.error("[{}] 配表自校验发现问题:{},异常:", new Object[] { ref.getClass().getSimpleName(), CommClass.getClassPropertyInfos(ref), e });
/* 183 */           rtn = false;
/*     */         } 
/*     */       } 
/*     */       try {
/* 187 */         RefBase base = ((Class<RefBase>)pair.getKey()).newInstance();
/* 188 */         if (!base.AssertAll(pair.getValue())) {
/* 189 */           CommLog.error("[{}] 配表全局校验发现问题", ((Class)pair.getKey()).getSimpleName());
/* 190 */           rtn = false;
/*     */         } 
/* 192 */       } catch (Throwable e) {
/* 193 */         CommLog.error("[{}] 配表全局校验发现问题, 异常:", ((Class)pair.getKey()).getSimpleName(), e);
/* 194 */         rtn = false;
/*     */       } 
/*     */     } 
/*     */     try {
/* 198 */       if (!assertAll()) {
/* 199 */         CommLog.error("配表总检不通过");
/* 200 */         rtn = false;
/*     */       } 
/* 202 */     } catch (Throwable e) {
/* 203 */       CommLog.error("refdata自校验不通过", e);
/* 204 */       rtn = false;
/*     */     } 
/* 206 */     CommLog.info("配表检测完毕，用时:{}ms", Long.valueOf(CommTime.nowMS() - begin));
/* 207 */     return rtn;
/*     */   }
/*     */   
/*     */   protected abstract boolean assertAll();
/*     */   
/*     */   public boolean isAssertPassed() {
/* 213 */     return this.assertPassed;
/*     */   }
/*     */   
/*     */   public <T extends RefBase> void load(Class<T> clazz) {
/* 217 */     List<Class<?>> refdatas = CommClass.getAllClassByInterface(clazz, clazz.getPackage().getName());
/* 218 */     for (Class<?> cs : refdatas) {
/* 219 */       RefBase refdata = null;
/*     */       try {
/* 221 */         refdata = CommClass.forName(cs.getName()).newInstance();
/* 222 */       } catch (Exception e) {
/* 223 */         CommLog.error("onAutoLoad occured error:{}", e.getMessage());
/*     */       } 
/* 225 */       if (refdata == null) {
/*     */         continue;
/*     */       }
/* 228 */       RefContainer<?> data = this.refs.get(cs);
/*     */       
/* 230 */       if (data == null) {
/* 231 */         data = new RefContainer();
/* 232 */         this.refs.put(refdata.getClass(), data);
/*     */       } 
/*     */       
/* 235 */       loadByClazz((Class)refdata.getClass(), data);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract void onCustomLoad();
/*     */ 
/*     */   
/*     */   public abstract String getRefPath();
/*     */ 
/*     */   
/*     */   public boolean checkDirects() {
/* 247 */     File path = new File(getRefPath());
/* 248 */     if (!path.exists() || !path.isDirectory()) {
/* 249 */       CommLog.error("【【【Refdata文件夹地址(" + path.getAbsolutePath() + ")配置错误！！！】】】");
/* 250 */       System.exit(-1);
/*     */     } 
/* 252 */     return true;
/*     */   }
/*     */   
/*     */   public boolean renameRefFilesToLowcase() {
/* 256 */     File path = new File(getRefPath());
/* 257 */     File[] files = path.listFiles(); byte b; int i; File[] arrayOfFile1;
/* 258 */     for (i = (arrayOfFile1 = files).length, b = 0; b < i; ) { File refFile = arrayOfFile1[b];
/* 259 */       if (refFile.isFile())
/*     */       {
/*     */ 
/*     */         
/* 263 */         if (refFile.getName().toLowerCase().endsWith(".txt")) {
/*     */ 
/*     */ 
/*     */           
/* 267 */           File lowcaseFile = new File(refFile.getParentFile(), refFile.getName().toLowerCase());
/* 268 */           if (!lowcaseFile.exists())
/*     */           {
/*     */ 
/*     */             
/* 272 */             refFile.renameTo(lowcaseFile); } 
/*     */         }  }  b++; }
/* 274 */      return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends RefBase> void loadByClazz(Class<? extends RefBase> clazz, RefContainer<T> data) {
/* 279 */     String name = clazz.getSimpleName();
/* 280 */     String path = String.format("%s%c%s.txt", new Object[] { getRefPath(), Character.valueOf(File.separatorChar), name.substring(3).toLowerCase() });
/* 281 */     Map<String, Map<String, String>> table = CommFile.GetTable(path, 2, 3);
/*     */     
/* 283 */     Field keysField = null; byte b; int i; Field[] arrayOfField;
/* 284 */     for (i = (arrayOfField = clazz.getFields()).length, b = 0; b < i; ) { Field f = arrayOfField[b];
/* 285 */       RefField annotation = f.<RefField>getAnnotation(RefField.class);
/* 286 */       if (annotation != null && annotation.iskey())
/* 287 */         if (keysField != null) {
/* 288 */           CommLog.error("[{}]配置了多个键值({},{})", new Object[] { clazz.getSimpleName(), f.getName(), keysField.getName() });
/* 289 */           System.exit(1);
/*     */         } else {
/* 291 */           keysField = f;
/*     */         }  
/*     */       b++; }
/*     */     
/* 295 */     if (keysField == null) {
/* 296 */       CommLog.error("[{}]未配置键值", clazz.getSimpleName());
/* 297 */       System.exit(1);
/*     */     } 
/*     */     
/* 300 */     for (Map<String, String> lineValue : table.values()) {
/* 301 */       String keysrtvalue = lineValue.get(keysField.getName());
/* 302 */       if (keysrtvalue == null) {
/* 303 */         CommLog.error("[{}]txt配表上缺少键值[{}]的值配置", clazz.getName(), keysField.getName());
/* 304 */         System.exit(1);
/*     */       } 
/* 306 */       Object keyvalue = parseCreateObj(keysField.getType(), keysrtvalue, clazz.getSimpleName(), keysField.getName());
/* 307 */       if (keyvalue == null) {
/* 308 */         CommLog.error("[{}]txt配表上缺少键值[{}]值[{}]解析失败", new Object[] { clazz.getName(), keysField.getName(), keysrtvalue });
/*     */         continue;
/*     */       } 
/* 311 */       RefBase refBase = (RefBase)data.get(keyvalue);
/* 312 */       if (refBase == null) {
/*     */         try {
/* 314 */           refBase = clazz.newInstance();
/* 315 */         } catch (Exception e) {
/* 316 */           CommLog.error("[{}]创建实例失败", e);
/* 317 */           System.exit(1);
/*     */         } 
/* 319 */         data.put(keyvalue, (T)refBase);
/*     */       } 
/*     */       try {
/* 322 */         loadLineValue(refBase, lineValue);
/* 323 */       } catch (Exception e) {
/* 324 */         CommLog.error("[{}]解析配置时发生错误", clazz.getName(), e);
/*     */       } 
/*     */     } 
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
/*     */   public <T extends RefBase> void loadLineValue(T obj, Map<String, String> lineValue) throws Exception {
/* 342 */     String parsingTable = obj.getClass().getSimpleName();
/* 343 */     Field[] fields = obj.getClass().getDeclaredFields(); byte b; int i; Field[] arrayOfField1;
/* 344 */     for (i = (arrayOfField1 = fields).length, b = 0; b < i; ) { Field field = arrayOfField1[b];
/* 345 */       int modifiers = field.getModifiers();
/* 346 */       if (!Modifier.isStatic(modifiers) && !Modifier.isFinal(modifiers)) {
/*     */ 
/*     */ 
/*     */         
/* 350 */         RefField annotation = field.<RefField>getAnnotation(RefField.class);
/* 351 */         if (annotation == null || annotation.isfield()) {
/*     */           Object fieldValue;
/*     */           
/* 354 */           String varName = field.getName();
/* 355 */           String varValue = lineValue.get(varName);
/* 356 */           Class<?> typeClass = field.getType();
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 361 */           if (List.class.isAssignableFrom(typeClass)) {
/* 362 */             List<Object> listValue = null;
/* 363 */             if (typeClass.isAssignableFrom(List.class)) {
/* 364 */               listValue = new ArrayList();
/*     */             } else {
/* 366 */               listValue = (List<Object>)typeClass.newInstance();
/*     */             } 
/*     */             
/* 369 */             if (varValue != null && !varValue.equals("")) {
/* 370 */               Type gt = field.getGenericType();
/* 371 */               ParameterizedType pt = (ParameterizedType)gt;
/* 372 */               Class<?> clazz = (Class)pt.getActualTypeArguments()[0];
/* 373 */               String[] valueList = varValue.split(";");
/* 374 */               int length = valueList.length;
/* 375 */               for (int index = 0; index < length; index++) {
/* 376 */                 listValue.add(parseCreateObj(clazz, valueList[index], parsingTable, varName));
/*     */               }
/*     */             } 
/* 379 */             fieldValue = listValue;
/*     */           }
/* 381 */           else if (typeClass.isArray()) {
/* 382 */             Class<Object[]> typeListClass = (Class)field.getType();
/*     */ 
/*     */             
/* 385 */             Class<Object> subTypeClass = (Class)typeClass.getComponentType();
/*     */             
/* 387 */             Object[] listValue = (Object[])Array.newInstance(subTypeClass, 0);
/* 388 */             if (varValue != null && !varValue.equals("")) {
/* 389 */               String[] valueList = varValue.split(";");
/* 390 */               int length = valueList.length;
/*     */               
/* 392 */               listValue = (Object[])Array.newInstance(subTypeClass, length);
/*     */               
/* 394 */               for (int index = 0; index < length; index++) {
/* 395 */                 listValue[index] = parseCreateObj(typeListClass.getComponentType(), valueList[index], parsingTable, varName);
/*     */               }
/*     */             } 
/*     */             
/* 399 */             fieldValue = listValue;
/*     */           }
/* 401 */           else if (varValue != null) {
/* 402 */             fieldValue = parseCreateObj(typeClass, varValue, parsingTable, varName);
/*     */           } else {
/* 404 */             fieldValue = null;
/*     */           } 
/*     */ 
/*     */ 
/*     */           
/* 409 */           if (fieldValue != null)
/* 410 */             field.set(obj, fieldValue); 
/*     */         } 
/*     */       } 
/*     */       b++; }
/*     */   
/*     */   }
/*     */   private Object parseCreateObj(Class<?> field, String value, String tbName, String fieldName) {
/*     */     try {
/* 418 */       if (field == int.class || field == Integer.class) {
/*     */         
/* 420 */         int index = value.indexOf(".");
/* 421 */         if (-1 != index) {
/* 422 */           value = value.substring(0, index);
/*     */         }
/*     */         
/* 425 */         if (value.equals("")) {
/* 426 */           return Integer.valueOf(0);
/*     */         }
/* 428 */         int ret = Integer.valueOf(value).intValue();
/* 429 */         return Integer.valueOf(ret);
/* 430 */       }  if (field == long.class || field == Long.class) {
/*     */         
/* 432 */         if (value.equals("")) {
/* 433 */           return Long.valueOf(0L);
/*     */         }
/* 435 */         return Long.valueOf(value);
/* 436 */       }  if (field == float.class || field == Float.class) {
/*     */         
/* 438 */         if (value.equals("")) {
/* 439 */           return Float.valueOf(0.0F);
/*     */         }
/* 441 */         return Float.valueOf(value);
/* 442 */       }  if (field == double.class || field == Double.class) {
/*     */         
/* 444 */         if (value.equals("")) {
/* 445 */           return Float.valueOf(0.0F);
/*     */         }
/* 447 */         return Double.valueOf(value);
/* 448 */       }  if (field == boolean.class || field == Boolean.class)
/*     */         try {
/* 450 */           int v = Integer.valueOf(value).intValue();
/* 451 */           return (v != 0) ? Boolean.valueOf(true) : Boolean.valueOf(false);
/* 452 */         } catch (Exception e) {
/* 453 */           return Boolean.valueOf(value);
/*     */         }  
/* 455 */       if (field == NumberRange.class)
/* 456 */         return NumberRange.parse(value); 
/* 457 */       if (field == String.class)
/* 458 */         return value; 
/* 459 */       if (field.isEnum()) {
/* 460 */         if (value.isEmpty()) {
/* 461 */           value = "None";
/*     */         }
/* 463 */         return Enum.valueOf(field, value);
/*     */       }
/*     */     
/* 466 */     } catch (Exception e) {
/* 467 */       log("BaseRefDataMgr 解析值失败 [%s] field:[%s] type:[%s] value:[%s] ", new Object[] { tbName, fieldName, field.getSimpleName(), value });
/*     */     } 
/* 469 */     return null;
/*     */   }
/*     */   
/*     */   private void log(String format, Object... param) {
/* 473 */     String err = String.format(format, param);
/* 474 */     if (!this.dulError.contains(err)) {
/* 475 */       CommLog.error(err);
/* 476 */       this.dulError.add(err);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/data/AbstractRefDataMgr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */