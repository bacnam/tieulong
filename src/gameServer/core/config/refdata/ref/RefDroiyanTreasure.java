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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RefDroiyanTreasure
/*    */   extends RefBaseGame
/*    */ {
/*    */   @RefField(iskey = true)
/*    */   public int id;
/*    */   public int LevelMax;
/*    */   public int Time;
/*    */   public int Rate;
/*    */   public int Reward;
/*    */   public int NormalWeight;
/*    */   public int RevengeWeight;
/*    */   public int Quality;
/*    */   
/*    */   public boolean Assert() {
/* 32 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean AssertAll(RefContainer<?> all) {
/* 37 */     return true;
/*    */   }
/*    */   
/*    */   public static RefDroiyanTreasure findTreature(int range) {
/* 41 */     RefContainer<RefDroiyanTreasure> all = RefDataMgr.getAll(RefDroiyanTreasure.class);
/* 42 */     RefDroiyanTreasure preOne = null;
/* 43 */     int total_range = 0;
/* 44 */     for (RefDroiyanTreasure ref : all.values()) {
/* 45 */       total_range += ref.Rate;
/* 46 */       if (range <= total_range) {
/* 47 */         preOne = ref;
/*    */         break;
/*    */       } 
/*    */     } 
/* 51 */     return preOne;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefDroiyanTreasure.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */