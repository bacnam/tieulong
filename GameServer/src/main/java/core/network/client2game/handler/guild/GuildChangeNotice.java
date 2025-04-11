package core.network.client2game.handler.guild;

import business.global.guild.Guild;
import business.global.guild.GuildMgr;
import business.player.Player;
import business.player.feature.guild.GuildMemberFeature;
import com.google.gson.Gson;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.ref.RefGuildJobInfo;
import core.network.client2game.handler.PlayerHandler;

import java.io.IOException;

public class GuildChangeNotice
        extends PlayerHandler {
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        Request req = (Request) (new Gson()).fromJson(message, Request.class);
        GuildMemberFeature guildMember = (GuildMemberFeature) player.getFeature(GuildMemberFeature.class);
        Guild guild = guildMember.getGuild();
        if (guild == null) {
            throw new WSException(ErrorCode.Guild_IndependentMan, "玩家[%s]未参与任何帮会", new Object[]{Long.valueOf(player.getPid())});
        }

        RefGuildJobInfo job = guildMember.getJobRef();
        if (!job.ChangeNotice) {
            throw new WSException(ErrorCode.Guild_PermissionDenied, "玩家[%s]职务[%s]没有改名[ChangeNotice]的权限", new Object[]{Long.valueOf(player.getPid()), job.id});
        }
        GuildMgr.CheckNoteValid(req.notice, player.getPid());

        guild.getBo().saveNotice(req.notice);

        request.response(guild.guildInfo());
    }

    public static class Request {
        String notice;
    }
}

