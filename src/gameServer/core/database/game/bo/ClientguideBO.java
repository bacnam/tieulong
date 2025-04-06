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
/*     */ public class ClientguideBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家ID")
/*     */   private long pid;
/*     */   @DataBaseField(type = "int(11)", fieldname = "groupId", comment = "组id")
/*     */   private int groupId;
/*     */   @DataBaseField(type = "varchar(2000)", fieldname = "finishList", comment = "完成的引导组id，建议采用组id列表的序号按位存放，使用long值存储。该字段由客户端使用与解析")
/*     */   private String finishList;
/*     */   @DataBaseField(type = "varchar(2000)", fieldname = "triggerList", comment = "完成的引导组id，建议采用组id列表的序号按位存放，使用long值存储。该字段由客户端使用与解析")
/*     */   private String triggerList;
/*     */   
/*     */   public ClientguideBO() {
/*  26 */     this.id = 0L;
/*  27 */     this.pid = 0L;
/*  28 */     this.groupId = 0;
/*  29 */     this.finishList = "";
/*  30 */     this.triggerList = "";
/*     */   }
/*     */   
/*     */   public ClientguideBO(ResultSet rs) throws Exception {
/*  34 */     this.id = rs.getLong(1);
/*  35 */     this.pid = rs.getLong(2);
/*  36 */     this.groupId = rs.getInt(3);
/*  37 */     this.finishList = rs.getString(4);
/*  38 */     this.triggerList = rs.getString(5);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<ClientguideBO> list) throws Exception {
/*  44 */     list.add(new ClientguideBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  49 */     return getPid();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  54 */     return "`id`, `pid`, `groupId`, `finishList`, `triggerList`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  59 */     return "`clientguide`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  64 */     StringBuilder strBuf = new StringBuilder();
/*  65 */     strBuf.append("'").append(this.id).append("', ");
/*  66 */     strBuf.append("'").append(this.pid).append("', ");
/*  67 */     strBuf.append("'").append(this.groupId).append("', ");
/*  68 */     strBuf.append("'").append((this.finishList == null) ? null : this.finishList.replace("'", "''")).append("', ");
/*  69 */     strBuf.append("'").append((this.triggerList == null) ? null : this.triggerList.replace("'", "''")).append("', ");
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
/*     */   public int getGroupId() {
/* 105 */     return this.groupId;
/*     */   } public void setGroupId(int groupId) {
/* 107 */     if (groupId == this.groupId)
/*     */       return; 
/* 109 */     this.groupId = groupId;
/*     */   }
/*     */   public void saveGroupId(int groupId) {
/* 112 */     if (groupId == this.groupId)
/*     */       return; 
/* 114 */     this.groupId = groupId;
/* 115 */     saveField("groupId", Integer.valueOf(groupId));
/*     */   }
/*     */   
/*     */   public String getFinishList() {
/* 119 */     return this.finishList;
/*     */   } public void setFinishList(String finishList) {
/* 121 */     if (finishList.equals(this.finishList))
/*     */       return; 
/* 123 */     this.finishList = finishList;
/*     */   }
/*     */   public void saveFinishList(String finishList) {
/* 126 */     if (finishList.equals(this.finishList))
/*     */       return; 
/* 128 */     this.finishList = finishList;
/* 129 */     saveField("finishList", finishList);
/*     */   }
/*     */   
/*     */   public String getTriggerList() {
/* 133 */     return this.triggerList;
/*     */   } public void setTriggerList(String triggerList) {
/* 135 */     if (triggerList.equals(this.triggerList))
/*     */       return; 
/* 137 */     this.triggerList = triggerList;
/*     */   }
/*     */   public void saveTriggerList(String triggerList) {
/* 140 */     if (triggerList.equals(this.triggerList))
/*     */       return; 
/* 142 */     this.triggerList = triggerList;
/* 143 */     saveField("triggerList", triggerList);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 150 */     StringBuilder sBuilder = new StringBuilder();
/* 151 */     sBuilder.append(" `pid` = '").append(this.pid).append("',");
/* 152 */     sBuilder.append(" `groupId` = '").append(this.groupId).append("',");
/* 153 */     sBuilder.append(" `finishList` = '").append((this.finishList == null) ? null : this.finishList.replace("'", "''")).append("',");
/* 154 */     sBuilder.append(" `triggerList` = '").append((this.triggerList == null) ? null : this.triggerList.replace("'", "''")).append("',");
/* 155 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 156 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 160 */     String sql = "CREATE TABLE IF NOT EXISTS `clientguide` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家ID',`groupId` int(11) NOT NULL DEFAULT '0' COMMENT '组id',`finishList` varchar(2000) NOT NULL DEFAULT '' COMMENT '完成的引导组id，建议采用组id列表的序号按位存放，使用long值存储。该字段由客户端使用与解析',`triggerList` varchar(2000) NOT NULL DEFAULT '' COMMENT '完成的引导组id，建议采用组id列表的序号按位存放，使用long值存储。该字段由客户端使用与解析',UNIQUE INDEX `pid` (`pid`),PRIMARY KEY (`id`)) COMMENT='新手引导数据存储表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
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


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/ClientguideBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */