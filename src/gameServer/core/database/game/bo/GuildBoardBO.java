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
/*     */ public class GuildBoardBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "留言玩家id")
/*     */   private long pid;
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "name", comment = "玩家名称")
/*     */   private String name;
/*     */   @DataBaseField(type = "int(11)", fieldname = "job", comment = "玩家职位")
/*     */   private int job;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "guildId", comment = "帮会ID")
/*     */   private long guildId;
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "message", comment = "留言内容")
/*     */   private String message;
/*     */   @DataBaseField(type = "int(11)", fieldname = "messageTime", comment = "留言时间")
/*     */   private int messageTime;
/*     */   @DataBaseField(type = "tinyint(1)", fieldname = "top", comment = "置顶")
/*     */   private boolean top;
/*     */   
/*     */   public GuildBoardBO() {
/*  32 */     this.id = 0L;
/*  33 */     this.pid = 0L;
/*  34 */     this.name = "";
/*  35 */     this.job = 0;
/*  36 */     this.guildId = 0L;
/*  37 */     this.message = "";
/*  38 */     this.messageTime = 0;
/*  39 */     this.top = false;
/*     */   }
/*     */   
/*     */   public GuildBoardBO(ResultSet rs) throws Exception {
/*  43 */     this.id = rs.getLong(1);
/*  44 */     this.pid = rs.getLong(2);
/*  45 */     this.name = rs.getString(3);
/*  46 */     this.job = rs.getInt(4);
/*  47 */     this.guildId = rs.getLong(5);
/*  48 */     this.message = rs.getString(6);
/*  49 */     this.messageTime = rs.getInt(7);
/*  50 */     this.top = rs.getBoolean(8);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<GuildBoardBO> list) throws Exception {
/*  56 */     list.add(new GuildBoardBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  61 */     return getPid();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  66 */     return "`id`, `pid`, `name`, `job`, `guildId`, `message`, `messageTime`, `top`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  71 */     return "`guildBoard`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  76 */     StringBuilder strBuf = new StringBuilder();
/*  77 */     strBuf.append("'").append(this.id).append("', ");
/*  78 */     strBuf.append("'").append(this.pid).append("', ");
/*  79 */     strBuf.append("'").append((this.name == null) ? null : this.name.replace("'", "''")).append("', ");
/*  80 */     strBuf.append("'").append(this.job).append("', ");
/*  81 */     strBuf.append("'").append(this.guildId).append("', ");
/*  82 */     strBuf.append("'").append((this.message == null) ? null : this.message.replace("'", "''")).append("', ");
/*  83 */     strBuf.append("'").append(this.messageTime).append("', ");
/*  84 */     strBuf.append("'").append(this.top ? 1 : 0).append("', ");
/*  85 */     strBuf.deleteCharAt(strBuf.length() - 2);
/*  86 */     return strBuf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<byte[]> getInsertValueBytes() {
/*  91 */     ArrayList<byte[]> ret = (ArrayList)new ArrayList<>();
/*  92 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setId(long iID) {
/*  97 */     this.id = iID;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getId() {
/* 102 */     return this.id;
/*     */   }
/*     */   
/*     */   public long getPid() {
/* 106 */     return this.pid;
/*     */   } public void setPid(long pid) {
/* 108 */     if (pid == this.pid)
/*     */       return; 
/* 110 */     this.pid = pid;
/*     */   }
/*     */   public void savePid(long pid) {
/* 113 */     if (pid == this.pid)
/*     */       return; 
/* 115 */     this.pid = pid;
/* 116 */     saveField("pid", Long.valueOf(pid));
/*     */   }
/*     */   
/*     */   public String getName() {
/* 120 */     return this.name;
/*     */   } public void setName(String name) {
/* 122 */     if (name.equals(this.name))
/*     */       return; 
/* 124 */     this.name = name;
/*     */   }
/*     */   public void saveName(String name) {
/* 127 */     if (name.equals(this.name))
/*     */       return; 
/* 129 */     this.name = name;
/* 130 */     saveField("name", name);
/*     */   }
/*     */   
/*     */   public int getJob() {
/* 134 */     return this.job;
/*     */   } public void setJob(int job) {
/* 136 */     if (job == this.job)
/*     */       return; 
/* 138 */     this.job = job;
/*     */   }
/*     */   public void saveJob(int job) {
/* 141 */     if (job == this.job)
/*     */       return; 
/* 143 */     this.job = job;
/* 144 */     saveField("job", Integer.valueOf(job));
/*     */   }
/*     */   
/*     */   public long getGuildId() {
/* 148 */     return this.guildId;
/*     */   } public void setGuildId(long guildId) {
/* 150 */     if (guildId == this.guildId)
/*     */       return; 
/* 152 */     this.guildId = guildId;
/*     */   }
/*     */   public void saveGuildId(long guildId) {
/* 155 */     if (guildId == this.guildId)
/*     */       return; 
/* 157 */     this.guildId = guildId;
/* 158 */     saveField("guildId", Long.valueOf(guildId));
/*     */   }
/*     */   
/*     */   public String getMessage() {
/* 162 */     return this.message;
/*     */   } public void setMessage(String message) {
/* 164 */     if (message.equals(this.message))
/*     */       return; 
/* 166 */     this.message = message;
/*     */   }
/*     */   public void saveMessage(String message) {
/* 169 */     if (message.equals(this.message))
/*     */       return; 
/* 171 */     this.message = message;
/* 172 */     saveField("message", message);
/*     */   }
/*     */   
/*     */   public int getMessageTime() {
/* 176 */     return this.messageTime;
/*     */   } public void setMessageTime(int messageTime) {
/* 178 */     if (messageTime == this.messageTime)
/*     */       return; 
/* 180 */     this.messageTime = messageTime;
/*     */   }
/*     */   public void saveMessageTime(int messageTime) {
/* 183 */     if (messageTime == this.messageTime)
/*     */       return; 
/* 185 */     this.messageTime = messageTime;
/* 186 */     saveField("messageTime", Integer.valueOf(messageTime));
/*     */   }
/*     */   
/*     */   public boolean getTop() {
/* 190 */     return this.top;
/*     */   } public void setTop(boolean top) {
/* 192 */     if (top == this.top)
/*     */       return; 
/* 194 */     this.top = top;
/*     */   }
/*     */   public void saveTop(boolean top) {
/* 197 */     if (top == this.top)
/*     */       return; 
/* 199 */     this.top = top;
/* 200 */     saveField("top", Integer.valueOf(top ? 1 : 0));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 207 */     StringBuilder sBuilder = new StringBuilder();
/* 208 */     sBuilder.append(" `pid` = '").append(this.pid).append("',");
/* 209 */     sBuilder.append(" `name` = '").append((this.name == null) ? null : this.name.replace("'", "''")).append("',");
/* 210 */     sBuilder.append(" `job` = '").append(this.job).append("',");
/* 211 */     sBuilder.append(" `guildId` = '").append(this.guildId).append("',");
/* 212 */     sBuilder.append(" `message` = '").append((this.message == null) ? null : this.message.replace("'", "''")).append("',");
/* 213 */     sBuilder.append(" `messageTime` = '").append(this.messageTime).append("',");
/* 214 */     sBuilder.append(" `top` = '").append(this.top ? 1 : 0).append("',");
/* 215 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 216 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 220 */     String sql = "CREATE TABLE IF NOT EXISTS `guildBoard` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '留言玩家id',`name` varchar(500) NOT NULL DEFAULT '' COMMENT '玩家名称',`job` int(11) NOT NULL DEFAULT '0' COMMENT '玩家职位',`guildId` bigint(20) NOT NULL DEFAULT '0' COMMENT '帮会ID',`message` varchar(500) NOT NULL DEFAULT '' COMMENT '留言内容',`messageTime` int(11) NOT NULL DEFAULT '0' COMMENT '留言时间',`top` tinyint(1) NOT NULL DEFAULT '0' COMMENT '置顶',PRIMARY KEY (`id`)) COMMENT='竞技场信息表,记录每个玩家竞技场相关信息'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 230 */       ServerConfig.getInitialID() + 1L);
/* 231 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/GuildBoardBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */