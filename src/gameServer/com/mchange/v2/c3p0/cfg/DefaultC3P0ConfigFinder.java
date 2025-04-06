/*     */ package com.mchange.v2.c3p0.cfg;
/*     */ 
/*     */ import com.mchange.v2.log.MLevel;
/*     */ import com.mchange.v2.log.MLog;
/*     */ import com.mchange.v2.log.MLogger;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.InputStream;
/*     */ import java.util.HashMap;
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
/*     */ public class DefaultC3P0ConfigFinder
/*     */   implements C3P0ConfigFinder
/*     */ {
/*     */   static final String XML_CFG_FILE_KEY = "com.mchange.v2.c3p0.cfg.xml";
/*     */   static final String CLASSLOADER_RESOURCE_PREFIX = "classloader:";
/*  49 */   static final MLogger logger = MLog.getLogger(DefaultC3P0ConfigFinder.class);
/*     */   
/*     */   final boolean warn_of_xml_overrides;
/*     */   
/*     */   public DefaultC3P0ConfigFinder(boolean warn_of_xml_overrides) {
/*  54 */     this.warn_of_xml_overrides = warn_of_xml_overrides;
/*     */   }
/*     */   public DefaultC3P0ConfigFinder() {
/*  57 */     this(false);
/*     */   }
/*     */ 
/*     */   
/*     */   public C3P0Config findConfig() throws Exception {
/*     */     C3P0Config out;
/*  63 */     HashMap flatDefaults = C3P0ConfigUtils.extractHardcodedC3P0Defaults();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  68 */     flatDefaults.putAll(C3P0ConfigUtils.extractC3P0PropertiesResources());
/*     */     
/*  70 */     String cfgFile = C3P0Config.getPropsFileConfigProperty("com.mchange.v2.c3p0.cfg.xml");
/*  71 */     if (cfgFile == null) {
/*     */       
/*  73 */       C3P0Config xmlConfig = C3P0ConfigXmlUtils.extractXmlConfigFromDefaultResource();
/*  74 */       if (xmlConfig != null) {
/*     */         
/*  76 */         insertDefaultsUnderNascentConfig(flatDefaults, xmlConfig);
/*  77 */         out = xmlConfig;
/*     */         
/*  79 */         mbOverrideWarning("resource", "/c3p0-config.xml");
/*     */       } else {
/*     */         
/*  82 */         out = C3P0ConfigUtils.configFromFlatDefaults(flatDefaults);
/*     */       } 
/*     */     } else {
/*     */       
/*  86 */       cfgFile = cfgFile.trim();
/*     */       
/*  88 */       InputStream is = null;
/*     */       
/*     */       try {
/*  91 */         if (cfgFile.startsWith("classloader:")) {
/*     */           
/*  93 */           ClassLoader cl = getClass().getClassLoader();
/*  94 */           String rsrcPath = cfgFile.substring("classloader:".length());
/*     */ 
/*     */ 
/*     */           
/*  98 */           if (rsrcPath.startsWith("/")) {
/*  99 */             rsrcPath = rsrcPath.substring(1);
/*     */           }
/* 101 */           is = cl.getResourceAsStream(rsrcPath);
/* 102 */           if (is == null) {
/* 103 */             throw new FileNotFoundException("Specified ClassLoader resource '" + rsrcPath + "' could not be found. " + "[ Found in configuration: " + "com.mchange.v2.c3p0.cfg.xml" + '=' + cfgFile + " ]");
/*     */           }
/*     */           
/* 106 */           mbOverrideWarning("resource", rsrcPath);
/*     */         }
/*     */         else {
/*     */           
/* 110 */           is = new BufferedInputStream(new FileInputStream(cfgFile));
/* 111 */           mbOverrideWarning("file", cfgFile);
/*     */         } 
/*     */         
/* 114 */         C3P0Config xmlConfig = C3P0ConfigXmlUtils.extractXmlConfigFromInputStream(is);
/* 115 */         insertDefaultsUnderNascentConfig(flatDefaults, xmlConfig);
/* 116 */         out = xmlConfig;
/*     */       } finally {
/*     */         
/*     */         try {
/* 120 */           if (is != null) is.close(); 
/* 121 */         } catch (Exception e) {
/* 122 */           e.printStackTrace();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 128 */     Properties sysPropConfig = C3P0ConfigUtils.findAllC3P0SystemProperties();
/* 129 */     out.defaultConfig.props.putAll(sysPropConfig);
/*     */     
/* 131 */     return out;
/*     */   }
/*     */ 
/*     */   
/*     */   private void insertDefaultsUnderNascentConfig(HashMap flatDefaults, C3P0Config config) {
/* 136 */     flatDefaults.putAll(config.defaultConfig.props);
/* 137 */     config.defaultConfig.props = flatDefaults;
/*     */   }
/*     */ 
/*     */   
/*     */   private void mbOverrideWarning(String srcType, String srcName) {
/* 142 */     if (this.warn_of_xml_overrides && logger.isLoggable(MLevel.WARNING))
/* 143 */       logger.log(MLevel.WARNING, "Configuation defined in " + srcType + "'" + srcName + "' overrides all other c3p0 config."); 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/cfg/DefaultC3P0ConfigFinder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */