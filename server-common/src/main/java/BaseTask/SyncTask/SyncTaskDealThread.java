package BaseTask.SyncTask;

import BaseCommon.CommLog;
import BaseThread.ThreadManager;
import BaseThread.ThreadMutexInfo;

public class SyncTaskDealThread extends Thread {
    private ThreadMutexInfo _m_tmiThreadMutexInfo;
    private boolean _m_bThreadExit;
    private SyncTaskQueue taskManager;
    private int _id = 0;

    public SyncTaskDealThread(SyncTaskQueue _mgr, int id) {
        this._m_tmiThreadMutexInfo = null;
        this._m_bThreadExit = false;
        this.taskManager = _mgr;
        this._id = id;
        this.setName("STT-" + _mgr.getTag() + "-" + this._id);
    }

    public void dispose() {
        this._m_bThreadExit = true;
    }

    @Override
    public void run() {
        if (ThreadManager.getInstance().getCheckDeadLock()) {
            long threadID = Thread.currentThread().getId();
            this._m_tmiThreadMutexInfo = ThreadManager.getInstance().regThread(threadID);
            if (this._m_tmiThreadMutexInfo == null) {
                return;
            }
        }

        while (!this._m_bThreadExit) {
            SyncTaskWrapper curTask = this.taskManager.PopTask();
            if (curTask != null) {
                try {
                    curTask.run();
                } catch (Exception var3) {
                    CommLog.error(curTask.getClass().getName() + " Error!!", (Throwable)var3);
                    if (ThreadManager.getInstance().getCheckDeadLock()) {
                        this._m_tmiThreadMutexInfo.releaseAllMutex();
                    }
                }

                if (ThreadManager.getInstance().getCheckDeadLock() && !this._m_tmiThreadMutexInfo.judgeAllMutexRelease()) {
                    CommLog.error(curTask.getClass().getName() + " Still get some mutexs are not released, info:" + curTask.getInfo());
                    this._m_tmiThreadMutexInfo.releaseAllMutex();
                }
            }
        }
    }
}
