/*    */ package core.network.client2game.handler;
/*    */ 
/*    */ import BaseCommon.CommLog;
/*    */ import business.player.Player;
/*    */ import com.zhonglian.server.common.utils.CommTime;
/*    */ import com.zhonglian.server.websocket.PkgCacheMgr;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.ClientSession;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class PlayerHandler
/*    */   extends BaseHandler
/*    */ {
/*    */   public void handle(WebSocketRequest request, String message) throws IOException {
/*    */     long costSec;
/* 25 */     ClientSession session = (ClientSession)request.getSession();
/* 26 */     if (!session.isValid()) {
/*    */       return;
/*    */     }
/* 29 */     Player player = session.getPlayer();
/* 30 */     if (player == null) {
/* 31 */       request.error(ErrorCode.Player_NotLogin, "玩家未登陆", new Object[0]);
/* 32 */       session.close();
/*    */       return;
/*    */     } 
/* 35 */     PkgCacheMgr.PkgRecv cachedRecv = player.getPacketCache().fetchSentOrRegist(request.getHeader());
/* 36 */     if (cachedRecv != null) {
/* 37 */       if (cachedRecv.getBody() != null) {
/* 38 */         session.sendPacket(cachedRecv.getHeader(), cachedRecv.getBody());
/*    */       }
/*    */       return;
/*    */     } 
/* 42 */     long curTime = CommTime.nowMS();
/*    */     try {
/* 44 */       player.lockIns();
/* 45 */       handle(player, request, message);
/* 46 */     } catch (WSException e) {
/* 47 */       CommLog.warn("[PlayerHandler]:[{}] cid:{} error:{}", new Object[] { (request.getHeader()).event, Long.valueOf(player.getPid()), e.getMessage() });
/* 48 */       request.error(e.getErrorCode(), e.getMessage(), new Object[0]);
/* 49 */     } catch (Exception e) {
/* 50 */       CommLog.error("[PlayerHandler]:[{}] cid:{} error:{}", new Object[] { (request.getHeader()).event, Long.valueOf(player.getPid()), e.getMessage(), e });
/* 51 */       request.error(ErrorCode.Unknown, "服务端发生异常，异常信息：" + e.getMessage(), new Object[0]);
/*    */     } finally {
/* 53 */       player.unlockIns();
/* 54 */       long l = CommTime.nowMS() - curTime;
/* 55 */       if (l >= 1000L)
/* 56 */         CommLog.error("[PlayerHandler]:[{}] cid:{} overtime cost:{}", new Object[] { (request.getHeader()).event, Long.valueOf(player.getPid()), Long.valueOf(l) }); 
/*    */     } 
/*    */   }
/*    */   
/*    */   public abstract void handle(Player paramPlayer, WebSocketRequest paramWebSocketRequest, String paramString) throws WSException, IOException;
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/PlayerHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */