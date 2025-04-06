/*    */ package javolution.util.internal.set.sorted;
/*    */ 
/*    */ import java.util.Comparator;
/*    */ import java.util.SortedSet;
/*    */ import javolution.util.internal.set.SharedSetImpl;
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
/*    */ public class SharedSortedSetImpl<E>
/*    */   extends SharedSetImpl<E>
/*    */   implements SortedSetService<E>
/*    */ {
/*    */   private static final long serialVersionUID = 1536L;
/*    */   
/*    */   public SharedSortedSetImpl(SetService<E> target) {
/* 24 */     super(target);
/*    */   }
/*    */ 
/*    */   
/*    */   public E first() {
/* 29 */     this.lock.readLock.lock();
/*    */     try {
/* 31 */       return (E)target().first();
/*    */     } finally {
/* 33 */       this.lock.readLock.unlock();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public SortedSetService<E> headSet(E toElement) {
/* 39 */     return new SubSortedSetImpl<E>(this, null, toElement);
/*    */   }
/*    */ 
/*    */   
/*    */   public E last() {
/* 44 */     this.lock.readLock.lock();
/*    */     try {
/* 46 */       return (E)target().last();
/*    */     } finally {
/* 48 */       this.lock.readLock.unlock();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public SortedSetService<E> subSet(E fromElement, E toElement) {
/* 54 */     return new SubSortedSetImpl<E>(this, fromElement, toElement);
/*    */   }
/*    */ 
/*    */   
/*    */   public SortedSetService<E> tailSet(E fromElement) {
/* 59 */     return new SubSortedSetImpl<E>(this, fromElement, null);
/*    */   }
/*    */ 
/*    */   
/*    */   public SortedSetService<E> threadSafe() {
/* 64 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SortedSetService<E> target() {
/* 69 */     return (SortedSetService<E>)super.target();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/set/sorted/SharedSortedSetImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */