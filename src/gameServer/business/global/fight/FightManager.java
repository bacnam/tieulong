/*    */ package business.global.fight;
/*    */ 
/*    */ import BaseTask.SyncTask.SyncTaskManager;
/*    */ import com.zhonglian.server.common.enums.FightResult;
/*    */ import com.zhonglian.server.common.utils.CommTime;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FightManager
/*    */ {
/* 15 */   private static FightManager instacne = null;
/*    */   
/*    */   public static FightManager getInstance() {
/* 18 */     if (instacne == null) {
/* 19 */       instacne = new FightManager();
/*    */     }
/* 21 */     return instacne;
/*    */   }
/*    */   private Map<Integer, Fight> fights;
/*    */   private FightManager() {
/* 25 */     this.fights = new HashMap<>();
/*    */     
/* 27 */     SyncTaskManager.schedule(3000, () -> {
/*    */           checkFightOverTime();
/*    */           return true;
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int pushFight(Fight fight) {
/* 38 */     this.fights.put(Integer.valueOf(fight.fightId), fight);
/* 39 */     return fight.fightId;
/*    */   }
/*    */ 
/*    */   
/*    */   public Fight popFight(int fightid) {
/* 44 */     synchronized (this.fights) {
/* 45 */       return this.fights.remove(Integer.valueOf(fightid));
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   private void checkFightOverTime() {
/* 51 */     if (this.fights.size() <= 0) {
/*    */       return;
/*    */     }
/* 54 */     List<Fight> list = new ArrayList<>();
/* 55 */     synchronized (this.fights) {
/* 56 */       list.addAll(this.fights.values());
/*    */     } 
/*    */     
/* 59 */     List<Fight> overtime = new ArrayList<>();
/*    */     
/* 61 */     int cur = CommTime.nowSecond();
/* 62 */     for (Fight fight : list) {
/* 63 */       int fightTime = fight.fightTime();
/* 64 */       if (cur > fight.beginTime + fightTime) {
/* 65 */         overtime.add(fight);
/*    */       }
/*    */     } 
/*    */     
/* 69 */     synchronized (this.fights) {
/* 70 */       for (Fight fight : overtime) {
/* 71 */         this.fights.remove(Integer.valueOf(fight.fightId));
/* 72 */         fight.settle(FightResult.Lost);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void removeFight(Fight fight) {
/* 79 */     this.fights.remove(Integer.valueOf(fight.fightId));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/fight/FightManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */