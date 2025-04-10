package business.player.feature.character;

import com.zhonglian.server.common.enums.Attribute;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AttrCalculator
{
protected Map<Attribute, Integer> allAttr = new HashMap<>();
protected int power = 0;
protected boolean updated = false;

protected void addAttr(Attribute attr, int value) {
this.allAttr.put(attr, Integer.valueOf(((Integer)this.allAttr.get(attr)).intValue() + value));
}

protected void addAttr(List<Attribute> attr, List<Integer> value) {
for (int i = 0; i < attr.size(); i++) {
addAttr(attr.get(i), ((Integer)value.get(i)).intValue());
}
}

protected abstract void doUpdate();

public void update() {
if (this.updated) {
return;
}
this.allAttr.clear();
doUpdate();
this.updated = true;
}

public int getPower() {
update();
return this.power;
}

public Map<Attribute, Integer> getAttrs() {
update();
return this.allAttr;
}

public Integer getAttr(Attribute attr) {
update();
Integer value = this.allAttr.get(attr);
return Integer.valueOf((value == null) ? 0 : value.intValue());
}
}

