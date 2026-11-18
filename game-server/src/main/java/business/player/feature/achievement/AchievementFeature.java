package business.player.feature.achievement;

import business.player.Player;
import business.player.feature.Feature;
import business.player.feature.PlayerItem;
import business.player.feature.character.CharFeature;
import business.player.feature.character.Character;
import business.player.feature.features.RechargeFeature;
import business.player.feature.task.TaskActivityFeature;
import business.player.item.Reward;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.enums.Achievement.AchievementType;
import com.zhonglian.server.common.enums.ConstEnum.AchieveReset;
import com.zhonglian.server.common.enums.ConstEnum.TaskClassify;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.common.utils.Maps;
import com.zhonglian.server.common.utils.StringUtils;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefAchievement;
import core.config.refdata.ref.RefReward;
import core.database.game.bo.AchievementBO;
import core.database.game.bo.UnlockRewardBO;
import core.network.proto.AchieveInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AchievementFeature extends Feature {
    public UnlockRewardBO unlockRewardBO;
    public Map<AchievementType, AchievementFeature.AchievementIns> achievemap = Maps.newMap();

    public AchievementFeature(Player owner) {
        super(owner);
    }

    @Override
    public void loadDB() {
        for (AchievementBO bo : BM.getBM(AchievementBO.class).findAll("pid", this.player.getPid())) {
            RefAchievement refData = RefDataMgr.get(RefAchievement.class, bo.getAchieveId());
            if (refData == null) {
                bo.del();
            } else {
                AchievementFeature.AchievementIns info = new AchievementFeature.AchievementIns(bo, refData);
                this.achievemap.put(refData.AchieveName, info);
            }
        }

        this.unlockRewardBO = BM.getBM(UnlockRewardBO.class).findOne("pid", this.player.getPid());
        if (this.unlockRewardBO == null) {
            this.unlockRewardBO = new UnlockRewardBO();
            this.unlockRewardBO.setPid(this.player.getPid());
            this.unlockRewardBO.insert();
        }
    }

    public AchieveInfo build(AchievementBO achieveBo) {
        AchieveInfo res = new AchieveInfo();
        res.setAchieveId(achieveBo.getAchieveId());
        res.setCompleteCount(achieveBo.getCompleteCount());
        res.setAchieveCount(achieveBo.getAchieveCount());
        res.setArgument1(achieveBo.getArgument());
        res.setArgument2(achieveBo.getArgument2());
        res.setArgument3(achieveBo.getArgument3());
        res.setGainPrizeList(StringUtils.string2Integer(achieveBo.getGainPrizeList()));
        return res;
    }

    public List<AchieveInfo> loadAchieveList() {
        this.checkMonthCard(AchievementType.MonthCardCrystal);
        this.checkMonthCard(AchievementType.YearCardCrystal);
        List<AchieveInfo> achieveInfoList = new ArrayList<>();

        for (AchievementFeature.AchievementIns ins : this.achievemap.values()) {
            achieveInfoList.add(this.build(ins.bo));
        }

        return achieveInfoList;
    }

    public UnlockRewardBO getUnlockRewardBO() {
        return this.unlockRewardBO;
    }

    public AchievementFeature.AchievementIns getOrCreate(AchievementType type) {
        AchievementFeature.AchievementIns ret = this.achievemap.get(type);
        if (ret == null) {
            synchronized (this.achievemap) {
                RefAchievement ref = RefAchievement.getByType(type);
                if (ref == null) {
                    return null;
                }

                AchievementBO bo = new AchievementBO();
                bo.setAchieveId(ref.id);
                bo.setPid(this.player.getPid());
                bo.insert();
                ret = new AchievementFeature.AchievementIns(bo, ref);
                this.achievemap.put(type, ret);
            }
        }

        return ret;
    }

    public void update0(AchievementType type, IUpdateAchievement iUpdate, Integer... args) {
        AchievementFeature.AchievementIns ins = this.getOrCreate(type);
        if (ins != null) {
            if (!ins.refData.IsHide) {
                if (ins.refData.FirstArgsList.size() > ins.bo.getAchieveCount()) {
                    int oldvalue = ins.bo.getCompleteCount();
                    iUpdate.update(ins.bo, ins.refData, args);
                    if (oldvalue != ins.bo.getCompleteCount()) {
                        this.player.pushProto("UpdateAchieve", this.build(ins.bo));
                    }
                }
            }
        }
    }

    public void updateInc(AchievementType type, Integer value) {
        this.update0(type, (bo, ref, values) -> {
            AchievementFeature.AchievementIns ins = this.achievemap.get(type);
            if (ins.refData.ConditionPreTaskId != 0) {
                RefAchievement pre_ref = RefDataMgr.get(RefAchievement.class, ins.refData.ConditionPreTaskId);
                AchievementFeature.AchievementIns pre_ins = this.achievemap.get(pre_ref.AchieveName);
                if (pre_ins == null) {
                    return;
                }

                List<Integer> pre_gainList = StringUtils.string2Integer(pre_ins.bo.getGainPrizeList());
                if (pre_gainList.size() < pre_ref.PrizeIDList.size()) {
                    return;
                }
            }

            int addCount = values[0];
            synchronized (bo) {
                int count = bo.getCompleteCount();
                bo.saveCompleteCount(count + addCount);
                this.taskChieveCount(bo, ref);
            }
        }, value);
    }

    public void updateInc(AchievementType type) {
        this.updateInc(type, 1);
    }

    public void updateMax(AchievementType type, Integer value) {
        this.update0(type, (bo, ref, values) -> {
            int newvalue = values[0];
            synchronized (bo) {
                int count = bo.getCompleteCount();
                bo.saveCompleteCount(Math.max(count, newvalue));
                this.taskChieveCount(bo, ref);
            }
        }, value);
    }

    public void updateMin(AchievementType type, Integer value) {
        this.update0(type, (bo, ref, values) -> {
            int newvalue = values[0];
            synchronized (bo) {
                int count = bo.getCompleteCount();
                if (count == 0) {
                    count = newvalue;
                }

                bo.saveCompleteCount(Math.min(count, newvalue));
                int i = bo.getAchieveCount();

                while (i < ref.FirstArgsList.size() && bo.getCompleteCount() <= ref.FirstArgsList.get(i)) {
                    bo.saveAchieveCount(++i);
                }
            }
        }, value);
    }

    public void clearValue(AchievementType type) {
        this.update0(type, (bo, ref, values) -> {
            synchronized (bo) {
                bo.saveCompleteCount(0);
            }
        });
    }

    public void dailyRefresh() {
        try {
            this.resetAchievement(AchieveReset.EveryDay);
            this.player.isOnline();
        } catch (Exception var2) {
            var2.printStackTrace();
        }
    }

    public void weekRefresh() {
        this.resetAchievement(AchieveReset.EveryWeek);
    }

    public void resetAchievement(AchieveReset resetType) {
        synchronized (this.achievemap) {
            for (AchievementFeature.AchievementIns achieve : this.achievemap.values()) {
                if (achieve.refData.Reset == resetType) {
                    if (achieve.refData.AchieveName == AchievementType.SkillUp) {
                        int level = 0;

                        for (Character charc : this.player.getFeature(CharFeature.class).getAll().values()) {
                            for (int skill : charc.getBo().getSkillAll()) {
                                level += skill;
                            }
                        }

                        if (level >= 1485) {
                            achieve.bo.setGainPrizeList("");
                            achieve.bo.saveAll();
                            this.player.pushProto("UpdateAchieve", this.build(achieve.bo));
                            continue;
                        }
                    }

                    achieve.bo.setAchieveCount(0);
                    achieve.bo.setCompleteCount(0);
                    achieve.bo.setGainPrizeList("");
                    achieve.bo.saveAll();
                    this.player.pushProto("UpdateAchieve", this.build(achieve.bo));
                }
            }
        }
    }

    public Reward cmd_pickUpActivityPrize(int achieveID, int achieveCount) throws WSException {
        RefAchievement ref = RefDataMgr.get(RefAchievement.class, achieveID);
        AchievementFeature.AchievementIns ins = this.achievemap.get(ref.AchieveName);
        if (ins == null) {
            throw new WSException(ErrorCode.NotFound_Achievement, "没有找到该成就列表 cid:%s,achieveID:%s", this.player.getPid(), achieveID);
        } else {
            List<Integer> gainList = StringUtils.string2Integer(ins.bo.getGainPrizeList());
            if (gainList.contains(achieveCount)) {
                throw new WSException(ErrorCode.ALREADY_FETCH, "已经领取了该阶段的奖励");
            } else {
                int maxIndex = ins.refData.FirstArgsList.size();
                if (achieveCount >= maxIndex) {
                    throw new WSException(ErrorCode.NotEnough_CompleteLevel, "achieveIndex%s>=ins.refData.FirstArgsList.size():%s", achieveCount, maxIndex);
                } else {
                    int curCnt = ins.bo.getCompleteCount();
                    int needCnt = ins.refData.FirstArgsList.get(achieveCount);
                    if (this.isRankAchievement(ref.AchieveName)) {
                        if (curCnt > needCnt) {
                            throw new WSException(
                                ErrorCode.NotEnough_CompleteCount,
                                "奖励还没达到领取条件cid:%s,achieveID:%s,index:%s,curCnt:%s,needCnt:%s",
                                this.player.getPid(),
                                achieveID,
                                achieveCount,
                                curCnt,
                                needCnt
                            );
                        }
                    } else if (curCnt < needCnt) {
                        throw new WSException(
                            ErrorCode.NotEnough_CompleteCount,
                            "奖励还没达到领取条件cid:%s,achieveID:%s,index:%s,curCnt:%s,needCnt:%s",
                            this.player.getPid(),
                            achieveID,
                            achieveCount,
                            curCnt,
                            needCnt
                        );
                    }

                    int prizeID = ins.refData.PrizeIDList.get(achieveCount);
                    Reward reward = RefDataMgr.get(RefReward.class, prizeID).genReward();
                    Reward pack;
                    if (reward != null) {
                        pack = this.player.getFeature(PlayerItem.class).gain(reward, ItemFlow.Achievement);
                    } else {
                        pack = new Reward();
                    }

                    gainList.add(achieveCount);
                    if (ins.refData.AchieveName == AchievementType.MonthCardCrystal) {
                        ins.bo.saveCompleteCount(ins.bo.getCompleteCount() - 1);
                    }

                    ins.bo.saveGainPrizeList(StringUtils.list2String(gainList));
                    this.player.pushProto("UpdateAchieve", this.build(ins.bo));
                    return pack;
                }
            }
        }
    }

    public boolean isRankAchievement(AchievementType type) {
        return false;
    }

    public Reward cmd_pickUpPrize(int achieveID, int achieveCount) throws WSException {
        RefAchievement ref = RefDataMgr.get(RefAchievement.class, achieveID);
        AchievementFeature.AchievementIns ins = this.achievemap.get(ref.AchieveName);
        if (ins == null) {
            throw new WSException(ErrorCode.NotFound_Achievement, "没有找到该成就列表 cid:%s,achieveID : %s", this.player.getPid(), achieveID);
        } else {
            List<Integer> gainList = StringUtils.string2Integer(ins.bo.getGainPrizeList());
            if (gainList.contains(achieveCount)) {
                throw new WSException(ErrorCode.ALREADY_FETCH, "已经领取了该阶段的奖励");
            } else if (achieveCount >= ins.refData.FirstArgsList.size() || achieveCount < 0) {
                throw new WSException(ErrorCode.InvalidParam, "客户端发来的参数错误,achieveCount不能<0||achieveCount>任务数量");
            } else if (achieveCount >= ins.bo.getAchieveCount()) {
                throw new WSException(ErrorCode.NotEnough_CompleteCount, "奖励还没达到领取条件 ");
            } else {
                if (ref.ConditionPreTaskId != 0) {
                    RefAchievement pre_ref = RefDataMgr.get(RefAchievement.class, ref.ConditionPreTaskId);
                    AchievementFeature.AchievementIns pre_ins = this.achievemap.get(pre_ref.AchieveName);
                    if (pre_ins == null) {
                        throw new WSException(ErrorCode.NotFound_Achievement, "没有找到前置成就列表 cid:%s,achieveID : %s", this.player.getPid(), ref.ConditionPreTaskId);
                    }

                    List<Integer> pre_gainList = StringUtils.string2Integer(pre_ins.bo.getGainPrizeList());
                    if (pre_gainList.size() < pre_ref.PrizeIDList.size()) {
                        throw new WSException(ErrorCode.NotEnough_CompleteCount, "前置任务没有完成 ");
                    }
                }

                if (ref.TaskType == TaskClassify.DailyTask) {
                    TaskActivityFeature taskActivityFeature = this.player.getFeature(TaskActivityFeature.class);
                    taskActivityFeature.addDailyActive(ins.refData.ActiveScore);
                }

                int prizeID = ins.refData.PrizeIDList.get(achieveCount);
                Reward reward = RefDataMgr.get(RefReward.class, prizeID).genReward();
                Reward pack = this.player.getFeature(PlayerItem.class).gain(reward, ItemFlow.Achievement);
                gainList.add(achieveCount);
                if (ins.refData.AchieveName == AchievementType.MonthCardCrystal) {
                    ins.bo.saveCompleteCount(ins.bo.getCompleteCount() - 1);
                }

                ins.bo.saveGainPrizeList(StringUtils.list2String(gainList));
                this.player.pushProto("UpdateAchieve", this.build(ins.bo));
                return pack;
            }
        }
    }

    public void refreshSevenDay() {
    }

    public void checkMonthCard(AchievementType type) {
        AchievementFeature.AchievementIns ins = this.getOrCreate(type);
        if (ins != null) {
            int achieveIndex = ins.bo.getAchieveCount();
            int maxIndex = ins.refData.FirstArgsList.size();
            if (achieveIndex < maxIndex) {
                RechargeFeature rechargeFeature = this.player.getFeature(RechargeFeature.class);
                int dayNum = rechargeFeature.getRebateRemains(type);
                if (dayNum != 0 && dayNum >= -1) {
                    int count = ins.bo.getCompleteCount();
                    ins.bo.saveCompleteCount(dayNum);
                    ins.bo.saveAchieveCount(count + 1);
                    this.player.pushProto("UpdateAchieve", this.build(ins.bo));
                }
            }
        }
    }

    public void checkAchieve(AchievementType type) {
        AchievementFeature.AchievementIns ins = this.getOrCreate(type);
        if (ins != null) {
            if (this.player.getPlayerBO().getLv() >= ins.refData.Condition) {
                long now = CommTime.nowMS();
                if (now >= CommTime.getTodayHourMS(ins.refData.SecondArgsList.get(0)) && now < CommTime.getTodayHourMS(ins.refData.SecondArgsList.get(1))) {
                    int count = ins.bo.getCompleteCount();
                    ins.bo.saveAchieveCount(count + 1);
                    this.player.pushProto("UpdateAchieve", this.build(ins.bo));
                } else if (now >= CommTime.getTodayHourMS(ins.refData.SecondArgsList.get(1))) {
                    if (StringUtils.string2Integer(ins.bo.getGainPrizeList()).size() != 0) {
                        return;
                    }

                    ins.bo.saveAchieveCount(0);
                    this.player.pushProto("UpdateAchieve", this.build(ins.bo));
                }
            }
        }
    }

    public void openGetEnergy(AchievementType type) {
        AchievementFeature.AchievementIns ins = this.getOrCreate(type);
        if (this.player.getPlayerBO().getLv() >= ins.refData.Condition) {
            long now = CommTime.nowMS();
            if (now >= CommTime.getTodayHourMS(ins.refData.SecondArgsList.get(0)) && now <= CommTime.getTodayHourMS(ins.refData.SecondArgsList.get(1))) {
                int count = ins.bo.getCompleteCount();
                ins.bo.saveAchieveCount(count + 1);
                this.player.pushProto("UpdateAchieve", this.build(ins.bo));
            }
        }
    }

    public void closeGetEnergy(AchievementType type) {
        AchievementFeature.AchievementIns ins = this.getOrCreate(type);
        if (this.player.getPlayerBO().getLv() >= ins.refData.Condition) {
            if (StringUtils.string2Integer(ins.bo.getGainPrizeList()).size() == 0) {
                ins.bo.saveAchieveCount(0);
                this.player.pushProto("UpdateAchieve", this.build(ins.bo));
            }
        }
    }

    public void effectActivation(int cardId) throws WSException {
    }

    private void taskChieveCount(AchievementBO bo, RefAchievement ref) {
        int i = bo.getAchieveCount();

        while (i < ref.FirstArgsList.size() && bo.getCompleteCount() >= ref.FirstArgsList.get(i)) {
            bo.saveAchieveCount(++i);
        }
    }

    public static class AchievementIns {
        public AchievementBO bo;
        public RefAchievement refData;

        public AchievementIns(AchievementBO bo, RefAchievement refData) {
            this.bo = bo;
            this.refData = refData;
        }
    }
}
