package business.player.feature.features;

import business.player.Player;
import business.player.feature.Feature;
import business.player.feature.PlayerItem;
import business.player.item.IItemFilter;
import business.player.item.IUniItemContainer;
import business.player.item.Reward;
import business.player.item.UniformItem;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.enums.ConstEnum;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefItem;
import core.config.refdata.ref.RefReward;
import core.database.game.bo.ItemBO;
import core.logger.flow.FlowLogger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemFeature
extends Feature
implements IUniItemContainer<ItemBO>
{
public final Map<Integer, ItemBO> itemMap = new HashMap<>();

public ItemFeature(Player owner) {
super(owner);
}

public void loadDB() {
List<ItemBO> boList = BM.getBM(ItemBO.class).findAll("pid", Long.valueOf(this.player.getPid()));
for (ItemBO bo : boList) {
this.itemMap.put(Integer.valueOf(bo.getItemId()), bo);
}
}

public ItemBO getItem(int itemId) {
return this.itemMap.get(Integer.valueOf(itemId));
}

public List<UniformItem> getAllItems() {
List<UniformItem> list = new ArrayList<>();
for (ItemBO i : this.itemMap.values()) {
list.add(new UniformItem(PrizeType.Item, i.getItemId(), i.getCount()));
}
return list;
}

public void cacheItem(ItemBO item) {
this.itemMap.put(Integer.valueOf(item.getItemId()), item);
}

public PrizeType getType() {
return PrizeType.Item;
}

public boolean check(int itemId, int count, IItemFilter<ItemBO> filter) {
ItemBO bo = getItem(itemId);
return (bo != null && bo.getCount() >= count);
}

public ItemBO consume(int itemId, int count, ItemFlow reason, IItemFilter<ItemBO> filter) {
if (count <= 0) {
return null;
}
RefItem ref = (RefItem)RefDataMgr.get(RefItem.class, Integer.valueOf(itemId));
if (ref == null) {
return null;
}
ItemBO bo = getItem(itemId);
if (bo == null) {
return null;
}
int beforeNum = bo.getCount();
int remain = bo.getCount() - count;
if (remain > 0) {
bo.saveCount(remain);
} else {
bo.setCount(0);
bo.del();
this.itemMap.remove(Integer.valueOf(itemId));
} 
this.player.pushProto("delItem", new UniformItem(getType(), itemId, count));
itemLog(itemId, reason.value(), -count, bo.getCount(), beforeNum, ConstEnum.ResOpType.Lose);
return bo;
}

public int gain(int itemId, int count, ItemFlow reason) {
RefItem refItem = (RefItem)RefDataMgr.get(RefItem.class, Integer.valueOf(itemId));

if (count <= 0 || refItem == null) {
return 0;
}
ItemBO bo = getItem(itemId);
int beforeNum = 0;
if (bo == null) {
bo = new ItemBO();
bo.setPid(this.player.getPid());
bo.setItemId(itemId);
bo.setCount(count);
bo.setGainTime(CommTime.nowSecond());
bo.insert();
this.itemMap.put(Integer.valueOf(itemId), bo);
} else {
beforeNum = bo.getCount();
int finalMaterial = Math.min(RefDataMgr.getFactor("Hero_Max_ItemMaterial", 999999999), beforeNum + count);
bo.setCount(finalMaterial);
bo.setGainTime(CommTime.nowSecond());
bo.saveAll();
} 
this.player.pushProto("addItem", new UniformItem(getType(), itemId, count));
itemLog(itemId, reason.value(), count, bo.getCount(), beforeNum, ConstEnum.ResOpType.Gain);

if (refItem.CanUse && refItem.UseNow) {
try {
doUseGenericItem(itemId, count);
} catch (WSException e) {

e.printStackTrace();
} 
}

return count;
}

public Reward doUseGenericItem(int itemId, int count) throws WSException {
RefItem refItem = (RefItem)RefDataMgr.get(RefItem.class, Integer.valueOf(itemId));
if (!check(itemId, count)) {
throw new WSException(ErrorCode.NotEnough_UseItem, "物品:%s数量不足", new Object[] { Integer.valueOf(itemId) });
}

Reward reward = new Reward();
for (int i = 0; i < count; i++) {
for (Integer rewardID : refItem.RewardList) {
reward.combine(((RefReward)RefDataMgr.get(RefReward.class, rewardID)).genReward());
}
} 

consume(itemId, count, ItemFlow.UseGenericItem);

Reward gain = ((PlayerItem)this.player.getFeature(PlayerItem.class)).gain(reward, ItemFlow.UseGenericItem);

return gain;
}

public void itemLog(int itemId, int reason, int crystal, int finalCrystal, int before, ConstEnum.ResOpType opType) {
FlowLogger.itemLog(
this.player.getPid(), 
this.player.getVipLevel(), 
this.player.getLv(), 
itemId, 
reason, 
crystal, 
finalCrystal, 
before, 
opType.ordinal());
}
}

