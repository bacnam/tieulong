package core.config.refdata.ref;

import BaseCommon.CommLog;
import business.player.item.Reward;
import com.zhonglian.server.common.data.RefAssert;
import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.common.utils.Random;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jsc.distributions.Binomial;

public class RefReward extends RefBaseGame {
    @RefField(
        iskey = true
    )
    public int id;
    public List<Integer> RegularItemIdList;
    public List<Integer> RegularCountList;
    public List<Integer> ItemIdList;
    public List<Integer> CountList;
    public List<Integer> WeightList;

    public Reward genReward() {
        Reward reward = new Reward();

        for (int i = 0; i < this.RegularItemIdList.size(); i++) {
            reward.add(this.RegularItemIdList.get(i), this.RegularCountList.get(i));
        }

        for (int index = 0; index < this.WeightList.size(); index++) {
            if (Random.nextInt(10000) <= this.WeightList.get(index)) {
                reward.add(this.ItemIdList.get(index), this.CountList.get(index));
            }
        }

        return reward;
    }

    public Reward genLimitReward(int num) {
        Reward reward = new Reward();

        for (int i = 0; i < this.RegularItemIdList.size(); i++) {
            reward.add(this.RegularItemIdList.get(i), this.RegularCountList.get(i));
        }

        for (int index = 0; index < this.WeightList.size(); index++) {
            if (Random.nextInt(10000) <= this.WeightList.get(index) * num / 10000) {
                reward.add(this.ItemIdList.get(index), this.CountList.get(index));
            }
        }

        return reward;
    }

    public Reward genReward(int times) {
        Reward reward = new Reward();

        for (int i = 0; i < this.RegularItemIdList.size(); i++) {
            reward.add(this.RegularItemIdList.get(i), this.RegularCountList.get(i) * times);
        }

        for (int index = 0; index < this.WeightList.size(); index++) {
            Binomial binoimal = new Binomial(times, this.WeightList.get(index).intValue() / 10000.0);
            int count = (int)binoimal.random();
            reward.add(this.ItemIdList.get(index), this.CountList.get(index) * count);
        }

        return reward;
    }

    public Reward genReward(int times, int max) {
        Reward reward = new Reward();
        int sum = 0;

        while (times > 0) {
            for (int i = 0; i < this.RegularItemIdList.size(); i++) {
                int count = this.RegularCountList.get(i);
                if (sum + count >= max) {
                    return reward.add(this.RegularItemIdList.get(i), max - sum);
                }

                reward.add(this.RegularItemIdList.get(i), count);
                sum += count;
            }

            for (int index = 0; index < this.WeightList.size(); index++) {
                if (Random.nextInt(10000) <= this.WeightList.get(index)) {
                    int count = this.CountList.get(index);
                    if (sum + count >= max) {
                        return reward.add(this.ItemIdList.get(index), max - sum);
                    }

                    reward.add(this.ItemIdList.get(index), count);
                    sum += count;
                }
            }

            times--;
        }

        return reward;
    }

    @Override
    public boolean Assert() {
        for (int w : this.WeightList) {
            if (w >= 10000 || w <= 0) {
                CommLog.error("掉落权重只能在(0,10000)之间.");
                return false;
            }
        }

        RefAssert.inRef(this.RegularItemIdList, RefUniformItem.class);
        return !RefAssert.listSize(this.ItemIdList, this.CountList, this.WeightList)
            ? false
            : RefAssert.listSize(this.RegularItemIdList, this.RegularCountList);
    }

    @Override
    public boolean AssertAll(RefContainer<?> all) {
        return true;
    }

    public static void main(String[] args) {
        int repeat = 100000;
        int times = 1;
        double p = 0.1;
        Map<Double, Integer> rslt = new HashMap<>();
        long begin = CommTime.nowMS();
        Binomial binoimal = new Binomial(times, p);
        int sum = 0;

        for (int i = 0; i < repeat; i++) {
            double count = binoimal.random();
            Integer precount = rslt.get(count);
            if (precount == null) {
                rslt.put(count, 1);
            } else {
                rslt.put(count, precount + 1);
            }

            sum = (int)(sum + count);
        }

        System.out.println("time:" + (CommTime.nowMS() - begin));
        System.out.println(rslt.toString());
        System.out.println(sum + "/" + repeat * times + "=" + 1.0 * sum / (repeat * times));
    }
}
