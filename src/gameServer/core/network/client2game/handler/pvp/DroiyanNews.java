/*    */ package core.network.client2game.handler.pvp;
/*    */ 
/*    */ import business.global.pvp.EncouterManager;
/*    */ import business.player.Player;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DroiyanNews
/*    */   extends PlayerHandler
/*    */ {
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 16 */     request.response(EncouterManager.getInstance().getEncouterNews());
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/pvp/DroiyanNews.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */