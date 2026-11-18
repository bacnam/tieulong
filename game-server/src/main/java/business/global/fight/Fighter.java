package business.global.fight;

import com.zhonglian.server.common.enums.Attribute;
import core.config.refdata.ref.RefSkill;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Fighter {
    public int id;
    public Map<Integer, RefSkill> skills = new HashMap<>();
    public Map<Integer, Integer> skillsLv = new HashMap<>();
    public Map<Attribute, Double> attrs = new HashMap<>();
    public boolean isMain = true;
    public double hp;
    Map<Integer, Integer> skillsCD = new HashMap<>();
    int fighttimes = 0;
    Map<Integer, Buff> buffs = new HashMap<>();
    public int x;
    public int y;

    public double attr(Attribute attr) {
        Double base = this.attrs.get(attr);
        if (base == null) {
            base = 0.0;
        }

        double per = 0.0;
        double fixed = 0.0;

        for (Buff b : this.buffs.values()) {
            if (!b.isExpired && (b.ref.HPCondition == 0 || !(this.hp / this.attrs.get(Attribute.MaxHP) > b.ref.HPCondition / 100.0))) {
                per += b.attrPer(attr);
                fixed += b.attrFixed(attr);
            }
        }

        return base * (1.0 + per) + fixed;
    }

    public void checkBuff(long time) {
        List<Integer> buff2remove = new ArrayList<>();

        for (Buff buff : this.buffs.values()) {
            buff.isExpired = buff.time < time;
            if (buff.time < time && buff.cd < time) {
                buff2remove.add(buff.id);
            }
        }

        for (int buffx : buff2remove) {
            this.buffs.remove(buffx);
        }
    }

    public void action(long time) {
        this.fighttimes++;
    }

    public void putAllAttr(Map<Attribute, Integer> attrs) {
        for (Entry<Attribute, Integer> attr : attrs.entrySet()) {
            this.attrs.put(attr.getKey(), new Double(attr.getValue().intValue()));
        }
    }

    public void putAttr(Attribute attr, double value) {
        this.attrs.put(attr, new Double(value));
    }

    public void removeBuffs(List<Integer> clearBuffList) {
        for (Integer id : clearBuffList) {
            this.buffs.remove(id);
        }

        this.hp = Math.min(this.hp, this.attr(Attribute.MaxHP));
    }

    public boolean isMain() {
        return this.isMain;
    }

    public void initBuff(int time) {
        for (Buff buff : this.buffs.values()) {
            buff.cd += time;
            buff.time += time;
        }
    }
}
