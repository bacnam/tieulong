package core.config.refdata.ref;

import BaseCommon.CommLog;
import com.zhonglian.server.common.data.RefAssert;
import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.enums.ConstEnum;
import com.zhonglian.server.common.utils.CommString;
import core.config.refdata.RefDataMgr;

import java.util.Iterator;
import java.util.List;

public class RefGoodsInfo
        extends RefBaseGame {
    @RefField(iskey = true)
    public long ID;
    public int UniformID;
    public ConstEnum.DiscountType DiscountType;
    public List<Integer> CostWeight;
    public List<String> CostUniformId;
    public List<String> Price;
    public List<String> Discount;
    public int TimeLimit;

    public boolean Assert() {
        if (this.DiscountType == null) {
            this.DiscountType = ConstEnum.DiscountType.None;
        }
        if (this.Discount == null || this.Discount.size() == 0) {
            this.Discount = this.Price;
        }
        if (!RefAssert.inRef(Integer.valueOf(this.UniformID), RefUniformItem.class, new Object[0])) {
            return false;
        }
        if (!RefAssert.listSize(this.CostWeight, this.CostUniformId, new List[]{this.Price, this.Discount})) {
            return false;
        }
        if (this.CostUniformId != null) {
            for (String costs : this.CostUniformId) {
                List<Integer> list = CommString.getIntegerList(costs, "&");
                for (Iterator<Integer> iterator = list.iterator(); iterator.hasNext(); ) {
                    int uniformId = ((Integer) iterator.next()).intValue();
                    if (RefDataMgr.get(RefUniformItem.class, Integer.valueOf(uniformId)) == null) {
                        CommLog.error("表GoodsInfo中ID={}的CostUniformId={}中的数值在物品总表UniformItem中无定义", Long.valueOf(this.ID), this.CostUniformId);
                        return false;
                    }
                }

            }
        }
        for (int index = 0; index < this.CostUniformId.size(); index++) {
            List<Integer> costs = CommString.getIntegerList(this.CostUniformId.get(index), "&");
            List<Integer> prices = CommString.getIntegerList(this.Price.get(index), "&");
            if (costs.size() != prices.size()) {
                CommLog.error("表GoodsInfo中ID={}的CostUniformId={}和Price={}配置长度不一致", new Object[]{Long.valueOf(this.ID), this.CostUniformId, this.Price});
                return false;
            }
            if (this.Discount != null && this.Discount.size() > 0) {
                List<Integer> discounts = CommString.getIntegerList(this.Discount.get(index), "&");
                if (costs.size() != discounts.size()) {
                    CommLog.error("表GoodsInfo中ID={}的Price={}和Discount={}配置长度不一致", new Object[]{Long.valueOf(this.ID), this.Price, this.Discount});
                    return false;
                }
            }
        }
        return true;
    }

    public boolean AssertAll(RefContainer<?> all) {
        return true;
    }
}

