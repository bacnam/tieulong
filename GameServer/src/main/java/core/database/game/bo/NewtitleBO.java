package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class NewtitleBO
        extends BaseBO {
    @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
    private long id;
    @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家id")
    private long pid;
    @DataBaseField(type = "int(11)", fieldname = "title_id", comment = "称号id")
    private int title_id;
    @DataBaseField(type = "int(11)", fieldname = "level", comment = "等级")
    private int level;
    @DataBaseField(type = "tinyint(1)", fieldname = "is_using", comment = "是否正在使用")
    private boolean is_using;
    @DataBaseField(type = "int(11)", fieldname = "active_time", comment = "激活时间")
    private int active_time;

    public NewtitleBO() {
        this.id = 0L;
        this.pid = 0L;
        this.title_id = 0;
        this.level = 0;
        this.is_using = false;
        this.active_time = 0;
    }

    public NewtitleBO(ResultSet rs) throws Exception {
        this.id = rs.getLong(1);
        this.pid = rs.getLong(2);
        this.title_id = rs.getInt(3);
        this.level = rs.getInt(4);
        this.is_using = rs.getBoolean(5);
        this.active_time = rs.getInt(6);
    }

    public static String getSql_TableCreate() {
        String sql = "CREATE TABLE IF NOT EXISTS `newtitle` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家id',`title_id` int(11) NOT NULL DEFAULT '0' COMMENT '称号id',`level` int(11) NOT NULL DEFAULT '0' COMMENT '等级',`is_using` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否正在使用',`active_time` int(11) NOT NULL DEFAULT '0' COMMENT '激活时间',PRIMARY KEY (`id`)) COMMENT='玩家信息表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

                ServerConfig.getInitialID() + 1L);
        return sql;
    }

    public void getFromResultSet(ResultSet rs, List<NewtitleBO> list) throws Exception {
        list.add(new NewtitleBO(rs));
    }

    public long getAsynTaskTag() {
        return getId();
    }

    public String getItemsName() {
        return "`id`, `pid`, `title_id`, `level`, `is_using`, `active_time`";
    }

    public String getTableName() {
        return "`newtitle`";
    }

    public String getItemsValue() {
        StringBuilder strBuf = new StringBuilder();
        strBuf.append("'").append(this.id).append("', ");
        strBuf.append("'").append(this.pid).append("', ");
        strBuf.append("'").append(this.title_id).append("', ");
        strBuf.append("'").append(this.level).append("', ");
        strBuf.append("'").append(this.is_using ? 1 : 0).append("', ");
        strBuf.append("'").append(this.active_time).append("', ");
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

    public int getTitleId() {
        return this.title_id;
    }

    public void setTitleId(int title_id) {
        if (title_id == this.title_id)
            return;
        this.title_id = title_id;
    }

    public void saveTitleId(int title_id) {
        if (title_id == this.title_id)
            return;
        this.title_id = title_id;
        saveField("title_id", Integer.valueOf(title_id));
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        if (level == this.level)
            return;
        this.level = level;
    }

    public void saveLevel(int level) {
        if (level == this.level)
            return;
        this.level = level;
        saveField("level", Integer.valueOf(level));
    }

    public boolean getIsUsing() {
        return this.is_using;
    }

    public void setIsUsing(boolean is_using) {
        if (is_using == this.is_using)
            return;
        this.is_using = is_using;
    }

    public void saveIsUsing(boolean is_using) {
        if (is_using == this.is_using)
            return;
        this.is_using = is_using;
        saveField("is_using", Integer.valueOf(is_using ? 1 : 0));
    }

    public int getActiveTime() {
        return this.active_time;
    }

    public void setActiveTime(int active_time) {
        if (active_time == this.active_time)
            return;
        this.active_time = active_time;
    }

    public void saveActiveTime(int active_time) {
        if (active_time == this.active_time)
            return;
        this.active_time = active_time;
        saveField("active_time", Integer.valueOf(active_time));
    }

    protected String getUpdateKeyValue() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(" `pid` = '").append(this.pid).append("',");
        sBuilder.append(" `title_id` = '").append(this.title_id).append("',");
        sBuilder.append(" `level` = '").append(this.level).append("',");
        sBuilder.append(" `is_using` = '").append(this.is_using ? 1 : 0).append("',");
        sBuilder.append(" `active_time` = '").append(this.active_time).append("',");
        sBuilder.deleteCharAt(sBuilder.length() - 1);
        return sBuilder.toString();
    }
}

