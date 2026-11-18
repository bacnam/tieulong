package business.global.battle;

import com.zhonglian.server.common.enums.Attribute;
import core.config.refdata.RefDataMgr;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Creature {
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

        for (Entry<Attribute, Integer> attr : attrs.entrySet()) {
            this.attrs.put(attr.getKey(), attr.getValue().doubleValue());
        }

        this.skillLauncher = new SkillLauncher(this);
        this.buffs = new HashMap<>();
        this.hp = this.attrs.get(Attribute.MaxHP);
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
            base = 0.0;
        }

        double per = 0.0;
        double fixed = 0.0;

        for (Buff b : this.buffs.values()) {
            if (b.isEffective()) {
                per += b.attrPer(attr);
                fixed += b.attrFixed(attr);
            }
        }

        return base * (1.0 + per) + fixed;
    }

    public double baseAttr(Attribute attr) {
        Double base = this.attrs.get(attr);
        return base == null ? 0.0 : base;
    }

    public boolean isDead() {
        return this.hp < 0.0;
    }

    public void update(double dt) {
        this.skillLauncher.update(dt);
        List<Integer> toremove = new ArrayList<>();

        for (Buff buff : this.buffs.values()) {
            buff.update(dt);
            if (!buff.isInCd() && !buff.isEffective()) {
                toremove.add(buff.buffid);
            }
        }

        for (Integer b : toremove) {
            this.buffs.remove(b);
        }

        this.hp = Math.min(this.hp, this.attr(Attribute.MaxHP));
    }

    public boolean isFriend(Creature ch) {
        return this.group == ch.group ? true : this.group == Group.Player && ch.group == Group.Allies || this.group == Group.Allies && ch.group == Group.Player;
    }

    public void castSkill() {
        if (!this.isDead()) {
            Skill skill = this.skillLauncher.nextSkill();
            if (skill != null && skill.cast()) {
                this.skillLauncher.resetCD();
            }
        }
    }

    public void removeBuff(List<Integer> buffidList) {
        for (int buffid : buffidList) {
            this.buffs.remove(buffid);
        }
    }

    public void addBuff(Buff buff) {
        Buff pre = this.buffs.get(buff.buffid);
        if (pre == null || !pre.isInCd()) {
            this.buffs.put(buff.buffid, buff);
        }
    }

    public void stepTo(Creature tar, double dt) {
        if (!(this.skillLauncher.cd > 0.0)) {
            if (!this.inRange(tar)) {
                double d = this.SPD * dt;
                double dy = this.y - tar.y;
                double dx = this.x - tar.x;
                if (dy == 0.0) {
                    this.x = dx > 0.0 ? this.x - Math.sqrt(d) : this.x + Math.sqrt(d);
                } else if (dx == 0.0) {
                    this.y = dy > 0.0 ? this.y - Math.sqrt(d) : this.y + Math.sqrt(d);
                } else {
                    double tx = Math.sqrt(d / (1.0 + dy / dx * (dy / dx)));
                    double ty = Math.abs(tx * dy / dx);
                    this.x = dx > 0.0 ? this.x - tx : this.x + tx;
                    this.y = dy > 0.0 ? this.y - ty : this.y + ty;
                }
            }
        }
    }

    public boolean hit(Creature tar) {
        double factor = RefDataMgr.getFactor("Battle_HitFactor", 40);
        double hitchance = factor * this.attr(Attribute.Hit) / (factor * this.attr(Attribute.Hit) + tar.attr(Attribute.Dodge));
        hitchance += (this.attr(Attribute.HitPro) - tar.attr(Attribute.DodgePro)) / 100.0;
        return this.battle.random() < hitchance;
    }

    public boolean critical(Creature tar) {
        double factor = RefDataMgr.getFactor("Battle_CriticalFactor", 40);
        double critchance = this.attr(Attribute.Critical) / (this.attr(Attribute.Critical) + factor * tar.attr(Attribute.Tenacity));
        critchance += (this.attr(Attribute.CriticalPro) - tar.attr(Attribute.TenacityPro)) / 100.0;
        return this.battle.random() < critchance;
    }

    public void damage(double damage, Creature me) {
        this.hp -= damage;
        if (this.isDead()) {
            this.die();
        }
    }

    protected void die() {
        this.battle.onDie(this);
    }

    public void heal(double heal) {
        this.hp = Math.min(this.attr(Attribute.MaxHP), this.hp + heal);
    }

    public boolean inRange(Creature tar) {
        double dx = this.x - tar.x;
        double dy = this.y - tar.y;
        double disance = dx * dx + dy * dy;
        return disance < this.RNG * this.RNG;
    }

    public List<Skill> getSkills() {
        return this.skillLauncher.$skillList;
    }

    public int getType() {
        return this.RoleType.ordinal();
    }
}
