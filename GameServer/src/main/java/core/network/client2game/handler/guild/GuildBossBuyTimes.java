package core.network.client2game.handler.guild;

import business.global.guild.Guild;
import business.global.guild.GuildConfig;
import business.player.Player;
import business.player.feature.PlayerCurrency;
import business.player.feature.guild.GuildMemberFeature;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.ref.RefCrystalPrice;
import core.network.client2game.handler.PlayerHandler;

import java.io.IOException;

public class GuildBossBuyTimes
        extends PlayerHandler {
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        Request req = (Request) (new Gson()).fromJson(message, Request.class);
        GuildMemberFeature guildMember = (GuildMemberFeature) player.getFeature(GuildMemberFeature.class);
        Guild guild = guildMember.getGuild();
        if (guild == null) {
            throw new WSException(ErrorCode.Guild_IndependentMan, "玩家[%s]未参与任何帮会", new Object[]{Long.valueOf(player.getPid())});
        }
        if (guildMember.getOrCreateChalleng().getChallengeBuyTimes() + req.times > GuildConfig.getGuildBossMaxBuyTime(player)) {
            throw new WSException(ErrorCode.GuildBoss_NotChallengeBuyTimes, "玩家购买次数不足");
        }

        int finalcount = 0;
        int times = guildMember.getOrCreateChalleng().getChallengeBuyTimes();
        for (int i = 0; i < req.times; i++) {
            RefCrystalPrice prize = RefCrystalPrice.getPrize(times + i);
            finalcount += prize.GuildBossBuyChallenge;
        }

        PlayerCurrency playerCurrency = (PlayerCurrency) player.getFeature(PlayerCurrency.class);
        if (!playerCurrency.checkAndConsume(PrizeType.Crystal, finalcount, ItemFlow.Guild_BuyTimes)) {
            throw new WSException(ErrorCode.NotEnough_Crystal, "玩家元宝不足");
        }
        guildMember.getOrCreateChalleng().saveChallengeMaxTimes(guildMember.getOrCreateChalleng().getChallengeMaxTimes() + req.times);
        guildMember.getOrCreateChalleng().saveChallengeBuyTimes(guildMember.getOrCreateChalleng().getChallengeBuyTimes() + req.times);
        request.response(new Response(guildMember.getOrCreateChalleng().getChallengeMaxTimes(), guildMember.getOrCreateChalleng().getChallengeBuyTimes(), null));
    }

    public static class Request {
        int times;
    }

    private static class Response {
        int leftTimes;
        int leftBuyTimes;

        private Response(int leftTimes, int leftBuyTimes) {
            this.leftTimes = leftTimes;
            this.leftBuyTimes = leftBuyTimes;
        }
    }
}

