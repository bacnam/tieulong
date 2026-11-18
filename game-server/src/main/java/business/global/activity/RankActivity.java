package business.global.activity;

import business.player.Player;
import business.player.feature.PlayerCurrency;
import business.player.feature.PlayerItem;
import business.player.item.Reward;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.zhonglian.server.common.data.ref.matcher.NumberRange;
import com.zhonglian.server.common.enums.ActivityStatus;
import com.zhonglian.server.common.enums.ActivityType;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.common.enums.ConstEnum.VIPGiftType;
import com.zhonglian.server.common.utils.StringUtils;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import core.database.game.bo.ActivityBO;
import core.database.game.bo.ActivityRecordBO;
import core.network.proto.OpenServerRankRewardInfo;
import core.network.proto.VipAwardInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RankActivity extends Activity {
    private int require_cost = 0;
    private Reward reward;
    public List<RankActivity.RankAward> rankrewardList;
    List<RankActivity.VipAward> vipawardList;

    public RankActivity(ActivityBO bo) {
        super(bo);
    }

    public VIPGiftType getAwardType() {
        return null;
    }

    public Reward getReward() {
        return this.reward;
    }

    public int getRequire_cost() {
        return this.require_cost;
    }

    @Override
    public void load(JsonObject json) throws WSException {
        this.reward = new Reward(json.get("awards").getAsJsonArray());
        this.require_cost = json.get("cost").getAsInt();
        this.vipawardList = new ArrayList<>();

        for (JsonElement element : json.get("vipawards").getAsJsonArray()) {
            JsonObject obeject = element.getAsJsonObject();
            RankActivity.VipAward ref = new RankActivity.VipAward();
            ref.aid = obeject.get("aid").getAsInt();
            ref.vip = obeject.get("vip").getAsInt();
            ref.icon = obeject.get("icon").getAsString();
            ref.reward = new Reward(obeject.get("items").getAsJsonArray());
            ref.price = obeject.get("price").getAsInt();
            ref.discount = obeject.get("discount").getAsInt();
            ref.buytimes = StringUtils.string2Integer(obeject.get("buytimes").getAsString());
            this.vipawardList.add(ref);
        }

        this.rankrewardList = new ArrayList<>();

        for (JsonElement element : json.get("rankawards").getAsJsonArray()) {
            JsonObject obeject = element.getAsJsonObject();
            RankActivity.RankAward ref = new RankActivity.RankAward();
            ref.aid = obeject.get("aid").getAsInt();
            ref.rankrange = NumberRange.parse(obeject.get("rankrange").getAsString());
            ref.reward = new Reward(obeject.get("items").getAsJsonArray());
            this.rankrewardList.add(ref);
        }
    }

    public int getCost(Player player) {
        ActivityRecordBO bo = this.getOrCreateRecord(player);
        return bo.getExtInt(0);
    }

    public void UpdateMaxRequire_cost(Player player, int crystal) {
        synchronized (this) {
            if (this.getStatus() == ActivityStatus.Open) {
                ActivityRecordBO bo = this.getOrCreateRecord(player);
                if (crystal > bo.getExtInt(0)) {
                    bo.saveExtInt(0, crystal);
                }
            }
        }
    }

    public OpenServerRankRewardInfo pickUpReward(Player player) throws WSException {
        ActivityRecordBO bo = this.getOrCreateRecord(player);
        int cost = this.getCost(player);
        if (cost < this.getRequire_cost()) {
            throw new WSException(ErrorCode.Not_Enough, "条件未达成");
        } else if (bo.getExtInt(1) != 0) {
            throw new WSException(ErrorCode.Already_Picked, "已领取");
        } else {
            bo.saveExtInt(1, 1);
            player.getFeature(PlayerItem.class).gain(this.getReward(), ItemFlow.RankReward);
            return new OpenServerRankRewardInfo(bo.getExtInt(0), bo.getExtInt(1) == 1, this.getReward());
        }
    }

    public boolean isPicked(Player player) {
        ActivityRecordBO bo = this.getOrCreateRecord(player);
        return bo.getExtInt(1) == 1;
    }

    public List<VipAwardInfo> vipAwardProto(Player player) {
        ActivityRecordBO bo = this.getOrCreateRecord(player);
        List<Integer> awardList = StringUtils.string2Integer(bo.getExtStr(0));
        List<Integer> hasBuyList = StringUtils.string2Integer(bo.getExtStr(1));
        List<RankActivity.VipAward> refList = this.vipawardList;
        return refList.stream().map(x -> {
            VipAwardInfo builder = new VipAwardInfo();
            builder.setAwardId(x.aid);
            builder.setMaxTimes(x.buytimes.get(player.getVipLevel()));
            builder.setReward(x.reward);
            builder.setIcon(x.icon);
            builder.setVip(x.vip);
            builder.setPrice(x.price);
            builder.setDiscount(x.discount);
            builder.setTimeslist(StringUtils.list2String(x.buytimes));
            int index = awardList.indexOf(x.aid);
            if (index < 0) {
                builder.setBuyTimes(0);
            } else {
                builder.setBuyTimes(hasBuyList.get(index));
            }

            return builder;
        }).collect(Collectors.toList());
    }

    public RankActivity.VipAward getVipRef(int rewardId) {
        for (RankActivity.VipAward ref : this.vipawardList) {
            if (ref.aid == rewardId) {
                return ref;
            }
        }

        return null;
    }

    public business.global.activity.detail.VipAward.VipAwardReceive doWeeklyReceive(Player player, int awardId) throws WSException {
        ActivityRecordBO bo = this.getOrCreateRecord(player);
        RankActivity.VipAward refVipAward = this.getVipRef(awardId);
        if (refVipAward == null) {
            throw new WSException(ErrorCode.NotFound_RefVipAward, "未找到[%s]的VIP礼包配置", awardId);
        } else if (refVipAward.vip > player.getVipLevel()) {
            throw new WSException(ErrorCode.VipAward_WeekNoNowVIPGift, "礼包VIP等级:%s != 玩家VIP等级:%s", refVipAward.vip, player.getVipLevel());
        } else {
            int maxTimes = refVipAward.buytimes.get(player.getVipLevel());
            List<Integer> awardList = StringUtils.string2Integer(bo.getExtStr(0));
            List<Integer> hasBuyList = StringUtils.string2Integer(bo.getExtStr(1));
            int buyTimes = 0;
            int index = awardList.indexOf(refVipAward.aid);
            if (index >= 0) {
                buyTimes = hasBuyList.get(index);
            }

            if (buyTimes >= maxTimes) {
                throw new WSException(ErrorCode.VipAward_WeekVIPGiftSold, "该礼包已卖光");
            } else {
                int cryStal = refVipAward.discount;
                if (!player.getFeature(PlayerCurrency.class).check(PrizeType.Crystal, cryStal)) {
                    throw new WSException(ErrorCode.NotEnough_Crystal, "玩家水晶:%s<购买水晶:%s", player.getPlayerBO().getCrystal(), cryStal);
                } else {
                    Reward reward = refVipAward.reward;
                    Reward pack = player.getFeature(PlayerItem.class).gain(reward, ItemFlow.Activity_WeeklyVipAward);
                    player.getFeature(PlayerCurrency.class).consume(PrizeType.Crystal, cryStal, ItemFlow.Activity_WeeklyVipAward);
                    if (index < 0) {
                        awardList.add(refVipAward.aid);
                        hasBuyList.add(1);
                    } else {
                        hasBuyList.set(index, buyTimes + 1);
                    }

                    bo.saveExtStr(0, StringUtils.list2String(awardList));
                    bo.saveExtStr(1, StringUtils.list2String(hasBuyList));
                    return new business.global.activity.detail.VipAward.VipAwardReceive(awardId, buyTimes + 1, pack);
                }
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
    public void onClosed() {
        this.clearActRecord();
    }

    @Override
    public ActivityType getType() {
        return null;
    }

    public static class RankAward {
        public int aid;
        public NumberRange rankrange;
        public Reward reward;
    }

    private static class VipAward {
        int aid;
        int vip;
        String icon;
        Reward reward;
        int price;
        int discount;
        List<Integer> buytimes;

        private VipAward() {
        }
    }
}
