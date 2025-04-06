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
/*    */ public class MissingSwitchException
/*    */   extends BadCommandLineException
/*    */ {
/*    */   String sw;
/*    */   
/*    */   MissingSwitchException(String paramString1, String paramString2) {
/* 44 */     super(paramString1);
/* 45 */     this.sw = paramString2;
/*    */   }
/*    */   
/*    */   public String getMissingSwitch() {
/* 49 */     return this.sw;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/cmdline/MissingSwitchException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */