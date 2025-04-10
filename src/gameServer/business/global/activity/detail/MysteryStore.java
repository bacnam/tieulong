package business.global.activity.detail;

import business.global.activity.Activity;
import com.google.gson.JsonObject;
import com.zhonglian.server.common.enums.ActivityType;
import com.zhonglian.server.http.server.RequestException;
import com.zhonglian.server.websocket.exception.WSException;
import core.database.game.bo.ActivityBO;
import core.server.OpenSeverTime;

public class MysteryStore
extends Activity
{
private int begin;

public MysteryStore(ActivityBO data) {
super(data);

this.begin = 0;
}

public void load(JsonObject json) throws WSException {
this.begin = json.get("begin").getAsInt();
}

public String check(JsonObject json) throws RequestException {
return "ok";
}

public ActivityType getType() {
return ActivityType.MysteryStore;
}

public void onOpen() {}

public void onEnd() {}

public void onClosed() {}

public int getBeginTime() {
return OpenSeverTime.getInstance().getOpenZeroTime() + this.begin;
}
}

