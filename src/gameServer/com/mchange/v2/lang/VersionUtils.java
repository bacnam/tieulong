/*     */ package com.mchange.v2.lang;
/*     */ 
/*     */ import com.mchange.v1.util.StringTokenizerUtils;
/*     */ import com.mchange.v2.log.MLevel;
/*     */ import com.mchange.v2.log.MLog;
/*     */ import com.mchange.v2.log.MLogger;
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
/*     */ public final class VersionUtils
/*     */ {
/*  43 */   private static final MLogger logger = MLog.getLogger(VersionUtils.class);
/*     */   
/*  45 */   private static final int[] DFLT_VERSION_ARRAY = new int[] { 1, 1 };
/*     */ 
/*     */   
/*     */   private static final int[] JDK_VERSION_ARRAY;
/*     */   
/*     */   private static final int JDK_VERSION;
/*     */ 
/*     */   
/*     */   static {
/*  54 */     String str = System.getProperty("java.version");
/*     */     
/*  56 */     if (str == null) {
/*     */       
/*  58 */       if (logger.isLoggable(MLevel.WARNING))
/*  59 */         logger.warning("Could not find java.version System property. Defaulting to JDK 1.1"); 
/*  60 */       arrayOfInt = DFLT_VERSION_ARRAY;
/*     */     } else {
/*     */       
/*     */       try {
/*  64 */         arrayOfInt = extractVersionNumberArray(str);
/*  65 */       } catch (NumberFormatException numberFormatException) {
/*     */         
/*  67 */         if (logger.isLoggable(MLevel.WARNING))
/*  68 */           logger.warning("java.version ''" + str + "'' could not be parsed. Defaulting to JDK 1.1."); 
/*  69 */         arrayOfInt = DFLT_VERSION_ARRAY;
/*     */       } 
/*     */     } 
/*  72 */     int i = 0;
/*  73 */     if (arrayOfInt.length > 0)
/*  74 */       i += arrayOfInt[0] * 10; 
/*  75 */     if (arrayOfInt.length > 1) {
/*  76 */       i += arrayOfInt[1];
/*     */     }
/*  78 */     JDK_VERSION_ARRAY = arrayOfInt;
/*  79 */     JDK_VERSION = i;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  86 */       String str1 = System.getProperty("sun.arch.data.model");
/*  87 */       if (str1 == null) {
/*  88 */         integer = null;
/*     */       } else {
/*  90 */         integer = new Integer(str1);
/*     */       } 
/*  92 */     } catch (Exception exception) {
/*     */       
/*  94 */       integer = null;
/*     */     } 
/*     */     
/*  97 */     if (integer == null || integer.intValue() == 32 || integer.intValue() == 64) {
/*  98 */       NUM_BITS = integer;
/*     */     } else {
/*     */       
/* 101 */       if (logger.isLoggable(MLevel.WARNING)) {
/* 102 */         logger.warning("Determined a surprising jvmNumerOfBits: " + integer + ". Setting jvmNumberOfBits to unknown (null).");
/*     */       }
/* 104 */       NUM_BITS = null;
/*     */     } 
/*     */   }
/*     */   private static final Integer NUM_BITS;
/*     */   static {
/*     */     int[] arrayOfInt;
/*     */     Integer integer;
/*     */   }
/*     */   
/*     */   public static Integer jvmNumberOfBits() {
/* 114 */     return NUM_BITS;
/*     */   }
/*     */   public static boolean isJavaVersion10() {
/* 117 */     return (JDK_VERSION == 10);
/*     */   }
/*     */   public static boolean isJavaVersion11() {
/* 120 */     return (JDK_VERSION == 11);
/*     */   }
/*     */   public static boolean isJavaVersion12() {
/* 123 */     return (JDK_VERSION == 12);
/*     */   }
/*     */   public static boolean isJavaVersion13() {
/* 126 */     return (JDK_VERSION == 13);
/*     */   }
/*     */   public static boolean isJavaVersion14() {
/* 129 */     return (JDK_VERSION == 14);
/*     */   }
/*     */   public static boolean isJavaVersion15() {
/* 132 */     return (JDK_VERSION == 15);
/*     */   }
/*     */   public static boolean isAtLeastJavaVersion10() {
/* 135 */     return (JDK_VERSION >= 10);
/*     */   }
/*     */   public static boolean isAtLeastJavaVersion11() {
/* 138 */     return (JDK_VERSION >= 11);
/*     */   }
/*     */   public static boolean isAtLeastJavaVersion12() {
/* 141 */     return (JDK_VERSION >= 12);
/*     */   }
/*     */   public static boolean isAtLeastJavaVersion13() {
/* 144 */     return (JDK_VERSION >= 13);
/*     */   }
/*     */   public static boolean isAtLeastJavaVersion14() {
/* 147 */     return (JDK_VERSION >= 14);
/*     */   }
/*     */   public static boolean isAtLeastJavaVersion15() {
/* 150 */     return (JDK_VERSION >= 15);
/*     */   }
/*     */   public static boolean isAtLeastJavaVersion16() {
/* 153 */     return (JDK_VERSION >= 16);
/*     */   }
/*     */   public static boolean isAtLeastJavaVersion17() {
/* 156 */     return (JDK_VERSION >= 17);
/*     */   }
/*     */   
/*     */   public static int[] extractVersionNumberArray(String paramString) throws NumberFormatException {
/* 160 */     return extractVersionNumberArray(paramString, paramString.split("\\D+"));
/*     */   }
/*     */ 
/*     */   
/*     */   public static int[] extractVersionNumberArray(String paramString1, String paramString2) throws NumberFormatException {
/* 165 */     String[] arrayOfString = StringTokenizerUtils.tokenizeToArray(paramString1, paramString2, false);
/* 166 */     return extractVersionNumberArray(paramString1, arrayOfString);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int[] extractVersionNumberArray(String paramString, String[] paramArrayOfString) throws NumberFormatException {
/* 172 */     int i = paramArrayOfString.length;
/* 173 */     int[] arrayOfInt = new int[i];
/* 174 */     for (byte b = 0; b < i; b++) {
/*     */ 
/*     */       
/*     */       try {
/* 178 */         arrayOfInt[b] = Integer.parseInt(paramArrayOfString[b]);
/*     */       }
/* 180 */       catch (NumberFormatException numberFormatException) {
/*     */         
/* 182 */         if (b <= 1) {
/* 183 */           throw numberFormatException;
/*     */         }
/*     */         
/* 186 */         if (logger.isLoggable(MLevel.INFO)) {
/* 187 */           logger.log(MLevel.INFO, "JVM version string (" + paramString + ") contains non-integral component (" + paramArrayOfString[b] + "). Using precending components only to resolve JVM version.");
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 193 */         int[] arrayOfInt1 = new int[b];
/* 194 */         System.arraycopy(arrayOfInt, 0, arrayOfInt1, 0, b);
/* 195 */         arrayOfInt = arrayOfInt1;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 200 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean prefixMatches(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 205 */     if (paramArrayOfint1.length > paramArrayOfint2.length)
/* 206 */       return false; 
/*     */     byte b;
/*     */     int i;
/* 209 */     for (b = 0, i = paramArrayOfint1.length; b < i; b++) {
/* 210 */       if (paramArrayOfint1[b] != paramArrayOfint2[b])
/* 211 */         return false; 
/* 212 */     }  return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int lexicalCompareVersionNumberArrays(int[] paramArrayOfint1, int[] paramArrayOfint2) {
/* 218 */     int i = paramArrayOfint1.length;
/* 219 */     int j = paramArrayOfint2.length;
/* 220 */     for (byte b = 0; b < i; b++) {
/*     */       
/* 222 */       if (b == j)
/* 223 */         return 1; 
/* 224 */       if (paramArrayOfint1[b] > paramArrayOfint2[b])
/* 225 */         return 1; 
/* 226 */       if (paramArrayOfint1[b] < paramArrayOfint2[b])
/* 227 */         return -1; 
/*     */     } 
/* 229 */     if (j > i) {
/* 230 */       return -1;
/*     */     }
/* 232 */     return 0;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/lang/VersionUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */