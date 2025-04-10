package core.config.refdata.ref;

import BaseCommon.CommLog;
import business.player.item.Reward;
import com.zhonglian.server.common.data.RefAssert;
import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.common.utils.Random;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import jsc.distributions.Binomial;

public class RefReward
extends RefBaseGame
{
@RefField(iskey = true)
public int id;
public List<Integer> RegularItemIdList;
public List<Integer> RegularCountList;
public List<Integer> ItemIdList;
public List<Integer> CountList;
public List<Integer> WeightList;

public Reward genReward() {
Reward reward = new Reward();
for (int i = 0; i < this.RegularItemIdList.size(); i++) {
reward.add(((Integer)this.RegularItemIdList.get(i)).intValue(), ((Integer)this.RegularCountList.get(i)).intValue());
}

for (int index = 0; index < this.WeightList.size(); index++) {
if (Random.nextInt(10000) <= ((Integer)this.WeightList.get(index)).intValue())
{

reward.add(((Integer)this.ItemIdList.get(index)).intValue(), ((Integer)this.CountList.get(index)).intValue()); } 
} 
return reward;
}

public Reward genLimitReward(int num) {
Reward reward = new Reward();
for (int i = 0; i < this.RegularItemIdList.size(); i++) {
reward.add(((Integer)this.RegularItemIdList.get(i)).intValue(), ((Integer)this.RegularCountList.get(i)).intValue());
}

for (int index = 0; index < this.WeightList.size(); index++) {
if (Random.nextInt(10000) <= ((Integer)this.WeightList.get(index)).intValue() * num / 10000)
{

reward.add(((Integer)this.ItemIdList.get(index)).intValue(), ((Integer)this.CountList.get(index)).intValue()); } 
} 
return reward;
}

public Reward genReward(int times) {
Reward reward = new Reward();
for (int i = 0; i < this.RegularItemIdList.size(); i++) {
reward.add(((Integer)this.RegularItemIdList.get(i)).intValue(), ((Integer)this.RegularCountList.get(i)).intValue() * times);
}

for (int index = 0; index < this.WeightList.size(); index++) {
Binomial binoimal = new Binomial(times, ((Integer)this.WeightList.get(index)).intValue() / 10000.0D);
int count = (int)binoimal.random();
reward.add(((Integer)this.ItemIdList.get(index)).intValue(), ((Integer)this.CountList.get(index)).intValue() * count);
} 
return reward;
}

public Reward genReward(int times, int max) {
Reward reward = new Reward();
int sum = 0;
while (times > 0) {
for (int i = 0; i < this.RegularItemIdList.size(); i++) {
int count = ((Integer)this.RegularCountList.get(i)).intValue();
if (sum + count >= max) {
return reward.add(((Integer)this.RegularItemIdList.get(i)).intValue(), max - sum);
}
reward.add(((Integer)this.RegularItemIdList.get(i)).intValue(), count);
sum += count;
} 

for (int index = 0; index < this.WeightList.size(); index++) {
if (Random.nextInt(10000) <= ((Integer)this.WeightList.get(index)).intValue()) {

int count = ((Integer)this.CountList.get(index)).intValue();
if (sum + count >= max) {
return reward.add(((Integer)this.ItemIdList.get(index)).intValue(), max - sum);
}
reward.add(((Integer)this.ItemIdList.get(index)).intValue(), count);
sum += count;
} 
} 
times--;
} 
return reward;
}

public boolean Assert() {
for (Iterator<Integer> iterator = this.WeightList.iterator(); iterator.hasNext(); ) { int w = ((Integer)iterator.next()).intValue();
if (w >= 10000 || w <= 0) {
CommLog.error("掉落权重只能在(0,10000)之间.");
return false;
}  }

RefAssert.inRef(this.RegularItemIdList, RefUniformItem.class, new Object[0]);

if (!RefAssert.listSize(this.ItemIdList, this.CountList, new List[] { this.WeightList })) {
return false;
}

if (!RefAssert.listSize(this.RegularItemIdList, this.RegularCountList, new List[0])) {
return false;
}

return true;
}

public boolean AssertAll(RefContainer<?> all) {
return true;
}

public static void main(String[] args) {
int repeat = 100000;

int times = 1;
double p = 0.1D;

Map<Double, Integer> rslt = new HashMap<>();

long begin = CommTime.nowMS();
Binomial binoimal = new Binomial(times, p);
int sum = 0;
for (int i = 0; i < repeat; i++) {

double count = binoimal.random();
Integer precount = rslt.get(Double.valueOf(count));
if (precount == null) {
rslt.put(Double.valueOf(count), Integer.valueOf(1));
} else {
rslt.put(Double.valueOf(count), Integer.valueOf(precount.intValue() + 1));
} 
sum = (int)(sum + count);
} 
System.out.println("time:" + (CommTime.nowMS() - begin));
System.out.println(rslt.toString());
System.out.println(String.valueOf(sum) + "/" + (repeat * times) + "=" + (1.0D * sum / (repeat * times)));
}
}

