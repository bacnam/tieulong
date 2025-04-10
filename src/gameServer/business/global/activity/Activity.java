package business.global.activity;

import business.player.Player;
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
import core.database.game.bo.ActivityBO;
import core.database.game.bo.ActivityRecordBO;
import java.util.HashMap;
import java.util.Map;
import proto.gameworld.ActivityInfo;

public abstract class Activity {
public ActivityBO bo;
public RefActivity ref;

public abstract void load(JsonObject paramJsonObject) throws WSException;

public String check(JsonObject json) throws RequestException {
return null;
}

public ActivityType type = null; public Map<Long, ActivityRecordBO> playerActRecords;

public abstract void onOpen();

public Activity(ActivityBO bo) {
this.bo = bo;
if (bo != null)
this.ref = (RefActivity)RefDataMgr.get(RefActivity.class, ActivityType.valueOf(bo.getActivity())); 
this.playerActRecords = new HashMap<>();
} public abstract void onEnd();
public abstract void onClosed();
public void load() throws Exception {
if (!this.bo.getIsActive()) {
return;
}
JsonObject jsonObject = (new JsonParser()).parse(this.bo.getJson()).getAsJsonObject();
load(jsonObject);
}

public String checkAndSubscribeItem(JsonArray itemArray) throws RequestException {
String result = "";
for (JsonElement itemElement : itemArray) {
JsonObject itemObj = itemElement.getAsJsonObject();
int uniformId = itemObj.get("uniformId").getAsInt();
RefUniformItem refUniformItem = (RefUniformItem)RefDataMgr.get(RefUniformItem.class, Integer.valueOf(uniformId));
if (refUniformItem == null) {
throw new RequestException(4000001, String.format("UniformId=%s的物品不存在", new Object[] { Integer.valueOf(uniformId) }), new Object[0]);
}
int count = itemObj.get("count").getAsInt();
if (count <= 0) {
throw new RequestException(4000001, String.format("UniformId=%s的物品数量小于0", new Object[] { Integer.valueOf(uniformId) }), new Object[0]);
}
result = String.valueOf(result) + "名称:" + refUniformItem.Name + "," + "数量:" + count + ";";
} 
return result;
}

public void clearActRecord() {
BM.getBM(ActivityRecordBO.class).delAll("aid", Long.valueOf(this.bo.getId()));
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
}
int now = CommTime.nowSecond();
if (now < getBeginTime()) {
return ActivityStatus.Close;
}
if (getCloseTime() != 0 && now >= getCloseTime()) {
return ActivityStatus.Close;
}
if (getEndTime() != 0 && now >= getEndTime()) {
return ActivityStatus.End;
}
return ActivityStatus.Open;
}

public boolean isOpen() {
ActivityStatus status = getStatus();
if (status == ActivityStatus.Open) {
return true;
}
return false;
}

public boolean isClosed() {
ActivityStatus status = getStatus();
if (status == ActivityStatus.Inactive || status == ActivityStatus.Close) {
return true;
}
return false;
}

public boolean isRecvReward() {
ActivityStatus status = getStatus();
if (status == ActivityStatus.Open || status == ActivityStatus.End) {
return true;
}
return false;
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

public ActivityInfo activitySummary(Player player) {
ActivityInfo builder = new ActivityInfo();
builder.setType(getType());
builder.setStatus(getStatus());
builder.setBeginTime(getBeginTime());
builder.setEndTime(getEndTime());
builder.setCloseTime(getCloseTime());
return builder;
}

public ActivityRecordBO getRecord(Player player) {
return this.playerActRecords.get(Long.valueOf(player.getPid()));
}

public final ActivityRecordBO getOrCreateRecord(Player player) {
ActivityRecordBO bo = this.playerActRecords.get(Long.valueOf(player.getPid()));
if (bo == null) {
synchronized (this) {
bo = this.playerActRecords.get(Long.valueOf(player.getPid()));
if (bo != null) {
return bo;
}
this.playerActRecords.put(Long.valueOf(player.getPid()), bo = createPlayerActRecord(player));
} 
}
return bo;
}

public ActivityRecordBO createPlayerActRecord(Player player) {
ActivityRecordBO bo = new ActivityRecordBO();
bo.setPid(player.getPid());
bo.setAid(this.bo.getId());
bo.setActivity(getType().toString());
bo.insert();
return bo;
}
}

