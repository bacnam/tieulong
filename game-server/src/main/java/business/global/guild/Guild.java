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
import com.zhonglian.server.common.enums.GuildJob;
import com.zhonglian.server.common.enums.GuildRankType;
import com.zhonglian.server.common.enums.JoinState;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.common.enums.RankType;
import com.zhonglian.server.common.enums.ConstEnum.DailyRefresh;
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
import core.server.OpenSeverTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Map.Entry;

public class Guild {
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
    protected final BaseMutexManager m_mutex = new BaseMutexManager();
    private Integer power;
    private Map<GuildRankType, GuildRank> ranks = new HashMap<>();

    public void lock() {
        this.m_mutex.lock();
    }

    public void unlock() {
        this.m_mutex.unlock();
    }

    public Guild(GuildBO bo) {
        this.bo = bo;
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
        } else {
            synchronized (this) {
                longnv = this.longnv;
                if (longnv != null) {
                    return longnv;
                } else {
                    longnv = new LongnvWar(this);
                    this.longnv = longnv;
                    return longnv;
                }
            }
        }
    }

    public int gainExp(int guildExp) {
        RefGuildLevel nextLeveLinfo = RefDataMgr.get(RefGuildLevel.class, this.getLevel() + 1);
        if (nextLeveLinfo == null) {
            return 0;
        } else {
            this.bo.saveExp(this.bo.getExp() + guildExp);
            RefGuildLevel levelinfo = RefDataMgr.get(RefGuildLevel.class, this.getLevel());
            GuildBO bo = this.getBo();
            if (bo.getExp() < levelinfo.UpgradeValue) {
                return guildExp;
            } else {
                bo.setExp(bo.getExp() - levelinfo.UpgradeValue);
                bo.setLevel(bo.getLevel() + 1);
                if (RefDataMgr.get(RefGuildLevel.class, this.getLevel() + 1) == null) {
                    bo.setExp(0);
                }

                bo.saveAll();
                this.broadcast("guildLevelUp", new Guild.levelUpInfo(bo.getLevel(), bo.getExp()));
                return guildExp;
            }
        }
    }

    public int gainLongnvExp(int Exp) {
        RefLongnvLevel nextLeveLinfo = RefDataMgr.get(RefLongnvLevel.class, this.getLongnvLevel() + 1);
        if (nextLeveLinfo == null) {
            return 0;
        } else {
            this.bo.saveLnexp(this.bo.getLnexp() + Exp);
            RefLongnvLevel levelinfo = RefDataMgr.get(RefLongnvLevel.class, this.getLongnvLevel());
            GuildBO bo = this.getBo();
            if (bo.getLnexp() < levelinfo.UpgradeValue) {
                return Exp;
            } else {
                bo.setLnexp(bo.getLnexp() - levelinfo.UpgradeValue);
                bo.setLnlevel(bo.getLnlevel() + 1);
                if (RefDataMgr.get(RefLongnvLevel.class, this.getLongnvLevel() + 1) == null) {
                    bo.setLnexp(0);
                }

                bo.saveAll();
                this.broadcast("guildLongnvLevelUp", new Guild.levelUpInfo(bo.getLnlevel(), bo.getLnexp()));
                return Exp;
            }
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
        return this.getMember(job).size();
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
        return this.applyDirCid.get(cid);
    }

    public List<GuildBoardBO> getBoardList() {
        return this.boards;
    }

    public int getSacrificeCount() {
        return this.sacrificeMember.size();
    }

    public void incSacrificeCount(long cid) {
        this.sacrificeMember.add(cid);
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

        for (Long member : new ArrayList<>(this.members)) {
            if (cid != member) {
                mgr.getPlayer(member).pushProto(protoName, proto);
            }
        }
    }

    public void broadcast(String protoName, Object proto) {
        PlayerMgr mgr = PlayerMgr.getInstance();

        for (Long member : new ArrayList<>(this.members)) {
            mgr.getPlayer(member).pushProto(protoName, proto);
        }
    }

    public void broadcastMember(long... cids) {
        PlayerMgr playerMgr = PlayerMgr.getInstance();
        List<core.network.proto.Guild.member> members = Lists.newArrayList();

        for (long cid : cids) {
            if (this.members.contains(cid)) {
                members.add(playerMgr.getPlayer(cid).getFeature(GuildMemberFeature.class).memberInfo());
            }
        }

        this.broadcast("memberInfo", members);
    }

    public List<GuildMemberFeature> getAllMemberFeatures() {
        List<GuildMemberFeature> members = Lists.newArrayList();
        PlayerMgr playerMgr = PlayerMgr.getInstance();

        for (Long memberid : new ArrayList<>(this.members)) {
            members.add(playerMgr.getPlayer(memberid).getFeature(GuildMemberFeature.class));
        }

        return members;
    }

    public GuildMemberFeature getMember(long cid) {
        return !this.members.contains(cid) ? null : PlayerMgr.getInstance().getPlayer(cid).getFeature(GuildMemberFeature.class);
    }

    public void takeinMember(Player player) {
        this.members.add(player.getPid());
        this.getMember(GuildJob.Member).add(player.getPid());
        GuildMemberBO memberBO = player.getFeature(GuildMemberFeature.class).getOrCreate();
        memberBO.setGuildId(this.getGuildId());
        memberBO.setJob(GuildJob.Member.ordinal());
        memberBO.setJoinTime(CommTime.nowSecond());
        memberBO.saveAll();
        player.notify2Zone();
        GuildMgr.getInstance().removeApply(player.getPid());
        this.broadcastMember(player.getPid());
        player.pushProto("guildSkill", player.getFeature(GuildMemberFeature.class).getGuildSkillList());
        player.getFeature(CharFeature.class).updateCharPower();
        this.updatePower(player.getPlayerBO().getMaxFightPower());
        MailCenter.getInstance().sendMail(player.getPid(), GuildConfig.JoinGuildMailID(), this.bo.getName());
    }

    public void removeMember(Player player) {
        this.members.remove(player.getPid());
        this.membersDirJob.remove(player.getFeature(GuildMemberFeature.class).getJob());
        this.sacrificeMember.remove(player.getPid());
        this.updatePower(-player.getPlayerBO().getMaxFightPower());
        player.getFeature(CharFeature.class).updateCharPower();
    }

    public void addBoardMessage(GuildBoardBO bo) {
        int maxCount = GuildConfig.MaxBoardMessageCount();
        List<GuildBoardBO> bList = new ArrayList<>(this.boards);
        bList.add(bo);
        bList.sort((left, right) -> {
            if (left.getTop() == right.getTop()) {
                return right.getMessageTime() - left.getMessageTime();
            } else {
                return left.getTop() ? -1 : 1;
            }
        });

        for (int i = maxCount; i < bList.size(); i++) {
            bList.get(i).del();
        }

        this.boards = bList.subList(0, Math.min(bList.size(), maxCount));
    }

    public void dailyEvent() {
        List<GuildMemberFeature> members = this.getAllMemberFeatures();
        this.promotePresident(members);
        this.sacrificeMember.clear();

        for (GuildBossBO boss : this.guildboss) {
            boss.del();
        }

        this.guildboss.clear();
        this.getBo().saveGuildbossOpenNum(0);
        this.clear(GuildRankType.GuildBoss);
        this.guildwarCenter = null;
        if (CommTime.nowSecond() >= OpenSeverTime.getInstance().getOpenZeroTime() + RefDataMgr.getFactor("GuildwarOpenServerTime", 604800)) {
            for (Entry<Integer, Guild> entry : GuildWarMgr.getInstance().historyWinner.entrySet()) {
                RefGuildWarCenter ref = RefDataMgr.get(RefGuildWarCenter.class, entry.getKey());
                if (ref.isOpenTime() && entry.getValue() == this) {
                    GuildwarapplyBO bo = new GuildwarapplyBO();
                    bo.setGuildId(this.getGuildId());
                    bo.setCenterId(entry.getKey());
                    bo.setApplyTime(CommTime.nowSecond());
                    bo.insert();
                    this.guildwarCenter = bo;
                }
            }
        }

        this.puppets.clear();
    }

    private void promotePresident(List<GuildMemberFeature> members) {
        Long oldPresidentCid = this.membersDirJob.get(GuildJob.President).get(0);
        Player president = PlayerMgr.getInstance().getPlayer(oldPresidentCid);
        if (CommTime.nowSecond() >= president.getPlayerBO().getLastLogin() + 604800) {
            GuildMemberFeature newPresident = null;
            GuildMemberFeature oldPresident = null;

            for (int i = 0; i < members.size(); i++) {
                GuildMemberFeature m = members.get(i);
                if (oldPresidentCid == m.getPlayer().getPid()) {
                    oldPresident = m;
                } else if (newPresident == null) {
                    newPresident = m;
                } else if (m.getJob().ordinal() < newPresident.getJob().ordinal()) {
                    newPresident = m;
                } else if (m.getJob() == newPresident.getJob() && m.getDonate() > newPresident.getDonate()) {
                    newPresident = m;
                }
            }

            if (newPresident != null) {
                this.getMember(oldPresident.getJob()).remove(oldPresident.getPid());
                oldPresident.setJob(GuildJob.Member);
                this.getMember(newPresident.getJob()).remove(newPresident.getPid());
                newPresident.setJob(GuildJob.President);
                this.broadcastMember(oldPresident.getPlayer().getPid(), newPresident.getPlayer().getPid());
            }
        }
    }

    public core.network.proto.Guild.JoinInfo joinInfo() {
        core.network.proto.Guild.JoinInfo builder = new core.network.proto.Guild.JoinInfo();
        builder.setsId(this.bo.getId());
        builder.setLevel(this.bo.getLevel());
        builder.setMemberCnt(this.getMemberCnt());
        RefGuildLevel refGuildLevel = RefDataMgr.get(RefGuildLevel.class, this.bo.getLevel());
        builder.setMaxmemberCnt(refGuildLevel.MaxMemberAmount);
        builder.setIconId(this.bo.getIcon());
        builder.setBorderId(this.bo.getBorder());
        builder.setName(this.bo.getName());
        Long mster = this.getMember(GuildJob.President).get(0);
        builder.setMaster(PlayerMgr.getInstance().getPlayer(mster).getFeature(PlayerBase.class).summary());
        builder.setManifesto(this.bo.getManifesto());
        builder.setJoinState(this.getJoinState());
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
        builder.setJoinState(this.getJoinState());
        builder.setJoinLevel(this.bo.getLevelLimit());
        Long mster = this.getMember(GuildJob.President).get(0);
        builder.setMaster(PlayerMgr.getInstance().getPlayer(mster).getFeature(PlayerBase.class).summary());
        builder.setNowNum(this.getMemberCnt());
        RefGuildLevel refGuildLevel = RefDataMgr.get(RefGuildLevel.class, this.bo.getLevel());
        builder.setMaxNum(refGuildLevel.MaxMemberAmount);
        return builder;
    }

    private int calculatePower() {
        int power = 0;

        for (Long pid : this.getMembers()) {
            Player player = PlayerMgr.getInstance().getPlayer(pid);
            power += player.getFeature(CharFeature.class).getPower();
        }

        if (power > this.bo.getMaxFightPower()) {
            this.bo.saveMaxFightPower(power);
        }

        return power;
    }

    public int updatePower() {
        this.power = this.calculatePower();
        RankManager.getInstance().update(RankType.Guild, this.getGuildId(), this.power.intValue());

        for (long pid : this.members) {
            Player player = PlayerMgr.getInstance().getPlayer(pid);
            ActivityMgr.getActivity(RankGuild.class).UpdateMaxRequire_cost(player, this.power);
        }

        return this.power;
    }

    public int updatePower(int change) {
        if (this.power == null) {
            return this.getPower();
        } else {
            this.power = this.power + change;
            if (this.power > this.bo.getMaxFightPower()) {
                this.bo.saveMaxFightPower(this.power);
            }

            RankManager.getInstance().update(RankType.Guild, this.getGuildId(), this.power.intValue());

            for (long pid : this.members) {
                Player player = PlayerMgr.getInstance().getPlayer(pid);
                ActivityMgr.getActivity(RankGuild.class).UpdateMaxRequire_cost(player, this.power);
            }

            return this.power;
        }
    }

    public int getPower() {
        return this.power != null ? this.power : this.updatePower();
    }

    public void openBoss(RefGuildBoss refboss) {
        RefMonster ref = RefDataMgr.get(RefMonster.class, refboss.MonsterId);
        GuildBossBO bossBO = new GuildBossBO();
        bossBO.setBossId(refboss.id);
        bossBO.setGuildId(this.getGuildId());
        bossBO.setBossMaxHp(ref.MaxHP);
        bossBO.setBossHp(bossBO.getBossMaxHp());
        bossBO.setIsOpen(true);
        bossBO.insert();
        this.guildboss.add(bossBO);
        this.bo.saveGuildbossOpenNum(this.bo.getGuildbossOpenNum() + 1);
        this.broadcast("GuildBossOpen", bossBO);
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
        } else if (!players.contains(player)) {
            players.add(player);
            this.joinPlayers.put(Boss, players);
        }

        for (Player tmpPlayer : this.joinPlayers.get(Boss)) {
            if (tmpPlayer != null && tmpPlayer != player) {
                tmpPlayer.pushProto("newGuildBossPlayer", player.getFeature(PlayerBase.class).fightInfo());
            }
        }
    }

    public List<Player> getPlayerList(GuildBossBO boss) {
        return this.joinPlayers.get(boss);
    }

    public boolean doHurtGuildBoss(Player player, int damage, int bossId) throws WSException {
        boolean isKill = false;
        synchronized (this) {
            GuildBossBO bo = this.getboss(bossId);
            if (bo.getIsDead()) {
                throw new WSException(ErrorCode.GuildBoss_IsDeath, "Boss已被消灭");
            } else {
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
                        RefGuildBoss refBoss = RefDataMgr.get(RefGuildBoss.class, bossId);
                        MailCenter.getInstance().sendMail(mem, refBoss.MailId);
                    }

                    this.bo.saveGuildbossLevel(Math.max(this.bo.getGuildbossLevel(), bossId));
                    this.broadCastProtoToPlayers(bo);
                }

                return isKill;
            }
        }
    }

    public void broadCastProtoToPlayers(GuildBossBO boss) {
        this.broadcast("guildbossDead", boss);
    }

    public void doLeaveGuildBoss(Player player, int bossId) throws WSException {
        GuildBossBO boss = this.getboss(bossId);
        if (this.joinPlayers.get(boss) != null) {
            boolean remove = this.joinPlayers.get(boss).remove(player);
            if (!remove) {
                throw new WSException(ErrorCode.GuildBoss_NotEnter, "未进入帮派BOSS");
            }
        }
    }

    public void init() {
        this.guildboss = BM.getBM(GuildBossBO.class).findAll("guildId", this.getGuildId());

        for (Class<?> clz : CommClass.getClasses(GuildNormalRank.class.getPackage().getName())) {
            try {
                Class<? extends GuildRank> clazz = (Class<? extends GuildRank>)clz;
                GuildRanks types = clz.getAnnotation(GuildRanks.class);
                if (types == null || types.value().length == 0) {
                    CommLog.error("[{}]的类型没有相关排行榜类型，请检查", clz.getSimpleName());
                    System.exit(0);
                }

                GuildRankType[] var9;
                for (GuildRankType type : var9 = types.value()) {
                    if (this.ranks.containsKey(type)) {
                        String preClass = this.ranks.get(type).getClass().getSimpleName();
                        CommLog.error("[{},{}]重复定义[{}]类型的排行榜", preClass, clazz.getSimpleName());
                        System.exit(0);
                    }

                    GuildRank instance = clazz.getConstructor(GuildRankType.class).newInstance(type);
                    this.ranks.put(type, instance);
                }
            } catch (Exception var11) {
                CommLog.error("排行榜[{}]初始化失败", clz.getSimpleName(), var11);
                System.exit(0);
            }
        }

        for (GuildRankRecordBO recordBO : BM.getBM(GuildRankRecordBO.class).findAll("guildid", this.getGuildId())) {
            GuildRankType type = GuildRankType.values()[recordBO.getType()];
            GuildRank rank = this.ranks.get(type);
            GuildRecord record = new GuildRecord(recordBO);
            rank.map.put(recordBO.getOwner(), record);
        }

        for (GuildRank rank : this.ranks.values()) {
            rank.resort();
        }

        this.getOrCreateLongnv().Restart();
    }

    public List<GuildRecord> getRankList(GuildRankType type, int size) {
        List<GuildRecord> list = this.getRank(type).list;
        List<GuildRecord> records;
        if (list.size() > size + 1) {
            records = list.subList(0, size + 1);
        } else {
            records = new ArrayList<>(list);
        }

        return records;
    }

    public long getPlayerId(GuildRankType type, int rank) {
        List<GuildRecord> records = this.getRank(type).list;
        if (rank >= 1 && records.size() >= 2) {
            int num;
            if (records.size() - 1 < rank) {
                num = records.size() - 1;
            } else {
                num = rank;
            }

            return records.get(num).getPid();
        } else {
            return 0L;
        }
    }

    public int getRank(GuildRankType type, long ownerid) {
        return this.getRank(type).getRank(ownerid);
    }

    public long getValue(GuildRankType type, long ownerid) {
        return this.getRank(type).getValue(ownerid);
    }

    public int update(GuildRankType type, long ownerid, long value) {
        return this.getRank(type).update(ownerid, value);
    }

    public int update(GuildRankType type, long ownerid, long value, long... ext) {
        return this.getRank(type).update(ownerid, value, ext);
    }

    public int minus(GuildRankType type, long ownerid, int value) {
        return this.getRank(type).minus(ownerid, value);
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
        this.getRank(type).clear();
    }

    public void clearPlayerData(Player player) {
        BM.getBM(GuildRankRecordBO.class).delAll("owner", player.getPid());
        GuildRanks types = GuildNormalRank.class.getAnnotation(GuildRanks.class);

        GuildRankType[] var6;
        for (GuildRankType type : var6 = types.value()) {
            this.ranks.get(type).del(player.getPid());
        }
    }

    public void applyGuildWar(int centerId) throws WSException {
        RefGuildWarCenter ref = RefDataMgr.get(RefGuildWarCenter.class, centerId);
        if (CommTime.nowSecond() < OpenSeverTime.getInstance().getOpenZeroTime() + RefDataMgr.getFactor("GuildwarOpenServerTime", 604800)) {
            throw new WSException(ErrorCode.GuildWar_TimeLimit, "帮派战功能未开启");
        } else if (ref == null) {
            throw new WSException(ErrorCode.GuildWar_NotFoundCenter, "据点不存在");
        } else if (this.guildwarCenter != null) {
            throw new WSException(ErrorCode.GuildWar_AlreadyOpen, "帮派已报名");
        } else if (!ref.isOpenTime()) {
            throw new WSException(ErrorCode.GuildWar_NotOpen, "据点未开启");
        } else if (!GuildWarConfig.applyTime()) {
            throw new WSException(ErrorCode.GuildWar_ApplyNotOpen, "申请时间没到");
        } else if (this.getLevel() < RefDataMgr.getFactor("GuildWarApplyLevel", 2)) {
            throw new WSException(ErrorCode.GuildWar_LevelLimit, "帮派等级不足[%s]级，无法报名", RefDataMgr.getFactor("GuildWarApplyLevel", 2));
        } else {
            GuildWarMgr.getInstance().applyGuildWar(centerId, this);
            GuildwarapplyBO bo = new GuildwarapplyBO();
            bo.setGuildId(this.getGuildId());
            bo.setCenterId(centerId);
            bo.setApplyTime(CommTime.nowSecond());
            bo.insert();
            this.guildwarCenter = bo;
        }
    }

    public List<Player> getGuildWarPlayer() {
        List<Player> list = new ArrayList<>();
        this.getMembers().stream().forEach(mem -> list.add(PlayerMgr.getInstance().getPlayer(mem)));
        Collections.shuffle(list);
        return list;
    }

    public Guild.rebirth rebirth(Player player) throws WSException {
        if (this.guildwarCenter == null) {
            throw new WSException(ErrorCode.GuildWar_NotApply, "帮派未报名");
        } else {
            int centerId = GuildWarMgr.getInstance().isFighting(this);
            if (centerId != 0) {
                GuildWarMgr.getInstance().rebirth(player, centerId);
                PlayerRecord recorder = player.getFeature(PlayerRecord.class);
                return new Guild.rebirth(this.getTotalPuppet(), this.getPersonPuppet(player.getPid()), recorder.getValue(DailyRefresh.GuildWarRebirth));
            } else {
                PlayerCurrency currency = player.getFeature(PlayerCurrency.class);
                PlayerRecord recorder = player.getFeature(PlayerRecord.class);
                int curTimes = recorder.getValue(DailyRefresh.GuildWarRebirth);
                RefCrystalPrice prize = RefCrystalPrice.getPrize(curTimes);
                if (!currency.check(PrizeType.Crystal, prize.GuildWarRebirth)) {
                    throw new WSException(ErrorCode.NotEnough_Crystal, "玩家召唤傀儡需要钻石%s", prize.GuildWarRebirth);
                } else {
                    currency.consume(PrizeType.Crystal, prize.GuildWarRebirth, ItemFlow.GuildWar_Rebirth);
                    recorder.addValue(DailyRefresh.GuildWarRebirth);
                    GuildwarpuppetBO bo = new GuildwarpuppetBO();
                    bo.setPid(player.getPid());
                    bo.setGuildId(this.getGuildId());
                    bo.savePuppetId(curTimes);
                    bo.setApplyTime(CommTime.nowSecond());
                    bo.insert();
                    if (this.puppets.get(player.getPid()) != null) {
                        this.puppets.get(player.getPid()).add(bo);
                    } else {
                        List<GuildwarpuppetBO> list = new ArrayList<>();
                        list.add(bo);
                        this.puppets.put(player.getPid(), list);
                    }

                    this.broadcast("beforePuppet", this.getTotalPuppet(), player.getPid());
                    return new Guild.rebirth(this.getTotalPuppet(), this.getPersonPuppet(player.getPid()), recorder.getValue(DailyRefresh.GuildWarRebirth));
                }
            }
        }
    }

    public Guild.rebirth getPuppetInfo(Player player) {
        PlayerRecord recorder = player.getFeature(PlayerRecord.class);
        return new Guild.rebirth(this.getTotalPuppet(), this.getPersonPuppet(player.getPid()), recorder.getValue(DailyRefresh.GuildWarRebirth));
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
        List<GuildwarpuppetBO> list = this.puppets.get(pid);
        if (list != null) {
            num = list.size();
        }

        return num;
    }

    public List<Player> createPuppets() {
        List<Player> puppets = new ArrayList<>();

        for (Entry<Long, List<GuildwarpuppetBO>> entry : this.puppets.entrySet()) {
            Player player = PlayerMgr.getInstance().getPlayer(entry.getKey());

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
        List<GuildwarpuppetBO> puppets = this.puppets.get(puppet.getPid());
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

        if (find != null) {
            puppets.remove(find);
        }
    }

    public static class GuildSummary {
        long guildId;
        String guildName;
        core.network.proto.Player.showModle president;

        public core.network.proto.Player.showModle getPresident() {
            return this.president;
        }

        public void setPresident(core.network.proto.Player.showModle president) {
            this.president = president;
        }

        public GuildSummary() {
        }

        public GuildSummary(Guild guild) {
            if (guild != null) {
                this.guildId = guild.getGuildId();
                this.guildName = guild.getName();
                this.president = PlayerMgr.getInstance().getPlayer(guild.getMember(GuildJob.President).get(0)).getFeature(PlayerBase.class).modle();
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

    private class levelUpInfo {
        int level;
        int exp;

        private levelUpInfo(int level, int exp) {
            this.level = level;
            this.exp = exp;
        }
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
}
