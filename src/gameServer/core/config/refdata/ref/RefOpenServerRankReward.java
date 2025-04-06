/*    */ package core.config.refdata.ref;
/*    */ 
/*    */ import com.zhonglian.server.common.data.RefAssert;
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.data.RefField;
/*    */ import com.zhonglian.server.common.data.ref.matcher.NumberRange;
/*    */ import com.zhonglian.server.common.enums.ConstEnum;
/*    */ import com.zhonglian.server.common.utils.Lists;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RefOpenServerRankReward
/*    */   extends RefBaseGame
/*    */ {
/*    */   @RefField(iskey = true)
/*    */   public int id;
/*    */   public NumberRange RankRange;
/*    */   public ConstEnum.RankRewardType Type;
/*    */   public int MailId;
/*    */   @RefField(isfield = false)
/* 24 */   public static Map<ConstEnum.RankRewardType, List<RefOpenServerRankReward>> RankRewardByType = new HashMap<>();
/*    */ 
/*    */   
/*    */   public boolean Assert() {
/* 28 */     if (!RefAssert.inRef(Integer.valueOf(this.MailId), RefMail.class, new Object[0])) {
/* 29 */       return false;
/*    */     }
/* 31 */     return true;
/*    */   }
/*    */   public boolean AssertAll(RefContainer<?> all) {
/*    */     byte b;
/*    */     int i;
/*    */     ConstEnum.RankRewardType[] arrayOfRankRewardType;
/* 37 */     for (i = (arrayOfRankRewardType = ConstEnum.RankRewardType.values()).length, b = 0; b < i; ) { ConstEnum.RankRewardType type = arrayOfRankRewardType[b];
/* 38 */       RankRewardByType.put(type, Lists.newArrayList());
/*    */       b++; }
/*    */     
/* 41 */     for (RefOpenServerRankReward ref : all.values()) {
/* 42 */       ((List<RefOpenServerRankReward>)RankRewardByType.get(ref.Type)).add(ref);
/*    */     }
/*    */     
/* 45 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefOpenServerRankReward.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */