package business.player.feature.features;

import business.global.recharge.RechargeMgr;
import business.player.Player;
import business.player.feature.Feature;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.enums.Achievement;
import com.zhonglian.server.common.utils.CommTime;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefRecharge;
import core.database.game.bo.PlayerRechargeInfoBO;
import core.database.game.bo.PlayerRechargeRecordBO;
import core.database.game.bo.RechargeOrderBO;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RechargeFeature
        extends Feature {
    public Map<String, PlayerRechargeRecordBO> rechargeRecords;
    public PlayerRechargeInfoBO rechargeInfo;

    public RechargeFeature(Player owner) {
        super(owner);
    }

    public void loadDB() {
        List<PlayerRechargeRecordBO> recordBOs = BM.getBM(PlayerRechargeRecordBO.class).findAll("pid", Long.valueOf(this.player.getPid()));
        this.rechargeRecords = new HashMap<>();
        for (PlayerRechargeRecordBO r : recordBOs) {
            this.rechargeRecords.put(r.getGoodsID(), r);
        }

        this.rechargeInfo = (PlayerRechargeInfoBO) BM.getBM(PlayerRechargeInfoBO.class).findOne("pid", Long.valueOf(this.player.getPid()));
    }

    public PlayerRechargeRecordBO getRecharged(String goodsid) {
        return this.rechargeRecords.get(goodsid);
    }

    public Collection<PlayerRechargeRecordBO> getRecharged() {
        return this.rechargeRecords.values();
    }

    public boolean isRecharged() {
        for (RefRecharge ref : RefDataMgr.getAll(RefRecharge.class).values()) {
            if (ref.RebateAchievement == Achievement.AchievementType.MonthCardCrystal || ref.RebateAchievement == Achievement.AchievementType.YearCardCrystal) {
                continue;
            }
            PlayerRechargeRecordBO record = this.rechargeRecords.get(ref.id);
            if (record != null && record.getResetSign().equals(RechargeMgr.getInstance().getResetSign(ref.id))) {
                return true;
            }
        }

        return false;
    }

    public void recordRecharge(RechargeOrderBO orderBO, String resetsign) {
        PlayerRechargeRecordBO bo = this.rechargeRecords.get(orderBO.getProductid());
        if (bo != null) {
            bo.setBuyCount(bo.getBuyCount() + orderBO.getQuantity());
            bo.setLastBuyTime(CommTime.nowSecond());
            bo.saveResetSign(resetsign);
            bo.saveAll();
        } else {
            bo = new PlayerRechargeRecordBO();
            bo.setPid(this.player.getPid());
            bo.setGoodsID(orderBO.getProductid());
            bo.setBuyCount(bo.getBuyCount() + orderBO.getQuantity());
            bo.setLastBuyTime(CommTime.nowSecond());
            bo.setResetSign(resetsign);
            bo.insert();
            this.rechargeRecords.put(orderBO.getProductid(), bo);
        }
    }

    public int getRebateRemains(Achievement.AchievementType rebateAchievement) {
        if (this.rechargeInfo == null)
            return 0;
        if (rebateAchievement == Achievement.AchievementType.MonthCardCrystal)
            return this.rechargeInfo.getMonthCardRemains();
        if (rebateAchievement == Achievement.AchievementType.YearCardCrystal) {
            return this.rechargeInfo.getPermantCardRemains();
        }
        throw new UnsupportedOperationException("暂不支持月卡或至尊卡以外的月卡类型");
    }

    public void desRebateRemains(int day) {
        try {
            if (this.rechargeInfo == null) {
                return;
            }
            this.rechargeInfo.saveMonthCardRemains(Math.max(0, this.rechargeInfo.getMonthCardRemains() - day));
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void incRebateRemains(Achievement.AchievementType rebateAchievement, int rebateDays) {
        if (this.rechargeInfo == null) {
            synchronized (this) {
                if (this.rechargeInfo == null) {
                    this.rechargeInfo = new PlayerRechargeInfoBO();
                    this.rechargeInfo.setPid(this.player.getPid());
                    this.rechargeInfo.insert();
                }
            }
        }
        if (rebateAchievement == Achievement.AchievementType.MonthCardCrystal) {
            this.rechargeInfo.saveMonthCardRemains(this.rechargeInfo.getMonthCardRemains() + rebateDays);
        } else if (rebateAchievement == Achievement.AchievementType.YearCardCrystal) {
            this.rechargeInfo.savePermantCardRemains(rebateDays);
        } else {
            throw new UnsupportedOperationException("暂不支持月卡或至尊卡以外的月卡类型");
        }
    }
}

