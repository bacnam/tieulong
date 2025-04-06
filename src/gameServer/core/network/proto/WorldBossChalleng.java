/*    */ package core.network.proto;
/*    */ 
/*    */ import core.database.game.bo.WorldBossChallengeBO;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WorldBossChalleng
/*    */ {
/*    */   long id;
/*    */   long pid;
/*    */   long teamLevel;
/*    */   long challengeTimes;
/*    */   List<Long> inspiringTimes;
/*    */   List<Long> totalDamage;
/*    */   List<Long> damageRank;
/*    */   long beginFightTime;
/*    */   long leaveFightTime;
/*    */   long attackTimes;
/*    */   long fightCD;
/*    */   
/*    */   public WorldBossChalleng() {}
/*    */   
/*    */   public WorldBossChalleng(WorldBossChallengeBO bo) {
/* 25 */     this.id = bo.getId();
/* 26 */     this.pid = bo.getPid();
/* 27 */     this.teamLevel = bo.getTeamLevel();
/* 28 */     this.challengeTimes = bo.getChallengeTimes();
/* 29 */     this.inspiringTimes = bo.getInspiringTimesAll();
/* 30 */     this.totalDamage = bo.getTotalDamageAll();
/* 31 */     this.damageRank = bo.getDamageRankAll();
/* 32 */     this.beginFightTime = bo.getBeginFightTime();
/* 33 */     this.leaveFightTime = bo.getLeaveFightTime();
/* 34 */     this.attackTimes = bo.getAttackTimes();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/proto/WorldBossChalleng.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */