package business.global.fight;

import BaseTask.SyncTask.SyncTaskManager;
import com.zhonglian.server.common.enums.FightResult;
import com.zhonglian.server.common.utils.CommTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FightManager {
    private static FightManager instacne = null;
    private Map<Integer, Fight> fights;

    private FightManager() {
        this.fights = new HashMap<>();

        SyncTaskManager.schedule(3000, () -> {
            checkFightOverTime();
            return true;
        });
    }

    public static FightManager getInstance() {
        if (instacne == null) {
            instacne = new FightManager();
        }
        return instacne;
    }

    public int pushFight(Fight fight) {
        this.fights.put(Integer.valueOf(fight.fightId), fight);
        return fight.fightId;
    }

    public Fight popFight(int fightid) {
        synchronized (this.fights) {
            return this.fights.remove(Integer.valueOf(fightid));
        }
    }

    private void checkFightOverTime() {
        if (this.fights.size() <= 0) {
            return;
        }
        List<Fight> list = new ArrayList<>();
        synchronized (this.fights) {
            list.addAll(this.fights.values());
        }

        List<Fight> overtime = new ArrayList<>();

        int cur = CommTime.nowSecond();
        for (Fight fight : list) {
            int fightTime = fight.fightTime();
            if (cur > fight.beginTime + fightTime) {
                overtime.add(fight);
            }
        }

        synchronized (this.fights) {
            for (Fight fight : overtime) {
                this.fights.remove(Integer.valueOf(fight.fightId));
                fight.settle(FightResult.Lost);
            }
        }
    }

    public void removeFight(Fight fight) {
        this.fights.remove(Integer.valueOf(fight.fightId));
    }
}

