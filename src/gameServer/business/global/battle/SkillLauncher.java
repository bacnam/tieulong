/*    */ package business.global.battle;
/*    */ 
/*    */ import core.config.refdata.RefDataMgr;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ public class SkillLauncher
/*    */ {
/*  9 */   private static double ATK_CD = 0.0D;
/* 10 */   public static double MinATK = 0.0D;
/* 11 */   public static double CriticalMultiple = 0.0D;
/*    */   static {
/* 13 */     ATK_CD = RefDataMgr.getFactor("ATKCD", 1.0D);
/* 14 */     MinATK = RefDataMgr.getFactor("MinATK", 3);
/* 15 */     CriticalMultiple = RefDataMgr.getFactor("CriticalMultiple", 2000) / 1000.0D;
/*    */   }
/*    */   
/*    */   Creature creature;
/*    */   List<Skill> $skillList;
/*    */   double cd;
/*    */   
/*    */   SkillLauncher(Creature creature) {
/* 23 */     this.creature = creature;
/* 24 */     this.$skillList = new ArrayList<>();
/* 25 */     this.cd = ATK_CD;
/*    */   }
/*    */   
/*    */   public void addSkill(Skill skill) {
/* 29 */     this.$skillList.add(skill);
/*    */   }
/*    */   
/*    */   public void update(double dt) {
/* 33 */     if (this.cd > 0.0D)
/* 34 */       this.cd = Math.max(this.cd - dt, 0.0D); 
/* 35 */     for (Skill skill : this.$skillList) {
/* 36 */       skill.update(dt);
/*    */     }
/*    */   }
/*    */   
/*    */   public Skill nextSkill() {
/* 41 */     if (this.cd > 0.0D) {
/* 42 */       return null;
/*    */     }
/* 44 */     Skill skill = null;
/* 45 */     for (Skill s : this.$skillList) {
/* 46 */       if (!s.isEnable()) {
/*    */         continue;
/*    */       }
/* 49 */       if (skill == null || skill.getPriority() < s.getPriority()) {
/* 50 */         skill = s;
/*    */       }
/*    */     } 
/* 53 */     return skill;
/*    */   }
/*    */   
/*    */   public void resetCD() {
/* 57 */     this.cd = ATK_CD;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/battle/SkillLauncher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */