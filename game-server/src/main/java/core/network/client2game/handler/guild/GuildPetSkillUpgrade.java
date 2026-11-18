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

public class GuildPetSkillUpgrade extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        GuildPetSkillUpgrade.Request req = new Gson().fromJson(message, GuildPetSkillUpgrade.Request.class);
        GuildMemberFeature guildMember = player.getFeature(GuildMemberFeature.class);
        Guild guild = guildMember.getGuild();
        if (guild == null) {
            throw new WSException(ErrorCode.Guild_IndependentMan, "玩家[%s]未参与任何帮会", player.getPid());
        } else {
            int skillId = req.skillid;
            if (skillId <= 0) {
                throw new WSException(ErrorCode.InvalidParam, "非法的参数skillId=:%s", skillId);
            } else {
                guildMember.memberSkillUpgrade(skillId);
                core.network.proto.Guild.GuildSkill builder = new core.network.proto.Guild.GuildSkill();
                builder.setSkillid(req.skillid);
                builder.setLevel(guildMember.getMemberSkill(skillId).getLevel());
                request.response(new GuildPetSkillUpgrade.Response(builder, guildMember.getSkillPointsLeft()));
            }
        }
    }

    public static class Request {
        int skillid;
    }

    private static class Response {
        core.network.proto.Guild.GuildSkill skill;
        int leftTimes;

        private Response(core.network.proto.Guild.GuildSkill skill, int leftTimes) {
            this.skill = skill;
            this.leftTimes = leftTimes;
        }
    }
}
