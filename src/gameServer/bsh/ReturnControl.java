/*    */ package bsh;
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
/*    */ class ReturnControl
/*    */   implements ParserConstants
/*    */ {
/*    */   public int kind;
/*    */   public Object value;
/*    */   public SimpleNode returnPoint;
/*    */   
/*    */   public ReturnControl(int kind, Object value, SimpleNode returnPoint) {
/* 47 */     this.kind = kind;
/* 48 */     this.value = value;
/* 49 */     this.returnPoint = returnPoint;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/ReturnControl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */