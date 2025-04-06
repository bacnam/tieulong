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
/*     */ public class ItemBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "所属玩家ID")
/*     */   private long pid;
/*     */   @DataBaseField(type = "int(11)", fieldname = "itemId", comment = "道具ID")
/*     */   private int itemId;
/*     */   @DataBaseField(type = "int(11)", fieldname = "count", comment = "道具数量")
/*     */   private int count;
/*     */   @DataBaseField(type = "int(11)", fieldname = "gainTime", comment = "获得时间,单位秒")
/*     */   private int gainTime;
/*     */   
/*     */   public ItemBO() {
/*  26 */     this.id = 0L;
/*  27 */     this.pid = 0L;
/*  28 */     this.itemId = 0;
/*  29 */     this.count = 0;
/*  30 */     this.gainTime = 0;
/*     */   }
/*     */   
/*     */   public ItemBO(ResultSet rs) throws Exception {
/*  34 */     this.id = rs.getLong(1);
/*  35 */     this.pid = rs.getLong(2);
/*  36 */     this.itemId = rs.getInt(3);
/*  37 */     this.count = rs.getInt(4);
/*  38 */     this.gainTime = rs.getInt(5);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<ItemBO> list) throws Exception {
/*  44 */     list.add(new ItemBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  49 */     return getPid();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  54 */     return "`id`, `pid`, `itemId`, `count`, `gainTime`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  59 */     return "`item`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  64 */     StringBuilder strBuf = new StringBuilder();
/*  65 */     strBuf.append("'").append(this.id).append("', ");
/*  66 */     strBuf.append("'").append(this.pid).append("', ");
/*  67 */     strBuf.append("'").append(this.itemId).append("', ");
/*  68 */     strBuf.append("'").append(this.count).append("', ");
/*  69 */     strBuf.append("'").append(this.gainTime).append("', ");
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
/*     */   public int getItemId() {
/* 105 */     return this.itemId;
/*     */   } public void setItemId(int itemId) {
/* 107 */     if (itemId == this.itemId)
/*     */       return; 
/* 109 */     this.itemId = itemId;
/*     */   }
/*     */   public void saveItemId(int itemId) {
/* 112 */     if (itemId == this.itemId)
/*     */       return; 
/* 114 */     this.itemId = itemId;
/* 115 */     saveField("itemId", Integer.valueOf(itemId));
/*     */   }
/*     */   
/*     */   public int getCount() {
/* 119 */     return this.count;
/*     */   } public void setCount(int count) {
/* 121 */     if (count == this.count)
/*     */       return; 
/* 123 */     this.count = count;
/*     */   }
/*     */   public void saveCount(int count) {
/* 126 */     if (count == this.count)
/*     */       return; 
/* 128 */     this.count = count;
/* 129 */     saveField("count", Integer.valueOf(count));
/*     */   }
/*     */   
/*     */   public int getGainTime() {
/* 133 */     return this.gainTime;
/*     */   } public void setGainTime(int gainTime) {
/* 135 */     if (gainTime == this.gainTime)
/*     */       return; 
/* 137 */     this.gainTime = gainTime;
/*     */   }
/*     */   public void saveGainTime(int gainTime) {
/* 140 */     if (gainTime == this.gainTime)
/*     */       return; 
/* 142 */     this.gainTime = gainTime;
/* 143 */     saveField("gainTime", Integer.valueOf(gainTime));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 150 */     StringBuilder sBuilder = new StringBuilder();
/* 151 */     sBuilder.append(" `pid` = '").append(this.pid).append("',");
/* 152 */     sBuilder.append(" `itemId` = '").append(this.itemId).append("',");
/* 153 */     sBuilder.append(" `count` = '").append(this.count).append("',");
/* 154 */     sBuilder.append(" `gainTime` = '").append(this.gainTime).append("',");
/* 155 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 156 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 160 */     String sql = "CREATE TABLE IF NOT EXISTS `item` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '所属玩家ID',`itemId` int(11) NOT NULL DEFAULT '0' COMMENT '道具ID',`count` int(11) NOT NULL DEFAULT '0' COMMENT '道具数量',`gainTime` int(11) NOT NULL DEFAULT '0' COMMENT '获得时间,单位秒',KEY `pid` (`pid`),PRIMARY KEY (`id`)) COMMENT='道具表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
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


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/ItemBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */