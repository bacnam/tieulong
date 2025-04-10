package business.global.guild;

import BaseCommon.CommLog;
import BaseTask.SyncTask.SyncTaskManager;
import business.global.chat.ChatMgr;
import business.global.rank.RankManager;
import business.player.Player;
import business.player.PlayerMgr;
import business.player.feature.guild.GuildMemberFeature;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.enums.GuildJob;
import com.zhonglian.server.common.enums.JoinState;
import com.zhonglian.server.common.mgr.sensitive.SensitiveWordMgr;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.common.utils.Maps;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefGuildWarCenter;
import core.database.game.bo.GuildApplyBO;
import core.database.game.bo.GuildBO;
import core.database.game.bo.GuildBoardBO;
import core.database.game.bo.GuildLogBO;
import core.database.game.bo.GuildMemberBO;
import core.database.game.bo.GuildwarpuppetBO;
import core.server.ServerConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GuildMgr
{
private static GuildMgr instance = new GuildMgr();

public static GuildMgr getInstance() {
return instance;
}

public Map<Long, Guild> guildDirId = Maps.newConcurrentHashMap();
public Map<String, Guild> guildDirName = Maps.newConcurrentHashMap();
public Map<Long, List<GuildApplyBO>> applysDirCid = Maps.newConcurrentHashMap();

public void init() {
List<GuildBO> guildBOs = BM.getBM(GuildBO.class).findAll();
for (GuildBO guildBO : guildBOs) {
if (!guildBO.getName().matches("s\\d+\\..+")) {
guildBO.saveName("s" + ServerConfig.ServerID() + "." + guildBO.getName());
}
Guild guild = new Guild(guildBO);
this.guildDirId.put(Long.valueOf(guildBO.getId()), guild);
this.guildDirName.put(guildBO.getName(), guild);
guild.init();
} 

List<GuildApplyBO> applys = BM.getBM(GuildApplyBO.class).findAll();
for (GuildApplyBO apply : applys) {
Guild guild = this.guildDirId.get(Long.valueOf(apply.getGuildId()));
if (guild == null) {
apply.del();
continue;
} 
if (guild.applyDirCid.get(Long.valueOf(apply.getPid())) != null) {
((GuildApplyBO)guild.applyDirCid.remove(Long.valueOf(apply.getPid()))).del();
}
guild.applyDirCid.put(Long.valueOf(apply.getPid()), apply);
List<GuildApplyBO> playerApply = this.applysDirCid.get(Long.valueOf(apply.getPid()));
if (playerApply == null) {
this.applysDirCid.put(Long.valueOf(apply.getPid()), playerApply = new ArrayList<>());
}
playerApply.add(apply);
} 

List<GuildMemberBO> memberBOs = BM.getBM(GuildMemberBO.class).findAll();
for (GuildMemberBO memberBO : memberBOs) {

if (memberBO.getGuildId() == 0L || memberBO.getGuildId() == -1L) {
continue;
}
Guild guild = this.guildDirId.get(Long.valueOf(memberBO.getGuildId()));
if (guild == null) {
memberBO.del();
continue;
} 
guild.members.add(Long.valueOf(memberBO.getPid()));
GuildJob job = GuildJob.values()[memberBO.getJob()];
if (job == null || job == GuildJob.None) {
job = GuildJob.Member;
memberBO.saveJob(job.ordinal());
} 
List<Long> jobMembers = guild.membersDirJob.get(job);
if (jobMembers == null) {
guild.membersDirJob.put(job, jobMembers = new ArrayList<>());
}
jobMembers.add(Long.valueOf(memberBO.getPid()));
if (memberBO.getSacrificeStatus() != 0)
{
guild.sacrificeMember.add(Long.valueOf(memberBO.getPid()));
}
} 

List<GuildwarpuppetBO> puppets = BM.getBM(GuildwarpuppetBO.class).findAll();
for (GuildwarpuppetBO puppet : puppets) {
Guild guild = this.guildDirId.get(Long.valueOf(puppet.getGuildId()));
if (guild == null) {
puppet.del();
continue;
} 
if (puppet.getApplyTime() < CommTime.getTodayZeroClockS()) {
puppet.del();
continue;
} 
if (guild.puppets.get(Long.valueOf(puppet.getPid())) != null) {
((List<GuildwarpuppetBO>)guild.puppets.get(Long.valueOf(puppet.getPid()))).add(puppet); continue;
} 
List<GuildwarpuppetBO> list = new ArrayList<>();
list.add(puppet);
guild.puppets.put(Long.valueOf(puppet.getPid()), list);
} 

List<GuildBoardBO> boards = BM.getBM(GuildBoardBO.class).findAll();
for (GuildBoardBO board : boards) {
Guild guild = this.guildDirId.get(Long.valueOf(board.getGuildId()));
if (guild == null) {
board.del();
continue;
} 
guild.boards.add(board);
} 

List<GuildLogBO> logs = BM.getBM(GuildLogBO.class).findAll();
logs.sort((left, right) -> left.getTime() - right.getTime());

for (GuildLogBO log : logs) {
Guild guild = this.guildDirId.get(Long.valueOf(log.getGuildId()));
if (guild == null) {
log.del();
continue;
} 
guild.logs.add(log);
} 
}

public List<Guild> getAllGuild() {
return new ArrayList<>(this.guildDirId.values());
}

public Guild getGuild(String name) {
Guild guild = this.guildDirName.get(name);
if (guild != null) {
return guild;
}
return null;
}

public Guild getGuild(long guildid) {
Guild guild = this.guildDirId.get(Long.valueOf(guildid));
if (guild != null) {
return guild;
}
return null;
}

public Guild getGuildData(long guildId) {
return this.guildDirId.get(Long.valueOf(guildId));
}

public void createGuild(String name, int iconId, Player player) {
GuildBO bo = new GuildBO();
bo.setName("s" + ServerConfig.ServerID() + "." + name);
bo.setIcon(iconId);
bo.setLevel(1);
bo.setJoinState(JoinState.AccptAll.ordinal());
bo.setLastLoginTime(CommTime.nowSecond());
bo.setCreateTime(CommTime.nowSecond());
bo.insert();

Guild guild = new Guild(bo);
guild.members.add(Long.valueOf(player.getPid()));
guild.membersDirJob.put(GuildJob.President, new ArrayList<>());
((List<Long>)guild.membersDirJob.get(GuildJob.President)).add(Long.valueOf(player.getPid()));

this.guildDirId.put(Long.valueOf(bo.getId()), guild);
this.guildDirName.put(bo.getName(), guild);

GuildMemberFeature guildMember = (GuildMemberFeature)player.getFeature(GuildMemberFeature.class);
GuildMemberBO memberBO = guildMember.getOrCreate();
memberBO.setGuildId(bo.getId());
memberBO.setJob(GuildJob.President.ordinal());
memberBO.setJoinTime(CommTime.nowSecond());
memberBO.saveAll();
player.notify2Zone();

guild.init();

guild.updatePower();
}

public void deleteGuild(long guildId) {
Guild guild = this.guildDirId.get(Long.valueOf(guildId));
if (guild == null) {
return;
}
this.guildDirName.remove(guild.bo.getName());

PlayerMgr playerMgr = PlayerMgr.getInstance();
(new ArrayList(guild.members)).forEach(x -> ((GuildMemberFeature)paramPlayerMgr.getPlayer(x.longValue()).getFeature(GuildMemberFeature.class)).leave());

guild.membersDirJob.clear();

guild.applyDirCid.values().forEach(x -> {
((List)this.applysDirCid.get(Long.valueOf(x.getPid()))).remove(x);
x.del();
});
guild.applyDirCid.clear();

guild.boards.forEach(x -> x.del());

guild.boards.clear();

ChatMgr.getInstance().cleanGuildMessage(guild.bo.getId());

guild.bo.del();

this.guildDirId.remove(Long.valueOf(guildId));

GuildWarMgr mgr = GuildWarMgr.getInstance();
for (RefGuildWarCenter ref : RefDataMgr.getAll(RefGuildWarCenter.class).values()) {
mgr.guildWarCenterOwner.remove(Integer.valueOf(ref.id), guild);
((List)mgr.guildWarApplyer.get(Integer.valueOf(ref.id))).remove(guild);
((List)mgr.guildWarAtk.get(Integer.valueOf(ref.id))).remove(guild);
} 

RankManager.getInstance().clearGuildDataById(guildId);
}

public void rename(String oldname, String newname) {
Guild guild = this.guildDirName.remove(oldname);
if (guild != null) {
this.guildDirName.put(newname, guild);
}
}

public List<GuildApplyBO> getApplyByCid(long cid) {
List<GuildApplyBO> find = this.applysDirCid.get(Long.valueOf(cid));
if (find != null) {
return this.applysDirCid.get(Long.valueOf(cid));
}
return new ArrayList<>();
}

public void apply(long cid, long guildId) {
Guild guildL = this.guildDirId.get(Long.valueOf(guildId));
if (guildL == null) {
CommLog.warn("玩家[{}]企图加入一个不存在的公会[{}]", Long.valueOf(cid), Long.valueOf(guildId));
return;
} 
List<GuildApplyBO> applysCid = this.applysDirCid.get(Long.valueOf(cid));
if (applysCid == null) {
this.applysDirCid.put(Long.valueOf(cid), applysCid = new ArrayList<>());
}
GuildApplyBO find = null;
for (GuildApplyBO applyBo : applysCid) {
if (applyBo.getGuildId() == guildId) {
find = applyBo;
}
} 
if (find != null) {
find.saveApplyTime(CommTime.nowSecond());
} else {
GuildApplyBO newApply = new GuildApplyBO();
newApply.setPid(cid);
newApply.setGuildId(guildId);
newApply.setApplyTime(CommTime.nowSecond());
newApply.insert();
applysCid.add(newApply);
find = guildL.applyDirCid.get(Long.valueOf(cid));
if (find != null && find.getId() != newApply.getId()) {
find.del();
}
guildL.applyDirCid.put(Long.valueOf(cid), newApply);
} 
}

public void removeApply(long cid) {
List<GuildApplyBO> list = this.applysDirCid.remove(Long.valueOf(cid));
if (list == null) {
return;
}

for (GuildApplyBO applyBO : list) {
Guild guild = this.guildDirId.get(Long.valueOf(applyBO.getGuildId()));
if (guild != null) {
guild.applyDirCid.remove(Long.valueOf(applyBO.getPid()));
}
applyBO.del();
} 
list.clear();
}

public void removeApply(long cid, long guildid) {
Guild guild = this.guildDirId.get(Long.valueOf(guildid));
if (guild != null) {
guild.applyDirCid.remove(Long.valueOf(cid));
}

List<GuildApplyBO> list = this.applysDirCid.get(Long.valueOf(cid));
if (list != null) {
for (GuildApplyBO bo : list) {
if (bo.getGuildId() == guildid) {
bo.del();
}
} 
list.removeIf(x -> (x.getGuildId() == paramLong));
} 
}

public static void CheckNameValid(String name, long operator) throws WSException {
if (SensitiveWordMgr.getInstance().isContainsSensitiveWord(name)) {
throw new WSException(ErrorCode.Dirty_Word, "玩家[%s]帮会名[%s]使用敏感词", new Object[] { Long.valueOf(operator), name });
}
int maxLength = RefDataMgr.getFactor("Guild_NameMaxLength", 6);
if (name.length() > maxLength) {
throw new WSException(ErrorCode.Dirty_Word, "玩家[%s]帮会名[%s]超过最大长度[%s]", new Object[] { Long.valueOf(operator), name, Integer.valueOf(maxLength) });
}
int minLength = RefDataMgr.getFactor("Guild_NameMinLength", 2);
if (name.length() < minLength) {
throw new WSException(ErrorCode.Dirty_Word, "玩家[%s]帮会名[%s]超过最小长度[%s]", new Object[] { Long.valueOf(operator), name, Integer.valueOf(minLength) });
}
String tmp_name = "s" + ServerConfig.ServerID() + "." + name;
Guild duplicate = getInstance().getGuild(tmp_name);
if (duplicate != null) {
throw new WSException(ErrorCode.Guild_AlreadyExist, "此名称已被使用，请重新命名");
}
}

public static void CheckNoteValid(String notice, long operator) throws WSException {
if (SensitiveWordMgr.getInstance().isContainsSensitiveWord(notice)) {
throw new WSException(ErrorCode.Dirty_Word, "玩家[%s]帮会公告/宣言[%s]使用敏感词", new Object[] { Long.valueOf(operator), notice });
}
int maxLength = RefDataMgr.getFactor("Guild_NoticeMaxLength", 20);
if (notice.length() > maxLength) {
throw new WSException(ErrorCode.Dirty_Word, "玩家[%s]帮会公告/宣言[%s]超过最大长度[%s]", new Object[] { Long.valueOf(operator), notice, Integer.valueOf(maxLength) });
}
int minLength = RefDataMgr.getFactor("Guild_NoticeMinLength", 2);
if (notice.length() < minLength) {
throw new WSException(ErrorCode.Dirty_Word, "玩家[%s]帮会公告/宣言[%s]超过最小长度[%s]", new Object[] { Long.valueOf(operator), notice, Integer.valueOf(minLength) });
}
}

public void dailyEvent() {
List<Guild> guilds = new ArrayList<>(this.guildDirId.values());
for (Guild guild : guilds) {
try {
SyncTaskManager.task(() -> paramGuild.dailyEvent());

}
catch (Exception e) {

e.printStackTrace();
} 
} 
}

public void longnvEvent() {
List<Guild> guilds = new ArrayList<>(this.guildDirId.values());
for (Guild guild : guilds) {
try {
SyncTaskManager.task(() -> paramGuild.getOrCreateLongnv().Start());

}
catch (Exception e) {

e.printStackTrace();
} 
} 
}
}

