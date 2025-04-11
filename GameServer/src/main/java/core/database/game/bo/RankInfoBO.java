package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RankInfoBO
        extends BaseBO {
    @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
    private long id;
    @DataBaseField(type = "int(11)", fieldname = "aid", comment = "记录数据ID[对应于RankSystemType枚举值]")
    private int aid;
    @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "*所属玩家ID")
    private long pid;
    @DataBaseField(type = "bigint(20)", fieldname = "value", comment = "数值")
    private long value;
    @DataBaseField(type = "int(11)", fieldname = "updateTime", comment = "更新时间")
    private int updateTime;
    @DataBaseField(type = "int(11)", fieldname = "ext1", comment = "扩展字段1")
    private int ext1;
    @DataBaseField(type = "int(11)", fieldname = "ext2", comment = "扩展字段2")
    private int ext2;
    @DataBaseField(type = "varchar(200)", fieldname = "ext3", comment = "扩展字段3")
    private String ext3;
    @DataBaseField(type = "varchar(200)", fieldname = "ext4", comment = "扩展字段4")
    private String ext4;
    @DataBaseField(type = "int(11)", fieldname = "ext5", comment = "扩展字段5")
    private int ext5;

    public RankInfoBO() {
        this.id = 0L;
        this.aid = 0;
        this.pid = 0L;
        this.value = 0L;
        this.updateTime = 0;
        this.ext1 = 0;
        this.ext2 = 0;
        this.ext3 = "";
        this.ext4 = "";
        this.ext5 = 0;
    }

    public RankInfoBO(ResultSet rs) throws Exception {
        this.id = rs.getLong(1);
        this.aid = rs.getInt(2);
        this.pid = rs.getLong(3);
        this.value = rs.getLong(4);
        this.updateTime = rs.getInt(5);
        this.ext1 = rs.getInt(6);
        this.ext2 = rs.getInt(7);
        this.ext3 = rs.getString(8);
        this.ext4 = rs.getString(9);
        this.ext5 = rs.getInt(10);
    }

    public static String getSql_TableCreate() {
        String sql = "CREATE TABLE IF NOT EXISTS `rankInfo` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`aid` int(11) NOT NULL DEFAULT '0' COMMENT '记录数据ID[对应于RankSystemType枚举值]',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '*所属玩家ID',`value` bigint(20) NOT NULL DEFAULT '0' COMMENT '数值',`updateTime` int(11) NOT NULL DEFAULT '0' COMMENT '更新时间',`ext1` int(11) NOT NULL DEFAULT '0' COMMENT '扩展字段1',`ext2` int(11) NOT NULL DEFAULT '0' COMMENT '扩展字段2',`ext3` varchar(200) NOT NULL DEFAULT '' COMMENT '扩展字段3',`ext4` varchar(200) NOT NULL DEFAULT '' COMMENT '扩展字段4',`ext5` int(11) NOT NULL DEFAULT '0' COMMENT '扩展字段5',PRIMARY KEY (`id`)) COMMENT='排行表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

                ServerConfig.getInitialID() + 1L);
        return sql;
    }

    public void getFromResultSet(ResultSet rs, List<RankInfoBO> list) throws Exception {
        list.add(new RankInfoBO(rs));
    }

    public long getAsynTaskTag() {
        return 0L;
    }

    public String getItemsName() {
        return "`id`, `aid`, `pid`, `value`, `updateTime`, `ext1`, `ext2`, `ext3`, `ext4`, `ext5`";
    }

    public String getTableName() {
        return "`rankInfo`";
    }

    public String getItemsValue() {
        StringBuilder strBuf = new StringBuilder();
        strBuf.append("'").append(this.id).append("', ");
        strBuf.append("'").append(this.aid).append("', ");
        strBuf.append("'").append(this.pid).append("', ");
        strBuf.append("'").append(this.value).append("', ");
        strBuf.append("'").append(this.updateTime).append("', ");
        strBuf.append("'").append(this.ext1).append("', ");
        strBuf.append("'").append(this.ext2).append("', ");
        strBuf.append("'").append((this.ext3 == null) ? null : this.ext3.replace("'", "''")).append("', ");
        strBuf.append("'").append((this.ext4 == null) ? null : this.ext4.replace("'", "''")).append("', ");
        strBuf.append("'").append(this.ext5).append("', ");
        strBuf.deleteCharAt(strBuf.length() - 2);
        return strBuf.toString();
    }

    public ArrayList<byte[]> getInsertValueBytes() {
        ArrayList<byte[]> ret = (ArrayList) new ArrayList<>();
        return ret;
    }

    public long getId() {
        return this.id;
    }

    public void setId(long iID) {
        this.id = iID;
    }

    public int getAid() {
        return this.aid;
    }

    public void setAid(int aid) {
        if (aid == this.aid)
            return;
        this.aid = aid;
    }

    public void saveAid(int aid) {
        if (aid == this.aid)
            return;
        this.aid = aid;
        saveField("aid", Integer.valueOf(aid));
    }

    public long getPid() {
        return this.pid;
    }

    public void setPid(long pid) {
        if (pid == this.pid)
            return;
        this.pid = pid;
    }

    public void savePid(long pid) {
        if (pid == this.pid)
            return;
        this.pid = pid;
        saveField("pid", Long.valueOf(pid));
    }

    public long getValue() {
        return this.value;
    }

    public void setValue(long value) {
        if (value == this.value)
            return;
        this.value = value;
    }

    public void saveValue(long value) {
        if (value == this.value)
            return;
        this.value = value;
        saveField("value", Long.valueOf(value));
    }

    public int getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(int updateTime) {
        if (updateTime == this.updateTime)
            return;
        this.updateTime = updateTime;
    }

    public void saveUpdateTime(int updateTime) {
        if (updateTime == this.updateTime)
            return;
        this.updateTime = updateTime;
        saveField("updateTime", Integer.valueOf(updateTime));
    }

    public int getExt1() {
        return this.ext1;
    }

    public void setExt1(int ext1) {
        if (ext1 == this.ext1)
            return;
        this.ext1 = ext1;
    }

    public void saveExt1(int ext1) {
        if (ext1 == this.ext1)
            return;
        this.ext1 = ext1;
        saveField("ext1", Integer.valueOf(ext1));
    }

    public int getExt2() {
        return this.ext2;
    }

    public void setExt2(int ext2) {
        if (ext2 == this.ext2)
            return;
        this.ext2 = ext2;
    }

    public void saveExt2(int ext2) {
        if (ext2 == this.ext2)
            return;
        this.ext2 = ext2;
        saveField("ext2", Integer.valueOf(ext2));
    }

    public String getExt3() {
        return this.ext3;
    }

    public void setExt3(String ext3) {
        if (ext3.equals(this.ext3))
            return;
        this.ext3 = ext3;
    }

    public void saveExt3(String ext3) {
        if (ext3.equals(this.ext3))
            return;
        this.ext3 = ext3;
        saveField("ext3", ext3);
    }

    public String getExt4() {
        return this.ext4;
    }

    public void setExt4(String ext4) {
        if (ext4.equals(this.ext4))
            return;
        this.ext4 = ext4;
    }

    public void saveExt4(String ext4) {
        if (ext4.equals(this.ext4))
            return;
        this.ext4 = ext4;
        saveField("ext4", ext4);
    }

    public int getExt5() {
        return this.ext5;
    }

    public void setExt5(int ext5) {
        if (ext5 == this.ext5)
            return;
        this.ext5 = ext5;
    }

    public void saveExt5(int ext5) {
        if (ext5 == this.ext5)
            return;
        this.ext5 = ext5;
        saveField("ext5", Integer.valueOf(ext5));
    }

    protected String getUpdateKeyValue() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(" `aid` = '").append(this.aid).append("',");
        sBuilder.append(" `pid` = '").append(this.pid).append("',");
        sBuilder.append(" `value` = '").append(this.value).append("',");
        sBuilder.append(" `updateTime` = '").append(this.updateTime).append("',");
        sBuilder.append(" `ext1` = '").append(this.ext1).append("',");
        sBuilder.append(" `ext2` = '").append(this.ext2).append("',");
        sBuilder.append(" `ext3` = '").append((this.ext3 == null) ? null : this.ext3.replace("'", "''")).append("',");
        sBuilder.append(" `ext4` = '").append((this.ext4 == null) ? null : this.ext4.replace("'", "''")).append("',");
        sBuilder.append(" `ext5` = '").append(this.ext5).append("',");
        sBuilder.deleteCharAt(sBuilder.length() - 1);
        return sBuilder.toString();
    }
}

