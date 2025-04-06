/*     */ package com.mchange.v1.xmlprops;
/*     */ 
/*     */ import com.mchange.v1.xml.StdErrErrorHandler;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.InputStream;
/*     */ import java.util.Properties;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.ContentHandler;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.XMLReader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SaxXmlPropsParser
/*     */ {
/*     */   static final String DEFAULT_XML_READER = "org.apache.xerces.parsers.SAXParser";
/*     */   static final String XMLPROPS_NAMESPACE_URI = "http://www.mchange.com/namespaces/xmlprops";
/*     */   
/*     */   public static Properties parseXmlProps(InputStream paramInputStream) throws XmlPropsException {
/*     */     try {
/*  57 */       String str = "org.apache.xerces.parsers.SAXParser";
/*  58 */       XMLReader xMLReader = (XMLReader)Class.forName(str).newInstance();
/*  59 */       InputSource inputSource = new InputSource(paramInputStream);
/*  60 */       return parseXmlProps(inputSource, xMLReader, null, null);
/*     */     }
/*  62 */     catch (XmlPropsException xmlPropsException) {
/*  63 */       throw xmlPropsException;
/*  64 */     } catch (Exception exception) {
/*     */       
/*  66 */       exception.printStackTrace();
/*  67 */       throw new XmlPropsException("Exception while instantiating XMLReader.", exception);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Properties parseXmlProps(InputSource paramInputSource, XMLReader paramXMLReader, EntityResolver paramEntityResolver, ErrorHandler paramErrorHandler) throws XmlPropsException {
/*     */     try {
/*     */       StdErrErrorHandler stdErrErrorHandler;
/*  77 */       if (paramEntityResolver != null)
/*  78 */         paramXMLReader.setEntityResolver(paramEntityResolver); 
/*  79 */       if (paramErrorHandler == null)
/*  80 */         stdErrErrorHandler = new StdErrErrorHandler(); 
/*  81 */       paramXMLReader.setErrorHandler((ErrorHandler)stdErrErrorHandler);
/*  82 */       XmlPropsContentHandler xmlPropsContentHandler = new XmlPropsContentHandler();
/*  83 */       paramXMLReader.setContentHandler(xmlPropsContentHandler);
/*  84 */       paramXMLReader.parse(paramInputSource);
/*  85 */       return xmlPropsContentHandler.getLastProperties();
/*     */     }
/*  87 */     catch (Exception exception) {
/*     */       
/*  89 */       if (exception instanceof SAXException)
/*     */       {
/*  91 */         ((SAXException)exception).getException().printStackTrace();
/*     */       }
/*  93 */       exception.printStackTrace();
/*  94 */       throw new XmlPropsException(exception);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   static class XmlPropsContentHandler
/*     */     implements ContentHandler
/*     */   {
/*     */     Locator locator;
/*     */     
/*     */     Properties props;
/*     */     String name;
/*     */     StringBuffer valueBuf;
/*     */     
/*     */     public void setDocumentLocator(Locator param1Locator) {
/* 109 */       this.locator = param1Locator;
/*     */     }
/*     */     
/*     */     public void startDocument() throws SAXException {
/* 113 */       this.props = new Properties();
/*     */     }
/*     */ 
/*     */     
/*     */     public void startElement(String param1String1, String param1String2, String param1String3, Attributes param1Attributes) {
/* 118 */       System.err.println("--> startElement( " + param1String1 + ", " + param1String2 + ", " + param1Attributes + ")");
/* 119 */       if (!param1String1.equals("") && !param1String1.equals("http://www.mchange.com/namespaces/xmlprops")) {
/*     */         return;
/*     */       }
/* 122 */       if (param1String2.equals("property")) {
/*     */         
/* 124 */         this.name = param1Attributes.getValue(param1String1, "name");
/* 125 */         this.valueBuf = new StringBuffer();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void characters(char[] param1ArrayOfchar, int param1Int1, int param1Int2) throws SAXException {
/* 131 */       if (this.valueBuf != null) {
/* 132 */         this.valueBuf.append(param1ArrayOfchar, param1Int1, param1Int2);
/*     */       }
/*     */     }
/*     */     
/*     */     public void ignorableWhitespace(char[] param1ArrayOfchar, int param1Int1, int param1Int2) throws SAXException {
/* 137 */       if (this.valueBuf != null) {
/* 138 */         this.valueBuf.append(param1ArrayOfchar, param1Int1, param1Int2);
/*     */       }
/*     */     }
/*     */     
/*     */     public void endElement(String param1String1, String param1String2, String param1String3) throws SAXException {
/* 143 */       if (!param1String1.equals("") && !param1String1.equals("http://www.mchange.com/namespaces/xmlprops")) {
/*     */         return;
/*     */       }
/* 146 */       if (param1String2.equals("property")) {
/*     */         
/* 148 */         System.err.println("NAME: " + this.name);
/* 149 */         this.props.put(this.name, this.valueBuf.toString());
/* 150 */         this.valueBuf = null;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void endDocument() throws SAXException {}
/*     */ 
/*     */     
/*     */     public void startPrefixMapping(String param1String1, String param1String2) throws SAXException {}
/*     */ 
/*     */     
/*     */     public void endPrefixMapping(String param1String) throws SAXException {}
/*     */ 
/*     */     
/*     */     public void processingInstruction(String param1String1, String param1String2) throws SAXException {}
/*     */ 
/*     */     
/*     */     public void skippedEntity(String param1String) throws SAXException {}
/*     */ 
/*     */     
/*     */     public Properties getLastProperties() {
/* 172 */       return this.props;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void main(String[] paramArrayOfString) {
/*     */     try {
/* 179 */       BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(paramArrayOfString[0]));
/* 180 */       SaxXmlPropsParser saxXmlPropsParser = new SaxXmlPropsParser();
/* 181 */       Properties properties = parseXmlProps(bufferedInputStream);
/* 182 */       for (String str1 : properties.keySet())
/*     */       {
/*     */         
/* 185 */         String str2 = properties.getProperty(str1);
/* 186 */         System.err.println(str1 + '=' + str2);
/*     */       }
/*     */     
/* 189 */     } catch (Exception exception) {
/* 190 */       exception.printStackTrace();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/xmlprops/SaxXmlPropsParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */