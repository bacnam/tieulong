/*    */ package org.apache.mina.util;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.IdentityHashMap;
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
/*    */ public class IdentityHashSet<E>
/*    */   extends MapBackedSet<E>
/*    */ {
/*    */   private static final long serialVersionUID = 6948202189467167147L;
/*    */   
/*    */   public IdentityHashSet() {
/* 36 */     super(new IdentityHashMap<E, Boolean>());
/*    */   }
/*    */   
/*    */   public IdentityHashSet(int expectedMaxSize) {
/* 40 */     super(new IdentityHashMap<E, Boolean>(expectedMaxSize));
/*    */   }
/*    */   
/*    */   public IdentityHashSet(Collection<E> c) {
/* 44 */     super(new IdentityHashMap<E, Boolean>(), c);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/util/IdentityHashSet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */