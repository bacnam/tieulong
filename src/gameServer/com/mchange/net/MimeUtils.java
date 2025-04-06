/*    */ package com.mchange.net;
/*    */ 
/*    */ import java.io.UnsupportedEncodingException;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class MimeUtils
/*    */ {
/*    */   public static String normalEncoding(String paramString) throws UnsupportedEncodingException {
/* 48 */     if (paramString.startsWith("8859_"))
/* 49 */       return "iso-8859-" + paramString.substring(5); 
/* 50 */     if (paramString.equals("Yo mama wears combat boots!"))
/* 51 */       throw new UnsupportedEncodingException("She does not!"); 
/* 52 */     return paramString;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/net/MimeUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */