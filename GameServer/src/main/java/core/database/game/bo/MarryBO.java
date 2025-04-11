package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MarryBO
        extends BaseBO {
    @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
    private long id;
    @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家id")
    private long pid;
    @DataBaseField(type = "int(11)", fieldname = "sex", comment = "性别")
    private int sex;
    @DataBaseField(type = "int(11)", fieldname = "married", comment = "婚恋状态")
    private int married;
    @DataBaseField(type = "bigint(20)", fieldname = "lover_pid", comment = "伴侣pid")
    private long lover_pid;
    @DataBaseField(type = "int(11)", fieldname = "level", comment = "恩爱等级")
    private int level;
    @DataBaseField(type = "int(11)", fieldname = "exp", comment = "恩爱经验")
    private int exp;
    @DataBaseField(type = "int(11)", fieldname = "signin", comment = "签到天数")
    private int signin;
    @DataBaseField(type = "tinyint(1)", fieldname = "isSign", comment = "当天是否签到")
    private boolean isSign;
    @DataBaseField(type = "varchar(500)", fieldname = "signReward", comment = "签到已领奖励")
    private String signReward;
    @DataBaseField(type = "varchar(500)", fieldname = "levelReward", comment = "恩爱等级已领奖励")
    private String levelReward;

    public MarryBO() {
        this.id = 0L;
        this.pid = 0L;
        this.sex = 0;
        this.married = 0;
        this.lover_pid = 0L;
        this.level = 0;
        this.exp = 0;
        this.signin = 0;
        this.isSign = false;
        this.signReward = "";
        this.levelReward = "";
    }

    public MarryBO(ResultSet rs) throws Exception {
        this.id = rs.getLong(1);
        this.pid = rs.getLong(2);
        this.sex = rs.getInt(3);
        this.married = rs.getInt(4);
        this.lover_pid = rs.getLong(5);
        this.level = rs.getInt(6);
        this.exp = rs.getInt(7);
        this.signin = rs.getInt(8);
        this.isSign = rs.getBoolean(9);
        this.signReward = rs.getString(10);
        this.levelReward = rs.getString(11);
    }

    public static String getSql_TableCreate() {
        String sql = "CREATE TABLE IF NOT EXISTS `marry` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家id',`sex` int(11) NOT NULL DEFAULT '0' COMMENT '性别',`married` int(11) NOT NULL DEFAULT '0' COMMENT '婚恋状态',`lover_pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '伴侣pid',`level` int(11) NOT NULL DEFAULT '0' COMMENT '恩爱等级',`exp` int(11) NOT NULL DEFAULT '0' COMMENT '恩爱经验',`signin` int(11) NOT NULL DEFAULT '0' COMMENT '签到天数',`isSign` tinyint(1) NOT NULL DEFAULT '0' COMMENT '当天是否签到',`signReward` varchar(500) NOT NULL DEFAULT '' COMMENT '签到已领奖励',`levelReward` varchar(500) NOT NULL DEFAULT '' COMMENT '恩爱等级已领奖励',PRIMARY KEY (`id`)) COMMENT='婚恋系统'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

                ServerConfig.getInitialID() + 1L);
        return sql;
    }

    public void getFromResultSet(ResultSet rs, List<MarryBO> list) throws Exception {
        list.add(new MarryBO(rs));
    }

    public long getAsynTaskTag() {
        return getId();
    }

    public String getItemsName() {
        return "`id`, `pid`, `sex`, `married`, `lover_pid`, `level`, `exp`, `signin`, `isSign`, `signReward`, `levelReward`";
    }

    public String getTableName() {
        return "`marry`";
    }

    public String getItemsValue() {
        StringBuilder strBuf = new StringBuilder();
        strBuf.append("'").append(this.id).append("', ");
        strBuf.append("'").append(this.pid).append("', ");
        strBuf.append("'").append(this.sex).append("', ");
        strBuf.append("'").append(this.married).append("', ");
        strBuf.append("'").append(this.lover_pid).append("', ");
        strBuf.append("'").append(this.level).append("', ");
        strBuf.append("'").append(this.exp).append("', ");
        strBuf.append("'").append(this.signin).append("', ");
        strBuf.append("'").append(this.isSign ? 1 : 0).append("', ");
        strBuf.append("'").append((this.signReward == null) ? null : this.signReward.replace("'", "''")).append("', ");
        strBuf.append("'").append((this.levelReward == null) ? null : this.levelReward.replace("'", "''")).append("', ");
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

    public int getSex() {
        return this.sex;
    }

    public void setSex(int sex) {
        if (sex == this.sex)
            return;
        this.sex = sex;
    }

    public void saveSex(int sex) {
        if (sex == this.sex)
            return;
        this.sex = sex;
        saveField("sex", Integer.valueOf(sex));
    }

    public int getMarried() {
        return this.married;
    }

    public void setMarried(int married) {
        if (married == this.married)
            return;
        this.married = married;
    }

    public void saveMarried(int married) {
        if (married == this.married)
            return;
        this.married = married;
        saveField("married", Integer.valueOf(married));
    }

    public long getLoverPid() {
        return this.lover_pid;
    }

    public void setLoverPid(long lover_pid) {
        if (lover_pid == this.lover_pid)
            return;
        this.lover_pid = lover_pid;
    }

    public void saveLoverPid(long lover_pid) {
        if (lover_pid == this.lover_pid)
            return;
        this.lover_pid = lover_pid;
        saveField("lover_pid", Long.valueOf(lover_pid));
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

    public int getSignin() {
        return this.signin;
    }

    public void setSignin(int signin) {
        if (signin == this.signin)
            return;
        this.signin = signin;
    }

    public void saveSignin(int signin) {
        if (signin == this.signin)
            return;
        this.signin = signin;
        saveField("signin", Integer.valueOf(signin));
    }

    public boolean getIsSign() {
        return this.isSign;
    }

    public void setIsSign(boolean isSign) {
        if (isSign == this.isSign)
            return;
        this.isSign = isSign;
    }

    public void saveIsSign(boolean isSign) {
        if (isSign == this.isSign)
            return;
        this.isSign = isSign;
        saveField("isSign", Integer.valueOf(isSign ? 1 : 0));
    }

    public String getSignReward() {
        return this.signReward;
    }

    public void setSignReward(String signReward) {
        if (signReward.equals(this.signReward))
            return;
        this.signReward = signReward;
    }

    public void saveSignReward(String signReward) {
        if (signReward.equals(this.signReward))
            return;
        this.signReward = signReward;
        saveField("signReward", signReward);
    }

    public String getLevelReward() {
        return this.levelReward;
    }

    public void setLevelReward(String levelReward) {
        if (levelReward.equals(this.levelReward))
            return;
        this.levelReward = levelReward;
    }

    public void saveLevelReward(String levelReward) {
        if (levelReward.equals(this.levelReward))
            return;
        this.levelReward = levelReward;
        saveField("levelReward", levelReward);
    }

    protected String getUpdateKeyValue() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(" `pid` = '").append(this.pid).append("',");
        sBuilder.append(" `sex` = '").append(this.sex).append("',");
        sBuilder.append(" `married` = '").append(this.married).append("',");
        sBuilder.append(" `lover_pid` = '").append(this.lover_pid).append("',");
        sBuilder.append(" `level` = '").append(this.level).append("',");
        sBuilder.append(" `exp` = '").append(this.exp).append("',");
        sBuilder.append(" `signin` = '").append(this.signin).append("',");
        sBuilder.append(" `isSign` = '").append(this.isSign ? 1 : 0).append("',");
        sBuilder.append(" `signReward` = '").append((this.signReward == null) ? null : this.signReward.replace("'", "''")).append("',");
        sBuilder.append(" `levelReward` = '").append((this.levelReward == null) ? null : this.levelReward.replace("'", "''")).append("',");
        sBuilder.deleteCharAt(sBuilder.length() - 1);
        return sBuilder.toString();
    }
}

