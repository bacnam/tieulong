package core.database.game.bo;

import com.zhonglian.server.common.db.BaseBO;
import com.zhonglian.server.common.db.annotation.DataBaseField;
import core.server.ServerConfig;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class FriendApplyBO extends BaseBO {
    @DataBaseField(
        type = "bigint(20)",
        fieldname = "id",
        comment = "数据库表唯一键值"
    )
    private long id;
    @DataBaseField(
        type = "bigint(20)",
        fieldname = "pid",
        comment = "所属玩家pid"
    )
    private long pid;
    @DataBaseField(
        type = "bigint(20)",
        fieldname = "friendCId",
        comment = "好友玩家pid"
    )
    private long friendCId;
    @DataBaseField(
        type = "varchar(500)",
        fieldname = "content",
        comment = "附加内容"
    )
    private String content;
    @DataBaseField(
        type = "int(11)",
        fieldname = "applyTime",
        comment = "申请时间戳[秒]"
    )
    private int applyTime;

    public FriendApplyBO() {
        this.id = 0L;
        this.pid = 0L;
        this.friendCId = 0L;
        this.content = "";
        this.applyTime = 0;
    }

    public FriendApplyBO(ResultSet rs) throws Exception {
        this.id = rs.getLong(1);
        this.pid = rs.getLong(2);
        this.friendCId = rs.getLong(3);
        this.content = rs.getString(4);
        this.applyTime = rs.getInt(5);
    }

    @Override
    public void getFromResultSet(ResultSet rs, List list) throws Exception {
        list.add(new FriendApplyBO(rs));
    }

    @Override
    public long getAsynTaskTag() {
        return this.getPid();
    }

    @Override
    public String getItemsName() {
        return "`id`, `pid`, `friendCId`, `content`, `applyTime`";
    }

    @Override
    public String getTableName() {
        return "`friendApply`";
    }

    @Override
    public String getItemsValue() {
        StringBuilder strBuf = new StringBuilder();
        strBuf.append("'").append(this.id).append("', ");
        strBuf.append("'").append(this.pid).append("', ");
        strBuf.append("'").append(this.friendCId).append("', ");
        strBuf.append("'").append(this.content == null ? null : this.content.replace("'", "''")).append("', ");
        strBuf.append("'").append(this.applyTime).append("', ");
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

    public long getPid() {
        return this.pid;
    }

    public void setPid(long pid) {
        if (pid != this.pid) {
            this.pid = pid;
        }
    }

    public void savePid(long pid) {
        if (pid != this.pid) {
            this.pid = pid;
            this.saveField("pid", pid);
        }
    }

    public long getFriendCId() {
        return this.friendCId;
    }

    public void setFriendCId(long friendCId) {
        if (friendCId != this.friendCId) {
            this.friendCId = friendCId;
        }
    }

    public void saveFriendCId(long friendCId) {
        if (friendCId != this.friendCId) {
            this.friendCId = friendCId;
            this.saveField("friendCId", friendCId);
        }
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        if (!content.equals(this.content)) {
            this.content = content;
        }
    }

    public void saveContent(String content) {
        if (!content.equals(this.content)) {
            this.content = content;
            this.saveField("content", content);
        }
    }

    public int getApplyTime() {
        return this.applyTime;
    }

    public void setApplyTime(int applyTime) {
        if (applyTime != this.applyTime) {
            this.applyTime = applyTime;
        }
    }

    public void saveApplyTime(int applyTime) {
        if (applyTime != this.applyTime) {
            this.applyTime = applyTime;
            this.saveField("applyTime", applyTime);
        }
    }

    @Override
    protected String getUpdateKeyValue() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(" `pid` = '").append(this.pid).append("',");
        sBuilder.append(" `friendCId` = '").append(this.friendCId).append("',");
        sBuilder.append(" `content` = '").append(this.content == null ? null : this.content.replace("'", "''")).append("',");
        sBuilder.append(" `applyTime` = '").append(this.applyTime).append("',");
        sBuilder.deleteCharAt(sBuilder.length() - 1);
        return sBuilder.toString();
    }

    public static String getSql_TableCreate() {
        return "CREATE TABLE IF NOT EXISTS `friendApply` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '所属玩家pid',`friendCId` bigint(20) NOT NULL DEFAULT '0' COMMENT '好友玩家pid',`content` varchar(500) NOT NULL DEFAULT '' COMMENT '附加内容',`applyTime` int(11) NOT NULL DEFAULT '0' COMMENT '申请时间戳[秒]',KEY `pid` (`pid`),PRIMARY KEY (`id`)) COMMENT='好友申请列表[存储加好友申请]'  DEFAULT CHARSET=utf8 AUTO_INCREMENT="
            + (ServerConfig.getInitialID() + 1L);
    }
}
