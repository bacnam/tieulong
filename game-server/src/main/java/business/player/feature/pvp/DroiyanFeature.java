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
import com.zhonglian.server.common.enums.RankType;
import com.zhonglian.server.common.enums.ConstEnum.DailyRefresh;
import com.zhonglian.server.common.enums.ConstEnum.UniverseMessageType;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class DroiyanFeature extends Feature {
    private static final int MAX_FIGHTERS = 3;
    private DroiyanBO bo;
    private List<DroiyanTreasureBO> treasures = new ArrayList<>();
    private List<DroiyanRecordBO> records = new ArrayList<>();
    private List<Long> enemies = new ArrayList<>();
    private Map<Integer, Integer> hp = new HashMap<>();

    public DroiyanFeature(Player player) {
        super(player);
    }

    @Override
    public void loadDB() {
        this.bo = BM.getBM(DroiyanBO.class).findOne("pid", this.getPid());
        if (this.bo == null) {
            this.bo = new DroiyanBO();
            this.bo.setPid(this.getPid());
            List<Long> list = ArenaManager.getInstance().randCompetitor(this.getPid(), 3);

            for (int i = 0; i < 3; i++) {
                this.bo.setFighters(i, list.get(i));
            }

            this.bo.setLastSearchTime(CommTime.nowSecond());
            this.bo.insert();
        } else {
            this.treasures = BM.getBM(DroiyanTreasureBO.class).findAll("pid", this.getPid());
            this.records = BM.getBM(DroiyanRecordBO.class).findAll("pid", this.getPid());
            List<DroiyanRecordBO> list = BM.getBM(DroiyanRecordBO.class).findAll("target", this.getPid());
            this.enemies = new ArrayList<>();
            int time = CommTime.nowSecond() - 604800;

            for (DroiyanRecordBO bo : list) {
                if (bo.getTime() < time) {
                    bo.del();
                } else {
                    if (bo.getResult() == 1 && !bo.getRevenged()) {
                        this.enemies.add(bo.getPid());
                    }

                    if (this.enemies.size() >= RefDataMgr.getFactor("DroiyanMaxEnemy", 20)) {
                        break;
                    }
                }
            }

            this.hp = new HashMap<>();
        }
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
        return this.beRobbedByType("Normal");
    }

    public DroiyanTreasureBO beRevengeRobbed() {
        return this.beRobbedByType("Revenge");
    }

    public DroiyanTreasureBO beRobbedByType(String type) {
        int now = CommTime.nowSecond();
        Optional<DroiyanTreasureBO> find = Optional.ofNullable(null);
        if ((!type.equals("Normal") || !this.isRed()) && (!type.equals("Revenge") || this.isRed())) {
            if (type.equals("Normal") && !this.isRed()) {
                find = this.treasures.stream().filter(box -> box.getExpireTime() > now).unordered().findAny();
            } else if (type.equals("Revenge") && this.isRed()) {
                List<DroiyanTreasureBO> findList = this.treasures.stream().filter(box -> box.getExpireTime() > now).collect(Collectors.toList());
                int quality = 0;

                for (DroiyanTreasureBO tmp_bo : findList) {
                    RefDroiyanTreasure tmp_ref = RefDataMgr.get(RefDroiyanTreasure.class, tmp_bo.getTreasureId());
                    if (tmp_ref.Quality > quality) {
                        quality = tmp_ref.Quality;
                    }
                }

                int quality1 = quality;
                find = findList.stream().filter(box -> this.getquality(box) == quality1).unordered().findAny();
            }
        } else {
            List<DroiyanTreasureBO> findList = this.treasures.stream().filter(box -> box.getExpireTime() > now).collect(Collectors.toList());
            List<Integer> rangeList = new ArrayList<>();
            List<RefDroiyanTreasure> refList = new ArrayList<>();

            for (DroiyanTreasureBO tmp_box : findList) {
                RefDroiyanTreasure tmp_ref = RefDataMgr.get(RefDroiyanTreasure.class, tmp_box.getTreasureId());
                if (!refList.contains(tmp_ref)) {
                    refList.add(tmp_ref);
                }
            }

            for (RefDroiyanTreasure ref : refList) {
                switch (type.hashCode()) {
                    case -1955878649:
                        if (type.equals("Normal")) {
                            rangeList.add(ref.NormalWeight);
                        }
                        break;
                    case -1530471862:
                        if (type.equals("Revenge")) {
                            rangeList.add(ref.RevengeWeight);
                        }
                }
            }

            int index = CommMath.getRandomIndexByRate(rangeList);
            if (index >= 0) {
                int id = refList.get(index).id;
                find = findList.stream().filter(box -> box.getTreasureId() == id).unordered().findAny();
            }
        }

        if (find != null && find.isPresent()) {
            DroiyanTreasureBO bo = find.get();
            this.treasures.remove(bo);
            this.player.pushProto("robbedTreasure", bo.getId());
            return bo;
        } else {
            return null;
        }
    }

    public void addTreasure(DroiyanTreasureBO treasure) {
        this.treasures.add(treasure);
    }

    public DroiyanTreasureBO addTreasure(RefDroiyanTreasure ref) {
        DroiyanTreasureBO treasure = new DroiyanTreasureBO();
        treasure.setPid(this.getPid());
        treasure.setTreasureId(ref.id);
        treasure.setGainTime(CommTime.nowSecond());
        treasure.setExpireTime(ref.Time + CommTime.nowSecond());
        treasure.insert();
        this.treasures.add(treasure);
        return treasure;
    }

    public void addEnemy(long pid) {
        if (this.enemies.size() < RefDataMgr.getFactor("DroiyanMaxEnemy", 20)) {
            this.enemies.add(pid);
            Player player = PlayerMgr.getInstance().getPlayer(pid);
            this.player.pushProto("newEnemy", player.getFeature(PlayerBase.class).summary());
        }
    }

    public boolean isEnemy(long pid) {
        return this.enemies.contains(pid);
    }

    public void damage() {
        if (this.bo.getRed() >= RefDataMgr.getFactor("Droiyan_Red", 80)) {
            this.player.getFeature(CharFeature.class).getAll().forEach((charid, character) -> {
                Integer curHp = this.hp.get(charid);
                if (curHp == null) {
                    curHp = 100;
                }

                int damage = CommMath.randomInt(RefDataMgr.getFactor("Droiyan_RedDamageMin"), RefDataMgr.getFactor("Droiyan_RedDamageMax"));
                this.hp.put(charid, Math.max(curHp - damage, 0));
            });
        }
    }

    public void reborn() {
        this.player.getFeature(CharFeature.class).getAll().forEach((charid, character) -> this.hp.put(charid, 100));
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
        if (times > 0) {
            List<Long> list = this.ranOpponent(this.getPid(), 4);
            list.removeAll(this.bo.getFightersAll());

            for (int i = 0; i < this.bo.getFightersSize() && times > 0; i++) {
                long cur = this.bo.getFighters(i);
                if (cur == 0L) {
                    times--;
                    this.bo.setFighters(i, list.remove(0));
                }
            }

            this.bo.setLastSearchTime(CommTime.nowSecond());
            this.bo.saveAll();
        }
    }

    public long search() {
        List<Long> list = this.ranOpponent(this.getPid(), 4);
        list.removeAll(this.bo.getFightersAll());

        for (int i = 0; i < this.bo.getFightersSize(); i++) {
            long cur = this.bo.getFighters(i);
            if (cur == 0L) {
                this.bo.setFighters(i, list.remove(0));
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
                    if (tmp_player.getFeature(DroiyanFeature.class).haveTreature()) {
                        treasurePid.add(tmp_pid);
                    } else {
                        realPid.add(tmp_pid);
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

            for (int rx = Math.max(1, beginA); rx <= maxA; rx++) {
                if (rx != rankA) {
                    list.add(ArenaManager.getInstance().getRank().get(rx - 1).getPid());
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
            if (bo.getExpireTime() > now) {
                return true;
            }
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
            throw new WSException(ErrorCode.Droiyan_TreasureNoFound, "%s玩家不持有宝物%s", this.getPid(), treatureId);
        } else if (find.getExpireTime() > CommTime.nowSecond()) {
            throw new WSException(ErrorCode.Droiyan_TreasureTimeLimit, "%s玩家宝物%s时间未到", this.getPid(), treatureId);
        } else {
            PlayerRecord record = this.player.getFeature(PlayerRecord.class);
            if (record.getValue(DailyRefresh.OpenTreasure) >= RefDataMgr.get(RefVIP.class, this.player.getVipLevel()).OpenTreasureTimes) {
                throw new WSException(ErrorCode.Droiyan_TreasureOpenTimes, "藏宝图开启次数已满");
            } else {
                find.del();
                this.treasures.remove(find);
                RefDroiyanTreasure ref = RefDataMgr.get(RefDroiyanTreasure.class, find.getTreasureId());
                Reward reward = RefDataMgr.get(RefReward.class, ref.Reward).genReward();
                this.player.getFeature(PlayerItem.class).gain(reward, ItemFlow.DroiyanTreasure);
                EncouterManager.getInstance().addNews(EncouterManager.NewsType.Open, this.getPlayerName(), find.getTreasureId());
                record.addValue(DailyRefresh.OpenTreasure);
                NoticeMgr.getInstance().sendMarquee(UniverseMessageType.UseTreasure, this.player.getName());
                return reward;
            }
        }
    }

    public Map<Integer, Integer> getHpMap() {
        return this.hp;
    }

    public List<Long> getEnemies() {
        return this.enemies;
    }

    public boolean popEnemy(long targetPid) {
        return this.enemies.remove(targetPid);
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
        return this.treasures.size() >= RefDataMgr.getFactor("MaxTreasureSize", 5);
    }

    public boolean isRed() {
        return this.bo.getRed() >= RefDataMgr.getFactor("Droiyan_MaxRed", 100);
    }

    public int getquality(DroiyanTreasureBO bo) {
        RefDroiyanTreasure ref = RefDataMgr.get(RefDroiyanTreasure.class, bo.getTreasureId());
        return ref.Quality;
    }
}
