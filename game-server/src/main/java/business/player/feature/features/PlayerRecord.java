package business.player.feature.features;

import business.player.Player;
import business.player.feature.Feature;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.enums.ConstEnum.DailyRefresh;
import core.database.game.bo.PlayerRecordBO;

public class PlayerRecord extends Feature {
    private PlayerRecordBO bo;

    public PlayerRecord(Player owner) {
        super(owner);
    }

    @Override
    public void loadDB() {
        this.bo = BM.getBM(PlayerRecordBO.class).findOne("pid", this.player.getPid());
    }

    public PlayerRecordBO getOrCreate() {
        PlayerRecordBO bo = this.bo;
        if (bo != null) {
            return bo;
        } else {
            synchronized (this) {
                bo = new PlayerRecordBO();
                bo.setPid(this.player.getPid());
                bo.insert();
                this.bo = bo;
                return bo;
            }
        }
    }

    public synchronized int getValue(DailyRefresh index) {
        if (index == DailyRefresh.None) {
            return -1;
        } else {
            PlayerRecordBO dr = this.getOrCreate();
            return dr.getValue(index.ordinal());
        }
    }

    public void addValue(DailyRefresh index) {
        this.addValue(index, 1);
    }

    public synchronized void addValue(DailyRefresh index, int value) {
        if (index != DailyRefresh.None) {
            this.setValue(index, this.getValue(index) + value);
        }
    }

    public synchronized void setValue(DailyRefresh index, int value) {
        if (value >= 0) {
            PlayerRecordBO record = this.getOrCreate();
            record.saveValue(index.ordinal(), value);
            this.notifyValue(record, index.ordinal(), value);
        }
    }

    public synchronized void clearValue8() {
    }

    public synchronized void clearValue0() {
        try {
            PlayerRecordBO record = this.getOrCreate();

            DailyRefresh[] var5;
            for (DailyRefresh dr : var5 = DailyRefresh.values()) {
                switch (dr) {
                    default:
                        record.setValue(dr.ordinal(), 0);
                    case PackageBuyTimes:
                }
            }

            record.saveAll();
        } catch (Exception var6) {
            var6.printStackTrace();
        }
    }

    public void notifyValue(PlayerRecordBO dr, int index, int value) {
        this.player.pushProto("refreshDailyValue", index + ";" + value);
    }
}
