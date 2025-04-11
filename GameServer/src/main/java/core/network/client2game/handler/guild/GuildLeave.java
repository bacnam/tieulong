package core.network.client2game.handler.guild;

import business.global.guild.Guild;
import business.global.guild.GuildMgr;
import business.player.Player;
import business.player.feature.guild.GuildMemberFeature;
import com.zhonglian.server.common.enums.GuildJob;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;

import java.io.IOException;

public class GuildLeave
        extends PlayerHandler {
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        GuildMemberFeature guildMember = (GuildMemberFeature) player.getFeature(GuildMemberFeature.class);
        Guild guild = guildMember.getGuild();
        if (guild == null) {
            throw new WSException(ErrorCode.Guild_IndependentMan, "玩家[%s]未参与任何帮会", new Object[]{Long.valueOf(player.getPid())});
        }
        if (guildMember.getJob() == GuildJob.President && guild.getMemberCnt() >= 2) {
            throw new WSException(ErrorCode.Guild_PermissionDenied, "帮会[%s]还有2个人以上，玩家[%s]作为帮主无法退出", new Object[]{guild.getName(), Long.valueOf(player.getPid())});
        }

        guildMember.leave();
        if (guild.getMemberCnt() == 0) {
            GuildMgr.getInstance().deleteGuild(guild.getGuildId());
        } else {
            guild.broadcast("memberleave", Long.valueOf(player.getPid()));
        }

        request.response();
    }
}

