/*    */ package core.network.client2game.handler.player;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.feature.character.Equip;
/*    */ import business.player.feature.character.EquipFeature;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import core.network.proto.EquipMessage;
/*    */ import java.io.IOException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ public class AllEquips
/*    */   extends PlayerHandler
/*    */ {
/* 17 */   private int limit = 200;
/*    */   
/*    */   private static class Response
/*    */   {
/*    */     int now;
/*    */     int max;
/*    */     List<EquipMessage> equips;
/*    */     
/*    */     private Response(int now, int max, List<EquipMessage> equips) {
/* 26 */       this.now = now;
/* 27 */       this.max = max;
/* 28 */       this.equips = equips;
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 35 */     List<Equip> equips = ((EquipFeature)player.getFeature(EquipFeature.class)).getAllEquips();
/* 36 */     int now = 1;
/* 37 */     int Max = equips.size() / (this.limit + 1) + 1;
/* 38 */     request.response(Integer.valueOf(Max));
/* 39 */     List<EquipMessage> rtn = new ArrayList<>();
/* 40 */     for (Equip bo : equips) {
/* 41 */       if (rtn.size() >= this.limit) {
/* 42 */         player.pushProto("allEquips", new Response(now, Max, rtn, null));
/* 43 */         now++;
/* 44 */         rtn = new ArrayList<>();
/*    */       } 
/* 46 */       rtn.add(new EquipMessage(bo.getBo()));
/*    */     } 
/* 48 */     if (rtn.size() != 0)
/* 49 */       player.pushProto("allEquips", new Response(now, Max, rtn, null)); 
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/player/AllEquips.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */