/*    */ package javolution.util.internal.table;
/*    */ 
/*    */ import javolution.util.function.Equality;
/*    */ import javolution.util.service.CollectionService;
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
/*    */ public class SubTableImpl<E>
/*    */   extends TableView<E>
/*    */ {
/*    */   private static final long serialVersionUID = 1536L;
/*    */   private final int fromIndex;
/*    */   private int toIndex;
/*    */   
/*    */   public static <E> CollectionService<E>[] splitOf(TableService<E> table, int n) {
/* 26 */     if (n <= 1) throw new IllegalArgumentException("Invalid argument n: " + n);
/*    */     
/* 28 */     CollectionService[] arrayOfCollectionService = new CollectionService[n];
/* 29 */     int minSize = table.size() / n;
/* 30 */     int start = 0;
/* 31 */     for (int i = 0; i < n - 1; i++) {
/* 32 */       arrayOfCollectionService[i] = (CollectionService)new SubTableImpl<E>(table, start, start + minSize);
/* 33 */       start += minSize;
/*    */     } 
/* 35 */     arrayOfCollectionService[n - 1] = (CollectionService)new SubTableImpl<E>(table, start, table.size());
/* 36 */     return (CollectionService<E>[])arrayOfCollectionService;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public SubTableImpl(TableService<E> target, int from, int to) {
/* 43 */     super(target);
/* 44 */     if (from < 0 || to > target.size() || from > to) throw new IndexOutOfBoundsException("fromIndex: " + from + ", toIndex: " + to + ", size(): " + target.size());
/*    */ 
/*    */     
/* 47 */     this.fromIndex = from;
/* 48 */     this.toIndex = to;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean add(E element) {
/* 53 */     target().add(this.toIndex++, element);
/* 54 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void add(int index, E element) {
/* 59 */     if (index < 0 && index > size()) indexError(index); 
/* 60 */     target().add(index + this.fromIndex, element);
/* 61 */     this.toIndex++;
/*    */   }
/*    */ 
/*    */   
/*    */   public void clear() {
/* 66 */     for (int i = this.toIndex - 1; i >= this.fromIndex; i--) {
/* 67 */       target().remove(i);
/*    */     }
/* 69 */     this.toIndex = this.fromIndex;
/*    */   }
/*    */ 
/*    */   
/*    */   public E get(int index) {
/* 74 */     if (index < 0 && index >= size()) indexError(index); 
/* 75 */     return (E)target().get(index + this.fromIndex);
/*    */   }
/*    */ 
/*    */   
/*    */   public E remove(int index) {
/* 80 */     if (index < 0 && index >= size()) indexError(index); 
/* 81 */     this.toIndex--;
/* 82 */     return (E)target().remove(index + this.fromIndex);
/*    */   }
/*    */ 
/*    */   
/*    */   public E set(int index, E element) {
/* 87 */     if (index < 0 && index >= size()) indexError(index); 
/* 88 */     return (E)target().set(index + this.fromIndex, element);
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 93 */     return this.toIndex - this.fromIndex;
/*    */   }
/*    */ 
/*    */   
/*    */   public Equality<? super E> comparator() {
/* 98 */     return target().comparator();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/table/SubTableImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */