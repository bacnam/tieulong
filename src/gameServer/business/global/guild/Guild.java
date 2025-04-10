package business.global.guild;

import BaseCommon.CommClass;
import BaseCommon.CommLog;
import BaseThread.BaseMutexManager;
import business.global.activity.ActivityMgr;
import business.global.activity.detail.RankGuild;
import business.global.gmmail.MailCenter;
import business.global.guild.rank.catagere.GuildNormalRank;
import business.global.rank.RankManager;
import business.player.Player;
import business.player.PlayerMgr;
import business.player.feature.PlayerBase;
import business.player.feature.PlayerCurrency;
import business.player.feature.character.CharFeature;
import business.player.feature.features.PlayerRecord;
import business.player.feature.guild.GuildMemberFeature;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.enums.ConstEnum;
import com.zhonglian.server.common.enums.GuildJob;
import com.zhonglian.server.common.enums.GuildRankType;
import com.zhonglian.server.common.enums.JoinState;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.common.enums.RankType;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.common.utils.Lists;
import com.zhonglian.server.common.utils.Maps;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefCrystalPrice;
import core.config.refdata.ref.RefGuildBoss;
import core.config.refdata.ref.RefGuildLevel;
import core.config.refdata.ref.RefGuildWarCenter;
import core.config.refdata.ref.RefLongnvLevel;
import core.config.refdata.ref.RefMonster;
import core.database.game.bo.GuildApplyBO;
import core.database.game.bo.GuildBO;
import core.database.game.bo.GuildBoardBO;
import core.database.game.bo.GuildBossBO;
import core.database.game.bo.GuildLogBO;
import core.database.game.bo.GuildMemberBO;
import core.database.game.bo.GuildRankRecordBO;
import core.database.game.bo.GuildwarapplyBO;
import core.database.game.bo.GuildwarpuppetBO;
import core.network.proto.Player;
import core.server.OpenSeverTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Guild
{
public final GuildBO bo;
public final List<Long> members = Lists.newArrayList();
public final Map<GuildJob, List<Long>> membersDirJob = Maps.newConcurrentHashMap();

public final Map<Long, GuildApplyBO> applyDirCid = Maps.newConcurrentHashMap();

public List<GuildBoardBO> boards = Lists.newArrayList();

public Queue<GuildLogBO> logs = Lists.newLinkedList();

public Set<Long> sacrificeMember = Lists.newHashSet();

public int rank;

public List<GuildBossBO> guildboss = Lists.newArrayList();

public Map<GuildBossBO, List<Player>> joinPlayers = new HashMap<>();

public GuildwarapplyBO guildwarCenter;

public Map<Long, List<GuildwarpuppetBO>> puppets = Maps.newConcurrentHashMap();

LongnvWar longnv;

protected final BaseMutexManager m_mutex = new BaseMutexManager(); private Integer power; private Map<GuildRankType, GuildRank> ranks;

public void lock() {
this.m_mutex.lock();
}

public void unlock() {
this.m_mutex.unlock();
}

public GuildBO getBo() {
return this.bo;
}

public long getGuildId() {
return this.bo.getId();
}

public String getName() {
return this.bo.getName();
}

public LongnvWar getOrCreateLongnv() {
LongnvWar longnv = this.longnv;
if (longnv != null) {
return longnv;
}
synchronized (this) {
longnv = this.longnv;
if (longnv != null) {
return longnv;
}
longnv = new LongnvWar(this);
this.longnv = longnv;
} 
return longnv;
}

public int gainExp(int guildExp) {
RefGuildLevel nextLeveLinfo = (RefGuildLevel)RefDataMgr.get(RefGuildLevel.class, Integer.valueOf(getLevel() + 1));
if (nextLeveLinfo == null) {
return 0;
}
this.bo.saveExp(this.bo.getExp() + guildExp);
RefGuildLevel levelinfo = (RefGuildLevel)RefDataMgr.get(RefGuildLevel.class, Integer.valueOf(getLevel()));
GuildBO bo = getBo();
if (bo.getExp() < levelinfo.UpgradeValue) {
return guildExp;
}
bo.setExp(bo.getExp() - levelinfo.UpgradeValue);
bo.setLevel(bo.getLevel() + 1);
if (RefDataMgr.get(RefGuildLevel.class, Integer.valueOf(getLevel() + 1)) == null) {
bo.setExp(0);
}
bo.saveAll();

broadcast("guildLevelUp", new levelUpInfo(bo.getLevel(), bo.getExp(), null));
return guildExp;
}

public int gainLongnvExp(int Exp) {
RefLongnvLevel nextLeveLinfo = (RefLongnvLevel)RefDataMgr.get(RefLongnvLevel.class, Integer.valueOf(getLongnvLevel() + 1));
if (nextLeveLinfo == null) {
return 0;
}
this.bo.saveLnexp(this.bo.getLnexp() + Exp);
RefLongnvLevel levelinfo = (RefLongnvLevel)RefDataMgr.get(RefLongnvLevel.class, Integer.valueOf(getLongnvLevel()));
GuildBO bo = getBo();
if (bo.getLnexp() < levelinfo.UpgradeValue) {
return Exp;
}
bo.setLnexp(bo.getLnexp() - levelinfo.UpgradeValue);
bo.setLnlevel(bo.getLnlevel() + 1);
if (RefDataMgr.get(RefLongnvLevel.class, Integer.valueOf(getLongnvLevel() + 1)) == null) {
bo.setLnexp(0);
}
bo.saveAll();

broadcast("guildLongnvLevelUp", new levelUpInfo(bo.getLnlevel(), bo.getLnexp(), null));
return Exp;
}

private class levelUpInfo
{
int level;
int exp;

private levelUpInfo(int level, int exp) {
this.level = level;
this.exp = exp;
}
}

public int getLevel() {
return this.bo.getLevel();
}

public int getLongnvLevel() {
return this.bo.getLnlevel();
}

public JoinState getJoinState() {
return JoinState.values()[this.bo.getJoinState()];
}

public int getLevelLimit() {
return this.bo.getLevelLimit();
}

public void setLastLoginTime(int current) {
this.bo.saveLastLoginTime(current);
}

public List<Long> getMembers() {
return Lists.newArrayList(this.members);
}

public int getMemberCount(GuildJob job) {
return getMember(job).size();
}

public List<Long> getMember(GuildJob job) {
List<Long> find = this.membersDirJob.get(job);
if (find == null) {
this.membersDirJob.put(job, find = new ArrayList<>());
}
return find;
}

public int getMemberCnt() {
return this.members.size();
}

public Collection<GuildApplyBO> getApplies() {
return this.applyDirCid.values();
}

public GuildApplyBO getApply(long cid) {
return this.applyDirCid.get(Long.valueOf(cid));
}

public List<GuildBoardBO> getBoardList() {
return this.boards;
}

public int getSacrificeCount() {
return this.sacrificeMember.size();
}

public void incSacrificeCount(long cid) {
this.sacrificeMember.add(Long.valueOf(cid));
}

public List<GuildBossBO> getbosslist() {
return this.guildboss;
}

public GuildBossBO getboss(int bossId) {
for (GuildBossBO bo : this.guildboss) {
if (bo.getBossId() == bossId) {
return bo;
}
} 
return null;
}

public void broadcast(String protoName, Object proto, long cid) {
PlayerMgr mgr = PlayerMgr.getInstance();
for (Long member : new ArrayList(this.members)) {
if (cid != member.longValue()) {
mgr.getPlayer(member.longValue()).pushProto(protoName, proto);
}
} 
}

public void broadcast(String protoName, Object proto) {
PlayerMgr mgr = PlayerMgr.getInstance();
for (Long member : new ArrayList(this.members)) {
mgr.getPlayer(member.longValue()).pushProto(protoName, proto);
}
}

public void broadcastMember(long... cids) {
PlayerMgr playerMgr = PlayerMgr.getInstance();
List<core.network.proto.Guild.member> members = Lists.newArrayList(); byte b; int i; long[] arrayOfLong;
for (i = (arrayOfLong = cids).length, b = 0; b < i; ) { long cid = arrayOfLong[b];
if (this.members.contains(Long.valueOf(cid)))
{

members.add(((GuildMemberFeature)playerMgr.getPlayer(cid).getFeature(GuildMemberFeature.class)).memberInfo()); }  b++; }

broadcast("memberInfo", members);
}

public List<GuildMemberFeature> getAllMemberFeatures() {
List<GuildMemberFeature> members = Lists.newArrayList();
PlayerMgr playerMgr = PlayerMgr.getInstance();
for (Long memberid : new ArrayList(this.members)) {
members.add((GuildMemberFeature)playerMgr.getPlayer(memberid.longValue()).getFeature(GuildMemberFeature.class));
}
return members;
}

public GuildMemberFeature getMember(long cid) {
if (!this.members.contains(Long.valueOf(cid))) {
return null;
}
return (GuildMemberFeature)PlayerMgr.getInstance().getPlayer(cid).getFeature(GuildMemberFeature.class);
}

public void takeinMember(Player player) {
this.members.add(Long.valueOf(player.getPid()));
getMember(GuildJob.Member).add(Long.valueOf(player.getPid()));

GuildMemberBO memberBO = ((GuildMemberFeature)player.getFeature(GuildMemberFeature.class)).getOrCreate();
memberBO.setGuildId(getGuildId());
memberBO.setJob(GuildJob.Member.ordinal());
memberBO.setJoinTime(CommTime.nowSecond());
memberBO.saveAll();
player.notify2Zone();

GuildMgr.getInstance().removeApply(player.getPid());
broadcastMember(new long[] { player.getPid() });

player.pushProto("guildSkill", ((GuildMemberFeature)player.getFeature(GuildMemberFeature.class)).getGuildSkillList());

((CharFeature)player.getFeature(CharFeature.class)).updateCharPower();
updatePower(player.getPlayerBO().getMaxFightPower());
MailCenter.getInstance().sendMail(player.getPid(), GuildConfig.JoinGuildMailID(), new String[] { this.bo.getName() });
}

public void removeMember(Player player) {
this.members.remove(Long.valueOf(player.getPid()));
this.membersDirJob.remove(((GuildMemberFeature)player.getFeature(GuildMemberFeature.class)).getJob());
this.sacrificeMember.remove(Long.valueOf(player.getPid()));

updatePower(-player.getPlayerBO().getMaxFightPower());
((CharFeature)player.getFeature(CharFeature.class)).updateCharPower();
}

public void addBoardMessage(GuildBoardBO bo) {
int maxCount = GuildConfig.MaxBoardMessageCount();
List<GuildBoardBO> bList = new ArrayList<>(this.boards);
bList.add(bo);
bList.sort((left, right) -> (left.getTop() == right.getTop()) ? (right.getMessageTime() - left.getMessageTime()) : (left.getTop() ? -1 : 1));

for (int i = maxCount; i < bList.size(); i++) {
((GuildBoardBO)bList.get(i)).del();
}
this.boards = bList.subList(0, Math.min(bList.size(), maxCount));
}

public void dailyEvent() {
List<GuildMemberFeature> members = getAllMemberFeatures();

promotePresident(members);

this.sacrificeMember.clear();

for (GuildBossBO boss : this.guildboss) {
boss.del();
}
this.guildboss.clear();

getBo().saveGuildbossOpenNum(0);

clear(GuildRankType.GuildBoss);

this.guildwarCenter = null;

if (CommTime.nowSecond() >= OpenSeverTime.getInstance().getOpenZeroTime() + RefDataMgr.getFactor("GuildwarOpenServerTime", 604800)) {
for (Map.Entry<Integer, Guild> entry : (GuildWarMgr.getInstance()).historyWinner.entrySet()) {
RefGuildWarCenter ref = (RefGuildWarCenter)RefDataMgr.get(RefGuildWarCenter.class, entry.getKey());

if (!ref.isOpenTime()) {
continue;
}

if (entry.getValue() == this) {
GuildwarapplyBO bo = new GuildwarapplyBO();
bo.setGuildId(getGuildId());
bo.setCenterId(((Integer)entry.getKey()).intValue());
bo.setApplyTime(CommTime.nowSecond());
bo.insert();
this.guildwarCenter = bo;
} 
} 
}

this.puppets.clear();
}

private void promotePresident(List<GuildMemberFeature> members) {
Long oldPresidentCid = ((List<Long>)this.membersDirJob.get(GuildJob.President)).get(0);
Player president = PlayerMgr.getInstance().getPlayer(oldPresidentCid.longValue());

if (CommTime.nowSecond() < president.getPlayerBO().getLastLogin() + 604800) {
return;
}

GuildMemberFeature newPresident = null;
GuildMemberFeature oldPresident = null;
for (int i = 0; i < members.size(); i++) {
GuildMemberFeature m = members.get(i);
if (oldPresidentCid.longValue() == m.getPlayer().getPid()) {
oldPresident = m;

}
else if (newPresident == null) {
newPresident = m;
} else if (m.getJob().ordinal() < newPresident.getJob().ordinal()) {
newPresident = m;
} else if (m.getJob() == newPresident.getJob() && m.getDonate() > newPresident.getDonate()) {
newPresident = m;
} 
} 

if (newPresident == null) {
return;
}

getMember(oldPresident.getJob()).remove(Long.valueOf(oldPresident.getPid()));
oldPresident.setJob(GuildJob.Member);

getMember(newPresident.getJob()).remove(Long.valueOf(newPresident.getPid()));
newPresident.setJob(GuildJob.President);

broadcastMember(new long[] { oldPresident.getPlayer().getPid(), newPresident.getPlayer().getPid() });
}

public core.network.proto.Guild.JoinInfo joinInfo() {
core.network.proto.Guild.JoinInfo builder = new core.network.proto.Guild.JoinInfo();
builder.setsId(this.bo.getId());
builder.setLevel(this.bo.getLevel());
builder.setMemberCnt(getMemberCnt());
RefGuildLevel refGuildLevel = (RefGuildLevel)RefDataMgr.get(RefGuildLevel.class, Integer.valueOf(this.bo.getLevel()));
builder.setMaxmemberCnt(refGuildLevel.MaxMemberAmount);
builder.setIconId(this.bo.getIcon());
builder.setBorderId(this.bo.getBorder());
builder.setName(this.bo.getName());
Long mster = getMember(GuildJob.President).get(0);
builder.setMaster(((PlayerBase)PlayerMgr.getInstance().getPlayer(mster.longValue()).getFeature(PlayerBase.class)).summary());
builder.setManifesto(this.bo.getManifesto());
builder.setJoinState(getJoinState());
builder.setJoinLevel(this.bo.getLevelLimit());
builder.setRank(this.rank);
return builder;
}

public core.network.proto.Guild.guildInfo guildInfo() {
core.network.proto.Guild.guildInfo builder = new core.network.proto.Guild.guildInfo();
builder.setsId(this.bo.getId());
builder.setLevel(this.bo.getLevel());
builder.setExp(this.bo.getExp());
builder.setName(this.bo.getName());
builder.setIconId(this.bo.getIcon());
builder.setBorderId(this.bo.getBorder());
builder.setCreateTime(this.bo.getCreateTime());
builder.setNotice(this.bo.getNotice());
builder.setManifesto(this.bo.getManifesto());
builder.setJoinState(getJoinState());
builder.setJoinLevel(this.bo.getLevelLimit());
Long mster = getMember(GuildJob.President).get(0);
builder.setMaster(((PlayerBase)PlayerMgr.getInstance().getPlayer(mster.longValue()).getFeature(PlayerBase.class)).summary());
builder.setNowNum(getMemberCnt());
RefGuildLevel refGuildLevel = (RefGuildLevel)RefDataMgr.get(RefGuildLevel.class, Integer.valueOf(this.bo.getLevel()));
builder.setMaxNum(refGuildLevel.MaxMemberAmount);

return builder;
}

private int calculatePower() {
int power = 0;

for (Long pid : getMembers()) {
Player player = PlayerMgr.getInstance().getPlayer(pid.longValue());
power += ((CharFeature)player.getFeature(CharFeature.class)).getPower();
} 

if (power > this.bo.getMaxFightPower()) {
this.bo.saveMaxFightPower(power);
}
return power;
}

public int updatePower() {
this.power = Integer.valueOf(calculatePower());

RankManager.getInstance().update(RankType.Guild, getGuildId(), this.power.intValue());
for (Iterator<Long> iterator = this.members.iterator(); iterator.hasNext(); ) { long pid = ((Long)iterator.next()).longValue();
Player player = PlayerMgr.getInstance().getPlayer(pid);
((RankGuild)ActivityMgr.getActivity(RankGuild.class)).UpdateMaxRequire_cost(player, this.power.intValue()); }

return this.power.intValue();
}

public int updatePower(int change) {
if (this.power == null) {
return getPower();
}
this.power = Integer.valueOf(this.power.intValue() + change);

if (this.power.intValue() > this.bo.getMaxFightPower()) {
this.bo.saveMaxFightPower(this.power.intValue());
}

RankManager.getInstance().update(RankType.Guild, getGuildId(), this.power.intValue());
for (Iterator<Long> iterator = this.members.iterator(); iterator.hasNext(); ) { long pid = ((Long)iterator.next()).longValue();
Player player = PlayerMgr.getInstance().getPlayer(pid);
((RankGuild)ActivityMgr.getActivity(RankGuild.class)).UpdateMaxRequire_cost(player, this.power.intValue()); }

return this.power.intValue();
}

public int getPower() {
if (this.power != null) {
return this.power.intValue();
}
return updatePower();
}

public void openBoss(RefGuildBoss refboss) {
RefMonster ref = (RefMonster)RefDataMgr.get(RefMonster.class, Integer.valueOf(refboss.MonsterId));
GuildBossBO bossBO = new GuildBossBO();
bossBO.setBossId(refboss.id);
bossBO.setGuildId(getGuildId());
bossBO.setBossMaxHp(ref.MaxHP);
bossBO.setBossHp(bossBO.getBossMaxHp());
bossBO.setIsOpen(true);
bossBO.insert();
this.guildboss.add(bossBO);
this.bo.saveGuildbossOpenNum(this.bo.getGuildbossOpenNum() + 1);
broadcast("GuildBossOpen", bossBO);
}

public boolean isInOpenHour() {
return GuildConfig.getGuildBossOpenTime().within(CommTime.getTodayHour());
}

public void doEnterGuildBoss(Player player, GuildBossBO Boss) {
List<Player> players = this.joinPlayers.get(Boss);
if (players == null) {
List<Player> playerL = new ArrayList<>();
playerL.add(player);
this.joinPlayers.put(Boss, playerL);
}
else if (!players.contains(player)) {
players.add(player);
this.joinPlayers.put(Boss, players);
} 

for (Player tmpPlayer : this.joinPlayers.get(Boss)) {
if (tmpPlayer != null && tmpPlayer != player)
tmpPlayer.pushProto("newGuildBossPlayer", ((PlayerBase)player.getFeature(PlayerBase.class)).fightInfo()); 
} 
}

public List<Player> getPlayerList(GuildBossBO boss) {
return this.joinPlayers.get(boss);
}

public boolean doHurtGuildBoss(Player player, int damage, int bossId) throws WSException {
boolean isKill = false;
synchronized (this) {
GuildBossBO bo = getboss(bossId);
if (bo.getIsDead()) {
throw new WSException(ErrorCode.GuildBoss_IsDeath, "Boss已被消灭");
}
bo.setBossHp(bo.getBossHp() - damage);
if (bo.getBossHp() <= 0L) {
bo.setDeadTime(CommTime.nowSecond());
bo.setBossHp(0L);
bo.setIsDead(true);
bo.saveIsOpen(false);
bo.setLastKillCid(player.getPid());
isKill = true;
} 
bo.saveAll();

if (isKill) {

for (Long mem : this.members) {
RefGuildBoss refBoss = (RefGuildBoss)RefDataMgr.get(RefGuildBoss.class, Integer.valueOf(bossId));
MailCenter.getInstance().sendMail(mem.longValue(), refBoss.MailId, new String[0]);
} 
this.bo.saveGuildbossLevel(Math.max(this.bo.getGuildbossLevel(), bossId));
broadCastProtoToPlayers(bo);
} 
} 
return isKill;
}

public void broadCastProtoToPlayers(GuildBossBO boss) {
broadcast("guildbossDead", boss);
}

public void doLeaveGuildBoss(Player player, int bossId) throws WSException {
GuildBossBO boss = getboss(bossId);
if (this.joinPlayers.get(boss) != null) {
boolean remove = ((List)this.joinPlayers.get(boss)).remove(player);
if (!remove) {
throw new WSException(ErrorCode.GuildBoss_NotEnter, "未进入帮派BOSS");
}
} 
}

public Guild(GuildBO bo) {
this.ranks = new HashMap<>();
this.bo = bo;
} public void init() {
this.guildboss = BM.getBM(GuildBossBO.class).findAll("guildId", Long.valueOf(getGuildId()));

Set<Class<?>> clazzs = CommClass.getClasses(GuildNormalRank.class.getPackage().getName());
for (Class<?> clz : clazzs) {

try {
Class<? extends GuildRank> clazz = (Class)clz;
GuildRanks types = clazz.<GuildRanks>getAnnotation(GuildRanks.class);
if (types == null || (types.value()).length == 0) {
CommLog.error("[{}]的类型没有相关排行榜类型，请检查", clazz.getSimpleName());
System.exit(0);
}  byte b; int i; GuildRankType[] arrayOfGuildRankType;
for (i = (arrayOfGuildRankType = types.value()).length, b = 0; b < i; ) { GuildRankType type = arrayOfGuildRankType[b];
if (this.ranks.containsKey(type)) {
String preClass = ((GuildRank)this.ranks.get(type)).getClass().getSimpleName();
CommLog.error("[{},{}]重复定义[{}]类型的排行榜", preClass, clazz.getSimpleName());
System.exit(0);
} 
GuildRank instance = clazz.getConstructor(new Class[] { GuildRankType.class }).newInstance(new Object[] { type });
this.ranks.put(type, instance); b++; }

} catch (Exception e) {
CommLog.error("排行榜[{}]初始化失败", clz.getSimpleName(), e);
System.exit(0);
} 
} 
List<GuildRankRecordBO> list = BM.getBM(GuildRankRecordBO.class).findAll("guildid", Long.valueOf(getGuildId()));
for (GuildRankRecordBO recordBO : list) {
GuildRankType type = GuildRankType.values()[recordBO.getType()];
GuildRank rank = this.ranks.get(type);
GuildRecord record = new GuildRecord(recordBO);
rank.map.put(Long.valueOf(recordBO.getOwner()), record);
} 
for (GuildRank rank : this.ranks.values()) {
rank.resort();
}

getOrCreateLongnv().Restart();
}

public List<GuildRecord> getRankList(GuildRankType type, int size) {
List<GuildRecord> records, list = (getRank(type)).list;
if (list.size() > size + 1) {
records = list.subList(0, size + 1);
} else {
records = new ArrayList<>(list);
} 
return records;
}

public long getPlayerId(GuildRankType type, int rank) {
int num;
List<GuildRecord> records = (getRank(type)).list;

if (rank < 1 || records.size() < 2) {
return 0L;
}
if (records.size() - 1 < rank) {
num = records.size() - 1;
} else {
num = rank;
} 

return ((GuildRecord)records.get(num)).getPid();
}

public int getRank(GuildRankType type, long ownerid) {
return getRank(type).getRank(ownerid);
}

public long getValue(GuildRankType type, long ownerid) {
return getRank(type).getValue(ownerid);
}

public int update(GuildRankType type, long ownerid, long value) {
return getRank(type).update(ownerid, value, new long[0]);
}

public int update(GuildRankType type, long ownerid, long value, long... ext) {
return getRank(type).update(ownerid, value, ext);
}

public int minus(GuildRankType type, long ownerid, int value) {
return getRank(type).minus(ownerid, value);
}

private GuildRank getRank(GuildRankType type) {
GuildRank rank = this.ranks.get(type);
if (rank == null) {
CommLog.error("排行榜[]未注册", type);
}
return rank;
}

public int getRankSize(GuildRankType type) {
GuildRank rank = this.ranks.get(type);
if (rank == null) {
CommLog.error("排行榜[]未注册", type);
}
return rank.list.size();
}

public void clear(GuildRankType type) {
getRank(type).clear();
}

public void clearPlayerData(Player player) {
BM.getBM(GuildRankRecordBO.class).delAll("owner", Long.valueOf(player.getPid()));
GuildRanks types = GuildNormalRank.class.<GuildRanks>getAnnotation(GuildRanks.class); byte b; int i; GuildRankType[] arrayOfGuildRankType;
for (i = (arrayOfGuildRankType = types.value()).length, b = 0; b < i; ) { GuildRankType type = arrayOfGuildRankType[b];
((GuildRank)this.ranks.get(type)).del(player.getPid());
b++; }

}

public void applyGuildWar(int centerId) throws WSException {
RefGuildWarCenter ref = (RefGuildWarCenter)RefDataMgr.get(RefGuildWarCenter.class, Integer.valueOf(centerId));

if (CommTime.nowSecond() < OpenSeverTime.getInstance().getOpenZeroTime() + RefDataMgr.getFactor("GuildwarOpenServerTime", 604800)) {
throw new WSException(ErrorCode.GuildWar_TimeLimit, "帮派战功能未开启");
}

if (ref == null) {
throw new WSException(ErrorCode.GuildWar_NotFoundCenter, "据点不存在");
}

if (this.guildwarCenter != null) {
throw new WSException(ErrorCode.GuildWar_AlreadyOpen, "帮派已报名");
}

if (!ref.isOpenTime()) {
throw new WSException(ErrorCode.GuildWar_NotOpen, "据点未开启");
}

if (!GuildWarConfig.applyTime()) {
throw new WSException(ErrorCode.GuildWar_ApplyNotOpen, "申请时间没到");
}

if (getLevel() < RefDataMgr.getFactor("GuildWarApplyLevel", 2)) {
throw new WSException(ErrorCode.GuildWar_LevelLimit, "帮派等级不足[%s]级，无法报名", new Object[] { Integer.valueOf(RefDataMgr.getFactor("GuildWarApplyLevel", 2)) });
}
GuildWarMgr.getInstance().applyGuildWar(centerId, this);
GuildwarapplyBO bo = new GuildwarapplyBO();
bo.setGuildId(getGuildId());
bo.setCenterId(centerId);
bo.setApplyTime(CommTime.nowSecond());
bo.insert();
this.guildwarCenter = bo;
}

public List<Player> getGuildWarPlayer() {
List<Player> list = new ArrayList<>();
getMembers().stream().forEach(mem -> paramList.add(PlayerMgr.getInstance().getPlayer(mem.longValue())));

Collections.shuffle(list);
return list;
}

public static class GuildSummary {
long guildId;
String guildName;
Player.showModle president;

public Player.showModle getPresident() {
return this.president;
}

public void setPresident(Player.showModle president) {
this.president = president;
}

public GuildSummary() {}

public GuildSummary(Guild guild) {
if (guild != null) {
this.guildId = guild.getGuildId();
this.guildName = guild.getName();
this.president = ((PlayerBase)PlayerMgr.getInstance().getPlayer(((Long)guild.getMember(GuildJob.President).get(0)).longValue()).getFeature(PlayerBase.class)).modle();
} 
}

public long getGuildId() {
return this.guildId;
}

public void setGuildId(long guildId) {
this.guildId = guildId;
}

public String getGuildName() {
return this.guildName;
}

public void setGuildName(String guildName) {
this.guildName = guildName;
}
}

public rebirth rebirth(Player player) throws WSException {
if (this.guildwarCenter == null) {
throw new WSException(ErrorCode.GuildWar_NotApply, "帮派未报名");
}
int centerId = GuildWarMgr.getInstance().isFighting(this);
if (centerId != 0) {
GuildWarMgr.getInstance().rebirth(player, centerId);
PlayerRecord playerRecord = (PlayerRecord)player.getFeature(PlayerRecord.class);
return new rebirth(getTotalPuppet(), getPersonPuppet(player.getPid()), playerRecord.getValue(ConstEnum.DailyRefresh.GuildWarRebirth));
} 

PlayerCurrency currency = (PlayerCurrency)player.getFeature(PlayerCurrency.class);
PlayerRecord recorder = (PlayerRecord)player.getFeature(PlayerRecord.class);

int curTimes = recorder.getValue(ConstEnum.DailyRefresh.GuildWarRebirth);
RefCrystalPrice prize = RefCrystalPrice.getPrize(curTimes);
if (!currency.check(PrizeType.Crystal, prize.GuildWarRebirth)) {
throw new WSException(ErrorCode.NotEnough_Crystal, "玩家召唤傀儡需要钻石%s", new Object[] { Integer.valueOf(prize.GuildWarRebirth) });
}
currency.consume(PrizeType.Crystal, prize.GuildWarRebirth, ItemFlow.GuildWar_Rebirth);
recorder.addValue(ConstEnum.DailyRefresh.GuildWarRebirth);
GuildwarpuppetBO bo = new GuildwarpuppetBO();
bo.setPid(player.getPid());
bo.setGuildId(getGuildId());
bo.savePuppetId(curTimes);
bo.setApplyTime(CommTime.nowSecond());
bo.insert();
if (this.puppets.get(Long.valueOf(player.getPid())) != null) {
((List<GuildwarpuppetBO>)this.puppets.get(Long.valueOf(player.getPid()))).add(bo);
} else {
List<GuildwarpuppetBO> list = new ArrayList<>();
list.add(bo);
this.puppets.put(Long.valueOf(player.getPid()), list);
} 

broadcast("beforePuppet", Integer.valueOf(getTotalPuppet()), player.getPid());

return new rebirth(getTotalPuppet(), getPersonPuppet(player.getPid()), recorder.getValue(ConstEnum.DailyRefresh.GuildWarRebirth));
}

public rebirth getPuppetInfo(Player player) {
PlayerRecord recorder = (PlayerRecord)player.getFeature(PlayerRecord.class);
return new rebirth(getTotalPuppet(), getPersonPuppet(player.getPid()), recorder.getValue(ConstEnum.DailyRefresh.GuildWarRebirth));
}

public int getTotalPuppet() {
int num = 0;
for (List<GuildwarpuppetBO> list : this.puppets.values()) {
num += list.size();
}

return num;
}

public int getPersonPuppet(long pid) {
int num = 0;
List<GuildwarpuppetBO> list = this.puppets.get(Long.valueOf(pid));
if (list != null) {
num = list.size();
}
return num;
}

public static class rebirth {
int total;
int my;
int rebirthTime;

public rebirth(int total, int my, int rebirthTime) {
this.total = total;
this.my = my;
this.rebirthTime = rebirthTime;
}
}

public List<Player> createPuppets() {
List<Player> puppets = new ArrayList<>();
for (Map.Entry<Long, List<GuildwarpuppetBO>> entry : this.puppets.entrySet()) {
Player player = PlayerMgr.getInstance().getPlayer(((Long)entry.getKey()).longValue());
for (GuildwarpuppetBO bo : entry.getValue()) {
GuildWarConfig.puppetPlayer p_player = new GuildWarConfig.puppetPlayer(player.getPlayerBO());
p_player.setPuppet_id(bo.getPuppetId());
p_player.setIs_puppet(true);
puppets.add(p_player);
} 
} 

return puppets;
}

public void removePuppet(Player puppet) {
List<GuildwarpuppetBO> puppets = this.puppets.get(Long.valueOf(puppet.getPid()));
GuildwarpuppetBO find = null;
if (puppets != null) {
GuildWarConfig.puppetPlayer puppetplayer = (GuildWarConfig.puppetPlayer)puppet;
for (GuildwarpuppetBO bo : puppets) {
if (bo.getPuppetId() == puppetplayer.getPuppet_id()) {
find = bo;
break;
} 
} 
} 
if (find != null)
puppets.remove(find); 
}
}

