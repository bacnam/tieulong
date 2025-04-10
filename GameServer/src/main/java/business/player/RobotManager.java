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

public class RobotManager
{
private final Map<Long, Player> pidDatas = Maps.newConcurrentMap();

private final Map<Integer, List<Player>> lvCidDatas = Maps.newConcurrentMap();

private final BaseMutexObject _lock = new BaseMutexObject();

private static RobotManager instance = new RobotManager();

public static RobotManager getInstance() {
return instance;
}

public void lock() {
this._lock.lock();
}

public void unlock() {
this._lock.unlock();
}

public void init() {
reload();
}

public Map<Long, Player> getAll() {
return this.pidDatas;
}

public Player getRandomPlayer() {
List<Player> players = new ArrayList<>();
players.addAll(this.pidDatas.values());
Player player = (Player)CommMath.randomOne(players);
return player;
}

public List<Player> getRandomPlayers(int size) {
List<Player> players = new ArrayList<>();
players.addAll(this.pidDatas.values());
List<Player> player = CommMath.getRandomListByCnt(players, size);
return player;
}

public void reload() {
this.lvCidDatas.clear();
this.pidDatas.clear();

for (Player player : PlayerMgr.getInstance().getAllPlayers()) {
if (!player.isVirtualPlayer())
continue; 
regVPlayer(player);
} 

int totalVPlayerCnt = 0;
List<RefRobot> vPlayerInits = new ArrayList<>(RefDataMgr.getAll(RefRobot.class).values());
for (RefRobot init : vPlayerInits) {
totalVPlayerCnt += init.Count;
}

if (this.pidDatas.size() >= totalVPlayerCnt) {
return;
}

CommLog.info("[RobotManager.init] 机器人数量不足，开始创建机器人... 大概需要1min 请稍等");

Collections.sort(vPlayerInits, new Comparator<RefRobot>()
{
public int compare(RefRobot o1, RefRobot o2) {
return o2.Level - o1.Level;
}
});

for (RefRobot init : vPlayerInits) {
List<Player> players = this.lvCidDatas.get(Integer.valueOf(init.Level));
if (players == null) {
players = new ArrayList<>();
this.lvCidDatas.put(Integer.valueOf(init.Level), players);
} 

if (players.size() >= init.Count) {
continue;
}

createRobot(init);
} 
CommLog.info("[RobotManager.init]机器人创建完毕，当前总量: {}", Integer.valueOf(this.pidDatas.size()));
CommLog.info("[RobotManager.init]机器人创建完后并未入库请入库完毕后再停服重启!!否则会导致数据丢失!!");
}

public void createRobot(RefRobot init) {
CommLog.info("[RobotManager.init] 创建机器人lv:{},数量:{}, 机器人创建完后并未入库请入库完毕后再停服重启!!!", Integer.valueOf(init.Level), Integer.valueOf(init.Count));
for (int i = 0; i < init.Count; i++) {
Player player = PlayerMgr.getInstance().createRobot(init);
if (player == null)
return; 
regVPlayer(player);
} 
}

public void regVPlayer(Player player) {
int lv = player.getPlayerBO().getLv();
List<Player> lvInfoMap = this.lvCidDatas.get(Integer.valueOf(lv));
if (lvInfoMap == null) {
lvInfoMap = new ArrayList<>();
this.lvCidDatas.put(Integer.valueOf(lv), lvInfoMap);
} 
lvInfoMap.add(player);
this.pidDatas.put(Long.valueOf(player.getPid()), player);
}

public List<Player> getLvlPlayers(int teamLevel) {
return this.lvCidDatas.get(Integer.valueOf(teamLevel));
}

public boolean isRobot(long pid) {
return this.pidDatas.containsKey(Long.valueOf(pid));
}

public void setRobot(long pid, Player player) {
this.pidDatas.put(Long.valueOf(pid), player);
}
}

