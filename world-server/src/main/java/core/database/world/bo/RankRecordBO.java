package core.database.world.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RankRecordBO extends BaseBO {
    @DataBaseField(
        type = "bigint(20)",
        fieldname = "id",
        comment = "数据库表唯一键值"
    )
    private long id;
    @DataBaseField(
        type = "int(11)",
        fieldname = "type",
        comment = "排行榜类型"
    )
    private int type;
    @DataBaseField(
        type = "bigint(20)",
        fieldname = "owner",
        comment = "所属者id"
    )
    private long owner;
    @DataBaseField(
        type = "int(11)",
        fieldname = "serverId",
        comment = "服务器id"
    )
    private int serverId;
    @DataBaseField(
        type = "bigint(20)",
        fieldname = "value",
        comment = "值"
    )
    private long value;
    @DataBaseField(
        type = "int(11)",
        fieldname = "updateTime",
        comment = "更新时间"
    )
    private int updateTime;
    @DataBaseField(
        type = "bigint(20)",
        fieldname = "ext1",
        comment = "扩展字段1"
    )
    private long ext1;
    @DataBaseField(
        type = "bigint(20)",
        fieldname = "ext2",
        comment = "扩展字段2"
    )
    private long ext2;
    @DataBaseField(
        type = "bigint(20)",
        fieldname = "ext3",
        comment = "扩展字段3"
    )
    private long ext3;
    @DataBaseField(
        type = "varchar(200)",
        fieldname = "ext4",
        comment = "扩展字段4"
    )
    private String ext4;
    @DataBaseField(
        type = "varchar(200)",
        fieldname = "ext5",
        comment = "扩展字段5"
    )
    private String ext5;
    @DataBaseField(
        type = "varchar(500)",
        fieldname = "name",
        comment = "姓名"
    )
    private String name;
    @DataBaseField(
        type = "int(11)",
        fieldname = "lv",
        comment = "等级"
    )
    private int lv;
    @DataBaseField(
        type = "int(11)",
        fieldname = "icon",
        comment = "头像"
    )
    private int icon;
    @DataBaseField(
        type = "int(11)",
        fieldname = "vipLv",
        comment = "vip"
    )
    private int vipLv;
    @DataBaseField(
        type = "int(11)",
        fieldname = "maxPower",
        comment = "战斗力"
    )
    private int maxPower;

    public RankRecordBO() {
        this.id = 0L;
        this.type = 0;
        this.owner = 0L;
        this.serverId = 0;
        this.value = 0L;
        this.updateTime = 0;
        this.ext1 = 0L;
        this.ext2 = 0L;
        this.ext3 = 0L;
        this.ext4 = "";
        this.ext5 = "";
        this.name = "";
        this.lv = 0;
        this.icon = 0;
        this.vipLv = 0;
        this.maxPower = 0;
    }

    public RankRecordBO(ResultSet rs) throws Exception {
        this.id = rs.getLong(1);
        this.type = rs.getInt(2);
        this.owner = rs.getLong(3);
        this.serverId = rs.getInt(4);
        this.value = rs.getLong(5);
        this.updateTime = rs.getInt(6);
        this.ext1 = rs.getLong(7);
        this.ext2 = rs.getLong(8);
        this.ext3 = rs.getLong(9);
        this.ext4 = rs.getString(10);
        this.ext5 = rs.getString(11);
        this.name = rs.getString(12);
        this.lv = rs.getInt(13);
        this.icon = rs.getInt(14);
        this.vipLv = rs.getInt(15);
        this.maxPower = rs.getInt(16);
    }

    @Override
    public void getFromResultSet(ResultSet rs, List list) throws Exception {
        list.add(new RankRecordBO(rs));
    }

    @Override
    public long getAsynTaskTag() {
        return 0L;
    }

    @Override
    public String getItemsName() {
        return "`id`, `type`, `owner`, `serverId`, `value`, `updateTime`, `ext1`, `ext2`, `ext3`, `ext4`, `ext5`, `name`, `lv`, `icon`, `vipLv`, `maxPower`";
    }

    @Override
    public String getTableName() {
        return "`rank_record`";
    }

    @Override
    public String getItemsValue() {
        StringBuilder strBuf = new StringBuilder();
        strBuf.append("'").append(this.id).append("', ");
        strBuf.append("'").append(this.type).append("', ");
        strBuf.append("'").append(this.owner).append("', ");
        strBuf.append("'").append(this.serverId).append("', ");
        strBuf.append("'").append(this.value).append("', ");
        strBuf.append("'").append(this.updateTime).append("', ");
        strBuf.append("'").append(this.ext1).append("', ");
        strBuf.append("'").append(this.ext2).append("', ");
        strBuf.append("'").append(this.ext3).append("', ");
        strBuf.append("'").append(this.ext4 == null ? null : this.ext4.replace("'", "''")).append("', ");
        strBuf.append("'").append(this.ext5 == null ? null : this.ext5.replace("'", "''")).append("', ");
        strBuf.append("'").append(this.name == null ? null : this.name.replace("'", "''")).append("', ");
        strBuf.append("'").append(this.lv).append("', ");
        strBuf.append("'").append(this.icon).append("', ");
        strBuf.append("'").append(this.vipLv).append("', ");
        strBuf.append("'").append(this.maxPower).append("', ");
        strBuf.deleteCharAt(strBuf.length() - 2);
        return strBuf.toString();
    }

    @Override
    public ArrayList<byte[]> getInsertValueBytes() {
        return new ArrayList<>();
    }

    @Override
    public void setId(long iID) {
        this.id = iID;
    }

    @Override
    public long getId() {
        return this.id;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        if (type != this.type) {
            this.type = type;
        }
    }

    public void saveType(int type) {
        if (type != this.type) {
            this.type = type;
            this.saveField("type", type);
        }
    }

    public long getOwner() {
        return this.owner;
    }

    public void setOwner(long owner) {
        if (owner != this.owner) {
            this.owner = owner;
        }
    }

    public void saveOwner(long owner) {
        if (owner != this.owner) {
            this.owner = owner;
            this.saveField("owner", owner);
        }
    }

    public int getServerId() {
        return this.serverId;
    }

    public void setServerId(int serverId) {
        if (serverId != this.serverId) {
            this.serverId = serverId;
        }
    }

    public void saveServerId(int serverId) {
        if (serverId != this.serverId) {
            this.serverId = serverId;
            this.saveField("serverId", serverId);
        }
    }

    public long getValue() {
        return this.value;
    }

    public void setValue(long value) {
        if (value != this.value) {
            this.value = value;
        }
    }

    public void saveValue(long value) {
        if (value != this.value) {
            this.value = value;
            this.saveField("value", value);
        }
    }

    public int getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(int updateTime) {
        if (updateTime != this.updateTime) {
            this.updateTime = updateTime;
        }
    }

    public void saveUpdateTime(int updateTime) {
        if (updateTime != this.updateTime) {
            this.updateTime = updateTime;
            this.saveField("updateTime", updateTime);
        }
    }

    public long getExt1() {
        return this.ext1;
    }

    public void setExt1(long ext1) {
        if (ext1 != this.ext1) {
            this.ext1 = ext1;
        }
    }

    public void saveExt1(long ext1) {
        if (ext1 != this.ext1) {
            this.ext1 = ext1;
            this.saveField("ext1", ext1);
        }
    }

    public long getExt2() {
        return this.ext2;
    }

    public void setExt2(long ext2) {
        if (ext2 != this.ext2) {
            this.ext2 = ext2;
        }
    }

    public void saveExt2(long ext2) {
        if (ext2 != this.ext2) {
            this.ext2 = ext2;
            this.saveField("ext2", ext2);
        }
    }

    public long getExt3() {
        return this.ext3;
    }

    public void setExt3(long ext3) {
        if (ext3 != this.ext3) {
            this.ext3 = ext3;
        }
    }

    public void saveExt3(long ext3) {
        if (ext3 != this.ext3) {
            this.ext3 = ext3;
            this.saveField("ext3", ext3);
        }
    }

    public String getExt4() {
        return this.ext4;
    }

    public void setExt4(String ext4) {
        if (!ext4.equals(this.ext4)) {
            this.ext4 = ext4;
        }
    }

    public void saveExt4(String ext4) {
        if (!ext4.equals(this.ext4)) {
            this.ext4 = ext4;
            this.saveField("ext4", ext4);
        }
    }

    public String getExt5() {
        return this.ext5;
    }

    public void setExt5(String ext5) {
        if (!ext5.equals(this.ext5)) {
            this.ext5 = ext5;
        }
    }

    public void saveExt5(String ext5) {
        if (!ext5.equals(this.ext5)) {
            this.ext5 = ext5;
            this.saveField("ext5", ext5);
        }
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        if (!name.equals(this.name)) {
            this.name = name;
        }
    }

    public void saveName(String name) {
        if (!name.equals(this.name)) {
            this.name = name;
            this.saveField("name", name);
        }
    }

    public int getLv() {
        return this.lv;
    }

    public void setLv(int lv) {
        if (lv != this.lv) {
            this.lv = lv;
        }
    }

    public void saveLv(int lv) {
        if (lv != this.lv) {
            this.lv = lv;
            this.saveField("lv", lv);
        }
    }

    public int getIcon() {
        return this.icon;
    }

    public void setIcon(int icon) {
        if (icon != this.icon) {
            this.icon = icon;
        }
    }

    public void saveIcon(int icon) {
        if (icon != this.icon) {
            this.icon = icon;
            this.saveField("icon", icon);
        }
    }

    public int getVipLv() {
        return this.vipLv;
    }

    public void setVipLv(int vipLv) {
        if (vipLv != this.vipLv) {
            this.vipLv = vipLv;
        }
    }

    public void saveVipLv(int vipLv) {
        if (vipLv != this.vipLv) {
            this.vipLv = vipLv;
            this.saveField("vipLv", vipLv);
        }
    }

    public int getMaxPower() {
        return this.maxPower;
    }

    public void setMaxPower(int maxPower) {
        if (maxPower != this.maxPower) {
            this.maxPower = maxPower;
        }
    }

    public void saveMaxPower(int maxPower) {
        if (maxPower != this.maxPower) {
            this.maxPower = maxPower;
            this.saveField("maxPower", maxPower);
        }
    }

    @Override
    protected String getUpdateKeyValue() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(" `type` = '").append(this.type).append("',");
        sBuilder.append(" `owner` = '").append(this.owner).append("',");
        sBuilder.append(" `serverId` = '").append(this.serverId).append("',");
        sBuilder.append(" `value` = '").append(this.value).append("',");
        sBuilder.append(" `updateTime` = '").append(this.updateTime).append("',");
        sBuilder.append(" `ext1` = '").append(this.ext1).append("',");
        sBuilder.append(" `ext2` = '").append(this.ext2).append("',");
        sBuilder.append(" `ext3` = '").append(this.ext3).append("',");
        sBuilder.append(" `ext4` = '").append(this.ext4 == null ? null : this.ext4.replace("'", "''")).append("',");
        sBuilder.append(" `ext5` = '").append(this.ext5 == null ? null : this.ext5.replace("'", "''")).append("',");
        sBuilder.append(" `name` = '").append(this.name == null ? null : this.name.replace("'", "''")).append("',");
        sBuilder.append(" `lv` = '").append(this.lv).append("',");
        sBuilder.append(" `icon` = '").append(this.icon).append("',");
        sBuilder.append(" `vipLv` = '").append(this.vipLv).append("',");
        sBuilder.append(" `maxPower` = '").append(this.maxPower).append("',");
        sBuilder.deleteCharAt(sBuilder.length() - 1);
        return sBuilder.toString();
    }

    public static String getSql_TableCreate() {
        return "CREATE TABLE IF NOT EXISTS `rank_record` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`type` int(11) NOT NULL DEFAULT '0' COMMENT '排行榜类型',`owner` bigint(20) NOT NULL DEFAULT '0' COMMENT '所属者id',`serverId` int(11) NOT NULL DEFAULT '0' COMMENT '服务器id',`value` bigint(20) NOT NULL DEFAULT '0' COMMENT '值',`updateTime` int(11) NOT NULL DEFAULT '0' COMMENT '更新时间',`ext1` bigint(20) NOT NULL DEFAULT '0' COMMENT '扩展字段1',`ext2` bigint(20) NOT NULL DEFAULT '0' COMMENT '扩展字段2',`ext3` bigint(20) NOT NULL DEFAULT '0' COMMENT '扩展字段3',`ext4` varchar(200) NOT NULL DEFAULT '' COMMENT '扩展字段4',`ext5` varchar(200) NOT NULL DEFAULT '' COMMENT '扩展字段5',`name` varchar(500) NOT NULL DEFAULT '' COMMENT '姓名',`lv` int(11) NOT NULL DEFAULT '0' COMMENT '等级',`icon` int(11) NOT NULL DEFAULT '0' COMMENT '头像',`vipLv` int(11) NOT NULL DEFAULT '0' COMMENT 'vip',`maxPower` int(11) NOT NULL DEFAULT '0' COMMENT '战斗力',PRIMARY KEY (`id`)) COMMENT='排行表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT="
            + (ServerConfig.getInitialID() + 1L);
    }
}
