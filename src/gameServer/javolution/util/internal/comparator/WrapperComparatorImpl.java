/*    */ package javolution.util.internal.comparator;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.Comparator;
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
/*    */ 
/*    */ public final class WrapperComparatorImpl<E>
/*    */   implements Equality<E>, Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 8775282553794347279L;
/*    */   private final Comparator<? super E> comparator;
/*    */   
/*    */   public WrapperComparatorImpl(Comparator<? super E> comparator) {
/* 28 */     this.comparator = comparator;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCodeOf(E obj) {
/* 33 */     throw new UnsupportedOperationException("Standard comparator (java.util.Comparator) cannot be used for hashcode calculations; please use a coherent equality comparator instead (e.g. javolution.util.function.Equality).");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean areEqual(E e1, E e2) {
/* 41 */     return (e1 == e2 || (e1 != null && this.comparator.compare(e1, e2) == 0));
/*    */   }
/*    */ 
/*    */   
/*    */   public int compare(E left, E right) {
/* 46 */     if (left == right)
/* 47 */       return 0; 
/* 48 */     if (left == null)
/* 49 */       return -1; 
/* 50 */     if (right == null)
/* 51 */       return 1; 
/* 52 */     return this.comparator.compare(left, right);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/comparator/WrapperComparatorImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */