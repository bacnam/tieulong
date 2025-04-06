/*    */ package core.network.client2game.handler.marry;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.feature.PlayerBase;
/*    */ import business.player.feature.marry.MarryFeature;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import core.network.proto.Player;
/*    */ import java.io.IOException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MarryList
/*    */   extends PlayerHandler
/*    */ {
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 20 */     List<Player.Summary> summary = new ArrayList<>();
/* 21 */     ((MarryFeature)player.getFeature(MarryFeature.class)).getMarryList().stream().forEach(x -> paramList.add(((PlayerBase)x.getFeature(PlayerBase.class)).summary()));
/*    */ 
/*    */     
/* 24 */     request.response(summary);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/marry/MarryList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */