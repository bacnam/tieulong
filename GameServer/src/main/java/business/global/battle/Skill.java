package business.global.battle;

import core.config.refdata.ref.RefSkill;
import core.network.proto.Fight;

import java.util.ArrayList;
import java.util.List;

public class Skill {
    public double strength;
    Creature creature;
    RefSkill skilldata;
    int skilllv;
    double cd;
    ISettler settler;
    Selector selector;

    Skill(Creature creature, RefSkill refSkill, int skilllv) {
        this.creature = creature;
        this.skilldata = refSkill;
        this.skilllv = skilllv;
        this.strength = (this.skilldata.Attr + this.skilldata.AttrAdd * this.skilllv) / 100.0D;

        this.selector = new Selector(this.skilldata.SelectTarget, this.skilldata.SelectStrategy, this.skilldata.CastTarget, this.skilldata.SelectArea, this.skilldata.SelectParam);
        this.settler = ISettler.get(refSkill.SettleType, this);
    }

    public boolean cast() {
        Selector.Result select = this.selector.select(this.creature, this.creature.battle.all);
        if (select.selected == null || select.castOn.size() <= 0) {
            return false;
        }
        resetCD();

        String bullet = this.skilldata.Bullet;
        if (bullet == null || bullet.isEmpty()) {
            settle(select.selected, select.castOn);
        } else {
            this.creature.battle.addBullet(new Bullet(this, select.selected, select.castOn));
        }

        return true;
    }

    void settle(Creature selected, List<Creature> castOn) {
        if (this.creature.hp < 0.0D) {
            return;
        }
        List<Fight.Settle> settles = new ArrayList<>();

        for (Creature tar : castOn) {
            if (tar.isDead()) {
                continue;
            }
            double value = this.settler.settle(this.creature, tar);
            settles.add(new Fight.Settle(tar.id, tar.group.ordinal(), value));
        }
    }

    private void resetCD() {
        this.cd = this.skilldata.CD / 1000.0D;
    }

    public void update(double dt) {
        if (this.cd > 0.0D)
            this.cd = Math.max(this.cd - dt, 0.0D);
    }

    public boolean isEnable() {
        return (Math.max(this.skilllv, this.creature.level) >= this.skilldata.Require && this.cd <= 0.0D);
    }

    public int getPriority() {
        return this.skilldata.Priority;
    }

    public int getId() {
        return this.skilldata.id;
    }

    public int getLevel() {
        return this.skilllv;
    }
}

