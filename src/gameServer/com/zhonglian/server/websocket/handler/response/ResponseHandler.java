/*    */ package com.zhonglian.server.websocket.handler.response;
/*    */ 
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.IBaseHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public abstract class ResponseHandler
/*    */   extends IBaseHandler {
/*    */   public ResponseHandler() {
/* 10 */     super("WSResponseHandler");
/*    */   }
/*    */   
/*    */   public abstract void handleResponse(WebSocketResponse paramWebSocketResponse, String paramString) throws WSException, IOException;
/*    */   
/*    */   public abstract void handleError(WebSocketResponse paramWebSocketResponse, short paramShort, String paramString);
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/websocket/handler/response/ResponseHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */