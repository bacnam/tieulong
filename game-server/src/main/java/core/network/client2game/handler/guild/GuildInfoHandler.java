package core.network.client2game.handler.guild;

import business.global.guild.Guild;
import business.player.Player;
import business.player.feature.guild.GuildMemberFeature;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class GuildInfoHandler extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        GuildMemberFeature guildMember = player.getFeature(GuildMemberFeature.class);
        Guild guildL = guildMember.getGuild();
        core.network.proto.Guild.guildInfo guildInfo = null;
        core.network.proto.Guild.member member = guildMember.memberInfo();
        if (guildL != null) {
            guildInfo = guildL.guildInfo();
        }

        request.response(new GuildInfoHandler.GuildBaseInfo(guildInfo, member, guildMember.getJoinCD()));
    }

    public static class GuildBaseInfo {
        core.network.proto.Guild.guildInfo guildInfo;
        core.network.proto.Guild.member myInfo;
        int joinCD;

        public GuildBaseInfo(core.network.proto.Guild.guildInfo guildInfo, core.network.proto.Guild.member myInfo, int joinCD) {
            this.guildInfo = guildInfo;
            this.myInfo = myInfo;
            this.joinCD = joinCD;
        }
    }
}
