/*    */ package com.mchange.v1.cachedstore;
/*    */ 
/*    */ import com.mchange.v1.util.WrapperIterator;
/*    */ import java.lang.ref.ReferenceQueue;
/*    */ import java.util.AbstractSet;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashSet;
/*    */ import java.util.Iterator;
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
/*    */ 
/*    */ class ManualCleanupSoftSet
/*    */   extends AbstractSet
/*    */   implements Vacuumable
/*    */ {
/* 44 */   HashSet inner = new HashSet();
/* 45 */   ReferenceQueue queue = new ReferenceQueue();
/*    */ 
/*    */   
/*    */   public Iterator iterator() {
/* 49 */     return (Iterator)new WrapperIterator(this.inner.iterator(), true)
/*    */       {
/*    */         protected Object transformObject(Object param1Object)
/*    */         {
/* 53 */           SoftKey softKey = (SoftKey)param1Object;
/* 54 */           T t = softKey.get();
/* 55 */           return (t == null) ? SKIP_TOKEN : t;
/*    */         }
/*    */       };
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int size() {
/* 67 */     return this.inner.size();
/*    */   }
/*    */   public boolean contains(Object paramObject) {
/* 70 */     return this.inner.contains(new SoftKey(paramObject, null));
/*    */   }
/*    */   
/*    */   private ArrayList toArrayList() {
/* 74 */     ArrayList arrayList = new ArrayList(size());
/* 75 */     for (Iterator iterator = iterator(); iterator.hasNext();)
/* 76 */       arrayList.add(iterator.next()); 
/* 77 */     return arrayList;
/*    */   }
/*    */   
/*    */   public Object[] toArray() {
/* 81 */     return toArrayList().toArray();
/*    */   }
/*    */   public Object[] toArray(Object[] paramArrayOfObject) {
/* 84 */     return toArrayList().toArray(paramArrayOfObject);
/*    */   }
/*    */   public boolean add(Object paramObject) {
/* 87 */     return this.inner.add(new SoftKey(paramObject, this.queue));
/*    */   }
/*    */   public boolean remove(Object paramObject) {
/* 90 */     return this.inner.remove(new SoftKey(paramObject, null));
/*    */   }
/*    */   public void clear() {
/* 93 */     this.inner.clear();
/*    */   }
/*    */   
/*    */   public void vacuum() throws CachedStoreException {
/*    */     SoftKey softKey;
/* 98 */     while ((softKey = (SoftKey)this.queue.poll()) != null)
/* 99 */       this.inner.remove(softKey); 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/cachedstore/ManualCleanupSoftSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */