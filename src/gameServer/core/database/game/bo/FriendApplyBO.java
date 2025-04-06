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
/*     */ public class FriendApplyBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "所属玩家pid")
/*     */   private long pid;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "friendCId", comment = "好友玩家pid")
/*     */   private long friendCId;
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "content", comment = "附加内容")
/*     */   private String content;
/*     */   @DataBaseField(type = "int(11)", fieldname = "applyTime", comment = "申请时间戳[秒]")
/*     */   private int applyTime;
/*     */   
/*     */   public FriendApplyBO() {
/*  26 */     this.id = 0L;
/*  27 */     this.pid = 0L;
/*  28 */     this.friendCId = 0L;
/*  29 */     this.content = "";
/*  30 */     this.applyTime = 0;
/*     */   }
/*     */   
/*     */   public FriendApplyBO(ResultSet rs) throws Exception {
/*  34 */     this.id = rs.getLong(1);
/*  35 */     this.pid = rs.getLong(2);
/*  36 */     this.friendCId = rs.getLong(3);
/*  37 */     this.content = rs.getString(4);
/*  38 */     this.applyTime = rs.getInt(5);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<FriendApplyBO> list) throws Exception {
/*  44 */     list.add(new FriendApplyBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  49 */     return getPid();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  54 */     return "`id`, `pid`, `friendCId`, `content`, `applyTime`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  59 */     return "`friendApply`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  64 */     StringBuilder strBuf = new StringBuilder();
/*  65 */     strBuf.append("'").append(this.id).append("', ");
/*  66 */     strBuf.append("'").append(this.pid).append("', ");
/*  67 */     strBuf.append("'").append(this.friendCId).append("', ");
/*  68 */     strBuf.append("'").append((this.content == null) ? null : this.content.replace("'", "''")).append("', ");
/*  69 */     strBuf.append("'").append(this.applyTime).append("', ");
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
/*     */   public long getFriendCId() {
/* 105 */     return this.friendCId;
/*     */   } public void setFriendCId(long friendCId) {
/* 107 */     if (friendCId == this.friendCId)
/*     */       return; 
/* 109 */     this.friendCId = friendCId;
/*     */   }
/*     */   public void saveFriendCId(long friendCId) {
/* 112 */     if (friendCId == this.friendCId)
/*     */       return; 
/* 114 */     this.friendCId = friendCId;
/* 115 */     saveField("friendCId", Long.valueOf(friendCId));
/*     */   }
/*     */   
/*     */   public String getContent() {
/* 119 */     return this.content;
/*     */   } public void setContent(String content) {
/* 121 */     if (content.equals(this.content))
/*     */       return; 
/* 123 */     this.content = content;
/*     */   }
/*     */   public void saveContent(String content) {
/* 126 */     if (content.equals(this.content))
/*     */       return; 
/* 128 */     this.content = content;
/* 129 */     saveField("content", content);
/*     */   }
/*     */   
/*     */   public int getApplyTime() {
/* 133 */     return this.applyTime;
/*     */   } public void setApplyTime(int applyTime) {
/* 135 */     if (applyTime == this.applyTime)
/*     */       return; 
/* 137 */     this.applyTime = applyTime;
/*     */   }
/*     */   public void saveApplyTime(int applyTime) {
/* 140 */     if (applyTime == this.applyTime)
/*     */       return; 
/* 142 */     this.applyTime = applyTime;
/* 143 */     saveField("applyTime", Integer.valueOf(applyTime));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 150 */     StringBuilder sBuilder = new StringBuilder();
/* 151 */     sBuilder.append(" `pid` = '").append(this.pid).append("',");
/* 152 */     sBuilder.append(" `friendCId` = '").append(this.friendCId).append("',");
/* 153 */     sBuilder.append(" `content` = '").append((this.content == null) ? null : this.content.replace("'", "''")).append("',");
/* 154 */     sBuilder.append(" `applyTime` = '").append(this.applyTime).append("',");
/* 155 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 156 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 160 */     String sql = "CREATE TABLE IF NOT EXISTS `friendApply` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '所属玩家pid',`friendCId` bigint(20) NOT NULL DEFAULT '0' COMMENT '好友玩家pid',`content` varchar(500) NOT NULL DEFAULT '' COMMENT '附加内容',`applyTime` int(11) NOT NULL DEFAULT '0' COMMENT '申请时间戳[秒]',KEY `pid` (`pid`),PRIMARY KEY (`id`)) COMMENT='好友申请列表[存储加好友申请]'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 168 */       ServerConfig.getInitialID() + 1L);
/* 169 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/FriendApplyBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */