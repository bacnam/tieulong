package BaseThread;

import BaseCommon.CommLog;

import java.util.concurrent.locks.ReentrantLock;

public class CosMutex {
    private ReentrantLock _m_lMutex;
    private int _m_eMutexLevel;

    public CosMutex(int _mutexLevel) {
        this._m_lMutex = new ReentrantLock(true);
        this._m_eMutexLevel = _mutexLevel;
    }

    public int getMutexLevel() {
        return this._m_eMutexLevel;
    }

    public void addMutexLevel() {
        this._m_eMutexLevel--;
    }

    public void addMutexLevel(int _level) {
        this._m_eMutexLevel -= _level;
    }

    public void reduceMutexLevel() {
        this._m_eMutexLevel++;
    }

    public void reduceMutexLevel(int _level) {
        this._m_eMutexLevel += _level;
    }

    public void lock() {
        if (!ThreadManager.getInstance().getCheckDeadLock()) {
            this._m_lMutex.lock();

            return;
        }

        long curThreadID = Thread.currentThread().getId();

        ThreadMutexInfo threadMutexInfo = ThreadManager.getInstance().getThreadMutexInfo(curThreadID);

        if (threadMutexInfo == null) {
            CommLog.warn("Unreg Thread try to get mutex", new Throwable());

            return;
        }
        try {
            threadMutexInfo.tryLock(this);
            this._m_lMutex.lock();
        } catch (CosMutexException e) {
            CommLog.warn("无法获取高等级锁", e);
        }
    }

    public void unlock() {
        if (ThreadManager.getInstance().getCheckDeadLock()) {

            long curThreadID = Thread.currentThread().getId();

            ThreadMutexInfo threadMutexInfo = ThreadManager.getInstance().getThreadMutexInfo(curThreadID);

            if (threadMutexInfo == null) {
                CommLog.warn("Unreg Thread try to release mutex", new Throwable());

                return;
            }
            try {
                threadMutexInfo.tryUnlock(this);
            } catch (CosMutexException e) {
                CommLog.warn("尝试释放未获取的锁", e);
            }
        }

        try {
            this._m_lMutex.unlock();
        } catch (Exception exception) {
        }
    }
}

