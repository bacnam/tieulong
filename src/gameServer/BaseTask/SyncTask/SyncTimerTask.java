package BaseTask.SyncTask;

import BaseCommon.CommLog;
import java.util.TimerTask;

public class SyncTimerTask
extends TimerTask
{
SyncTaskWrapper task;

public SyncTimerTask(SyncTaskWrapper task) {
this.task = task;
}

public void run() {
Thread runningThread = this.task.getThread();
String threadName = "";
try {
threadName = runningThread.getName();
} catch (Exception exception) {}

StringBuffer info = new StringBuffer();
info.append(String.format("[%s] [SyncTaskManager][timeout][id]:%s/[pool]:%s [run] > 3000ms, [timer]:%s, exInfo:%s \n", new Object[] { threadName, Long.valueOf(this.task.getID()), 
Integer.valueOf(this.task.getParentQueue().getNormalTaskSize()), Long.valueOf(this.task.getTimer()), this.task.getInfo() }));
info.append("Regist StackTrace: \n");
int dept = 0;
int ignoreDept = (this.task.getTimer() > 0L) ? 5 : 2; byte b; int i; StackTraceElement[] arrayOfStackTraceElement;
for (i = (arrayOfStackTraceElement = this.task.getException().getStackTrace()).length, b = 0; b < i; ) { StackTraceElement st = arrayOfStackTraceElement[b];
dept++;
if (dept > ignoreDept) {
info.append("  ");
info.append(st.toString());
info.append("\n");
} 
b++; }

info.append(String.valueOf(threadName) + " Block StackTrace: \n");

if (runningThread != null) {
StackTraceElement[] stackTrace = runningThread.getStackTrace();

if (stackTrace == null) {
info.append("  SyncTask does not contain statckInfo\n");
} else {
StackTraceElement[] arrayOfStackTraceElement1; for (int j = (arrayOfStackTraceElement1 = stackTrace).length; i < j; ) { StackTraceElement st = arrayOfStackTraceElement1[i];
info.append("  " + st.toString() + "\n"); i++; }

} 
} else {
info.append("  SyncTask never run! Boring in the task_queue!\n");
} 
CommLog.warn(info.toString());
}
}

