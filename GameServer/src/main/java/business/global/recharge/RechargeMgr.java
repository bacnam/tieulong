package business.global.recharge;

import BaseCommon.CommLog;
import business.global.activity.ActivityMgr;
import business.global.activity.detail.*;
import business.player.Player;
import business.player.PlayerMgr;
import business.player.feature.AccountFeature;
import business.player.feature.PlayerCurrency;
import business.player.feature.achievement.AchievementFeature;
import business.player.feature.features.PlayerRecord;
import business.player.feature.features.RechargeFeature;
import business.player.feature.player.TitleFeature;
import business.player.item.Reward;
import com.zhonglian.server.common.Config;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.enums.*;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.logger.flow.ItemFlow;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefRecharge;
import core.database.game.bo.AccountBO;
import core.database.game.bo.PlayerRechargeRecordBO;
import core.database.game.bo.RechargeOrderBO;
import core.database.game.bo.RechargeResetBO;
import core.logger.flow.FlowLogger;
import core.logger.flow.TDRechargeLogger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RechargeMgr {
    private static RechargeMgr instance = new RechargeMgr();
    public Map<Long, RechargeOrderBO> cachedOrders;
    public Map<String, RechargeResetBO> resets;

    public static RechargeMgr getInstance() {
        return instance;
    }

    public boolean init() {
        List<RechargeOrderBO> paid = BM.getBM(RechargeOrderBO.class).findAll("status", OrderStatus.Paid.toString());
        this.cachedOrders = new HashMap<>();
        for (RechargeOrderBO o : paid) {
            this.cachedOrders.put(Long.valueOf(o.getPid()), o);
        }
        List<RechargeResetBO> resetbos = BM.getBM(RechargeResetBO.class).findAll();
        this.resets = new HashMap<>();
        for (RechargeResetBO o : resetbos) {
            this.resets.put(o.getGoodsid(), o);
        }
        return true;
    }

    public void cacheOrder(RechargeOrderBO orderBO) {
        this.cachedOrders.put(Long.valueOf(orderBO.getPid()), orderBO);
    }

    public void trySendCachedOrder(long cid) {
        if (!this.cachedOrders.containsKey(Long.valueOf(cid))) {
            return;
        }
        synchronized (this) {
            RechargeOrderBO orderBO = this.cachedOrders.remove(Long.valueOf(cid));
            if (orderBO != null) {
                sendPrize(orderBO);
            }
        }
    }

    public void sendPrize(RechargeOrderBO orderBO) {
        synchronized (this) {

            if (orderBO.getStatus().equals(OrderStatus.Delivered)) {
                return;
            }
            RefRecharge ref = (RefRecharge) RefDataMgr.get(RefRecharge.class, orderBO.getProductid());
            if (ref == null) {
                CommLog.error("订单[{}]充值失败，对应商品id[{}]无法查到", orderBO.getCporderid(), orderBO.getProductid());
                return;
            }
            Player player = PlayerMgr.getInstance().getPlayer(orderBO.getPid());
            if (player == null) {
                CommLog.error("玩家[{}]不存在或已注销", Long.valueOf(orderBO.getPid()));
                return;
            }
            RechargeFeature rechargeFeature = (RechargeFeature) player.getFeature(RechargeFeature.class);

            PlayerRechargeRecordBO record = rechargeFeature.getRecharged(ref.id);

            int crystal = ref.Crystal;

            ((NewFirstReward) ActivityMgr.getActivity(NewFirstReward.class)).setFirstReward(player);

            String resetsign = getResetSign(ref.id);
            boolean first = !(record != null && resetsign.equals(record.getResetSign()));

            if (first && ref.RebateAchievement == Achievement.AchievementType.None) {
                crystal = ref.First;
            }

            rechargeFeature.isRecharged();

            if (ref.RebateAchievement == Achievement.AchievementType.MonthCardCrystal || ref.RebateAchievement == Achievement.AchievementType.YearCardCrystal) {
                player.getPlayerBO().saveExtPackage(player.getPlayerBO().getExtPackage() + ref.AddPackage);
                player.pushProto("extPackage", Integer.valueOf(player.getPlayerBO().getExtPackage()));
            }
            if (ref.RebateAchievement == Achievement.AchievementType.MonthCardCrystal) {
                rechargeFeature.incRebateRemains(ref.RebateAchievement, ref.RebateDays);
                AchievementFeature.AchievementIns ins = ((AchievementFeature) player.getFeature(AchievementFeature.class)).getOrCreate(ref.RebateAchievement);
                if (ins.bo.getCompleteCount() != 0) {
                    ins.bo.saveCompleteCount(ins.bo.getCompleteCount() + ref.RebateDays);
                }
                ((AchievementFeature) player.getFeature(AchievementFeature.class)).checkMonthCard(Achievement.AchievementType.MonthCardCrystal);
            }
            if (ref.RebateAchievement == Achievement.AchievementType.YearCardCrystal) {
                rechargeFeature.incRebateRemains(ref.RebateAchievement, ref.RebateDays);
                ((AchievementFeature) player.getFeature(AchievementFeature.class)).checkMonthCard(Achievement.AchievementType.YearCardCrystal);
            }
            orderBO.saveStatus(OrderStatus.Delivered.toString());
            orderBO.saveDeliverTime(CommTime.nowSecond());

            rechargeFeature.recordRecharge(orderBO, resetsign);

            int gained = ((PlayerCurrency) player.getFeature(PlayerCurrency.class)).gain(PrizeType.Crystal, crystal * orderBO.getQuantity(), ItemFlow.Recharge);

            player.getPlayerBO().saveTotalRecharge(player.getPlayerBO().getTotalRecharge() + ref.Price);

            ((PlayerRecord) player.getFeature(PlayerRecord.class)).addValue(ConstEnum.DailyRefresh.DailyRecharge, ref.Price / 10);
            ((PlayerCurrency) player.getFeature(PlayerCurrency.class)).gain(PrizeType.VipExp, ref.VipExp, ItemFlow.Recharge);

            Player.RechargeNotify notify = new Player.RechargeNotify(new Reward(PrizeType.Crystal, gained), ref.id);

            player.pushProto("RechargeNotifyResult", notify);

            if (ref.RebateAchievement != Achievement.AchievementType.MonthCardCrystal && ref.RebateAchievement != Achievement.AchievementType.YearCardCrystal) {
                ((FirstRecharge) ActivityMgr.getActivity(FirstRecharge.class)).sendFirstRechargeReward(player);
            }

            ((DailyRecharge) ActivityMgr.getActivity(DailyRecharge.class)).handlePlayerChange(player, ref.Price / 10);

            ((AccumRecharge) ActivityMgr.getActivity(AccumRecharge.class)).handlePlayerChange(player, ref.Price / 10);

            ((AccumRechargeDay) ActivityMgr.getActivity(AccumRechargeDay.class)).handleRecharge(player);

            ((TitleFeature) player.getFeature(TitleFeature.class)).updateInc(Title.RechargeTo, Integer.valueOf(ref.Price / 10));

            ((RedPacket) ActivityMgr.getActivity(RedPacket.class)).handle(ref.Price / 10, player);

            ActivityMgr.updateWorldRank(player, (ref.Price / 10), RankType.WorldRecharge);

            ((FortuneWheel) ActivityMgr.getActivity(FortuneWheel.class)).handlePlayerChange(player, ref.Price / 10);

            rechargeLog(player, orderBO);
            TDrechargeLog(player, orderBO, gained);
        }
    }

    public String getResetSign(String id) {
        RechargeResetBO bo = this.resets.get(id);
        if (bo == null) {
            return "";
        }
        return bo.getResetSign();
    }

    public void reset(String goodsid) {
        RechargeResetBO bo = this.resets.get(goodsid);
        if (bo != null) {
            bo.saveResetSign(String.valueOf(CommTime.nowMS()) + "@server" + Config.ServerID());
        } else {
            bo = new RechargeResetBO();
            bo.setGoodsid(goodsid);
            bo.saveResetSign(String.valueOf(CommTime.nowMS()) + "@server" + Config.ServerID());
            bo.insert();
            this.resets.put(goodsid, bo);
        }
    }

    public void rechargeLog(Player player, RechargeOrderBO bo) {
        AccountBO account = ((AccountFeature) player.getFeature(AccountFeature.class)).getAccount();
        try {
            RefRecharge ref = (RefRecharge) RefDataMgr.get(RefRecharge.class, bo.getProductid());
            FlowLogger.rechargeLog(
                    player.getPid(),
                    player.getOpenId(),
                    player.getVipLevel(),
                    player.getLv(),
                    bo.getAdfromOrderid(),
                    bo.getCporderid(),
                    bo.getProductid(),
                    ref.Price,
                    ref.Crystal,
                    bo.getStatus(),
                    bo.getDeliverTime(),
                    CommTime.getTimeStringSYMD(bo.getDeliverTime()),
                    bo.getOrderTime(),
                    CommTime.getTimeStringSYMD(bo.getOrderTime()),
                    bo.getAdfrom(),
                    bo.getAdfrom2(),
                    account.getRegIp(),
                    account.getRegTime(),
                    CommTime.getTimeStringSYMD(account.getRegTime()),
                    account.getAdid());
        } catch (Exception exception) {
        }
    }

    public void TDrechargeLog(Player player, RechargeOrderBO bo, int gained) {
        try {
            String orderid = bo.getCporderid();
            if (orderid != null && !orderid.equals("") &&
                    orderid.contains("模拟")) {
                return;
            }

            RefRecharge ref = (RefRecharge) RefDataMgr.get(RefRecharge.class, bo.getProductid());
            TDRechargeLogger.getInstance().sendRechargeLog(String.valueOf(bo.getId()),
                    "success",
                    "h5",
                    String.valueOf(player.getPid()),
                    bo.getCporderid(), (
                            ref.Price / 10),
                    "CNY",
                    gained,
                    CommTime.nowSecond(),
                    ref.Title,
                    player.getLv());
        } catch (Exception exception) {
        }
    }

    public enum OrderStatus {
        Paid,
        Delivered,
        Cancled;
    }
}

