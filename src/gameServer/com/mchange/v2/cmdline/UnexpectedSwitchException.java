/*    */ package com.mchange.v2.cmdline;
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
/*    */ public class UnexpectedSwitchException
/*    */   extends BadCommandLineException
/*    */ {
/*    */   String sw;
/*    */   
/*    */   UnexpectedSwitchException(String paramString1, String paramString2) {
/* 44 */     super(paramString1);
/* 45 */     this.sw = paramString2;
/*    */   }
/*    */   
/*    */   public String getUnexpectedSwitch() {
/* 49 */     return this.sw;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/cmdline/UnexpectedSwitchException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */