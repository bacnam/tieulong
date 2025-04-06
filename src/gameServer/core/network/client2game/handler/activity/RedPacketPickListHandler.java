/*    */ package core.network.client2game.handler.activity;
/*    */ 
/*    */ import business.global.activity.ActivityMgr;
/*    */ import business.global.activity.detail.RedPacket;
/*    */ import business.global.redpacket.RedPacketMgr;
/*    */ import business.player.Player;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.common.enums.ActivityStatus;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import core.network.proto.RedPacketInfo;
/*    */ import core.network.proto.RedPacketPickInfo;
/*    */ import java.io.IOException;
/*    */ import java.util.List;
/*    */ 
/*    */ public class RedPacketPickListHandler
/*    */   extends PlayerHandler
/*    */ {
/*    */   class Request
/*    */   {
/*    */     long id;
/*    */   }
/*    */   
/*    */   private static class Response {
/*    */     RedPacketInfo info;
/*    */     List<RedPacketPickInfo> pickList;
/*    */     
/*    */     public Response(RedPacketInfo info, List<RedPacketPickInfo> pickList) {
/* 31 */       this.info = info;
/* 32 */       this.pickList = pickList;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 39 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 40 */     RedPacket packet = (RedPacket)ActivityMgr.getActivity(RedPacket.class);
/* 41 */     if (packet.getStatus() == ActivityStatus.Close) {
/* 42 */       throw new WSException(ErrorCode.Activity_Close, "活动[%s]已经关闭", new Object[] { packet.getType() });
/*    */     }
/* 44 */     RedPacketInfo info = RedPacketMgr.getInstance().getPacket(req.id, player);
/* 45 */     List<RedPacketPickInfo> pickList = packet.getPickList(req.id);
/* 46 */     request.response(new Response(info, pickList));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/activity/RedPacketPickListHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */