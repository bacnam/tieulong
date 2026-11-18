package business.global.arena;

import BaseCommon.CommLog;
import business.global.gmmail.MailCenter;
import business.player.Player;
import business.player.RobotManager;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.enums.RankType;
import com.zhonglian.server.common.utils.CommMath;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.common.utils.Lists;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefRankReward;
import core.database.game.bo.ArenaCompetitorBO;
import core.database.game.bo.ArenaFightRecordBO;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

public class ArenaManager {
    private static ArenaManager instance = new ArenaManager();
    public static final int Max_Record = 20;
    private Map<Long, Competitor> competitors = new HashMap<>();
    private List<Competitor> rank = new ArrayList<>();
    private Map<Long, Queue<ArenaFightRecordBO>> records = new HashMap<>();

    private ArenaManager() {
    }

    public static ArenaManager getInstance() {
        return instance;
    }

    public List<Competitor> getRank() {
        return this.rank;
    }

    public void setRank(List<Competitor> rank) {
        this.rank = rank;
    }

    public void init() {
        for (ArenaCompetitorBO bo : BM.getBM(ArenaCompetitorBO.class).findAll()) {
            this.competitors.put(bo.getPid(), new Competitor(bo));
        }

        this.resortRanks();
        List<Player> robots = Lists.newArrayList(RobotManager.getInstance().getAll().values());
        robots.sort((o1, o2) -> o2.getPlayerBO().getMaxFightPower() - o1.getPlayerBO().getMaxFightPower());

        for (Player player : robots) {
            if (player.getLv() <= RefDataMgr.getFactor("ArenaMaxLevel", 89)) {
                this.getOrCreate(player.getPid());
            }
        }

        List<ArenaFightRecordBO> records = BM.getBM(ArenaFightRecordBO.class).findAll();
        records.sort((left, right) -> left.getEndTime() - right.getEndTime());
        int cur = CommTime.nowSecond();

        for (ArenaFightRecordBO bo : records) {
            if (cur > bo.getBeginTime() + 1209600) {
                bo.del();
            } else {
                this.addFightRecord(bo);
            }
        }
    }

    public Competitor getOrCreate(long pid) {
        Competitor competitor = this.competitors.get(pid);
        if (competitor != null) {
            return competitor;
        } else {
            synchronized (this.competitors) {
                competitor = this.competitors.get(pid);
                if (competitor != null) {
                    return competitor;
                }

                ArenaCompetitorBO bo = new ArenaCompetitorBO();
                bo.setPid(pid);
                bo.setRank(this.competitors.size() + 1);
                bo.setHighestRank(bo.getRank());
                bo.setLastRankTime(CommTime.nowSecond());
                bo.insert();
                competitor = new Competitor(bo);
                this.competitors.put(pid, competitor);
            }

            synchronized (this.rank) {
                this.rank.add(competitor);
                return competitor;
            }
        }
    }

    public void addFightRecord(ArenaFightRecordBO record) {
        synchronized (this.records) {
            this.addFightRecord(record, record.getAtkPid());
            this.addFightRecord(record, record.getDefPid());
        }
    }

    private void addFightRecord(ArenaFightRecordBO record, long pid) {
        Queue<ArenaFightRecordBO> queue = this.records.get(pid);
        if (queue == null) {
            this.records.put(pid, queue = new LinkedList<>());
        }

        queue.add(record);
        if (queue.size() > 20) {
            this.tryDel(queue.poll());
        }
    }

    private void tryDel(ArenaFightRecordBO bo) {
        Queue<ArenaFightRecordBO> atkBos = this.records.get(bo.getAtkPid());
        Queue<ArenaFightRecordBO> defBos = this.records.get(bo.getDefPid());
        if ((atkBos == null || !atkBos.contains(bo)) && (defBos == null || !defBos.contains(bo))) {
            bo.del();
        }
    }

    private void resortRanks() {
        Collection<Competitor> curCompetitors = new ArrayList<>();
        synchronized (this.competitors) {
            curCompetitors.addAll(this.competitors.values());
        }

        List<Competitor> ranklist = new ArrayList<>();
        List<Competitor> idlelist = new ArrayList<>();

        for (int index = 1; index <= this.competitors.size(); index++) {
            ranklist.add(null);
        }

        for (Competitor competitor : curCompetitors) {
            int rank = competitor.bo.getRank();
            if (rank <= 0 || rank > this.competitors.size()) {
                idlelist.add(competitor);
            }

            Competitor preOne = ranklist.get(rank - 1);
            if (preOne == null) {
                ranklist.set(rank - 1, competitor);
            } else if (competitor.bo.getLastRankTime() > preOne.bo.getLastRankTime()) {
                ranklist.set(rank - 1, competitor);
                idlelist.add(preOne);
            } else {
                idlelist.add(competitor);
            }
        }

        if (idlelist.size() != 0) {
            idlelist.sort(
                (left, right) -> left.bo.getRank() != right.bo.getRank()
                    ? left.bo.getRank() - right.bo.getRank()
                    : right.bo.getLastRankTime() - left.bo.getLastRankTime()
            );
            int next = 0;

            for (Competitor arenaInfo : idlelist) {
                while (next < ranklist.size()) {
                    if (ranklist.get(next) == null) {
                        ranklist.set(next, arenaInfo);
                        int preRank = arenaInfo.bo.getRank();
                        arenaInfo.bo.saveRank(next + 1);
                        arenaInfo.bo.saveLastRankTime(CommTime.nowSecond());
                        CommLog.warn("arena fix rank cid:{} preRank:{} newRank:{}", arenaInfo.bo.getPid(), preRank, arenaInfo.bo.getRank());
                        break;
                    }

                    next++;
                }
            }
        }

        synchronized (this.rank) {
            this.rank = ranklist;
        }
    }

    public List<Competitor> getOpponents(int rank) {
        List<Competitor> list = new ArrayList<>();
        int OpponentsSize = ArenaConfig.opponentsSize();
        if (rank <= OpponentsSize) {
            for (int r = 1; r <= OpponentsSize + 1; r++) {
                if (r != rank) {
                    list.add(this.rank.get(r - 1));
                }
            }
        } else if (rank < 50) {
            List<Competitor> head = this.getCompetitors(rank - OpponentsSize * 2, rank, rank);
            if (head.size() > OpponentsSize - 1) {
                Collections.shuffle(head);
                list.addAll(head.subList(0, 4));
            }

            int more = OpponentsSize - list.size();
            List<Competitor> tail = this.getCompetitors(rank + 1, rank + more * 2, rank);
            Collections.shuffle(tail);
            list.addAll(tail.subList(0, more));
        } else {
            int offset = ArenaConfig.opponentsOffset();
            int rx = rank * 17 / 16;
            if (rx <= this.rank.size()) {
                List<Competitor> tail = this.getCompetitors(rx - offset, rx + offset, rank);
                list.add(CommMath.randomOne(tail));
                OpponentsSize--;
            }

            for (int i = 1; i <= OpponentsSize; i++) {
                rx = rank * (16 - i) / 16;
                List<Competitor> head = this.getCompetitors(rx - offset, rx + offset, rank);
                head.removeAll(list);
                list.add(CommMath.randomOne(head));
            }
        }

        list.sort((left, right) -> left.bo.getRank() - right.bo.getRank());
        return list;
    }

    private List<Competitor> getCompetitors(int begin, int end, int exclude) {
        List<Competitor> list = new ArrayList<>();
        int max = Math.min(end, this.rank.size());

        for (int r = Math.max(1, begin); r <= max; r++) {
            if (r != exclude) {
                list.add(this.rank.get(r - 1));
            }
        }

        return list;
    }

    public List<Competitor> getRankList(int size) {
        synchronized (this.rank) {
            return this.rank.subList(0, Math.min(size, this.rank.size()));
        }
    }

    public void swithRank(int r1, int r2) {
        Competitor competitor1 = this.rank.get(r1 - 1);
        Competitor competitor2 = this.rank.get(r2 - 1);
        competitor1.bo.setRank(r2);
        competitor2.bo.setRank(r1);
        int cur = CommTime.nowSecond();
        competitor1.bo.setLastRankTime(cur);
        competitor2.bo.setLastRankTime(cur);
        synchronized (this.rank) {
            this.rank.set(r2 - 1, competitor1);
            this.rank.set(r1 - 1, competitor2);
        }

        competitor1.bo.saveAll();
        competitor2.bo.saveAll();
        competitor1.resetOpponents();
        competitor2.resetOpponents();
    }

    public Collection<ArenaFightRecordBO> getFightRecords(long pid) {
        return this.records.get(pid);
    }

    public void settle() {
        try {
            List<Competitor> ranklist = new ArrayList<>();
            synchronized (this.rank) {
                ranklist.addAll(this.rank);
            }

            for (RefRankReward ref : RefRankReward.getRewards(RankType.Arena)) {
                for (int rnk = ref.MinRank; rnk <= ref.MaxRank && rnk <= ranklist.size(); rnk++) {
                    Competitor r = ranklist.get(rnk - 1);
                    if (r != null) {
                        MailCenter.getInstance().sendMail(r.getPid(), ref.MailId, String.valueOf(rnk));
                    }
                }
            }
        } catch (Exception var8) {
        }
    }

    public List<Long> randCompetitor(long pid, int size) {
        Competitor competitor = this.competitors.get(pid);
        int rank = competitor == null ? this.rank.size() : competitor.getRank();
        int offset = 50 + size * 2;
        List<Competitor> list = this.getCompetitors(rank - offset, rank + offset, rank);
        Collections.shuffle(list);
        return list.subList(0, size).stream().map(c -> c.getPid()).collect(Collectors.toList());
    }
}
