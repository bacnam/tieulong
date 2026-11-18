package core.network.client2game.handler.guild;

import business.global.guild.Guild;
import business.player.Player;
import business.player.feature.guild.GuildMemberFeature;
import com.google.gson.Gson;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class GuildBossFight extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        GuildBossFight.Request req = new Gson().fromJson(message, GuildBossFight.Request.class);
        GuildMemberFeature guildMember = player.getFeature(GuildMemberFeature.class);
        Guild guild = guildMember.getGuild();
        if (guild == null) {
            throw new WSException(ErrorCode.Guild_IndependentMan, "玩家[%s]未参与任何帮会", player.getPid());
        } else {
            if (req.damage < 0) {
                req.damage = 0;
            }

            guildMember.hurtWorldBoss(req.charId, req.bossId, req.damage);
            request.response(guild.getboss(req.bossId));
        }
    }

    public static class Request {
        int charId;
        int bossId;
        int damage;
    }
}
