/*    */ package core.network.proto;
/*    */ 
/*    */ import business.global.guild.Guild;
/*    */ import business.global.guild.GuildMgr;
/*    */ import core.database.game.bo.GuildwarGuildResultBO;
/*    */ import java.util.List;
/*    */ 
/*    */ public class GuildWarCenterInfo
/*    */ {
/*    */   int centerId;
/*    */   boolean isOpen;
/*    */   List<Guild.GuildSummary> atkGuild;
/*    */   Guild.GuildSummary owner;
/*    */   Guild.GuildSummary atkingGuild;
/*    */   Guild.GuildSummary winner;
/*    */   List<GuildwarGuildResultRecord> result;
/*    */   
/*    */   public Guild.GuildSummary getAtkingGuild() {
/* 19 */     return this.atkingGuild;
/*    */   }
/*    */   
/*    */   public void setAtkingGuild(Guild.GuildSummary atkingGuild) {
/* 23 */     this.atkingGuild = atkingGuild;
/*    */   }
/*    */   
/*    */   public int getCenterId() {
/* 27 */     return this.centerId;
/*    */   }
/*    */   
/*    */   public void setCenterId(int centerId) {
/* 31 */     this.centerId = centerId;
/*    */   }
/*    */   
/*    */   public List<Guild.GuildSummary> getAtkGuild() {
/* 35 */     return this.atkGuild;
/*    */   }
/*    */   
/*    */   public void setAtkGuild(List<Guild.GuildSummary> atkGuild) {
/* 39 */     this.atkGuild = atkGuild;
/*    */   }
/*    */   
/*    */   public Guild.GuildSummary getWinner() {
/* 43 */     return this.winner;
/*    */   }
/*    */   
/*    */   public void setWinner(Guild.GuildSummary winner) {
/* 47 */     this.winner = winner;
/*    */   }
/*    */   
/*    */   public Guild.GuildSummary getOwner() {
/* 51 */     return this.owner;
/*    */   }
/*    */   
/*    */   public void setOwner(Guild.GuildSummary owner) {
/* 55 */     this.owner = owner;
/*    */   }
/*    */   
/*    */   public List<GuildwarGuildResultRecord> getResult() {
/* 59 */     return this.result;
/*    */   }
/*    */   
/*    */   public void setResult(List<GuildwarGuildResultRecord> result) {
/* 63 */     this.result = result;
/*    */   }
/*    */   
/*    */   public boolean isOpen() {
/* 67 */     return this.isOpen;
/*    */   }
/*    */   
/*    */   public void setOpen(boolean isOpen) {
/* 71 */     this.isOpen = isOpen;
/*    */   }
/*    */   
/*    */   public static class GuildwarGuildResultRecord {
/*    */     long id;
/*    */     long atkGuildId;
/*    */     String atkName;
/*    */     long defGuildId;
/*    */     String defName;
/*    */     int result;
/*    */     int centerId;
/*    */     int fightTime;
/*    */     
/*    */     public GuildwarGuildResultRecord(GuildwarGuildResultBO bo) {
/* 85 */       this.id = bo.getId();
/* 86 */       this.atkGuildId = bo.getAtkGuildId();
/* 87 */       this.atkName = GuildMgr.getInstance().getGuild(bo.getAtkGuildId()).getName();
/* 88 */       this.defGuildId = bo.getDefGuildId();
/* 89 */       if (bo.getDefGuildId() != 0L) {
/* 90 */         this.defName = GuildMgr.getInstance().getGuild(bo.getDefGuildId()).getName();
/*    */       } else {
/* 92 */         this.defName = "神秘帮会";
/*    */       } 
/* 94 */       this.result = bo.getResult();
/* 95 */       this.centerId = bo.getCenterId();
/* 96 */       this.fightTime = bo.getFightTime();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/proto/GuildWarCenterInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */