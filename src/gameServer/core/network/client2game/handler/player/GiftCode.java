/*    */ package core.network.client2game.handler.player;
/*    */ 
/*    */ import BaseCommon.CommLog;
/*    */ import business.global.gmmail.MailCenter;
/*    */ import business.player.Player;
/*    */ import com.google.gson.Gson;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.google.gson.JsonParser;
/*    */ import com.zhonglian.server.http.client.IResponseHandler;
/*    */ import com.zhonglian.server.http.server.GMParam;
/*    */ import com.zhonglian.server.http.server.HttpUtils;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class GiftCode extends PlayerHandler {
/*    */   private static class Request {
/*    */     String code;
/*    */   }
/*    */   
/*    */   private static class Response {
/*    */     int status;
/*    */     String msg;
/*    */     
/*    */     public Response(int status, String msg) {
/* 29 */       this.status = status;
/* 30 */       this.msg = msg;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void handle(final Player player, final WebSocketRequest request, String message) throws WSException, IOException {
/* 39 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/*    */ 
/*    */     
/* 42 */     GMParam params = new GMParam();
/* 43 */     params.put("code", req.code);
/* 44 */     params.put("uid", Long.valueOf(player.getPid()));
/* 45 */     params.put("cid", Long.valueOf(player.getPid()));
/* 46 */     params.put("lv", Integer.valueOf(player.getLv()));
/* 47 */     params.put("vipLv", Integer.valueOf(player.getVipLevel()));
/*    */     
/* 49 */     String baseurl = System.getProperty("downConfUrl");
/* 50 */     HttpUtils.RequestGM("http://" + baseurl + "/gm/giftNotice", params, new IResponseHandler()
/*    */         {
/*    */           public void compeleted(String response) {
/*    */             try {
/* 54 */               JsonObject json = (new JsonParser()).parse(response).getAsJsonObject();
/* 55 */               if (json.get("state").getAsInt() != 1000) {
/* 56 */                 request.response(new GiftCode.Response(json.get("state").getAsInt(), json.get("message").getAsString()));
/*    */                 return;
/*    */               } 
/* 59 */               json = json.get("gifid").getAsJsonObject();
/* 60 */               String sender = json.get("mailSender").getAsString();
/* 61 */               String title = json.get("mailTitle").getAsString();
/* 62 */               String content = json.get("mailContext").getAsString();
/* 63 */               StringBuilder itemids = new StringBuilder();
/* 64 */               StringBuilder counts = new StringBuilder();
/* 65 */               for (JsonElement j : json.get("itemlist").getAsJsonArray()) {
/* 66 */                 JsonObject item = j.getAsJsonObject();
/* 67 */                 itemids.append(item.get("uniformId").getAsInt()).append(";");
/* 68 */                 counts.append(item.get("count").getAsInt()).append(";");
/*    */               } 
/* 70 */               if (itemids.toString().endsWith(";")) {
/* 71 */                 itemids.deleteCharAt(itemids.length() - 1);
/*    */               }
/* 73 */               if (counts.toString().endsWith(";")) {
/* 74 */                 counts.deleteCharAt(counts.length() - 1);
/*    */               }
/*    */               
/* 77 */               MailCenter.getInstance().sendMail(player.getPid(), sender, title, content, itemids.toString(), counts.toString());
/*    */               
/* 79 */               request.response(new GiftCode.Response(1000, "ok"));
/* 80 */             } catch (Exception e) {
/* 81 */               request.response(new GiftCode.Response(500, "内部异常"));
/* 82 */               CommLog.error("订单充值失败,error", e);
/*    */             } 
/*    */           }
/*    */ 
/*    */           
/*    */           public void failed(Exception exception) {
/* 88 */             request.error(ErrorCode.RechargeOrderFailed, "兑换码使用失败", new Object[0]);
/*    */           }
/*    */         });
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/player/GiftCode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */