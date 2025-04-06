/*    */ package javolution.util.internal.set;
/*    */ 
/*    */ import javolution.util.internal.collection.CollectionView;
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
/*    */ 
/*    */ 
/*    */ public abstract class SetView<E>
/*    */   extends CollectionView<E>
/*    */   implements SetService<E>
/*    */ {
/*    */   private static final long serialVersionUID = 1536L;
/*    */   
/*    */   public SetView(SetService<E> target) {
/* 26 */     super((CollectionService)target);
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
/*    */   public SetService<E> threadSafe() {
/* 40 */     return new SharedSetImpl<E>(this);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected SetService<E> target() {
/* 46 */     return (SetService<E>)super.target();
/*    */   }
/*    */   
/*    */   public abstract int size();
/*    */   
/*    */   public abstract boolean contains(Object paramObject);
/*    */   
/*    */   public abstract boolean remove(Object paramObject);
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/util/internal/set/SetView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */