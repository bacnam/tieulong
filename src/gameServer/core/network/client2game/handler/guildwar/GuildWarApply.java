package core.network.client2game.handler.guildwar;

import business.global.guild.Guild;
import business.global.guild.GuildWarMgr;
import business.player.Player;
import business.player.feature.guild.GuildMemberFeature;
import com.google.gson.Gson;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefGuildJobInfo;
import core.config.refdata.ref.RefGuildWarCenter;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class GuildWarApply
extends PlayerHandler
{
public static class Request
{
int centerId;
}

public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
Request req = (Request)(new Gson()).fromJson(message, Request.class);
Guild guild = ((GuildMemberFeature)player.getFeature(GuildMemberFeature.class)).getGuild();
RefGuildJobInfo job = ((GuildMemberFeature)player.getFeature(GuildMemberFeature.class)).getJobRef();
if (!job.GuildwarApply) {
throw new WSException(ErrorCode.GuildWar_NotPermit, "权限不足");
}
guild.applyGuildWar(req.centerId);
RefGuildWarCenter ref = (RefGuildWarCenter)RefDataMgr.get(RefGuildWarCenter.class, Integer.valueOf(req.centerId));
request.response(GuildWarMgr.getInstance().centerInfo(ref));
}
}

