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
/*     */ public class FixedTimeMailBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "mailId", comment = "php邮件ID")
/*     */   private long mailId;
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "senderName", comment = "发送者")
/*     */   private String senderName;
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "title", comment = "邮件标题")
/*     */   private String title;
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "content", comment = "邮件描述")
/*     */   private String content;
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "uniformIDList", comment = "物品Id列表")
/*     */   private String uniformIDList;
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "uniformCountList", comment = "物品数量列表")
/*     */   private String uniformCountList;
/*     */   @DataBaseField(type = "int(11)", fieldname = "beginTime", comment = "开始时间")
/*     */   private int beginTime;
/*     */   @DataBaseField(type = "int(11)", fieldname = "hasSendNum", comment = "已发次数")
/*     */   private int hasSendNum;
/*     */   @DataBaseField(type = "int(11)", fieldname = "cyclesNum", comment = "循环次数")
/*     */   private int cyclesNum;
/*     */   
/*     */   public FixedTimeMailBO() {
/*  36 */     this.id = 0L;
/*  37 */     this.mailId = 0L;
/*  38 */     this.senderName = "";
/*  39 */     this.title = "";
/*  40 */     this.content = "";
/*  41 */     this.uniformIDList = "";
/*  42 */     this.uniformCountList = "";
/*  43 */     this.beginTime = 0;
/*  44 */     this.hasSendNum = 0;
/*  45 */     this.cyclesNum = 0;
/*     */   }
/*     */   
/*     */   public FixedTimeMailBO(ResultSet rs) throws Exception {
/*  49 */     this.id = rs.getLong(1);
/*  50 */     this.mailId = rs.getLong(2);
/*  51 */     this.senderName = rs.getString(3);
/*  52 */     this.title = rs.getString(4);
/*  53 */     this.content = rs.getString(5);
/*  54 */     this.uniformIDList = rs.getString(6);
/*  55 */     this.uniformCountList = rs.getString(7);
/*  56 */     this.beginTime = rs.getInt(8);
/*  57 */     this.hasSendNum = rs.getInt(9);
/*  58 */     this.cyclesNum = rs.getInt(10);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<FixedTimeMailBO> list) throws Exception {
/*  64 */     list.add(new FixedTimeMailBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  69 */     return 0L;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  74 */     return "`id`, `mailId`, `senderName`, `title`, `content`, `uniformIDList`, `uniformCountList`, `beginTime`, `hasSendNum`, `cyclesNum`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  79 */     return "`fixedTimeMail`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  84 */     StringBuilder strBuf = new StringBuilder();
/*  85 */     strBuf.append("'").append(this.id).append("', ");
/*  86 */     strBuf.append("'").append(this.mailId).append("', ");
/*  87 */     strBuf.append("'").append((this.senderName == null) ? null : this.senderName.replace("'", "''")).append("', ");
/*  88 */     strBuf.append("'").append((this.title == null) ? null : this.title.replace("'", "''")).append("', ");
/*  89 */     strBuf.append("'").append((this.content == null) ? null : this.content.replace("'", "''")).append("', ");
/*  90 */     strBuf.append("'").append((this.uniformIDList == null) ? null : this.uniformIDList.replace("'", "''")).append("', ");
/*  91 */     strBuf.append("'").append((this.uniformCountList == null) ? null : this.uniformCountList.replace("'", "''")).append("', ");
/*  92 */     strBuf.append("'").append(this.beginTime).append("', ");
/*  93 */     strBuf.append("'").append(this.hasSendNum).append("', ");
/*  94 */     strBuf.append("'").append(this.cyclesNum).append("', ");
/*  95 */     strBuf.deleteCharAt(strBuf.length() - 2);
/*  96 */     return strBuf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<byte[]> getInsertValueBytes() {
/* 101 */     ArrayList<byte[]> ret = (ArrayList)new ArrayList<>();
/* 102 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setId(long iID) {
/* 107 */     this.id = iID;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getId() {
/* 112 */     return this.id;
/*     */   }
/*     */   
/*     */   public long getMailId() {
/* 116 */     return this.mailId;
/*     */   } public void setMailId(long mailId) {
/* 118 */     if (mailId == this.mailId)
/*     */       return; 
/* 120 */     this.mailId = mailId;
/*     */   }
/*     */   public void saveMailId(long mailId) {
/* 123 */     if (mailId == this.mailId)
/*     */       return; 
/* 125 */     this.mailId = mailId;
/* 126 */     saveField("mailId", Long.valueOf(mailId));
/*     */   }
/*     */   
/*     */   public String getSenderName() {
/* 130 */     return this.senderName;
/*     */   } public void setSenderName(String senderName) {
/* 132 */     if (senderName.equals(this.senderName))
/*     */       return; 
/* 134 */     this.senderName = senderName;
/*     */   }
/*     */   public void saveSenderName(String senderName) {
/* 137 */     if (senderName.equals(this.senderName))
/*     */       return; 
/* 139 */     this.senderName = senderName;
/* 140 */     saveField("senderName", senderName);
/*     */   }
/*     */   
/*     */   public String getTitle() {
/* 144 */     return this.title;
/*     */   } public void setTitle(String title) {
/* 146 */     if (title.equals(this.title))
/*     */       return; 
/* 148 */     this.title = title;
/*     */   }
/*     */   public void saveTitle(String title) {
/* 151 */     if (title.equals(this.title))
/*     */       return; 
/* 153 */     this.title = title;
/* 154 */     saveField("title", title);
/*     */   }
/*     */   
/*     */   public String getContent() {
/* 158 */     return this.content;
/*     */   } public void setContent(String content) {
/* 160 */     if (content.equals(this.content))
/*     */       return; 
/* 162 */     this.content = content;
/*     */   }
/*     */   public void saveContent(String content) {
/* 165 */     if (content.equals(this.content))
/*     */       return; 
/* 167 */     this.content = content;
/* 168 */     saveField("content", content);
/*     */   }
/*     */   
/*     */   public String getUniformIDList() {
/* 172 */     return this.uniformIDList;
/*     */   } public void setUniformIDList(String uniformIDList) {
/* 174 */     if (uniformIDList.equals(this.uniformIDList))
/*     */       return; 
/* 176 */     this.uniformIDList = uniformIDList;
/*     */   }
/*     */   public void saveUniformIDList(String uniformIDList) {
/* 179 */     if (uniformIDList.equals(this.uniformIDList))
/*     */       return; 
/* 181 */     this.uniformIDList = uniformIDList;
/* 182 */     saveField("uniformIDList", uniformIDList);
/*     */   }
/*     */   
/*     */   public String getUniformCountList() {
/* 186 */     return this.uniformCountList;
/*     */   } public void setUniformCountList(String uniformCountList) {
/* 188 */     if (uniformCountList.equals(this.uniformCountList))
/*     */       return; 
/* 190 */     this.uniformCountList = uniformCountList;
/*     */   }
/*     */   public void saveUniformCountList(String uniformCountList) {
/* 193 */     if (uniformCountList.equals(this.uniformCountList))
/*     */       return; 
/* 195 */     this.uniformCountList = uniformCountList;
/* 196 */     saveField("uniformCountList", uniformCountList);
/*     */   }
/*     */   
/*     */   public int getBeginTime() {
/* 200 */     return this.beginTime;
/*     */   } public void setBeginTime(int beginTime) {
/* 202 */     if (beginTime == this.beginTime)
/*     */       return; 
/* 204 */     this.beginTime = beginTime;
/*     */   }
/*     */   public void saveBeginTime(int beginTime) {
/* 207 */     if (beginTime == this.beginTime)
/*     */       return; 
/* 209 */     this.beginTime = beginTime;
/* 210 */     saveField("beginTime", Integer.valueOf(beginTime));
/*     */   }
/*     */   
/*     */   public int getHasSendNum() {
/* 214 */     return this.hasSendNum;
/*     */   } public void setHasSendNum(int hasSendNum) {
/* 216 */     if (hasSendNum == this.hasSendNum)
/*     */       return; 
/* 218 */     this.hasSendNum = hasSendNum;
/*     */   }
/*     */   public void saveHasSendNum(int hasSendNum) {
/* 221 */     if (hasSendNum == this.hasSendNum)
/*     */       return; 
/* 223 */     this.hasSendNum = hasSendNum;
/* 224 */     saveField("hasSendNum", Integer.valueOf(hasSendNum));
/*     */   }
/*     */   
/*     */   public int getCyclesNum() {
/* 228 */     return this.cyclesNum;
/*     */   } public void setCyclesNum(int cyclesNum) {
/* 230 */     if (cyclesNum == this.cyclesNum)
/*     */       return; 
/* 232 */     this.cyclesNum = cyclesNum;
/*     */   }
/*     */   public void saveCyclesNum(int cyclesNum) {
/* 235 */     if (cyclesNum == this.cyclesNum)
/*     */       return; 
/* 237 */     this.cyclesNum = cyclesNum;
/* 238 */     saveField("cyclesNum", Integer.valueOf(cyclesNum));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 245 */     StringBuilder sBuilder = new StringBuilder();
/* 246 */     sBuilder.append(" `mailId` = '").append(this.mailId).append("',");
/* 247 */     sBuilder.append(" `senderName` = '").append((this.senderName == null) ? null : this.senderName.replace("'", "''")).append("',");
/* 248 */     sBuilder.append(" `title` = '").append((this.title == null) ? null : this.title.replace("'", "''")).append("',");
/* 249 */     sBuilder.append(" `content` = '").append((this.content == null) ? null : this.content.replace("'", "''")).append("',");
/* 250 */     sBuilder.append(" `uniformIDList` = '").append((this.uniformIDList == null) ? null : this.uniformIDList.replace("'", "''")).append("',");
/* 251 */     sBuilder.append(" `uniformCountList` = '").append((this.uniformCountList == null) ? null : this.uniformCountList.replace("'", "''")).append("',");
/* 252 */     sBuilder.append(" `beginTime` = '").append(this.beginTime).append("',");
/* 253 */     sBuilder.append(" `hasSendNum` = '").append(this.hasSendNum).append("',");
/* 254 */     sBuilder.append(" `cyclesNum` = '").append(this.cyclesNum).append("',");
/* 255 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 256 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 260 */     String sql = "CREATE TABLE IF NOT EXISTS `fixedTimeMail` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`mailId` bigint(20) NOT NULL DEFAULT '0' COMMENT 'php邮件ID',`senderName` varchar(500) NOT NULL DEFAULT '' COMMENT '发送者',`title` varchar(500) NOT NULL DEFAULT '' COMMENT '邮件标题',`content` varchar(500) NOT NULL DEFAULT '' COMMENT '邮件描述',`uniformIDList` varchar(500) NOT NULL DEFAULT '' COMMENT '物品Id列表',`uniformCountList` varchar(500) NOT NULL DEFAULT '' COMMENT '物品数量列表',`beginTime` int(11) NOT NULL DEFAULT '0' COMMENT '开始时间',`hasSendNum` int(11) NOT NULL DEFAULT '0' COMMENT '已发次数',`cyclesNum` int(11) NOT NULL DEFAULT '0' COMMENT '循环次数',PRIMARY KEY (`id`)) COMMENT='后台推送定时邮件'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
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
/* 272 */       ServerConfig.getInitialID() + 1L);
/* 273 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/FixedTimeMailBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */