package business.player.feature.pve;

import business.player.Player;
import business.player.feature.Feature;
import business.player.feature.PlayerItem;
import business.player.feature.character.EquipFeature;
import business.player.item.Reward;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.common.enums.UnlockType;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.common.utils.Maps;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefDungeon;
import core.config.refdata.ref.RefDungeonRebirth;
import core.config.refdata.ref.RefReward;
import core.config.refdata.ref.RefUnlockFunction;
import core.config.refdata.ref.RefVIP;
import core.database.game.bo.PlayerBO;
import core.logger.flow.FlowLogger;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class DungeonFeature extends Feature {
PlayerBO bo;
Map<Integer, RefDungeonRebirth> dungeonRebirth;
Queue<Integer> incdRecords;

public DungeonFeature(Player player) { super(player);

this.dungeonRebirth = Maps.newConcurrentHashMap();

this.begintime = 0;
this.reward = null; }
public void loadDB() { this.bo = this.player.getPlayerBO(); }
private static int maxCdPerMin = 5; private int begintime; private Reward reward; public boolean isInWinCD() { boolean rtn = (this.bo.getDungeonTime() + RefDataMgr.getFactor("DungeonCD", 3) > CommTime.nowSecond()); if (rtn) { if (this.incdRecords == null) this.incdRecords = new LinkedList<>();  this.incdRecords.add(Integer.valueOf(CommTime.nowSecond())); if (this.incdRecords.size() > maxCdPerMin)
this.incdRecords.poll();  }  return rtn; } public void beginFight(Reward reward) { this.begintime = CommTime.nowSecond();
this.reward = reward; }
public boolean isCheater() { return (this.incdRecords != null && this.incdRecords.size() >= maxCdPerMin && ((Integer)this.incdRecords.peek()).intValue() > CommTime.nowSecond() - 60); }
public int getLevel() { return this.bo.getDungeonLv(); }
public void nextDungeon() { this.bo.setDungeonLv(this.bo.getDungeonLv() + 1); }
public void DungeonUp() { this.bo.saveAll(); this.player.pushProperties("dungeonlv", this.bo.getDungeonLv()); } public void calcOfflineReward() { if (!RefUnlockFunction.checkUnlockSave(this.player, UnlockType.OfflineReward)) return;  if (this.bo.getDungeonTime() == 0 && this.bo.getLastLogout() != 0) this.bo.setDungeonTime(this.bo.getLastLogout());  if (this.bo.getDungeonTime() == 0) return;  int now = CommTime.nowSecond(); int offline = now - this.bo.getDungeonTime(); int maxOffline = RefDataMgr.getFactor("Offline_MaxOffline", 86400); int waves = Math.min(maxOffline, offline) * 100 / 1000; if (waves <= 0) return;  int add = ((RefVIP)RefDataMgr.get(RefVIP.class, Integer.valueOf(this.player.getVipLevel()))).OfflineDungeonAdd; waves = waves * (100 + add) / 100; RefDungeon refDungeon = (RefDungeon)RefDataMgr.get(RefDungeon.class, Integer.valueOf(this.bo.getDungeonLv())); Reward reward = new Reward(); reward.add(PrizeType.Gold, refDungeon.Gold * waves); reward.add(PrizeType.Exp, refDungeon.Exp * waves); RefReward rwd = (RefReward)RefDataMgr.get(RefReward.class, Integer.valueOf(refDungeon.Drop)); int remain = ((EquipFeature)this.player.getFeature(EquipFeature.class)).getRemain(); reward.addAll((Collection)rwd.genReward(waves, remain)); ((PlayerItem)this.player.getFeature(PlayerItem.class)).gain(reward, ItemFlow.Offline); this.player.pushProto("offlineReward", new Player.OfflineReward(reward, offline)); this.bo.saveDungeonTime(now); FlowLogger.offlineRewards(this.bo.getId(), this.bo.getVipLevel(), this.bo.getLv(), this.bo.getDungeonLv(), waves, offline, maxOffline); } public Reward win() throws WSException { if (this.begintime == 0) {
throw new WSException(ErrorCode.Dungeon_NotBegin, "战斗未开始，没有对应奖励");
}
int last = this.bo.getDungeonTime();
int cur = CommTime.nowSecond();
if (last != 0 && cur > last + 60) {
calcOfflineReward();
} else {
((PlayerItem)this.player.getFeature(PlayerItem.class)).gain(this.reward, ItemFlow.Dungeon_Win);
} 
this.bo.saveDungeonTime(CommTime.nowSecond());
return this.reward; }

public RefDungeonRebirth getRebirthRef(int level) {
return this.dungeonRebirth.get(Integer.valueOf(level));
}

public void setRebirthRef(int level, RefDungeonRebirth ref) {
this.dungeonRebirth.put(Integer.valueOf(level), ref);
}

public void removeRebirthRef(int level) {
this.dungeonRebirth.remove(Integer.valueOf(level));
}
}

