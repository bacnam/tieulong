package business.player;

import BaseCommon.CommLog;
import BaseTask.SyncTask.SyncTask;
import BaseTask.SyncTask.SyncTaskManager;
import BaseThread.BaseMutexObject;
import business.player.feature.AccountFeature;
import business.player.feature.PlayerBase;
import business.player.feature.marry.MarryFeature;
import com.google.gson.JsonObject;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.utils.*;
import com.zhonglian.server.common.utils.Random;
import com.zhonglian.server.websocket.def.ErrorCode;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefPlayerInit;
import core.config.refdata.ref.RefPlayerName;
import core.config.refdata.ref.RefRobot;
import core.database.game.bo.MarryBO;
import core.database.game.bo.PlayerBO;
import core.network.client2game.ClientSession;
import core.network.game2zone.ZoneConnector;
import core.server.ServerConfig;
import proto.gamezone.Player;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class PlayerMgr {
    private static PlayerMgr instance = new PlayerMgr();
    public final Map<Long, Player> _onlinePlayersByCid = Maps.newConcurrentMap();

    public final Map<Long, Player> pidDatas = Maps.newConcurrentMap();

    public final Map<String, Map<Integer, Player>> openidSidDatas = Maps.newConcurrentMap();

    public final Map<String, Player> nameDatas = Maps.newConcurrentMap();

    public final Map<String, JsonObject> playerJsons = Maps.newConcurrentMap();

    public final Map<String, Player> sexPlayers = Maps.newConcurrentMap();
    private final String EmptyPlayerPid = "";
    private final BaseMutexObject _lock = new BaseMutexObject();
    private BlockingQueue<Player> toNotifyWorld;

    private PlayerMgr() {
        this.toNotifyWorld = new LinkedBlockingQueue<>();
        this._lock.reduceMutexLevel(1);
    }

    public static PlayerMgr getInstance() {
        return instance;
    }

    public void lock() {
        this._lock.lock();
    }

    public void unlock() {
        this._lock.unlock();
    }

    public void init() {
        CommLog.info("[PlayerMgr.init] load player begin...]");

        lock();
        List<PlayerBO> allPlayers = BM.getBM(PlayerBO.class).findAll();
        for (PlayerBO bo : allPlayers) {
            if (!bo.getName().matches("s\\d+\\..+")) {
                bo.saveName("s" + bo.getSid() + "." + bo.getName());
            }
            regPlayer(new Player(bo));
        }
        unlock();
        CommLog.info("[PlayerMgr.init] load player success, count: {}", Integer.valueOf(this.pidDatas.size()));

        List<MarryBO> allMarry = BM.getBM(MarryBO.class).findAll();
        for (MarryBO bo : allMarry) {
            if (bo.getSex() != 0) {
                Player player = getPlayer(bo.getPid());
                this.sexPlayers.put(player.getName(), player);
            }
        }
        notifyAll2World();
        startSync2Zone();
    }

    private void regPlayer(Player player) {
        lock();
        long playerCid = player.getPlayerBO().getId();
        this.pidDatas.put(Long.valueOf(playerCid), player);
        this.nameDatas.put(player.getName(), player);
        if (!player.getOpenId().isEmpty()) {
            Map<Integer, Player> sidDatas = this.openidSidDatas.get(player.getOpenId());
            if (sidDatas == null) {
                sidDatas = Maps.newConcurrentMap();
                this.openidSidDatas.put(player.getOpenId(), sidDatas);
            }
            sidDatas.put(Integer.valueOf(player.getPlayerBO().getSid()), player);
        }
        unlock();
    }

    private void unregPlayer(Player player) {
        lock();
        long playerCid = player.getPlayerBO().getId();
        this.pidDatas.remove(Long.valueOf(playerCid));
        this.nameDatas.remove(player.getPlayerBO().getName());
        this._onlinePlayersByCid.remove(Long.valueOf(playerCid));
        if (!player.getOpenId().isEmpty()) {
            Map<Integer, Player> sidDatas = this.openidSidDatas.get(player.getOpenId());
            if (sidDatas != null) {
                sidDatas.remove(Integer.valueOf(player.getPlayerBO().getSid()));
            }
        }
        unlock();
    }

    public void regOnlinePlayer(Player player) {
        lock();
        this._onlinePlayersByCid.put(Long.valueOf(player.getPid()), player);
        unlock();
    }

    public void unregOnlinePlayer(Player player) {
        lock();
        this._onlinePlayersByCid.remove(Long.valueOf(player.getPid()));
        unlock();
    }

    public Player getOnlinePlayerByCid(long cid) {
        return this._onlinePlayersByCid.get(Long.valueOf(cid));
    }

    public Player getPlayer(long pid) {
        return this.pidDatas.get(Long.valueOf(pid));
    }

    public Player getPlayer(String openid, int sid) {
        if (openid == null || openid.length() <= 0) {
            return null;
        }
        if ("LEGEND_ROBOT".equals(openid)) {
            return null;
        }
        Map<Integer, Player> sidDatas = this.openidSidDatas.get(openid);
        if (sidDatas != null) {
            return sidDatas.get(Integer.valueOf(sid));
        }
        return null;
    }

    public List<Player> searchPlayer(String openid, int serverid) {
        List<Player> rtn = new ArrayList<>();
        if (openid == null || openid.length() <= 0) {
            return rtn;
        }
        List<String> names = new ArrayList<>(this.nameDatas.keySet());
        for (String name : names) {
            if (name.indexOf(openid) != -1) {
                rtn.add(this.nameDatas.get(name));
            }
        }
        return rtn;
    }

    public Player getPlayer(String name) {
        return this.nameDatas.get(name);
    }

    public List<Player> getAllPlayers() {
        return Lists.newArrayList(this.pidDatas.values());
    }

    public Collection<Player> getOnlinePlayers() {
        return Lists.newArrayList(this._onlinePlayersByCid.values());
    }

    public Collection<Long> getOnlinePlayersCid() {
        return Lists.newArrayList(this._onlinePlayersByCid.keySet());
    }

    public Player createPlayer(PlayerBO newBO) {
        Player ret = null;
        lock();

        if (!this.nameDatas.containsKey(newBO.getName())) {

            Player player = getPlayer(newBO.getOpenId(), newBO.getSid());
            if (player == null)

                try {

                    boolean suc = newBO.insert_sync();
                    if (suc) {
                        ret = new Player(newBO);
                        regPlayer(ret);
                    }
                } catch (Exception e) {
                    CommLog.error("数据库中已经存在名为{}的玩家", newBO.getName());
                }
        }
        unlock();

        return ret;
    }

    public void delPlayer(long cid) {
        lock();

        Player player = getPlayer(cid);
        if (player == null) {
            unlock();

            return;
        }
        if (player.getOpenId().isEmpty()) {
            Map<Integer, Player> sidDatas = this.openidSidDatas.get(player.getOpenId());
            if (sidDatas != null) {
                sidDatas.remove(Integer.valueOf(player.getPlayerBO().getSid()));
            }
            player.getPlayerBO().saveOpenId("");
        }

        unlock();
    }

    public void linkPlayer(String openId, int serverid, long pid) {
        Player player = getPlayer(pid);
        if (player == null) {
            return;
        }
        lock();
        unregPlayer(player);
        player.getPlayerBO().saveOpenId(openId);
        player.getPlayerBO().saveSid(serverid);
        regPlayer(player);
        unlock();
    }

    public synchronized boolean connectPlayer(ClientSession session, Player player) {
        ClientSession oldSession = player.getClientSession();

        if (oldSession == session) {
            return true;
        }

        if (oldSession != null) {
            oldSession.losePlayer();
        }

        Player oldPlayer = session.getPlayer();
        if (oldPlayer != null) {
            oldPlayer.loseSession();
        }

        session.bindPlayer(player);
        player.bindSession(session);
        return true;
    }

    public void onPlayerRename(long cid, String oldName, String newName) {
        lock();

        Player data = getPlayer(cid);
        if (data != null) {
            this.nameDatas.remove(oldName);
            this.nameDatas.put(newName, data);
        }

        unlock();
    }

    public void cachePlayerJson(String openid, JsonObject player_info) {
        this.playerJsons.put(openid, player_info);
    }

    public JsonObject getPlayerJson(String openid) {
        return this.playerJsons.get(openid);
    }

    public void removePlayerJson(long pid) {
        this.playerJsons.remove(Long.valueOf(pid));
    }

    public String output() {
        StringBuilder sBuilder = new StringBuilder();
        lock();

        sBuilder.append(String.format("online:%s, all:%s\n", new Object[]{Integer.valueOf(this._onlinePlayersByCid.size()), Integer.valueOf(this.pidDatas.size())}));
        sBuilder.append(String.format("%20s %10s %10s %10s %10s %10s\n", new Object[]{"cid", "sid", "pid", "isOnline", "lv", "name"}));
        for (Player player : this.pidDatas.values()) {
            sBuilder.append(String.format("%20s %10s %10s %10s %10s %10s\n", new Object[]{Long.valueOf(player.getPid()), Integer.valueOf(player.getSid()), player.getOpenId(), Boolean.valueOf(player.isOnline()),
                    Integer.valueOf(player.getLv()), player.getName()}));
        }
        sBuilder.append(String.format("online:%s, all:%s\n", new Object[]{Integer.valueOf(this._onlinePlayersByCid.size()), Integer.valueOf(this.pidDatas.size())}));

        unlock();
        return sBuilder.toString();
    }

    public String getRandomName() {
        List<RefPlayerName> rolenameList = new ArrayList<>(RefDataMgr.getAll(RefPlayerName.class).values());
        int canSeek = 10000;
        while (true) {
            int preIdx = Random.nextInt(rolenameList.size());
            int midIdx = Random.nextInt(rolenameList.size());
            int suffIdx = Random.nextInt(rolenameList.size());
            String str = String.valueOf(((RefPlayerName) rolenameList.get(preIdx)).PrefixName) + ((RefPlayerName) rolenameList.get(midIdx)).MidName + ((RefPlayerName) rolenameList.get(suffIdx)).SuffixName;
            if (!this.nameDatas.containsKey(str)) {
                return str;
            }
            if (canSeek-- <= 0) {
                return str;
            }
        }
    }

    public Player createRobot(RefRobot init) {
        String roleName = getRandomName();
        lock();
        try {
            PlayerBO bo = new PlayerBO();
            bo.setName(roleName);
            bo.setOpenId("LEGEND_ROBOT");
            bo.setSid(ServerConfig.ServerID());
            bo.setLv(init.Level);
            bo.setDungeonLv(Math.min(init.DungeonLevel, 1));
            bo.setVipLevel(init.VIP);
            Player player = createPlayer(bo);
            if (player == null) {
                return null;
            }
            RobotManager.getInstance().setRobot(player.getPid(), player);
            ((PlayerBase) player.<PlayerBase>getFeature(PlayerBase.class)).onCreate(init);
            return player;
        } catch (Exception e) {
            CommLog.error("创建角色失败出现异常", e);
            return null;
        } finally {
            unlock();
        }
    }

    public ErrorCode createRole(Player player, String name, int selectedId) {
        if (player == null) {
            return ErrorCode.Player_NotFound;
        }
        lock();

        try {
            if (this.nameDatas.containsKey(name)) {
                return ErrorCode.Player_AlreadyExist;
            }
            player.getPlayerBO().saveName(name);
            player.getPlayerBO().saveIcon(selectedId);
            this.nameDatas.put(player.getName(), player);
        } catch (Exception e) {
            CommLog.error("创建角色失败出现异常", e);
            return ErrorCode.Unknown;
        } finally {
            unlock();
        }
        unlock();

        return ErrorCode.Success;
    }

    public ErrorCode createPlayer(ClientSession session, String openid, int sid, String name, int selectedId) {
        Player player = null;
        lock();

        try {
            PlayerBO bo = new PlayerBO();
            bo.setOpenId(openid);
            bo.setSid(sid);
            bo.setName(name);
            bo.setIcon(selectedId);
            bo.setCreateTime(CommTime.nowSecond());
            initPlayerBO(bo, 0);
            player = createPlayer(bo);
            if (player == null) {
                return ErrorCode.Player_AlreadyExist;
            }
        } catch (Exception e) {
            CommLog.error("创建角色失败出现异常", e);
            return ErrorCode.Unknown;
        } finally {
            unlock();
        }
        unlock();

        connectPlayer(session, player);
        ((AccountFeature) player.<AccountFeature>getFeature(AccountFeature.class)).updateCreateRole();
        return ErrorCode.Success;
    }

    public ErrorCode createPlayerFirst(ClientSession session, String openid, int sid) {
        Player player = null;
        lock();
        try {
            PlayerBO bo = new PlayerBO();
            bo.setOpenId(openid);
            bo.setSid(sid);
            bo.setName("大侠小虾米" + openid);
            bo.setCreateTime(CommTime.nowSecond());
            initPlayerBO(bo, 0);
            player = createPlayer(bo);
            if (player == null) {
                return ErrorCode.Player_AlreadyExist;
            }
        } catch (Exception e) {
            CommLog.error("创建角色失败出现异常", e);
            return ErrorCode.Unknown;
        } finally {
            unlock();
        }
        connectPlayer(session, player);
        ((AccountFeature) player.<AccountFeature>getFeature(AccountFeature.class)).updateCreateRole();
        return ErrorCode.Success;
    }

    public void initPlayerBO(PlayerBO bo, int initType) {
        RefPlayerInit playerInit = (RefPlayerInit) RefDataMgr.get(RefPlayerInit.class, Integer.valueOf(initType));
        if (playerInit == null) {
            playerInit = (RefPlayerInit) RefDataMgr.get(RefPlayerInit.class, Integer.valueOf(0));
        }
        bo.setDungeonLv(playerInit.DungeonLevel);
        bo.setLv(playerInit.Level);
        bo.setExp(playerInit.Exp);
        bo.setCrystal(playerInit.Crystal);
        bo.setGold(playerInit.Gold);
        bo.setVipLevel(playerInit.VipLevel);
        bo.setGmLevel(playerInit.GmLevel);
        bo.setWarspiritLv(playerInit.WarspiritLv);
    }

    public void notifyAll2World() {
        if (!"on".equalsIgnoreCase(System.getProperty("GameServer.ZoneOpen"))) {
            return;
        }
        List<Player> playerList = new ArrayList<>(this.pidDatas.values());
        for (Player t : playerList)
            tryNotify(t);
    }

    public void tryNotify(Player data) {
        int levelLimit = RefDataMgr.getFactor("ZoneServer_LevelLimit", 30);
        if (data.getLv() < levelLimit) {
            return;
        }
        if (!this.toNotifyWorld.contains(data)) {
            try {
                this.toNotifyWorld.put(data);
            } catch (InterruptedException e) {
                CommLog.error(e.getMessage(), e);
            }
        }
    }

    public void tryNotifyNoLimit(Player data) {
        if (!this.toNotifyWorld.contains(data)) {
            try {
                this.toNotifyWorld.put(data);
            } catch (InterruptedException e) {
                CommLog.error(e.getMessage(), e);
            }
        }
    }

    public void startSync2Zone() {
        SyncTaskManager.task(new SyncTask() {
            public void run() {
                if (!ZoneConnector.getInstance().isLogined()) {
                    SyncTaskManager.task(this, 2000);
                    return;
                }
                List<Player.PlayerInfo> sendList = new ArrayList<>();

                for (int index = 1; index <= 100 &&
                        PlayerMgr.this.toNotifyWorld.size() > 0; index++) {

                    try {

                        Player data = PlayerMgr.this.toNotifyWorld.take();

                        if (data.getPid() != -1L) {
                            sendList.add(((PlayerBase) data.<PlayerBase>getFeature(PlayerBase.class)).zoneProto());
                        }
                    } catch (InterruptedException e) {
                        CommLog.error(e.getMessage(), e);
                    }
                }
                if (sendList.size() > 0) {
                    ZoneConnector.getInstance().notifyMessage("base.playerinfo", new Player.G_PlayerInfo(sendList));
                }
                int todeal = PlayerMgr.this.toNotifyWorld.size();
                if (todeal > 0) {
                    SyncTaskManager.task(this);
                } else {
                    SyncTaskManager.task(this, 5000);
                }
            }
        });
    }

    public void releasPlayer(int activeTime) {
        for (Player player : getAllPlayers()) {
            player.releaseFeature(activeTime);
        }
    }

    public List<Player> randomLoadOnlinePlayersSync(int count, Collection<Long> exceptCids) {
        List<Player> randomOnlinePlayers = Lists.newArrayList();
        for (Player player : this.pidDatas.values()) {
            if (exceptCids.contains(Long.valueOf(player.getPid()))) {
                continue;
            }
            randomOnlinePlayers.add(player);
        }
        if (randomOnlinePlayers.size() <= count) {
            return randomOnlinePlayers;
        }
        List<Player> listPlayers = Lists.newArrayList();
        int randIndex = CommMath.randomInt(randomOnlinePlayers.size() - count);
        for (int i = randIndex; i < randomOnlinePlayers.size(); i++) {
            Player player = randomOnlinePlayers.get(i);
            listPlayers.add(player);
            if (listPlayers.size() == count) {
                break;
            }
        }
        return listPlayers;
    }

    public List<Player> randomLoadSexPlayers(int sex, int count, Collection<Long> exceptCids) {
        List<Player> randomOnlinePlayers = Lists.newArrayList();
        for (Player player : this.sexPlayers.values()) {

            if (exceptCids.contains(Long.valueOf(player.getPid()))) {
                continue;
            }

            if (((MarryFeature) player.<MarryFeature>getFeature(MarryFeature.class)).getSex() == sex) {
                continue;
            }

            if (((MarryFeature) player.<MarryFeature>getFeature(MarryFeature.class)).isMarried()) {
                continue;
            }

            randomOnlinePlayers.add(player);
        }
        if (randomOnlinePlayers.size() <= count) {
            return randomOnlinePlayers;
        }
        List<Player> listPlayers = Lists.newArrayList();
        Collections.shuffle(randomOnlinePlayers);
        for (int i = 0; i < randomOnlinePlayers.size(); i++) {
            Player player = randomOnlinePlayers.get(i);
            listPlayers.add(player);
            if (listPlayers.size() == count) {
                break;
            }
        }
        return listPlayers;
    }

    public void kickoutAllPlayer() {
        for (Player player : getInstance().getOnlinePlayers()) {
            player.pushProto("kickout", "");
            player.loseSession();
        }
    }
}

