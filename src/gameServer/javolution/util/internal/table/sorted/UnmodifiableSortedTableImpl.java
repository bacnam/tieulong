/*    */ package javolution.util.internal.table.sorted;
/*    */ 
/*    */ import javolution.util.internal.table.UnmodifiableTableImpl;
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
/*    */ public class UnmodifiableSortedTableImpl<E>
/*    */   extends UnmodifiableTableImpl<E>
/*    */   implements SortedTableService<E>
/*    */ {
/*    */   private static final long serialVersionUID = 1536L;
/*    */   
/*    */   public UnmodifiableSortedTableImpl(SortedTableService<E> target) {
/* 23 */     super((TableService)target);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean addIfAbsent(E element) {
/* 28 */     throw new UnsupportedOperationException("Read-Only Collection.");
/*    */   }
/*    */ 
/*    */   
/*    */   public int positionOf(E element) {
/* 33 */     return target().positionOf(element);
/*    */   }
/*    */ 
/*    */   
/*    */   public SortedTableService<E> threadSafe() {
/* 38 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected SortedTableService<E> target() {
/* 44 */     return (SortedTableService<E>)super.target();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/table/sorted/UnmodifiableSortedTableImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */