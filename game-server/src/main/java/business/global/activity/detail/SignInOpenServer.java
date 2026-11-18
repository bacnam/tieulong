package business.global.activity.detail;

import business.global.activity.Activity;
import business.player.Player;
import business.player.feature.PlayerItem;
import business.player.item.Reward;
import com.google.gson.JsonObject;
import com.zhonglian.server.common.enums.ActivityStatus;
import com.zhonglian.server.common.enums.ActivityType;
import com.zhonglian.server.common.enums.SignInType;
import com.zhonglian.server.http.server.RequestException;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefSignIn;
import core.database.game.bo.ActivityBO;
import core.database.game.bo.ActivityRecordBO;
import core.server.OpenSeverTime;
import java.util.List;
import java.util.stream.Collectors;

public class SignInOpenServer extends Activity {
    public SignInOpenServer(ActivityBO data) {
        super(data);
    }

    @Override
    public void load(JsonObject json) throws WSException {
    }

    @Override
    public String check(JsonObject json) throws RequestException {
        return "ok";
    }

    @Override
    public ActivityType getType() {
        return ActivityType.SignInOpenServer;
    }

    @Override
    public int getBeginTime() {
        return OpenSeverTime.getInstance().getOpenZeroTime() + RefDataMgr.getFactor("SevenDayActivityOpenTime", 0);
    }

    @Override
    public int getEndTime() {
        return OpenSeverTime.getInstance().getOpenZeroTime() + 604800 - 5;
    }

    @Override
    public int getCloseTime() {
        return OpenSeverTime.getInstance().getOpenZeroTime() + 604800 + RefDataMgr.getFactor("SevenDayActivitycloseOffset", 86400);
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

    public void handDailyRefresh(Player player) {
        try {
            if (this.getStatus() != ActivityStatus.Open) {
                return;
            }

            ActivityRecordBO bo = this.getRecord(player);
            if (bo == null) {
                return;
            }

            if (bo.getExtInt(1) == 0) {
                return;
            }

            if (bo.getExtInt(0) >= RefSignIn.SignInOpenServerMaxDay) {
                return;
            }

            bo.setExtInt(1, 0);
            bo.saveAll();
            player.pushProto("signInOpenServerRefresh", this.dailySignProto(player));
        } catch (Exception var3) {
            var3.printStackTrace();
        }
    }

    public SignInOpenServer.signInfo dailySignProto(Player player) {
        ActivityRecordBO bo = this.getOrCreateRecord(player);
        return new SignInOpenServer.signInfo(bo.getExtInt(0), bo.getExtInt(1) == 1);
    }

    private int getKey(int signInCount) {
        return SignInType.SignInOpenServer.ordinal() * 1000 + signInCount;
    }

    public SignInOpenServer.signInfo doSignIn(Player player) throws WSException {
        ActivityRecordBO bo = this.getOrCreateRecord(player);
        if (bo.getExtInt(1) == 1) {
            throw new WSException(ErrorCode.Signin_AlreadySigned, "今天已签到过");
        } else {
            bo.setExtInt(0, bo.getExtInt(0) + 1);
            bo.setExtInt(1, 1);
            bo.saveAll();
            RefSignIn refSignIn = RefDataMgr.get(RefSignIn.class, this.getKey(bo.getExtInt(0)));
            List<Integer> count = refSignIn.Count;
            if (player.getVipLevel() >= refSignIn.VIPLevel) {
                count = refSignIn.Count.stream().map(x -> x * refSignIn.VIPTimes).collect(Collectors.toList());
            }

            Reward pack = player.getFeature(PlayerItem.class).gain(refSignIn.UniformitemId, count, ItemFlow.Activity_SignInDaily);
            return new SignInOpenServer.signInfo(bo.getExtInt(0), bo.getExtInt(1) == 1, pack);
        }
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

    private static class signInfo {
        int count;
        boolean isSigned;
        Reward reward;

        public signInfo(int count, boolean isSigned) {
            this.count = count;
            this.isSigned = isSigned;
        }

        public signInfo(int count, boolean isSigned, Reward reward) {
            this.count = count;
            this.isSigned = isSigned;
            this.reward = reward;
        }
    }
}
