package core.network.proto;

import business.global.guild.Guild;
import business.global.guild.GuildConfig;
import business.player.Player;
import business.player.feature.character.CharFeature;
import business.player.feature.character.WarSpirit;
import business.player.feature.character.WarSpiritFeature;
import business.player.feature.guild.GuildMemberFeature;
import business.player.feature.player.LingBaoFeature;
import business.player.feature.player.NewTitleFeature;
import java.util.List;

public class TeamInfo
{
List<Character.CharInfo> characters;
WarSpiritInfo warSpirit;
List<Guild.GuildSkill> guildSkill;
List<TitleInfo> title;
int LingBaoLevel;
String name;

public TeamInfo(Player player) {
WarSpirit warSpirit = ((WarSpiritFeature)player.getFeature(WarSpiritFeature.class)).getWarSpiritNow();
if (warSpirit != null) {
this.warSpirit = new WarSpiritInfo(warSpirit);
}
GuildMemberFeature guildMemberFeature = (GuildMemberFeature)player.getFeature(GuildMemberFeature.class);
Guild guild = guildMemberFeature.getGuild();
if (guild != null) {
this.guildSkill = guildMemberFeature.getGuildSkillList();
for (Guild.GuildSkill skill : this.guildSkill) {
int maxLevel = GuildConfig.getSkillMaxLevel(skill.getSkillid(), guild.getLevel());
int level = Math.min(maxLevel, skill.getLevel());
skill.setLevel(level);
} 
} 
this.title = ((NewTitleFeature)player.getFeature(NewTitleFeature.class)).getAllTitleInfo();

this.characters = ((CharFeature)player.getFeature(CharFeature.class)).getAllInfo();

this.LingBaoLevel = ((LingBaoFeature)player.getFeature(LingBaoFeature.class)).getLevel();
this.name = player.getName();
}
}

