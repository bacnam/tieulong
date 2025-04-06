/*    */ package core.network.client2game.handler.guildwar;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.feature.features.PlayerRecord;
/*    */ import com.zhonglian.server.common.enums.ConstEnum;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class GuildWarPersonInfo
/*    */   extends PlayerHandler
/*    */ {
/*    */   private static class PersonInfo
/*    */   {
/*    */     int inspireTime;
/*    */     
/*    */     private PersonInfo() {}
/*    */   }
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 22 */     PlayerRecord record = (PlayerRecord)player.getFeature(PlayerRecord.class);
/* 23 */     int times = record.getValue(ConstEnum.DailyRefresh.GuildwarInspire);
/* 24 */     PersonInfo info = new PersonInfo(null);
/* 25 */     info.inspireTime = times;
/* 26 */     request.response(info);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/guildwar/GuildWarPersonInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */