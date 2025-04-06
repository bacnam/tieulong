/*     */ package com.mchange.v2.c3p0.cfg;
/*     */ 
/*     */ import com.mchange.v1.lang.BooleanUtils;
/*     */ import com.mchange.v2.beans.BeansUtils;
/*     */ import com.mchange.v2.c3p0.C3P0Registry;
/*     */ import com.mchange.v2.c3p0.impl.C3P0Defaults;
/*     */ import com.mchange.v2.c3p0.impl.C3P0ImplUtils;
/*     */ import com.mchange.v2.cfg.MConfig;
/*     */ import com.mchange.v2.cfg.MultiPropertiesConfig;
/*     */ import com.mchange.v2.log.MLevel;
/*     */ import com.mchange.v2.log.MLog;
/*     */ import com.mchange.v2.log.MLogger;
/*     */ import java.beans.IntrospectionException;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
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
/*     */ public final class C3P0Config
/*     */ {
/*     */   static final String PROP_STYLE_NAMED_CFG_PFX = "c3p0.named-configs";
/*  55 */   static final int PROP_STYLE_NAMED_CFG_PFX_LEN = "c3p0.named-configs".length();
/*     */   static final String PROP_STYLE_USER_OVERRIDES_PART = "user-overrides";
/*     */   static final String PROP_STYLE_USER_OVERRIDES_PFX = "c3p0.user-overrides";
/*  58 */   static final int PROP_STYLE_USER_OVERRIDES_PFX_LEN = "c3p0.user-overrides".length();
/*     */   static final String PROP_STYLE_EXTENSIONS_PART = "extensions";
/*     */   static final String PROP_STYLE_EXTENSIONS_PFX = "c3p0.extensions";
/*  61 */   static final int PROP_STYLE_EXTENSIONS_PFX_LEN = "c3p0.extensions".length();
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String CFG_FINDER_CLASSNAME_KEY = "com.mchange.v2.c3p0.cfg.finder";
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String DEFAULT_CONFIG_NAME = "default";
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String PROPS_FILE_RSRC_PATH = "/c3p0.properties";
/*     */ 
/*     */ 
/*     */   
/*     */   private static synchronized MultiPropertiesConfig MPCONFIG() {
/*  78 */     return _MPCONFIG;
/*     */   }
/*     */   private static synchronized C3P0Config MAIN() {
/*  81 */     return _MAIN;
/*     */   }
/*     */   private static synchronized void setLibraryMultiPropertiesConfig(MultiPropertiesConfig mpc) {
/*  84 */     _MPCONFIG = mpc;
/*     */   }
/*     */   public static Properties allCurrentProperties() {
/*  87 */     return MPCONFIG().getPropertiesByPrefix("");
/*     */   }
/*     */   public static synchronized void setMainConfig(C3P0Config protoMain) {
/*  90 */     _MAIN = protoMain;
/*     */   }
/*     */   public static synchronized void refreshMainConfig() {
/*  93 */     refreshMainConfig(null, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public static synchronized void refreshMainConfig(MultiPropertiesConfig[] overrides, String overridesDescription) {
/*  98 */     MultiPropertiesConfig libMpc = findLibraryMultiPropertiesConfig();
/*  99 */     if (overrides != null) {
/*     */       
/* 101 */       int olen = overrides.length;
/* 102 */       MultiPropertiesConfig[] combineMe = new MultiPropertiesConfig[olen + 1];
/* 103 */       combineMe[0] = libMpc;
/* 104 */       for (int i = 0; i < olen; i++) {
/* 105 */         combineMe[i + 1] = overrides[i];
/*     */       }
/* 107 */       MultiPropertiesConfig overriddenMpc = MConfig.combine(combineMe);
/* 108 */       setLibraryMultiPropertiesConfig(overriddenMpc);
/* 109 */       setMainConfig(findLibraryC3P0Config(true));
/*     */       
/* 111 */       if (logger.isLoggable(MLevel.INFO)) {
/* 112 */         logger.log(MLevel.INFO, "c3p0 main configuration was refreshed, with overrides specified" + ((overridesDescription == null) ? "." : (" - " + overridesDescription)));
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 117 */       setLibraryMultiPropertiesConfig(libMpc);
/* 118 */       setMainConfig(findLibraryC3P0Config(false));
/*     */       
/* 120 */       if (logger.isLoggable(MLevel.INFO)) {
/* 121 */         logger.log(MLevel.INFO, "c3p0 main configuration was refreshed, with no overrides specified (and any previous overrides removed).");
/*     */       }
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
/* 133 */     C3P0Registry.markConfigRefreshed();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 138 */   static final MLogger logger = MLog.getLogger(C3P0Config.class);
/*     */   private static MultiPropertiesConfig _MPCONFIG;
/*     */   private static C3P0Config _MAIN;
/*     */   
/*     */   static {
/* 143 */     setLibraryMultiPropertiesConfig(findLibraryMultiPropertiesConfig());
/* 144 */     setMainConfig(findLibraryC3P0Config(false));
/*     */     
/* 146 */     warnOnUnknownProperties(MAIN());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static MultiPropertiesConfig findLibraryMultiPropertiesConfig() {
/* 154 */     String[] defaults = { "/mchange-commons.properties", "/mchange-log.properties" };
/* 155 */     String[] preempts = { "hocon:/reference,/application,/c3p0,/", "/c3p0.properties", "/" };
/*     */     
/* 157 */     return MConfig.readVmConfig(defaults, preempts);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static C3P0Config findLibraryC3P0Config(boolean warn_on_conflicting_overrides) {
/*     */     C3P0Config c3P0Config;
/* 165 */     String cname = MPCONFIG().getProperty("com.mchange.v2.c3p0.cfg.finder");
/*     */     
/* 167 */     C3P0ConfigFinder cfgFinder = null;
/*     */     
/*     */     try {
/* 170 */       if (cname != null) {
/* 171 */         cfgFinder = (C3P0ConfigFinder)Class.forName(cname).newInstance();
/*     */       }
/*     */     }
/* 174 */     catch (Exception e) {
/*     */       
/* 176 */       if (logger.isLoggable(MLevel.WARNING)) {
/* 177 */         logger.log(MLevel.WARNING, "Could not load specified C3P0ConfigFinder class'" + cname + "'.", e);
/*     */       }
/*     */     } 
/*     */     
/*     */     try {
/* 182 */       if (cfgFinder == null) {
/*     */         
/* 184 */         Class.forName("org.w3c.dom.Node");
/* 185 */         Class.forName("com.mchange.v2.c3p0.cfg.C3P0ConfigXmlUtils");
/* 186 */         cfgFinder = new DefaultC3P0ConfigFinder(warn_on_conflicting_overrides);
/*     */       } 
/* 188 */       c3P0Config = cfgFinder.findConfig();
/*     */     }
/* 190 */     catch (Exception e) {
/*     */ 
/*     */       
/* 193 */       if (logger.isLoggable(MLevel.WARNING)) {
/* 194 */         logger.log(MLevel.WARNING, "XML configuration disabled! Verify that standard XML libs are available.", e);
/*     */       }
/* 196 */       HashMap flatDefaults = C3P0ConfigUtils.extractHardcodedC3P0Defaults();
/* 197 */       flatDefaults.putAll(C3P0ConfigUtils.extractC3P0PropertiesResources());
/* 198 */       c3P0Config = C3P0ConfigUtils.configFromFlatDefaults(flatDefaults);
/*     */     } 
/*     */     
/* 201 */     HashMap propStyleConfigNamesToNamedScopes = findPropStyleNamedScopes();
/* 202 */     HashMap cfgFoundConfigNamesToNamedScopes = c3P0Config.configNamesToNamedScopes;
/* 203 */     HashMap<Object, Object> mergedConfigNamesToNamedScopes = new HashMap<Object, Object>();
/*     */     
/* 205 */     HashSet allConfigNames = new HashSet(cfgFoundConfigNamesToNamedScopes.keySet());
/* 206 */     allConfigNames.addAll(propStyleConfigNamesToNamedScopes.keySet());
/*     */     
/* 208 */     for (Iterator<String> ii = allConfigNames.iterator(); ii.hasNext(); ) {
/*     */       
/* 210 */       String cfgName = ii.next();
/* 211 */       NamedScope cfgFound = (NamedScope)cfgFoundConfigNamesToNamedScopes.get(cfgName);
/* 212 */       NamedScope propStyle = (NamedScope)propStyleConfigNamesToNamedScopes.get(cfgName);
/* 213 */       if (cfgFound != null && propStyle != null) {
/* 214 */         mergedConfigNamesToNamedScopes.put(cfgName, cfgFound.mergedOver(propStyle)); continue;
/* 215 */       }  if (cfgFound != null && propStyle == null) {
/* 216 */         mergedConfigNamesToNamedScopes.put(cfgName, cfgFound); continue;
/* 217 */       }  if (cfgFound == null && propStyle != null) {
/* 218 */         mergedConfigNamesToNamedScopes.put(cfgName, propStyle); continue;
/*     */       } 
/* 220 */       throw new AssertionError("Huh? allConfigNames is the union, every name should be in one of the two maps.");
/*     */     } 
/*     */     
/* 223 */     HashMap propStyleUserOverridesDefaultConfig = findPropStyleUserOverridesDefaultConfig();
/* 224 */     HashMap propStyleExtensionsDefaultConfig = findPropStyleExtensionsDefaultConfig();
/* 225 */     NamedScope mergedDefaultConfig = new NamedScope(c3P0Config.defaultConfig.props, NamedScope.mergeUserNamesToOverrides(c3P0Config.defaultConfig.userNamesToOverrides, propStyleUserOverridesDefaultConfig), NamedScope.mergeExtensions(c3P0Config.defaultConfig.extensions, propStyleExtensionsDefaultConfig));
/*     */ 
/*     */ 
/*     */     
/* 229 */     return new C3P0Config(mergedDefaultConfig, mergedConfigNamesToNamedScopes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void warnOnUnknownProperties(C3P0Config cfg) {
/* 236 */     warnOnUnknownProperties(cfg.defaultConfig);
/* 237 */     for (Iterator<NamedScope> ii = cfg.configNamesToNamedScopes.values().iterator(); ii.hasNext();) {
/* 238 */       warnOnUnknownProperties(ii.next());
/*     */     }
/*     */   }
/*     */   
/*     */   private static void warnOnUnknownProperties(NamedScope scope) {
/* 243 */     warnOnUnknownProperties(scope.props);
/* 244 */     for (Iterator<Map> ii = scope.userNamesToOverrides.values().iterator(); ii.hasNext();) {
/* 245 */       warnOnUnknownProperties(ii.next());
/*     */     }
/*     */   }
/*     */   
/*     */   private static void warnOnUnknownProperties(Map propMap) {
/* 250 */     for (Iterator<String> ii = propMap.keySet().iterator(); ii.hasNext(); ) {
/*     */       
/* 252 */       String prop = ii.next();
/* 253 */       if (!C3P0Defaults.isKnownProperty(prop) && logger.isLoggable(MLevel.WARNING))
/* 254 */         logger.log(MLevel.WARNING, "Unknown c3p0-config property: " + prop); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String getPropsFileConfigProperty(String prop) {
/* 259 */     return MPCONFIG().getProperty(prop);
/*     */   }
/*     */   static Properties findResourceProperties() {
/* 262 */     return MPCONFIG().getPropertiesByResourcePath("/c3p0.properties");
/*     */   }
/*     */   
/*     */   static Properties findAllOneLevelC3P0Properties() {
/* 266 */     Properties out = MPCONFIG().getPropertiesByPrefix("c3p0");
/* 267 */     for (Iterator ii = out.keySet().iterator(); ii.hasNext();) {
/* 268 */       if (((String)ii.next()).lastIndexOf('.') > 4) ii.remove(); 
/* 269 */     }  return out;
/*     */   }
/*     */ 
/*     */   
/*     */   static HashMap findPropStyleUserOverridesDefaultConfig() {
/* 274 */     HashMap<Object, Object> userNamesToOverrides = new HashMap<Object, Object>();
/*     */     
/* 276 */     Properties props = MPCONFIG().getPropertiesByPrefix("c3p0.user-overrides");
/* 277 */     for (Iterator<String> ii = props.keySet().iterator(); ii.hasNext(); ) {
/*     */       
/* 279 */       String fullKey = ii.next();
/* 280 */       String userProp = fullKey.substring(PROP_STYLE_USER_OVERRIDES_PFX_LEN + 1);
/* 281 */       int dot_index = userProp.indexOf('.');
/* 282 */       if (dot_index < 0) {
/*     */         
/* 284 */         if (logger.isLoggable(MLevel.WARNING)) {
/* 285 */           logger.log(MLevel.WARNING, "Bad specification of user-override property '" + fullKey + "', propfile key should look like '" + "c3p0.user-overrides" + ".<user>.<property>'. Ignoring.");
/*     */         }
/*     */ 
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 292 */       String user = userProp.substring(0, dot_index);
/* 293 */       String propName = userProp.substring(dot_index + 1);
/*     */       
/* 295 */       HashMap<Object, Object> userOverridesMap = (HashMap)userNamesToOverrides.get(user);
/* 296 */       if (userOverridesMap == null) {
/*     */         
/* 298 */         userOverridesMap = new HashMap<Object, Object>();
/* 299 */         userNamesToOverrides.put(user, userOverridesMap);
/*     */       } 
/* 301 */       userOverridesMap.put(propName, props.get(fullKey));
/*     */     } 
/*     */     
/* 304 */     return userNamesToOverrides;
/*     */   }
/*     */ 
/*     */   
/*     */   static HashMap findPropStyleExtensionsDefaultConfig() {
/* 309 */     HashMap<Object, Object> extensions = new HashMap<Object, Object>();
/*     */     
/* 311 */     Properties props = MPCONFIG().getPropertiesByPrefix("c3p0.extensions");
/* 312 */     for (Iterator<String> ii = props.keySet().iterator(); ii.hasNext(); ) {
/*     */       
/* 314 */       String fullKey = ii.next();
/* 315 */       String extensionsKey = fullKey.substring(PROP_STYLE_EXTENSIONS_PFX_LEN + 1);
/* 316 */       extensions.put(extensionsKey, props.get(fullKey));
/*     */     } 
/*     */     
/* 319 */     return extensions;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static HashMap findPropStyleNamedScopes() {
/* 325 */     HashMap<Object, Object> namesToNamedScopes = new HashMap<Object, Object>();
/*     */     
/* 327 */     Properties props = MPCONFIG().getPropertiesByPrefix("c3p0.named-configs");
/* 328 */     for (Iterator<String> ii = props.keySet().iterator(); ii.hasNext(); ) {
/*     */       
/* 330 */       String fullKey = ii.next();
/* 331 */       String nameProp = fullKey.substring(PROP_STYLE_NAMED_CFG_PFX_LEN + 1);
/* 332 */       int dot_index = nameProp.indexOf('.');
/* 333 */       if (dot_index < 0) {
/*     */         
/* 335 */         if (logger.isLoggable(MLevel.WARNING)) {
/* 336 */           logger.log(MLevel.WARNING, "Bad specification of named config property '" + fullKey + "', propfile key should look like '" + "c3p0.named-configs" + ".<cfgname>.<property>' or '" + "c3p0.named-configs" + ".<cfgname>.user-overrides.<user>.<property>'. Ignoring.");
/*     */         }
/*     */ 
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 343 */       String configName = nameProp.substring(0, dot_index);
/* 344 */       String propName = nameProp.substring(dot_index + 1);
/*     */ 
/*     */ 
/*     */       
/* 348 */       NamedScope scope = (NamedScope)namesToNamedScopes.get(configName);
/* 349 */       if (scope == null) {
/*     */         
/* 351 */         scope = new NamedScope();
/* 352 */         namesToNamedScopes.put(configName, scope);
/*     */       } 
/*     */       
/* 355 */       int second_dot_index = propName.indexOf('.');
/*     */       
/* 357 */       if (second_dot_index >= 0) {
/*     */         
/* 359 */         if (propName.startsWith("user-overrides")) {
/*     */           
/* 361 */           int third_dot_index = propName.substring(second_dot_index + 1).indexOf('.');
/* 362 */           if (third_dot_index < 0)
/*     */           {
/* 364 */             if (logger.isLoggable(MLevel.WARNING)) {
/* 365 */               logger.log(MLevel.WARNING, "Misformatted user-override property; missing user or property name: " + propName);
/*     */             }
/*     */           }
/* 368 */           String user = propName.substring(second_dot_index + 1, third_dot_index);
/* 369 */           String userPropName = propName.substring(third_dot_index + 1);
/*     */           
/* 371 */           HashMap<Object, Object> userOverridesMap = (HashMap)scope.userNamesToOverrides.get(user);
/* 372 */           if (userOverridesMap == null) {
/*     */             
/* 374 */             userOverridesMap = new HashMap<Object, Object>();
/* 375 */             scope.userNamesToOverrides.put(user, userOverridesMap);
/*     */           } 
/* 377 */           userOverridesMap.put(userPropName, props.get(fullKey)); continue;
/*     */         } 
/* 379 */         if (propName.startsWith("extensions")) {
/*     */           
/* 381 */           String extensionsKey = propName.substring(second_dot_index + 1);
/* 382 */           scope.extensions.put(extensionsKey, props.get(fullKey));
/*     */           
/*     */           continue;
/*     */         } 
/* 386 */         if (logger.isLoggable(MLevel.WARNING)) {
/* 387 */           logger.log(MLevel.WARNING, "Unexpected compound property, ignored: " + propName);
/*     */         }
/*     */         continue;
/*     */       } 
/* 391 */       scope.props.put(propName, props.get(fullKey));
/*     */     } 
/*     */     
/* 394 */     return namesToNamedScopes;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getUnspecifiedUserProperty(String propKey, String configName) {
/* 399 */     String out = null;
/*     */     
/* 401 */     if (configName == null) {
/* 402 */       out = (String)(MAIN()).defaultConfig.props.get(propKey);
/*     */     } else {
/*     */       
/* 405 */       NamedScope named = (NamedScope)(MAIN()).configNamesToNamedScopes.get(configName);
/* 406 */       if (named != null) {
/* 407 */         out = (String)named.props.get(propKey);
/*     */       } else {
/* 409 */         logger.warning("named-config with name '" + configName + "' does not exist. Using default-config for property '" + propKey + "'.");
/*     */       } 
/* 411 */       if (out == null) {
/* 412 */         out = (String)(MAIN()).defaultConfig.props.get(propKey);
/*     */       }
/*     */     } 
/* 415 */     return out;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Map getExtensions(String configName) {
/* 420 */     HashMap<?, ?> raw = (MAIN()).defaultConfig.extensions;
/* 421 */     if (configName != null) {
/*     */       
/* 423 */       NamedScope named = (NamedScope)(MAIN()).configNamesToNamedScopes.get(configName);
/* 424 */       if (named != null) {
/* 425 */         raw = named.extensions;
/*     */       } else {
/* 427 */         logger.warning("named-config with name '" + configName + "' does not exist. Using default-config extensions.");
/*     */       } 
/* 429 */     }  return Collections.unmodifiableMap(raw);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Map getUnspecifiedUserProperties(String configName) {
/* 434 */     Map<Object, Object> out = new HashMap<Object, Object>();
/*     */     
/* 436 */     out.putAll((MAIN()).defaultConfig.props);
/*     */     
/* 438 */     if (configName != null) {
/*     */       
/* 440 */       NamedScope named = (NamedScope)(MAIN()).configNamesToNamedScopes.get(configName);
/* 441 */       if (named != null) {
/* 442 */         out.putAll(named.props);
/*     */       } else {
/* 444 */         logger.warning("named-config with name '" + configName + "' does not exist. Using default-config.");
/*     */       } 
/*     */     } 
/* 447 */     return out;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Map getUserOverrides(String configName) {
/* 452 */     Map<Object, Object> out = new HashMap<Object, Object>();
/*     */     
/* 454 */     NamedScope namedConfigScope = null;
/*     */     
/* 456 */     if (configName != null) {
/* 457 */       namedConfigScope = (NamedScope)(MAIN()).configNamesToNamedScopes.get(configName);
/*     */     }
/* 459 */     out.putAll((MAIN()).defaultConfig.userNamesToOverrides);
/*     */     
/* 461 */     if (namedConfigScope != null) {
/* 462 */       out.putAll(namedConfigScope.userNamesToOverrides);
/*     */     }
/* 464 */     return out.isEmpty() ? null : out;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getUserOverridesAsString(String configName) throws IOException {
/* 469 */     Map userOverrides = getUserOverrides(configName);
/* 470 */     if (userOverrides == null) {
/* 471 */       return null;
/*     */     }
/* 473 */     return C3P0ImplUtils.createUserOverridesAsString(userOverrides).intern();
/*     */   }
/*     */   
/* 476 */   static final Class[] SUOAS_ARGS = new Class[] { String.class };
/*     */   
/* 478 */   static final Collection SKIP_BIND_PROPS = Arrays.asList(new String[] { "loginTimeout", "properties" }); NamedScope defaultConfig;
/*     */   HashMap configNamesToNamedScopes;
/*     */   
/*     */   public static void bindUserOverridesAsString(Object bean, String uoas) throws Exception {
/* 482 */     Method m = bean.getClass().getMethod("setUserOverridesAsString", SUOAS_ARGS);
/* 483 */     m.invoke(bean, new Object[] { uoas });
/*     */   }
/*     */   
/*     */   public static void bindUserOverridesToBean(Object bean, String configName) throws Exception {
/* 487 */     bindUserOverridesAsString(bean, getUserOverridesAsString(configName));
/*     */   }
/*     */   
/*     */   public static void bindNamedConfigToBean(Object bean, String configName, boolean shouldBindUserOverridesAsString) throws IntrospectionException {
/* 491 */     Map<?, ?> defaultUserProps = getUnspecifiedUserProperties(configName);
/* 492 */     Map extensions = getExtensions(configName);
/* 493 */     Map<Object, Object> union = new HashMap<Object, Object>();
/* 494 */     union.putAll(defaultUserProps);
/* 495 */     union.put("extensions", extensions);
/* 496 */     BeansUtils.overwriteAccessiblePropertiesFromMap(union, bean, false, SKIP_BIND_PROPS, true, MLevel.FINEST, MLevel.WARNING, false);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 506 */       if (shouldBindUserOverridesAsString) {
/* 507 */         bindUserOverridesToBean(bean, configName);
/*     */       }
/* 509 */     } catch (NoSuchMethodException e) {
/*     */       
/* 511 */       e.printStackTrace();
/*     */     
/*     */     }
/* 514 */     catch (Exception e) {
/*     */       
/* 516 */       if (logger.isLoggable(MLevel.WARNING)) {
/* 517 */         logger.log(MLevel.WARNING, "An exception occurred while trying to bind user overrides for named config '" + configName + "'. Only default user configs " + "will be used.", e);
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
/*     */   public static String initializeUserOverridesAsString() {
/*     */     try {
/* 534 */       return getUserOverridesAsString(null);
/* 535 */     } catch (Exception e) {
/*     */       
/* 537 */       if (logger.isLoggable(MLevel.WARNING))
/* 538 */         logger.log(MLevel.WARNING, "Error initializing default user overrides. User overrides may be ignored.", e); 
/* 539 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Map initializeExtensions() {
/* 544 */     return getExtensions(null);
/*     */   }
/*     */   
/*     */   public static String initializeStringPropertyVar(String propKey, String dflt) {
/* 548 */     String out = getUnspecifiedUserProperty(propKey, null);
/* 549 */     if (out == null) out = dflt; 
/* 550 */     return out;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int initializeIntPropertyVar(String propKey, int dflt) {
/* 555 */     boolean set = false;
/* 556 */     int out = -1;
/*     */     
/* 558 */     String outStr = getUnspecifiedUserProperty(propKey, null);
/* 559 */     if (outStr != null) {
/*     */       
/*     */       try {
/*     */         
/* 563 */         out = Integer.parseInt(outStr.trim());
/* 564 */         set = true;
/*     */       }
/* 566 */       catch (NumberFormatException e) {
/*     */         
/* 568 */         logger.info("'" + outStr + "' is not a legal value for property '" + propKey + "'. Using default value: " + dflt);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 573 */     if (!set) {
/* 574 */       out = dflt;
/*     */     }
/*     */     
/* 577 */     return out;
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean initializeBooleanPropertyVar(String propKey, boolean dflt) {
/* 582 */     boolean set = false;
/* 583 */     boolean out = false;
/*     */     
/* 585 */     String outStr = getUnspecifiedUserProperty(propKey, null);
/* 586 */     if (outStr != null) {
/*     */       
/*     */       try {
/*     */         
/* 590 */         out = BooleanUtils.parseBoolean(outStr.trim());
/* 591 */         set = true;
/*     */       }
/* 593 */       catch (IllegalArgumentException e) {
/*     */         
/* 595 */         logger.info("'" + outStr + "' is not a legal value for property '" + propKey + "'. Using default value: " + dflt);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 600 */     if (!set) {
/* 601 */       out = dflt;
/*     */     }
/* 603 */     return out;
/*     */   }
/*     */   
/*     */   public static MultiPropertiesConfig getMultiPropertiesConfig() {
/* 607 */     return MPCONFIG();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   C3P0Config(NamedScope defaultConfig, HashMap configNamesToNamedScopes) {
/* 614 */     this.defaultConfig = defaultConfig;
/* 615 */     this.configNamesToNamedScopes = configNamesToNamedScopes;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/cfg/C3P0Config.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */