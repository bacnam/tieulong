package business.global.activity.detail;

import business.global.activity.Activity;
import business.player.Player;
import business.player.feature.PlayerCurrency;
import business.player.feature.PlayerItem;
import business.player.item.Reward;
import com.google.gson.JsonObject;
import com.zhonglian.server.common.enums.ActivityStatus;
import com.zhonglian.server.common.enums.ActivityType;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.http.server.RequestException;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.exception.WSException;
import core.config.refdata.ref.RefCrystalPrice;
import core.database.game.bo.ActivityBO;
import core.database.game.bo.ActivityRecordBO;
import core.server.OpenSeverTime;

public class ActivityInstance extends Activity {
    private Integer challengTimes;
    private Integer costId;
    private Integer costCount;
    private Integer gainId;
    private Integer gainCount;
    private Integer buyTimes;
    private ActivityInstance.RefInstanceInfo instanceref;
    private int begin = 0;
    private int end = 0;
    private int close;

    public ActivityInstance(ActivityBO data) {
        super(data);
    }

    @Override
    public void load(JsonObject json) throws WSException {
        this.begin = json.get("begin").getAsInt();
        this.end = json.get("end").getAsInt();
        this.close = json.get("close").getAsInt();
        this.challengTimes = json.get("challengTimes").getAsInt();
        this.costId = json.get("costId").getAsInt();
        this.costCount = json.get("costCount").getAsInt();
        this.gainId = json.get("gainId").getAsInt();
        this.gainCount = json.get("gainCount").getAsInt();
        this.buyTimes = json.get("buyTimes").getAsInt();
        this.instanceref = new ActivityInstance.RefInstanceInfo(json.get("Ref").getAsJsonObject());
    }

    @Override
    public int getBeginTime() {
        return OpenSeverTime.getInstance().getOpenZeroTime() + this.begin;
    }

    @Override
    public int getEndTime() {
        return OpenSeverTime.getInstance().getOpenZeroTime() + this.end;
    }

    @Override
    public int getCloseTime() {
        return OpenSeverTime.getInstance().getOpenZeroTime() + this.close;
    }

    @Override
    public String check(JsonObject json) throws RequestException {
        return "";
    }

    public void handDailyRefresh(Player player) {
        try {
            if (this.getStatus() != ActivityStatus.Open) {
                return;
            }

            ActivityRecordBO bo = this.getRecord(player);
            if (bo == null) {
                return;
            }

            bo.setExtInt(0, 0);
            bo.setExtInt(1, 0);
            bo.saveAll();
            player.pushProto("activityInstanceRefresh", this.protoInfo(player));
        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }

    public ActivityInstance.Response protoInfo(Player player) {
        return new ActivityInstance.Response(
            this.getLeftTimes(player), this.getBuyTimes() - this.getBuyTimes(player), this.getCost(), this.getReward(), this.getRef());
    }

    @Override
    public ActivityRecordBO createPlayerActRecord(Player player) {
        ActivityRecordBO bo = new ActivityRecordBO();
        bo.setPid(player.getPid());
        bo.setAid(this.bo.getId());
        bo.setActivity(this.getType().toString());
        bo.setExtInt(0, 0);
        bo.setExtInt(1, 0);
        bo.insert();
        return bo;
    }

    @Override
    public ActivityType getType() {
        return ActivityType.ActivityInstance;
    }

    public int getLeftTimes(Player player) {
        return this.challengTimes - this.getOrCreateRecord(player).getExtInt(0);
    }

    public int getBuyTimes(Player player) {
        return this.getOrCreateRecord(player).getExtInt(1);
    }

    public Reward getCost() {
        return new Reward(this.costId, this.costCount);
    }

    public Reward getReward() {
        return new Reward(this.gainId, this.gainCount);
    }

    public ActivityInstance.RefInstanceInfo getRef() {
        return this.instanceref;
    }

    public int getBuyTimes() {
        return this.buyTimes;
    }

    public Reward instanceWin(Player player) {
        if (this.getStatus() != ActivityStatus.Open) {
            return null;
        } else {
            ActivityRecordBO bo = this.getOrCreateRecord(player);
            if (bo.getExtInt(0) >= this.challengTimes) {
                return null;
            } else if (!player.getFeature(PlayerItem.class).checkAndConsume(this.getCost(), ItemFlow.ActivityInstance)) {
                return null;
            } else {
                Reward reward = this.getReward();
                player.getFeature(PlayerItem.class).gain(reward, ItemFlow.ActivityInstance);
                bo.setExtInt(0, bo.getExtInt(0) + 1);
                bo.saveAll();
                return reward;
            }
        }
    }

    public void buyTimes(Player player) {
        if (this.getBuyTimes(player) < this.getBuyTimes()) {
            RefCrystalPrice prize = RefCrystalPrice.getPrize(this.getBuyTimes(player));
            int count = prize.ActivityInstanceAddChallenge;
            PlayerCurrency currency = player.getFeature(PlayerCurrency.class);
            if (currency.check(PrizeType.Crystal, count)) {
                currency.consume(PrizeType.Crystal, count, ItemFlow.AddInstanceChallenge);
                ActivityRecordBO bo = this.getOrCreateRecord(player);
                bo.saveExtInt(0, bo.getExtInt(0) - 1);
                bo.saveExtInt(1, bo.getExtInt(1) + 1);
            }
        }
    }

    @Override
    public void onClosed() {
        this.clearActRecord();
    }

    @Override
    public void onOpen() {
    }

    @Override
    public void onEnd() {
    }

    public static class RefInstanceInfo {
        String MapId;
        int Level;
        String Name;
        String BossName;
        String Icon;
        int Power;
        int BeginDialog;
        int EndDialog;
        String Wave1;
        String Wave2;
        String Wave3;
        String Wave4;
        String Wave5;

        private RefInstanceInfo(JsonObject json) {
            this.MapId = json.get("MapId").getAsString();
            this.Level = json.get("Level").getAsInt();
            this.Name = json.get("Name").getAsString();
            this.BossName = json.get("BossName").getAsString();
            this.Icon = json.get("Icon").getAsString();
            this.Power = json.get("Power").getAsInt();
            this.BeginDialog = json.get("BeginDialog").getAsInt();
            this.EndDialog = json.get("EndDialog").getAsInt();
            this.Wave1 = json.get("Wave1").getAsString();
            this.Wave2 = json.get("Wave2").getAsString();
            this.Wave3 = json.get("Wave3").getAsString();
            this.Wave4 = json.get("Wave4").getAsString();
            this.Wave5 = json.get("Wave5").getAsString();
        }
    }

    private static class Response {
        int leftTimes;
        int leftBuyTimes;
        Reward costItem;
        Reward rewardItem;
        ActivityInstance.RefInstanceInfo ref;

        private Response(int leftTimes, int leftBuyTimes, Reward costItem, Reward rewardItem, ActivityInstance.RefInstanceInfo ref) {
            this.leftTimes = leftTimes;
            this.leftBuyTimes = leftBuyTimes;
            this.costItem = costItem;
            this.rewardItem = rewardItem;
            this.ref = ref;
        }
    }
}
