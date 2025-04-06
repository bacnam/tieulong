/*    */ package com.mchange.v1.xml;
/*    */ 
/*    */ import org.xml.sax.ErrorHandler;
/*    */ import org.xml.sax.SAXException;
/*    */ import org.xml.sax.SAXParseException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StdErrErrorHandler
/*    */   implements ErrorHandler
/*    */ {
/*    */   public void warning(SAXParseException paramSAXParseException) {
/* 44 */     System.err.println("[Warning]");
/* 45 */     showExceptionInformation(paramSAXParseException);
/* 46 */     paramSAXParseException.printStackTrace();
/*    */   }
/*    */ 
/*    */   
/*    */   public void error(SAXParseException paramSAXParseException) {
/* 51 */     System.err.println("[Error]");
/* 52 */     showExceptionInformation(paramSAXParseException);
/* 53 */     paramSAXParseException.printStackTrace();
/*    */   }
/*    */ 
/*    */   
/*    */   public void fatalError(SAXParseException paramSAXParseException) throws SAXException {
/* 58 */     System.err.println("[Fatal Error]");
/* 59 */     showExceptionInformation(paramSAXParseException);
/* 60 */     paramSAXParseException.printStackTrace();
/* 61 */     throw paramSAXParseException;
/*    */   }
/*    */ 
/*    */   
/*    */   private void showExceptionInformation(SAXParseException paramSAXParseException) {
/* 66 */     System.err.println("[\tLine Number: " + paramSAXParseException.getLineNumber() + ']');
/* 67 */     System.err.println("[\tColumn Number: " + paramSAXParseException.getColumnNumber() + ']');
/* 68 */     System.err.println("[\tPublic ID: " + paramSAXParseException.getPublicId() + ']');
/* 69 */     System.err.println("[\tSystem ID: " + paramSAXParseException.getSystemId() + ']');
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/xml/StdErrErrorHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */