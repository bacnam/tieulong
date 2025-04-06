/*    */ package org.apache.mina.util;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.AbstractSet;
/*    */ import java.util.Collection;
/*    */ import java.util.Iterator;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MapBackedSet<E>
/*    */   extends AbstractSet<E>
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = -8347878570391674042L;
/*    */   protected final Map<E, Boolean> map;
/*    */   
/*    */   public MapBackedSet(Map<E, Boolean> map) {
/* 41 */     this.map = map;
/*    */   }
/*    */   
/*    */   public MapBackedSet(Map<E, Boolean> map, Collection<E> c) {
/* 45 */     this.map = map;
/* 46 */     addAll(c);
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 51 */     return this.map.size();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean contains(Object o) {
/* 56 */     return this.map.containsKey(o);
/*    */   }
/*    */ 
/*    */   
/*    */   public Iterator<E> iterator() {
/* 61 */     return this.map.keySet().iterator();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean add(E o) {
/* 66 */     return (this.map.put(o, Boolean.TRUE) == null);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean remove(Object o) {
/* 71 */     return (this.map.remove(o) != null);
/*    */   }
/*    */ 
/*    */   
/*    */   public void clear() {
/* 76 */     this.map.clear();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/util/MapBackedSet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */