package core.network.client2game.handler.guild;

import business.global.guild.Guild;
import business.global.guild.GuildMgr;
import business.player.Player;
import business.player.PlayerMgr;
import business.player.feature.guild.GuildMemberFeature;
import com.google.gson.Gson;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefGuildJobInfo;
import core.config.refdata.ref.RefGuildLevel;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class GuildDealApply
extends PlayerHandler
{
public static class Request
{
long pid;
boolean isAccept;
}

public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
Request req = (Request)(new Gson()).fromJson(message, Request.class);

GuildMemberFeature guildMember = (GuildMemberFeature)player.getFeature(GuildMemberFeature.class);
Guild guild = guildMember.getGuild();
if (guild == null) {
throw new WSException(ErrorCode.Guild_IndependentMan, "玩家[%s]未参与任何帮会", new Object[] { Long.valueOf(player.getPid()) });
}
RefGuildJobInfo job = guildMember.getJobRef();
if (!job.DealRequest) {
throw new WSException(ErrorCode.Guild_PermissionDenied, "玩家[%s]职务[%s]没有处理申请[DealRequest]的权限", new Object[] { Long.valueOf(player.getPid()), job.id });
}
if (guild.getApply(req.pid) == null) {
throw new WSException(ErrorCode.Guild_NotMember, "玩家[%s]没有申请加入[%s]公会，或者已经加入其他公会", new Object[] { Long.valueOf(req.pid), guild.getName() });
}
if (req.isAccept) {
RefGuildLevel refGuildLevel = (RefGuildLevel)RefDataMgr.get(RefGuildLevel.class, Integer.valueOf(guild.getLevel()));
if (guild.getMemberCnt() >= refGuildLevel.MaxMemberAmount) {
throw new WSException(ErrorCode.Guild_FullMember, "[%s]帮会成员已满", new Object[] { guild.getName() });
}
Player newMember = PlayerMgr.getInstance().getPlayer(req.pid);
GuildMemberFeature newFeature = guild.getMember(req.pid);
if (newFeature != null) {
throw new WSException(ErrorCode.Guild_Already, "玩家已在公会");
}

guild.takeinMember(newMember);
} else {
GuildMgr.getInstance().removeApply(req.pid, guild.getGuildId());
} 
request.response();
}
}

