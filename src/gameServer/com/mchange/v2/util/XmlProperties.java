/*     */ package com.mchange.v2.util;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Iterator;
/*     */ import java.util.Properties;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.TransformerConfigurationException;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import javax.xml.transform.TransformerFactory;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import org.w3c.dom.Comment;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.w3c.dom.Text;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class XmlProperties
/*     */   extends Properties
/*     */ {
/*     */   static final String DTD_SYSTEM_ID = "http://www.mchange.com/dtd/xml-properties.dtd";
/*     */   static final String DTD_RSRC_PATH = "dtd/xml-properties.dtd";
/*     */   DocumentBuilder docBuilder;
/*     */   Transformer identityTransformer;
/*     */   
/*     */   public XmlProperties() throws ParserConfigurationException, TransformerConfigurationException {
/*  72 */     EntityResolver entityResolver = new EntityResolver()
/*     */       {
/*     */         public InputSource resolveEntity(String param1String1, String param1String2)
/*     */         {
/*  76 */           if ("http://www.mchange.com/dtd/xml-properties.dtd".equals(param1String2)) {
/*     */             
/*  78 */             InputStream inputStream = XmlProperties.class.getResourceAsStream("dtd/xml-properties.dtd");
/*  79 */             return new InputSource(inputStream);
/*     */           } 
/*  81 */           return null;
/*     */         }
/*     */       };
/*     */     
/*  85 */     ErrorHandler errorHandler = new ErrorHandler()
/*     */       {
/*     */         public void warning(SAXParseException param1SAXParseException) throws SAXException {
/*  88 */           System.err.println("[Warning] " + param1SAXParseException.toString());
/*     */         }
/*     */         public void error(SAXParseException param1SAXParseException) throws SAXException {
/*  91 */           System.err.println("[Error] " + param1SAXParseException.toString());
/*     */         }
/*     */         public void fatalError(SAXParseException param1SAXParseException) throws SAXException {
/*  94 */           System.err.println("[Fatal Error] " + param1SAXParseException.toString());
/*     */         }
/*     */       };
/*     */     
/*  98 */     DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
/*  99 */     documentBuilderFactory.setValidating(true);
/* 100 */     documentBuilderFactory.setCoalescing(true);
/* 101 */     documentBuilderFactory.setIgnoringComments(true);
/* 102 */     this.docBuilder = documentBuilderFactory.newDocumentBuilder();
/* 103 */     this.docBuilder.setEntityResolver(entityResolver);
/* 104 */     this.docBuilder.setErrorHandler(errorHandler);
/*     */     
/* 106 */     TransformerFactory transformerFactory = TransformerFactory.newInstance();
/* 107 */     this.identityTransformer = transformerFactory.newTransformer();
/* 108 */     this.identityTransformer.setOutputProperty("indent", "yes");
/* 109 */     this.identityTransformer.setOutputProperty("doctype-system", "http://www.mchange.com/dtd/xml-properties.dtd");
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void loadXml(InputStream paramInputStream) throws IOException, SAXException {
/* 114 */     Document document = this.docBuilder.parse(paramInputStream);
/* 115 */     NodeList nodeList = document.getElementsByTagName("property"); byte b; int i;
/* 116 */     for (b = 0, i = nodeList.getLength(); b < i; b++) {
/* 117 */       extractProperty(nodeList.item(b));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void extractProperty(Node paramNode) {
/* 126 */     Element element = (Element)paramNode;
/* 127 */     String str1 = element.getAttribute("name");
/* 128 */     boolean bool = Boolean.valueOf(element.getAttribute("trim")).booleanValue();
/* 129 */     NodeList nodeList = element.getChildNodes();
/* 130 */     int i = nodeList.getLength();
/*     */     
/* 132 */     assert i >= 0 && i <= 1 : "Bad number of children of property element: " + i;
/*     */     
/* 134 */     String str2 = (i == 0) ? "" : ((Text)nodeList.item(0)).getNodeValue();
/* 135 */     if (bool) {
/* 136 */       str2 = str2.trim();
/*     */     }
/* 138 */     put(str1, str2);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void saveXml(OutputStream paramOutputStream) throws IOException, TransformerException {
/* 143 */     storeXml(paramOutputStream, (String)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void storeXml(OutputStream paramOutputStream, String paramString) throws IOException, TransformerException {
/* 148 */     Document document = this.docBuilder.newDocument();
/* 149 */     if (paramString != null) {
/*     */       
/* 151 */       Comment comment = document.createComment(paramString);
/* 152 */       document.appendChild(comment);
/*     */     } 
/*     */     
/* 155 */     Element element = document.createElement("xml-properties");
/*     */     
/* 157 */     for (Iterator<String> iterator = keySet().iterator(); iterator.hasNext(); ) {
/*     */       
/* 159 */       Element element1 = document.createElement("property");
/* 160 */       String str1 = iterator.next();
/* 161 */       String str2 = (String)get(str1);
/* 162 */       element1.setAttribute("name", str1);
/* 163 */       Text text = document.createTextNode(str2);
/* 164 */       element1.appendChild(text);
/* 165 */       element.appendChild(element1);
/*     */     } 
/* 167 */     document.appendChild(element);
/*     */     
/* 169 */     this.identityTransformer.transform(new DOMSource(document), new StreamResult(paramOutputStream));
/*     */   }
/*     */ 
/*     */   
/*     */   public static void main(String[] paramArrayOfString) {
/* 174 */     BufferedInputStream bufferedInputStream = null;
/* 175 */     BufferedOutputStream bufferedOutputStream = null;
/*     */     
/*     */     try {
/* 178 */       bufferedInputStream = new BufferedInputStream(new FileInputStream(paramArrayOfString[0]));
/* 179 */       bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(paramArrayOfString[1]));
/* 180 */       XmlProperties xmlProperties = new XmlProperties();
/* 181 */       xmlProperties.loadXml(bufferedInputStream);
/* 182 */       xmlProperties.list(System.out);
/* 183 */       xmlProperties.storeXml(bufferedOutputStream, "This is the resaved test document.");
/* 184 */       bufferedOutputStream.flush();
/*     */     }
/* 186 */     catch (Exception exception) {
/* 187 */       exception.printStackTrace();
/*     */     } finally {
/*     */       
/* 190 */       try { if (bufferedInputStream != null) bufferedInputStream.close();  } catch (Exception exception) { exception.printStackTrace(); }
/* 191 */        try { if (bufferedOutputStream != null) bufferedOutputStream.close();  } catch (Exception exception) { exception.printStackTrace(); }
/*     */     
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/util/XmlProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */