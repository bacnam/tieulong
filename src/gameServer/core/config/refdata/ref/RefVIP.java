/*    */ package core.config.refdata.ref;
/*    */ 
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.data.RefField;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RefVIP
/*    */   extends RefBaseGame
/*    */ {
/*    */   @RefField(iskey = true)
/*    */   public int id;
/*    */   public int CurNeedExp;
/*    */   public int AddPackage;
/*    */   public int EquipInstanceTimes;
/*    */   public int GemInstanceTimes;
/*    */   public int MeridianInstanceTimes;
/*    */   public int ArenalTimes;
/*    */   public int DailyRefreshStoreTimes;
/*    */   public int PrivateGiftID;
/*    */   public int OfflineDungeonAdd;
/*    */   public int StealGold;
/*    */   public int GuildChallengeBuyTimes;
/*    */   public int OpenTreasureTimes;
/*    */   
/*    */   public boolean Assert() {
/* 46 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean AssertAll(RefContainer<?> all) {
/* 51 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefVIP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */