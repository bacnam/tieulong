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
/*     */ public class EquipBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家id")
/*     */   private long pid;
/*     */   @DataBaseField(type = "int(11)", fieldname = "equip_id", comment = "装备id")
/*     */   private int equip_id;
/*     */   @DataBaseField(type = "int(11)", size = 6, fieldname = "attr", comment = "浮动属性")
/*     */   private List<Integer> attr;
/*     */   @DataBaseField(type = "int(11)", fieldname = "pos", comment = "装备位置")
/*     */   private int pos;
/*     */   @DataBaseField(type = "int(11)", fieldname = "char_id", comment = "装备的角色")
/*     */   private int char_id;
/*     */   @DataBaseField(type = "int(11)", fieldname = "gain_time", comment = "获取时间")
/*     */   private int gain_time;
/*     */   
/*     */   public EquipBO() {
/*  30 */     this.id = 0L;
/*  31 */     this.pid = 0L;
/*  32 */     this.equip_id = 0;
/*  33 */     this.attr = new ArrayList<>(6);
/*  34 */     for (int i = 0; i < 6; i++) {
/*  35 */       this.attr.add(Integer.valueOf(0));
/*     */     }
/*  37 */     this.pos = 0;
/*  38 */     this.char_id = 0;
/*  39 */     this.gain_time = 0;
/*     */   }
/*     */   
/*     */   public EquipBO(ResultSet rs) throws Exception {
/*  43 */     this.id = rs.getLong(1);
/*  44 */     this.pid = rs.getLong(2);
/*  45 */     this.equip_id = rs.getInt(3);
/*  46 */     this.attr = new ArrayList<>(6);
/*  47 */     for (int i = 0; i < 6; i++) {
/*  48 */       this.attr.add(Integer.valueOf(rs.getInt(i + 4)));
/*     */     }
/*  50 */     this.pos = rs.getInt(10);
/*  51 */     this.char_id = rs.getInt(11);
/*  52 */     this.gain_time = rs.getInt(12);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<EquipBO> list) throws Exception {
/*  58 */     list.add(new EquipBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  63 */     return getId();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  68 */     return "`id`, `pid`, `equip_id`, `attr_0`, `attr_1`, `attr_2`, `attr_3`, `attr_4`, `attr_5`, `pos`, `char_id`, `gain_time`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  73 */     return "`equip`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  78 */     StringBuilder strBuf = new StringBuilder();
/*  79 */     strBuf.append("'").append(this.id).append("', ");
/*  80 */     strBuf.append("'").append(this.pid).append("', ");
/*  81 */     strBuf.append("'").append(this.equip_id).append("', ");
/*  82 */     for (int i = 0; i < this.attr.size(); i++) {
/*  83 */       strBuf.append("'").append(this.attr.get(i)).append("', ");
/*     */     }
/*  85 */     strBuf.append("'").append(this.pos).append("', ");
/*  86 */     strBuf.append("'").append(this.char_id).append("', ");
/*  87 */     strBuf.append("'").append(this.gain_time).append("', ");
/*  88 */     strBuf.deleteCharAt(strBuf.length() - 2);
/*  89 */     return strBuf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<byte[]> getInsertValueBytes() {
/*  94 */     ArrayList<byte[]> ret = (ArrayList)new ArrayList<>();
/*  95 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setId(long iID) {
/* 100 */     this.id = iID;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getId() {
/* 105 */     return this.id;
/*     */   }
/*     */   
/*     */   public long getPid() {
/* 109 */     return this.pid;
/*     */   } public void setPid(long pid) {
/* 111 */     if (pid == this.pid)
/*     */       return; 
/* 113 */     this.pid = pid;
/*     */   }
/*     */   public void savePid(long pid) {
/* 116 */     if (pid == this.pid)
/*     */       return; 
/* 118 */     this.pid = pid;
/* 119 */     saveField("pid", Long.valueOf(pid));
/*     */   }
/*     */   
/*     */   public int getEquipId() {
/* 123 */     return this.equip_id;
/*     */   } public void setEquipId(int equip_id) {
/* 125 */     if (equip_id == this.equip_id)
/*     */       return; 
/* 127 */     this.equip_id = equip_id;
/*     */   }
/*     */   public void saveEquipId(int equip_id) {
/* 130 */     if (equip_id == this.equip_id)
/*     */       return; 
/* 132 */     this.equip_id = equip_id;
/* 133 */     saveField("equip_id", Integer.valueOf(equip_id));
/*     */   }
/*     */   
/*     */   public int getAttrSize() {
/* 137 */     return this.attr.size();
/* 138 */   } public List<Integer> getAttrAll() { return new ArrayList<>(this.attr); }
/* 139 */   public void setAttrAll(int value) { for (int i = 0; i < this.attr.size(); ) { this.attr.set(i, Integer.valueOf(value)); i++; }
/* 140 */      } public void saveAttrAll(int value) { setAttrAll(value); saveAll(); } public int getAttr(int index) {
/* 141 */     return ((Integer)this.attr.get(index)).intValue();
/*     */   } public void setAttr(int index, int value) {
/* 143 */     if (value == ((Integer)this.attr.get(index)).intValue())
/*     */       return; 
/* 145 */     this.attr.set(index, Integer.valueOf(value));
/*     */   }
/*     */   public void saveAttr(int index, int value) {
/* 148 */     if (value == ((Integer)this.attr.get(index)).intValue())
/*     */       return; 
/* 150 */     this.attr.set(index, Integer.valueOf(value));
/* 151 */     saveField("attr_" + index, this.attr.get(index));
/*     */   }
/*     */   
/*     */   public int getPos() {
/* 155 */     return this.pos;
/*     */   } public void setPos(int pos) {
/* 157 */     if (pos == this.pos)
/*     */       return; 
/* 159 */     this.pos = pos;
/*     */   }
/*     */   public void savePos(int pos) {
/* 162 */     if (pos == this.pos)
/*     */       return; 
/* 164 */     this.pos = pos;
/* 165 */     saveField("pos", Integer.valueOf(pos));
/*     */   }
/*     */   
/*     */   public int getCharId() {
/* 169 */     return this.char_id;
/*     */   } public void setCharId(int char_id) {
/* 171 */     if (char_id == this.char_id)
/*     */       return; 
/* 173 */     this.char_id = char_id;
/*     */   }
/*     */   public void saveCharId(int char_id) {
/* 176 */     if (char_id == this.char_id)
/*     */       return; 
/* 178 */     this.char_id = char_id;
/* 179 */     saveField("char_id", Integer.valueOf(char_id));
/*     */   }
/*     */   
/*     */   public int getGainTime() {
/* 183 */     return this.gain_time;
/*     */   } public void setGainTime(int gain_time) {
/* 185 */     if (gain_time == this.gain_time)
/*     */       return; 
/* 187 */     this.gain_time = gain_time;
/*     */   }
/*     */   public void saveGainTime(int gain_time) {
/* 190 */     if (gain_time == this.gain_time)
/*     */       return; 
/* 192 */     this.gain_time = gain_time;
/* 193 */     saveField("gain_time", Integer.valueOf(gain_time));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 200 */     StringBuilder sBuilder = new StringBuilder();
/* 201 */     sBuilder.append(" `pid` = '").append(this.pid).append("',");
/* 202 */     sBuilder.append(" `equip_id` = '").append(this.equip_id).append("',");
/* 203 */     for (int i = 0; i < this.attr.size(); i++) {
/* 204 */       sBuilder.append(" `attr_").append(i).append("` = '").append(this.attr.get(i)).append("',");
/*     */     }
/* 206 */     sBuilder.append(" `pos` = '").append(this.pos).append("',");
/* 207 */     sBuilder.append(" `char_id` = '").append(this.char_id).append("',");
/* 208 */     sBuilder.append(" `gain_time` = '").append(this.gain_time).append("',");
/* 209 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 210 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 214 */     String sql = "CREATE TABLE IF NOT EXISTS `equip` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家id',`equip_id` int(11) NOT NULL DEFAULT '0' COMMENT '装备id',`attr_0` int(11) NOT NULL DEFAULT '0' COMMENT '浮动属性',`attr_1` int(11) NOT NULL DEFAULT '0' COMMENT '浮动属性',`attr_2` int(11) NOT NULL DEFAULT '0' COMMENT '浮动属性',`attr_3` int(11) NOT NULL DEFAULT '0' COMMENT '浮动属性',`attr_4` int(11) NOT NULL DEFAULT '0' COMMENT '浮动属性',`attr_5` int(11) NOT NULL DEFAULT '0' COMMENT '浮动属性',`pos` int(11) NOT NULL DEFAULT '0' COMMENT '装备位置',`char_id` int(11) NOT NULL DEFAULT '0' COMMENT '装备的角色',`gain_time` int(11) NOT NULL DEFAULT '0' COMMENT '获取时间',PRIMARY KEY (`id`)) COMMENT='玩家信息表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
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
/* 228 */       ServerConfig.getInitialID() + 1L);
/* 229 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/EquipBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */