/*    */ package javolution.util.internal.set;
/*    */ 
/*    */ import javolution.util.function.Function;
/*    */ import javolution.util.internal.collection.MappedCollectionImpl;
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
/*    */ 
/*    */ public abstract class MappedSetImpl<E, R>
/*    */   extends MappedCollectionImpl<E, R>
/*    */   implements SetService<R>
/*    */ {
/*    */   private static final long serialVersionUID = 1536L;
/*    */   
/*    */   public MappedSetImpl(SetService<E> target, Function<? super E, ? extends R> function) {
/* 25 */     super((CollectionService)target, function);
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
/*    */   public SetService<R> threadSafe() {
/* 39 */     return new SharedSetImpl<R>(this);
/*    */   }
/*    */   
/*    */   public abstract boolean add(R paramR);
/*    */   
/*    */   public abstract boolean contains(Object paramObject);
/*    */   
/*    */   public abstract boolean remove(Object paramObject);
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/set/MappedSetImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */