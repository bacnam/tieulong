package business.player.feature.store;

import business.global.store.Store;
import business.player.Player;
import com.zhonglian.server.common.enums.StoreType;

@Store({StoreType.ArenaStore, StoreType.NormalStore})
public class NormalStore
extends PlayerStore
{
public NormalStore(StoreType storeType, Player player) {
super(storeType, player);
}
}

