/*     */ package core.network.client2game.handler.base;
/*     */ 
/*     */ import BaseCommon.CommLog;
/*     */ import business.player.Player;
/*     */ import business.player.PlayerMgr;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParser;
/*     */ import com.zhonglian.server.common.utils.secure.MD5;
/*     */ import com.zhonglian.server.http.client.HttpAsyncClient;
/*     */ import com.zhonglian.server.http.client.IResponseHandler;
/*     */ import com.zhonglian.server.websocket.def.ErrorCode;
/*     */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*     */ import core.network.client2game.ClientSession;
/*     */ import core.network.client2game.handler.BaseHandler;
/*     */ import core.server.DynamicConfig;
/*     */ import core.server.ServerConfig;
/*     */ import core.server.ServerIDUtils;
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Login
/*     */   extends BaseHandler
/*     */ {
/*     */   private static class Request
/*     */   {
/*     */     public String open_id;
/*     */     public String access_token;
/*     */     public String serverid;
/*     */     public String client_version;
/*     */     public boolean testLogin;
/*     */     public String channelId;
/*     */   }
/*     */   
/*     */   public void handle(final WebSocketRequest request, String data) throws IOException {
/*  37 */     final Request req = (Request)(new Gson()).fromJson(data, Request.class);
/*     */     
/*  39 */     int onlineCount = PlayerMgr.getInstance().getOnlinePlayers().size();
/*  40 */     if (onlineCount >= DynamicConfig.get("OnlineLimit", 2000)) {
/*  41 */       request.error(ErrorCode.Login_OnlineLimit, "在线人数超过上限", new Object[0]);
/*     */       
/*     */       return;
/*     */     } 
/*  45 */     String sidString = req.serverid;
/*  46 */     final int sidInt = ServerIDUtils.getSidBySids(sidString);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  52 */     if (req.testLogin) {
/*  53 */       login(request, req, sidInt, new JsonObject());
/*     */     } else {
/*  55 */       String param = "access_token=" + req.access_token + "&open_id=" + req.open_id;
/*  56 */       String sign = MD5.md5(String.valueOf(param) + "&secret_key=" + ServerConfig.AAY_SecretKey());
/*  57 */       final String url = "http://passport.aiaiu.com/game/user/info?" + param + "&sign=" + sign;
/*     */       
/*  59 */       ClientSession session = (ClientSession)request.getSession();
/*  60 */       session.setAccessToken(req.access_token);
/*     */       
/*  62 */       HttpAsyncClient.startHttpGet(url, new IResponseHandler()
/*     */           {
/*     */             public void failed(Exception e)
/*     */             {
/*  66 */               request.error(ErrorCode.Login_ThirdAuthError, "和爱爱游服务器通信发生异常:%s", new Object[] { e.getMessage() });
/*     */             }
/*     */ 
/*     */ 
/*     */             
/*     */             public void compeleted(String res) {
/*     */               try {
/*  73 */                 JsonObject resJson = (new JsonParser()).parse(res).getAsJsonObject();
/*  74 */                 if (resJson.get("code").getAsInt() != 200) {
/*  75 */                   int code = resJson.get("code").getAsInt();
/*  76 */                   String msg = resJson.get("msg").getAsString();
/*  77 */                   CommLog.error("request:{}", url);
/*  78 */                   request.error(ErrorCode.Login_ThirdAuthError, "第三方验证失败, code:%s, msg:%s", new Object[] { Integer.valueOf(code), msg });
/*     */                   return;
/*     */                 } 
/*  81 */                 Login.this.login(request, req, sidInt, resJson.get("data").getAsJsonObject());
/*  82 */               } catch (Exception e) {
/*  83 */                 CommLog.error("解析爱爱游返回数据失败", e);
/*  84 */                 request.error(ErrorCode.Unknown, "解析爱爱游返回数据失败", new Object[0]);
/*     */               } 
/*     */             }
/*     */           });
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void login(WebSocketRequest request, Request req, int sidInt, JsonObject data) {
/*     */     try {
/*  95 */       ClientSession session = (ClientSession)request.getSession();
/*  96 */       session.setValid(true);
/*  97 */       session.setOpenId(req.open_id);
/*  98 */       session.setPlayerSid(sidInt);
/*  99 */       Player player = PlayerMgr.getInstance().getPlayer(session.getOpenId(), session.getPlayerSid());
/* 100 */       data.addProperty("channelId", req.channelId);
/* 101 */       if (player != null) {
/* 102 */         int bantime = player.getBanTime();
/* 103 */         if (bantime != 0) {
/* 104 */           request.error(ErrorCode.Login_PlayerBanned, getBanText(bantime, player.getPid()), new Object[0]);
/*     */           
/*     */           return;
/*     */         } 
/* 108 */         PlayerMgr.getInstance().connectPlayer(session, player);
/*     */       } else {
/* 110 */         PlayerMgr.getInstance().cachePlayerJson(req.open_id, data);
/*     */       } 
/*     */       
/* 113 */       request.response(data);
/* 114 */     } catch (Exception e) {
/* 115 */       CommLog.error("登录服务器失败", e);
/* 116 */       request.error(ErrorCode.Unknown, "登录服务器失败", new Object[0]);
/*     */     } 
/*     */   }
/*     */   
/*     */   private String getBanText(int bantime, long id) {
/* 121 */     if (bantime == -1)
/* 122 */       return "您被永久封号，请联系客服解封。\nid:" + id; 
/* 123 */     if (bantime > 86400)
/* 124 */       return "您被封号" + (bantime / 86400) + "天，请联系客服解封。\nid:" + id; 
/* 125 */     if (bantime > 3600)
/* 126 */       return "您被封号" + (bantime / 3600) + "小时，请联系客服解封。\nid:" + id; 
/* 127 */     if (bantime > 60) {
/* 128 */       return "您被封号" + (bantime / 60) + "分钟，请联系客服解封。\nid:" + id;
/*     */     }
/* 130 */     return "您被封号" + bantime + "秒，请联系客服解封。\nid:" + id;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/base/Login.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */