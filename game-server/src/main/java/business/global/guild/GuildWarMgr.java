package business.global.guild;

import BaseTask.SyncTask.SyncTaskManager;
import business.global.battle.SimulatBattle;
import business.global.gmmail.MailCenter;
import business.player.Player;
import business.player.PlayerMgr;
import business.player.RobotManager;
import business.player.feature.PlayerCurrency;
import business.player.feature.features.PlayerRecord;
import business.player.feature.guild.GuildMemberFeature;
import business.player.item.Reward;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.enums.FightResult;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.common.enums.ConstEnum.DailyRefresh;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.common.utils.Maps;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefCrystalPrice;
import core.config.refdata.ref.RefGuildWarCenter;
import core.config.refdata.ref.RefGuildWarPersonReward;
import core.database.game.bo.GuildwarGuildResultBO;
import core.database.game.bo.GuildwarResultBO;
import core.database.game.bo.GuildwarapplyBO;
import core.database.game.bo.GuildwarpuppetBO;
import core.network.proto.Fight;
import core.network.proto.GuildWarCenterInfo;
import core.network.proto.GuildWarFightProtol;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Vector;
import java.util.Map.Entry;

public class GuildWarMgr {
    private static GuildWarMgr instance = new GuildWarMgr();
    public Map<Integer, List<Guild>> guildWarApplyer = Maps.newConcurrentHashMap();
    public Map<Integer, Guild> guildWarCenterOwner = Maps.newConcurrentHashMap();
    public Map<Integer, List<Guild>> guildWarAtk = Maps.newConcurrentHashMap();
    private int road = 3;
    private Map<Integer, Integer> fightTime = Maps.newConcurrentHashMap();
    private Map<Integer, List<GuildwarResultBO>> fightResult = Maps.newConcurrentHashMap();
    public Map<Long, Fight.Battle> fightBattle = Maps.newConcurrentHashMap();
    public Map<Long, List<GuildwarResultBO>> guildAllResulet = Maps.newConcurrentHashMap();
    private Map<Integer, List<GuildwarGuildResultBO>> guildFightResult = Maps.newConcurrentHashMap();
    private Map<Integer, GuildWarMgr.Center> fighters = Maps.newConcurrentHashMap();
    public Map<Integer, List<Player>> watchPlayers = Maps.newConcurrentHashMap();
    public Map<Long, List<Double>> playersHP = Maps.newConcurrentHashMap();
    public Map<Integer, Guild> historyWinner = Maps.newConcurrentHashMap();
    Map<Integer, Map<Long, GuildWarMgr.Report>> reports = new HashMap<>();
    public Map<Integer, List<GuildWarMgr.Road>> guildWarRoad = Maps.newConcurrentHashMap();

    public static GuildWarMgr getInstance() {
        return instance;
    }

    public void dailyRefresh() {
        try {
            for (RefGuildWarCenter ref : RefDataMgr.getAll(RefGuildWarCenter.class).values()) {
                this.guildWarApplyer.put(ref.id, new LinkedList<>());
                this.guildWarAtk.put(ref.id, new LinkedList<>());
                this.guildWarRoad.put(ref.id, new LinkedList<>());
                this.watchPlayers.put(ref.id, new LinkedList<>());

                for (int i = 0; i < this.road; i++) {
                    this.guildWarRoad.get(ref.id).add(new GuildWarMgr.Road());
                }
            }

            this.guildFightResult.clear();
            this.fightResult.clear();
            this.guildAllResulet.clear();
            this.fightBattle.clear();
        } catch (Exception var4) {
            var4.printStackTrace();
        }
    }

    public void init() {
        for (RefGuildWarCenter ref : RefDataMgr.getAll(RefGuildWarCenter.class).values()) {
            this.guildWarApplyer.put(ref.id, new LinkedList<>());
            this.guildWarAtk.put(ref.id, new LinkedList<>());
            this.guildWarRoad.put(ref.id, new LinkedList<>());
            this.watchPlayers.put(ref.id, new LinkedList<>());

            for (int i = 0; i < this.road; i++) {
                this.guildWarRoad.get(ref.id).add(new GuildWarMgr.Road());
            }
        }

        for (GuildwarapplyBO guildwarapply : BM.getBM(GuildwarapplyBO.class).findAllBySort("applyTime", true, 0)) {
            Guild guild = GuildMgr.getInstance().getGuild(guildwarapply.getGuildId());
            if (guild == null) {
                guildwarapply.del();
            } else {
                if (guildwarapply.getWinCenterId() != 0) {
                    this.historyWinner.put(guildwarapply.getWinCenterId(), guild);
                }

                if (guildwarapply.getApplyTime() > CommTime.getTodayZeroClockS()) {
                    guild.guildwarCenter = guildwarapply;
                    this.guildWarApplyer.get(guildwarapply.getCenterId()).add(guild);
                }
            }
        }

        for (RefGuildWarCenter ref : RefDataMgr.getAll(RefGuildWarCenter.class).values()) {
            if (this.historyWinner.get(ref.id) != null) {
                this.guildWarCenterOwner.put(ref.id, this.historyWinner.get(ref.id));
            }

            int centerId = ref.id;
            Guild owner = this.guildWarCenterOwner.get(centerId);
            if (owner == null) {
                this.guildWarAtk.get(ref.id).addAll(this.guildWarApplyer.get(ref.id));
            } else {
                boolean flag = false;

                for (Guild tmp_guild : this.guildWarApplyer.get(centerId)) {
                    if (owner == tmp_guild || flag) {
                        if (owner == tmp_guild) {
                            flag = true;
                        } else {
                            this.guildWarAtk.get(ref.id).add(tmp_guild);
                        }
                    }
                }
            }
        }

        if (CommTime.getTodayHour() >= 19) {
            this.Start();
        }
    }

    public Guild getAtkGuild(int centerId) {
        return this.guildWarAtk.get(centerId).size() > 0 ? this.guildWarAtk.get(centerId).get(0) : null;
    }

    public void applyGuildWar(int centerId, Guild guild) throws WSException {
        if (this.guildWarApplyer.get(centerId).size() >= RefDataMgr.getFactor("MaxGuildWarApply", 6)) {
            throw new WSException(ErrorCode.GuildWar_AlreadyApply, "据点申请已满");
        } else {
            synchronized (this) {
                this.guildWarApplyer.get(centerId).add(guild);
                this.guildWarAtk.get(centerId).add(guild);
            }
        }
    }

    public void prepareFight() {
        for (RefGuildWarCenter ref : RefDataMgr.getAll(RefGuildWarCenter.class).values()) {
            if (ref.isOpenTime() && this.getAtkGuild(ref.id) != null) {
                int centerId = ref.id;
                Guild owner = this.guildWarCenterOwner.get(centerId);
                new ArrayList();
                new ArrayList();
                List<Player> defpuppets = new ArrayList<>();
                new ArrayList();
                this.guildWarRoad.put(ref.id, new LinkedList<>());

                for (int i = 0; i < this.road; i++) {
                    this.guildWarRoad.get(ref.id).add(new GuildWarMgr.Road());
                }

                this.fightResult.put(centerId, new ArrayList<>());
                this.fighters.put(centerId, new GuildWarMgr.Center());
                List defList;
                if (owner == null) {
                    defList = RobotManager.getInstance().getRandomPlayers(14);
                } else {
                    defList = owner.getGuildWarPlayer();
                    defpuppets = owner.createPuppets();
                }

                Guild atkGuild = this.getAtkGuild(ref.id);

                while (atkGuild.getGuildWarPlayer().size() == 0) {
                    this.guildWarApplyer.get(ref.id).remove(atkGuild);
                    this.guildWarAtk.get(ref.id).remove(atkGuild);
                    atkGuild = this.getAtkGuild(ref.id);
                    if (atkGuild == null) {
                        break;
                    }
                }

                if (atkGuild != null) {
                    List<Player> atkList = atkGuild.getGuildWarPlayer();
                    List<Player> atkpuppets = atkGuild.createPuppets();
                    Map<Long, GuildWarMgr.Report> reportmap = new HashMap<>();
                    GuildWarMgr.Report defreport = new GuildWarMgr.Report();
                    defreport.guild = owner;
                    defreport.total = defList.size() + defpuppets.size();
                    if (owner == null) {
                        reportmap.put(0L, defreport);
                    } else {
                        for (Entry<Long, List<GuildwarpuppetBO>> entry : owner.puppets.entrySet()) {
                            defreport.puppet.put(entry.getKey(), entry.getValue().size());
                        }

                        reportmap.put(owner.getGuildId(), defreport);
                    }

                    GuildWarMgr.Report atkreport = new GuildWarMgr.Report();
                    atkreport.guild = atkGuild;
                    atkreport.total = atkList.size() + atkpuppets.size();

                    for (Entry<Long, List<GuildwarpuppetBO>> entry : atkGuild.puppets.entrySet()) {
                        atkreport.puppet.put(entry.getKey(), entry.getValue().size());
                    }

                    reportmap.put(atkGuild.getGuildId(), atkreport);
                    this.reports.put(centerId, reportmap);
                    this.fighters.put(centerId, new GuildWarMgr.Center(atkList, atkGuild, defList, owner));

                    for (int i = 0; i < defList.size(); i++) {
                        int index = i % this.road;
                        this.guildWarRoad.get(ref.id).get(index).getDefplayers().add((Player)defList.get(i));
                    }

                    for (int i = 0; i < defpuppets.size(); i++) {
                        int index = i % this.road;
                        this.guildWarRoad.get(ref.id).get(index).getDefplayers().add(defpuppets.get(i));
                    }

                    for (int i = 0; i < atkList.size(); i++) {
                        int index = i % this.road;
                        this.guildWarRoad.get(ref.id).get(index).getAtkplayers().add((Player)atkList.get(i));
                    }

                    for (int i = 0; i < atkpuppets.size(); i++) {
                        int index = i % this.road;
                        this.guildWarRoad.get(ref.id).get(index).getAtkplayers().add((Player)atkpuppets.get(i));
                    }
                }
            }
        }
    }

    public void beginFightByCenterId(int centerId) {
        this.fightTime.put(centerId, CommTime.nowSecond());

        for (int i = 0; i < this.road; i++) {
            GuildWarMgr.Road road = this.guildWarRoad.get(centerId).get(i);
            road.setBegin(CommTime.nowSecond());
            if (road.getAtkplayers().size() == 0 || road.getDefplayers().size() == 0) {
                road.setOverTime(CommTime.nowSecond());
            }

            SyncTaskManager.task(() -> SyncTaskManager.schedule(GuildWarConfig.oneFightTime(), () -> this.roadFight(road, centerId)));
        }
    }

    private boolean roadFight(GuildWarMgr.Road road, int centerId) {
        synchronized (this) {
            road.setBegin(CommTime.nowSecond());
            if (this.atkResult(centerId, road)) {
                return false;
            } else if (this.timeOver(centerId, road)) {
                return false;
            } else if (road.getWinner() != 0L) {
                return false;
            } else {
                Player atkPlayer = null;
                Player defPlayer = null;
                if (road.getAtkplayers().size() > 0) {
                    atkPlayer = road.getAtkplayers().get(0);
                }

                if (road.getDefplayers().size() > 0) {
                    defPlayer = road.getDefplayers().get(0);
                }

                if (atkPlayer != null && defPlayer != null) {
                    SimulatBattle battle = new SimulatBattle(atkPlayer, defPlayer);
                    battle.initHp(this.playersHP.get(atkPlayer.getPid()), this.playersHP.get(defPlayer.getPid()));
                    battle.initInspire();
                    battle.fight();
                    FightResult result = battle.getResult();
                    GuildwarResultBO resultbo = new GuildwarResultBO();
                    resultbo.setAtkpid(atkPlayer.getPid());
                    resultbo.setDefpid(defPlayer.getPid());
                    resultbo.setResult(result.ordinal());
                    resultbo.setCenterId(centerId);
                    resultbo.setFightTime(CommTime.nowSecond());
                    resultbo.insert();
                    this.fightResult.get(centerId).add(resultbo);
                    this.fightBattle.put(resultbo.getId(), battle.proto());
                    Player lose = null;
                    Player winner = null;
                    if (result == FightResult.Win) {
                        lose = road.getDefplayers().remove(0);
                        winner = atkPlayer;
                        road.getDeaddefplayers().add(lose);
                    }

                    if (result == FightResult.Lost) {
                        lose = road.getAtkplayers().remove(0);
                        winner = defPlayer;
                        road.getDeadatkplayers().add(lose);
                    }

                    if (result == FightResult.Draw) {
                        Player lose1 = road.getDefplayers().remove(0);
                        road.getDeaddefplayers().add(lose1);
                        Player lose2 = road.getAtkplayers().remove(0);
                        road.getDeadatkplayers().add(lose2);
                    }

                    if (lose != null && winner != null) {
                        this.playersHP.put(winner.getPid(), battle.getWinnerHp());
                        if (GuildWarConfig.puppetPlayer.class.isInstance(lose)) {
                            Guild guild = lose.getFeature(GuildMemberFeature.class).getGuild();
                            guild.removePuppet(lose);
                        }
                    }

                    if (road.getDefplayers().size() == 0 || road.getAtkplayers().size() == 0) {
                        road.setOverTime(CommTime.nowSecond());
                    }
                } else {
                    if (road.getOverTime() != 0 && CommTime.nowSecond() - road.getOverTime() + 2 > GuildWarConfig.overTime()) {
                        if (atkPlayer == null) {
                            if (RobotManager.getInstance().isRobot(defPlayer.getPid())) {
                                road.setWinner(-1L);
                            } else {
                                road.setWinner(defPlayer.getFeature(GuildMemberFeature.class).getGuildID());
                            }

                            Optional<GuildWarMgr.Road> find = this.guildWarRoad
                                .get(centerId)
                                .stream()
                                .parallel()
                                .filter(x -> x != road && x.getWinner() == 0L)
                                .findAny();
                            if (find.isPresent()) {
                                find.get().getDefplayers().addAll(road.getDefplayers());
                                road.getDefplayers().clear();
                            }
                        } else {
                            road.setWinner(atkPlayer.getFeature(GuildMemberFeature.class).getGuildID());
                            Optional<GuildWarMgr.Road> find = this.guildWarRoad
                                .get(centerId)
                                .stream()
                                .parallel()
                                .filter(x -> x != road && x.getWinner() == 0L)
                                .findAny();
                            if (find.isPresent()) {
                                find.get().getAtkplayers().addAll(road.getAtkplayers());
                                road.getAtkplayers().clear();
                            }
                        }

                        return false;
                    }

                    if (road.getOverTime() == 0) {
                        road.setOverTime(CommTime.nowSecond());
                    }
                }

                return true;
            }
        }
    }

    private void dealWarFinish(int centerId, GuildwarGuildResultBO result) {
        synchronized (this) {
            for (int i = 0; i < this.road; i++) {
                GuildWarMgr.Road road1 = this.guildWarRoad.get(centerId).get(i);
                road1.isOver = true;
            }

            this.personReward(this.fightResult.get(centerId), centerId);
            this.fighters.remove(centerId);
            this.playersHP.clear();

            for (Player player : this.watchPlayers.get(centerId)) {
                player.pushProto("warFinish", "");
            }

            this.checkReward(centerId);
            this.roportAll(centerId, result);
        }
    }

    public void roportAll(int centerId, GuildwarGuildResultBO result) {
        Map<Long, GuildWarMgr.Report> map = this.reports.get(centerId);
        RefGuildWarCenter ref = RefDataMgr.get(RefGuildWarCenter.class, centerId);
        long defId = result.getDefGuildId();
        if (defId != 0L) {
            GuildWarMgr.Report report = map.get(defId);
            Guild defGuild = GuildMgr.getInstance().getGuild(defId);
            String myGuild = defGuild.getName();
            String total = String.valueOf(map.get(defId).total);
            String dead = String.valueOf(this.getdeadDef(centerId));
            String EnemyGuild = GuildMgr.getInstance().getGuild(result.getAtkGuildId()).getName();
            String enemyTotal = String.valueOf(map.get(result.getAtkGuildId()).total);
            String enemyDead = String.valueOf(this.getdeadAtk(centerId));
            SyncTaskManager.task(
                () -> {
                    int mailId = 0;
                    if (result.getResult() == FightResult.Win.ordinal()) {
                        mailId = ref.FailMail;
                    } else if (result.getResult() == FightResult.Lost.ordinal()) {
                        mailId = ref.MailId;
                    }

                    for (Long pid : defGuild.getMembers()) {
                        int puppet = 0;
                        int kill = 0;

                        try {
                            if (report.puppet.get(pid) != null) {
                                puppet = report.puppet.get(pid);
                            }
                        } catch (Exception var18x) {
                        }

                        kill = this.getKillNum(centerId, pid);
                        MailCenter.getInstance()
                            .sendMail(pid, mailId, myGuild, total, dead, EnemyGuild, enemyTotal, enemyDead, String.valueOf(puppet), String.valueOf(kill));
                    }
                }
            );
        }

        long guildId = result.getAtkGuildId();
        GuildWarMgr.Report report = map.get(guildId);
        Guild defGuild = GuildMgr.getInstance().getGuild(guildId);
        String myGuild = defGuild.getName();
        String total = String.valueOf(map.get(guildId).total);
        String dead = String.valueOf(this.getdeadAtk(centerId));
        String enemyTotal = String.valueOf(map.get(result.getDefGuildId()).total);
        String enemyDead = String.valueOf(this.getdeadDef(centerId));
        SyncTaskManager.task(
            () -> {
                String EnemyGuild = "";
                if (GuildMgr.getInstance().getGuild(result.getDefGuildId()) == null) {
                    EnemyGuild = "神秘帮派";
                } else {
                    EnemyGuild = GuildMgr.getInstance().getGuild(result.getDefGuildId()).getName();
                }

                int mailId = 0;
                if (result.getResult() == FightResult.Win.ordinal()) {
                    mailId = ref.MailId;
                } else if (result.getResult() == FightResult.Lost.ordinal()) {
                    mailId = ref.FailMail;
                }

                for (Long pid : defGuild.getMembers()) {
                    int puppet = 0;
                    int kill = 0;

                    try {
                        if (report.puppet.get(pid) != null) {
                            puppet = report.puppet.get(pid);
                        }
                    } catch (Exception var18x) {
                    }

                    kill = this.getKillNum(centerId, pid);
                    MailCenter.getInstance()
                        .sendMail(pid, mailId, myGuild, total, dead, EnemyGuild, enemyTotal, enemyDead, String.valueOf(puppet), String.valueOf(kill));
                }
            }
        );
    }

    public boolean atkResult(int centerId, GuildWarMgr.Road road) {
        synchronized (this) {
            if (road.isOver) {
                return true;
            } else {
                int atknum = this.getleftAtk(centerId);
                int defnum = this.getleftDef(centerId);
                if (atknum != 0 && defnum != 0) {
                    return false;
                } else {
                    GuildwarGuildResultBO bo = new GuildwarGuildResultBO();
                    if (this.getAtkGuild(centerId) != null) {
                        bo.setAtkGuildId(this.getAtkGuild(centerId).getGuildId());
                    }

                    if (this.guildWarCenterOwner.get(centerId) != null) {
                        bo.setDefGuildId(this.guildWarCenterOwner.get(centerId).getGuildId());
                    }

                    bo.setCenterId(centerId);
                    bo.setFightTime(CommTime.nowSecond());
                    if (atknum == 0) {
                        bo.setResult(FightResult.Lost.ordinal());
                    }

                    if (defnum == 0) {
                        bo.setResult(FightResult.Win.ordinal());
                    }

                    bo.insert();
                    this.guildAllResulet.put(bo.getId(), new ArrayList<>(this.fightResult.get(centerId)));
                    if (this.guildFightResult.get(centerId) == null) {
                        List<GuildwarGuildResultBO> list = new ArrayList<>();
                        list.add(bo);
                        this.guildFightResult.put(centerId, list);
                    } else {
                        this.guildFightResult.get(centerId).add(bo);
                    }

                    Guild guild = this.guildWarAtk.get(centerId).remove(0);
                    if (defnum == 0) {
                        this.guildWarCenterOwner.put(centerId, guild);
                    }

                    this.dealWarFinish(centerId, bo);
                    return true;
                }
            }
        }
    }

    public boolean timeOver(int centerId, GuildWarMgr.Road road) {
        synchronized (this) {
            if (road.isOver) {
                return true;
            } else if (CommTime.nowSecond() - this.fightTime.get(centerId) > GuildWarConfig.fightTime() - GuildWarConfig.restTime()) {
                GuildwarGuildResultBO bo = new GuildwarGuildResultBO();
                bo.setAtkGuildId(this.getAtkGuild(centerId).getGuildId());
                if (this.guildWarCenterOwner.get(centerId) != null) {
                    bo.setDefGuildId(this.guildWarCenterOwner.get(centerId).getGuildId());
                }

                bo.setCenterId(centerId);
                bo.setFightTime(CommTime.nowSecond());
                bo.setResult(FightResult.Lost.ordinal());
                bo.insert();
                if (this.guildFightResult.get(centerId) == null) {
                    List<GuildwarGuildResultBO> list = new ArrayList<>();
                    list.add(bo);
                    this.guildFightResult.put(centerId, list);
                } else {
                    this.guildFightResult.get(centerId).add(bo);
                }

                this.guildWarAtk.get(centerId).remove(0);
                this.dealWarFinish(centerId, bo);
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean fight() {
        int guilds = 0;

        for (List<Guild> list : this.guildWarAtk.values()) {
            guilds += list.size();
        }

        if (guilds == 0) {
            return false;
        } else {
            this.prepareFight();

            for (RefGuildWarCenter ref : RefDataMgr.getAll(RefGuildWarCenter.class).values()) {
                if (ref.isOpenTime() && this.getAtkGuild(ref.id) != null) {
                    this.beginFightByCenterId(ref.id);
                }
            }

            return true;
        }
    }

    public void Start() {
        try {
            this.fight();
            SyncTaskManager.schedule(GuildWarConfig.fightTime() * 1000, () -> this.fight());
        } catch (Exception var2) {
            var2.printStackTrace();
        }
    }

    public void guildWarEvent() {
        try {
            this.checkTakeReward();
            this.Start();
        } catch (Exception var2) {
            var2.printStackTrace();
        }
    }

    public void checkTakeReward() {
        for (RefGuildWarCenter ref : RefDataMgr.getAll(RefGuildWarCenter.class).values()) {
            if (ref.isOpenTime() && this.getAtkGuild(ref.id) == null) {
                Guild owner = this.guildWarCenterOwner.get(ref.id);
                if (owner != null) {
                    for (Long pid : owner.getMembers()) {
                        MailCenter.getInstance().sendMail(pid, ref.TakeMail);
                    }
                }
            }
        }
    }

    public List<GuildWarCenterInfo> getCenterInfo() {
        List<GuildWarCenterInfo> listInfo = new ArrayList<>();

        for (RefGuildWarCenter ref : RefDataMgr.getAll(RefGuildWarCenter.class).values()) {
            listInfo.add(this.centerInfo(ref));
        }

        return listInfo;
    }

    public GuildWarCenterInfo centerInfo(RefGuildWarCenter ref) {
        int centerId = ref.id;
        GuildWarCenterInfo info = new GuildWarCenterInfo();
        info.setCenterId(centerId);
        info.setOpen(ref.isOpenTime());
        List<Guild.GuildSummary> list = new ArrayList<>();

        for (Guild guild : this.guildWarApplyer.get(centerId)) {
            Guild.GuildSummary summary = new Guild.GuildSummary();
            summary.setGuildId(guild.getGuildId());
            summary.setGuildName(guild.getName());
            list.add(summary);
        }

        info.setAtkGuild(list);
        Guild guildowner = this.guildWarCenterOwner.get(centerId);
        if (guildowner != null && guildowner.guildwarCenter != null && guildowner.guildwarCenter.getWinCenterId() == centerId) {
            info.setWinner(new Guild.GuildSummary(guildowner));
        }

        if (guildowner != null) {
            info.setOwner(new Guild.GuildSummary(guildowner));
        } else {
            Guild.GuildSummary summary = new Guild.GuildSummary();
            summary.setGuildName("神秘帮会");
            core.network.proto.Player.showModle modle = new core.network.proto.Player.showModle();
            modle.name = "神秘帮主";
            summary.setPresident(modle);
            info.setOwner(summary);
        }

        List<GuildWarCenterInfo.GuildwarGuildResultRecord> resultlist = new ArrayList<>();
        if (this.guildFightResult.get(centerId) != null) {
            for (GuildwarGuildResultBO bo : this.guildFightResult.get(centerId)) {
                resultlist.add(new GuildWarCenterInfo.GuildwarGuildResultRecord(bo));
            }
        }

        info.setResult(resultlist);
        info.setAtkingGuild(new Guild.GuildSummary(this.getAtkGuild(centerId)));
        return info;
    }

    public GuildWarFightProtol getFightInfo(int centerId, Player player) throws WSException {
        GuildWarFightProtol info = new GuildWarFightProtol();
        info.setCenterId(centerId);
        Guild guildatk = this.getAtkGuild(centerId);
        if (this.fighters.get(centerId) == null) {
            throw new WSException(ErrorCode.GuildWar_NotFoundFight, "战斗未开始或已结束");
        } else {
            if (guildatk != null) {
                info.setAtk(new Guild.GuildSummary(guildatk));
            } else {
                Guild.GuildSummary summary = new Guild.GuildSummary();
                summary.setGuildId(0L);
                summary.setGuildName("");
                info.setAtk(summary);
            }

            Guild guilddef = this.guildWarCenterOwner.get(centerId);
            if (guilddef != null) {
                info.setDef(new Guild.GuildSummary(guilddef));
            } else {
                Guild.GuildSummary summary = new Guild.GuildSummary();
                summary.setGuildId(-1L);
                summary.setGuildName("神秘帮会");
                info.setDef(summary);
            }

            List<GuildWarFightProtol.GuildInfo> guildInfo = new ArrayList<>();
            guildInfo.add(
                new GuildWarFightProtol.GuildInfo(
                    info.getAtk().getGuildName(),
                    this.getleftAtk(centerId),
                    this.fighters.get(centerId).atkplayers.size() - this.getleftAtk(centerId),
                    this.getAtkPuppetNum(centerId)
                )
            );
            guildInfo.add(
                new GuildWarFightProtol.GuildInfo(
                    info.getDef().getGuildName(),
                    this.getleftDef(centerId),
                    this.fighters.get(centerId).defplayers.size() - this.getleftDef(centerId),
                    this.getDefPuppetNum(centerId)
                )
            );
            info.setGuildInfo(guildInfo);
            List<GuildWarFightProtol.ResultRecord> result = new ArrayList<>();

            for (GuildwarResultBO bo : this.fightResult.get(centerId)) {
                result.add(new GuildWarFightProtol.ResultRecord(bo));
            }

            info.setResultInfo(result);
            info.setEndTime(this.nextRefreshTime());
            info.setKillnum(this.getKillNum(centerId, player.getPid()));
            List<GuildWarFightProtol.RoadSummry> summarylist = new ArrayList<>();

            for (GuildWarMgr.Road road : this.guildWarRoad.get(centerId)) {
                summarylist.add(new GuildWarFightProtol.RoadSummry(road));
            }

            info.setRoad(summarylist);

            for (GuildWarMgr.Road road : this.guildWarRoad.get(centerId)) {
                if (road.getDeadatkplayers().contains(player) || road.getDeaddefplayers().contains(player)) {
                    info.setDead(true);
                    break;
                }
            }

            info.setRebirthTime(player.getFeature(PlayerRecord.class).getValue(DailyRefresh.GuildWarRebirth));
            List<Player> listplayer = this.watchPlayers.get(centerId);
            if (!listplayer.contains(player)) {
                listplayer.add(player);
            }

            return info;
        }
    }

    public int nextRefreshTime() {
        int time = CommTime.getTodayZeroClockS();
        time += 68400;

        do {
            time += GuildWarConfig.fightTime();
        } while (time <= CommTime.nowSecond());

        return time - GuildWarConfig.restTime();
    }

    public int getleftAtk(int centerId) {
        int num = 0;

        for (int i = 0; i < this.road; i++) {
            GuildWarMgr.Road road = this.guildWarRoad.get(centerId).get(i);
            num += road.getAtkplayers().size();
        }

        return num;
    }

    public int getleftDef(int centerId) {
        int num = 0;

        for (int i = 0; i < this.road; i++) {
            GuildWarMgr.Road road = this.guildWarRoad.get(centerId).get(i);
            num += road.getDefplayers().size();
        }

        return num;
    }

    public int getdeadAtk(int centerId) {
        int num = 0;

        for (int i = 0; i < this.road; i++) {
            GuildWarMgr.Road road = this.guildWarRoad.get(centerId).get(i);
            num += road.getDeadatkplayers().size();
        }

        return num;
    }

    public int getdeadDef(int centerId) {
        int num = 0;

        for (int i = 0; i < this.road; i++) {
            GuildWarMgr.Road road = this.guildWarRoad.get(centerId).get(i);
            num += road.getDeaddefplayers().size();
        }

        return num;
    }

    public int getKillNum(int centerId, long pid) {
        int num = 0;

        for (GuildwarResultBO bo : this.fightResult.get(centerId)) {
            if (bo.getAtkpid() == pid && bo.getResult() == FightResult.Win.ordinal()) {
                num++;
            }
        }

        return num;
    }

    public int getAtkPuppetNum(int centerId) {
        int num = 0;

        for (Player player : this.fighters.get(centerId).atkplayers) {
            if (GuildWarConfig.puppetPlayer.class.isInstance(player)) {
                num++;
            }
        }

        return num;
    }

    public int getDefPuppetNum(int centerId) {
        int num = 0;

        for (Player player : this.fighters.get(centerId).defplayers) {
            if (GuildWarConfig.puppetPlayer.class.isInstance(player)) {
                num++;
            }
        }

        return num;
    }

    public void checkReward(int centerId) {
        Guild guild = this.guildWarCenterOwner.get(centerId);
        if (guild != null && this.guildWarAtk.get(centerId).size() == 0) {
            if (guild.guildwarCenter.getWinCenterId() != 0) {
                return;
            }

            RefGuildWarCenter ref = RefDataMgr.get(RefGuildWarCenter.class, centerId);

            for (Long pid : guild.getMembers()) {
                MailCenter.getInstance().sendMail(pid, ref.TakeMail);
            }

            guild.guildwarCenter.saveWinCenterId(centerId);
            this.historyWinner.put(centerId, guild);
        }
    }

    public void personReward(List<GuildwarResultBO> list, int centerId) {
        List<GuildWarMgr.Road> roads = this.guildWarRoad.get(centerId);
        int atkwin = 0;
        int defwin = 0;

        for (GuildWarMgr.Road road : roads) {
            if (road.getWinner() != 0L) {
                if (road.getAtkplayers().size() != 0) {
                    atkwin++;
                } else if (road.getDefplayers().size() != 0) {
                    defwin++;
                }
            }
        }

        Map<Long, List<GuildwarResultBO>> map = Maps.newConcurrentHashMap();

        for (GuildwarResultBO bo : list) {
            if (bo.getResult() == FightResult.Win.ordinal()) {
                if (map.get(bo.getAtkpid()) != null) {
                    map.get(bo.getAtkpid()).add(bo);
                } else {
                    List<GuildwarResultBO> tmp_list = new ArrayList<>();
                    tmp_list.add(bo);
                    map.put(bo.getAtkpid(), tmp_list);
                }
            }
        }

        for (Entry<Long, List<GuildwarResultBO>> entry : map.entrySet()) {
            int num = 1;
            Player player = PlayerMgr.getInstance().getPlayer(entry.getKey());
            if (this.fighters.get(centerId).atkplayers.contains(player)) {
                num = Math.max(num, atkwin);
            } else if (this.fighters.get(centerId).defplayers.contains(player)) {
                num = Math.max(num, defwin);
            }

            Reward rewardall = new Reward();
            Reward reward = RefGuildWarPersonReward.getReward(entry.getValue().size());

            for (int i = 0; i < num; i++) {
                rewardall.combine(reward);
            }

            MailCenter.getInstance().sendMail(player.getPid(), "GM", "帮派战个人奖励", "在帮派战中你奋勇杀敌，获得以下奖励", rewardall);
        }
    }

    public GuildWarFightProtol rebirth(Player player, int centerId) throws WSException {
        GuildWarMgr.Center center = this.fighters.get(centerId);
        if (center == null) {
            throw new WSException(ErrorCode.GuildWar_NotFoundFight, "战斗已结束");
        } else {
            PlayerCurrency currency = player.getFeature(PlayerCurrency.class);
            PlayerRecord recorder = player.getFeature(PlayerRecord.class);
            int curTimes = recorder.getValue(DailyRefresh.GuildWarRebirth);
            RefCrystalPrice prize = RefCrystalPrice.getPrize(curTimes);
            if (!currency.check(PrizeType.Crystal, prize.GuildWarRebirth)) {
                throw new WSException(ErrorCode.NotEnough_Crystal, "玩家复活需要钻石%s", prize.GuildWarRebirth);
            } else {
                currency.consume(PrizeType.Crystal, prize.GuildWarRebirth, ItemFlow.GuildWar_Rebirth);
                recorder.addValue(DailyRefresh.GuildWarRebirth);
                Guild guild = player.getFeature(GuildMemberFeature.class).getGuild();
                GuildwarpuppetBO bo = new GuildwarpuppetBO();
                bo.setPid(player.getPid());
                bo.setGuildId(guild.getGuildId());
                bo.savePuppetId(curTimes);
                bo.setApplyTime(CommTime.nowSecond());
                bo.insert();
                if (guild.puppets.get(player.getPid()) != null) {
                    guild.puppets.get(player.getPid()).add(bo);
                } else {
                    List<GuildwarpuppetBO> list = new ArrayList<>();
                    list.add(bo);
                    guild.puppets.put(player.getPid(), list);
                }

                GuildWarMgr.Report report = this.reports.get(centerId).get(guild.getGuildId());
                report.total++;
                int num = 0;
                if (report.puppet.get(player.getPid()) != null) {
                    num = report.puppet.get(player.getPid());
                }

                report.puppet.put(player.getPid(), ++num);
                List<GuildWarMgr.Road> list = this.guildWarRoad.get(centerId);
                List<GuildWarMgr.Road> tmp_list = new ArrayList<>(list);
                Collections.shuffle(tmp_list);
                if (center.defplayers.contains(player)) {
                    Optional<GuildWarMgr.Road> find = tmp_list.stream().filter(x -> x.getWinner() == 0L).findAny();
                    if (find.isPresent()) {
                        GuildWarConfig.puppetPlayer p_player = new GuildWarConfig.puppetPlayer(player.getPlayerBO());
                        p_player.setPuppet_id(curTimes);
                        p_player.setIs_puppet(true);
                        find.get().getDefplayers().add(p_player);
                    }
                }

                if (center.atkplayers.contains(player)) {
                    Optional<GuildWarMgr.Road> find = tmp_list.stream().filter(x -> x.getWinner() == 0L).findAny();
                    if (find.isPresent()) {
                        GuildWarConfig.puppetPlayer p_player = new GuildWarConfig.puppetPlayer(player.getPlayerBO());
                        p_player.setPuppet_id(curTimes);
                        p_player.setIs_puppet(true);
                        find.get().getAtkplayers().add(p_player);
                    }
                }

                SyncTaskManager.task(() -> {
                    for (Player player_tmp : this.watchPlayers.get(centerId)) {
                        if (player_tmp != player) {
                            try {
                                player_tmp.pushProto("guildwarRebirth", this.getFightInfo(centerId, player_tmp));
                            } catch (WSException var6x) {
                                var6x.printStackTrace();
                            }
                        }
                    }
                });
                return this.getFightInfo(centerId, player);
            }
        }
    }

    public int isFighting(Guild guild) {
        for (RefGuildWarCenter ref : RefDataMgr.getAll(RefGuildWarCenter.class).values()) {
            int centerId = 0;
            if (this.fighters.get(ref.id) != null) {
                centerId = ref.id;
            }

            if (centerId != 0) {
                Guild Def = this.guildWarCenterOwner.get(centerId);
                Guild Atk = this.getAtkGuild(centerId);
                if (Def != null && Def == guild) {
                    return centerId;
                }

                if (Atk != null && Atk == guild) {
                    return centerId;
                }
            }
        }

        return 0;
    }

    private static class Center {
        Guild atkGuild;
        Guild defGuild;
        List<Player> atkplayers = new ArrayList<>();
        List<Player> defplayers = new ArrayList<>();

        private Center() {
        }

        private Center(List<Player> atkplayers, Guild atkGuild, List<Player> defplayers, Guild defGuild) {
            this.atkplayers = atkplayers;
            this.atkGuild = atkGuild;
            this.defplayers = defplayers;
            this.defGuild = defGuild;
        }
    }

    private static class Report {
        Guild guild;
        int total;
        Map<Long, Integer> puppet = new HashMap<>();

        private Report() {
        }
    }

    public static class Road {
        List<Player> atkplayers;
        List<Player> defplayers;
        List<Player> deadatkplayers;
        List<Player> deaddefplayers;
        long Winner = 0L;
        int overTime;
        boolean isOver = false;
        int begin;

        public Road() {
            this.atkplayers = new Vector<>();
            this.defplayers = new Vector<>();
            this.deadatkplayers = new Vector<>();
            this.deaddefplayers = new Vector<>();
        }

        public long getWinner() {
            return this.Winner;
        }

        public void setWinner(long winner) {
            this.Winner = winner;
        }

        public List<Player> getAtkplayers() {
            return this.atkplayers;
        }

        public void setAtkplayers(List<Player> atkplayers) {
            this.atkplayers = atkplayers;
        }

        public List<Player> getDefplayers() {
            return this.defplayers;
        }

        public void setDefplayers(List<Player> defplayers) {
            this.defplayers = defplayers;
        }

        public int getOverTime() {
            return this.overTime;
        }

        public void setOverTime(int overTime) {
            this.overTime = overTime;
        }

        public List<Player> getDeadatkplayers() {
            return this.deadatkplayers;
        }

        public void setDeadatkplayers(List<Player> deadatkplayers) {
            this.deadatkplayers = deadatkplayers;
        }

        public List<Player> getDeaddefplayers() {
            return this.deaddefplayers;
        }

        public void setDeaddefplayers(List<Player> deaddefplayers) {
            this.deaddefplayers = deaddefplayers;
        }

        public boolean isOver() {
            return this.isOver;
        }

        public void setOver(boolean isOver) {
            this.isOver = isOver;
        }

        public int getBegin() {
            return this.begin;
        }

        public void setBegin(int begin) {
            this.begin = begin;
        }
    }
}
