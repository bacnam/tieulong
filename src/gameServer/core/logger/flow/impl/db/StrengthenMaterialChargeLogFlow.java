/*    */ package core.logger.flow.impl.db;
/*    */ 
/*    */ import com.zhonglian.server.common.db.annotation.DataBaseField;
/*    */ import com.zhonglian.server.common.utils.CommTime;
/*    */ import com.zhonglian.server.logger.flow.db.DBFlowBase;
/*    */ import core.server.ServerConfig;
/*    */ import java.sql.PreparedStatement;
/*    */ import java.sql.SQLException;
/*    */ 
/*    */ public class StrengthenMaterialChargeLogFlow
/*    */   extends DBFlowBase
/*    */ {
/*    */   @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家pid")
/* 14 */   private long pid = 0L;
/*    */   @DataBaseField(type = "int(11)", fieldname = "vip_level", comment = "VIP等级")
/* 16 */   private int vip_level = 0;
/*    */   @DataBaseField(type = "int(11)", fieldname = "level", comment = "队伍等级")
/* 18 */   private int level = 0;
/*    */   @DataBaseField(type = "int(11)", fieldname = "reason", comment = "产生原因类型")
/* 20 */   private int reason = 0;
/*    */   @DataBaseField(type = "int(11)", fieldname = "num", comment = "数量")
/* 22 */   private int num = 0;
/*    */   @DataBaseField(type = "int(11)", fieldname = "cur_remainder", comment = "当前剩余")
/* 24 */   private int cur_remainder = 0;
/*    */   @DataBaseField(type = "int(11)", fieldname = "pre_value", comment = "前值")
/* 26 */   private int pre_value = 0;
/*    */   @DataBaseField(type = "int(11)", fieldname = "type", comment = "类型(1:获得,2:消耗)")
/* 28 */   private int type = 0;
/*    */ 
/*    */   
/*    */   public StrengthenMaterialChargeLogFlow() {}
/*    */   
/*    */   public StrengthenMaterialChargeLogFlow(long pid, int vip_level, int level, int reason, int num, int cur_remainder, int pre_value, int type) {
/* 34 */     this.pid = pid;
/* 35 */     this.vip_level = vip_level;
/* 36 */     this.level = level;
/* 37 */     this.reason = reason;
/* 38 */     this.num = num;
/* 39 */     this.cur_remainder = cur_remainder;
/* 40 */     this.pre_value = pre_value;
/* 41 */     this.type = type;
/*    */   }
/*    */   
/* 44 */   public static String TABLE_NAME = "StrengthenMaterialChargeLog";
/*    */ 
/*    */   
/*    */   public String getTableName() {
/* 48 */     return TABLE_NAME;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getInsertSql() {
/* 53 */     return "INSERT INTO StrengthenMaterialChargeLog(`server_id`, `timestamp`, `date_time`, `pid`, `vip_level`, `level`, `reason`, `num`, `cur_remainder`, `pre_value`, `type`)values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static String getCreateTableSQL() {
/* 59 */     String sql = "CREATE TABLE IF NOT EXISTS `StrengthenMaterialChargeLog` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`server_id` int(11) NOT NULL DEFAULT '0' COMMENT '服务器ID',`timestamp` int(11) NOT NULL DEFAULT '0' COMMENT '日志时间(时间戳)',`date_time` varchar(500) NOT NULL DEFAULT '20160801' COMMENT '日志时间(yyyymmdd)',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家pid',`vip_level` int(11) NOT NULL DEFAULT '0' COMMENT 'VIP等级',`level` int(11) NOT NULL DEFAULT '0' COMMENT '队伍等级',`reason` int(11) NOT NULL DEFAULT '0' COMMENT '产生原因类型',`num` int(11) NOT NULL DEFAULT '0' COMMENT '数量',`cur_remainder` int(11) NOT NULL DEFAULT '0' COMMENT '当前剩余',`pre_value` int(11) NOT NULL DEFAULT '0' COMMENT '前值',`type` int(11) NOT NULL DEFAULT '0' COMMENT '类型(1:获得,2:消耗)',PRIMARY KEY (`id`)) COMMENT='StrengthenMaterial Charge 强化消费日志' DEFAULT CHARSET=utf8";
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
/* 76 */     return sql;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addToBatch(PreparedStatement pstmt) throws SQLException {
/* 81 */     pstmt.setInt(1, ServerConfig.ServerID());
/* 82 */     pstmt.setInt(2, CommTime.nowSecond());
/* 83 */     pstmt.setString(3, CommTime.getNowTimeStringYMD());
/* 84 */     pstmt.setLong(4, this.pid);
/* 85 */     pstmt.setInt(5, this.vip_level);
/* 86 */     pstmt.setInt(6, this.level);
/* 87 */     pstmt.setInt(7, this.reason);
/* 88 */     pstmt.setInt(8, this.num);
/* 89 */     pstmt.setInt(9, this.cur_remainder);
/* 90 */     pstmt.setInt(10, this.pre_value);
/* 91 */     pstmt.setInt(11, this.type);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/logger/flow/impl/db/StrengthenMaterialChargeLogFlow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */