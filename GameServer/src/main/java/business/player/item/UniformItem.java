package business.player.item;

import com.zhonglian.server.common.enums.PrizeType;
import core.config.refdata.ref.RefUniformItem;

public class UniformItem {
    private Integer uniformId;
    private int count;

    public UniformItem(RefUniformItem ref, int count) {
        this.uniformId = Integer.valueOf(ref.id);
        this.count = count;
    }

    public UniformItem(int uniformid, int count) {
        this.uniformId = Integer.valueOf(uniformid);
        this.count = count;
    }

    public UniformItem(PrizeType type, int itemid, int count) {
        this.uniformId = Integer.valueOf(ItemUtils.getUniformId(type, itemid));
        this.count = count;
    }

    public UniformItem(PrizeType currency, int count) {
        this.uniformId = Integer.valueOf(ItemUtils.getUniformId(currency, 0));
        this.count = count;
    }

    public int getUniformId() {
        return this.uniformId.intValue();
    }

    public void setUniformId(int uniformId) {
        this.uniformId = Integer.valueOf(uniformId);
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void addCount(int count) {
        this.count += count;
    }

    public String toString() {
        return "{\"uniformId\":" + this.uniformId + ",\"count\":" + this.count + "},";
    }
}

