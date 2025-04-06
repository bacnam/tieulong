/*    */ package core.logger.flow.impl.db;
/*    */ 
/*    */ import com.zhonglian.server.common.db.annotation.DataBaseField;
/*    */ import com.zhonglian.server.common.utils.CommTime;
/*    */ import com.zhonglian.server.logger.flow.db.DBFlowBase;
/*    */ import core.server.ServerConfig;
/*    */ import java.sql.PreparedStatement;
/*    */ import java.sql.SQLException;
/*    */ 
/*    */ public class ItemLogFlow
/*    */   extends DBFlowBase
/*    */ {
/*    */   @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家pid")
/* 14 */   private long pid = 0L;
/*    */   @DataBaseField(type = "int(11)", fieldname = "vip_level", comment = "VIP等级")
/* 16 */   private int vip_level = 0;
/*    */   @DataBaseField(type = "int(11)", fieldname = "level", comment = "队伍等级")
/* 18 */   private int level = 0;
/*    */   @DataBaseField(type = "int(11)", fieldname = "itemId", comment = "道具Id")
/* 20 */   private int itemId = 0;
/*    */   @DataBaseField(type = "int(11)", fieldname = "reason", comment = "产生原因类型")
/* 22 */   private int reason = 0;
/*    */   @DataBaseField(type = "int(11)", fieldname = "num", comment = "数量")
/* 24 */   private int num = 0;
/*    */   @DataBaseField(type = "int(11)", fieldname = "cur_remainder", comment = "当前剩余")
/* 26 */   private int cur_remainder = 0;
/*    */   @DataBaseField(type = "int(11)", fieldname = "pre_value", comment = "前值")
/* 28 */   private int pre_value = 0;
/*    */   @DataBaseField(type = "int(11)", fieldname = "type", comment = "类型(1:获得,2:消耗)")
/* 30 */   private int type = 0;
/*    */ 
/*    */   
/*    */   public ItemLogFlow() {}
/*    */   
/*    */   public ItemLogFlow(long pid, int vip_level, int level, int itemId, int reason, int num, int cur_remainder, int pre_value, int type) {
/* 36 */     this.pid = pid;
/* 37 */     this.vip_level = vip_level;
/* 38 */     this.level = level;
/* 39 */     this.itemId = itemId;
/* 40 */     this.reason = reason;
/* 41 */     this.num = num;
/* 42 */     this.cur_remainder = cur_remainder;
/* 43 */     this.pre_value = pre_value;
/* 44 */     this.type = type;
/*    */   }
/*    */   
/* 47 */   public static String TABLE_NAME = "ItemLog";
/*    */ 
/*    */   
/*    */   public String getTableName() {
/* 51 */     return TABLE_NAME;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getInsertSql() {
/* 56 */     return "INSERT INTO ItemLog(`server_id`, `timestamp`, `date_time`, `pid`, `vip_level`, `level`, `itemId`, `reason`, `num`, `cur_remainder`, `pre_value`, `type`)values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static String getCreateTableSQL() {
/* 62 */     String sql = "CREATE TABLE IF NOT EXISTS `ItemLog` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`server_id` int(11) NOT NULL DEFAULT '0' COMMENT '服务器ID',`timestamp` int(11) NOT NULL DEFAULT '0' COMMENT '日志时间(时间戳)',`date_time` varchar(500) NOT NULL DEFAULT '20160801' COMMENT '日志时间(yyyymmdd)',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家pid',`vip_level` int(11) NOT NULL DEFAULT '0' COMMENT 'VIP等级',`level` int(11) NOT NULL DEFAULT '0' COMMENT '队伍等级',`itemId` int(11) NOT NULL DEFAULT '0' COMMENT '道具Id',`reason` int(11) NOT NULL DEFAULT '0' COMMENT '产生原因类型',`num` int(11) NOT NULL DEFAULT '0' COMMENT '数量',`cur_remainder` int(11) NOT NULL DEFAULT '0' COMMENT '当前剩余',`pre_value` int(11) NOT NULL DEFAULT '0' COMMENT '前值',`type` int(11) NOT NULL DEFAULT '0' COMMENT '类型(1:获得,2:消耗)',PRIMARY KEY (`id`)) COMMENT='道具消费日志' DEFAULT CHARSET=utf8";
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
/* 88 */     pstmt.setLong(4, this.pid);
/* 89 */     pstmt.setInt(5, this.vip_level);
/* 90 */     pstmt.setInt(6, this.level);
/* 91 */     pstmt.setInt(7, this.itemId);
/* 92 */     pstmt.setInt(8, this.reason);
/* 93 */     pstmt.setInt(9, this.num);
/* 94 */     pstmt.setInt(10, this.cur_remainder);
/* 95 */     pstmt.setInt(11, this.pre_value);
/* 96 */     pstmt.setInt(12, this.type);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/logger/flow/impl/db/ItemLogFlow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */