/*    */ package core.network.http.handler;
/*    */ 
/*    */ import business.gmcmd.cmd.CommandMgr;
/*    */ import business.player.Player;
/*    */ import business.player.PlayerMgr;
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.zhonglian.server.http.annotation.RequestMapping;
/*    */ import com.zhonglian.server.http.server.HttpRequest;
/*    */ import com.zhonglian.server.http.server.HttpResponse;
/*    */ import com.zhonglian.server.http.server.HttpUtils;
/*    */ import core.database.game.bo.ServerConfigBO;
/*    */ import core.server.BshRunner;
/*    */ import core.server.DynamicConfig;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ public class Control
/*    */ {
/*    */   @RequestMapping(uri = "/game/control/cmd")
/*    */   public void script(HttpRequest request, HttpResponse response) throws Exception {
/* 22 */     JsonObject json = HttpUtils.abstractGMParams(request.getRequestBody());
/* 23 */     String scrpit = HttpUtils.getString(json, "cmd");
/* 24 */     String mode = HttpUtils.getString(json, "mode");
/* 25 */     boolean async = HttpUtils.getBool(json, "async", false);
/* 26 */     if (async) {
/* 27 */       (new Thread(() -> runScript(paramString1, paramString2, paramHttpResponse))).start();
/*    */     } else {
/* 29 */       runScript(mode, scrpit, response);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void runScript(String mode, String script, HttpResponse response) {
/* 34 */     Object rslt = null;
/* 35 */     if (mode.equalsIgnoreCase("script")) {
/* 36 */       rslt = (new BshRunner()).eval(script);
/*    */     } else {
/*    */       try {
/* 39 */         String[] args = script.split(" ", 2);
/* 40 */         Player p = PlayerMgr.getInstance().getPlayer(Long.parseLong(args[0]));
/* 41 */         rslt = CommandMgr.getInstance().run(p, args[1]);
/* 42 */       } catch (Exception e) {
/* 43 */         JsonObject jsonObject = new JsonObject();
/* 44 */         jsonObject.addProperty("state", Integer.valueOf(30114));
/* 45 */         jsonObject.addProperty("msg", "执行命令[" + script + "]错误，异常：" + e.toString());
/* 46 */         response.response(jsonObject.toString());
/*    */         return;
/*    */       } 
/*    */     } 
/* 50 */     String msg = (rslt == null) ? "执行成功" : rslt.toString();
/*    */     
/* 52 */     JsonObject rtn = new JsonObject();
/* 53 */     rtn.addProperty("state", Integer.valueOf(1000));
/* 54 */     rtn.addProperty("msg", msg);
/* 55 */     response.response(rtn.toString());
/*    */   }
/*    */   
/*    */   @RequestMapping(uri = "/game/control/set")
/*    */   public void set(HttpRequest request, HttpResponse response) throws Exception {
/* 60 */     JsonObject json = HttpUtils.abstractGMParams(request.getRequestBody());
/* 61 */     String key = HttpUtils.getString(json, "key");
/* 62 */     String value = HttpUtils.getString(json, "value");
/*    */     
/* 64 */     DynamicConfig.set(key, value);
/*    */     
/* 66 */     JsonObject rtn = new JsonObject();
/* 67 */     rtn.addProperty("state", Integer.valueOf(1000));
/* 68 */     rtn.addProperty("msg", "set [" + key + "] to [" + value + "]");
/* 69 */     response.response(rtn.toString());
/*    */   }
/*    */   
/*    */   @RequestMapping(uri = "/game/control/setlist")
/*    */   public void setlist(HttpRequest request, HttpResponse response) throws Exception {
/* 74 */     HttpUtils.abstractGMParams(request.getRequestBody());
/* 75 */     Map<String, ServerConfigBO> settings = DynamicConfig.getAllConfig();
/*    */     
/* 77 */     JsonObject jSettings = new JsonObject();
/* 78 */     for (ServerConfigBO config : settings.values()) {
/* 79 */       jSettings.addProperty(config.getKey(), config.getValue());
/*    */     }
/*    */     
/* 82 */     String msg = "";
/* 83 */     JsonObject rtn = new JsonObject();
/* 84 */     rtn.addProperty("state", Integer.valueOf(1000));
/* 85 */     rtn.addProperty("msg", msg);
/* 86 */     rtn.add("settings", (JsonElement)jSettings);
/* 87 */     response.response(rtn.toString());
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/http/handler/Control.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */