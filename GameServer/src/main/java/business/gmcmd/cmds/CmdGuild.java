package business.gmcmd.cmds;

import business.global.guild.Guild;
import business.global.guild.GuildMgr;
import business.global.guild.GuildWarMgr;
import business.gmcmd.annotation.Command;
import business.gmcmd.annotation.Commander;
import business.player.Player;
import business.player.feature.guild.GuildMemberFeature;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefGuildLevel;
import core.config.refdata.ref.RefGuildWarCenter;
import core.database.game.bo.GuildwarapplyBO;

@Commander(name = "guild", comment = "公会相关命令")
public class CmdGuild
{
@Command(comment = "删除公会")
public String del(Player player, String name) {
Guild guild = GuildMgr.getInstance().getGuild(name);
GuildMgr.getInstance().deleteGuild(guild.getGuildId());
return String.format("删除公会", new Object[0]);
}

@Command(comment = "设置公会等级")
public String level(Player player, int level) {
RefGuildLevel ref = (RefGuildLevel)RefDataMgr.get(RefGuildLevel.class, Integer.valueOf(level));
Guild guild = ((GuildMemberFeature)player.getFeature(GuildMemberFeature.class)).getGuild();
if (guild == null || ref == null) {
return "failed";
}
guild.bo.saveLevel(level);
return String.format("设置公会等级", new Object[0]);
}

@Command(comment = "模拟工会战")
public String warStart(Player player) {
GuildWarMgr.getInstance().Start();
return String.format("模拟工会战", new Object[0]);
}

@Command(comment = "模拟工会战")
public String warfight(Player player) {
GuildWarMgr.getInstance().fight();
return String.format("模拟工会战", new Object[0]);
}

@Command(comment = "模拟工会战报名")
public String warapply(Player player, int centerId) throws WSException {
GuildMemberFeature guildMember = (GuildMemberFeature)player.getFeature(GuildMemberFeature.class);
Guild guild = guildMember.getGuild();
if (guild == null) {
throw new WSException(ErrorCode.Guild_IndependentMan, "玩家[%s]未参与任何帮会", new Object[] { Long.valueOf(player.getPid()) });
}

RefGuildWarCenter ref = (RefGuildWarCenter)RefDataMgr.get(RefGuildWarCenter.class, Integer.valueOf(centerId));
if (ref == null) {
throw new WSException(ErrorCode.GuildWar_NotFoundCenter, "据点不存在");
}

if (guild.guildwarCenter != null) {
throw new WSException(ErrorCode.GuildWar_AlreadyOpen, "帮派已报名");
}

GuildWarMgr.getInstance().applyGuildWar(centerId, guild);
GuildwarapplyBO bo = new GuildwarapplyBO();
bo.setGuildId(guild.getGuildId());
bo.setCenterId(centerId);
bo.setApplyTime(CommTime.nowSecond());
bo.insert();
guild.guildwarCenter = bo;
return String.format("模拟工会战报名", new Object[0]);
}

@Command(comment = "模拟龙女")
public String longnvfight(Player player) throws WSException {
GuildMemberFeature guildMember = (GuildMemberFeature)player.getFeature(GuildMemberFeature.class);
Guild guild = guildMember.getGuild();
if (guild == null) {
throw new WSException(ErrorCode.Guild_IndependentMan, "玩家[%s]未参与任何帮会", new Object[] { Long.valueOf(player.getPid()) });
}
guild.getOrCreateLongnv().Start();
return String.format("模拟工会战", new Object[0]);
}
}

