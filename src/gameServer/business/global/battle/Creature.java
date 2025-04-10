package business.global.battle;

import com.zhonglian.server.common.enums.Attribute;
import core.config.refdata.RefDataMgr;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Creature
{
double x;
double y;
double hp;
public double initHp;
RoleType RoleType;
Group group;
Battle battle;
int level;
int id;
boolean isMain;
SkillLauncher skillLauncher;
Map<Integer, Buff> buffs;
Map<Attribute, Double> attrs = new HashMap<>();

double SPD;

double RNG;
int model;
int wing;

public Creature(Battle battle, int charid, int model, int wing, Map<Attribute, Integer> attrs) {
this.battle = battle;
this.id = charid;
this.model = model;
this.wing = wing;

for (Map.Entry<Attribute, Integer> attr : attrs.entrySet()) {
this.attrs.put(attr.getKey(), Double.valueOf(((Integer)attr.getValue()).doubleValue()));
}
this.skillLauncher = new SkillLauncher(this);

this.buffs = new HashMap<>();
this.hp = ((Double)this.attrs.get(Attribute.MaxHP)).doubleValue();
this.initHp = this.hp;
}

public int getId() {
return this.id;
}

public int getLevel() {
return this.level;
}

public double getRange() {
return this.RNG;
}

public double getSpeed() {
return this.SPD;
}

public int getModel() {
return this.model;
}

public int getWing() {
return this.wing;
}

public double getHp() {
return this.hp;
}

public Double attr(Attribute attr) {
Double base = this.attrs.get(attr);
if (base == null) {
base = Double.valueOf(0.0D);
}
double per = 0.0D;
double fixed = 0.0D;
for (Buff b : this.buffs.values()) {
if (!b.isEffective()) {
continue;
}
per += b.attrPer(attr);
fixed += b.attrFixed(attr);
} 
return Double.valueOf(base.doubleValue() * (1.0D + per) + fixed);
}

public double baseAttr(Attribute attr) {
Double base = this.attrs.get(attr);
return (base == null) ? 0.0D : base.doubleValue();
}

public boolean isDead() {
return (this.hp < 0.0D);
}

public void update(double dt) {
this.skillLauncher.update(dt);

List<Integer> toremove = new ArrayList<>();

for (Buff buff : this.buffs.values()) {
buff.update(dt);
if (!buff.isInCd() && !buff.isEffective()) {
toremove.add(Integer.valueOf(buff.buffid));
}
} 
for (Integer b : toremove) {
this.buffs.remove(b);
}

this.hp = Math.min(this.hp, attr(Attribute.MaxHP).doubleValue());
}

public boolean isFriend(Creature ch) {
if (this.group == ch.group)
return true; 
return !((this.group != Group.Player || ch.group != Group.Allies) && (
this.group != Group.Allies || ch.group != Group.Player));
}

public void castSkill() {
if (isDead()) {
return;
}
Skill skill = this.skillLauncher.nextSkill();
if (skill != null && skill.cast()) {
this.skillLauncher.resetCD();
}
}

public void removeBuff(List<Integer> buffidList) {
for (Iterator<Integer> iterator = buffidList.iterator(); iterator.hasNext(); ) { int buffid = ((Integer)iterator.next()).intValue();
this.buffs.remove(Integer.valueOf(buffid)); }

}

public void addBuff(Buff buff) {
Buff pre = this.buffs.get(Integer.valueOf(buff.buffid));
if (pre != null && 
pre.isInCd()) {
return;
}
this.buffs.put(Integer.valueOf(buff.buffid), buff);
}

public void stepTo(Creature tar, double dt) {
if (this.skillLauncher.cd > 0.0D) {
return;
}
if (inRange(tar)) {
return;
}
double d = this.SPD * dt;

double dy = this.y - tar.y;
double dx = this.x - tar.x;
if (dy == 0.0D) {
this.x = (dx > 0.0D) ? (this.x - Math.sqrt(d)) : (this.x + Math.sqrt(d));
return;
} 
if (dx == 0.0D) {
this.y = (dy > 0.0D) ? (this.y - Math.sqrt(d)) : (this.y + Math.sqrt(d));
return;
} 
double tx = Math.sqrt(d / (1.0D + dy / dx * dy / dx));
double ty = Math.abs(tx * dy / dx);
this.x = (dx > 0.0D) ? (this.x - tx) : (this.x + tx);
this.y = (dy > 0.0D) ? (this.y - ty) : (this.y + ty);
}

public boolean hit(Creature tar) {
double factor = RefDataMgr.getFactor("Battle_HitFactor", 40);
double hitchance = factor * attr(Attribute.Hit).doubleValue() / (factor * attr(Attribute.Hit).doubleValue() + tar.attr(Attribute.Dodge).doubleValue());
hitchance += (attr(Attribute.HitPro).doubleValue() - tar.attr(Attribute.DodgePro).doubleValue()) / 100.0D;
return (this.battle.random() < hitchance);
}

public boolean critical(Creature tar) {
double factor = RefDataMgr.getFactor("Battle_CriticalFactor", 40);
double critchance = attr(Attribute.Critical).doubleValue() / (attr(Attribute.Critical).doubleValue() + factor * tar.attr(Attribute.Tenacity).doubleValue());
critchance += (attr(Attribute.CriticalPro).doubleValue() - tar.attr(Attribute.TenacityPro).doubleValue()) / 100.0D;
return (this.battle.random() < critchance);
}

public void damage(double damage, Creature me) {
this.hp -= damage;
if (isDead()) {
die();
}
}

protected void die() {
this.battle.onDie(this);
}

public void heal(double heal) {
this.hp = Math.min(attr(Attribute.MaxHP).doubleValue(), this.hp + heal);
}

public boolean inRange(Creature tar) {
double dx = this.x - tar.x;
double dy = this.y - tar.y;
double disance = dx * dx + dy * dy;
return (disance < this.RNG * this.RNG);
}

public List<Skill> getSkills() {
return this.skillLauncher.$skillList;
}

public int getType() {
return this.RoleType.ordinal();
}
}

