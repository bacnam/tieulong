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
/*     */ public class RankRecordBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "int(11)", fieldname = "type", comment = "排行榜类型")
/*     */   private int type;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "owner", comment = "所属者id")
/*     */   private long owner;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "value", comment = "值")
/*     */   private long value;
/*     */   @DataBaseField(type = "int(11)", fieldname = "updateTime", comment = "更新时间")
/*     */   private int updateTime;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "ext1", comment = "扩展字段1")
/*     */   private long ext1;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "ext2", comment = "扩展字段2")
/*     */   private long ext2;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "ext3", comment = "扩展字段3")
/*     */   private long ext3;
/*     */   @DataBaseField(type = "varchar(200)", fieldname = "ext4", comment = "扩展字段4")
/*     */   private String ext4;
/*     */   @DataBaseField(type = "varchar(200)", fieldname = "ext5", comment = "扩展字段5")
/*     */   private String ext5;
/*     */   
/*     */   public RankRecordBO() {
/*  36 */     this.id = 0L;
/*  37 */     this.type = 0;
/*  38 */     this.owner = 0L;
/*  39 */     this.value = 0L;
/*  40 */     this.updateTime = 0;
/*  41 */     this.ext1 = 0L;
/*  42 */     this.ext2 = 0L;
/*  43 */     this.ext3 = 0L;
/*  44 */     this.ext4 = "";
/*  45 */     this.ext5 = "";
/*     */   }
/*     */   
/*     */   public RankRecordBO(ResultSet rs) throws Exception {
/*  49 */     this.id = rs.getLong(1);
/*  50 */     this.type = rs.getInt(2);
/*  51 */     this.owner = rs.getLong(3);
/*  52 */     this.value = rs.getLong(4);
/*  53 */     this.updateTime = rs.getInt(5);
/*  54 */     this.ext1 = rs.getLong(6);
/*  55 */     this.ext2 = rs.getLong(7);
/*  56 */     this.ext3 = rs.getLong(8);
/*  57 */     this.ext4 = rs.getString(9);
/*  58 */     this.ext5 = rs.getString(10);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<RankRecordBO> list) throws Exception {
/*  64 */     list.add(new RankRecordBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  69 */     return 0L;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  74 */     return "`id`, `type`, `owner`, `value`, `updateTime`, `ext1`, `ext2`, `ext3`, `ext4`, `ext5`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  79 */     return "`rank_record`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  84 */     StringBuilder strBuf = new StringBuilder();
/*  85 */     strBuf.append("'").append(this.id).append("', ");
/*  86 */     strBuf.append("'").append(this.type).append("', ");
/*  87 */     strBuf.append("'").append(this.owner).append("', ");
/*  88 */     strBuf.append("'").append(this.value).append("', ");
/*  89 */     strBuf.append("'").append(this.updateTime).append("', ");
/*  90 */     strBuf.append("'").append(this.ext1).append("', ");
/*  91 */     strBuf.append("'").append(this.ext2).append("', ");
/*  92 */     strBuf.append("'").append(this.ext3).append("', ");
/*  93 */     strBuf.append("'").append((this.ext4 == null) ? null : this.ext4.replace("'", "''")).append("', ");
/*  94 */     strBuf.append("'").append((this.ext5 == null) ? null : this.ext5.replace("'", "''")).append("', ");
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
/*     */   public int getType() {
/* 116 */     return this.type;
/*     */   } public void setType(int type) {
/* 118 */     if (type == this.type)
/*     */       return; 
/* 120 */     this.type = type;
/*     */   }
/*     */   public void saveType(int type) {
/* 123 */     if (type == this.type)
/*     */       return; 
/* 125 */     this.type = type;
/* 126 */     saveField("type", Integer.valueOf(type));
/*     */   }
/*     */   
/*     */   public long getOwner() {
/* 130 */     return this.owner;
/*     */   } public void setOwner(long owner) {
/* 132 */     if (owner == this.owner)
/*     */       return; 
/* 134 */     this.owner = owner;
/*     */   }
/*     */   public void saveOwner(long owner) {
/* 137 */     if (owner == this.owner)
/*     */       return; 
/* 139 */     this.owner = owner;
/* 140 */     saveField("owner", Long.valueOf(owner));
/*     */   }
/*     */   
/*     */   public long getValue() {
/* 144 */     return this.value;
/*     */   } public void setValue(long value) {
/* 146 */     if (value == this.value)
/*     */       return; 
/* 148 */     this.value = value;
/*     */   }
/*     */   public void saveValue(long value) {
/* 151 */     if (value == this.value)
/*     */       return; 
/* 153 */     this.value = value;
/* 154 */     saveField("value", Long.valueOf(value));
/*     */   }
/*     */   
/*     */   public int getUpdateTime() {
/* 158 */     return this.updateTime;
/*     */   } public void setUpdateTime(int updateTime) {
/* 160 */     if (updateTime == this.updateTime)
/*     */       return; 
/* 162 */     this.updateTime = updateTime;
/*     */   }
/*     */   public void saveUpdateTime(int updateTime) {
/* 165 */     if (updateTime == this.updateTime)
/*     */       return; 
/* 167 */     this.updateTime = updateTime;
/* 168 */     saveField("updateTime", Integer.valueOf(updateTime));
/*     */   }
/*     */   
/*     */   public long getExt1() {
/* 172 */     return this.ext1;
/*     */   } public void setExt1(long ext1) {
/* 174 */     if (ext1 == this.ext1)
/*     */       return; 
/* 176 */     this.ext1 = ext1;
/*     */   }
/*     */   public void saveExt1(long ext1) {
/* 179 */     if (ext1 == this.ext1)
/*     */       return; 
/* 181 */     this.ext1 = ext1;
/* 182 */     saveField("ext1", Long.valueOf(ext1));
/*     */   }
/*     */   
/*     */   public long getExt2() {
/* 186 */     return this.ext2;
/*     */   } public void setExt2(long ext2) {
/* 188 */     if (ext2 == this.ext2)
/*     */       return; 
/* 190 */     this.ext2 = ext2;
/*     */   }
/*     */   public void saveExt2(long ext2) {
/* 193 */     if (ext2 == this.ext2)
/*     */       return; 
/* 195 */     this.ext2 = ext2;
/* 196 */     saveField("ext2", Long.valueOf(ext2));
/*     */   }
/*     */   
/*     */   public long getExt3() {
/* 200 */     return this.ext3;
/*     */   } public void setExt3(long ext3) {
/* 202 */     if (ext3 == this.ext3)
/*     */       return; 
/* 204 */     this.ext3 = ext3;
/*     */   }
/*     */   public void saveExt3(long ext3) {
/* 207 */     if (ext3 == this.ext3)
/*     */       return; 
/* 209 */     this.ext3 = ext3;
/* 210 */     saveField("ext3", Long.valueOf(ext3));
/*     */   }
/*     */   
/*     */   public String getExt4() {
/* 214 */     return this.ext4;
/*     */   } public void setExt4(String ext4) {
/* 216 */     if (ext4.equals(this.ext4))
/*     */       return; 
/* 218 */     this.ext4 = ext4;
/*     */   }
/*     */   public void saveExt4(String ext4) {
/* 221 */     if (ext4.equals(this.ext4))
/*     */       return; 
/* 223 */     this.ext4 = ext4;
/* 224 */     saveField("ext4", ext4);
/*     */   }
/*     */   
/*     */   public String getExt5() {
/* 228 */     return this.ext5;
/*     */   } public void setExt5(String ext5) {
/* 230 */     if (ext5.equals(this.ext5))
/*     */       return; 
/* 232 */     this.ext5 = ext5;
/*     */   }
/*     */   public void saveExt5(String ext5) {
/* 235 */     if (ext5.equals(this.ext5))
/*     */       return; 
/* 237 */     this.ext5 = ext5;
/* 238 */     saveField("ext5", ext5);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 245 */     StringBuilder sBuilder = new StringBuilder();
/* 246 */     sBuilder.append(" `type` = '").append(this.type).append("',");
/* 247 */     sBuilder.append(" `owner` = '").append(this.owner).append("',");
/* 248 */     sBuilder.append(" `value` = '").append(this.value).append("',");
/* 249 */     sBuilder.append(" `updateTime` = '").append(this.updateTime).append("',");
/* 250 */     sBuilder.append(" `ext1` = '").append(this.ext1).append("',");
/* 251 */     sBuilder.append(" `ext2` = '").append(this.ext2).append("',");
/* 252 */     sBuilder.append(" `ext3` = '").append(this.ext3).append("',");
/* 253 */     sBuilder.append(" `ext4` = '").append((this.ext4 == null) ? null : this.ext4.replace("'", "''")).append("',");
/* 254 */     sBuilder.append(" `ext5` = '").append((this.ext5 == null) ? null : this.ext5.replace("'", "''")).append("',");
/* 255 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 256 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 260 */     String sql = "CREATE TABLE IF NOT EXISTS `rank_record` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`type` int(11) NOT NULL DEFAULT '0' COMMENT '排行榜类型',`owner` bigint(20) NOT NULL DEFAULT '0' COMMENT '所属者id',`value` bigint(20) NOT NULL DEFAULT '0' COMMENT '值',`updateTime` int(11) NOT NULL DEFAULT '0' COMMENT '更新时间',`ext1` bigint(20) NOT NULL DEFAULT '0' COMMENT '扩展字段1',`ext2` bigint(20) NOT NULL DEFAULT '0' COMMENT '扩展字段2',`ext3` bigint(20) NOT NULL DEFAULT '0' COMMENT '扩展字段3',`ext4` varchar(200) NOT NULL DEFAULT '' COMMENT '扩展字段4',`ext5` varchar(200) NOT NULL DEFAULT '' COMMENT '扩展字段5',PRIMARY KEY (`id`)) COMMENT='排行表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
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


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/RankRecordBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */