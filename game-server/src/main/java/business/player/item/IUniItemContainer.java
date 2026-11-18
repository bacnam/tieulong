package business.player.item;

import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.logger.flow.ItemFlow;

public interface IUniItemContainer<ItemType> {
    PrizeType getType();

    boolean check(int var1, int var2, IItemFilter<ItemType> var3);

    default boolean check(int id, int count) {
        return this.check(id, count, null);
    }

    ItemType consume(int var1, int var2, ItemFlow var3, IItemFilter<ItemType> var4);

    default ItemType consume(int id, int count, ItemFlow reason) {
        return this.consume(id, count, reason, null);
    }

    int gain(int var1, int var2, ItemFlow var3);
}
