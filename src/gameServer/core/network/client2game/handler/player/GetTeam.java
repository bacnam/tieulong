/*    */ package core.network.client2game.handler.player;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.PlayerMgr;
/*    */ import com.google.gson.Gson;
/*    */ import com.google.gson.reflect.TypeToken;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import com.zhonglian.server.websocket.handler.response.ResponseHandler;
/*    */ import com.zhonglian.server.websocket.handler.response.WebSocketResponse;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import core.network.game2world.WorldConnector;
/*    */ import core.network.proto.TeamInfo;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ public class GetTeam
/*    */   extends PlayerHandler
/*    */ {
/*    */   private static class Request
/*    */   {
/*    */     long targetPid;
/*    */     int serverId;
/*    */   }
/*    */   
/*    */   public void handle(Player player, final WebSocketRequest request, String message) throws WSException, IOException {
/* 27 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 28 */     Player target = PlayerMgr.getInstance().getPlayer(req.targetPid);
/* 29 */     if (target == null && req.serverId != 0 && WorldConnector.getInstance().isConnected()) {
/* 30 */       WorldConnector.request("player.GetTeam", req, new ResponseHandler()
/*    */           {
/*    */             public void handleResponse(WebSocketResponse ssresponse, String body) throws WSException, IOException {
/* 33 */               TeamInfo info = (TeamInfo)(new Gson()).fromJson(body, (new TypeToken<TeamInfo>() {  }
/* 34 */                   ).getType());
/* 35 */               request.response(info);
/*    */             }
/*    */ 
/*    */             
/*    */             public void handleError(WebSocketResponse ssresponse, short statusCode, String message) {
/* 40 */               request.response();
/*    */             }
/*    */           });
/*    */     } else {
/* 44 */       request.response(new TeamInfo(target));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/player/GetTeam.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */