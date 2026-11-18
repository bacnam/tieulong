package business.player;

import BaseCommon.CommClass;
import BaseCommon.CommLog;
import BaseThread.BaseMutexInstance;
import BaseThread.BaseMutexObject;
import business.player.feature.Feature;
import business.player.feature.PlayerBase;
import business.player.feature.features.DailyRefreshFeature;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.websocket.PkgCacheMgr;
import core.database.game.bo.PlayerBO;
import core.network.client2game.ClientSession;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Player {
    public static final String VirtualPlayerOpenId = "LEGEND_ROBOT";
    protected final BaseMutexObject m_mutex = new BaseMutexObject();
    protected final BaseMutexInstance m_mutexIns = new BaseMutexInstance();
    private final PlayerBO playerBO;
    private final boolean isVPlayer;
    private Hashtable<String, Feature> features = new Hashtable<>();
    private ClientSession session;
    private DailyRefreshFeature dailyRefreshFeature;
    private Object dailyLockObject = new Object();
    private PkgCacheMgr pkgCacheMgr = null;

    public void lockIns() {
        this.m_mutexIns.lock();
    }

    public void unlockIns() {
        this.m_mutexIns.unlock();
    }

    public void lock() {
        this.m_mutex.lock();
    }

    public void unlock() {
        this.m_mutex.unlock();
    }

    public Player(PlayerBO playerBO) {
        this.playerBO = playerBO;
        this.isVPlayer = "LEGEND_ROBOT".equals(playerBO.getOpenId());
        this.m_mutex.reduceMutexLevel(2);
    }

    public PlayerBO getPlayerBO() {
        return this.playerBO;
    }

    public long getPid() {
        return this.playerBO.getId();
    }

    public String getOpenId() {
        return this.playerBO.getOpenId();
    }

    public int getSid() {
        return this.playerBO.getSid();
    }

    public String getName() {
        return this.playerBO.getName();
    }

    public int getLv() {
        return this.playerBO.getLv();
    }

    public int getVipLevel() {
        return this.playerBO.getVipLevel();
    }

    public boolean isVirtualPlayer() {
        return this.isVPlayer;
    }

    public int getBanTime() {
        int expiredTime = this.playerBO.getBannedLoginExpiredTime();
        return expiredTime == -1 ? -1 : Math.max(0, expiredTime - CommTime.nowSecond());
    }

    public boolean isBanned() {
        int expiredTime = this.playerBO.getBannedLoginExpiredTime();
        return expiredTime == -1 || expiredTime > CommTime.nowSecond();
    }

    public boolean isLoaded(Class<? extends Feature> clazz) {
        return this.features.get(clazz.getName()) != null;
    }

    public <T extends Feature> T getFeature(Class<T> clazz) {
        Feature feature = this.getFeature(clazz.getName());
        feature.tryLoadDBData();
        return (T)feature;
    }

    private Feature getFeature(String featureName) {
        Feature feature = this.features.get(featureName);
        if (feature != null) {
            feature.updateLastActiveTime();
            return feature;
        } else {
            synchronized (this.features) {
                feature = this.features.get(featureName);
                if (feature != null) {
                    return feature;
                } else {
                    Feature var10000;
                    try {
                        Class<?> cs = CommClass.forName(featureName);
                        Constructor<Feature> constructor = (Constructor<Feature>)cs.getConstructor(Player.class);
                        feature = constructor.newInstance(this);
                        this.features.put(featureName, feature);
                        var10000 = feature;
                    } catch (Exception var6) {
                        CommLog.error(var6.getMessage(), (Throwable)var6);
                        System.exit(-1);
                        return null;
                    }

                    return var10000;
                }
            }
        }
    }

    public void releaseFeature(int activeTime) {
        int curTime = CommTime.nowSecond();
        if (!this.isVirtualPlayer()) {
            if (!this.isOnline()) {
                if (this.getPlayerBO().getLastLogin() + activeTime <= curTime) {
                    this.lockIns();

                    try {
                        for (Feature feature : new ArrayList<>(this.features.values())) {
                            if (feature.getLastActiveTime() + activeTime <= curTime) {
                                this.features.remove(feature.getClass().getName());
                            }
                        }
                    } catch (Exception var5) {
                        CommLog.error("释放玩家Feature时发生异常", (Throwable)var5);
                    }

                    this.unlockIns();
                }
            }
        }
    }

    public ClientSession getClientSession() {
        return this.session;
    }

    public boolean isOnline() {
        return this.session != null;
    }

    public void bindSession(ClientSession session) {
        this.lockIns();
        this.session = session;
        PlayerMgr.getInstance().regOnlinePlayer(this);
        this.playerBO.saveLastLogin(CommTime.nowSecond());
        this.getFeature(PlayerBase.class).onConnect();
        this.unlockIns();
    }

    public void loseSession() {
        PlayerMgr.getInstance().unregOnlinePlayer(this);
        this.session = null;
        this.getFeature(PlayerBase.class).onDisconnect();
    }

    public void pushProto(String operation, Object proto) {
        if (this.session != null && this.session.isPrint()) {
            CommLog.error(String.format("[notify][%s]：%s", operation, proto.toString()));
        }

        if (this.session != null) {
            this.session.notifyMessage(operation, proto);
        }
    }

    public void pushProperties(String name, int value) {
        List<core.network.proto.Player.Property> properties = new ArrayList<>();
        properties.add(new core.network.proto.Player.Property(name, value));
        this.pushProperties(properties);
    }

    public void pushProperties(String name0, int value0, String name1, int value1) {
        List<core.network.proto.Player.Property> properties = new ArrayList<>();
        properties.add(new core.network.proto.Player.Property(name0, value0));
        properties.add(new core.network.proto.Player.Property(name1, value1));
        this.pushProperties(properties);
    }

    public void pushProperties(List<core.network.proto.Player.Property> properties) {
        this.pushProto("playerchanged", properties);
    }

    public void pushMessage(String key, String... params) {
        ArrayList<String> p = new ArrayList<>();

        for (String s : params) {
            p.add(s);
        }

        this.pushProto("systemMesage", "");
    }

    public void notify2Zone() {
        PlayerMgr.getInstance().tryNotify(this);
    }

    public boolean isDailyRefreshLoaded() {
        return this.dailyRefreshFeature != null;
    }

    public DailyRefreshFeature getDailyRefreshFeature() {
        if (this.dailyRefreshFeature == null) {
            synchronized (this.dailyLockObject) {
                if (this.dailyRefreshFeature == null) {
                    this.dailyRefreshFeature = new DailyRefreshFeature(this);
                }
            }
        }

        return this.dailyRefreshFeature;
    }

    public PkgCacheMgr getPacketCache() {
        if (this.pkgCacheMgr == null) {
            this.pkgCacheMgr = new PkgCacheMgr();
        }

        return this.pkgCacheMgr;
    }
}
