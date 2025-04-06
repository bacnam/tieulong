/*    */ package core.network.client2game.handler.pvp;
/*    */ 
/*    */ import business.player.Player;
/*    */ import business.player.PlayerMgr;
/*    */ import business.player.feature.PlayerBase;
/*    */ import business.player.feature.pvp.DroiyanFeature;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.database.game.bo.DroiyanBO;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import core.network.proto.Player;
/*    */ import java.io.IOException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class DroiyanInfo
/*    */   extends PlayerHandler
/*    */ {
/*    */   private static PlayerMgr mananer;
/*    */   
/*    */   private static class Response {
/*    */     int point;
/*    */     int red;
/*    */     int lastSearchTime;
/*    */     List<Player.Summary> fihgters;
/*    */     Map<Integer, Integer> hp;
/*    */     
/*    */     public Response(DroiyanBO bo, Map<Integer, Integer> hp, List<Player.Summary> fihgters) {
/* 31 */       this.point = bo.getPoint();
/* 32 */       this.red = bo.getRed();
/* 33 */       this.lastSearchTime = bo.getLastSearchTime();
/* 34 */       this.fihgters = fihgters;
/* 35 */       this.hp = hp;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 41 */     DroiyanFeature feature = (DroiyanFeature)player.getFeature(DroiyanFeature.class);
/* 42 */     if (mananer == null) {
/* 43 */       mananer = PlayerMgr.getInstance();
/*    */     }
/*    */     
/* 46 */     feature.checkDroiyan();
/*    */     
/* 48 */     List<Player.Summary> fihgters = new ArrayList<>();
/* 49 */     for (Iterator<Long> iterator = feature.getBo().getFightersAll().iterator(); iterator.hasNext(); ) { long pid = ((Long)iterator.next()).longValue();
/* 50 */       if (pid == 0L) {
/*    */         continue;
/*    */       }
/* 53 */       Player tar = mananer.getPlayer(pid);
/* 54 */       Player.Summary summary = ((PlayerBase)tar.getFeature(PlayerBase.class)).summary();
/* 55 */       if (((DroiyanFeature)tar.getFeature(DroiyanFeature.class)).haveTreature()) {
/* 56 */         summary.name = "神秘玩家";
/*    */       }
/* 58 */       fihgters.add(summary); }
/*    */     
/* 60 */     request.response(new Response(feature.getBo(), feature.getHpMap(), fihgters));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/pvp/DroiyanInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */