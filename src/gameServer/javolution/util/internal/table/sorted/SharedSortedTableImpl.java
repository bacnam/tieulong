/*    */ package javolution.util.internal.table.sorted;
/*    */ 
/*    */ import javolution.util.internal.table.SharedTableImpl;
/*    */ import javolution.util.service.CollectionService;
/*    */ import javolution.util.service.SortedTableService;
/*    */ import javolution.util.service.TableService;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SharedSortedTableImpl<E>
/*    */   extends SharedTableImpl<E>
/*    */   implements SortedTableService<E>
/*    */ {
/*    */   private static final long serialVersionUID = 1536L;
/*    */   
/*    */   public SharedSortedTableImpl(SortedTableService<E> target) {
/* 23 */     super((TableService)target);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean addIfAbsent(E element) {
/* 28 */     this.lock.writeLock.lock();
/*    */     try {
/* 30 */       return target().addIfAbsent(element);
/*    */     } finally {
/* 32 */       this.lock.writeLock.unlock();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public int positionOf(E element) {
/* 38 */     this.lock.readLock.lock();
/*    */     try {
/* 40 */       return target().positionOf(element);
/*    */     } finally {
/* 42 */       this.lock.readLock.unlock();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public SortedTableService<E> threadSafe() {
/* 48 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected SortedTableService<E> target() {
/* 54 */     return (SortedTableService<E>)super.target();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/table/sorted/SharedSortedTableImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */