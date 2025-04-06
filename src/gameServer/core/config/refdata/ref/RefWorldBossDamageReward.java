/*    */ package core.config.refdata.ref;
/*    */ 
/*    */ import BaseCommon.CommLog;
/*    */ import com.zhonglian.server.common.data.RefAssert;
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.data.RefField;
/*    */ import com.zhonglian.server.common.utils.Lists;
/*    */ import com.zhonglian.server.common.utils.Maps;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RefWorldBossDamageReward
/*    */   extends RefBaseGame
/*    */ {
/*    */   @RefField(iskey = true)
/*    */   public int id;
/*    */   public int BossId;
/*    */   public int BeginRank;
/*    */   public int EndRank;
/*    */   public int MailId;
/*    */   @RefField(isfield = false)
/* 25 */   private static Map<Integer, List<RefWorldBossDamageReward>> damageRewardByBossId = Maps.newConcurrentMap();
/*    */ 
/*    */   
/*    */   public boolean Assert() {
/* 29 */     if (!RefAssert.inRef(Integer.valueOf(this.MailId), RefMail.class, new Object[0])) {
/* 30 */       return false;
/*    */     }
/* 32 */     if (!RefAssert.inRef(Integer.valueOf(this.BossId), RefWorldBoss.class, new Object[0])) {
/* 33 */       return false;
/*    */     }
/* 35 */     if (this.BeginRank > this.EndRank) {
/* 36 */       CommLog.error("BeginRank > EndRank");
/* 37 */       return false;
/*    */     } 
/* 39 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean AssertAll(RefContainer<?> all) {
/* 45 */     for (RefWorldBoss refWorldBoss : RefDataMgr.getAll(RefWorldBoss.class).values()) {
/* 46 */       damageRewardByBossId.put(Integer.valueOf(refWorldBoss.id), Lists.newArrayList());
/*    */     }
/* 48 */     for (RefWorldBossDamageReward damageReward : all.values()) {
/* 49 */       ((List<RefWorldBossDamageReward>)damageRewardByBossId.get(Integer.valueOf(damageReward.BossId))).add(damageReward);
/*    */     }
/* 51 */     return true;
/*    */   }
/*    */   
/*    */   public static RefWorldBossDamageReward getReward(int bossId, int rank) {
/* 55 */     List<RefWorldBossDamageReward> rewardList = damageRewardByBossId.get(Integer.valueOf(bossId));
/* 56 */     for (RefWorldBossDamageReward ref : rewardList) {
/* 57 */       if (rank >= ref.BeginRank && rank <= ref.EndRank) {
/* 58 */         return ref;
/*    */       }
/*    */     } 
/* 61 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefWorldBossDamageReward.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */