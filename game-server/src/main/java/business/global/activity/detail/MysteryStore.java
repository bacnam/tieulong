package business.global.activity.detail;

import business.global.activity.Activity;
import com.google.gson.JsonObject;
import com.zhonglian.server.common.enums.ActivityType;
import com.zhonglian.server.http.server.RequestException;
import com.zhonglian.server.websocket.exception.WSException;
import core.database.game.bo.ActivityBO;
import core.server.OpenSeverTime;

public class MysteryStore extends Activity {
    private int begin = 0;

    public MysteryStore(ActivityBO data) {
        super(data);
    }

    @Override
    public void load(JsonObject json) throws WSException {
        this.begin = json.get("begin").getAsInt();
    }

    @Override
    public String check(JsonObject json) throws RequestException {
        return "ok";
    }

    @Override
    public ActivityType getType() {
        return ActivityType.MysteryStore;
    }

    @Override
    public void onOpen() {
    }

    @Override
    public void onEnd() {
    }

    @Override
    public void onClosed() {
    }

    @Override
    public int getBeginTime() {
        return OpenSeverTime.getInstance().getOpenZeroTime() + this.begin;
    }
}
