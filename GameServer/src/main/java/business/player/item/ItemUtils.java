package business.player.item;

import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.enums.PrizeType;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefUniformItem;

import java.util.HashMap;
import java.util.Map;

public class ItemUtils {
    private static Map<PrizeType, Map<Integer, Integer>> uniformitems = new HashMap<>();

    static {
        RefContainer<RefUniformItem> container = RefDataMgr.getAll(RefUniformItem.class);
        for (RefUniformItem ref : container.values()) {
            Map<Integer, Integer> items = uniformitems.get(ref.Type);
            if (items == null) {
                items = new HashMap<>();
                uniformitems.put(ref.Type, items);
            }
            items.put(Integer.valueOf(ref.ItemID), Integer.valueOf(ref.id));
        }
    }

    public static int getUniformId(PrizeType type, int itemId) {
        Map<Integer, Integer> items = uniformitems.get(type);
        if (items == null) {
            return 0;
        }
        if (items.size() == 0) {
            return ((Integer) items.values().iterator().next()).intValue();
        }
        Integer item = items.get(Integer.valueOf(itemId));
        if (item == null) {
            return 0;
        }
        return item.intValue();
    }

    public static RefUniformItem getRefUniformItem(PrizeType type, int itemId) {
        Map<Integer, Integer> items = uniformitems.get(type);
        if (items == null || items.size() == 0) {
            return null;
        }
        Integer item = items.get(Integer.valueOf(itemId));
        if (item == null) {
            return null;
        }
        return (RefUniformItem) RefDataMgr.get(RefUniformItem.class, item);
    }
}

