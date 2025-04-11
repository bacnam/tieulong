package BaseThread;

import BaseCommon.CommLog;

import java.util.Hashtable;

public class ThreadManager {
    private static ThreadManager g_instance = new ThreadManager();
    private boolean g_checkDeadLock = true;
    private Hashtable<Long, ThreadMutexInfo> _m_htThreadMutexInfoTable;

    protected ThreadManager() {
        this._m_htThreadMutexInfoTable = new Hashtable<>();
    }

    public static ThreadManager getInstance() {
        if (g_instance == null) {
            g_instance = new ThreadManager();
        }
        return g_instance;
    }

    public void regThread() {
        long threadID = Thread.currentThread().getId();
        regThread(threadID);
    }

    public ThreadMutexInfo regThread(long _threadID) {
        if (this._m_htThreadMutexInfoTable.containsKey(Long.valueOf(_threadID))) {
            return null;
        }
        CommLog.info("Reg thread: " + _threadID);

        ThreadMutexInfo threadMutexInfo = new ThreadMutexInfo(_threadID);
        this._m_htThreadMutexInfoTable.put(Long.valueOf(_threadID), threadMutexInfo);

        return threadMutexInfo;
    }

    public ThreadMutexInfo getThreadMutexInfo(long _threadID) {
        return this._m_htThreadMutexInfoTable.get(Long.valueOf(_threadID));
    }

    public boolean getCheckDeadLock() {
        return this.g_checkDeadLock;
    }

    public void setCheckDeadThread(boolean isCheck) {
        this.g_checkDeadLock = isCheck;
    }
}

