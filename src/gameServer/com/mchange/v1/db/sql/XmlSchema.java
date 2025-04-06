/*     */ package com.mchange.v1.db.sql;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Statement;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.parsers.SAXParser;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import org.xml.sax.AttributeList;
/*     */ import org.xml.sax.HandlerBase;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XmlSchema
/*     */   implements Schema
/*     */ {
/*     */   private static final int CREATE = 0;
/*     */   private static final int DROP = 1;
/*     */   List createStmts;
/*     */   List dropStmts;
/*     */   Map appMap;
/*     */   
/*     */   public XmlSchema(URL paramURL) throws SAXException, IOException, ParserConfigurationException {
/*  55 */     parse(paramURL.openStream());
/*     */   }
/*     */   public XmlSchema(InputStream paramInputStream) throws SAXException, IOException, ParserConfigurationException {
/*  58 */     parse(paramInputStream);
/*     */   }
/*     */ 
/*     */   
/*     */   public XmlSchema() {}
/*     */   
/*     */   public void parse(InputStream paramInputStream) throws SAXException, IOException, ParserConfigurationException {
/*  65 */     this.createStmts = new ArrayList();
/*  66 */     this.dropStmts = new ArrayList();
/*  67 */     this.appMap = new HashMap<Object, Object>();
/*     */     
/*  69 */     InputSource inputSource = new InputSource();
/*  70 */     inputSource.setByteStream(paramInputStream);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  75 */     inputSource.setSystemId(XmlSchema.class.getResource("schema.dtd").toExternalForm());
/*     */     
/*  77 */     SAXParser sAXParser = SAXParserFactory.newInstance().newSAXParser();
/*  78 */     MySaxHandler mySaxHandler = new MySaxHandler();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  87 */     sAXParser.parse(inputSource, mySaxHandler);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void doStatementList(List paramList, Connection paramConnection) throws SQLException {
/*  93 */     if (paramList != null) {
/*     */       
/*  95 */       Statement statement = null;
/*     */       
/*     */       try {
/*  98 */         statement = paramConnection.createStatement();
/*  99 */         for (Iterator<String> iterator = paramList.iterator(); iterator.hasNext();)
/* 100 */           statement.executeUpdate(iterator.next()); 
/* 101 */         paramConnection.commit();
/*     */       }
/* 103 */       catch (SQLException sQLException) {
/*     */         
/* 105 */         ConnectionUtils.attemptRollback(paramConnection);
/* 106 */         sQLException.fillInStackTrace();
/* 107 */         throw sQLException;
/*     */       } finally {
/*     */         
/* 110 */         StatementUtils.attemptClose(statement);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getStatementText(String paramString1, String paramString2) {
/* 116 */     SqlApp sqlApp = (SqlApp)this.appMap.get(paramString1);
/* 117 */     String str = null;
/* 118 */     if (sqlApp != null)
/* 119 */       str = sqlApp.getStatementText(paramString2); 
/* 120 */     return str;
/*     */   }
/*     */   
/*     */   public void createSchema(Connection paramConnection) throws SQLException {
/* 124 */     doStatementList(this.createStmts, paramConnection);
/*     */   }
/*     */   public void dropSchema(Connection paramConnection) throws SQLException {
/* 127 */     doStatementList(this.dropStmts, paramConnection);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] paramArrayOfString) {
/*     */     try {
/* 134 */       XmlSchema xmlSchema = new XmlSchema(XmlSchema.class.getResource("/com/mchange/v1/hjug/hjugschema.xml"));
/*     */     
/*     */     }
/* 137 */     catch (Exception exception) {
/* 138 */       exception.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   class MySaxHandler
/*     */     extends HandlerBase {
/* 144 */     int state = -1;
/*     */     boolean in_statement = false;
/*     */     boolean in_comment = false;
/* 147 */     StringBuffer charBuff = null;
/* 148 */     XmlSchema.SqlApp currentApp = null;
/* 149 */     String currentStmtName = null;
/*     */ 
/*     */     
/*     */     public void startElement(String param1String, AttributeList param1AttributeList) {
/* 153 */       if (param1String.equals("create")) {
/* 154 */         this.state = 0;
/* 155 */       } else if (param1String.equals("drop")) {
/* 156 */         this.state = 1;
/* 157 */       } else if (param1String.equals("statement")) {
/*     */         
/* 159 */         this.in_statement = true;
/* 160 */         this.charBuff = new StringBuffer();
/* 161 */         if (this.currentApp != null) {
/*     */           byte b; int i;
/* 163 */           for (b = 0, i = param1AttributeList.getLength(); b < i; b++) {
/*     */             
/* 165 */             String str = param1AttributeList.getName(b);
/* 166 */             if (str.equals("name")) {
/*     */               
/* 168 */               this.currentStmtName = param1AttributeList.getValue(b);
/*     */               
/*     */               break;
/*     */             } 
/*     */           } 
/*     */         } 
/* 174 */       } else if (param1String.equals("comment")) {
/* 175 */         this.in_comment = true;
/* 176 */       } else if (param1String.equals("application")) {
/*     */         byte b; int i;
/* 178 */         for (b = 0, i = param1AttributeList.getLength(); b < i; b++) {
/*     */           
/* 180 */           String str = param1AttributeList.getName(b);
/* 181 */           if (str.equals("name")) {
/*     */             
/* 183 */             String str1 = param1AttributeList.getValue(b);
/* 184 */             this.currentApp = (XmlSchema.SqlApp)XmlSchema.this.appMap.get(str1);
/* 185 */             if (this.currentApp == null) {
/*     */               
/* 187 */               this.currentApp = new XmlSchema.SqlApp();
/* 188 */               XmlSchema.this.appMap.put(str1.intern(), this.currentApp);
/*     */             } 
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void characters(char[] param1ArrayOfchar, int param1Int1, int param1Int2) throws SAXException {
/* 199 */       if (!this.in_comment)
/*     */       {
/* 201 */         if (this.in_statement) {
/* 202 */           this.charBuff.append(param1ArrayOfchar, param1Int1, param1Int2);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public void endElement(String param1String) {
/* 208 */       if (param1String.equals("statement")) {
/*     */         
/* 210 */         String str = this.charBuff.toString().trim();
/* 211 */         if (this.state == 0) {
/* 212 */           XmlSchema.this.createStmts.add(str);
/* 213 */         } else if (this.state == 1) {
/* 214 */           XmlSchema.this.dropStmts.add(str);
/*     */         }
/* 216 */         else if (this.currentApp != null && this.currentStmtName != null) {
/* 217 */           this.currentApp.setStatementText(this.currentStmtName, str);
/*     */         } 
/* 219 */       } else if (param1String.equals("create") || param1String.equals("drop")) {
/* 220 */         this.state = -1;
/* 221 */       } else if (param1String.equals("comment")) {
/* 222 */         this.in_comment = false;
/* 223 */       } else if (param1String.equals("application")) {
/* 224 */         this.currentApp = null;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void warning(SAXParseException param1SAXParseException) {
/* 230 */       System.err.println("[Warning] " + param1SAXParseException.getMessage());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void error(SAXParseException param1SAXParseException) {
/* 237 */       System.err.println("[Error] " + param1SAXParseException.getMessage());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void fatalError(SAXParseException param1SAXParseException) throws SAXException {
/* 244 */       System.err.println("[Fatal Error] " + param1SAXParseException.getMessage());
/*     */ 
/*     */       
/* 247 */       throw param1SAXParseException;
/*     */     }
/*     */   }
/*     */   
/*     */   class SqlApp
/*     */   {
/* 253 */     Map stmtMap = new HashMap<Object, Object>();
/*     */     
/*     */     public void setStatementText(String param1String1, String param1String2) {
/* 256 */       this.stmtMap.put(param1String1, param1String2);
/*     */     }
/*     */     public String getStatementText(String param1String) {
/* 259 */       return (String)this.stmtMap.get(param1String);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/db/sql/XmlSchema.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */