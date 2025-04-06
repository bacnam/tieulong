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
/*     */ public class DroiyanRecordBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家id")
/*     */   private long pid;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "target", comment = "被攻击玩家")
/*     */   private long target;
/*     */   @DataBaseField(type = "varchar(50)", fieldname = "target_name", comment = "被攻击玩家名称")
/*     */   private String target_name;
/*     */   @DataBaseField(type = "int(11)", fieldname = "result", comment = "挑战结果")
/*     */   private int result;
/*     */   @DataBaseField(type = "int(11)", fieldname = "time", comment = "发生时间")
/*     */   private int time;
/*     */   @DataBaseField(type = "int(11)", fieldname = "point", comment = "获得积分")
/*     */   private int point;
/*     */   @DataBaseField(type = "int(11)", fieldname = "gold", comment = "获得金币")
/*     */   private int gold;
/*     */   @DataBaseField(type = "int(11)", fieldname = "exp", comment = "获得经验")
/*     */   private int exp;
/*     */   @DataBaseField(type = "int(11)", fieldname = "rob", comment = "抢劫获得物品")
/*     */   private int rob;
/*     */   @DataBaseField(type = "int(11)", fieldname = "treasure", comment = "寻宝获得物品")
/*     */   private int treasure;
/*     */   @DataBaseField(type = "tinyint(1)", fieldname = "revenged", comment = "已经复仇")
/*     */   private boolean revenged;
/*     */   
/*     */   public DroiyanRecordBO() {
/*  40 */     this.id = 0L;
/*  41 */     this.pid = 0L;
/*  42 */     this.target = 0L;
/*  43 */     this.target_name = "";
/*  44 */     this.result = 0;
/*  45 */     this.time = 0;
/*  46 */     this.point = 0;
/*  47 */     this.gold = 0;
/*  48 */     this.exp = 0;
/*  49 */     this.rob = 0;
/*  50 */     this.treasure = 0;
/*  51 */     this.revenged = false;
/*     */   }
/*     */   
/*     */   public DroiyanRecordBO(ResultSet rs) throws Exception {
/*  55 */     this.id = rs.getLong(1);
/*  56 */     this.pid = rs.getLong(2);
/*  57 */     this.target = rs.getLong(3);
/*  58 */     this.target_name = rs.getString(4);
/*  59 */     this.result = rs.getInt(5);
/*  60 */     this.time = rs.getInt(6);
/*  61 */     this.point = rs.getInt(7);
/*  62 */     this.gold = rs.getInt(8);
/*  63 */     this.exp = rs.getInt(9);
/*  64 */     this.rob = rs.getInt(10);
/*  65 */     this.treasure = rs.getInt(11);
/*  66 */     this.revenged = rs.getBoolean(12);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<DroiyanRecordBO> list) throws Exception {
/*  72 */     list.add(new DroiyanRecordBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  77 */     return getId();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  82 */     return "`id`, `pid`, `target`, `target_name`, `result`, `time`, `point`, `gold`, `exp`, `rob`, `treasure`, `revenged`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  87 */     return "`droiyan_record`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  92 */     StringBuilder strBuf = new StringBuilder();
/*  93 */     strBuf.append("'").append(this.id).append("', ");
/*  94 */     strBuf.append("'").append(this.pid).append("', ");
/*  95 */     strBuf.append("'").append(this.target).append("', ");
/*  96 */     strBuf.append("'").append((this.target_name == null) ? null : this.target_name.replace("'", "''")).append("', ");
/*  97 */     strBuf.append("'").append(this.result).append("', ");
/*  98 */     strBuf.append("'").append(this.time).append("', ");
/*  99 */     strBuf.append("'").append(this.point).append("', ");
/* 100 */     strBuf.append("'").append(this.gold).append("', ");
/* 101 */     strBuf.append("'").append(this.exp).append("', ");
/* 102 */     strBuf.append("'").append(this.rob).append("', ");
/* 103 */     strBuf.append("'").append(this.treasure).append("', ");
/* 104 */     strBuf.append("'").append(this.revenged ? 1 : 0).append("', ");
/* 105 */     strBuf.deleteCharAt(strBuf.length() - 2);
/* 106 */     return strBuf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<byte[]> getInsertValueBytes() {
/* 111 */     ArrayList<byte[]> ret = (ArrayList)new ArrayList<>();
/* 112 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setId(long iID) {
/* 117 */     this.id = iID;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getId() {
/* 122 */     return this.id;
/*     */   }
/*     */   
/*     */   public long getPid() {
/* 126 */     return this.pid;
/*     */   } public void setPid(long pid) {
/* 128 */     if (pid == this.pid)
/*     */       return; 
/* 130 */     this.pid = pid;
/*     */   }
/*     */   public void savePid(long pid) {
/* 133 */     if (pid == this.pid)
/*     */       return; 
/* 135 */     this.pid = pid;
/* 136 */     saveField("pid", Long.valueOf(pid));
/*     */   }
/*     */   
/*     */   public long getTarget() {
/* 140 */     return this.target;
/*     */   } public void setTarget(long target) {
/* 142 */     if (target == this.target)
/*     */       return; 
/* 144 */     this.target = target;
/*     */   }
/*     */   public void saveTarget(long target) {
/* 147 */     if (target == this.target)
/*     */       return; 
/* 149 */     this.target = target;
/* 150 */     saveField("target", Long.valueOf(target));
/*     */   }
/*     */   
/*     */   public String getTargetName() {
/* 154 */     return this.target_name;
/*     */   } public void setTargetName(String target_name) {
/* 156 */     if (target_name.equals(this.target_name))
/*     */       return; 
/* 158 */     this.target_name = target_name;
/*     */   }
/*     */   public void saveTargetName(String target_name) {
/* 161 */     if (target_name.equals(this.target_name))
/*     */       return; 
/* 163 */     this.target_name = target_name;
/* 164 */     saveField("target_name", target_name);
/*     */   }
/*     */   
/*     */   public int getResult() {
/* 168 */     return this.result;
/*     */   } public void setResult(int result) {
/* 170 */     if (result == this.result)
/*     */       return; 
/* 172 */     this.result = result;
/*     */   }
/*     */   public void saveResult(int result) {
/* 175 */     if (result == this.result)
/*     */       return; 
/* 177 */     this.result = result;
/* 178 */     saveField("result", Integer.valueOf(result));
/*     */   }
/*     */   
/*     */   public int getTime() {
/* 182 */     return this.time;
/*     */   } public void setTime(int time) {
/* 184 */     if (time == this.time)
/*     */       return; 
/* 186 */     this.time = time;
/*     */   }
/*     */   public void saveTime(int time) {
/* 189 */     if (time == this.time)
/*     */       return; 
/* 191 */     this.time = time;
/* 192 */     saveField("time", Integer.valueOf(time));
/*     */   }
/*     */   
/*     */   public int getPoint() {
/* 196 */     return this.point;
/*     */   } public void setPoint(int point) {
/* 198 */     if (point == this.point)
/*     */       return; 
/* 200 */     this.point = point;
/*     */   }
/*     */   public void savePoint(int point) {
/* 203 */     if (point == this.point)
/*     */       return; 
/* 205 */     this.point = point;
/* 206 */     saveField("point", Integer.valueOf(point));
/*     */   }
/*     */   
/*     */   public int getGold() {
/* 210 */     return this.gold;
/*     */   } public void setGold(int gold) {
/* 212 */     if (gold == this.gold)
/*     */       return; 
/* 214 */     this.gold = gold;
/*     */   }
/*     */   public void saveGold(int gold) {
/* 217 */     if (gold == this.gold)
/*     */       return; 
/* 219 */     this.gold = gold;
/* 220 */     saveField("gold", Integer.valueOf(gold));
/*     */   }
/*     */   
/*     */   public int getExp() {
/* 224 */     return this.exp;
/*     */   } public void setExp(int exp) {
/* 226 */     if (exp == this.exp)
/*     */       return; 
/* 228 */     this.exp = exp;
/*     */   }
/*     */   public void saveExp(int exp) {
/* 231 */     if (exp == this.exp)
/*     */       return; 
/* 233 */     this.exp = exp;
/* 234 */     saveField("exp", Integer.valueOf(exp));
/*     */   }
/*     */   
/*     */   public int getRob() {
/* 238 */     return this.rob;
/*     */   } public void setRob(int rob) {
/* 240 */     if (rob == this.rob)
/*     */       return; 
/* 242 */     this.rob = rob;
/*     */   }
/*     */   public void saveRob(int rob) {
/* 245 */     if (rob == this.rob)
/*     */       return; 
/* 247 */     this.rob = rob;
/* 248 */     saveField("rob", Integer.valueOf(rob));
/*     */   }
/*     */   
/*     */   public int getTreasure() {
/* 252 */     return this.treasure;
/*     */   } public void setTreasure(int treasure) {
/* 254 */     if (treasure == this.treasure)
/*     */       return; 
/* 256 */     this.treasure = treasure;
/*     */   }
/*     */   public void saveTreasure(int treasure) {
/* 259 */     if (treasure == this.treasure)
/*     */       return; 
/* 261 */     this.treasure = treasure;
/* 262 */     saveField("treasure", Integer.valueOf(treasure));
/*     */   }
/*     */   
/*     */   public boolean getRevenged() {
/* 266 */     return this.revenged;
/*     */   } public void setRevenged(boolean revenged) {
/* 268 */     if (revenged == this.revenged)
/*     */       return; 
/* 270 */     this.revenged = revenged;
/*     */   }
/*     */   public void saveRevenged(boolean revenged) {
/* 273 */     if (revenged == this.revenged)
/*     */       return; 
/* 275 */     this.revenged = revenged;
/* 276 */     saveField("revenged", Integer.valueOf(revenged ? 1 : 0));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 283 */     StringBuilder sBuilder = new StringBuilder();
/* 284 */     sBuilder.append(" `pid` = '").append(this.pid).append("',");
/* 285 */     sBuilder.append(" `target` = '").append(this.target).append("',");
/* 286 */     sBuilder.append(" `target_name` = '").append((this.target_name == null) ? null : this.target_name.replace("'", "''")).append("',");
/* 287 */     sBuilder.append(" `result` = '").append(this.result).append("',");
/* 288 */     sBuilder.append(" `time` = '").append(this.time).append("',");
/* 289 */     sBuilder.append(" `point` = '").append(this.point).append("',");
/* 290 */     sBuilder.append(" `gold` = '").append(this.gold).append("',");
/* 291 */     sBuilder.append(" `exp` = '").append(this.exp).append("',");
/* 292 */     sBuilder.append(" `rob` = '").append(this.rob).append("',");
/* 293 */     sBuilder.append(" `treasure` = '").append(this.treasure).append("',");
/* 294 */     sBuilder.append(" `revenged` = '").append(this.revenged ? 1 : 0).append("',");
/* 295 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 296 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 300 */     String sql = "CREATE TABLE IF NOT EXISTS `droiyan_record` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家id',`target` bigint(20) NOT NULL DEFAULT '0' COMMENT '被攻击玩家',`target_name` varchar(50) NOT NULL DEFAULT '' COMMENT '被攻击玩家名称',`result` int(11) NOT NULL DEFAULT '0' COMMENT '挑战结果',`time` int(11) NOT NULL DEFAULT '0' COMMENT '发生时间',`point` int(11) NOT NULL DEFAULT '0' COMMENT '获得积分',`gold` int(11) NOT NULL DEFAULT '0' COMMENT '获得金币',`exp` int(11) NOT NULL DEFAULT '0' COMMENT '获得经验',`rob` int(11) NOT NULL DEFAULT '0' COMMENT '抢劫获得物品',`treasure` int(11) NOT NULL DEFAULT '0' COMMENT '寻宝获得物品',`revenged` tinyint(1) NOT NULL DEFAULT '0' COMMENT '已经复仇',PRIMARY KEY (`id`)) COMMENT='决战记录'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
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
/* 314 */       ServerConfig.getInitialID() + 1L);
/* 315 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/DroiyanRecordBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */