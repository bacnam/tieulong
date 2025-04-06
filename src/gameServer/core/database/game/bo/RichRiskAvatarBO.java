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
/*     */ public class RichRiskAvatarBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "玩家pid")
/*     */   private long pid;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "avatarPid", comment = "路霸玩家pid")
/*     */   private long avatarPid;
/*     */   @DataBaseField(type = "int(11)", fieldname = "x", comment = "所在坐标的x")
/*     */   private int x;
/*     */   @DataBaseField(type = "int(11)", fieldname = "y", comment = "所在坐标的y")
/*     */   private int y;
/*     */   @DataBaseField(type = "tinyint(1)", fieldname = "isAlive", comment = "路霸是否已经被击杀")
/*     */   private boolean isAlive;
/*     */   
/*     */   public RichRiskAvatarBO() {
/*  28 */     this.id = 0L;
/*  29 */     this.pid = 0L;
/*  30 */     this.avatarPid = 0L;
/*  31 */     this.x = 0;
/*  32 */     this.y = 0;
/*  33 */     this.isAlive = false;
/*     */   }
/*     */   
/*     */   public RichRiskAvatarBO(ResultSet rs) throws Exception {
/*  37 */     this.id = rs.getLong(1);
/*  38 */     this.pid = rs.getLong(2);
/*  39 */     this.avatarPid = rs.getLong(3);
/*  40 */     this.x = rs.getInt(4);
/*  41 */     this.y = rs.getInt(5);
/*  42 */     this.isAlive = rs.getBoolean(6);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<RichRiskAvatarBO> list) throws Exception {
/*  48 */     list.add(new RichRiskAvatarBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  53 */     return getPid();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  58 */     return "`id`, `pid`, `avatarPid`, `x`, `y`, `isAlive`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  63 */     return "`richRiskAvatar`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  68 */     StringBuilder strBuf = new StringBuilder();
/*  69 */     strBuf.append("'").append(this.id).append("', ");
/*  70 */     strBuf.append("'").append(this.pid).append("', ");
/*  71 */     strBuf.append("'").append(this.avatarPid).append("', ");
/*  72 */     strBuf.append("'").append(this.x).append("', ");
/*  73 */     strBuf.append("'").append(this.y).append("', ");
/*  74 */     strBuf.append("'").append(this.isAlive ? 1 : 0).append("', ");
/*  75 */     strBuf.deleteCharAt(strBuf.length() - 2);
/*  76 */     return strBuf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<byte[]> getInsertValueBytes() {
/*  81 */     ArrayList<byte[]> ret = (ArrayList)new ArrayList<>();
/*  82 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setId(long iID) {
/*  87 */     this.id = iID;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getId() {
/*  92 */     return this.id;
/*     */   }
/*     */   
/*     */   public long getPid() {
/*  96 */     return this.pid;
/*     */   } public void setPid(long pid) {
/*  98 */     if (pid == this.pid)
/*     */       return; 
/* 100 */     this.pid = pid;
/*     */   }
/*     */   public void savePid(long pid) {
/* 103 */     if (pid == this.pid)
/*     */       return; 
/* 105 */     this.pid = pid;
/* 106 */     saveField("pid", Long.valueOf(pid));
/*     */   }
/*     */   
/*     */   public long getAvatarPid() {
/* 110 */     return this.avatarPid;
/*     */   } public void setAvatarPid(long avatarPid) {
/* 112 */     if (avatarPid == this.avatarPid)
/*     */       return; 
/* 114 */     this.avatarPid = avatarPid;
/*     */   }
/*     */   public void saveAvatarPid(long avatarPid) {
/* 117 */     if (avatarPid == this.avatarPid)
/*     */       return; 
/* 119 */     this.avatarPid = avatarPid;
/* 120 */     saveField("avatarPid", Long.valueOf(avatarPid));
/*     */   }
/*     */   
/*     */   public int getX() {
/* 124 */     return this.x;
/*     */   } public void setX(int x) {
/* 126 */     if (x == this.x)
/*     */       return; 
/* 128 */     this.x = x;
/*     */   }
/*     */   public void saveX(int x) {
/* 131 */     if (x == this.x)
/*     */       return; 
/* 133 */     this.x = x;
/* 134 */     saveField("x", Integer.valueOf(x));
/*     */   }
/*     */   
/*     */   public int getY() {
/* 138 */     return this.y;
/*     */   } public void setY(int y) {
/* 140 */     if (y == this.y)
/*     */       return; 
/* 142 */     this.y = y;
/*     */   }
/*     */   public void saveY(int y) {
/* 145 */     if (y == this.y)
/*     */       return; 
/* 147 */     this.y = y;
/* 148 */     saveField("y", Integer.valueOf(y));
/*     */   }
/*     */   
/*     */   public boolean getIsAlive() {
/* 152 */     return this.isAlive;
/*     */   } public void setIsAlive(boolean isAlive) {
/* 154 */     if (isAlive == this.isAlive)
/*     */       return; 
/* 156 */     this.isAlive = isAlive;
/*     */   }
/*     */   public void saveIsAlive(boolean isAlive) {
/* 159 */     if (isAlive == this.isAlive)
/*     */       return; 
/* 161 */     this.isAlive = isAlive;
/* 162 */     saveField("isAlive", Integer.valueOf(isAlive ? 1 : 0));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 169 */     StringBuilder sBuilder = new StringBuilder();
/* 170 */     sBuilder.append(" `pid` = '").append(this.pid).append("',");
/* 171 */     sBuilder.append(" `avatarPid` = '").append(this.avatarPid).append("',");
/* 172 */     sBuilder.append(" `x` = '").append(this.x).append("',");
/* 173 */     sBuilder.append(" `y` = '").append(this.y).append("',");
/* 174 */     sBuilder.append(" `isAlive` = '").append(this.isAlive ? 1 : 0).append("',");
/* 175 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 176 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 180 */     String sql = "CREATE TABLE IF NOT EXISTS `richRiskAvatar` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '玩家pid',`avatarPid` bigint(20) NOT NULL DEFAULT '0' COMMENT '路霸玩家pid',`x` int(11) NOT NULL DEFAULT '0' COMMENT '所在坐标的x',`y` int(11) NOT NULL DEFAULT '0' COMMENT '所在坐标的y',`isAlive` tinyint(1) NOT NULL DEFAULT '0' COMMENT '路霸是否已经被击杀',KEY `pid` (`pid`),PRIMARY KEY (`id`)) COMMENT='大富翁路霸表'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 189 */       ServerConfig.getInitialID() + 1L);
/* 190 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/RichRiskAvatarBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */