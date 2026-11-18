package business.player.feature.pve;

import business.player.Player;
import business.player.feature.Feature;
import business.player.feature.PlayerCurrency;
import business.player.feature.PlayerItem;
import business.player.feature.features.PlayerRecord;
import business.player.item.Reward;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.enums.InstanceType;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.common.enums.ConstEnum.DailyRefresh;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefCrystalPrice;
import core.config.refdata.ref.RefInstance;
import core.config.refdata.ref.RefReward;
import core.config.refdata.ref.RefVIP;
import core.database.game.bo.InstanceInfoBO;

public class InstanceFeature extends Feature {
    private InstanceInfoBO instanceInfo;

    public InstanceFeature(Player player) {
        super(player);
    }

    @Override
    public void loadDB() {
        this.instanceInfo = BM.getBM(InstanceInfoBO.class).findOne("pid", this.player.getPid());
    }

    public InstanceInfoBO getOrCreate() {
        InstanceInfoBO bo = this.instanceInfo;
        if (bo != null) {
            return bo;
        } else {
            synchronized (this) {
                bo = this.instanceInfo;
                if (bo != null) {
                    return bo;
                } else {
                    bo = new InstanceInfoBO();
                    bo.setPid(this.player.getPid());
                    bo.setInstanceMaxLevelAll(0);

                    InstanceType[] var6;
                    for (InstanceType type : var6 = InstanceType.values()) {
                        if (type == InstanceType.EquipInstance) {
                            bo.setChallengTimes(type.ordinal(), RefDataMgr.get(RefVIP.class, this.player.getVipLevel()).EquipInstanceTimes);
                        }

                        if (type == InstanceType.MeridianInstance) {
                            bo.setChallengTimes(type.ordinal(), RefDataMgr.get(RefVIP.class, this.player.getVipLevel()).MeridianInstanceTimes);
                        }

                        if (type == InstanceType.GemInstance) {
                            bo.setChallengTimes(type.ordinal(), RefDataMgr.get(RefVIP.class, this.player.getVipLevel()).GemInstanceTimes);
                        }

                        if (type == InstanceType.GuaidInstance) {
                            bo.setChallengTimes(type.ordinal(), RefDataMgr.getFactor("GuaidInstanceChallengTimes", 3));
                        }
                    }

                    bo.insert();
                    this.instanceInfo = bo;
                    return bo;
                }
            }
        }
    }

    public void dailyRefresh() {
        try {
            InstanceInfoBO bo = this.getOrCreate();

            InstanceType[] var5;
            for (InstanceType type : var5 = InstanceType.values()) {
                if (type == InstanceType.EquipInstance) {
                    bo.setChallengTimes(type.ordinal(), RefDataMgr.get(RefVIP.class, this.player.getVipLevel()).EquipInstanceTimes);
                }

                if (type == InstanceType.MeridianInstance) {
                    bo.setChallengTimes(type.ordinal(), RefDataMgr.get(RefVIP.class, this.player.getVipLevel()).MeridianInstanceTimes);
                }

                if (type == InstanceType.GemInstance) {
                    bo.setChallengTimes(type.ordinal(), RefDataMgr.get(RefVIP.class, this.player.getVipLevel()).GemInstanceTimes);
                }
            }

            bo.saveAll();
        } catch (Exception var6) {
            var6.printStackTrace();
        }
    }

    public int fightInstance(int reqLevel, InstanceType type) throws WSException {
        int level = this.getOrCreate().getInstanceMaxLevel(type.ordinal());
        if (reqLevel > level + 1) {
            throw new WSException(ErrorCode.Instance_Locked, "副本未解锁");
        } else if (this.getOrCreate().getChallengTimes(type.ordinal()) <= 0) {
            throw new WSException(ErrorCode.Instance_NotEounghTimes, "挑战次数不够");
        } else if (reqLevel > RefInstance.instanceMap.get(type).size()) {
            throw new WSException(ErrorCode.Instance_Full, "副本已全部挑战");
        } else {
            RefInstance ref = RefInstance.instanceMap.get(type).get(reqLevel - 1);
            if (ref != null && ref.Material != 0) {
                PrizeType prizeType = null;
                switch (type) {
                    case EquipInstance:
                        prizeType = PrizeType.EquipInstanceMaterial;
                        break;
                    case GemInstance:
                        prizeType = PrizeType.GemInstanceMaterial;
                        break;
                    case MeridianInstance:
                        prizeType = PrizeType.MeridianInstanceMaterial;
                }

                PlayerCurrency playerCurrency = this.player.getFeature(PlayerCurrency.class);
                if (!playerCurrency.check(prizeType, ref.Material)) {
                    throw new WSException(ErrorCode.Instance_NotEounghMaterial, "材料不够");
                }

                playerCurrency.consume(prizeType, ref.Material, ItemFlow.Instance);
            }

            this.getOrCreate().saveChallengTimes(type.ordinal(), this.getOrCreate().getChallengTimes(type.ordinal()) - 1);
            return this.getOrCreate().getChallengTimes(type.ordinal());
        }
    }

    public Reward getReward(int reqLevel, boolean sweep, InstanceType type) throws WSException {
        int level = this.getOrCreate().getInstanceMaxLevel(type.ordinal());
        if (reqLevel > RefInstance.instanceMap.get(type).size()) {
            throw new WSException(ErrorCode.Instance_Full, "副本已全部挑战");
        } else if (reqLevel > level + 1) {
            throw new WSException(ErrorCode.Instance_Locked, "副本未解锁");
        } else {
            if (reqLevel == level + 1) {
                if (sweep) {
                    throw new WSException(ErrorCode.Instance_NotPassed, "副本未通关");
                }

                this.getOrCreate().saveInstanceMaxLevel(type.ordinal(), level + 1);
            }

            RefInstance ref = RefInstance.instanceMap.get(type).get(reqLevel - 1);
            RefReward refReward = RefDataMgr.get(RefReward.class, ref.RewardId);
            Reward reward = null;
            if (refReward != null) {
                reward = refReward.genReward();
                this.player.getFeature(PlayerItem.class).gain(reward, ItemFlow.InstanceWin);
            }

            return reward;
        }
    }

    public String buyChallengTimes(InstanceType type, int buytimes) throws WSException {
        DailyRefresh dailyType = null;
        PlayerRecord recorder = this.player.getFeature(PlayerRecord.class);
        PlayerCurrency currency = this.player.getFeature(PlayerCurrency.class);
        RefCrystalPrice prize = null;
        int count = 0;
        int times = 0;
        switch (type) {
            case EquipInstance:
                dailyType = DailyRefresh.EquipInstanceBuyTimes;
                times = recorder.getValue(dailyType);

                for (int i = 0; i < buytimes; i++) {
                    prize = RefCrystalPrice.getPrize(times + i);
                    count += prize.EquipInstanceAddChallenge;
                }
                break;
            case GemInstance:
                dailyType = DailyRefresh.GemInstanceBuyTimes;
                times = recorder.getValue(dailyType);

                for (int i = 0; i < buytimes; i++) {
                    prize = RefCrystalPrice.getPrize(times + i);
                    count += prize.EquipInstanceAddChallenge;
                }
                break;
            case MeridianInstance:
                dailyType = DailyRefresh.MeridianInstanceBuyTimes;
                times = recorder.getValue(dailyType);

                for (int i = 0; i < buytimes; i++) {
                    prize = RefCrystalPrice.getPrize(times + i);
                    count += prize.EquipInstanceAddChallenge;
                }
        }

        if (!currency.check(PrizeType.Crystal, count)) {
            throw new WSException(ErrorCode.NotEnough_Crystal, "玩家增加挑战需要钻石%s", count);
        } else {
            currency.consume(PrizeType.Crystal, count, ItemFlow.AddInstanceChallenge);
            recorder.addValue(dailyType, buytimes);
            this.getOrCreate().saveChallengTimes(type.ordinal(), this.getOrCreate().getChallengTimes(type.ordinal()) + buytimes);
            return this.getOrCreate().getChallengTimes(type.ordinal()) + ";" + recorder.getValue(dailyType);
        }
    }
}
