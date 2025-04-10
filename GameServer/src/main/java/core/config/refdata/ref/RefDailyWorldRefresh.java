package core.config.refdata.ref;

import com.zhonglian.server.common.data.RefContainer;
import com.zhonglian.server.common.data.RefField;
import com.zhonglian.server.common.mgr.daily.IDailyRefreshRef;
import java.util.ArrayList;
import java.util.List;

public class RefDailyWorldRefresh
extends RefBaseGame
implements IDailyRefreshRef
{
@RefField(iskey = true)
public int Index;
public String Comment;
public IDailyRefreshRef.StartRefer StartRefer;
public int FirstSec;
public int Interval;
public IDailyRefreshRef.DailyRefreshEventType EventTypes;
public ArrayList<Integer> EventValues;

public boolean checkFieldValue() {
return false;
}

public int getIndex() {
return this.Index;
}

public String getComment() {
return this.Comment;
}

public IDailyRefreshRef.StartRefer getStartRefer() {
return this.StartRefer;
}

public int getFirstSec() {
return this.FirstSec;
}

public int getInterval() {
return this.Interval;
}

public IDailyRefreshRef.DailyRefreshEventType getEventTypes() {
return this.EventTypes;
}

public List<Integer> getEventValue() {
return this.EventValues;
}

public boolean Assert() {
return true;
}

public boolean AssertAll(RefContainer<?> all) {
return true;
}
}

