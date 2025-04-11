package business.global.guild;

import business.player.Player;
import com.zhonglian.server.common.data.ref.matcher.NumberRange;
import com.zhonglian.server.common.utils.CommTime;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefGuildWarCenter;
import core.database.game.bo.PlayerBO;

public class GuildWarConfig {
    public static int overTime() {
        return RefDataMgr.getFactor("GuildWar_OverTime", 60);
    }

    public static int fightTime() {
        return RefDataMgr.getFactor("GuildWar_FightTime", 1800);
    }

    public static int restTime() {
        return RefDataMgr.getFactor("GuildWar_RestTime", 600);
    }

    public static int oneFightTime() {
        return RefDataMgr.getFactor("GuildWar_OneFight", 30000);
    }

    public static int winRewardMailId(int centerId) {
        RefGuildWarCenter ref = (RefGuildWarCenter) RefDataMgr.get(RefGuildWarCenter.class, Integer.valueOf(centerId));
        return ref.MailId;
    }

    public static int rebirthCD() {
        return RefDataMgr.getFactor("GuildWar_rebirthCD", 3);
    }

    public static boolean applyTime() {
        int begin = RefDataMgr.getFactor("GuildWar_ApplyBegin", 12);
        int end = RefDataMgr.getFactor("GuildWar_ApplyEnd", 13);
        NumberRange range = new NumberRange(begin, end);
        return range.within(CommTime.getTodayHour());
    }

    public static class puppetPlayer extends Player {
        int puppet_id;
        boolean is_puppet;

        public puppetPlayer(PlayerBO playerBO) {
            super(playerBO);
        }

        public int getPuppet_id() {
            return this.puppet_id;
        }

        public void setPuppet_id(int puppet_id) {
            this.puppet_id = puppet_id;
        }

        public boolean isIs_puppet() {
            return this.is_puppet;
        }

        public void setIs_puppet(boolean is_puppet) {
            this.is_puppet = is_puppet;
        }
    }
}

