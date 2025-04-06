/*    */ package javolution.util.internal.table;
/*    */ 
/*    */ import javolution.util.function.Equality;
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
/*    */ public class ReversedTableImpl<E>
/*    */   extends TableView<E>
/*    */ {
/*    */   private static final long serialVersionUID = 1536L;
/*    */   
/*    */   public ReversedTableImpl(TableService<E> that) {
/* 22 */     super(that);
/*    */   }
/*    */ 
/*    */   
/*    */   public void add(int index, E element) {
/* 27 */     target().add(size() - index - 1, element);
/*    */   }
/*    */ 
/*    */   
/*    */   public E get(int index) {
/* 32 */     return (E)target().get(size() - index - 1);
/*    */   }
/*    */ 
/*    */   
/*    */   public int indexOf(Object o) {
/* 37 */     return size() - target().lastIndexOf(o) - 1;
/*    */   }
/*    */ 
/*    */   
/*    */   public int lastIndexOf(Object o) {
/* 42 */     return size() - target().indexOf(o) - 1;
/*    */   }
/*    */ 
/*    */   
/*    */   public E remove(int index) {
/* 47 */     return (E)target().remove(size() - index - 1);
/*    */   }
/*    */ 
/*    */   
/*    */   public E set(int index, E element) {
/* 52 */     return (E)target().set(size() - index - 1, element);
/*    */   }
/*    */ 
/*    */   
/*    */   public void clear() {
/* 57 */     target().clear();
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 62 */     return target().size();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean add(E e) {
/* 67 */     target().addFirst(e);
/* 68 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public Equality<? super E> comparator() {
/* 73 */     return target().comparator();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/table/ReversedTableImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */