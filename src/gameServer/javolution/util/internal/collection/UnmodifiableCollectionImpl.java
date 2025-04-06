/*    */ package javolution.util.internal.collection;
/*    */ 
/*    */ import java.util.Iterator;
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
/*    */ public class UnmodifiableCollectionImpl<E>
/*    */   extends CollectionView<E>
/*    */ {
/*    */   private static final long serialVersionUID = 1536L;
/*    */   
/*    */   private class IteratorImpl
/*    */     implements Iterator<E>
/*    */   {
/* 23 */     private final Iterator<E> targetIterator = UnmodifiableCollectionImpl.this.target().iterator();
/*    */ 
/*    */     
/*    */     public boolean hasNext() {
/* 27 */       return this.targetIterator.hasNext();
/*    */     }
/*    */ 
/*    */     
/*    */     public E next() {
/* 32 */       return this.targetIterator.next();
/*    */     }
/*    */ 
/*    */     
/*    */     public void remove() {
/* 37 */       throw new UnsupportedOperationException("Read-Only Collection.");
/*    */     }
/*    */     
/*    */     private IteratorImpl() {}
/*    */   }
/*    */   
/*    */   public UnmodifiableCollectionImpl(CollectionService<E> target) {
/* 44 */     super(target);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean add(E element) {
/* 49 */     throw new UnsupportedOperationException("Read-Only Collection.");
/*    */   }
/*    */ 
/*    */   
/*    */   public void clear() {
/* 54 */     throw new UnsupportedOperationException("Read-Only Collection.");
/*    */   }
/*    */ 
/*    */   
/*    */   public Equality<? super E> comparator() {
/* 59 */     return target().comparator();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean contains(Object obj) {
/* 64 */     return target().contains(obj);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isEmpty() {
/* 69 */     return target().isEmpty();
/*    */   }
/*    */ 
/*    */   
/*    */   public Iterator<E> iterator() {
/* 74 */     return new IteratorImpl();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean remove(Object o) {
/* 79 */     throw new UnsupportedOperationException("Read-Only Collection.");
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 84 */     return target().size();
/*    */   }
/*    */ 
/*    */   
/*    */   public CollectionService<E> threadSafe() {
/* 89 */     return this;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/collection/UnmodifiableCollectionImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */