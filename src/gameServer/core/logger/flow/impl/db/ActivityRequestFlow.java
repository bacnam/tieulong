/*    */ package core.logger.flow.impl.db;
/*    */ 
/*    */ import com.zhonglian.server.common.db.annotation.DataBaseField;
/*    */ import com.zhonglian.server.common.utils.CommTime;
/*    */ import com.zhonglian.server.logger.flow.db.DBFlowBase;
/*    */ import core.server.ServerConfig;
/*    */ import java.sql.PreparedStatement;
/*    */ import java.sql.SQLException;
/*    */ 
/*    */ public class ActivityRequestFlow
/*    */   extends DBFlowBase
/*    */ {
/*    */   @DataBaseField(type = "varchar(500)", fieldname = "activity", comment = "活动类型")
/* 14 */   private String activity = "";
/*    */   @DataBaseField(type = "tinyint(1)", fieldname = "isActive", comment = "是否启用")
/*    */   private boolean isActive = false;
/*    */   @DataBaseField(type = "varchar(500)", fieldname = "gm_id", comment = "GM后台对应的id")
/* 18 */   private String gm_id = "";
/*    */   @DataBaseField(type = "int(11)", fieldname = "beginTime", comment = "活动开启时间")
/* 20 */   private int beginTime = 0;
/*    */   @DataBaseField(type = "int(11)", fieldname = "endTime", comment = "活动结束时间")
/* 22 */   private int endTime = 0;
/*    */   @DataBaseField(type = "int(11)", fieldname = "closeTime", comment = "活动关闭时间")
/* 24 */   private int closeTime = 0;
/*    */   @DataBaseField(type = "text(500)", fieldname = "json", comment = "活动具体配置")
/* 26 */   private String json = "";
/*    */   @DataBaseField(type = "varchar(500)", fieldname = "type", comment = "操作类型:增删改")
/* 28 */   private String type = "";
/*    */   @DataBaseField(type = "varchar(500)", fieldname = "operator", comment = "操作员")
/* 30 */   private String operator = "";
/*    */ 
/*    */   
/*    */   public ActivityRequestFlow() {}
/*    */   
/*    */   public ActivityRequestFlow(String activity, boolean isActive, String gm_id, int beginTime, int endTime, int closeTime, String json, String type, String operator) {
/* 36 */     this.activity = activity;
/* 37 */     this.isActive = isActive;
/* 38 */     this.gm_id = gm_id;
/* 39 */     this.beginTime = beginTime;
/* 40 */     this.endTime = endTime;
/* 41 */     this.closeTime = closeTime;
/* 42 */     this.json = json;
/* 43 */     this.type = type;
/* 44 */     this.operator = operator;
/*    */   }
/*    */   
/* 47 */   public static String TABLE_NAME = "ActivityRequest";
/*    */ 
/*    */   
/*    */   public String getTableName() {
/* 51 */     return TABLE_NAME;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getInsertSql() {
/* 56 */     return "INSERT INTO ActivityRequest(`server_id`, `timestamp`, `date_time`, `activity`, `isActive`, `gm_id`, `beginTime`, `endTime`, `closeTime`, `json`, `type`, `operator`)values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static String getCreateTableSQL() {
/* 62 */     String sql = "CREATE TABLE IF NOT EXISTS `ActivityRequest` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`server_id` int(11) NOT NULL DEFAULT '0' COMMENT '服务器ID',`timestamp` int(11) NOT NULL DEFAULT '0' COMMENT '日志时间(时间戳)',`date_time` varchar(500) NOT NULL DEFAULT '20160801' COMMENT '日志时间(yyyymmdd)',`activity` varchar(500) NOT NULL DEFAULT '' COMMENT '活动类型',`isActive` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否启用',`gm_id` varchar(500) NOT NULL DEFAULT '' COMMENT 'GM后台对应的id',`beginTime` int(11) NOT NULL DEFAULT '0' COMMENT '活动开启时间',`endTime` int(11) NOT NULL DEFAULT '0' COMMENT '活动结束时间',`closeTime` int(11) NOT NULL DEFAULT '0' COMMENT '活动关闭时间',`json` text NULL COMMENT '活动具体配置',`type` varchar(500) NOT NULL DEFAULT '' COMMENT '操作类型:增删改',`operator` varchar(500) NOT NULL DEFAULT '' COMMENT '操作员',PRIMARY KEY (`id`)) COMMENT='User Info 活动操作日志表' DEFAULT CHARSET=utf8";
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 80 */     return sql;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addToBatch(PreparedStatement pstmt) throws SQLException {
/* 85 */     pstmt.setInt(1, ServerConfig.ServerID());
/* 86 */     pstmt.setInt(2, CommTime.nowSecond());
/* 87 */     pstmt.setString(3, CommTime.getNowTimeStringYMD());
/* 88 */     pstmt.setString(4, this.activity);
/* 89 */     pstmt.setBoolean(5, this.isActive);
/* 90 */     pstmt.setString(6, this.gm_id);
/* 91 */     pstmt.setInt(7, this.beginTime);
/* 92 */     pstmt.setInt(8, this.endTime);
/* 93 */     pstmt.setInt(9, this.closeTime);
/* 94 */     pstmt.setString(10, this.json);
/* 95 */     pstmt.setString(11, this.type);
/* 96 */     pstmt.setString(12, this.operator);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/logger/flow/impl/db/ActivityRequestFlow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */