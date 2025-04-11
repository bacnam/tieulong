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
import com.zhonglian.server.common.enums.*;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.*;
import core.database.game.bo.*;
import core.logger.flow.FlowLogger;

import java.util.*;
import java.util.stream.Collectors;

public class GuildMemberFeature
        extends Feature {
    private static int fight_interval = 300;
    public GuildMemberBO bo;
    public List<GuildTaskLogBO> tasklogs;
    public List<GuildMemberSkillBO> memberSkills;
    private int lastBoardTime;
    private GuildBossChallengeBO challenge;
    private long GuildBossDamage;
    private Queue<Long> lasttime;
    private double maxdamage;

    public GuildMemberFeature(Player owner) {
        super(owner);

        this.lasttime = new LinkedList<>();
        this.maxdamage = 0.0D;
    }

    public void loadDB() {
        this.bo = (GuildMemberBO) BM.getBM(GuildMemberBO.class).findOne("pid", Long.valueOf(this.player.getPid()));
        this.tasklogs = BM.getBM(GuildTaskLogBO.class).findAll("pid", Long.valueOf(this.player.getPid()));
        this.memberSkills = BM.getBM(GuildMemberSkillBO.class).findAll("pid", Long.valueOf(this.player.getPid()));
        this.challenge = (GuildBossChallengeBO) BM.getBM(GuildBossChallengeBO.class).findOne("pid", Long.valueOf(this.player.getPid()));
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
        if (bo != null) return bo;
        synchronized (this) {
            bo = this.challenge;
            if (bo != null) return bo;
            bo = new GuildBossChallengeBO();
            bo.setPid(this.player.getPid());
            bo.setChallengeMaxTimes(GuildConfig.getGuildBossMaxTime());
            bo.insert();
            this.challenge = bo;
        }
        return bo;
    }

    public GuildMemberBO getBo() {
        return this.bo;
    }

    public boolean checkDonate(int amount) {
        if (amount <= 0 || this.bo == null) return false;
        return (this.bo.getDonate() >= amount);
    }

    public void consumeDonate(int donate, ItemFlow reason) {
        lock();
        if (donate <= 0) return;
        GuildMemberBO bo = getOrCreate();
        int before = bo.getDonate();
        int finalDonate = Math.max(before - donate, 0);
        bo.saveDonate(finalDonate);
        unlock();
        this.player.pushProto("guildDonate", Integer.valueOf(finalDonate));
        FlowLogger.guildDonateChargeLog(this.player.getPid(), this.player.getVipLevel(), this.player.getLv(), reason.value(), -donate, finalDonate, before, ConstEnum.ResOpType.Lose.ordinal());
    }

    public int gainDonate(int donate, ItemFlow reason) {
        lock();
        if (donate <= 0) return 0;
        GuildMemberBO bo = getOrCreate();
        int before = bo.getDonate();
        int finalDonate = before + donate;
        bo.saveDonate(finalDonate);
        bo.saveWeekDonate(bo.getWeekDonate() + donate);
        bo.saveHistoryDonate(bo.getHistoryDonate() + donate);
        unlock();
        this.player.pushProto("guildDonate", Integer.valueOf(finalDonate));
        FlowLogger.guildDonateChargeLog(this.player.getPid(), this.player.getVipLevel(), this.player.getLv(), reason.value(), donate, finalDonate, before, ConstEnum.ResOpType.Gain.ordinal());
        return donate;
    }

    public long getGuildID() {
        if (this.bo != null) return this.bo.getGuildId();
        return 0L;
    }

    public Guild getGuild() {
        if (this.bo != null && this.bo.getGuildId() != 0L) return GuildMgr.getInstance().getGuild(this.bo.getGuildId());
        return null;
    }

    public void leave() {
        getGuild().removeMember(this.player);
        this.bo.setGuildId(0L);
        this.bo.setWeekDonate(0);
        this.bo.setLastLeaveTime(CommTime.nowSecond());
        this.bo.setJob(GuildJob.None.ordinal());
        this.bo.saveAll();
        ((CharFeature) this.player.getFeature(CharFeature.class)).updateCharPower();
        this.player.notify2Zone();
    }

    public int getDonate() {
        if (this.bo == null) return 0;
        return this.bo.getDonate();
    }

    public int getWeekDonate() {
        if (this.bo == null) return 0;
        return this.bo.getWeekDonate();
    }

    public GuildJob getJob() {
        if (this.bo == null) return GuildJob.None;
        return GuildJob.values()[this.bo.getJob()];
    }

    public void setJob(GuildJob job) {
        this.bo.saveJob(job.ordinal());
        getGuild().getMember(job).add(Long.valueOf(this.player.getPid()));
    }

    public RefGuildJobInfo getJobRef() {
        return (RefGuildJobInfo) RefDataMgr.get(RefGuildJobInfo.class, getJob());
    }

    public String getJobName() {
        return (getJobRef()).JobName;
    }

    public int getJoinCD() {
        if (this.bo == null) return 0;
        return this.bo.getLastLeaveTime() + GuildConfig.JoinCD() - CommTime.nowSecond();
    }

    public int getFuncCD() {
        if (this.bo == null) return 0;
        if (this.bo.getJob() == GuildJob.President.ordinal()) return 0;
        if (this.bo.getLastLeaveTime() == 0) return 0;
        return this.bo.getJoinTime() + GuildConfig.FuncCD() - CommTime.nowSecond();
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
        Optional<GuildMemberSkillBO> optional = this.memberSkills.stream().filter(b -> (b.getSkillId() == paramInt)).findFirst();
        if (optional.isPresent()) return optional.get();
        return null;
    }

    public int getMemberSkillValue(ConstEnum.BuffType buffType) {
        if (getGuild() == null) return 0;
        RefGuildSkill ref = RefGuildSkill.getGuildSkillRef(buffType);
        if (ref == null) return 0;
        GuildMemberSkillBO bo = getMemberSkill(ref.id);
        if (bo == null || bo.getLevel() == 0)
            return 0;
        return ref.getSkillValue(bo.getLevel());
    }

    public boolean hurtWorldBoss(int charId, int bossId, int damage) throws WSException {
        if (!getGuild().isInOpenHour()) {
            throw new WSException(ErrorCode.GuildBoss_NotOpen, "Boss未开启");
        }

        long now = CommTime.nowMS();
        if (this.lasttime.size() >= ((CharFeature) this.player.getFeature(CharFeature.class)).size()) {
            long last = ((Long) this.lasttime.poll()).longValue() + fight_interval;

            if (now < last) {
                throw new WSException(ErrorCode.Fight_CheckFailed, "伤害CD中");
            }
        }
        this.lasttime.add(Long.valueOf(now));
        if (damage > 100 && damage > this.maxdamage) {
            throw new WSException(ErrorCode.Fight_CheckFailed, "伤害异常");
        }

        getGuild().doHurtGuildBoss(this.player, damage, bossId);

        updateChallengeDamage(this.challenge, damage, bossId);
        return false;
    }

    public int getSkillPointsLeft() {
        return RefDataMgr.getFactor("guildSkillPoints", 10) - this.bo.getSkillpoints();
    }

    public void setSkillPoints(int value) {
        this.bo.saveSkillpoints(value);
    }

    public void memberSkillUpgrade(int skillId) throws WSException {
        int maxLevel = 0;
        maxLevel = GuildConfig.getSkillMaxLevel(skillId, getGuild().getLevel());
        if (maxLevel == 0)
            throw new WSException(ErrorCode.Guild_UpgradeSkillIsLock, "该技能尚未解锁，无法学习");
        GuildMemberSkillBO bo = getMemberSkill(skillId);
        if (bo == null) {
            bo = new GuildMemberSkillBO();
            bo.setPid(this.player.getPid());
            bo.setSkillId(skillId);
            bo.setCreateTime(CommTime.nowSecond());
            bo.insert();
            this.memberSkills.add(bo);
        }
        if (bo.getLevel() >= maxLevel)
            throw new WSException(ErrorCode.Guild_SkillUpgradeFull, "帮会技能已达到当前满级，无需学习");
        RefGuildSkillLevel skillLevelRef = GuildConfig.getGuildSkillLevel(skillId, bo.getLevel());
        if (skillLevelRef == null)
            throw new WSException(ErrorCode.Guild_SkillUpgradeFull, "帮会技能已满");
        if (!((PlayerItem) this.player.getFeature(PlayerItem.class)).checkAndConsume(skillLevelRef.CostItemList, skillLevelRef.UpgradeCostList, ItemFlow.Guild_SkillUpgrade))
            throw new WSException(ErrorCode.NotEnough_Money, "资源不足");
        bo.saveLevel(bo.getLevel() + 1);
        ((CharFeature) this.player.getFeature(CharFeature.class)).updateCharPower();
    }

    public void dailyEvent() {
        try {
            if (this.bo == null)
                return;
            this.bo.setSacrificeStatus(0);
            this.bo.setSacrificeDonate(0);
            getOrCreateChalleng().setTotalDamageAll(0L);
            getOrCreateChalleng().setAttackTimes(0);
            getOrCreateChalleng().setChallengeMaxTimes(GuildConfig.getGuildBossMaxTime());
            getOrCreateChalleng().setChallengeTimes(0);
            getOrCreateChalleng().setChallengeBuyTimes(0);
            getOrCreateChalleng().saveAll();
            if (getGuild() != null) {
                int rank = RankManager.getInstance().getRank(RankType.Guild, getGuildID());
                if (getJob() == GuildJob.President) {
                    ((TitleFeature) this.player.getFeature(TitleFeature.class)).updateMin(Title.RankGuildTopLeader, Integer.valueOf(rank));
                } else {
                    ((TitleFeature) this.player.getFeature(TitleFeature.class)).updateMin(Title.RankGuildTopMember, Integer.valueOf(rank));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateChallengeDamage(GuildBossChallengeBO bo, int damage, int bossId) {
        this.GuildBossDamage += damage;

        long totalDamage = bo.getTotalDamage(bossId - 1) + damage;
        if (totalDamage > getGuild().getboss(bossId).getBossMaxHp()) {
            bo.saveTotalDamage(bossId - 1, getGuild().getboss(bossId).getBossMaxHp());
        } else {
            bo.saveTotalDamage(bossId - 1, totalDamage);
        }
        long alltotalDamage = 0L;

        for (Iterator<Long> iterator = bo.getTotalDamageAll().iterator(); iterator.hasNext(); ) {
            long damage1 = ((Long) iterator.next()).longValue();
            alltotalDamage += damage1;
        }

        getGuild().update(GuildRankType.GuildBoss, this.player.getPid(), alltotalDamage);
    }

    public void weeklyEvent() {
        this.player.lockIns();
        this.bo.saveWeekDonate(0);
        this.player.unlockIns();
    }

    public Guild.member memberInfo() {
        if (this.bo == null)
            return null;
        Guild.member builder = new Guild.member();
        builder.setRole(((PlayerBase) this.player.getFeature(PlayerBase.class)).summary());
        builder.setJob(GuildJob.values()[this.bo.getJob()]);
        builder.setGuildDonate(getDonate());
        builder.setSacrificeDonate(this.bo.getSacrificeDonate());
        builder.setOnline((this.player.getClientSession() != null));
        builder.setLastOnlineTime(this.player.getPlayerBO().getLastLogout());
        builder.setJoinTime(this.bo.getJoinTime());
        builder.setSkillUpLeftTimes(getSkillPointsLeft());
        builder.setSacrificeLeftTimes(getSacrificeLeftTimes());
        PlayerRecord recorder = (PlayerRecord) this.player.getFeature(PlayerRecord.class);
        int times = recorder.getValue(ConstEnum.DailyRefresh.GuildSacrifice);
        builder.setSacrificeCrystal(times);
        return builder;
    }

    public List<Guild.GuildSkill> getGuildSkillList() {
        return (List<Guild.GuildSkill>) this.memberSkills.stream().map(x -> {
            Guild.GuildSkill builder = new Guild.GuildSkill();
            builder.setSkillid(x.getSkillId());
            builder.setLevel(x.getLevel());
            return builder;
        }).collect(Collectors.toList());
    }

    public int nextRefreshTime() {
        int time = CommTime.getTodayZeroClockS();
        for (int i = 0; i < 12; i++) {
            time += 7200;
            if (time > CommTime.nowSecond())
                return time;
        }
        return CommTime.getTodayZeroClockS() + 86400;
    }

    public GuildBossChallengeBO beginFightGuildBoss(int bossId) throws WSException {
        Guild guild = getGuild();
        if (!guild.isInOpenHour())
            throw new WSException(ErrorCode.GuildBoss_NotOpen, "帮派boss尚未开放，无法挑战");
        GuildBossBO boss = guild.getboss(bossId);
        if (boss == null || boss.getIsDead())
            throw new WSException(ErrorCode.GuildBoss_IsDeath, "当前世界Boss已死，无法挑战");
        if (!boss.getIsOpen())
            throw new WSException(ErrorCode.GuildBoss_NotOpen, "帮派boss尚未开放，无法挑战");
        GuildBossChallengeBO bo = this.challenge;
        int challengeTimes = bo.getChallengeTimes();
        if (challengeTimes >= bo.getChallengeMaxTimes())
            throw new WSException(ErrorCode.GuildBoss_NotChallengeTimes, "玩家的挑战次数已用完");
        bo.setChallengeTimes(bo.getChallengeTimes() + 1);
        guild.doEnterGuildBoss(this.player, boss);
        bo.setBeginFightTime(CommTime.nowSecond());
        bo.setAttackTimes(bo.getAttackTimes() + 1);
        bo.saveAll();
        this.GuildBossDamage = 0L;
        RefGuildBoss ref = (RefGuildBoss) RefDataMgr.get(RefGuildBoss.class, Integer.valueOf(boss.getBossId()));
        this.maxdamage = ((CharFeature) this.player.getFeature(CharFeature.class)).getMaxDamage(ref.MonsterId);
        return bo;
    }

    public Reward LeaveGuildBoss(int bossId) throws WSException {
        getGuild().doLeaveGuildBoss(this.player, bossId);
        this.challenge.saveLeaveFightTime(CommTime.nowSecond());
        this.GuildBossDamage = Math.min(this.GuildBossDamage, getGuild().getboss(bossId).getBossMaxHp());
        int result = (int) (this.GuildBossDamage * 10000L / getGuild().getboss(bossId).getBossMaxHp());
        Reward reward = RefGuildBossDamageReward.getReward(result);
        ((PlayerItem) this.player.getFeature(PlayerItem.class)).gain(reward, ItemFlow.Guild_Boss);
        return reward;
    }

    public long getOnceDamage() {
        return this.GuildBossDamage;
    }

    public void refreshChallengeTimes() {
        try {
            getOrCreateChalleng().saveChallengeMaxTimes(getOrCreateChalleng().getChallengeMaxTimes() + 1);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}

