package business.player.feature.store;

import business.player.Player;
import business.player.feature.Feature;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.enums.StoreType;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefStoreRefresh;
import core.database.game.bo.PlayerGoodsBO;
import core.database.game.bo.StoreRecordBO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreFeature
extends Feature {
private Map<StoreType, PlayerStore> stores;

public StoreFeature(Player player) {
super(player);

this.stores = new HashMap<>();
}

public void loadDB() {
List<PlayerGoodsBO> boList = BM.getBM(PlayerGoodsBO.class).findAll("pid", Long.valueOf(this.player.getPid()));
for (PlayerGoodsBO bo : boList) {
StoreType storeType = StoreType.values()[bo.getStoreType()];
PlayerStore playerStore = this.stores.get(storeType);
if (playerStore == null) {
playerStore = new PlayerStore(storeType, this.player);
this.stores.put(storeType, playerStore = new PlayerStore(storeType, this.player));
} 
playerStore.addGoods(bo);
} 
initRefresh();
}

public PlayerStore getOrCreate(StoreType storeType) {
PlayerStore playerStore = this.stores.get(storeType);
if (playerStore != null) {
return playerStore;
}
synchronized (this) {
playerStore = this.stores.get(storeType);
if (playerStore != null) {
return playerStore;
}
this.stores.put(storeType, playerStore = new PlayerStore(storeType, this.player));
} 
return playerStore;
}

public boolean doAddFreeRefreshTimes(List<Integer> typeIdList, int times) {
StoreRecordBO bo = ((StoreRecord)this.player.getFeature(StoreRecord.class)).getOrCreate();

typeIdList.forEach(typeId -> {
int maxTimes = ((Integer)RefStoreRefresh.FreeStoreTimes.get(StoreType.values()[typeId.intValue()])).intValue();
int nowTimes = paramStoreRecordBO.getFreeRefreshTimes(typeId.intValue()) + paramInt;
if (nowTimes > maxTimes) {
nowTimes = maxTimes;
}
paramStoreRecordBO.setFreeRefreshTimes(typeId.intValue(), nowTimes);
if (nowTimes >= maxTimes) {
paramStoreRecordBO.setFlushIcon(typeId.intValue(), 1);
}
});
bo.saveAll();

typeIdList.forEach(typeId -> this.player.pushProto("store.RefreshInfo", getOrCreate(StoreType.values()[typeId.intValue()]).refreshInfo()));

return true;
}

public boolean doAutoRefresh() {
try {
for (RefStoreRefresh ref : RefDataMgr.getAll(RefStoreRefresh.class).values()) {
if (!ref.AutoRefresh) {
continue;
}
StoreType storeType = ref.id;

PlayerStore playerStore = getOrCreate(storeType);

playerStore.doAutoRefresh();

((StoreRecord)this.player.getFeature(StoreRecord.class)).doAutoRefresh(storeType);

this.player.pushProto("store.RefreshInfo", playerStore.refreshInfo());

this.player.pushProto("store.GoodsList", playerStore.getGoodsList());
} 
} catch (Exception e) {

e.printStackTrace();
} 
return true;
}

public boolean initRefresh() {
for (RefStoreRefresh ref : RefDataMgr.getAll(RefStoreRefresh.class).values()) {
if (ref.AutoRefresh) {
continue;
}
StoreType storeType = ref.id;

PlayerStore playerStore = getOrCreate(storeType);

playerStore.doAutoRefresh();

((StoreRecord)this.player.getFeature(StoreRecord.class)).doAutoRefresh(storeType);

this.player.pushProto("store.RefreshInfo", playerStore.refreshInfo());

this.player.pushProto("store.GoodsList", playerStore.getGoodsList());
} 
return true;
}
}

