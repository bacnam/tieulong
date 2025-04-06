/*    */ package core.network.http.handler;
/*    */ 
/*    */ import business.global.chat.ChatMgr;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.zhonglian.server.common.enums.ChatType;
/*    */ import com.zhonglian.server.http.annotation.RequestMapping;
/*    */ import com.zhonglian.server.http.server.HttpRequest;
/*    */ import com.zhonglian.server.http.server.HttpResponse;
/*    */ import com.zhonglian.server.http.server.HttpUtils;
/*    */ 
/*    */ 
/*    */ public class ChatRequest
/*    */ {
/*    */   @RequestMapping(uri = "/game/chat/answer")
/*    */   public void answer(HttpRequest request, HttpResponse response) throws Exception {
/* 16 */     JsonObject chat = HttpUtils.abstractGMParams(request.getRequestBody());
/* 17 */     String content = HttpUtils.getString(chat, "content");
/* 18 */     long toCId = HttpUtils.getLong(chat, "receiveId");
/* 19 */     ChatMgr.getInstance().addChat(null, content, ChatType.CHATTYPE_GM, toCId);
/* 20 */     response.response("ok");
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/http/handler/ChatRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */