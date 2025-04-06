/*    */ package core.network.client2game.handler.activity;
/*    */ 
/*    */ import business.global.activity.ActivityMgr;
/*    */ import business.global.activity.detail.RedPacket;
/*    */ import business.global.redpacket.RedPacketMgr;
/*    */ import business.player.Player;
/*    */ import business.player.feature.features.PlayerRecord;
/*    */ import com.zhonglian.server.common.enums.ActivityStatus;
/*    */ import com.zhonglian.server.common.enums.ConstEnum;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import core.network.proto.RedPacketInfo;
/*    */ import java.io.IOException;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ public class RedPacketListHandler
/*    */   extends PlayerHandler
/*    */ {
/*    */   private static class Response
/*    */   {
/*    */     int pickTimes;
/*    */     int maxPickTimes;
/*    */     List<RedPacketInfo> packetList;
/*    */     
/*    */     public Response(int pickTimes, int maxPickTimes, List<RedPacketInfo> packetList) {
/* 29 */       this.pickTimes = pickTimes;
/* 30 */       this.maxPickTimes = maxPickTimes;
/* 31 */       this.packetList = packetList;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 38 */     RedPacket packet = (RedPacket)ActivityMgr.getActivity(RedPacket.class);
/* 39 */     if (packet.getStatus() == ActivityStatus.Close) {
/* 40 */       throw new WSException(ErrorCode.Activity_Close, "活动[%s]已经关闭", new Object[] { packet.getType() });
/*    */     }
/* 42 */     int pickTimes = ((PlayerRecord)player.getFeature(PlayerRecord.class)).getValue(ConstEnum.DailyRefresh.RedPacket);
/* 43 */     int maxTimes = RedPacketMgr.getInstance().getMaxTime();
/* 44 */     request.response(new Response(pickTimes, maxTimes, packet.getPacketList(player)));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/activity/RedPacketListHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */