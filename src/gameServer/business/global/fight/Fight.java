/*     */ package business.global.fight;
/*     */ 
/*     */ import BaseCommon.CommLog;
/*     */ import com.zhonglian.server.common.enums.Attribute;
/*     */ import com.zhonglian.server.common.enums.FightResult;
/*     */ import com.zhonglian.server.common.utils.CommTime;
/*     */ import com.zhonglian.server.websocket.def.ErrorCode;
/*     */ import com.zhonglian.server.websocket.exception.WSException;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefBuff;
/*     */ import core.config.refdata.ref.RefSkill;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Fight<E>
/*     */ {
/*  22 */   private static int nextFightId = 1;
/*     */   
/*  24 */   int beginTime = CommTime.nowSecond();
/*  25 */   int fightId = nextFightId++;
/*  26 */   long seed = this.fightId;
/*     */   
/*     */   double random() {
/*  29 */     this.seed = (this.seed * 9301L + 49297L) % 233280L;
/*  30 */     return this.seed / 233280.0D;
/*     */   }
/*     */   
/*     */   public int getId() {
/*  34 */     return this.fightId;
/*     */   }
/*     */   public E settle(FightResult result) {
/*     */     E rtn;
/*  38 */     beforeSettle(result);
/*     */     
/*  40 */     if (result == FightResult.Win) {
/*  41 */       rtn = onWin();
/*     */     } else {
/*  43 */       rtn = onLost();
/*     */     } 
/*  45 */     afterSettle(result);
/*  46 */     return rtn;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void beforeSettle(FightResult result) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void afterSettle(FightResult result) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void onCheckError() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void check(List<core.network.proto.Fight.CheckData> checks) throws WSException {
/*     */     try {
/*  68 */       if (checks == null) {
/*  69 */         throw new WSException(ErrorCode.Fight_CheckFailed, "数据不足");
/*     */       }
/*     */       
/*  72 */       checks.sort((left, right) -> left.time - right.time);
/*     */       
/*  74 */       Map<Integer, Fighter> attacker = getAttackers();
/*  75 */       Map<Integer, Fighter> deffender = getDeffenders();
/*  76 */       int time = ((core.network.proto.Fight.CheckData)checks.get(0)).time;
/*  77 */       initbuff(attacker, time);
/*  78 */       initbuff(deffender, time);
/*     */ 
/*     */       
/*  81 */       for (int i = 0; i < checks.size(); i++) {
/*  82 */         core.network.proto.Fight.CheckData cdata = checks.get(i);
/*  83 */         for (Fighter f : attacker.values()) {
/*  84 */           f.checkBuff(cdata.time);
/*     */         }
/*  86 */         for (Fighter f : deffender.values()) {
/*  87 */           f.checkBuff(cdata.time);
/*     */         }
/*     */         
/*  90 */         Fighter fighter = null;
/*  91 */         if (cdata.agroup == 1) {
/*  92 */           fighter = attacker.get(Integer.valueOf(cdata.attacker));
/*  93 */         } else if (cdata.agroup == 2) {
/*  94 */           fighter = deffender.get(Integer.valueOf(cdata.attacker));
/*     */         } 
/*  96 */         if (fighter == null) {
/*  97 */           CommLog.error("fighter:{},group:{}", Integer.valueOf(cdata.attacker), Integer.valueOf(cdata.agroup));
/*  98 */           throw new WSException(ErrorCode.Fight_CheckFailed, "找不到相关的战斗角色0");
/*     */         } 
/* 100 */         if (fighter.hp < -100.0D) {
/* 101 */           CommLog.error("fighter:{}, hp:{}", Integer.valueOf(fighter.id), Double.valueOf(fighter.hp));
/* 102 */           throw new WSException(ErrorCode.Fight_CheckFailed, "非法角色0");
/*     */         } 
/*     */ 
/*     */         
/* 106 */         RefSkill refSkill = fighter.skills.get(Integer.valueOf(cdata.skill));
/* 107 */         if (refSkill == null) {
/* 108 */           CommLog.info("caster:{}, group:{}, skill:{}, time:{}", new Object[] { Integer.valueOf(fighter.id), Integer.valueOf(cdata.agroup), Integer.valueOf(cdata.skill), Integer.valueOf(cdata.time) });
/* 109 */           throw new WSException(ErrorCode.Fight_CheckFailed, "技能不存在0");
/*     */         } 
/* 111 */         Integer lastCastTime = fighter.skillsCD.get(Integer.valueOf(cdata.skill));
/* 112 */         if (lastCastTime != null && 
/* 113 */           cdata.time + 300 < lastCastTime.intValue() + refSkill.CD) {
/* 114 */           CommLog.error("time:{}, fighter:{},skill:{},s_time:{},c_time:{},cd:{}", new Object[] {
/* 115 */                 Integer.valueOf(cdata.time - time), Integer.valueOf(fighter.id), Integer.valueOf(cdata.skill), Integer.valueOf(cdata.time + 3), Integer.valueOf(lastCastTime.intValue() + refSkill.CD), Integer.valueOf(refSkill.CD) });
/* 116 */           throw new WSException(ErrorCode.Fight_CheckFailed, "非法技能间隔1");
/*     */         } 
/*     */         
/* 119 */         fighter.skillsCD.put(Integer.valueOf(cdata.skill), Integer.valueOf(cdata.time));
/* 120 */         fighter.action(cdata.time);
/*     */         
/* 122 */         Set<Fighter> settled = new HashSet<>(); byte b; int j; core.network.proto.Fight.Settle[] arrayOfSettle;
/* 123 */         for (j = (arrayOfSettle = cdata.settles).length, b = 0; b < j; ) { core.network.proto.Fight.Settle settle = arrayOfSettle[b];
/* 124 */           Fighter victim = null;
/* 125 */           if (settle.vgroup == 1) {
/* 126 */             victim = attacker.get(Integer.valueOf(settle.victim));
/* 127 */           } else if (settle.vgroup == 2) {
/* 128 */             victim = deffender.get(Integer.valueOf(settle.victim));
/*     */           } 
/* 130 */           if (victim == null) {
/* 131 */             throw new WSException(ErrorCode.Fight_CheckFailed, "找不到相关的战斗角色1");
/*     */           }
/* 133 */           if (settled.contains(victim)) {
/* 134 */             throw new WSException(ErrorCode.Fight_CheckFailed, "非法角色1");
/*     */           }
/* 136 */           if (victim.hp < -100.0D) {
/* 137 */             throw new WSException(ErrorCode.Fight_CheckFailed, "非法角色2");
/*     */           }
/* 139 */           if ("Enemy".equals(refSkill.CastTarget) && cdata.agroup == settle.vgroup)
/* 140 */             throw new WSException(ErrorCode.Fight_CheckFailed, "非法技能对象0"); 
/* 141 */           if ("SelfAndAlias".equals(refSkill.CastTarget) && cdata.agroup != settle.vgroup) {
/* 142 */             throw new WSException(ErrorCode.Fight_CheckFailed, "非法技能对象0");
/*     */           }
/* 144 */           settled.add(victim);
/*     */           
/* 146 */           if ("Damage".equals(refSkill.SettleType))
/* 147 */           { double defvalue = 0.0D;
/* 148 */             if ("DEF".equals(refSkill.DefAttr)) {
/* 149 */               defvalue = victim.attr(Attribute.DEF);
/* 150 */             } else if ("RGS".equals(refSkill.DefAttr)) {
/* 151 */               defvalue = victim.attr(Attribute.RGS);
/*     */             } 
/* 153 */             double damage = fighter.attr(Attribute.ATK) - defvalue;
/* 154 */             double skillstenth = (refSkill.Attr + refSkill.AttrAdd * ((Integer)fighter.skillsLv.get(Integer.valueOf(cdata.skill))).intValue()) / 100.0D;
/* 155 */             damage *= skillstenth;
/* 156 */             if (damage <= 0.0D) {
/* 157 */               damage = random() * 10.0D;
/*     */             }
/*     */             
/* 160 */             double hitfactor = RefDataMgr.getFactor("Battle_HitFactor", 40);
/* 161 */             double hitchance = hitfactor * fighter.attr(Attribute.Hit) / (hitfactor * fighter.attr(Attribute.Hit) + victim.attr(Attribute.Dodge));
/* 162 */             hitchance += (fighter.attr(Attribute.HitPro) - victim.attr(Attribute.DodgePro)) / 100.0D;
/* 163 */             boolean hit = (random() < hitchance);
/*     */             
/* 165 */             boolean critical = false;
/*     */             
/* 167 */             if (hit) {
/*     */               
/* 169 */               double crtfactor = RefDataMgr.getFactor("Battle_CriticalFactor", 40);
/* 170 */               double critchance = fighter.attr(Attribute.Critical) / (
/* 171 */                 fighter.attr(Attribute.Critical) + crtfactor * victim.attr(Attribute.Tenacity));
/* 172 */               critchance += (fighter.attr(Attribute.CriticalPro) - victim.attr(Attribute.TenacityPro)) / 100.0D;
/* 173 */               critical = (random() < critchance);
/* 174 */               if (critical) {
/* 175 */                 damage *= RefDataMgr.getFactor("CriticalMultiple", 2000) / 1000.0D;
/*     */               }
/*     */             } else {
/* 178 */               damage = 0.0D;
/*     */             } 
/* 180 */             damage *= 1.0D + (fighter.attr(Attribute.Damage) - victim.attr(Attribute.Reduction)) / 100.0D;
/* 181 */             damage = Math.floor(damage);
/*     */ 
/*     */             
/* 184 */             if (Math.abs(damage - settle.value) > 1000.0D) {
/* 185 */               CommLog.info("time:{}, atk:{}-{}, victim:{}, s_hit:{}, s_crit:{}, s_damage:{}, c_damage:{}", new Object[] {
/* 186 */                     Integer.valueOf(cdata.time - time), Integer.valueOf(cdata.agroup), Integer.valueOf(fighter.id), Integer.valueOf(victim.id), Boolean.valueOf(hit), Boolean.valueOf(critical), Double.valueOf(damage), Double.valueOf(settle.value) });
/* 187 */               throw new WSException(ErrorCode.Fight_CheckFailed, "伤害错误1");
/*     */             } 
/* 189 */             victim.hp -= settle.value; }
/* 190 */           else if ("Heal".equals(refSkill.SettleType))
/*     */           
/* 192 */           { double skillstenth = (refSkill.Attr + refSkill.AttrAdd * ((Integer)fighter.skillsLv.get(Integer.valueOf(cdata.skill))).intValue()) / 100.0D;
/* 193 */             double heal = fighter.attr(Attribute.ATK) * skillstenth;
/* 194 */             if (Math.abs(heal - settle.value) > 1000.0D) {
/* 195 */               throw new WSException(ErrorCode.Fight_CheckFailed, "伤害错误2");
/*     */             }
/* 197 */             victim.hp += settle.value;
/* 198 */             if (victim.hp > victim.attr(Attribute.MaxHP)) {
/* 199 */               victim.hp = victim.attr(Attribute.MaxHP);
/*     */             } }
/* 201 */           else if ("Buff".equals(refSkill.SettleType))
/*     */           
/* 203 */           { if (refSkill.ClearBuffList.size() != 0) {
/* 204 */               victim.removeBuffs(refSkill.ClearBuffList);
/*     */             }
/*     */             
/* 207 */             if (refSkill.Buff != 0) {
/*     */ 
/*     */               
/* 210 */               RefBuff refBuff = (RefBuff)RefDataMgr.get(RefBuff.class, Integer.valueOf(refSkill.Buff));
/* 211 */               if (refBuff == null) {
/* 212 */                 CommLog.info("buff不存在，skill:{}, tar:{}", Integer.valueOf(refSkill.id), Integer.valueOf(victim.id));
/* 213 */                 throw new WSException(ErrorCode.Fight_CheckFailed, "Buff结算错误0");
/*     */               } 
/* 215 */               Buff pre = victim.buffs.get(Integer.valueOf(refBuff.id));
/* 216 */               if (pre == null || cdata.time >= pre.cd)
/*     */               
/*     */               { 
/*     */                 
/* 220 */                 Buff buff = new Buff(refBuff, ((Integer)fighter.skillsLv.get(Integer.valueOf(cdata.skill))).intValue());
/* 221 */                 buff.time = (cdata.time + refBuff.Time * 1000);
/* 222 */                 buff.cd = (cdata.time + refBuff.CD * 1000);
/* 223 */                 victim.buffs.put(Integer.valueOf(buff.id), buff); } 
/*     */             }  }
/* 225 */           else { throw new WSException(ErrorCode.Fight_CheckFailed, "结算错误2"); }
/*     */ 
/*     */           
/*     */           b++; }
/*     */       
/*     */       } 
/*     */       
/* 232 */       int maxatk = 0;
/* 233 */       int atkSurvivor = 0;
/* 234 */       for (Fighter f : attacker.values()) {
/* 235 */         maxatk = Math.max(f.fighttimes, maxatk);
/* 236 */         if (f.isMain() && f.hp > 0.0D)
/* 237 */           atkSurvivor++; 
/*     */       } 
/* 239 */       int maxdef = 0;
/* 240 */       int defSurvivor = 0;
/* 241 */       for (Fighter f : deffender.values()) {
/* 242 */         maxdef = Math.max(f.fighttimes, maxdef);
/* 243 */         if (f.isMain() && f.hp > 0.0D)
/* 244 */           defSurvivor++; 
/*     */       } 
/* 246 */       if (Math.abs(maxatk - maxdef) > 5) {
/* 247 */         throw new WSException(ErrorCode.Fight_CheckFailed, "攻击频率");
/*     */       }
/* 249 */       if (atkSurvivor < 0) {
/* 250 */         throw new WSException(ErrorCode.Fight_CheckFailed, "结算错误atk");
/*     */       }
/* 252 */       if (defSurvivor > 0) {
/* 253 */         throw new WSException(ErrorCode.Fight_CheckFailed, "结算错误def");
/*     */       }
/* 255 */     } catch (Exception e) {
/* 256 */       onCheckError();
/* 257 */       FightManager.getInstance().removeFight(this);
/* 258 */       throw e;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void initbuff(Map<Integer, Fighter> attacker, int time) {
/* 263 */     for (Fighter f : attacker.values())
/* 264 */       f.initBuff(time); 
/*     */   }
/*     */   
/*     */   protected abstract E onLost();
/*     */   
/*     */   protected abstract E onWin();
/*     */   
/*     */   protected abstract Map<Integer, Fighter> getDeffenders();
/*     */   
/*     */   protected abstract Map<Integer, Fighter> getAttackers();
/*     */   
/*     */   public abstract int fightTime();
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/fight/Fight.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */