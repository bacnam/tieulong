package business.global.fight;

import com.zhonglian.server.common.enums.Attribute;
import core.config.refdata.ref.RefBuff;
import java.util.HashMap;
import java.util.Map;

public class Buff
{
int id;
long time;
RefBuff ref;
int lv;
long cd;
boolean isExpired = true;
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

public Buff(RefBuff ref, int bufflv) {
this.id = ref.id;
this.ref = ref;
this.lv = bufflv;

this.attrsPer = new HashMap<>();
this.attrsFixed = new HashMap<>();

for (int i = 0; i < this.ref.AttrTypeList.size(); i++) {
Attribute attr = this.ref.AttrTypeList.get(i);
double value = ((Double)this.ref.AttrValueList.get(i)).doubleValue() + ((Double)this.ref.AttrIncList.get(i)).doubleValue() * this.lv;
if (((Boolean)this.ref.AttrFixedList.get(i)).booleanValue()) {
this.attrsFixed.put(attr, Double.valueOf(value));
} else {
this.attrsPer.put(attr, Double.valueOf(value / 100.0D));
} 
} 
}
}

