/*    */ package javolution.util.internal.comparator;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import javolution.util.function.Equality;
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
/*    */ public class IdentityComparatorImpl<E>
/*    */   implements Equality<E>, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 6576306094743751922L;
/*    */   
/*    */   public int hashCodeOf(E obj) {
/* 25 */     return System.identityHashCode(obj);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean areEqual(E e1, E e2) {
/* 30 */     return (e1 == e2);
/*    */   }
/*    */ 
/*    */   
/*    */   public int compare(E left, E right) {
/* 35 */     if (left == right)
/* 36 */       return 0; 
/* 37 */     if (left == null)
/* 38 */       return -1; 
/* 39 */     if (right == null) {
/* 40 */       return 1;
/*    */     }
/*    */     
/* 43 */     return (hashCodeOf(left) < hashCodeOf(right)) ? -1 : 1;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/comparator/IdentityComparatorImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */