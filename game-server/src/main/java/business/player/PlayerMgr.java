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
import com.zhonglian.server.common.utils.CommMath;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.common.utils.Lists;
import com.zhonglian.server.common.utils.Maps;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import proto.gamezone.Player.G_PlayerInfo;
import proto.gamezone.Player.PlayerInfo;

public class PlayerMgr {
    private final String EmptyPlayerPid = "";
    public final Map<Long, Player> _onlinePlayersByCid = Maps.newConcurrentMap();
    public final Map<Long, Player> pidDatas = Maps.newConcurrentMap();
    public final Map<String, Map<Integer, Player>> openidSidDatas = Maps.newConcurrentMap();
    public final Map<String, Player> nameDatas = Maps.newConcurrentMap();
    public final Map<String, JsonObject> playerJsons = Maps.newConcurrentMap();
    public final Map<String, Player> sexPlayers = Maps.newConcurrentMap();
    private final BaseMutexObject _lock = new BaseMutexObject();
    private static PlayerMgr instance = new PlayerMgr();
    private BlockingQueue<Player> toNotifyWorld = new LinkedBlockingQueue<>();

    public void lock() {
        this._lock.lock();
    }

    public void unlock() {
        this._lock.unlock();
    }

    public static PlayerMgr getInstance() {
        return instance;
    }

    private PlayerMgr() {
        this._lock.reduceMutexLevel(1);
    }

    public void init() {
        CommLog.info("[PlayerMgr.init] load player begin...]");
        this.lock();

        for (PlayerBO bo : BM.getBM(PlayerBO.class).findAll()) {
            if (!bo.getName().matches("s\\d+\\..+")) {
                bo.saveName("s" + bo.getSid() + "." + bo.getName());
            }

            this.regPlayer(new Player(bo));
        }

        this.unlock();
        CommLog.info("[PlayerMgr.init] load player success, count: {}", this.pidDatas.size());

        for (MarryBO bo : BM.getBM(MarryBO.class).findAll()) {
            if (bo.getSex() != 0) {
                Player player = this.getPlayer(bo.getPid());
                this.sexPlayers.put(player.getName(), player);
            }
        }

        this.notifyAll2World();
        this.startSync2Zone();
    }

    private void regPlayer(Player player) {
        this.lock();
        long playerCid = player.getPlayerBO().getId();
        this.pidDatas.put(playerCid, player);
        this.nameDatas.put(player.getName(), player);
        if (!player.getOpenId().isEmpty()) {
            Map<Integer, Player> sidDatas = this.openidSidDatas.get(player.getOpenId());
            if (sidDatas == null) {
                sidDatas = Maps.newConcurrentMap();
                this.openidSidDatas.put(player.getOpenId(), sidDatas);
            }

            sidDatas.put(player.getPlayerBO().getSid(), player);
        }

        this.unlock();
    }

    private void unregPlayer(Player player) {
        this.lock();
        long playerCid = player.getPlayerBO().getId();
        this.pidDatas.remove(playerCid);
        this.nameDatas.remove(player.getPlayerBO().getName());
        this._onlinePlayersByCid.remove(playerCid);
        if (!player.getOpenId().isEmpty()) {
            Map<Integer, Player> sidDatas = this.openidSidDatas.get(player.getOpenId());
            if (sidDatas != null) {
                sidDatas.remove(player.getPlayerBO().getSid());
            }
        }

        this.unlock();
    }

    public void regOnlinePlayer(Player player) {
        this.lock();
        this._onlinePlayersByCid.put(player.getPid(), player);
        this.unlock();
    }

    public void unregOnlinePlayer(Player player) {
        this.lock();
        this._onlinePlayersByCid.remove(player.getPid());
        this.unlock();
    }

    public Player getOnlinePlayerByCid(long cid) {
        return this._onlinePlayersByCid.get(cid);
    }

    public Player getPlayer(long pid) {
        return this.pidDatas.get(pid);
    }

    public Player getPlayer(String openid, int sid) {
        if (openid == null || openid.length() <= 0) {
            return null;
        } else if ("LEGEND_ROBOT".equals(openid)) {
            return null;
        } else {
            Map<Integer, Player> sidDatas = this.openidSidDatas.get(openid);
            return sidDatas != null ? sidDatas.get(sid) : null;
        }
    }

    public List<Player> searchPlayer(String openid, int serverid) {
        List<Player> rtn = new ArrayList<>();
        if (openid != null && openid.length() > 0) {
            for (String name : new ArrayList<>(this.nameDatas.keySet())) {
                if (name.indexOf(openid) != -1) {
                    rtn.add(this.nameDatas.get(name));
                }
            }

            return rtn;
        } else {
            return rtn;
        }
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
        this.lock();
        if (!this.nameDatas.containsKey(newBO.getName())) {
            Player player = this.getPlayer(newBO.getOpenId(), newBO.getSid());
            if (player == null) {
                try {
                    boolean suc = newBO.insert_sync();
                    if (suc) {
                        ret = new Player(newBO);
                        this.regPlayer(ret);
                    }
                } catch (Exception var5) {
                    CommLog.error("数据库中已经存在名为{}的玩家", newBO.getName());
                }
            }
        }

        this.unlock();
        return ret;
    }

    public void delPlayer(long cid) {
        this.lock();
        Player player = this.getPlayer(cid);
        if (player == null) {
            this.unlock();
        } else {
            if (player.getOpenId().isEmpty()) {
                Map<Integer, Player> sidDatas = this.openidSidDatas.get(player.getOpenId());
                if (sidDatas != null) {
                    sidDatas.remove(player.getPlayerBO().getSid());
                }

                player.getPlayerBO().saveOpenId("");
            }

            this.unlock();
        }
    }

    public void linkPlayer(String openId, int serverid, long pid) {
        Player player = this.getPlayer(pid);
        if (player != null) {
            this.lock();
            this.unregPlayer(player);
            player.getPlayerBO().saveOpenId(openId);
            player.getPlayerBO().saveSid(serverid);
            this.regPlayer(player);
            this.unlock();
        }
    }

    public synchronized boolean connectPlayer(ClientSession session, Player player) {
        ClientSession oldSession = player.getClientSession();
        if (oldSession == session) {
            return true;
        } else {
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
    }

    public void onPlayerRename(long cid, String oldName, String newName) {
        this.lock();
        Player data = this.getPlayer(cid);
        if (data != null) {
            this.nameDatas.remove(oldName);
            this.nameDatas.put(newName, data);
        }

        this.unlock();
    }

    public void cachePlayerJson(String openid, JsonObject player_info) {
        this.playerJsons.put(openid, player_info);
    }

    public JsonObject getPlayerJson(String openid) {
        return this.playerJsons.get(openid);
    }

    public void removePlayerJson(long pid) {
        this.playerJsons.remove(pid);
    }

    public String output() {
        StringBuilder sBuilder = new StringBuilder();
        this.lock();
        sBuilder.append(String.format("online:%s, all:%s\n", this._onlinePlayersByCid.size(), this.pidDatas.size()));
        sBuilder.append(String.format("%20s %10s %10s %10s %10s %10s\n", "cid", "sid", "pid", "isOnline", "lv", "name"));

        for (Player player : this.pidDatas.values()) {
            sBuilder.append(
                String.format(
                    "%20s %10s %10s %10s %10s %10s\n",
                    player.getPid(),
                    player.getSid(),
                    player.getOpenId(),
                    player.isOnline(),
                    player.getLv(),
                    player.getName()
                )
            );
        }

        sBuilder.append(String.format("online:%s, all:%s\n", this._onlinePlayersByCid.size(), this.pidDatas.size()));
        this.unlock();
        return sBuilder.toString();
    }

    public String getRandomName() {
        List<RefPlayerName> rolenameList = new ArrayList<>(RefDataMgr.getAll(RefPlayerName.class).values());
        int canSeek = 10000;

        String str;
        do {
            int preIdx = Random.nextInt(rolenameList.size());
            int midIdx = Random.nextInt(rolenameList.size());
            int suffIdx = Random.nextInt(rolenameList.size());
            str = rolenameList.get(preIdx).PrefixName + rolenameList.get(midIdx).MidName + rolenameList.get(suffIdx).SuffixName;
            if (!this.nameDatas.containsKey(str)) {
                return str;
            }
        } while (canSeek-- > 0);

        return str;
    }

    public Player createRobot(RefRobot init) {
        String roleName = this.getRandomName();
        this.lock();

        try {
            PlayerBO bo = new PlayerBO();
            bo.setName(roleName);
            bo.setOpenId("LEGEND_ROBOT");
            bo.setSid(ServerConfig.ServerID());
            bo.setLv(init.Level);
            bo.setDungeonLv(Math.min(init.DungeonLevel, 1));
            bo.setVipLevel(init.VIP);
            Player player = this.createPlayer(bo);
            if (player != null) {
                RobotManager.getInstance().setRobot(player.getPid(), player);
                player.getFeature(PlayerBase.class).onCreate(init);
                return player;
            }
        } catch (Exception var9) {
            CommLog.error("创建角色失败出现异常", (Throwable)var9);
            return null;
        } finally {
            this.unlock();
        }

        return null;
    }

    public ErrorCode createRole(Player player, String name, int selectedId) {
        if (player == null) {
            return ErrorCode.Player_NotFound;
        } else {
            this.lock();

            ErrorCode var11;
            try {
                if (!this.nameDatas.containsKey(name)) {
                    player.getPlayerBO().saveName(name);
                    player.getPlayerBO().saveIcon(selectedId);
                    this.nameDatas.put(player.getName(), player);
                    player.getFeature(PlayerBase.class).onCreate(selectedId);
                    return ErrorCode.Success;
                }

                var11 = ErrorCode.Player_AlreadyExist;
            } catch (Exception var9) {
                CommLog.error("创建角色失败出现异常", (Throwable)var9);
                return ErrorCode.Unknown;
            } finally {
                this.unlock();
            }

            return var11;
        }
    }

    public ErrorCode createPlayer(ClientSession session, String openid, int sid, String name, int selectedId) {
        Player player = null;
        this.lock();

        label45: {
            ErrorCode var15;
            try {
                PlayerBO bo = new PlayerBO();
                bo.setOpenId(openid);
                bo.setSid(sid);
                bo.setName(name);
                bo.setIcon(selectedId);
                bo.setCreateTime(CommTime.nowSecond());
                this.initPlayerBO(bo, 0);
                player = this.createPlayer(bo);
                if (player != null) {
                    player.getFeature(PlayerBase.class).onCreate(selectedId);
                    break label45;
                }

                var15 = ErrorCode.Player_AlreadyExist;
            } catch (Exception var12) {
                CommLog.error("创建角色失败出现异常", (Throwable)var12);
                return ErrorCode.Unknown;
            } finally {
                this.unlock();
            }

            return var15;
        }

        this.connectPlayer(session, player);
        player.getFeature(AccountFeature.class).updateCreateRole();
        return ErrorCode.Success;
    }

    public ErrorCode createPlayerFirst(ClientSession session, String openid, int sid) {
        Player player = null;
        this.lock();

        try {
            PlayerBO bo = new PlayerBO();
            bo.setOpenId(openid);
            bo.setSid(sid);
            bo.setName("大侠小虾米" + openid);
            bo.setCreateTime(CommTime.nowSecond());
            this.initPlayerBO(bo, 0);
            player = this.createPlayer(bo);
            if (player == null) {
                return ErrorCode.Player_AlreadyExist;
            }
        } catch (Exception var10) {
            CommLog.error("创建角色失败出现异常", (Throwable)var10);
            return ErrorCode.Unknown;
        } finally {
            this.unlock();
        }

        this.connectPlayer(session, player);
        player.getFeature(AccountFeature.class).updateCreateRole();
        return ErrorCode.Success;
    }

    public void initPlayerBO(PlayerBO bo, int initType) {
        RefPlayerInit playerInit = RefDataMgr.get(RefPlayerInit.class, initType);
        if (playerInit == null) {
            playerInit = RefDataMgr.get(RefPlayerInit.class, 0);
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
        if ("on".equalsIgnoreCase(System.getProperty("GameServer.ZoneOpen"))) {
            for (Player t : new ArrayList<>(this.pidDatas.values())) {
                this.tryNotify(t);
            }
        }
    }

    public void tryNotify(Player data) {
        int levelLimit = RefDataMgr.getFactor("ZoneServer_LevelLimit", 30);
        if (data.getLv() >= levelLimit) {
            if (!this.toNotifyWorld.contains(data)) {
                try {
                    this.toNotifyWorld.put(data);
                } catch (InterruptedException var4) {
                    CommLog.error(var4.getMessage(), (Throwable)var4);
                }
            }
        }
    }

    public void tryNotifyNoLimit(Player data) {
        if (!this.toNotifyWorld.contains(data)) {
            try {
                this.toNotifyWorld.put(data);
            } catch (InterruptedException var3) {
                CommLog.error(var3.getMessage(), (Throwable)var3);
            }
        }
    }

    public void startSync2Zone() {
        SyncTaskManager.task(new SyncTask() {
            @Override
            public void run() {
                if (!ZoneConnector.getInstance().isLogined()) {
                    SyncTaskManager.task(this, 2000);
                } else {
                    List<PlayerInfo> sendList = new ArrayList<>();

                    for (int index = 1; index <= 100 && PlayerMgr.this.toNotifyWorld.size() > 0; index++) {
                        try {
                            Player data = PlayerMgr.this.toNotifyWorld.take();
                            if (data.getPid() != -1L) {
                                sendList.add(data.getFeature(PlayerBase.class).zoneProto());
                            }
                        } catch (InterruptedException var4) {
                            CommLog.error(var4.getMessage(), (Throwable)var4);
                        }
                    }

                    if (sendList.size() > 0) {
                        ZoneConnector.getInstance().notifyMessage("base.playerinfo", new G_PlayerInfo(sendList));
                    }

                    int todeal = PlayerMgr.this.toNotifyWorld.size();
                    if (todeal > 0) {
                        SyncTaskManager.task(this);
                    } else {
                        SyncTaskManager.task(this, 5000);
                    }
                }
            }
        });
    }

    public void releasPlayer(int activeTime) {
        for (Player player : this.getAllPlayers()) {
            player.releaseFeature(activeTime);
        }
    }

    public List<Player> randomLoadOnlinePlayersSync(int count, Collection<Long> exceptCids) {
        List<Player> randomOnlinePlayers = Lists.newArrayList();

        for (Player player : this.pidDatas.values()) {
            if (!exceptCids.contains(player.getPid())) {
                randomOnlinePlayers.add(player);
            }
        }

        if (randomOnlinePlayers.size() <= count) {
            return randomOnlinePlayers;
        } else {
            List<Player> listPlayers = Lists.newArrayList();
            int randIndex = CommMath.randomInt(randomOnlinePlayers.size() - count);

            for (int i = randIndex; i < randomOnlinePlayers.size(); i++) {
                Player playerx = randomOnlinePlayers.get(i);
                listPlayers.add(playerx);
                if (listPlayers.size() == count) {
                    break;
                }
            }

            return listPlayers;
        }
    }

    public List<Player> randomLoadSexPlayers(int sex, int count, Collection<Long> exceptCids) {
        List<Player> randomOnlinePlayers = Lists.newArrayList();

        for (Player player : this.sexPlayers.values()) {
            if (!exceptCids.contains(player.getPid())
                && player.getFeature(MarryFeature.class).getSex() != sex
                && !player.getFeature(MarryFeature.class).isMarried()) {
                randomOnlinePlayers.add(player);
            }
        }

        if (randomOnlinePlayers.size() <= count) {
            return randomOnlinePlayers;
        } else {
            List<Player> listPlayers = Lists.newArrayList();
            Collections.shuffle(randomOnlinePlayers);

            for (int i = 0; i < randomOnlinePlayers.size(); i++) {
                Player playerx = randomOnlinePlayers.get(i);
                listPlayers.add(playerx);
                if (listPlayers.size() == count) {
                    break;
                }
            }

            return listPlayers;
        }
    }

    public void kickoutAllPlayer() {
        for (Player player : getInstance().getOnlinePlayers()) {
            player.pushProto("kickout", "");
            player.loseSession();
        }
    }
}
