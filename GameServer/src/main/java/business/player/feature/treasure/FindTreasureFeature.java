package business.player.feature.treasure;

import business.global.activity.ActivityMgr;
import business.global.notice.NoticeMgr;
import business.player.Player;
import business.player.feature.Feature;
import business.player.item.Reward;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.enums.ConstEnum;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.common.enums.Quality;
import com.zhonglian.server.common.enums.RankType;
import com.zhonglian.server.common.utils.CommMath;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefTreasure;
import core.config.refdata.ref.RefUniformItem;
import core.database.game.bo.PlayerFindTreasureBO;
import java.util.ArrayList;

public class FindTreasureFeature
extends Feature
{
public PlayerFindTreasureBO findTreasure;

public FindTreasureFeature(Player player) {
super(player);
}

public void loadDB() {
this.findTreasure = (PlayerFindTreasureBO)BM.getBM(PlayerFindTreasureBO.class).findOne("pid", Long.valueOf(this.player.getPid()));
}

public PlayerFindTreasureBO getOrCreate() {
PlayerFindTreasureBO bo = this.findTreasure;
if (bo != null) {
return bo;
}
synchronized (this) {
bo = this.findTreasure;
if (bo != null) {
return bo;
}
bo = new PlayerFindTreasureBO();
bo.setPid(this.player.getPid());
bo.setTotal(0);
bo.insert();
this.findTreasure = bo;
} 
return bo;
}

public RefTreasure selectRef(int level) {
for (RefTreasure ref : RefDataMgr.getAll(RefTreasure.class).values()) {
if (ref.LevelRange.within(level)) {
return ref;
}
} 
return null;
}

public Reward findTen() {
PlayerFindTreasureBO findTreasure = getOrCreate();
findTreasure.saveTentimes(this.findTreasure.getTentimes() + 1);
RefTreasure ref = selectRef(this.player.getLv());

ArrayList<Integer> weightList = ref.FixedTenWeightList;
ArrayList<Integer> idList = ref.FixedTenIdList;
ArrayList<Integer> countList = ref.FixedTenCountList;
Reward reward = null;
for (int i = 0; i < 9; i++) {
int j = CommMath.getRandomIndexByRate(weightList);
int k = ((Integer)idList.get(j)).intValue();
int m = ((Integer)countList.get(j)).intValue();
if (reward != null) {
reward.add(k, m);
} else {
reward = new Reward(k, m);
} 

RefUniformItem refUniformItem = (RefUniformItem)RefDataMgr.get(RefUniformItem.class, Integer.valueOf(k));
if (refUniformItem.Quality == Quality.Red.ordinal() && refUniformItem.Type == PrizeType.Equip) {
NoticeMgr.getInstance().sendMarquee(ConstEnum.UniverseMessageType.FindTreasure, new String[] { this.player.getName(), refUniformItem.Name });
}
} 

int index = CommMath.getRandomIndexByRate(ref.LeastTenWeightList);
int uniformID = ((Integer)ref.LeastIdList.get(index)).intValue();
int count = ((Integer)ref.LeastTenCountList.get(index)).intValue();

RefUniformItem refUniform = (RefUniformItem)RefDataMgr.get(RefUniformItem.class, Integer.valueOf(uniformID));
if (refUniform.Quality == Quality.Red.ordinal() && refUniform.Type == PrizeType.Equip) {
NoticeMgr.getInstance().sendMarquee(ConstEnum.UniverseMessageType.FindTreasure, new String[] { this.player.getName(), refUniform.Name });
}
reward.add(uniformID, count);
reward.combine(getExtReward(10));

return reward;
}

public Reward find() {
ArrayList<Integer> weightList, idList, countList;
PlayerFindTreasureBO findTreasure = getOrCreate();
findTreasure.saveTimes(this.findTreasure.getTimes() + 1);

RefTreasure ref = selectRef(this.player.getLv());

int total = 0;
total = getOrCreate().getTotal();

if (total >= 9) {
weightList = ref.LeastTenWeightList;
idList = ref.LeastIdList;
countList = ref.LeastTenCountList;
} else {
weightList = ref.NormalWeightList;
idList = ref.NormalIdList;
countList = ref.NormalCountList;
} 
int index = CommMath.getRandomIndexByRate(weightList);
int uniformID = ((Integer)idList.get(index)).intValue();
int count = ((Integer)countList.get(index)).intValue();
getOrCreate().saveTotal(total + 1);
if (getOrCreate().getTotal() == 10) {
getOrCreate().saveTotal(0);
}
Reward reward = new Reward(uniformID, count);
reward.combine(getExtReward(1));

RefUniformItem refUniform = (RefUniformItem)RefDataMgr.get(RefUniformItem.class, Integer.valueOf(uniformID));
if (refUniform.Quality == Quality.Red.ordinal() && refUniform.Type == PrizeType.Equip) {
NoticeMgr.getInstance().sendMarquee(ConstEnum.UniverseMessageType.FindTreasure, new String[] { this.player.getName(), refUniform.Name });
}

return reward;
}

public Reward findTreasure(int times) {
ActivityMgr.updateWorldRank(this.player, times, RankType.WorldTreasure);

if (times == 1) {
return find();
}
if (times == 10) {
return findTen();
}

return null;
}

public Reward getExtReward(int times) {
RefTreasure ref = selectRef(this.player.getLv());
Reward reward = new Reward();
for (int i = 0; i < times; i++) {
reward.add(ref.UniformId, ref.UniformCount);
}
return reward;
}

public int getLeftTimes(ConstEnum.FindTreasureType find) {
if (find == ConstEnum.FindTreasureType.single) {
int times = RefDataMgr.getFactor("findTreasureTimes", 20);
if (times < 0) {
return times;
}
return times - getOrCreate().getTimes();
} 
if (find == ConstEnum.FindTreasureType.Ten) {
int times = RefDataMgr.getFactor("findTreasureTentimes", 2);
if (times < 0) {
return times;
}
return RefDataMgr.getFactor("findTreasureTentimes", 2) - getOrCreate().getTentimes();
} 

return 0;
}
}

