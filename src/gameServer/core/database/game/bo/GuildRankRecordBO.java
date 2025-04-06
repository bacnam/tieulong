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
/*     */ public class GuildRankRecordBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "int(11)", fieldname = "type", comment = "排行榜类型")
/*     */   private int type;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "guildid", comment = "帮派id")
/*     */   private long guildid;
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
/*     */   public GuildRankRecordBO() {
/*  38 */     this.id = 0L;
/*  39 */     this.type = 0;
/*  40 */     this.guildid = 0L;
/*  41 */     this.owner = 0L;
/*  42 */     this.value = 0L;
/*  43 */     this.updateTime = 0;
/*  44 */     this.ext1 = 0L;
/*  45 */     this.ext2 = 0L;
/*  46 */     this.ext3 = 0L;
/*  47 */     this.ext4 = "";
/*  48 */     this.ext5 = "";
/*     */   }
/*     */   
/*     */   public GuildRankRecordBO(ResultSet rs) throws Exception {
/*  52 */     this.id = rs.getLong(1);
/*  53 */     this.type = rs.getInt(2);
/*  54 */     this.guildid = rs.getLong(3);
/*  55 */     this.owner = rs.getLong(4);
/*  56 */     this.value = rs.getLong(5);
/*  57 */     this.updateTime = rs.getInt(6);
/*  58 */     this.ext1 = rs.getLong(7);
/*  59 */     this.ext2 = rs.getLong(8);
/*  60 */     this.ext3 = rs.getLong(9);
/*  61 */     this.ext4 = rs.getString(10);
/*  62 */     this.ext5 = rs.getString(11);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<GuildRankRecordBO> list) throws Exception {
/*  68 */     list.add(new GuildRankRecordBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  73 */     return 0L;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  78 */     return "`id`, `type`, `guildid`, `owner`, `value`, `updateTime`, `ext1`, `ext2`, `ext3`, `ext4`, `ext5`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  83 */     return "`guild_rank_record`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  88 */     StringBuilder strBuf = new StringBuilder();
/*  89 */     strBuf.append("'").append(this.id).append("', ");
/*  90 */     strBuf.append("'").append(this.type).append("', ");
/*  91 */     strBuf.append("'").append(this.guildid).append("', ");
/*  92 */     strBuf.append("'").append(this.owner).append("', ");
/*  93 */     strBuf.append("'").append(this.value).append("', ");
/*  94 */     strBuf.append("'").append(this.updateTime).append("', ");
/*  95 */     strBuf.append("'").append(this.ext1).append("', ");
/*  96 */     strBuf.append("'").append(this.ext2).append("', ");
/*  97 */     strBuf.append("'").append(this.ext3).append("', ");
/*  98 */     strBuf.append("'").append((this.ext4 == null) ? null : this.ext4.replace("'", "''")).append("', ");
/*  99 */     strBuf.append("'").append((this.ext5 == null) ? null : this.ext5.replace("'", "''")).append("', ");
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
/*     */   public int getType() {
/* 121 */     return this.type;
/*     */   } public void setType(int type) {
/* 123 */     if (type == this.type)
/*     */       return; 
/* 125 */     this.type = type;
/*     */   }
/*     */   public void saveType(int type) {
/* 128 */     if (type == this.type)
/*     */       return; 
/* 130 */     this.type = type;
/* 131 */     saveField("type", Integer.valueOf(type));
/*     */   }
/*     */   
/*     */   public long getGuildid() {
/* 135 */     return this.guildid;
/*     */   } public void setGuildid(long guildid) {
/* 137 */     if (guildid == this.guildid)
/*     */       return; 
/* 139 */     this.guildid = guildid;
/*     */   }
/*     */   public void saveGuildid(long guildid) {
/* 142 */     if (guildid == this.guildid)
/*     */       return; 
/* 144 */     this.guildid = guildid;
/* 145 */     saveField("guildid", Long.valueOf(guildid));
/*     */   }
/*     */   
/*     */   public long getOwner() {
/* 149 */     return this.owner;
/*     */   } public void setOwner(long owner) {
/* 151 */     if (owner == this.owner)
/*     */       return; 
/* 153 */     this.owner = owner;
/*     */   }
/*     */   public void saveOwner(long owner) {
/* 156 */     if (owner == this.owner)
/*     */       return; 
/* 158 */     this.owner = owner;
/* 159 */     saveField("owner", Long.valueOf(owner));
/*     */   }
/*     */   
/*     */   public long getValue() {
/* 163 */     return this.value;
/*     */   } public void setValue(long value) {
/* 165 */     if (value == this.value)
/*     */       return; 
/* 167 */     this.value = value;
/*     */   }
/*     */   public void saveValue(long value) {
/* 170 */     if (value == this.value)
/*     */       return; 
/* 172 */     this.value = value;
/* 173 */     saveField("value", Long.valueOf(value));
/*     */   }
/*     */   
/*     */   public int getUpdateTime() {
/* 177 */     return this.updateTime;
/*     */   } public void setUpdateTime(int updateTime) {
/* 179 */     if (updateTime == this.updateTime)
/*     */       return; 
/* 181 */     this.updateTime = updateTime;
/*     */   }
/*     */   public void saveUpdateTime(int updateTime) {
/* 184 */     if (updateTime == this.updateTime)
/*     */       return; 
/* 186 */     this.updateTime = updateTime;
/* 187 */     saveField("updateTime", Integer.valueOf(updateTime));
/*     */   }
/*     */   
/*     */   public long getExt1() {
/* 191 */     return this.ext1;
/*     */   } public void setExt1(long ext1) {
/* 193 */     if (ext1 == this.ext1)
/*     */       return; 
/* 195 */     this.ext1 = ext1;
/*     */   }
/*     */   public void saveExt1(long ext1) {
/* 198 */     if (ext1 == this.ext1)
/*     */       return; 
/* 200 */     this.ext1 = ext1;
/* 201 */     saveField("ext1", Long.valueOf(ext1));
/*     */   }
/*     */   
/*     */   public long getExt2() {
/* 205 */     return this.ext2;
/*     */   } public void setExt2(long ext2) {
/* 207 */     if (ext2 == this.ext2)
/*     */       return; 
/* 209 */     this.ext2 = ext2;
/*     */   }
/*     */   public void saveExt2(long ext2) {
/* 212 */     if (ext2 == this.ext2)
/*     */       return; 
/* 214 */     this.ext2 = ext2;
/* 215 */     saveField("ext2", Long.valueOf(ext2));
/*     */   }
/*     */   
/*     */   public long getExt3() {
/* 219 */     return this.ext3;
/*     */   } public void setExt3(long ext3) {
/* 221 */     if (ext3 == this.ext3)
/*     */       return; 
/* 223 */     this.ext3 = ext3;
/*     */   }
/*     */   public void saveExt3(long ext3) {
/* 226 */     if (ext3 == this.ext3)
/*     */       return; 
/* 228 */     this.ext3 = ext3;
/* 229 */     saveField("ext3", Long.valueOf(ext3));
/*     */   }
/*     */   
/*     */   public String getExt4() {
/* 233 */     return this.ext4;
/*     */   } public void setExt4(String ext4) {
/* 235 */     if (ext4.equals(this.ext4))
/*     */       return; 
/* 237 */     this.ext4 = ext4;
/*     */   }
/*     */   public void saveExt4(String ext4) {
/* 240 */     if (ext4.equals(this.ext4))
/*     */       return; 
/* 242 */     this.ext4 = ext4;
/* 243 */     saveField("ext4", ext4);
/*     */   }
/*     */   
/*     */   public String getExt5() {
/* 247 */     return this.ext5;
/*     */   } public void setExt5(String ext5) {
/* 249 */     if (ext5.equals(this.ext5))
/*     */       return; 
/* 251 */     this.ext5 = ext5;
/*     */   }
/*     */   public void saveExt5(String ext5) {
/* 254 */     if (ext5.equals(this.ext5))
/*     */       return; 
/* 256 */     this.ext5 = ext5;
/* 257 */     saveField("ext5", ext5);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 264 */     StringBuilder sBuilder = new StringBuilder();
/* 265 */     sBuilder.append(" `type` = '").append(this.type).append("',");
/* 266 */     sBuilder.append(" `guildid` = '").append(this.guildid).append("',");
/* 267 */     sBuilder.append(" `owner` = '").append(this.owner).append("',");
/* 268 */     sBuilder.append(" `value` = '").append(this.value).append("',");
/* 269 */     sBuilder.append(" `updateTime` = '").append(this.updateTime).append("',");
/* 270 */     sBuilder.append(" `ext1` = '").append(this.ext1).append("',");
/* 271 */     sBuilder.append(" `ext2` = '").append(this.ext2).append("',");
/* 272 */     sBuilder.append(" `ext3` = '").append(this.ext3).append("',");
/* 273 */     sBuilder.append(" `ext4` = '").append((this.ext4 == null) ? null : this.ext4.replace("'", "''")).append("',");
/* 274 */     sBuilder.append(" `ext5` = '").append((this.ext5 == null) ? null : this.ext5.replace("'", "''")).append("',");
/* 275 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 276 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 280 */     String sql = "CREATE TABLE IF NOT EXISTS `guild_rank_record` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`type` int(11) NOT NULL DEFAULT '0' COMMENT '排行榜类型',`guildid` bigint(20) NOT NULL DEFAULT '0' COMMENT '帮派id',`owner` bigint(20) NOT NULL DEFAULT '0' COMMENT '所属者id',`value` bigint(20) NOT NULL DEFAULT '0' COMMENT '值',`updateTime` int(11) NOT NULL DEFAULT '0' COMMENT '更新时间',`ext1` bigint(20) NOT NULL DEFAULT '0' COMMENT '扩展字段1',`ext2` bigint(20) NOT NULL DEFAULT '0' COMMENT '扩展字段2',`ext3` bigint(20) NOT NULL DEFAULT '0' COMMENT '扩展字段3',`ext4` varchar(200) NOT NULL DEFAULT '' COMMENT '扩展字段4',`ext5` varchar(200) NOT NULL DEFAULT '' COMMENT '扩展字段5',PRIMARY KEY (`id`)) COMMENT='排行表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
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
/* 293 */       ServerConfig.getInitialID() + 1L);
/* 294 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/GuildRankRecordBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */