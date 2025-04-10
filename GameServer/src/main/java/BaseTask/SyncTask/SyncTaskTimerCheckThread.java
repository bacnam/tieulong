package BaseTask.SyncTask;

import java.util.Calendar;

public class SyncTaskTimerCheckThread
extends Thread
{
private boolean _m_bThreadExit;
private int _m_iCheckTime = 50;
private SyncTaskQueue parenTaskQueue;

public SyncTaskTimerCheckThread(SyncTaskQueue parent) {
this.parenTaskQueue = parent;
this._m_bThreadExit = false;
setName(this.parenTaskQueue.getTag());
}

public void dispose() {
this._m_bThreadExit = true;
}

public void run() {
this.parenTaskQueue._refreshTimerTaskNowTime();
long startTime = this.parenTaskQueue._getNowTime();
long now = Calendar.getInstance().getTimeInMillis();
while (!this._m_bThreadExit) {
if (Calendar.getInstance().getTimeInMillis() - now > 10000L) {
now = Calendar.getInstance().getTimeInMillis();
}

long nowTime = this.parenTaskQueue._refreshTimerTaskNowTime();

this.parenTaskQueue.transTimer2NormalList(startTime);

startTime = nowTime;

try {
sleep(this._m_iCheckTime);
} catch (InterruptedException interruptedException) {}
} 
}
}

