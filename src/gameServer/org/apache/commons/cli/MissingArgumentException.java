/*    */ package org.apache.commons.cli;
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
/*    */ public class MissingArgumentException
/*    */   extends ParseException
/*    */ {
/*    */   private Option option;
/*    */   
/*    */   public MissingArgumentException(String message) {
/* 40 */     super(message);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MissingArgumentException(Option option) {
/* 52 */     this("Missing argument for option: " + option.getKey());
/* 53 */     this.option = option;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Option getOption() {
/* 65 */     return this.option;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/commons/cli/MissingArgumentException.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */