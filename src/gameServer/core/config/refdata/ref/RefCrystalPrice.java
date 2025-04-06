/*    */ package core.config.refdata.ref;
/*    */ 
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.data.RefField;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RefCrystalPrice
/*    */   extends RefBaseGame
/*    */ {
/*    */   @RefField(iskey = true)
/*    */   public int id;
/*    */   public int ArenaResetFightCD;
/*    */   public int ArenaResetRefreshCD;
/*    */   public int ArenaAddChallenge;
/*    */   public int DroiyanSearch;
/*    */   public int EquipInstanceAddChallenge;
/*    */   public int GemInstanceAddChallenge;
/*    */   public int MeridianInstanceAddChallenge;
/*    */   public int ActivityInstanceAddChallenge;
/*    */   public int PackageBuyTimes;
/*    */   public int StealGoldPrice;
/*    */   public int StealGoldGain;
/*    */   public int GuildBossBuyChallenge;
/*    */   public int GuildWarRebirth;
/*    */   public int GuildBossOpen;
/*    */   public int SacrificeCost;
/*    */   public int SacrificeExp;
/*    */   public int SacrificeDonate;
/*    */   public int LoversSend;
/*    */   public int GuildwarInspire;
/*    */   public int GuildwarInspireValue;
/*    */   public int LongnvDonate;
/*    */   public int LongnvCrystal;
/*    */   public int LongnvCrystalExp;
/*    */   public int LongnvRebirth;
/*    */   public int AutoFightWorldboss;
/*    */   @RefField(isfield = false)
/* 42 */   private static int max = 0;
/*    */ 
/*    */   
/*    */   public boolean Assert() {
/* 46 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean AssertAll(RefContainer<?> all) {
/* 51 */     max = all.size() - 1;
/*    */     
/* 53 */     return true;
/*    */   }
/*    */   
/*    */   public static RefCrystalPrice getPrize(int times) {
/* 57 */     if (times > max)
/* 58 */       times = max; 
/* 59 */     return (RefCrystalPrice)RefDataMgr.get(RefCrystalPrice.class, Integer.valueOf(times));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefCrystalPrice.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */