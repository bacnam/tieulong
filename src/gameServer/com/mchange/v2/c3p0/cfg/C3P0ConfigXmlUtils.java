/*     */ package com.mchange.v2.c3p0.cfg;
/*     */ 
/*     */ import com.mchange.v1.xml.DomParseUtils;
/*     */ import com.mchange.v2.log.MLevel;
/*     */ import com.mchange.v2.log.MLog;
/*     */ import com.mchange.v2.log.MLogger;
/*     */ import java.io.InputStream;
/*     */ import java.util.HashMap;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NodeList;
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
/*     */ public final class C3P0ConfigXmlUtils
/*     */ {
/*     */   public static final String XML_CONFIG_RSRC_PATH = "/c3p0-config.xml";
/*  50 */   static final MLogger logger = MLog.getLogger(C3P0ConfigXmlUtils.class);
/*     */   
/*     */   public static final String LINESEP;
/*     */   
/*  54 */   private static final String[] MISSPELL_PFXS = new String[] { "/c3p0", "/c3pO", "/c3po", "/C3P0", "/C3PO" };
/*  55 */   private static final char[] MISSPELL_LINES = new char[] { '-', '_' };
/*  56 */   private static final String[] MISSPELL_CONFIG = new String[] { "config", "CONFIG" };
/*  57 */   private static final String[] MISSPELL_XML = new String[] { "xml", "XML" };
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*     */     String str;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static final void warnCommonXmlConfigResourceMisspellings() {
/*  68 */     if (logger.isLoggable(MLevel.WARNING))
/*     */     {
/*  70 */       for (int a = 0, lena = MISSPELL_PFXS.length; a < lena; a++) {
/*     */         
/*  72 */         StringBuffer sb = new StringBuffer(16);
/*  73 */         sb.append(MISSPELL_PFXS[a]);
/*  74 */         for (int b = 0, lenb = MISSPELL_LINES.length; b < lenb; b++) {
/*     */           
/*  76 */           sb.append(MISSPELL_LINES[b]);
/*  77 */           for (int c = 0, lenc = MISSPELL_CONFIG.length; c < lenc; c++) {
/*     */             
/*  79 */             sb.append(MISSPELL_CONFIG[c]);
/*  80 */             sb.append('.');
/*  81 */             for (int d = 0, lend = MISSPELL_XML.length; d < lend; d++) {
/*     */               
/*  83 */               sb.append(MISSPELL_XML[d]);
/*  84 */               String test = sb.toString();
/*  85 */               if (!test.equals("/c3p0-config.xml")) {
/*     */                 
/*  87 */                 Object hopefullyNull = C3P0ConfigXmlUtils.class.getResource(test);
/*  88 */                 if (hopefullyNull != null) {
/*     */                   
/*  90 */                   logger.warning("POSSIBLY MISSPELLED c3p0-conf.xml RESOURCE FOUND. Please ensure the file name is c3p0-config.xml, all lower case, with the digit 0 (NOT the letter O) in c3p0. It should be placed  in the top level of c3p0's effective classpath.");
/*     */                   return;
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */           } 
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
/*     */   static {
/*     */     try {
/* 110 */       str = System.getProperty("line.separator", "\r\n");
/* 111 */     } catch (Exception e) {
/* 112 */       str = "\r\n";
/*     */     } 
/* 114 */     LINESEP = str;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static C3P0Config extractXmlConfigFromDefaultResource() throws Exception {
/* 120 */     InputStream is = null;
/*     */ 
/*     */     
/*     */     try {
/* 124 */       is = C3P0ConfigUtils.class.getResourceAsStream("/c3p0-config.xml");
/* 125 */       if (is == null) {
/*     */         
/* 127 */         warnCommonXmlConfigResourceMisspellings();
/* 128 */         return null;
/*     */       } 
/*     */       
/* 131 */       return extractXmlConfigFromInputStream(is);
/*     */     } finally {
/*     */       
/*     */       try {
/* 135 */         if (is != null) is.close(); 
/* 136 */       } catch (Exception e) {
/*     */         
/* 138 */         if (logger.isLoggable(MLevel.FINE)) {
/* 139 */           logger.log(MLevel.FINE, "Exception on resource InputStream close.", e);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static C3P0Config extractXmlConfigFromInputStream(InputStream is) throws Exception {
/* 146 */     DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
/* 147 */     DocumentBuilder db = fact.newDocumentBuilder();
/* 148 */     Document doc = db.parse(is);
/*     */     
/* 150 */     return extractConfigFromXmlDoc(doc);
/*     */   }
/*     */ 
/*     */   
/*     */   public static C3P0Config extractConfigFromXmlDoc(Document doc) throws Exception {
/* 155 */     Element docElem = doc.getDocumentElement();
/* 156 */     if (docElem.getTagName().equals("c3p0-config")) {
/*     */       NamedScope defaults;
/*     */       
/* 159 */       HashMap<Object, Object> configNamesToNamedScopes = new HashMap<Object, Object>();
/*     */       
/* 161 */       Element defaultConfigElem = DomParseUtils.uniqueChild(docElem, "default-config");
/* 162 */       if (defaultConfigElem != null) {
/* 163 */         defaults = extractNamedScopeFromLevel(defaultConfigElem);
/*     */       } else {
/* 165 */         defaults = new NamedScope();
/* 166 */       }  NodeList nl = DomParseUtils.immediateChildElementsByTagName(docElem, "named-config");
/* 167 */       for (int i = 0, len = nl.getLength(); i < len; i++) {
/*     */         
/* 169 */         Element namedConfigElem = (Element)nl.item(i);
/* 170 */         String configName = namedConfigElem.getAttribute("name");
/* 171 */         if (configName != null && configName.length() > 0) {
/*     */           
/* 173 */           NamedScope namedConfig = extractNamedScopeFromLevel(namedConfigElem);
/* 174 */           configNamesToNamedScopes.put(configName, namedConfig);
/*     */         } else {
/*     */           
/* 177 */           logger.warning("Configuration XML contained named-config element without name attribute: " + namedConfigElem);
/*     */         } 
/* 179 */       }  return new C3P0Config(defaults, configNamesToNamedScopes);
/*     */     } 
/*     */     
/* 182 */     throw new Exception("Root element of c3p0 config xml should be 'c3p0-config', not '" + docElem.getTagName() + "'.");
/*     */   }
/*     */ 
/*     */   
/*     */   private static NamedScope extractNamedScopeFromLevel(Element elem) {
/* 187 */     HashMap props = extractPropertiesFromLevel(elem);
/* 188 */     HashMap<Object, Object> userNamesToOverrides = new HashMap<Object, Object>();
/*     */     
/* 190 */     NodeList nl = DomParseUtils.immediateChildElementsByTagName(elem, "user-overrides");
/* 191 */     for (int i = 0, len = nl.getLength(); i < len; i++) {
/*     */       
/* 193 */       Element perUserConfigElem = (Element)nl.item(i);
/* 194 */       String userName = perUserConfigElem.getAttribute("user");
/* 195 */       if (userName != null && userName.length() > 0) {
/*     */         
/* 197 */         HashMap userProps = extractPropertiesFromLevel(perUserConfigElem);
/* 198 */         userNamesToOverrides.put(userName, userProps);
/*     */       } else {
/*     */         
/* 201 */         logger.warning("Configuration XML contained user-overrides element without user attribute: " + LINESEP + perUserConfigElem);
/*     */       } 
/*     */     } 
/* 204 */     HashMap extensions = extractExtensionsFromLevel(elem);
/*     */     
/* 206 */     return new NamedScope(props, userNamesToOverrides, extensions);
/*     */   }
/*     */ 
/*     */   
/*     */   private static HashMap extractExtensionsFromLevel(Element elem) {
/* 211 */     HashMap<Object, Object> out = new HashMap<Object, Object>();
/* 212 */     NodeList nl = DomParseUtils.immediateChildElementsByTagName(elem, "extensions");
/* 213 */     for (int i = 0, len = nl.getLength(); i < len; i++) {
/*     */       
/* 215 */       Element extensionsElem = (Element)nl.item(i);
/* 216 */       out.putAll(extractPropertiesFromLevel(extensionsElem));
/*     */     } 
/* 218 */     return out;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static HashMap extractPropertiesFromLevel(Element elem) {
/* 225 */     HashMap<Object, Object> out = new HashMap<Object, Object>();
/*     */ 
/*     */     
/*     */     try {
/* 229 */       NodeList nl = DomParseUtils.immediateChildElementsByTagName(elem, "property");
/* 230 */       int len = nl.getLength();
/* 231 */       for (int i = 0; i < len; i++) {
/*     */         
/* 233 */         Element propertyElem = (Element)nl.item(i);
/* 234 */         String propName = propertyElem.getAttribute("name");
/* 235 */         if (propName != null && propName.length() > 0) {
/*     */           
/* 237 */           String propVal = DomParseUtils.allTextFromElement(propertyElem, true);
/* 238 */           out.put(propName, propVal);
/*     */         }
/*     */         else {
/*     */           
/* 242 */           logger.warning("Configuration XML contained property element without name attribute: " + LINESEP + propertyElem);
/*     */         } 
/*     */       } 
/* 245 */     } catch (Exception e) {
/*     */       
/* 247 */       logger.log(MLevel.WARNING, "An exception occurred while reading config XML. Some configuration information has probably been ignored.", e);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 253 */     return out;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/cfg/C3P0ConfigXmlUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */