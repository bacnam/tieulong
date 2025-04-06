/*    */ package core.network.client2game.handler;
/*    */ 
/*    */ import BaseCommon.CommLog;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.RequestHandler;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class BaseHandler
/*    */   extends RequestHandler
/*    */ {
/*    */   public void handleMessage(WebSocketRequest request, String data) throws WSException, IOException {
/* 19 */     if (!(request.getSession() instanceof core.network.client2game.ClientSession)) {
/* 20 */       CommLog.warn("{} not handled.", (request.getHeader()).event);
/*    */       
/*    */       return;
/*    */     } 
/*    */     
/*    */     try {
/* 26 */       handle(request, data);
/* 27 */     } catch (Throwable e) {
/* 28 */       CommLog.error(String.valueOf(getClass().getName()) + " Exception: ", e);
/* 29 */       request.error(ErrorCode.Unknown, e.toString(), new Object[0]);
/*    */     } 
/*    */   }
/*    */   
/*    */   public abstract void handle(WebSocketRequest paramWebSocketRequest, String paramString) throws IOException;
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/BaseHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */