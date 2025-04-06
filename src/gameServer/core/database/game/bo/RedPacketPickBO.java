/*     */ package core.database.game.bo;
/*     */ 
/*     */ import com.zhonglian.server.common.db.BaseBO;
/*     */ import com.zhonglian.server.common.db.annotation.DataBaseField;
/*     */ import core.server.ServerConfig;
/*     */ import java.sql.ResultSet;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ public class RedPacketPickBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家id")
/*     */   private long pid;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "packet_id", comment = "红包id")
/*     */   private long packet_id;
/*     */   @DataBaseField(type = "int(11)", fieldname = "money", comment = "领取金额")
/*     */   private int money;
/*     */   @DataBaseField(type = "int(11)", fieldname = "time", comment = "领取时间")
/*     */   private int time;
/*     */   
/*     */   public RedPacketPickBO() {
/*  26 */     this.id = 0L;
/*  27 */     this.pid = 0L;
/*  28 */     this.packet_id = 0L;
/*  29 */     this.money = 0;
/*  30 */     this.time = 0;
/*     */   }
/*     */   
/*     */   public RedPacketPickBO(ResultSet rs) throws Exception {
/*  34 */     this.id = rs.getLong(1);
/*  35 */     this.pid = rs.getLong(2);
/*  36 */     this.packet_id = rs.getLong(3);
/*  37 */     this.money = rs.getInt(4);
/*  38 */     this.time = rs.getInt(5);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<RedPacketPickBO> list) throws Exception {
/*  44 */     list.add(new RedPacketPickBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  49 */     return getId();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  54 */     return "`id`, `pid`, `packet_id`, `money`, `time`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  59 */     return "`red_packet_pick`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  64 */     StringBuilder strBuf = new StringBuilder();
/*  65 */     strBuf.append("'").append(this.id).append("', ");
/*  66 */     strBuf.append("'").append(this.pid).append("', ");
/*  67 */     strBuf.append("'").append(this.packet_id).append("', ");
/*  68 */     strBuf.append("'").append(this.money).append("', ");
/*  69 */     strBuf.append("'").append(this.time).append("', ");
/*  70 */     strBuf.deleteCharAt(strBuf.length() - 2);
/*  71 */     return strBuf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<byte[]> getInsertValueBytes() {
/*  76 */     ArrayList<byte[]> ret = (ArrayList)new ArrayList<>();
/*  77 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setId(long iID) {
/*  82 */     this.id = iID;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getId() {
/*  87 */     return this.id;
/*     */   }
/*     */   
/*     */   public long getPid() {
/*  91 */     return this.pid;
/*     */   } public void setPid(long pid) {
/*  93 */     if (pid == this.pid)
/*     */       return; 
/*  95 */     this.pid = pid;
/*     */   }
/*     */   public void savePid(long pid) {
/*  98 */     if (pid == this.pid)
/*     */       return; 
/* 100 */     this.pid = pid;
/* 101 */     saveField("pid", Long.valueOf(pid));
/*     */   }
/*     */   
/*     */   public long getPacketId() {
/* 105 */     return this.packet_id;
/*     */   } public void setPacketId(long packet_id) {
/* 107 */     if (packet_id == this.packet_id)
/*     */       return; 
/* 109 */     this.packet_id = packet_id;
/*     */   }
/*     */   public void savePacketId(long packet_id) {
/* 112 */     if (packet_id == this.packet_id)
/*     */       return; 
/* 114 */     this.packet_id = packet_id;
/* 115 */     saveField("packet_id", Long.valueOf(packet_id));
/*     */   }
/*     */   
/*     */   public int getMoney() {
/* 119 */     return this.money;
/*     */   } public void setMoney(int money) {
/* 121 */     if (money == this.money)
/*     */       return; 
/* 123 */     this.money = money;
/*     */   }
/*     */   public void saveMoney(int money) {
/* 126 */     if (money == this.money)
/*     */       return; 
/* 128 */     this.money = money;
/* 129 */     saveField("money", Integer.valueOf(money));
/*     */   }
/*     */   
/*     */   public int getTime() {
/* 133 */     return this.time;
/*     */   } public void setTime(int time) {
/* 135 */     if (time == this.time)
/*     */       return; 
/* 137 */     this.time = time;
/*     */   }
/*     */   public void saveTime(int time) {
/* 140 */     if (time == this.time)
/*     */       return; 
/* 142 */     this.time = time;
/* 143 */     saveField("time", Integer.valueOf(time));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 150 */     StringBuilder sBuilder = new StringBuilder();
/* 151 */     sBuilder.append(" `pid` = '").append(this.pid).append("',");
/* 152 */     sBuilder.append(" `packet_id` = '").append(this.packet_id).append("',");
/* 153 */     sBuilder.append(" `money` = '").append(this.money).append("',");
/* 154 */     sBuilder.append(" `time` = '").append(this.time).append("',");
/* 155 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 156 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 160 */     String sql = "CREATE TABLE IF NOT EXISTS `red_packet_pick` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家id',`packet_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '红包id',`money` int(11) NOT NULL DEFAULT '0' COMMENT '领取金额',`time` int(11) NOT NULL DEFAULT '0' COMMENT '领取时间',PRIMARY KEY (`id`)) COMMENT='红包领取表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 167 */       ServerConfig.getInitialID() + 1L);
/* 168 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/RedPacketPickBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */