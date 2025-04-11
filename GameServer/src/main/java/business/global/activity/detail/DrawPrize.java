package business.global.activity.detail;

import business.global.activity.Activity;
import business.global.gmmail.MailCenter;
import business.global.rank.RankManager;
import business.global.rank.Record;
import business.player.Player;
import business.player.feature.PlayerItem;
import business.player.item.Reward;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.zhonglian.server.common.data.ref.matcher.NumberRange;
import com.zhonglian.server.common.enums.ActivityStatus;
import com.zhonglian.server.common.enums.ActivityType;
import com.zhonglian.server.common.enums.FetchStatus;
import com.zhonglian.server.common.enums.RankType;
import com.zhonglian.server.common.utils.CommMath;
import com.zhonglian.server.common.utils.Lists;
import com.zhonglian.server.common.utils.Maps;
import com.zhonglian.server.common.utils.StringUtils;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import core.database.game.bo.ActivityBO;
import core.database.game.bo.ActivityRecordBO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DrawPrize
        extends Activity {
    public int price;
    public int tenPrice;
    public List<DrawTaskInfo> arList;
    public int maxRank = 0;
    int point;
    List<Integer> NormalIdList;
    List<Integer> NormalCountList;
    List<Integer> NormalWeightList;
    List<Integer> LeastIdList;
    List<Integer> LeastCountList;
    List<Integer> LeastWeightList;
    List<Reward> RewardList = new ArrayList<>();
    List<RankReward> rankList = new ArrayList<>();

    public DrawPrize(ActivityBO bo) {
        super(bo);
    }

    public void load(JsonObject json) throws WSException {
        JsonObject draw = json.get("DrawList").getAsJsonObject();
        this.price = draw.get("price").getAsInt();
        this.tenPrice = draw.get("tenprice").getAsInt();
        this.point = draw.get("point").getAsInt();
        this.NormalIdList = StringUtils.string2Integer(draw.get("NormalIdList").getAsString());
        this.NormalCountList = StringUtils.string2Integer(draw.get("NormalCountList").getAsString());
        this.NormalWeightList = StringUtils.string2Integer(draw.get("NormalWeightList").getAsString());
        this.LeastIdList = StringUtils.string2Integer(draw.get("LeastIdList").getAsString());
        this.LeastCountList = StringUtils.string2Integer(draw.get("LeastCountList").getAsString());
        this.LeastWeightList = StringUtils.string2Integer(draw.get("LeastWeightList").getAsString());

        this.arList = Lists.newArrayList();
        for (JsonElement element : json.get("ExtReward").getAsJsonArray()) {
            JsonObject obj = element.getAsJsonObject();
            DrawTaskInfo builder = new DrawTaskInfo(null);
            builder.awardId = obj.get("aid").getAsInt();
            builder.condition = obj.get("condition").getAsInt();
            builder.prize = new Reward(obj.get("items").getAsJsonArray());
            this.arList.add(builder);
        }

        for (JsonElement ele : json.get("RankReward").getAsJsonArray()) {
            JsonObject obj = ele.getAsJsonObject();
            RankReward rankreward = new RankReward(null);
            rankreward.rank = NumberRange.parse(obj.get("rank").getAsString());
            rankreward.reward = new Reward(obj.get("reward").getAsJsonArray());
            this.rankList.add(rankreward);
            if (rankreward.rank.getTop() > this.maxRank) {
                this.maxRank = rankreward.rank.getTop();
            }
        }
    }

    public void onOpen() {
        clearActRecord();
        RankManager.getInstance().clear(RankType.DrawPoint);
    }

    public void onEnd() {
        settle();
    }

    public void onClosed() {
    }

    public ActivityType getType() {
        return ActivityType.DrawPrize;
    }

    public void settle() {
        List<Record> records = RankManager.getInstance().getRankList(RankType.DrawPoint, this.maxRank);
        for (Record record : records) {
            if (record == null)
                continue;
            Reward reward = getReward(record.getRank());
            if (reward != null)
                MailCenter.getInstance().sendMail(record.getPid(), this.ref.MailSender, this.ref.MailTitle, this.ref.MailContent, reward, new String[0]);
        }
    }

    private Reward getReward(int rank) {
        for (RankReward reward : this.rankList) {
            if (reward.rank.within(rank))
                return reward.reward;
        }
        return null;
    }

    public Reward find(Player player, int times) {
        Reward reward = new Reward();

        for (int i = 0; i < times; i++) {
            if (times > 1 && times - i <= 1) {
                int index = CommMath.getRandomIndexByRate(this.LeastWeightList);
                int uniformID = ((Integer) this.LeastIdList.get(index)).intValue();
                int count = ((Integer) this.LeastCountList.get(index)).intValue();
                reward.combine(new Reward(uniformID, count));
            } else {

                int index = CommMath.getRandomIndexByRate(this.NormalWeightList);
                int uniformID = ((Integer) this.NormalIdList.get(index)).intValue();
                int count = ((Integer) this.NormalCountList.get(index)).intValue();
                reward.combine(new Reward(uniformID, count));
            }
        }
        getExtReward(player, times);

        return reward;
    }

    public void getExtReward(Player player, int times) {
        if (getStatus() != ActivityStatus.Open) {
            return;
        }
        ActivityRecordBO bo = getOrCreateRecord(player);
        bo.setExtInt(0, bo.getExtInt(0) + this.point * times);

        List<Integer> awardList = StringUtils.string2Integer(bo.getExtStr(0));

        List<Integer> stateList = StringUtils.string2Integer(bo.getExtStr(1));

        this.arList.forEach(x -> {
            if (!paramList1.contains(Integer.valueOf(x.awardId)) && paramActivityRecordBO.getExtInt(0) >= x.condition) {
                paramList1.add(Integer.valueOf(x.awardId));
                paramList2.add(Integer.valueOf(FetchStatus.Can.ordinal()));
            }
        });
        bo.setExtStr(0, StringUtils.list2String(awardList));
        bo.setExtStr(1, StringUtils.list2String(stateList));
        bo.saveAll();
        RankManager.getInstance().update(RankType.DrawPoint, player.getPid(), bo.getExtInt(0));

        player.pushProto("DrawPrize", accumRechargeProto(player));
    }

    public accumRechargeInfoP accumRechargeProto(Player player) {
        ActivityRecordBO bo = getRecord(player);

        int recharge = 0;
        if (bo != null) {
            recharge = bo.getExtInt(0);
        }

        List<Integer> awardList = null, stateList = null;
        if (bo != null) {
            awardList = StringUtils.string2Integer(bo.getExtStr(0));
            stateList = StringUtils.string2Integer(bo.getExtStr(1));
        }
        List<DrawTaskInfo> accumerecharges = Lists.newArrayList();

        for (DrawTaskInfo ar : this.arList) {
            DrawTaskInfo builder = new DrawTaskInfo(null);
            builder.setAwardId(ar.getAwardId());
            builder.setCondition(ar.getCondition());
            builder.setPrize(ar.getPrize());
            int index = (awardList == null) ? -1 : awardList.indexOf(Integer.valueOf(ar.getAwardId()));
            if (index != -1) {
                builder.setStatus(((Integer) stateList.get(index)).intValue());
            } else {
                builder.setStatus(FetchStatus.Cannot.ordinal());
            }
            accumerecharges.add(builder);
        }

        return new accumRechargeInfoP(this.price, this.tenPrice, this.point, RankManager.getInstance().getRank(RankType.DrawPoint, player.getPid()), recharge,
                accumerecharges, new Reward(this.NormalIdList, this.NormalCountList), this.rankList, null);
    }

    public DrawTaskInfo doReceivePrize(Player player, int awardId) throws WSException {
        Map<Integer, DrawTaskInfo> arMap = Maps.list2Map(DrawTaskInfo::getAwardId, this.arList);

        DrawTaskInfo arInfo = arMap.get(Integer.valueOf(awardId));
        if (arInfo == null) {
            throw new WSException(ErrorCode.NotFound_ActivityAwardId, "奖励ID[%s]未找到", new Object[]{Integer.valueOf(awardId)});
        }

        ActivityRecordBO bo = getOrCreateRecord(player);

        List<Integer> awardList = StringUtils.string2Integer(bo.getExtStr(0));

        List<Integer> stateList = StringUtils.string2Integer(bo.getExtStr(1));

        int index = awardList.indexOf(Integer.valueOf(awardId));
        if (index == -1) {
            throw new WSException(ErrorCode.AccumRecharge_CanNotReceive, "充值金额:%s<需求金额:%s", new Object[]{Integer.valueOf(bo.getExtInt(0)), Integer.valueOf(arInfo.getCondition())});
        }
        int state = ((Integer) stateList.get(index)).intValue();
        if (state != FetchStatus.Can.ordinal()) {
            throw new WSException(ErrorCode.AccumRecharge_HasReceive, "奖励ID[%s]已领取", new Object[]{Integer.valueOf(awardId)});
        }

        stateList.set(index, Integer.valueOf(FetchStatus.Fetched.ordinal()));
        bo.saveExtStr(1, StringUtils.list2String(stateList));

        Reward reward = arInfo.getPrize();

        ((PlayerItem) player.getFeature(PlayerItem.class)).gain(reward, ItemFlow.Activity_AccumRecharge);

        DrawTaskInfo builder = new DrawTaskInfo(null);
        builder.setAwardId(awardId);
        builder.setStatus(FetchStatus.Fetched.ordinal());
        builder.setPrize(reward);
        return builder;
    }

    private static class RankReward {
        NumberRange rank;
        Reward reward;

        private RankReward() {
        }
    }

    private static class DrawTaskInfo {
        private int awardId;
        private int status;
        private int condition;
        private Reward prize;

        private DrawTaskInfo() {
        }

        public int getAwardId() {
            return this.awardId;
        }

        public void setAwardId(int awardId) {
            this.awardId = awardId;
        }

        public int getStatus() {
            return this.status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getCondition() {
            return this.condition;
        }

        public void setCondition(int condition) {
            this.condition = condition;
        }

        public Reward getPrize() {
            return this.prize;
        }

        public void setPrize(Reward prize) {
            this.prize = prize;
        }
    }

    private class accumRechargeInfoP {
        int price;
        int tenprice;
        int point;
        int rank;
        int condition;
        List<DrawPrize.DrawTaskInfo> accumerecharges;
        Reward reward;
        List<DrawPrize.RankReward> rankList;

        private accumRechargeInfoP(int price, int tenprice, int point, int rank, int condition, List<DrawPrize.DrawTaskInfo> accumerecharges, Reward reward, List<DrawPrize.RankReward> rankList) {
            this.price = price;
            this.tenprice = tenprice;
            this.point = point;
            this.rank = rank;
            this.condition = condition;
            this.accumerecharges = accumerecharges;
            this.reward = reward;
            this.rankList = rankList;
        }
    }
}

