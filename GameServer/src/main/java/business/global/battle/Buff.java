package business.global.battle;

import com.zhonglian.server.common.enums.Attribute;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefBuff;
import java.util.HashMap;
import java.util.Map;

public class Buff
{
private Creature creature;
int buffid;
private RefBuff buffdata;
private int lv;
private double time;
private Map<Attribute, Double> attrsPer;
private Map<Attribute, Double> attrsFixed;

public double attrPer(Attribute attr) {
Double value = this.attrsPer.get(attr);
return (value == null) ? 0.0D : value.doubleValue();
}

public double attrFixed(Attribute attr) {
Double value = this.attrsFixed.get(attr);
return (value == null) ? 0.0D : value.doubleValue();
}

public Buff(Creature tar, int buffid, int skilllv) {
this.creature = tar;
this.lv = skilllv;
this.buffid = buffid;
this.buffdata = (RefBuff)RefDataMgr.get(RefBuff.class, Integer.valueOf(buffid));

this.attrsPer = new HashMap<>();
this.attrsFixed = new HashMap<>();

for (int i = 0; i < this.buffdata.AttrTypeList.size(); i++) {
Attribute attr = this.buffdata.AttrTypeList.get(i);
double value = ((Double)this.buffdata.AttrValueList.get(i)).doubleValue() + ((Double)this.buffdata.AttrIncList.get(i)).doubleValue() * this.lv;
if (((Boolean)this.buffdata.AttrFixedList.get(i)).booleanValue()) {
this.attrsFixed.put(attr, Double.valueOf(value));
} else {
this.attrsPer.put(attr, Double.valueOf(value / 100.0D));
} 
} 
}

public void update(double dt) {
double pre = this.time;
this.time += dt;
if (this.time > this.buffdata.Time) {
return;
}
Math.floor(pre); Math.floor(this.time);
}

public boolean isEffective() {
if (this.time > this.buffdata.Time) {
return false;
}
if (this.buffdata.HPCondition > 0) {
return (this.creature.hp / this.creature.baseAttr(Attribute.MaxHP) < (this.buffdata.HPCondition / 100));
}
return true;
}

public boolean isInCd() {
return (this.time < this.buffdata.CD);
}

public int getId() {
return this.buffid;
}

public int getLevel() {
return this.lv;
}
}

