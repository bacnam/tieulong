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

public abstract class Fight<E> {
    private static int nextFightId = 1;
    int beginTime = CommTime.nowSecond();
    int fightId = nextFightId++;
    long seed = this.fightId;

    double random() {
        this.seed = (this.seed * 9301L + 49297L) % 233280L;
        return this.seed / 233280.0;
    }

    public int getId() {
        return this.fightId;
    }

    public E settle(FightResult result) {
        this.beforeSettle(result);
        E rtn;
        if (result == FightResult.Win) {
            rtn = this.onWin();
        } else {
            rtn = this.onLost();
        }

        this.afterSettle(result);
        return rtn;
    }

    protected void beforeSettle(FightResult result) {
    }

    protected void afterSettle(FightResult result) {
    }

    protected void onCheckError() {
    }

    protected abstract E onLost();

    protected abstract E onWin();

    protected abstract Map<Integer, Fighter> getDeffenders();

    protected abstract Map<Integer, Fighter> getAttackers();

    public void check(List<core.network.proto.Fight.CheckData> checks) throws WSException {
        try {
            if (checks == null) {
                throw new WSException(ErrorCode.Fight_CheckFailed, "数据不足");
            } else {
                checks.sort((left, right) -> left.time - right.time);
                Map<Integer, Fighter> attacker = this.getAttackers();
                Map<Integer, Fighter> deffender = this.getDeffenders();
                int time = checks.get(0).time;
                this.initbuff(attacker, time);
                this.initbuff(deffender, time);

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
                        fighter = attacker.get(cdata.attacker);
                    } else if (cdata.agroup == 2) {
                        fighter = deffender.get(cdata.attacker);
                    }

                    if (fighter == null) {
                        CommLog.error("fighter:{},group:{}", cdata.attacker, cdata.agroup);
                        throw new WSException(ErrorCode.Fight_CheckFailed, "找不到相关的战斗角色0");
                    }

                    if (fighter.hp < -100.0) {
                        CommLog.error("fighter:{}, hp:{}", fighter.id, fighter.hp);
                        throw new WSException(ErrorCode.Fight_CheckFailed, "非法角色0");
                    }

                    RefSkill refSkill = fighter.skills.get(cdata.skill);
                    if (refSkill == null) {
                        CommLog.info("caster:{}, group:{}, skill:{}, time:{}", fighter.id, cdata.agroup, cdata.skill, cdata.time);
                        throw new WSException(ErrorCode.Fight_CheckFailed, "技能不存在0");
                    }

                    Integer lastCastTime = fighter.skillsCD.get(cdata.skill);
                    if (lastCastTime != null && cdata.time + 300 < lastCastTime + refSkill.CD) {
                        CommLog.error(
                            "time:{}, fighter:{},skill:{},s_time:{},c_time:{},cd:{}",
                            cdata.time - time,
                            fighter.id,
                            cdata.skill,
                            cdata.time + 3,
                            lastCastTime + refSkill.CD,
                            refSkill.CD
                        );
                        throw new WSException(ErrorCode.Fight_CheckFailed, "非法技能间隔1");
                    }

                    fighter.skillsCD.put(cdata.skill, cdata.time);
                    fighter.action(cdata.time);
                    Set<Fighter> settled = new HashSet<>();

                    for (core.network.proto.Fight.Settle settle : cdata.settles) {
                        Fighter victim = null;
                        if (settle.vgroup == 1) {
                            victim = attacker.get(settle.victim);
                        } else if (settle.vgroup == 2) {
                            victim = deffender.get(settle.victim);
                        }

                        if (victim == null) {
                            throw new WSException(ErrorCode.Fight_CheckFailed, "找不到相关的战斗角色1");
                        }

                        if (settled.contains(victim)) {
                            throw new WSException(ErrorCode.Fight_CheckFailed, "非法角色1");
                        }

                        if (victim.hp < -100.0) {
                            throw new WSException(ErrorCode.Fight_CheckFailed, "非法角色2");
                        }

                        if ("Enemy".equals(refSkill.CastTarget) && cdata.agroup == settle.vgroup) {
                            throw new WSException(ErrorCode.Fight_CheckFailed, "非法技能对象0");
                        }

                        if ("SelfAndAlias".equals(refSkill.CastTarget) && cdata.agroup != settle.vgroup) {
                            throw new WSException(ErrorCode.Fight_CheckFailed, "非法技能对象0");
                        }

                        settled.add(victim);
                        if ("Damage".equals(refSkill.SettleType)) {
                            double defvalue = 0.0;
                            if ("DEF".equals(refSkill.DefAttr)) {
                                defvalue = victim.attr(Attribute.DEF);
                            } else if ("RGS".equals(refSkill.DefAttr)) {
                                defvalue = victim.attr(Attribute.RGS);
                            }

                            double damage = fighter.attr(Attribute.ATK) - defvalue;
                            double skillstenth = (refSkill.Attr + refSkill.AttrAdd * fighter.skillsLv.get(cdata.skill).intValue()) / 100.0;
                            damage *= skillstenth;
                            if (damage <= 0.0) {
                                damage = this.random() * 10.0;
                            }

                            double hitfactor = RefDataMgr.getFactor("Battle_HitFactor", 40);
                            double hitchance = hitfactor
                                * fighter.attr(Attribute.Hit)
                                / (hitfactor * fighter.attr(Attribute.Hit) + victim.attr(Attribute.Dodge));
                            hitchance += (fighter.attr(Attribute.HitPro) - victim.attr(Attribute.DodgePro)) / 100.0;
                            boolean hit = this.random() < hitchance;
                            boolean critical = false;
                            if (hit) {
                                double crtfactor = RefDataMgr.getFactor("Battle_CriticalFactor", 40);
                                double critchance = fighter.attr(Attribute.Critical)
                                    / (fighter.attr(Attribute.Critical) + crtfactor * victim.attr(Attribute.Tenacity));
                                critchance += (fighter.attr(Attribute.CriticalPro) - victim.attr(Attribute.TenacityPro)) / 100.0;
                                critical = this.random() < critchance;
                                if (critical) {
                                    damage *= RefDataMgr.getFactor("CriticalMultiple", 2000) / 1000.0;
                                }
                            } else {
                                damage = 0.0;
                            }

                            damage *= 1.0 + (fighter.attr(Attribute.Damage) - victim.attr(Attribute.Reduction)) / 100.0;
                            damage = Math.floor(damage);
                            if (Math.abs(damage - settle.value) > 1000.0) {
                                CommLog.info(
                                    "time:{}, atk:{}-{}, victim:{}, s_hit:{}, s_crit:{}, s_damage:{}, c_damage:{}",
                                    cdata.time - time,
                                    cdata.agroup,
                                    fighter.id,
                                    victim.id,
                                    hit,
                                    critical,
                                    damage,
                                    settle.value
                                );
                                throw new WSException(ErrorCode.Fight_CheckFailed, "伤害错误1");
                            }

                            victim.hp = victim.hp - settle.value;
                        } else if ("Heal".equals(refSkill.SettleType)) {
                            double skillstenthx = (refSkill.Attr + refSkill.AttrAdd * fighter.skillsLv.get(cdata.skill).intValue()) / 100.0;
                            double heal = fighter.attr(Attribute.ATK) * skillstenthx;
                            if (Math.abs(heal - settle.value) > 1000.0) {
                                throw new WSException(ErrorCode.Fight_CheckFailed, "伤害错误2");
                            }

                            victim.hp = victim.hp + settle.value;
                            if (victim.hp > victim.attr(Attribute.MaxHP)) {
                                victim.hp = victim.attr(Attribute.MaxHP);
                            }
                        } else {
                            if (!"Buff".equals(refSkill.SettleType)) {
                                throw new WSException(ErrorCode.Fight_CheckFailed, "结算错误2");
                            }

                            if (refSkill.ClearBuffList.size() != 0) {
                                victim.removeBuffs(refSkill.ClearBuffList);
                            }

                            if (refSkill.Buff != 0) {
                                RefBuff refBuff = RefDataMgr.get(RefBuff.class, refSkill.Buff);
                                if (refBuff == null) {
                                    CommLog.info("buff不存在，skill:{}, tar:{}", refSkill.id, victim.id);
                                    throw new WSException(ErrorCode.Fight_CheckFailed, "Buff结算错误0");
                                }

                                Buff pre = victim.buffs.get(refBuff.id);
                                if (pre == null || cdata.time >= pre.cd) {
                                    Buff buff = new Buff(refBuff, fighter.skillsLv.get(cdata.skill));
                                    buff.time = cdata.time + refBuff.Time * 1000;
                                    buff.cd = cdata.time + refBuff.CD * 1000;
                                    victim.buffs.put(buff.id, buff);
                                }
                            }
                        }
                    }
                }

                int maxatk = 0;
                int atkSurvivor = 0;

                for (Fighter f : attacker.values()) {
                    maxatk = Math.max(f.fighttimes, maxatk);
                    if (f.isMain() && f.hp > 0.0) {
                        atkSurvivor++;
                    }
                }

                int maxdef = 0;
                int defSurvivor = 0;

                for (Fighter fx : deffender.values()) {
                    maxdef = Math.max(fx.fighttimes, maxdef);
                    if (fx.isMain() && fx.hp > 0.0) {
                        defSurvivor++;
                    }
                }

                if (Math.abs(maxatk - maxdef) > 5) {
                    throw new WSException(ErrorCode.Fight_CheckFailed, "攻击频率");
                } else if (atkSurvivor < 0) {
                    throw new WSException(ErrorCode.Fight_CheckFailed, "结算错误atk");
                } else if (defSurvivor > 0) {
                    throw new WSException(ErrorCode.Fight_CheckFailed, "结算错误def");
                }
            }
        } catch (Exception var32) {
            this.onCheckError();
            FightManager.getInstance().removeFight(this);
            throw var32;
        }
    }

    private void initbuff(Map<Integer, Fighter> attacker, int time) {
        for (Fighter f : attacker.values()) {
            f.initBuff(time);
        }
    }

    public abstract int fightTime();
}
