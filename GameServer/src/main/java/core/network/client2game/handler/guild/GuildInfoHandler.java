package core.network.client2game.handler.guild;

import business.global.guild.Guild;
import business.player.Player;
import business.player.feature.guild.GuildMemberFeature;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;

import java.io.IOException;

public class GuildInfoHandler
        extends PlayerHandler {
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        GuildMemberFeature guildMember = (GuildMemberFeature) player.getFeature(GuildMemberFeature.class);
        Guild guildL = guildMember.getGuild();
        Guild.guildInfo guildInfo = null;
        Guild.member member = guildMember.memberInfo();
        if (guildL != null) {
            guildInfo = guildL.guildInfo();
        }
        request.response(new GuildBaseInfo(guildInfo, member, guildMember.getJoinCD()));
    }

    public static class GuildBaseInfo {
        Guild.guildInfo guildInfo;
        Guild.member myInfo;
        int joinCD;

        public GuildBaseInfo(Guild.guildInfo guildInfo1, Guild.member myInfo, int joinCD) {
            this.guildInfo = guildInfo1;
            this.myInfo = myInfo;
            this.joinCD = joinCD;
        }
    }
}

