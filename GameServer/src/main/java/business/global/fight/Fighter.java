package business.global.fight;

import com.zhonglian.server.common.enums.Attribute;
import core.config.refdata.ref.RefSkill;

import java.util.*;

public class Fighter {
    public int id;
    public Map<Integer, RefSkill> skills = new HashMap<>();
    public Map<Integer, Integer> skillsLv = new HashMap<>();
    public Map<Attribute, Double> attrs = new HashMap<>();

    public boolean isMain = true;

    public double hp;
    public int x;
    public int y;
    Map<Integer, Integer> skillsCD = new HashMap<>();
    int fighttimes = 0;
    Map<Integer, Buff> buffs = new HashMap<>();

    public double attr(Attribute attr) {
        Double base = this.attrs.get(attr);
        if (base == null) {
            base = Double.valueOf(0.0D);
        }
        double per = 0.0D;
        double fixed = 0.0D;
        for (Buff b : this.buffs.values()) {
            if (b.isExpired) {
                continue;
            }
            if (b.ref.HPCondition != 0 && this.hp / ((Double) this.attrs.get(Attribute.MaxHP)).doubleValue() > b.ref.HPCondition / 100.0D) {
                continue;
            }
            per += b.attrPer(attr);
            fixed += b.attrFixed(attr);
        }
        return base.doubleValue() * (1.0D + per) + fixed;
    }

    public void checkBuff(long time) {
        List<Integer> buff2remove = new ArrayList<>();
        for (Buff buff : this.buffs.values()) {
            buff.isExpired = (buff.time < time);
            if (buff.time < time && buff.cd < time) {
                buff2remove.add(Integer.valueOf(buff.id));
            }
        }
        for (Iterator<Integer> iterator = buff2remove.iterator(); iterator.hasNext(); ) {
            int buff = ((Integer) iterator.next()).intValue();
            this.buffs.remove(Integer.valueOf(buff));
        }

    }

    public void action(long time) {
        this.fighttimes++;
    }

    public void putAllAttr(Map<Attribute, Integer> attrs) {
        for (Map.Entry<Attribute, Integer> attr : attrs.entrySet()) {
            this.attrs.put(attr.getKey(), new Double(((Integer) attr.getValue()).intValue()));
        }
    }

    public void putAttr(Attribute attr, double value) {
        this.attrs.put(attr, new Double(value));
    }

    public void removeBuffs(List<Integer> clearBuffList) {
        for (Integer id : clearBuffList) {
            this.buffs.remove(id);
        }

        this.hp = Math.min(this.hp, attr(Attribute.MaxHP));
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

