/*    */ package core.config.refdata.ref;
/*    */ 
/*    */ import business.player.item.Reward;
/*    */ import com.zhonglian.server.common.data.RefAssert;
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.data.RefField;
/*    */ import com.zhonglian.server.common.enums.RankType;
/*    */ import com.zhonglian.server.common.utils.Lists;
/*    */ import com.zhonglian.server.common.utils.Maps;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RefRankReward
/*    */   extends RefBaseGame
/*    */ {
/*    */   @RefField(iskey = true)
/*    */   public int id;
/*    */   public RankType Type;
/*    */   public int MailId;
/*    */   public int MinRank;
/*    */   public int MaxRank;
/*    */   @RefField(isfield = false)
/* 27 */   private static Map<RankType, List<RefRankReward>> rankRewardByType = Maps.newConcurrentMap();
/*    */   
/*    */   public Reward reward() {
/* 30 */     RefMail refMail = (RefMail)RefDataMgr.get(RefMail.class, Integer.valueOf(this.MailId));
/* 31 */     if (refMail == null) {
/* 32 */       return null;
/*    */     }
/* 34 */     RefReward refReward = (RefReward)RefDataMgr.get(RefReward.class, Integer.valueOf(refMail.RewardId));
/* 35 */     if (refReward == null) {
/* 36 */       return null;
/*    */     }
/* 38 */     return refReward.genReward();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean Assert() {
/* 43 */     if (!RefAssert.inRef(Integer.valueOf(this.MailId), RefMail.class, new Object[0])) {
/* 44 */       return false;
/*    */     }
/* 46 */     return true;
/*    */   }
/*    */   public boolean AssertAll(RefContainer<?> all) {
/*    */     byte b;
/*    */     int i;
/*    */     RankType[] arrayOfRankType;
/* 52 */     for (i = (arrayOfRankType = RankType.values()).length, b = 0; b < i; ) { RankType rankType = arrayOfRankType[b];
/* 53 */       rankRewardByType.put(rankType, Lists.newArrayList()); b++; }
/*    */     
/* 55 */     for (RefRankReward rankReward : all.values()) {
/* 56 */       ((List<RefRankReward>)rankRewardByType.get(rankReward.Type)).add(rankReward);
/*    */     }
/* 58 */     return true;
/*    */   }
/*    */   
/*    */   public static List<RefRankReward> getRewards(RankType rank) {
/* 62 */     return rankRewardByType.get(rank);
/*    */   }
/*    */   
/*    */   public static RefRankReward getReward(RankType type, int rank) {
/* 66 */     List<RefRankReward> rewardList = rankRewardByType.get(type);
/* 67 */     for (RefRankReward ref : rewardList) {
/* 68 */       if (rank >= ref.MinRank && (rank <= ref.MaxRank || ref.MaxRank == -1)) {
/* 69 */         return ref;
/*    */       }
/*    */     } 
/* 72 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefRankReward.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */