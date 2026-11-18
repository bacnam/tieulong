package business.global.recharge;

import BaseCommon.CommLog;
import business.global.activity.ActivityMgr;
import business.global.activity.detail.AccumRecharge;
import business.global.activity.detail.AccumRechargeDay;
import business.global.activity.detail.DailyRecharge;
import business.global.activity.detail.FirstRecharge;
import business.global.activity.detail.FortuneWheel;
import business.global.activity.detail.NewFirstReward;
import business.global.activity.detail.RedPacket;
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
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.common.enums.RankType;
import com.zhonglian.server.common.enums.Title;
import com.zhonglian.server.common.enums.Achievement.AchievementType;
import com.zhonglian.server.common.enums.ConstEnum.DailyRefresh;
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
        List<RechargeOrderBO> paid = BM.getBM(RechargeOrderBO.class).findAll("status", RechargeMgr.OrderStatus.Paid.toString());
        this.cachedOrders = new HashMap<>();

        for (RechargeOrderBO o : paid) {
            this.cachedOrders.put(o.getPid(), o);
        }

        List<RechargeResetBO> resetbos = BM.getBM(RechargeResetBO.class).findAll();
        this.resets = new HashMap<>();

        for (RechargeResetBO o : resetbos) {
            this.resets.put(o.getGoodsid(), o);
        }

        return true;
    }

    public void cacheOrder(RechargeOrderBO orderBO) {
        this.cachedOrders.put(orderBO.getPid(), orderBO);
    }

    public void trySendCachedOrder(long cid) {
        if (this.cachedOrders.containsKey(cid)) {
            synchronized (this) {
                RechargeOrderBO orderBO = this.cachedOrders.remove(cid);
                if (orderBO != null) {
                    this.sendPrize(orderBO);
                }
            }
        }
    }

    public void sendPrize(RechargeOrderBO orderBO) {
        synchronized (this) {
            if (!orderBO.getStatus().equals(RechargeMgr.OrderStatus.Delivered)) {
                RefRecharge ref = RefDataMgr.get(RefRecharge.class, orderBO.getProductid());
                if (ref == null) {
                    CommLog.error("订单[{}]充值失败，对应商品id[{}]无法查到", orderBO.getCporderid(), orderBO.getProductid());
                } else {
                    Player player = PlayerMgr.getInstance().getPlayer(orderBO.getPid());
                    if (player == null) {
                        CommLog.error("玩家[{}]不存在或已注销", orderBO.getPid());
                    } else {
                        RechargeFeature rechargeFeature = player.getFeature(RechargeFeature.class);
                        PlayerRechargeRecordBO record = rechargeFeature.getRecharged(ref.id);
                        int crystal = ref.Crystal;
                        ActivityMgr.getActivity(NewFirstReward.class).setFirstReward(player);
                        String resetsign = this.getResetSign(ref.id);
                        boolean first = record == null || !resetsign.equals(record.getResetSign());
                        if (first && ref.RebateAchievement == AchievementType.None) {
                            crystal = ref.First;
                        }

                        rechargeFeature.isRecharged();
                        if (ref.RebateAchievement == AchievementType.MonthCardCrystal || ref.RebateAchievement == AchievementType.YearCardCrystal) {
                            player.getPlayerBO().saveExtPackage(player.getPlayerBO().getExtPackage() + ref.AddPackage);
                            player.pushProto("extPackage", player.getPlayerBO().getExtPackage());
                        }

                        if (ref.RebateAchievement == AchievementType.MonthCardCrystal) {
                            rechargeFeature.incRebateRemains(ref.RebateAchievement, ref.RebateDays);
                            AchievementFeature.AchievementIns ins = player.getFeature(AchievementFeature.class).getOrCreate(ref.RebateAchievement);
                            if (ins.bo.getCompleteCount() != 0) {
                                ins.bo.saveCompleteCount(ins.bo.getCompleteCount() + ref.RebateDays);
                            }

                            player.getFeature(AchievementFeature.class).checkMonthCard(AchievementType.MonthCardCrystal);
                        }

                        if (ref.RebateAchievement == AchievementType.YearCardCrystal) {
                            rechargeFeature.incRebateRemains(ref.RebateAchievement, ref.RebateDays);
                            player.getFeature(AchievementFeature.class).checkMonthCard(AchievementType.YearCardCrystal);
                        }

                        orderBO.saveStatus(RechargeMgr.OrderStatus.Delivered.toString());
                        orderBO.saveDeliverTime(CommTime.nowSecond());
                        rechargeFeature.recordRecharge(orderBO, resetsign);
                        int gained = player.getFeature(PlayerCurrency.class).gain(PrizeType.Crystal, crystal * orderBO.getQuantity(), ItemFlow.Recharge);
                        player.getPlayerBO().saveTotalRecharge(player.getPlayerBO().getTotalRecharge() + ref.Price);
                        player.getFeature(PlayerRecord.class).addValue(DailyRefresh.DailyRecharge, ref.Price / 10);
                        player.getFeature(PlayerCurrency.class).gain(PrizeType.VipExp, ref.VipExp, ItemFlow.Recharge);
                        core.network.proto.Player.RechargeNotify notify = new core.network.proto.Player.RechargeNotify(
                            new Reward(PrizeType.Crystal, gained), ref.id
                        );
                        player.pushProto("RechargeNotifyResult", notify);
                        if (ref.RebateAchievement != AchievementType.MonthCardCrystal && ref.RebateAchievement != AchievementType.YearCardCrystal) {
                            ActivityMgr.getActivity(FirstRecharge.class).sendFirstRechargeReward(player);
                        }

                        ActivityMgr.getActivity(DailyRecharge.class).handlePlayerChange(player, ref.Price / 10);
                        ActivityMgr.getActivity(AccumRecharge.class).handlePlayerChange(player, ref.Price / 10);
                        ActivityMgr.getActivity(AccumRechargeDay.class).handleRecharge(player);
                        player.getFeature(TitleFeature.class).updateInc(Title.RechargeTo, ref.Price / 10);
                        ActivityMgr.getActivity(RedPacket.class).handle(ref.Price / 10, player);
                        ActivityMgr.updateWorldRank(player, ref.Price / 10, RankType.WorldRecharge);
                        ActivityMgr.getActivity(FortuneWheel.class).handlePlayerChange(player, ref.Price / 10);
                        this.rechargeLog(player, orderBO);
                        this.TDrechargeLog(player, orderBO, gained);
                    }
                }
            }
        }
    }

    public String getResetSign(String id) {
        RechargeResetBO bo = this.resets.get(id);
        return bo == null ? "" : bo.getResetSign();
    }

    public void reset(String goodsid) {
        RechargeResetBO bo = this.resets.get(goodsid);
        if (bo != null) {
            bo.saveResetSign(CommTime.nowMS() + "@server" + Config.ServerID());
        } else {
            bo = new RechargeResetBO();
            bo.setGoodsid(goodsid);
            bo.saveResetSign(CommTime.nowMS() + "@server" + Config.ServerID());
            bo.insert();
            this.resets.put(goodsid, bo);
        }
    }

    public void rechargeLog(Player player, RechargeOrderBO bo) {
        AccountBO account = player.getFeature(AccountFeature.class).getAccount();

        try {
            RefRecharge ref = RefDataMgr.get(RefRecharge.class, bo.getProductid());
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
                account.getAdid()
            );
        } catch (Exception var5) {
        }
    }

    public void TDrechargeLog(Player player, RechargeOrderBO bo, int gained) {
        try {
            String orderid = bo.getCporderid();
            if (orderid != null && !orderid.equals("") && orderid.contains("模拟")) {
                return;
            }

            RefRecharge ref = RefDataMgr.get(RefRecharge.class, bo.getProductid());
            TDRechargeLogger.getInstance()
                .sendRechargeLog(
                    String.valueOf(bo.getId()),
                    "success",
                    "h5",
                    String.valueOf(player.getPid()),
                    bo.getCporderid(),
                    ref.Price / 10,
                    "CNY",
                    gained,
                    CommTime.nowSecond(),
                    ref.Title,
                    player.getLv()
                );
        } catch (Exception var6) {
        }
    }

    public static enum OrderStatus {
        Paid,
        Delivered,
        Cancled;
    }
}
