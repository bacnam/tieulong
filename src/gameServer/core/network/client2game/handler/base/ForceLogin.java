/*    */ package core.network.client2game.handler.base;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.PlayerMgr;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.ClientSession;
/*    */ import core.network.client2game.handler.BaseHandler;
/*    */ import core.server.ServerConfig;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ public class ForceLogin
/*    */   extends BaseHandler
/*    */ {
/*    */   private static class Request
/*    */   {
/*    */     public int loginkey;
/*    */     public long pid;
/*    */   }
/*    */   
/*    */   public void handle(WebSocketRequest request, String message) throws IOException {
/* 24 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 25 */     int loginkey = ServerConfig.getLoginKey();
/* 26 */     if (loginkey == 0 || req.loginkey != loginkey) {
/* 27 */       request.error(ErrorCode.InvalidParam, "loginkey 错误", new Object[0]);
/*    */       return;
/*    */     } 
/* 30 */     Player p = PlayerMgr.getInstance().getPlayer(req.pid);
/* 31 */     if (p == null) {
/* 32 */       request.error(ErrorCode.Player_NotFound, "指定玩家" + req.pid + "不存在", new Object[0]);
/*    */       
/*    */       return;
/*    */     } 
/* 36 */     ClientSession session = (ClientSession)request.getSession();
/* 37 */     session.setValid(true);
/*    */     
/* 39 */     PlayerMgr.getInstance().connectPlayer(session, p);
/* 40 */     session.setOpenId(p.getOpenId());
/* 41 */     session.setPlayerSid(p.getPlayerBO().getSid());
/* 42 */     request.response();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/base/ForceLogin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */