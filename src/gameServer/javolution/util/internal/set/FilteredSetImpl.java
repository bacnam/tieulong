/*    */ package javolution.util.internal.set;
/*    */ 
/*    */ import javolution.util.function.Predicate;
/*    */ import javolution.util.internal.collection.FilteredCollectionImpl;
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
/*    */ public class FilteredSetImpl<E>
/*    */   extends FilteredCollectionImpl<E>
/*    */   implements SetService<E>
/*    */ {
/*    */   private static final long serialVersionUID = 1536L;
/*    */   
/*    */   public FilteredSetImpl(SetService<E> target, Predicate<? super E> filter) {
/* 24 */     super((CollectionService)target, filter);
/*    */   }
/*    */ 
/*    */   
/*    */   public SetService<E> threadSafe() {
/* 29 */     return new SharedSetImpl<E>(this);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/set/FilteredSetImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */