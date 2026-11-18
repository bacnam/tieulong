package core.network.client2game.handler.guild;

import business.global.guild.Guild;
import business.player.Player;
import business.player.feature.PlayerCurrency;
import business.player.feature.features.PlayerRecord;
import business.player.feature.guild.GuildMemberFeature;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.common.enums.SacrificeType;
import com.zhonglian.server.common.enums.ConstEnum.DailyRefresh;
import com.zhonglian.server.common.utils.CommMath;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefCrystalPrice;
import core.config.refdata.ref.RefGuildSacrifice;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;

public class GuildSacrificeCrystal extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        GuildMemberFeature guildMember = player.getFeature(GuildMemberFeature.class);
        Guild guild = guildMember.getGuild();
        if (guild == null) {
            throw new WSException(ErrorCode.Guild_IndependentMan, "玩家[%s]未参与任何帮会", player.getPid());
        } else {
            PlayerCurrency currency = player.getFeature(PlayerCurrency.class);
            PlayerRecord recorder = player.getFeature(PlayerRecord.class);
            int times = recorder.getValue(DailyRefresh.GuildSacrifice);
            RefCrystalPrice prize = RefCrystalPrice.getPrize(times);
            if (!currency.check(PrizeType.Crystal, prize.SacrificeCost)) {
                throw new WSException(ErrorCode.NotEnough_Crystal, "玩家第%s次祭天需要钻石%s", times, prize.ArenaResetRefreshCD);
            } else {
                currency.consume(PrizeType.Crystal, prize.SacrificeCost, ItemFlow.Guild_GuildSacrifice);
                RefGuildSacrifice ref = RefDataMgr.get(RefGuildSacrifice.class, SacrificeType.Crystal);
                boolean critical = CommMath.randomInt(10000) < ref.Critical;
                int crivalue = critical ? ref.CriticalValue : 10000;
                int exp = prize.SacrificeExp * crivalue / 10000;
                int donate = prize.SacrificeDonate * crivalue / 10000;
                int gain = guild.gainExp(exp);
                guild.incSacrificeCount(player.getPid());
                recorder.addValue(DailyRefresh.GuildSacrifice);
                guildMember.gainDonate(donate, ItemFlow.Guild_GuildSacrifice);
                guildMember.gainSacriDonate(donate);
                GuildSacrificeCrystal.Sacrifice sacrifice = new GuildSacrificeCrystal.Sacrifice(
                    critical, donate, gain, guild.bo.getExp(), recorder.getValue(DailyRefresh.GuildSacrifice));
                guild.broadcast("sacrifice", sacrifice, player.getPid());
                request.response(sacrifice);
            }
        }
    }

    private static class Sacrifice {
        boolean iscritical;
        int donate;
        int exp;
        int totalexp;
        int sacrificeTimes;

        private Sacrifice(boolean iscritical, int donate, int exp, int totalexp, int sacrificeTimes) {
            this.iscritical = iscritical;
            this.donate = donate;
            this.exp = exp;
            this.totalexp = totalexp;
            this.sacrificeTimes = sacrificeTimes;
        }
    }
}
