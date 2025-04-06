/*    */ package bsh.collection;
/*    */ 
/*    */ import bsh.BshIterator;
/*    */ import bsh.CollectionManager;
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
/*    */ public class CollectionManagerImpl
/*    */   extends CollectionManager
/*    */ {
/*    */   public BshIterator getBshIterator(Object obj) throws IllegalArgumentException {
/* 52 */     if (obj instanceof java.util.Collection || obj instanceof java.util.Iterator) {
/* 53 */       return new CollectionIterator(obj);
/*    */     }
/* 55 */     return (BshIterator)new CollectionManager.BasicBshIterator(obj);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isMap(Object obj) {
/* 60 */     if (obj instanceof Map) {
/* 61 */       return true;
/*    */     }
/* 63 */     return super.isMap(obj);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Object getFromMap(Object map, Object key) {
/* 69 */     return ((Map)map).get(key);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object putInMap(Object map, Object key, Object value) {
/* 78 */     return ((Map<Object, Object>)map).put(key, value);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/collection/CollectionManagerImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */