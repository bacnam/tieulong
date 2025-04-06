/*    */ package business.global.battle;
/*    */ 
/*    */ import core.config.refdata.ref.RefSkill;
/*    */ import core.network.proto.Fight;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Skill
/*    */ {
/*    */   Creature creature;
/*    */   RefSkill skilldata;
/*    */   int skilllv;
/*    */   public double strength;
/*    */   double cd;
/*    */   ISettler settler;
/*    */   Selector selector;
/*    */   
/*    */   Skill(Creature creature, RefSkill refSkill, int skilllv) {
/* 22 */     this.creature = creature;
/* 23 */     this.skilldata = refSkill;
/* 24 */     this.skilllv = skilllv;
/* 25 */     this.strength = (this.skilldata.Attr + this.skilldata.AttrAdd * this.skilllv) / 100.0D;
/*    */     
/* 27 */     this.selector = new Selector(this.skilldata.SelectTarget, this.skilldata.SelectStrategy, this.skilldata.CastTarget, this.skilldata.SelectArea, this.skilldata.SelectParam);
/* 28 */     this.settler = ISettler.get(refSkill.SettleType, this);
/*    */   }
/*    */   
/*    */   public boolean cast() {
/* 32 */     Selector.Result select = this.selector.select(this.creature, this.creature.battle.all);
/* 33 */     if (select.selected == null || select.castOn.size() <= 0) {
/* 34 */       return false;
/*    */     }
/* 36 */     resetCD();
/*    */     
/* 38 */     String bullet = this.skilldata.Bullet;
/* 39 */     if (bullet == null || bullet.isEmpty()) {
/* 40 */       settle(select.selected, select.castOn);
/*    */     } else {
/* 42 */       this.creature.battle.addBullet(new Bullet(this, select.selected, select.castOn));
/*    */     } 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 48 */     return true;
/*    */   }
/*    */   
/*    */   void settle(Creature selected, List<Creature> castOn) {
/* 52 */     if (this.creature.hp < 0.0D) {
/*    */       return;
/*    */     }
/* 55 */     List<Fight.Settle> settles = new ArrayList<>();
/*    */ 
/*    */     
/* 58 */     for (Creature tar : castOn) {
/* 59 */       if (tar.isDead()) {
/*    */         continue;
/*    */       }
/* 62 */       double value = this.settler.settle(this.creature, tar);
/* 63 */       settles.add(new Fight.Settle(tar.id, tar.group.ordinal(), value));
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void resetCD() {
/* 75 */     this.cd = this.skilldata.CD / 1000.0D;
/*    */   }
/*    */   
/*    */   public void update(double dt) {
/* 79 */     if (this.cd > 0.0D)
/* 80 */       this.cd = Math.max(this.cd - dt, 0.0D); 
/*    */   }
/*    */   
/*    */   public boolean isEnable() {
/* 84 */     return (Math.max(this.skilllv, this.creature.level) >= this.skilldata.Require && this.cd <= 0.0D);
/*    */   }
/*    */   
/*    */   public int getPriority() {
/* 88 */     return this.skilldata.Priority;
/*    */   }
/*    */   
/*    */   public int getId() {
/* 92 */     return this.skilldata.id;
/*    */   }
/*    */   
/*    */   public int getLevel() {
/* 96 */     return this.skilllv;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/battle/Skill.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */