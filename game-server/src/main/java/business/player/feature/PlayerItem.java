package business.player.feature;

import business.player.Player;
import business.player.item.IItemFilter;
import business.player.item.IUniItemContainer;
import business.player.item.ItemContainerTable;
import business.player.item.ItemUtils;
import business.player.item.Reward;
import business.player.item.UniformItem;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.logger.flow.ItemFlow;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefUniformItem;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PlayerItem extends Feature {
    public PlayerItem(Player data) {
        super(data);
    }

    @Override
    public void loadDB() {
    }

    public boolean check(int uniformID, int count) {
        return this.check(uniformID, count, null);
    }

    public boolean check(int uniformID, int count, IItemFilter<?> filter) {
        RefUniformItem ref = RefDataMgr.get(RefUniformItem.class, uniformID);
        return ref == null ? false : this.check(ref.Type, ref.ItemID, count, filter);
    }

    public boolean check(PrizeType type, int itemID, int count) {
        return this.check(type, itemID, count, null);
    }

    public boolean check(PrizeType type, int itemID, int count, IItemFilter<?> filter) {
        Class<? extends Feature> clazz = ItemContainerTable.getItemLogic(type);
        if (clazz != null) {
            IUniItemContainer iContainer = (IUniItemContainer) this.player.getFeature(clazz);
            return iContainer.check(itemID, count, filter);
        } else {
            return this.player.getFeature(PlayerCurrency.class).check(type, count);
        }
    }

    public boolean check(List<Integer> uniformIDList, List<Integer> countList) {
        for (int i = 0; i < uniformIDList.size() && i < countList.size(); i++) {
            int uniformid = uniformIDList.get(i);
            int count = countList.get(i);
            if (!this.check(uniformid, count)) {
                return false;
            }
        }

        return true;
    }

    public boolean check(List<Integer> uniformIDList, List<Integer> countList, IItemFilter<?>... filter) {
        Map<PrizeType, IItemFilter<?>> map = this.mapFilter(filter);

        for (int i = 0; i < uniformIDList.size() && i < countList.size(); i++) {
            int uniformid = uniformIDList.get(i);
            int count = countList.get(i);
            RefUniformItem ref = RefDataMgr.get(RefUniformItem.class, uniformid);
            if (ref == null) {
                return false;
            }

            if (!this.check(ref.Type, ref.ItemID, count, map.get(ref.Type))) {
                return false;
            }
        }

        return true;
    }

    public boolean check(List<UniformItem> list) {
        for (UniformItem uniformItem : list) {
            if (!this.check(uniformItem.getUniformId(), uniformItem.getCount())) {
                return false;
            }
        }

        return true;
    }

    public boolean check(List<UniformItem> list, IItemFilter<?>... filter) {
        Map<PrizeType, IItemFilter<?>> map = this.mapFilter(filter);

        for (UniformItem uniformItem : list) {
            int uniformid = uniformItem.getUniformId();
            int count = uniformItem.getCount();
            RefUniformItem ref = RefDataMgr.get(RefUniformItem.class, uniformid);
            if (ref == null) {
                return false;
            }

            if (!this.check(ref.Type, ref.ItemID, count, map.get(ref.Type))) {
                return false;
            }
        }

        return true;
    }

    public boolean check(Reward reward) {
        return this.check(reward.merge());
    }

    public boolean check(Reward reward, IItemFilter<?>... filter) {
        return this.check(reward.merge(), filter);
    }

    private void consumeLog(PrizeType type, int itemID, int count, ItemFlow reason) {
        RefUniformItem item = ItemUtils.getRefUniformItem(type, itemID);
        if (item != null) {
            ;
        }
    }

    public void consume(int uniformID, int count, ItemFlow reason) {
        this.consume(uniformID, count, reason, null);
    }

    public void consume(int uniformID, int count, ItemFlow reason, IItemFilter<?> filter) {
        RefUniformItem ref = RefDataMgr.get(RefUniformItem.class, uniformID);
        if (ref != null) {
            this.consume(ref.Type, ref.ItemID, count, reason, filter);
        }
    }

    public void consume(PrizeType type, int itemID, int count, ItemFlow reason) {
        this.consume(type, itemID, count, reason, null);
    }

    public void consume(PrizeType type, int itemID, int count, ItemFlow reason, IItemFilter<?> filter) {
        Class<? extends Feature> clazz = ItemContainerTable.getItemLogic(type);
        if (clazz != null) {
            IUniItemContainer iContainer = (IUniItemContainer) this.player.getFeature(clazz);
            iContainer.consume(itemID, count, reason, filter);
            this.consumeLog(type, itemID, count, reason);
        } else {
            this.player.getFeature(PlayerCurrency.class).consume(type, count, reason);
        }
    }

    public void consume(List<Integer> uniformIDList, List<Integer> countList, ItemFlow reason) {
        for (int i = 0; i < uniformIDList.size() && i < countList.size(); i++) {
            int uniformid = uniformIDList.get(i);
            int count = countList.get(i);
            this.consume(uniformid, count, reason);
        }
    }

    public void consume(List<Integer> uniformIDList, List<Integer> countList, ItemFlow reason, IItemFilter<?>... filter) {
        Map<PrizeType, IItemFilter<?>> map = this.mapFilter(filter);

        for (int i = 0; i < uniformIDList.size() && i < countList.size(); i++) {
            int uniformid = uniformIDList.get(i);
            int count = countList.get(i);
            RefUniformItem ref = RefDataMgr.get(RefUniformItem.class, uniformid);
            if (ref != null) {
                this.consume(ref.Type, ref.ItemID, count, reason, map.get(ref.Type));
            }
        }
    }

    public void consume(List<UniformItem> list, ItemFlow reason) {
        for (UniformItem uniformItem : list) {
            int uniformid = uniformItem.getUniformId();
            int count = uniformItem.getCount();
            this.consume(uniformid, count, reason);
        }
    }

    public void consume(List<UniformItem> list, ItemFlow reason, IItemFilter<?>... filter) {
        Map<PrizeType, IItemFilter<?>> map = this.mapFilter(filter);

        for (UniformItem uniformItem : list) {
            int uniformid = uniformItem.getUniformId();
            int count = uniformItem.getCount();
            RefUniformItem ref = RefDataMgr.get(RefUniformItem.class, uniformid);
            if (ref != null) {
                this.consume(ref.Type, ref.ItemID, count, reason, map.get(ref.Type));
            }
        }
    }

    public void consume(Reward reward, ItemFlow reason) {
        this.consume(reward.merge(), reason);
    }

    public void consume(Reward reward, ItemFlow reason, IItemFilter<?>... filter) {
        this.consume(reward.merge(), reason, filter);
    }

    public boolean checkAndConsume(int uniformID, int count, ItemFlow reason) {
        if (!this.check(uniformID, count)) {
            return false;
        } else {
            this.consume(uniformID, count, reason);
            return true;
        }
    }

    public boolean checkAndConsume(int uniformID, int count, ItemFlow reason, IItemFilter<?> filter) {
        if (!this.check(uniformID, count, filter)) {
            return false;
        } else {
            this.consume(uniformID, count, reason, filter);
            return true;
        }
    }

    public boolean checkAndConsume(PrizeType type, int itemID, int count, ItemFlow reason) {
        if (!this.check(type, itemID, count)) {
            return false;
        } else {
            this.consume(type, itemID, count, reason);
            return true;
        }
    }

    public boolean checkAndConsume(PrizeType type, int itemID, int count, ItemFlow reason, IItemFilter<?> filter) {
        if (!this.check(type, itemID, count, filter)) {
            return false;
        } else {
            this.consume(type, itemID, count, reason, filter);
            return true;
        }
    }

    public boolean checkAndConsume(List<Integer> uniformIDList, List<Integer> countList, ItemFlow reason) {
        if (!this.check(uniformIDList, countList)) {
            return false;
        } else {
            this.consume(uniformIDList, countList, reason);
            return true;
        }
    }

    public boolean checkAndConsume(List<Integer> uniformIDList, List<Integer> countList, ItemFlow reason, IItemFilter<?>... filter) {
        if (!this.check(uniformIDList, countList, filter)) {
            return false;
        } else {
            this.consume(uniformIDList, countList, reason, filter);
            return true;
        }
    }

    public boolean checkAndConsume(List<UniformItem> list, ItemFlow reason) {
        if (!this.check(list)) {
            return false;
        } else {
            this.consume(list, reason);
            return true;
        }
    }

    public boolean checkAndConsume(List<UniformItem> list, ItemFlow reason, IItemFilter<?>... filter) {
        if (!this.check(list, filter)) {
            return false;
        } else {
            this.consume(list, reason, filter);
            return true;
        }
    }

    public boolean checkAndConsume(Reward reward, ItemFlow reason) {
        List<UniformItem> list = reward.merge();
        if (!this.check(list)) {
            return false;
        } else {
            this.consume(list, reason);
            return true;
        }
    }

    public boolean checkAndConsume(Reward reward, ItemFlow reason, IItemFilter<?>... filter) {
        List<UniformItem> list = reward.merge();
        if (!this.check(list, filter)) {
            return false;
        } else {
            this.consume(list, reason, filter);
            return true;
        }
    }

    public Map<PrizeType, IItemFilter<?>> mapFilter(IItemFilter<?>... filter) {
        Map<PrizeType, IItemFilter<?>> map = new HashMap<>();

        for (IItemFilter<?> f : filter) {
            map.put(f.getType(), f);
        }

        return map;
    }

    public int gain(int uniformID, int count, ItemFlow reason) {
        RefUniformItem ref = RefDataMgr.get(RefUniformItem.class, uniformID);
        return ref == null ? 0 : this.gain(ref.Type, ref.ItemID, count, reason);
    }

    public int gain(PrizeType type, int itemID, int count, ItemFlow reason) {
        Class<? extends Feature> clazz = ItemContainerTable.getItemLogic(type);
        if (clazz != null) {
            IUniItemContainer<?> iContainer = (IUniItemContainer<?>) this.player.getFeature(clazz);
            return iContainer.gain(itemID, count, reason);
        } else {
            return this.player.getFeature(PlayerCurrency.class).gain(type, count, reason);
        }
    }

    public Reward gain(List<Integer> uniformIDList, List<Integer> countList, ItemFlow reason) {
        Reward rtn = new Reward();

        for (int i = 0; i < uniformIDList.size() && i < countList.size(); i++) {
            int gained = this.gain(uniformIDList.get(i), countList.get(i), reason);
            if (gained != 0) {
                rtn.add(new UniformItem(uniformIDList.get(i), gained));
            }
        }

        return rtn;
    }

    public Reward gain(Reward reward, ItemFlow reason) {
        Iterator<UniformItem> iterator = reward.iterator();

        while (iterator.hasNext()) {
            UniformItem item = iterator.next();
            int gained = this.gain(item.getUniformId(), item.getCount(), reason);
            if (gained == 0) {
                iterator.remove();
            } else {
                item.setCount(gained);
            }
        }

        return reward;
    }
}
