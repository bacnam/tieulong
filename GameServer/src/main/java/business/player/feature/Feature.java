package business.player.feature;

import BaseCommon.CommLog;
import BaseThread.BaseMutexObject;
import business.player.Player;
import com.zhonglian.server.common.utils.CommTime;

public abstract class Feature
{
protected final Player player;
private Boolean _loaded = Boolean.valueOf(false);
private int lastActiveTime = 0;

protected final BaseMutexObject m_mutex = new BaseMutexObject();

public Player getPlayer() {
return this.player;
}

public String getPlayerName() {
return this.player.getName();
}

public long getPid() {
return this.player.getPid();
}

protected void lock() {
this.m_mutex.lock();
}

protected void unlock() {
this.m_mutex.unlock();
}

public Feature(Player player) {
this.player = player;
}

public int getLastActiveTime() {
return this.lastActiveTime;
}

public void updateLastActiveTime() {
this.lastActiveTime = CommTime.RecentSec;
}

public Feature tryLoadDBData() {
this.lastActiveTime = CommTime.nowSecond();

if (this._loaded.booleanValue()) {
return this;
}

synchronized (this._loaded) {
if (this._loaded.booleanValue()) {
return this;
}

try {
loadDB();
} catch (Exception e) {
CommLog.error("Feature.tryLoadDBData", e);
} 
this._loaded = Boolean.valueOf(true);
} 
return this;
}

public abstract void loadDB();
}

