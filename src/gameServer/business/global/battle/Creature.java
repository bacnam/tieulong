/*     */ package business.global.battle;
/*     */ 
/*     */ import com.zhonglian.server.common.enums.Attribute;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Creature
/*     */ {
/*     */   double x;
/*     */   double y;
/*     */   double hp;
/*     */   public double initHp;
/*     */   RoleType RoleType;
/*     */   Group group;
/*     */   Battle battle;
/*     */   int level;
/*     */   int id;
/*     */   boolean isMain;
/*     */   SkillLauncher skillLauncher;
/*     */   Map<Integer, Buff> buffs;
/*  28 */   Map<Attribute, Double> attrs = new HashMap<>();
/*     */   
/*     */   double SPD;
/*     */   
/*     */   double RNG;
/*     */   int model;
/*     */   int wing;
/*     */   
/*     */   public Creature(Battle battle, int charid, int model, int wing, Map<Attribute, Integer> attrs) {
/*  37 */     this.battle = battle;
/*  38 */     this.id = charid;
/*  39 */     this.model = model;
/*  40 */     this.wing = wing;
/*     */     
/*  42 */     for (Map.Entry<Attribute, Integer> attr : attrs.entrySet()) {
/*  43 */       this.attrs.put(attr.getKey(), Double.valueOf(((Integer)attr.getValue()).doubleValue()));
/*     */     }
/*  45 */     this.skillLauncher = new SkillLauncher(this);
/*     */     
/*  47 */     this.buffs = new HashMap<>();
/*  48 */     this.hp = ((Double)this.attrs.get(Attribute.MaxHP)).doubleValue();
/*  49 */     this.initHp = this.hp;
/*     */   }
/*     */   
/*     */   public int getId() {
/*  53 */     return this.id;
/*     */   }
/*     */   
/*     */   public int getLevel() {
/*  57 */     return this.level;
/*     */   }
/*     */   
/*     */   public double getRange() {
/*  61 */     return this.RNG;
/*     */   }
/*     */   
/*     */   public double getSpeed() {
/*  65 */     return this.SPD;
/*     */   }
/*     */   
/*     */   public int getModel() {
/*  69 */     return this.model;
/*     */   }
/*     */   
/*     */   public int getWing() {
/*  73 */     return this.wing;
/*     */   }
/*     */   
/*     */   public double getHp() {
/*  77 */     return this.hp;
/*     */   }
/*     */   
/*     */   public Double attr(Attribute attr) {
/*  81 */     Double base = this.attrs.get(attr);
/*  82 */     if (base == null) {
/*  83 */       base = Double.valueOf(0.0D);
/*     */     }
/*  85 */     double per = 0.0D;
/*  86 */     double fixed = 0.0D;
/*  87 */     for (Buff b : this.buffs.values()) {
/*  88 */       if (!b.isEffective()) {
/*     */         continue;
/*     */       }
/*  91 */       per += b.attrPer(attr);
/*  92 */       fixed += b.attrFixed(attr);
/*     */     } 
/*  94 */     return Double.valueOf(base.doubleValue() * (1.0D + per) + fixed);
/*     */   }
/*     */   
/*     */   public double baseAttr(Attribute attr) {
/*  98 */     Double base = this.attrs.get(attr);
/*  99 */     return (base == null) ? 0.0D : base.doubleValue();
/*     */   }
/*     */   
/*     */   public boolean isDead() {
/* 103 */     return (this.hp < 0.0D);
/*     */   }
/*     */   
/*     */   public void update(double dt) {
/* 107 */     this.skillLauncher.update(dt);
/*     */     
/* 109 */     List<Integer> toremove = new ArrayList<>();
/*     */     
/* 111 */     for (Buff buff : this.buffs.values()) {
/* 112 */       buff.update(dt);
/* 113 */       if (!buff.isInCd() && !buff.isEffective()) {
/* 114 */         toremove.add(Integer.valueOf(buff.buffid));
/*     */       }
/*     */     } 
/* 117 */     for (Integer b : toremove) {
/* 118 */       this.buffs.remove(b);
/*     */     }
/*     */     
/* 121 */     this.hp = Math.min(this.hp, attr(Attribute.MaxHP).doubleValue());
/*     */   }
/*     */   
/*     */   public boolean isFriend(Creature ch) {
/* 125 */     if (this.group == ch.group)
/* 126 */       return true; 
/* 127 */     return !((this.group != Group.Player || ch.group != Group.Allies) && (
/* 128 */       this.group != Group.Allies || ch.group != Group.Player));
/*     */   }
/*     */   
/*     */   public void castSkill() {
/* 132 */     if (isDead()) {
/*     */       return;
/*     */     }
/* 135 */     Skill skill = this.skillLauncher.nextSkill();
/* 136 */     if (skill != null && skill.cast()) {
/* 137 */       this.skillLauncher.resetCD();
/*     */     }
/*     */   }
/*     */   
/*     */   public void removeBuff(List<Integer> buffidList) {
/* 142 */     for (Iterator<Integer> iterator = buffidList.iterator(); iterator.hasNext(); ) { int buffid = ((Integer)iterator.next()).intValue();
/* 143 */       this.buffs.remove(Integer.valueOf(buffid)); }
/*     */   
/*     */   }
/*     */   
/*     */   public void addBuff(Buff buff) {
/* 148 */     Buff pre = this.buffs.get(Integer.valueOf(buff.buffid));
/* 149 */     if (pre != null && 
/* 150 */       pre.isInCd()) {
/*     */       return;
/*     */     }
/* 153 */     this.buffs.put(Integer.valueOf(buff.buffid), buff);
/*     */   }
/*     */   
/*     */   public void stepTo(Creature tar, double dt) {
/* 157 */     if (this.skillLauncher.cd > 0.0D) {
/*     */       return;
/*     */     }
/* 160 */     if (inRange(tar)) {
/*     */       return;
/*     */     }
/* 163 */     double d = this.SPD * dt;
/*     */     
/* 165 */     double dy = this.y - tar.y;
/* 166 */     double dx = this.x - tar.x;
/* 167 */     if (dy == 0.0D) {
/* 168 */       this.x = (dx > 0.0D) ? (this.x - Math.sqrt(d)) : (this.x + Math.sqrt(d));
/*     */       return;
/*     */     } 
/* 171 */     if (dx == 0.0D) {
/* 172 */       this.y = (dy > 0.0D) ? (this.y - Math.sqrt(d)) : (this.y + Math.sqrt(d));
/*     */       return;
/*     */     } 
/* 175 */     double tx = Math.sqrt(d / (1.0D + dy / dx * dy / dx));
/* 176 */     double ty = Math.abs(tx * dy / dx);
/* 177 */     this.x = (dx > 0.0D) ? (this.x - tx) : (this.x + tx);
/* 178 */     this.y = (dy > 0.0D) ? (this.y - ty) : (this.y + ty);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hit(Creature tar) {
/* 184 */     double factor = RefDataMgr.getFactor("Battle_HitFactor", 40);
/* 185 */     double hitchance = factor * attr(Attribute.Hit).doubleValue() / (factor * attr(Attribute.Hit).doubleValue() + tar.attr(Attribute.Dodge).doubleValue());
/* 186 */     hitchance += (attr(Attribute.HitPro).doubleValue() - tar.attr(Attribute.DodgePro).doubleValue()) / 100.0D;
/* 187 */     return (this.battle.random() < hitchance);
/*     */   }
/*     */   
/*     */   public boolean critical(Creature tar) {
/* 191 */     double factor = RefDataMgr.getFactor("Battle_CriticalFactor", 40);
/* 192 */     double critchance = attr(Attribute.Critical).doubleValue() / (attr(Attribute.Critical).doubleValue() + factor * tar.attr(Attribute.Tenacity).doubleValue());
/* 193 */     critchance += (attr(Attribute.CriticalPro).doubleValue() - tar.attr(Attribute.TenacityPro).doubleValue()) / 100.0D;
/* 194 */     return (this.battle.random() < critchance);
/*     */   }
/*     */   
/*     */   public void damage(double damage, Creature me) {
/* 198 */     this.hp -= damage;
/* 199 */     if (isDead()) {
/* 200 */       die();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void die() {
/* 205 */     this.battle.onDie(this);
/*     */   }
/*     */   
/*     */   public void heal(double heal) {
/* 209 */     this.hp = Math.min(attr(Attribute.MaxHP).doubleValue(), this.hp + heal);
/*     */   }
/*     */   
/*     */   public boolean inRange(Creature tar) {
/* 213 */     double dx = this.x - tar.x;
/* 214 */     double dy = this.y - tar.y;
/* 215 */     double disance = dx * dx + dy * dy;
/* 216 */     return (disance < this.RNG * this.RNG);
/*     */   }
/*     */   
/*     */   public List<Skill> getSkills() {
/* 220 */     return this.skillLauncher.$skillList;
/*     */   }
/*     */   
/*     */   public int getType() {
/* 224 */     return this.RoleType.ordinal();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/battle/Creature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */