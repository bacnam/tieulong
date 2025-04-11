package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RichRiskAvatarBO
        extends BaseBO {
    @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
    private long id;
    @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家pid")
    private long pid;
    @DataBaseField(type = "bigint(20)", fieldname = "avatarPid", comment = "路霸玩家pid")
    private long avatarPid;
    @DataBaseField(type = "int(11)", fieldname = "x", comment = "所在坐标的x")
    private int x;
    @DataBaseField(type = "int(11)", fieldname = "y", comment = "所在坐标的y")
    private int y;
    @DataBaseField(type = "tinyint(1)", fieldname = "isAlive", comment = "路霸是否已经被击杀")
    private boolean isAlive;

    public RichRiskAvatarBO() {
        this.id = 0L;
        this.pid = 0L;
        this.avatarPid = 0L;
        this.x = 0;
        this.y = 0;
        this.isAlive = false;
    }

    public RichRiskAvatarBO(ResultSet rs) throws Exception {
        this.id = rs.getLong(1);
        this.pid = rs.getLong(2);
        this.avatarPid = rs.getLong(3);
        this.x = rs.getInt(4);
        this.y = rs.getInt(5);
        this.isAlive = rs.getBoolean(6);
    }

    public static String getSql_TableCreate() {
        String sql = "CREATE TABLE IF NOT EXISTS `richRiskAvatar` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家pid',`avatarPid` bigint(20) NOT NULL DEFAULT '0' COMMENT '路霸玩家pid',`x` int(11) NOT NULL DEFAULT '0' COMMENT '所在坐标的x',`y` int(11) NOT NULL DEFAULT '0' COMMENT '所在坐标的y',`isAlive` tinyint(1) NOT NULL DEFAULT '0' COMMENT '路霸是否已经被击杀',KEY `pid` (`pid`),PRIMARY KEY (`id`)) COMMENT='大富翁路霸表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

                ServerConfig.getInitialID() + 1L);
        return sql;
    }

    public void getFromResultSet(ResultSet rs, List<RichRiskAvatarBO> list) throws Exception {
        list.add(new RichRiskAvatarBO(rs));
    }

    public long getAsynTaskTag() {
        return getPid();
    }

    public String getItemsName() {
        return "`id`, `pid`, `avatarPid`, `x`, `y`, `isAlive`";
    }

    public String getTableName() {
        return "`richRiskAvatar`";
    }

    public String getItemsValue() {
        StringBuilder strBuf = new StringBuilder();
        strBuf.append("'").append(this.id).append("', ");
        strBuf.append("'").append(this.pid).append("', ");
        strBuf.append("'").append(this.avatarPid).append("', ");
        strBuf.append("'").append(this.x).append("', ");
        strBuf.append("'").append(this.y).append("', ");
        strBuf.append("'").append(this.isAlive ? 1 : 0).append("', ");
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

    public long getAvatarPid() {
        return this.avatarPid;
    }

    public void setAvatarPid(long avatarPid) {
        if (avatarPid == this.avatarPid)
            return;
        this.avatarPid = avatarPid;
    }

    public void saveAvatarPid(long avatarPid) {
        if (avatarPid == this.avatarPid)
            return;
        this.avatarPid = avatarPid;
        saveField("avatarPid", Long.valueOf(avatarPid));
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        if (x == this.x)
            return;
        this.x = x;
    }

    public void saveX(int x) {
        if (x == this.x)
            return;
        this.x = x;
        saveField("x", Integer.valueOf(x));
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        if (y == this.y)
            return;
        this.y = y;
    }

    public void saveY(int y) {
        if (y == this.y)
            return;
        this.y = y;
        saveField("y", Integer.valueOf(y));
    }

    public boolean getIsAlive() {
        return this.isAlive;
    }

    public void setIsAlive(boolean isAlive) {
        if (isAlive == this.isAlive)
            return;
        this.isAlive = isAlive;
    }

    public void saveIsAlive(boolean isAlive) {
        if (isAlive == this.isAlive)
            return;
        this.isAlive = isAlive;
        saveField("isAlive", Integer.valueOf(isAlive ? 1 : 0));
    }

    protected String getUpdateKeyValue() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(" `pid` = '").append(this.pid).append("',");
        sBuilder.append(" `avatarPid` = '").append(this.avatarPid).append("',");
        sBuilder.append(" `x` = '").append(this.x).append("',");
        sBuilder.append(" `y` = '").append(this.y).append("',");
        sBuilder.append(" `isAlive` = '").append(this.isAlive ? 1 : 0).append("',");
        sBuilder.deleteCharAt(sBuilder.length() - 1);
        return sBuilder.toString();
    }
}

