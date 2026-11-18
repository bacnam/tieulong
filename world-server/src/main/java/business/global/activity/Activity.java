package business.global.activity;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.enums.ActivityStatus;
import com.zhonglian.server.common.enums.ActivityType;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.http.server.RequestException;
import com.zhonglian.server.websocket.exception.WSException;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefActivity;
import core.config.refdata.ref.RefUniformItem;
import core.database.world.bo.ActivityBO;
import core.database.world.bo.ActivityRecordBO;
import java.util.HashMap;
import java.util.Map;
import proto.gameworld.ActivityInfo;
import proto.gamezone.Player;
import proto.gamezone.Player.PlayerInfo;

public abstract class Activity {
    public ActivityBO bo;
    public RefActivity ref;
    public ActivityType type = null;
    public Map<Long, ActivityRecordBO> playerActRecords;

    public abstract void load(JsonObject var1) throws WSException;

    public String check(JsonObject json) throws RequestException {
        return null;
    }

    public abstract void onOpen();

    public abstract void onEnd();

    public abstract void onClosed();

    public Activity(ActivityBO bo) {
        this.bo = bo;
        if (bo != null) {
            this.ref = RefDataMgr.get(RefActivity.class, ActivityType.valueOf(bo.getActivity()));
        }

        this.playerActRecords = new HashMap<>();
    }

    public void load() throws Exception {
        if (this.bo.getIsActive()) {
            JsonObject jsonObject = new JsonParser().parse(this.bo.getJson()).getAsJsonObject();
            this.load(jsonObject);
        }
    }

    public String checkAndSubscribeItem(JsonArray itemArray) throws RequestException {
        String result = "";

        for (JsonElement itemElement : itemArray) {
            JsonObject itemObj = itemElement.getAsJsonObject();
            int uniformId = itemObj.get("uniformId").getAsInt();
            RefUniformItem refUniformItem = RefDataMgr.get(RefUniformItem.class, uniformId);
            if (refUniformItem == null) {
                throw new RequestException(4000001, String.format("UniformId=%s的物品不存在", uniformId));
            }

            int count = itemObj.get("count").getAsInt();
            if (count <= 0) {
                throw new RequestException(4000001, String.format("UniformId=%s的物品数量小于0", uniformId));
            }

            result = result + "名称:" + refUniformItem.Name + "," + "数量:" + count + ";";
        }

        return result;
    }

    public void clearActRecord() {
        BM.getBM(ActivityRecordBO.class).delAll("aid", this.bo.getId());
        this.playerActRecords.clear();
    }

    public long getId() {
        return this.bo.getId();
    }

    public int getActNo() {
        return this.ref.ActNo;
    }

    public ActivityStatus getStatus() {
        if (!this.bo.getIsActive()) {
            return ActivityStatus.Inactive;
        } else {
            int now = CommTime.nowSecond();
            if (now < this.getBeginTime()) {
                return ActivityStatus.Close;
            } else if (this.getCloseTime() != 0 && now >= this.getCloseTime()) {
                return ActivityStatus.Close;
            } else {
                return this.getEndTime() != 0 && now >= this.getEndTime() ? ActivityStatus.End : ActivityStatus.Open;
            }
        }
    }

    public boolean isOpen() {
        ActivityStatus status = this.getStatus();
        return status == ActivityStatus.Open;
    }

    public boolean isClosed() {
        ActivityStatus status = this.getStatus();
        return status == ActivityStatus.Inactive || status == ActivityStatus.Close;
    }

    public boolean isRecvReward() {
        ActivityStatus status = this.getStatus();
        return status == ActivityStatus.Open || status == ActivityStatus.End;
    }

    public abstract ActivityType getType();

    public boolean needNotify(Player player) {
        return false;
    }

    public int getBeginTime() {
        return this.bo.getBeginTime();
    }

    public int getEndTime() {
        return this.bo.getEndTime();
    }

    public int getCloseTime() {
        return this.bo.getCloseTime();
    }

    public ActivityInfo activitySummary() {
        ActivityInfo builder = new ActivityInfo();
        builder.setType(this.getType());
        builder.setStatus(this.getStatus());
        builder.setBeginTime(this.getBeginTime());
        builder.setEndTime(this.getEndTime());
        builder.setCloseTime(this.getCloseTime());
        return builder;
    }

    public ActivityRecordBO getRecord(PlayerInfo player) {
        return this.playerActRecords.get(player.pid);
    }

    public final ActivityRecordBO getOrCreateRecord(PlayerInfo player) {
        ActivityRecordBO bo = this.playerActRecords.get(player.pid);
        if (bo == null) {
            synchronized (this) {
                bo = this.playerActRecords.get(player.pid);
                if (bo != null) {
                    return bo;
                }

                this.playerActRecords.put(player.pid, bo = this.createPlayerActRecord(player));
            }
        }

        return bo;
    }

    public ActivityRecordBO createPlayerActRecord(PlayerInfo player) {
        ActivityRecordBO bo = new ActivityRecordBO();
        bo.setPid(player.pid);
        bo.setAid(this.bo.getId());
        bo.setActivity(this.getType().toString());
        bo.setServerId(player.serverId);
        bo.insert();
        return bo;
    }
}
