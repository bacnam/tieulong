/*    */ package core.config.refdata.ref;
/*    */ 
/*    */ import business.player.item.Reward;
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.data.RefField;
/*    */ import com.zhonglian.server.common.data.ref.matcher.NumberRange;
/*    */ import com.zhonglian.server.common.utils.Maps;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ public class RefGuildBossDamageReward
/*    */   extends RefBaseGame
/*    */ {
/*    */   @RefField(iskey = true)
/*    */   public int id;
/*    */   public NumberRange DamageRange;
/*    */   public int RewardId;
/*    */   @RefField(isfield = false)
/* 21 */   private static Map<Integer, List<RefGuildBossDamageReward>> damageRewardByBossId = Maps.newConcurrentMap();
/*    */ 
/*    */   
/*    */   public boolean Assert() {
/* 25 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean AssertAll(RefContainer<?> all) {
/* 30 */     return true;
/*    */   }
/*    */   
/*    */   public static Reward getReward(int damage) {
/* 34 */     for (RefGuildBossDamageReward ref : RefDataMgr.getAll(RefGuildBossDamageReward.class).values()) {
/* 35 */       if (ref.DamageRange.within(damage)) {
/* 36 */         RefReward refreward = (RefReward)RefDataMgr.get(RefReward.class, Integer.valueOf(ref.RewardId));
/* 37 */         return refreward.genReward();
/*    */       } 
/*    */     } 
/* 40 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefGuildBossDamageReward.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */