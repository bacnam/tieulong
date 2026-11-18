package BaseTask.SyncTask;

import BaseCommon.CommLog;
import java.util.TimerTask;

public class SyncTimerTask extends TimerTask {
    SyncTaskWrapper task;

    public SyncTimerTask(SyncTaskWrapper task) {
        this.task = task;
    }

    @Override
    public void run() {
        Thread runningThread = this.task.getThread();
        String threadName = "";

        try {
            threadName = runningThread.getName();
        } catch (Exception var11) {
        }

        StringBuffer info = new StringBuffer();
        info.append(
            String.format(
                "[%s] [SyncTaskManager][timeout][id]:%s/[pool]:%s [run] > 3000ms, [timer]:%s, exInfo:%s \n",
                threadName,
                this.task.getID(),
                this.task.getParentQueue().getNormalTaskSize(),
                this.task.getTimer(),
                this.task.getInfo()
            )
        );
        info.append("Regist StackTrace: \n");
        int dept = 0;
        int ignoreDept = this.task.getTimer() > 0L ? 5 : 2;

        StackTraceElement[] var9;
        for (StackTraceElement st : var9 = this.task.getException().getStackTrace()) {
            if (++dept > ignoreDept) {
                info.append("  ");
                info.append(st.toString());
                info.append("\n");
            }
        }

        info.append(threadName + " Block StackTrace: \n");
        if (runningThread != null) {
            StackTraceElement[] stackTrace = runningThread.getStackTrace();
            if (stackTrace == null) {
                info.append("  SyncTask does not contain statckInfo\n");
            } else {
                for (StackTraceElement stx : stackTrace) {
                    info.append("  " + stx.toString() + "\n");
                }
            }
        } else {
            info.append("  SyncTask never run! Boring in the task_queue!\n");
        }

        CommLog.warn(info.toString());
    }
}
