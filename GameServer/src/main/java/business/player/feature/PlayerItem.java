package business.player.feature;

import business.player.Player;
import business.player.item.*;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.logger.flow.ItemFlow;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefUniformItem;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PlayerItem
        extends Feature {
    public PlayerItem(Player data) {
        super(data);
    }

    public void loadDB() {
    }

    public boolean check(int uniformID, int count) {
        return check(uniformID, count, (IItemFilter<?>) null);
    }

    public boolean check(int uniformID, int count, IItemFilter<?> filter) {
        RefUniformItem ref = (RefUniformItem) RefDataMgr.get(RefUniformItem.class, Integer.valueOf(uniformID));
        if (ref == null) {
            return false;
        }
        return check(ref.Type, ref.ItemID, count, filter);
    }

    public boolean check(PrizeType type, int itemID, int count) {
        return check(type, itemID, count, null);
    }

    public boolean check(PrizeType type, int itemID, int count, IItemFilter<?> filter) {
        Class<? extends Feature> clazz = ItemContainerTable.getItemLogic(type);
        if (clazz != null) {
            IUniItemContainer iContainer = (IUniItemContainer) this.player.getFeature(clazz);
            return iContainer.check(itemID, count, filter);
        }
        return ((PlayerCurrency) this.player.getFeature(PlayerCurrency.class)).check(type, count);
    }

    public boolean check(List<Integer> uniformIDList, List<Integer> countList) {
        for (int i = 0; i < uniformIDList.size() && i < countList.size(); i++) {
            int uniformid = ((Integer) uniformIDList.get(i)).intValue();
            int count = ((Integer) countList.get(i)).intValue();
            if (!check(uniformid, count)) {
                return false;
            }
        }
        return true;
    }

    public boolean check(List<Integer> uniformIDList, List<Integer> countList, IItemFilter... filter) {
        Map<PrizeType, IItemFilter<?>> map = mapFilter((IItemFilter<?>[]) filter);

        for (int i = 0; i < uniformIDList.size() && i < countList.size(); i++) {
            int uniformid = ((Integer) uniformIDList.get(i)).intValue();
            int count = ((Integer) countList.get(i)).intValue();
            RefUniformItem ref = (RefUniformItem) RefDataMgr.get(RefUniformItem.class, Integer.valueOf(uniformid));
            if (ref == null) {
                return false;
            }
            if (!check(ref.Type, ref.ItemID, count, map.get(ref.Type))) {
                return false;
            }
        }
        return true;
    }

    public boolean check(List<UniformItem> list) {
        for (UniformItem uniformItem : list) {
            if (!check(uniformItem.getUniformId(), uniformItem.getCount())) {
                return false;
            }
        }
        return true;
    }

    public boolean check(List<UniformItem> list, IItemFilter... filter) {
        Map<PrizeType, IItemFilter<?>> map = mapFilter((IItemFilter<?>[]) filter);

        for (UniformItem uniformItem : list) {
            int uniformid = uniformItem.getUniformId();
            int count = uniformItem.getCount();
            RefUniformItem ref = (RefUniformItem) RefDataMgr.get(RefUniformItem.class, Integer.valueOf(uniformid));
            if (ref == null) {
                return false;
            }
            if (!check(ref.Type, ref.ItemID, count, map.get(ref.Type))) {
                return false;
            }
        }
        return true;
    }

    public boolean check(Reward reward) {
        return check(reward.merge());
    }

    public boolean check(Reward reward, IItemFilter... filter) {
        return check(reward.merge(), (IItemFilter<?>[]) filter);
    }

    private void consumeLog(PrizeType type, int itemID, int count, ItemFlow reason) {
        RefUniformItem item = ItemUtils.getRefUniformItem(type, itemID);
        if (item == null) {
            return;
        }
    }

    public void consume(int uniformID, int count, ItemFlow reason) {
        consume(uniformID, count, reason, (IItemFilter<?>) null);
    }

    public void consume(int uniformID, int count, ItemFlow reason, IItemFilter<?> filter) {
        RefUniformItem ref = (RefUniformItem) RefDataMgr.get(RefUniformItem.class, Integer.valueOf(uniformID));
        if (ref == null) {
            return;
        }
        consume(ref.Type, ref.ItemID, count, reason, filter);
    }

    public void consume(PrizeType type, int itemID, int count, ItemFlow reason) {
        consume(type, itemID, count, reason, null);
    }

    public void consume(PrizeType type, int itemID, int count, ItemFlow reason, IItemFilter<?> filter) {
        Class<? extends Feature> clazz = ItemContainerTable.getItemLogic(type);
        if (clazz != null) {
            IUniItemContainer iContainer = (IUniItemContainer) this.player.getFeature(clazz);
            iContainer.consume(itemID, count, reason, filter);
            consumeLog(type, itemID, count, reason);
        } else {
            ((PlayerCurrency) this.player.getFeature(PlayerCurrency.class)).consume(type, count, reason);
        }
    }

    public void consume(List<Integer> uniformIDList, List<Integer> countList, ItemFlow reason) {
        for (int i = 0; i < uniformIDList.size() && i < countList.size(); i++) {
            int uniformid = ((Integer) uniformIDList.get(i)).intValue();
            int count = ((Integer) countList.get(i)).intValue();
            consume(uniformid, count, reason);
        }
    }

    public void consume(List<Integer> uniformIDList, List<Integer> countList, ItemFlow reason, IItemFilter... filter) {
        Map<PrizeType, IItemFilter<?>> map = mapFilter((IItemFilter<?>[]) filter);

        for (int i = 0; i < uniformIDList.size() && i < countList.size(); i++) {
            int uniformid = ((Integer) uniformIDList.get(i)).intValue();
            int count = ((Integer) countList.get(i)).intValue();
            RefUniformItem ref = (RefUniformItem) RefDataMgr.get(RefUniformItem.class, Integer.valueOf(uniformid));
            if (ref != null) {

                consume(ref.Type, ref.ItemID, count, reason, map.get(ref.Type));
            }
        }
    }

    public void consume(List<UniformItem> list, ItemFlow reason) {
        for (UniformItem uniformItem : list) {
            int uniformid = uniformItem.getUniformId();
            int count = uniformItem.getCount();
            consume(uniformid, count, reason);
        }
    }

    public void consume(List<UniformItem> list, ItemFlow reason, IItemFilter... filter) {
        Map<PrizeType, IItemFilter<?>> map = mapFilter((IItemFilter<?>[]) filter);

        for (UniformItem uniformItem : list) {
            int uniformid = uniformItem.getUniformId();
            int count = uniformItem.getCount();
            RefUniformItem ref = (RefUniformItem) RefDataMgr.get(RefUniformItem.class, Integer.valueOf(uniformid));
            if (ref == null) {
                continue;
            }
            consume(ref.Type, ref.ItemID, count, reason, map.get(ref.Type));
        }
    }

    public void consume(Reward reward, ItemFlow reason) {
        consume(reward.merge(), reason);
    }

    public void consume(Reward reward, ItemFlow reason, IItemFilter... filter) {
        consume(reward.merge(), reason, (IItemFilter<?>[]) filter);
    }

    public boolean checkAndConsume(int uniformID, int count, ItemFlow reason) {
        if (!check(uniformID, count)) {
            return false;
        }
        consume(uniformID, count, reason);
        return true;
    }

    public boolean checkAndConsume(int uniformID, int count, ItemFlow reason, IItemFilter<?> filter) {
        if (!check(uniformID, count, filter)) {
            return false;
        }
        consume(uniformID, count, reason, filter);
        return true;
    }

    public boolean checkAndConsume(PrizeType type, int itemID, int count, ItemFlow reason) {
        if (!check(type, itemID, count)) {
            return false;
        }
        consume(type, itemID, count, reason);
        return true;
    }

    public boolean checkAndConsume(PrizeType type, int itemID, int count, ItemFlow reason, IItemFilter<?> filter) {
        if (!check(type, itemID, count, filter)) {
            return false;
        }
        consume(type, itemID, count, reason, filter);
        return true;
    }

    public boolean checkAndConsume(List<Integer> uniformIDList, List<Integer> countList, ItemFlow reason) {
        if (!check(uniformIDList, countList)) {
            return false;
        }
        consume(uniformIDList, countList, reason);
        return true;
    }

    public boolean checkAndConsume(List<Integer> uniformIDList, List<Integer> countList, ItemFlow reason, IItemFilter... filter) {
        if (!check(uniformIDList, countList, (IItemFilter<?>[]) filter)) {
            return false;
        }
        consume(uniformIDList, countList, reason, (IItemFilter<?>[]) filter);
        return true;
    }

    public boolean checkAndConsume(List<UniformItem> list, ItemFlow reason) {
        if (!check(list)) {
            return false;
        }
        consume(list, reason);
        return true;
    }

    public boolean checkAndConsume(List<UniformItem> list, ItemFlow reason, IItemFilter... filter) {
        if (!check(list, (IItemFilter<?>[]) filter)) {
            return false;
        }
        consume(list, reason, (IItemFilter<?>[]) filter);
        return true;
    }

    public boolean checkAndConsume(Reward reward, ItemFlow reason) {
        List<UniformItem> list = reward.merge();
        if (!check(list)) {
            return false;
        }
        consume(list, reason);
        return true;
    }

    public boolean checkAndConsume(Reward reward, ItemFlow reason, IItemFilter... filter) {
        List<UniformItem> list = reward.merge();
        if (!check(list, (IItemFilter<?>[]) filter)) {
            return false;
        }
        consume(list, reason, (IItemFilter<?>[]) filter);
        return true;
    }

    public Map<PrizeType, IItemFilter<?>> mapFilter(IItemFilter... filter) {
        Map<PrizeType, IItemFilter<?>> map = new HashMap<>();
        byte b;
        int i;
        IItemFilter[] arrayOfIItemFilter;
        for (i = (arrayOfIItemFilter = filter).length, b = 0; b < i; ) {
            IItemFilter<?> f = arrayOfIItemFilter[b];
            map.put(f.getType(), f);
            b++;
        }

        return map;
    }

    public int gain(int uniformID, int count, ItemFlow reason) {
        RefUniformItem ref = (RefUniformItem) RefDataMgr.get(RefUniformItem.class, Integer.valueOf(uniformID));
        if (ref == null) {
            return 0;
        }
        return gain(ref.Type, ref.ItemID, count, reason);
    }

    public int gain(PrizeType type, int itemID, int count, ItemFlow reason) {
        Class<? extends Feature> clazz = ItemContainerTable.getItemLogic(type);
        if (clazz != null) {
            IUniItemContainer<?> iContainer = (IUniItemContainer) this.player.getFeature(clazz);
            return iContainer.gain(itemID, count, reason);
        }
        return ((PlayerCurrency) this.player.getFeature(PlayerCurrency.class)).gain(type, count, reason);
    }

    public Reward gain(List<Integer> uniformIDList, List<Integer> countList, ItemFlow reason) {
        Reward rtn = new Reward();
        for (int i = 0; i < uniformIDList.size() && i < countList.size(); i++) {
            int gained = gain(((Integer) uniformIDList.get(i)).intValue(), ((Integer) countList.get(i)).intValue(), reason);
            if (gained != 0) {
                rtn.add(new UniformItem(((Integer) uniformIDList.get(i)).intValue(), gained));
            }
        }
        return rtn;
    }

    public Reward gain(Reward reward, ItemFlow reason) {
        Iterator<UniformItem> iterator = reward.iterator();
        while (iterator.hasNext()) {
            UniformItem item = iterator.next();
            int gained = gain(item.getUniformId(), item.getCount(), reason);
            if (gained == 0) {
                iterator.remove();
                continue;
            }
            item.setCount(gained);
        }

        return reward;
    }
}

