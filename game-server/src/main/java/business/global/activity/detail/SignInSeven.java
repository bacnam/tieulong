package business.global.activity.detail;

import business.global.activity.Activity;
import business.player.Player;
import business.player.PlayerMgr;
import business.player.feature.PlayerCurrency;
import business.player.feature.PlayerItem;
import business.player.item.Reward;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.zhonglian.server.common.enums.ActivityStatus;
import com.zhonglian.server.common.enums.ActivityType;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.common.utils.Lists;
import com.zhonglian.server.common.utils.StringUtils;
import com.zhonglian.server.http.server.RequestException;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import core.database.game.bo.ActivityBO;
import core.database.game.bo.ActivityRecordBO;
import java.util.List;

public class SignInSeven extends Activity {
    public List<SignInSeven.SignIn> ardList;
    private int ReSignCost;

    public SignInSeven(ActivityBO data) {
        super(data);
    }

    @Override
    public void load(JsonObject json) throws WSException {
        this.ardList = Lists.newArrayList();

        for (JsonElement element : json.get("awards").getAsJsonArray()) {
            JsonObject obj = element.getAsJsonObject();
            SignInSeven.SignIn builder = new SignInSeven.SignIn();
            builder.aid = obj.get("aid").getAsInt();
            builder.day = obj.get("day").getAsInt();
            builder.reward = new Reward(obj.get("items").getAsJsonArray());
            this.ardList.add(builder);
        }

        this.ReSignCost = json.get("ReSignCost").getAsInt();
    }

    @Override
    public String check(JsonObject json) throws RequestException {
        return "ok";
    }

    @Override
    public ActivityType getType() {
        return ActivityType.SignInSeven;
    }

    public int getDay() {
        int second = CommTime.nowSecond() - CommTime.getZeroClockS(this.getBeginTime());
        return second / 86400;
    }

    public void handDailyRefresh() {
        try {
            if (this.getStatus() != ActivityStatus.Open) {
                return;
            }

            this.bo.setExtInt(0, this.getDay());
            this.bo.saveAll();

            for (Player player : PlayerMgr.getInstance().getOnlinePlayers()) {
                player.pushProto("signInSevenRefresh", this.dailySignProto(player));
            }
        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }

    public SignInSeven.signInfo dailySignProto(Player player) {
        ActivityRecordBO bo = this.getOrCreateRecord(player);
        int today = this.bo.getExtInt(0) + 1;
        return new SignInSeven.signInfo(today, bo.getExtStr(0), this.ReSignCost, this.ardList);
    }

    private SignInSeven.SignIn getKey(int day) {
        for (SignInSeven.SignIn signin : this.ardList) {
            if (signin.day == day) {
                return signin;
            }
        }

        return null;
    }

    public SignInSeven.signInfo doSignIn(Player player, int day) throws WSException {
        ActivityRecordBO bo = this.getOrCreateRecord(player);
        List<Integer> list = StringUtils.string2Integer(bo.getExtStr(0));
        int today = this.bo.getExtInt(0) + 1;
        if (list.contains(day)) {
            throw new WSException(ErrorCode.SignIn_AlreadyPick, "奖励已领取");
        } else if (today != day) {
            throw new WSException(ErrorCode.SignIn_OnlyToday, "只能签到当天");
        } else {
            SignInSeven.SignIn signIn = this.getKey(today);
            if (signIn == null) {
                throw new WSException(ErrorCode.SignIn_NotFound, "签到未找到");
            } else {
                Reward pack = player.getFeature(PlayerItem.class).gain(signIn.reward, ItemFlow.Activity_SignInDaily);
                list.add(day);
                bo.saveExtStr(0, StringUtils.list2String(list));
                return new SignInSeven.signInfo(today, bo.getExtStr(0), pack);
            }
        }
    }

    public SignInSeven.signInfo reSign(Player player, int day) throws WSException {
        ActivityRecordBO bo = this.getOrCreateRecord(player);
        List<Integer> list = StringUtils.string2Integer(bo.getExtStr(0));
        int today = this.bo.getExtInt(0) + 1;
        if (list.contains(day)) {
            throw new WSException(ErrorCode.SignIn_AlreadyPick, "奖励已领取");
        } else if (today <= day) {
            throw new WSException(ErrorCode.SignIn_OnlyToday, "只能补签以前的日子");
        } else if (!player.getFeature(PlayerCurrency.class).checkAndConsume(PrizeType.Crystal, this.ReSignCost, ItemFlow.ReSign)) {
            throw new WSException(ErrorCode.NotEnough_Crystal, "元宝不足");
        } else {
            SignInSeven.SignIn signIn = this.getKey(today);
            if (signIn == null) {
                throw new WSException(ErrorCode.SignIn_NotFound, "签到未找到");
            } else {
                Reward pack = player.getFeature(PlayerItem.class).gain(signIn.reward, ItemFlow.Activity_SignInDaily);
                list.add(day);
                bo.saveExtStr(0, StringUtils.list2String(list));
                return new SignInSeven.signInfo(today, bo.getExtStr(0), pack);
            }
        }
    }

    @Override
    public void onOpen() {
        if (this.bo.getExtInt(0) < 0) {
            this.bo.saveExtInt(0, 0);
        }
    }

    @Override
    public void onEnd() {
    }

    @Override
    public void onClosed() {
        this.clearActRecord();
        this.bo.saveExtInt(0, 0);
    }

    private static class SignIn {
        int aid;
        int day;
        Reward reward;

        private SignIn() {
        }
    }

    private static class signInfo {
        int day;
        String gain;
        Reward reward;
        int reSignCost;
        public List<SignInSeven.SignIn> ardList;

        public signInfo(int day, String gain, int reSignCost, List<SignInSeven.SignIn> ardList) {
            this.day = day;
            this.gain = gain;
            this.reSignCost = reSignCost;
            this.ardList = ardList;
        }

        public signInfo(int day, String gain, Reward reward) {
            this.day = day;
            this.gain = gain;
            this.reward = reward;
        }
    }
}
