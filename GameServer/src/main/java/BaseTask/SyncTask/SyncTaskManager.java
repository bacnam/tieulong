package BaseTask.SyncTask;

import BaseCommon.CommLog;

import java.util.HashMap;

public class SyncTaskManager {
    private static SyncTaskManager g_instance = new SyncTaskManager();
    private HashMap<String, SyncTaskQueue> syncQueues = new HashMap<>();

    public static SyncTaskManager getInstance() {
        return g_instance;
    }

    public static void task(SyncTask _task) {
        getInstance().getQueue("Default").RegisterTask(_task);
    }

    public static void task(SyncTask _task, int _time) {
        getInstance().getQueue("Default").RegisterTask(_task, _time);
    }

    public static void task(SyncTask _task, long _time) {
        getInstance().getQueue("Default").RegisterTask(_task, _time);
    }

    public static void task(SyncTask _task, String info) {
        getInstance().getQueue("Default").RegisterTask(_task, info);
    }

    public static void task(SyncTask _task, int _time, String info) {
        getInstance().getQueue("Default").RegisterTask(_task, _time, info);
    }

    public static void task(SyncTask _task, long _time, String info) {
        getInstance().getQueue("Default").RegisterTask(_task, _time, info);
    }

    public static void schedule(final int interval, final SyncTimer timer) {
        task(new SyncTask() {
            public void run() {
                boolean cont = true;
                try {
                    cont = timer.run();
                } catch (Exception e) {
                    CommLog.error("schedule Exception", e);
                }
                if (cont)
                    SyncTaskManager.task(this, interval);
            }

        });
    }

    public SyncTaskQueue getQueue(String tag) {
        SyncTaskQueue ret = this.syncQueues.get(tag);
        if (ret == null) {
            synchronized (this.syncQueues) {
                ret = this.syncQueues.get(tag);
                if (ret == null) {
                    ret = new SyncTaskQueue(tag);
                    this.syncQueues.put(tag, ret);
                }
            }
        }
        return ret;
    }

    public String toString() {
        StringBuilder sBuilder = new StringBuilder();

        sBuilder.append("SyncTaskManager, size:" + this.syncQueues.size());
        sBuilder.append("\n");
        sBuilder.append(String.format("%-20s%-20s%-20s%-20s\n", new Object[]{"tag", "ThreadSize", "TaskSize", "TimerSize"}));

        for (SyncTaskQueue queue : this.syncQueues.values()) {
            sBuilder.append(queue.toString());
        }

        return sBuilder.toString();
    }
}

