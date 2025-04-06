/*    */ package com.google.common.collect;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
/*    */ import java.util.Map;
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
/*    */ @GwtCompatible(serializable = true, emulated = true)
/*    */ class RegularImmutableBiMap<K, V>
/*    */   extends ImmutableBiMap<K, V>
/*    */ {
/*    */   final transient ImmutableMap<K, V> delegate;
/*    */   final transient ImmutableBiMap<V, K> inverse;
/*    */   
/*    */   RegularImmutableBiMap(ImmutableMap<K, V> delegate) {
/* 33 */     this.delegate = delegate;
/*    */     
/* 35 */     ImmutableMap.Builder<V, K> builder = ImmutableMap.builder();
/* 36 */     for (Map.Entry<K, V> entry : delegate.entrySet()) {
/* 37 */       builder.put(entry.getValue(), entry.getKey());
/*    */     }
/* 39 */     ImmutableMap<V, K> backwardMap = builder.build();
/* 40 */     this.inverse = new RegularImmutableBiMap(backwardMap, this);
/*    */   }
/*    */ 
/*    */   
/*    */   RegularImmutableBiMap(ImmutableMap<K, V> delegate, ImmutableBiMap<V, K> inverse) {
/* 45 */     this.delegate = delegate;
/* 46 */     this.inverse = inverse;
/*    */   }
/*    */   
/*    */   ImmutableMap<K, V> delegate() {
/* 50 */     return this.delegate;
/*    */   }
/*    */   
/*    */   public ImmutableBiMap<V, K> inverse() {
/* 54 */     return this.inverse;
/*    */   }
/*    */   
/*    */   boolean isPartialView() {
/* 58 */     return (this.delegate.isPartialView() || this.inverse.delegate().isPartialView());
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/google/common/collect/RegularImmutableBiMap.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */