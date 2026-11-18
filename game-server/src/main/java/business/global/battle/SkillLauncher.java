package business.global.battle;

import core.config.refdata.RefDataMgr;
import java.util.ArrayList;
import java.util.List;

public class SkillLauncher {
    private static double ATK_CD = 0.0;
    public static double MinATK = 0.0;
    public static double CriticalMultiple = 0.0;
    Creature creature;
    List<Skill> $skillList;
    double cd;

    static {
        ATK_CD = RefDataMgr.getFactor("ATKCD", 1.0);
        MinATK = RefDataMgr.getFactor("MinATK", 3);
        CriticalMultiple = RefDataMgr.getFactor("CriticalMultiple", 2000) / 1000.0;
    }

    SkillLauncher(Creature creature) {
        this.creature = creature;
        this.$skillList = new ArrayList<>();
        this.cd = ATK_CD;
    }

    public void addSkill(Skill skill) {
        this.$skillList.add(skill);
    }

    public void update(double dt) {
        if (this.cd > 0.0) {
            this.cd = Math.max(this.cd - dt, 0.0);
        }

        for (Skill skill : this.$skillList) {
            skill.update(dt);
        }
    }

    public Skill nextSkill() {
        if (this.cd > 0.0) {
            return null;
        } else {
            Skill skill = null;

            for (Skill s : this.$skillList) {
                if (s.isEnable() && (skill == null || skill.getPriority() < s.getPriority())) {
                    skill = s;
                }
            }

            return skill;
        }
    }

    public void resetCD() {
        this.cd = ATK_CD;
    }
}
