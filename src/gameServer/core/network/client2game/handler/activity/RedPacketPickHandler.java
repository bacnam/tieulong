/*    */ package core.network.client2game.handler.activity;
/*    */ 
/*    */ import business.global.activity.ActivityMgr;
/*    */ import business.global.activity.detail.RedPacket;
/*    */ import business.global.redpacket.RedPacketMgr;
/*    */ import business.player.Player;
/*    */ import business.player.feature.features.PlayerRecord;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.common.enums.ActivityStatus;
/*    */ import com.zhonglian.server.common.enums.ConstEnum;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import core.network.proto.RedPacketInfo;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class RedPacketPickHandler
/*    */   extends PlayerHandler
/*    */ {
/*    */   class Request
/*    */   {
/*    */     long id;
/*    */   }
/*    */   
/*    */   private static class Response {
/*    */     int pickTimes;
/*    */     int gain;
/*    */     RedPacketInfo packetInfo;
/*    */     
/*    */     public Response(int pickTimes, int gain, RedPacketInfo packetInfo) {
/* 32 */       this.pickTimes = pickTimes;
/* 33 */       this.gain = gain;
/* 34 */       this.packetInfo = packetInfo;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 41 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 42 */     RedPacket packet = (RedPacket)ActivityMgr.getActivity(RedPacket.class);
/* 43 */     if (packet.getStatus() == ActivityStatus.Close) {
/* 44 */       throw new WSException(ErrorCode.Activity_Close, "活动[%s]已经关闭", new Object[] { packet.getType() });
/*    */     }
/* 46 */     int gain = packet.pick(req.id, player);
/* 47 */     int pickTimes = ((PlayerRecord)player.getFeature(PlayerRecord.class)).getValue(ConstEnum.DailyRefresh.RedPacket);
/* 48 */     request.response(new Response(pickTimes, gain, RedPacketMgr.getInstance().getPacket(req.id, player)));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/activity/RedPacketPickHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */