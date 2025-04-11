package business.global.notice;

import BaseTask.SyncTask.SyncTaskManager;
import business.global.guild.Guild;
import business.player.Player;
import business.player.PlayerMgr;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zhonglian.server.common.Config;
import com.zhonglian.server.common.enums.ConstEnum;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.common.utils.Lists;
import com.zhonglian.server.http.client.HttpUtil;
import com.zhonglian.server.http.server.HttpUtils;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefLanguage;
import core.network.proto.NoticeInfo;

import java.util.LinkedList;
import java.util.List;

public class NoticeMgr {
    private static NoticeMgr instance = new NoticeMgr();
    public LinkedList<Notice> NoticeList = Lists.newLinkedList();

    public static NoticeMgr getInstance() {
        return instance;
    }

    public void init() {
        SyncTaskManager.schedule(1000, () -> {
            checkAndSend();
            return true;
        });
    }

    public void downMarqueeFromPHP() throws Exception {
        JsonObject param = new JsonObject();
        param.addProperty("gameid", Integer.valueOf(Config.GameID()));
        param.addProperty("platform", Config.getPlatform());
        param.addProperty("server_id", Integer.valueOf(Config.ServerID()));

        String webBody = HttpUtil.sendHttpPost2Web(15000, 15000, Config.PhpMargueeNoticeAddr(), param.toString(), "");

        webBody = HttpUtil.decodeUnicode(webBody);

        JsonArray array = (new JsonParser()).parse(webBody).getAsJsonArray();

        LinkedList<Notice> noticeList = Lists.newLinkedList();
        for (JsonElement element : array) {
            JsonObject obj = element.getAsJsonObject();

            Notice notice = new Notice();
            notice.setId(HttpUtils.getLong(obj, "id"));
            notice.setContent(HttpUtils.getString(obj, "noticeList"));
            notice.setBeginTime(HttpUtils.getTime(obj, "beginTime"));
            notice.setEndTime(HttpUtils.getTime(obj, "endTime"));
            notice.setInterval(HttpUtils.getInt(obj, "interval") * 60);
            notice.setClientType(HttpUtils.getInt(obj, "clientType"));

            noticeList.add(notice);
        }
        synchronized (this) {
            this.NoticeList.clear();
            this.NoticeList = noticeList;
        }
    }

    public String gmAddMarqueeNotice(long id, String content, int beginTime, int endTime, int interval, int clientType) {
        Notice bo = new Notice();
        bo.setId(id);
        bo.setContent(content);
        bo.setBeginTime(beginTime);
        bo.setEndTime(endTime);
        bo.setInterval(interval);
        bo.setClientType(clientType);
        synchronized (this) {
            this.NoticeList.add(bo);
        }
        return "添加成功";
    }

    public void sendMarquee(ConstEnum.UniverseMessageType messageType, String... params) {
        if (messageType == null) {
            messageType = ConstEnum.UniverseMessageType.None;
        }
        List<String> values = Lists.newArrayList();
        byte b;
        int j;
        String[] arrayOfString;
        for (j = (arrayOfString = params).length, b = 0; b < j; ) {
            String s = arrayOfString[b];
            values.add(s);
            b++;
        }

        RefLanguage ref = (RefLanguage) RefDataMgr.get(RefLanguage.class, messageType.toString());
        String content = ref.CN;

        for (int i = 0; i < values.size(); i++) {
            content = content.replace("{" + i + "}", values.get(i));
        }

        NoticeInfo info = new NoticeInfo(messageType, values);
        for (Player player : PlayerMgr.getInstance().getOnlinePlayers()) {
            player.pushProto("marquee", info);
        }
    }

    public void sendMarquee(ConstEnum.UniverseMessageType messageType, Guild guild, String... params) {
        if (messageType == null) {
            messageType = ConstEnum.UniverseMessageType.None;
        }
        List<String> values = Lists.newArrayList();
        byte b;
        int j;
        String[] arrayOfString;
        for (j = (arrayOfString = params).length, b = 0; b < j; ) {
            String s = arrayOfString[b];
            values.add(s);
            b++;
        }

        RefLanguage ref = (RefLanguage) RefDataMgr.get(RefLanguage.class, messageType.toString());
        String content = ref.CN;

        for (int i = 0; i < values.size(); i++) {
            content = content.replace("{" + i + "}", values.get(i));
        }
        NoticeInfo info = new NoticeInfo(messageType, values);
        guild.broadcast("marquee", info);
    }

    public void checkAndSend() {
        int nowSecond = CommTime.nowSecond();

        List<Notice> noticeList = null;
        synchronized (this) {
            List<Notice> removeList = Lists.newArrayList();
            for (Notice notice : this.NoticeList) {
                if (notice.getEndTime() < nowSecond) {
                    removeList.add(notice);
                }
            }
            this.NoticeList.removeAll(removeList);
            noticeList = this.NoticeList;
        }
        if (noticeList == null || noticeList.size() == 0) {
            return;
        }
        for (Notice bo : noticeList) {
            int beginTime = bo.getBeginTime();
            if (nowSecond < beginTime) {
                continue;
            }
            boolean send = false;
            if (bo.getInterval() > 0) {
                if (bo.getLastSendTime() + bo.getInterval() < nowSecond) {
                    bo.setLastSendTime(nowSecond);
                    send = true;
                }
            } else if (bo.getInterval() == 0) {
                bo.setLastSendTime(nowSecond);
                send = true;
            }
            if (send)
                sendMarquee(ConstEnum.UniverseMessageType.CommonAnounce, new String[]{bo.getContent()});
        }
    }
}

