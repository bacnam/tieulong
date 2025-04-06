package business.player.item;

import com.zhonglian.server.common.enums.PrizeType;

public interface IItemFilter<Item> {
  PrizeType getType();
  
  boolean isFiltered(Item paramItem);
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/player/item/IItemFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */