/*     */ package com.mchange.v2.c3p0.cfg;
/*     */ 
/*     */ import com.mchange.v2.c3p0.impl.C3P0Defaults;
/*     */ import com.mchange.v2.lang.Coerce;
/*     */ import com.mchange.v2.log.MLevel;
/*     */ import com.mchange.v2.log.MLog;
/*     */ import com.mchange.v2.log.MLogger;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
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
/*     */ public final class C3P0ConfigUtils
/*     */ {
/*     */   public static final String PROPS_FILE_RSRC_PATH = "/c3p0.properties";
/*     */   public static final String PROPS_FILE_PROP_PFX = "c3p0.";
/*     */   public static final int PROPS_FILE_PROP_PFX_LEN = 5;
/*  53 */   private static final String[] MISSPELL_PFXS = new String[] { "/c3pO", "/c3po", "/C3P0", "/C3PO" };
/*     */   
/*  55 */   static final MLogger logger = MLog.getLogger(C3P0ConfigUtils.class);
/*     */ 
/*     */   
/*     */   static {
/*  59 */     if (logger.isLoggable(MLevel.WARNING) && C3P0ConfigUtils.class.getResource("/c3p0.properties") == null)
/*     */     {
/*     */       
/*  62 */       for (int i = 0; i < MISSPELL_PFXS.length; i++) {
/*     */         
/*  64 */         String test = MISSPELL_PFXS[i] + ".properties";
/*  65 */         if (C3P0ConfigUtils.class.getResource(MISSPELL_PFXS[i] + ".properties") != null) {
/*     */           
/*  67 */           logger.warning("POSSIBLY MISSPELLED c3p0.properties CONFIG RESOURCE FOUND. Please ensure the file name is c3p0.properties, all lower case, with the digit 0 (NOT the letter O) in c3p0. It should be placed  in the top level of c3p0's effective classpath.");
/*     */           break;
/*     */         } 
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
/*     */   public static HashMap extractHardcodedC3P0Defaults(boolean stringify_coercibles) {
/*  86 */     HashMap<Object, Object> out = new HashMap<Object, Object>();
/*     */ 
/*     */     
/*     */     try {
/*  90 */       Method[] methods = C3P0Defaults.class.getMethods();
/*  91 */       for (int i = 0, len = methods.length; i < len; i++) {
/*     */         
/*  93 */         Method m = methods[i];
/*  94 */         int mods = m.getModifiers();
/*  95 */         if ((mods & 0x1) != 0 && (mods & 0x8) != 0 && (m.getParameterTypes()).length == 0) {
/*     */           
/*  97 */           Object val = m.invoke(null, null);
/*  98 */           if (val != null) {
/*  99 */             out.put(m.getName(), (stringify_coercibles && Coerce.canCoerce(val)) ? String.valueOf(val) : val);
/*     */           }
/*     */         } 
/*     */       } 
/* 103 */     } catch (Exception e) {
/*     */       
/* 105 */       logger.log(MLevel.WARNING, "Failed to extract hardcoded default config!?", e);
/*     */     } 
/*     */     
/* 108 */     return out;
/*     */   }
/*     */   
/*     */   public static HashMap extractHardcodedC3P0Defaults() {
/* 112 */     return extractHardcodedC3P0Defaults(true);
/*     */   }
/*     */   
/*     */   public static HashMap extractC3P0PropertiesResources() {
/* 116 */     HashMap<Object, Object> out = new HashMap<Object, Object>();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 121 */     Properties props = findAllOneLevelC3P0Properties();
/* 122 */     for (Iterator<String> ii = props.keySet().iterator(); ii.hasNext(); ) {
/*     */       
/* 124 */       String key = ii.next();
/* 125 */       String val = (String)props.get(key);
/* 126 */       if (key.startsWith("c3p0.")) {
/* 127 */         out.put(key.substring(5).trim(), val.trim());
/*     */       }
/*     */     } 
/* 130 */     return out;
/*     */   }
/*     */ 
/*     */   
/*     */   public static C3P0Config configFromFlatDefaults(HashMap flatDefaults) {
/* 135 */     NamedScope defaults = new NamedScope();
/* 136 */     defaults.props.putAll(flatDefaults);
/*     */     
/* 138 */     HashMap<Object, Object> configNamesToNamedScopes = new HashMap<Object, Object>();
/*     */     
/* 140 */     return new C3P0Config(defaults, configNamesToNamedScopes);
/*     */   }
/*     */   
/*     */   public static String getPropsFileConfigProperty(String prop) {
/* 144 */     return C3P0Config.getPropsFileConfigProperty(prop);
/*     */   }
/*     */   public static Properties findResourceProperties() {
/* 147 */     return C3P0Config.findResourceProperties();
/*     */   }
/*     */   private static Properties findAllOneLevelC3P0Properties() {
/* 150 */     return C3P0Config.findAllOneLevelC3P0Properties();
/*     */   }
/*     */   
/*     */   static Properties findAllC3P0SystemProperties() {
/* 154 */     Properties out = new Properties();
/*     */ 
/*     */     
/*     */     try {
/* 158 */       for (Iterator<String> ii = C3P0Defaults.getKnownProperties(null).iterator(); ii.hasNext(); ) {
/*     */         
/* 160 */         String key = ii.next();
/* 161 */         String prefixedKey = "c3p0." + key;
/* 162 */         String value = System.getProperty(prefixedKey);
/* 163 */         if (value != null && value.trim().length() > 0) {
/* 164 */           out.put(key, value);
/*     */         }
/*     */       } 
/* 167 */     } catch (SecurityException e) {
/*     */       
/* 169 */       if (logger.isLoggable(MLevel.WARNING)) {
/* 170 */         logger.log(MLevel.WARNING, "A SecurityException occurred while trying to read c3p0 System properties. c3p0 configuration set via System properties may be ignored!", e);
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 176 */     return out;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object extractUserOverride(String propName, String userName, Map userOverrides) {
/* 184 */     Map specificUserOverrides = (Map)userOverrides.get(userName);
/* 185 */     if (specificUserOverrides != null) {
/* 186 */       return specificUserOverrides.get(propName);
/*     */     }
/* 188 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Boolean extractBooleanOverride(String propName, String userName, Map userOverrides) {
/* 193 */     Object check = extractUserOverride(propName, userName, userOverrides);
/* 194 */     if (check == null || check instanceof Boolean)
/* 195 */       return (Boolean)check; 
/* 196 */     if (check instanceof String) {
/* 197 */       return Boolean.valueOf((String)check);
/*     */     }
/* 199 */     throw new ClassCastException("Parameter '" + propName + "' as overridden for user '" + userName + "' is " + check + ", which cannot be converted to Boolean.");
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/cfg/C3P0ConfigUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */