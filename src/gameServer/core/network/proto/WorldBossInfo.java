/*    */ package core.network.proto;
/*    */ 
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import core.config.refdata.ref.RefWorldBoss;
/*    */ import core.database.game.bo.WorldBossBO;
/*    */ 
/*    */ 
/*    */ public class WorldBossInfo
/*    */ {
/*    */   long id;
/*    */   long bossId;
/*    */   long bossHp;
/*    */   long bossMaxHp;
/*    */   long bossLevel;
/*    */   boolean isDead;
/*    */   long deadTime;
/*    */   long reviveTime;
/*    */   long lastKillCid;
/*    */   boolean canChalleng;
/*    */   
/*    */   public WorldBossInfo() {}
/*    */   
/*    */   public WorldBossInfo(WorldBossBO bo) {
/* 24 */     this.id = bo.getId();
/* 25 */     this.bossId = bo.getBossId();
/* 26 */     this.bossHp = bo.getBossHp();
/* 27 */     this.bossMaxHp = bo.getBossMaxHp();
/* 28 */     this.bossLevel = bo.getBossLevel();
/* 29 */     this.isDead = bo.getIsDead();
/* 30 */     this.deadTime = bo.getDeadTime();
/* 31 */     this.reviveTime = bo.getReviveTime();
/* 32 */     this.lastKillCid = bo.getLastKillCid();
/*    */     
/* 34 */     RefWorldBoss ref = (RefWorldBoss)RefDataMgr.get(RefWorldBoss.class, Integer.valueOf((int)bo.getBossId()));
/* 35 */     this.canChalleng = ref.isInOpenHour();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/proto/WorldBossInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */