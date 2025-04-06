/*    */ package javolution.util.internal.set.sorted;
/*    */ 
/*    */ import java.util.Comparator;
/*    */ import java.util.SortedSet;
/*    */ import javolution.util.internal.set.UnmodifiableSetImpl;
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
/*    */ 
/*    */ public class UnmodifiableSortedSetImpl<E>
/*    */   extends UnmodifiableSetImpl<E>
/*    */   implements SortedSetService<E>
/*    */ {
/*    */   private static final long serialVersionUID = 1536L;
/*    */   
/*    */   public UnmodifiableSortedSetImpl(SetService<E> target) {
/* 24 */     super(target);
/*    */   }
/*    */ 
/*    */   
/*    */   public E first() {
/* 29 */     return (E)target().first();
/*    */   }
/*    */ 
/*    */   
/*    */   public SortedSetService<E> headSet(E toElement) {
/* 34 */     return new SubSortedSetImpl<E>(this, null, toElement);
/*    */   }
/*    */ 
/*    */   
/*    */   public E last() {
/* 39 */     return (E)target().last();
/*    */   }
/*    */ 
/*    */   
/*    */   public SortedSetService<E> subSet(E fromElement, E toElement) {
/* 44 */     return new SubSortedSetImpl<E>(this, fromElement, toElement);
/*    */   }
/*    */ 
/*    */   
/*    */   public SortedSetService<E> tailSet(E fromElement) {
/* 49 */     return new SubSortedSetImpl<E>(this, fromElement, null);
/*    */   }
/*    */ 
/*    */   
/*    */   public SortedSetService<E> threadSafe() {
/* 54 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SortedSetService<E> target() {
/* 59 */     return (SortedSetService<E>)super.target();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/set/sorted/UnmodifiableSortedSetImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */