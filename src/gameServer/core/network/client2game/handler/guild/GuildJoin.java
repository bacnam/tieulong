package core.network.client2game.handler.guild;

import business.global.guild.Guild;
import business.global.guild.GuildConfig;
import business.global.guild.GuildMgr;
import business.player.Player;
import business.player.feature.PlayerBase;
import business.player.feature.guild.GuildMemberFeature;
import com.google.gson.Gson;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefGuildLevel;
import core.database.game.bo.GuildApplyBO;
import core.network.client2game.handler.PlayerHandler;
import core.network.proto.Guild;
import java.io.IOException;
import java.util.List;

public class GuildJoin
extends PlayerHandler
{
public static class Request
{
long sid;
}

public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
Request req = (Request)(new Gson()).fromJson(message, Request.class);
int openlevel = GuildConfig.UnlockLevel();
if (player.getLv() < openlevel) {
throw new WSException(ErrorCode.NotEnough_TeamLevel, "[%s]玩家等级不足[%s]，无法加入公会", new Object[] { Long.valueOf(player.getPid()), Integer.valueOf(openlevel) });
}

GuildMgr guildMgrL = GuildMgr.getInstance();
Guild guild = guildMgrL.getGuild(req.sid);
if (guild == null) {
throw new WSException(ErrorCode.NotFound_Guild, "未查找到[%s]帮会的信息", new Object[] { Long.valueOf(req.sid) });
}

GuildMemberFeature guildMember = (GuildMemberFeature)player.getFeature(GuildMemberFeature.class);

if (guildMember.getJoinCD() > 0) {
throw new WSException(ErrorCode.Guild_InJoinCD, "[%s]玩家已经在帮会在加入CD中，不能加入帮会", new Object[] { Long.valueOf(player.getPid()) });
}

synchronized (this) {
RefGuildLevel refGuildLevel = (RefGuildLevel)RefDataMgr.get(RefGuildLevel.class, Integer.valueOf(guild.getLevel()));
if (guild.getMemberCnt() >= refGuildLevel.MaxMemberAmount) {
throw new WSException(ErrorCode.Guild_FullMember, "[%s]帮会成员已满", new Object[] { Long.valueOf(req.sid) });
}
List<GuildApplyBO> applyBOs = guildMgrL.getApplyByCid(player.getPid());
for (GuildApplyBO applyBO : applyBOs) {
if (applyBO.getGuildId() == req.sid) {
throw new WSException(ErrorCode.InvalidParam, "[%s]玩家已经申请过帮会[%s]，不能重复申请", new Object[] { Long.valueOf(player.getPid()), guild.getName() });
}
} 
Guild oldGuild = guildMember.getGuild();
if (oldGuild != null) {
throw new WSException(ErrorCode.Guild_AlreadyInGuild, "[%s]玩家已经在帮会[%s]，不能再加其他帮会", new Object[] { Long.valueOf(player.getPid()), oldGuild.getName() });
}

if (applyBOs.size() >= RefDataMgr.getFactor("Guild_MaxApplyCount", 3)) {
throw new WSException(ErrorCode.Guild_InJoinCD, "[%s]玩家申请[%s条]已经到达上限，不能再申请帮会", new Object[] { Long.valueOf(player.getPid()), Integer.valueOf(RefDataMgr.getFactor("Guild_MaxApplyCount", 3)) });
}
GuildMgr.getInstance().apply(player.getPid(), guild.getGuildId());

Guild.GuildApply proto = new Guild.GuildApply();
proto.applyer = ((PlayerBase)player.getFeature(PlayerBase.class)).summary();

guild.broadcast("newApply", proto, player.getPid());
} 

request.response();
}
}

