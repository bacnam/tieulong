/*    */ package com.mchange.v1.identicator;
/*    */ 
/*    */ import com.mchange.v1.util.WrapperIterator;
/*    */ import java.util.AbstractSet;
/*    */ import java.util.Collection;
/*    */ import java.util.HashSet;
/*    */ import java.util.Iterator;
/*    */ import java.util.Set;
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
/*    */ public class IdHashSet
/*    */   extends AbstractSet
/*    */   implements Set
/*    */ {
/*    */   HashSet inner;
/*    */   Identicator id;
/*    */   
/*    */   private IdHashSet(HashSet paramHashSet, Identicator paramIdenticator) {
/* 48 */     this.inner = paramHashSet;
/* 49 */     this.id = paramIdenticator;
/*    */   }
/*    */   
/*    */   public IdHashSet(Identicator paramIdenticator) {
/* 53 */     this(new HashSet(), paramIdenticator);
/*    */   }
/*    */   public IdHashSet(Collection paramCollection, Identicator paramIdenticator) {
/* 56 */     this(new HashSet(2 * paramCollection.size()), paramIdenticator);
/*    */   }
/*    */   public IdHashSet(int paramInt, float paramFloat, Identicator paramIdenticator) {
/* 59 */     this(new HashSet(paramInt, paramFloat), paramIdenticator);
/*    */   }
/*    */   public IdHashSet(int paramInt, Identicator paramIdenticator) {
/* 62 */     this(new HashSet(paramInt, 0.75F), paramIdenticator);
/*    */   }
/*    */   
/*    */   public Iterator iterator() {
/* 66 */     return (Iterator)new WrapperIterator(this.inner.iterator(), true)
/*    */       {
/*    */         protected Object transformObject(Object param1Object)
/*    */         {
/* 70 */           IdHashKey idHashKey = (IdHashKey)param1Object;
/* 71 */           return idHashKey.getKeyObj();
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   public int size() {
/* 77 */     return this.inner.size();
/*    */   }
/*    */   public boolean contains(Object paramObject) {
/* 80 */     return this.inner.contains(createKey(paramObject));
/*    */   }
/*    */   public boolean add(Object paramObject) {
/* 83 */     return this.inner.add(createKey(paramObject));
/*    */   }
/*    */   public boolean remove(Object paramObject) {
/* 86 */     return this.inner.remove(createKey(paramObject));
/*    */   }
/*    */   public void clear() {
/* 89 */     this.inner.clear();
/*    */   }
/*    */   private IdHashKey createKey(Object paramObject) {
/* 92 */     return new StrongIdHashKey(paramObject, this.id);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/identicator/IdHashSet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */