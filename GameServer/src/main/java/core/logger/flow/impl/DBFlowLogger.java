package core.logger.flow.impl;

import com.zhonglian.server.logger.flow.db.DBFlowBase;
import com.zhonglian.server.logger.flow.db.DBFlowMgr;
import core.logger.flow.GameFlowLogger;
import core.logger.flow.impl.db.*;

public class DBFlowLogger
        extends GameFlowLogger {
    public boolean isOpen() {
        return true;
    }

    public void activityRequest(String activity, boolean isActive, String gm_id, int beginTime, int endTime, int closeTime, String json, String type, String operator) {
        DBFlowMgr.getInstance().add((DBFlowBase) new ActivityRequestFlow(activity, isActive, gm_id, beginTime, endTime, closeTime, json, type, operator));
    }

    public void arenaTokenChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
        DBFlowMgr.getInstance().add((DBFlowBase) new ArenaTokenChargeLogFlow(pid, vip_level, level, reason, num, cur_remainder, pre_value, type));
    }

    public void artificeMaterialChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
        DBFlowMgr.getInstance().add((DBFlowBase) new ArtificeMaterialChargeLogFlow(pid, vip_level, level, reason, num, cur_remainder, pre_value, type));
    }

    public void createRoleLog(String openid, long pid, int createTime, String channel, int serverid) {
        DBFlowMgr.getInstance().add((DBFlowBase) new CreateRoleLogFlow(openid, pid, createTime, channel, serverid));
    }

    public void crystalChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
        DBFlowMgr.getInstance().add((DBFlowBase) new CrystalChargeLogFlow(pid, vip_level, level, reason, num, cur_remainder, pre_value, type));
    }

    public void EquipInstanceMaterialChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
        DBFlowMgr.getInstance().add((DBFlowBase) new EquipInstanceMaterialChargeLogFlow(pid, vip_level, level, reason, num, cur_remainder, pre_value, type));
    }

    public void expChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
        DBFlowMgr.getInstance().add((DBFlowBase) new ExpChargeLogFlow(pid, vip_level, level, reason, num, cur_remainder, pre_value, type));
    }

    public void GemInstanceMaterialChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
        DBFlowMgr.getInstance().add((DBFlowBase) new GemInstanceMaterialChargeLogFlow(pid, vip_level, level, reason, num, cur_remainder, pre_value, type));
    }

    public void gemMaterialChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
        DBFlowMgr.getInstance().add((DBFlowBase) new GemMaterialChargeLogFlow(pid, vip_level, level, reason, num, cur_remainder, pre_value, type));
    }

    public void goldChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
        DBFlowMgr.getInstance().add((DBFlowBase) new GoldChargeLogFlow(pid, vip_level, level, reason, num, cur_remainder, pre_value, type));
    }

    public void guildDonateChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
        DBFlowMgr.getInstance().add((DBFlowBase) new GuildDonateChargeLogFlow(pid, vip_level, level, reason, num, cur_remainder, pre_value, type));
    }

    public void itemLog(long pid, int vip_level, int level, int itemId, int reason, int num, int cur_remainder, int pre_value, int type) {
        DBFlowMgr.getInstance().add((DBFlowBase) new ItemLogFlow(pid, vip_level, level, itemId, reason, num, cur_remainder, pre_value, type));
    }

    public void lotteryChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
        DBFlowMgr.getInstance().add((DBFlowBase) new LotteryChargeLogFlow(pid, vip_level, level, reason, num, cur_remainder, pre_value, type));
    }

    public void mail(long cid, String open_id, int vip_level, int level, long mail_id, String title, String item_ids, String item_nums, String sender, String type) {
        DBFlowMgr.getInstance().add((DBFlowBase) new MailFlow(cid, open_id, vip_level, level, mail_id, title, item_ids, item_nums, sender, type));
    }

    public void meridianInstanceMaterialChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
        DBFlowMgr.getInstance().add((DBFlowBase) new MeridianInstanceMaterialChargeLogFlow(pid, vip_level, level, reason, num, cur_remainder, pre_value, type));
    }

    public void merMaterialChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
        DBFlowMgr.getInstance().add((DBFlowBase) new MerMaterialChargeLogFlow(pid, vip_level, level, reason, num, cur_remainder, pre_value, type));
    }

    public void offlineRewards(long pid, int vip_level, int team_level, int dungeon_level, int waves, int offline_time, int calc_time) {
        DBFlowMgr.getInstance().add((DBFlowBase) new OfflineRewardsFlow(pid, vip_level, team_level, dungeon_level, waves, offline_time, calc_time));
    }

    public void rechargeLog(long cid, String open_id, int vip_level, int level, String order_id, String tp_order_id, String goods_id, int money, int coin_num, String status, int pay_time, String pay_time_date, int order_time, String order_time_date, String adfrom, String adfrom2, String ar_ip, int ar_time, String ar_time_date, String adid) {
        DBFlowMgr.getInstance().add((DBFlowBase) new RechargeLogFlow(cid, open_id, vip_level, level, order_id, tp_order_id, goods_id, money, coin_num, status, pay_time, pay_time_date, order_time, order_time_date, adfrom, adfrom2, ar_ip, ar_time, ar_time_date, adid));
    }

    public void redPieceChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
        DBFlowMgr.getInstance().add((DBFlowBase) new RedPieceChargeLogFlow(pid, vip_level, level, reason, num, cur_remainder, pre_value, type));
    }

    public void starMaterialChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
        DBFlowMgr.getInstance().add((DBFlowBase) new StarMaterialChargeLogFlow(pid, vip_level, level, reason, num, cur_remainder, pre_value, type));
    }

    public void strengthenMaterialChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
        DBFlowMgr.getInstance().add((DBFlowBase) new StrengthenMaterialChargeLogFlow(pid, vip_level, level, reason, num, cur_remainder, pre_value, type));
    }

    public void warspiritTalentMaterialChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
        DBFlowMgr.getInstance().add((DBFlowBase) new WarspiritTalentMaterialChargeLogFlow(pid, vip_level, level, reason, num, cur_remainder, pre_value, type));
    }

    public void wingMaterialChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
        DBFlowMgr.getInstance().add((DBFlowBase) new WingMaterialChargeLogFlow(pid, vip_level, level, reason, num, cur_remainder, pre_value, type));
    }
}

