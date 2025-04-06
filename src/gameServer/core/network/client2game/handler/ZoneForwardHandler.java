/*    */ package core.network.client2game.handler;
/*    */ 
/*    */ import business.player.Player;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.def.MessageType;
/*    */ import com.zhonglian.server.websocket.def.TerminalType;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.MessageHeader;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.game2zone.ZoneConnector;
/*    */ import core.network.game2zone.ZoneSession;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ZoneForwardHandler
/*    */   extends PlayerHandler
/*    */ {
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 20 */     forwardToZone(player, request, message);
/*    */   }
/*    */   
/*    */   private void forwardToZone(Player player, WebSocketRequest request, String message) {
/* 24 */     ZoneSession zonesession = (ZoneSession)ZoneConnector.getInstance().getSocketSession();
/* 25 */     if (zonesession == null) {
/* 26 */       request.error(ErrorCode.Server_NotConnected, "未连接上Zone跨服服务器", new Object[0]);
/*    */       return;
/*    */     } 
/* 29 */     MessageHeader header = new MessageHeader();
/* 30 */     header.messageType = MessageType.Request;
/* 31 */     header.srcType = TerminalType.Client.value();
/* 32 */     header.srcId = player.getPid();
/* 33 */     header.descType = TerminalType.ZoneServer.value();
/* 34 */     header.descId = zonesession.getRemoteServerID();
/* 35 */     header.event = (request.getHeader()).event;
/* 36 */     header.sequence = (request.getHeader()).sequence;
/* 37 */     zonesession.forward(header, message);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/ZoneForwardHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */