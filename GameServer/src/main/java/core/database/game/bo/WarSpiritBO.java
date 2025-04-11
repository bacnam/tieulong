package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class WarSpiritBO
        extends BaseBO {
    @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
    private long id;
    @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家id")
    private long pid;
    @DataBaseField(type = "int(11)", fieldname = "spirit_id", comment = "战灵id")
    private int spirit_id;
    @DataBaseField(type = "int(11)", fieldname = "skill", comment = "技能等级")
    private int skill;
    @DataBaseField(type = "int(11)", fieldname = "star", comment = "星级")
    private int star;
    @DataBaseField(type = "tinyint(1)", fieldname = "is_selected", comment = "是否出战")
    private boolean is_selected;

    public WarSpiritBO() {
        this.id = 0L;
        this.pid = 0L;
        this.spirit_id = 0;
        this.skill = 0;
        this.star = 0;
        this.is_selected = false;
    }

    public WarSpiritBO(ResultSet rs) throws Exception {
        this.id = rs.getLong(1);
        this.pid = rs.getLong(2);
        this.spirit_id = rs.getInt(3);
        this.skill = rs.getInt(4);
        this.star = rs.getInt(5);
        this.is_selected = rs.getBoolean(6);
    }

    public static String getSql_TableCreate() {
        String sql = "CREATE TABLE IF NOT EXISTS `warSpirit` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家id',`spirit_id` int(11) NOT NULL DEFAULT '0' COMMENT '战灵id',`skill` int(11) NOT NULL DEFAULT '0' COMMENT '技能等级',`star` int(11) NOT NULL DEFAULT '0' COMMENT '星级',`is_selected` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否出战',PRIMARY KEY (`id`)) COMMENT='战灵信息表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

                ServerConfig.getInitialID() + 1L);
        return sql;
    }

    public void getFromResultSet(ResultSet rs, List<WarSpiritBO> list) throws Exception {
        list.add(new WarSpiritBO(rs));
    }

    public long getAsynTaskTag() {
        return getId();
    }

    public String getItemsName() {
        return "`id`, `pid`, `spirit_id`, `skill`, `star`, `is_selected`";
    }

    public String getTableName() {
        return "`warSpirit`";
    }

    public String getItemsValue() {
        StringBuilder strBuf = new StringBuilder();
        strBuf.append("'").append(this.id).append("', ");
        strBuf.append("'").append(this.pid).append("', ");
        strBuf.append("'").append(this.spirit_id).append("', ");
        strBuf.append("'").append(this.skill).append("', ");
        strBuf.append("'").append(this.star).append("', ");
        strBuf.append("'").append(this.is_selected ? 1 : 0).append("', ");
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

    public int getSpiritId() {
        return this.spirit_id;
    }

    public void setSpiritId(int spirit_id) {
        if (spirit_id == this.spirit_id)
            return;
        this.spirit_id = spirit_id;
    }

    public void saveSpiritId(int spirit_id) {
        if (spirit_id == this.spirit_id)
            return;
        this.spirit_id = spirit_id;
        saveField("spirit_id", Integer.valueOf(spirit_id));
    }

    public int getSkill() {
        return this.skill;
    }

    public void setSkill(int skill) {
        if (skill == this.skill)
            return;
        this.skill = skill;
    }

    public void saveSkill(int skill) {
        if (skill == this.skill)
            return;
        this.skill = skill;
        saveField("skill", Integer.valueOf(skill));
    }

    public int getStar() {
        return this.star;
    }

    public void setStar(int star) {
        if (star == this.star)
            return;
        this.star = star;
    }

    public void saveStar(int star) {
        if (star == this.star)
            return;
        this.star = star;
        saveField("star", Integer.valueOf(star));
    }

    public boolean getIsSelected() {
        return this.is_selected;
    }

    public void setIsSelected(boolean is_selected) {
        if (is_selected == this.is_selected)
            return;
        this.is_selected = is_selected;
    }

    public void saveIsSelected(boolean is_selected) {
        if (is_selected == this.is_selected)
            return;
        this.is_selected = is_selected;
        saveField("is_selected", Integer.valueOf(is_selected ? 1 : 0));
    }

    protected String getUpdateKeyValue() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(" `pid` = '").append(this.pid).append("',");
        sBuilder.append(" `spirit_id` = '").append(this.spirit_id).append("',");
        sBuilder.append(" `skill` = '").append(this.skill).append("',");
        sBuilder.append(" `star` = '").append(this.star).append("',");
        sBuilder.append(" `is_selected` = '").append(this.is_selected ? 1 : 0).append("',");
        sBuilder.deleteCharAt(sBuilder.length() - 1);
        return sBuilder.toString();
    }
}

