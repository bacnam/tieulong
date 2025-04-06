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
/*     */ public class SharedCollectionImpl<E>
/*     */   extends CollectionView<E>
/*     */ {
/*     */   private static final long serialVersionUID = 1536L;
/*     */   protected ReadWriteLockImpl lock;
/*     */   
/*     */   private class IteratorImpl
/*     */     implements Iterator<E>
/*     */   {
/*     */     private E next;
/*     */     private final Iterator<E> targetIterator;
/*     */     
/*     */     public IteratorImpl() {
/*  29 */       SharedCollectionImpl.this.lock.readLock.lock();
/*     */       try {
/*  31 */         this.targetIterator = SharedCollectionImpl.this.cloneTarget().iterator();
/*     */       } finally {
/*  33 */         SharedCollectionImpl.this.lock.readLock.unlock();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/*  39 */       return this.targetIterator.hasNext();
/*     */     }
/*     */ 
/*     */     
/*     */     public E next() {
/*  44 */       this.next = this.targetIterator.next();
/*  45 */       return this.next;
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/*  50 */       if (this.next == null) throw new IllegalStateException(); 
/*  51 */       SharedCollectionImpl.this.remove(this.next);
/*  52 */       this.next = null;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SharedCollectionImpl(CollectionService<E> target) {
/*  60 */     this(target, new ReadWriteLockImpl());
/*     */   }
/*     */ 
/*     */   
/*     */   public SharedCollectionImpl(CollectionService<E> target, ReadWriteLockImpl lock) {
/*  65 */     super(target);
/*  66 */     this.lock = lock;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean add(E element) {
/*  71 */     this.lock.writeLock.lock();
/*     */     try {
/*  73 */       return target().add(element);
/*     */     } finally {
/*  75 */       this.lock.writeLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addAll(Collection<? extends E> c) {
/*  81 */     this.lock.writeLock.lock();
/*     */     try {
/*  83 */       return target().addAll(c);
/*     */     } finally {
/*  85 */       this.lock.writeLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/*  91 */     this.lock.writeLock.lock();
/*     */     try {
/*  93 */       target().clear();
/*     */     } finally {
/*  95 */       this.lock.writeLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public SharedCollectionImpl<E> clone() {
/* 101 */     this.lock.readLock.lock();
/*     */     try {
/* 103 */       SharedCollectionImpl<E> copy = (SharedCollectionImpl<E>)super.clone();
/*     */       
/* 105 */       copy.lock = new ReadWriteLockImpl();
/* 106 */       return copy;
/*     */     } finally {
/* 108 */       this.lock.readLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Equality<? super E> comparator() {
/* 114 */     return target().comparator();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean contains(Object o) {
/* 119 */     this.lock.readLock.lock();
/*     */     try {
/* 121 */       return target().contains(o);
/*     */     } finally {
/* 123 */       this.lock.readLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsAll(Collection<?> c) {
/* 129 */     this.lock.readLock.lock();
/*     */     try {
/* 131 */       return target().containsAll(c);
/*     */     } finally {
/* 133 */       this.lock.readLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 139 */     this.lock.readLock.lock();
/*     */     try {
/* 141 */       return target().equals(o);
/*     */     } finally {
/* 143 */       this.lock.readLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 149 */     this.lock.readLock.lock();
/*     */     try {
/* 151 */       return target().hashCode();
/*     */     } finally {
/* 153 */       this.lock.readLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 159 */     this.lock.readLock.lock();
/*     */     try {
/* 161 */       return target().isEmpty();
/*     */     } finally {
/* 163 */       this.lock.readLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<E> iterator() {
/* 169 */     return new IteratorImpl();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void perform(Consumer<CollectionService<E>> action, CollectionService<E> view) {
/* 175 */     this.lock.readLock.lock();
/*     */     try {
/* 177 */       target().perform(action, view);
/*     */     } finally {
/* 179 */       this.lock.readLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean remove(Object o) {
/* 185 */     this.lock.writeLock.lock();
/*     */     try {
/* 187 */       return target().remove(o);
/*     */     } finally {
/* 189 */       this.lock.writeLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeAll(Collection<?> c) {
/* 195 */     this.lock.writeLock.lock();
/*     */     try {
/* 197 */       return target().removeAll(c);
/*     */     } finally {
/* 199 */       this.lock.writeLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean retainAll(Collection<?> c) {
/* 205 */     this.lock.writeLock.lock();
/*     */     try {
/* 207 */       return target().retainAll(c);
/*     */     } finally {
/* 209 */       this.lock.writeLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 215 */     this.lock.readLock.lock();
/*     */     try {
/* 217 */       return target().size();
/*     */     } finally {
/* 219 */       this.lock.readLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public CollectionService<E>[] split(int n) {
/*     */     CollectionService[] arrayOfCollectionService1;
/* 227 */     this.lock.readLock.lock();
/*     */     try {
/* 229 */       arrayOfCollectionService1 = (CollectionService[])target().split(n);
/*     */     } finally {
/* 231 */       this.lock.readLock.unlock();
/*     */     } 
/* 233 */     CollectionService[] arrayOfCollectionService2 = new CollectionService[arrayOfCollectionService1.length];
/* 234 */     for (int i = 0; i < arrayOfCollectionService1.length; i++) {
/* 235 */       arrayOfCollectionService2[i] = new SharedCollectionImpl(arrayOfCollectionService1[i], this.lock);
/*     */     }
/* 237 */     return (CollectionService<E>[])arrayOfCollectionService2;
/*     */   }
/*     */ 
/*     */   
/*     */   public CollectionService<E> threadSafe() {
/* 242 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object[] toArray() {
/* 247 */     this.lock.readLock.lock();
/*     */     try {
/* 249 */       return target().toArray();
/*     */     } finally {
/* 251 */       this.lock.readLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T[] toArray(T[] a) {
/* 257 */     this.lock.readLock.lock();
/*     */     try {
/* 259 */       return (T[])target().toArray((Object[])a);
/*     */     } finally {
/* 261 */       this.lock.readLock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected CollectionService<E> cloneTarget() {
/*     */     try {
/* 268 */       return target().clone();
/* 269 */     } catch (CloneNotSupportedException e) {
/* 270 */       throw new Error("Cannot happen since target is Cloneable.");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/collection/SharedCollectionImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */