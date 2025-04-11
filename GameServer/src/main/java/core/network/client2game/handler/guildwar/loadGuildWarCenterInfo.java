package core.network.client2game.handler.guildwar;

import business.global.guild.Guild;
import business.global.guild.GuildWarMgr;
import business.player.Player;
import business.player.feature.guild.GuildMemberFeature;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import core.network.proto.GuildWarCenterInfo;

import java.io.IOException;
import java.util.List;

public class loadGuildWarCenterInfo
        extends PlayerHandler {
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        GuildMemberFeature guildMember = (GuildMemberFeature) player.getFeature(GuildMemberFeature.class);
        Guild guild = guildMember.getGuild();
        int centerId = 0;
        if (guild != null && guild.guildwarCenter != null) {
            centerId = guild.guildwarCenter.getCenterId();
        }

        request.response(new Respones(centerId, GuildWarMgr.getInstance().getCenterInfo(), null));
    }

    private static class Respones {
        int applyCenter;
        List<GuildWarCenterInfo> centerInfo;

        private Respones(int applyCenter, List<GuildWarCenterInfo> centerInfo) {
            this.applyCenter = applyCenter;
            this.centerInfo = centerInfo;
        }
    }
}

