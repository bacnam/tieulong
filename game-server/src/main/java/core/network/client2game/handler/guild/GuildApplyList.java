package core.network.client2game.handler.guild;

import business.global.guild.Guild;
import business.player.PlayerMgr;
import business.player.feature.PlayerBase;
import business.player.feature.guild.GuildMemberFeature;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.database.game.bo.GuildApplyBO;
import core.network.client2game.handler.PlayerHandler;
import core.network.proto.Player;
import java.io.IOException;
import java.util.Collection;
import java.util.stream.Collectors;

public class GuildApplyList extends PlayerHandler {
    @Override
    public void handle(business.player.Player player, WebSocketRequest request, String message) throws WSException, IOException {
        GuildMemberFeature guildFeature = player.getFeature(GuildMemberFeature.class);
        Guild guild = guildFeature.getGuild();
        if (guild == null) {
            throw new WSException(ErrorCode.Guild_IndependentMan, "玩家[%s]未参与任何帮会", player.getPid());
        } else {
            Collection<GuildApplyBO> applicants = guild.getApplies();
            PlayerMgr playerMgr = PlayerMgr.getInstance();
            Collection<Player.Summary> proto = applicants.stream()
                .map(x -> playerMgr.getPlayer(x.getPid()).getFeature(PlayerBase.class).summary())
                .collect(Collectors.toList());
            request.response(proto);
        }
    }
}
