/*    */ package javolution.util.internal.table.sorted;
/*    */ 
/*    */ import javolution.util.internal.table.TableView;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class SortedTableView<E>
/*    */   extends TableView<E>
/*    */   implements SortedTableService<E>
/*    */ {
/*    */   private static final long serialVersionUID = 1536L;
/*    */   
/*    */   public SortedTableView(SortedTableService<E> target) {
/* 27 */     super((TableService)target);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean addIfAbsent(E element) {
/* 32 */     if (!contains(element)) return add(element); 
/* 33 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int indexOf(Object o) {
/* 39 */     int i = positionOf((E)o);
/* 40 */     if (i >= size() || !comparator().areEqual(o, get(i))) return -1; 
/* 41 */     return i;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int lastIndexOf(Object o) {
/* 47 */     int i = positionOf((E)o);
/* 48 */     int result = -1;
/* 49 */     while (i < size() && comparator().areEqual(o, get(i))) {
/* 50 */       result = i++;
/*    */     }
/* 52 */     return result;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public SortedTableService<E> threadSafe() {
/* 60 */     return new SharedSortedTableImpl<E>(this);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected SortedTableService<E> target() {
/* 66 */     return (SortedTableService<E>)super.target();
/*    */   }
/*    */   
/*    */   public abstract int positionOf(E paramE);
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/table/sorted/SortedTableView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */