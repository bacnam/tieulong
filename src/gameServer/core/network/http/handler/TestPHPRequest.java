/*    */ package core.network.http.handler;
/*    */ 
/*    */ import com.zhonglian.server.http.annotation.RequestMapping;
/*    */ import com.zhonglian.server.http.server.HttpRequest;
/*    */ import com.zhonglian.server.http.server.HttpResponse;
/*    */ import java.io.File;
/*    */ 
/*    */ 
/*    */ public class TestPHPRequest
/*    */ {
/*    */   @RequestMapping(uri = "/game/test/index")
/*    */   public void index(HttpRequest request, HttpResponse response) {
/* 13 */     response.response(new File("conf/httpserver/test.html"));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/http/handler/TestPHPRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */