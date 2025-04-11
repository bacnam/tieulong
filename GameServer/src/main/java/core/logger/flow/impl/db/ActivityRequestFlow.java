package core.logger.flow.impl.db;

import com.zhonglian.server.common.db.annotation.DataBaseField;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.logger.flow.db.DBFlowBase;
import core.server.ServerConfig;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ActivityRequestFlow
        extends DBFlowBase {
    public static String TABLE_NAME = "ActivityRequest";
    @DataBaseField(type = "varchar(500)", fieldname = "activity", comment = "活动类型")
    private String activity = "";
    @DataBaseField(type = "tinyint(1)", fieldname = "isActive", comment = "是否启用")
    private boolean isActive = false;
    @DataBaseField(type = "varchar(500)", fieldname = "gm_id", comment = "GM后台对应的id")
    private String gm_id = "";
    @DataBaseField(type = "int(11)", fieldname = "beginTime", comment = "活动开启时间")
    private int beginTime = 0;
    @DataBaseField(type = "int(11)", fieldname = "endTime", comment = "活动结束时间")
    private int endTime = 0;
    @DataBaseField(type = "int(11)", fieldname = "closeTime", comment = "活动关闭时间")
    private int closeTime = 0;
    @DataBaseField(type = "text(500)", fieldname = "json", comment = "活动具体配置")
    private String json = "";
    @DataBaseField(type = "varchar(500)", fieldname = "type", comment = "操作类型:增删改")
    private String type = "";
    @DataBaseField(type = "varchar(500)", fieldname = "operator", comment = "操作员")
    private String operator = "";

    public ActivityRequestFlow() {
    }

    public ActivityRequestFlow(String activity, boolean isActive, String gm_id, int beginTime, int endTime, int closeTime, String json, String type, String operator) {
        this.activity = activity;
        this.isActive = isActive;
        this.gm_id = gm_id;
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.closeTime = closeTime;
        this.json = json;
        this.type = type;
        this.operator = operator;
    }

    public static String getCreateTableSQL() {
        String sql = "CREATE TABLE IF NOT EXISTS `ActivityRequest` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`server_id` int(11) NOT NULL DEFAULT '0' COMMENT '服务器ID',`timestamp` int(11) NOT NULL DEFAULT '0' COMMENT '日志时间(时间戳)',`date_time` varchar(500) NOT NULL DEFAULT '20160801' COMMENT '日志时间(yyyymmdd)',`activity` varchar(500) NOT NULL DEFAULT '' COMMENT '活动类型',`isActive` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否启用',`gm_id` varchar(500) NOT NULL DEFAULT '' COMMENT 'GM后台对应的id',`beginTime` int(11) NOT NULL DEFAULT '0' COMMENT '活动开启时间',`endTime` int(11) NOT NULL DEFAULT '0' COMMENT '活动结束时间',`closeTime` int(11) NOT NULL DEFAULT '0' COMMENT '活动关闭时间',`json` text NULL COMMENT '活动具体配置',`type` varchar(500) NOT NULL DEFAULT '' COMMENT '操作类型:增删改',`operator` varchar(500) NOT NULL DEFAULT '' COMMENT '操作员',PRIMARY KEY (`id`)) COMMENT='User Info 活动操作日志表' DEFAULT CHARSET=utf8";

        return sql;
    }

    public String getTableName() {
        return TABLE_NAME;
    }

    public String getInsertSql() {
        return "INSERT INTO ActivityRequest(`server_id`, `timestamp`, `date_time`, `activity`, `isActive`, `gm_id`, `beginTime`, `endTime`, `closeTime`, `json`, `type`, `operator`)values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    }

    public void addToBatch(PreparedStatement pstmt) throws SQLException {
        pstmt.setInt(1, ServerConfig.ServerID());
        pstmt.setInt(2, CommTime.nowSecond());
        pstmt.setString(3, CommTime.getNowTimeStringYMD());
        pstmt.setString(4, this.activity);
        pstmt.setBoolean(5, this.isActive);
        pstmt.setString(6, this.gm_id);
        pstmt.setInt(7, this.beginTime);
        pstmt.setInt(8, this.endTime);
        pstmt.setInt(9, this.closeTime);
        pstmt.setString(10, this.json);
        pstmt.setString(11, this.type);
        pstmt.setString(12, this.operator);
    }
}

