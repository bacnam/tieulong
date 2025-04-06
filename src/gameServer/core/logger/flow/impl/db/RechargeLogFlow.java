/*     */ package core.logger.flow.impl.db;
/*     */ 
/*     */ import com.zhonglian.server.common.db.annotation.DataBaseField;
/*     */ import com.zhonglian.server.common.utils.CommTime;
/*     */ import com.zhonglian.server.logger.flow.db.DBFlowBase;
/*     */ import core.server.ServerConfig;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.SQLException;
/*     */ 
/*     */ public class RechargeLogFlow
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
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "order_id", comment = "订单号")
/*  22 */   private String order_id = "";
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "tp_order_id", comment = "第三方订单号")
/*  24 */   private String tp_order_id = "";
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "goods_id", comment = "充值货物ID")
/*  26 */   private String goods_id = "";
/*     */   @DataBaseField(type = "int(11)", fieldname = "money", comment = "充值金额")
/*  28 */   private int money = 0;
/*     */   @DataBaseField(type = "int(11)", fieldname = "coin_num", comment = "货币数量")
/*  30 */   private int coin_num = 0;
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "status", comment = "充值状态")
/*  32 */   private String status = "";
/*     */   @DataBaseField(type = "int(11)", fieldname = "pay_time", comment = "充值时间")
/*  34 */   private int pay_time = 0;
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "pay_time_date", comment = "充值时间date")
/*  36 */   private String pay_time_date = "";
/*     */   @DataBaseField(type = "int(11)", fieldname = "order_time", comment = "下单时间")
/*  38 */   private int order_time = 0;
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "order_time_date", comment = "下单时间date")
/*  40 */   private String order_time_date = "";
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "adfrom", comment = "渠道")
/*  42 */   private String adfrom = "";
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "adfrom2", comment = "二级渠道")
/*  44 */   private String adfrom2 = "";
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "ar_ip", comment = "账号注册IP")
/*  46 */   private String ar_ip = "";
/*     */   @DataBaseField(type = "int(11)", fieldname = "ar_time", comment = "账号注册时间")
/*  48 */   private int ar_time = 0;
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "ar_time_date", comment = "账号注册时间date")
/*  50 */   private String ar_time_date = "";
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "adid", comment = "设备ID")
/*  52 */   private String adid = "";
/*     */ 
/*     */   
/*     */   public RechargeLogFlow() {}
/*     */   
/*     */   public RechargeLogFlow(long cid, String open_id, int vip_level, int level, String order_id, String tp_order_id, String goods_id, int money, int coin_num, String status, int pay_time, String pay_time_date, int order_time, String order_time_date, String adfrom, String adfrom2, String ar_ip, int ar_time, String ar_time_date, String adid) {
/*  58 */     this.cid = cid;
/*  59 */     this.open_id = open_id;
/*  60 */     this.vip_level = vip_level;
/*  61 */     this.level = level;
/*  62 */     this.order_id = order_id;
/*  63 */     this.tp_order_id = tp_order_id;
/*  64 */     this.goods_id = goods_id;
/*  65 */     this.money = money;
/*  66 */     this.coin_num = coin_num;
/*  67 */     this.status = status;
/*  68 */     this.pay_time = pay_time;
/*  69 */     this.pay_time_date = pay_time_date;
/*  70 */     this.order_time = order_time;
/*  71 */     this.order_time_date = order_time_date;
/*  72 */     this.adfrom = adfrom;
/*  73 */     this.adfrom2 = adfrom2;
/*  74 */     this.ar_ip = ar_ip;
/*  75 */     this.ar_time = ar_time;
/*  76 */     this.ar_time_date = ar_time_date;
/*  77 */     this.adid = adid;
/*     */   }
/*     */   
/*  80 */   public static String TABLE_NAME = "RechargeLog";
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  84 */     return TABLE_NAME;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getInsertSql() {
/*  89 */     return "INSERT INTO RechargeLog(`server_id`, `timestamp`, `date_time`, `cid`, `open_id`, `vip_level`, `level`, `order_id`, `tp_order_id`, `goods_id`, `money`, `coin_num`, `status`, `pay_time`, `pay_time_date`, `order_time`, `order_time_date`, `adfrom`, `adfrom2`, `ar_ip`, `ar_time`, `ar_time_date`, `adid`)values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getCreateTableSQL() {
/*  95 */     String sql = "CREATE TABLE IF NOT EXISTS `RechargeLog` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`server_id` int(11) NOT NULL DEFAULT '0' COMMENT '服务器ID',`timestamp` int(11) NOT NULL DEFAULT '0' COMMENT '日志时间(时间戳)',`date_time` varchar(500) NOT NULL DEFAULT '20160801' COMMENT '日志时间(yyyymmdd)',`cid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家cid',`open_id` varchar(500) NOT NULL DEFAULT '' COMMENT '账号open_id',`vip_level` int(11) NOT NULL DEFAULT '0' COMMENT 'VIP等级',`level` int(11) NOT NULL DEFAULT '0' COMMENT '队伍等级',`order_id` varchar(500) NOT NULL DEFAULT '' COMMENT '订单号',`tp_order_id` varchar(500) NOT NULL DEFAULT '' COMMENT '第三方订单号',`goods_id` varchar(500) NOT NULL DEFAULT '' COMMENT '充值货物ID',`money` int(11) NOT NULL DEFAULT '0' COMMENT '充值金额',`coin_num` int(11) NOT NULL DEFAULT '0' COMMENT '货币数量',`status` varchar(500) NOT NULL DEFAULT '' COMMENT '充值状态',`pay_time` int(11) NOT NULL DEFAULT '0' COMMENT '充值时间',`pay_time_date` varchar(500) NOT NULL DEFAULT '' COMMENT '充值时间date',`order_time` int(11) NOT NULL DEFAULT '0' COMMENT '下单时间',`order_time_date` varchar(500) NOT NULL DEFAULT '' COMMENT '下单时间date',`adfrom` varchar(500) NOT NULL DEFAULT '' COMMENT '渠道',`adfrom2` varchar(500) NOT NULL DEFAULT '' COMMENT '二级渠道',`ar_ip` varchar(500) NOT NULL DEFAULT '' COMMENT '账号注册IP',`ar_time` int(11) NOT NULL DEFAULT '0' COMMENT '账号注册时间',`ar_time_date` varchar(500) NOT NULL DEFAULT '' COMMENT '账号注册时间date',`adid` varchar(500) NOT NULL DEFAULT '' COMMENT '设备ID',PRIMARY KEY (`id`)) COMMENT='Recharge 充值订单记录日志表' DEFAULT CHARSET=utf8";
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
/* 124 */     return sql;
/*     */   }
/*     */ 
/*     */   
/*     */   public void addToBatch(PreparedStatement pstmt) throws SQLException {
/* 129 */     pstmt.setInt(1, ServerConfig.ServerID());
/* 130 */     pstmt.setInt(2, CommTime.nowSecond());
/* 131 */     pstmt.setString(3, CommTime.getNowTimeStringYMD());
/* 132 */     pstmt.setLong(4, this.cid);
/* 133 */     pstmt.setString(5, this.open_id);
/* 134 */     pstmt.setInt(6, this.vip_level);
/* 135 */     pstmt.setInt(7, this.level);
/* 136 */     pstmt.setString(8, this.order_id);
/* 137 */     pstmt.setString(9, this.tp_order_id);
/* 138 */     pstmt.setString(10, this.goods_id);
/* 139 */     pstmt.setInt(11, this.money);
/* 140 */     pstmt.setInt(12, this.coin_num);
/* 141 */     pstmt.setString(13, this.status);
/* 142 */     pstmt.setInt(14, this.pay_time);
/* 143 */     pstmt.setString(15, this.pay_time_date);
/* 144 */     pstmt.setInt(16, this.order_time);
/* 145 */     pstmt.setString(17, this.order_time_date);
/* 146 */     pstmt.setString(18, this.adfrom);
/* 147 */     pstmt.setString(19, this.adfrom2);
/* 148 */     pstmt.setString(20, this.ar_ip);
/* 149 */     pstmt.setInt(21, this.ar_time);
/* 150 */     pstmt.setString(22, this.ar_time_date);
/* 151 */     pstmt.setString(23, this.adid);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/logger/flow/impl/db/RechargeLogFlow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */