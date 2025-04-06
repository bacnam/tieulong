/*    */ package core.config.refdata.ref;
/*    */ 
/*    */ import BaseCommon.CommLog;
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.data.RefField;
/*    */ 
/*    */ public class RefTeamExp
/*    */   extends RefBaseGame
/*    */ {
/*    */   @RefField(iskey = true)
/*    */   public int id;
/*    */   public int UpExp;
/*    */   public int EnergyToExpRate;
/*    */   public int AddCurrEnergy;
/*    */   public int MaxEnergy;
/*    */   public int TokensToGoldRate;
/*    */   public int MaxTokens;
/*    */   public int TotalBattleNumber;
/*    */   public int TotalLittleFriend;
/*    */   public int LevelRewardID;
/*    */   
/*    */   public boolean Assert() {
/* 23 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean AssertAll(RefContainer<?> all) {
/* 28 */     if (all.size() == 0) {
/* 29 */       CommLog.error("[teanExp]配表不能为空");
/* 30 */       return false;
/*    */     } 
/* 32 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefTeamExp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */