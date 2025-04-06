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
/*     */ public class FriendBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "所属玩家pid")
/*     */   private long pid;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "friendCId", comment = "好友玩家pid")
/*     */   private long friendCId;
/*     */   @DataBaseField(type = "tinyint(1)", fieldname = "isBlack", comment = "是否被加入黑名单")
/*     */   private boolean isBlack;
/*     */   @DataBaseField(type = "tinyint(1)", fieldname = "isDelete", comment = "是否被玩家删除")
/*     */   private boolean isDelete;
/*     */   @DataBaseField(type = "int(11)", fieldname = "lastGiveTime", comment = "最近赠送好友体力时间戳[秒]")
/*     */   private int lastGiveTime;
/*     */   @DataBaseField(type = "int(11)", fieldname = "giveTimes", comment = "赠送好友体力次数")
/*     */   private int giveTimes;
/*     */   @DataBaseField(type = "int(11)", fieldname = "lastRecvTime", comment = "最近领取好友体力时间戳[秒]")
/*     */   private int lastRecvTime;
/*     */   @DataBaseField(type = "int(11)", fieldname = "recvTimes", comment = "领取好友体力次数")
/*     */   private int recvTimes;
/*     */   @DataBaseField(type = "int(11)", fieldname = "createTime", comment = "成为好友时间")
/*     */   private int createTime;
/*     */   @DataBaseField(type = "int(11)", fieldname = "blackTime", comment = "拉黑好友时间")
/*     */   private int blackTime;
/*     */   
/*     */   public FriendBO() {
/*  38 */     this.id = 0L;
/*  39 */     this.pid = 0L;
/*  40 */     this.friendCId = 0L;
/*  41 */     this.isBlack = false;
/*  42 */     this.isDelete = false;
/*  43 */     this.lastGiveTime = 0;
/*  44 */     this.giveTimes = 0;
/*  45 */     this.lastRecvTime = 0;
/*  46 */     this.recvTimes = 0;
/*  47 */     this.createTime = 0;
/*  48 */     this.blackTime = 0;
/*     */   }
/*     */   
/*     */   public FriendBO(ResultSet rs) throws Exception {
/*  52 */     this.id = rs.getLong(1);
/*  53 */     this.pid = rs.getLong(2);
/*  54 */     this.friendCId = rs.getLong(3);
/*  55 */     this.isBlack = rs.getBoolean(4);
/*  56 */     this.isDelete = rs.getBoolean(5);
/*  57 */     this.lastGiveTime = rs.getInt(6);
/*  58 */     this.giveTimes = rs.getInt(7);
/*  59 */     this.lastRecvTime = rs.getInt(8);
/*  60 */     this.recvTimes = rs.getInt(9);
/*  61 */     this.createTime = rs.getInt(10);
/*  62 */     this.blackTime = rs.getInt(11);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<FriendBO> list) throws Exception {
/*  68 */     list.add(new FriendBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  73 */     return getPid();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  78 */     return "`id`, `pid`, `friendCId`, `isBlack`, `isDelete`, `lastGiveTime`, `giveTimes`, `lastRecvTime`, `recvTimes`, `createTime`, `blackTime`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  83 */     return "`friend`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  88 */     StringBuilder strBuf = new StringBuilder();
/*  89 */     strBuf.append("'").append(this.id).append("', ");
/*  90 */     strBuf.append("'").append(this.pid).append("', ");
/*  91 */     strBuf.append("'").append(this.friendCId).append("', ");
/*  92 */     strBuf.append("'").append(this.isBlack ? 1 : 0).append("', ");
/*  93 */     strBuf.append("'").append(this.isDelete ? 1 : 0).append("', ");
/*  94 */     strBuf.append("'").append(this.lastGiveTime).append("', ");
/*  95 */     strBuf.append("'").append(this.giveTimes).append("', ");
/*  96 */     strBuf.append("'").append(this.lastRecvTime).append("', ");
/*  97 */     strBuf.append("'").append(this.recvTimes).append("', ");
/*  98 */     strBuf.append("'").append(this.createTime).append("', ");
/*  99 */     strBuf.append("'").append(this.blackTime).append("', ");
/* 100 */     strBuf.deleteCharAt(strBuf.length() - 2);
/* 101 */     return strBuf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<byte[]> getInsertValueBytes() {
/* 106 */     ArrayList<byte[]> ret = (ArrayList)new ArrayList<>();
/* 107 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setId(long iID) {
/* 112 */     this.id = iID;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getId() {
/* 117 */     return this.id;
/*     */   }
/*     */   
/*     */   public long getPid() {
/* 121 */     return this.pid;
/*     */   } public void setPid(long pid) {
/* 123 */     if (pid == this.pid)
/*     */       return; 
/* 125 */     this.pid = pid;
/*     */   }
/*     */   public void savePid(long pid) {
/* 128 */     if (pid == this.pid)
/*     */       return; 
/* 130 */     this.pid = pid;
/* 131 */     saveField("pid", Long.valueOf(pid));
/*     */   }
/*     */   
/*     */   public long getFriendCId() {
/* 135 */     return this.friendCId;
/*     */   } public void setFriendCId(long friendCId) {
/* 137 */     if (friendCId == this.friendCId)
/*     */       return; 
/* 139 */     this.friendCId = friendCId;
/*     */   }
/*     */   public void saveFriendCId(long friendCId) {
/* 142 */     if (friendCId == this.friendCId)
/*     */       return; 
/* 144 */     this.friendCId = friendCId;
/* 145 */     saveField("friendCId", Long.valueOf(friendCId));
/*     */   }
/*     */   
/*     */   public boolean getIsBlack() {
/* 149 */     return this.isBlack;
/*     */   } public void setIsBlack(boolean isBlack) {
/* 151 */     if (isBlack == this.isBlack)
/*     */       return; 
/* 153 */     this.isBlack = isBlack;
/*     */   }
/*     */   public void saveIsBlack(boolean isBlack) {
/* 156 */     if (isBlack == this.isBlack)
/*     */       return; 
/* 158 */     this.isBlack = isBlack;
/* 159 */     saveField("isBlack", Integer.valueOf(isBlack ? 1 : 0));
/*     */   }
/*     */   
/*     */   public boolean getIsDelete() {
/* 163 */     return this.isDelete;
/*     */   } public void setIsDelete(boolean isDelete) {
/* 165 */     if (isDelete == this.isDelete)
/*     */       return; 
/* 167 */     this.isDelete = isDelete;
/*     */   }
/*     */   public void saveIsDelete(boolean isDelete) {
/* 170 */     if (isDelete == this.isDelete)
/*     */       return; 
/* 172 */     this.isDelete = isDelete;
/* 173 */     saveField("isDelete", Integer.valueOf(isDelete ? 1 : 0));
/*     */   }
/*     */   
/*     */   public int getLastGiveTime() {
/* 177 */     return this.lastGiveTime;
/*     */   } public void setLastGiveTime(int lastGiveTime) {
/* 179 */     if (lastGiveTime == this.lastGiveTime)
/*     */       return; 
/* 181 */     this.lastGiveTime = lastGiveTime;
/*     */   }
/*     */   public void saveLastGiveTime(int lastGiveTime) {
/* 184 */     if (lastGiveTime == this.lastGiveTime)
/*     */       return; 
/* 186 */     this.lastGiveTime = lastGiveTime;
/* 187 */     saveField("lastGiveTime", Integer.valueOf(lastGiveTime));
/*     */   }
/*     */   
/*     */   public int getGiveTimes() {
/* 191 */     return this.giveTimes;
/*     */   } public void setGiveTimes(int giveTimes) {
/* 193 */     if (giveTimes == this.giveTimes)
/*     */       return; 
/* 195 */     this.giveTimes = giveTimes;
/*     */   }
/*     */   public void saveGiveTimes(int giveTimes) {
/* 198 */     if (giveTimes == this.giveTimes)
/*     */       return; 
/* 200 */     this.giveTimes = giveTimes;
/* 201 */     saveField("giveTimes", Integer.valueOf(giveTimes));
/*     */   }
/*     */   
/*     */   public int getLastRecvTime() {
/* 205 */     return this.lastRecvTime;
/*     */   } public void setLastRecvTime(int lastRecvTime) {
/* 207 */     if (lastRecvTime == this.lastRecvTime)
/*     */       return; 
/* 209 */     this.lastRecvTime = lastRecvTime;
/*     */   }
/*     */   public void saveLastRecvTime(int lastRecvTime) {
/* 212 */     if (lastRecvTime == this.lastRecvTime)
/*     */       return; 
/* 214 */     this.lastRecvTime = lastRecvTime;
/* 215 */     saveField("lastRecvTime", Integer.valueOf(lastRecvTime));
/*     */   }
/*     */   
/*     */   public int getRecvTimes() {
/* 219 */     return this.recvTimes;
/*     */   } public void setRecvTimes(int recvTimes) {
/* 221 */     if (recvTimes == this.recvTimes)
/*     */       return; 
/* 223 */     this.recvTimes = recvTimes;
/*     */   }
/*     */   public void saveRecvTimes(int recvTimes) {
/* 226 */     if (recvTimes == this.recvTimes)
/*     */       return; 
/* 228 */     this.recvTimes = recvTimes;
/* 229 */     saveField("recvTimes", Integer.valueOf(recvTimes));
/*     */   }
/*     */   
/*     */   public int getCreateTime() {
/* 233 */     return this.createTime;
/*     */   } public void setCreateTime(int createTime) {
/* 235 */     if (createTime == this.createTime)
/*     */       return; 
/* 237 */     this.createTime = createTime;
/*     */   }
/*     */   public void saveCreateTime(int createTime) {
/* 240 */     if (createTime == this.createTime)
/*     */       return; 
/* 242 */     this.createTime = createTime;
/* 243 */     saveField("createTime", Integer.valueOf(createTime));
/*     */   }
/*     */   
/*     */   public int getBlackTime() {
/* 247 */     return this.blackTime;
/*     */   } public void setBlackTime(int blackTime) {
/* 249 */     if (blackTime == this.blackTime)
/*     */       return; 
/* 251 */     this.blackTime = blackTime;
/*     */   }
/*     */   public void saveBlackTime(int blackTime) {
/* 254 */     if (blackTime == this.blackTime)
/*     */       return; 
/* 256 */     this.blackTime = blackTime;
/* 257 */     saveField("blackTime", Integer.valueOf(blackTime));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 264 */     StringBuilder sBuilder = new StringBuilder();
/* 265 */     sBuilder.append(" `pid` = '").append(this.pid).append("',");
/* 266 */     sBuilder.append(" `friendCId` = '").append(this.friendCId).append("',");
/* 267 */     sBuilder.append(" `isBlack` = '").append(this.isBlack ? 1 : 0).append("',");
/* 268 */     sBuilder.append(" `isDelete` = '").append(this.isDelete ? 1 : 0).append("',");
/* 269 */     sBuilder.append(" `lastGiveTime` = '").append(this.lastGiveTime).append("',");
/* 270 */     sBuilder.append(" `giveTimes` = '").append(this.giveTimes).append("',");
/* 271 */     sBuilder.append(" `lastRecvTime` = '").append(this.lastRecvTime).append("',");
/* 272 */     sBuilder.append(" `recvTimes` = '").append(this.recvTimes).append("',");
/* 273 */     sBuilder.append(" `createTime` = '").append(this.createTime).append("',");
/* 274 */     sBuilder.append(" `blackTime` = '").append(this.blackTime).append("',");
/* 275 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 276 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 280 */     String sql = "CREATE TABLE IF NOT EXISTS `friend` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '所属玩家pid',`friendCId` bigint(20) NOT NULL DEFAULT '0' COMMENT '好友玩家pid',`isBlack` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否被加入黑名单',`isDelete` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否被玩家删除',`lastGiveTime` int(11) NOT NULL DEFAULT '0' COMMENT '最近赠送好友体力时间戳[秒]',`giveTimes` int(11) NOT NULL DEFAULT '0' COMMENT '赠送好友体力次数',`lastRecvTime` int(11) NOT NULL DEFAULT '0' COMMENT '最近领取好友体力时间戳[秒]',`recvTimes` int(11) NOT NULL DEFAULT '0' COMMENT '领取好友体力次数',`createTime` int(11) NOT NULL DEFAULT '0' COMMENT '成为好友时间',`blackTime` int(11) NOT NULL DEFAULT '0' COMMENT '拉黑好友时间',KEY `pid` (`pid`),PRIMARY KEY (`id`)) COMMENT='好友信息表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
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
/* 294 */       ServerConfig.getInitialID() + 1L);
/* 295 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/FriendBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */