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
/*     */ public class RedPacketBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家id")
/*     */   private long pid;
/*     */   @DataBaseField(type = "int(11)", fieldname = "left_money", comment = "剩余金额")
/*     */   private int left_money;
/*     */   @DataBaseField(type = "int(11)", fieldname = "max_money", comment = "总金额")
/*     */   private int max_money;
/*     */   @DataBaseField(type = "int(11)", fieldname = "already_pick", comment = "已领取人数")
/*     */   private int already_pick;
/*     */   @DataBaseField(type = "int(11)", fieldname = "max_pick", comment = "最大领取人数")
/*     */   private int max_pick;
/*     */   @DataBaseField(type = "int(11)", fieldname = "time", comment = "红包生成时间")
/*     */   private int time;
/*     */   @DataBaseField(type = "int(11)", fieldname = "packet_type_id", comment = "红包种类id")
/*     */   private int packet_type_id;
/*     */   @DataBaseField(type = "int(11)", fieldname = "status", comment = "领取状态")
/*     */   private int status;
/*     */   
/*     */   public RedPacketBO() {
/*  34 */     this.id = 0L;
/*  35 */     this.pid = 0L;
/*  36 */     this.left_money = 0;
/*  37 */     this.max_money = 0;
/*  38 */     this.already_pick = 0;
/*  39 */     this.max_pick = 0;
/*  40 */     this.time = 0;
/*  41 */     this.packet_type_id = 0;
/*  42 */     this.status = 0;
/*     */   }
/*     */   
/*     */   public RedPacketBO(ResultSet rs) throws Exception {
/*  46 */     this.id = rs.getLong(1);
/*  47 */     this.pid = rs.getLong(2);
/*  48 */     this.left_money = rs.getInt(3);
/*  49 */     this.max_money = rs.getInt(4);
/*  50 */     this.already_pick = rs.getInt(5);
/*  51 */     this.max_pick = rs.getInt(6);
/*  52 */     this.time = rs.getInt(7);
/*  53 */     this.packet_type_id = rs.getInt(8);
/*  54 */     this.status = rs.getInt(9);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<RedPacketBO> list) throws Exception {
/*  60 */     list.add(new RedPacketBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  65 */     return getId();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  70 */     return "`id`, `pid`, `left_money`, `max_money`, `already_pick`, `max_pick`, `time`, `packet_type_id`, `status`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  75 */     return "`red_packet`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  80 */     StringBuilder strBuf = new StringBuilder();
/*  81 */     strBuf.append("'").append(this.id).append("', ");
/*  82 */     strBuf.append("'").append(this.pid).append("', ");
/*  83 */     strBuf.append("'").append(this.left_money).append("', ");
/*  84 */     strBuf.append("'").append(this.max_money).append("', ");
/*  85 */     strBuf.append("'").append(this.already_pick).append("', ");
/*  86 */     strBuf.append("'").append(this.max_pick).append("', ");
/*  87 */     strBuf.append("'").append(this.time).append("', ");
/*  88 */     strBuf.append("'").append(this.packet_type_id).append("', ");
/*  89 */     strBuf.append("'").append(this.status).append("', ");
/*  90 */     strBuf.deleteCharAt(strBuf.length() - 2);
/*  91 */     return strBuf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<byte[]> getInsertValueBytes() {
/*  96 */     ArrayList<byte[]> ret = (ArrayList)new ArrayList<>();
/*  97 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setId(long iID) {
/* 102 */     this.id = iID;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getId() {
/* 107 */     return this.id;
/*     */   }
/*     */   
/*     */   public long getPid() {
/* 111 */     return this.pid;
/*     */   } public void setPid(long pid) {
/* 113 */     if (pid == this.pid)
/*     */       return; 
/* 115 */     this.pid = pid;
/*     */   }
/*     */   public void savePid(long pid) {
/* 118 */     if (pid == this.pid)
/*     */       return; 
/* 120 */     this.pid = pid;
/* 121 */     saveField("pid", Long.valueOf(pid));
/*     */   }
/*     */   
/*     */   public int getLeftMoney() {
/* 125 */     return this.left_money;
/*     */   } public void setLeftMoney(int left_money) {
/* 127 */     if (left_money == this.left_money)
/*     */       return; 
/* 129 */     this.left_money = left_money;
/*     */   }
/*     */   public void saveLeftMoney(int left_money) {
/* 132 */     if (left_money == this.left_money)
/*     */       return; 
/* 134 */     this.left_money = left_money;
/* 135 */     saveField("left_money", Integer.valueOf(left_money));
/*     */   }
/*     */   
/*     */   public int getMaxMoney() {
/* 139 */     return this.max_money;
/*     */   } public void setMaxMoney(int max_money) {
/* 141 */     if (max_money == this.max_money)
/*     */       return; 
/* 143 */     this.max_money = max_money;
/*     */   }
/*     */   public void saveMaxMoney(int max_money) {
/* 146 */     if (max_money == this.max_money)
/*     */       return; 
/* 148 */     this.max_money = max_money;
/* 149 */     saveField("max_money", Integer.valueOf(max_money));
/*     */   }
/*     */   
/*     */   public int getAlreadyPick() {
/* 153 */     return this.already_pick;
/*     */   } public void setAlreadyPick(int already_pick) {
/* 155 */     if (already_pick == this.already_pick)
/*     */       return; 
/* 157 */     this.already_pick = already_pick;
/*     */   }
/*     */   public void saveAlreadyPick(int already_pick) {
/* 160 */     if (already_pick == this.already_pick)
/*     */       return; 
/* 162 */     this.already_pick = already_pick;
/* 163 */     saveField("already_pick", Integer.valueOf(already_pick));
/*     */   }
/*     */   
/*     */   public int getMaxPick() {
/* 167 */     return this.max_pick;
/*     */   } public void setMaxPick(int max_pick) {
/* 169 */     if (max_pick == this.max_pick)
/*     */       return; 
/* 171 */     this.max_pick = max_pick;
/*     */   }
/*     */   public void saveMaxPick(int max_pick) {
/* 174 */     if (max_pick == this.max_pick)
/*     */       return; 
/* 176 */     this.max_pick = max_pick;
/* 177 */     saveField("max_pick", Integer.valueOf(max_pick));
/*     */   }
/*     */   
/*     */   public int getTime() {
/* 181 */     return this.time;
/*     */   } public void setTime(int time) {
/* 183 */     if (time == this.time)
/*     */       return; 
/* 185 */     this.time = time;
/*     */   }
/*     */   public void saveTime(int time) {
/* 188 */     if (time == this.time)
/*     */       return; 
/* 190 */     this.time = time;
/* 191 */     saveField("time", Integer.valueOf(time));
/*     */   }
/*     */   
/*     */   public int getPacketTypeId() {
/* 195 */     return this.packet_type_id;
/*     */   } public void setPacketTypeId(int packet_type_id) {
/* 197 */     if (packet_type_id == this.packet_type_id)
/*     */       return; 
/* 199 */     this.packet_type_id = packet_type_id;
/*     */   }
/*     */   public void savePacketTypeId(int packet_type_id) {
/* 202 */     if (packet_type_id == this.packet_type_id)
/*     */       return; 
/* 204 */     this.packet_type_id = packet_type_id;
/* 205 */     saveField("packet_type_id", Integer.valueOf(packet_type_id));
/*     */   }
/*     */   
/*     */   public int getStatus() {
/* 209 */     return this.status;
/*     */   } public void setStatus(int status) {
/* 211 */     if (status == this.status)
/*     */       return; 
/* 213 */     this.status = status;
/*     */   }
/*     */   public void saveStatus(int status) {
/* 216 */     if (status == this.status)
/*     */       return; 
/* 218 */     this.status = status;
/* 219 */     saveField("status", Integer.valueOf(status));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 226 */     StringBuilder sBuilder = new StringBuilder();
/* 227 */     sBuilder.append(" `pid` = '").append(this.pid).append("',");
/* 228 */     sBuilder.append(" `left_money` = '").append(this.left_money).append("',");
/* 229 */     sBuilder.append(" `max_money` = '").append(this.max_money).append("',");
/* 230 */     sBuilder.append(" `already_pick` = '").append(this.already_pick).append("',");
/* 231 */     sBuilder.append(" `max_pick` = '").append(this.max_pick).append("',");
/* 232 */     sBuilder.append(" `time` = '").append(this.time).append("',");
/* 233 */     sBuilder.append(" `packet_type_id` = '").append(this.packet_type_id).append("',");
/* 234 */     sBuilder.append(" `status` = '").append(this.status).append("',");
/* 235 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 236 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 240 */     String sql = "CREATE TABLE IF NOT EXISTS `red_packet` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家id',`left_money` int(11) NOT NULL DEFAULT '0' COMMENT '剩余金额',`max_money` int(11) NOT NULL DEFAULT '0' COMMENT '总金额',`already_pick` int(11) NOT NULL DEFAULT '0' COMMENT '已领取人数',`max_pick` int(11) NOT NULL DEFAULT '0' COMMENT '最大领取人数',`time` int(11) NOT NULL DEFAULT '0' COMMENT '红包生成时间',`packet_type_id` int(11) NOT NULL DEFAULT '0' COMMENT '红包种类id',`status` int(11) NOT NULL DEFAULT '0' COMMENT '领取状态',PRIMARY KEY (`id`)) COMMENT='红包表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
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
/* 251 */       ServerConfig.getInitialID() + 1L);
/* 252 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/RedPacketBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */