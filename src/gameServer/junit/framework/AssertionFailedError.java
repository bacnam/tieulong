/*    */ package junit.framework;
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
/*    */ public class AssertionFailedError
/*    */   extends AssertionError
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public AssertionFailedError() {}
/*    */   
/*    */   public AssertionFailedError(String message) {
/* 23 */     super(defaultString(message));
/*    */   }
/*    */   
/*    */   private static String defaultString(String message) {
/* 27 */     return (message == null) ? "" : message;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/junit/framework/AssertionFailedError.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */