package business.global.activity;

import business.global.rank.RankManager;
import business.player.item.Reward;
import business.player.item.UniformItem;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.zhonglian.server.common.data.ref.matcher.NumberRange;
import com.zhonglian.server.common.enums.ActivityStatus;
import com.zhonglian.server.common.enums.ActivityType;
import com.zhonglian.server.common.enums.RankType;
import com.zhonglian.server.common.enums.ConstEnum.VIPGiftType;
import com.zhonglian.server.websocket.exception.WSException;
import core.database.world.bo.ActivityBO;
import core.database.world.bo.ActivityRecordBO;
import java.util.ArrayList;
import java.util.List;
import proto.gameworld.RankAward;
import proto.gameworld.UniformItemInfo;
import proto.gamezone.Player.PlayerInfo;

public class RankActivity extends Activity {
    public List<RankAward> rankrewardList;
    public int mailId;

    public RankActivity(ActivityBO bo) {
        super(bo);
    }

    public VIPGiftType getAwardType() {
        return null;
    }

    public void Update(PlayerInfo player, long crystal) {
        synchronized (this) {
            if (this.getStatus() == ActivityStatus.Open) {
                ActivityRecordBO bo = this.getOrCreateRecord(player);
                bo.saveExtInt(0, bo.getExtInt(0) + (int)crystal);
                RankManager.getInstance().update(this.getRankType(), player.pid, bo.getExtInt(0), player.serverId);
            }
        }
    }

    @Override
    public void onOpen() {
        RankManager.getInstance().clear(this.getRankType());
        this.clearActRecord();
    }

    @Override
    public void onEnd() {
    }

    @Override
    public void onClosed() {
    }

    @Override
    public ActivityType getType() {
        return null;
    }

    public RankType getRankType() {
        return null;
    }

    @Override
    public void load(JsonObject json) throws WSException {
        this.rankrewardList = new ArrayList<>();

        for (JsonElement element : json.get("rankawards").getAsJsonArray()) {
            JsonObject obeject = element.getAsJsonObject();
            RankAward ref = new RankAward();
            ref.aid = obeject.get("aid").getAsInt();
            ref.rankrange = NumberRange.parse(obeject.get("rankrange").getAsString());
            ArrayList<UniformItemInfo> rewardlist = new ArrayList<>();

            for (UniformItem reward : new Reward(obeject.get("items").getAsJsonArray())) {
                UniformItemInfo info = new UniformItemInfo();
                info.uniformId = reward.getUniformId();
                info.count = reward.getCount();
                rewardlist.add(info);
            }

            ref.reward = rewardlist;
            this.rankrewardList.add(ref);
        }

        this.mailId = Integer.parseInt(this.ref.MailTitle);
    }
}
