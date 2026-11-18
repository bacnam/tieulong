package business.player.feature.guild;

import business.global.guild.Guild;
import business.global.guild.GuildConfig;
import business.global.guild.GuildMgr;
import business.global.rank.RankManager;
import business.player.Player;
import business.player.feature.Feature;
import business.player.feature.PlayerBase;
import business.player.feature.PlayerItem;
import business.player.feature.character.CharFeature;
import business.player.feature.features.PlayerRecord;
import business.player.feature.player.TitleFeature;
import business.player.item.Reward;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.enums.GuildJob;
import com.zhonglian.server.common.enums.GuildRankType;
import com.zhonglian.server.common.enums.RankType;
import com.zhonglian.server.common.enums.Title;
import com.zhonglian.server.common.enums.ConstEnum.BuffType;
import com.zhonglian.server.common.enums.ConstEnum.DailyRefresh;
import com.zhonglian.server.common.enums.ConstEnum.ResOpType;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefGuildBoss;
import core.config.refdata.ref.RefGuildBossDamageReward;
import core.config.refdata.ref.RefGuildJobInfo;
import core.config.refdata.ref.RefGuildSkill;
import core.config.refdata.ref.RefGuildSkillLevel;
import core.database.game.bo.GuildBossBO;
import core.database.game.bo.GuildBossChallengeBO;
import core.database.game.bo.GuildMemberBO;
import core.database.game.bo.GuildMemberSkillBO;
import core.database.game.bo.GuildTaskLogBO;
import core.logger.flow.FlowLogger;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.stream.Collectors;

public class GuildMemberFeature extends Feature {
    public GuildMemberBO bo;
    public List<GuildTaskLogBO> tasklogs;
    public List<GuildMemberSkillBO> memberSkills;
    private int lastBoardTime;
    private GuildBossChallengeBO challenge;
    private long GuildBossDamage;
    private Queue<Long> lasttime = new LinkedList<>();
    private double maxdamage = 0.0;
    private static int fight_interval = 300;

    public GuildMemberFeature(Player owner) {
        super(owner);
    }

    @Override
    public void loadDB() {
        this.bo = BM.getBM(GuildMemberBO.class).findOne("pid", this.player.getPid());
        this.tasklogs = BM.getBM(GuildTaskLogBO.class).findAll("pid", this.player.getPid());
        this.memberSkills = BM.getBM(GuildMemberSkillBO.class).findAll("pid", this.player.getPid());
        this.challenge = BM.getBM(GuildBossChallengeBO.class).findOne("pid", this.player.getPid());
    }

    public GuildMemberBO getOrCreate() {
        if (this.bo == null) {
            this.bo = new GuildMemberBO();
            this.bo.savePid(this.player.getPid());
            this.bo.insert();
        }

        return this.bo;
    }

    public GuildBossChallengeBO getOrCreateChalleng() {
        GuildBossChallengeBO bo = this.challenge;
        if (bo != null) {
            return bo;
        } else {
            synchronized (this) {
                bo = this.challenge;
                if (bo != null) {
                    return bo;
                } else {
                    bo = new GuildBossChallengeBO();
                    bo.setPid(this.player.getPid());
                    bo.setChallengeMaxTimes(GuildConfig.getGuildBossMaxTime());
                    bo.insert();
                    this.challenge = bo;
                    return bo;
                }
            }
        }
    }

    public GuildMemberBO getBo() {
        return this.bo;
    }

    public boolean checkDonate(int amount) {
        return amount > 0 && this.bo != null ? this.bo.getDonate() >= amount : false;
    }

    public void consumeDonate(int donate, ItemFlow reason) {
        this.lock();
        if (donate > 0) {
            GuildMemberBO bo = this.getOrCreate();
            int before = bo.getDonate();
            int finalDonate = Math.max(before - donate, 0);
            bo.saveDonate(finalDonate);
            this.unlock();
            this.player.pushProto("guildDonate", finalDonate);
            FlowLogger.guildDonateChargeLog(
                this.player.getPid(), this.player.getVipLevel(), this.player.getLv(), reason.value(), -donate, finalDonate, before, ResOpType.Lose.ordinal()
            );
        }
    }

    public int gainDonate(int donate, ItemFlow reason) {
        this.lock();
        if (donate <= 0) {
            return 0;
        } else {
            GuildMemberBO bo = this.getOrCreate();
            int before = bo.getDonate();
            int finalDonate = before + donate;
            bo.saveDonate(finalDonate);
            bo.saveWeekDonate(bo.getWeekDonate() + donate);
            bo.saveHistoryDonate(bo.getHistoryDonate() + donate);
            this.unlock();
            this.player.pushProto("guildDonate", finalDonate);
            FlowLogger.guildDonateChargeLog(
                this.player.getPid(), this.player.getVipLevel(), this.player.getLv(), reason.value(), donate, finalDonate, before, ResOpType.Gain.ordinal()
            );
            return donate;
        }
    }

    public long getGuildID() {
        return this.bo != null ? this.bo.getGuildId() : 0L;
    }

    public Guild getGuild() {
        return this.bo != null && this.bo.getGuildId() != 0L ? GuildMgr.getInstance().getGuild(this.bo.getGuildId()) : null;
    }

    public void leave() {
        this.getGuild().removeMember(this.player);
        this.bo.setGuildId(0L);
        this.bo.setWeekDonate(0);
        this.bo.setLastLeaveTime(CommTime.nowSecond());
        this.bo.setJob(GuildJob.None.ordinal());
        this.bo.saveAll();
        this.player.getFeature(CharFeature.class).updateCharPower();
        this.player.notify2Zone();
    }

    public int getDonate() {
        return this.bo == null ? 0 : this.bo.getDonate();
    }

    public int getWeekDonate() {
        return this.bo == null ? 0 : this.bo.getWeekDonate();
    }

    public GuildJob getJob() {
        return this.bo == null ? GuildJob.None : GuildJob.values()[this.bo.getJob()];
    }

    public void setJob(GuildJob job) {
        this.bo.saveJob(job.ordinal());
        this.getGuild().getMember(job).add(this.player.getPid());
    }

    public RefGuildJobInfo getJobRef() {
        return RefDataMgr.get(RefGuildJobInfo.class, this.getJob());
    }

    public String getJobName() {
        return this.getJobRef().JobName;
    }

    public int getJoinCD() {
        return this.bo == null ? 0 : this.bo.getLastLeaveTime() + GuildConfig.JoinCD() - CommTime.nowSecond();
    }

    public int getFuncCD() {
        if (this.bo == null) {
            return 0;
        } else if (this.bo.getJob() == GuildJob.President.ordinal()) {
            return 0;
        } else {
            return this.bo.getLastLeaveTime() == 0 ? 0 : this.bo.getJoinTime() + GuildConfig.FuncCD() - CommTime.nowSecond();
        }
    }

    public int getSacrificeStatu() {
        return this.bo.getSacrificeStatus();
    }

    public int getSacrificeLeftTimes() {
        return RefDataMgr.getFactor("SacrificeTimes", 5) - this.bo.getSacrificeStatus();
    }

    public void setSacrificed() {
        this.bo.saveSacrificeStatus(this.bo.getSacrificeStatus() + 1);
    }

    public void gainSacriDonate(int donate) {
        this.bo.saveSacrificeDonate(this.bo.getSacrificeDonate() + donate);
    }

    public int getLastBoardTime() {
        return this.lastBoardTime;
    }

    public void setLastBoardTime(int nowTimeSecond) {
        this.lastBoardTime = nowTimeSecond;
    }

    public List<GuildMemberSkillBO> getMemberSkillList() {
        return this.memberSkills;
    }

    public GuildMemberSkillBO getMemberSkill(int skillId) {
        Optional<GuildMemberSkillBO> optional = this.memberSkills.stream().filter(b -> b.getSkillId() == skillId).findFirst();
        return optional.isPresent() ? optional.get() : null;
    }

    public int getMemberSkillValue(BuffType buffType) {
        if (this.getGuild() == null) {
            return 0;
        } else {
            RefGuildSkill ref = RefGuildSkill.getGuildSkillRef(buffType);
            if (ref == null) {
                return 0;
            } else {
                GuildMemberSkillBO bo = this.getMemberSkill(ref.id);
                return bo != null && bo.getLevel() != 0 ? ref.getSkillValue(bo.getLevel()) : 0;
            }
        }
    }

    public int getSkillPointsLeft() {
        return RefDataMgr.getFactor("guildSkillPoints", 10) - this.bo.getSkillpoints();
    }

    public void setSkillPoints(int value) {
        this.bo.saveSkillpoints(value);
    }

    public void memberSkillUpgrade(int skillId) throws WSException {
        int maxLevel = 0;
        maxLevel = GuildConfig.getSkillMaxLevel(skillId, this.getGuild().getLevel());
        if (maxLevel == 0) {
            throw new WSException(ErrorCode.Guild_UpgradeSkillIsLock, "该技能尚未解锁，无法学习");
        } else {
            GuildMemberSkillBO bo = this.getMemberSkill(skillId);
            if (bo == null) {
                bo = new GuildMemberSkillBO();
                bo.setPid(this.player.getPid());
                bo.setSkillId(skillId);
                bo.setCreateTime(CommTime.nowSecond());
                bo.insert();
                this.memberSkills.add(bo);
            }

            if (bo.getLevel() >= maxLevel) {
                throw new WSException(ErrorCode.Guild_SkillUpgradeFull, "帮会技能已达到当前满级，无需学习");
            } else {
                RefGuildSkillLevel skillLevelRef = GuildConfig.getGuildSkillLevel(skillId, bo.getLevel());
                if (skillLevelRef == null) {
                    throw new WSException(ErrorCode.Guild_SkillUpgradeFull, "帮会技能已满");
                } else if (!this.player
                    .getFeature(PlayerItem.class)
                    .checkAndConsume(skillLevelRef.CostItemList, skillLevelRef.UpgradeCostList, ItemFlow.Guild_SkillUpgrade)) {
                    throw new WSException(ErrorCode.NotEnough_Money, "资源不足");
                } else {
                    bo.saveLevel(bo.getLevel() + 1);
                    this.player.getFeature(CharFeature.class).updateCharPower();
                }
            }
        }
    }

    public void dailyEvent() {
        try {
            if (this.bo == null) {
                return;
            }

            this.bo.setSacrificeStatus(0);
            this.bo.setSacrificeDonate(0);
            this.getOrCreateChalleng().setTotalDamageAll(0L);
            this.getOrCreateChalleng().setAttackTimes(0);
            this.getOrCreateChalleng().setChallengeMaxTimes(GuildConfig.getGuildBossMaxTime());
            this.getOrCreateChalleng().setChallengeTimes(0);
            this.getOrCreateChalleng().setChallengeBuyTimes(0);
            this.getOrCreateChalleng().saveAll();
            if (this.getGuild() != null) {
                int rank = RankManager.getInstance().getRank(RankType.Guild, this.getGuildID());
                if (this.getJob() == GuildJob.President) {
                    this.player.getFeature(TitleFeature.class).updateMin(Title.RankGuildTopLeader, rank);
                } else {
                    this.player.getFeature(TitleFeature.class).updateMin(Title.RankGuildTopMember, rank);
                }
            }
        } catch (Exception var2) {
            var2.printStackTrace();
        }
    }

    public void weeklyEvent() {
        this.player.lockIns();
        this.bo.saveWeekDonate(0);
        this.player.unlockIns();
    }

    public core.network.proto.Guild.member memberInfo() {
        if (this.bo == null) {
            return null;
        } else {
            core.network.proto.Guild.member builder = new core.network.proto.Guild.member();
            builder.setRole(this.player.getFeature(PlayerBase.class).summary());
            builder.setJob(GuildJob.values()[this.bo.getJob()]);
            builder.setGuildDonate(this.getDonate());
            builder.setSacrificeDonate(this.bo.getSacrificeDonate());
            builder.setOnline(this.player.getClientSession() != null);
            builder.setLastOnlineTime(this.player.getPlayerBO().getLastLogout());
            builder.setJoinTime(this.bo.getJoinTime());
            builder.setSkillUpLeftTimes(this.getSkillPointsLeft());
            builder.setSacrificeLeftTimes(this.getSacrificeLeftTimes());
            PlayerRecord recorder = this.player.getFeature(PlayerRecord.class);
            int times = recorder.getValue(DailyRefresh.GuildSacrifice);
            builder.setSacrificeCrystal(times);
            return builder;
        }
    }

    public List<core.network.proto.Guild.GuildSkill> getGuildSkillList() {
        return this.memberSkills.stream().map(x -> {
            core.network.proto.Guild.GuildSkill builder = new core.network.proto.Guild.GuildSkill();
            builder.setSkillid(x.getSkillId());
            builder.setLevel(x.getLevel());
            return builder;
        }).collect(Collectors.toList());
    }

    public int nextRefreshTime() {
        int time = CommTime.getTodayZeroClockS();

        for (int i = 0; i < 12; i++) {
            time += 7200;
            if (time > CommTime.nowSecond()) {
                return time;
            }
        }

        return CommTime.getTodayZeroClockS() + 86400;
    }

    public GuildBossChallengeBO beginFightGuildBoss(int bossId) throws WSException {
        Guild guild = this.getGuild();
        if (!guild.isInOpenHour()) {
            throw new WSException(ErrorCode.GuildBoss_NotOpen, "帮派boss尚未开放，无法挑战");
        } else {
            GuildBossBO boss = guild.getboss(bossId);
            if (boss == null || boss.getIsDead()) {
                throw new WSException(ErrorCode.GuildBoss_IsDeath, "当前世界Boss已死，无法挑战");
            } else if (!boss.getIsOpen()) {
                throw new WSException(ErrorCode.GuildBoss_NotOpen, "帮派boss尚未开放，无法挑战");
            } else {
                GuildBossChallengeBO bo = this.challenge;
                int challengeTimes = bo.getChallengeTimes();
                if (challengeTimes >= bo.getChallengeMaxTimes()) {
                    throw new WSException(ErrorCode.GuildBoss_NotChallengeTimes, "玩家的挑战次数已用完");
                } else {
                    bo.setChallengeTimes(bo.getChallengeTimes() + 1);
                    guild.doEnterGuildBoss(this.player, boss);
                    bo.setBeginFightTime(CommTime.nowSecond());
                    bo.setAttackTimes(bo.getAttackTimes() + 1);
                    bo.saveAll();
                    this.GuildBossDamage = 0L;
                    RefGuildBoss ref = RefDataMgr.get(RefGuildBoss.class, boss.getBossId());
                    this.maxdamage = this.player.getFeature(CharFeature.class).getMaxDamage(ref.MonsterId);
                    return bo;
                }
            }
        }
    }

    public boolean hurtWorldBoss(int charId, int bossId, int damage) throws WSException {
        if (!this.getGuild().isInOpenHour()) {
            throw new WSException(ErrorCode.GuildBoss_NotOpen, "Boss未开启");
        } else {
            long now = CommTime.nowMS();
            if (this.lasttime.size() >= this.player.getFeature(CharFeature.class).size()) {
                long last = this.lasttime.poll() + fight_interval;
                if (now < last) {
                    throw new WSException(ErrorCode.Fight_CheckFailed, "伤害CD中");
                }
            }

            this.lasttime.add(now);
            if (damage > 100 && damage > this.maxdamage) {
                throw new WSException(ErrorCode.Fight_CheckFailed, "伤害异常");
            } else {
                this.getGuild().doHurtGuildBoss(this.player, damage, bossId);
                this.updateChallengeDamage(this.challenge, damage, bossId);
                return false;
            }
        }
    }

    public void updateChallengeDamage(GuildBossChallengeBO bo, int damage, int bossId) {
        this.GuildBossDamage += damage;
        long totalDamage = bo.getTotalDamage(bossId - 1) + damage;
        if (totalDamage > this.getGuild().getboss(bossId).getBossMaxHp()) {
            bo.saveTotalDamage(bossId - 1, this.getGuild().getboss(bossId).getBossMaxHp());
        } else {
            bo.saveTotalDamage(bossId - 1, totalDamage);
        }

        long alltotalDamage = 0L;

        for (long damage1 : bo.getTotalDamageAll()) {
            alltotalDamage += damage1;
        }

        this.getGuild().update(GuildRankType.GuildBoss, this.player.getPid(), alltotalDamage);
    }

    public Reward LeaveGuildBoss(int bossId) throws WSException {
        this.getGuild().doLeaveGuildBoss(this.player, bossId);
        this.challenge.saveLeaveFightTime(CommTime.nowSecond());
        this.GuildBossDamage = Math.min(this.GuildBossDamage, this.getGuild().getboss(bossId).getBossMaxHp());
        int result = (int)(this.GuildBossDamage * 10000L / this.getGuild().getboss(bossId).getBossMaxHp());
        Reward reward = RefGuildBossDamageReward.getReward(result);
        this.player.getFeature(PlayerItem.class).gain(reward, ItemFlow.Guild_Boss);
        return reward;
    }

    public long getOnceDamage() {
        return this.GuildBossDamage;
    }

    public void refreshChallengeTimes() {
        try {
            this.getOrCreateChalleng().saveChallengeMaxTimes(this.getOrCreateChalleng().getChallengeMaxTimes() + 1);
        } catch (Exception var2) {
            var2.printStackTrace();
        }
    }
}
