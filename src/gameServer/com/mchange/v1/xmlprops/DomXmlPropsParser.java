/*     */ package com.mchange.v1.xmlprops;
/*     */ 
/*     */ import com.mchange.v1.xml.ResourceEntityResolver;
/*     */ import com.mchange.v1.xml.StdErrErrorHandler;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.InputStream;
/*     */ import java.util.Properties;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.xml.sax.EntityResolver;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.InputSource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DomXmlPropsParser
/*     */ {
/*     */   static final String XMLPROPS_NAMESPACE_URI = "http://www.mchange.com/namespaces/xmlprops";
/*  51 */   static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
/*     */ 
/*     */   
/*     */   static {
/*  55 */     factory.setNamespaceAware(true);
/*  56 */     factory.setValidating(true);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Properties parseXmlProps(InputStream paramInputStream) throws XmlPropsException {
/*  62 */     return parseXmlProps(new InputSource(paramInputStream), (EntityResolver)new ResourceEntityResolver(getClass()), (ErrorHandler)new StdErrErrorHandler());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Properties parseXmlProps(InputSource paramInputSource, EntityResolver paramEntityResolver, ErrorHandler paramErrorHandler) throws XmlPropsException {
/*     */     try {
/*  72 */       Properties properties = new Properties();
/*     */       
/*  74 */       DocumentBuilder documentBuilder = factory.newDocumentBuilder();
/*  75 */       documentBuilder.setEntityResolver(paramEntityResolver);
/*  76 */       documentBuilder.setErrorHandler(paramErrorHandler);
/*     */       
/*  78 */       Document document = documentBuilder.parse(paramInputSource);
/*  79 */       Element element = document.getDocumentElement();
/*     */       
/*  81 */       NodeList nodeList = element.getElementsByTagName("property"); byte b; int i;
/*  82 */       for (b = 0, i = nodeList.getLength(); b < i; b++) {
/*     */         
/*  84 */         Element element1 = (Element)nodeList.item(b);
/*     */         
/*  86 */         String str = element1.getAttribute("name");
/*  87 */         StringBuffer stringBuffer = new StringBuffer();
/*  88 */         NodeList nodeList1 = element1.getChildNodes(); byte b1; int j;
/*  89 */         for (b1 = 0, j = nodeList1.getLength(); b1 < j; b1++) {
/*     */           
/*  91 */           Node node = nodeList1.item(b1);
/*  92 */           if (node.getNodeType() == 3) {
/*  93 */             stringBuffer.append(node.getNodeValue());
/*     */           }
/*     */         } 
/*     */         
/*  97 */         properties.put(str, stringBuffer.toString());
/*     */       } 
/*     */       
/* 100 */       return properties;
/*     */     }
/* 102 */     catch (Exception exception) {
/*     */       
/* 104 */       exception.printStackTrace();
/* 105 */       throw new XmlPropsException(exception);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] paramArrayOfString) {
/*     */     try {
/* 113 */       BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(paramArrayOfString[0]));
/* 114 */       DomXmlPropsParser domXmlPropsParser = new DomXmlPropsParser();
/* 115 */       Properties properties = domXmlPropsParser.parseXmlProps(bufferedInputStream);
/* 116 */       for (String str1 : properties.keySet())
/*     */       {
/*     */         
/* 119 */         String str2 = properties.getProperty(str1);
/* 120 */         System.err.println(str1 + '=' + str2);
/*     */       }
/*     */     
/* 123 */     } catch (Exception exception) {
/* 124 */       exception.printStackTrace();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/xmlprops/DomXmlPropsParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */