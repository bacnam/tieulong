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

public class GuildWarApply extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        GuildWarApply.Request req = new Gson().fromJson(message, GuildWarApply.Request.class);
        Guild guild = player.getFeature(GuildMemberFeature.class).getGuild();
        RefGuildJobInfo job = player.getFeature(GuildMemberFeature.class).getJobRef();
        if (!job.GuildwarApply) {
            throw new WSException(ErrorCode.GuildWar_NotPermit, "权限不足");
        } else {
            guild.applyGuildWar(req.centerId);
            RefGuildWarCenter ref = RefDataMgr.get(RefGuildWarCenter.class, req.centerId);
            request.response(GuildWarMgr.getInstance().centerInfo(ref));
        }
    }

    public static class Request {
        int centerId;
    }
}
