package business.player.feature.character;

import com.zhonglian.server.common.enums.Attribute;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AttrCalculator {
    protected Map<Attribute, Integer> allAttr = new HashMap<>();
    protected int power = 0;
    protected boolean updated = false;

    protected void addAttr(Attribute attr, int value) {
        this.allAttr.put(attr, this.allAttr.get(attr) + value);
    }

    protected void addAttr(List<Attribute> attr, List<Integer> value) {
        for (int i = 0; i < attr.size(); i++) {
            this.addAttr(attr.get(i), value.get(i));
        }
    }

    protected abstract void doUpdate();

    public void update() {
        if (!this.updated) {
            this.allAttr.clear();
            this.doUpdate();
            this.updated = true;
        }
    }

    public int getPower() {
        this.update();
        return this.power;
    }

    public Map<Attribute, Integer> getAttrs() {
        this.update();
        return this.allAttr;
    }

    public Integer getAttr(Attribute attr) {
        this.update();
        Integer value = this.allAttr.get(attr);
        return value == null ? 0 : value;
    }
}
