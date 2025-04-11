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

public class GuildPetSkillUpgrade
        extends PlayerHandler {
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        Request req = (Request) (new Gson()).fromJson(message, Request.class);
        GuildMemberFeature guildMember = (GuildMemberFeature) player.getFeature(GuildMemberFeature.class);
        Guild guild = guildMember.getGuild();
        if (guild == null) {
            throw new WSException(ErrorCode.Guild_IndependentMan, "玩家[%s]未参与任何帮会", new Object[]{Long.valueOf(player.getPid())});
        }
        int skillId = req.skillid;
        if (skillId <= 0) {
            throw new WSException(ErrorCode.InvalidParam, "非法的参数skillId=:%s", new Object[]{Integer.valueOf(skillId)});
        }
        guildMember.memberSkillUpgrade(skillId);
        Guild.GuildSkill builder = new Guild.GuildSkill();
        builder.setSkillid(req.skillid);
        builder.setLevel(guildMember.getMemberSkill(skillId).getLevel());
        request.response(new Response(builder, guildMember.getSkillPointsLeft(), null));
    }

    public static class Request {
        int skillid;
    }

    private static class Response {
        Guild.GuildSkill skill;
        int leftTimes;

        private Response(Guild.GuildSkill skill, int leftTimes) {
            this.skill = skill;
            this.leftTimes = leftTimes;
        }
    }
}

