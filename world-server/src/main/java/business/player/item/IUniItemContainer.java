package business.player.item;

import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.logger.flow.ItemFlow;

public interface IUniItemContainer<ItemType> {
    PrizeType getType();

    boolean check(int var1, int var2, IItemFilter<ItemType> var3);

    ItemType consume(int var1, int var2, ItemFlow var3, IItemFilter<ItemType> var4);

    int gain(int var1, int var2, ItemFlow var3);
}
