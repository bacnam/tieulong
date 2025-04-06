/*    */ package core.network.client2game.handler.base;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.PlayerMgr;
/*    */ import business.player.feature.PlayerBase;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.common.mgr.sensitive.SensitiveWordMgr;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.ClientSession;
/*    */ import core.network.client2game.handler.BaseHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ public class CreateRole
/*    */   extends BaseHandler
/*    */ {
/*    */   public static class RoleCreate
/*    */   {
/*    */     String name;
/*    */     int selected;
/*    */   }
/*    */   
/*    */   public void handle(WebSocketRequest request, String message) throws IOException {
/* 25 */     RoleCreate req = (RoleCreate)(new Gson()).fromJson(message, RoleCreate.class);
/*    */     
/* 27 */     ClientSession session = (ClientSession)request.getSession();
/*    */     
/* 29 */     Player player = PlayerMgr.getInstance().getPlayer(session.getOpenId(), session.getPlayerSid());
/* 30 */     if (player == null) {
/* 31 */       request.error(ErrorCode.Player_NotFound, "玩家未创号", new Object[0]);
/*    */       return;
/*    */     } 
/* 34 */     if (SensitiveWordMgr.getInstance().isContainsSensitiveWord(req.name)) {
/* 35 */       request.error(ErrorCode.InvalidParam, "玩家名字包含敏感词", new Object[0]);
/*    */       
/*    */       return;
/*    */     } 
/* 39 */     String name = "s" + session.getPlayerSid() + "." + req.name;
/* 40 */     ErrorCode rslt = PlayerMgr.getInstance().createRole(player, name, req.selected);
/* 41 */     if (rslt == ErrorCode.Player_AlreadyExist) {
/* 42 */       request.error(rslt, "玩家重名了，请换一个名字", new Object[0]); return;
/*    */     } 
/* 44 */     if (rslt != ErrorCode.Success) {
/* 45 */       request.error(rslt, "创号时发生错误, 详细错误信息看服务端控制台or游戏服日志", new Object[0]);
/*    */       
/*    */       return;
/*    */     } 
/* 49 */     request.response(((PlayerBase)session.getPlayer().getFeature(PlayerBase.class)).fullInfo());
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/base/CreateRole.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */