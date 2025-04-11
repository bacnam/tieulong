package core.logger.flow.impl;

import com.google.gson.JsonObject;
import com.zhonglian.server.common.enums.ChatType;
import com.zhonglian.server.common.enums.ConstEnum;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.logger.flow.redis.RedisFlowMgr;
import core.logger.flow.GameFlowLogger;
import core.server.ServerConfig;

public class RedisFlowLogger
        extends GameFlowLogger {
    private Boolean isopen = null;

    public static void chatLog(long pid, String content, ChatType type, long toCId) {
        JsonObject param = new JsonObject();
        param.addProperty("srv", "lncq");
        param.addProperty("type", "chat");
        String types = "";
        switch (type) {
            case CHATTYPE_WORLD:
                types = "world";
                break;
            case CHATTYPE_GUILD:
                types = "guild";
                break;
            case CHATTYPE_COMPANY:
                types = "company";
                break;
            case CHATTYPE_SYSTEM:
                types = "system";
                break;
            default:
                types = "none";
                break;
        }
        param.addProperty("gaintype", types);
        param.addProperty("@timestamp", CommTime.getTimeString8601(CommTime.nowSecond()));
        param.addProperty("ServerID", Integer.valueOf(ServerConfig.ServerID()));
        param.addProperty("world", System.getProperty("world_sid", "0"));
        param.addProperty("senderId", pid);
        param.addProperty("content", content);
        param.addProperty("toPid", toCId);

        RedisFlowMgr.getInstance().add(param);
    }

    public boolean isOpen() {
        if (this.isopen == null) {
            String ADDR = System.getProperty("Redis.ADDR");
            this.isopen = Boolean.valueOf((ADDR != null && !ADDR.trim().isEmpty()));
        }
        return this.isopen.booleanValue();
    }

    public void arenaTokenChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
        JsonObject param = new JsonObject();
        param.addProperty("srv", "lncq");
        param.addProperty("type", "arenatoken");
        param.addProperty("gaintype", ConstEnum.ResdisType.values()[type].toString());
        param.addProperty("@timestamp", CommTime.getTimeString8601(CommTime.nowSecond()));
        param.addProperty("MoneyType", "竞技币");
        param.addProperty("MoneyNum", Integer.valueOf(num));
        param.addProperty("cur_value", Integer.valueOf(cur_remainder));
        param.addProperty("pre_value", Integer.valueOf(pre_value));
        param.addProperty("Reason", reason);
        param.addProperty("RoleID", Long.valueOf(pid));
        param.addProperty("ServerID", Integer.valueOf(ServerConfig.ServerID()));
        param.addProperty("vip_level", vip_level);
        param.addProperty("level", level);
        param.addProperty("world", System.getProperty("world_sid", "0"));
        RedisFlowMgr.getInstance().add(param);
    }

    public void artificeMaterialChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
        JsonObject param = new JsonObject();
        param.addProperty("srv", "lncq");
        param.addProperty("type", "artificematerial");
        param.addProperty("gaintype", ConstEnum.ResdisType.values()[type].toString());
        param.addProperty("@timestamp", CommTime.getTimeString8601(CommTime.nowSecond()));
        param.addProperty("MoneyType", "炼化石");
        param.addProperty("MoneyNum", Integer.valueOf(num));
        param.addProperty("cur_value", Integer.valueOf(cur_remainder));
        param.addProperty("pre_value", Integer.valueOf(pre_value));
        param.addProperty("Reason", reason);
        param.addProperty("RoleID", Long.valueOf(pid));
        param.addProperty("ServerID", Integer.valueOf(ServerConfig.ServerID()));
        param.addProperty("vip_level", vip_level);
        param.addProperty("level", level);
        param.addProperty("world", System.getProperty("world_sid", "0"));
        RedisFlowMgr.getInstance().add(param);
    }

    public void createRoleLog(String openid, long pid, int createTime, String channel, int serverid) {
        JsonObject param = new JsonObject();
        param.addProperty("srv", "lncq");
        param.addProperty("type", "createrole");
        param.addProperty("@timestamp", CommTime.getTimeString8601(CommTime.nowSecond()));
        param.addProperty("openid", openid);
        param.addProperty("pid", Long.valueOf(pid));
        param.addProperty("createTime", Integer.valueOf(CommTime.nowSecond()));
        param.addProperty("channel", channel);
        param.addProperty("serverid", Integer.valueOf(serverid));
        param.addProperty("world", System.getProperty("world_sid", "0"));
        RedisFlowMgr.getInstance().add(param);
    }

    public void crystalChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
        JsonObject param = new JsonObject();
        param.addProperty("srv", "lncq");
        param.addProperty("type", "crystal");
        param.addProperty("gaintype", ConstEnum.ResdisType.values()[type].toString());
        param.addProperty("@timestamp", CommTime.getTimeString8601(CommTime.nowSecond()));
        param.addProperty("MoneyType", "元宝");
        param.addProperty("MoneyNum", Integer.valueOf(num));
        param.addProperty("cur_value", Integer.valueOf(cur_remainder));
        param.addProperty("pre_value", Integer.valueOf(pre_value));
        param.addProperty("Reason", reason);
        param.addProperty("RoleID", Long.valueOf(pid));
        param.addProperty("ServerID", Integer.valueOf(ServerConfig.ServerID()));
        param.addProperty("vip_level", vip_level);
        param.addProperty("level", level);
        param.addProperty("world", System.getProperty("world_sid", "0"));
        RedisFlowMgr.getInstance().add(param);
    }

    public void EquipInstanceMaterialChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
        JsonObject param = new JsonObject();
        param.addProperty("srv", "lncq");
        param.addProperty("type", "equipinstancematerial");
        param.addProperty("gaintype", ConstEnum.ResdisType.values()[type].toString());
        param.addProperty("MoneyType", "装备副本材料");
        param.addProperty("MoneyNum", Integer.valueOf(num));
        param.addProperty("cur_value", Integer.valueOf(cur_remainder));
        param.addProperty("pre_value", Integer.valueOf(pre_value));
        param.addProperty("Reason", reason);
        param.addProperty("RoleID", Long.valueOf(pid));
        param.addProperty("ServerID", Integer.valueOf(ServerConfig.ServerID()));
        param.addProperty("vip_level", vip_level);
        param.addProperty("level", level);
        param.addProperty("world", System.getProperty("world_sid", "0"));
        RedisFlowMgr.getInstance().add(param);
    }

    public void expChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
        JsonObject param = new JsonObject();
        param.addProperty("srv", "lncq");
        param.addProperty("type", "exp");
        param.addProperty("gaintype", ConstEnum.ResdisType.values()[type].toString());
        param.addProperty("@timestamp", CommTime.getTimeString8601(CommTime.nowSecond()));
        param.addProperty("MoneyType", "经验");
        param.addProperty("MoneyNum", Integer.valueOf(num));
        param.addProperty("cur_value", Integer.valueOf(cur_remainder));
        param.addProperty("pre_value", Integer.valueOf(pre_value));
        param.addProperty("Reason", reason);
        param.addProperty("RoleID", Long.valueOf(pid));
        param.addProperty("ServerID", Integer.valueOf(ServerConfig.ServerID()));
        param.addProperty("vip_level", vip_level);
        param.addProperty("level", level);
        param.addProperty("world", System.getProperty("world_sid", "0"));
        RedisFlowMgr.getInstance().add(param);
    }

    public void GemInstanceMaterialChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
        JsonObject param = new JsonObject();
        param.addProperty("srv", "lncq");
        param.addProperty("type", "geminstancematerial");
        param.addProperty("gaintype", ConstEnum.ResdisType.values()[type].toString());
        param.addProperty("@timestamp", CommTime.getTimeString8601(CommTime.nowSecond()));
        param.addProperty("MoneyType", "宝石副本材料");
        param.addProperty("MoneyNum", Integer.valueOf(num));
        param.addProperty("cur_value", Integer.valueOf(cur_remainder));
        param.addProperty("pre_value", Integer.valueOf(pre_value));
        param.addProperty("Reason", reason);
        param.addProperty("RoleID", Long.valueOf(pid));
        param.addProperty("ServerID", Integer.valueOf(ServerConfig.ServerID()));
        param.addProperty("vip_level", vip_level);
        param.addProperty("level", level);
        param.addProperty("world", System.getProperty("world_sid", "0"));
        RedisFlowMgr.getInstance().add(param);
    }

    public void gemMaterialChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
        JsonObject param = new JsonObject();
        param.addProperty("srv", "lncq");
        param.addProperty("type", "gemmaterial");
        param.addProperty("gaintype", ConstEnum.ResdisType.values()[type].toString());
        param.addProperty("@timestamp", CommTime.getTimeString8601(CommTime.nowSecond()));
        param.addProperty("MoneyType", "宝石");
        param.addProperty("MoneyNum", Integer.valueOf(num));
        param.addProperty("cur_value", Integer.valueOf(cur_remainder));
        param.addProperty("pre_value", Integer.valueOf(pre_value));
        param.addProperty("Reason", reason);
        param.addProperty("RoleID", Long.valueOf(pid));
        param.addProperty("ServerID", Integer.valueOf(ServerConfig.ServerID()));
        param.addProperty("vip_level", vip_level);
        param.addProperty("level", level);
        param.addProperty("world", System.getProperty("world_sid", "0"));
        RedisFlowMgr.getInstance().add(param);
    }

    public void guildDonateChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
        JsonObject param = new JsonObject();
        param.addProperty("srv", "lncq");
        param.addProperty("type", "guilddonate");
        param.addProperty("gaintype", ConstEnum.ResdisType.values()[type].toString());
        param.addProperty("@timestamp", CommTime.getTimeString8601(CommTime.nowSecond()));
        param.addProperty("MoneyType", "帮贡");
        param.addProperty("MoneyNum", Integer.valueOf(num));
        param.addProperty("cur_value", Integer.valueOf(cur_remainder));
        param.addProperty("pre_value", Integer.valueOf(pre_value));
        param.addProperty("Reason", reason);
        param.addProperty("RoleID", Long.valueOf(pid));
        param.addProperty("ServerID", Integer.valueOf(ServerConfig.ServerID()));
        param.addProperty("vip_level", vip_level);
        param.addProperty("level", level);
        param.addProperty("world", System.getProperty("world_sid", "0"));
        RedisFlowMgr.getInstance().add(param);
    }

    public void goldChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
        JsonObject param = new JsonObject();
        param.addProperty("srv", "lncq");
        param.addProperty("type", "gold");
        param.addProperty("gaintype", ConstEnum.ResdisType.values()[type].toString());
        param.addProperty("@timestamp", CommTime.getTimeString8601(CommTime.nowSecond()));
        param.addProperty("MoneyType", "金币");
        param.addProperty("MoneyNum", Integer.valueOf(num));
        param.addProperty("cur_value", Integer.valueOf(cur_remainder));
        param.addProperty("pre_value", Integer.valueOf(pre_value));
        param.addProperty("Reason", reason);
        param.addProperty("RoleID", Long.valueOf(pid));
        param.addProperty("ServerID", Integer.valueOf(ServerConfig.ServerID()));
        param.addProperty("vip_level", vip_level);
        param.addProperty("level", level);
        param.addProperty("world", System.getProperty("world_sid", "0"));
        RedisFlowMgr.getInstance().add(param);
    }

    public void meridianInstanceMaterialChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
        JsonObject param = new JsonObject();
        param.addProperty("srv", "lncq");
        param.addProperty("type", "meridianinstancematerial");
        param.addProperty("gaintype", ConstEnum.ResdisType.values()[type].toString());
        param.addProperty("@timestamp", CommTime.getTimeString8601(CommTime.nowSecond()));
        param.addProperty("MoneyType", "经脉副本材料");
        param.addProperty("MoneyNum", Integer.valueOf(num));
        param.addProperty("cur_value", Integer.valueOf(cur_remainder));
        param.addProperty("pre_value", Integer.valueOf(pre_value));
        param.addProperty("Reason", reason);
        param.addProperty("RoleID", Long.valueOf(pid));
        param.addProperty("ServerID", Integer.valueOf(ServerConfig.ServerID()));
        param.addProperty("vip_level", vip_level);
        param.addProperty("level", level);
        param.addProperty("world", System.getProperty("world_sid", "0"));
        RedisFlowMgr.getInstance().add(param);
    }

    public void merMaterialChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
        JsonObject param = new JsonObject();
        param.addProperty("srv", "lncq");
        param.addProperty("type", "mermaterial");
        param.addProperty("gaintype", ConstEnum.ResdisType.values()[type].toString());
        param.addProperty("@timestamp", CommTime.getTimeString8601(CommTime.nowSecond()));
        param.addProperty("MoneyType", "经脉丹");
        param.addProperty("MoneyNum", Integer.valueOf(num));
        param.addProperty("cur_value", Integer.valueOf(cur_remainder));
        param.addProperty("pre_value", Integer.valueOf(pre_value));
        param.addProperty("Reason", reason);
        param.addProperty("RoleID", Long.valueOf(pid));
        param.addProperty("ServerID", Integer.valueOf(ServerConfig.ServerID()));
        param.addProperty("vip_level", vip_level);
        param.addProperty("level", level);
        param.addProperty("world", System.getProperty("world_sid", "0"));
        RedisFlowMgr.getInstance().add(param);
    }

    public void redPieceChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
        JsonObject param = new JsonObject();
        param.addProperty("srv", "lncq");
        param.addProperty("type", "redpiece");
        param.addProperty("gaintype", ConstEnum.ResdisType.values()[type].toString());
        param.addProperty("@timestamp", CommTime.getTimeString8601(CommTime.nowSecond()));
        param.addProperty("MoneyType", "神装碎片");
        param.addProperty("MoneyNum", Integer.valueOf(num));
        param.addProperty("cur_value", Integer.valueOf(cur_remainder));
        param.addProperty("pre_value", Integer.valueOf(pre_value));
        param.addProperty("Reason", reason);
        param.addProperty("RoleID", Long.valueOf(pid));
        param.addProperty("ServerID", Integer.valueOf(ServerConfig.ServerID()));
        param.addProperty("vip_level", vip_level);
        param.addProperty("level", level);
        param.addProperty("world", System.getProperty("world_sid", "0"));
        RedisFlowMgr.getInstance().add(param);
    }

    public void starMaterialChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
        JsonObject param = new JsonObject();
        param.addProperty("srv", "lncq");
        param.addProperty("type", "starmaterial");
        param.addProperty("gaintype", ConstEnum.ResdisType.values()[type].toString());
        param.addProperty("@timestamp", CommTime.getTimeString8601(CommTime.nowSecond()));
        param.addProperty("MoneyType", "升星石");
        param.addProperty("MoneyNum", Integer.valueOf(num));
        param.addProperty("cur_value", Integer.valueOf(cur_remainder));
        param.addProperty("pre_value", Integer.valueOf(pre_value));
        param.addProperty("Reason", reason);
        param.addProperty("RoleID", Long.valueOf(pid));
        param.addProperty("ServerID", Integer.valueOf(ServerConfig.ServerID()));
        param.addProperty("vip_level", vip_level);
        param.addProperty("level", level);
        param.addProperty("world", System.getProperty("world_sid", "0"));
        RedisFlowMgr.getInstance().add(param);
    }

    public void strengthenMaterialChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
        JsonObject param = new JsonObject();
        param.addProperty("srv", "lncq");
        param.addProperty("type", "strengthenmaterial");
        param.addProperty("gaintype", ConstEnum.ResdisType.values()[type].toString());
        param.addProperty("@timestamp", CommTime.getTimeString8601(CommTime.nowSecond()));
        param.addProperty("MoneyType", "强化石");
        param.addProperty("MoneyNum", Integer.valueOf(num));
        param.addProperty("cur_value", Integer.valueOf(cur_remainder));
        param.addProperty("pre_value", Integer.valueOf(pre_value));
        param.addProperty("Reason", reason);
        param.addProperty("RoleID", Long.valueOf(pid));
        param.addProperty("ServerID", Integer.valueOf(ServerConfig.ServerID()));
        param.addProperty("vip_level", vip_level);
        param.addProperty("level", level);
        param.addProperty("world", System.getProperty("world_sid", "0"));
        RedisFlowMgr.getInstance().add(param);
    }

    public void warspiritTalentMaterialChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
        JsonObject param = new JsonObject();
        param.addProperty("srv", "lncq");
        param.addProperty("type", "warspirittalentmaterial");
        param.addProperty("gaintype", ConstEnum.ResdisType.values()[type].toString());
        param.addProperty("@timestamp", CommTime.getTimeString8601(CommTime.nowSecond()));
        param.addProperty("MoneyType", "战灵天赋丹");
        param.addProperty("MoneyNum", Integer.valueOf(num));
        param.addProperty("cur_value", Integer.valueOf(cur_remainder));
        param.addProperty("pre_value", Integer.valueOf(pre_value));
        param.addProperty("Reason", reason);
        param.addProperty("RoleID", Long.valueOf(pid));
        param.addProperty("ServerID", Integer.valueOf(ServerConfig.ServerID()));
        param.addProperty("vip_level", vip_level);
        param.addProperty("level", level);
        param.addProperty("world", System.getProperty("world_sid", "0"));
        RedisFlowMgr.getInstance().add(param);
    }

    public void wingMaterialChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
        JsonObject param = new JsonObject();
        param.addProperty("srv", "lncq");
        param.addProperty("type", "wingmaterial");
        param.addProperty("gaintype", ConstEnum.ResdisType.values()[type].toString());
        param.addProperty("@timestamp", CommTime.getTimeString8601(CommTime.nowSecond()));
        param.addProperty("MoneyType", "羽毛");
        param.addProperty("MoneyNum", Integer.valueOf(num));
        param.addProperty("cur_value", Integer.valueOf(cur_remainder));
        param.addProperty("pre_value", Integer.valueOf(pre_value));
        param.addProperty("Reason", reason);
        param.addProperty("RoleID", Long.valueOf(pid));
        param.addProperty("ServerID", Integer.valueOf(ServerConfig.ServerID()));
        param.addProperty("vip_level", vip_level);
        param.addProperty("level", level);
        param.addProperty("world", System.getProperty("world_sid", "0"));
        RedisFlowMgr.getInstance().add(param);
    }

    public void lotteryChargeLog(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
        JsonObject param = new JsonObject();
        param.addProperty("srv", "lncq");
        param.addProperty("type", "lottery");
        param.addProperty("gaintype", ConstEnum.ResdisType.values()[type].toString());
        param.addProperty("@timestamp", CommTime.getTimeString8601(CommTime.nowSecond()));
        param.addProperty("MoneyType", "奖券");
        param.addProperty("MoneyNum", Integer.valueOf(num));
        param.addProperty("cur_value", Integer.valueOf(cur_remainder));
        param.addProperty("pre_value", Integer.valueOf(pre_value));
        param.addProperty("Reason", reason);
        param.addProperty("RoleID", Long.valueOf(pid));
        param.addProperty("ServerID", Integer.valueOf(ServerConfig.ServerID()));
        param.addProperty("vip_level", vip_level);
        param.addProperty("level", level);
        param.addProperty("world", System.getProperty("world_sid", "0"));
        RedisFlowMgr.getInstance().add(param);
    }

    public void itemLog(long pid, int vip_level, int level, int itemId, int reason, int num, int cur_remainder, int pre_value, int type) {
        JsonObject param = new JsonObject();
        param.addProperty("srv", "lncq");
        param.addProperty("type", "item");
        param.addProperty("gaintype", ConstEnum.ResdisType.values()[type].toString());
        param.addProperty("@timestamp", CommTime.getTimeString8601(CommTime.nowSecond()));
        param.addProperty("MoneyType", "道具");
        param.addProperty("MoneyNum", Integer.valueOf(num));
        param.addProperty("ItemId", Integer.valueOf(itemId));
        param.addProperty("cur_value", Integer.valueOf(cur_remainder));
        param.addProperty("pre_value", Integer.valueOf(pre_value));
        param.addProperty("Reason", reason);
        param.addProperty("RoleID", Long.valueOf(pid));
        param.addProperty("ServerID", Integer.valueOf(ServerConfig.ServerID()));
        param.addProperty("vip_level", vip_level);
        param.addProperty("level", level);
        param.addProperty("world", System.getProperty("world_sid", "0"));
        RedisFlowMgr.getInstance().add(param);
    }
}

