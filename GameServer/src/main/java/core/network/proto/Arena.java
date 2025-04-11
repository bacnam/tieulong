package core.network.proto;

import business.global.arena.Competitor;
import business.player.Player;
import business.player.PlayerMgr;
import business.player.feature.character.CharFeature;
import business.player.feature.features.RechargeFeature;
import com.zhonglian.server.common.enums.Achievement;
import core.database.game.bo.PlayerBO;

public class Arena {
    public static class CompetitorInfo
            extends Player.Summary {
        static PlayerMgr playerMgr;
        int rank;

        public CompetitorInfo(Competitor competitor) {
            if (playerMgr == null) {
                playerMgr = PlayerMgr.getInstance();
            }

            Player player = playerMgr.getPlayer(competitor.getPid());
            PlayerBO bo = player.getPlayerBO();
            this.pid = bo.getId();
            this.name = bo.getName();
            this.lv = bo.getLv();
            this.icon = bo.getIcon();
            this.vipLv = bo.getVipLevel();
            this.power = ((CharFeature) player.getFeature(CharFeature.class)).getPower();
            this.rank = competitor.getBo().getRank();

            RechargeFeature rechargeFeature = (RechargeFeature) player.getFeature(RechargeFeature.class);
            int monthNum = rechargeFeature.getRebateRemains(Achievement.AchievementType.MonthCardCrystal);
            this.MonthCard = (monthNum > 0);
            int yearNum = rechargeFeature.getRebateRemains(Achievement.AchievementType.YearCardCrystal);
            this.YearCard = (yearNum == -1);
        }
    }
}

