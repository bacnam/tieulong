/*     */ package core.network.proto;
/*     */ 
/*     */ import business.global.guild.Guild;
/*     */ import business.global.guild.GuildWarConfig;
/*     */ import business.global.guild.GuildWarMgr;
/*     */ import business.player.Player;
/*     */ import business.player.PlayerMgr;
/*     */ import business.player.feature.PlayerBase;
/*     */ import com.zhonglian.server.common.utils.CommTime;
/*     */ import core.database.game.bo.LongnvResultBO;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ public class LongnvWarFightProtol
/*     */ {
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
/*  35 */       this.name = name;
/*  36 */       this.left = left;
/*  37 */       this.dead = dead;
/*  38 */       this.puppet = puppet;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRebirthTime() {
/*  44 */     return this.rebirthTime;
/*     */   }
/*     */   
/*     */   public void setRebirthTime(int rebirthTime) {
/*  48 */     this.rebirthTime = rebirthTime;
/*     */   }
/*     */   
/*     */   public Guild.GuildSummary getAtk() {
/*  52 */     return this.atk;
/*     */   }
/*     */   
/*     */   public void setAtk(Guild.GuildSummary atk) {
/*  56 */     this.atk = atk;
/*     */   }
/*     */   
/*     */   public Guild.GuildSummary getDef() {
/*  60 */     return this.def;
/*     */   }
/*     */   
/*     */   public void setDef(Guild.GuildSummary def) {
/*  64 */     this.def = def;
/*     */   }
/*     */   
/*     */   public List<GuildInfo> getGuildInfo() {
/*  68 */     return this.guildInfo;
/*     */   }
/*     */   
/*     */   public void setGuildInfo(List<GuildInfo> guildInfo) {
/*  72 */     this.guildInfo = guildInfo;
/*     */   }
/*     */   
/*     */   public List<ResultRecord> getResultInfo() {
/*  76 */     return this.resultInfo;
/*     */   }
/*     */   
/*     */   public void setResultInfo(List<ResultRecord> resultInfo) {
/*  80 */     this.resultInfo = resultInfo;
/*     */   }
/*     */   
/*     */   public int getKillnum() {
/*  84 */     return this.killnum;
/*     */   }
/*     */   
/*     */   public void setKillnum(int killnum) {
/*  88 */     this.killnum = killnum;
/*     */   }
/*     */   
/*     */   public List<RoadSummry> getRoad() {
/*  92 */     return this.road;
/*     */   }
/*     */   
/*     */   public void setRoad(List<RoadSummry> road) {
/*  96 */     this.road = road;
/*     */   }
/*     */   
/*     */   public int getEndTime() {
/* 100 */     return this.endTime;
/*     */   }
/*     */   
/*     */   public void setEndTime(int endTime) {
/* 104 */     this.endTime = endTime;
/*     */   }
/*     */   
/*     */   public boolean isDead() {
/* 108 */     return this.isDead;
/*     */   }
/*     */   
/*     */   public void setDead(boolean isDead) {
/* 112 */     this.isDead = isDead;
/*     */   }
/*     */   
/*     */   public int getRebirthCD() {
/* 116 */     return this.rebirthCD;
/*     */   }
/*     */   
/*     */   public void setRebirthCD(int rebirthCD) {
/* 120 */     this.rebirthCD = rebirthCD;
/*     */   }
/*     */   
/*     */   public static class RoadSummry {
/* 124 */     List<Player.showModle> atkplayers = new ArrayList<>();
/* 125 */     List<Player.showModle> defplayers = new ArrayList<>();
/*     */     long Winner;
/*     */     int overTime;
/*     */     int oneFightOver;
/*     */     
/*     */     public RoadSummry(GuildWarMgr.Road road) {
/* 131 */       road.getAtkplayers().stream().forEach(x -> this.atkplayers.add(((PlayerBase)x.getFeature(PlayerBase.class)).modle()));
/*     */ 
/*     */       
/* 134 */       road.getDefplayers().stream().forEach(x -> this.defplayers.add(((PlayerBase)x.getFeature(PlayerBase.class)).modle()));
/*     */ 
/*     */       
/* 137 */       this.Winner = road.getWinner();
/* 138 */       this.overTime = Math.max(0, GuildWarConfig.overTime() - CommTime.nowSecond() - road.getOverTime());
/* 139 */       this.oneFightOver = Math.max(0, road.getBegin() + GuildWarConfig.oneFightTime() / 1000 - CommTime.nowSecond());
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
/*     */     private int fightTime;
/*     */     
/*     */     public ResultRecord(LongnvResultBO bo) {
/* 154 */       this.sid = bo.getId();
/* 155 */       this.atkpid = bo.getAtkpid();
/* 156 */       this.atkName = PlayerMgr.getInstance().getPlayer(this.atkpid).getName();
/* 157 */       this.defpid = bo.getDefpid();
/* 158 */       this.defName = PlayerMgr.getInstance().getPlayer(this.defpid).getName();
/* 159 */       this.result = bo.getResult();
/* 160 */       this.fightTime = bo.getFightTime();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/proto/LongnvWarFightProtol.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */