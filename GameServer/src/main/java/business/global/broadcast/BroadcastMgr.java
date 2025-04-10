package business.global.broadcast;

import BaseTask.SyncTask.SyncTaskManager;
import BaseThread.BaseMutexObject;
import business.player.Player;
import business.player.PlayerMgr;
import com.zhonglian.server.common.utils.Lists;
import java.util.Collection;
import java.util.LinkedList;

public class BroadcastMgr
{
public static BroadcastMgr instance;
public final LinkedList<BroadcastRecord> queue;
private final BaseMutexObject m_mutex;

public static BroadcastMgr getInstance() {
if (instance == null) {
instance = new BroadcastMgr();
}
return instance;
}

public BroadcastMgr() {
this.queue = Lists.newLinkedList();

this.m_mutex = new BaseMutexObject();
regBroadCastTask();
} public void lock() {
this.m_mutex.lock();
}

public void unlock() {
this.m_mutex.unlock();
}

public void regBroadCastTask() {
SyncTaskManager.schedule(1000, () -> {
broadcastTask();
return true;
});
}

public void addNewBroadcastTask(BroadcastTask task) {
Collection<Player> players = PlayerMgr.getInstance().getOnlinePlayers();

lock();

BroadcastRecord record = new BroadcastRecord(Lists.newLinkedList(players), task);
this.queue.add(record);

unlock();
}

public void addNewBroadcastTask(LinkedList<Player> players, BroadcastTask task) {
lock();

BroadcastRecord record = new BroadcastRecord(players, task);
this.queue.add(record);

unlock();
}

public void broadcastTask() {
for (int i = 0; i < 100; i++) {
Player player = null;
BroadcastTask task = null;
lock();
while (!this.queue.isEmpty()) {
BroadcastRecord peek = this.queue.peek();
player = peek.players.poll();
if (peek.players.isEmpty()) {
this.queue.pop();
}
if (player != null) {
task = peek.task;
break;
} 
} 
unlock();
if (player != null && task != null)
task.poll(player); 
} 
}
}

