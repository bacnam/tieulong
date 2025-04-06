/*     */ package com.mchange.v1.xml;
/*     */ 
/*     */ import com.mchange.v1.util.DebugUtils;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.DOMException;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class DomParseUtils
/*     */ {
/*     */   static final boolean DEBUG = true;
/*     */   
/*     */   public static String allTextFromUniqueChild(Element paramElement, String paramString) throws DOMException {
/*  52 */     return allTextFromUniqueChild(paramElement, paramString, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String allTextFromUniqueChild(Element paramElement, String paramString, boolean paramBoolean) throws DOMException {
/*  60 */     Element element = uniqueChildByTagName(paramElement, paramString);
/*  61 */     if (element == null) {
/*  62 */       return null;
/*     */     }
/*  64 */     return allTextFromElement(element, paramBoolean);
/*     */   }
/*     */   
/*     */   public static Element uniqueChild(Element paramElement, String paramString) throws DOMException {
/*  68 */     return uniqueChildByTagName(paramElement, paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Element uniqueChildByTagName(Element paramElement, String paramString) throws DOMException {
/*  75 */     NodeList nodeList = paramElement.getElementsByTagName(paramString);
/*  76 */     int i = nodeList.getLength();
/*     */     
/*  78 */     DebugUtils.myAssert((i <= 1), "There is more than one (" + i + ") child with tag name: " + paramString + "!!!");
/*     */ 
/*     */     
/*  81 */     return (i == 1) ? (Element)nodeList.item(0) : null;
/*     */   }
/*     */   
/*     */   public static String allText(Element paramElement) throws DOMException {
/*  85 */     return allTextFromElement(paramElement);
/*     */   }
/*     */   public static String allText(Element paramElement, boolean paramBoolean) throws DOMException {
/*  88 */     return allTextFromElement(paramElement, paramBoolean);
/*     */   }
/*     */   
/*     */   public static String allTextFromElement(Element paramElement) throws DOMException {
/*  92 */     return allTextFromElement(paramElement, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String allTextFromElement(Element paramElement, boolean paramBoolean) throws DOMException {
/*  97 */     StringBuffer stringBuffer = new StringBuffer();
/*  98 */     NodeList nodeList = paramElement.getChildNodes(); byte b; int i;
/*  99 */     for (b = 0, i = nodeList.getLength(); b < i; b++) {
/*     */       
/* 101 */       Node node = nodeList.item(b);
/* 102 */       if (node instanceof org.w3c.dom.Text)
/* 103 */         stringBuffer.append(node.getNodeValue()); 
/*     */     } 
/* 105 */     String str = stringBuffer.toString();
/* 106 */     return paramBoolean ? str.trim() : str;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String[] allTextFromImmediateChildElements(Element paramElement, String paramString) throws DOMException {
/* 111 */     return allTextFromImmediateChildElements(paramElement, paramString, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String[] allTextFromImmediateChildElements(Element paramElement, String paramString, boolean paramBoolean) throws DOMException {
/* 116 */     NodeList nodeList = immediateChildElementsByTagName(paramElement, paramString);
/* 117 */     int i = nodeList.getLength();
/* 118 */     String[] arrayOfString = new String[i];
/* 119 */     for (byte b = 0; b < i; b++)
/* 120 */       arrayOfString[b] = allText((Element)nodeList.item(b), paramBoolean); 
/* 121 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static NodeList immediateChildElementsByTagName(Element paramElement, String paramString) throws DOMException {
/* 127 */     return getImmediateChildElementsByTagName(paramElement, paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static NodeList getImmediateChildElementsByTagName(Element paramElement, String paramString) throws DOMException {
/* 135 */     final ArrayList<Node> nodes = new ArrayList();
/* 136 */     for (Node node = paramElement.getFirstChild(); node != null; node = node.getNextSibling()) {
/* 137 */       if (node instanceof Element && ((Element)node).getTagName().equals(paramString))
/* 138 */         arrayList.add(node); 
/* 139 */     }  return new NodeList()
/*     */       {
/*     */         public int getLength() {
/* 142 */           return nodes.size();
/*     */         }
/*     */         public Node item(int param1Int) {
/* 145 */           return nodes.get(param1Int);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */   
/*     */   public static String allTextFromUniqueImmediateChild(Element paramElement, String paramString) throws DOMException {
/* 152 */     Element element = uniqueImmediateChildByTagName(paramElement, paramString);
/* 153 */     if (element == null)
/* 154 */       return null; 
/* 155 */     return allTextFromElement(element);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Element uniqueImmediateChild(Element paramElement, String paramString) throws DOMException {
/* 160 */     return uniqueImmediateChildByTagName(paramElement, paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Element uniqueImmediateChildByTagName(Element paramElement, String paramString) throws DOMException {
/* 168 */     NodeList nodeList = getImmediateChildElementsByTagName(paramElement, paramString);
/* 169 */     int i = nodeList.getLength();
/*     */     
/* 171 */     DebugUtils.myAssert((i <= 1), "There is more than one (" + i + ") child with tag name: " + paramString + "!!!");
/*     */ 
/*     */     
/* 174 */     return (i == 1) ? (Element)nodeList.item(0) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String attrValFromElement(Element paramElement, String paramString) throws DOMException {
/* 183 */     Attr attr = paramElement.getAttributeNode(paramString);
/* 184 */     return (attr == null) ? null : attr.getValue();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/xml/DomParseUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */