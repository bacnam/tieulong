package business.global.activity;

import BaseCommon.CommClass;
import BaseCommon.CommLog;
import BaseTask.SyncTask.SyncTask;
import BaseTask.SyncTask.SyncTaskManager;
import business.player.Player;
import com.zhonglian.server.common.Config;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.enums.ActivityStatus;
import com.zhonglian.server.common.enums.ActivityType;
import com.zhonglian.server.common.enums.RankType;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.websocket.exception.WSException;
import com.zhonglian.server.websocket.handler.response.ResponseHandler;
import com.zhonglian.server.websocket.handler.response.WebSocketResponse;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefActivity;
import core.database.game.bo.ActivityBO;
import core.database.game.bo.ActivityRecordBO;
import core.network.game2world.WorldConnector;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.*;

public class ActivityMgr {
    public static Comparator<Activity> Sorter;
    private static ActivityMgr instance = new ActivityMgr();
    private static Map<String, ActivityType> calss2Type = new HashMap<>();
    private static Map<ActivityType, String> type2Class = new HashMap<>();

    static {
        List<Class<?>> bases = CommClass.getAllClassByInterface(Activity.class, "business.global.activity.detail");
        for (Class<?> a : bases) {
            try {
                Constructor<?> constructor = a.getDeclaredConstructor(new Class[]{ActivityBO.class});
                ActivityBO actBo = null;
                Activity base = (Activity) constructor.newInstance(new Object[]{actBo});
                calss2Type.put(a.getName(), base.getType());
                type2Class.put(base.getType(), a.getName());
            } catch (Exception e) {
                CommLog.error("活动Map初始化错误", e);
            }
        }

        Sorter = ((left, right) -> {
            if (!left.bo.getIsActive()) {
                return 1;
            }
            if (!right.bo.getIsActive()) {
                return -1;
            }
            int nowsec = CommTime.nowSecond();
            int leftend = left.bo.getCloseTime();
            int rightend = right.bo.getCloseTime();

            return (leftend != 0 && leftend > nowsec && rightend != 0 && rightend > nowsec) ? (leftend - rightend) : ((left.bo.getCloseTime() < nowsec && right.bo.getCloseTime() < nowsec) ? (left.bo.getCloseTime() - right.bo.getCloseTime()) : (right.bo.getCloseTime() - left.bo.getCloseTime()));
        });
    }

    public Map<ActivityType, List<Activity>> activities = new HashMap<>();
    public long pid;
    public int icon;
    public String name;
    public int lv;
    public int vipLv;
    public long guildID;
    public String guildName;
    public int serverId;
    public int maxPower;

    public static ActivityMgr getInstance() {
        return instance;
    }

    public static <ACT extends Activity> ACT getActivity(Class<ACT> clazz) {
        ActivityType type = calss2Type.get(clazz.getName());
        List<Activity> actList = (getInstance()).activities.get(type);
        return (ACT) actList.get(0);
    }

    public static <ACT extends Activity> ACT getLastActivity(Class<ACT> clazz) {
        ActivityType type = calss2Type.get(clazz.getName());

        List<Activity> actList = (getInstance()).activities.get(type);

        if (actList.size() > 1) {

            return (ACT) actList.get(1);
        }

        return (ACT) actList.get(0);
    }

    public static Activity getActivityByType(ActivityType type) {
        List<Activity> actList = (getInstance()).activities.get(type);
        return actList.get(0);
    }

    public static void updateWorldRank(Player player, long value, RankType rankType) {
        if (!WorldConnector.getInstance().isConnected()) {
            return;
        }
        Player info = new Player();
        info.pid = player.getPid();
        info.serverId = Config.ServerID();
        WorldRankRequest request = new WorldRankRequest();
        request.player = info;
        request.value = value;
        request.rankType = rankType;

        WorldConnector.request("activity.RankActivityUpdate", request, new ResponseHandler() {
            public void handleResponse(WebSocketResponse ssresponse, String body) throws WSException, IOException {
            }

            public void handleError(WebSocketResponse ssresponse, short statusCode, String message) {
            }
        });
    }

    public String getActivityName(ActivityType type) {
        return type2Class.get(type);
    }

    public Collection<ActivityType> getAllSurportActivity() {
        return calss2Type.values();
    }

    private Activity instanceACT(ActivityBO bo) {
        ActivityType type = ActivityType.valueOf(bo.getActivity());
        Activity instance = null;
        try {
            String clsName = getActivityName(type);
            Class<? extends Activity> clazz = (Class) Class.forName(clsName);
            instance = clazz.getConstructor(new Class[]{ActivityBO.class}).newInstance(new Object[]{bo});
        } catch (Exception e) {
            CommLog.error("实例化活动错误", e);
        }
        return instance;
    }

    public void activityStatusCheck() {
        for (Map.Entry<ActivityType, List<Activity>> list : this.activities.entrySet()) {

            ActivityType type = list.getKey();
            try {
                Activity activity = ((List<Activity>) list.getValue()).get(0);
                ActivityBO bo = activity.bo;
                ActivityStatus curStatus = activity.getStatus();

                if (curStatus == ActivityStatus.Inactive) {
                    continue;
                }

                ActivityStatus preStatus = ActivityStatus.Inactive;

                if (bo.getLastStatus() != null && !bo.getLastStatus().trim().isEmpty()) {
                    preStatus = ActivityStatus.valueOf(bo.getLastStatus());
                }

                if (preStatus == curStatus) {
                    continue;
                }

                if (curStatus == ActivityStatus.Open) {
                    activity.onOpen();
                } else if (curStatus == ActivityStatus.End) {
                    if (preStatus == ActivityStatus.Open) {
                        activity.onEnd();
                    } else {

                    }
                } else if (curStatus == ActivityStatus.Close) {
                    if (preStatus == ActivityStatus.Open) {
                        activity.onEnd();
                        activity.onClosed();
                    } else if (preStatus == ActivityStatus.End) {
                        activity.onClosed();
                    }
                }
                bo.saveLastStatus(curStatus.toString());
                ((List<Activity>) list.getValue()).sort(Sorter);
            } catch (Exception e) {
                CommLog.error("活动{}状态检测出现错误", type, e);
            }
        }
    }

    public List<Activity> getCurActivities() {
        List<Activity> activityBases = new ArrayList<>();
        this.activities.forEach((x, y) -> {
            Activity activityBase = y.get(0);
            ActivityStatus status = activityBase.getStatus();
            if (status != ActivityStatus.Close && status != ActivityStatus.Inactive) {
                paramList1.add(activityBase);
            }
        });
        return activityBases;
    }

    public void init() {
        for (ActivityType type : getAllSurportActivity())
            this.activities.put(type, new ArrayList<>());
        List<ActivityBO> actList = BM.getBM(ActivityBO.class).findAll();
        for (ActivityBO bo : actList) {
            if (bo.getGmId() == null || bo.getGmId().trim().isEmpty())
                bo.saveGmId("game_default");
            ActivityType type = ActivityType.valueOf(bo.getActivity());
            Activity instance = instanceACT(bo);
            ((List<Activity>) this.activities.get(type)).add(instance);
        }
        this.activities.forEach((key, list) -> {
            if (list.size() == 0) {
                RefActivity ref = (RefActivity) RefDataMgr.get(RefActivity.class, key);
                ActivityBO bo = new ActivityBO();
                bo.setActivity(key.toString());
                if (ref != null) {
                    bo.setIsActive(ref.Open);
                    bo.setBeginTime(ref.BeginTime);
                    bo.setEndTime(ref.EndTime);
                    bo.setCloseTime(ref.CloseTime);
                    bo.setJson(ref.Json);
                }
                bo.setGmId("game_default");
                bo.insert();
                Activity instance = instanceACT(bo);
                list.add(instance);
            } else if (list.size() == 1) {
                RefActivity ref = (RefActivity) RefDataMgr.get(RefActivity.class, key);
                ActivityBO bo = ((Activity) list.get(0)).bo;
                if (ref != null && "game_default".equals(bo.getGmId())) {
                    bo.setIsActive(ref.Open);
                    bo.setBeginTime(ref.BeginTime);
                    bo.setEndTime(ref.EndTime);
                    bo.setCloseTime(ref.CloseTime);
                    bo.setJson(ref.Json);
                    bo.saveAll();
                }
            }
            list.sort(Sorter);
        });
        List<ActivityRecordBO> actRecordList = BM.getBM(ActivityRecordBO.class).findAll();
        for (ActivityRecordBO bo : actRecordList) {
            ActivityType type = ActivityType.valueOf(bo.getActivity());
            Activity find = null;
            for (Activity activity : this.activities.get(type)) {
                if (activity.bo.getId() == bo.getAid()) {
                    find = activity;
                    break;
                }
            }
            if (find == null) {
                bo.del();
                continue;
            }
            find.playerActRecords.put(Long.valueOf(bo.getPid()), bo);
        }
        this.activities.forEach((key, list) -> activities.forEach());
        final int MS_PER_MIN = 30000;
        Long nextMin = Long.valueOf(MS_PER_MIN - CommTime.nowMS() % MS_PER_MIN);
        SyncTaskManager.task(new SyncTask() {
            public void run() {
                ActivityMgr.this.activityStatusCheck();
                SyncTaskManager.task(this, MS_PER_MIN);
            }
        }, nextMin.longValue());
    }

    public Activity getActivity(ActivityType type) {
        List<Activity> actList = (getInstance()).activities.get(type);
        return actList.get(0);
    }

    private static class WorldRankRequest {
        Player player;

        long value;

        RankType rankType;

        private WorldRankRequest() {
        }
    }
}