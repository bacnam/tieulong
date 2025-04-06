/*    */ package core.network.world2game.handler;
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
/*    */ public abstract class WBaseHandler
/*    */   extends RequestHandler
/*    */ {
/*    */   public void handleMessage(WebSocketRequest request, String data) throws WSException, IOException {
/*    */     try {
/* 17 */       handle(request, data);
/* 18 */     } catch (Throwable e) {
/* 19 */       CommLog.error(String.valueOf(getClass().getSimpleName()) + " Exception: ", e);
/* 20 */       request.error(ErrorCode.Unknown, e.toString(), new Object[0]);
/*    */     } 
/*    */   }
/*    */   
/*    */   public abstract void handle(WebSocketRequest paramWebSocketRequest, String paramString) throws WSException;
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/world2game/handler/WBaseHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */