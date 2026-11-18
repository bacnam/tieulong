package core.network.client2game.handler.guild;

import business.player.Player;
import business.player.PlayerMgr;
import business.player.feature.guild.GuildMemberFeature;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import core.network.proto.Guild;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class GuildMemberList extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        GuildMemberFeature guildFeature = player.getFeature(GuildMemberFeature.class);
        business.global.guild.Guild guild = guildFeature.getGuild();
        if (guild == null) {
            throw new WSException(ErrorCode.Guild_IndependentMan, "玩家[%s]未参与任何帮会", player.getPid());
        } else {
            List<Long> members = guild.getMembers();
            PlayerMgr playerMgr = PlayerMgr.getInstance();
            List<Guild.member> memberProto = members.stream()
                .map(x -> playerMgr.getPlayer(x).getFeature(GuildMemberFeature.class).memberInfo())
                .collect(Collectors.toList());
            request.response(memberProto);
        }
    }
}
