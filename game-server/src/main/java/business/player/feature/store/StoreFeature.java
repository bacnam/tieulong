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

public class StoreFeature extends Feature {
    private Map<StoreType, PlayerStore> stores = new HashMap<>();

    public StoreFeature(Player player) {
        super(player);
    }

    @Override
    public void loadDB() {
        for (PlayerGoodsBO bo : BM.getBM(PlayerGoodsBO.class).findAll("pid", this.player.getPid())) {
            StoreType storeType = StoreType.values()[bo.getStoreType()];
            PlayerStore playerStore = this.stores.get(storeType);
            if (playerStore == null) {
                new PlayerStore(storeType, this.player);
                this.stores.put(storeType, playerStore = new PlayerStore(storeType, this.player));
            }

            playerStore.addGoods(bo);
        }

        this.initRefresh();
    }

    public PlayerStore getOrCreate(StoreType storeType) {
        PlayerStore playerStore = this.stores.get(storeType);
        if (playerStore != null) {
            return playerStore;
        } else {
            synchronized (this) {
                playerStore = this.stores.get(storeType);
                if (playerStore != null) {
                    return playerStore;
                } else {
                    this.stores.put(storeType, playerStore = new PlayerStore(storeType, this.player));
                    return playerStore;
                }
            }
        }
    }

    public boolean doAddFreeRefreshTimes(List<Integer> typeIdList, int times) {
        StoreRecordBO bo = this.player.getFeature(StoreRecord.class).getOrCreate();
        typeIdList.forEach(typeId -> {
            int maxTimes = RefStoreRefresh.FreeStoreTimes.get(StoreType.values()[typeId]);
            int nowTimes = bo.getFreeRefreshTimes(typeId) + times;
            if (nowTimes > maxTimes) {
                nowTimes = maxTimes;
            }

            bo.setFreeRefreshTimes(typeId, nowTimes);
            if (nowTimes >= maxTimes) {
                bo.setFlushIcon(typeId, 1);
            }
        });
        bo.saveAll();
        typeIdList.forEach(typeId -> this.player.pushProto("store.RefreshInfo", this.getOrCreate(StoreType.values()[typeId]).refreshInfo()));
        return true;
    }

    public boolean doAutoRefresh() {
        try {
            for (RefStoreRefresh ref : RefDataMgr.getAll(RefStoreRefresh.class).values()) {
                if (ref.AutoRefresh) {
                    StoreType storeType = ref.id;
                    PlayerStore playerStore = this.getOrCreate(storeType);
                    playerStore.doAutoRefresh();
                    this.player.getFeature(StoreRecord.class).doAutoRefresh(storeType);
                    this.player.pushProto("store.RefreshInfo", playerStore.refreshInfo());
                    this.player.pushProto("store.GoodsList", playerStore.getGoodsList());
                }
            }
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return true;
    }

    public boolean initRefresh() {
        for (RefStoreRefresh ref : RefDataMgr.getAll(RefStoreRefresh.class).values()) {
            if (!ref.AutoRefresh) {
                StoreType storeType = ref.id;
                PlayerStore playerStore = this.getOrCreate(storeType);
                playerStore.doAutoRefresh();
                this.player.getFeature(StoreRecord.class).doAutoRefresh(storeType);
                this.player.pushProto("store.RefreshInfo", playerStore.refreshInfo());
                this.player.pushProto("store.GoodsList", playerStore.getGoodsList());
            }
        }

        return true;
    }
}
