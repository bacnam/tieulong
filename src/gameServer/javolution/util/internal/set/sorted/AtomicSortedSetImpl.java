/*    */ package javolution.util.internal.set.sorted;
/*    */ 
/*    */ import java.util.Comparator;
/*    */ import java.util.SortedSet;
/*    */ import javolution.util.internal.set.AtomicSetImpl;
/*    */ import javolution.util.service.CollectionService;
/*    */ import javolution.util.service.SetService;
/*    */ import javolution.util.service.SortedSetService;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AtomicSortedSetImpl<E>
/*    */   extends AtomicSetImpl<E>
/*    */   implements SortedSetService<E>
/*    */ {
/*    */   private static final long serialVersionUID = 1536L;
/*    */   
/*    */   public AtomicSortedSetImpl(SortedSetService<E> target) {
/* 23 */     super((SetService)target);
/*    */   }
/*    */ 
/*    */   
/*    */   public E first() {
/* 28 */     return (E)targetView().first();
/*    */   }
/*    */ 
/*    */   
/*    */   public SortedSetService<E> headSet(E toElement) {
/* 33 */     return new SubSortedSetImpl<E>(this, null, toElement);
/*    */   }
/*    */ 
/*    */   
/*    */   public E last() {
/* 38 */     return (E)targetView().last();
/*    */   }
/*    */ 
/*    */   
/*    */   public SortedSetService<E> subSet(E fromElement, E toElement) {
/* 43 */     return new SubSortedSetImpl<E>(this, fromElement, toElement);
/*    */   }
/*    */ 
/*    */   
/*    */   public SortedSetService<E> tailSet(E fromElement) {
/* 48 */     return new SubSortedSetImpl<E>(this, fromElement, null);
/*    */   }
/*    */ 
/*    */   
/*    */   public SortedSetService<E> threadSafe() {
/* 53 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SortedSetService<E> targetView() {
/* 58 */     return (SortedSetService<E>)super.targetView();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/set/sorted/AtomicSortedSetImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */