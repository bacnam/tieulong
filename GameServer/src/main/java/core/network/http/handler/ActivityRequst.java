package core.network.http.handler;

import business.global.activity.Activity;
import business.global.activity.ActivityMgr;
import business.global.notice.NoticeMgr;
import com.google.gson.*;
import com.zhonglian.server.common.Config;
import com.zhonglian.server.common.enums.ActivityStatus;
import com.zhonglian.server.common.enums.ActivityType;
import com.zhonglian.server.http.annotation.RequestMapping;
import com.zhonglian.server.http.server.HttpRequest;
import com.zhonglian.server.http.server.HttpResponse;
import com.zhonglian.server.http.server.HttpUtils;
import core.database.game.bo.ActivityBO;
import core.logger.flow.FlowLogger;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ActivityRequst {
    Gson gson = new Gson();

    @RequestMapping(uri = "/game/activity/index")
    public void index(HttpRequest request, HttpResponse response) {
        response.response(new File("conf/httpserver/activity.html"));
    }

    @RequestMapping(uri = "/game/activity/surport")
    public void getSurport(HttpRequest request, HttpResponse response) {
        String json = this.gson.toJson(ActivityMgr.getInstance().getAllSurportActivity());
        response.response(json);
    }

    @RequestMapping(uri = "/game/activity/list")
    public void list(HttpRequest request, HttpResponse response) throws Exception {
        JsonObject postParam = HttpUtils.abstractGMParams(request.getRequestBody());
        String strType = HttpUtils.getString(postParam, "activity", null);
        JsonArray rtnActivities = new JsonArray();
        Map<ActivityType, List<Activity>> activities = (ActivityMgr.getInstance()).activities;
        if (strType == null) {
            List<Activity> actList = new ArrayList<>();
            for (List<Activity> list : activities.values()) {
                actList.addAll(list);
            }
            actList.stream().sorted((left, right) -> left.getStatus().ordinal() - right.getStatus().ordinal());

            for (Activity a : actList) {
                JsonObject object = new JsonObject();
                object.addProperty("activity", a.getType().toString());
                object.addProperty("state", a.getStatus().toString());
                object.addProperty("activityid", a.bo.getGmId());
                rtnActivities.add((JsonElement) object);
            }
        } else {
            ActivityType type;
            try {
                type = ActivityType.valueOf(strType);
            } catch (Exception e) {
                response.error(30101, "wrong acitity type[" + strType + "]", new Object[0]);
                return;
            }
            List<Activity> list = activities.get(type);
            list.stream().sorted((left, right) -> left.getStatus().ordinal() - right.getStatus().ordinal());

            for (Activity a : list) {
                JsonObject object = new JsonObject();
                object.addProperty("activity", a.getType().toString());
                object.addProperty("state", a.getStatus().toString());
                object.addProperty("activityid", a.bo.getGmId());
                rtnActivities.add((JsonElement) object);
            }
        }
        JsonObject rtn = new JsonObject();
        rtn.addProperty("state", Integer.valueOf(1000));
        rtn.add("activities", (JsonElement) rtnActivities);
        response.response(this.gson.toJson((JsonElement) rtn));
    }

    @RequestMapping(uri = "/game/activity/detail")
    public void detail(HttpRequest request, HttpResponse response) throws Exception {
        JsonObject postParam = HttpUtils.abstractGMParams(request.getRequestBody());
        ActivityType type = (ActivityType) HttpUtils.getEnum(postParam, "activity", ActivityType.class);
        String phpId = HttpUtils.getString(postParam, "activityid");
        JsonObject find = null;
        List<Activity> list = (List<Activity>) (ActivityMgr.getInstance()).activities.get(type);
        for (Activity a : list) {
            if (phpId != null && phpId.equals(a.bo.getGmId())) {
                continue;
            }
            ActivityBO bo = a.bo;
            find = new JsonObject();
            find.addProperty("activity", bo.getActivity());
            find.addProperty("activityid", bo.getGmId());
            find.addProperty("active", Boolean.valueOf(bo.getIsActive()));
            find.addProperty("beginTime", Integer.valueOf(bo.getBeginTime()));
            find.addProperty("endTime", Integer.valueOf(bo.getEndTime()));
            find.addProperty("closeTime", Integer.valueOf(bo.getCloseTime()));
            find.add("config", (JsonElement) (new JsonParser()).parse(bo.getJson()).getAsJsonObject());
        }
        if (find == null) {
            response.error(30001, "活动[%s][%s]未找到", new Object[]{type, phpId});
            return;
        }
        JsonObject rtn = new JsonObject();
        rtn.addProperty("state", Integer.valueOf(1000));
        rtn.add("activity", (JsonElement) find);
        response.response(this.gson.toJson((JsonElement) rtn));
    }

    @RequestMapping(uri = "/game/activity/createOrEdit")
    public void createOrEdit(HttpRequest request, HttpResponse response) throws Exception {
        JsonObject json, activity = HttpUtils.abstractGMParams(request.getRequestBody());

        ActivityType type = (ActivityType) HttpUtils.getEnum(activity, "activity", ActivityType.class);
        Collection<ActivityType> suport = ActivityMgr.getInstance().getAllSurportActivity();
        if (!suport.contains(type)) {
            response.error(30103, "活动[" + type + "]不支持配置", new Object[0]);

            return;
        }
        int begintime = HttpUtils.getTime(activity, "beginTime", 0);
        int endtime = HttpUtils.getTime(activity, "endTime", 0);
        if (begintime >= endtime && begintime != 0) {
            response.error(30103, "beginTime 要求小于 endTime", new Object[0]);
            return;
        }
        int closeTime = HttpUtils.getTime(activity, "closeTime", endtime);
        if (closeTime != 0 && endtime > closeTime) {
            response.error(30103, "endtime 要求小于 closeTime", new Object[0]);
            return;
        }
        List<Activity> list = (List<Activity>) (ActivityMgr.getInstance()).activities.get(type);
        String phpId = HttpUtils.getString(activity, "activityid");
        Activity find = null;
        for (Activity a : list) {
            if (phpId != null && phpId.equals(a.bo.getGmId())) {
                find = a;
                continue;
            }
            if ((begintime >= a.bo.getBeginTime() && begintime < a.bo.getEndTime()) || (
                    endtime > a.bo.getBeginTime() && endtime <= a.bo.getEndTime()) || (
                    begintime < a.bo.getBeginTime() && endtime > a.bo.getEndTime())) {
                response.error(30103, "在[%s - %s]期间已经有活动开启,id:%s", new Object[]{Integer.valueOf(a.bo.getBeginTime()), Integer.valueOf(a.bo.getEndTime()), a.bo.getGmId()});

                return;
            }
        }
        if (find == null && list.size() >= 3) {
            response.error(30103, "不允许同时配置3个以上活动", new Object[0]);

            return;
        }
        boolean active = HttpUtils.getBool(activity, "active");

        try {
            json = HttpUtils.getJsonObject(activity, "config");
        } catch (Exception e) {
            json = new JsonObject();
        }

        ActivityBO bo = new ActivityBO();
        bo.setIsActive(active);
        bo.setActivity(type.toString());
        bo.setBeginTime(begintime);
        bo.setEndTime(endtime);
        bo.setCloseTime(closeTime);
        bo.setGmId(phpId);
        bo.setJson(json.toString());

        Activity newAct = null;
        try {
            String clsName = ActivityMgr.getInstance().getActivityName(type);

            Class<? extends Activity> clazz = (Class) Class.forName(clsName);
            newAct = clazz.getConstructor(new Class[]{ActivityBO.class}).newInstance(new Object[]{bo});

            newAct.load();
        } catch (Exception e) {
            response.error(30103, "活动配置的json解析错误,错误信息:%s", new Object[]{e.getMessage()});
            return;
        }
        String opType = "create";
        if (find == null) {
            bo.insert();
            list.add(newAct);
        } else {
            opType = "edit";

            bo = find.bo;
            bo.setIsActive(active);
            bo.setActivity(type.toString());
            bo.setBeginTime(begintime);
            bo.setEndTime(endtime);
            bo.setCloseTime(closeTime);
            bo.setJson(json.toString());
            try {
                find.load();
            } catch (Exception e) {
                response.error(30103, "活动配置的json解析错误,错误信息:%s", new Object[]{e.getMessage()});
                return;
            }
            bo.saveAll();
        }
        list.sort(ActivityMgr.Sorter);

        JsonObject rtn = new JsonObject();
        rtn.addProperty("state", Integer.valueOf(1000));
        rtn.addProperty("msg", "success");
        response.response(rtn.toString());

        activityLog(
                bo.getActivity(),
                bo.getIsActive(),
                phpId,
                bo.getBeginTime(),
                bo.getEndTime(),
                bo.getCloseTime(),
                bo.getJson(),
                opType,
                HttpUtils.getString(activity, "operator", "unkown"));
    }

    @RequestMapping(uri = "/game/activity/delete")
    public void delete(HttpRequest request, HttpResponse response) throws Exception {
        JsonObject activity = HttpUtils.abstractGMParams(request.getRequestBody());

        ActivityType type = (ActivityType) HttpUtils.getEnum(activity, "activity", ActivityType.class);
        Collection<ActivityType> suport = ActivityMgr.getInstance().getAllSurportActivity();
        if (!suport.contains(type)) {
            response.error(30103, "活动[" + type + "]不支持配置", new Object[0]);

            return;
        }
        String phpId = HttpUtils.getString(activity, "activityid");

        List<Activity> list = (List<Activity>) (ActivityMgr.getInstance()).activities.get(type);
        if (list.size() <= 1) {
            response.error(30104, "活动[" + type + "]至少要保留一个配置，如果不想开启，只需要配置为active:false", new Object[0]);
            return;
        }
        Activity find = null;
        for (Activity a : list) {
            if (phpId != null && phpId.equals(a.bo.getGmId())) {
                find = a;
            }
        }
        if (find != null && find.getStatus() == ActivityStatus.Open) {
            response.error(30103, "活动[%s][%s]在开启中，不允许删除，请先关闭后再尝试删除", new Object[]{type, phpId});
            return;
        }
        if (find != null) {
            find.bo.del();
            list.remove(find);
        }
        JsonObject rtn = new JsonObject();
        rtn.addProperty("state", Integer.valueOf(1000));
        rtn.addProperty("msg", "success");
        response.response(rtn.toString());
    }

    @RequestMapping(uri = "/game/activity/check")
    public void check(HttpRequest request, HttpResponse response) throws Exception {
        JsonObject json, activity = HttpUtils.abstractGMParams(request.getRequestBody());

        ActivityType type = (ActivityType) HttpUtils.getEnum(activity, "activity", ActivityType.class);
        Collection<ActivityType> suport = ActivityMgr.getInstance().getAllSurportActivity();
        if (!suport.contains(type)) {
            response.error(30103, "活动[" + type + "]不支持配置", new Object[0]);

            return;
        }
        try {
            json = HttpUtils.getJsonObject(activity, "config");
        } catch (Exception e) {
            json = new JsonObject();
        }
        String checkRlt = ActivityMgr.getActivityByType(type).check(json);

        JsonObject rtn = new JsonObject();
        rtn.addProperty("state", Integer.valueOf(1000));
        rtn.addProperty("msg", checkRlt);
        response.response(rtn.toString());
    }

    public void activityLog(String activity, boolean isActive, String phpId, int beginTime, int endTime, int closeTime, String json, String type, String operator) {
        FlowLogger.activityRequest(activity, isActive, phpId, beginTime, endTime, closeTime, json, type, operator);
    }

    @RequestMapping(uri = "/game/activity/marquee")
    public void marquee(HttpRequest request, HttpResponse response) throws Exception {
        JsonObject notice = HttpUtils.abstractGMParams(request.getRequestBody());
        int gameId = HttpUtils.getInt(notice, "gameid");
        if (gameId != Config.GameID()) {
            response.error(30103, "非法的游戏ID:%s", new Object[]{Integer.valueOf(gameId)});

            return;
        }
        NoticeMgr.getInstance().downMarqueeFromPHP();

        JsonObject rtn = new JsonObject();
        rtn.addProperty("state", Integer.valueOf(1000));
        rtn.addProperty("msg", "success");
        response.response(rtn.toString());
    }
}

