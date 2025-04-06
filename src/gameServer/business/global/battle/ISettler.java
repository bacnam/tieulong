/*    */ package business.global.battle;
/*    */ 
/*    */ import com.zhonglian.server.common.enums.Attribute;
/*    */ 
/*    */ abstract class ISettler
/*    */ {
/*    */   Skill skill;
/*    */   
/*    */   ISettler(Skill skill) {
/* 10 */     this.skill = skill;
/*    */   }
/*    */   public abstract double settle(Creature paramCreature1, Creature paramCreature2);
/*    */   
/*    */   static ISettler get(String name, Skill skill) {
/*    */     String str;
/* 16 */     switch ((str = name).hashCode()) { case 2081907: if (!str.equals("Buff")) {
/*    */           break;
/*    */         }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 27 */         return new ISettler(skill) { public double settle(Creature me, Creature tar) { double heal = me.attr(Attribute.ATK).doubleValue() * this.skill.strength; heal = Math.floor(heal); tar.heal(heal); return heal; } }
/*    */           ;
/*    */       case 2245128: if (!str.equals("Heal"))
/* 30 */           break;  return new ISettler(skill) { public double settle(Creature me, Creature tar) { if (this.skill.skilldata.ClearBuffList.size() > 0) {
/* 31 */                 tar.removeBuff(this.skill.skilldata.ClearBuffList);
/*    */               }
/*    */               
/* 34 */               if (this.skill.skilldata.Buff != 0) {
/* 35 */                 tar.addBuff(new Buff(tar, this.skill.skilldata.Buff, this.skill.skilllv));
/*    */               }
/* 37 */               return 0.0D; } };
/*    */       case 2039707535:
/*    */         if (!str.equals("Damage"));
/*    */         break; }
/*    */     
/* 42 */     return new ISettler(skill) {
/*    */         public double settle(Creature me, Creature tar) {
/* 44 */           double defvalue = 0.0D;
/* 45 */           if ("DEF".equals(this.skill.skilldata.DefAttr)) {
/* 46 */             defvalue = tar.attr(Attribute.DEF).doubleValue();
/* 47 */           } else if ("RGS".equals(this.skill.skilldata.DefAttr)) {
/* 48 */             defvalue = tar.attr(Attribute.RGS).doubleValue();
/*    */           } 
/* 50 */           double dmg = (me.attr(Attribute.ATK).doubleValue() - defvalue) * this.skill.strength;
/* 51 */           dmg = (dmg <= 0.0D) ? (me.battle.random() * SkillLauncher.MinATK) : dmg;
/* 52 */           boolean isHit = me.hit(tar);
/* 53 */           if (isHit) {
/* 54 */             if (me.critical(tar)) {
/* 55 */               dmg *= SkillLauncher.CriticalMultiple;
/*    */             }
/*    */           } else {
/* 58 */             dmg = 0.0D;
/*    */           } 
/* 60 */           dmg *= 1.0D + (me.attr(Attribute.Damage).doubleValue() - tar.attr(Attribute.Reduction).doubleValue()) / 100.0D;
/* 61 */           dmg = Math.floor(dmg);
/* 62 */           tar.damage(dmg, me);
/* 63 */           return dmg;
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/battle/ISettler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */