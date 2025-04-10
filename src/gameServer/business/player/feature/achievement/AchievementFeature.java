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
import com.zhonglian.server.common.enums.Achievement;
import com.zhonglian.server.common.enums.ConstEnum;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AchievementFeature
extends Feature
{
public UnlockRewardBO unlockRewardBO;
public Map<Achievement.AchievementType, AchievementIns> achievemap;

public AchievementFeature(Player owner) {
super(owner);

this.achievemap = Maps.newMap();
}

public void loadDB() {
List<AchievementBO> achieveList = BM.getBM(AchievementBO.class).findAll("pid", Long.valueOf(this.player.getPid()));
for (AchievementBO bo : achieveList) {
RefAchievement refData = (RefAchievement)RefDataMgr.get(RefAchievement.class, Integer.valueOf(bo.getAchieveId()));
if (refData == null) {
bo.del();
continue;
} 
AchievementIns info = new AchievementIns(bo, refData);
this.achievemap.put(refData.AchieveName, info);
} 
this.unlockRewardBO = (UnlockRewardBO)BM.getBM(UnlockRewardBO.class).findOne("pid", Long.valueOf(this.player.getPid()));
if (this.unlockRewardBO == null) {
this.unlockRewardBO = new UnlockRewardBO();
this.unlockRewardBO.setPid(this.player.getPid());
this.unlockRewardBO.insert();
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
checkMonthCard(Achievement.AchievementType.MonthCardCrystal);
checkMonthCard(Achievement.AchievementType.YearCardCrystal);
List<AchieveInfo> achieveInfoList = new ArrayList<>();
for (AchievementIns ins : this.achievemap.values()) {
achieveInfoList.add(build(ins.bo));
}
return achieveInfoList;
}

public UnlockRewardBO getUnlockRewardBO() {
return this.unlockRewardBO;
}

public AchievementIns getOrCreate(Achievement.AchievementType type) {
AchievementIns ret = this.achievemap.get(type);
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
ret = new AchievementIns(bo, ref);
this.achievemap.put(type, ret);
} 
}
return ret;
}

public void update0(Achievement.AchievementType type, IUpdateAchievement iUpdate, Integer... args) {
AchievementIns ins = getOrCreate(type);
if (ins == null) {
return;
}

if (ins.refData.IsHide) {
return;
}

if (ins.refData.FirstArgsList.size() <= ins.bo.getAchieveCount()) {
return;
}
int oldvalue = ins.bo.getCompleteCount();
iUpdate.update(ins.bo, ins.refData, args);
if (oldvalue != ins.bo.getCompleteCount()) {
this.player.pushProto("UpdateAchieve", build(ins.bo));
}
}

public void updateInc(Achievement.AchievementType type, Integer value) {
update0(type, (bo, ref, values) -> { AchievementIns ins = this.achievemap.get(paramAchievementType); if (ins.refData.ConditionPreTaskId != 0) { RefAchievement pre_ref = (RefAchievement)RefDataMgr.get(RefAchievement.class, Integer.valueOf(ins.refData.ConditionPreTaskId)); AchievementIns pre_ins = this.achievemap.get(pre_ref.AchieveName); if (pre_ins == null) return;  List<Integer> pre_gainList = StringUtils.string2Integer(pre_ins.bo.getGainPrizeList()); if (pre_gainList.size() < pre_ref.PrizeIDList.size()) return;  }  int addCount = values[0].intValue(); synchronized (bo) { int count = bo.getCompleteCount(); bo.saveCompleteCount(count + addCount); taskChieveCount(bo, ref); }  }new Integer[] {

value
});
}

public void updateInc(Achievement.AchievementType type) {
updateInc(type, Integer.valueOf(1));
}

public void updateMax(Achievement.AchievementType type, Integer value) {
update0(type, (bo, ref, values) -> { int newvalue = values[0].intValue(); synchronized (bo) { int count = bo.getCompleteCount(); bo.saveCompleteCount(Math.max(count, newvalue)); taskChieveCount(bo, ref); }  }new Integer[] {

value
});
}

public void updateMin(Achievement.AchievementType type, Integer value) {
update0(type, (bo, ref, values) -> { int newvalue = values[0].intValue(); synchronized (bo) { int count = bo.getCompleteCount(); if (count == 0) count = newvalue;  bo.saveCompleteCount(Math.min(count, newvalue)); int i = bo.getAchieveCount(); while (i < ref.FirstArgsList.size() && bo.getCompleteCount() <= ((Integer)ref.FirstArgsList.get(i)).intValue()) bo.saveAchieveCount(++i);  }  }new Integer[] {

value
});
}

public void clearValue(Achievement.AchievementType type) {
update0(type, (bo, ref, values) -> { synchronized (bo) { bo.saveCompleteCount(0); }  }new Integer[0]);
}

public void dailyRefresh() {
try {
resetAchievement(ConstEnum.AchieveReset.EveryDay);
this.player.isOnline();
}
catch (Exception e) {

e.printStackTrace();
} 
}

public void weekRefresh() {
resetAchievement(ConstEnum.AchieveReset.EveryWeek);
}

public void resetAchievement(ConstEnum.AchieveReset resetType) {
synchronized (this.achievemap) {
for (AchievementIns achieve : this.achievemap.values()) {
if (achieve.refData.Reset != resetType) {
continue;
}

if (achieve.refData.AchieveName == Achievement.AchievementType.SkillUp) {
int level = 0;
for (Character charc : ((CharFeature)this.player.getFeature(CharFeature.class)).getAll().values()) {
for (Iterator<Integer> iterator = charc.getBo().getSkillAll().iterator(); iterator.hasNext(); ) { int skill = ((Integer)iterator.next()).intValue();
level += skill; }

} 
if (level >= 1485) {
achieve.bo.setGainPrizeList("");
achieve.bo.saveAll();
this.player.pushProto("UpdateAchieve", build(achieve.bo));

continue;
} 
} 

achieve.bo.setAchieveCount(0);
achieve.bo.setCompleteCount(0);
achieve.bo.setGainPrizeList("");
achieve.bo.saveAll();
this.player.pushProto("UpdateAchieve", build(achieve.bo));
} 
} 
}

public Reward cmd_pickUpActivityPrize(int achieveID, int achieveCount) throws WSException {
Reward pack;
RefAchievement ref = (RefAchievement)RefDataMgr.get(RefAchievement.class, Integer.valueOf(achieveID));
AchievementIns ins = this.achievemap.get(ref.AchieveName);
if (ins == null) {
throw new WSException(ErrorCode.NotFound_Achievement, "没有找到该成就列表 cid:%s,achieveID:%s", new Object[] { Long.valueOf(this.player.getPid()), Integer.valueOf(achieveID) });
}
List<Integer> gainList = StringUtils.string2Integer(ins.bo.getGainPrizeList());
if (gainList.contains(Integer.valueOf(achieveCount))) {
throw new WSException(ErrorCode.ALREADY_FETCH, "已经领取了该阶段的奖励");
}
int achieveIndex = achieveCount;
int maxIndex = ins.refData.FirstArgsList.size();

if (achieveIndex >= maxIndex) {
throw new WSException(ErrorCode.NotEnough_CompleteLevel, "achieveIndex%s>=ins.refData.FirstArgsList.size():%s", new Object[] { Integer.valueOf(achieveIndex), Integer.valueOf(maxIndex) });
}

int curCnt = ins.bo.getCompleteCount();
int needCnt = ((Integer)ins.refData.FirstArgsList.get(achieveIndex)).intValue();
if (isRankAchievement(ref.AchieveName)) {
if (curCnt > needCnt) {
throw new WSException(ErrorCode.NotEnough_CompleteCount, "奖励还没达到领取条件cid:%s,achieveID:%s,index:%s,curCnt:%s,needCnt:%s", new Object[] { Long.valueOf(this.player.getPid()), 
Integer.valueOf(achieveID), Integer.valueOf(achieveIndex), Integer.valueOf(curCnt), Integer.valueOf(needCnt) });
}
}
else if (curCnt < needCnt) {
throw new WSException(ErrorCode.NotEnough_CompleteCount, "奖励还没达到领取条件cid:%s,achieveID:%s,index:%s,curCnt:%s,needCnt:%s", new Object[] { Long.valueOf(this.player.getPid()), 
Integer.valueOf(achieveID), Integer.valueOf(achieveIndex), Integer.valueOf(curCnt), Integer.valueOf(needCnt) });
} 

int prizeID = ((Integer)ins.refData.PrizeIDList.get(achieveIndex)).intValue();
Reward reward = ((RefReward)RefDataMgr.get(RefReward.class, Integer.valueOf(prizeID))).genReward();

if (reward != null) {
pack = ((PlayerItem)this.player.getFeature(PlayerItem.class)).gain(reward, ItemFlow.Achievement);
} else {
pack = new Reward();
} 
gainList.add(Integer.valueOf(achieveCount));
if (ins.refData.AchieveName == Achievement.AchievementType.MonthCardCrystal) {
ins.bo.saveCompleteCount(ins.bo.getCompleteCount() - 1);
}
ins.bo.saveGainPrizeList(StringUtils.list2String(gainList));

this.player.pushProto("UpdateAchieve", build(ins.bo));
return pack;
}

public boolean isRankAchievement(Achievement.AchievementType type) {
return false;
}

public Reward cmd_pickUpPrize(int achieveID, int achieveCount) throws WSException {
RefAchievement ref = (RefAchievement)RefDataMgr.get(RefAchievement.class, Integer.valueOf(achieveID));
AchievementIns ins = this.achievemap.get(ref.AchieveName);
if (ins == null) {
throw new WSException(ErrorCode.NotFound_Achievement, "没有找到该成就列表 cid:%s,achieveID : %s", new Object[] { Long.valueOf(this.player.getPid()), Integer.valueOf(achieveID) });
}
List<Integer> gainList = StringUtils.string2Integer(ins.bo.getGainPrizeList());
if (gainList.contains(Integer.valueOf(achieveCount))) {
throw new WSException(ErrorCode.ALREADY_FETCH, "已经领取了该阶段的奖励");
}
if (achieveCount >= ins.refData.FirstArgsList.size() || achieveCount < 0) {
throw new WSException(ErrorCode.InvalidParam, "客户端发来的参数错误,achieveCount不能<0||achieveCount>任务数量");
}
if (achieveCount >= ins.bo.getAchieveCount()) {
throw new WSException(ErrorCode.NotEnough_CompleteCount, "奖励还没达到领取条件 ");
}
if (ref.ConditionPreTaskId != 0) {
RefAchievement pre_ref = (RefAchievement)RefDataMgr.get(RefAchievement.class, Integer.valueOf(ref.ConditionPreTaskId));
AchievementIns pre_ins = this.achievemap.get(pre_ref.AchieveName);
if (pre_ins == null) {
throw new WSException(ErrorCode.NotFound_Achievement, "没有找到前置成就列表 cid:%s,achieveID : %s", new Object[] { Long.valueOf(this.player.getPid()), Integer.valueOf(ref.ConditionPreTaskId) });
}
List<Integer> pre_gainList = StringUtils.string2Integer(pre_ins.bo.getGainPrizeList());

if (pre_gainList.size() < pre_ref.PrizeIDList.size()) {
throw new WSException(ErrorCode.NotEnough_CompleteCount, "前置任务没有完成 ");
}
} 

if (ref.TaskType == ConstEnum.TaskClassify.DailyTask) {
TaskActivityFeature taskActivityFeature = (TaskActivityFeature)this.player.getFeature(TaskActivityFeature.class);
taskActivityFeature.addDailyActive(ins.refData.ActiveScore);
} 

int prizeID = ((Integer)ins.refData.PrizeIDList.get(achieveCount)).intValue();
Reward reward = ((RefReward)RefDataMgr.get(RefReward.class, Integer.valueOf(prizeID))).genReward();
Reward pack = ((PlayerItem)this.player.getFeature(PlayerItem.class)).gain(reward, ItemFlow.Achievement);
gainList.add(Integer.valueOf(achieveCount));
if (ins.refData.AchieveName == Achievement.AchievementType.MonthCardCrystal) {
ins.bo.saveCompleteCount(ins.bo.getCompleteCount() - 1);
}
ins.bo.saveGainPrizeList(StringUtils.list2String(gainList));
this.player.pushProto("UpdateAchieve", build(ins.bo));
return pack;
}

public void refreshSevenDay() {}

public void checkMonthCard(Achievement.AchievementType type) {
AchievementIns ins = getOrCreate(type);
if (ins == null) {
return;
}

int achieveIndex = ins.bo.getAchieveCount();
int maxIndex = ins.refData.FirstArgsList.size();

if (achieveIndex >= maxIndex) {
return;
}

RechargeFeature rechargeFeature = (RechargeFeature)this.player.getFeature(RechargeFeature.class);
int dayNum = rechargeFeature.getRebateRemains(type);
if (dayNum == 0 || dayNum < -1) {
return;
}
int count = ins.bo.getCompleteCount();
ins.bo.saveCompleteCount(dayNum);
ins.bo.saveAchieveCount(count + 1);
this.player.pushProto("UpdateAchieve", build(ins.bo));
}

public void checkAchieve(Achievement.AchievementType type) {
AchievementIns ins = getOrCreate(type);
if (ins == null) {
return;
}

if (this.player.getPlayerBO().getLv() < ins.refData.Condition) {
return;
}
long now = CommTime.nowMS();
if (now >= CommTime.getTodayHourMS(((Integer)ins.refData.SecondArgsList.get(0)).intValue()) && now < CommTime.getTodayHourMS(((Integer)ins.refData.SecondArgsList.get(1)).intValue())) {
int count = ins.bo.getCompleteCount();
ins.bo.saveAchieveCount(count + 1);
this.player.pushProto("UpdateAchieve", build(ins.bo));
} else if (now >= CommTime.getTodayHourMS(((Integer)ins.refData.SecondArgsList.get(1)).intValue())) {
if (StringUtils.string2Integer(ins.bo.getGainPrizeList()).size() != 0) {
return;
}
ins.bo.saveAchieveCount(0);
this.player.pushProto("UpdateAchieve", build(ins.bo));
} 
}

public void openGetEnergy(Achievement.AchievementType type) {
AchievementIns ins = getOrCreate(type);

if (this.player.getPlayerBO().getLv() < ins.refData.Condition) {
return;
}

long now = CommTime.nowMS();
if (now < CommTime.getTodayHourMS(((Integer)ins.refData.SecondArgsList.get(0)).intValue()) || now > CommTime.getTodayHourMS(((Integer)ins.refData.SecondArgsList.get(1)).intValue())) {
return;
}
int count = ins.bo.getCompleteCount();
ins.bo.saveAchieveCount(count + 1);
this.player.pushProto("UpdateAchieve", build(ins.bo));
}

public void closeGetEnergy(Achievement.AchievementType type) {
AchievementIns ins = getOrCreate(type);

if (this.player.getPlayerBO().getLv() < ins.refData.Condition) {
return;
}
if (StringUtils.string2Integer(ins.bo.getGainPrizeList()).size() != 0) {
return;
}
ins.bo.saveAchieveCount(0);
this.player.pushProto("UpdateAchieve", build(ins.bo));
}

public void effectActivation(int cardId) throws WSException {}

private void taskChieveCount(AchievementBO bo, RefAchievement ref) {
for (int i = bo.getAchieveCount(); i < ref.FirstArgsList.size() && 
bo.getCompleteCount() >= ((Integer)ref.FirstArgsList.get(i)).intValue();)
{

bo.saveAchieveCount(++i);
}
}
}

