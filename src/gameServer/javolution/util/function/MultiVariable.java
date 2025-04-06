/*    */ package javolution.util.function;
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
/*    */ public class MultiVariable<L, R>
/*    */ {
/*    */   private final L left;
/*    */   private final R right;
/*    */   
/*    */   public MultiVariable(L left, R right) {
/* 37 */     this.left = left;
/* 38 */     this.right = right;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public L getLeft() {
/* 45 */     return this.left;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public R getRight() {
/* 52 */     return this.right;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/function/MultiVariable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */