/*     */ package javolution.util.internal.collection;
/*     */ 
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import javolution.util.FastTable;
/*     */ import javolution.util.function.Equality;
/*     */ import javolution.util.internal.comparator.WrapperComparatorImpl;
/*     */ import javolution.util.service.CollectionService;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SortedCollectionImpl<E>
/*     */   extends CollectionView<E>
/*     */ {
/*     */   private static final long serialVersionUID = 1536L;
/*     */   protected final Equality<E> comparator;
/*     */   
/*     */   private class IteratorImpl
/*     */     implements Iterator<E>
/*     */   {
/*     */     private final Iterator<E> iterator;
/*     */     private E next;
/*     */     
/*     */     public IteratorImpl() {
/*  30 */       FastTable<E> sorted = new FastTable(SortedCollectionImpl.this.comparator);
/*  31 */       Iterator<E> it = SortedCollectionImpl.this.target().iterator();
/*  32 */       while (it.hasNext()) {
/*  33 */         sorted.add(it.next());
/*     */       }
/*  35 */       sorted.sort();
/*  36 */       this.iterator = sorted.iterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/*  41 */       return this.iterator.hasNext();
/*     */     }
/*     */ 
/*     */     
/*     */     public E next() {
/*  46 */       this.next = this.iterator.next();
/*  47 */       return this.next;
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/*  52 */       if (this.next == null) throw new IllegalStateException(); 
/*  53 */       SortedCollectionImpl.this.target().remove(this.next);
/*  54 */       this.next = null;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SortedCollectionImpl(CollectionService<E> target, Comparator<? super E> comparator) {
/*  64 */     super(target);
/*  65 */     this.comparator = (comparator instanceof Equality) ? (Equality)comparator : (Equality<E>)new WrapperComparatorImpl(comparator);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean add(E e) {
/*  71 */     return target().add(e);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/*  76 */     target().clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public Equality<? super E> comparator() {
/*  81 */     return this.comparator;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Object obj) {
/*  86 */     return target().contains(obj);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  91 */     return target().isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<E> iterator() {
/*  96 */     return new IteratorImpl();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean remove(Object obj) {
/* 101 */     return target().remove(obj);
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 106 */     return target().size();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/collection/SortedCollectionImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */