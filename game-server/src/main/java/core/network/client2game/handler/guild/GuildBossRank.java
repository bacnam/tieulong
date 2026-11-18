package core.network.client2game.handler.guild;

import business.global.guild.Guild;
import business.global.guild.GuildRecord;
import business.player.Player;
import business.player.PlayerMgr;
import business.player.feature.character.CharFeature;
import business.player.feature.features.RechargeFeature;
import business.player.feature.guild.GuildMemberFeature;
import com.zhonglian.server.common.enums.GuildRankType;
import com.zhonglian.server.common.enums.Achievement.AchievementType;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.requset.WebSocketRequest;
import core.database.game.bo.PlayerBO;
import core.network.client2game.handler.PlayerHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuildBossRank extends PlayerHandler {
    @Override
    public void handle(Player player, WebSocketRequest request, String message) throws WSException, IOException {
        GuildMemberFeature guildMember = player.getFeature(GuildMemberFeature.class);
        Guild guild = guildMember.getGuild();
        if (guild == null) {
            throw new WSException(ErrorCode.Guild_IndependentMan, "玩家[%s]未参与任何帮会", player.getPid());
        } else {
            List<GuildRecord> records = guild.getRankList(GuildRankType.GuildBoss, 30);
            int rank = guild.getRank(GuildRankType.GuildBoss, player.getPid());
            long damage = guild.getValue(GuildRankType.GuildBoss, player.getPid());
            int times = guildMember.getOrCreateChalleng().getAttackTimes();
            request.response(new GuildBossRank.Response(rank, damage, times, records));
        }
    }

    public static class RankInfo extends core.network.proto.Player.Summary {
        static PlayerMgr playerMgr;
        int rank;
        long value;
        int attacktimes;

        public RankInfo(GuildRecord record) {
            if (playerMgr == null) {
                playerMgr = PlayerMgr.getInstance();
            }

            Player player = playerMgr.getPlayer(record.getPid());
            PlayerBO bo = player.getPlayerBO();
            this.pid = bo.getId();
            this.name = bo.getName();
            this.lv = bo.getLv();
            this.icon = bo.getIcon();
            this.vipLv = bo.getVipLevel();
            this.power = player.getFeature(CharFeature.class).getPower();
            this.rank = record.getRank();
            this.value = record.getValue();
            this.attacktimes = player.getFeature(GuildMemberFeature.class).getOrCreateChalleng().getAttackTimes();
            RechargeFeature rechargeFeature = player.getFeature(RechargeFeature.class);
            int monthNum = rechargeFeature.getRebateRemains(AchievementType.MonthCardCrystal);
            this.MonthCard = monthNum > 0;
            int yearNum = rechargeFeature.getRebateRemains(AchievementType.YearCardCrystal);
            this.YearCard = yearNum == -1;
        }
    }

    private static class Response {
        int rank;
        long damage;
        int attacktimes;
        List<GuildBossRank.RankInfo> rankList;

        public Response(int rank, long damage, int attacktimes, List<GuildRecord> records) {
            this.rank = rank;
            this.damage = damage;
            this.attacktimes = attacktimes;
            this.rankList = new ArrayList<>();

            for (int i = 0; i < records.size(); i++) {
                GuildRecord r = records.get(i);
                if (r != null) {
                    this.rankList.add(new GuildBossRank.RankInfo(r));
                }
            }
        }
    }
}
