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
/*     */ public class GlobalMailBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "senderName", comment = "发送者名称")
/*     */   private String senderName;
/*     */   @DataBaseField(type = "varchar(200)", fieldname = "title", comment = "邮件标题")
/*     */   private String title;
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "content", comment = "邮件描述")
/*     */   private String content;
/*     */   @DataBaseField(type = "int(11)", fieldname = "createTime", comment = "邮件发送时间(单位s)")
/*     */   private int createTime;
/*     */   @DataBaseField(type = "int(11)", fieldname = "existTime", comment = "邮件生存时间(s)")
/*     */   private int existTime;
/*     */   @DataBaseField(type = "int(11)", fieldname = "pickUpExistTime", comment = "读取领取后生存时间(单位s)")
/*     */   private int pickUpExistTime;
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "uniformIDList", comment = "物品Id列表")
/*     */   private String uniformIDList;
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "uniformCountList", comment = "物品数量列表")
/*     */   private String uniformCountList;
/*     */   
/*     */   public GlobalMailBO() {
/*  34 */     this.id = 0L;
/*  35 */     this.senderName = "";
/*  36 */     this.title = "";
/*  37 */     this.content = "";
/*  38 */     this.createTime = 0;
/*  39 */     this.existTime = 0;
/*  40 */     this.pickUpExistTime = 0;
/*  41 */     this.uniformIDList = "";
/*  42 */     this.uniformCountList = "";
/*     */   }
/*     */   
/*     */   public GlobalMailBO(ResultSet rs) throws Exception {
/*  46 */     this.id = rs.getLong(1);
/*  47 */     this.senderName = rs.getString(2);
/*  48 */     this.title = rs.getString(3);
/*  49 */     this.content = rs.getString(4);
/*  50 */     this.createTime = rs.getInt(5);
/*  51 */     this.existTime = rs.getInt(6);
/*  52 */     this.pickUpExistTime = rs.getInt(7);
/*  53 */     this.uniformIDList = rs.getString(8);
/*  54 */     this.uniformCountList = rs.getString(9);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<GlobalMailBO> list) throws Exception {
/*  60 */     list.add(new GlobalMailBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  65 */     return 0L;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  70 */     return "`id`, `senderName`, `title`, `content`, `createTime`, `existTime`, `pickUpExistTime`, `uniformIDList`, `uniformCountList`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  75 */     return "`globalMail`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  80 */     StringBuilder strBuf = new StringBuilder();
/*  81 */     strBuf.append("'").append(this.id).append("', ");
/*  82 */     strBuf.append("'").append((this.senderName == null) ? null : this.senderName.replace("'", "''")).append("', ");
/*  83 */     strBuf.append("'").append((this.title == null) ? null : this.title.replace("'", "''")).append("', ");
/*  84 */     strBuf.append("'").append((this.content == null) ? null : this.content.replace("'", "''")).append("', ");
/*  85 */     strBuf.append("'").append(this.createTime).append("', ");
/*  86 */     strBuf.append("'").append(this.existTime).append("', ");
/*  87 */     strBuf.append("'").append(this.pickUpExistTime).append("', ");
/*  88 */     strBuf.append("'").append((this.uniformIDList == null) ? null : this.uniformIDList.replace("'", "''")).append("', ");
/*  89 */     strBuf.append("'").append((this.uniformCountList == null) ? null : this.uniformCountList.replace("'", "''")).append("', ");
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
/*     */   public String getSenderName() {
/* 111 */     return this.senderName;
/*     */   } public void setSenderName(String senderName) {
/* 113 */     if (senderName.equals(this.senderName))
/*     */       return; 
/* 115 */     this.senderName = senderName;
/*     */   }
/*     */   public void saveSenderName(String senderName) {
/* 118 */     if (senderName.equals(this.senderName))
/*     */       return; 
/* 120 */     this.senderName = senderName;
/* 121 */     saveField("senderName", senderName);
/*     */   }
/*     */   
/*     */   public String getTitle() {
/* 125 */     return this.title;
/*     */   } public void setTitle(String title) {
/* 127 */     if (title.equals(this.title))
/*     */       return; 
/* 129 */     this.title = title;
/*     */   }
/*     */   public void saveTitle(String title) {
/* 132 */     if (title.equals(this.title))
/*     */       return; 
/* 134 */     this.title = title;
/* 135 */     saveField("title", title);
/*     */   }
/*     */   
/*     */   public String getContent() {
/* 139 */     return this.content;
/*     */   } public void setContent(String content) {
/* 141 */     if (content.equals(this.content))
/*     */       return; 
/* 143 */     this.content = content;
/*     */   }
/*     */   public void saveContent(String content) {
/* 146 */     if (content.equals(this.content))
/*     */       return; 
/* 148 */     this.content = content;
/* 149 */     saveField("content", content);
/*     */   }
/*     */   
/*     */   public int getCreateTime() {
/* 153 */     return this.createTime;
/*     */   } public void setCreateTime(int createTime) {
/* 155 */     if (createTime == this.createTime)
/*     */       return; 
/* 157 */     this.createTime = createTime;
/*     */   }
/*     */   public void saveCreateTime(int createTime) {
/* 160 */     if (createTime == this.createTime)
/*     */       return; 
/* 162 */     this.createTime = createTime;
/* 163 */     saveField("createTime", Integer.valueOf(createTime));
/*     */   }
/*     */   
/*     */   public int getExistTime() {
/* 167 */     return this.existTime;
/*     */   } public void setExistTime(int existTime) {
/* 169 */     if (existTime == this.existTime)
/*     */       return; 
/* 171 */     this.existTime = existTime;
/*     */   }
/*     */   public void saveExistTime(int existTime) {
/* 174 */     if (existTime == this.existTime)
/*     */       return; 
/* 176 */     this.existTime = existTime;
/* 177 */     saveField("existTime", Integer.valueOf(existTime));
/*     */   }
/*     */   
/*     */   public int getPickUpExistTime() {
/* 181 */     return this.pickUpExistTime;
/*     */   } public void setPickUpExistTime(int pickUpExistTime) {
/* 183 */     if (pickUpExistTime == this.pickUpExistTime)
/*     */       return; 
/* 185 */     this.pickUpExistTime = pickUpExistTime;
/*     */   }
/*     */   public void savePickUpExistTime(int pickUpExistTime) {
/* 188 */     if (pickUpExistTime == this.pickUpExistTime)
/*     */       return; 
/* 190 */     this.pickUpExistTime = pickUpExistTime;
/* 191 */     saveField("pickUpExistTime", Integer.valueOf(pickUpExistTime));
/*     */   }
/*     */   
/*     */   public String getUniformIDList() {
/* 195 */     return this.uniformIDList;
/*     */   } public void setUniformIDList(String uniformIDList) {
/* 197 */     if (uniformIDList.equals(this.uniformIDList))
/*     */       return; 
/* 199 */     this.uniformIDList = uniformIDList;
/*     */   }
/*     */   public void saveUniformIDList(String uniformIDList) {
/* 202 */     if (uniformIDList.equals(this.uniformIDList))
/*     */       return; 
/* 204 */     this.uniformIDList = uniformIDList;
/* 205 */     saveField("uniformIDList", uniformIDList);
/*     */   }
/*     */   
/*     */   public String getUniformCountList() {
/* 209 */     return this.uniformCountList;
/*     */   } public void setUniformCountList(String uniformCountList) {
/* 211 */     if (uniformCountList.equals(this.uniformCountList))
/*     */       return; 
/* 213 */     this.uniformCountList = uniformCountList;
/*     */   }
/*     */   public void saveUniformCountList(String uniformCountList) {
/* 216 */     if (uniformCountList.equals(this.uniformCountList))
/*     */       return; 
/* 218 */     this.uniformCountList = uniformCountList;
/* 219 */     saveField("uniformCountList", uniformCountList);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 226 */     StringBuilder sBuilder = new StringBuilder();
/* 227 */     sBuilder.append(" `senderName` = '").append((this.senderName == null) ? null : this.senderName.replace("'", "''")).append("',");
/* 228 */     sBuilder.append(" `title` = '").append((this.title == null) ? null : this.title.replace("'", "''")).append("',");
/* 229 */     sBuilder.append(" `content` = '").append((this.content == null) ? null : this.content.replace("'", "''")).append("',");
/* 230 */     sBuilder.append(" `createTime` = '").append(this.createTime).append("',");
/* 231 */     sBuilder.append(" `existTime` = '").append(this.existTime).append("',");
/* 232 */     sBuilder.append(" `pickUpExistTime` = '").append(this.pickUpExistTime).append("',");
/* 233 */     sBuilder.append(" `uniformIDList` = '").append((this.uniformIDList == null) ? null : this.uniformIDList.replace("'", "''")).append("',");
/* 234 */     sBuilder.append(" `uniformCountList` = '").append((this.uniformCountList == null) ? null : this.uniformCountList.replace("'", "''")).append("',");
/* 235 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 236 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 240 */     String sql = "CREATE TABLE IF NOT EXISTS `globalMail` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`senderName` varchar(500) NOT NULL DEFAULT '' COMMENT '发送者名称',`title` varchar(200) NOT NULL DEFAULT '' COMMENT '邮件标题',`content` varchar(500) NOT NULL DEFAULT '' COMMENT '邮件描述',`createTime` int(11) NOT NULL DEFAULT '0' COMMENT '邮件发送时间(单位s)',`existTime` int(11) NOT NULL DEFAULT '0' COMMENT '邮件生存时间(s)',`pickUpExistTime` int(11) NOT NULL DEFAULT '0' COMMENT '读取领取后生存时间(单位s)',`uniformIDList` varchar(500) NOT NULL DEFAULT '' COMMENT '物品Id列表',`uniformCountList` varchar(500) NOT NULL DEFAULT '' COMMENT '物品数量列表',PRIMARY KEY (`id`)) COMMENT='记录gm发送的全服邮件信息'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
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


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/GlobalMailBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */