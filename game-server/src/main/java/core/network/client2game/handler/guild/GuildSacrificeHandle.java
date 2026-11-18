package core.network.client2game.handler.guild;

import business.global.guild.Guild;
import business.player.Player;
import business.player.feature.PlayerItem;
import business.player.feature.guild.GuildMemberFeature;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.SacrificeType;
import com.zhonglian.server.common.utils.CommMath;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefGuildSacrifice;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class GuildSacrificeHandle extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        GuildSacrificeHandle.Request req = new Gson().fromJson(message, GuildSacrificeHandle.Request.class);
        GuildMemberFeature guildMember = player.getFeature(GuildMemberFeature.class);
        Guild guild = guildMember.getGuild();
        if (guild == null) {
            throw new WSException(ErrorCode.Guild_IndependentMan, "玩家[%s]未参与任何帮会", player.getPid());
        } else if (guildMember.getSacrificeLeftTimes() <= 0) {
            throw new WSException(ErrorCode.Guild_SacrificeAlready, "玩家[%s]祭天次数已满", player.getPid());
        } else {
            RefGuildSacrifice ref = RefDataMgr.get(RefGuildSacrifice.class, req.type);
            PlayerItem playerItem = player.getFeature(PlayerItem.class);
            if (!playerItem.check(ref.CostItemID, ref.CostCount)) {
                throw new WSException(ErrorCode.NotEnough_GuildSacrificeCost, "玩家[%s]资源不足，不能进行相关类型的祭天", player.getPid());
            } else {
                playerItem.consume(ref.CostItemID, ref.CostCount, ItemFlow.Guild_Sacrifice);
                boolean critical = CommMath.randomInt(10000) < ref.Critical;
                int crivalue = critical ? ref.CriticalValue : 10000;
                int exp = ref.GuildExp * crivalue / 10000;
                int donate = ref.GuildDonate * crivalue / 10000;
                guild.gainExp(exp);
                guild.incSacrificeCount(player.getPid());
                guildMember.setSacrificed();
                guildMember.gainDonate(donate, ItemFlow.Guild_GuildSacrifice);
                guildMember.gainSacriDonate(donate);
                GuildSacrificeHandle.Sacrifice sacrifice = new GuildSacrificeHandle.Sacrifice(
                    critical, donate, exp, guild.bo.getExp(), guildMember.getSacrificeLeftTimes());
                guild.broadcast("sacrifice", sacrifice, player.getPid());
                request.response(sacrifice);
            }
        }
    }

    public static class Request {
        SacrificeType type;
    }

    private static class Sacrifice {
        boolean iscritical;
        int donate;
        int exp;
        int totalexp;
        int leftTimes;

        private Sacrifice(boolean iscritical, int donate, int exp, int totalexp, int leftTimes) {
            this.iscritical = iscritical;
            this.donate = donate;
            this.exp = exp;
            this.totalexp = totalexp;
            this.leftTimes = leftTimes;
        }
    }
}
