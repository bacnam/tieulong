/*    */ package core.network.client2game.handler.pvp;
/*    */ 
/*    */ import business.global.fight.BossFight;
/*    */ import business.global.fight.FightManager;
/*    */ import business.player.Player;
/*    */ import business.player.item.Reward;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.common.enums.FightResult;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import core.network.proto.Fight;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DungeonBossWin
/*    */   extends PlayerHandler
/*    */ {
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 23 */     Fight.End fightend = (Fight.End)(new Gson()).fromJson(message, Fight.End.class);
/* 24 */     BossFight fight = (BossFight)FightManager.getInstance().popFight(fightend.fightId);
/* 25 */     if (fight == null) {
/* 26 */       throw new WSException(ErrorCode.Arena_FightNotFound, "玩家[%s]提交的战斗[%s]不存在", new Object[] { Long.valueOf(player.getPid()), Integer.valueOf(fightend.fightId) });
/*    */     }
/* 28 */     fight.initAttr();
/* 29 */     fight.check(fightend.checks);
/* 30 */     Reward reward = (Reward)fight.settle(FightResult.Win);
/* 31 */     request.response(reward);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/pvp/DungeonBossWin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */