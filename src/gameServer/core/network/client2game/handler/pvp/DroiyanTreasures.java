/*    */ package core.network.client2game.handler.pvp;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.feature.features.PlayerRecord;
/*    */ import business.player.feature.pvp.DroiyanFeature;
/*    */ import com.zhonglian.server.common.enums.ConstEnum;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.database.game.bo.DroiyanTreasureBO;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ public class DroiyanTreasures
/*    */   extends PlayerHandler
/*    */ {
/*    */   private static class Respose
/*    */   {
/*    */     int openTimes;
/*    */     List<DroiyanTreasures.Treasure> treasures;
/*    */     
/*    */     private Respose(int openTimes, List<DroiyanTreasures.Treasure> treasures) {
/* 25 */       this.openTimes = openTimes;
/* 26 */       this.treasures = treasures;
/*    */     }
/*    */   }
/*    */   
/*    */   public static class Treasure {
/*    */     long sid;
/*    */     int treasureId;
/*    */     int expireTime;
/*    */     
/*    */     public Treasure(DroiyanTreasureBO bo) {
/* 36 */       this.sid = bo.getId();
/* 37 */       this.treasureId = bo.getTreasureId();
/* 38 */       this.expireTime = bo.getExpireTime();
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 45 */     List<DroiyanTreasureBO> list = ((DroiyanFeature)player.getFeature(DroiyanFeature.class)).getTreasures();
/* 46 */     List<Treasure> rtn = new ArrayList<>();
/* 47 */     for (DroiyanTreasureBO bo : list) {
/* 48 */       rtn.add(new Treasure(bo));
/*    */     }
/* 50 */     int times = ((PlayerRecord)player.getFeature(PlayerRecord.class)).getValue(ConstEnum.DailyRefresh.OpenTreasure);
/* 51 */     request.response(new Respose(times, rtn, null));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/pvp/DroiyanTreasures.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */