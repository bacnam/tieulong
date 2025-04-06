/*     */ package com.mchange.v3.decode;
/*     */ 
/*     */ import com.mchange.v2.log.MLevel;
/*     */ import com.mchange.v2.log.MLog;
/*     */ import com.mchange.v2.log.MLogger;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public final class DecodeUtils
/*     */ {
/*     */   public static final String DECODER_CLASS_DOT_KEY = ".decoderClass";
/*     */   public static final String DECODER_CLASS_NO_DOT_KEY = "decoderClass";
/*  47 */   private static final Object[] DECODER_CLASS_DOT_KEY_OBJ_ARRAY = new Object[] { ".decoderClass" };
/*  48 */   private static final Object[] DECODER_CLASS_NO_DOT_KEY_OBJ_ARRAY = new Object[] { "decoderClass" };
/*     */   
/*  50 */   private static final MLogger logger = MLog.getLogger(DecodeUtils.class);
/*     */   
/*     */   private static final List<DecoderFinder> finders;
/*     */   
/*  54 */   private static final String[] finderClassNames = new String[] { "com.mchange.sc.v1.decode.ScalaMapDecoderFinder" };
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*  60 */     LinkedList<JavaMapDecoderFinder> linkedList = new LinkedList();
/*  61 */     linkedList.add(new JavaMapDecoderFinder()); byte b; int i;
/*  62 */     for (b = 0, i = finderClassNames.length; b < i; b++) {
/*     */       try {
/*  64 */         linkedList.add((DecoderFinder)Class.forName(finderClassNames[b]).newInstance());
/*  65 */       } catch (Exception exception) {
/*     */         
/*  67 */         if (logger.isLoggable(MLevel.INFO))
/*  68 */           logger.log(MLevel.INFO, "Could not load DecoderFinder '" + finderClassNames[b] + "'", exception); 
/*     */       } 
/*     */     } 
/*  71 */     finders = Collections.unmodifiableList((List)linkedList);
/*     */   }
/*     */   
/*     */   static class JavaMapDecoderFinder
/*     */     implements DecoderFinder
/*     */   {
/*     */     public String decoderClassName(Object param1Object) throws CannotDecodeException {
/*  78 */       if (param1Object instanceof Map) {
/*     */         
/*  80 */         String str = null;
/*  81 */         Map map = (Map)param1Object;
/*  82 */         str = (String)map.get(".decoderClass");
/*  83 */         if (str == null)
/*  84 */           str = (String)map.get("decoderClass"); 
/*  85 */         if (str == null) {
/*  86 */           throw new CannotDecodeException("Could not find the decoder class for java.util.Map: " + param1Object);
/*     */         }
/*  88 */         return str;
/*     */       } 
/*     */       
/*  91 */       return null;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final String findDecoderClassName(Object paramObject) throws CannotDecodeException {
/*  97 */     for (DecoderFinder decoderFinder : finders) {
/*     */       
/*  99 */       String str = decoderFinder.decoderClassName(paramObject);
/* 100 */       if (str != null) return str; 
/*     */     } 
/* 102 */     throw new CannotDecodeException("Could not find a decoder class name for object: " + paramObject);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object decode(String paramString, Object paramObject) throws CannotDecodeException {
/*     */     try {
/* 109 */       Class<?> clazz = Class.forName(paramString);
/* 110 */       Decoder decoder = (Decoder)clazz.newInstance();
/* 111 */       return decoder.decode(paramObject);
/*     */     }
/* 113 */     catch (Exception exception) {
/* 114 */       throw new CannotDecodeException("An exception occurred while attempting to decode " + paramObject, exception);
/*     */     } 
/*     */   }
/*     */   public static Object decode(Object paramObject) throws CannotDecodeException {
/* 118 */     return decode(findDecoderClassName(paramObject), paramObject);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v3/decode/DecodeUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */