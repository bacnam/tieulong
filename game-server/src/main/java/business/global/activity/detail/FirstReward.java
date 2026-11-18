package business.global.activity.detail;

import business.global.activity.Activity;
import business.global.gmmail.MailCenter;
import business.player.Player;
import business.player.PlayerMgr;
import business.player.feature.features.RechargeFeature;
import business.player.item.Reward;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.zhonglian.server.common.enums.ActivityType;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.http.server.RequestException;
import com.zhonglian.server.websocket.exception.WSException;
import core.database.game.bo.ActivityBO;
import core.database.game.bo.ActivityRecordBO;
import java.util.ArrayList;
import java.util.List;

public class FirstReward extends Activity {
    public List<FirstReward.DayReward> firstReward = new ArrayList<>();

    public FirstReward(ActivityBO data) {
        super(data);
    }

    @Override
    public void load(JsonObject json) throws WSException {
        for (JsonElement element : json.get("rewards").getAsJsonArray()) {
            JsonObject obj = element.getAsJsonObject();
            FirstReward.DayReward builder = new FirstReward.DayReward(obj);
            this.firstReward.add(builder);
        }
    }

    @Override
    public String check(JsonObject json) throws RequestException {
        return "ok";
    }

    @Override
    public ActivityType getType() {
        return ActivityType.FirstReward;
    }

    @Override
    public boolean needNotify(Player player) {
        return false;
    }

    public void clearAllRecharge() {
        this.clearActRecord();

        for (Player player : PlayerMgr.getInstance().getOnlinePlayers()) {
            player.pushProto("clearFirstReward", this.firstReward);
        }
    }

    public boolean isFirstRecharge(Player player) {
        ActivityRecordBO bo = this.getRecord(player);
        return bo == null ? true : bo.getExtInt(0) == 0;
    }

    public FirstReward.fristRewardProto fristRechargeProto(Player player) {
        FirstReward.fristRewardProto proto = new FirstReward.fristRewardProto();
        RechargeFeature rechargeFeature = player.getFeature(RechargeFeature.class);
        proto.isFirstRecharge = this.isFirstRecharge(player) && !rechargeFeature.isRecharged();
        proto.firstReward = this.firstReward;
        ActivityRecordBO bo = this.getOrCreateRecord(player);
        int times = bo.getExtInt(2);
        proto.isOver = times >= this.firstReward.size();
        return proto;
    }

    public void setFirstReward(Player player) {
        synchronized (this) {
            boolean isFirstRecharge = this.isFirstRecharge(player);
            if (!isFirstRecharge) {
                return;
            }

            ActivityRecordBO bo = this.getOrCreateRecord(player);
            bo.setExtInt(0, 1);
            bo.setExtInt(1, CommTime.nowSecond());
            bo.saveAll();
            player.pushProto("firstThreeReward", this.isFirstRecharge(player));
        }

        this.sendFirstReward(player);
    }

    public void sendFirstReward(Player player) {
        try {
            synchronized (this) {
                boolean isFirstRecharge = this.isFirstRecharge(player);
                if (isFirstRecharge) {
                    return;
                }

                ActivityRecordBO bo = this.getOrCreateRecord(player);
                int times = bo.getExtInt(2);
                if (times > this.firstReward.size() - 1) {
                    return;
                }

                Reward reward = this.getReward(times + 1);
                if (reward == null) {
                    return;
                }

                bo.saveExtInt(2, times + 1);
                MailCenter.getInstance()
                    .sendMail(
                        player.getPid(), this.ref.MailSender, this.ref.MailTitle, this.ref.MailContent, reward.uniformItemIds(), reward.uniformItemCounts()
                    );
            }
        } catch (Exception var8) {
            var8.printStackTrace();
        }
    }

    public Reward getReward(int times) {
        for (FirstReward.DayReward day : this.firstReward) {
            if (day.id == times) {
                return day.reward;
            }
        }

        return null;
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

    private static class DayReward {
        int id;
        int value;
        int power;
        Reward reward;

        private DayReward(JsonObject json) throws WSException {
            this.id = json.get("aid").getAsInt();
            this.value = json.get("value").getAsInt();
            this.power = json.get("power").getAsInt();
            this.reward = new Reward(json.get("awards").getAsJsonArray());
        }
    }

    private static class fristRewardProto {
        boolean isFirstRecharge;
        List<FirstReward.DayReward> firstReward;
        boolean isOver;

        private fristRewardProto() {
        }
    }
}
