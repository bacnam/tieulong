/*     */ package core.logger.flow.impl.db;
/*     */ 
/*     */ import com.zhonglian.server.common.db.annotation.DataBaseField;
/*     */ import com.zhonglian.server.common.utils.CommTime;
/*     */ import com.zhonglian.server.logger.flow.db.DBFlowBase;
/*     */ import core.server.ServerConfig;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.SQLException;
/*     */ 
/*     */ public class MailFlow
/*     */   extends DBFlowBase
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "cid", comment = "玩家cid")
/*  14 */   private long cid = 0L;
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "open_id", comment = "账号open_id")
/*  16 */   private String open_id = "";
/*     */   @DataBaseField(type = "int(11)", fieldname = "vip_level", comment = "VIP等级")
/*  18 */   private int vip_level = 0;
/*     */   @DataBaseField(type = "int(11)", fieldname = "level", comment = "队伍等级")
/*  20 */   private int level = 0;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "mail_id", comment = "邮件实例ID")
/*  22 */   private long mail_id = 0L;
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "title", comment = "邮件标题")
/*  24 */   private String title = "";
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "item_ids", comment = "物品ID列表")
/*  26 */   private String item_ids = "";
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "item_nums", comment = "物品数量列表")
/*  28 */   private String item_nums = "";
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "sender", comment = "发送者")
/*  30 */   private String sender = "";
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "type", comment = "类型：新增/领取")
/*  32 */   private String type = "";
/*     */ 
/*     */   
/*     */   public MailFlow() {}
/*     */   
/*     */   public MailFlow(long cid, String open_id, int vip_level, int level, long mail_id, String title, String item_ids, String item_nums, String sender, String type) {
/*  38 */     this.cid = cid;
/*  39 */     this.open_id = open_id;
/*  40 */     this.vip_level = vip_level;
/*  41 */     this.level = level;
/*  42 */     this.mail_id = mail_id;
/*  43 */     this.title = title;
/*  44 */     this.item_ids = item_ids;
/*  45 */     this.item_nums = item_nums;
/*  46 */     this.sender = sender;
/*  47 */     this.type = type;
/*     */   }
/*     */   
/*  50 */   public static String TABLE_NAME = "Mail";
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  54 */     return TABLE_NAME;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getInsertSql() {
/*  59 */     return "INSERT INTO Mail(`server_id`, `timestamp`, `date_time`, `cid`, `open_id`, `vip_level`, `level`, `mail_id`, `title`, `item_ids`, `item_nums`, `sender`, `type`)values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getCreateTableSQL() {
/*  65 */     String sql = "CREATE TABLE IF NOT EXISTS `Mail` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`server_id` int(11) NOT NULL DEFAULT '0' COMMENT '服务器ID',`timestamp` int(11) NOT NULL DEFAULT '0' COMMENT '日志时间(时间戳)',`date_time` varchar(500) NOT NULL DEFAULT '20160801' COMMENT '日志时间(yyyymmdd)',`cid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家cid',`open_id` varchar(500) NOT NULL DEFAULT '' COMMENT '账号open_id',`vip_level` int(11) NOT NULL DEFAULT '0' COMMENT 'VIP等级',`level` int(11) NOT NULL DEFAULT '0' COMMENT '队伍等级',`mail_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '邮件实例ID',`title` varchar(500) NOT NULL DEFAULT '' COMMENT '邮件标题',`item_ids` varchar(500) NOT NULL DEFAULT '' COMMENT '物品ID列表',`item_nums` varchar(500) NOT NULL DEFAULT '' COMMENT '物品数量列表',`sender` varchar(500) NOT NULL DEFAULT '' COMMENT '发送者',`type` varchar(500) NOT NULL DEFAULT '' COMMENT '类型：新增/领取',PRIMARY KEY (`id`)) COMMENT='User Info 玩家邮件日志表' DEFAULT CHARSET=utf8";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  84 */     return sql;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addToBatch(PreparedStatement pstmt) throws SQLException {
/*  89 */     pstmt.setInt(1, ServerConfig.ServerID());
/*  90 */     pstmt.setInt(2, CommTime.nowSecond());
/*  91 */     pstmt.setString(3, CommTime.getNowTimeStringYMD());
/*  92 */     pstmt.setLong(4, this.cid);
/*  93 */     pstmt.setString(5, this.open_id);
/*  94 */     pstmt.setInt(6, this.vip_level);
/*  95 */     pstmt.setInt(7, this.level);
/*  96 */     pstmt.setLong(8, this.mail_id);
/*  97 */     pstmt.setString(9, this.title);
/*  98 */     pstmt.setString(10, this.item_ids);
/*  99 */     pstmt.setString(11, this.item_nums);
/* 100 */     pstmt.setString(12, this.sender);
/* 101 */     pstmt.setString(13, this.type);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/logger/flow/impl/db/MailFlow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */