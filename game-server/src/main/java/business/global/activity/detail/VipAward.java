package business.global.activity.detail;

import business.global.activity.Activity;
import business.player.Player;
import business.player.feature.PlayerCurrency;
import business.player.feature.PlayerItem;
import business.player.item.Reward;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.zhonglian.server.common.enums.ActivityType;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.common.utils.StringUtils;
import com.zhonglian.server.http.server.RequestException;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import core.database.game.bo.ActivityBO;
import core.database.game.bo.ActivityRecordBO;
import core.network.proto.VipAwardInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VipAward extends Activity {
    private List<VipAward.VipAwardRef> arList;

    public VipAward(ActivityBO data) {
        super(data);
    }

    @Override
    public void load(JsonObject json) throws WSException {
        this.arList = new ArrayList<>();

        for (JsonElement element : json.get("awards").getAsJsonArray()) {
            JsonObject obeject = element.getAsJsonObject();
            VipAward.VipAwardRef ref = new VipAward.VipAwardRef();
            ref.aid = obeject.get("aid").getAsInt();
            ref.vip = obeject.get("vip").getAsInt();
            ref.icon = obeject.get("icon").getAsString();
            ref.reward = new Reward(obeject.get("items").getAsJsonArray());
            ref.price = obeject.get("price").getAsInt();
            ref.discount = obeject.get("discount").getAsInt();
            ref.buytimes = StringUtils.string2Integer(obeject.get("buytimes").getAsString());
            this.arList.add(ref);
        }
    }

    @Override
    public String check(JsonObject json) throws RequestException {
        return "ok";
    }

    @Override
    public ActivityType getType() {
        return ActivityType.VipAward;
    }

    public List<VipAwardInfo> vipAwardProto(Player player) {
        ActivityRecordBO bo = this.getOrCreateRecord(player);
        List<Integer> awardList = StringUtils.string2Integer(bo.getExtStr(0));
        List<Integer> hasBuyList = StringUtils.string2Integer(bo.getExtStr(1));
        List<VipAward.VipAwardRef> refList = this.arList;
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

    public VipAward.VipAwardRef getVipRef(int rewardId) {
        for (VipAward.VipAwardRef ref : this.arList) {
            if (ref.aid == rewardId) {
                return ref;
            }
        }

        return null;
    }

    public VipAward.VipAwardReceive doWeeklyReceive(Player player, int awardId) throws WSException {
        ActivityRecordBO bo = this.getOrCreateRecord(player);
        VipAward.VipAwardRef refVipAward = this.getVipRef(awardId);
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
                    return new VipAward.VipAwardReceive(awardId, buyTimes + 1, pack);
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

    public static class VipAwardReceive {
        int awardId;
        int buyTimes;
        Reward reward;

        public VipAwardReceive(int awardId, int buyTimes, Reward reward) {
            this.awardId = awardId;
            this.buyTimes = buyTimes;
            this.reward = reward;
        }
    }

    private static class VipAwardRef {
        int aid;
        int vip;
        String icon;
        Reward reward;
        int price;
        int discount;
        List<Integer> buytimes;

        private VipAwardRef() {
        }
    }
}
