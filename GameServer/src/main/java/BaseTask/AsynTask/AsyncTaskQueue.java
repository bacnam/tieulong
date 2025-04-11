package BaseTask.AsynTask;

import BaseServer.Monitor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class AsyncTaskQueue {
    private ReentrantLock _m_rMutex = new ReentrantLock();
    private List<AsyncThreadQueue> taskDealQueue = new ArrayList<>();
    private List<AsyncTaskDealThread> taskDealThread = new ArrayList<>();
    private String tag;
    private boolean isMultQueue;
    public AsyncTaskQueue(String tag, boolean isMultQueue) {
        this.tag = tag;
        this.isMultQueue = isMultQueue;

        int cpuCnt = Runtime.getRuntime().availableProcessors();
        setDealerCount(Math.max(4, cpuCnt));
    }

    private synchronized AsyncThreadQueue getSubQueue(int id) {
        if (this.isMultQueue) {
            if (this.taskDealQueue.size() <= id) {
                this.taskDealQueue.add(new AsyncThreadQueue(this.tag, id));
            }
            return this.taskDealQueue.get(id);
        }

        if (this.taskDealQueue.size() == 0) {
            this.taskDealQueue.add(new AsyncThreadQueue(this.tag, 0));
        }
        return this.taskDealQueue.get(0);
    }

    private void removeSubQueue(int id) {
        if (this.isMultQueue) {
            this.taskDealQueue.remove(id);
        } else if (id == 0) {
            this.taskDealQueue.remove(id);
        }
    }

    public void setDealerCount(int dealderCount) {
        _lock();

        int addCount = dealderCount - this.taskDealThread.size();

        while (addCount > 0) {

            int id = this.taskDealThread.size();
            AsyncThreadQueue queue = getSubQueue(id);
            AsyncTaskDealThread dealer = new AsyncTaskDealThread(queue, id);
            dealer.start();
            this.taskDealThread.add(dealer);

            addCount--;
        }

        while (addCount < 0 &&
                this.taskDealThread.size() != 0) {

            AsyncTaskDealThread toRemoveDealThread = this.taskDealThread.remove(this.taskDealThread.size() - 1);
            toRemoveDealThread.dispose();
            removeSubQueue(this.taskDealThread.size() - 1);

            addCount++;
        }

        _unlock();
    }

    public String toString() {
        return String.format("%-20s%-20s%-20s%-20s\n", new Object[]{this.tag, Boolean.valueOf(this.isMultQueue), Integer.valueOf(this.taskDealThread.size()), getQueueInfo()});
    }

    public String getQueueInfo() {
        StringBuilder sBuilder = new StringBuilder();
        for (int index = 0; index < this.taskDealQueue.size(); index++) {
            AsyncThreadQueue queue = this.taskDealQueue.get(index);
            sBuilder.append(String.format("{%s:%s}", new Object[]{Integer.valueOf(queue.getID()), Integer.valueOf(queue.getSize())}));
        }
        return sBuilder.toString();
    }

    public <T> void regAsynTask(AsyncTaskBase<T> _callObj, AsyncCallBackTaskBase<T> _callBackObj, long index) {
        index = Math.abs(index);
        AsyncTaskWrapper<T> info = new AsyncTaskWrapper<>(_callObj, _callBackObj);

        _lock();

        if (this.taskDealQueue.size() == 0) {
            _unlock();

            return;
        }

        if (this.isMultQueue) {
            int id = (int) (index % this.taskDealQueue.size());
            AsyncThreadQueue mgr = this.taskDealQueue.get(id);
            mgr.regAsynTask(info);
        } else {
            ((AsyncThreadQueue) this.taskDealQueue.get(0)).regAsynTask(info);
        }

        _unlock();
    }

    public <T> void regAsynTask(AsyncTaskBase<T> _callObj, AsyncCallBackTaskBase<T> _callBackObj) {
        regAsynTask(_callObj, _callBackObj, 0L);
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
            _lock();
            this.infoList.add(obj);
            this._m_sSemaphore.release();
            _unlock();

            if (this.infoList.size() > 10000) {
                Monitor.getInstance().regLog();
            }
        }

        public AsyncTaskWrapper popFirstAsynTask() {
            this._m_sSemaphore.acquireUninterruptibly();
            AsyncTaskWrapper ret = null;
            _lock();
            ret = this.infoList.pop();
            _unlock();
            return ret;
        }

        public int getSize() {
            return this.infoList.size();
        }
    }
}

