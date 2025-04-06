/*     */ package core.network.proto;
/*     */ 
/*     */ import com.zhonglian.server.common.enums.GuildJob;
/*     */ import com.zhonglian.server.common.enums.JoinState;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Guild
/*     */ {
/*     */   public static class GuildApply
/*     */   {
/*     */     public Player.Summary applyer;
/*     */   }
/*     */   
/*     */   public static class JoinInfo
/*     */   {
/*     */     private long sid;
/*     */     private int level;
/*     */     private int memberCnt;
/*     */     private int MaxmemberCnt;
/*     */     private int iconId;
/*     */     private int borderId;
/*     */     private String name;
/*     */     private Player.Summary master;
/*     */     private String manifesto;
/*  28 */     private JoinState joinState = JoinState.AccptAll;
/*     */     
/*     */     private boolean hasRequest;
/*     */     private int rank;
/*     */     private int joinLevel;
/*     */     
/*     */     public int getMaxmemberCnt() {
/*  35 */       return this.MaxmemberCnt;
/*     */     }
/*     */     
/*     */     public void setMaxmemberCnt(int maxmemberCnt) {
/*  39 */       this.MaxmemberCnt = maxmemberCnt;
/*     */     }
/*     */     
/*     */     public long getsId() {
/*  43 */       return this.sid;
/*     */     }
/*     */     
/*     */     public void setsId(long sId) {
/*  47 */       this.sid = sId;
/*     */     }
/*     */     
/*     */     public int getLevel() {
/*  51 */       return this.level;
/*     */     }
/*     */     
/*     */     public void setLevel(int level) {
/*  55 */       this.level = level;
/*     */     }
/*     */     
/*     */     public int getMemberCnt() {
/*  59 */       return this.memberCnt;
/*     */     }
/*     */     
/*     */     public void setMemberCnt(int memberCnt) {
/*  63 */       this.memberCnt = memberCnt;
/*     */     }
/*     */     
/*     */     public int getIconId() {
/*  67 */       return this.iconId;
/*     */     }
/*     */     
/*     */     public void setIconId(int iconId) {
/*  71 */       this.iconId = iconId;
/*     */     }
/*     */     
/*     */     public int getBorderId() {
/*  75 */       return this.borderId;
/*     */     }
/*     */     
/*     */     public void setBorderId(int borderId) {
/*  79 */       this.borderId = borderId;
/*     */     }
/*     */     
/*     */     public String getName() {
/*  83 */       return this.name;
/*     */     }
/*     */     
/*     */     public void setName(String name) {
/*  87 */       this.name = name;
/*     */     }
/*     */     
/*     */     public Player.Summary getMaster() {
/*  91 */       return this.master;
/*     */     }
/*     */     
/*     */     public void setMaster(Player.Summary master) {
/*  95 */       this.master = master;
/*     */     }
/*     */     
/*     */     public String getManifesto() {
/*  99 */       return this.manifesto;
/*     */     }
/*     */     
/*     */     public void setManifesto(String manifesto) {
/* 103 */       this.manifesto = manifesto;
/*     */     }
/*     */     
/*     */     public JoinState getJoinState() {
/* 107 */       return this.joinState;
/*     */     }
/*     */     
/*     */     public void setJoinState(JoinState joinState) {
/* 111 */       this.joinState = joinState;
/*     */     }
/*     */     
/*     */     public boolean isHasRequest() {
/* 115 */       return this.hasRequest;
/*     */     }
/*     */     
/*     */     public void setHasRequest(boolean hasRequest) {
/* 119 */       this.hasRequest = hasRequest;
/*     */     }
/*     */     
/*     */     public int getRank() {
/* 123 */       return this.rank;
/*     */     }
/*     */     
/*     */     public void setRank(int rank) {
/* 127 */       this.rank = rank;
/*     */     }
/*     */     
/*     */     public int getJoinLevel() {
/* 131 */       return this.joinLevel;
/*     */     }
/*     */     
/*     */     public void setJoinLevel(int joinLevel) {
/* 135 */       this.joinLevel = joinLevel;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static class guildInfo
/*     */   {
/*     */     private long sid;
/*     */     
/*     */     private int level;
/*     */     
/*     */     private int exp;
/*     */     private String name;
/*     */     private int iconId;
/*     */     private int borderId;
/*     */     private int createTime;
/*     */     private String notice;
/*     */     private String manifesto;
/*     */     private JoinState joinState;
/*     */     private int joinLevel;
/*     */     private int unlockIcon;
/*     */     private int unlockBorder;
/*     */     private Player.Summary master;
/*     */     private int nowNum;
/*     */     private int maxNum;
/*     */     
/*     */     public Player.Summary getMaster() {
/* 162 */       return this.master;
/*     */     }
/*     */     
/*     */     public void setMaster(Player.Summary master) {
/* 166 */       this.master = master;
/*     */     }
/*     */     
/*     */     public int getNowNum() {
/* 170 */       return this.nowNum;
/*     */     }
/*     */     
/*     */     public void setNowNum(int nowNum) {
/* 174 */       this.nowNum = nowNum;
/*     */     }
/*     */     
/*     */     public int getMaxNum() {
/* 178 */       return this.maxNum;
/*     */     }
/*     */     
/*     */     public void setMaxNum(int maxNum) {
/* 182 */       this.maxNum = maxNum;
/*     */     }
/*     */     
/*     */     public long getsId() {
/* 186 */       return this.sid;
/*     */     }
/*     */     
/*     */     public void setsId(long sId) {
/* 190 */       this.sid = sId;
/*     */     }
/*     */     
/*     */     public int getLevel() {
/* 194 */       return this.level;
/*     */     }
/*     */     
/*     */     public void setLevel(int level) {
/* 198 */       this.level = level;
/*     */     }
/*     */     
/*     */     public int getExp() {
/* 202 */       return this.exp;
/*     */     }
/*     */     
/*     */     public void setExp(int exp) {
/* 206 */       this.exp = exp;
/*     */     }
/*     */     
/*     */     public String getName() {
/* 210 */       return this.name;
/*     */     }
/*     */     
/*     */     public void setName(String name) {
/* 214 */       this.name = name;
/*     */     }
/*     */     
/*     */     public int getIconId() {
/* 218 */       return this.iconId;
/*     */     }
/*     */     
/*     */     public void setIconId(int iconId) {
/* 222 */       this.iconId = iconId;
/*     */     }
/*     */     
/*     */     public int getBorderId() {
/* 226 */       return this.borderId;
/*     */     }
/*     */     
/*     */     public void setBorderId(int borderId) {
/* 230 */       this.borderId = borderId;
/*     */     }
/*     */     
/*     */     public int getCreateTime() {
/* 234 */       return this.createTime;
/*     */     }
/*     */     
/*     */     public void setCreateTime(int createTime) {
/* 238 */       this.createTime = createTime;
/*     */     }
/*     */     
/*     */     public String getNotice() {
/* 242 */       return this.notice;
/*     */     }
/*     */     
/*     */     public void setNotice(String notice) {
/* 246 */       this.notice = notice;
/*     */     }
/*     */     
/*     */     public String getManifesto() {
/* 250 */       return this.manifesto;
/*     */     }
/*     */     
/*     */     public void setManifesto(String manifesto) {
/* 254 */       this.manifesto = manifesto;
/*     */     }
/*     */     
/*     */     public JoinState getJoinState() {
/* 258 */       return this.joinState;
/*     */     }
/*     */     
/*     */     public void setJoinState(JoinState joinState) {
/* 262 */       this.joinState = joinState;
/*     */     }
/*     */     
/*     */     public int getJoinLevel() {
/* 266 */       return this.joinLevel;
/*     */     }
/*     */     
/*     */     public void setJoinLevel(int joinLevel) {
/* 270 */       this.joinLevel = joinLevel;
/*     */     }
/*     */     
/*     */     public int getUnlockIcon() {
/* 274 */       return this.unlockIcon;
/*     */     }
/*     */     
/*     */     public void setUnlockIcon(int unlockIcon) {
/* 278 */       this.unlockIcon = unlockIcon;
/*     */     }
/*     */     
/*     */     public int getUnlockBorder() {
/* 282 */       return this.unlockBorder;
/*     */     }
/*     */     
/*     */     public void setUnlockBorder(int unlockBorder) {
/* 286 */       this.unlockBorder = unlockBorder;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class member
/*     */   {
/*     */     Player.Summary role;
/*     */     GuildJob job;
/*     */     int guildDonate;
/*     */     int sacrificeDonate;
/*     */     int weekDonate;
/*     */     boolean online;
/*     */     int lastOnlineTime;
/*     */     int joinTime;
/*     */     int skillUpLeftTimes;
/*     */     int sacrificeLeftTimes;
/*     */     int sacrificeCrystal;
/*     */     
/*     */     public int getSacrificeLeftTimes() {
/* 305 */       return this.sacrificeLeftTimes;
/*     */     }
/*     */     
/*     */     public void setSacrificeLeftTimes(int sacrificeLeftTimes) {
/* 309 */       this.sacrificeLeftTimes = sacrificeLeftTimes;
/*     */     }
/*     */     
/*     */     public int getSkillUpLeftTimes() {
/* 313 */       return this.skillUpLeftTimes;
/*     */     }
/*     */     
/*     */     public void setSkillUpLeftTimes(int skillUpTimes) {
/* 317 */       this.skillUpLeftTimes = skillUpTimes;
/*     */     }
/*     */     
/*     */     public int getGuildDonate() {
/* 321 */       return this.guildDonate;
/*     */     }
/*     */     
/*     */     public void setGuildDonate(int guildDonate) {
/* 325 */       this.guildDonate = guildDonate;
/*     */     }
/*     */     
/*     */     public Player.Summary getRole() {
/* 329 */       return this.role;
/*     */     }
/*     */     
/*     */     public void setRole(Player.Summary role) {
/* 333 */       this.role = role;
/*     */     }
/*     */     
/*     */     public GuildJob getJob() {
/* 337 */       return this.job;
/*     */     }
/*     */     
/*     */     public void setJob(GuildJob job) {
/* 341 */       this.job = job;
/*     */     }
/*     */     
/*     */     public int getSacrificeDonate() {
/* 345 */       return this.sacrificeDonate;
/*     */     }
/*     */     
/*     */     public void setSacrificeDonate(int sacrificeDonate) {
/* 349 */       this.sacrificeDonate = sacrificeDonate;
/*     */     }
/*     */     
/*     */     public int getWeekDonate() {
/* 353 */       return this.weekDonate;
/*     */     }
/*     */     
/*     */     public void setWeekDonate(int weekDonate) {
/* 357 */       this.weekDonate = weekDonate;
/*     */     }
/*     */     
/*     */     public boolean isOnline() {
/* 361 */       return this.online;
/*     */     }
/*     */     
/*     */     public void setOnline(boolean online) {
/* 365 */       this.online = online;
/*     */     }
/*     */     
/*     */     public int getLastOnlineTime() {
/* 369 */       return this.lastOnlineTime;
/*     */     }
/*     */     
/*     */     public void setLastOnlineTime(int lastOnlineTime) {
/* 373 */       this.lastOnlineTime = lastOnlineTime;
/*     */     }
/*     */     
/*     */     public int getJoinTime() {
/* 377 */       return this.joinTime;
/*     */     }
/*     */     
/*     */     public void setJoinTime(int joinTime) {
/* 381 */       this.joinTime = joinTime;
/*     */     }
/*     */     
/*     */     public int getSacrificeCrystal() {
/* 385 */       return this.sacrificeCrystal;
/*     */     }
/*     */     
/*     */     public void setSacrificeCrystal(int sacrificeCrystal) {
/* 389 */       this.sacrificeCrystal = sacrificeCrystal;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class GuildSkill
/*     */   {
/*     */     int skillid;
/*     */     int level;
/*     */     
/*     */     public int getSkillid() {
/* 399 */       return this.skillid;
/*     */     }
/*     */     
/*     */     public void setSkillid(int skillid) {
/* 403 */       this.skillid = skillid;
/*     */     }
/*     */     
/*     */     public int getLevel() {
/* 407 */       return this.level;
/*     */     }
/*     */     
/*     */     public void setLevel(int level) {
/* 411 */       this.level = level;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/proto/Guild.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */