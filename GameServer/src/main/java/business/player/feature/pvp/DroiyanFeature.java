package business.player.feature.pvp;

import business.global.arena.ArenaManager;
import business.global.arena.Competitor;
import business.global.notice.NoticeMgr;
import business.global.pvp.EncouterManager;
import business.global.rank.RankManager;
import business.player.Player;
import business.player.PlayerMgr;
import business.player.feature.Feature;
import business.player.feature.PlayerBase;
import business.player.feature.PlayerItem;
import business.player.feature.character.CharFeature;
import business.player.feature.features.PlayerRecord;
import business.player.item.Reward;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.enums.ConstEnum;
import com.zhonglian.server.common.enums.RankType;
import com.zhonglian.server.common.utils.CommMath;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefDroiyanTreasure;
import core.config.refdata.ref.RefReward;
import core.config.refdata.ref.RefVIP;
import core.database.game.bo.DroiyanBO;
import core.database.game.bo.DroiyanRecordBO;
import core.database.game.bo.DroiyanTreasureBO;

import java.util.*;
import java.util.stream.Collectors;

public class DroiyanFeature extends Feature {
    private static final int MAX_FIGHTERS = 3;
    private DroiyanBO bo;
    private List<DroiyanTreasureBO> treasures;
    private List<DroiyanRecordBO> records;
    private List<Long> enemies;
    private Map<Integer, Integer> hp;

    public DroiyanFeature(Player player) {
        super(player);

        this.treasures = new ArrayList<>();
        this.records = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.hp = new HashMap<>();
    }

    public void loadDB() {
        this.bo = (DroiyanBO) BM.getBM(DroiyanBO.class).findOne("pid", Long.valueOf(getPid()));
        if (this.bo == null) {
            this.bo = new DroiyanBO();
            this.bo.setPid(getPid());
            List<Long> list1 = ArenaManager.getInstance().randCompetitor(getPid(), 3);
            for (int i = 0; i < 3; i++) {
                this.bo.setFighters(i, ((Long) list1.get(i)).longValue());
            }
            this.bo.setLastSearchTime(CommTime.nowSecond());
            this.bo.insert();
            return;
        }
        this.treasures = BM.getBM(DroiyanTreasureBO.class).findAll("pid", Long.valueOf(getPid()));
        this.records = BM.getBM(DroiyanRecordBO.class).findAll("pid", Long.valueOf(getPid()));

        List<DroiyanRecordBO> list = BM.getBM(DroiyanRecordBO.class).findAll("target", Long.valueOf(getPid()));
        this.enemies = new ArrayList<>();
        int time = CommTime.nowSecond() - 604800;
        for (DroiyanRecordBO bo : list) {
            if (bo.getTime() < time) {
                bo.del();
                continue;
            }
            if (bo.getResult() == 1 && !bo.getRevenged())
                this.enemies.add(Long.valueOf(bo.getPid()));
            if (this.enemies.size() >= RefDataMgr.getFactor("DroiyanMaxEnemy", 20)) {
                break;
            }
        }

        this.hp = new HashMap<>();
    }

    public DroiyanBO getBo() {
        return this.bo;
    }

    public void addDroiyan(long pid) {
        for (int i = 0; i < this.bo.getFightersSize(); i++) {
            if (this.bo.getFighters(i) == 0L) {
                this.bo.saveFighters(i, pid);
                return;
            }
        }
    }

    public List<DroiyanRecordBO> getRecords() {
        return this.records;
    }

    public boolean popDroiyan(long targetPid) {
        int count = 0;
        boolean find = false;
        for (int i = 0; i < this.bo.getFightersSize(); i++) {
            if (this.bo.getFighters(i) == 0L) {
                count++;
            } else if (this.bo.getFighters(i) == targetPid) {
                this.bo.saveFighters(i, 0L);
                find = true;
            }
        }
        if (count == 0) {
            this.bo.saveLastSearchTime(CommTime.nowSecond());
        }
        return find;
    }

    public DroiyanTreasureBO beRobbed() {
        return beRobbedByType("Normal");
    }

    public DroiyanTreasureBO beRevengeRobbed() {
        return beRobbedByType("Revenge");
    }

    public DroiyanTreasureBO beRobbedByType(String type) {
        int now = CommTime.nowSecond();
        Optional<DroiyanTreasureBO> find = Optional.ofNullable(null);

        if ((type.equals("Normal") && isRed()) || (type.equals("Revenge") && !isRed())) {

            List<DroiyanTreasureBO> findList = (List<DroiyanTreasureBO>) this.treasures.stream().filter(bo -> (bo.getExpireTime() > paramInt)).collect(Collectors.toList());

            List<Integer> rangeList = new ArrayList<>();
            List<RefDroiyanTreasure> refList = new ArrayList<>();
            for (DroiyanTreasureBO tmp_bo : findList) {
                RefDroiyanTreasure tmp_ref = (RefDroiyanTreasure) RefDataMgr.get(RefDroiyanTreasure.class, Integer.valueOf(tmp_bo.getTreasureId()));
                if (!refList.contains(tmp_ref))
                    refList.add(tmp_ref);
            }
            for (RefDroiyanTreasure ref : refList) {
                String str;
                switch ((str = type).hashCode()) {
                    case -1955878649:
                        if (!str.equals("Normal"))
                            continue;
                        rangeList.add(Integer.valueOf(ref.NormalWeight));
                    case -1530471862:
                        if (!str.equals("Revenge"))
                            continue;
                        rangeList.add(Integer.valueOf(ref.RevengeWeight));
                }

            }
            int index = CommMath.getRandomIndexByRate(rangeList);
            if (index >= 0) {
                int id = ((RefDroiyanTreasure) refList.get(index)).id;
                find = findList.stream().filter(bo -> (bo.getTreasureId() == paramInt)).unordered().findAny();

            }

        } else if (type.equals("Normal") && !isRed()) {
            find = this.treasures.stream().filter(bo -> (bo.getExpireTime() > paramInt)).unordered().findAny();

        } else if (type.equals("Revenge") && isRed()) {

            List<DroiyanTreasureBO> findList = (List<DroiyanTreasureBO>) this.treasures.stream().filter(bo -> (bo.getExpireTime() > paramInt)).collect(Collectors.toList());
            int quality = 0;
            for (DroiyanTreasureBO tmp_bo : findList) {
                RefDroiyanTreasure tmp_ref = (RefDroiyanTreasure) RefDataMgr.get(RefDroiyanTreasure.class, Integer.valueOf(tmp_bo.getTreasureId()));
                if (tmp_ref.Quality > quality)
                    quality = tmp_ref.Quality;
            }
            int quality1 = quality;

            find = findList.stream().filter(bo -> (getquality(bo) == paramInt)).unordered().findAny();
        }

        if (find == null || !find.isPresent()) {
            return null;
        }
        DroiyanTreasureBO bo = find.get();
        this.treasures.remove(bo);
        this.player.pushProto("robbedTreasure", Long.valueOf(bo.getId()));
        return bo;
    }

    public void addTreasure(DroiyanTreasureBO treasure) {
        this.treasures.add(treasure);
    }

    public DroiyanTreasureBO addTreasure(RefDroiyanTreasure ref) {
        DroiyanTreasureBO treasure = new DroiyanTreasureBO();
        treasure.setPid(getPid());
        treasure.setTreasureId(ref.id);
        treasure.setGainTime(CommTime.nowSecond());
        treasure.setExpireTime(ref.Time + CommTime.nowSecond());
        treasure.insert();
        this.treasures.add(treasure);
        return treasure;
    }

    public void addEnemy(long pid) {
        if (this.enemies.size() >= RefDataMgr.getFactor("DroiyanMaxEnemy", 20)) {
            return;
        }
        this.enemies.add(Long.valueOf(pid));
        Player player = PlayerMgr.getInstance().getPlayer(pid);
        this.player.pushProto("newEnemy", ((PlayerBase) player.getFeature(PlayerBase.class)).summary());
    }

    public boolean isEnemy(long pid) {
        if (this.enemies.contains(Long.valueOf(pid))) {
            return true;
        }
        return false;
    }

    public void damage() {
        if (this.bo.getRed() < RefDataMgr.getFactor("Droiyan_Red", 80)) {
            return;
        }
        ((CharFeature) this.player.getFeature(CharFeature.class)).getAll().forEach((charid, character) -> {
            Integer curHp = this.hp.get(charid);
            if (curHp == null) {
                curHp = Integer.valueOf(100);
            }
            int damage = CommMath.randomInt(RefDataMgr.getFactor("Droiyan_RedDamageMin"), RefDataMgr.getFactor("Droiyan_RedDamageMax"));
            this.hp.put(charid, Integer.valueOf(Math.max(curHp.intValue() - damage, 0)));
        });
    }

    public void reborn() {
        ((CharFeature) this.player.getFeature(CharFeature.class)).getAll().forEach((charid, character) -> this.hp.put(charid, Integer.valueOf(100)));
    }

    public boolean isFullDroiyan() {
        for (int i = 0; i < this.bo.getFightersSize(); i++) {
            if (this.bo.getFighters(i) == 0L) {
                return false;
            }
        }
        return true;
    }

    public void checkDroiyan() {
        int interval = RefDataMgr.getFactor("Droiyan_SearchInterval", 1200);
        int times = (CommTime.nowSecond() - this.bo.getLastSearchTime()) / interval;
        if (times <= 0) {
            return;
        }
        List<Long> list = ranOpponent(getPid(), 4);
        list.removeAll(this.bo.getFightersAll());
        for (int i = 0; i < this.bo.getFightersSize() && times > 0; i++) {
            long cur = this.bo.getFighters(i);
            if (cur == 0L) {
                times--;
                this.bo.setFighters(i, ((Long) list.remove(0)).longValue());
            }
        }
        this.bo.setLastSearchTime(CommTime.nowSecond());
        this.bo.saveAll();
    }

    public long search() {
        List<Long> list = ranOpponent(getPid(), 4);
        list.removeAll(this.bo.getFightersAll());

        for (int i = 0; i < this.bo.getFightersSize(); i++) {
            long cur = this.bo.getFighters(i);
            if (cur == 0L) {
                this.bo.setFighters(i, ((Long) list.remove(0)).longValue());
                this.bo.setLastSearchTime(CommTime.nowSecond());
                this.bo.saveAll();
                return this.bo.getFighters(i);
            }
        }
        return 0L;
    }

    public List<Long> ranOpponent(long pid, int size) {
        List<Long> realPid = new ArrayList<>();

        List<Long> treasurePid = new ArrayList<>();

        RankManager instance = RankManager.getInstance();
        int rank = instance.getRank(RankType.Power, pid);
        int offset = RefDataMgr.getFactor("DroiyanSearchOffset", 20);
        int begin = rank - offset;
        int end = rank + offset;
        List<Long> list = new ArrayList<>();
        int max = Math.min(instance.getRankSize(RankType.Power) - 1, end);

        for (int r = Math.max(1, begin); r <= max; r++) {
            if (r != rank) {

                long tmp_pid = instance.getPlayerId(RankType.Power, r);

                Player tmp_player = PlayerMgr.getInstance().getPlayer(tmp_pid);
                if (tmp_player != null) {
                    if (((DroiyanFeature) tmp_player.getFeature(DroiyanFeature.class)).haveTreature()) {
                        treasurePid.add(Long.valueOf(tmp_pid));
                    } else {
                        realPid.add(Long.valueOf(tmp_pid));
                    }
                }
            }
        }

        if (treasurePid.size() + realPid.size() < size) {
            Competitor competitor = ArenaManager.getInstance().getOrCreate(pid);
            int rankA = competitor.getRank();
            int offsetA = 50 + size * 2;

            int beginA = rankA - offsetA;
            int endA = rankA + offsetA;
            int maxA = Math.min(endA, ArenaManager.getInstance().getRank().size());
            for (int j = Math.max(1, beginA); j <= maxA; j++) {
                if (j != rankA) {

                    list.add(Long.valueOf(((Competitor) ArenaManager.getInstance().getRank().get(j - 1)).getPid()));
                }
            }
        }

        Collections.shuffle(list);

        int pidSize = realPid.size() + treasurePid.size();

        for (int i = 0; i < size - pidSize; i++) {
            realPid.add(list.get(i));
        }

        Collections.shuffle(treasurePid);
        Collections.shuffle(realPid);

        treasurePid.addAll(realPid);

        return treasurePid.subList(0, size);
    }

    public boolean haveTreature() {
        int now = CommTime.nowSecond();
        for (DroiyanTreasureBO bo : this.treasures) {
            if (bo.getExpireTime() > now)
                return true;
        }
        return false;
    }

    public Reward openTreature(long treatureId) throws WSException {
        DroiyanTreasureBO find = null;
        for (DroiyanTreasureBO bo : this.treasures) {
            if (bo.getId() == treatureId) {
                find = bo;
                break;
            }
        }
        if (find == null) {
            throw new WSException(ErrorCode.Droiyan_TreasureNoFound, "%s玩家不持有宝物%s", new Object[]{Long.valueOf(getPid()), Long.valueOf(treatureId)});
        }
        if (find.getExpireTime() > CommTime.nowSecond()) {
            throw new WSException(ErrorCode.Droiyan_TreasureTimeLimit, "%s玩家宝物%s时间未到", new Object[]{Long.valueOf(getPid()), Long.valueOf(treatureId)});
        }
        PlayerRecord record = (PlayerRecord) this.player.getFeature(PlayerRecord.class);
        if (record.getValue(ConstEnum.DailyRefresh.OpenTreasure) >= ((RefVIP) RefDataMgr.get(RefVIP.class, Integer.valueOf(this.player.getVipLevel()))).OpenTreasureTimes) {
            throw new WSException(ErrorCode.Droiyan_TreasureOpenTimes, "藏宝图开启次数已满");
        }

        find.del();
        this.treasures.remove(find);

        RefDroiyanTreasure ref = (RefDroiyanTreasure) RefDataMgr.get(RefDroiyanTreasure.class, Integer.valueOf(find.getTreasureId()));
        Reward reward = ((RefReward) RefDataMgr.get(RefReward.class, Integer.valueOf(ref.Reward))).genReward();
        ((PlayerItem) this.player.getFeature(PlayerItem.class)).gain(reward, ItemFlow.DroiyanTreasure);
        EncouterManager.getInstance().addNews(EncouterManager.NewsType.Open, getPlayerName(), find.getTreasureId());

        record.addValue(ConstEnum.DailyRefresh.OpenTreasure);

        NoticeMgr.getInstance().sendMarquee(ConstEnum.UniverseMessageType.UseTreasure, new String[]{this.player.getName()});

        return reward;
    }

    public Map<Integer, Integer> getHpMap() {
        return this.hp;
    }

    public List<Long> getEnemies() {
        return this.enemies;
    }

    public boolean popEnemy(long targetPid) {
        return this.enemies.remove(Long.valueOf(targetPid));
    }

    public void setRevenged(long pid) {
        for (DroiyanRecordBO bo : this.records) {
            if (bo.getTarget() == pid) {
                bo.saveRevenged(true);
                break;
            }
        }
    }

    public List<DroiyanTreasureBO> getTreasures() {
        return this.treasures;
    }

    public void addRecored(DroiyanRecordBO recordBO) {
        this.records.add(recordBO);
    }

    public boolean isTreasureFull() {
        return (this.treasures.size() >= RefDataMgr.getFactor("MaxTreasureSize", 5));
    }

    public boolean isRed() {
        return (this.bo.getRed() >= RefDataMgr.getFactor("Droiyan_MaxRed", 100));
    }

    public int getquality(DroiyanTreasureBO bo) {
        RefDroiyanTreasure ref = (RefDroiyanTreasure) RefDataMgr.get(RefDroiyanTreasure.class, Integer.valueOf(bo.getTreasureId()));
        return ref.Quality;
    }
}

