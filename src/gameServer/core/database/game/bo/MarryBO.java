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
/*     */ public class MarryBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家id")
/*     */   private long pid;
/*     */   @DataBaseField(type = "int(11)", fieldname = "sex", comment = "性别")
/*     */   private int sex;
/*     */   @DataBaseField(type = "int(11)", fieldname = "married", comment = "婚恋状态")
/*     */   private int married;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "lover_pid", comment = "伴侣pid")
/*     */   private long lover_pid;
/*     */   @DataBaseField(type = "int(11)", fieldname = "level", comment = "恩爱等级")
/*     */   private int level;
/*     */   @DataBaseField(type = "int(11)", fieldname = "exp", comment = "恩爱经验")
/*     */   private int exp;
/*     */   @DataBaseField(type = "int(11)", fieldname = "signin", comment = "签到天数")
/*     */   private int signin;
/*     */   @DataBaseField(type = "tinyint(1)", fieldname = "isSign", comment = "当天是否签到")
/*     */   private boolean isSign;
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "signReward", comment = "签到已领奖励")
/*     */   private String signReward;
/*     */   @DataBaseField(type = "varchar(500)", fieldname = "levelReward", comment = "恩爱等级已领奖励")
/*     */   private String levelReward;
/*     */   
/*     */   public MarryBO() {
/*  38 */     this.id = 0L;
/*  39 */     this.pid = 0L;
/*  40 */     this.sex = 0;
/*  41 */     this.married = 0;
/*  42 */     this.lover_pid = 0L;
/*  43 */     this.level = 0;
/*  44 */     this.exp = 0;
/*  45 */     this.signin = 0;
/*  46 */     this.isSign = false;
/*  47 */     this.signReward = "";
/*  48 */     this.levelReward = "";
/*     */   }
/*     */   
/*     */   public MarryBO(ResultSet rs) throws Exception {
/*  52 */     this.id = rs.getLong(1);
/*  53 */     this.pid = rs.getLong(2);
/*  54 */     this.sex = rs.getInt(3);
/*  55 */     this.married = rs.getInt(4);
/*  56 */     this.lover_pid = rs.getLong(5);
/*  57 */     this.level = rs.getInt(6);
/*  58 */     this.exp = rs.getInt(7);
/*  59 */     this.signin = rs.getInt(8);
/*  60 */     this.isSign = rs.getBoolean(9);
/*  61 */     this.signReward = rs.getString(10);
/*  62 */     this.levelReward = rs.getString(11);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<MarryBO> list) throws Exception {
/*  68 */     list.add(new MarryBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  73 */     return getId();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  78 */     return "`id`, `pid`, `sex`, `married`, `lover_pid`, `level`, `exp`, `signin`, `isSign`, `signReward`, `levelReward`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  83 */     return "`marry`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  88 */     StringBuilder strBuf = new StringBuilder();
/*  89 */     strBuf.append("'").append(this.id).append("', ");
/*  90 */     strBuf.append("'").append(this.pid).append("', ");
/*  91 */     strBuf.append("'").append(this.sex).append("', ");
/*  92 */     strBuf.append("'").append(this.married).append("', ");
/*  93 */     strBuf.append("'").append(this.lover_pid).append("', ");
/*  94 */     strBuf.append("'").append(this.level).append("', ");
/*  95 */     strBuf.append("'").append(this.exp).append("', ");
/*  96 */     strBuf.append("'").append(this.signin).append("', ");
/*  97 */     strBuf.append("'").append(this.isSign ? 1 : 0).append("', ");
/*  98 */     strBuf.append("'").append((this.signReward == null) ? null : this.signReward.replace("'", "''")).append("', ");
/*  99 */     strBuf.append("'").append((this.levelReward == null) ? null : this.levelReward.replace("'", "''")).append("', ");
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
/*     */   public int getSex() {
/* 135 */     return this.sex;
/*     */   } public void setSex(int sex) {
/* 137 */     if (sex == this.sex)
/*     */       return; 
/* 139 */     this.sex = sex;
/*     */   }
/*     */   public void saveSex(int sex) {
/* 142 */     if (sex == this.sex)
/*     */       return; 
/* 144 */     this.sex = sex;
/* 145 */     saveField("sex", Integer.valueOf(sex));
/*     */   }
/*     */   
/*     */   public int getMarried() {
/* 149 */     return this.married;
/*     */   } public void setMarried(int married) {
/* 151 */     if (married == this.married)
/*     */       return; 
/* 153 */     this.married = married;
/*     */   }
/*     */   public void saveMarried(int married) {
/* 156 */     if (married == this.married)
/*     */       return; 
/* 158 */     this.married = married;
/* 159 */     saveField("married", Integer.valueOf(married));
/*     */   }
/*     */   
/*     */   public long getLoverPid() {
/* 163 */     return this.lover_pid;
/*     */   } public void setLoverPid(long lover_pid) {
/* 165 */     if (lover_pid == this.lover_pid)
/*     */       return; 
/* 167 */     this.lover_pid = lover_pid;
/*     */   }
/*     */   public void saveLoverPid(long lover_pid) {
/* 170 */     if (lover_pid == this.lover_pid)
/*     */       return; 
/* 172 */     this.lover_pid = lover_pid;
/* 173 */     saveField("lover_pid", Long.valueOf(lover_pid));
/*     */   }
/*     */   
/*     */   public int getLevel() {
/* 177 */     return this.level;
/*     */   } public void setLevel(int level) {
/* 179 */     if (level == this.level)
/*     */       return; 
/* 181 */     this.level = level;
/*     */   }
/*     */   public void saveLevel(int level) {
/* 184 */     if (level == this.level)
/*     */       return; 
/* 186 */     this.level = level;
/* 187 */     saveField("level", Integer.valueOf(level));
/*     */   }
/*     */   
/*     */   public int getExp() {
/* 191 */     return this.exp;
/*     */   } public void setExp(int exp) {
/* 193 */     if (exp == this.exp)
/*     */       return; 
/* 195 */     this.exp = exp;
/*     */   }
/*     */   public void saveExp(int exp) {
/* 198 */     if (exp == this.exp)
/*     */       return; 
/* 200 */     this.exp = exp;
/* 201 */     saveField("exp", Integer.valueOf(exp));
/*     */   }
/*     */   
/*     */   public int getSignin() {
/* 205 */     return this.signin;
/*     */   } public void setSignin(int signin) {
/* 207 */     if (signin == this.signin)
/*     */       return; 
/* 209 */     this.signin = signin;
/*     */   }
/*     */   public void saveSignin(int signin) {
/* 212 */     if (signin == this.signin)
/*     */       return; 
/* 214 */     this.signin = signin;
/* 215 */     saveField("signin", Integer.valueOf(signin));
/*     */   }
/*     */   
/*     */   public boolean getIsSign() {
/* 219 */     return this.isSign;
/*     */   } public void setIsSign(boolean isSign) {
/* 221 */     if (isSign == this.isSign)
/*     */       return; 
/* 223 */     this.isSign = isSign;
/*     */   }
/*     */   public void saveIsSign(boolean isSign) {
/* 226 */     if (isSign == this.isSign)
/*     */       return; 
/* 228 */     this.isSign = isSign;
/* 229 */     saveField("isSign", Integer.valueOf(isSign ? 1 : 0));
/*     */   }
/*     */   
/*     */   public String getSignReward() {
/* 233 */     return this.signReward;
/*     */   } public void setSignReward(String signReward) {
/* 235 */     if (signReward.equals(this.signReward))
/*     */       return; 
/* 237 */     this.signReward = signReward;
/*     */   }
/*     */   public void saveSignReward(String signReward) {
/* 240 */     if (signReward.equals(this.signReward))
/*     */       return; 
/* 242 */     this.signReward = signReward;
/* 243 */     saveField("signReward", signReward);
/*     */   }
/*     */   
/*     */   public String getLevelReward() {
/* 247 */     return this.levelReward;
/*     */   } public void setLevelReward(String levelReward) {
/* 249 */     if (levelReward.equals(this.levelReward))
/*     */       return; 
/* 251 */     this.levelReward = levelReward;
/*     */   }
/*     */   public void saveLevelReward(String levelReward) {
/* 254 */     if (levelReward.equals(this.levelReward))
/*     */       return; 
/* 256 */     this.levelReward = levelReward;
/* 257 */     saveField("levelReward", levelReward);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 264 */     StringBuilder sBuilder = new StringBuilder();
/* 265 */     sBuilder.append(" `pid` = '").append(this.pid).append("',");
/* 266 */     sBuilder.append(" `sex` = '").append(this.sex).append("',");
/* 267 */     sBuilder.append(" `married` = '").append(this.married).append("',");
/* 268 */     sBuilder.append(" `lover_pid` = '").append(this.lover_pid).append("',");
/* 269 */     sBuilder.append(" `level` = '").append(this.level).append("',");
/* 270 */     sBuilder.append(" `exp` = '").append(this.exp).append("',");
/* 271 */     sBuilder.append(" `signin` = '").append(this.signin).append("',");
/* 272 */     sBuilder.append(" `isSign` = '").append(this.isSign ? 1 : 0).append("',");
/* 273 */     sBuilder.append(" `signReward` = '").append((this.signReward == null) ? null : this.signReward.replace("'", "''")).append("',");
/* 274 */     sBuilder.append(" `levelReward` = '").append((this.levelReward == null) ? null : this.levelReward.replace("'", "''")).append("',");
/* 275 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 276 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 280 */     String sql = "CREATE TABLE IF NOT EXISTS `marry` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家id',`sex` int(11) NOT NULL DEFAULT '0' COMMENT '性别',`married` int(11) NOT NULL DEFAULT '0' COMMENT '婚恋状态',`lover_pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '伴侣pid',`level` int(11) NOT NULL DEFAULT '0' COMMENT '恩爱等级',`exp` int(11) NOT NULL DEFAULT '0' COMMENT '恩爱经验',`signin` int(11) NOT NULL DEFAULT '0' COMMENT '签到天数',`isSign` tinyint(1) NOT NULL DEFAULT '0' COMMENT '当天是否签到',`signReward` varchar(500) NOT NULL DEFAULT '' COMMENT '签到已领奖励',`levelReward` varchar(500) NOT NULL DEFAULT '' COMMENT '恩爱等级已领奖励',PRIMARY KEY (`id`)) COMMENT='婚恋系统'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
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


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/MarryBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */