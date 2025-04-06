/*    */ package javolution.util.internal.table.sorted;
/*    */ 
/*    */ import javolution.util.function.Equality;
/*    */ import javolution.util.internal.table.FastTableImpl;
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
/*    */ public class FastSortedTableImpl<E>
/*    */   extends FastTableImpl<E>
/*    */   implements SortedTableService<E>
/*    */ {
/*    */   private static final long serialVersionUID = 1536L;
/*    */   
/*    */   public FastSortedTableImpl(Equality<? super E> comparator) {
/* 24 */     super(comparator);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean add(E element) {
/* 29 */     add(positionOf(element), element);
/* 30 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean addIfAbsent(E element) {
/* 35 */     int i = positionOf(element);
/* 36 */     if (i < size() && comparator().areEqual(element, get(i))) return false; 
/* 37 */     add(i, element);
/* 38 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int indexOf(Object element) {
/* 44 */     int i = positionOf((E)element);
/* 45 */     if (i >= size() || !comparator().areEqual(get(i), element)) return -1; 
/* 46 */     return i;
/*    */   }
/*    */ 
/*    */   
/*    */   public int positionOf(E element) {
/* 51 */     return positionOf(element, 0, size());
/*    */   }
/*    */ 
/*    */   
/*    */   public SortedTableService<E> threadSafe() {
/* 56 */     return new SharedSortedTableImpl<E>(this);
/*    */   }
/*    */   
/*    */   private int positionOf(E element, int start, int length) {
/* 60 */     if (length == 0) return start; 
/* 61 */     int half = length >> 1;
/* 62 */     return (comparator().compare(element, get(start + half)) <= 0) ? positionOf(element, start, half) : positionOf(element, start + half + 1, length - half - 1);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/table/sorted/FastSortedTableImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */