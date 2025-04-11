package business.global.guild;

import business.player.Player;
import com.zhonglian.server.common.data.ref.matcher.NumberRange;
import com.zhonglian.server.common.enums.UnlockType;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefGuildSkillLevel;
import core.config.refdata.ref.RefUnlockFunction;
import core.config.refdata.ref.RefVIP;

public class GuildConfig {
    public static int JoinCD() {
        return RefDataMgr.getFactor("Guild_JoinCD", 10800);
    }

    public static int FuncCD() {
        return RefDataMgr.getFactor("Guild_FuncCD", 172800);
    }

    public static int UnlockLevel() {
        return ((RefUnlockFunction) RefDataMgr.get(RefUnlockFunction.class, UnlockType.Guild)).UnlockLevel;
    }

    public static int BoardCD() {
        return RefDataMgr.getFactor("GuildMessageInterval", 5);
    }

    public static int MaxBoardMessageCount() {
        return RefDataMgr.getFactor("Guild_MaxBoardMessageCount", 30);
    }

    public static int MaxLogCount() {
        return RefDataMgr.getFactor("Guild_MaxLogCount", 100);
    }

    public static String VicePresidentNotice() {
        return RefDataMgr.getGeneral("Guild_VicePresidentNotice", "我任命{0}为副帮主，请大家多多支持TA！");
    }

    public static int JoinGuildMailID() {
        return RefDataMgr.getFactor("Guild_JoninGuildMailID", 500001);
    }

    public static int KickoutGuildMailID() {
        return RefDataMgr.getFactor("Guild_JoninGuildMailID", 500002);
    }

    public static int ChangeJobGuildMailID() {
        return RefDataMgr.getFactor("Guild_JoninGuildMailID", 500003);
    }

    public static RefGuildSkillLevel getGuildSkillLevel(int skillId, int level) {
        for (RefGuildSkillLevel ref : RefDataMgr.getAll(RefGuildSkillLevel.class).values()) {
            if (ref.SkillID == skillId && ref.SkillLevel == level) {
                return ref;
            }
        }
        return null;
    }

    public static int getSkillMaxLevel(int skillId, int level) {
        int i = 0;
        for (RefGuildSkillLevel ref : RefDataMgr.getAll(RefGuildSkillLevel.class).values()) {
            if (ref.NeedGuildLevel <= level && ref.SkillID == skillId) {
                i++;
            }
        }
        return i - 1;
    }

    public static NumberRange getGuildBossOpenTime() {
        int from = RefDataMgr.getFactor("GuildBossBeginTime", 10);
        int to = RefDataMgr.getFactor("GuildBossEndTime", 22);
        return new NumberRange(from, to);
    }

    public static int getGuildBossMaxTime() {
        return RefDataMgr.getFactor("GuildBossChallengeTimes", 2);
    }

    public static int getGuildBossMaxBuyTime(Player player) {
        return ((RefVIP) RefDataMgr.get(RefVIP.class, Integer.valueOf(player.getVipLevel()))).GuildChallengeBuyTimes;
    }

    public static int getGuildJoinListSize() {
        return RefDataMgr.getFactor("GuildJoinListSize", 20);
    }

    public static int getGuildJoinTimeLimit() {
        return RefDataMgr.getFactor("GuildJoinTimeLimit", 10800);
    }
}

