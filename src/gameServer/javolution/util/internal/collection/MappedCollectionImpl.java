/*    */ package javolution.util.internal.collection;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import javolution.util.function.Equalities;
/*    */ import javolution.util.function.Equality;
/*    */ import javolution.util.function.Function;
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
/*    */ public class MappedCollectionImpl<E, R>
/*    */   extends CollectionView<R>
/*    */ {
/*    */   private static final long serialVersionUID = 1536L;
/*    */   protected final Function<? super E, ? extends R> function;
/*    */   
/*    */   private class IteratorImpl
/*    */     implements Iterator<R>
/*    */   {
/* 29 */     private final Iterator<E> targetIterator = MappedCollectionImpl.this.target().iterator();
/*    */ 
/*    */ 
/*    */     
/*    */     public boolean hasNext() {
/* 34 */       return this.targetIterator.hasNext();
/*    */     }
/*    */ 
/*    */     
/*    */     public R next() {
/* 39 */       return (R)MappedCollectionImpl.this.function.apply(this.targetIterator.next());
/*    */     }
/*    */ 
/*    */     
/*    */     public void remove() {
/* 44 */       this.targetIterator.remove();
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MappedCollectionImpl(CollectionService<E> target, Function<? super E, ? extends R> function) {
/* 55 */     super(target);
/* 56 */     this.function = function;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean add(R element) {
/* 61 */     throw new UnsupportedOperationException("New elements cannot be added to mapped views");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void clear() {
/* 67 */     target().clear();
/*    */   }
/*    */ 
/*    */   
/*    */   public Equality<? super R> comparator() {
/* 72 */     return Equalities.STANDARD;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isEmpty() {
/* 77 */     return target().isEmpty();
/*    */   }
/*    */ 
/*    */   
/*    */   public Iterator<R> iterator() {
/* 82 */     return new IteratorImpl();
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 87 */     return target().size();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/collection/MappedCollectionImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */