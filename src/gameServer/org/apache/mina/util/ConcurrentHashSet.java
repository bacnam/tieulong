/*    */ package org.apache.mina.util;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import java.util.concurrent.ConcurrentMap;
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
/*    */ public class ConcurrentHashSet<E>
/*    */   extends MapBackedSet<E>
/*    */ {
/*    */   private static final long serialVersionUID = 8518578988740277828L;
/*    */   
/*    */   public ConcurrentHashSet() {
/* 37 */     super(new ConcurrentHashMap<E, Boolean>());
/*    */   }
/*    */   
/*    */   public ConcurrentHashSet(Collection<E> c) {
/* 41 */     super(new ConcurrentHashMap<E, Boolean>(), c);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean add(E o) {
/* 46 */     Boolean answer = ((ConcurrentMap<E, Boolean>)this.map).putIfAbsent(o, Boolean.TRUE);
/* 47 */     return (answer == null);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/util/ConcurrentHashSet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */