package business.global.battle;

import com.zhonglian.server.common.enums.Attribute;

abstract class ISettler
{
Skill skill;

ISettler(Skill skill) {
this.skill = skill;
}
public abstract double settle(Creature paramCreature1, Creature paramCreature2);

static ISettler get(String name, Skill skill) {
String str;
switch ((str = name).hashCode()) { case 2081907: if (!str.equals("Buff")) {
break;
}

return new ISettler(skill) { public double settle(Creature me, Creature tar) { double heal = me.attr(Attribute.ATK).doubleValue() * this.skill.strength; heal = Math.floor(heal); tar.heal(heal); return heal; } }
;
case 2245128: if (!str.equals("Heal"))
break;  return new ISettler(skill) { public double settle(Creature me, Creature tar) { if (this.skill.skilldata.ClearBuffList.size() > 0) {
tar.removeBuff(this.skill.skilldata.ClearBuffList);
}

if (this.skill.skilldata.Buff != 0) {
tar.addBuff(new Buff(tar, this.skill.skilldata.Buff, this.skill.skilllv));
}
return 0.0D; } };
case 2039707535:
if (!str.equals("Damage"));
break; }

return new ISettler(skill) {
public double settle(Creature me, Creature tar) {
double defvalue = 0.0D;
if ("DEF".equals(this.skill.skilldata.DefAttr)) {
defvalue = tar.attr(Attribute.DEF).doubleValue();
} else if ("RGS".equals(this.skill.skilldata.DefAttr)) {
defvalue = tar.attr(Attribute.RGS).doubleValue();
} 
double dmg = (me.attr(Attribute.ATK).doubleValue() - defvalue) * this.skill.strength;
dmg = (dmg <= 0.0D) ? (me.battle.random() * SkillLauncher.MinATK) : dmg;
boolean isHit = me.hit(tar);
if (isHit) {
if (me.critical(tar)) {
dmg *= SkillLauncher.CriticalMultiple;
}
} else {
dmg = 0.0D;
} 
dmg *= 1.0D + (me.attr(Attribute.Damage).doubleValue() - tar.attr(Attribute.Reduction).doubleValue()) / 100.0D;
dmg = Math.floor(dmg);
tar.damage(dmg, me);
return dmg;
}
};
}
}

