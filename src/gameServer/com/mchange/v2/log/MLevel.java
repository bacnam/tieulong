/*     */ package com.mchange.v2.log;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
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
/*     */ public final class MLevel
/*     */ {
/*     */   public static final MLevel ALL;
/*     */   public static final MLevel CONFIG;
/*     */   public static final MLevel FINE;
/*     */   public static final MLevel FINER;
/*     */   public static final MLevel FINEST;
/*     */   public static final MLevel INFO;
/*     */   public static final MLevel OFF;
/*     */   public static final MLevel SEVERE;
/*     */   public static final MLevel WARNING;
/*     */   private static final Map integersToMLevels;
/*     */   private static final Map namesToMLevels;
/*     */   private static final int ALL_INTVAL = -2147483648;
/*     */   private static final int CONFIG_INTVAL = 700;
/*     */   private static final int FINE_INTVAL = 500;
/*     */   private static final int FINER_INTVAL = 400;
/*     */   private static final int FINEST_INTVAL = 300;
/*     */   private static final int INFO_INTVAL = 800;
/*     */   private static final int OFF_INTVAL = 2147483647;
/*     */   private static final int SEVERE_INTVAL = 1000;
/*     */   private static final int WARNING_INTVAL = 900;
/*     */   Object level;
/*     */   int intval;
/*     */   String lvlstring;
/*     */   
/*     */   public static MLevel fromIntValue(int paramInt) {
/*  66 */     return (MLevel)integersToMLevels.get(new Integer(paramInt));
/*     */   }
/*     */   public static MLevel fromSeverity(String paramString) {
/*  69 */     return (MLevel)namesToMLevels.get(paramString);
/*     */   }
/*     */   
/*     */   static {
/*     */     Class clazz;
/*     */     boolean bool;
/*     */     MLevel mLevel1, mLevel2, mLevel3, mLevel4, mLevel5, mLevel6, mLevel7, mLevel8, mLevel9;
/*     */     try {
/*  77 */       clazz = Class.forName("java.util.logging.Level");
/*  78 */       bool = true;
/*     */     }
/*  80 */     catch (ClassNotFoundException classNotFoundException) {
/*     */       
/*  82 */       clazz = null;
/*  83 */       bool = false;
/*     */     } 
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
/*     */     try {
/*  99 */       mLevel1 = new MLevel(bool ? clazz.getField("ALL").get(null) : null, -2147483648, "ALL");
/* 100 */       mLevel2 = new MLevel(bool ? clazz.getField("CONFIG").get(null) : null, 700, "CONFIG");
/* 101 */       mLevel3 = new MLevel(bool ? clazz.getField("FINE").get(null) : null, 500, "FINE");
/* 102 */       mLevel4 = new MLevel(bool ? clazz.getField("FINER").get(null) : null, 400, "FINER");
/* 103 */       mLevel5 = new MLevel(bool ? clazz.getField("FINEST").get(null) : null, 300, "FINEST");
/* 104 */       mLevel6 = new MLevel(bool ? clazz.getField("INFO").get(null) : null, 800, "INFO");
/* 105 */       mLevel7 = new MLevel(bool ? clazz.getField("OFF").get(null) : null, 2147483647, "OFF");
/* 106 */       mLevel8 = new MLevel(bool ? clazz.getField("SEVERE").get(null) : null, 1000, "SEVERE");
/* 107 */       mLevel9 = new MLevel(bool ? clazz.getField("WARNING").get(null) : null, 900, "WARNING");
/*     */     }
/* 109 */     catch (Exception exception) {
/*     */       
/* 111 */       exception.printStackTrace();
/* 112 */       throw new InternalError("Huh? java.util.logging.Level is here, but not its expected public fields?");
/*     */     } 
/*     */     
/* 115 */     ALL = mLevel1;
/* 116 */     CONFIG = mLevel2;
/* 117 */     FINE = mLevel3;
/* 118 */     FINER = mLevel4;
/* 119 */     FINEST = mLevel5;
/* 120 */     INFO = mLevel6;
/* 121 */     OFF = mLevel7;
/* 122 */     SEVERE = mLevel8;
/* 123 */     WARNING = mLevel9;
/*     */     
/* 125 */     HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
/* 126 */     hashMap.put(new Integer(mLevel1.intValue()), mLevel1);
/* 127 */     hashMap.put(new Integer(mLevel2.intValue()), mLevel2);
/* 128 */     hashMap.put(new Integer(mLevel3.intValue()), mLevel3);
/* 129 */     hashMap.put(new Integer(mLevel4.intValue()), mLevel4);
/* 130 */     hashMap.put(new Integer(mLevel5.intValue()), mLevel5);
/* 131 */     hashMap.put(new Integer(mLevel6.intValue()), mLevel6);
/* 132 */     hashMap.put(new Integer(mLevel7.intValue()), mLevel7);
/* 133 */     hashMap.put(new Integer(mLevel8.intValue()), mLevel8);
/* 134 */     hashMap.put(new Integer(mLevel9.intValue()), mLevel9);
/*     */     
/* 136 */     integersToMLevels = Collections.unmodifiableMap(hashMap);
/*     */     
/* 138 */     hashMap = new HashMap<Object, Object>();
/* 139 */     hashMap.put(mLevel1.getSeverity(), mLevel1);
/* 140 */     hashMap.put(mLevel2.getSeverity(), mLevel2);
/* 141 */     hashMap.put(mLevel3.getSeverity(), mLevel3);
/* 142 */     hashMap.put(mLevel4.getSeverity(), mLevel4);
/* 143 */     hashMap.put(mLevel5.getSeverity(), mLevel5);
/* 144 */     hashMap.put(mLevel6.getSeverity(), mLevel6);
/* 145 */     hashMap.put(mLevel7.getSeverity(), mLevel7);
/* 146 */     hashMap.put(mLevel8.getSeverity(), mLevel8);
/* 147 */     hashMap.put(mLevel9.getSeverity(), mLevel9);
/*     */     
/* 149 */     namesToMLevels = Collections.unmodifiableMap(hashMap);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int intValue() {
/* 157 */     return this.intval;
/*     */   }
/*     */   public Object asJdk14Level() {
/* 160 */     return this.level;
/*     */   }
/*     */   public String getSeverity() {
/* 163 */     return this.lvlstring;
/*     */   }
/*     */   public String toString() {
/* 166 */     return getClass().getName() + getLineHeader();
/*     */   }
/*     */   public String getLineHeader() {
/* 169 */     return "[" + this.lvlstring + ']';
/*     */   }
/*     */   public boolean isLoggable(MLevel paramMLevel) {
/* 172 */     return (this.intval >= paramMLevel.intval);
/*     */   }
/*     */   
/*     */   private MLevel(Object paramObject, int paramInt, String paramString) {
/* 176 */     this.level = paramObject;
/* 177 */     this.intval = paramInt;
/* 178 */     this.lvlstring = paramString;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/log/MLevel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */