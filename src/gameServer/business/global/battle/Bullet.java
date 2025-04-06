/*    */ package business.global.battle;
/*    */ 
/*    */ import java.util.List;
/*    */ 
/*    */ public class Bullet
/*    */ {
/*    */   double time;
/*    */   Skill skill;
/*    */   Creature selected;
/*    */   List<Creature> castOn;
/*    */   
/*    */   public Bullet(Skill skill, Creature selected, List<Creature> castOn) {
/* 13 */     double dx = skill.creature.x - selected.x;
/* 14 */     double dy = skill.creature.y - selected.y;
/* 15 */     this.time = Math.sqrt(dx * dx + dy * dy) / skill.skilldata.BulletSpeed;
/*    */     
/* 17 */     this.skill = skill;
/* 18 */     this.selected = selected;
/* 19 */     this.castOn = castOn;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void update(double dt) {
/* 26 */     this.time -= dt;
/* 27 */     if (this.time <= 0.0D) {
/* 28 */       this.skill.settle(this.selected, this.castOn);
/*    */     }
/*    */   }
/*    */   
/*    */   public boolean alive() {
/* 33 */     return (this.time > 0.0D);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/battle/Bullet.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */