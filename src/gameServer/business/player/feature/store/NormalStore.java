/*    */ package business.player.feature.store;
/*    */ 
/*    */ import business.global.store.Store;
/*    */ import business.player.Player;
/*    */ import com.zhonglian.server.common.enums.StoreType;
/*    */ 
/*    */ @Store({StoreType.ArenaStore, StoreType.NormalStore})
/*    */ public class NormalStore
/*    */   extends PlayerStore
/*    */ {
/*    */   public NormalStore(StoreType storeType, Player player) {
/* 12 */     super(storeType, player);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/player/feature/store/NormalStore.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */