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

public class GuildMgr {
    private static GuildMgr instance = new GuildMgr();
    public Map<Long, Guild> guildDirId = Maps.newConcurrentHashMap();
    public Map<String, Guild> guildDirName = Maps.newConcurrentHashMap();
    public Map<Long, List<GuildApplyBO>> applysDirCid = Maps.newConcurrentHashMap();

    public static GuildMgr getInstance() {
        return instance;
    }

    public void init() {
        for (GuildBO guildBO : BM.getBM(GuildBO.class).findAll()) {
            if (!guildBO.getName().matches("s\\d+\\..+")) {
                guildBO.saveName("s" + ServerConfig.ServerID() + "." + guildBO.getName());
            }

            Guild guild = new Guild(guildBO);
            this.guildDirId.put(guildBO.getId(), guild);
            this.guildDirName.put(guildBO.getName(), guild);
            guild.init();
        }

        for (GuildApplyBO apply : BM.getBM(GuildApplyBO.class).findAll()) {
            Guild guild = this.guildDirId.get(apply.getGuildId());
            if (guild == null) {
                apply.del();
            } else {
                if (guild.applyDirCid.get(apply.getPid()) != null) {
                    guild.applyDirCid.remove(apply.getPid()).del();
                }

                guild.applyDirCid.put(apply.getPid(), apply);
                List<GuildApplyBO> playerApply = this.applysDirCid.get(apply.getPid());
                if (playerApply == null) {
                    this.applysDirCid.put(apply.getPid(), playerApply = new ArrayList<>());
                }

                playerApply.add(apply);
            }
        }

        for (GuildMemberBO memberBO : BM.getBM(GuildMemberBO.class).findAll()) {
            if (memberBO.getGuildId() != 0L && memberBO.getGuildId() != -1L) {
                Guild guild = this.guildDirId.get(memberBO.getGuildId());
                if (guild == null) {
                    memberBO.del();
                } else {
                    guild.members.add(memberBO.getPid());
                    GuildJob job = GuildJob.values()[memberBO.getJob()];
                    if (job == null || job == GuildJob.None) {
                        job = GuildJob.Member;
                        memberBO.saveJob(job.ordinal());
                    }

                    List<Long> jobMembers = guild.membersDirJob.get(job);
                    if (jobMembers == null) {
                        guild.membersDirJob.put(job, jobMembers = new ArrayList<>());
                    }

                    jobMembers.add(memberBO.getPid());
                    if (memberBO.getSacrificeStatus() != 0) {
                        guild.sacrificeMember.add(memberBO.getPid());
                    }
                }
            }
        }

        for (GuildwarpuppetBO puppet : BM.getBM(GuildwarpuppetBO.class).findAll()) {
            Guild guild = this.guildDirId.get(puppet.getGuildId());
            if (guild == null) {
                puppet.del();
            } else if (puppet.getApplyTime() < CommTime.getTodayZeroClockS()) {
                puppet.del();
            } else if (guild.puppets.get(puppet.getPid()) != null) {
                guild.puppets.get(puppet.getPid()).add(puppet);
            } else {
                List<GuildwarpuppetBO> list = new ArrayList<>();
                list.add(puppet);
                guild.puppets.put(puppet.getPid(), list);
            }
        }

        for (GuildBoardBO board : BM.getBM(GuildBoardBO.class).findAll()) {
            Guild guild = this.guildDirId.get(board.getGuildId());
            if (guild == null) {
                board.del();
            } else {
                guild.boards.add(board);
            }
        }

        List<GuildLogBO> logs = BM.getBM(GuildLogBO.class).findAll();
        logs.sort((left, right) -> left.getTime() - right.getTime());

        for (GuildLogBO log : logs) {
            Guild guild = this.guildDirId.get(log.getGuildId());
            if (guild == null) {
                log.del();
            } else {
                guild.logs.add(log);
            }
        }
    }

    public List<Guild> getAllGuild() {
        return new ArrayList<>(this.guildDirId.values());
    }

    public Guild getGuild(String name) {
        Guild guild = this.guildDirName.get(name);
        return guild != null ? guild : null;
    }

    public Guild getGuild(long guildid) {
        Guild guild = this.guildDirId.get(guildid);
        return guild != null ? guild : null;
    }

    public Guild getGuildData(long guildId) {
        return this.guildDirId.get(guildId);
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
        guild.members.add(player.getPid());
        guild.membersDirJob.put(GuildJob.President, new ArrayList<>());
        guild.membersDirJob.get(GuildJob.President).add(player.getPid());
        this.guildDirId.put(bo.getId(), guild);
        this.guildDirName.put(bo.getName(), guild);
        GuildMemberFeature guildMember = player.getFeature(GuildMemberFeature.class);
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
        Guild guild = this.guildDirId.get(guildId);
        if (guild != null) {
            this.guildDirName.remove(guild.bo.getName());
            PlayerMgr playerMgr = PlayerMgr.getInstance();
            new ArrayList<>(guild.members).forEach(x -> playerMgr.getPlayer(x).getFeature(GuildMemberFeature.class).leave());
            guild.membersDirJob.clear();
            guild.applyDirCid.values().forEach(x -> {
                this.applysDirCid.get(x.getPid()).remove(x);
                x.del();
            });
            guild.applyDirCid.clear();
            guild.boards.forEach(x -> x.del());
            guild.boards.clear();
            ChatMgr.getInstance().cleanGuildMessage(guild.bo.getId());
            guild.bo.del();
            this.guildDirId.remove(guildId);
            GuildWarMgr mgr = GuildWarMgr.getInstance();

            for (RefGuildWarCenter ref : RefDataMgr.getAll(RefGuildWarCenter.class).values()) {
                mgr.guildWarCenterOwner.remove(ref.id, guild);
                mgr.guildWarApplyer.get(ref.id).remove(guild);
                mgr.guildWarAtk.get(ref.id).remove(guild);
            }

            RankManager.getInstance().clearGuildDataById(guildId);
        }
    }

    public void rename(String oldname, String newname) {
        Guild guild = this.guildDirName.remove(oldname);
        if (guild != null) {
            this.guildDirName.put(newname, guild);
        }
    }

    public List<GuildApplyBO> getApplyByCid(long cid) {
        List<GuildApplyBO> find = this.applysDirCid.get(cid);
        return (List<GuildApplyBO>)(find != null ? this.applysDirCid.get(cid) : new ArrayList<>());
    }

    public void apply(long cid, long guildId) {
        Guild guildL = this.guildDirId.get(guildId);
        if (guildL == null) {
            CommLog.warn("玩家[{}]企图加入一个不存在的公会[{}]", cid, guildId);
        } else {
            List<GuildApplyBO> applysCid = this.applysDirCid.get(cid);
            if (applysCid == null) {
                this.applysDirCid.put(cid, applysCid = new ArrayList<>());
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
                find = guildL.applyDirCid.get(cid);
                if (find != null && find.getId() != newApply.getId()) {
                    find.del();
                }

                guildL.applyDirCid.put(cid, newApply);
            }
        }
    }

    public void removeApply(long cid) {
        List<GuildApplyBO> list = this.applysDirCid.remove(cid);
        if (list != null) {
            for (GuildApplyBO applyBO : list) {
                Guild guild = this.guildDirId.get(applyBO.getGuildId());
                if (guild != null) {
                    guild.applyDirCid.remove(applyBO.getPid());
                }

                applyBO.del();
            }

            list.clear();
        }
    }

    public void removeApply(long cid, long guildid) {
        Guild guild = this.guildDirId.get(guildid);
        if (guild != null) {
            guild.applyDirCid.remove(cid);
        }

        List<GuildApplyBO> list = this.applysDirCid.get(cid);
        if (list != null) {
            for (GuildApplyBO bo : list) {
                if (bo.getGuildId() == guildid) {
                    bo.del();
                }
            }

            list.removeIf(x -> x.getGuildId() == guildid);
        }
    }

    public static void CheckNameValid(String name, long operator) throws WSException {
        if (SensitiveWordMgr.getInstance().isContainsSensitiveWord(name)) {
            throw new WSException(ErrorCode.Dirty_Word, "玩家[%s]帮会名[%s]使用敏感词", operator, name);
        } else {
            int maxLength = RefDataMgr.getFactor("Guild_NameMaxLength", 6);
            if (name.length() > maxLength) {
                throw new WSException(ErrorCode.Dirty_Word, "玩家[%s]帮会名[%s]超过最大长度[%s]", operator, name, maxLength);
            } else {
                int minLength = RefDataMgr.getFactor("Guild_NameMinLength", 2);
                if (name.length() < minLength) {
                    throw new WSException(ErrorCode.Dirty_Word, "玩家[%s]帮会名[%s]超过最小长度[%s]", operator, name, minLength);
                } else {
                    String tmp_name = "s" + ServerConfig.ServerID() + "." + name;
                    Guild duplicate = getInstance().getGuild(tmp_name);
                    if (duplicate != null) {
                        throw new WSException(ErrorCode.Guild_AlreadyExist, "此名称已被使用，请重新命名");
                    }
                }
            }
        }
    }

    public static void CheckNoteValid(String notice, long operator) throws WSException {
        if (SensitiveWordMgr.getInstance().isContainsSensitiveWord(notice)) {
            throw new WSException(ErrorCode.Dirty_Word, "玩家[%s]帮会公告/宣言[%s]使用敏感词", operator, notice);
        } else {
            int maxLength = RefDataMgr.getFactor("Guild_NoticeMaxLength", 20);
            if (notice.length() > maxLength) {
                throw new WSException(ErrorCode.Dirty_Word, "玩家[%s]帮会公告/宣言[%s]超过最大长度[%s]", operator, notice, maxLength);
            } else {
                int minLength = RefDataMgr.getFactor("Guild_NoticeMinLength", 2);
                if (notice.length() < minLength) {
                    throw new WSException(ErrorCode.Dirty_Word, "玩家[%s]帮会公告/宣言[%s]超过最小长度[%s]", operator, notice, minLength);
                }
            }
        }
    }

    public void dailyEvent() {
        for (Guild guild : new ArrayList<>(this.guildDirId.values())) {
            try {
                SyncTaskManager.task(() -> guild.dailyEvent());
            } catch (Exception var5) {
                var5.printStackTrace();
            }
        }
    }

    public void longnvEvent() {
        for (Guild guild : new ArrayList<>(this.guildDirId.values())) {
            try {
                SyncTaskManager.task(() -> guild.getOrCreateLongnv().Start());
            } catch (Exception var5) {
                var5.printStackTrace();
            }
        }
    }
}
