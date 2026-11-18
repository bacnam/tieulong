package BaseTask.AsynTask;

import BaseServer.Monitor;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class AsyncTaskQueue {
    private ReentrantLock _m_rMutex = new ReentrantLock();
    private List<AsyncTaskQueue.AsyncThreadQueue> taskDealQueue = new ArrayList<>();
    private List<AsyncTaskDealThread> taskDealThread = new ArrayList<>();
    private String tag;
    private boolean isMultQueue;

    public AsyncTaskQueue(String tag, boolean isMultQueue) {
        this.tag = tag;
        this.isMultQueue = isMultQueue;
        int cpuCnt = Runtime.getRuntime().availableProcessors();
        this.setDealerCount(Math.max(4, cpuCnt));
    }

    private synchronized AsyncTaskQueue.AsyncThreadQueue getSubQueue(int id) {
        if (this.isMultQueue) {
            if (this.taskDealQueue.size() <= id) {
                this.taskDealQueue.add(new AsyncTaskQueue.AsyncThreadQueue(this.tag, id));
            }

            return this.taskDealQueue.get(id);
        } else {
            if (this.taskDealQueue.size() == 0) {
                this.taskDealQueue.add(new AsyncTaskQueue.AsyncThreadQueue(this.tag, 0));
            }

            return this.taskDealQueue.get(0);
        }
    }

    private void removeSubQueue(int id) {
        if (this.isMultQueue) {
            this.taskDealQueue.remove(id);
        } else if (id == 0) {
            this.taskDealQueue.remove(id);
        }
    }

    public void setDealerCount(int dealderCount) {
        this._lock();

        int addCount;
        for (addCount = dealderCount - this.taskDealThread.size(); addCount > 0; addCount--) {
            int id = this.taskDealThread.size();
            AsyncTaskQueue.AsyncThreadQueue queue = this.getSubQueue(id);
            AsyncTaskDealThread dealer = new AsyncTaskDealThread(queue, id);
            dealer.start();
            this.taskDealThread.add(dealer);
        }

        while (addCount < 0 && this.taskDealThread.size() != 0) {
            AsyncTaskDealThread toRemoveDealThread = this.taskDealThread.remove(this.taskDealThread.size() - 1);
            toRemoveDealThread.dispose();
            this.removeSubQueue(this.taskDealThread.size() - 1);
            addCount++;
        }

        this._unlock();
    }

    @Override
    public String toString() {
        return String.format("%-20s%-20s%-20s%-20s\n", this.tag, this.isMultQueue, this.taskDealThread.size(), this.getQueueInfo());
    }

    public String getQueueInfo() {
        StringBuilder sBuilder = new StringBuilder();

        for (int index = 0; index < this.taskDealQueue.size(); index++) {
            AsyncTaskQueue.AsyncThreadQueue queue = this.taskDealQueue.get(index);
            sBuilder.append(String.format("{%s:%s}", queue.getID(), queue.getSize()));
        }

        return sBuilder.toString();
    }

    public <T> void regAsynTask(AsyncTaskBase<T> _callObj, AsyncCallBackTaskBase<T> _callBackObj, long index) {
        index = Math.abs(index);
        AsyncTaskWrapper<T> info = new AsyncTaskWrapper<>(_callObj, _callBackObj);
        this._lock();
        if (this.taskDealQueue.size() == 0) {
            this._unlock();
        } else {
            if (this.isMultQueue) {
                int id = (int)(index % this.taskDealQueue.size());
                AsyncTaskQueue.AsyncThreadQueue mgr = this.taskDealQueue.get(id);
                mgr.regAsynTask(info);
            } else {
                this.taskDealQueue.get(0).regAsynTask(info);
            }

            this._unlock();
        }
    }

    public <T> void regAsynTask(AsyncTaskBase<T> _callObj, AsyncCallBackTaskBase<T> _callBackObj) {
        this.regAsynTask(_callObj, _callBackObj, 0L);
    }

    protected void _lock() {
        this._m_rMutex.lock();
    }

    protected void _unlock() {
        this._m_rMutex.unlock();
    }

    public class AsyncThreadQueue {
        private LinkedList<AsyncTaskWrapper> infoList = new LinkedList<>();
        private Semaphore _m_sSemaphore = new Semaphore(0);
        private ReentrantLock _m_rMutex = new ReentrantLock();
        private String tag;
        private int id;

        public AsyncThreadQueue(String tag, int id) {
            this.tag = tag;
            this.id = id;
        }

        public int getID() {
            return this.id;
        }

        public String getTag() {
            return this.tag;
        }

        protected void _lock() {
            this._m_rMutex.lock();
        }

        protected void _unlock() {
            this._m_rMutex.unlock();
        }

        public <T> void regAsynTask(AsyncTaskWrapper<T> obj) {
            this._lock();
            this.infoList.add(obj);
            this._m_sSemaphore.release();
            this._unlock();
            if (this.infoList.size() > 10000) {
                Monitor.getInstance().regLog();
            }
        }

        public AsyncTaskWrapper popFirstAsynTask() {
            this._m_sSemaphore.acquireUninterruptibly();
            AsyncTaskWrapper ret = null;
            this._lock();
            ret = this.infoList.pop();
            this._unlock();
            return ret;
        }

        public int getSize() {
            return this.infoList.size();
        }
    }
}
