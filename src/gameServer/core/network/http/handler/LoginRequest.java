/*    */ package core.network.http.handler;
/*    */ 
/*    */ import com.zhonglian.server.http.annotation.RequestMapping;
/*    */ import com.zhonglian.server.http.server.HttpRequest;
/*    */ import com.zhonglian.server.http.server.HttpResponse;
/*    */ import core.server.ServerConfig;
/*    */ import java.io.File;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LoginRequest
/*    */ {
/*    */   @RequestMapping(uri = "/game/loginkey/index")
/*    */   public void index(HttpRequest request, HttpResponse response) {
/* 15 */     response.response(new File("conf/httpserver/login.html"));
/*    */   }
/*    */   
/*    */   @RequestMapping(uri = "/game/loginkey/refresh")
/*    */   public void create(HttpRequest request, HttpResponse response) {
/* 20 */     response.response("{\"loginKey\":" + ServerConfig.refreshLoginKey() + "}");
/*    */   }
/*    */   
/*    */   @RequestMapping(uri = "/game/loginkey/get")
/*    */   public void get(HttpRequest request, HttpResponse response) {
/* 25 */     response.response("{\"loginKey\":" + ServerConfig.getLoginKey() + "}");
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/http/handler/LoginRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */