package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class FriendBO
        extends BaseBO {
    @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
    private long id;
    @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "所属玩家pid")
    private long pid;
    @DataBaseField(type = "bigint(20)", fieldname = "friendCId", comment = "好友玩家pid")
    private long friendCId;
    @DataBaseField(type = "tinyint(1)", fieldname = "isBlack", comment = "是否被加入黑名单")
    private boolean isBlack;
    @DataBaseField(type = "tinyint(1)", fieldname = "isDelete", comment = "是否被玩家删除")
    private boolean isDelete;
    @DataBaseField(type = "int(11)", fieldname = "lastGiveTime", comment = "最近赠送好友体力时间戳[秒]")
    private int lastGiveTime;
    @DataBaseField(type = "int(11)", fieldname = "giveTimes", comment = "赠送好友体力次数")
    private int giveTimes;
    @DataBaseField(type = "int(11)", fieldname = "lastRecvTime", comment = "最近领取好友体力时间戳[秒]")
    private int lastRecvTime;
    @DataBaseField(type = "int(11)", fieldname = "recvTimes", comment = "领取好友体力次数")
    private int recvTimes;
    @DataBaseField(type = "int(11)", fieldname = "createTime", comment = "成为好友时间")
    private int createTime;
    @DataBaseField(type = "int(11)", fieldname = "blackTime", comment = "拉黑好友时间")
    private int blackTime;

    public FriendBO() {
        this.id = 0L;
        this.pid = 0L;
        this.friendCId = 0L;
        this.isBlack = false;
        this.isDelete = false;
        this.lastGiveTime = 0;
        this.giveTimes = 0;
        this.lastRecvTime = 0;
        this.recvTimes = 0;
        this.createTime = 0;
        this.blackTime = 0;
    }

    public FriendBO(ResultSet rs) throws Exception {
        this.id = rs.getLong(1);
        this.pid = rs.getLong(2);
        this.friendCId = rs.getLong(3);
        this.isBlack = rs.getBoolean(4);
        this.isDelete = rs.getBoolean(5);
        this.lastGiveTime = rs.getInt(6);
        this.giveTimes = rs.getInt(7);
        this.lastRecvTime = rs.getInt(8);
        this.recvTimes = rs.getInt(9);
        this.createTime = rs.getInt(10);
        this.blackTime = rs.getInt(11);
    }

    public static String getSql_TableCreate() {
        String sql = "CREATE TABLE IF NOT EXISTS `friend` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '所属玩家pid',`friendCId` bigint(20) NOT NULL DEFAULT '0' COMMENT '好友玩家pid',`isBlack` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否被加入黑名单',`isDelete` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否被玩家删除',`lastGiveTime` int(11) NOT NULL DEFAULT '0' COMMENT '最近赠送好友体力时间戳[秒]',`giveTimes` int(11) NOT NULL DEFAULT '0' COMMENT '赠送好友体力次数',`lastRecvTime` int(11) NOT NULL DEFAULT '0' COMMENT '最近领取好友体力时间戳[秒]',`recvTimes` int(11) NOT NULL DEFAULT '0' COMMENT '领取好友体力次数',`createTime` int(11) NOT NULL DEFAULT '0' COMMENT '成为好友时间',`blackTime` int(11) NOT NULL DEFAULT '0' COMMENT '拉黑好友时间',KEY `pid` (`pid`),PRIMARY KEY (`id`)) COMMENT='好友信息表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (

                ServerConfig.getInitialID() + 1L);
        return sql;
    }

    public void getFromResultSet(ResultSet rs, List<FriendBO> list) throws Exception {
        list.add(new FriendBO(rs));
    }

    public long getAsynTaskTag() {
        return getPid();
    }

    public String getItemsName() {
        return "`id`, `pid`, `friendCId`, `isBlack`, `isDelete`, `lastGiveTime`, `giveTimes`, `lastRecvTime`, `recvTimes`, `createTime`, `blackTime`";
    }

    public String getTableName() {
        return "`friend`";
    }

    public String getItemsValue() {
        StringBuilder strBuf = new StringBuilder();
        strBuf.append("'").append(this.id).append("', ");
        strBuf.append("'").append(this.pid).append("', ");
        strBuf.append("'").append(this.friendCId).append("', ");
        strBuf.append("'").append(this.isBlack ? 1 : 0).append("', ");
        strBuf.append("'").append(this.isDelete ? 1 : 0).append("', ");
        strBuf.append("'").append(this.lastGiveTime).append("', ");
        strBuf.append("'").append(this.giveTimes).append("', ");
        strBuf.append("'").append(this.lastRecvTime).append("', ");
        strBuf.append("'").append(this.recvTimes).append("', ");
        strBuf.append("'").append(this.createTime).append("', ");
        strBuf.append("'").append(this.blackTime).append("', ");
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

    public long getFriendCId() {
        return this.friendCId;
    }

    public void setFriendCId(long friendCId) {
        if (friendCId == this.friendCId)
            return;
        this.friendCId = friendCId;
    }

    public void saveFriendCId(long friendCId) {
        if (friendCId == this.friendCId)
            return;
        this.friendCId = friendCId;
        saveField("friendCId", Long.valueOf(friendCId));
    }

    public boolean getIsBlack() {
        return this.isBlack;
    }

    public void setIsBlack(boolean isBlack) {
        if (isBlack == this.isBlack)
            return;
        this.isBlack = isBlack;
    }

    public void saveIsBlack(boolean isBlack) {
        if (isBlack == this.isBlack)
            return;
        this.isBlack = isBlack;
        saveField("isBlack", Integer.valueOf(isBlack ? 1 : 0));
    }

    public boolean getIsDelete() {
        return this.isDelete;
    }

    public void setIsDelete(boolean isDelete) {
        if (isDelete == this.isDelete)
            return;
        this.isDelete = isDelete;
    }

    public void saveIsDelete(boolean isDelete) {
        if (isDelete == this.isDelete)
            return;
        this.isDelete = isDelete;
        saveField("isDelete", Integer.valueOf(isDelete ? 1 : 0));
    }

    public int getLastGiveTime() {
        return this.lastGiveTime;
    }

    public void setLastGiveTime(int lastGiveTime) {
        if (lastGiveTime == this.lastGiveTime)
            return;
        this.lastGiveTime = lastGiveTime;
    }

    public void saveLastGiveTime(int lastGiveTime) {
        if (lastGiveTime == this.lastGiveTime)
            return;
        this.lastGiveTime = lastGiveTime;
        saveField("lastGiveTime", Integer.valueOf(lastGiveTime));
    }

    public int getGiveTimes() {
        return this.giveTimes;
    }

    public void setGiveTimes(int giveTimes) {
        if (giveTimes == this.giveTimes)
            return;
        this.giveTimes = giveTimes;
    }

    public void saveGiveTimes(int giveTimes) {
        if (giveTimes == this.giveTimes)
            return;
        this.giveTimes = giveTimes;
        saveField("giveTimes", Integer.valueOf(giveTimes));
    }

    public int getLastRecvTime() {
        return this.lastRecvTime;
    }

    public void setLastRecvTime(int lastRecvTime) {
        if (lastRecvTime == this.lastRecvTime)
            return;
        this.lastRecvTime = lastRecvTime;
    }

    public void saveLastRecvTime(int lastRecvTime) {
        if (lastRecvTime == this.lastRecvTime)
            return;
        this.lastRecvTime = lastRecvTime;
        saveField("lastRecvTime", Integer.valueOf(lastRecvTime));
    }

    public int getRecvTimes() {
        return this.recvTimes;
    }

    public void setRecvTimes(int recvTimes) {
        if (recvTimes == this.recvTimes)
            return;
        this.recvTimes = recvTimes;
    }

    public void saveRecvTimes(int recvTimes) {
        if (recvTimes == this.recvTimes)
            return;
        this.recvTimes = recvTimes;
        saveField("recvTimes", Integer.valueOf(recvTimes));
    }

    public int getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(int createTime) {
        if (createTime == this.createTime)
            return;
        this.createTime = createTime;
    }

    public void saveCreateTime(int createTime) {
        if (createTime == this.createTime)
            return;
        this.createTime = createTime;
        saveField("createTime", Integer.valueOf(createTime));
    }

    public int getBlackTime() {
        return this.blackTime;
    }

    public void setBlackTime(int blackTime) {
        if (blackTime == this.blackTime)
            return;
        this.blackTime = blackTime;
    }

    public void saveBlackTime(int blackTime) {
        if (blackTime == this.blackTime)
            return;
        this.blackTime = blackTime;
        saveField("blackTime", Integer.valueOf(blackTime));
    }

    protected String getUpdateKeyValue() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(" `pid` = '").append(this.pid).append("',");
        sBuilder.append(" `friendCId` = '").append(this.friendCId).append("',");
        sBuilder.append(" `isBlack` = '").append(this.isBlack ? 1 : 0).append("',");
        sBuilder.append(" `isDelete` = '").append(this.isDelete ? 1 : 0).append("',");
        sBuilder.append(" `lastGiveTime` = '").append(this.lastGiveTime).append("',");
        sBuilder.append(" `giveTimes` = '").append(this.giveTimes).append("',");
        sBuilder.append(" `lastRecvTime` = '").append(this.lastRecvTime).append("',");
        sBuilder.append(" `recvTimes` = '").append(this.recvTimes).append("',");
        sBuilder.append(" `createTime` = '").append(this.createTime).append("',");
        sBuilder.append(" `blackTime` = '").append(this.blackTime).append("',");
        sBuilder.deleteCharAt(sBuilder.length() - 1);
        return sBuilder.toString();
    }
}

