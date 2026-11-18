package business.global.battle;

import com.zhonglian.server.common.enums.Attribute;

abstract class ISettler {
    Skill skill;

    ISettler(Skill skill) {
        this.skill = skill;
    }

    public abstract double settle(Creature var1, Creature var2);

    static ISettler get(String name, Skill skill) {
        switch (name.hashCode()) {
            case 2081907:
                if (name.equals("Buff")) {
                    return new ISettler(skill) {
                        @Override
                        public double settle(Creature me, Creature tar) {
                            if (this.skill.skilldata.ClearBuffList.size() > 0) {
                                tar.removeBuff(this.skill.skilldata.ClearBuffList);
                            }

                            if (this.skill.skilldata.Buff != 0) {
                                tar.addBuff(new Buff(tar, this.skill.skilldata.Buff, this.skill.skilllv));
                            }

                            return 0.0;
                        }
                    };
                }
                break;
            case 2245128:
                if (name.equals("Heal")) {
                    return new ISettler(skill) {
                        @Override
                        public double settle(Creature me, Creature tar) {
                            double heal = me.attr(Attribute.ATK) * this.skill.strength;
                            heal = Math.floor(heal);
                            tar.heal(heal);
                            return heal;
                        }
                    };
                }
                break;
            case 2039707535:
                if (!name.equals("Damage")) {
                }
        }

        return new ISettler(skill) {
            @Override
            public double settle(Creature me, Creature tar) {
                double defvalue = 0.0;
                if ("DEF".equals(this.skill.skilldata.DefAttr)) {
                    defvalue = tar.attr(Attribute.DEF);
                } else if ("RGS".equals(this.skill.skilldata.DefAttr)) {
                    defvalue = tar.attr(Attribute.RGS);
                }

                double dmg = (me.attr(Attribute.ATK) - defvalue) * this.skill.strength;
                dmg = dmg <= 0.0 ? me.battle.random() * SkillLauncher.MinATK : dmg;
                boolean isHit = me.hit(tar);
                if (isHit) {
                    if (me.critical(tar)) {
                        dmg *= SkillLauncher.CriticalMultiple;
                    }
                } else {
                    dmg = 0.0;
                }

                dmg *= 1.0 + (me.attr(Attribute.Damage) - tar.attr(Attribute.Reduction)) / 100.0;
                dmg = Math.floor(dmg);
                tar.damage(dmg, me);
                return dmg;
            }
        };
    }
}
