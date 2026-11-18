package business.player.feature;

import BaseCommon.CommLog;
import BaseThread.BaseMutexObject;
import business.player.Player;
import com.zhonglian.server.common.utils.CommTime;

public abstract class Feature {
    protected final Player player;
    private Boolean _loaded = false;
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
        if (this._loaded) {
            return this;
        } else {
            synchronized (this._loaded) {
                if (this._loaded) {
                    return this;
                } else {
                    try {
                        this.loadDB();
                    } catch (Exception var3) {
                        CommLog.error("Feature.tryLoadDBData", (Throwable)var3);
                    }

                    this._loaded = true;
                    return this;
                }
            }
        }
    }

    public abstract void loadDB();
}
