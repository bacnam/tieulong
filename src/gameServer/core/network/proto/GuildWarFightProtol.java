/*     */ package core.network.proto;
/*     */ 
/*     */ import business.global.guild.Guild;
/*     */ import business.global.guild.GuildWarConfig;
/*     */ import business.global.guild.GuildWarMgr;
/*     */ import business.player.Player;
/*     */ import business.player.PlayerMgr;
/*     */ import business.player.feature.PlayerBase;
/*     */ import com.zhonglian.server.common.utils.CommTime;
/*     */ import core.database.game.bo.GuildwarResultBO;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ public class GuildWarFightProtol
/*     */ {
/*     */   int centerId;
/*     */   Guild.GuildSummary atk;
/*     */   Guild.GuildSummary def;
/*     */   List<GuildInfo> guildInfo;
/*     */   List<ResultRecord> resultInfo;
/*     */   int endTime;
/*     */   int killnum;
/*     */   boolean isDead = false;
/*     */   List<RoadSummry> road;
/*     */   int rebirthCD;
/*     */   int rebirthTime;
/*     */   
/*     */   public static class GuildInfo
/*     */   {
/*     */     String name;
/*     */     int left;
/*     */     int dead;
/*     */     int puppet;
/*     */     
/*     */     public GuildInfo(String name, int left, int dead, int puppet) {
/*  36 */       this.name = name;
/*  37 */       this.left = left;
/*  38 */       this.dead = dead;
/*  39 */       this.puppet = puppet;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRebirthTime() {
/*  45 */     return this.rebirthTime;
/*     */   }
/*     */   
/*     */   public void setRebirthTime(int rebirthTime) {
/*  49 */     this.rebirthTime = rebirthTime;
/*     */   }
/*     */   
/*     */   public int getCenterId() {
/*  53 */     return this.centerId;
/*     */   }
/*     */   
/*     */   public void setCenterId(int centerId) {
/*  57 */     this.centerId = centerId;
/*     */   }
/*     */   
/*     */   public Guild.GuildSummary getAtk() {
/*  61 */     return this.atk;
/*     */   }
/*     */   
/*     */   public void setAtk(Guild.GuildSummary atk) {
/*  65 */     this.atk = atk;
/*     */   }
/*     */   
/*     */   public Guild.GuildSummary getDef() {
/*  69 */     return this.def;
/*     */   }
/*     */   
/*     */   public void setDef(Guild.GuildSummary def) {
/*  73 */     this.def = def;
/*     */   }
/*     */   
/*     */   public List<GuildInfo> getGuildInfo() {
/*  77 */     return this.guildInfo;
/*     */   }
/*     */   
/*     */   public void setGuildInfo(List<GuildInfo> guildInfo) {
/*  81 */     this.guildInfo = guildInfo;
/*     */   }
/*     */   
/*     */   public List<ResultRecord> getResultInfo() {
/*  85 */     return this.resultInfo;
/*     */   }
/*     */   
/*     */   public void setResultInfo(List<ResultRecord> resultInfo) {
/*  89 */     this.resultInfo = resultInfo;
/*     */   }
/*     */   
/*     */   public int getKillnum() {
/*  93 */     return this.killnum;
/*     */   }
/*     */   
/*     */   public void setKillnum(int killnum) {
/*  97 */     this.killnum = killnum;
/*     */   }
/*     */   
/*     */   public List<RoadSummry> getRoad() {
/* 101 */     return this.road;
/*     */   }
/*     */   
/*     */   public void setRoad(List<RoadSummry> road) {
/* 105 */     this.road = road;
/*     */   }
/*     */   
/*     */   public int getEndTime() {
/* 109 */     return this.endTime;
/*     */   }
/*     */   
/*     */   public void setEndTime(int endTime) {
/* 113 */     this.endTime = endTime;
/*     */   }
/*     */   
/*     */   public boolean isDead() {
/* 117 */     return this.isDead;
/*     */   }
/*     */   
/*     */   public void setDead(boolean isDead) {
/* 121 */     this.isDead = isDead;
/*     */   }
/*     */   
/*     */   public int getRebirthCD() {
/* 125 */     return this.rebirthCD;
/*     */   }
/*     */   
/*     */   public void setRebirthCD(int rebirthCD) {
/* 129 */     this.rebirthCD = rebirthCD;
/*     */   }
/*     */   
/*     */   public static class RoadSummry {
/* 133 */     List<Player.showModle> atkplayers = new ArrayList<>();
/* 134 */     List<Player.showModle> defplayers = new ArrayList<>();
/*     */     long Winner;
/*     */     int overTime;
/*     */     int oneFightOver;
/*     */     
/*     */     public RoadSummry(GuildWarMgr.Road road) {
/* 140 */       road.getAtkplayers().stream().forEach(x -> this.atkplayers.add(((PlayerBase)x.getFeature(PlayerBase.class)).modle()));
/*     */ 
/*     */       
/* 143 */       road.getDefplayers().stream().forEach(x -> this.defplayers.add(((PlayerBase)x.getFeature(PlayerBase.class)).modle()));
/*     */ 
/*     */       
/* 146 */       this.Winner = road.getWinner();
/* 147 */       this.overTime = Math.max(0, GuildWarConfig.overTime() - CommTime.nowSecond() - road.getOverTime());
/* 148 */       this.oneFightOver = Math.max(0, road.getBegin() + GuildWarConfig.oneFightTime() / 1000 - CommTime.nowSecond());
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ResultRecord
/*     */   {
/*     */     private long sid;
/*     */     private long atkpid;
/*     */     private String atkName;
/*     */     private long defpid;
/*     */     private String defName;
/*     */     private int result;
/*     */     private int centerId;
/*     */     private int fightTime;
/*     */     
/*     */     public ResultRecord(GuildwarResultBO bo) {
/* 164 */       this.sid = bo.getId();
/* 165 */       this.atkpid = bo.getAtkpid();
/* 166 */       this.atkName = PlayerMgr.getInstance().getPlayer(this.atkpid).getName();
/* 167 */       this.defpid = bo.getDefpid();
/* 168 */       this.defName = PlayerMgr.getInstance().getPlayer(this.defpid).getName();
/* 169 */       this.result = bo.getResult();
/* 170 */       this.centerId = bo.getCenterId();
/* 171 */       this.fightTime = bo.getFightTime();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/proto/GuildWarFightProtol.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */