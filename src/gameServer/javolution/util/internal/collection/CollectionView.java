/*     */ package javolution.util.internal.collection;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javolution.util.FastCollection;
/*     */ import javolution.util.function.Consumer;
/*     */ import javolution.util.function.Equalities;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class CollectionView<E>
/*     */   extends FastCollection<E>
/*     */   implements CollectionService<E>
/*     */ {
/*     */   private static final long serialVersionUID = 1536L;
/*     */   private CollectionService<E> target;
/*     */   
/*     */   public CollectionView(CollectionService<E> target) {
/*  39 */     this.target = target;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean addAll(Collection<? extends E> c) {
/*  47 */     boolean changed = false;
/*  48 */     Iterator<? extends E> it = c.iterator();
/*  49 */     while (it.hasNext()) {
/*  50 */       if (add(it.next())) changed = true; 
/*     */     } 
/*  52 */     return changed;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/*  57 */     Iterator<? extends E> it = iterator();
/*  58 */     while (it.hasNext()) {
/*  59 */       it.next();
/*  60 */       it.remove();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public CollectionView<E> clone() {
/*     */     try {
/*  68 */       CollectionView<E> copy = (CollectionView<E>)super.clone();
/*  69 */       if (this.target != null) {
/*  70 */         copy.target = this.target.clone();
/*     */       }
/*  72 */       return copy;
/*  73 */     } catch (CloneNotSupportedException e) {
/*  74 */       throw new Error("Should not happen since target is cloneable");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(Object obj) {
/*  84 */     Iterator<? extends E> it = iterator();
/*  85 */     Equality<Object> cmp = (Equality)comparator();
/*  86 */     while (it.hasNext()) {
/*  87 */       if (cmp.areEqual(obj, it.next())) return true; 
/*     */     } 
/*  89 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean containsAll(Collection<?> c) {
/*  94 */     for (Object e : c) {
/*  95 */       if (!contains(e)) return false; 
/*     */     } 
/*  97 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 104 */     if (this == o) return true;
/*     */     
/* 106 */     if (o instanceof CollectionService)
/* 107 */     { if (!comparator().equals(((CollectionService)o).comparator())) return false;
/*     */        }
/* 109 */     else if (!comparator().equals(Equalities.STANDARD)) { return false; }
/*     */ 
/*     */     
/* 112 */     if (this instanceof Set) {
/* 113 */       if (!(o instanceof Set)) return false; 
/* 114 */       Set<E> set = (Set<E>)o;
/* 115 */       return (size() == set.size() && containsAll(set));
/* 116 */     }  if (this instanceof List) {
/* 117 */       if (!(o instanceof List)) return false; 
/* 118 */       List<E> list = (List<E>)o;
/* 119 */       if (size() != list.size()) return false; 
/* 120 */       Equality<? super E> cmp = comparator();
/* 121 */       Iterator<E> it1 = iterator();
/* 122 */       Iterator<E> it2 = list.iterator();
/* 123 */       while (it1.hasNext()) {
/* 124 */         if (!it2.hasNext()) return false; 
/* 125 */         if (!cmp.areEqual(it1.next(), it2.next())) return false; 
/*     */       } 
/* 127 */       if (it2.hasNext()) return false; 
/* 128 */       return true;
/*     */     } 
/* 130 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 137 */     Equality<? super E> cmp = comparator();
/* 138 */     Iterator<E> it = iterator();
/* 139 */     int hash = 0;
/* 140 */     if (this instanceof Set) {
/* 141 */       while (it.hasNext()) {
/* 142 */         hash += cmp.hashCodeOf(it.next());
/*     */       }
/* 144 */     } else if (this instanceof List) {
/* 145 */       while (it.hasNext()) {
/* 146 */         hash += 31 * hash + cmp.hashCodeOf(it.next());
/*     */       }
/*     */     } else {
/* 149 */       hash = super.hashCode();
/*     */     } 
/* 151 */     return hash;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 156 */     return iterator().hasNext();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void perform(Consumer<CollectionService<E>> action, CollectionService<E> view) {
/* 164 */     if (this.target == null) {
/* 165 */       action.accept(view);
/*     */     } else {
/* 167 */       this.target.perform(action, view);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean remove(Object obj) {
/* 174 */     Iterator<? extends E> it = iterator();
/* 175 */     Equality<Object> cmp = (Equality)comparator();
/* 176 */     while (it.hasNext()) {
/* 177 */       if (cmp.areEqual(obj, it.next())) {
/* 178 */         it.remove();
/* 179 */         return true;
/*     */       } 
/*     */     } 
/* 182 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeAll(Collection<?> c) {
/* 187 */     boolean changed = false;
/* 188 */     Iterator<? extends E> it = iterator();
/* 189 */     while (it.hasNext()) {
/* 190 */       if (c.contains(it.next())) {
/* 191 */         it.remove();
/* 192 */         changed = true;
/*     */       } 
/*     */     } 
/* 195 */     return changed;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean retainAll(Collection<?> c) {
/* 200 */     boolean changed = false;
/* 201 */     Iterator<? extends E> it = iterator();
/* 202 */     while (it.hasNext()) {
/* 203 */       if (!c.contains(it.next())) {
/* 204 */         it.remove();
/* 205 */         changed = true;
/*     */       } 
/*     */     } 
/* 208 */     return changed;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 213 */     int count = 0;
/* 214 */     Iterator<? extends E> it = iterator();
/* 215 */     while (it.hasNext()) {
/* 216 */       count++;
/* 217 */       it.next();
/*     */     } 
/* 219 */     return count;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public CollectionService<E>[] split(int n) {
/* 225 */     if (this.target == null) return (CollectionService<E>[])new CollectionService[] { this }; 
/* 226 */     CollectionService[] arrayOfCollectionService1 = (CollectionService[])this.target.split(n);
/* 227 */     CollectionService[] arrayOfCollectionService2 = new CollectionService[arrayOfCollectionService1.length];
/* 228 */     for (int i = 0; i < arrayOfCollectionService1.length; i++) {
/* 229 */       CollectionView<E> copy = clone();
/* 230 */       copy.target = arrayOfCollectionService1[i];
/* 231 */       arrayOfCollectionService2[i] = copy;
/*     */     } 
/* 233 */     return (CollectionService<E>[])arrayOfCollectionService2;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public CollectionService<E> threadSafe() {
/* 239 */     return new SharedCollectionImpl<E>(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object[] toArray() {
/* 244 */     return toArray(new Object[size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T[] toArray(T[] a) {
/* 250 */     int size = size();
/* 251 */     T[] result = (size <= a.length) ? a : (T[])Array.newInstance(a.getClass().getComponentType(), size);
/*     */ 
/*     */     
/* 254 */     int i = 0;
/* 255 */     Iterator<E> it = iterator();
/* 256 */     while (it.hasNext()) {
/* 257 */       result[i++] = (T)it.next();
/*     */     }
/* 259 */     if (result.length > size) {
/* 260 */       result[size] = null;
/*     */     }
/* 262 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public void update(Consumer<CollectionService<E>> action, CollectionService<E> view) {
/* 267 */     if (this.target == null) {
/* 268 */       action.accept(view);
/*     */     } else {
/* 270 */       this.target.perform(action, view);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected CollectionService<E> service() {
/* 275 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   protected CollectionService<E> target() {
/* 280 */     return this.target;
/*     */   }
/*     */   
/*     */   public abstract boolean add(E paramE);
/*     */   
/*     */   public abstract Equality<? super E> comparator();
/*     */   
/*     */   public abstract Iterator<E> iterator();
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/collection/CollectionView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */