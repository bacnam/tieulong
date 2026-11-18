package business.player;

import BaseCommon.CommLog;
import BaseThread.BaseMutexObject;
import com.zhonglian.server.common.utils.CommMath;
import com.zhonglian.server.common.utils.Maps;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefRobot;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class RobotManager {
    private final Map<Long, Player> pidDatas = Maps.newConcurrentMap();
    private final Map<Integer, List<Player>> lvCidDatas = Maps.newConcurrentMap();
    private final BaseMutexObject _lock = new BaseMutexObject();
    private static RobotManager instance = new RobotManager();

    public static RobotManager getInstance() {
        return instance;
    }

    private RobotManager() {
    }

    public void lock() {
        this._lock.lock();
    }

    public void unlock() {
        this._lock.unlock();
    }

    public void init() {
        this.reload();
    }

    public Map<Long, Player> getAll() {
        return this.pidDatas;
    }

    public Player getRandomPlayer() {
        List<Player> players = new ArrayList<>();
        players.addAll(this.pidDatas.values());
        return CommMath.randomOne(players);
    }

    public List<Player> getRandomPlayers(int size) {
        List<Player> players = new ArrayList<>();
        players.addAll(this.pidDatas.values());
        return CommMath.getRandomListByCnt(players, size);
    }

    public void reload() {
        this.lvCidDatas.clear();
        this.pidDatas.clear();

        for (Player player : PlayerMgr.getInstance().getAllPlayers()) {
            if (player.isVirtualPlayer()) {
                this.regVPlayer(player);
            }
        }

        int totalVPlayerCnt = 0;
        List<RefRobot> vPlayerInits = new ArrayList<>(RefDataMgr.getAll(RefRobot.class).values());

        for (RefRobot init : vPlayerInits) {
            totalVPlayerCnt += init.Count;
        }

        if (this.pidDatas.size() < totalVPlayerCnt) {
            CommLog.info("[RobotManager.init] 机器人数量不足，开始创建机器人... 大概需要1min 请稍等");
            Collections.sort(vPlayerInits, new Comparator<RefRobot>() {
                public int compare(RefRobot o1, RefRobot o2) {
                    return o2.Level - o1.Level;
                }
            });

            for (RefRobot init : vPlayerInits) {
                List<Player> players = this.lvCidDatas.get(init.Level);
                if (players == null) {
                    players = new ArrayList<>();
                    this.lvCidDatas.put(init.Level, players);
                }

                if (players.size() < init.Count) {
                    this.createRobot(init);
                }
            }

            CommLog.info("[RobotManager.init]机器人创建完毕，当前总量: {}", this.pidDatas.size());
            CommLog.info("[RobotManager.init]机器人创建完后并未入库请入库完毕后再停服重启!!否则会导致数据丢失!!");
        }
    }

    public void createRobot(RefRobot init) {
        CommLog.info("[RobotManager.init] 创建机器人lv:{},数量:{}, 机器人创建完后并未入库请入库完毕后再停服重启!!!", init.Level, init.Count);

        for (int i = 0; i < init.Count; i++) {
            Player player = PlayerMgr.getInstance().createRobot(init);
            if (player == null) {
                return;
            }

            this.regVPlayer(player);
        }
    }

    public void regVPlayer(Player player) {
        int lv = player.getPlayerBO().getLv();
        List<Player> lvInfoMap = this.lvCidDatas.get(lv);
        if (lvInfoMap == null) {
            lvInfoMap = new ArrayList<>();
            this.lvCidDatas.put(lv, lvInfoMap);
        }

        lvInfoMap.add(player);
        this.pidDatas.put(player.getPid(), player);
    }

    public List<Player> getLvlPlayers(int teamLevel) {
        return this.lvCidDatas.get(teamLevel);
    }

    public boolean isRobot(long pid) {
        return this.pidDatas.containsKey(pid);
    }

    public void setRobot(long pid, Player player) {
        this.pidDatas.put(pid, player);
    }
}
