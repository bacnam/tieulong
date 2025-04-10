package proto.gameworld;

import com.zhonglian.server.common.enums.ActivityStatus;
import com.zhonglian.server.common.enums.ActivityType;

public class ActivityInfo
{
public ActivityType type;
public ActivityStatus status;
public int beginTime;
public int endTime;
public int closeTime;

public ActivityType getType() {
return this.type;
}

public void setType(ActivityType type) {
this.type = type;
}

public ActivityStatus getStatus() {
return this.status;
}

public void setStatus(ActivityStatus status) {
this.status = status;
}

public int getBeginTime() {
return this.beginTime;
}

public void setBeginTime(int beginTime) {
this.beginTime = beginTime;
}

public int getEndTime() {
return this.endTime;
}

public void setEndTime(int endTime) {
this.endTime = endTime;
}

public int getCloseTime() {
return this.closeTime;
}

public void setCloseTime(int closeTime) {
this.closeTime = closeTime;
}
}

