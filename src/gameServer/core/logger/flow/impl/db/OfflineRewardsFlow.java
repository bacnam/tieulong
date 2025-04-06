/*    */ package core.logger.flow.impl.db;
/*    */ 
/*    */ import com.zhonglian.server.common.db.annotation.DataBaseField;
/*    */ import com.zhonglian.server.common.utils.CommTime;
/*    */ import com.zhonglian.server.logger.flow.db.DBFlowBase;
/*    */ import core.server.ServerConfig;
/*    */ import java.sql.PreparedStatement;
/*    */ import java.sql.SQLException;
/*    */ 
/*    */ public class OfflineRewardsFlow
/*    */   extends DBFlowBase
/*    */ {
/*    */   @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家pid")
/* 14 */   private long pid = 0L;
/*    */   @DataBaseField(type = "int(11)", fieldname = "vip_level", comment = "VIP等级")
/* 16 */   private int vip_level = 0;
/*    */   @DataBaseField(type = "int(11)", fieldname = "team_level", comment = "VIP等级")
/* 18 */   private int team_level = 0;
/*    */   @DataBaseField(type = "int(11)", fieldname = "dungeon_level", comment = "当前等级")
/* 20 */   private int dungeon_level = 0;
/*    */   @DataBaseField(type = "int(11)", fieldname = "waves", comment = "结算波数")
/* 22 */   private int waves = 0;
/*    */   @DataBaseField(type = "int(11)", fieldname = "offline_time", comment = "离线时间")
/* 24 */   private int offline_time = 0;
/*    */   @DataBaseField(type = "int(11)", fieldname = "calc_time", comment = "结算使用的时间")
/* 26 */   private int calc_time = 0;
/*    */ 
/*    */   
/*    */   public OfflineRewardsFlow() {}
/*    */   
/*    */   public OfflineRewardsFlow(long pid, int vip_level, int team_level, int dungeon_level, int waves, int offline_time, int calc_time) {
/* 32 */     this.pid = pid;
/* 33 */     this.vip_level = vip_level;
/* 34 */     this.team_level = team_level;
/* 35 */     this.dungeon_level = dungeon_level;
/* 36 */     this.waves = waves;
/* 37 */     this.offline_time = offline_time;
/* 38 */     this.calc_time = calc_time;
/*    */   }
/*    */   
/* 41 */   public static String TABLE_NAME = "OfflineRewards";
/*    */ 
/*    */   
/*    */   public String getTableName() {
/* 45 */     return TABLE_NAME;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getInsertSql() {
/* 50 */     return "INSERT INTO OfflineRewards(`server_id`, `timestamp`, `date_time`, `pid`, `vip_level`, `team_level`, `dungeon_level`, `waves`, `offline_time`, `calc_time`)values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static String getCreateTableSQL() {
/* 56 */     String sql = "CREATE TABLE IF NOT EXISTS `OfflineRewards` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`server_id` int(11) NOT NULL DEFAULT '0' COMMENT '服务器ID',`timestamp` int(11) NOT NULL DEFAULT '0' COMMENT '日志时间(时间戳)',`date_time` varchar(500) NOT NULL DEFAULT '20160801' COMMENT '日志时间(yyyymmdd)',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家pid',`vip_level` int(11) NOT NULL DEFAULT '0' COMMENT 'VIP等级',`team_level` int(11) NOT NULL DEFAULT '0' COMMENT 'VIP等级',`dungeon_level` int(11) NOT NULL DEFAULT '0' COMMENT '当前等级',`waves` int(11) NOT NULL DEFAULT '0' COMMENT '结算波数',`offline_time` int(11) NOT NULL DEFAULT '0' COMMENT '离线时间',`calc_time` int(11) NOT NULL DEFAULT '0' COMMENT '结算使用的时间',PRIMARY KEY (`id`)) COMMENT='Offline Rewards 离线奖励领取日志' DEFAULT CHARSET=utf8";
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
/* 72 */     return sql;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addToBatch(PreparedStatement pstmt) throws SQLException {
/* 77 */     pstmt.setInt(1, ServerConfig.ServerID());
/* 78 */     pstmt.setInt(2, CommTime.nowSecond());
/* 79 */     pstmt.setString(3, CommTime.getNowTimeStringYMD());
/* 80 */     pstmt.setLong(4, this.pid);
/* 81 */     pstmt.setInt(5, this.vip_level);
/* 82 */     pstmt.setInt(6, this.team_level);
/* 83 */     pstmt.setInt(7, this.dungeon_level);
/* 84 */     pstmt.setInt(8, this.waves);
/* 85 */     pstmt.setInt(9, this.offline_time);
/* 86 */     pstmt.setInt(10, this.calc_time);
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/logger/flow/impl/db/OfflineRewardsFlow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */