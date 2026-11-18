package business.player.feature.store;

import business.global.activity.ActivityMgr;
import business.global.activity.detail.AllPeopleReward;
import business.player.Player;
import business.player.feature.PlayerItem;
import business.player.feature.achievement.AchievementFeature;
import business.player.item.Reward;
import com.zhonglian.server.common.enums.StoreType;
import com.zhonglian.server.common.enums.Achievement.AchievementType;
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

public class PlayerStore {
    private StoreType storeType;
    private Player player;
    private List<PlayerGoodsBO> goodsList;

    public PlayerStore(StoreType storeType, Player player) {
        this.storeType = storeType;
        this.player = player;
        this.goodsList = new ArrayList<>();
    }

    public List<Store.Goods> getGoodsList() {
        for (PlayerGoodsBO bo : this.goodsList.stream().filter(b -> RefDataMgr.get(RefGoodsInfo.class, b.getGoodsId()) == null).collect(Collectors.toList())) {
            this.delPlayerGoods(bo);
        }

        if (this.goodsList.isEmpty()) {
            this.doAutoRefresh();
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
        List<PlayerGoodsBO> brushList = this.brushGoodsList();
        List<PlayerGoodsBO> removeList = new ArrayList<>();

        for (PlayerGoodsBO bo : this.goodsList) {
            if (brushList.stream().filter(b -> b.getStoreId() == bo.getStoreId()).count() == 0L) {
                removeList.add(bo);
            }
        }

        removeList.forEach(x -> this.delPlayerGoods(x));
        Map<Integer, PlayerGoodsBO> remainGoods = Maps.list2Map(PlayerGoodsBO::getStoreId, this.goodsList);

        for (PlayerGoodsBO form : brushList) {
            PlayerGoodsBO to = remainGoods.get(form.getStoreId());
            if (to == null) {
                this.updatePlayerGoods(form, new PlayerGoodsBO());
            } else {
                RefStore storeRef = RefDataMgr.get(RefStore.class, to.getStoreId());
                if (storeRef != null && storeRef.IsDailyRefresh) {
                    this.updatePlayerGoods(form, to);
                }
            }
        }

        return true;
    }

    public StoreRefresh refreshInfo() {
        StoreType storeType = this.storeType;
        StoreRecord storeRecordL = this.player.getFeature(StoreRecord.class);
        StoreRefresh storeRefresh = new StoreRefresh();
        storeRefresh.setStoreType(storeType);
        int nextRefreshTime = this.nextRefreshTime();
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
        return RefStore.StoreByType.get(this.storeType);
    }

    public List<PlayerGoodsBO> brushGoodsList() {
        List<PlayerGoodsBO> brushList = new ArrayList<>();

        for (RefStore storeRef : this.newStoreRefList()) {
            if (storeRef.LevelRange.within(this.player.getLv())) {
                int index = CommMath.getRandomIndexByRate(storeRef.GoodsWeightList);
                if (index < 0 || index >= storeRef.GoodsIDList.size()) {
                    index = 0;
                }

                long goodsId = storeRef.GoodsIDList.get(index);
                RefGoodsInfo goodsRef = RefDataMgr.get(RefGoodsInfo.class, goodsId);
                if (goodsRef != null) {
                    int amount = storeRef.GoodsCountList.get(index);
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
            }
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
        StoreRecord storeRecordL = this.player.getFeature(StoreRecord.class);
        if (storeRecordL.getFreeRefreshTimes(storeType) > 0) {
            storeRecordL.doHandleRefresh(storeType, true);
        } else {
            int paidRefreshTimes = storeRecordL.getPaidRefreshTimes(storeType);
            int limitPaidTimes = RefDataMgr.getOrLast(RefVIP.class, this.player.getPlayerBO().getVipLevel()).DailyRefreshStoreTimes;
            if (limitPaidTimes >= 0 && paidRefreshTimes >= limitPaidTimes) {
                throw new WSException(ErrorCode.Store_RefreshFull, "商店类型:%s的刷新次数已满", storeType);
            }

            RefStoreRefresh storeRefresh = RefDataMgr.get(RefStoreRefresh.class, storeType);
            if (!this.player.getFeature(PlayerItem.class).checkAndConsume(storeRefresh.UniformId, storeRefresh.Count, ItemFlow.RefreshStore)) {
                throw new WSException(ErrorCode.NotEnough_Currency, "商店类型:%s的刷新代币不足", storeType);
            }

            storeRecordL.doHandleRefresh(storeType, false);
        }

        return this.doAutoRefresh();
    }

    public Reward doBuyGoods(long sId, int buyTimes) throws WSException {
        PlayerGoodsBO bo = null;

        for (PlayerGoodsBO model : this.goodsList) {
            if (model.getId() == sId) {
                bo = model;
            }
        }

        if (bo == null) {
            throw new WSException(ErrorCode.Goods_HasRefresh, "商品:%s已刷新,请重新获取", sId);
        } else if (bo.getSoldout()) {
            throw new WSException(ErrorCode.Goods_Soldout, "商品:%s已售空", sId);
        } else if (bo.getTotalBuyTimes() > 0 && bo.getTotalBuyTimes() - bo.getBuyTimes() < buyTimes) {
            throw new WSException(ErrorCode.Goods_NotEnough, "商品:%s的当前可购买次数不足:%s", sId, buyTimes);
        } else {
            return this.buyStoreGoods(bo, buyTimes);
        }
    }

    public Reward buyStoreGoods(PlayerGoodsBO bo, int buyTimes) throws WSException {
        RefGoodsInfo goodsRef = RefDataMgr.get(RefGoodsInfo.class, bo.getGoodsId());
        if (goodsRef == null) {
            throw new WSException(ErrorCode.NotFound_RefGoodsInfo, "商品资源不存在");
        } else {
            int count = bo.getAmount() * buyTimes;
            List<Integer> costItem = CommString.getIntegerList(bo.getCostUniformId(), "&");
            List<Integer> costCount = new ArrayList<>();
            this.getTotalPrice(CommString.getDoubleList(bo.getDiscount(), "&"), count, goodsRef.DiscountType.ordinal())
                .stream()
                .forEach(x -> costCount.add(x.intValue()));
            if (!this.player.getFeature(PlayerItem.class).check(costItem, costCount)) {
                throw new WSException(ErrorCode.Goods_PriceLess, "商品购买的价钱不足");
            } else {
                this.player.getFeature(PlayerItem.class).consume(costItem, costCount, ItemFlow.BuyItem);
                bo.saveBuyTimes(bo.getBuyTimes() + buyTimes);
                if (bo.getTotalBuyTimes() > 0 && bo.getBuyTimes() >= bo.getTotalBuyTimes()) {
                    bo.saveSoldout(true);
                }

                Reward reward = new Reward(goodsRef.UniformID, count);
                Reward rspns = this.player.getFeature(PlayerItem.class).gain(reward, ItemFlow.BuyItem);
                this.player.pushProto("store.Buy", new Store.Goods(bo));
                this.player.getFeature(StoreRecord.class).doBuyGoods(this.storeType);
                this.player.getFeature(AchievementFeature.class).updateInc(AchievementType.ShoppingTimes_M1);
                this.player.getFeature(AchievementFeature.class).updateInc(AchievementType.ShoppingTimes_M2);
                if (this.storeType == StoreType.MysteryStore) {
                    ActivityMgr.getActivity(AllPeopleReward.class).handlePlayerChange(buyTimes);
                }

                return rspns;
            }
        }
    }

    public List<Double> getTotalPrice(List<Double> discount, int count, int discountType) {
        return discount.stream().map(x -> x * count).collect(Collectors.toList());
    }

    public void delPlayerGoods(PlayerGoodsBO bo) {
        bo.del();
        this.goodsList.remove(bo);
    }
}
