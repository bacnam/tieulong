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
/*    */ public class UnexpectedSwitchArgumentException
/*    */   extends BadCommandLineException
/*    */ {
/*    */   String sw;
/*    */   String arg;
/*    */   
/*    */   UnexpectedSwitchArgumentException(String paramString1, String paramString2, String paramString3) {
/* 45 */     super(paramString1);
/* 46 */     this.sw = paramString2;
/* 47 */     this.arg = paramString3;
/*    */   }
/*    */   
/*    */   public String getSwitch() {
/* 51 */     return this.sw;
/*    */   }
/*    */   public String getUnexpectedArgument() {
/* 54 */     return this.arg;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/cmdline/UnexpectedSwitchArgumentException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */