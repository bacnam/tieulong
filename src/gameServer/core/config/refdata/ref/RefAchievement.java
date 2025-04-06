/*    */ package core.config.refdata.ref;
/*    */ 
/*    */ import BaseCommon.CommLog;
/*    */ import com.zhonglian.server.common.data.RefContainer;
/*    */ import com.zhonglian.server.common.data.RefField;
/*    */ import com.zhonglian.server.common.enums.Achievement;
/*    */ import com.zhonglian.server.common.enums.ConstEnum;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RefAchievement
/*    */   extends RefBaseGame
/*    */ {
/*    */   @RefField(iskey = true)
/*    */   public int id;
/*    */   public Achievement.AchievementType AchieveName;
/*    */   public ConstEnum.AchieveClassify AchieveType;
/*    */   public ArrayList<Integer> PrizeIDList;
/*    */   public ArrayList<Integer> FirstArgsList;
/*    */   public ArrayList<Integer> SecondArgsList;
/*    */   public int Condition;
/*    */   public ConstEnum.TaskClassify TaskType;
/*    */   public int IsConditionWork;
/*    */   public int ConditionPreTaskId;
/*    */   public ConstEnum.AchieveReset Reset;
/*    */   public int ActiveScore;
/*    */   public int Days;
/*    */   public boolean IsHide;
/*    */   @RefField(isfield = false)
/* 39 */   public static Map<Achievement.AchievementType, RefAchievement> achieveName2ref = new HashMap<>();
/*    */   
/*    */   public static RefAchievement getByType(Achievement.AchievementType type) {
/* 42 */     RefAchievement ref = achieveName2ref.get(type);
/* 43 */     if (ref == null) {
/* 44 */       CommLog.warn("RefAchievement not find type:" + type);
/*    */     }
/* 46 */     return ref;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean Assert() {
/* 51 */     achieveName2ref.put(this.AchieveName, this);
/* 52 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean AssertAll(RefContainer<?> all) {
/* 57 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/config/refdata/ref/RefAchievement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */