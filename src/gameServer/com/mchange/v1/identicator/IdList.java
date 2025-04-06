/*     */ package com.mchange.v1.identicator;
/*     */ 
/*     */ import com.mchange.v1.util.IteratorUtils;
/*     */ import com.mchange.v1.util.ListUtils;
/*     */ import com.mchange.v1.util.WrapperIterator;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
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
/*     */ public class IdList
/*     */   implements List
/*     */ {
/*     */   Identicator id;
/*     */   List inner;
/*     */   
/*     */   public IdList(Identicator paramIdenticator, List paramList) {
/*  48 */     this.id = paramIdenticator;
/*  49 */     this.inner = paramList;
/*     */   }
/*     */   
/*     */   public int size() {
/*  53 */     return this.inner.size();
/*     */   }
/*     */   public boolean isEmpty() {
/*  56 */     return this.inner.isEmpty();
/*     */   }
/*     */   
/*     */   public boolean contains(Object paramObject) {
/*  60 */     StrongIdHashKey strongIdHashKey = new StrongIdHashKey(paramObject, this.id);
/*  61 */     return this.inner.contains(paramObject);
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator iterator() {
/*  66 */     return (Iterator)new WrapperIterator(this.inner.iterator(), true)
/*     */       {
/*     */         protected Object transformObject(Object param1Object)
/*     */         {
/*  70 */           if (param1Object instanceof IdHashKey) {
/*     */             
/*  72 */             IdHashKey idHashKey = (IdHashKey)param1Object;
/*  73 */             return idHashKey.getKeyObj();
/*     */           } 
/*     */           
/*  76 */           return param1Object;
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   public Object[] toArray() {
/*  82 */     return toArray(new Object[size()]);
/*     */   }
/*     */   public Object[] toArray(Object[] paramArrayOfObject) {
/*  85 */     return IteratorUtils.toArray(iterator(), size(), paramArrayOfObject);
/*     */   }
/*     */   public boolean add(Object paramObject) {
/*  88 */     return this.inner.add(new StrongIdHashKey(paramObject, this.id));
/*     */   }
/*     */   public boolean remove(Object paramObject) {
/*  91 */     return this.inner.remove(new StrongIdHashKey(paramObject, this.id));
/*     */   }
/*     */   
/*     */   public boolean containsAll(Collection paramCollection) {
/*  95 */     Iterator iterator = paramCollection.iterator();
/*  96 */     while (iterator.hasNext()) {
/*     */       
/*  98 */       StrongIdHashKey strongIdHashKey = new StrongIdHashKey(iterator.next(), this.id);
/*  99 */       if (!this.inner.contains(strongIdHashKey))
/* 100 */         return false; 
/*     */     } 
/* 102 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addAll(Collection paramCollection) {
/* 107 */     Iterator iterator = paramCollection.iterator();
/* 108 */     boolean bool = false;
/* 109 */     while (iterator.hasNext()) {
/*     */       
/* 111 */       StrongIdHashKey strongIdHashKey = new StrongIdHashKey(iterator.next(), this.id);
/* 112 */       bool |= this.inner.add(strongIdHashKey);
/*     */     } 
/* 114 */     return bool;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addAll(int paramInt, Collection paramCollection) {
/* 119 */     Iterator iterator = paramCollection.iterator();
/* 120 */     while (iterator.hasNext()) {
/*     */       
/* 122 */       StrongIdHashKey strongIdHashKey = new StrongIdHashKey(iterator.next(), this.id);
/* 123 */       this.inner.add(paramInt, strongIdHashKey);
/* 124 */       paramInt++;
/*     */     } 
/* 126 */     return (paramCollection.size() > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeAll(Collection paramCollection) {
/* 131 */     Iterator iterator = paramCollection.iterator();
/* 132 */     boolean bool = false;
/* 133 */     while (iterator.hasNext()) {
/*     */       
/* 135 */       StrongIdHashKey strongIdHashKey = new StrongIdHashKey(iterator.next(), this.id);
/* 136 */       bool |= this.inner.remove(strongIdHashKey);
/*     */     } 
/* 138 */     return bool;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean retainAll(Collection paramCollection) {
/* 143 */     Iterator<IdHashKey> iterator = this.inner.iterator();
/* 144 */     boolean bool = false;
/* 145 */     while (iterator.hasNext()) {
/*     */       
/* 147 */       IdHashKey idHashKey = iterator.next();
/* 148 */       if (!paramCollection.contains(idHashKey.getKeyObj())) {
/*     */         
/* 150 */         this.inner.remove(idHashKey);
/* 151 */         bool = true;
/*     */       } 
/*     */     } 
/* 154 */     return bool;
/*     */   }
/*     */   
/*     */   public void clear() {
/* 158 */     this.inner.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object paramObject) {
/* 163 */     if (paramObject instanceof List) {
/* 164 */       return ListUtils.equivalent(this, (List)paramObject);
/*     */     }
/* 166 */     return false;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 170 */     return ListUtils.hashContents(this);
/*     */   }
/*     */   public Object get(int paramInt) {
/* 173 */     return ((IdHashKey)this.inner.get(paramInt)).getKeyObj();
/*     */   }
/*     */   
/*     */   public Object set(int paramInt, Object paramObject) {
/* 177 */     IdHashKey idHashKey = this.inner.set(paramInt, new StrongIdHashKey(paramObject, this.id));
/* 178 */     return idHashKey.getKeyObj();
/*     */   }
/*     */ 
/*     */   
/*     */   public void add(int paramInt, Object paramObject) {
/* 183 */     this.inner.add(paramInt, new StrongIdHashKey(paramObject, this.id));
/*     */   }
/*     */ 
/*     */   
/*     */   public Object remove(int paramInt) {
/* 188 */     IdHashKey idHashKey = this.inner.remove(paramInt);
/* 189 */     return (idHashKey == null) ? null : idHashKey.getKeyObj();
/*     */   }
/*     */   
/*     */   public int indexOf(Object paramObject) {
/* 193 */     return this.inner.indexOf(new StrongIdHashKey(paramObject, this.id));
/*     */   }
/*     */   public int lastIndexOf(Object paramObject) {
/* 196 */     return this.inner.lastIndexOf(new StrongIdHashKey(paramObject, this.id));
/*     */   }
/*     */   
/*     */   public ListIterator listIterator() {
/* 200 */     return (new LinkedList(this)).listIterator();
/*     */   }
/*     */   
/*     */   public ListIterator listIterator(int paramInt) {
/* 204 */     return (new LinkedList(this)).listIterator(paramInt);
/*     */   }
/*     */   public List subList(int paramInt1, int paramInt2) {
/* 207 */     return new IdList(this.id, this.inner.subList(paramInt1, paramInt2));
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/identicator/IdList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */