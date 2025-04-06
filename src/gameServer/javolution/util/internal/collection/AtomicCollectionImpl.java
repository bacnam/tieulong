/*     */ package javolution.util.internal.collection;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import javolution.util.function.Consumer;
/*     */ import javolution.util.function.Equality;
/*     */ import javolution.util.service.CollectionService;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AtomicCollectionImpl<E>
/*     */   extends CollectionView<E>
/*     */ {
/*     */   private static final long serialVersionUID = 1536L;
/*     */   protected volatile CollectionService<E> immutable;
/*     */   protected transient Thread updatingThread;
/*     */   
/*     */   private class IteratorImpl
/*     */     implements Iterator<E>
/*     */   {
/*     */     private E current;
/*  29 */     private final Iterator<E> targetIterator = AtomicCollectionImpl.this.targetView().iterator();
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/*  34 */       return this.targetIterator.hasNext();
/*     */     }
/*     */ 
/*     */     
/*     */     public E next() {
/*  39 */       this.current = this.targetIterator.next();
/*  40 */       return this.current;
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/*  45 */       if (this.current == null) throw new IllegalStateException(); 
/*  46 */       AtomicCollectionImpl.this.remove(this.current);
/*  47 */       this.current = null;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AtomicCollectionImpl(CollectionService<E> target) {
/*  56 */     super(target);
/*  57 */     this.immutable = cloneTarget();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean add(E element) {
/*  62 */     boolean changed = target().add(element);
/*  63 */     if (changed && !updateInProgress()) this.immutable = cloneTarget(); 
/*  64 */     return changed;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean addAll(Collection<? extends E> c) {
/*  69 */     boolean changed = target().addAll(c);
/*  70 */     if (changed && !updateInProgress()) this.immutable = cloneTarget(); 
/*  71 */     return changed;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void clear() {
/*  76 */     clear();
/*  77 */     if (!updateInProgress()) {
/*  78 */       this.immutable = cloneTarget();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized AtomicCollectionImpl<E> clone() {
/*  84 */     AtomicCollectionImpl<E> copy = (AtomicCollectionImpl<E>)super.clone();
/*  85 */     copy.updatingThread = null;
/*  86 */     return copy;
/*     */   }
/*     */ 
/*     */   
/*     */   public Equality<? super E> comparator() {
/*  91 */     return this.immutable.comparator();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Object o) {
/*  96 */     return targetView().contains(o);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsAll(Collection<?> c) {
/* 101 */     return targetView().containsAll(c);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 106 */     return targetView().equals(o);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 111 */     return targetView().hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 116 */     return targetView().isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<E> iterator() {
/* 121 */     return new IteratorImpl();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean remove(Object o) {
/* 126 */     boolean changed = target().remove(o);
/* 127 */     if (changed && !updateInProgress()) this.immutable = cloneTarget(); 
/* 128 */     return changed;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean removeAll(Collection<?> c) {
/* 133 */     boolean changed = target().removeAll(c);
/* 134 */     if (changed && !updateInProgress()) this.immutable = cloneTarget(); 
/* 135 */     return changed;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean retainAll(Collection<?> c) {
/* 140 */     boolean changed = target().retainAll(c);
/* 141 */     if (changed && !updateInProgress()) this.immutable = cloneTarget(); 
/* 142 */     return changed;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 147 */     return targetView().size();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public CollectionService<E>[] split(int n) {
/* 153 */     return (CollectionService<E>[])new CollectionService[] { this };
/*     */   }
/*     */ 
/*     */   
/*     */   public CollectionService<E> threadSafe() {
/* 158 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object[] toArray() {
/* 163 */     return targetView().toArray();
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T[] toArray(T[] a) {
/* 168 */     return (T[])targetView().toArray((Object[])a);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void update(Consumer<CollectionService<E>> action, CollectionService<E> view) {
/* 174 */     this.updatingThread = Thread.currentThread();
/*     */     try {
/* 176 */       target().update(action, view);
/*     */     } finally {
/* 178 */       this.updatingThread = null;
/* 179 */       this.immutable = cloneTarget();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected CollectionService<E> targetView() {
/* 186 */     return (this.updatingThread == null || this.updatingThread != Thread.currentThread()) ? this.immutable : target();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected CollectionService<E> cloneTarget() {
/*     */     try {
/* 193 */       return target().clone();
/* 194 */     } catch (CloneNotSupportedException e) {
/* 195 */       throw new Error("Cannot happen since target is Cloneable.");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected final boolean updateInProgress() {
/* 201 */     return (this.updatingThread == Thread.currentThread());
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/collection/AtomicCollectionImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */