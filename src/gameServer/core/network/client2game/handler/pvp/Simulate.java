/*    */ package core.network.client2game.handler.pvp;
/*    */ 
/*    */ import business.global.battle.SimulatBattle;
/*    */ import business.player.Player;
/*    */ import business.player.PlayerMgr;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import core.network.proto.TeamInfo;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Simulate
/*    */   extends PlayerHandler
/*    */ {
/*    */   private static class Request
/*    */   {
/*    */     long targetPid;
/*    */   }
/*    */   
/*    */   private static class Response
/*    */   {
/*    */     long fightid;
/*    */     TeamInfo team;
/*    */   }
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 31 */     Request begin = (Request)(new Gson()).fromJson(message, Request.class);
/* 32 */     Player target = PlayerMgr.getInstance().getPlayer(begin.targetPid);
/* 33 */     if (target == null) {
/* 34 */       throw new WSException(ErrorCode.Player_NotFound, "玩家%s不存在", new Object[] { Long.valueOf(begin.targetPid) });
/*    */     }
/*    */     
/* 37 */     SimulatBattle battle = new SimulatBattle(player, target);
/* 38 */     battle.fight();
/* 39 */     request.response(battle.proto());
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/pvp/Simulate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */