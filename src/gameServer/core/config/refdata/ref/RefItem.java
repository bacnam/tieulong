/*    */ package core.config.refdata.ref;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.data.RefField;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RefItem
/*    */   extends RefBaseGame
/*    */ {
/*    */   @RefField(iskey = true)
/*    */   public int ID;
/*    */   public String Name;
/*    */   public String IconID;
/*    */   public boolean CanUse;
/*    */   public boolean UseNow;
/*    */   public ArrayList<Integer> RewardList;
/*    */   public String Description;
/*    */   public int SuccessRatio;
/*    */   public int CarryExp;
/* 29 */   public static List<RefItem> refineStone = Lists.newArrayList();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean Assert() {
/* 36 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean AssertAll(RefContainer<?> all) {
/* 41 */     return true;
/*    */   }
/*    */   
/*    */   public static List<RefItem> getRefineStoneList() {
/* 45 */     return refineStone;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */