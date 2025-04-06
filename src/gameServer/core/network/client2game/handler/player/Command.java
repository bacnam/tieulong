/*    */ package core.network.client2game.handler.player;
/*    */ 
/*    */ import business.gmcmd.cmd.CommandMgr;
/*    */ import business.player.Player;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import com.zhonglian.server.websocket.handler.response.ResponseHandler;
/*    */ import com.zhonglian.server.websocket.handler.response.WebSocketResponse;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import core.network.game2world.WorldConnector;
/*    */ import core.network.game2zone.ZoneConnector;
/*    */ import java.io.IOException;
/*    */ import proto.common.GmCommand;
/*    */ 
/*    */ public class Command
/*    */   extends PlayerHandler
/*    */ {
/*    */   private static class Request
/*    */   {
/*    */     String cmd;
/*    */   }
/*    */   
/*    */   private static class Result {
/*    */     String rslt;
/*    */     
/*    */     private Result() {}
/*    */   }
/*    */   
/*    */   public void handle(Player player, final WebSocketRequest request, String message) throws WSException, IOException {
/* 32 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/*    */     
/* 34 */     String ip = player.getClientSession().remoteIP();
/* 35 */     if (!ip.startsWith("192.168.") && !ip.startsWith("127.0.0.") && player.getPlayerBO().getGmLevel() == 0) {
/* 36 */       throw new WSException(ErrorCode.Player_AccessDenied, "玩家不是GM账号，权限不足");
/*    */     }
/*    */     
/* 39 */     if (req.cmd.matches("^(z|Z)\\s.*")) {
/* 40 */       String cmd = req.cmd.replaceAll("^(z|Z)\\s", "");
/* 41 */       ZoneConnector.request("base.gmcommand", new GmCommand.G_GmCommand(player.getPid(), cmd), new ResponseHandler()
/*    */           {
/*    */             public void handleResponse(WebSocketResponse ssresponse, String body) throws WSException, IOException {
/* 44 */               request.response(body);
/*    */             }
/*    */ 
/*    */             
/*    */             public void handleError(WebSocketResponse ssresponse, short statusCode, String message) {
/* 49 */               request.error(statusCode, message, new Object[0]);
/*    */             }
/*    */           });
/* 52 */     } else if (req.cmd.matches("^(w|W)\\s.*")) {
/* 53 */       String cmd = req.cmd.replaceAll("^(w|W)\\s", "");
/* 54 */       WorldConnector.request("base.GmCommand", new GmCommand.G_GmCommand(player.getPid(), cmd), new ResponseHandler()
/*    */           {
/*    */             public void handleResponse(WebSocketResponse ssresponse, String body) throws WSException, IOException {
/* 57 */               request.response(body);
/*    */             }
/*    */ 
/*    */             
/*    */             public void handleError(WebSocketResponse ssresponse, short statusCode, String message) {
/* 62 */               request.error(statusCode, message, new Object[0]);
/*    */             }
/*    */           });
/*    */     } else {
/* 66 */       Result result = new Result(null);
/* 67 */       result.rslt = CommandMgr.getInstance().run(player, req.cmd);
/* 68 */       request.response(result);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/player/Command.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */