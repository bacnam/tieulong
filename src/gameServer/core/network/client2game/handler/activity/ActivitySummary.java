/*    */ package core.network.client2game.handler.activity;
/*    */ 
/*    */ import business.global.activity.Activity;
/*    */ import business.global.activity.ActivityMgr;
/*    */ import business.player.Player;
/*    */ import com.google.gson.Gson;
/*    */ import com.google.gson.reflect.TypeToken;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import com.zhonglian.server.websocket.handler.response.ResponseHandler;
/*    */ import com.zhonglian.server.websocket.handler.response.WebSocketResponse;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import core.network.game2world.WorldConnector;
/*    */ import java.io.IOException;
/*    */ import java.util.List;
/*    */ import java.util.stream.Collectors;
/*    */ import proto.gameworld.ActivityInfo;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ActivitySummary
/*    */   extends PlayerHandler
/*    */ {
/*    */   public void handle(final Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 26 */     List<Activity> activities = ActivityMgr.getInstance().getCurActivities();
/*    */ 
/*    */     
/* 29 */     List<ActivityInfo> localActivitys = (List<ActivityInfo>)activities.stream().map(x -> x.activitySummary(paramPlayer))
/*    */       
/* 31 */       .collect(Collectors.toList());
/*    */ 
/*    */     
/* 34 */     if (WorldConnector.getInstance().isConnected()) {
/* 35 */       WorldConnector.request("activity.ActivitySummary", "", new ResponseHandler()
/*    */           {
/*    */             public void handleResponse(WebSocketResponse ssresponse, String body) throws WSException, IOException {
/* 38 */               List<ActivityInfo> list = (List<ActivityInfo>)(new Gson()).fromJson(body, (new TypeToken<List<ActivityInfo>>() {  }
/* 39 */                   ).getType());
/* 40 */               player.pushProto("worldactivitysummary", list);
/*    */             }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */             
/*    */             public void handleError(WebSocketResponse ssresponse, short statusCode, String message) {}
/*    */           });
/*    */     }
/* 50 */     request.response(localActivitys);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/activity/ActivitySummary.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */