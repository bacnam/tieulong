/*    */ package core.network.client2game.handler.base;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.PlayerMgr;
/*    */ import business.player.feature.PlayerBase;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.ClientSession;
/*    */ import core.network.client2game.handler.BaseHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CreatePlayer
/*    */   extends BaseHandler
/*    */ {
/*    */   public void handle(WebSocketRequest request, String message) throws IOException {
/* 19 */     ClientSession session = (ClientSession)request.getSession();
/*    */     
/* 21 */     Player player = PlayerMgr.getInstance().getPlayer(session.getOpenId(), session.getPlayerSid());
/* 22 */     if (player != null) {
/* 23 */       request.error(ErrorCode.Player_AlreadyExist, "玩家已经有账号了，禁止重复创号", new Object[0]);
/*    */       
/*    */       return;
/*    */     } 
/* 27 */     ErrorCode rslt = PlayerMgr.getInstance().createPlayerFirst(session, session.getOpenId(), session.getPlayerSid());
/* 28 */     if (rslt != ErrorCode.Success) {
/* 29 */       request.error(rslt, "创号时发生错误, 详细错误信息看服务端控制台or游戏服日志", new Object[0]);
/*    */       
/*    */       return;
/*    */     } 
/* 33 */     request.response(((PlayerBase)session.getPlayer().getFeature(PlayerBase.class)).fullInfo());
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/base/CreatePlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */