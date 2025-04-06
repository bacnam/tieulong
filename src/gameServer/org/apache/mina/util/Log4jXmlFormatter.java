/*     */ package org.apache.mina.util;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.logging.Formatter;
/*     */ import java.util.logging.LogRecord;
/*     */ import org.slf4j.MDC;
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
/*     */ public class Log4jXmlFormatter
/*     */   extends Formatter
/*     */ {
/*  49 */   private final int DEFAULT_SIZE = 256;
/*     */   
/*  51 */   private final int UPPER_LIMIT = 2048;
/*     */   
/*  53 */   private StringBuffer buf = new StringBuffer(256);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean locationInfo = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean properties = false;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLocationInfo(boolean flag) {
/*  69 */     this.locationInfo = flag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getLocationInfo() {
/*  78 */     return this.locationInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProperties(boolean flag) {
/*  87 */     this.properties = flag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getProperties() {
/*  96 */     return this.properties;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String format(LogRecord record) {
/* 104 */     if (this.buf.capacity() > 2048) {
/* 105 */       this.buf = new StringBuffer(256);
/*     */     } else {
/* 107 */       this.buf.setLength(0);
/*     */     } 
/* 109 */     this.buf.append("<log4j:event logger=\"");
/* 110 */     this.buf.append(Transform.escapeTags(record.getLoggerName()));
/* 111 */     this.buf.append("\" timestamp=\"");
/* 112 */     this.buf.append(record.getMillis());
/* 113 */     this.buf.append("\" level=\"");
/*     */     
/* 115 */     this.buf.append(Transform.escapeTags(record.getLevel().getName()));
/* 116 */     this.buf.append("\" thread=\"");
/* 117 */     this.buf.append(String.valueOf(record.getThreadID()));
/* 118 */     this.buf.append("\">\r\n");
/*     */     
/* 120 */     this.buf.append("<log4j:message><![CDATA[");
/*     */ 
/*     */     
/* 123 */     Transform.appendEscapingCDATA(this.buf, record.getMessage());
/* 124 */     this.buf.append("]]></log4j:message>\r\n");
/*     */     
/* 126 */     if (record.getThrown() != null) {
/* 127 */       String[] s = Transform.getThrowableStrRep(record.getThrown());
/* 128 */       if (s != null) {
/* 129 */         this.buf.append("<log4j:throwable><![CDATA[");
/* 130 */         for (String value : s) {
/* 131 */           Transform.appendEscapingCDATA(this.buf, value);
/* 132 */           this.buf.append("\r\n");
/*     */         } 
/* 134 */         this.buf.append("]]></log4j:throwable>\r\n");
/*     */       } 
/*     */     } 
/*     */     
/* 138 */     if (this.locationInfo) {
/* 139 */       this.buf.append("<log4j:locationInfo class=\"");
/* 140 */       this.buf.append(Transform.escapeTags(record.getSourceClassName()));
/* 141 */       this.buf.append("\" method=\"");
/* 142 */       this.buf.append(Transform.escapeTags(record.getSourceMethodName()));
/* 143 */       this.buf.append("\" file=\"?\" line=\"?\"/>\r\n");
/*     */     } 
/*     */     
/* 146 */     if (this.properties) {
/* 147 */       Map contextMap = MDC.getCopyOfContextMap();
/* 148 */       if (contextMap != null) {
/* 149 */         Set keySet = contextMap.keySet();
/* 150 */         if (keySet != null && keySet.size() > 0) {
/* 151 */           this.buf.append("<log4j:properties>\r\n");
/* 152 */           Object[] keys = keySet.toArray();
/* 153 */           Arrays.sort(keys);
/* 154 */           for (Object key1 : keys) {
/* 155 */             String key = (key1 == null) ? "" : key1.toString();
/* 156 */             Object val = contextMap.get(key);
/* 157 */             if (val != null) {
/* 158 */               this.buf.append("<log4j:data name=\"");
/* 159 */               this.buf.append(Transform.escapeTags(key));
/* 160 */               this.buf.append("\" value=\"");
/* 161 */               this.buf.append(Transform.escapeTags(String.valueOf(val)));
/* 162 */               this.buf.append("\"/>\r\n");
/*     */             } 
/*     */           } 
/* 165 */           this.buf.append("</log4j:properties>\r\n");
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 170 */     this.buf.append("</log4j:event>\r\n\r\n");
/*     */     
/* 172 */     return this.buf.toString();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/util/Log4jXmlFormatter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */