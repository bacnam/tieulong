package BaseTask.AsynTask;

import BaseCommon.CommLog;

public class AsyncTaskDealThread
        extends Thread {
    private boolean _m_bThreadExit;
    private int _id = 0;
    private AsyncTaskQueue.AsyncThreadQueue taskManager;

    public AsyncTaskDealThread(AsyncTaskQueue.AsyncThreadQueue _mgr, int id) {
        this._m_bThreadExit = false;
        this.taskManager = _mgr;
        this._id = id;
        setName("ATT-" + _mgr.getTag() + "-" + this._id);
    }

    public void dispose() {
        this._m_bThreadExit = true;
    }

    public void run() {
        while (!this._m_bThreadExit) {

            AsyncTaskWrapper info = this.taskManager.popFirstAsynTask();

            if (info != null)
                try {
                    info.run();
                } catch (Throwable e) {
                    CommLog.error("AsyncTaskDealThread.run", e);
                }
        }
    }
}

