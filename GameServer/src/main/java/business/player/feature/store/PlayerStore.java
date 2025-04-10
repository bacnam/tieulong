package business.player.feature.store;

import business.global.activity.ActivityMgr;
import business.global.activity.detail.AllPeopleReward;
import business.player.Player;
import business.player.feature.PlayerItem;
import business.player.feature.achievement.AchievementFeature;
import business.player.item.Reward;
import com.zhonglian.server.common.enums.Achievement;
import com.zhonglian.server.common.enums.StoreType;
import com.zhonglian.server.common.utils.CommMath;
import com.zhonglian.server.common.utils.CommString;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.common.utils.Maps;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefGoodsInfo;
import core.config.refdata.ref.RefStore;
import core.config.refdata.ref.RefStoreRefresh;
import core.config.refdata.ref.RefVIP;
import core.database.game.bo.PlayerGoodsBO;
import core.network.proto.Store;
import core.network.proto.StoreRefresh;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PlayerStore
{
private StoreType storeType;
private Player player;
private List<PlayerGoodsBO> goodsList;

public PlayerStore(StoreType storeType, Player player) {
this.storeType = storeType;
this.player = player;
this.goodsList = new ArrayList<>();
}

public List<Store.Goods> getGoodsList() {
List<PlayerGoodsBO> removeList = (List<PlayerGoodsBO>)this.goodsList.stream().filter(b -> (RefDataMgr.get(RefGoodsInfo.class, Long.valueOf(b.getGoodsId())) == null))
.collect(Collectors.toList());
for (PlayerGoodsBO bo : removeList) {
delPlayerGoods(bo);
}
if (this.goodsList.isEmpty()) {
doAutoRefresh();
}

List<Store.Goods> goods = new ArrayList<>();
for (PlayerGoodsBO bo : this.goodsList) {
goods.add(new Store.Goods(bo));
}
return goods;
}

void addGoods(PlayerGoodsBO bo) {
this.goodsList.add(bo);
}

public boolean doAutoRefresh() {
List<PlayerGoodsBO> brushList = brushGoodsList();

List<PlayerGoodsBO> removeList = new ArrayList<>();
for (PlayerGoodsBO bo : this.goodsList) {
if (brushList.stream().filter(b -> (b.getStoreId() == paramPlayerGoodsBO1.getStoreId())).count() == 0L) {
removeList.add(bo);
}
} 
removeList.forEach(x -> delPlayerGoods(x));

Map<Integer, PlayerGoodsBO> remainGoods = Maps.list2Map(PlayerGoodsBO::getStoreId, this.goodsList);
for (PlayerGoodsBO form : brushList) {
PlayerGoodsBO to = remainGoods.get(Integer.valueOf(form.getStoreId()));
if (to == null) {
updatePlayerGoods(form, to = new PlayerGoodsBO());
continue;
} 
RefStore storeRef = (RefStore)RefDataMgr.get(RefStore.class, Integer.valueOf(to.getStoreId()));
if (storeRef != null && storeRef.IsDailyRefresh) {
updatePlayerGoods(form, to);
}
} 
return true;
}

public StoreRefresh refreshInfo() {
StoreType storeType = this.storeType;

StoreRecord storeRecordL = (StoreRecord)this.player.getFeature(StoreRecord.class);

StoreRefresh storeRefresh = new StoreRefresh();
storeRefresh.setStoreType(storeType);

int nextRefreshTime = nextRefreshTime();
storeRefresh.setNextRefreshTime(nextRefreshTime);
storeRefresh.setRemainsec(Math.max(nextRefreshTime - CommTime.nowSecond(), 0));

int freeTimes = storeRecordL.getFreeRefreshTimes(storeType);

storeRefresh.setFreeRefreshTimes(freeTimes);
storeRefresh.setPaidRefreshTimes(storeRecordL.getPaidRefreshTimes(storeType));

return storeRefresh;
}

public int nextRefreshTime() {
int time = CommTime.getTodayZeroClockS();
for (int i = 0; i < 8; i++) {
time += 10800;
if (time > CommTime.nowSecond()) {
return time;
}
} 
return CommTime.getTodayZeroClockS() + 86400;
}

public List<RefStore> newStoreRefList() {
return (List<RefStore>)RefStore.StoreByType.get(this.storeType);
}

public List<PlayerGoodsBO> brushGoodsList() {
List<PlayerGoodsBO> brushList = new ArrayList<>();

for (RefStore storeRef : newStoreRefList()) {
if (!storeRef.LevelRange.within(this.player.getLv())) {
continue;
}

int index = CommMath.getRandomIndexByRate(storeRef.GoodsWeightList);
if (index < 0 || index >= storeRef.GoodsIDList.size()) {
index = 0;
}
long goodsId = ((Long)storeRef.GoodsIDList.get(index)).longValue();

RefGoodsInfo goodsRef = (RefGoodsInfo)RefDataMgr.get(RefGoodsInfo.class, Long.valueOf(goodsId));
if (goodsRef == null) {
continue;
}

int amount = ((Integer)storeRef.GoodsCountList.get(index)).intValue();

PlayerGoodsBO bo = new PlayerGoodsBO();

bo.setPid(this.player.getPid());

bo.setGoodsId(goodsRef.ID);
bo.setStoreId(storeRef.ID);
bo.setStoreType(storeRef.StoreType.ordinal());

int costIndex = CommMath.getRandomIndexByRate(goodsRef.CostWeight);
bo.setCostUniformId(goodsRef.CostUniformId.get(costIndex));
bo.setPrice(goodsRef.Price.get(costIndex));
bo.setDiscount(goodsRef.Discount.get(costIndex));

bo.setAmount(amount);
bo.setBuyTimes(0);
bo.setTotalBuyTimes(storeRef.BuyLimit);
bo.setSoldout(false);

bo.setCreateTime(CommTime.nowSecond());

brushList.add(bo);
} 
return brushList;
}

public void updatePlayerGoods(PlayerGoodsBO form, PlayerGoodsBO to) {
to.setPid(form.getPid());
to.setGoodsId(form.getGoodsId());
to.setStoreId(form.getStoreId());
to.setStoreType(form.getStoreType());
to.setCostUniformId(form.getCostUniformId());
to.setPrice(form.getPrice());
to.setDiscount(form.getDiscount());
to.setAmount(form.getAmount());
to.setBuyTimes(form.getBuyTimes());
to.setTotalBuyTimes(form.getTotalBuyTimes());
to.setSoldout(form.getSoldout());
to.setCreateTime(form.getCreateTime());
if (to.getId() == 0L) {
to.insert();
this.goodsList.add(to);
} else {
to.saveAll();
} 
}

public boolean manualRefresh() throws WSException {
StoreType storeType = this.storeType;

StoreRecord storeRecordL = (StoreRecord)this.player.getFeature(StoreRecord.class);

if (storeRecordL.getFreeRefreshTimes(storeType) > 0) {
storeRecordL.doHandleRefresh(storeType, true);

}
else {

int paidRefreshTimes = storeRecordL.getPaidRefreshTimes(storeType);

int limitPaidTimes = ((RefVIP)RefDataMgr.getOrLast(RefVIP.class, Integer.valueOf(this.player.getPlayerBO().getVipLevel()))).DailyRefreshStoreTimes;
if (limitPaidTimes >= 0 && paidRefreshTimes >= limitPaidTimes) {
throw new WSException(ErrorCode.Store_RefreshFull, "商店类型:%s的刷新次数已满", new Object[] { storeType });
}

RefStoreRefresh storeRefresh = (RefStoreRefresh)RefDataMgr.get(RefStoreRefresh.class, storeType);
if (!((PlayerItem)this.player.getFeature(PlayerItem.class)).checkAndConsume(storeRefresh.UniformId, storeRefresh.Count, ItemFlow.RefreshStore)) {
throw new WSException(ErrorCode.NotEnough_Currency, "商店类型:%s的刷新代币不足", new Object[] { storeType });
}

storeRecordL.doHandleRefresh(storeType, false);
} 
return doAutoRefresh();
}

public Reward doBuyGoods(long sId, int buyTimes) throws WSException {
PlayerGoodsBO bo = null;
for (PlayerGoodsBO model : this.goodsList) {
if (model.getId() == sId) {
bo = model;
}
} 
if (bo == null) {
throw new WSException(ErrorCode.Goods_HasRefresh, "商品:%s已刷新,请重新获取", new Object[] { Long.valueOf(sId) });
}
if (bo.getSoldout()) {
throw new WSException(ErrorCode.Goods_Soldout, "商品:%s已售空", new Object[] { Long.valueOf(sId) });
}
if (bo.getTotalBuyTimes() > 0 && 
bo.getTotalBuyTimes() - bo.getBuyTimes() < buyTimes) {
throw new WSException(ErrorCode.Goods_NotEnough, "商品:%s的当前可购买次数不足:%s", new Object[] { Long.valueOf(sId), Integer.valueOf(buyTimes) });
}

return buyStoreGoods(bo, buyTimes);
}

public Reward buyStoreGoods(PlayerGoodsBO bo, int buyTimes) throws WSException {
RefGoodsInfo goodsRef = (RefGoodsInfo)RefDataMgr.get(RefGoodsInfo.class, Long.valueOf(bo.getGoodsId()));
if (goodsRef == null) {
throw new WSException(ErrorCode.NotFound_RefGoodsInfo, "商品资源不存在");
}

int count = bo.getAmount() * buyTimes;

List<Integer> costItem = CommString.getIntegerList(bo.getCostUniformId(), "&");
List<Integer> costCount = new ArrayList<>();
getTotalPrice(CommString.getDoubleList(bo.getDiscount(), "&"), count, goodsRef.DiscountType.ordinal()).stream().forEach(x -> paramList.add(Integer.valueOf(x.intValue())));

if (!((PlayerItem)this.player.getFeature(PlayerItem.class)).check(costItem, costCount)) {
throw new WSException(ErrorCode.Goods_PriceLess, "商品购买的价钱不足");
}

((PlayerItem)this.player.getFeature(PlayerItem.class)).consume(costItem, costCount, ItemFlow.BuyItem);

bo.saveBuyTimes(bo.getBuyTimes() + buyTimes);
if (bo.getTotalBuyTimes() > 0 && bo.getBuyTimes() >= bo.getTotalBuyTimes()) {
bo.saveSoldout(true);
}

Reward reward = new Reward(goodsRef.UniformID, count);

Reward rspns = ((PlayerItem)this.player.getFeature(PlayerItem.class)).gain(reward, ItemFlow.BuyItem);

this.player.pushProto("store.Buy", new Store.Goods(bo));

((StoreRecord)this.player.getFeature(StoreRecord.class)).doBuyGoods(this.storeType);

((AchievementFeature)this.player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.ShoppingTimes_M1);
((AchievementFeature)this.player.getFeature(AchievementFeature.class)).updateInc(Achievement.AchievementType.ShoppingTimes_M2);

if (this.storeType == StoreType.MysteryStore) {
((AllPeopleReward)ActivityMgr.getActivity(AllPeopleReward.class)).handlePlayerChange(buyTimes);
}

return rspns;
}

public List<Double> getTotalPrice(List<Double> discount, int count, int discountType) {
List<Double> costCount = (List<Double>)discount.stream().map(x -> Double.valueOf(x.doubleValue() * paramInt)).collect(Collectors.toList());
return costCount;
}

public void delPlayerGoods(PlayerGoodsBO bo) {
bo.del();
this.goodsList.remove(bo);
}
}

