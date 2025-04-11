package business.global.activity.detail;

import business.global.activity.Activity;
import business.global.gmmail.MailCenter;
import business.player.Player;
import business.player.PlayerMgr;
import business.player.item.Reward;
import com.google.gson.JsonObject;
import com.zhonglian.server.common.enums.ActivityStatus;
import com.zhonglian.server.common.enums.ActivityType;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.http.server.RequestException;
import com.zhonglian.server.websocket.exception.WSException;
import core.database.game.bo.ActivityBO;
import core.database.game.bo.ActivityRecordBO;

public class FirstRecharge
        extends Activity {
    public Reward firstReward;

    public FirstRecharge(ActivityBO data) {
        super(data);
    }

    public void load(JsonObject json) throws WSException {
        this.firstReward = new Reward(json.get("awards").getAsJsonArray());
    }

    public String check(JsonObject json) throws RequestException {
        return "ok";
    }

    public ActivityType getType() {
        return ActivityType.FirstRecharge;
    }

    public boolean needNotify(Player player) {
        return false;
    }

    public void clearAllRecharge() {
        clearActRecord();
        for (Player player : PlayerMgr.getInstance().getOnlinePlayers()) {
            player.pushProto("clearFirstReward", this.firstReward);
        }
    }

    public boolean isFirstRecharge(Player player) {
        ActivityRecordBO bo = getRecord(player);
        if (bo == null) {
            return true;
        }
        return (bo.getExtInt(0) == 0);
    }

    public boolean fristRechargeProto(Player player) {
        return isFirstRecharge(player);
    }

    public void sendFirstRechargeReward(Player player) {
        if (getStatus() != ActivityStatus.Open) {
            return;
        }

        synchronized (this) {
            boolean isFirstRecharge = isFirstRecharge(player);
            if (!isFirstRecharge) {
                return;
            }
            ActivityRecordBO bo = getOrCreateRecord(player);
            bo.setExtInt(0, 1);
            bo.setExtInt(1, CommTime.nowSecond());
            bo.saveAll();

            MailCenter.getInstance().sendMail(player.getPid(), this.ref.MailSender, this.ref.MailTitle, this.ref.MailContent, this.firstReward.uniformItemIds(),
                    this.firstReward.uniformItemCounts());

            player.pushProto("firstReward", this.firstReward);
        }
    }

    public void onOpen() {
    }

    public void onEnd() {
    }

    public void onClosed() {
    }
}

