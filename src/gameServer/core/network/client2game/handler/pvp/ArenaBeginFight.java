/*    */ package core.network.client2game.handler.pvp;
/*    */ 
/*    */ import business.global.arena.ArenaConfig;
/*    */ import business.global.arena.ArenaManager;
/*    */ import business.global.arena.Competitor;
/*    */ import business.global.fight.ArenaFight;
/*    */ import business.global.fight.Fight;
/*    */ import business.global.fight.FightFactory;
/*    */ import business.global.fight.FightManager;
/*    */ import business.player.Player;
/*    */ import business.player.feature.features.PlayerRecord;
/*    */ import com.google.gson.Gson;
/*    */ import com.zhonglian.server.common.enums.ConstEnum;
/*    */ import com.zhonglian.server.common.enums.UnlockType;
/*    */ import com.zhonglian.server.websocket.def.ErrorCode;
/*    */ import com.zhonglian.server.websocket.exception.WSException;
/*    */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*    */ import core.config.refdata.ref.RefUnlockFunction;
/*    */ import core.network.client2game.handler.PlayerHandler;
/*    */ import core.network.proto.Fight;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class ArenaBeginFight
/*    */   extends PlayerHandler
/*    */ {
/*    */   static class Request
/*    */   {
/*    */     long targetPid;
/*    */   }
/*    */   
/*    */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/* 32 */     RefUnlockFunction.checkUnlock(player, UnlockType.Arena);
/*    */     
/* 34 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/* 35 */     if (req.targetPid == player.getPid()) {
/* 36 */       throw new WSException(ErrorCode.Arena_WrongTarget, "玩家[%s]不能自攻自受", new Object[] { Long.valueOf(req.targetPid) });
/*    */     }
/* 38 */     ArenaManager manager = ArenaManager.getInstance();
/* 39 */     Competitor competitor = manager.getOrCreate(player.getPid());
/* 40 */     Competitor opponent = competitor.getOpponent(req.targetPid);
/* 41 */     if (opponent == null) {
/* 42 */       throw new WSException(ErrorCode.Arena_WrongTarget, "玩家[%s]不在玩家[%s]的挑战列表中", new Object[] { Long.valueOf(player.getPid()), Long.valueOf(req.targetPid) });
/*    */     }
/* 44 */     PlayerRecord recorder = (PlayerRecord)player.getFeature(PlayerRecord.class);
/* 45 */     int curTimes = recorder.getValue(ConstEnum.DailyRefresh.ArenaChallenge);
/* 46 */     if (curTimes >= ArenaConfig.maxChallengeTimes()) {
/* 47 */       throw new WSException(ErrorCode.Arena_ChallengeTimesRequired, "玩家[%s]的挑战次数不足", new Object[] { Long.valueOf(player.getPid()) });
/*    */     }
/* 49 */     if (competitor.getFightCD() > 0) {
/* 50 */       throw new WSException(ErrorCode.Arena_FightInCD, "玩家[%s]的冷却中,还有[%s]秒", new Object[] { Long.valueOf(player.getPid()), Integer.valueOf(competitor.getFightCD()) });
/*    */     }
/* 52 */     competitor.setFightCD(ArenaConfig.fightCD());
/*    */     
/* 54 */     ArenaFight fight = FightFactory.createFight(competitor, opponent);
/* 55 */     FightManager.getInstance().pushFight((Fight)fight);
/* 56 */     recorder.addValue(ConstEnum.DailyRefresh.ArenaChallenge);
/*    */     
/* 58 */     request.response(new Fight.Begin(fight.getId()));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/pvp/ArenaBeginFight.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */