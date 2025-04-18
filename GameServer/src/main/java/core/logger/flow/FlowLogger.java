package core.logger.flow;

import com.zhonglian.server.logger.flow.FlowLoggerMgrBase;

public class FlowLogger
extends FlowLoggerMgrBase<GameFlowLogger>
{
private static FlowLogger instance = new FlowLogger();

public static FlowLogger getInstance() {
return instance;
}

public static void activityRequest(String activity, boolean isActive, String gm_id, int beginTime, int endTime, int closeTime, String json, String type, String operator) {
instance.log("activityRequest", new Object[] { activity, Boolean.valueOf(isActive), gm_id, Integer.valueOf(beginTime), Integer.valueOf(endTime), Integer.valueOf(closeTime), json, type, operator });
}

public static void arenaTokenChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
instance.log("arenaTokenChargeLog", new Object[] { Long.valueOf(pid), Integer.valueOf(vip_level), Integer.valueOf(level), Integer.valueOf(reason), Integer.valueOf(num), Integer.valueOf(cur_remainder), Integer.valueOf(pre_value), Integer.valueOf(type) });
}

public static void artificeMaterialChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
instance.log("artificeMaterialChargeLog", new Object[] { Long.valueOf(pid), Integer.valueOf(vip_level), Integer.valueOf(level), Integer.valueOf(reason), Integer.valueOf(num), Integer.valueOf(cur_remainder), Integer.valueOf(pre_value), Integer.valueOf(type) });
}

public static void createRoleLog(String openid, long pid, int createTime, String channel, int serverid) {
instance.log("createRoleLog", new Object[] { openid, Long.valueOf(pid), Integer.valueOf(createTime), channel, Integer.valueOf(serverid) });
}

public static void crystalChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
instance.log("crystalChargeLog", new Object[] { Long.valueOf(pid), Integer.valueOf(vip_level), Integer.valueOf(level), Integer.valueOf(reason), Integer.valueOf(num), Integer.valueOf(cur_remainder), Integer.valueOf(pre_value), Integer.valueOf(type) });
}

public static void EquipInstanceMaterialChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
instance.log("EquipInstanceMaterialChargeLog", new Object[] { Long.valueOf(pid), Integer.valueOf(vip_level), Integer.valueOf(level), Integer.valueOf(reason), Integer.valueOf(num), Integer.valueOf(cur_remainder), Integer.valueOf(pre_value), Integer.valueOf(type) });
}

public static void expChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
instance.log("expChargeLog", new Object[] { Long.valueOf(pid), Integer.valueOf(vip_level), Integer.valueOf(level), Integer.valueOf(reason), Integer.valueOf(num), Integer.valueOf(cur_remainder), Integer.valueOf(pre_value), Integer.valueOf(type) });
}

public static void GemInstanceMaterialChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
instance.log("GemInstanceMaterialChargeLog", new Object[] { Long.valueOf(pid), Integer.valueOf(vip_level), Integer.valueOf(level), Integer.valueOf(reason), Integer.valueOf(num), Integer.valueOf(cur_remainder), Integer.valueOf(pre_value), Integer.valueOf(type) });
}

public static void gemMaterialChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
instance.log("gemMaterialChargeLog", new Object[] { Long.valueOf(pid), Integer.valueOf(vip_level), Integer.valueOf(level), Integer.valueOf(reason), Integer.valueOf(num), Integer.valueOf(cur_remainder), Integer.valueOf(pre_value), Integer.valueOf(type) });
}

public static void goldChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
instance.log("goldChargeLog", new Object[] { Long.valueOf(pid), Integer.valueOf(vip_level), Integer.valueOf(level), Integer.valueOf(reason), Integer.valueOf(num), Integer.valueOf(cur_remainder), Integer.valueOf(pre_value), Integer.valueOf(type) });
}

public static void guildDonateChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
instance.log("guildDonateChargeLog", new Object[] { Long.valueOf(pid), Integer.valueOf(vip_level), Integer.valueOf(level), Integer.valueOf(reason), Integer.valueOf(num), Integer.valueOf(cur_remainder), Integer.valueOf(pre_value), Integer.valueOf(type) });
}

public static void itemLog(long pid, int vip_level, int level, int itemId, int reason, int num, int cur_remainder, int pre_value, int type) {
instance.log("itemLog", new Object[] { Long.valueOf(pid), Integer.valueOf(vip_level), Integer.valueOf(level), Integer.valueOf(itemId), Integer.valueOf(reason), Integer.valueOf(num), Integer.valueOf(cur_remainder), Integer.valueOf(pre_value), Integer.valueOf(type) });
}

public static void lotteryChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
instance.log("lotteryChargeLog", new Object[] { Long.valueOf(pid), Integer.valueOf(vip_level), Integer.valueOf(level), Integer.valueOf(reason), Integer.valueOf(num), Integer.valueOf(cur_remainder), Integer.valueOf(pre_value), Integer.valueOf(type) });
}

public static void mail(long cid, String open_id, int vip_level, int level, long mail_id, String title, String item_ids, String item_nums, String sender, String type) {
instance.log("mail", new Object[] { Long.valueOf(cid), open_id, Integer.valueOf(vip_level), Integer.valueOf(level), Long.valueOf(mail_id), title, item_ids, item_nums, sender, type });
}

public static void meridianInstanceMaterialChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
instance.log("meridianInstanceMaterialChargeLog", new Object[] { Long.valueOf(pid), Integer.valueOf(vip_level), Integer.valueOf(level), Integer.valueOf(reason), Integer.valueOf(num), Integer.valueOf(cur_remainder), Integer.valueOf(pre_value), Integer.valueOf(type) });
}

public static void merMaterialChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
instance.log("merMaterialChargeLog", new Object[] { Long.valueOf(pid), Integer.valueOf(vip_level), Integer.valueOf(level), Integer.valueOf(reason), Integer.valueOf(num), Integer.valueOf(cur_remainder), Integer.valueOf(pre_value), Integer.valueOf(type) });
}

public static void offlineRewards(long pid, int vip_level, int team_level, int dungeon_level, int waves, int offline_time, int calc_time) {
instance.log("offlineRewards", new Object[] { Long.valueOf(pid), Integer.valueOf(vip_level), Integer.valueOf(team_level), Integer.valueOf(dungeon_level), Integer.valueOf(waves), Integer.valueOf(offline_time), Integer.valueOf(calc_time) });
}

public static void rechargeLog(long cid, String open_id, int vip_level, int level, String order_id, String tp_order_id, String goods_id, int money, int coin_num, String status, int pay_time, String pay_time_date, int order_time, String order_time_date, String adfrom, String adfrom2, String ar_ip, int ar_time, String ar_time_date, String adid) {
instance.log("rechargeLog", new Object[] { Long.valueOf(cid), open_id, Integer.valueOf(vip_level), Integer.valueOf(level), order_id, tp_order_id, goods_id, Integer.valueOf(money), Integer.valueOf(coin_num), status, Integer.valueOf(pay_time), pay_time_date, Integer.valueOf(order_time), order_time_date, adfrom, adfrom2, ar_ip, Integer.valueOf(ar_time), ar_time_date, adid });
}

public static void redPieceChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
instance.log("redPieceChargeLog", new Object[] { Long.valueOf(pid), Integer.valueOf(vip_level), Integer.valueOf(level), Integer.valueOf(reason), Integer.valueOf(num), Integer.valueOf(cur_remainder), Integer.valueOf(pre_value), Integer.valueOf(type) });
}

public static void starMaterialChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
instance.log("starMaterialChargeLog", new Object[] { Long.valueOf(pid), Integer.valueOf(vip_level), Integer.valueOf(level), Integer.valueOf(reason), Integer.valueOf(num), Integer.valueOf(cur_remainder), Integer.valueOf(pre_value), Integer.valueOf(type) });
}

public static void strengthenMaterialChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
instance.log("strengthenMaterialChargeLog", new Object[] { Long.valueOf(pid), Integer.valueOf(vip_level), Integer.valueOf(level), Integer.valueOf(reason), Integer.valueOf(num), Integer.valueOf(cur_remainder), Integer.valueOf(pre_value), Integer.valueOf(type) });
}

public static void warspiritTalentMaterialChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
instance.log("warspiritTalentMaterialChargeLog", new Object[] { Long.valueOf(pid), Integer.valueOf(vip_level), Integer.valueOf(level), Integer.valueOf(reason), Integer.valueOf(num), Integer.valueOf(cur_remainder), Integer.valueOf(pre_value), Integer.valueOf(type) });
}

public static void wingMaterialChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
instance.log("wingMaterialChargeLog", new Object[] { Long.valueOf(pid), Integer.valueOf(vip_level), Integer.valueOf(level), Integer.valueOf(reason), Integer.valueOf(num), Integer.valueOf(cur_remainder), Integer.valueOf(pre_value), Integer.valueOf(type) });
}
}

