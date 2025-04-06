/*    */ package core.network.proto;
/*    */ 
/*    */ import business.global.guild.Guild;
/*    */ import business.global.guild.GuildConfig;
/*    */ import business.player.Player;
/*    */ import business.player.feature.character.CharFeature;
/*    */ import business.player.feature.character.WarSpirit;
/*    */ import business.player.feature.character.WarSpiritFeature;
/*    */ import business.player.feature.guild.GuildMemberFeature;
/*    */ import business.player.feature.player.LingBaoFeature;
/*    */ import business.player.feature.player.NewTitleFeature;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TeamInfo
/*    */ {
/*    */   List<Character.CharInfo> characters;
/*    */   WarSpiritInfo warSpirit;
/*    */   List<Guild.GuildSkill> guildSkill;
/*    */   List<TitleInfo> title;
/*    */   int LingBaoLevel;
/*    */   String name;
/*    */   
/*    */   public TeamInfo(Player player) {
/* 26 */     WarSpirit warSpirit = ((WarSpiritFeature)player.getFeature(WarSpiritFeature.class)).getWarSpiritNow();
/* 27 */     if (warSpirit != null) {
/* 28 */       this.warSpirit = new WarSpiritInfo(warSpirit);
/*    */     }
/* 30 */     GuildMemberFeature guildMemberFeature = (GuildMemberFeature)player.getFeature(GuildMemberFeature.class);
/* 31 */     Guild guild = guildMemberFeature.getGuild();
/* 32 */     if (guild != null) {
/* 33 */       this.guildSkill = guildMemberFeature.getGuildSkillList();
/* 34 */       for (Guild.GuildSkill skill : this.guildSkill) {
/* 35 */         int maxLevel = GuildConfig.getSkillMaxLevel(skill.getSkillid(), guild.getLevel());
/* 36 */         int level = Math.min(maxLevel, skill.getLevel());
/* 37 */         skill.setLevel(level);
/*    */       } 
/*    */     } 
/* 40 */     this.title = ((NewTitleFeature)player.getFeature(NewTitleFeature.class)).getAllTitleInfo();
/*    */     
/* 42 */     this.characters = ((CharFeature)player.getFeature(CharFeature.class)).getAllInfo();
/*    */     
/* 44 */     this.LingBaoLevel = ((LingBaoFeature)player.getFeature(LingBaoFeature.class)).getLevel();
/* 45 */     this.name = player.getName();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/proto/TeamInfo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */