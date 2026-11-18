package business.global.activity;

import BaseCommon.CommClass;
import BaseCommon.CommLog;
import BaseTask.SyncTask.SyncTask;
import BaseTask.SyncTask.SyncTaskManager;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.enums.ActivityStatus;
import com.zhonglian.server.common.enums.ActivityType;
import com.zhonglian.server.common.utils.CommTime;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefActivity;
import core.database.world.bo.ActivityBO;
import core.database.world.bo.ActivityRecordBO;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class ActivityMgr {
    private static ActivityMgr instance = new ActivityMgr();
    public Map<ActivityType, List<Activity>> activities = new HashMap<>();
    private static Map<String, ActivityType> calss2Type = new HashMap<>();
    private static Map<ActivityType, String> type2Class = new HashMap<>();
    public static Comparator<Activity> Sorter;

    static {
        for (Class<?> a : CommClass.getAllClassByInterface(Activity.class, "business.global.activity.detail")) {
            try {
                Constructor<?> constructor = a.getDeclaredConstructor(ActivityBO.class);
                ActivityBO actBo = null;
                Activity base = (Activity)constructor.newInstance(actBo);
                calss2Type.put(a.getName(), base.getType());
                type2Class.put(base.getType(), a.getName());
            } catch (Exception var6) {
                CommLog.error("活动Map初始化错误", (Throwable)var6);
            }
        }

        Sorter = (left, right) -> {
            if (!left.bo.getIsActive()) {
                return 1;
            } else if (!right.bo.getIsActive()) {
                return -1;
            } else {
                int nowsec = CommTime.nowSecond();
                int leftend = left.bo.getCloseTime();
                int rightend = right.bo.getCloseTime();
                if (leftend != 0 && leftend > nowsec && rightend != 0 && rightend > nowsec) {
                    return leftend - rightend;
                } else {
                    return left.bo.getCloseTime() < nowsec && right.bo.getCloseTime() < nowsec
                        ? left.bo.getCloseTime() - right.bo.getCloseTime()
                        : right.bo.getCloseTime() - left.bo.getCloseTime();
                }
            }
        };
    }

    public static ActivityMgr getInstance() {
        return instance;
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
            String clsName = this.getActivityName(type);
            Class<? extends Activity> clazz = (Class<? extends Activity>)Class.forName(clsName);
            instance = clazz.getConstructor(ActivityBO.class).newInstance(bo);
        } catch (Exception var6) {
            CommLog.error("实例化活动错误", (Throwable)var6);
        }

        return instance;
    }

    public void init() {
        for (ActivityType type : this.getAllSurportActivity()) {
            this.activities.put(type, new ArrayList<>());
        }

        for (ActivityBO bo : BM.getBM(ActivityBO.class).findAll()) {
            if (bo.getGmId() == null || bo.getGmId().trim().isEmpty()) {
                bo.saveGmId("game_default");
            }

            ActivityType type = ActivityType.valueOf(bo.getActivity());
            Activity instance = this.instanceACT(bo);
            this.activities.get(type).add(instance);
        }

        this.activities.forEach((key, list) -> {
            if (list.size() == 0) {
                RefActivity ref = RefDataMgr.get(RefActivity.class, key);
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
                Activity instance = this.instanceACT(bo);
                list.add(instance);
            } else if (list.size() == 1) {
                RefActivity ref = RefDataMgr.get(RefActivity.class, key);
                ActivityBO bo = list.get(0).bo;
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

        for (ActivityRecordBO bo : BM.getBM(ActivityRecordBO.class).findAll()) {
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
            } else {
                find.playerActRecords.put(bo.getPid(), bo);
            }
        }

        this.activities.forEach((key, list) -> list.forEach(x -> {
            try {
                x.load();
            } catch (Exception var3) {
                CommLog.error("活动[{}][{}]初始化失败", key, x.bo.getGmId(), var3);
            }
        }));
        final int MS_PER_MIN = 30000;
        Long nextMin = MS_PER_MIN - CommTime.nowMS() % MS_PER_MIN;
        SyncTaskManager.task(new SyncTask() {
            @Override
            public void run() {
                ActivityMgr.this.activityStatusCheck();
                SyncTaskManager.task(this, MS_PER_MIN);
            }
        }, nextMin);
    }

    public void activityStatusCheck() {
        for (Entry<ActivityType, List<Activity>> list : this.activities.entrySet()) {
            ActivityType type = list.getKey();

            try {
                Activity activity = list.getValue().get(0);
                ActivityBO bo = activity.bo;
                ActivityStatus curStatus = activity.getStatus();
                if (curStatus != ActivityStatus.Inactive) {
                    ActivityStatus preStatus = ActivityStatus.Inactive;
                    if (bo.getLastStatus() != null && !bo.getLastStatus().trim().isEmpty()) {
                        preStatus = ActivityStatus.valueOf(bo.getLastStatus());
                    }

                    if (preStatus != curStatus) {
                        if (curStatus == ActivityStatus.Open) {
                            activity.onOpen();
                        } else if (curStatus == ActivityStatus.End) {
                            if (preStatus == ActivityStatus.Open) {
                                activity.onEnd();
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
                        list.getValue().sort(Sorter);
                    }
                }
            } catch (Exception var8) {
                CommLog.error("活动{}状态检测出现错误", type, var8);
            }
        }
    }

    public List<Activity> getCurActivities() {
        List<Activity> activityBases = new ArrayList<>();
        this.activities.forEach((x, y) -> {
            Activity activityBase = y.get(0);
            ActivityStatus status = activityBase.getStatus();
            if (status != ActivityStatus.Close && status != ActivityStatus.Inactive) {
                activityBases.add(activityBase);
            }
        });
        return activityBases;
    }

    public static <ACT extends Activity> ACT getActivity(Class<ACT> clazz) {
        ActivityType type = calss2Type.get(clazz.getName());
        List<Activity> actList = getInstance().activities.get(type);
        return (ACT)actList.get(0);
    }

    public static <ACT extends Activity> ACT getLastActivity(Class<ACT> clazz) {
        ActivityType type = calss2Type.get(clazz.getName());
        List<Activity> actList = getInstance().activities.get(type);
        return (ACT)(actList.size() > 1 ? actList.get(1) : actList.get(0));
    }

    public static Activity getActivityByType(ActivityType type) {
        List<Activity> actList = getInstance().activities.get(type);
        return actList.get(0);
    }

    public Activity getActivity(ActivityType type) {
        List<Activity> actList = getInstance().activities.get(type);
        return actList.get(0);
    }
}
