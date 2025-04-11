package business.player.feature.features;

import business.player.Player;
import business.player.feature.Feature;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.enums.ConstEnum;
import core.database.game.bo.PlayerRecordBO;

public class PlayerRecord
        extends Feature {
    private PlayerRecordBO bo;

    public PlayerRecord(Player owner) {
        super(owner);
    }

    public void loadDB() {
        this.bo = (PlayerRecordBO) BM.getBM(PlayerRecordBO.class).findOne("pid", Long.valueOf(this.player.getPid()));
    }

    public PlayerRecordBO getOrCreate() {
        PlayerRecordBO bo = this.bo;
        if (bo != null) {
            return bo;
        }
        synchronized (this) {
            bo = new PlayerRecordBO();
            bo.setPid(this.player.getPid());
            bo.insert();
            this.bo = bo;
        }
        return bo;
    }

    public synchronized int getValue(ConstEnum.DailyRefresh index) {
        if (index == ConstEnum.DailyRefresh.None) {
            return -1;
        }
        PlayerRecordBO dr = getOrCreate();
        return dr.getValue(index.ordinal());
    }

    public void addValue(ConstEnum.DailyRefresh index) {
        addValue(index, 1);
    }

    public synchronized void addValue(ConstEnum.DailyRefresh index, int value) {
        if (index == ConstEnum.DailyRefresh.None) {
            return;
        }
        setValue(index, getValue(index) + value);
    }

    public synchronized void setValue(ConstEnum.DailyRefresh index, int value) {
        if (value < 0) {
            return;
        }
        PlayerRecordBO record = getOrCreate();

        record.saveValue(index.ordinal(), value);

        notifyValue(record, index.ordinal(), value);
    }

    public synchronized void clearValue8() {
    }

    public synchronized void clearValue0() {
        try {
            PlayerRecordBO record = getOrCreate();
            byte b;
            int i;
            ConstEnum.DailyRefresh[] arrayOfDailyRefresh;
            for (i = (arrayOfDailyRefresh = ConstEnum.DailyRefresh.values()).length, b = 0; b < i; ) {
                ConstEnum.DailyRefresh dr = arrayOfDailyRefresh[b];
                switch (dr) {
                    case PackageBuyTimes:
                        break;
                    default:
                        record.setValue(dr.ordinal(), 0);
                        break;
                }
                b++;
            }

            record.saveAll();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void notifyValue(PlayerRecordBO dr, int index, int value) {
        this.player.pushProto("refreshDailyValue", String.valueOf(index) + ";" + value);
    }
}

