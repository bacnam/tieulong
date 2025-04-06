package core.logger.flow;

import com.zhonglian.server.logger.flow.FlowLoggerBase;

public abstract class GameFlowLogger extends FlowLoggerBase {
  public void activityRequest(String activity, boolean isActive, String gm_id, int beginTime, int endTime, int closeTime, String json, String type, String operator) {}
  
  public void arenaTokenChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {}
  
  public void artificeMaterialChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {}
  
  public void createRoleLog(String openid, long pid, int createTime, String channel, int serverid) {}
  
  public void crystalChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {}
  
  public void EquipInstanceMaterialChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {}
  
  public void expChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {}
  
  public void GemInstanceMaterialChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {}
  
  public void gemMaterialChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {}
  
  public void goldChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {}
  
  public void guildDonateChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {}
  
  public void itemLog(long pid, int vip_level, int level, int itemId, int reason, int num, int cur_remainder, int pre_value, int type) {}
  
  public void lotteryChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {}
  
  public void mail(long cid, String open_id, int vip_level, int level, long mail_id, String title, String item_ids, String item_nums, String sender, String type) {}
  
  public void meridianInstanceMaterialChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {}
  
  public void merMaterialChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {}
  
  public void offlineRewards(long pid, int vip_level, int team_level, int dungeon_level, int waves, int offline_time, int calc_time) {}
  
  public void rechargeLog(long cid, String open_id, int vip_level, int level, String order_id, String tp_order_id, String goods_id, int money, int coin_num, String status, int pay_time, String pay_time_date, int order_time, String order_time_date, String adfrom, String adfrom2, String ar_ip, int ar_time, String ar_time_date, String adid) {}
  
  public void redPieceChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {}
  
  public void starMaterialChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {}
  
  public void strengthenMaterialChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {}
  
  public void warspiritTalentMaterialChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {}
  
  public void wingMaterialChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {}
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/logger/flow/GameFlowLogger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */