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
import com.zhonglian.server.common.enums.Achievement.AchievementType;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.http.server.RequestException;
import com.zhonglian.server.websocket.exception.WSException;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefRecharge;
import core.database.game.bo.ActivityBO;
import core.database.game.bo.ActivityRecordBO;
import core.database.game.bo.PlayerRechargeRecordBO;
import java.util.ArrayList;
import java.util.List;

public class NewFirstReward extends Activity {
    public List<NewFirstReward.DayReward> firstReward = new ArrayList<>();

    public NewFirstReward(ActivityBO data) {
        super(data);
    }

    @Override
    public void load(JsonObject json) throws WSException {
        for (JsonElement element : json.get("rewards").getAsJsonArray()) {
            JsonObject obj = element.getAsJsonObject();
            NewFirstReward.DayReward builder = new NewFirstReward.DayReward(obj);
            this.firstReward.add(builder);
        }
    }

    @Override
    public String check(JsonObject json) throws RequestException {
        return "ok";
    }

    @Override
    public ActivityType getType() {
        return ActivityType.NewFirstReward;
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

    public void dailyRefesh(Player player, int days) {
        if (!this.isFirstRecharge(player)) {
            ActivityRecordBO bo = this.getRecord(player);
            if (bo.getExtInt(2) <= this.firstReward.size() - 1) {
                if (bo.getExtInt(2) == this.firstReward.size() - 1) {
                    bo.saveExtInt(2, bo.getExtInt(2) + days);
                } else {
                    this.resetRecharge(player);
                    bo.saveExtInt(3, 0);
                    bo.saveExtInt(2, bo.getExtInt(2) + days);
                }
            }
        }
    }

    public void resetRecharge(Player player) {
        for (RefRecharge ref : RefDataMgr.getAll(RefRecharge.class).values()) {
            if (ref.RebateAchievement != AchievementType.MonthCardCrystal && ref.RebateAchievement != AchievementType.YearCardCrystal) {
                RechargeFeature rechargeFeature = player.getFeature(RechargeFeature.class);
                PlayerRechargeRecordBO record = rechargeFeature.getRecharged(ref.id);
                if (record != null) {
                    record.saveResetSign(CommTime.nowSecond() + "+" + player.getPid());
                }
            }
        }
    }

    public NewFirstReward.fristRewardProto fristRechargeProto(Player player) {
        NewFirstReward.fristRewardProto proto = new NewFirstReward.fristRewardProto();
        proto.isFirstRecharge = this.isFirstRecharge(player);
        proto.firstReward = this.firstReward;
        ActivityRecordBO bo = this.getOrCreateRecord(player);
        int times = bo.getExtInt(2);
        proto.times = times;
        proto.isOver = times > this.firstReward.size() - 1;
        proto.todayRecharge = bo.getExtInt(3);
        return proto;
    }

    public void setFirstReward(Player player) {
        boolean isFirstRecharge = this.isFirstRecharge(player);
        if (isFirstRecharge) {
            ActivityRecordBO bo = this.getOrCreateRecord(player);
            bo.setExtInt(0, 1);
            bo.setExtInt(1, CommTime.nowSecond());
            bo.saveAll();
            this.resetRecharge(player);
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
                if (bo.getExtInt(3) != 0) {
                    return;
                }

                int times = bo.getExtInt(2);
                Reward reward = this.getReward(times + 1);
                if (reward == null) {
                    return;
                }

                bo.setExtInt(3, 1);
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
        for (NewFirstReward.DayReward day : this.firstReward) {
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
        List<NewFirstReward.DayReward> firstReward;
        int times;
        boolean isOver;
        int todayRecharge;

        private fristRewardProto() {
        }
    }
}
