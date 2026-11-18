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

public class GuildBossLeave extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        GuildBossLeave.Request req = new Gson().fromJson(message, GuildBossLeave.Request.class);
        GuildMemberFeature guildMember = player.getFeature(GuildMemberFeature.class);
        Guild guild = guildMember.getGuild();
        if (guild == null) {
            throw new WSException(ErrorCode.Guild_IndependentMan, "玩家[%s]未参与任何帮会", player.getPid());
        } else {
            Reward reward = guildMember.LeaveGuildBoss(req.bossId);
            request.response(new GuildBossLeave.Response(guildMember.getOnceDamage(), reward));
        }
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
