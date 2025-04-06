/*    */ package core.network.client2game.handler.title;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.feature.player.NewTitleFeature;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class LoadTitleInfo
/*    */   extends PlayerHandler
/*    */ {
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 16 */     request.response(((NewTitleFeature)player.getFeature(NewTitleFeature.class)).getAllTitleInfo());
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/title/LoadTitleInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */