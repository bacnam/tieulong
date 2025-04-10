package core.network.client2game.handler.guild;

import business.global.chat.ChatMgr;
import business.global.gmmail.MailCenter;
import business.global.guild.Guild;
import business.global.guild.GuildConfig;
import business.player.Player;
import business.player.feature.guild.GuildMemberFeature;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.ChatType;
import com.zhonglian.server.common.enums.GuildJob;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefGuildJobInfo;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class GuildChangeJob
extends PlayerHandler
{
public static class Request
{
long pid;
GuildJob job;
}

public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
Request req = (Request)(new Gson()).fromJson(message, Request.class);
GuildMemberFeature guildMember = (GuildMemberFeature)player.getFeature(GuildMemberFeature.class);
Guild guild = guildMember.getGuild();
RefGuildJobInfo job = guildMember.getJobRef();
if (guild == null) {
throw new WSException(ErrorCode.Guild_IndependentMan, "玩家[%s]未参与任何帮会", new Object[] { Long.valueOf(player.getPid()) });
}

if (!job.ManageGuild) {
throw new WSException(ErrorCode.Guild_PermissionDenied, "玩家[%s]职务[%s]没有任命职位的权限", new Object[] { Long.valueOf(player.getPid()), guildMember.getJob() });
}
if (req.pid == player.getPid()) {
throw new WSException(ErrorCode.Guild_PermissionDenied, "玩家[%s]无法调整自己在公会[%s]中的职务", new Object[] { Long.valueOf(player.getPid()), guild.getName() });
}

GuildMemberFeature target = guild.getMember(req.pid);

if (target == null) {
throw new WSException(ErrorCode.Guild_NotMember, "玩家[%s]并不是公会[%s]的成员，无法任职", new Object[] { Long.valueOf(req.pid), guild.getName() });
}
String oldjob = target.getJobName();

RefGuildJobInfo jobref = (RefGuildJobInfo)RefDataMgr.get(RefGuildJobInfo.class, req.job);
if (guild.getLevel() < jobref.UnlockLevel) {
throw new WSException(ErrorCode.Guild_PermissionDenied, "玩家[%s]公会[%s]未解锁[%s]职位", new Object[] { Long.valueOf(player.getPid()), guild.getName(), req.job });
}

int jobcount = guild.getMemberCount(req.job);
if (jobcount >= jobref.JobAmount) {
throw new WSException(ErrorCode.Guild_FullMember, "公会[%s]的职务[%s]已经[%s]人满员", new Object[] { guild.getName(), req.job, Integer.valueOf(jobcount) });
}
guild.getMember(target.getJob()).remove(Long.valueOf(target.getPid()));
target.setJob(req.job);
guild.broadcastMember(new long[] { req.pid });
if (req.job == GuildJob.VicePresident) {
String notice = GuildConfig.VicePresidentNotice();
notice = notice.replace("{0}", target.getPlayer().getName());
ChatMgr.getInstance().addChat(player, notice, ChatType.CHATTYPE_GUILD, 0L);
} 
MailCenter.getInstance().sendMail(req.pid, GuildConfig.ChangeJobGuildMailID(), new String[] { oldjob, target.getJobName() });
request.response();
}
}

