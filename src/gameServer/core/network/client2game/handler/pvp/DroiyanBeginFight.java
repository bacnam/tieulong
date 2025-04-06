/*    */ package core.network.client2game.handler.pvp;
/*    */ 
/*    */ import business.global.fight.DroiyanFight;
/*    */ import business.global.fight.Fight;
/*    */ import business.global.fight.FightFactory;
/*    */ import business.global.fight.FightManager;
/*    */ import business.player.Player;
/*    */ import business.player.PlayerMgr;
/*    */ import business.player.feature.pvp.DroiyanFeature;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import core.network.proto.Fight;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class DroiyanBeginFight
/*    */   extends PlayerHandler
/*    */ {
/*    */   private static class Request
/*    */   {
/*    */     long targetPid;
/*    */     boolean revenge = false;
/*    */   }
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 28 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 29 */     if (!req.revenge) {
/* 30 */       if (!((DroiyanFeature)player.getFeature(DroiyanFeature.class)).popDroiyan(req.targetPid)) {
/* 31 */         throw new WSException(ErrorCode.Droiyan_WrongTarget, "玩家[%s]没有遭遇[%s]无法攻击", new Object[] { Long.valueOf(player.getPid()), Long.valueOf(req.targetPid) });
/*    */       }
/*    */     }
/* 34 */     else if (!((DroiyanFeature)player.getFeature(DroiyanFeature.class)).popEnemy(req.targetPid)) {
/* 35 */       throw new WSException(ErrorCode.Droiyan_WrongTarget, "玩家[%s]没有仇人[%s]无法攻击", new Object[] { Long.valueOf(player.getPid()), Long.valueOf(req.targetPid) });
/*    */     } 
/*    */     
/* 38 */     DroiyanFeature atk = (DroiyanFeature)player.getFeature(DroiyanFeature.class);
/* 39 */     DroiyanFeature def = (DroiyanFeature)PlayerMgr.getInstance().getPlayer(req.targetPid).getFeature(DroiyanFeature.class);
/*    */     
/* 41 */     DroiyanFight fight = FightFactory.createFight(atk, def, req.revenge);
/* 42 */     int fightid = FightManager.getInstance().pushFight((Fight)fight);
/*    */     
/* 44 */     request.response(new Fight.Begin(fightid));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/pvp/DroiyanBeginFight.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */