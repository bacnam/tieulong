/*     */ package org.slf4j;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.util.Arrays;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.slf4j.helpers.NOPLoggerFactory;
/*     */ import org.slf4j.helpers.SubstituteLogger;
/*     */ import org.slf4j.helpers.SubstituteLoggerFactory;
/*     */ import org.slf4j.helpers.Util;
/*     */ import org.slf4j.impl.StaticLoggerBinder;
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
/*     */ public final class LoggerFactory
/*     */ {
/*     */   static final String CODES_PREFIX = "http://www.slf4j.org/codes.html";
/*     */   static final String NO_STATICLOGGERBINDER_URL = "http://www.slf4j.org/codes.html#StaticLoggerBinder";
/*     */   static final String MULTIPLE_BINDINGS_URL = "http://www.slf4j.org/codes.html#multiple_bindings";
/*     */   static final String NULL_LF_URL = "http://www.slf4j.org/codes.html#null_LF";
/*     */   static final String VERSION_MISMATCH = "http://www.slf4j.org/codes.html#version_mismatch";
/*     */   static final String SUBSTITUTE_LOGGER_URL = "http://www.slf4j.org/codes.html#substituteLogger";
/*     */   static final String LOGGER_NAME_MISMATCH_URL = "http://www.slf4j.org/codes.html#loggerNameMismatch";
/*     */   static final String UNSUCCESSFUL_INIT_URL = "http://www.slf4j.org/codes.html#unsuccessfulInit";
/*     */   static final String UNSUCCESSFUL_INIT_MSG = "org.slf4j.LoggerFactory could not be successfully initialized. See also http://www.slf4j.org/codes.html#unsuccessfulInit";
/*     */   static final int UNINITIALIZED = 0;
/*     */   static final int ONGOING_INITIALIZATION = 1;
/*     */   static final int FAILED_INITIALIZATION = 2;
/*     */   static final int SUCCESSFUL_INITIALIZATION = 3;
/*     */   static final int NOP_FALLBACK_INITIALIZATION = 4;
/*  82 */   static int INITIALIZATION_STATE = 0;
/*  83 */   static SubstituteLoggerFactory TEMP_FACTORY = new SubstituteLoggerFactory();
/*  84 */   static NOPLoggerFactory NOP_FALLBACK_FACTORY = new NOPLoggerFactory();
/*     */   
/*     */   static final String DETECT_LOGGER_NAME_MISMATCH_PROPERTY = "slf4j.detectLoggerNameMismatch";
/*     */   
/*  88 */   static boolean DETECT_LOGGER_NAME_MISMATCH = Boolean.getBoolean("slf4j.detectLoggerNameMismatch");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  97 */   private static final String[] API_COMPATIBILITY_LIST = new String[] { "1.6", "1.7" };
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
/*     */   static void reset() {
/* 115 */     INITIALIZATION_STATE = 0;
/* 116 */     TEMP_FACTORY = new SubstituteLoggerFactory();
/*     */   }
/*     */   
/*     */   private static final void performInitialization() {
/* 120 */     bind();
/* 121 */     if (INITIALIZATION_STATE == 3) {
/* 122 */       versionSanityCheck();
/*     */     }
/*     */   }
/*     */   
/*     */   private static boolean messageContainsOrgSlf4jImplStaticLoggerBinder(String msg) {
/* 127 */     if (msg == null)
/* 128 */       return false; 
/* 129 */     if (msg.indexOf("org/slf4j/impl/StaticLoggerBinder") != -1)
/* 130 */       return true; 
/* 131 */     if (msg.indexOf("org.slf4j.impl.StaticLoggerBinder") != -1)
/* 132 */       return true; 
/* 133 */     return false;
/*     */   }
/*     */   
/*     */   private static final void bind() {
/*     */     try {
/* 138 */       Set<URL> staticLoggerBinderPathSet = findPossibleStaticLoggerBinderPathSet();
/* 139 */       reportMultipleBindingAmbiguity(staticLoggerBinderPathSet);
/*     */       
/* 141 */       StaticLoggerBinder.getSingleton();
/* 142 */       INITIALIZATION_STATE = 3;
/* 143 */       reportActualBinding(staticLoggerBinderPathSet);
/* 144 */       fixSubstitutedLoggers();
/* 145 */     } catch (NoClassDefFoundError ncde) {
/* 146 */       String msg = ncde.getMessage();
/* 147 */       if (messageContainsOrgSlf4jImplStaticLoggerBinder(msg)) {
/* 148 */         INITIALIZATION_STATE = 4;
/* 149 */         Util.report("Failed to load class \"org.slf4j.impl.StaticLoggerBinder\".");
/* 150 */         Util.report("Defaulting to no-operation (NOP) logger implementation");
/* 151 */         Util.report("See http://www.slf4j.org/codes.html#StaticLoggerBinder for further details.");
/*     */       } else {
/* 153 */         failedBinding(ncde);
/* 154 */         throw ncde;
/*     */       } 
/* 156 */     } catch (NoSuchMethodError nsme) {
/* 157 */       String msg = nsme.getMessage();
/* 158 */       if (msg != null && msg.indexOf("org.slf4j.impl.StaticLoggerBinder.getSingleton()") != -1) {
/* 159 */         INITIALIZATION_STATE = 2;
/* 160 */         Util.report("slf4j-api 1.6.x (or later) is incompatible with this binding.");
/* 161 */         Util.report("Your binding is version 1.5.5 or earlier.");
/* 162 */         Util.report("Upgrade your binding to version 1.6.x.");
/*     */       } 
/* 164 */       throw nsme;
/* 165 */     } catch (Exception e) {
/* 166 */       failedBinding(e);
/* 167 */       throw new IllegalStateException("Unexpected initialization failure", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   static void failedBinding(Throwable t) {
/* 172 */     INITIALIZATION_STATE = 2;
/* 173 */     Util.report("Failed to instantiate SLF4J LoggerFactory", t);
/*     */   }
/*     */   
/*     */   private static final void fixSubstitutedLoggers() {
/* 177 */     List<SubstituteLogger> loggers = TEMP_FACTORY.getLoggers();
/*     */     
/* 179 */     if (loggers.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/* 183 */     Util.report("The following set of substitute loggers may have been accessed");
/* 184 */     Util.report("during the initialization phase. Logging calls during this");
/* 185 */     Util.report("phase were not honored. However, subsequent logging calls to these");
/* 186 */     Util.report("loggers will work as normally expected.");
/* 187 */     Util.report("See also http://www.slf4j.org/codes.html#substituteLogger");
/* 188 */     for (SubstituteLogger subLogger : loggers) {
/* 189 */       subLogger.setDelegate(getLogger(subLogger.getName()));
/* 190 */       Util.report(subLogger.getName());
/*     */     } 
/*     */     
/* 193 */     TEMP_FACTORY.clear();
/*     */   }
/*     */   
/*     */   private static final void versionSanityCheck() {
/*     */     try {
/* 198 */       String requested = StaticLoggerBinder.REQUESTED_API_VERSION;
/*     */       
/* 200 */       boolean match = false;
/* 201 */       for (int i = 0; i < API_COMPATIBILITY_LIST.length; i++) {
/* 202 */         if (requested.startsWith(API_COMPATIBILITY_LIST[i])) {
/* 203 */           match = true;
/*     */         }
/*     */       } 
/* 206 */       if (!match) {
/* 207 */         Util.report("The requested version " + requested + " by your slf4j binding is not compatible with " + Arrays.<String>asList(API_COMPATIBILITY_LIST).toString());
/*     */         
/* 209 */         Util.report("See http://www.slf4j.org/codes.html#version_mismatch for further details.");
/*     */       } 
/* 211 */     } catch (NoSuchFieldError nsfe) {
/*     */ 
/*     */ 
/*     */     
/*     */     }
/* 216 */     catch (Throwable e) {
/*     */       
/* 218 */       Util.report("Unexpected problem occured during version sanity check", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 224 */   private static String STATIC_LOGGER_BINDER_PATH = "org/slf4j/impl/StaticLoggerBinder.class";
/*     */ 
/*     */ 
/*     */   
/*     */   private static Set<URL> findPossibleStaticLoggerBinderPathSet() {
/* 229 */     Set<URL> staticLoggerBinderPathSet = new LinkedHashSet<URL>(); try {
/*     */       Enumeration<URL> paths;
/* 231 */       ClassLoader loggerFactoryClassLoader = LoggerFactory.class.getClassLoader();
/*     */       
/* 233 */       if (loggerFactoryClassLoader == null) {
/* 234 */         paths = ClassLoader.getSystemResources(STATIC_LOGGER_BINDER_PATH);
/*     */       } else {
/* 236 */         paths = loggerFactoryClassLoader.getResources(STATIC_LOGGER_BINDER_PATH);
/*     */       } 
/* 238 */       while (paths.hasMoreElements()) {
/* 239 */         URL path = paths.nextElement();
/* 240 */         staticLoggerBinderPathSet.add(path);
/*     */       } 
/* 242 */     } catch (IOException ioe) {
/* 243 */       Util.report("Error getting resources from path", ioe);
/*     */     } 
/* 245 */     return staticLoggerBinderPathSet;
/*     */   }
/*     */   
/*     */   private static boolean isAmbiguousStaticLoggerBinderPathSet(Set<URL> staticLoggerBinderPathSet) {
/* 249 */     return (staticLoggerBinderPathSet.size() > 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void reportMultipleBindingAmbiguity(Set<URL> staticLoggerBinderPathSet) {
/* 258 */     if (isAmbiguousStaticLoggerBinderPathSet(staticLoggerBinderPathSet)) {
/* 259 */       Util.report("Class path contains multiple SLF4J bindings.");
/* 260 */       Iterator<URL> iterator = staticLoggerBinderPathSet.iterator();
/* 261 */       while (iterator.hasNext()) {
/* 262 */         URL path = iterator.next();
/* 263 */         Util.report("Found binding in [" + path + "]");
/*     */       } 
/* 265 */       Util.report("See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.");
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void reportActualBinding(Set<URL> staticLoggerBinderPathSet) {
/* 270 */     if (isAmbiguousStaticLoggerBinderPathSet(staticLoggerBinderPathSet)) {
/* 271 */       Util.report("Actual binding is of type [" + StaticLoggerBinder.getSingleton().getLoggerFactoryClassStr() + "]");
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
/*     */   public static Logger getLogger(String name) {
/* 283 */     ILoggerFactory iLoggerFactory = getILoggerFactory();
/* 284 */     return iLoggerFactory.getLogger(name);
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static Logger getLogger(Class<?> clazz) {
/* 304 */     Logger logger = getLogger(clazz.getName());
/* 305 */     if (DETECT_LOGGER_NAME_MISMATCH) {
/* 306 */       Class<?> autoComputedCallingClass = Util.getCallingClass();
/* 307 */       if (nonMatchingClasses(clazz, autoComputedCallingClass)) {
/* 308 */         Util.report(String.format("Detected logger name mismatch. Given name: \"%s\"; computed name: \"%s\".", new Object[] { logger.getName(), autoComputedCallingClass.getName() }));
/*     */         
/* 310 */         Util.report("See http://www.slf4j.org/codes.html#loggerNameMismatch for an explanation");
/*     */       } 
/*     */     } 
/* 313 */     return logger;
/*     */   }
/*     */   
/*     */   private static boolean nonMatchingClasses(Class<?> clazz, Class<?> autoComputedCallingClass) {
/* 317 */     return !autoComputedCallingClass.isAssignableFrom(clazz);
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
/*     */   public static ILoggerFactory getILoggerFactory() {
/* 329 */     if (INITIALIZATION_STATE == 0) {
/* 330 */       INITIALIZATION_STATE = 1;
/* 331 */       performInitialization();
/*     */     } 
/* 333 */     switch (INITIALIZATION_STATE) {
/*     */       case 3:
/* 335 */         return StaticLoggerBinder.getSingleton().getLoggerFactory();
/*     */       case 4:
/* 337 */         return (ILoggerFactory)NOP_FALLBACK_FACTORY;
/*     */       case 2:
/* 339 */         throw new IllegalStateException("org.slf4j.LoggerFactory could not be successfully initialized. See also http://www.slf4j.org/codes.html#unsuccessfulInit");
/*     */ 
/*     */       
/*     */       case 1:
/* 343 */         return (ILoggerFactory)TEMP_FACTORY;
/*     */     } 
/* 345 */     throw new IllegalStateException("Unreachable code");
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/slf4j/LoggerFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */