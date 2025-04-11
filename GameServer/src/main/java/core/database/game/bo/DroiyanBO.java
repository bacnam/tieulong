package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DroiyanBO
        extends BaseBO {
    @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
    private long id;
    @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家id")
    private long pid;
    @DataBaseField(type = "int(11)", fieldname = "point", comment = "决战积分")
    private int point;
    @DataBaseField(type = "int(11)", fieldname = "red", comment = "红名值")
    private int red;
    @DataBaseField(type = "int(11)", fieldname = "win_times", comment = "连胜次数")
    private int win_times;
    @DataBaseField(type = "bigint(20)", size = 3, fieldname = "fighters", comment = "路人")
    private List<Long> fighters;
    @DataBaseField(type = "int(11)", fieldname = "last_search_time", comment = "最后检索时间")
    private int last_search_time;

    public DroiyanBO() {
        this.id = 0L;
        this.pid = 0L;
        this.point = 0;
        this.red = 0;
        this.win_times = 0;
        this.fighters = new ArrayList<>(3);
        for (int i = 0; i < 3; i++) {
            this.fighters.add(Long.valueOf(0L));
        }
        this.last_search_time = 0;
    }

    public DroiyanBO(ResultSet rs) throws Exception {
        this.id = rs.getLong(1);
        this.pid = rs.getLong(2);
        this.point = rs.getInt(3);
        this.red = rs.getInt(4);
        this.win_times = rs.getInt(5);
        this.fighters = new ArrayList<>(3);
        for (int i = 0; i < 3; i++) {
            this.fighters.add(Long.valueOf(rs.getLong(i + 6)));
        }
        this.last_search_time = rs.getInt(9);
    }

    public static String getSql_TableCreate() {
        String sql = "CREATE TABLE IF NOT EXISTS `droiyan` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家id',`point` int(11) NOT NULL DEFAULT '0' COMMENT '决战积分',`red` int(11) NOT NULL DEFAULT '0' COMMENT '红名值',`win_times` int(11) NOT NULL DEFAULT '0' COMMENT '连胜次数',`fighters_0` bigint(20) NOT NULL DEFAULT '0' COMMENT '路人',`fighters_1` bigint(20) NOT NULL DEFAULT '0' COMMENT '路人',`fighters_2` bigint(20) NOT NULL DEFAULT '0' COMMENT '路人',`last_search_time` int(11) NOT NULL DEFAULT '0' COMMENT '最后检索时间',PRIMARY KEY (`id`)) COMMENT='决战系统'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

                ServerConfig.getInitialID() + 1L);
        return sql;
    }

    public void getFromResultSet(ResultSet rs, List<DroiyanBO> list) throws Exception {
        list.add(new DroiyanBO(rs));
    }

    public long getAsynTaskTag() {
        return getId();
    }

    public String getItemsName() {
        return "`id`, `pid`, `point`, `red`, `win_times`, `fighters_0`, `fighters_1`, `fighters_2`, `last_search_time`";
    }

    public String getTableName() {
        return "`droiyan`";
    }

    public String getItemsValue() {
        StringBuilder strBuf = new StringBuilder();
        strBuf.append("'").append(this.id).append("', ");
        strBuf.append("'").append(this.pid).append("', ");
        strBuf.append("'").append(this.point).append("', ");
        strBuf.append("'").append(this.red).append("', ");
        strBuf.append("'").append(this.win_times).append("', ");
        for (int i = 0; i < this.fighters.size(); i++) {
            strBuf.append("'").append(this.fighters.get(i)).append("', ");
        }
        strBuf.append("'").append(this.last_search_time).append("', ");
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

    public int getPoint() {
        return this.point;
    }

    public void setPoint(int point) {
        if (point == this.point)
            return;
        this.point = point;
    }

    public void savePoint(int point) {
        if (point == this.point)
            return;
        this.point = point;
        saveField("point", Integer.valueOf(point));
    }

    public int getRed() {
        return this.red;
    }

    public void setRed(int red) {
        if (red == this.red)
            return;
        this.red = red;
    }

    public void saveRed(int red) {
        if (red == this.red)
            return;
        this.red = red;
        saveField("red", Integer.valueOf(red));
    }

    public int getWinTimes() {
        return this.win_times;
    }

    public void setWinTimes(int win_times) {
        if (win_times == this.win_times)
            return;
        this.win_times = win_times;
    }

    public void saveWinTimes(int win_times) {
        if (win_times == this.win_times)
            return;
        this.win_times = win_times;
        saveField("win_times", Integer.valueOf(win_times));
    }

    public int getFightersSize() {
        return this.fighters.size();
    }

    public List<Long> getFightersAll() {
        return new ArrayList<>(this.fighters);
    }

    public void setFightersAll(long value) {
        for (int i = 0; i < this.fighters.size(); ) {
            this.fighters.set(i, Long.valueOf(value));
            i++;
        }
    }

    public void saveFightersAll(long value) {
        setFightersAll(value);
        saveAll();
    }

    public long getFighters(int index) {
        return ((Long) this.fighters.get(index)).longValue();
    }

    public void setFighters(int index, long value) {
        if (value == ((Long) this.fighters.get(index)).longValue())
            return;
        this.fighters.set(index, Long.valueOf(value));
    }

    public void saveFighters(int index, long value) {
        if (value == ((Long) this.fighters.get(index)).longValue())
            return;
        this.fighters.set(index, Long.valueOf(value));
        saveField("fighters_" + index, this.fighters.get(index));
    }

    public int getLastSearchTime() {
        return this.last_search_time;
    }

    public void setLastSearchTime(int last_search_time) {
        if (last_search_time == this.last_search_time)
            return;
        this.last_search_time = last_search_time;
    }

    public void saveLastSearchTime(int last_search_time) {
        if (last_search_time == this.last_search_time)
            return;
        this.last_search_time = last_search_time;
        saveField("last_search_time", Integer.valueOf(last_search_time));
    }

    protected String getUpdateKeyValue() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(" `pid` = '").append(this.pid).append("',");
        sBuilder.append(" `point` = '").append(this.point).append("',");
        sBuilder.append(" `red` = '").append(this.red).append("',");
        sBuilder.append(" `win_times` = '").append(this.win_times).append("',");
        for (int i = 0; i < this.fighters.size(); i++) {
            sBuilder.append(" `fighters_").append(i).append("` = '").append(this.fighters.get(i)).append("',");
        }
        sBuilder.append(" `last_search_time` = '").append(this.last_search_time).append("',");
        sBuilder.deleteCharAt(sBuilder.length() - 1);
        return sBuilder.toString();
    }
}

