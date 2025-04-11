package BaseServer;

import BaseCommon.CommLog;
import BaseTask.AsynTask.AsyncTaskManager;
import BaseTask.SyncTask.SyncTaskManager;

public class Monitor
        extends Thread {
    private static final int MB = 1049600;
    private static Monitor instance = new Monitor();
    private boolean _m_bThreadExit;
    private _ACleanMemory cleanMemory;
    private boolean needLog = false;

    public Monitor() {
        this._m_bThreadExit = false;
        setName("CosCmdTaskThread");
    }

    public static Monitor getInstance() {
        return instance;
    }

    public void regLog() {
        this.needLog = true;
    }

    public void regCleanMemory(_ACleanMemory cleanMemory) {
        this.cleanMemory = cleanMemory;
    }

    public void ExitThread() {
        this._m_bThreadExit = true;
    }

    public void run() {
        while (!this._m_bThreadExit) {

            try {
                sleep(60000L);
            } catch (InterruptedException e) {
                CommLog.error("Monitor", e);
            }

            if (this.needLog) {
                this.needLog = false;
                StringBuilder sBuilder = new StringBuilder();
                sBuilder.append("ThreadMonitor\n");
                sBuilder.append(SyncTaskManager.getInstance().toString());
                sBuilder.append(AsyncTaskManager.getInstance().toString());
                CommLog.warn(sBuilder.toString());
            }

            long total = Runtime.getRuntime().totalMemory() / 1049600L;
            long free = Runtime.getRuntime().freeMemory() / 1049600L;
            long max = Runtime.getRuntime().maxMemory() / 1049600L;
            long usable = max - total - free;

            if (usable < max / 5L) {
                CommLog.info(String.format("[Memory] max：%10sMB total：%10sMB free：%10sMB available：%10sMB", new Object[]{Long.valueOf(max), Long.valueOf(total), Long.valueOf(free), Long.valueOf(usable)}));
                if (this.cleanMemory != null) {
                    SyncTaskManager.task(() -> {
                        try {
                            CommLog.info("[Memory] 剩余内存不足20%尝试进行内存释放");
                            this.cleanMemory.run();
                        } catch (Throwable t) {
                            CommLog.error("尝试清理内存时发生异常", t);
                        }
                    });
                }
            }
        }
    }

    public void outputMemoryInfo() {
        long total = Runtime.getRuntime().totalMemory() / 1049600L;
        long free = Runtime.getRuntime().freeMemory() / 1049600L;
        long max = Runtime.getRuntime().maxMemory() / 1049600L;
        long usable = max - total - free;
        CommLog.info(String.format("[Memory] max：%10sMB total：%10sMB free：%10sMB available：%10sMB", new Object[]{Long.valueOf(max), Long.valueOf(total), Long.valueOf(free), Long.valueOf(usable)}));
    }
}

