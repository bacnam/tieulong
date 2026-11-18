package business.player.feature.store;

import business.player.Player;
import business.player.feature.Feature;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.enums.StoreType;
import com.zhonglian.server.common.utils.CommTime;
import core.config.refdata.ref.RefStoreRefresh;
import core.database.game.bo.StoreRecordBO;

public class StoreRecord extends Feature {
    public StoreRecordBO storeRecord;

    public StoreRecord(Player owner) {
        super(owner);
    }

    @Override
    public void loadDB() {
        this.storeRecord = BM.getBM(StoreRecordBO.class).findOne("pid", this.player.getPid());
    }

    public StoreRecordBO getOrCreate() {
        StoreRecordBO record = this.storeRecord;
        if (record != null) {
            return record;
        } else {
            synchronized (this) {
                record = this.storeRecord;
                if (record != null) {
                    return record;
                } else {
                    record = new StoreRecordBO();
                    record.setPid(this.player.getPid());

                    StoreType[] var6;
                    for (StoreType storeType : var6 = StoreType.values()) {
                        record.setFreeRefreshTimes(storeType.ordinal(), RefStoreRefresh.FreeStoreTimes.get(storeType));
                    }

                    record.insert();
                    this.storeRecord = record;
                    return record;
                }
            }
        }
    }

    public void dailyRefresh() {
        try {
            StoreRecordBO record = this.getOrCreate();
            record.setPaidRefreshTimesAll(0);
            record.setBuyTimesAll(0);

            StoreType[] var5;
            for (StoreType storeType : var5 = StoreType.values()) {
                record.setFreeRefreshTimes(storeType.ordinal(), RefStoreRefresh.FreeStoreTimes.get(storeType));
            }

            record.saveAll();
        } catch (Exception var6) {
            var6.printStackTrace();
        }
    }

    public int getFreeRefreshTimes(StoreType storeType) {
        return this.getOrCreate().getFreeRefreshTimes(storeType.ordinal());
    }

    public int getPaidRefreshTimes(StoreType storeType) {
        return this.getOrCreate().getPaidRefreshTimes(storeType.ordinal());
    }

    public int getBuyTimes(StoreType storeType) {
        return this.getOrCreate().getBuyTimes(storeType.ordinal());
    }

    public int getFlushIconByIndex(StoreType storeType) {
        return this.getOrCreate().getFlushIcon(storeType.ordinal());
    }

    public void doAutoRefresh(StoreType storeType) {
        int index = storeType.ordinal();
        if (index > StoreType.None.ordinal()) {
            StoreRecordBO record = this.getOrCreate();
            record.saveFlushIcon(index, 1);
            record.saveLastRefreshTime(index, CommTime.nowSecond());
        }
    }

    public void doHandleRefresh(StoreType storeType, boolean useFreeRefresh) {
        int index = storeType.ordinal();
        if (index > StoreType.None.ordinal()) {
            StoreRecordBO record = this.getOrCreate();
            if (useFreeRefresh) {
                record.saveFreeRefreshTimes(index, this.getFreeRefreshTimes(storeType) - 1);
            } else {
                record.savePaidRefreshTimes(index, this.getPaidRefreshTimes(storeType) + 1);
            }

            record.saveLastRefreshTime(index, CommTime.nowSecond());
        }
    }

    public void doBuyGoods(StoreType storeType) {
        int index = storeType.ordinal();
        if (index > StoreType.None.ordinal()) {
            StoreRecordBO record = this.getOrCreate();
            record.saveBuyTimes(index, this.getBuyTimes(storeType) + 1);
        }
    }

    public void doSaveFlushIcon(StoreType storeType, int value) {
        this.getOrCreate().saveFlushIcon(storeType.ordinal(), value);
    }
}
