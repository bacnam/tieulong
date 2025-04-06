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
/*    */ public class UnmodifiableTableImpl<E>
/*    */   extends TableView<E>
/*    */ {
/*    */   private static final long serialVersionUID = 1536L;
/*    */   
/*    */   public UnmodifiableTableImpl(TableService<E> target) {
/* 22 */     super(target);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean add(E element) {
/* 27 */     throw new UnsupportedOperationException("Read-Only Collection.");
/*    */   }
/*    */ 
/*    */   
/*    */   public void add(int index, E element) {
/* 32 */     throw new UnsupportedOperationException("Unmodifiable");
/*    */   }
/*    */ 
/*    */   
/*    */   public void clear() {
/* 37 */     throw new UnsupportedOperationException("Read-Only Collection.");
/*    */   }
/*    */ 
/*    */   
/*    */   public int indexOf(Object o) {
/* 42 */     return target().indexOf(o);
/*    */   }
/*    */ 
/*    */   
/*    */   public int lastIndexOf(Object o) {
/* 47 */     return target().lastIndexOf(o);
/*    */   }
/*    */ 
/*    */   
/*    */   public E remove(int index) {
/* 52 */     throw new UnsupportedOperationException("Read-Only Collection.");
/*    */   }
/*    */ 
/*    */   
/*    */   public E set(int index, E element) {
/* 57 */     throw new UnsupportedOperationException("Read-Only Collection.");
/*    */   }
/*    */ 
/*    */   
/*    */   public TableService<E> threadSafe() {
/* 62 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public E get(int index) {
/* 67 */     return (E)target().get(index);
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 72 */     return target().size();
/*    */   }
/*    */ 
/*    */   
/*    */   public Equality<? super E> comparator() {
/* 77 */     return target().comparator();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/table/UnmodifiableTableImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */