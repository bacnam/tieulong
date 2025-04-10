package business.player.item;

import com.zhonglian.server.common.enums.PrizeType;

public interface IItemFilter<Item> {
  PrizeType getType();

  boolean isFiltered(Item paramItem);
}

