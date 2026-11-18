package business.global.fight;

import com.zhonglian.server.common.enums.Attribute;
import core.config.refdata.ref.RefBuff;
import java.util.HashMap;
import java.util.Map;

public class Buff {
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
        return value == null ? 0.0 : value;
    }

    public double attrFixed(Attribute attr) {
        Double value = this.attrsFixed.get(attr);
        return value == null ? 0.0 : value;
    }

    public Buff(RefBuff ref, int bufflv) {
        this.id = ref.id;
        this.ref = ref;
        this.lv = bufflv;
        this.attrsPer = new HashMap<>();
        this.attrsFixed = new HashMap<>();

        for (int i = 0; i < this.ref.AttrTypeList.size(); i++) {
            Attribute attr = this.ref.AttrTypeList.get(i);
            double value = this.ref.AttrValueList.get(i) + this.ref.AttrIncList.get(i) * this.lv;
            if (this.ref.AttrFixedList.get(i)) {
                this.attrsFixed.put(attr, value);
            } else {
                this.attrsPer.put(attr, value / 100.0);
            }
        }
    }
}
