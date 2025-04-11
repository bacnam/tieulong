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
import com.zhonglian.server.common.enums.ConstEnum;
import com.zhonglian.server.common.enums.PrizeType;
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
    public List<RankAward> rankrewardList;
    List<VipAward> vipawardList;
    private int require_cost;
    private Reward reward;
    public RankActivity(ActivityBO bo) {
        super(bo);

        this.require_cost = 0;
    }

    public ConstEnum.VIPGiftType getAwardType() {
        return null;
    }

    public Reward getReward() {
        return this.reward;
    }

    public int getRequire_cost() {
        return this.require_cost;
    }

    public void load(JsonObject json) throws WSException {
        this.reward = new Reward(json.get("awards").getAsJsonArray());
        this.require_cost = json.get("cost").getAsInt();

        this.vipawardList = new ArrayList<>();
        for (JsonElement element : json.get("vipawards").getAsJsonArray()) {
            JsonObject obeject = element.getAsJsonObject();
            VipAward ref = new VipAward(null);
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
            RankAward ref = new RankAward();
            ref.aid = obeject.get("aid").getAsInt();
            ref.rankrange = NumberRange.parse(obeject.get("rankrange").getAsString());
            ref.reward = new Reward(obeject.get("items").getAsJsonArray());
            this.rankrewardList.add(ref);
        }
    }

    public int getCost(Player player) {
        ActivityRecordBO bo = getOrCreateRecord(player);
        return bo.getExtInt(0);
    }

    public void UpdateMaxRequire_cost(Player player, int crystal) {
        synchronized (this) {
            if (getStatus() != ActivityStatus.Open) {
                return;
            }
            ActivityRecordBO bo = getOrCreateRecord(player);
            if (crystal > bo.getExtInt(0)) {
                bo.saveExtInt(0, crystal);
            }
        }
    }

    public OpenServerRankRewardInfo pickUpReward(Player player) throws WSException {
        ActivityRecordBO bo = getOrCreateRecord(player);
        int cost = getCost(player);
        if (cost < getRequire_cost()) {
            throw new WSException(ErrorCode.Not_Enough, "条件未达成");
        }

        if (bo.getExtInt(1) != 0) {
            throw new WSException(ErrorCode.Already_Picked, "已领取");
        }
        bo.saveExtInt(1, 1);
        ((PlayerItem) player.getFeature(PlayerItem.class)).gain(getReward(), ItemFlow.RankReward);
        return new OpenServerRankRewardInfo(bo.getExtInt(0), (bo.getExtInt(1) == 1), getReward());
    }

    public boolean isPicked(Player player) {
        ActivityRecordBO bo = getOrCreateRecord(player);
        return (bo.getExtInt(1) == 1);
    }

    public List<VipAwardInfo> vipAwardProto(Player player) {
        ActivityRecordBO bo = getOrCreateRecord(player);

        List<Integer> awardList = StringUtils.string2Integer(bo.getExtStr(0));
        List<Integer> hasBuyList = StringUtils.string2Integer(bo.getExtStr(1));

        List<VipAward> refList = this.vipawardList;

        List<VipAwardInfo> weeks = (List<VipAwardInfo>) refList.stream().map(x -> {
            VipAwardInfo builder = new VipAwardInfo();
            builder.setAwardId(x.aid);
            builder.setMaxTimes(((Integer) x.buytimes.get(paramPlayer.getVipLevel())).intValue());
            builder.setReward(x.reward);
            builder.setIcon(x.icon);
            builder.setVip(x.vip);
            builder.setPrice(x.price);
            builder.setDiscount(x.discount);
            builder.setTimeslist(StringUtils.list2String(x.buytimes));
            int index = paramList1.indexOf(Integer.valueOf(x.aid));
            if (index < 0) {
                builder.setBuyTimes(0);
            } else {
                builder.setBuyTimes(((Integer) paramList2.get(index)).intValue());
            }
            return builder;
        }).collect(Collectors.toList());
        return weeks;
    }

    public VipAward getVipRef(int rewardId) {
        for (VipAward ref : this.vipawardList) {
            if (ref.aid == rewardId) {
                return ref;
            }
        }

        return null;
    }

    public VipAward.VipAwardReceive doWeeklyReceive(Player player, int awardId) throws WSException {
        ActivityRecordBO bo = getOrCreateRecord(player);

        VipAward refVipAward = getVipRef(awardId);
        if (refVipAward == null) {
            throw new WSException(ErrorCode.NotFound_RefVipAward, "未找到[%s]的VIP礼包配置", new Object[]{Integer.valueOf(awardId)});
        }
        if (refVipAward.vip > player.getVipLevel()) {
            throw new WSException(ErrorCode.VipAward_WeekNoNowVIPGift, "礼包VIP等级:%s != 玩家VIP等级:%s", new Object[]{Integer.valueOf(refVipAward.vip), Integer.valueOf(player.getVipLevel())});
        }

        int maxTimes = ((Integer) refVipAward.buytimes.get(player.getVipLevel())).intValue();

        List<Integer> awardList = StringUtils.string2Integer(bo.getExtStr(0));
        List<Integer> hasBuyList = StringUtils.string2Integer(bo.getExtStr(1));

        int buyTimes = 0;
        int index = awardList.indexOf(Integer.valueOf(refVipAward.aid));
        if (index >= 0) {
            buyTimes = ((Integer) hasBuyList.get(index)).intValue();
        }
        if (buyTimes >= maxTimes) {
            throw new WSException(ErrorCode.VipAward_WeekVIPGiftSold, "该礼包已卖光");
        }

        int cryStal = refVipAward.discount;
        if (!((PlayerCurrency) player.getFeature(PlayerCurrency.class)).check(PrizeType.Crystal, cryStal)) {
            throw new WSException(ErrorCode.NotEnough_Crystal, "玩家水晶:%s<购买水晶:%s", new Object[]{Integer.valueOf(player.getPlayerBO().getCrystal()), Integer.valueOf(cryStal)});
        }
        Reward reward = refVipAward.reward;

        Reward pack = ((PlayerItem) player.getFeature(PlayerItem.class)).gain(reward, ItemFlow.Activity_WeeklyVipAward);

        ((PlayerCurrency) player.getFeature(PlayerCurrency.class)).consume(PrizeType.Crystal, cryStal, ItemFlow.Activity_WeeklyVipAward);

        if (index < 0) {
            awardList.add(Integer.valueOf(refVipAward.aid));
            hasBuyList.add(Integer.valueOf(1));
        } else {
            hasBuyList.set(index, Integer.valueOf(buyTimes + 1));
        }
        bo.saveExtStr(0, StringUtils.list2String(awardList));
        bo.saveExtStr(1, StringUtils.list2String(hasBuyList));

        return new VipAward.VipAwardReceive(awardId, buyTimes + 1, pack);
    }

    public void onOpen() {
    }

    public void onEnd() {
    }

    public void onClosed() {
        clearActRecord();
    }

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

