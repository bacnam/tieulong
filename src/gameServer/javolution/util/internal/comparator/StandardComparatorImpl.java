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
/*    */ public class StandardComparatorImpl<E>
/*    */   implements Equality<E>, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = -615690677813206151L;
/*    */   
/*    */   public int hashCodeOf(E e) {
/* 25 */     return (e == null) ? 0 : e.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean areEqual(E e1, E e2) {
/* 30 */     return (e1 == e2 || (e1 != null && e1.equals(e2)));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int compare(E left, E right) {
/* 36 */     if (left == right)
/* 37 */       return 0; 
/* 38 */     if (left == null)
/* 39 */       return -1; 
/* 40 */     if (right == null)
/* 41 */       return 1; 
/* 42 */     if (left instanceof Comparable) {
/* 43 */       return ((Comparable<E>)left).compareTo(right);
/*    */     }
/*    */     
/* 46 */     if (left.equals(right))
/* 47 */       return 0; 
/* 48 */     return (left.hashCode() < right.hashCode()) ? -1 : 1;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/comparator/StandardComparatorImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */