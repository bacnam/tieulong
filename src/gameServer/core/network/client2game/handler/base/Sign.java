/*    */ package core.network.client2game.handler.base;
/*    */ 
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.common.utils.secure.MD5;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.BaseHandler;
/*    */ import core.server.ServerConfig;
/*    */ import java.io.IOException;
/*    */ import java.util.List;
/*    */ 
/*    */ public class Sign
/*    */   extends BaseHandler
/*    */ {
/*    */   private static class Pair
/*    */   {
/*    */     String key;
/*    */     String value;
/*    */   }
/*    */   
/*    */   private static class Request {
/*    */     List<Sign.Pair> params;
/*    */   }
/*    */   
/*    */   private static class Response {
/*    */     String sign;
/*    */     
/*    */     private Response() {}
/*    */   }
/*    */   
/*    */   public void handle(WebSocketRequest request, String data) throws IOException {
/* 31 */     Request req = (Request)(new Gson()).fromJson(data, Request.class);
/* 32 */     StringBuilder sb = new StringBuilder();
/* 33 */     for (Pair pair : req.params) {
/* 34 */       sb.append(pair.key).append('=').append(pair.value).append('&');
/*    */     }
/* 36 */     sb.append("secret_key").append('=').append(ServerConfig.AAY_SecretKey());
/*    */     
/* 38 */     Response response = new Response(null);
/* 39 */     response.sign = MD5.md5(sb.toString());
/* 40 */     request.response(response);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/base/Sign.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */