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
    private Map<Integer, Fight> fights = new HashMap<>();

    public static FightManager getInstance() {
        if (instacne == null) {
            instacne = new FightManager();
        }

        return instacne;
    }

    private FightManager() {
        SyncTaskManager.schedule(3000, () -> {
            this.checkFightOverTime();
            return true;
        });
    }

    public int pushFight(Fight fight) {
        this.fights.put(fight.fightId, fight);
        return fight.fightId;
    }

    public Fight popFight(int fightid) {
        synchronized (this.fights) {
            return this.fights.remove(fightid);
        }
    }

    private void checkFightOverTime() {
        if (this.fights.size() > 0) {
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
                for (Fight fightx : overtime) {
                    this.fights.remove(fightx.fightId);
                    fightx.settle(FightResult.Lost);
                }
            }
        }
    }

    public void removeFight(Fight fight) {
        this.fights.remove(fight.fightId);
    }
}
