package BaseTask.SyncTask;

import BaseServer.Monitor;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class SyncTaskQueue {
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
        this.setDealerCount(Math.max(2, cpuCnt));
    }

    public synchronized void setDealerCount(int dealderCount) {
        int addCount;
        for (addCount = dealderCount - this.taskDealThread.size(); addCount > 0; addCount--) {
            SyncTaskDealThread newTaskDealThread = new SyncTaskDealThread(this, this.taskDealThread.size());
            newTaskDealThread.start();
            this.taskDealThread.add(newTaskDealThread);
        }

        while (addCount < 0 && this.taskDealThread.size() != 0) {
            SyncTaskDealThread toRemoveDealThread = this.taskDealThread.remove(this.taskDealThread.size() - 1);
            toRemoveDealThread.dispose();
            addCount++;
        }
    }

    public void dispose() {
        this.timerCheck.dispose();
        this.setDealerCount(0);
    }

    public String getTag() {
        return this.tag;
    }

    public void RegisterTask(SyncTask _task, String info) {
        this._lockNormalTaskList();
        this._m_lNormalTaskList.add(new SyncTaskWrapper(_task, 0L, info, this));
        this._releaseTaskEvent();
        this._unlockNormalTaskList();
        if (this._m_lNormalTaskList.size() > 10000) {
            Monitor.getInstance().regLog();
        }
    }

    public void RegisterTask(SyncTask _task) {
        this.RegisterTask(_task, "");
    }

    public void RegisterTask(SyncTask _task, int _time) {
        if (_time <= 0) {
            this.RegisterTask(_task);
        } else {
            this._lockTimerTaskList();
            this._addTimerTask(_time, _task, "");
            this._unlockTimerTaskList();
        }
    }

    public void RegisterTask(SyncTask _task, int _time, String info) {
        this._lockTimerTaskList();
        this._addTimerTask(_time, _task, info);
        this._unlockTimerTaskList();
    }

    public void RegisterTask(SyncTask _task, long _time) {
        this._lockTimerTaskList();
        this._addTimerTask(_time, _task, "");
        this._unlockTimerTaskList();
    }

    public void RegisterTask(SyncTask _task, long _time, String info) {
        this._lockTimerTaskList();
        this._addTimerTask(_time, _task, info);
        this._unlockTimerTaskList();
    }

    public SyncTaskWrapper PopTask() {
        this._acquireTaskEvent();
        this._lockNormalTaskList();
        if (this._m_lNormalTaskList.isEmpty()) {
            this._unlockNormalTaskList();
            return null;
        } else {
            SyncTaskWrapper task = this._m_lNormalTaskList.removeFirst();
            this._unlockNormalTaskList();
            return task;
        }
    }

    public void transTimer2NormalList(long _startTime) {
        ArrayList<SyncTaskWrapper> needAddTaskList = this._popTimerTask(_startTime);
        if (needAddTaskList != null && !needAddTaskList.isEmpty()) {
            this.RegisterTaskList(needAddTaskList);
        }
    }

    private void RegisterTaskList(ArrayList<SyncTaskWrapper> _taskList) {
        this._lockNormalTaskList();
        int taskCount = _taskList.size();
        this._m_lNormalTaskList.addAll(_taskList);
        this._releaseTaskEvent(taskCount);
        this._unlockNormalTaskList();
    }

    protected long _getNowTime() {
        return this._m_lNowTime;
    }

    protected long _refreshTimerTaskNowTime() {
        long nowTime = new Date().getTime();
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

    @Override
    public String toString() {
        return String.format("%-20s%-20s%-20s%-20s\n", this.tag, this.taskDealThread.size(), this.getNormalTaskSize(), this.getTimerTaskSize());
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
        long _dealTime = this._getNowTime() + _waitTime;
        int deltaTime = (int)(_dealTime % this._m_iTimerTaskCheckTime);
        long realDealTime = _dealTime;
        if (deltaTime != 0) {
            realDealTime = _dealTime - deltaTime + this._m_iTimerTaskCheckTime;
        }

        this._lockTimerTaskList();
        ArrayList<SyncTaskWrapper> taskList = this._m_htTimerTaskTable.get(realDealTime);
        if (taskList == null) {
            taskList = new ArrayList<>();
            this._m_htTimerTaskTable.put(realDealTime, taskList);
        }

        taskList.add(new SyncTaskWrapper(_task, _waitTime, info, this));
        this._unlockTimerTaskList();
    }

    protected ArrayList<SyncTaskWrapper> _popTimerTask(long _startTime) {
        ArrayList<SyncTaskWrapper> list = new ArrayList<>();
        this._lockTimerTaskList();
        long endTime = this._getNowTime();

        for (long time = _startTime; time <= endTime; time += this._m_iTimerTaskCheckTime) {
            ArrayList<SyncTaskWrapper> tmpList = this._m_htTimerTaskTable.remove(time);
            if (tmpList != null) {
                list.addAll(tmpList);
            }
        }

        this._unlockTimerTaskList();
        return list;
    }
}
