/*    */ package javolution.util.internal.table.sorted;
/*    */ 
/*    */ import javolution.util.internal.table.AtomicTableImpl;
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
/*    */ 
/*    */ public class AtomicSortedTableImpl<E>
/*    */   extends AtomicTableImpl<E>
/*    */   implements SortedTableService<E>
/*    */ {
/*    */   private static final long serialVersionUID = 1536L;
/*    */   
/*    */   public AtomicSortedTableImpl(TableService<E> target) {
/* 24 */     super(target);
/*    */   }
/*    */ 
/*    */   
/*    */   public synchronized boolean addIfAbsent(E element) {
/* 29 */     boolean changed = target().addIfAbsent(element);
/* 30 */     if (changed && !updateInProgress()) this.immutable = cloneTarget(); 
/* 31 */     return changed;
/*    */   }
/*    */ 
/*    */   
/*    */   public int positionOf(E element) {
/* 36 */     return targetView().positionOf(element);
/*    */   }
/*    */ 
/*    */   
/*    */   public SortedTableService<E> threadSafe() {
/* 41 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   protected SortedTableService<E> targetView() {
/* 46 */     return (SortedTableService<E>)super.targetView();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected SortedTableService<E> target() {
/* 52 */     return (SortedTableService<E>)super.target();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/table/sorted/AtomicSortedTableImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */