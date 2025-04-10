package BaseTask.AsynTask;

import java.util.HashMap;

public class AsyncTaskManager
{
private static AsyncTaskManager g_instance = new AsyncTaskManager();

public static AsyncTaskManager getInstance() {
return g_instance;
}

private HashMap<String, AsyncTaskQueue> syncQueues = new HashMap<>();

public AsyncTaskQueue getQueue(String tag) {
return getQueue(tag, false);
}

public AsyncTaskQueue getQueue(String tag, boolean isMultiQueue) {
AsyncTaskQueue ret = this.syncQueues.get(tag);
if (ret == null) {
synchronized (this.syncQueues) {
ret = this.syncQueues.get(tag);
if (ret == null) {
ret = new AsyncTaskQueue(tag, isMultiQueue);
this.syncQueues.put(tag, ret);
} 
} 
}
return ret;
}

public static AsyncTaskQueue getDefaultMultQueue() {
return getInstance().getQueue("DB", true);
}

public String toString() {
StringBuilder sBuilder = new StringBuilder();

sBuilder.append("AsyncTaskManager, size:" + this.syncQueues.size());
sBuilder.append("\n");
sBuilder.append(String.format("%-20s%-20s%-20s%-20s\n", new Object[] { "tag", "MultQueue", "ThreadSize", "QueueSize" }));

for (AsyncTaskQueue queue : this.syncQueues.values()) {
sBuilder.append(queue.toString());
}

return sBuilder.toString();
}
}

