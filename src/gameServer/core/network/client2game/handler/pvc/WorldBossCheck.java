/*     */ package core.network.client2game.handler.pvc;
/*     */ 
/*     */ import business.global.rank.RankManager;
/*     */ import business.global.worldboss.WorldBossMgr;
/*     */ import business.player.Player;
/*     */ import business.player.PlayerMgr;
/*     */ import business.player.feature.PlayerBase;
/*     */ import business.player.feature.features.PlayerRecord;
/*     */ import business.player.feature.worldboss.WorldBossFeature;
/*     */ import com.google.gson.Gson;
/*     */ import com.zhonglian.server.common.enums.ConstEnum;
/*     */ import com.zhonglian.server.common.enums.RankType;
/*     */ import com.zhonglian.server.common.utils.CommTime;
/*     */ import com.zhonglian.server.websocket.exception.WSException;
/*     */ import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.database.game.bo.WorldBossBO;
/*     */ import core.database.game.bo.WorldBossChallengeBO;
/*     */ import core.network.client2game.handler.PlayerHandler;
/*     */ import core.network.proto.Player;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class WorldBossCheck
/*     */   extends PlayerHandler
/*     */ {
/*     */   public static class Request
/*     */   {
/*     */     int bossId;
/*     */   }
/*     */   
/*     */   public static class BossInfo
/*     */   {
/*     */     WorldBossBO boss;
/*     */     Player.Summary summary;
/*     */     long firstDamage;
/*     */     WorldBossChallengeBO challengBO;
/*     */     int myRank;
/*     */     long myDamage;
/*     */     int fightCD;
/*     */     Player.Summary LastKillPlayer;
/*     */     boolean isAuto;
/*     */     int autoTimes;
/*     */     
/*     */     public BossInfo(WorldBossBO boss, Player.Summary summary, long firstDamage, WorldBossChallengeBO challengBO, int myRank, long myDamage, int fightCD, Player.Summary LastKillPlayer, boolean isAuto, int autoTimes) {
/*  45 */       this.boss = boss;
/*  46 */       this.summary = summary;
/*  47 */       this.firstDamage = firstDamage;
/*  48 */       this.challengBO = challengBO;
/*  49 */       this.myRank = myRank;
/*  50 */       this.myDamage = myDamage;
/*  51 */       this.fightCD = fightCD;
/*  52 */       this.LastKillPlayer = LastKillPlayer;
/*  53 */       this.isAuto = isAuto;
/*  54 */       this.autoTimes = autoTimes;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
/*     */     Player.Summary summary;
/*  61 */     Request req = (Request)(new Gson()).fromJson(message, Request.class);
/*  62 */     RankType type = null;
/*  63 */     switch (req.bossId) {
/*     */       case 1:
/*  65 */         type = RankType.WorldBoss1;
/*     */         break;
/*     */       case 2:
/*  68 */         type = RankType.WorldBoss2;
/*     */         break;
/*     */       case 3:
/*  71 */         type = RankType.WorldBoss3;
/*     */         break;
/*     */       case 4:
/*  74 */         type = RankType.WorldBoss4;
/*     */         break;
/*     */     } 
/*     */ 
/*     */     
/*  79 */     WorldBossBO worldBoss = WorldBossMgr.getInstance().getBO(req.bossId);
/*  80 */     long pid = RankManager.getInstance().getPlayerId(type, 1);
/*  81 */     Player tar = PlayerMgr.getInstance().getPlayer(pid);
/*     */     
/*  83 */     if (tar != null) {
/*  84 */       summary = ((PlayerBase)tar.getFeature(PlayerBase.class)).summary();
/*     */     } else {
/*  86 */       summary = null;
/*     */     } 
/*  88 */     long damage = RankManager.getInstance().getValue(type, pid);
/*  89 */     int myRank = ((WorldBossFeature)player.getFeature(WorldBossFeature.class)).playerDamageRank(req.bossId);
/*  90 */     long myDamage = ((WorldBossFeature)player.getFeature(WorldBossFeature.class)).playerDamageNum(req.bossId);
/*  91 */     WorldBossChallengeBO challengBO = ((WorldBossFeature)player.getFeature(WorldBossFeature.class)).getOrCreate();
/*  92 */     int fightCD = (int)(CommTime.nowSecond() - challengBO.getLeaveFightTime());
/*  93 */     if (fightCD > RefDataMgr.getFactor("WorldBossAttackCD", 30)) {
/*  94 */       fightCD = 0;
/*     */     } else {
/*  96 */       fightCD = RefDataMgr.getFactor("WorldBossAttackCD", 30) - fightCD;
/*  97 */     }  long lastKillPid = worldBoss.getLastKillCid();
/*  98 */     Player lastKillPlayer = PlayerMgr.getInstance().getPlayer(lastKillPid);
/*  99 */     Player.Summary LastKillPlayersum = null;
/* 100 */     if (lastKillPlayer != null) {
/* 101 */       LastKillPlayersum = ((PlayerBase)lastKillPlayer.getFeature(PlayerBase.class)).summary();
/*     */     }
/* 103 */     boolean isAuto = ((WorldBossFeature)player.getFeature(WorldBossFeature.class)).getOrCreate().getAutoChallenge();
/* 104 */     int autoTimes = ((PlayerRecord)player.getFeature(PlayerRecord.class)).getValue(ConstEnum.DailyRefresh.AutoFightWorldboss);
/*     */     
/* 106 */     request.response(new BossInfo(worldBoss, summary, damage, challengBO, myRank, myDamage, fightCD, LastKillPlayersum, isAuto, autoTimes));
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/client2game/handler/pvc/WorldBossCheck.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */