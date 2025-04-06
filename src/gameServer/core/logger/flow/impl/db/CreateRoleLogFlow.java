/*    */ package core.logger.flow.impl.db;
/*    */ 
/*    */ import com.zhonglian.server.common.db.annotation.DataBaseField;
/*    */ import com.zhonglian.server.common.utils.CommTime;
/*    */ import com.zhonglian.server.logger.flow.db.DBFlowBase;
/*    */ import core.server.ServerConfig;
/*    */ import java.sql.PreparedStatement;
/*    */ import java.sql.SQLException;
/*    */ 
/*    */ public class CreateRoleLogFlow
/*    */   extends DBFlowBase
/*    */ {
/*    */   @DataBaseField(type = "varchar(500)", fieldname = "openid", comment = "玩家openid")
/* 14 */   private String openid = "";
/*    */   @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家pid")
/* 16 */   private long pid = 0L;
/*    */   @DataBaseField(type = "int(11)", fieldname = "createTime", comment = "创角时间")
/* 18 */   private int createTime = 0;
/*    */   @DataBaseField(type = "varchar(500)", fieldname = "channel", comment = "渠道")
/* 20 */   private String channel = "";
/*    */   @DataBaseField(type = "int(11)", fieldname = "serverid", comment = "服务器id")
/* 22 */   private int serverid = 0;
/*    */ 
/*    */   
/*    */   public CreateRoleLogFlow() {}
/*    */   
/*    */   public CreateRoleLogFlow(String openid, long pid, int createTime, String channel, int serverid) {
/* 28 */     this.openid = openid;
/* 29 */     this.pid = pid;
/* 30 */     this.createTime = createTime;
/* 31 */     this.channel = channel;
/* 32 */     this.serverid = serverid;
/*    */   }
/*    */   
/* 35 */   public static String TABLE_NAME = "CreateRoleLog";
/*    */ 
/*    */   
/*    */   public String getTableName() {
/* 39 */     return TABLE_NAME;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getInsertSql() {
/* 44 */     return "INSERT INTO CreateRoleLog(`server_id`, `timestamp`, `date_time`, `openid`, `pid`, `createTime`, `channel`, `serverid`)values(?, ?, ?, ?, ?, ?, ?, ?)";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static String getCreateTableSQL() {
/* 50 */     String sql = "CREATE TABLE IF NOT EXISTS `CreateRoleLog` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`server_id` int(11) NOT NULL DEFAULT '0' COMMENT '服务器ID',`timestamp` int(11) NOT NULL DEFAULT '0' COMMENT '日志时间(时间戳)',`date_time` varchar(500) NOT NULL DEFAULT '20160801' COMMENT '日志时间(yyyymmdd)',`openid` varchar(500) NOT NULL DEFAULT '' COMMENT '玩家openid',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家pid',`createTime` int(11) NOT NULL DEFAULT '0' COMMENT '创角时间',`channel` varchar(500) NOT NULL DEFAULT '' COMMENT '渠道',`serverid` int(11) NOT NULL DEFAULT '0' COMMENT '服务器id',PRIMARY KEY (`id`)) COMMENT='Create Role 创角日志' DEFAULT CHARSET=utf8";
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
/* 64 */     return sql;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addToBatch(PreparedStatement pstmt) throws SQLException {
/* 69 */     pstmt.setInt(1, ServerConfig.ServerID());
/* 70 */     pstmt.setInt(2, CommTime.nowSecond());
/* 71 */     pstmt.setString(3, CommTime.getNowTimeStringYMD());
/* 72 */     pstmt.setString(4, this.openid);
/* 73 */     pstmt.setLong(5, this.pid);
/* 74 */     pstmt.setInt(6, this.createTime);
/* 75 */     pstmt.setString(7, this.channel);
/* 76 */     pstmt.setInt(8, this.serverid);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/logger/flow/impl/db/CreateRoleLogFlow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */