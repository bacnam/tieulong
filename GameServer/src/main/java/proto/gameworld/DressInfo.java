package proto.gameworld;

import com.zhonglian.server.common.db.annotation.DataBaseField;

public class DressInfo {
  DressBO dressbo;

  int dressLeftTime;

  private static class DressBO {
    @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
    private long id;

    @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家id")
    private long pid;

    @DataBaseField(type = "int(11)", fieldname = "dress_id", comment = "时装id")
    private int dress_id;

    @DataBaseField(type = "int(11)", fieldname = "type", comment = "时装类型")
    private int type;

    @DataBaseField(type = "int(11)", fieldname = "char_id", comment = "装备的角色")
    private int char_id;

    @DataBaseField(type = "int(11)", fieldname = "active_time", comment = "激活时间")
    private int active_time;

    @DataBaseField(type = "int(11)", fieldname = "equip_time", comment = "穿戴时间")
    private int equip_time;
  }
}

