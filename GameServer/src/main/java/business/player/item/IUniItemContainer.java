package business.player.item;

import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.logger.flow.ItemFlow;

public interface IUniItemContainer<ItemType>
{
PrizeType getType();

boolean check(int paramInt1, int paramInt2, IItemFilter<ItemType> paramIItemFilter);

default boolean check(int id, int count) {
return check(id, count, null);
}

ItemType consume(int paramInt1, int paramInt2, ItemFlow paramItemFlow, IItemFilter<ItemType> paramIItemFilter);

default ItemType consume(int id, int count, ItemFlow reason) {
return consume(id, count, reason, null);
}

int gain(int paramInt1, int paramInt2, ItemFlow paramItemFlow);
}

