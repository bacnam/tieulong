/*    */ package javolution.util.internal.collection;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import javolution.util.function.Consumer;
/*    */ import javolution.util.function.Equality;
/*    */ import javolution.util.service.CollectionService;
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
/*    */ public class SequentialCollectionImpl<E>
/*    */   extends CollectionView<E>
/*    */ {
/*    */   private static final long serialVersionUID = 1536L;
/*    */   
/*    */   public SequentialCollectionImpl(CollectionService<E> target) {
/* 25 */     super(target);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean add(E e) {
/* 30 */     return target().add(e);
/*    */   }
/*    */ 
/*    */   
/*    */   public void clear() {
/* 35 */     target().clear();
/*    */   }
/*    */ 
/*    */   
/*    */   public Equality<? super E> comparator() {
/* 40 */     return target().comparator();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean contains(Object obj) {
/* 45 */     return target().contains(obj);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isEmpty() {
/* 50 */     return target().isEmpty();
/*    */   }
/*    */ 
/*    */   
/*    */   public Iterator<E> iterator() {
/* 55 */     return target().iterator();
/*    */   }
/*    */ 
/*    */   
/*    */   public void perform(Consumer<CollectionService<E>> action, CollectionService<E> view) {
/* 60 */     action.accept(view);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean remove(Object obj) {
/* 65 */     return target().remove(obj);
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 70 */     return target().size();
/*    */   }
/*    */ 
/*    */   
/*    */   public void update(Consumer<CollectionService<E>> action, CollectionService<E> view) {
/* 75 */     action.accept(view);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/collection/SequentialCollectionImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */