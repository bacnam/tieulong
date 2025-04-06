/*    */ package core.network.world2game.handler.player;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.PlayerMgr;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.proto.TeamInfo;
/*    */ import core.network.world2game.handler.WBaseHandler;
/*    */ 
/*    */ public class GetTeam
/*    */   extends WBaseHandler
/*    */ {
/*    */   private static class Request
/*    */   {
/*    */     long targetPid;
/*    */   }
/*    */   
/*    */   public void handle(WebSocketRequest request, String message) throws WSException {
/* 20 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 21 */     Player target = PlayerMgr.getInstance().getPlayer(req.targetPid);
/* 22 */     if (target == null) {
/* 23 */       request.response();
/*    */       return;
/*    */     } 
/* 26 */     request.response(new TeamInfo(target));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/world2game/handler/player/GetTeam.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */