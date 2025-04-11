package business.global.arena;

import com.zhonglian.server.common.utils.CommTime;
import core.database.game.bo.ArenaCompetitorBO;

import java.util.List;

public class Competitor {
    ArenaCompetitorBO bo;
    List<Competitor> opponents = null;
    int refreshTime = 0;
    int lastFightTime = 0;

    public Competitor(ArenaCompetitorBO bo) {
        this.bo = bo;
    }

    public ArenaCompetitorBO getBo() {
        return this.bo;
    }

    public List<Competitor> getOpponents() {
        if (this.opponents == null) {
            this.opponents = ArenaManager.getInstance().getOpponents(this.bo.getRank());
        }
        return this.opponents;
    }

    public int getRefreshCD() {
        return Math.max(this.refreshTime + ArenaConfig.refreshCD() - CommTime.nowSecond(), 0);
    }

    public void setRefreshCD(int cd) {
        this.refreshTime = CommTime.nowSecond() - ArenaConfig.refreshCD() + cd;
    }

    public long getPid() {
        return this.bo.getPid();
    }

    public Competitor getOpponent(long targetPid) {
        if (this.opponents == null) {
            return null;
        }
        for (Competitor opp : this.opponents) {
            if (opp.getPid() == targetPid) {
                return opp;
            }
        }
        return null;
    }

    public int getFightCD() {
        return Math.max(this.lastFightTime + ArenaConfig.fightCD() - CommTime.nowSecond(), 0);
    }

    public void setFightCD(int cd) {
        this.lastFightTime = CommTime.nowSecond() - ArenaConfig.fightCD() + cd;
    }

    public int getRank() {
        return this.bo.getRank();
    }

    public void resetOpponents() {
        this.opponents = null;
    }
}

