package business.global.refresh;

import com.zhonglian.server.common.mgr.daily.BaseDailyRefreshEvent;
import com.zhonglian.server.common.mgr.daily.IDailyRefreshRef;

public class WorldDailyRefresh extends BaseDailyRefreshEvent {
    public WorldDailyRefresh(IDailyRefreshRef ref, WorldDailyRefreshContainer container) {
        super(ref, container);
    }

    @Override
    public void doEvent(int times) {
        switch (this.ref.getEventTypes()) {
            case Day_h0:
            case Day_h10:
            case Week_d1h0:
            case Every_m5:
            case Every_m15:
        }
    }
}
