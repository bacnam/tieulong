package core.network.client2game.handler.longnv;

import business.global.guild.Guild;
import business.player.Player;
import business.player.feature.PlayerItem;
import business.player.feature.features.PlayerRecord;
import business.player.feature.guild.GuildMemberFeature;
import com.google.gson.Gson;
import com.zhonglian.server.common.enums.LongnvSacrificeType;
import com.zhonglian.server.common.utils.CommMath;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefCrystalPrice;
import core.config.refdata.ref.RefLongnvLevel;
import core.config.refdata.ref.RefLongnvSacrifice;
import core.network.client2game.handler.PlayerHandler;

import java.io.IOException;

public class LongnvSacrifice
        extends PlayerHandler {
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        Request req = (Request) (new Gson()).fromJson(message, Request.class);
        GuildMemberFeature guildMember = (GuildMemberFeature) player.getFeature(GuildMemberFeature.class);
        Guild guild = guildMember.getGuild();

        if (guild == null) {
            throw new WSException(ErrorCode.Guild_IndependentMan, "玩家[%s]未参与任何帮会", new Object[]{Long.valueOf(player.getPid())});
        }

        RefLongnvLevel nextLeveLinfo = (RefLongnvLevel) RefDataMgr.get(RefLongnvLevel.class, Integer.valueOf(guild.getLongnvLevel() + 1));
        if (nextLeveLinfo == null) {
            throw new WSException(ErrorCode.Guild_LevelMax, "龙女等级已满");
        }

        RefLongnvSacrifice ref = (RefLongnvSacrifice) RefDataMgr.get(RefLongnvSacrifice.class, req.type);

        PlayerRecord recorder = (PlayerRecord) player.getFeature(PlayerRecord.class);
        int times = recorder.getValue(ref.RefreshType);

        if (ref.Limit >= 0 && times >= ref.Limit) {
            throw new WSException(ErrorCode.Guild_SacrificeAlready, "玩家[%s]祭天次数已满", new Object[]{Long.valueOf(player.getPid())});
        }
        RefCrystalPrice prize = RefCrystalPrice.getPrize(times);
        int cost = 0;
        int basicexp = 0;
        if (ref.id == LongnvSacrificeType.Donate) {
            cost = prize.LongnvDonate;
            basicexp = ref.Exp;
        } else if (ref.id == LongnvSacrificeType.Crystal) {
            cost = prize.LongnvCrystal;
            basicexp = prize.LongnvCrystalExp;
        }

        PlayerItem playerItem = (PlayerItem) player.getFeature(PlayerItem.class);
        if (!playerItem.check(ref.CostItemID, cost)) {
            throw new WSException(ErrorCode.NotEnough_GuildSacrificeCost, "玩家[%s]资源不足，不能进行相关类型的祭天", new Object[]{Long.valueOf(player.getPid())});
        }
        playerItem.consume(ref.CostItemID, cost, ItemFlow.Guild_Sacrifice);

        boolean critical = (CommMath.randomInt(10000) < ref.Critical);
        int crivalue = critical ? ref.CriticalValue : 10000;

        int exp = basicexp * crivalue / 10000;

        guild.gainLongnvExp(exp);

        recorder.addValue(ref.RefreshType);

        Sacrifice sacrifice = new Sacrifice(critical, guild.bo.getLnexp(), exp, recorder.getValue(ref.RefreshType), req.type, null);

        guild.broadcast("Longnvsacrifice", Integer.valueOf(exp), player.getPid());
        request.response(sacrifice);
    }

    public static class Request {
        LongnvSacrificeType type;
    }

    private static class Sacrifice {
        boolean iscritical;
        int nowExp;
        int exp;
        int times;
        LongnvSacrificeType type;

        private Sacrifice(boolean iscritical, int nowExp, int exp, int times, LongnvSacrificeType type) {
            this.iscritical = iscritical;
            this.nowExp = nowExp;
            this.exp = exp;
            this.times = times;
            this.type = type;
        }
    }
}

