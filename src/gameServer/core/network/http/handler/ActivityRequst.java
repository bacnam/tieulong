/*     */ package core.network.http.handler;
/*     */ 
/*     */ import business.global.activity.Activity;
/*     */ import business.global.activity.ActivityMgr;
/*     */ import business.global.notice.NoticeMgr;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.JsonArray;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.google.gson.JsonObject;
/*     */ import com.google.gson.JsonParser;
/*     */ import com.zhonglian.server.common.Config;
/*     */ import com.zhonglian.server.common.enums.ActivityStatus;
/*     */ import com.zhonglian.server.common.enums.ActivityType;
/*     */ import com.zhonglian.server.http.annotation.RequestMapping;
/*     */ import com.zhonglian.server.http.server.HttpRequest;
/*     */ import com.zhonglian.server.http.server.HttpResponse;
/*     */ import com.zhonglian.server.http.server.HttpUtils;
/*     */ import core.database.game.bo.ActivityBO;
/*     */ import core.logger.flow.FlowLogger;
/*     */ import java.io.File;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ public class ActivityRequst
/*     */ {
/*  29 */   Gson gson = new Gson();
/*     */   
/*     */   @RequestMapping(uri = "/game/activity/index")
/*     */   public void index(HttpRequest request, HttpResponse response) {
/*  33 */     response.response(new File("conf/httpserver/activity.html"));
/*     */   }
/*     */   
/*     */   @RequestMapping(uri = "/game/activity/surport")
/*     */   public void getSurport(HttpRequest request, HttpResponse response) {
/*  38 */     String json = this.gson.toJson(ActivityMgr.getInstance().getAllSurportActivity());
/*  39 */     response.response(json);
/*     */   }
/*     */   
/*     */   @RequestMapping(uri = "/game/activity/list")
/*     */   public void list(HttpRequest request, HttpResponse response) throws Exception {
/*  44 */     JsonObject postParam = HttpUtils.abstractGMParams(request.getRequestBody());
/*  45 */     String strType = HttpUtils.getString(postParam, "activity", null);
/*  46 */     JsonArray rtnActivities = new JsonArray();
/*  47 */     Map<ActivityType, List<Activity>> activities = (ActivityMgr.getInstance()).activities;
/*  48 */     if (strType == null) {
/*  49 */       List<Activity> actList = new ArrayList<>();
/*  50 */       for (List<Activity> list : activities.values()) {
/*  51 */         actList.addAll(list);
/*     */       }
/*  53 */       actList.stream().sorted((left, right) -> left.getStatus().ordinal() - right.getStatus().ordinal());
/*     */ 
/*     */       
/*  56 */       for (Activity a : actList) {
/*  57 */         JsonObject object = new JsonObject();
/*  58 */         object.addProperty("activity", a.getType().toString());
/*  59 */         object.addProperty("state", a.getStatus().toString());
/*  60 */         object.addProperty("activityid", a.bo.getGmId());
/*  61 */         rtnActivities.add((JsonElement)object);
/*     */       } 
/*     */     } else {
/*     */       ActivityType type;
/*     */       try {
/*  66 */         type = ActivityType.valueOf(strType);
/*  67 */       } catch (Exception e) {
/*  68 */         response.error(30101, "wrong acitity type[" + strType + "]", new Object[0]);
/*     */         return;
/*     */       } 
/*  71 */       List<Activity> list = activities.get(type);
/*  72 */       list.stream().sorted((left, right) -> left.getStatus().ordinal() - right.getStatus().ordinal());
/*     */ 
/*     */       
/*  75 */       for (Activity a : list) {
/*  76 */         JsonObject object = new JsonObject();
/*  77 */         object.addProperty("activity", a.getType().toString());
/*  78 */         object.addProperty("state", a.getStatus().toString());
/*  79 */         object.addProperty("activityid", a.bo.getGmId());
/*  80 */         rtnActivities.add((JsonElement)object);
/*     */       } 
/*     */     } 
/*  83 */     JsonObject rtn = new JsonObject();
/*  84 */     rtn.addProperty("state", Integer.valueOf(1000));
/*  85 */     rtn.add("activities", (JsonElement)rtnActivities);
/*  86 */     response.response(this.gson.toJson((JsonElement)rtn));
/*     */   }
/*     */   
/*     */   @RequestMapping(uri = "/game/activity/detail")
/*     */   public void detail(HttpRequest request, HttpResponse response) throws Exception {
/*  91 */     JsonObject postParam = HttpUtils.abstractGMParams(request.getRequestBody());
/*  92 */     ActivityType type = (ActivityType)HttpUtils.getEnum(postParam, "activity", ActivityType.class);
/*  93 */     String phpId = HttpUtils.getString(postParam, "activityid");
/*  94 */     JsonObject find = null;
/*  95 */     List<Activity> list = (List<Activity>)(ActivityMgr.getInstance()).activities.get(type);
/*  96 */     for (Activity a : list) {
/*  97 */       if (phpId != null && phpId.equals(a.bo.getGmId())) {
/*     */         continue;
/*     */       }
/* 100 */       ActivityBO bo = a.bo;
/* 101 */       find = new JsonObject();
/* 102 */       find.addProperty("activity", bo.getActivity());
/* 103 */       find.addProperty("activityid", bo.getGmId());
/* 104 */       find.addProperty("active", Boolean.valueOf(bo.getIsActive()));
/* 105 */       find.addProperty("beginTime", Integer.valueOf(bo.getBeginTime()));
/* 106 */       find.addProperty("endTime", Integer.valueOf(bo.getEndTime()));
/* 107 */       find.addProperty("closeTime", Integer.valueOf(bo.getCloseTime()));
/* 108 */       find.add("config", (JsonElement)(new JsonParser()).parse(bo.getJson()).getAsJsonObject());
/*     */     } 
/* 110 */     if (find == null) {
/* 111 */       response.error(30001, "活动[%s][%s]未找到", new Object[] { type, phpId });
/*     */       return;
/*     */     } 
/* 114 */     JsonObject rtn = new JsonObject();
/* 115 */     rtn.addProperty("state", Integer.valueOf(1000));
/* 116 */     rtn.add("activity", (JsonElement)find);
/* 117 */     response.response(this.gson.toJson((JsonElement)rtn));
/*     */   }
/*     */   
/*     */   @RequestMapping(uri = "/game/activity/createOrEdit")
/*     */   public void createOrEdit(HttpRequest request, HttpResponse response) throws Exception {
/* 122 */     JsonObject json, activity = HttpUtils.abstractGMParams(request.getRequestBody());
/*     */     
/* 124 */     ActivityType type = (ActivityType)HttpUtils.getEnum(activity, "activity", ActivityType.class);
/* 125 */     Collection<ActivityType> suport = ActivityMgr.getInstance().getAllSurportActivity();
/* 126 */     if (!suport.contains(type)) {
/* 127 */       response.error(30103, "活动[" + type + "]不支持配置", new Object[0]);
/*     */       
/*     */       return;
/*     */     } 
/* 131 */     int begintime = HttpUtils.getTime(activity, "beginTime", 0);
/* 132 */     int endtime = HttpUtils.getTime(activity, "endTime", 0);
/* 133 */     if (begintime >= endtime && begintime != 0) {
/* 134 */       response.error(30103, "beginTime 要求小于 endTime", new Object[0]);
/*     */       return;
/*     */     } 
/* 137 */     int closeTime = HttpUtils.getTime(activity, "closeTime", endtime);
/* 138 */     if (closeTime != 0 && endtime > closeTime) {
/* 139 */       response.error(30103, "endtime 要求小于 closeTime", new Object[0]);
/*     */       return;
/*     */     } 
/* 142 */     List<Activity> list = (List<Activity>)(ActivityMgr.getInstance()).activities.get(type);
/* 143 */     String phpId = HttpUtils.getString(activity, "activityid");
/* 144 */     Activity find = null;
/* 145 */     for (Activity a : list) {
/* 146 */       if (phpId != null && phpId.equals(a.bo.getGmId())) {
/* 147 */         find = a;
/*     */         continue;
/*     */       } 
/* 150 */       if ((begintime >= a.bo.getBeginTime() && begintime < a.bo.getEndTime()) || (
/* 151 */         endtime > a.bo.getBeginTime() && endtime <= a.bo.getEndTime()) || (
/* 152 */         begintime < a.bo.getBeginTime() && endtime > a.bo.getEndTime())) {
/* 153 */         response.error(30103, "在[%s - %s]期间已经有活动开启,id:%s", new Object[] { Integer.valueOf(a.bo.getBeginTime()), Integer.valueOf(a.bo.getEndTime()), a.bo.getGmId() });
/*     */         
/*     */         return;
/*     */       } 
/*     */     } 
/* 158 */     if (find == null && list.size() >= 3) {
/* 159 */       response.error(30103, "不允许同时配置3个以上活动", new Object[0]);
/*     */       
/*     */       return;
/*     */     } 
/* 163 */     boolean active = HttpUtils.getBool(activity, "active");
/*     */     
/*     */     try {
/* 166 */       json = HttpUtils.getJsonObject(activity, "config");
/* 167 */     } catch (Exception e) {
/* 168 */       json = new JsonObject();
/*     */     } 
/*     */ 
/*     */     
/* 172 */     ActivityBO bo = new ActivityBO();
/* 173 */     bo.setIsActive(active);
/* 174 */     bo.setActivity(type.toString());
/* 175 */     bo.setBeginTime(begintime);
/* 176 */     bo.setEndTime(endtime);
/* 177 */     bo.setCloseTime(closeTime);
/* 178 */     bo.setGmId(phpId);
/* 179 */     bo.setJson(json.toString());
/*     */     
/* 181 */     Activity newAct = null;
/*     */     try {
/* 183 */       String clsName = ActivityMgr.getInstance().getActivityName(type);
/*     */       
/* 185 */       Class<? extends Activity> clazz = (Class)Class.forName(clsName);
/* 186 */       newAct = clazz.getConstructor(new Class[] { ActivityBO.class }).newInstance(new Object[] { bo });
/*     */       
/* 188 */       newAct.load();
/* 189 */     } catch (Exception e) {
/* 190 */       response.error(30103, "活动配置的json解析错误,错误信息:%s", new Object[] { e.getMessage() });
/*     */       return;
/*     */     } 
/* 193 */     String opType = "create";
/* 194 */     if (find == null) {
/* 195 */       bo.insert();
/* 196 */       list.add(newAct);
/*     */     } else {
/* 198 */       opType = "edit";
/*     */       
/* 200 */       bo = find.bo;
/* 201 */       bo.setIsActive(active);
/* 202 */       bo.setActivity(type.toString());
/* 203 */       bo.setBeginTime(begintime);
/* 204 */       bo.setEndTime(endtime);
/* 205 */       bo.setCloseTime(closeTime);
/* 206 */       bo.setJson(json.toString());
/*     */       try {
/* 208 */         find.load();
/* 209 */       } catch (Exception e) {
/* 210 */         response.error(30103, "活动配置的json解析错误,错误信息:%s", new Object[] { e.getMessage() });
/*     */         return;
/*     */       } 
/* 213 */       bo.saveAll();
/*     */     } 
/* 215 */     list.sort(ActivityMgr.Sorter);
/*     */     
/* 217 */     JsonObject rtn = new JsonObject();
/* 218 */     rtn.addProperty("state", Integer.valueOf(1000));
/* 219 */     rtn.addProperty("msg", "success");
/* 220 */     response.response(rtn.toString());
/*     */     
/* 222 */     activityLog(
/* 223 */         bo.getActivity(), 
/* 224 */         bo.getIsActive(), 
/* 225 */         phpId, 
/* 226 */         bo.getBeginTime(), 
/* 227 */         bo.getEndTime(), 
/* 228 */         bo.getCloseTime(), 
/* 229 */         bo.getJson(), 
/* 230 */         opType, 
/* 231 */         HttpUtils.getString(activity, "operator", "unkown"));
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping(uri = "/game/activity/delete")
/*     */   public void delete(HttpRequest request, HttpResponse response) throws Exception {
/* 237 */     JsonObject activity = HttpUtils.abstractGMParams(request.getRequestBody());
/*     */     
/* 239 */     ActivityType type = (ActivityType)HttpUtils.getEnum(activity, "activity", ActivityType.class);
/* 240 */     Collection<ActivityType> suport = ActivityMgr.getInstance().getAllSurportActivity();
/* 241 */     if (!suport.contains(type)) {
/* 242 */       response.error(30103, "活动[" + type + "]不支持配置", new Object[0]);
/*     */       
/*     */       return;
/*     */     } 
/* 246 */     String phpId = HttpUtils.getString(activity, "activityid");
/*     */     
/* 248 */     List<Activity> list = (List<Activity>)(ActivityMgr.getInstance()).activities.get(type);
/* 249 */     if (list.size() <= 1) {
/* 250 */       response.error(30104, "活动[" + type + "]至少要保留一个配置，如果不想开启，只需要配置为active:false", new Object[0]);
/*     */       return;
/*     */     } 
/* 253 */     Activity find = null;
/* 254 */     for (Activity a : list) {
/* 255 */       if (phpId != null && phpId.equals(a.bo.getGmId())) {
/* 256 */         find = a;
/*     */       }
/*     */     } 
/* 259 */     if (find != null && find.getStatus() == ActivityStatus.Open) {
/* 260 */       response.error(30103, "活动[%s][%s]在开启中，不允许删除，请先关闭后再尝试删除", new Object[] { type, phpId });
/*     */       return;
/*     */     } 
/* 263 */     if (find != null) {
/* 264 */       find.bo.del();
/* 265 */       list.remove(find);
/*     */     } 
/* 267 */     JsonObject rtn = new JsonObject();
/* 268 */     rtn.addProperty("state", Integer.valueOf(1000));
/* 269 */     rtn.addProperty("msg", "success");
/* 270 */     response.response(rtn.toString());
/*     */   }
/*     */   
/*     */   @RequestMapping(uri = "/game/activity/check")
/*     */   public void check(HttpRequest request, HttpResponse response) throws Exception {
/* 275 */     JsonObject json, activity = HttpUtils.abstractGMParams(request.getRequestBody());
/*     */     
/* 277 */     ActivityType type = (ActivityType)HttpUtils.getEnum(activity, "activity", ActivityType.class);
/* 278 */     Collection<ActivityType> suport = ActivityMgr.getInstance().getAllSurportActivity();
/* 279 */     if (!suport.contains(type)) {
/* 280 */       response.error(30103, "活动[" + type + "]不支持配置", new Object[0]);
/*     */       
/*     */       return;
/*     */     } 
/*     */     try {
/* 285 */       json = HttpUtils.getJsonObject(activity, "config");
/* 286 */     } catch (Exception e) {
/* 287 */       json = new JsonObject();
/*     */     } 
/* 289 */     String checkRlt = ActivityMgr.getActivityByType(type).check(json);
/*     */     
/* 291 */     JsonObject rtn = new JsonObject();
/* 292 */     rtn.addProperty("state", Integer.valueOf(1000));
/* 293 */     rtn.addProperty("msg", checkRlt);
/* 294 */     response.response(rtn.toString());
/*     */   }
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
/*     */   public void activityLog(String activity, boolean isActive, String phpId, int beginTime, int endTime, int closeTime, String json, String type, String operator) {
/* 312 */     FlowLogger.activityRequest(activity, isActive, phpId, beginTime, endTime, closeTime, json, type, operator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping(uri = "/game/activity/marquee")
/*     */   public void marquee(HttpRequest request, HttpResponse response) throws Exception {
/* 324 */     JsonObject notice = HttpUtils.abstractGMParams(request.getRequestBody());
/* 325 */     int gameId = HttpUtils.getInt(notice, "gameid");
/* 326 */     if (gameId != Config.GameID()) {
/* 327 */       response.error(30103, "非法的游戏ID:%s", new Object[] { Integer.valueOf(gameId) });
/*     */       
/*     */       return;
/*     */     } 
/* 331 */     NoticeMgr.getInstance().downMarqueeFromPHP();
/*     */     
/* 333 */     JsonObject rtn = new JsonObject();
/* 334 */     rtn.addProperty("state", Integer.valueOf(1000));
/* 335 */     rtn.addProperty("msg", "success");
/* 336 */     response.response(rtn.toString());
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/core/network/http/handler/ActivityRequst.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */