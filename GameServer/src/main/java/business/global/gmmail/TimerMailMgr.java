package business.global.gmmail;

import BaseTask.SyncTask.SyncTaskManager;
import business.player.item.Reward;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.common.utils.StringUtils;
import com.zhonglian.server.http.server.HttpUtils;
import core.database.game.bo.FixedTimeMailBO;

import java.util.List;

public class TimerMailMgr {
    private static TimerMailMgr instance = new TimerMailMgr();
    public List<FixedTimeMailBO> timerMailList = Lists.newArrayList();

    public static TimerMailMgr getInstance() {
        return instance;
    }

    public void init() {
        this.timerMailList = BM.getBM(FixedTimeMailBO.class).findAll();

        SyncTaskManager.schedule(1000, () -> {
            check2sendMail();
            return true;
        });
    }

    public JsonArray timerMailList() {
        JsonArray mailArray = new JsonArray();
        for (FixedTimeMailBO bo : this.timerMailList) {
            if (bo.getBeginTime() + bo.getCyclesNum() * 86400 < CommTime.nowSecond()) {
                continue;
            }
            JsonObject mailObj = new JsonObject();
            mailObj.addProperty("mailid", Long.valueOf(bo.getMailId()));
            mailObj.addProperty("name", bo.getSenderName());
            mailObj.addProperty("title", bo.getTitle());
            mailObj.addProperty("content", bo.getContent());
            JsonArray itemArray = new JsonArray();
            if (bo.getUniformIDList() != null && bo.getUniformIDList().length() > 0) {
                List<Integer> unifromIds = StringUtils.string2Integer(bo.getUniformIDList());
                List<Integer> itemCounts = StringUtils.string2Integer(bo.getUniformCountList());
                for (int index = 0; index < unifromIds.size(); index++) {
                    JsonObject itemObj = new JsonObject();
                    itemObj.addProperty("uniformId", unifromIds.get(index));
                    itemObj.addProperty("count", itemCounts.get(index));
                    itemArray.add((JsonElement) itemObj);
                }
            }
            mailObj.add("item", (JsonElement) itemArray);
            mailObj.addProperty("begintime", CommTime.getTimeString(bo.getBeginTime()));
            mailObj.addProperty("cycles", Integer.valueOf(bo.getCyclesNum()));
            mailArray.add((JsonElement) mailObj);
        }
        return mailArray;
    }

    public long addTimerMail(JsonObject json) throws Exception {
        Reward reward = new Reward(HttpUtils.getJsonArray(json, "itemlist"));

        int cycles = HttpUtils.getInt(json, "cycles");
        if (cycles < 1) {
            cycles = 1;
        }
        FixedTimeMailBO bo = new FixedTimeMailBO();
        bo.setMailId(HttpUtils.getLong(json, "mailid"));
        bo.setSenderName(HttpUtils.getString(json, "name"));
        bo.setTitle(HttpUtils.getString(json, "title"));
        bo.setContent(HttpUtils.getString(json, "content"));
        bo.setUniformIDList(reward.uniformItemIds());
        bo.setUniformCountList(reward.uniformItemCounts());
        bo.setBeginTime(HttpUtils.getTime(json, "begintime"));
        bo.setCyclesNum(cycles);
        bo.insert();
        synchronized (this) {
            this.timerMailList.add(bo);
        }
        return bo.getId();
    }

    public void delTimerMail(long mailId) {
        FixedTimeMailBO find = null;
        for (FixedTimeMailBO bo : this.timerMailList) {
            if (bo.getMailId() == mailId) {
                find = bo;
                break;
            }
        }
        if (find == null) {
            return;
        }
        synchronized (this) {
            this.timerMailList.remove(find);
            find.del();
        }
    }

    public void check2sendMail() {
        List<FixedTimeMailBO> list = null;
        synchronized (this) {
            list = Lists.newArrayList(this.timerMailList);
        }
        int nowSecond = CommTime.nowSecond();

        for (FixedTimeMailBO bo : list) {
            int beginTime = bo.getBeginTime();

            if (beginTime > nowSecond) {
                continue;
            }
            int hasSendNum = bo.getHasSendNum();

            if (hasSendNum >= bo.getCyclesNum()) {
                continue;
            }
            if (beginTime + hasSendNum * 86400 > nowSecond) {
                continue;
            }

            bo.saveHasSendNum(hasSendNum + 1);

            MailCenter.getInstance().sendGlobalMail(bo.getSenderName(), bo.getTitle(), bo.getContent(), 0, 0, bo.getUniformIDList(), bo.getUniformCountList());
        }
    }
}

