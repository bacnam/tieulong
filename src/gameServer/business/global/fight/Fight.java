package business.global.fight;

import BaseCommon.CommLog;
import com.zhonglian.server.common.enums.Attribute;
import com.zhonglian.server.common.enums.FightResult;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefBuff;
import core.config.refdata.ref.RefSkill;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class Fight<E>
{
private static int nextFightId = 1;

int beginTime = CommTime.nowSecond();
int fightId = nextFightId++;
long seed = this.fightId;

double random() {
this.seed = (this.seed * 9301L + 49297L) % 233280L;
return this.seed / 233280.0D;
}

public int getId() {
return this.fightId;
}
public E settle(FightResult result) {
E rtn;
beforeSettle(result);

if (result == FightResult.Win) {
rtn = onWin();
} else {
rtn = onLost();
} 
afterSettle(result);
return rtn;
}

protected void beforeSettle(FightResult result) {}

protected void afterSettle(FightResult result) {}

protected void onCheckError() {}

public void check(List<core.network.proto.Fight.CheckData> checks) throws WSException {
try {
if (checks == null) {
throw new WSException(ErrorCode.Fight_CheckFailed, "数据不足");
}

checks.sort((left, right) -> left.time - right.time);

Map<Integer, Fighter> attacker = getAttackers();
Map<Integer, Fighter> deffender = getDeffenders();
int time = ((core.network.proto.Fight.CheckData)checks.get(0)).time;
initbuff(attacker, time);
initbuff(deffender, time);

for (int i = 0; i < checks.size(); i++) {
core.network.proto.Fight.CheckData cdata = checks.get(i);
for (Fighter f : attacker.values()) {
f.checkBuff(cdata.time);
}
for (Fighter f : deffender.values()) {
f.checkBuff(cdata.time);
}

Fighter fighter = null;
if (cdata.agroup == 1) {
fighter = attacker.get(Integer.valueOf(cdata.attacker));
} else if (cdata.agroup == 2) {
fighter = deffender.get(Integer.valueOf(cdata.attacker));
} 
if (fighter == null) {
CommLog.error("fighter:{},group:{}", Integer.valueOf(cdata.attacker), Integer.valueOf(cdata.agroup));
throw new WSException(ErrorCode.Fight_CheckFailed, "找不到相关的战斗角色0");
} 
if (fighter.hp < -100.0D) {
CommLog.error("fighter:{}, hp:{}", Integer.valueOf(fighter.id), Double.valueOf(fighter.hp));
throw new WSException(ErrorCode.Fight_CheckFailed, "非法角色0");
} 

RefSkill refSkill = fighter.skills.get(Integer.valueOf(cdata.skill));
if (refSkill == null) {
CommLog.info("caster:{}, group:{}, skill:{}, time:{}", new Object[] { Integer.valueOf(fighter.id), Integer.valueOf(cdata.agroup), Integer.valueOf(cdata.skill), Integer.valueOf(cdata.time) });
throw new WSException(ErrorCode.Fight_CheckFailed, "技能不存在0");
} 
Integer lastCastTime = fighter.skillsCD.get(Integer.valueOf(cdata.skill));
if (lastCastTime != null && 
cdata.time + 300 < lastCastTime.intValue() + refSkill.CD) {
CommLog.error("time:{}, fighter:{},skill:{},s_time:{},c_time:{},cd:{}", new Object[] {
Integer.valueOf(cdata.time - time), Integer.valueOf(fighter.id), Integer.valueOf(cdata.skill), Integer.valueOf(cdata.time + 3), Integer.valueOf(lastCastTime.intValue() + refSkill.CD), Integer.valueOf(refSkill.CD) });
throw new WSException(ErrorCode.Fight_CheckFailed, "非法技能间隔1");
} 

fighter.skillsCD.put(Integer.valueOf(cdata.skill), Integer.valueOf(cdata.time));
fighter.action(cdata.time);

Set<Fighter> settled = new HashSet<>(); byte b; int j; core.network.proto.Fight.Settle[] arrayOfSettle;
for (j = (arrayOfSettle = cdata.settles).length, b = 0; b < j; ) { core.network.proto.Fight.Settle settle = arrayOfSettle[b];
Fighter victim = null;
if (settle.vgroup == 1) {
victim = attacker.get(Integer.valueOf(settle.victim));
} else if (settle.vgroup == 2) {
victim = deffender.get(Integer.valueOf(settle.victim));
} 
if (victim == null) {
throw new WSException(ErrorCode.Fight_CheckFailed, "找不到相关的战斗角色1");
}
if (settled.contains(victim)) {
throw new WSException(ErrorCode.Fight_CheckFailed, "非法角色1");
}
if (victim.hp < -100.0D) {
throw new WSException(ErrorCode.Fight_CheckFailed, "非法角色2");
}
if ("Enemy".equals(refSkill.CastTarget) && cdata.agroup == settle.vgroup)
throw new WSException(ErrorCode.Fight_CheckFailed, "非法技能对象0"); 
if ("SelfAndAlias".equals(refSkill.CastTarget) && cdata.agroup != settle.vgroup) {
throw new WSException(ErrorCode.Fight_CheckFailed, "非法技能对象0");
}
settled.add(victim);

if ("Damage".equals(refSkill.SettleType))
{ double defvalue = 0.0D;
if ("DEF".equals(refSkill.DefAttr)) {
defvalue = victim.attr(Attribute.DEF);
} else if ("RGS".equals(refSkill.DefAttr)) {
defvalue = victim.attr(Attribute.RGS);
} 
double damage = fighter.attr(Attribute.ATK) - defvalue;
double skillstenth = (refSkill.Attr + refSkill.AttrAdd * ((Integer)fighter.skillsLv.get(Integer.valueOf(cdata.skill))).intValue()) / 100.0D;
damage *= skillstenth;
if (damage <= 0.0D) {
damage = random() * 10.0D;
}

double hitfactor = RefDataMgr.getFactor("Battle_HitFactor", 40);
double hitchance = hitfactor * fighter.attr(Attribute.Hit) / (hitfactor * fighter.attr(Attribute.Hit) + victim.attr(Attribute.Dodge));
hitchance += (fighter.attr(Attribute.HitPro) - victim.attr(Attribute.DodgePro)) / 100.0D;
boolean hit = (random() < hitchance);

boolean critical = false;

if (hit) {

double crtfactor = RefDataMgr.getFactor("Battle_CriticalFactor", 40);
double critchance = fighter.attr(Attribute.Critical) / (
fighter.attr(Attribute.Critical) + crtfactor * victim.attr(Attribute.Tenacity));
critchance += (fighter.attr(Attribute.CriticalPro) - victim.attr(Attribute.TenacityPro)) / 100.0D;
critical = (random() < critchance);
if (critical) {
damage *= RefDataMgr.getFactor("CriticalMultiple", 2000) / 1000.0D;
}
} else {
damage = 0.0D;
} 
damage *= 1.0D + (fighter.attr(Attribute.Damage) - victim.attr(Attribute.Reduction)) / 100.0D;
damage = Math.floor(damage);

if (Math.abs(damage - settle.value) > 1000.0D) {
CommLog.info("time:{}, atk:{}-{}, victim:{}, s_hit:{}, s_crit:{}, s_damage:{}, c_damage:{}", new Object[] {
Integer.valueOf(cdata.time - time), Integer.valueOf(cdata.agroup), Integer.valueOf(fighter.id), Integer.valueOf(victim.id), Boolean.valueOf(hit), Boolean.valueOf(critical), Double.valueOf(damage), Double.valueOf(settle.value) });
throw new WSException(ErrorCode.Fight_CheckFailed, "伤害错误1");
} 
victim.hp -= settle.value; }
else if ("Heal".equals(refSkill.SettleType))

{ double skillstenth = (refSkill.Attr + refSkill.AttrAdd * ((Integer)fighter.skillsLv.get(Integer.valueOf(cdata.skill))).intValue()) / 100.0D;
double heal = fighter.attr(Attribute.ATK) * skillstenth;
if (Math.abs(heal - settle.value) > 1000.0D) {
throw new WSException(ErrorCode.Fight_CheckFailed, "伤害错误2");
}
victim.hp += settle.value;
if (victim.hp > victim.attr(Attribute.MaxHP)) {
victim.hp = victim.attr(Attribute.MaxHP);
} }
else if ("Buff".equals(refSkill.SettleType))

{ if (refSkill.ClearBuffList.size() != 0) {
victim.removeBuffs(refSkill.ClearBuffList);
}

if (refSkill.Buff != 0) {

RefBuff refBuff = (RefBuff)RefDataMgr.get(RefBuff.class, Integer.valueOf(refSkill.Buff));
if (refBuff == null) {
CommLog.info("buff不存在，skill:{}, tar:{}", Integer.valueOf(refSkill.id), Integer.valueOf(victim.id));
throw new WSException(ErrorCode.Fight_CheckFailed, "Buff结算错误0");
} 
Buff pre = victim.buffs.get(Integer.valueOf(refBuff.id));
if (pre == null || cdata.time >= pre.cd)

{ 

Buff buff = new Buff(refBuff, ((Integer)fighter.skillsLv.get(Integer.valueOf(cdata.skill))).intValue());
buff.time = (cdata.time + refBuff.Time * 1000);
buff.cd = (cdata.time + refBuff.CD * 1000);
victim.buffs.put(Integer.valueOf(buff.id), buff); } 
}  }
else { throw new WSException(ErrorCode.Fight_CheckFailed, "结算错误2"); }

b++; }

} 

int maxatk = 0;
int atkSurvivor = 0;
for (Fighter f : attacker.values()) {
maxatk = Math.max(f.fighttimes, maxatk);
if (f.isMain() && f.hp > 0.0D)
atkSurvivor++; 
} 
int maxdef = 0;
int defSurvivor = 0;
for (Fighter f : deffender.values()) {
maxdef = Math.max(f.fighttimes, maxdef);
if (f.isMain() && f.hp > 0.0D)
defSurvivor++; 
} 
if (Math.abs(maxatk - maxdef) > 5) {
throw new WSException(ErrorCode.Fight_CheckFailed, "攻击频率");
}
if (atkSurvivor < 0) {
throw new WSException(ErrorCode.Fight_CheckFailed, "结算错误atk");
}
if (defSurvivor > 0) {
throw new WSException(ErrorCode.Fight_CheckFailed, "结算错误def");
}
} catch (Exception e) {
onCheckError();
FightManager.getInstance().removeFight(this);
throw e;
} 
}

private void initbuff(Map<Integer, Fighter> attacker, int time) {
for (Fighter f : attacker.values())
f.initBuff(time); 
}

protected abstract E onLost();

protected abstract E onWin();

protected abstract Map<Integer, Fighter> getDeffenders();

protected abstract Map<Integer, Fighter> getAttackers();

public abstract int fightTime();
}

