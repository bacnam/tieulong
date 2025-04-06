/*     */ package core.network.client2game.handler.player;
/*     */ 
/*     */ import BaseCommon.CommLog;
/*     */ import business.global.recharge.RechargeMgr;
/*     */ import business.player.Player;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParser;
/*     */ import com.zhonglian.server.common.utils.secure.MD5;
/*     */ import com.zhonglian.server.http.client.IResponseHandler;
/*     */ import com.zhonglian.server.http.server.GMParam;
/*     */ import com.zhonglian.server.http.server.HttpUtils;
/*     */ import com.zhonglian.server.websocket.def.ErrorCode;
/*     */ import com.zhonglian.server.websocket.exception.WSException;
/*     */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefRecharge;
/*     */ import core.network.client2game.handler.PlayerHandler;
/*     */ import core.server.ServerConfig;
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ public class RechargeOrder
/*     */   extends PlayerHandler
/*     */ {
/*  26 */   private static RechargeMgr rechargeManager = null;
/*     */   
/*     */   private static class Request {
/*     */     String goodsId; }
/*     */   
/*     */   private static class PayData {
/*     */     String open_id;
/*     */     String access_token;
/*     */     String goods_name;
/*     */     String bill_no;
/*     */     int total_fee;
/*     */     String ext;
/*     */     String sign;
/*     */     
/*     */     private PayData() {}
/*     */     
/*     */     public String getSignSrc() {
/*  43 */       return "access_token=" + this.access_token + 
/*  44 */         "&bill_no=" + this.bill_no + 
/*  45 */         "&ext=" + this.ext + 
/*  46 */         "&goods_name=" + this.goods_name + 
/*  47 */         "&open_id=" + this.open_id + 
/*  48 */         "&secret_key=" + ServerConfig.AAY_SecretKey() + 
/*  49 */         "&total_fee=" + this.total_fee;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void handle(final Player player, final WebSocketRequest request, String message) throws WSException, IOException {
/*  55 */     if (rechargeManager == null) {
/*  56 */       rechargeManager = RechargeMgr.getInstance();
/*     */     }
/*  58 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/*     */     
/*  60 */     final RefRecharge ref = (RefRecharge)RefDataMgr.get(RefRecharge.class, req.goodsId);
/*  61 */     if (ref == null) {
/*  62 */       throw new WSException(ErrorCode.RechargeOrderFailed, "产品%s不存在", new Object[] { req.goodsId });
/*     */     }
/*     */     
/*  65 */     GMParam params = new GMParam();
/*  66 */     params.put("carrier", "aiaiu");
/*  67 */     params.put("adfrom", "aiaiu");
/*  68 */     params.put("adfrom2", "aiaiu");
/*  69 */     params.put("app_id", "10294");
/*  70 */     params.put("amount", Integer.valueOf(ref.Price));
/*  71 */     params.put("quantity", Integer.valueOf(1));
/*  72 */     params.put("crystal", Integer.valueOf(ref.Price));
/*  73 */     params.put("productid", req.goodsId);
/*  74 */     params.put("uid", player.getOpenId());
/*  75 */     params.put("cid", Long.valueOf(player.getPid()));
/*     */     
/*  77 */     String createorderurl = System.getProperty("RechargeCreateOrderAddr", 
/*  78 */         "http://106.75.142.3:8099/aiaiyou/createOrder");
/*  79 */     HttpUtils.RequestGM(createorderurl, params, new IResponseHandler()
/*     */         {
/*     */           public void compeleted(String response) {
/*     */             try {
/*  83 */               JsonObject resJson = (new JsonParser()).parse(response).getAsJsonObject();
/*  84 */               if (resJson.get("code").getAsInt() != 1000) {
/*  85 */                 CommLog.error("充值回调结果：{}", response);
/*  86 */                 request.error(ErrorCode.RechargeOrderFailed, "充值创建订单失败1", new Object[0]);
/*     */                 return;
/*     */               } 
/*  89 */               RechargeOrder.PayData data = new RechargeOrder.PayData(null);
/*  90 */               data.open_id = player.getOpenId();
/*  91 */               data.access_token = player.getClientSession().getAccessToken();
/*  92 */               data.bill_no = resJson.get("bill_no").getAsString();
/*  93 */               data.goods_name = ref.Title;
/*  94 */               data.total_fee = ref.Price;
/*  95 */               data.ext = String.valueOf(resJson.get("bill_no").getAsString());
/*  96 */               String signsrc = data.getSignSrc();
/*  97 */               data.sign = MD5.md5(signsrc);
/*  98 */               CommLog.info("sign src: {}, \nsign:{}", signsrc, data.sign);
/*  99 */               request.response(data);
/* 100 */             } catch (Exception e) {
/* 101 */               request.error(ErrorCode.RechargeOrderFailed, "充值创建订单失败2", new Object[] { e });
/* 102 */               CommLog.error("订单充值失败,error", e);
/*     */             } 
/*     */           }
/*     */ 
/*     */           
/*     */           public void failed(Exception exception) {
/* 108 */             request.error(ErrorCode.RechargeOrderFailed, "充值创建订单失败3", new Object[0]);
/*     */           }
/*     */         });
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/player/RechargeOrder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */