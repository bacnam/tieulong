/*    */ package bsh.collection;
/*    */ 
/*    */ import bsh.BshIterator;
/*    */ import java.util.Collection;
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
/*    */ public class CollectionIterator
/*    */   implements BshIterator
/*    */ {
/*    */   private Iterator iterator;
/*    */   
/*    */   public CollectionIterator(Object iterateOverMe) {
/* 33 */     this.iterator = createIterator(iterateOverMe);
/*    */   }
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
/*    */   protected Iterator createIterator(Object iterateOverMe) {
/* 51 */     if (iterateOverMe == null) {
/* 52 */       throw new NullPointerException("Object arguments passed to the CollectionIterator constructor cannot be null.");
/*    */     }
/*    */     
/* 55 */     if (iterateOverMe instanceof Iterator) {
/* 56 */       return (Iterator)iterateOverMe;
/*    */     }
/* 58 */     if (iterateOverMe instanceof Collection) {
/* 59 */       return ((Collection)iterateOverMe).iterator();
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 67 */     throw new IllegalArgumentException("Cannot enumerate object of type " + iterateOverMe.getClass());
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object next() {
/* 77 */     return this.iterator.next();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean hasNext() {
/* 87 */     return this.iterator.hasNext();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/collection/CollectionIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */