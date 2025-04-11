import BaseCommon.CommLog;
import BaseTask.AsynTask.AsyncTaskManager;
import BaseTask.SyncTask.SyncTaskManager;
import business.player.Player;
import business.player.PlayerMgr;
import com.google.gson.JsonObject;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.http.client.HttpUtil;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefAchievement;
import core.database.game.bo.PlayerBO;
import core.server.GameServer;

public class d {
    public static void time() {
        System.out.println(CommTime.getTodayZeroClockS());
        System.out.println(CommTime.getTodayZeroClockS() + 2592000);
        System.out.println(CommTime.getTodayZeroClockS() + 2678400);
    }

    public static void t() throws Exception {
        int MB = 1049600;
        long total = Runtime.getRuntime().totalMemory() / MB;
        long free = Runtime.getRuntime().freeMemory() / MB;
        long max = Runtime.getRuntime().maxMemory() / MB;
        long usable = max - total + free;
        CommLog.info(String.format("MemoryMonitor total：%10sMB free：%10sMB maxavail：%10sMB useable：%10sMB", new Object[]{Long.valueOf(total), Long.valueOf(free), Long.valueOf(max), Long.valueOf(usable)}));
    }

    public static void gc(int time) throws Exception {
        PlayerMgr.getInstance().releasPlayer(time);
        System.gc();
        t();
    }

    public static void t2(int achieveID, int count) throws Exception {
        Player player = PlayerMgr.getInstance().getPlayer(2750000000000082L);
        if (player == null) {
            return;
        }
        RefAchievement ref = (RefAchievement) RefDataMgr.get(RefAchievement.class, Integer.valueOf(achieveID));
        if (ref == null) {
            return;
        }
    }

    public static void send() {
        JsonObject param = new JsonObject();
        param.addProperty("cid", "11");
        param.addProperty("buntime", "-1");
        HttpUtil.sendHttpPost2Web(3000, 3000, "http:
    }

    public static void taskamount() {
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("ThreadMonitor\n");
        sBuilder.append(SyncTaskManager.getInstance().toString());
        sBuilder.append(AsyncTaskManager.getInstance().toString());
        CommLog.warn(sBuilder.toString());
    }

    public static void reload() {
        GameServer.reload();
    }

    public static void player() {
        PlayerBO newBO = new PlayerBO();
        newBO.setOpenId("new" + CommTime.nowMS());
        newBO.setSid(1);
        newBO.setName("123");
        PlayerMgr.getInstance().initPlayerBO(newBO, 1);
        PlayerMgr.getInstance().createPlayer(newBO);
    }

    public static void player(int cid) {
        CommLog.error(PlayerMgr.getInstance().getOnlinePlayers().size());
    }
}

