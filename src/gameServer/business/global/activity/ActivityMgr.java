/*     */ package business.global.activity;
/*     */ 
/*     */ import BaseCommon.CommClass;
/*     */ import BaseCommon.CommLog;
/*     */ import BaseTask.SyncTask.SyncTask;
/*     */ import BaseTask.SyncTask.SyncTaskManager;
/*     */ import business.player.Player;
/*     */ import com.zhonglian.server.common.Config;
/*     */ import com.zhonglian.server.common.db.BM;
/*     */ import com.zhonglian.server.common.enums.ActivityStatus;
/*     */ import com.zhonglian.server.common.enums.ActivityType;
/*     */ import com.zhonglian.server.common.enums.RankType;
/*     */ import com.zhonglian.server.common.utils.CommTime;
/*     */ import com.zhonglian.server.websocket.exception.WSException;
/*     */ import com.zhonglian.server.websocket.handler.response.ResponseHandler;
/*     */ import com.zhonglian.server.websocket.handler.response.WebSocketResponse;
/*     */ import core.config.refdata.RefDataMgr;
/*     */ import core.config.refdata.ref.RefActivity;
/*     */ import core.database.game.bo.ActivityBO;
/*     */ import core.database.game.bo.ActivityRecordBO;
/*     */ import core.network.game2world.WorldConnector;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import proto.gamezone.Player;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ActivityMgr
/*     */ {
/*  37 */   private static ActivityMgr instance = new ActivityMgr();
/*     */   
/*     */   public static ActivityMgr getInstance() {
/*  40 */     return instance;
/*     */   }
/*     */ 
/*     */   
/*  44 */   public Map<ActivityType, List<Activity>> activities = new HashMap<>();
/*     */   
/*  46 */   private static Map<String, ActivityType> calss2Type = new HashMap<>();
/*  47 */   private static Map<ActivityType, String> type2Class = new HashMap<>(); public static Comparator<Activity> Sorter; public long pid; public int icon; public String name; public int lv; public int vipLv; public long guildID; public String guildName; public int serverId; public int maxPower;
/*     */   
/*     */   public String getActivityName(ActivityType type) {
/*  50 */     return type2Class.get(type);
/*     */   }
/*     */   
/*     */   public Collection<ActivityType> getAllSurportActivity() {
/*  54 */     return calss2Type.values();
/*     */   }
/*     */   
/*     */   static
/*     */   {
/*  59 */     List<Class<?>> bases = CommClass.getAllClassByInterface(Activity.class, "business.global.activity.detail");
/*  60 */     for (Class<?> a : bases) {
/*     */       try {
/*  62 */         Constructor<?> constructor = a.getDeclaredConstructor(new Class[] { ActivityBO.class });
/*  63 */         ActivityBO actBo = null;
/*  64 */         Activity base = (Activity)constructor.newInstance(new Object[] { actBo });
/*  65 */         calss2Type.put(a.getName(), base.getType());
/*  66 */         type2Class.put(base.getType(), a.getName());
/*  67 */       } catch (Exception e) {
/*  68 */         CommLog.error("活动Map初始化错误", e);
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 177 */     Sorter = ((left, right) -> {
/*     */         if (!left.bo.getIsActive()) {
/*     */           return 1;
/*     */         }
/*     */         if (!right.bo.getIsActive()) {
/*     */           return -1;
/*     */         }
/*     */         int nowsec = CommTime.nowSecond();
/*     */         int leftend = left.bo.getCloseTime();
/*     */         int rightend = right.bo.getCloseTime();
/* 187 */         return (leftend != 0 && leftend > nowsec && rightend != 0 && rightend > nowsec) ? (leftend - rightend) : (
/*     */           
/* 189 */           (left.bo.getCloseTime() < nowsec && right.bo.getCloseTime() < nowsec) ? (left.bo.getCloseTime() - right.bo.getCloseTime()) : (right.bo.getCloseTime() - left.bo.getCloseTime()));
/*     */       }); } private Activity instanceACT(ActivityBO bo) { ActivityType type = ActivityType.valueOf(bo.getActivity()); Activity instance = null; try {
/*     */       String clsName = getActivityName(type); Class<? extends Activity> clazz = (Class)Class.forName(clsName);
/*     */       instance = clazz.getConstructor(new Class[] { ActivityBO.class }).newInstance(new Object[] { bo });
/*     */     } catch (Exception e) {
/*     */       CommLog.error("实例化活动错误", e);
/*     */     } 
/*     */     return instance; }
/* 197 */   public void activityStatusCheck() { for (Map.Entry<ActivityType, List<Activity>> list : this.activities.entrySet()) {
/* 198 */       ActivityType type = list.getKey();
/*     */       try {
/* 200 */         Activity activity = ((List<Activity>)list.getValue()).get(0);
/* 201 */         ActivityBO bo = activity.bo;
/* 202 */         ActivityStatus curStatus = activity.getStatus();
/*     */         
/* 204 */         if (curStatus == ActivityStatus.Inactive) {
/*     */           continue;
/*     */         }
/* 207 */         ActivityStatus preStatus = ActivityStatus.Inactive;
/* 208 */         if (bo.getLastStatus() != null && !bo.getLastStatus().trim().isEmpty()) {
/* 209 */           preStatus = ActivityStatus.valueOf(bo.getLastStatus());
/*     */         }
/*     */         
/* 212 */         if (preStatus == curStatus) {
/*     */           continue;
/*     */         }
/*     */         
/* 216 */         if (curStatus == ActivityStatus.Open) {
/* 217 */           activity.onOpen();
/* 218 */         } else if (curStatus == ActivityStatus.End) {
/* 219 */           if (preStatus == ActivityStatus.Open) {
/* 220 */             activity.onEnd();
/*     */           } else {
/*     */           
/*     */           } 
/* 224 */         } else if (curStatus == ActivityStatus.Close) {
/* 225 */           if (preStatus == ActivityStatus.Open) {
/* 226 */             activity.onEnd();
/* 227 */             activity.onClosed();
/* 228 */           } else if (preStatus == ActivityStatus.End) {
/* 229 */             activity.onClosed();
/*     */           } 
/*     */         } 
/* 232 */         bo.saveLastStatus(curStatus.toString());
/* 233 */         ((List<Activity>)list.getValue()).sort(Sorter);
/* 234 */       } catch (Exception e) {
/* 235 */         CommLog.error("活动{}状态检测出现错误", type, e);
/*     */       } 
/*     */     }  }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Activity> getCurActivities() {
/* 246 */     List<Activity> activityBases = new ArrayList<>();
/* 247 */     this.activities.forEach((x, y) -> {
/*     */           Activity activityBase = y.get(0);
/*     */           ActivityStatus status = activityBase.getStatus();
/*     */           if (status != ActivityStatus.Close && status != ActivityStatus.Inactive) {
/*     */             paramList1.add(activityBase);
/*     */           }
/*     */         });
/* 254 */     return activityBases;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <ACT extends Activity> ACT getActivity(Class<ACT> clazz) {
/* 265 */     ActivityType type = calss2Type.get(clazz.getName());
/* 266 */     List<Activity> actList = (getInstance()).activities.get(type);
/* 267 */     return (ACT)actList.get(0);
/*     */   } public void init() { for (ActivityType type : getAllSurportActivity())
/*     */       this.activities.put(type, new ArrayList<>());  List<ActivityBO> actList = BM.getBM(ActivityBO.class).findAll(); for (ActivityBO bo : actList) { if (bo.getGmId() == null || bo.getGmId().trim().isEmpty())
/*     */         bo.saveGmId("game_default");  ActivityType type = ActivityType.valueOf(bo.getActivity()); Activity instance = instanceACT(bo); ((List<Activity>)this.activities.get(type)).add(instance); }  this.activities.forEach((key, list) -> { if (list.size() == 0) { RefActivity ref = (RefActivity)RefDataMgr.get(RefActivity.class, key); ActivityBO bo = new ActivityBO(); bo.setActivity(key.toString()); if (ref != null) { bo.setIsActive(ref.Open); bo.setBeginTime(ref.BeginTime); bo.setEndTime(ref.EndTime); bo.setCloseTime(ref.CloseTime); bo.setJson(ref.Json); }  bo.setGmId("game_default"); bo.insert(); Activity instance = instanceACT(bo); list.add(instance); } else if (list.size() == 1) { RefActivity ref = (RefActivity)RefDataMgr.get(RefActivity.class, key); ActivityBO bo = ((Activity)list.get(0)).bo; if (ref != null && "game_default".equals(bo.getGmId())) { bo.setIsActive(ref.Open); bo.setBeginTime(ref.BeginTime); bo.setEndTime(ref.EndTime); bo.setCloseTime(ref.CloseTime); bo.setJson(ref.Json); bo.saveAll(); }
/*     */              }
/*     */            list.sort(Sorter);
/*     */         }); List<ActivityRecordBO> actRecordList = BM.getBM(ActivityRecordBO.class).findAll(); for (ActivityRecordBO bo : actRecordList) { ActivityType type = ActivityType.valueOf(bo.getActivity()); Activity find = null; for (Activity activity : this.activities.get(type)) { if (activity.bo.getId() == bo.getAid()) { find = activity; break; }
/*     */          }
/*     */        if (find == null) { bo.del(); continue; }
/*     */        find.playerActRecords.put(Long.valueOf(bo.getPid()), bo); }
/*     */      this.activities.forEach((key, list) -> list.forEach(())); final int MS_PER_MIN = 30000; Long nextMin = Long.valueOf(MS_PER_MIN - CommTime.nowMS() % MS_PER_MIN); SyncTaskManager.task(new SyncTask() { public void run() { ActivityMgr.this.activityStatusCheck(); SyncTaskManager.task(this, MS_PER_MIN); } }
/* 278 */         nextMin.longValue()); } public static <ACT extends Activity> ACT getLastActivity(Class<ACT> clazz) { ActivityType type = calss2Type.get(clazz.getName());
/* 279 */     List<Activity> actList = (getInstance()).activities.get(type);
/* 280 */     if (actList.size() > 1) {
/* 281 */       return (ACT)actList.get(1);
/*     */     }
/* 283 */     return (ACT)actList.get(0); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Activity getActivityByType(ActivityType type) {
/* 293 */     List<Activity> actList = (getInstance()).activities.get(type);
/* 294 */     return actList.get(0);
/*     */   }
/*     */   
/*     */   public Activity getActivity(ActivityType type) {
/* 298 */     List<Activity> actList = (getInstance()).activities.get(type);
/* 299 */     return actList.get(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class WorldRankRequest
/*     */   {
/*     */     Player.PlayerInfo player;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     long value;
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     RankType rankType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private WorldRankRequest() {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void updateWorldRank(Player player, long value, RankType rankType) {
/* 334 */     if (!WorldConnector.getInstance().isConnected()) {
/*     */       return;
/*     */     }
/* 337 */     Player.PlayerInfo info = new Player.PlayerInfo();
/* 338 */     info.pid = player.getPid();
/* 339 */     info.serverId = Config.ServerID();
/* 340 */     WorldRankRequest request = new WorldRankRequest(null);
/* 341 */     request.player = info;
/* 342 */     request.value = value;
/* 343 */     request.rankType = rankType;
/*     */     
/* 345 */     WorldConnector.request("activity.RankActivityUpdate", request, new ResponseHandler() {
/*     */           public void handleResponse(WebSocketResponse ssresponse, String body) throws WSException, IOException {}
/*     */           
/*     */           public void handleError(WebSocketResponse ssresponse, short statusCode, String message) {}
/*     */         });
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/global/activity/ActivityMgr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */