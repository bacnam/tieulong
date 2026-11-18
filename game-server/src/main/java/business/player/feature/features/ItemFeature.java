package business.player.feature.features;

import business.player.Player;
import business.player.feature.Feature;
import business.player.feature.PlayerItem;
import business.player.item.IItemFilter;
import business.player.item.IUniItemContainer;
import business.player.item.Reward;
import business.player.item.UniformItem;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.common.enums.ConstEnum.ResOpType;
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

public class ItemFeature extends Feature implements IUniItemContainer<ItemBO> {
    public final Map<Integer, ItemBO> itemMap = new HashMap<>();

    public ItemFeature(Player owner) {
        super(owner);
    }

    @Override
    public void loadDB() {
        for (ItemBO bo : BM.getBM(ItemBO.class).findAll("pid", this.player.getPid())) {
            this.itemMap.put(bo.getItemId(), bo);
        }
    }

    public ItemBO getItem(int itemId) {
        return this.itemMap.get(itemId);
    }

    public List<UniformItem> getAllItems() {
        List<UniformItem> list = new ArrayList<>();

        for (ItemBO i : this.itemMap.values()) {
            list.add(new UniformItem(PrizeType.Item, i.getItemId(), i.getCount()));
        }

        return list;
    }

    public void cacheItem(ItemBO item) {
        this.itemMap.put(item.getItemId(), item);
    }

    @Override
    public PrizeType getType() {
        return PrizeType.Item;
    }

    @Override
    public boolean check(int itemId, int count, IItemFilter<ItemBO> filter) {
        ItemBO bo = this.getItem(itemId);
        return bo != null && bo.getCount() >= count;
    }

    public ItemBO consume(int itemId, int count, ItemFlow reason, IItemFilter<ItemBO> filter) {
        if (count <= 0) {
            return null;
        } else {
            RefItem ref = RefDataMgr.get(RefItem.class, itemId);
            if (ref == null) {
                return null;
            } else {
                ItemBO bo = this.getItem(itemId);
                if (bo == null) {
                    return null;
                } else {
                    int beforeNum = bo.getCount();
                    int remain = bo.getCount() - count;
                    if (remain > 0) {
                        bo.saveCount(remain);
                    } else {
                        bo.setCount(0);
                        bo.del();
                        this.itemMap.remove(itemId);
                    }

                    this.player.pushProto("delItem", new UniformItem(this.getType(), itemId, count));
                    this.itemLog(itemId, reason.value(), -count, bo.getCount(), beforeNum, ResOpType.Lose);
                    return bo;
                }
            }
        }
    }

    @Override
    public int gain(int itemId, int count, ItemFlow reason) {
        RefItem refItem = RefDataMgr.get(RefItem.class, itemId);
        if (count > 0 && refItem != null) {
            ItemBO bo = this.getItem(itemId);
            int beforeNum = 0;
            if (bo == null) {
                bo = new ItemBO();
                bo.setPid(this.player.getPid());
                bo.setItemId(itemId);
                bo.setCount(count);
                bo.setGainTime(CommTime.nowSecond());
                bo.insert();
                this.itemMap.put(itemId, bo);
            } else {
                beforeNum = bo.getCount();
                int finalMaterial = Math.min(RefDataMgr.getFactor("Hero_Max_ItemMaterial", 999999999), beforeNum + count);
                bo.setCount(finalMaterial);
                bo.setGainTime(CommTime.nowSecond());
                bo.saveAll();
            }

            this.player.pushProto("addItem", new UniformItem(this.getType(), itemId, count));
            this.itemLog(itemId, reason.value(), count, bo.getCount(), beforeNum, ResOpType.Gain);
            if (refItem.CanUse && refItem.UseNow) {
                try {
                    this.doUseGenericItem(itemId, count);
                } catch (WSException var8) {
                    var8.printStackTrace();
                }
            }

            return count;
        } else {
            return 0;
        }
    }

    public Reward doUseGenericItem(int itemId, int count) throws WSException {
        RefItem refItem = RefDataMgr.get(RefItem.class, itemId);
        if (!this.check(itemId, count)) {
            throw new WSException(ErrorCode.NotEnough_UseItem, "物品:%s数量不足", itemId);
        } else {
            Reward reward = new Reward();

            for (int i = 0; i < count; i++) {
                for (Integer rewardID : refItem.RewardList) {
                    reward.combine(RefDataMgr.get(RefReward.class, rewardID).genReward());
                }
            }

            this.consume(itemId, count, ItemFlow.UseGenericItem);
            return this.player.getFeature(PlayerItem.class).gain(reward, ItemFlow.UseGenericItem);
        }
    }

    public void itemLog(int itemId, int reason, int crystal, int finalCrystal, int before, ResOpType opType) {
        FlowLogger.itemLog(
            this.player.getPid(), this.player.getVipLevel(), this.player.getLv(), itemId, reason, crystal, finalCrystal, before, opType.ordinal()
        );
    }
}
