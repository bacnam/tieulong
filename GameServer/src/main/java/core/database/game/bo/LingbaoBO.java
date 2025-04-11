package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class LingbaoBO
        extends BaseBO {
    @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
    private long id;
    @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家id")
    private long pid;
    @DataBaseField(type = "int(11)", fieldname = "level", comment = "灵宝等级")
    private int level;
    @DataBaseField(type = "int(11)", fieldname = "exp", comment = "灵宝经验")
    private int exp;

    public LingbaoBO() {
        this.id = 0L;
        this.pid = 0L;
        this.level = 0;
        this.exp = 0;
    }

    public LingbaoBO(ResultSet rs) throws Exception {
        this.id = rs.getLong(1);
        this.pid = rs.getLong(2);
        this.level = rs.getInt(3);
        this.exp = rs.getInt(4);
    }

    public static String getSql_TableCreate() {
        String sql = "CREATE TABLE IF NOT EXISTS `lingbao` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家id',`level` int(11) NOT NULL DEFAULT '0' COMMENT '灵宝等级',`exp` int(11) NOT NULL DEFAULT '0' COMMENT '灵宝经验',PRIMARY KEY (`id`)) COMMENT='灵宝表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

                ServerConfig.getInitialID() + 1L);
        return sql;
    }

    public void getFromResultSet(ResultSet rs, List<LingbaoBO> list) throws Exception {
        list.add(new LingbaoBO(rs));
    }

    public long getAsynTaskTag() {
        return getId();
    }

    public String getItemsName() {
        return "`id`, `pid`, `level`, `exp`";
    }

    public String getTableName() {
        return "`lingbao`";
    }

    public String getItemsValue() {
        StringBuilder strBuf = new StringBuilder();
        strBuf.append("'").append(this.id).append("', ");
        strBuf.append("'").append(this.pid).append("', ");
        strBuf.append("'").append(this.level).append("', ");
        strBuf.append("'").append(this.exp).append("', ");
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

    public int getExp() {
        return this.exp;
    }

    public void setExp(int exp) {
        if (exp == this.exp)
            return;
        this.exp = exp;
    }

    public void saveExp(int exp) {
        if (exp == this.exp)
            return;
        this.exp = exp;
        saveField("exp", Integer.valueOf(exp));
    }

    protected String getUpdateKeyValue() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(" `pid` = '").append(this.pid).append("',");
        sBuilder.append(" `level` = '").append(this.level).append("',");
        sBuilder.append(" `exp` = '").append(this.exp).append("',");
        sBuilder.deleteCharAt(sBuilder.length() - 1);
        return sBuilder.toString();
    }
}

