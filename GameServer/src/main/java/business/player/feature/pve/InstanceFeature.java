package business.player.feature.pve;

import business.player.Player;
import business.player.feature.Feature;
import business.player.feature.PlayerCurrency;
import business.player.feature.PlayerItem;
import business.player.feature.features.PlayerRecord;
import business.player.item.Reward;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.enums.ConstEnum;
import com.zhonglian.server.common.enums.InstanceType;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefCrystalPrice;
import core.config.refdata.ref.RefInstance;
import core.config.refdata.ref.RefReward;
import core.config.refdata.ref.RefVIP;
import core.database.game.bo.InstanceInfoBO;

import java.util.List;

public class InstanceFeature extends Feature {
    private InstanceInfoBO instanceInfo;

    public InstanceFeature(Player player) {
        super(player);
    }

    public void loadDB() {
        this.instanceInfo = (InstanceInfoBO) BM.getBM(InstanceInfoBO.class).findOne("pid", Long.valueOf(this.player.getPid()));
    }

    public InstanceInfoBO getOrCreate() {
        InstanceInfoBO bo = this.instanceInfo;
        if (bo != null) {
            return bo;
        }
        synchronized (this) {
            bo = this.instanceInfo;
            if (bo != null) {
                return bo;
            }
            bo = new InstanceInfoBO();
            bo.setPid(this.player.getPid());
            bo.setInstanceMaxLevelAll(0);
            byte b;
            int i;
            InstanceType[] arrayOfInstanceType;
            for (i = (arrayOfInstanceType = InstanceType.values()).length, b = 0; b < i; ) {
                InstanceType type = arrayOfInstanceType[b];
                if (type == InstanceType.EquipInstance) {
                    bo.setChallengTimes(type.ordinal(), ((RefVIP) RefDataMgr.get(RefVIP.class, Integer.valueOf(this.player.getVipLevel()))).EquipInstanceTimes);
                }
                if (type == InstanceType.MeridianInstance) {
                    bo.setChallengTimes(type.ordinal(), ((RefVIP) RefDataMgr.get(RefVIP.class, Integer.valueOf(this.player.getVipLevel()))).MeridianInstanceTimes);
                }
                if (type == InstanceType.GemInstance) {
                    bo.setChallengTimes(type.ordinal(), ((RefVIP) RefDataMgr.get(RefVIP.class, Integer.valueOf(this.player.getVipLevel()))).GemInstanceTimes);
                }
                if (type == InstanceType.GuaidInstance)
                    bo.setChallengTimes(type.ordinal(), RefDataMgr.getFactor("GuaidInstanceChallengTimes", 3));
                b++;
            }

            bo.insert();
            this.instanceInfo = bo;
        }
        return bo;
    }

    public void dailyRefresh() {
        try {
            InstanceInfoBO bo = getOrCreate();
            byte b;
            int i;
            InstanceType[] arrayOfInstanceType;
            for (i = (arrayOfInstanceType = InstanceType.values()).length, b = 0; b < i; ) {
                InstanceType type = arrayOfInstanceType[b];
                if (type == InstanceType.EquipInstance) {
                    bo.setChallengTimes(type.ordinal(), ((RefVIP) RefDataMgr.get(RefVIP.class, Integer.valueOf(this.player.getVipLevel()))).EquipInstanceTimes);
                }
                if (type == InstanceType.MeridianInstance) {
                    bo.setChallengTimes(type.ordinal(), ((RefVIP) RefDataMgr.get(RefVIP.class, Integer.valueOf(this.player.getVipLevel()))).MeridianInstanceTimes);
                }
                if (type == InstanceType.GemInstance)
                    bo.setChallengTimes(type.ordinal(), ((RefVIP) RefDataMgr.get(RefVIP.class, Integer.valueOf(this.player.getVipLevel()))).GemInstanceTimes);
                b++;
            }

            bo.saveAll();
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public int fightInstance(int reqLevel, InstanceType type) throws WSException {
        int level = getOrCreate().getInstanceMaxLevel(type.ordinal());
        if (reqLevel > level + 1) {
            throw new WSException(ErrorCode.Instance_Locked, "副本未解锁");
        }
        if (getOrCreate().getChallengTimes(type.ordinal()) <= 0) {
            throw new WSException(ErrorCode.Instance_NotEounghTimes, "挑战次数不够");
        }
        if (reqLevel > ((List) RefInstance.instanceMap.get(type)).size()) {
            throw new WSException(ErrorCode.Instance_Full, "副本已全部挑战");
        }
        RefInstance ref = ((List<RefInstance>) RefInstance.instanceMap.get(type)).get(reqLevel - 1);
        if (ref != null && ref.Material != 0) {
            PrizeType prizeType = null;
            switch (type) {
                case null:
                    prizeType = PrizeType.EquipInstanceMaterial;
                    break;
                case MeridianInstance:
                    prizeType = PrizeType.MeridianInstanceMaterial;
                    break;
                case GemInstance:
                    prizeType = PrizeType.GemInstanceMaterial;
                    break;
            }

            PlayerCurrency playerCurrency = (PlayerCurrency) this.player.getFeature(PlayerCurrency.class);
            if (!playerCurrency.check(prizeType, ref.Material)) {
                throw new WSException(ErrorCode.Instance_NotEounghMaterial, "材料不够");
            }
            playerCurrency.consume(prizeType, ref.Material, ItemFlow.Instance);
        }
        getOrCreate().saveChallengTimes(type.ordinal(), getOrCreate().getChallengTimes(type.ordinal()) - 1);

        return getOrCreate().getChallengTimes(type.ordinal());
    }

    public Reward getReward(int reqLevel, boolean sweep, InstanceType type) throws WSException {
        int level = getOrCreate().getInstanceMaxLevel(type.ordinal());
        if (reqLevel > ((List) RefInstance.instanceMap.get(type)).size()) {
            throw new WSException(ErrorCode.Instance_Full, "副本已全部挑战");
        }

        if (reqLevel > level + 1) {
            throw new WSException(ErrorCode.Instance_Locked, "副本未解锁");
        }

        if (reqLevel == level + 1) {
            if (sweep) {
                throw new WSException(ErrorCode.Instance_NotPassed, "副本未通关");
            }
            getOrCreate().saveInstanceMaxLevel(type.ordinal(), level + 1);
        }
        RefInstance ref = ((List<RefInstance>) RefInstance.instanceMap.get(type)).get(reqLevel - 1);
        RefReward refReward = (RefReward) RefDataMgr.get(RefReward.class, Integer.valueOf(ref.RewardId));
        Reward reward = null;
        if (refReward != null) {
            reward = refReward.genReward();
            ((PlayerItem) this.player.getFeature(PlayerItem.class)).gain(reward, ItemFlow.InstanceWin);
        }

        return reward;
    }

    public String buyChallengTimes(InstanceType type, int buytimes) throws WSException {
        int i;
        ConstEnum.DailyRefresh dailyType = null;
        PlayerRecord recorder = (PlayerRecord) this.player.getFeature(PlayerRecord.class);
        PlayerCurrency currency = (PlayerCurrency) this.player.getFeature(PlayerCurrency.class);
        RefCrystalPrice prize = null;
        int count = 0;
        int times = 0;

        switch (type) {
            case null:
                dailyType = ConstEnum.DailyRefresh.EquipInstanceBuyTimes;
                times = recorder.getValue(dailyType);
                for (i = 0; i < buytimes; i++) {
                    prize = RefCrystalPrice.getPrize(times + i);
                    count += prize.EquipInstanceAddChallenge;
                }
                break;
            case GemInstance:
                dailyType = ConstEnum.DailyRefresh.GemInstanceBuyTimes;
                times = recorder.getValue(dailyType);
                for (i = 0; i < buytimes; i++) {
                    prize = RefCrystalPrice.getPrize(times + i);
                    count += prize.EquipInstanceAddChallenge;
                }
                break;
            case MeridianInstance:
                dailyType = ConstEnum.DailyRefresh.MeridianInstanceBuyTimes;
                times = recorder.getValue(dailyType);
                for (i = 0; i < buytimes; i++) {
                    prize = RefCrystalPrice.getPrize(times + i);
                    count += prize.EquipInstanceAddChallenge;
                }
                break;
        }

        if (!currency.check(PrizeType.Crystal, count)) {
            throw new WSException(ErrorCode.NotEnough_Crystal, "玩家增加挑战需要钻石%s", new Object[]{Integer.valueOf(count)});
        }
        currency.consume(PrizeType.Crystal, count, ItemFlow.AddInstanceChallenge);

        recorder.addValue(dailyType, buytimes);
        getOrCreate().saveChallengTimes(type.ordinal(), getOrCreate().getChallengTimes(type.ordinal()) + buytimes);
        return String.valueOf(getOrCreate().getChallengTimes(type.ordinal())) + ";" + recorder.getValue(dailyType);
    }
}

