package core.network.client2game.handler.longnv;

import business.global.guild.Guild;
import business.player.Player;
import business.player.feature.features.PlayerRecord;
import business.player.feature.guild.GuildMemberFeature;
import com.zhonglian.server.common.enums.LongnvSacrificeType;
import com.zhonglian.server.common.enums.ConstEnum.DailyRefresh;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class LongnvSacrificeInfo extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        GuildMemberFeature guildMember = player.getFeature(GuildMemberFeature.class);
        Guild guild = guildMember.getGuild();
        if (guild == null) {
            throw new WSException(ErrorCode.Guild_IndependentMan, "玩家[%s]未参与任何帮会", player.getPid());
        } else {
            int level = guild.bo.getLnlevel();
            int exp = guild.bo.getLnexp();
            int donateTimes = player.getFeature(PlayerRecord.class).getValue(DailyRefresh.LongnvDonate);
            int crystalTimes = player.getFeature(PlayerRecord.class).getValue(DailyRefresh.LongnvCrystal);
            request.response(new LongnvSacrificeInfo.Sacrifice(level, exp, donateTimes, crystalTimes));
        }
    }

    public static class Request {
        LongnvSacrificeType type;
    }

    private static class Sacrifice {
        int level;
        int exp;
        int donateTimes;
        int crystalTimes;

        private Sacrifice(int level, int exp, int donateTimes, int crystalTimes) {
            this.level = level;
            this.exp = exp;
            this.donateTimes = donateTimes;
            this.crystalTimes = crystalTimes;
        }
    }
}
