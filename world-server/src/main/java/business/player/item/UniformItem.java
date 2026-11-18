package business.player.item;

import com.zhonglian.server.common.enums.PrizeType;
import core.config.refdata.ref.RefUniformItem;

public class UniformItem {
    private Integer uniformId;
    private int count;

    public UniformItem(RefUniformItem ref, int count) {
        this.uniformId = ref.id;
        this.count = count;
    }

    public UniformItem(int uniformid, int count) {
        this.uniformId = uniformid;
        this.count = count;
    }

    public UniformItem(PrizeType type, int itemid, int count) {
        this.uniformId = ItemUtils.getUniformId(type, itemid);
        this.count = count;
    }

    public UniformItem(PrizeType currency, int count) {
        this.uniformId = ItemUtils.getUniformId(currency, 0);
        this.count = count;
    }

    public void setUniformId(int uniformId) {
        this.uniformId = uniformId;
    }

    public int getUniformId() {
        return this.uniformId;
    }

    public int getCount() {
        return this.count;
    }

    public void addCount(int count) {
        this.count += count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "{\"uniformId\":" + this.uniformId + ",\"count\":" + this.count + "},";
    }
}
