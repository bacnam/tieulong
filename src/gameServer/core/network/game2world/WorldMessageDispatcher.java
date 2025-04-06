/*    */ package core.network.game2world;
/*    */ 
/*    */ import BaseCommon.CommLog;
/*    */ import business.player.Player;
/*    */ import business.player.PlayerMgr;
/*    */ import com.zhonglian.server.websocket.def.MessageType;
/*    */ import com.zhonglian.server.websocket.def.TerminalType;
/*    */ import com.zhonglian.server.websocket.handler.MessageDispatcher;
/*    */ import com.zhonglian.server.websocket.handler.MessageHeader;
/*    */ import com.zhonglian.server.websocket.handler.requset.NotifyDispatcher;
/*    */ import com.zhonglian.server.websocket.handler.requset.RequestDispatcher;
/*    */ import com.zhonglian.server.websocket.handler.response.ResponseDispatcher;
/*    */ import com.zhonglian.server.websocket.server.ServerMessageDispatcher;
/*    */ import com.zhonglian.server.websocket.server.ServerSession;
/*    */ import core.network.client2game.ClientSession;
/*    */ import core.network.world2game.handler.WBaseHandler;
/*    */ import core.server.ServerConfig;
/*    */ 
/*    */ public class WorldMessageDispatcher
/*    */   extends ServerMessageDispatcher<WorldSession> {
/*    */   public void init() {
/* 22 */     RequestDispatcher<WorldSession> dRequest = new RequestDispatcher<WorldSession>(TerminalType.GameServer, ServerConfig.ServerID())
/*    */       {
/*    */         
/*    */         public void forward(WorldSession session, MessageHeader header, String body)
/*    */         {
/* 27 */           CommLog.info("[WorldServerConnector]从World处收到一条转发至[{},{}]的Request信息", Byte.valueOf(header.descType), Long.valueOf(header.descId));
/*    */         }
/*    */       };
/*    */     
/* 31 */     putDispatcher(MessageType.Request, (MessageDispatcher)dRequest);
/* 32 */     NotifyDispatcher<WorldSession> dNotify = new NotifyDispatcher<WorldSession>(TerminalType.GameServer, ServerConfig.ServerID(), dRequest)
/*    */       {
/*    */         public void forward(WorldSession session, MessageHeader header, String body)
/*    */         {
/* 36 */           CommLog.error("[WorldServerConnector]从World处收到一条转发至[{},{}]的Notify信息", Byte.valueOf(header.descType), Long.valueOf(header.descId));
/*    */         }
/*    */       };
/* 39 */     putDispatcher(MessageType.Notify, (MessageDispatcher)dNotify);
/*    */     
/* 41 */     ResponseDispatcher<WorldSession> dResponse = new ResponseDispatcher<WorldSession>(TerminalType.GameServer, ServerConfig.ServerID())
/*    */       {
/*    */         
/*    */         public void forward(WorldSession session, MessageHeader header, String body)
/*    */         {
/* 46 */           if (header.descType != TerminalType.Client.value()) {
/* 47 */             CommLog.error("[WorldServerConnector]从World处收到一条转发至[{},{}]的Response信息", Byte.valueOf(header.descType), Long.valueOf(header.descId));
/*    */             return;
/*    */           } 
/* 50 */           Player player = PlayerMgr.getInstance().getOnlinePlayerByCid(header.descId);
/* 51 */           if (player == null) {
/*    */             return;
/*    */           }
/*    */           
/* 55 */           ClientSession clientSession = player.getClientSession();
/* 56 */           if (clientSession == null) {
/*    */             return;
/*    */           }
/*    */           
/* 60 */           clientSession.sendPacket(header, body);
/*    */         }
/*    */       };
/* 63 */     putDispatcher(MessageType.Response, (MessageDispatcher)dResponse);
/*    */     
/* 65 */     registerRequestHandlers(WBaseHandler.class);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/game2world/WorldMessageDispatcher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */