package core.config.refdata.ref;

import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.mgr.daily.IDailyRefreshRef;
import com.zhonglian.server.common.mgr.daily.IDailyRefreshRef.DailyRefreshEventType;
import com.zhonglian.server.common.mgr.daily.IDailyRefreshRef.StartRefer;
import java.util.ArrayList;
import java.util.List;

public class RefDailyWorldRefresh extends RefBaseWorld implements IDailyRefreshRef {
    @RefField(
        iskey = true
    )
    public int Index;
    public String Comment;
    public StartRefer StartRefer;
    public int FirstSec;
    public int Interval;
    public DailyRefreshEventType EventTypes;
    public ArrayList<Integer> EventValues;

    public boolean checkFieldValue() {
        return false;
    }

    @Override
    public int getIndex() {
        return this.Index;
    }

    @Override
    public String getComment() {
        return this.Comment;
    }

    @Override
    public StartRefer getStartRefer() {
        return this.StartRefer;
    }

    @Override
    public int getFirstSec() {
        return this.FirstSec;
    }

    @Override
    public int getInterval() {
        return this.Interval;
    }

    @Override
    public DailyRefreshEventType getEventTypes() {
        return this.EventTypes;
    }

    @Override
    public List<Integer> getEventValue() {
        return this.EventValues;
    }

    @Override
    public boolean Assert() {
        return true;
    }

    @Override
    public boolean AssertAll(RefContainer<?> all) {
        return true;
    }
}
