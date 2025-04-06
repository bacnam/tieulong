/*     */ package javolution.util.internal.table;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import javolution.util.FastCollection;
/*     */ import javolution.util.function.Equality;
/*     */ import javolution.util.internal.collection.CollectionView;
/*     */ import javolution.util.service.CollectionService;
/*     */ import javolution.util.service.TableService;
/*     */ 
/*     */ public class FastTableImpl<E>
/*     */   extends TableView<E>
/*     */ {
/*     */   private static final long serialVersionUID = 1536L;
/*     */   private transient int capacity;
/*     */   private final Equality<? super E> comparator;
/*     */   private transient FractalTableImpl fractal;
/*     */   private transient int size;
/*     */   
/*     */   private class IteratorImpl
/*     */     implements Iterator<E> {
/*  25 */     private int currentIndex = -1;
/*     */     
/*     */     private int nextIndex;
/*     */     
/*     */     public boolean hasNext() {
/*  30 */       return (this.nextIndex < FastTableImpl.this.size);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public E next() {
/*  36 */       if (this.nextIndex >= FastTableImpl.this.size) throw new NoSuchElementException(); 
/*  37 */       this.currentIndex = this.nextIndex++;
/*  38 */       return (E)FastTableImpl.this.fractal.get(this.currentIndex);
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/*  43 */       if (this.currentIndex < 0) throw new IllegalStateException(); 
/*  44 */       FastTableImpl.this.remove(this.currentIndex);
/*  45 */       this.nextIndex--;
/*  46 */       this.currentIndex = -1;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     private IteratorImpl() {}
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public FastTableImpl(Equality<? super E> comparator) {
/*  57 */     super((TableService<E>)null);
/*  58 */     this.comparator = comparator;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean add(E element) {
/*  63 */     addLast(element);
/*  64 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void add(int index, E element) {
/*  69 */     if (index < 0 || index > this.size) indexError(index); 
/*  70 */     checkUpsize();
/*  71 */     if (index >= this.size >> 1) {
/*  72 */       this.fractal.shiftRight(element, index, this.size - index);
/*     */     } else {
/*  74 */       this.fractal.shiftLeft(element, index - 1, index);
/*  75 */       this.fractal.offset--;
/*     */     } 
/*  77 */     this.size++;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addFirst(E element) {
/*  82 */     checkUpsize();
/*  83 */     this.fractal.offset--;
/*  84 */     this.fractal.set(0, element);
/*  85 */     this.size++;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addLast(E element) {
/*  90 */     checkUpsize();
/*  91 */     this.fractal.set(this.size++, element);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/*  96 */     this.fractal = null;
/*  97 */     this.capacity = 0;
/*  98 */     this.size = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public FastTableImpl<E> clone() {
/* 103 */     FastTableImpl<E> copy = new FastTableImpl(comparator());
/* 104 */     copy.addAll((FastCollection)this);
/* 105 */     return copy;
/*     */   }
/*     */ 
/*     */   
/*     */   public Equality<? super E> comparator() {
/* 110 */     return this.comparator;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public E get(int index) {
/* 116 */     if (index < 0 && index >= this.size) indexError(index); 
/* 117 */     return (E)this.fractal.get(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public E getFirst() {
/* 122 */     if (this.size == 0) emptyError(); 
/* 123 */     return get(0);
/*     */   }
/*     */ 
/*     */   
/*     */   public E getLast() {
/* 128 */     if (this.size == 0) emptyError(); 
/* 129 */     return get(this.size - 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<E> iterator() {
/* 134 */     return new IteratorImpl();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public E remove(int index) {
/* 140 */     if (index < 0 || index >= this.size) indexError(index); 
/* 141 */     E removed = (E)this.fractal.get(index);
/* 142 */     if (index >= this.size >> 1) {
/* 143 */       this.fractal.shiftLeft(null, this.size - 1, this.size - index - 1);
/*     */     } else {
/* 145 */       this.fractal.shiftRight(null, 0, index);
/* 146 */       this.fractal.offset++;
/*     */     } 
/* 148 */     this.size--;
/* 149 */     return removed;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public E removeFirst() {
/* 155 */     if (this.size == 0) emptyError(); 
/* 156 */     E first = (E)this.fractal.set(0, null);
/* 157 */     this.fractal.offset++;
/* 158 */     this.size--;
/* 159 */     return first;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public E removeLast() {
/* 165 */     if (this.size == 0) emptyError(); 
/* 166 */     E last = (E)this.fractal.set(--this.size, null);
/* 167 */     return last;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public E set(int index, E element) {
/* 173 */     if (index < 0 && index >= this.size) indexError(index); 
/* 174 */     return (E)this.fractal.set(index, element);
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 179 */     return this.size;
/*     */   }
/*     */   
/*     */   private void checkUpsize() {
/* 183 */     if (this.size >= this.capacity) upsize();
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
/* 190 */     s.defaultReadObject();
/* 191 */     int n = s.readInt();
/* 192 */     for (int i = 0; i < n; i++)
/* 193 */       addLast((E)s.readObject()); 
/*     */   }
/*     */   
/*     */   private void upsize() {
/* 197 */     this.fractal = (this.fractal == null) ? new FractalTableImpl() : this.fractal.upsize();
/* 198 */     this.capacity = this.fractal.capacity();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream s) throws IOException {
/* 204 */     s.defaultWriteObject();
/* 205 */     s.writeInt(this.size);
/* 206 */     for (int i = 0; i < this.size; i++)
/* 207 */       s.writeObject(this.fractal.get(i)); 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/table/FastTableImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */