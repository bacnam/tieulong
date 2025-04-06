/*    */ package javolution.util.internal.collection;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import javolution.util.FastSet;
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
/*    */ public class DistinctCollectionImpl<E>
/*    */   extends CollectionView<E>
/*    */ {
/*    */   private static final long serialVersionUID = 1536L;
/*    */   
/*    */   private class IteratorImpl
/*    */     implements Iterator<E>
/*    */   {
/*    */     private boolean ahead;
/* 26 */     private final FastSet<E> iterated = new FastSet(DistinctCollectionImpl.this.comparator());
/*    */     private E next;
/* 28 */     private final Iterator<E> targetIterator = DistinctCollectionImpl.this.target().iterator();
/*    */ 
/*    */     
/*    */     public boolean hasNext() {
/* 32 */       if (this.ahead) return true; 
/* 33 */       while (this.targetIterator.hasNext()) {
/* 34 */         this.next = this.targetIterator.next();
/* 35 */         if (!this.iterated.contains(this.next)) {
/* 36 */           this.ahead = true;
/* 37 */           return true;
/*    */         } 
/*    */       } 
/* 40 */       return false;
/*    */     }
/*    */ 
/*    */     
/*    */     public E next() {
/* 45 */       hasNext();
/* 46 */       this.ahead = false;
/* 47 */       return this.next;
/*    */     }
/*    */ 
/*    */     
/*    */     public void remove() {
/* 52 */       this.targetIterator.remove();
/*    */     }
/*    */     
/*    */     private IteratorImpl() {}
/*    */   }
/*    */   
/*    */   public DistinctCollectionImpl(CollectionService<E> target) {
/* 59 */     super(target);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean add(E element) {
/* 64 */     if (target().contains(element)) return false; 
/* 65 */     return target().add(element);
/*    */   }
/*    */ 
/*    */   
/*    */   public void clear() {
/* 70 */     target().clear();
/*    */   }
/*    */ 
/*    */   
/*    */   public Equality<? super E> comparator() {
/* 75 */     return target().comparator();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean contains(Object o) {
/* 80 */     return target().contains(o);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isEmpty() {
/* 85 */     return target().isEmpty();
/*    */   }
/*    */ 
/*    */   
/*    */   public Iterator<E> iterator() {
/* 90 */     return new IteratorImpl();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean remove(Object o) {
/* 95 */     boolean changed = false;
/*    */     while (true) {
/* 97 */       if (!remove(o)) return changed; 
/* 98 */       changed = true;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/collection/DistinctCollectionImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */