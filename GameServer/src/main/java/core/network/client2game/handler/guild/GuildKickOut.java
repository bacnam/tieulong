package core.network.client2game.handler.guild;

import business.global.gmmail.MailCenter;
import business.global.guild.Guild;
import business.global.guild.GuildConfig;
import business.player.Player;
import business.player.PlayerMgr;
import business.player.feature.guild.GuildMemberFeature;
import com.google.gson.Gson;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.ref.RefGuildJobInfo;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class GuildKickOut
extends PlayerHandler
{
public static class Request
{
long pid;
}

public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
Request req = (Request)(new Gson()).fromJson(message, Request.class);
GuildMemberFeature guildMember = (GuildMemberFeature)player.getFeature(GuildMemberFeature.class);
Guild guild = guildMember.getGuild();
if (guild == null) {
throw new WSException(ErrorCode.Guild_IndependentMan, "玩家[%s]未参与任何帮会", new Object[] { Long.valueOf(player.getPid()) });
}

RefGuildJobInfo job = guildMember.getJobRef();

if (!job.Kickout) {
throw new WSException(ErrorCode.Guild_PermissionDenied, "玩家[%s]职务[%s]没有踢人的权限", new Object[] { Long.valueOf(player.getPid()), job });
}

GuildMemberFeature target = guild.getMember(req.pid);
if (target == null) {
throw new WSException(ErrorCode.Guild_NotMember, "玩家[%s]并不是公会[%s]的成员，无法踢人", new Object[] { Long.valueOf(req.pid), guild.getName() });
}
target.leave();
target.getBo().saveLastLeaveTime(guildMember.getBo().getLastLeaveTime() - GuildConfig.JoinCD());
guild.broadcast("guildkickout", Long.valueOf(req.pid));
Player targetplayer = PlayerMgr.getInstance().getPlayer(req.pid);
targetplayer.pushProto("bekickout", "");
MailCenter.getInstance().sendMail(targetplayer.getPid(), GuildConfig.KickoutGuildMailID(), new String[] { guild.getName() });
request.response(Long.valueOf(req.pid));
}
}

