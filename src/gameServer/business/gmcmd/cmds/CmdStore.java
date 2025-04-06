/*    */ package business.gmcmd.cmds;
/*    */ 
/*    */ import business.gmcmd.annotation.Command;
/*    */ import business.gmcmd.annotation.Commander;
/*    */ import business.player.Player;
/*    */ import business.player.feature.store.PlayerStore;
/*    */ import business.player.feature.store.StoreFeature;
/*    */ import business.player.feature.store.StoreRecord;
/*    */ import com.zhonglian.server.common.enums.StoreType;
/*    */ import com.zhonglian.server.common.utils.Lists;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ @Commander(name = "store", comment = "商城相关命令")
/*    */ public class CmdStore
/*    */ {
/*    */   @Command(comment = "增加免费刷新次数")
/*    */   public String addfreetimes(Player player, int times) {
/* 21 */     List<Integer> list = Lists.newArrayList(); byte b; int i; StoreType[] arrayOfStoreType;
/* 22 */     for (i = (arrayOfStoreType = StoreType.values()).length, b = 0; b < i; ) { StoreType type = arrayOfStoreType[b];
/* 23 */       list.add(Integer.valueOf(type.ordinal())); b++; }
/*    */     
/* 25 */     ((StoreFeature)player.getFeature(StoreFeature.class)).doAddFreeRefreshTimes(list, times);
/* 26 */     return "增加免费刷新次数";
/*    */   }
/*    */   
/*    */   @Command(comment = "手动刷新")
/*    */   public String hander(Player player, int typeId) throws WSException {
/* 31 */     if (typeId <= StoreType.None.ordinal() || typeId >= StoreType.Max.ordinal()) {
/* 32 */       return "非法的刷新类型";
/*    */     }
/* 34 */     StoreType storeType = StoreType.values()[typeId];
/* 35 */     StoreFeature feature = (StoreFeature)player.getFeature(StoreFeature.class);
/*    */     
/* 37 */     PlayerStore playerStore = feature.getOrCreate(storeType);
/*    */     
/* 39 */     playerStore.doAutoRefresh();
/*    */     
/* 41 */     return "手动刷新成功";
/*    */   }
/*    */   
/*    */   @Command(comment = "重置刷新次数等信息")
/*    */   public String record(Player player) throws WSException {
/* 46 */     ((StoreRecord)player.getFeature(StoreRecord.class)).dailyRefresh();
/* 47 */     return "重置刷新次数成功";
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/gmcmd/cmds/CmdStore.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */