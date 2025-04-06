/*     */ package javolution.util.internal.set.sorted;
/*     */ 
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import javolution.util.function.Equality;
/*     */ import javolution.util.service.SortedSetService;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SubSortedSetImpl<E>
/*     */   extends SortedSetView<E>
/*     */ {
/*     */   private static final long serialVersionUID = 1536L;
/*     */   private final E from;
/*     */   private final E to;
/*     */   
/*     */   private class IteratorImpl
/*     */     implements Iterator<E>
/*     */   {
/*     */     private boolean ahead;
/*  26 */     private final Equality<? super E> cmp = SubSortedSetImpl.this.comparator();
/*     */     private E next;
/*  28 */     private final Iterator<E> targetIterator = SubSortedSetImpl.this.target().iterator();
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/*  32 */       if (this.ahead) return true; 
/*  33 */       while (this.targetIterator.hasNext()) {
/*  34 */         this.next = this.targetIterator.next();
/*  35 */         if (SubSortedSetImpl.this.from != null && this.cmp.compare(this.next, SubSortedSetImpl.this.from) < 0)
/*  36 */           continue;  if (SubSortedSetImpl.this.to != null && this.cmp.compare(this.next, SubSortedSetImpl.this.to) >= 0)
/*  37 */           break;  this.ahead = true;
/*  38 */         return true;
/*     */       } 
/*  40 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public E next() {
/*  45 */       hasNext();
/*  46 */       this.ahead = false;
/*  47 */       return this.next;
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/*  52 */       this.targetIterator.remove();
/*     */     }
/*     */ 
/*     */     
/*     */     private IteratorImpl() {}
/*     */   }
/*     */ 
/*     */   
/*     */   public SubSortedSetImpl(SortedSetService<E> target, E from, E to) {
/*  61 */     super(target);
/*  62 */     if (from != null && to != null && comparator().compare(from, to) > 0) {
/*  63 */       throw new IllegalArgumentException("from: " + from + ", to: " + to);
/*     */     }
/*  65 */     this.from = from;
/*  66 */     this.to = to;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean add(E e) {
/*  71 */     Equality<? super E> cmp = comparator();
/*  72 */     if (this.from != null && cmp.compare(e, this.from) < 0) throw new IllegalArgumentException("Element: " + e + " outside of this sub-set bounds");
/*     */     
/*  74 */     if (this.to != null && cmp.compare(e, this.to) >= 0) throw new IllegalArgumentException("Element: " + e + " outside of this sub-set bounds");
/*     */     
/*  76 */     return target().add(e);
/*     */   }
/*     */ 
/*     */   
/*     */   public Equality<? super E> comparator() {
/*  81 */     return target().comparator();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(Object obj) {
/*  87 */     Equality<? super E> cmp = comparator();
/*  88 */     if (this.from != null && cmp.compare(obj, this.from) < 0) return false; 
/*  89 */     if (this.to != null && cmp.compare(obj, this.to) >= 0) return false; 
/*  90 */     return target().contains(obj);
/*     */   }
/*     */ 
/*     */   
/*     */   public E first() {
/*  95 */     if (this.from == null) return (E)target().first(); 
/*  96 */     Iterator<E> it = iterator();
/*  97 */     if (!it.hasNext()) throw new NoSuchElementException(); 
/*  98 */     return it.next();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 103 */     return iterator().hasNext();
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<E> iterator() {
/* 108 */     return new IteratorImpl();
/*     */   }
/*     */ 
/*     */   
/*     */   public E last() {
/* 113 */     if (this.to == null) return (E)target().last(); 
/* 114 */     Iterator<E> it = iterator();
/* 115 */     if (!it.hasNext()) throw new NoSuchElementException(); 
/* 116 */     E last = it.next();
/* 117 */     while (it.hasNext()) {
/* 118 */       last = it.next();
/*     */     }
/* 120 */     return last;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean remove(Object obj) {
/* 126 */     Equality<? super E> cmp = comparator();
/* 127 */     if (this.from != null && cmp.compare(obj, this.from) < 0) return false; 
/* 128 */     if (this.to != null && cmp.compare(obj, this.to) >= 0) return false; 
/* 129 */     return target().remove(obj);
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 134 */     int count = 0;
/* 135 */     Iterator<E> it = iterator();
/* 136 */     while (it.hasNext()) {
/* 137 */       count++;
/* 138 */       it.next();
/*     */     } 
/* 140 */     return count;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/set/sorted/SubSortedSetImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */