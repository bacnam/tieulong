package core.network.client2game.handler.guild;

import business.global.guild.Guild;
import business.player.Player;
import business.player.feature.guild.GuildMemberFeature;
import business.player.item.Reward;
import com.google.gson.Gson;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;

import java.io.IOException;

public class GuildBossLeave
        extends PlayerHandler {
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        Request req = (Request) (new Gson()).fromJson(message, Request.class);
        GuildMemberFeature guildMember = (GuildMemberFeature) player.getFeature(GuildMemberFeature.class);
        Guild guild = guildMember.getGuild();
        if (guild == null) {
            throw new WSException(ErrorCode.Guild_IndependentMan, "玩家[%s]未参与任何帮会", new Object[]{Long.valueOf(player.getPid())});
        }
        Reward reward = guildMember.LeaveGuildBoss(req.bossId);
        request.response(new Response(guildMember.getOnceDamage(), reward, null));
    }

    public static class Request {
        int bossId;
    }

    private static class Response {
        long damage;
        Reward reward;

        private Response(long damage, Reward reward) {
            this.damage = damage;
            this.reward = reward;
        }
    }
}

