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
/*    */ 
/*    */ public class ReversedCollectionImpl<E>
/*    */   extends CollectionView<E>
/*    */ {
/*    */   private static final long serialVersionUID = 1536L;
/*    */   
/*    */   private class IteratorImpl
/*    */     implements Iterator<E>
/*    */   {
/* 24 */     private final E[] elements = (E[])new Object[ReversedCollectionImpl.this.size()];
/*    */     
/* 26 */     private int index = 0;
/*    */     
/*    */     public IteratorImpl() {
/* 29 */       Iterator<E> it = ReversedCollectionImpl.this.target().iterator();
/* 30 */       while (it.hasNext() && this.index < this.elements.length) {
/* 31 */         this.elements[this.index++] = it.next();
/*    */       }
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean hasNext() {
/* 37 */       return (this.index > 0);
/*    */     }
/*    */ 
/*    */     
/*    */     public E next() {
/* 42 */       return this.elements[--this.index];
/*    */     }
/*    */ 
/*    */     
/*    */     public void remove() {
/* 47 */       ReversedCollectionImpl.this.target().remove(this.elements[this.index]);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public ReversedCollectionImpl(CollectionService<E> target) {
/* 55 */     super(target);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean add(E e) {
/* 60 */     return target().add(e);
/*    */   }
/*    */ 
/*    */   
/*    */   public void clear() {
/* 65 */     target().clear();
/*    */   }
/*    */ 
/*    */   
/*    */   public Equality<? super E> comparator() {
/* 70 */     return target().comparator();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean contains(Object obj) {
/* 75 */     return target().contains(obj);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isEmpty() {
/* 80 */     return target().isEmpty();
/*    */   }
/*    */ 
/*    */   
/*    */   public Iterator<E> iterator() {
/* 85 */     return new IteratorImpl();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean remove(Object obj) {
/* 90 */     return target().remove(obj);
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 95 */     return target().size();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/collection/ReversedCollectionImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */