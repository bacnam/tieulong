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
/*     */ public class PlayerMailBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家ID")
/*     */   private long pid;
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "senderName", comment = "发送者名称")
/*     */   private String senderName;
/*     */   @DataBaseField(type = "varchar(200)", fieldname = "title", comment = "邮件标题")
/*     */   private String title;
/*     */   @DataBaseField(type = "varchar(1000)", fieldname = "content", comment = "邮件描述")
/*     */   private String content;
/*     */   @DataBaseField(type = "int(11)", fieldname = "createTime", comment = "邮件发送时间(单位s)")
/*     */   private int createTime;
/*     */   @DataBaseField(type = "int(11)", fieldname = "existTime", comment = "邮件生存时间(s)")
/*     */   private int existTime;
/*     */   @DataBaseField(type = "int(11)", fieldname = "pickUpTime", comment = "读取领取时间(单位s)")
/*     */   private int pickUpTime;
/*     */   @DataBaseField(type = "int(11)", fieldname = "pickUpExistTime", comment = "读取领取后生存时间(单位s)")
/*     */   private int pickUpExistTime;
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "uniformIDList", comment = "物品Id列表")
/*     */   private String uniformIDList;
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "uniformCountList", comment = "物品数量列表")
/*     */   private String uniformCountList;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "globalMailID", comment = "全体邮件的ID")
/*     */   private long globalMailID;
/*     */   
/*     */   public PlayerMailBO() {
/*  40 */     this.id = 0L;
/*  41 */     this.pid = 0L;
/*  42 */     this.senderName = "";
/*  43 */     this.title = "";
/*  44 */     this.content = "";
/*  45 */     this.createTime = 0;
/*  46 */     this.existTime = 0;
/*  47 */     this.pickUpTime = 0;
/*  48 */     this.pickUpExistTime = 0;
/*  49 */     this.uniformIDList = "";
/*  50 */     this.uniformCountList = "";
/*  51 */     this.globalMailID = 0L;
/*     */   }
/*     */   
/*     */   public PlayerMailBO(ResultSet rs) throws Exception {
/*  55 */     this.id = rs.getLong(1);
/*  56 */     this.pid = rs.getLong(2);
/*  57 */     this.senderName = rs.getString(3);
/*  58 */     this.title = rs.getString(4);
/*  59 */     this.content = rs.getString(5);
/*  60 */     this.createTime = rs.getInt(6);
/*  61 */     this.existTime = rs.getInt(7);
/*  62 */     this.pickUpTime = rs.getInt(8);
/*  63 */     this.pickUpExistTime = rs.getInt(9);
/*  64 */     this.uniformIDList = rs.getString(10);
/*  65 */     this.uniformCountList = rs.getString(11);
/*  66 */     this.globalMailID = rs.getLong(12);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<PlayerMailBO> list) throws Exception {
/*  72 */     list.add(new PlayerMailBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  77 */     return getPid();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  82 */     return "`id`, `pid`, `senderName`, `title`, `content`, `createTime`, `existTime`, `pickUpTime`, `pickUpExistTime`, `uniformIDList`, `uniformCountList`, `globalMailID`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  87 */     return "`playerMail`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  92 */     StringBuilder strBuf = new StringBuilder();
/*  93 */     strBuf.append("'").append(this.id).append("', ");
/*  94 */     strBuf.append("'").append(this.pid).append("', ");
/*  95 */     strBuf.append("'").append((this.senderName == null) ? null : this.senderName.replace("'", "''")).append("', ");
/*  96 */     strBuf.append("'").append((this.title == null) ? null : this.title.replace("'", "''")).append("', ");
/*  97 */     strBuf.append("'").append((this.content == null) ? null : this.content.replace("'", "''")).append("', ");
/*  98 */     strBuf.append("'").append(this.createTime).append("', ");
/*  99 */     strBuf.append("'").append(this.existTime).append("', ");
/* 100 */     strBuf.append("'").append(this.pickUpTime).append("', ");
/* 101 */     strBuf.append("'").append(this.pickUpExistTime).append("', ");
/* 102 */     strBuf.append("'").append((this.uniformIDList == null) ? null : this.uniformIDList.replace("'", "''")).append("', ");
/* 103 */     strBuf.append("'").append((this.uniformCountList == null) ? null : this.uniformCountList.replace("'", "''")).append("', ");
/* 104 */     strBuf.append("'").append(this.globalMailID).append("', ");
/* 105 */     strBuf.deleteCharAt(strBuf.length() - 2);
/* 106 */     return strBuf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<byte[]> getInsertValueBytes() {
/* 111 */     ArrayList<byte[]> ret = (ArrayList)new ArrayList<>();
/* 112 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setId(long iID) {
/* 117 */     this.id = iID;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getId() {
/* 122 */     return this.id;
/*     */   }
/*     */   
/*     */   public long getPid() {
/* 126 */     return this.pid;
/*     */   } public void setPid(long pid) {
/* 128 */     if (pid == this.pid)
/*     */       return; 
/* 130 */     this.pid = pid;
/*     */   }
/*     */   public void savePid(long pid) {
/* 133 */     if (pid == this.pid)
/*     */       return; 
/* 135 */     this.pid = pid;
/* 136 */     saveField("pid", Long.valueOf(pid));
/*     */   }
/*     */   
/*     */   public String getSenderName() {
/* 140 */     return this.senderName;
/*     */   } public void setSenderName(String senderName) {
/* 142 */     if (senderName.equals(this.senderName))
/*     */       return; 
/* 144 */     this.senderName = senderName;
/*     */   }
/*     */   public void saveSenderName(String senderName) {
/* 147 */     if (senderName.equals(this.senderName))
/*     */       return; 
/* 149 */     this.senderName = senderName;
/* 150 */     saveField("senderName", senderName);
/*     */   }
/*     */   
/*     */   public String getTitle() {
/* 154 */     return this.title;
/*     */   } public void setTitle(String title) {
/* 156 */     if (title.equals(this.title))
/*     */       return; 
/* 158 */     this.title = title;
/*     */   }
/*     */   public void saveTitle(String title) {
/* 161 */     if (title.equals(this.title))
/*     */       return; 
/* 163 */     this.title = title;
/* 164 */     saveField("title", title);
/*     */   }
/*     */   
/*     */   public String getContent() {
/* 168 */     return this.content;
/*     */   } public void setContent(String content) {
/* 170 */     if (content.equals(this.content))
/*     */       return; 
/* 172 */     this.content = content;
/*     */   }
/*     */   public void saveContent(String content) {
/* 175 */     if (content.equals(this.content))
/*     */       return; 
/* 177 */     this.content = content;
/* 178 */     saveField("content", content);
/*     */   }
/*     */   
/*     */   public int getCreateTime() {
/* 182 */     return this.createTime;
/*     */   } public void setCreateTime(int createTime) {
/* 184 */     if (createTime == this.createTime)
/*     */       return; 
/* 186 */     this.createTime = createTime;
/*     */   }
/*     */   public void saveCreateTime(int createTime) {
/* 189 */     if (createTime == this.createTime)
/*     */       return; 
/* 191 */     this.createTime = createTime;
/* 192 */     saveField("createTime", Integer.valueOf(createTime));
/*     */   }
/*     */   
/*     */   public int getExistTime() {
/* 196 */     return this.existTime;
/*     */   } public void setExistTime(int existTime) {
/* 198 */     if (existTime == this.existTime)
/*     */       return; 
/* 200 */     this.existTime = existTime;
/*     */   }
/*     */   public void saveExistTime(int existTime) {
/* 203 */     if (existTime == this.existTime)
/*     */       return; 
/* 205 */     this.existTime = existTime;
/* 206 */     saveField("existTime", Integer.valueOf(existTime));
/*     */   }
/*     */   
/*     */   public int getPickUpTime() {
/* 210 */     return this.pickUpTime;
/*     */   } public void setPickUpTime(int pickUpTime) {
/* 212 */     if (pickUpTime == this.pickUpTime)
/*     */       return; 
/* 214 */     this.pickUpTime = pickUpTime;
/*     */   }
/*     */   public void savePickUpTime(int pickUpTime) {
/* 217 */     if (pickUpTime == this.pickUpTime)
/*     */       return; 
/* 219 */     this.pickUpTime = pickUpTime;
/* 220 */     saveField("pickUpTime", Integer.valueOf(pickUpTime));
/*     */   }
/*     */   
/*     */   public int getPickUpExistTime() {
/* 224 */     return this.pickUpExistTime;
/*     */   } public void setPickUpExistTime(int pickUpExistTime) {
/* 226 */     if (pickUpExistTime == this.pickUpExistTime)
/*     */       return; 
/* 228 */     this.pickUpExistTime = pickUpExistTime;
/*     */   }
/*     */   public void savePickUpExistTime(int pickUpExistTime) {
/* 231 */     if (pickUpExistTime == this.pickUpExistTime)
/*     */       return; 
/* 233 */     this.pickUpExistTime = pickUpExistTime;
/* 234 */     saveField("pickUpExistTime", Integer.valueOf(pickUpExistTime));
/*     */   }
/*     */   
/*     */   public String getUniformIDList() {
/* 238 */     return this.uniformIDList;
/*     */   } public void setUniformIDList(String uniformIDList) {
/* 240 */     if (uniformIDList.equals(this.uniformIDList))
/*     */       return; 
/* 242 */     this.uniformIDList = uniformIDList;
/*     */   }
/*     */   public void saveUniformIDList(String uniformIDList) {
/* 245 */     if (uniformIDList.equals(this.uniformIDList))
/*     */       return; 
/* 247 */     this.uniformIDList = uniformIDList;
/* 248 */     saveField("uniformIDList", uniformIDList);
/*     */   }
/*     */   
/*     */   public String getUniformCountList() {
/* 252 */     return this.uniformCountList;
/*     */   } public void setUniformCountList(String uniformCountList) {
/* 254 */     if (uniformCountList.equals(this.uniformCountList))
/*     */       return; 
/* 256 */     this.uniformCountList = uniformCountList;
/*     */   }
/*     */   public void saveUniformCountList(String uniformCountList) {
/* 259 */     if (uniformCountList.equals(this.uniformCountList))
/*     */       return; 
/* 261 */     this.uniformCountList = uniformCountList;
/* 262 */     saveField("uniformCountList", uniformCountList);
/*     */   }
/*     */   
/*     */   public long getGlobalMailID() {
/* 266 */     return this.globalMailID;
/*     */   } public void setGlobalMailID(long globalMailID) {
/* 268 */     if (globalMailID == this.globalMailID)
/*     */       return; 
/* 270 */     this.globalMailID = globalMailID;
/*     */   }
/*     */   public void saveGlobalMailID(long globalMailID) {
/* 273 */     if (globalMailID == this.globalMailID)
/*     */       return; 
/* 275 */     this.globalMailID = globalMailID;
/* 276 */     saveField("globalMailID", Long.valueOf(globalMailID));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 283 */     StringBuilder sBuilder = new StringBuilder();
/* 284 */     sBuilder.append(" `pid` = '").append(this.pid).append("',");
/* 285 */     sBuilder.append(" `senderName` = '").append((this.senderName == null) ? null : this.senderName.replace("'", "''")).append("',");
/* 286 */     sBuilder.append(" `title` = '").append((this.title == null) ? null : this.title.replace("'", "''")).append("',");
/* 287 */     sBuilder.append(" `content` = '").append((this.content == null) ? null : this.content.replace("'", "''")).append("',");
/* 288 */     sBuilder.append(" `createTime` = '").append(this.createTime).append("',");
/* 289 */     sBuilder.append(" `existTime` = '").append(this.existTime).append("',");
/* 290 */     sBuilder.append(" `pickUpTime` = '").append(this.pickUpTime).append("',");
/* 291 */     sBuilder.append(" `pickUpExistTime` = '").append(this.pickUpExistTime).append("',");
/* 292 */     sBuilder.append(" `uniformIDList` = '").append((this.uniformIDList == null) ? null : this.uniformIDList.replace("'", "''")).append("',");
/* 293 */     sBuilder.append(" `uniformCountList` = '").append((this.uniformCountList == null) ? null : this.uniformCountList.replace("'", "''")).append("',");
/* 294 */     sBuilder.append(" `globalMailID` = '").append(this.globalMailID).append("',");
/* 295 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 296 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 300 */     String sql = "CREATE TABLE IF NOT EXISTS `playerMail` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家ID',`senderName` varchar(500) NOT NULL DEFAULT '' COMMENT '发送者名称',`title` varchar(200) NOT NULL DEFAULT '' COMMENT '邮件标题',`content` varchar(1000) NOT NULL DEFAULT '' COMMENT '邮件描述',`createTime` int(11) NOT NULL DEFAULT '0' COMMENT '邮件发送时间(单位s)',`existTime` int(11) NOT NULL DEFAULT '0' COMMENT '邮件生存时间(s)',`pickUpTime` int(11) NOT NULL DEFAULT '0' COMMENT '读取领取时间(单位s)',`pickUpExistTime` int(11) NOT NULL DEFAULT '0' COMMENT '读取领取后生存时间(单位s)',`uniformIDList` varchar(500) NOT NULL DEFAULT '' COMMENT '物品Id列表',`uniformCountList` varchar(500) NOT NULL DEFAULT '' COMMENT '物品数量列表',`globalMailID` bigint(20) NOT NULL DEFAULT '0' COMMENT '全体邮件的ID',KEY `pid` (`pid`),PRIMARY KEY (`id`)) COMMENT='邮件信息'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
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
/* 315 */       ServerConfig.getInitialID() + 1L);
/* 316 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/PlayerMailBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */