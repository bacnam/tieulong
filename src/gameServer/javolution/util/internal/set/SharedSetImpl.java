/*    */ package javolution.util.internal.set;
/*    */ 
/*    */ import javolution.util.internal.collection.SharedCollectionImpl;
/*    */ import javolution.util.service.CollectionService;
/*    */ import javolution.util.service.SetService;
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
/*    */ public class SharedSetImpl<E>
/*    */   extends SharedCollectionImpl<E>
/*    */   implements SetService<E>
/*    */ {
/*    */   private static final long serialVersionUID = 1536L;
/*    */   
/*    */   public SharedSetImpl(SetService<E> target) {
/* 23 */     super((CollectionService)target);
/*    */   }
/*    */ 
/*    */   
/*    */   public SetService<E> threadSafe() {
/* 28 */     return this;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/set/SharedSetImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */