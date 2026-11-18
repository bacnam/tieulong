package business.global.activity.detail;

import business.global.activity.Activity;
import business.global.gmmail.MailCenter;
import business.player.Player;
import business.player.feature.PlayerItem;
import business.player.item.Reward;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.zhonglian.server.common.enums.ActivityStatus;
import com.zhonglian.server.common.enums.ActivityType;
import com.zhonglian.server.common.utils.CommMath;
import com.zhonglian.server.common.utils.Lists;
import com.zhonglian.server.common.utils.StringUtils;
import com.zhonglian.server.http.server.RequestException;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import core.database.game.bo.ActivityBO;
import core.database.game.bo.ActivityRecordBO;
import java.util.ArrayList;
import java.util.List;

public class FortuneWheel extends Activity {
    public List<FortuneWheel.WheelExtReward> extRewards;
    public List<Integer> weightList;
    List<FortuneWheel.RollReward> rollReward;
    public int recharge;
    public int doubleNum;
    public List<FortuneWheel.WheelRecord> records = new ArrayList<>();

    public FortuneWheel(ActivityBO data) {
        super(data);
    }

    @Override
    public void load(JsonObject json) throws WSException {
        this.extRewards = Lists.newArrayList();

        for (JsonElement element : json.get("awards").getAsJsonArray()) {
            JsonObject obj = element.getAsJsonObject();
            FortuneWheel.WheelExtReward builder = new FortuneWheel.WheelExtReward();
            builder.setAwardId(obj.get("aid").getAsInt());
            builder.setTimes(obj.get("times").getAsInt());
            builder.setPrize(new Reward(obj.get("items").getAsJsonArray()));
            this.extRewards.add(builder);
        }

        this.weightList = Lists.newArrayList();
        this.rollReward = Lists.newArrayList();

        for (JsonElement element : json.get("roll_awards").getAsJsonArray()) {
            JsonObject obj = element.getAsJsonObject();
            this.weightList.add(obj.get("weight").getAsInt());
            Reward reward = new Reward(obj.get("items").getAsJsonArray());
            this.rollReward.add(new FortuneWheel.RollReward(obj.get("aid").getAsInt(), reward));
        }

        this.recharge = json.get("recharge").getAsInt();
        this.doubleNum = json.get("double").getAsInt();
    }

    @Override
    public String check(JsonObject json) throws RequestException {
        return "ok";
    }

    @Override
    public ActivityType getType() {
        return ActivityType.FortuneWheel;
    }

    @Override
    public void onClosed() {
        this.clearActRecord();
    }

    public void dailyRefresh(Player player) {
        if (this.getStatus() == ActivityStatus.Open) {
            ActivityRecordBO bo = this.getOrCreateRecord(player);
            bo.saveExtInt(0, bo.getExtInt(0) + 1);
            player.pushProto("FortuneWheelInfo", this.accumRechargeProto(player));
        }
    }

    public void handlePlayerChange(Player player, int money) {
        if (this.getStatus() == ActivityStatus.Open) {
            ActivityRecordBO bo = this.getOrCreateRecord(player);
            int now_money = bo.getExtInt(2);
            now_money += money;

            int times;
            for (times = 0; now_money >= this.recharge; times++) {
                now_money -= this.recharge;
            }

            bo.saveExtInt(2, now_money);
            bo.setExtInt(0, bo.getExtInt(0) + times);
            bo.saveAll();
            player.pushProto("FortuneWheelInfo", this.accumRechargeProto(player));
        }
    }

    public FortuneWheel.FortuneWheelInfo accumRechargeProto(Player player) {
        FortuneWheel.FortuneWheelInfo info = new FortuneWheel.FortuneWheelInfo();
        ActivityRecordBO bo = this.getOrCreateRecord(player);
        info.rechargeNum = this.recharge;
        info.doubleNum = this.doubleNum;
        info.timesReward = this.extRewards;
        info.rollReward = this.rollReward;
        info.haveTimes = bo.getExtInt(0);
        info.useTimes = bo.getExtInt(1);
        info.nowRecharge = bo.getExtInt(2);
        info.records = this.records;
        return info;
    }

    public FortuneWheel.RollReward roll(Player player) throws WSException {
        Reward finalreward = new Reward();
        ActivityRecordBO bo = this.getOrCreateRecord(player);
        if (bo.getExtInt(0) <= 0) {
            throw new WSException(ErrorCode.Wheel_NoTimes, "次数不足");
        } else {
            int index = CommMath.getRandomIndexByRate(this.weightList);
            Reward reward = this.rollReward.get(index).reward;
            int num = bo.getExtInt(1) / this.doubleNum + 1;

            for (int i = 0; i < num; i++) {
                finalreward.combine(reward);
            }

            player.getFeature(PlayerItem.class).gain(finalreward, ItemFlow.FortuneWheel);
            bo.setExtInt(0, bo.getExtInt(0) - 1);
            bo.setExtInt(1, bo.getExtInt(1) + 1);
            bo.saveAll();
            this.checkTimes(player);
            synchronized (this) {
                if (this.records.size() >= 30) {
                    this.records.remove(0);
                }

                this.records.add(new FortuneWheel.WheelRecord(player.getName(), finalreward));
            }

            player.pushProto("FortuneWheelInfo", this.accumRechargeProto(player));
            return new FortuneWheel.RollReward(this.rollReward.get(index).aid, finalreward);
        }
    }

    public void checkTimes(Player player) {
        ActivityRecordBO bo = this.getOrCreateRecord(player);
        List<Integer> extreward = StringUtils.string2Integer(bo.getExtStr(0));

        for (FortuneWheel.WheelExtReward ext : this.extRewards) {
            if (!extreward.contains(ext.awardId) && bo.getExtInt(1) >= ext.times) {
                MailCenter.getInstance()
                    .sendMail(player.getPid(), this.ref.MailSender, this.ref.MailTitle, this.ref.MailContent, ext.prize, String.valueOf(bo.getExtInt(1)));
                extreward.add(ext.awardId);
                bo.saveExtStr(0, StringUtils.list2String(extreward));
            }
        }
    }

    @Override
    public void onOpen() {
    }

    @Override
    public void onEnd() {
    }

    @Override
    public ActivityRecordBO createPlayerActRecord(Player player) {
        ActivityRecordBO bo = new ActivityRecordBO();
        bo.setPid(player.getPid());
        bo.setAid(this.bo.getId());
        bo.setActivity(this.getType().toString());
        bo.saveExtInt(0, 1);
        bo.insert();
        return bo;
    }

    private class FortuneWheelInfo {
        int rechargeNum;
        int doubleNum;
        List<FortuneWheel.WheelExtReward> timesReward;
        List<FortuneWheel.RollReward> rollReward;
        int haveTimes;
        int useTimes;
        int nowRecharge;
        List<FortuneWheel.WheelRecord> records;

        private FortuneWheelInfo() {
        }
    }

    private class RollReward {
        int aid;
        Reward reward;

        private RollReward(int aid, Reward reward) {
            this.aid = aid;
            this.reward = reward;
        }
    }

    public class WheelExtReward {
        private int awardId;
        private int times;
        private Reward prize;

        public int getAwardId() {
            return this.awardId;
        }

        public void setAwardId(int awardId) {
            this.awardId = awardId;
        }

        public int getTimes() {
            return this.times;
        }

        public void setTimes(int times) {
            this.times = times;
        }

        public Reward getPrize() {
            return this.prize;
        }

        public void setPrize(Reward prize) {
            this.prize = prize;
        }
    }

    private static class WheelRecord {
        String name;
        Reward reward;

        private WheelRecord(String name, Reward reward) {
            this.name = name;
            this.reward = reward;
        }
    }
}
