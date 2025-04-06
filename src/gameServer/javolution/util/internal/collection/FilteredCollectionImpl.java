/*    */ package javolution.util.internal.collection;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import javolution.util.function.Equality;
/*    */ import javolution.util.function.Predicate;
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
/*    */ public class FilteredCollectionImpl<E>
/*    */   extends CollectionView<E>
/*    */ {
/*    */   private static final long serialVersionUID = 1536L;
/*    */   protected final Predicate<? super E> filter;
/*    */   
/*    */   private class IteratorImpl
/*    */     implements Iterator<E>
/*    */   {
/*    */     private boolean ahead;
/*    */     private final Predicate<? super E> filter;
/*    */     private E next;
/*    */     private final Iterator<E> targetIterator;
/*    */     
/*    */     public IteratorImpl(Predicate<? super E> filter) {
/* 31 */       this.filter = filter;
/* 32 */       this.targetIterator = FilteredCollectionImpl.this.target().iterator();
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean hasNext() {
/* 37 */       if (this.ahead) return true; 
/* 38 */       while (this.targetIterator.hasNext()) {
/* 39 */         this.next = this.targetIterator.next();
/* 40 */         if (this.filter.test(this.next)) {
/* 41 */           this.ahead = true;
/* 42 */           return true;
/*    */         } 
/*    */       } 
/* 45 */       return false;
/*    */     }
/*    */ 
/*    */     
/*    */     public E next() {
/* 50 */       hasNext();
/* 51 */       this.ahead = false;
/* 52 */       return this.next;
/*    */     }
/*    */ 
/*    */     
/*    */     public void remove() {
/* 57 */       this.targetIterator.remove();
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public FilteredCollectionImpl(CollectionService<E> target, Predicate<? super E> filter) {
/* 66 */     super(target);
/* 67 */     this.filter = filter;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean add(E element) {
/* 72 */     if (!this.filter.test(element)) return false; 
/* 73 */     return target().add(element);
/*    */   }
/*    */ 
/*    */   
/*    */   public Equality<? super E> comparator() {
/* 78 */     return target().comparator();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean contains(Object o) {
/* 84 */     if (!this.filter.test(o)) return false; 
/* 85 */     return target().contains(o);
/*    */   }
/*    */ 
/*    */   
/*    */   public Iterator<E> iterator() {
/* 90 */     return new IteratorImpl(this.filter);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean remove(Object o) {
/* 96 */     if (!this.filter.test(o)) return false; 
/* 97 */     return target().remove(o);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/collection/FilteredCollectionImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */