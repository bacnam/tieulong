/*    */ package core.network.client2game.handler.marry;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.feature.PlayerBase;
/*    */ import business.player.feature.marry.MarryFeature;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import core.network.proto.Player;
/*    */ import java.io.IOException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ public class Search
/*    */   extends PlayerHandler
/*    */ {
/*    */   public static class Request
/*    */   {
/*    */     String name;
/*    */   }
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 25 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 26 */     List<Player> search = ((MarryFeature)player.getFeature(MarryFeature.class)).search(req.name);
/* 27 */     List<Player.Summary> summary = new ArrayList<>();
/* 28 */     search.stream().forEach(x -> paramList.add(((PlayerBase)x.getFeature(PlayerBase.class)).summary()));
/*    */ 
/*    */ 
/*    */     
/* 32 */     request.response(summary);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/marry/Search.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */