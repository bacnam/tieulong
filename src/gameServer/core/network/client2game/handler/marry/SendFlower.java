/*    */ package core.network.client2game.handler.marry;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.feature.features.PlayerRecord;
/*    */ import business.player.feature.marry.MarryFeature;
/*    */ import com.zhonglian.server.common.enums.ConstEnum;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SendFlower
/*    */   extends PlayerHandler
/*    */ {
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 18 */     MarryFeature feature = (MarryFeature)player.getFeature(MarryFeature.class);
/* 19 */     feature.sendFlower();
/* 20 */     PlayerRecord record = (PlayerRecord)player.getFeature(PlayerRecord.class);
/* 21 */     int times = record.getValue(ConstEnum.DailyRefresh.LoversSend);
/* 22 */     request.response(Integer.valueOf(times));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/marry/SendFlower.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */