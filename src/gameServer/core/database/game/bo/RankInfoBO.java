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
/*     */ public class RankInfoBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "int(11)", fieldname = "aid", comment = "记录数据ID[对应于RankSystemType枚举值]")
/*     */   private int aid;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "*所属玩家ID")
/*     */   private long pid;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "value", comment = "数值")
/*     */   private long value;
/*     */   @DataBaseField(type = "int(11)", fieldname = "updateTime", comment = "更新时间")
/*     */   private int updateTime;
/*     */   @DataBaseField(type = "int(11)", fieldname = "ext1", comment = "扩展字段1")
/*     */   private int ext1;
/*     */   @DataBaseField(type = "int(11)", fieldname = "ext2", comment = "扩展字段2")
/*     */   private int ext2;
/*     */   @DataBaseField(type = "varchar(200)", fieldname = "ext3", comment = "扩展字段3")
/*     */   private String ext3;
/*     */   @DataBaseField(type = "varchar(200)", fieldname = "ext4", comment = "扩展字段4")
/*     */   private String ext4;
/*     */   @DataBaseField(type = "int(11)", fieldname = "ext5", comment = "扩展字段5")
/*     */   private int ext5;
/*     */   
/*     */   public RankInfoBO() {
/*  36 */     this.id = 0L;
/*  37 */     this.aid = 0;
/*  38 */     this.pid = 0L;
/*  39 */     this.value = 0L;
/*  40 */     this.updateTime = 0;
/*  41 */     this.ext1 = 0;
/*  42 */     this.ext2 = 0;
/*  43 */     this.ext3 = "";
/*  44 */     this.ext4 = "";
/*  45 */     this.ext5 = 0;
/*     */   }
/*     */   
/*     */   public RankInfoBO(ResultSet rs) throws Exception {
/*  49 */     this.id = rs.getLong(1);
/*  50 */     this.aid = rs.getInt(2);
/*  51 */     this.pid = rs.getLong(3);
/*  52 */     this.value = rs.getLong(4);
/*  53 */     this.updateTime = rs.getInt(5);
/*  54 */     this.ext1 = rs.getInt(6);
/*  55 */     this.ext2 = rs.getInt(7);
/*  56 */     this.ext3 = rs.getString(8);
/*  57 */     this.ext4 = rs.getString(9);
/*  58 */     this.ext5 = rs.getInt(10);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<RankInfoBO> list) throws Exception {
/*  64 */     list.add(new RankInfoBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  69 */     return 0L;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  74 */     return "`id`, `aid`, `pid`, `value`, `updateTime`, `ext1`, `ext2`, `ext3`, `ext4`, `ext5`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  79 */     return "`rankInfo`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  84 */     StringBuilder strBuf = new StringBuilder();
/*  85 */     strBuf.append("'").append(this.id).append("', ");
/*  86 */     strBuf.append("'").append(this.aid).append("', ");
/*  87 */     strBuf.append("'").append(this.pid).append("', ");
/*  88 */     strBuf.append("'").append(this.value).append("', ");
/*  89 */     strBuf.append("'").append(this.updateTime).append("', ");
/*  90 */     strBuf.append("'").append(this.ext1).append("', ");
/*  91 */     strBuf.append("'").append(this.ext2).append("', ");
/*  92 */     strBuf.append("'").append((this.ext3 == null) ? null : this.ext3.replace("'", "''")).append("', ");
/*  93 */     strBuf.append("'").append((this.ext4 == null) ? null : this.ext4.replace("'", "''")).append("', ");
/*  94 */     strBuf.append("'").append(this.ext5).append("', ");
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
/*     */   public int getAid() {
/* 116 */     return this.aid;
/*     */   } public void setAid(int aid) {
/* 118 */     if (aid == this.aid)
/*     */       return; 
/* 120 */     this.aid = aid;
/*     */   }
/*     */   public void saveAid(int aid) {
/* 123 */     if (aid == this.aid)
/*     */       return; 
/* 125 */     this.aid = aid;
/* 126 */     saveField("aid", Integer.valueOf(aid));
/*     */   }
/*     */   
/*     */   public long getPid() {
/* 130 */     return this.pid;
/*     */   } public void setPid(long pid) {
/* 132 */     if (pid == this.pid)
/*     */       return; 
/* 134 */     this.pid = pid;
/*     */   }
/*     */   public void savePid(long pid) {
/* 137 */     if (pid == this.pid)
/*     */       return; 
/* 139 */     this.pid = pid;
/* 140 */     saveField("pid", Long.valueOf(pid));
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
/*     */   public int getExt1() {
/* 172 */     return this.ext1;
/*     */   } public void setExt1(int ext1) {
/* 174 */     if (ext1 == this.ext1)
/*     */       return; 
/* 176 */     this.ext1 = ext1;
/*     */   }
/*     */   public void saveExt1(int ext1) {
/* 179 */     if (ext1 == this.ext1)
/*     */       return; 
/* 181 */     this.ext1 = ext1;
/* 182 */     saveField("ext1", Integer.valueOf(ext1));
/*     */   }
/*     */   
/*     */   public int getExt2() {
/* 186 */     return this.ext2;
/*     */   } public void setExt2(int ext2) {
/* 188 */     if (ext2 == this.ext2)
/*     */       return; 
/* 190 */     this.ext2 = ext2;
/*     */   }
/*     */   public void saveExt2(int ext2) {
/* 193 */     if (ext2 == this.ext2)
/*     */       return; 
/* 195 */     this.ext2 = ext2;
/* 196 */     saveField("ext2", Integer.valueOf(ext2));
/*     */   }
/*     */   
/*     */   public String getExt3() {
/* 200 */     return this.ext3;
/*     */   } public void setExt3(String ext3) {
/* 202 */     if (ext3.equals(this.ext3))
/*     */       return; 
/* 204 */     this.ext3 = ext3;
/*     */   }
/*     */   public void saveExt3(String ext3) {
/* 207 */     if (ext3.equals(this.ext3))
/*     */       return; 
/* 209 */     this.ext3 = ext3;
/* 210 */     saveField("ext3", ext3);
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
/*     */   public int getExt5() {
/* 228 */     return this.ext5;
/*     */   } public void setExt5(int ext5) {
/* 230 */     if (ext5 == this.ext5)
/*     */       return; 
/* 232 */     this.ext5 = ext5;
/*     */   }
/*     */   public void saveExt5(int ext5) {
/* 235 */     if (ext5 == this.ext5)
/*     */       return; 
/* 237 */     this.ext5 = ext5;
/* 238 */     saveField("ext5", Integer.valueOf(ext5));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 245 */     StringBuilder sBuilder = new StringBuilder();
/* 246 */     sBuilder.append(" `aid` = '").append(this.aid).append("',");
/* 247 */     sBuilder.append(" `pid` = '").append(this.pid).append("',");
/* 248 */     sBuilder.append(" `value` = '").append(this.value).append("',");
/* 249 */     sBuilder.append(" `updateTime` = '").append(this.updateTime).append("',");
/* 250 */     sBuilder.append(" `ext1` = '").append(this.ext1).append("',");
/* 251 */     sBuilder.append(" `ext2` = '").append(this.ext2).append("',");
/* 252 */     sBuilder.append(" `ext3` = '").append((this.ext3 == null) ? null : this.ext3.replace("'", "''")).append("',");
/* 253 */     sBuilder.append(" `ext4` = '").append((this.ext4 == null) ? null : this.ext4.replace("'", "''")).append("',");
/* 254 */     sBuilder.append(" `ext5` = '").append(this.ext5).append("',");
/* 255 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 256 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 260 */     String sql = "CREATE TABLE IF NOT EXISTS `rankInfo` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`aid` int(11) NOT NULL DEFAULT '0' COMMENT '记录数据ID[对应于RankSystemType枚举值]',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '*所属玩家ID',`value` bigint(20) NOT NULL DEFAULT '0' COMMENT '数值',`updateTime` int(11) NOT NULL DEFAULT '0' COMMENT '更新时间',`ext1` int(11) NOT NULL DEFAULT '0' COMMENT '扩展字段1',`ext2` int(11) NOT NULL DEFAULT '0' COMMENT '扩展字段2',`ext3` varchar(200) NOT NULL DEFAULT '' COMMENT '扩展字段3',`ext4` varchar(200) NOT NULL DEFAULT '' COMMENT '扩展字段4',`ext5` int(11) NOT NULL DEFAULT '0' COMMENT '扩展字段5',PRIMARY KEY (`id`)) COMMENT='排行表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
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


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/RankInfoBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */