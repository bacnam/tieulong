/*    */ package core.config.refdata.ref;
/*    */ 
/*    */ import BaseCommon.CommLog;
/*    */ import com.zhonglian.server.common.data.RefAssert;
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.data.RefField;
/*    */ import com.zhonglian.server.common.data.ref.matcher.NumberRange;
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RefTreasureWarspirit
/*    */   extends RefBaseGame
/*    */ {
/*    */   @RefField(iskey = true)
/*    */   public int id;
/*    */   public NumberRange LevelRange;
/*    */   public ArrayList<Integer> NormalIdList;
/*    */   public ArrayList<Integer> NormalCountList;
/*    */   public ArrayList<Integer> NormalWeightList;
/*    */   public ArrayList<Integer> FixedTenIdList;
/*    */   public ArrayList<Integer> FixedTenCountList;
/*    */   public ArrayList<Integer> FixedTenWeightList;
/*    */   public ArrayList<Integer> LeastIdList;
/*    */   public ArrayList<Integer> LeastTenCountList;
/*    */   public ArrayList<Integer> LeastTenWeightList;
/*    */   public int UniformId;
/*    */   public int UniformCount;
/*    */   public int Price;
/*    */   public int TenPrice;
/*    */   
/*    */   public boolean Assert() {
/* 35 */     if (!RefAssert.listSize(this.NormalIdList, this.NormalCountList, new List[] { this.NormalWeightList })) {
/* 36 */       CommLog.error("normal");
/* 37 */       return false;
/*    */     } 
/*    */     
/* 40 */     if (!RefAssert.listSize(this.FixedTenIdList, this.FixedTenCountList, new List[] { this.FixedTenWeightList })) {
/* 41 */       CommLog.error("fix");
/* 42 */       return false;
/*    */     } 
/* 44 */     if (!RefAssert.listSize(this.LeastIdList, this.LeastTenCountList, new List[] { this.LeastTenWeightList })) {
/* 45 */       CommLog.error("least");
/* 46 */       return false;
/*    */     } 
/* 48 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean AssertAll(RefContainer<?> all) {
/* 53 */     return true;
/*    */   }
/*    */   
/*    */   public static RefTreasureWarspirit getByLevel(int level) {
/* 57 */     for (RefTreasureWarspirit ref : RefDataMgr.getAll(RefTreasureWarspirit.class).values()) {
/* 58 */       if (ref.LevelRange.within(level)) {
/* 59 */         return ref;
/*    */       }
/*    */     } 
/*    */     
/* 63 */     return null;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefTreasureWarspirit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */