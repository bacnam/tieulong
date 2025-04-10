package ch.qos.logback.core.status;

import java.util.ArrayList;
import java.util.List;

public class StatusListenerAsList
implements StatusListener
{
List<Status> statusList = new ArrayList<Status>();

public void addStatusEvent(Status status) {
this.statusList.add(status);
}

public List<Status> getStatusList() {
return this.statusList;
}
}

