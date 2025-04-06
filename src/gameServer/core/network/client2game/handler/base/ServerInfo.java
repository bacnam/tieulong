/*    */ package core.network.client2game.handler.base;
/*    */ 
/*    */ import com.zhonglian.server.common.utils.CommTime;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.BaseHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class ServerInfo
/*    */   extends BaseHandler
/*    */ {
/*    */   private static class Info {
/*    */     private Info() {}
/*    */     
/* 14 */     int nowTime = CommTime.nowSecond();
/* 15 */     int timeZone = CommTime.timezone().getRawOffset();
/* 16 */     String version = "0.0.0";
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(WebSocketRequest request, String data) throws IOException {
/* 21 */     request.response(new Info(null));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/base/ServerInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */