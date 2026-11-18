package core.network.client2game.handler.guild;

import business.global.guild.Guild;
import business.player.Player;
import business.player.feature.PlayerBase;
import business.player.feature.guild.GuildMemberFeature;
import com.google.gson.Gson;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.database.game.bo.GuildBossBO;
import core.database.game.bo.GuildBossChallengeBO;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuildBossEnter extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        GuildBossEnter.Request req = new Gson().fromJson(message, GuildBossEnter.Request.class);
        GuildMemberFeature guildMember = player.getFeature(GuildMemberFeature.class);
        Guild guild = guildMember.getGuild();
        if (guild == null) {
            throw new WSException(ErrorCode.Guild_IndependentMan, "玩家[%s]未参与任何帮会", player.getPid());
        } else {
            GuildBossChallengeBO challengBo = guildMember.beginFightGuildBoss(req.bossId);
            GuildBossBO boss = guild.getboss(req.bossId);
            List<Player> players = guild.getPlayerList(boss);
            List<core.network.proto.Player.FightInfo> fullInfoList = new ArrayList<>();

            for (Player tmpPlay : players) {
                if (tmpPlay != null && tmpPlay != player) {
                    fullInfoList.add(tmpPlay.getFeature(PlayerBase.class).fightInfo());
                }
            }

            request.response(new GuildBossEnter.EnterInfo(fullInfoList, boss, challengBo));
        }
    }

    private static class EnterInfo {
        List<core.network.proto.Player.FightInfo> players;
        GuildBossBO boss;
        GuildBossChallengeBO challenge;

        private EnterInfo(List<core.network.proto.Player.FightInfo> fullInfo, GuildBossBO boss, GuildBossChallengeBO challenge) {
            this.players = fullInfo;
            this.boss = boss;
            this.challenge = challenge;
        }
    }

    public static class Request {
        int bossId;
    }
}
