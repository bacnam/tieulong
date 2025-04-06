/*     */ package business.player.feature.worldboss;
/*     */ 
/*     */ import business.global.gmmail.MailCenter;
/*     */ import business.global.rank.RankManager;
/*     */ import business.global.worldboss.WorldBossMgr;
/*     */ import business.player.Player;
/*     */ import business.player.feature.Feature;
/*     */ import business.player.feature.character.CharFeature;
/*     */ import com.zhonglian.server.common.db.BM;
/*     */ import com.zhonglian.server.common.enums.RankType;
/*     */ import com.zhonglian.server.common.utils.CommTime;
/*     */ import com.zhonglian.server.websocket.def.ErrorCode;
/*     */ import com.zhonglian.server.websocket.exception.WSException;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefMail;
/*     */ import core.config.refdata.ref.RefReward;
/*     */ import core.config.refdata.ref.RefWorldBoss;
/*     */ import core.database.game.bo.WorldBossBO;
/*     */ import core.database.game.bo.WorldBossChallengeBO;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Queue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WorldBossFeature
/*     */   extends Feature
/*     */ {
/*     */   public WorldBossChallengeBO challenge;
/*     */   private Queue<Long> lasttime;
/*     */   private double maxdamage;
/*     */   
/*     */   public WorldBossFeature(Player owner) {
/*  34 */     super(owner);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 161 */     this.lasttime = new LinkedList<>();
/* 162 */     this.maxdamage = 0.0D;
/* 163 */   } public void loadDB() { this.challenge = (WorldBossChallengeBO)BM.getBM(WorldBossChallengeBO.class).findOne("pid", Long.valueOf(this.player.getPid())); } public WorldBossChallengeBO getOrCreate() { WorldBossChallengeBO bo = this.challenge; if (bo != null) return bo;  synchronized (this) { bo = this.challenge; if (bo != null) return bo;  bo = new WorldBossChallengeBO(); bo.setPid(this.player.getPid()); bo.setTeamLevel(1L); bo.setInspiringTimesAll(0L); bo.setChallengeTimes(RefDataMgr.getFactor("WorldBossChallengeTimes", 10)); bo.insert(); this.challenge = bo; }  return bo; } public void buyChallengeTimes(int times) { WorldBossChallengeBO bo = getOrCreate(); bo.saveChallengeTimes(bo.getChallengeTimes() + times); } private static int fight_interval = 300;
/*     */   public void challengTimesRefresh() { WorldBossChallengeBO bo = getOrCreate(); bo.saveChallengeTimes(RefDataMgr.getFactor("WorldBossChallengeTimes", 10)); }
/*     */   public void dailyRefresh() { try {
/*     */       WorldBossChallengeBO bo = getOrCreate(); bo.setTeamLevel(this.player.getPlayerBO().getLv()); bo.setInspiringTimesAll(0L); bo.setChallengeTimes(RefDataMgr.getFactor("WorldBossChallengeTimes", 10)); bo.setTotalDamageAll(0L);
/*     */       bo.setLeaveFightTime(0L);
/*     */       bo.setAttackTimes(0L);
/*     */       bo.setResurrection(0);
/*     */       bo.saveAll();
/*     */     } catch (Exception e) {
/*     */       e.printStackTrace();
/* 173 */     }  } public boolean hurtWorldBoss(int bossId, long damage) throws WSException { long now = CommTime.nowMS();
/* 174 */     if (this.lasttime.size() >= ((CharFeature)this.player.getFeature(CharFeature.class)).size()) {
/* 175 */       long last = ((Long)this.lasttime.poll()).longValue() + fight_interval;
/*     */       
/* 177 */       if (now < last) {
/* 178 */         throw new WSException(ErrorCode.Fight_CheckFailed, "伤害CD中");
/*     */       }
/*     */     } 
/* 181 */     this.lasttime.add(Long.valueOf(now));
/* 182 */     if (damage > 100L && damage > this.maxdamage) {
/* 183 */       throw new WSException(ErrorCode.Fight_CheckFailed, "伤害异常");
/*     */     }
/*     */ 
/*     */     
/* 187 */     RefWorldBoss refBoss = (RefWorldBoss)RefDataMgr.get(RefWorldBoss.class, Integer.valueOf(bossId));
/* 188 */     if (!refBoss.isInOpenHour()) {
/* 189 */       throw new WSException(ErrorCode.WorldBoss_NotOpen, "世界boss尚未开放，无法挑战");
/*     */     }
/*     */     
/* 192 */     boolean isKill = WorldBossMgr.getInstance().doHurtWorldBoss(this.player, damage, bossId);
/*     */     
/* 194 */     dealKillBoss(isKill, bossId);
/* 195 */     this.player.getPlayerBO().setDungeonTime(CommTime.nowSecond());
/* 196 */     return false; }
/*     */   public void updateChallengeDamage(WorldBossChallengeBO bo, long damage, int bossId) { long totalDamage = bo.getTotalDamage(bossId - 1) + damage; if (totalDamage > WorldBossMgr.getInstance().getBO(bossId).getBossMaxHp()) { bo.saveTotalDamage(bossId - 1, WorldBossMgr.getInstance().getBO(bossId).getBossMaxHp()); } else { bo.saveTotalDamage(bossId - 1, totalDamage); }  RankType type = WorldBossMgr.getInstance().getRankType(bossId); RankManager.getInstance().update(type, this.player.getPid(), bo.getTotalDamage(bossId - 1)); }
/*     */   public WorldBossChallengeBO beginFightWorldBoss(int bossId) throws WSException { WorldBossMgr worldBossMgr = WorldBossMgr.getInstance(); RefWorldBoss ref = (RefWorldBoss)RefDataMgr.get(RefWorldBoss.class, Integer.valueOf(bossId)); if (!ref.isInOpenHour()) throw new WSException(ErrorCode.WorldBoss_NotOpen, "世界boss尚未开放，无法挑战");  WorldBossBO boss = worldBossMgr.getBO(bossId); if (boss.getIsDead())
/*     */       throw new WSException(ErrorCode.WorldBoss_IsDeath, "当前世界Boss已死，无法挑战");  WorldBossChallengeBO bo = this.challenge; int fightCD = RefDataMgr.getFactor("WorldBossAttackCD", 30); if (CommTime.nowSecond() - bo.getLeaveFightTime() < fightCD)
/* 200 */       throw new WSException(ErrorCode.WorldBoss_FightInCD, "玩家处于CD状态");  worldBossMgr.doEnterWorldBoss(this.player, boss); bo.setBeginFightTime(CommTime.nowSecond()); bo.setAttackTimes(bo.getAttackTimes() + 1L); bo.saveAll(); this.maxdamage = ((CharFeature)this.player.getFeature(CharFeature.class)).getMaxDamage(ref.BossId); return bo; } public void dealKillBoss(boolean isKill, int bossId) { if (isKill) {
/*     */       
/* 202 */       RefWorldBoss refBoss = (RefWorldBoss)RefDataMgr.get(RefWorldBoss.class, Integer.valueOf(bossId));
/* 203 */       RefMail refMail = (RefMail)RefDataMgr.get(RefMail.class, Integer.valueOf(refBoss.MailId));
/* 204 */       RefReward refReward = (RefReward)RefDataMgr.get(RefReward.class, Integer.valueOf(refMail.RewardId));
/* 205 */       WorldBossMgr.getInstance().addWorldBossKillRecord(bossId, this.player, refReward.genReward());
/* 206 */       MailCenter.getInstance().sendMail(this.player.getPid(), refBoss.MailId, new String[0]);
/*     */     }  }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public WorldBossChallengeBO LeaveWorldBoss(int bossId) throws WSException {
/* 220 */     this.challenge.saveLeaveFightTime(CommTime.nowSecond());
/* 221 */     WorldBossMgr.getInstance().doLeaveWorldBoss(this.player, bossId);
/* 222 */     return this.challenge;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int playerDamageRank(int bossId) {
/* 231 */     RankType type = null;
/* 232 */     switch (bossId) {
/*     */       case 1:
/* 234 */         type = RankType.WorldBoss1;
/*     */         break;
/*     */       case 2:
/* 237 */         type = RankType.WorldBoss2;
/*     */         break;
/*     */       case 3:
/* 240 */         type = RankType.WorldBoss3;
/*     */         break;
/*     */       case 4:
/* 243 */         type = RankType.WorldBoss4;
/*     */         break;
/*     */     } 
/*     */     
/* 247 */     return RankManager.getInstance().getRank(type, this.player.getPid());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long playerDamageNum(int bossId) {
/* 256 */     RankType type = null;
/* 257 */     switch (bossId) {
/*     */       case 1:
/* 259 */         type = RankType.WorldBoss1;
/*     */         break;
/*     */       case 2:
/* 262 */         type = RankType.WorldBoss2;
/*     */         break;
/*     */       case 3:
/* 265 */         type = RankType.WorldBoss3;
/*     */         break;
/*     */       case 4:
/* 268 */         type = RankType.WorldBoss4;
/*     */         break;
/*     */     } 
/*     */     
/* 272 */     return RankManager.getInstance().getValue(type, this.player.getPid());
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/player/feature/worldboss/WorldBossFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */