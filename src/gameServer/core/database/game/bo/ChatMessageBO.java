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
/*     */ public class ChatMessageBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "int(11)", fieldname = "chatType", comment = "聊天频道[1、世界频道 2、公会频道 3、私聊频道 4、系统频道]")
/*     */   private int chatType;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "receiveCId", comment = "其余表示玩家pid[现在的玩家pid都是大数字]")
/*     */   private long receiveCId;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "senderCId", comment = "发送者CID")
/*     */   private long senderCId;
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "content", comment = "聊天内容")
/*     */   private String content;
/*     */   @DataBaseField(type = "int(11)", fieldname = "sendTime", comment = "发送时间")
/*     */   private int sendTime;
/*     */   
/*     */   public ChatMessageBO() {
/*  28 */     this.id = 0L;
/*  29 */     this.chatType = 0;
/*  30 */     this.receiveCId = 0L;
/*  31 */     this.senderCId = 0L;
/*  32 */     this.content = "";
/*  33 */     this.sendTime = 0;
/*     */   }
/*     */   
/*     */   public ChatMessageBO(ResultSet rs) throws Exception {
/*  37 */     this.id = rs.getLong(1);
/*  38 */     this.chatType = rs.getInt(2);
/*  39 */     this.receiveCId = rs.getLong(3);
/*  40 */     this.senderCId = rs.getLong(4);
/*  41 */     this.content = rs.getString(5);
/*  42 */     this.sendTime = rs.getInt(6);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<ChatMessageBO> list) throws Exception {
/*  48 */     list.add(new ChatMessageBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  53 */     return 0L;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  58 */     return "`id`, `chatType`, `receiveCId`, `senderCId`, `content`, `sendTime`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  63 */     return "`chatMessage`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  68 */     StringBuilder strBuf = new StringBuilder();
/*  69 */     strBuf.append("'").append(this.id).append("', ");
/*  70 */     strBuf.append("'").append(this.chatType).append("', ");
/*  71 */     strBuf.append("'").append(this.receiveCId).append("', ");
/*  72 */     strBuf.append("'").append(this.senderCId).append("', ");
/*  73 */     strBuf.append("'").append((this.content == null) ? null : this.content.replace("'", "''")).append("', ");
/*  74 */     strBuf.append("'").append(this.sendTime).append("', ");
/*  75 */     strBuf.deleteCharAt(strBuf.length() - 2);
/*  76 */     return strBuf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<byte[]> getInsertValueBytes() {
/*  81 */     ArrayList<byte[]> ret = (ArrayList)new ArrayList<>();
/*  82 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setId(long iID) {
/*  87 */     this.id = iID;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getId() {
/*  92 */     return this.id;
/*     */   }
/*     */   
/*     */   public int getChatType() {
/*  96 */     return this.chatType;
/*     */   } public void setChatType(int chatType) {
/*  98 */     if (chatType == this.chatType)
/*     */       return; 
/* 100 */     this.chatType = chatType;
/*     */   }
/*     */   public void saveChatType(int chatType) {
/* 103 */     if (chatType == this.chatType)
/*     */       return; 
/* 105 */     this.chatType = chatType;
/* 106 */     saveField("chatType", Integer.valueOf(chatType));
/*     */   }
/*     */   
/*     */   public long getReceiveCId() {
/* 110 */     return this.receiveCId;
/*     */   } public void setReceiveCId(long receiveCId) {
/* 112 */     if (receiveCId == this.receiveCId)
/*     */       return; 
/* 114 */     this.receiveCId = receiveCId;
/*     */   }
/*     */   public void saveReceiveCId(long receiveCId) {
/* 117 */     if (receiveCId == this.receiveCId)
/*     */       return; 
/* 119 */     this.receiveCId = receiveCId;
/* 120 */     saveField("receiveCId", Long.valueOf(receiveCId));
/*     */   }
/*     */   
/*     */   public long getSenderCId() {
/* 124 */     return this.senderCId;
/*     */   } public void setSenderCId(long senderCId) {
/* 126 */     if (senderCId == this.senderCId)
/*     */       return; 
/* 128 */     this.senderCId = senderCId;
/*     */   }
/*     */   public void saveSenderCId(long senderCId) {
/* 131 */     if (senderCId == this.senderCId)
/*     */       return; 
/* 133 */     this.senderCId = senderCId;
/* 134 */     saveField("senderCId", Long.valueOf(senderCId));
/*     */   }
/*     */   
/*     */   public String getContent() {
/* 138 */     return this.content;
/*     */   } public void setContent(String content) {
/* 140 */     if (content.equals(this.content))
/*     */       return; 
/* 142 */     this.content = content;
/*     */   }
/*     */   public void saveContent(String content) {
/* 145 */     if (content.equals(this.content))
/*     */       return; 
/* 147 */     this.content = content;
/* 148 */     saveField("content", content);
/*     */   }
/*     */   
/*     */   public int getSendTime() {
/* 152 */     return this.sendTime;
/*     */   } public void setSendTime(int sendTime) {
/* 154 */     if (sendTime == this.sendTime)
/*     */       return; 
/* 156 */     this.sendTime = sendTime;
/*     */   }
/*     */   public void saveSendTime(int sendTime) {
/* 159 */     if (sendTime == this.sendTime)
/*     */       return; 
/* 161 */     this.sendTime = sendTime;
/* 162 */     saveField("sendTime", Integer.valueOf(sendTime));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 169 */     StringBuilder sBuilder = new StringBuilder();
/* 170 */     sBuilder.append(" `chatType` = '").append(this.chatType).append("',");
/* 171 */     sBuilder.append(" `receiveCId` = '").append(this.receiveCId).append("',");
/* 172 */     sBuilder.append(" `senderCId` = '").append(this.senderCId).append("',");
/* 173 */     sBuilder.append(" `content` = '").append((this.content == null) ? null : this.content.replace("'", "''")).append("',");
/* 174 */     sBuilder.append(" `sendTime` = '").append(this.sendTime).append("',");
/* 175 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 176 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 180 */     String sql = "CREATE TABLE IF NOT EXISTS `chatMessage` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`chatType` int(11) NOT NULL DEFAULT '0' COMMENT '聊天频道[1、世界频道 2、公会频道 3、私聊频道 4、系统频道]',`receiveCId` bigint(20) NOT NULL DEFAULT '0' COMMENT '其余表示玩家pid[现在的玩家pid都是大数字]',`senderCId` bigint(20) NOT NULL DEFAULT '0' COMMENT '发送者CID',`content` varchar(500) NOT NULL DEFAULT '' COMMENT '聊天内容',`sendTime` int(11) NOT NULL DEFAULT '0' COMMENT '发送时间',PRIMARY KEY (`id`)) COMMENT='聊天信息表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 188 */       ServerConfig.getInitialID() + 1L);
/* 189 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/ChatMessageBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */