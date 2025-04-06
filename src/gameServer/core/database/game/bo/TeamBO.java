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
/*     */ public class TeamBO
/*     */   extends BaseBO
/*     */ {
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "id", comment = "数据库表唯一键值")
/*     */   private long id;
/*     */   @DataBaseField(type = "bigint(20)", fieldname = "pid", comment = "所属玩家ID")
/*     */   private long pid;
/*     */   @DataBaseField(type = "int(11)", fieldname = "teamType", comment = "队伍类型")
/*     */   private int teamType;
/*     */   @DataBaseField(type = "int(11)", fieldname = "fightPower", comment = "战斗力")
/*     */   private int fightPower;
/*     */   @DataBaseField(type = "int(11)", size = 4, fieldname = "battleCard", comment = "战斗队伍卡牌信息")
/*     */   private List<Integer> battleCard;
/*     */   @DataBaseField(type = "int(11)", size = 3, fieldname = "reserveCard", comment = "小伙伴卡牌信息")
/*     */   private List<Integer> reserveCard;
/*     */   @DataBaseField(type = "int(11)", size = 9, fieldname = "position", comment = "后援队伍卡牌信息")
/*     */   private List<Integer> position;
/*     */   
/*     */   public TeamBO() {
/*  30 */     this.id = 0L;
/*  31 */     this.pid = 0L;
/*  32 */     this.teamType = 0;
/*  33 */     this.fightPower = 0;
/*  34 */     this.battleCard = new ArrayList<>(4); int i;
/*  35 */     for (i = 0; i < 4; i++) {
/*  36 */       this.battleCard.add(Integer.valueOf(0));
/*     */     }
/*  38 */     this.reserveCard = new ArrayList<>(3);
/*  39 */     for (i = 0; i < 3; i++) {
/*  40 */       this.reserveCard.add(Integer.valueOf(0));
/*     */     }
/*  42 */     this.position = new ArrayList<>(9);
/*  43 */     for (i = 0; i < 9; i++) {
/*  44 */       this.position.add(Integer.valueOf(0));
/*     */     }
/*     */   }
/*     */   
/*     */   public TeamBO(ResultSet rs) throws Exception {
/*  49 */     this.id = rs.getLong(1);
/*  50 */     this.pid = rs.getLong(2);
/*  51 */     this.teamType = rs.getInt(3);
/*  52 */     this.fightPower = rs.getInt(4);
/*  53 */     this.battleCard = new ArrayList<>(4); int i;
/*  54 */     for (i = 0; i < 4; i++) {
/*  55 */       this.battleCard.add(Integer.valueOf(rs.getInt(i + 5)));
/*     */     }
/*  57 */     this.reserveCard = new ArrayList<>(3);
/*  58 */     for (i = 0; i < 3; i++) {
/*  59 */       this.reserveCard.add(Integer.valueOf(rs.getInt(i + 9)));
/*     */     }
/*  61 */     this.position = new ArrayList<>(9);
/*  62 */     for (i = 0; i < 9; i++) {
/*  63 */       this.position.add(Integer.valueOf(rs.getInt(i + 12)));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getFromResultSet(ResultSet rs, List<TeamBO> list) throws Exception {
/*  70 */     list.add(new TeamBO(rs));
/*     */   }
/*     */ 
/*     */   
/*     */   public long getAsynTaskTag() {
/*  75 */     return getPid();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsName() {
/*  80 */     return "`id`, `pid`, `teamType`, `fightPower`, `battleCard_0`, `battleCard_1`, `battleCard_2`, `battleCard_3`, `reserveCard_0`, `reserveCard_1`, `reserveCard_2`, `position_0`, `position_1`, `position_2`, `position_3`, `position_4`, `position_5`, `position_6`, `position_7`, `position_8`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableName() {
/*  85 */     return "`team`";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getItemsValue() {
/*  90 */     StringBuilder strBuf = new StringBuilder();
/*  91 */     strBuf.append("'").append(this.id).append("', ");
/*  92 */     strBuf.append("'").append(this.pid).append("', ");
/*  93 */     strBuf.append("'").append(this.teamType).append("', ");
/*  94 */     strBuf.append("'").append(this.fightPower).append("', "); int i;
/*  95 */     for (i = 0; i < this.battleCard.size(); i++) {
/*  96 */       strBuf.append("'").append(this.battleCard.get(i)).append("', ");
/*     */     }
/*  98 */     for (i = 0; i < this.reserveCard.size(); i++) {
/*  99 */       strBuf.append("'").append(this.reserveCard.get(i)).append("', ");
/*     */     }
/* 101 */     for (i = 0; i < this.position.size(); i++) {
/* 102 */       strBuf.append("'").append(this.position.get(i)).append("', ");
/*     */     }
/* 104 */     strBuf.deleteCharAt(strBuf.length() - 2);
/* 105 */     return strBuf.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public ArrayList<byte[]> getInsertValueBytes() {
/* 110 */     ArrayList<byte[]> ret = (ArrayList)new ArrayList<>();
/* 111 */     return ret;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setId(long iID) {
/* 116 */     this.id = iID;
/*     */   }
/*     */ 
/*     */   
/*     */   public long getId() {
/* 121 */     return this.id;
/*     */   }
/*     */   
/*     */   public long getPid() {
/* 125 */     return this.pid;
/*     */   } public void setPid(long pid) {
/* 127 */     if (pid == this.pid)
/*     */       return; 
/* 129 */     this.pid = pid;
/*     */   }
/*     */   public void savePid(long pid) {
/* 132 */     if (pid == this.pid)
/*     */       return; 
/* 134 */     this.pid = pid;
/* 135 */     saveField("pid", Long.valueOf(pid));
/*     */   }
/*     */   
/*     */   public int getTeamType() {
/* 139 */     return this.teamType;
/*     */   } public void setTeamType(int teamType) {
/* 141 */     if (teamType == this.teamType)
/*     */       return; 
/* 143 */     this.teamType = teamType;
/*     */   }
/*     */   public void saveTeamType(int teamType) {
/* 146 */     if (teamType == this.teamType)
/*     */       return; 
/* 148 */     this.teamType = teamType;
/* 149 */     saveField("teamType", Integer.valueOf(teamType));
/*     */   }
/*     */   
/*     */   public int getFightPower() {
/* 153 */     return this.fightPower;
/*     */   } public void setFightPower(int fightPower) {
/* 155 */     if (fightPower == this.fightPower)
/*     */       return; 
/* 157 */     this.fightPower = fightPower;
/*     */   }
/*     */   public void saveFightPower(int fightPower) {
/* 160 */     if (fightPower == this.fightPower)
/*     */       return; 
/* 162 */     this.fightPower = fightPower;
/* 163 */     saveField("fightPower", Integer.valueOf(fightPower));
/*     */   }
/*     */   
/*     */   public int getBattleCardSize() {
/* 167 */     return this.battleCard.size();
/* 168 */   } public List<Integer> getBattleCardAll() { return new ArrayList<>(this.battleCard); }
/* 169 */   public void setBattleCardAll(int value) { for (int i = 0; i < this.battleCard.size(); ) { this.battleCard.set(i, Integer.valueOf(value)); i++; }
/* 170 */      } public void saveBattleCardAll(int value) { setBattleCardAll(value); saveAll(); } public int getBattleCard(int index) {
/* 171 */     return ((Integer)this.battleCard.get(index)).intValue();
/*     */   } public void setBattleCard(int index, int value) {
/* 173 */     if (value == ((Integer)this.battleCard.get(index)).intValue())
/*     */       return; 
/* 175 */     this.battleCard.set(index, Integer.valueOf(value));
/*     */   }
/*     */   public void saveBattleCard(int index, int value) {
/* 178 */     if (value == ((Integer)this.battleCard.get(index)).intValue())
/*     */       return; 
/* 180 */     this.battleCard.set(index, Integer.valueOf(value));
/* 181 */     saveField("battleCard_" + index, this.battleCard.get(index));
/*     */   }
/*     */   
/*     */   public int getReserveCardSize() {
/* 185 */     return this.reserveCard.size();
/* 186 */   } public List<Integer> getReserveCardAll() { return new ArrayList<>(this.reserveCard); }
/* 187 */   public void setReserveCardAll(int value) { for (int i = 0; i < this.reserveCard.size(); ) { this.reserveCard.set(i, Integer.valueOf(value)); i++; }
/* 188 */      } public void saveReserveCardAll(int value) { setReserveCardAll(value); saveAll(); } public int getReserveCard(int index) {
/* 189 */     return ((Integer)this.reserveCard.get(index)).intValue();
/*     */   } public void setReserveCard(int index, int value) {
/* 191 */     if (value == ((Integer)this.reserveCard.get(index)).intValue())
/*     */       return; 
/* 193 */     this.reserveCard.set(index, Integer.valueOf(value));
/*     */   }
/*     */   public void saveReserveCard(int index, int value) {
/* 196 */     if (value == ((Integer)this.reserveCard.get(index)).intValue())
/*     */       return; 
/* 198 */     this.reserveCard.set(index, Integer.valueOf(value));
/* 199 */     saveField("reserveCard_" + index, this.reserveCard.get(index));
/*     */   }
/*     */   
/*     */   public int getPositionSize() {
/* 203 */     return this.position.size();
/* 204 */   } public List<Integer> getPositionAll() { return new ArrayList<>(this.position); }
/* 205 */   public void setPositionAll(int value) { for (int i = 0; i < this.position.size(); ) { this.position.set(i, Integer.valueOf(value)); i++; }
/* 206 */      } public void savePositionAll(int value) { setPositionAll(value); saveAll(); } public int getPosition(int index) {
/* 207 */     return ((Integer)this.position.get(index)).intValue();
/*     */   } public void setPosition(int index, int value) {
/* 209 */     if (value == ((Integer)this.position.get(index)).intValue())
/*     */       return; 
/* 211 */     this.position.set(index, Integer.valueOf(value));
/*     */   }
/*     */   public void savePosition(int index, int value) {
/* 214 */     if (value == ((Integer)this.position.get(index)).intValue())
/*     */       return; 
/* 216 */     this.position.set(index, Integer.valueOf(value));
/* 217 */     saveField("position_" + index, this.position.get(index));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getUpdateKeyValue() {
/* 224 */     StringBuilder sBuilder = new StringBuilder();
/* 225 */     sBuilder.append(" `pid` = '").append(this.pid).append("',");
/* 226 */     sBuilder.append(" `teamType` = '").append(this.teamType).append("',");
/* 227 */     sBuilder.append(" `fightPower` = '").append(this.fightPower).append("',"); int i;
/* 228 */     for (i = 0; i < this.battleCard.size(); i++) {
/* 229 */       sBuilder.append(" `battleCard_").append(i).append("` = '").append(this.battleCard.get(i)).append("',");
/*     */     }
/* 231 */     for (i = 0; i < this.reserveCard.size(); i++) {
/* 232 */       sBuilder.append(" `reserveCard_").append(i).append("` = '").append(this.reserveCard.get(i)).append("',");
/*     */     }
/* 234 */     for (i = 0; i < this.position.size(); i++) {
/* 235 */       sBuilder.append(" `position_").append(i).append("` = '").append(this.position.get(i)).append("',");
/*     */     }
/* 237 */     sBuilder.deleteCharAt(sBuilder.length() - 1);
/* 238 */     return sBuilder.toString();
/*     */   }
/*     */   
/*     */   public static String getSql_TableCreate() {
/* 242 */     String sql = "CREATE TABLE IF NOT EXISTS `team` (`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',`pid` bigint(20) NOT NULL DEFAULT '0' COMMENT '所属玩家ID',`teamType` int(11) NOT NULL DEFAULT '0' COMMENT '队伍类型',`fightPower` int(11) NOT NULL DEFAULT '0' COMMENT '战斗力',`battleCard_0` int(11) NOT NULL DEFAULT '0' COMMENT '战斗队伍卡牌信息',`battleCard_1` int(11) NOT NULL DEFAULT '0' COMMENT '战斗队伍卡牌信息',`battleCard_2` int(11) NOT NULL DEFAULT '0' COMMENT '战斗队伍卡牌信息',`battleCard_3` int(11) NOT NULL DEFAULT '0' COMMENT '战斗队伍卡牌信息',`reserveCard_0` int(11) NOT NULL DEFAULT '0' COMMENT '小伙伴卡牌信息',`reserveCard_1` int(11) NOT NULL DEFAULT '0' COMMENT '小伙伴卡牌信息',`reserveCard_2` int(11) NOT NULL DEFAULT '0' COMMENT '小伙伴卡牌信息',`position_0` int(11) NOT NULL DEFAULT '0' COMMENT '后援队伍卡牌信息',`position_1` int(11) NOT NULL DEFAULT '0' COMMENT '后援队伍卡牌信息',`position_2` int(11) NOT NULL DEFAULT '0' COMMENT '后援队伍卡牌信息',`position_3` int(11) NOT NULL DEFAULT '0' COMMENT '后援队伍卡牌信息',`position_4` int(11) NOT NULL DEFAULT '0' COMMENT '后援队伍卡牌信息',`position_5` int(11) NOT NULL DEFAULT '0' COMMENT '后援队伍卡牌信息',`position_6` int(11) NOT NULL DEFAULT '0' COMMENT '后援队伍卡牌信息',`position_7` int(11) NOT NULL DEFAULT '0' COMMENT '后援队伍卡牌信息',`position_8` int(11) NOT NULL DEFAULT '0' COMMENT '后援队伍卡牌信息',KEY `pid` (`pid`),PRIMARY KEY (`id`)) COMMENT='玩家队伍信息'  DEFAULT CHARSET=utf8 AUTO_INCREMENT=" + (
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 265 */       ServerConfig.getInitialID() + 1L);
/* 266 */     return sql;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/database/game/bo/TeamBO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */