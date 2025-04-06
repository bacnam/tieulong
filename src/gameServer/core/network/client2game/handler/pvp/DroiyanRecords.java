/*    */ package core.network.client2game.handler.pvp;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.feature.pvp.DroiyanFeature;
/*    */ import com.zhonglian.server.common.enums.FightResult;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.database.game.bo.DroiyanRecordBO;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ public class DroiyanRecords
/*    */   extends PlayerHandler
/*    */ {
/*    */   private static class Record
/*    */   {
/*    */     int time;
/*    */     String name;
/*    */     FightResult result;
/*    */     int point;
/*    */     int gold;
/*    */     int exp;
/*    */     int treasure;
/*    */     
/*    */     public Record(DroiyanRecordBO bo) {
/* 29 */       this.time = bo.getTime();
/* 30 */       this.name = bo.getTargetName();
/* 31 */       this.result = FightResult.values()[bo.getResult()];
/* 32 */       this.point = bo.getPoint();
/* 33 */       this.gold = bo.getGold();
/* 34 */       this.exp = bo.getExp();
/* 35 */       this.treasure = bo.getTreasure();
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 42 */     List<DroiyanRecordBO> list = ((DroiyanFeature)player.getFeature(DroiyanFeature.class)).getRecords();
/* 43 */     List<Record> rtn = new ArrayList<>();
/* 44 */     for (DroiyanRecordBO bo : list) {
/* 45 */       rtn.add(new Record(bo));
/*    */     }
/* 47 */     request.response(rtn);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/pvp/DroiyanRecords.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */