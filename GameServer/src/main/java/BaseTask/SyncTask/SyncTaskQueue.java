package BaseTask.SyncTask;

import BaseServer.Monitor;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class SyncTaskQueue
{
private String tag;
private LinkedList<SyncTaskWrapper> _m_lNormalTaskList;
private ReentrantLock _m_lNormalTaskMutex;
private List<SyncTaskDealThread> taskDealThread = new ArrayList<>();

private SyncTaskTimerCheckThread timerCheck;

private HashMap<Long, ArrayList<SyncTaskWrapper>> _m_htTimerTaskTable;

private ReentrantLock _m_lTimerTaskMutex;

private int _m_iTimerTaskCheckTime = 50;

private long _m_lNowTime;

private Semaphore _m_sTaskEvent;

public SyncTaskQueue(String tag) {
this.tag = tag;

this._m_lNormalTaskList = new LinkedList<>();
this._m_lNormalTaskMutex = new ReentrantLock();
this._m_htTimerTaskTable = new HashMap<>();
this._m_lTimerTaskMutex = new ReentrantLock();
this._m_sTaskEvent = new Semaphore(0);

this.timerCheck = new SyncTaskTimerCheckThread(this);
this.timerCheck.start();

int cpuCnt = Runtime.getRuntime().availableProcessors();
setDealerCount(Math.max(2, cpuCnt));
}

public synchronized void setDealerCount(int dealderCount) {
int addCount = dealderCount - this.taskDealThread.size();

while (addCount > 0) {

SyncTaskDealThread newTaskDealThread = new SyncTaskDealThread(this, this.taskDealThread.size());
newTaskDealThread.start();
this.taskDealThread.add(newTaskDealThread);

addCount--;
} 

while (addCount < 0 && 
this.taskDealThread.size() != 0) {

SyncTaskDealThread toRemoveDealThread = this.taskDealThread.remove(this.taskDealThread.size() - 1);
toRemoveDealThread.dispose();

addCount++;
} 
}

public void dispose() {
this.timerCheck.dispose();
setDealerCount(0);
}

public String getTag() {
return this.tag;
}

public void RegisterTask(SyncTask _task, String info) {
_lockNormalTaskList();

this._m_lNormalTaskList.add(new SyncTaskWrapper(_task, 0L, info, this));

_releaseTaskEvent();

_unlockNormalTaskList();

if (this._m_lNormalTaskList.size() > 10000) {
Monitor.getInstance().regLog();
}
}

public void RegisterTask(SyncTask _task) {
RegisterTask(_task, "");
}

public void RegisterTask(SyncTask _task, int _time) {
if (_time <= 0) {
RegisterTask(_task);

return;
} 
_lockTimerTaskList();

_addTimerTask(_time, _task, "");

_unlockTimerTaskList();
}

public void RegisterTask(SyncTask _task, int _time, String info) {
_lockTimerTaskList();

_addTimerTask(_time, _task, info);

_unlockTimerTaskList();
}

public void RegisterTask(SyncTask _task, long _time) {
_lockTimerTaskList();

_addTimerTask(_time, _task, "");

_unlockTimerTaskList();
}

public void RegisterTask(SyncTask _task, long _time, String info) {
_lockTimerTaskList();

_addTimerTask(_time, _task, info);

_unlockTimerTaskList();
}

public SyncTaskWrapper PopTask() {
_acquireTaskEvent();
_lockNormalTaskList();

if (this._m_lNormalTaskList.isEmpty()) {
_unlockNormalTaskList();
return null;
} 

SyncTaskWrapper task = this._m_lNormalTaskList.removeFirst();

_unlockNormalTaskList();
return task;
}

public void transTimer2NormalList(long _startTime) {
ArrayList<SyncTaskWrapper> needAddTaskList = _popTimerTask(_startTime);
if (needAddTaskList != null && !needAddTaskList.isEmpty())
{
RegisterTaskList(needAddTaskList);
}
}

private void RegisterTaskList(ArrayList<SyncTaskWrapper> _taskList) {
_lockNormalTaskList();

int taskCount = _taskList.size();
this._m_lNormalTaskList.addAll(_taskList);

_releaseTaskEvent(taskCount);

_unlockNormalTaskList();
}

protected long _getNowTime() {
return this._m_lNowTime;
}

protected long _refreshTimerTaskNowTime() {
long nowTime = (new Date()).getTime();

int deltaTime = (int)(nowTime % this._m_iTimerTaskCheckTime);
this._m_lNowTime = nowTime - deltaTime;

return this._m_lNowTime;
}

protected void _lockNormalTaskList() {
this._m_lNormalTaskMutex.lock();
}

protected void _unlockNormalTaskList() {
this._m_lNormalTaskMutex.unlock();
}

protected void _lockTimerTaskList() {
this._m_lTimerTaskMutex.lock();
}

protected void _unlockTimerTaskList() {
this._m_lTimerTaskMutex.unlock();
}

protected void _releaseTaskEvent() {
this._m_sTaskEvent.release();
}

protected void _releaseTaskEvent(int count) {
this._m_sTaskEvent.release(count);
}

protected void _acquireTaskEvent() {
this._m_sTaskEvent.acquireUninterruptibly();
}

public String toString() {
return String.format("%-20s%-20s%-20s%-20s\n", new Object[] { this.tag, Integer.valueOf(this.taskDealThread.size()), Integer.valueOf(getNormalTaskSize()), Integer.valueOf(getTimerTaskSize()) });
}

public int getTimerTaskSize() {
int ret = 0;
for (ArrayList<SyncTaskWrapper> cnt : this._m_htTimerTaskTable.values()) {
ret += cnt.size();
}
return ret;
}

public int getNormalTaskSize() {
return this._m_lNormalTaskList.size();
}

protected void _addTimerTask(long _waitTime, SyncTask _task, String info) {
long _dealTime = _getNowTime() + _waitTime;

int deltaTime = (int)(_dealTime % this._m_iTimerTaskCheckTime);

long realDealTime = _dealTime;
if (deltaTime != 0) {
realDealTime = _dealTime - deltaTime + this._m_iTimerTaskCheckTime;
}
_lockTimerTaskList();

ArrayList<SyncTaskWrapper> taskList = this._m_htTimerTaskTable.get(Long.valueOf(realDealTime));
if (taskList == null) {
taskList = new ArrayList<>();
this._m_htTimerTaskTable.put(Long.valueOf(realDealTime), taskList);
} 

taskList.add(new SyncTaskWrapper(_task, _waitTime, info, this));

_unlockTimerTaskList();
}

protected ArrayList<SyncTaskWrapper> _popTimerTask(long _startTime) {
ArrayList<SyncTaskWrapper> list = new ArrayList<>();

_lockTimerTaskList();

long endTime = _getNowTime();

long time = _startTime;
while (time <= endTime) {
ArrayList<SyncTaskWrapper> tmpList = this._m_htTimerTaskTable.remove(Long.valueOf(time));
if (tmpList != null) {
list.addAll(tmpList);
}
time += this._m_iTimerTaskCheckTime;
} 

_unlockTimerTaskList();

return list;
}
}

