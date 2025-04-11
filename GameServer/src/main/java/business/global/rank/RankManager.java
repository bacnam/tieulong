package business.global.rank;

import BaseCommon.CommClass;
import BaseCommon.CommLog;
import business.global.rank.catagere.GuildRank;
import business.global.rank.catagere.NormalRank;
import business.player.Player;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.enums.RankType;
import core.database.game.bo.RankRecordBO;

import java.util.*;

public class RankManager {
    private static RankManager instance = new RankManager();
    private Map<RankType, Rank> ranks;

    private RankManager() {
        this.ranks = new HashMap<>();
    }

    public static RankManager getInstance() {
        return instance;
    }

    public void init() {
        Set<Class<?>> clazzs = CommClass.getClasses(NormalRank.class.getPackage().getName());
        for (Class<?> clz : clazzs) {

            try {
                Class<? extends Rank> clazz = (Class) clz;
                Ranks types = clazz.<Ranks>getAnnotation(Ranks.class);
                if (types == null || (types.value()).length == 0) {
                    CommLog.error("[{}]的类型没有相关排行榜类型，请检查", clazz.getSimpleName());
                    System.exit(0);
                }
                byte b;
                int i;
                RankType[] arrayOfRankType;
                for (i = (arrayOfRankType = types.value()).length, b = 0; b < i; ) {
                    RankType type = arrayOfRankType[b];
                    if (this.ranks.containsKey(type)) {
                        String preClass = ((Rank) this.ranks.get(type)).getClass().getSimpleName();
                        CommLog.error("[{},{}]重复定义[{}]类型的排行榜", preClass, clazz.getSimpleName());
                        System.exit(0);
                    }
                    Rank instance = clazz.getConstructor(new Class[]{RankType.class}).newInstance(new Object[]{type});
                    this.ranks.put(type, instance);
                    b++;
                }

            } catch (Exception e) {
                CommLog.error("排行榜[{}]初始化失败", clz.getSimpleName(), e);
                System.exit(0);
            }
        }
        List<RankRecordBO> list = BM.getBM(RankRecordBO.class).findAll();
        for (RankRecordBO recordBO : list) {
            RankType type = RankType.values()[recordBO.getType()];
            Rank rank = this.ranks.get(type);
            Record record = new Record(recordBO);
            rank.map.put(Long.valueOf(recordBO.getOwner()), record);
        }
        for (Rank rank : this.ranks.values()) {
            rank.resort();
        }
    }

    public List<Record> getRankList(RankType type, int size) {
        List<Record> records, list = (getRank(type)).list;
        if (list.size() > size + 1) {
            records = list.subList(0, size + 1);
        } else {
            records = new ArrayList<>(list);
        }
        return records;
    }

    public long getPlayerId(RankType type, int rank) {
        int num;
        List<Record> records = (getRank(type)).list;

        if (rank < 1 || records.size() < 2) {
            return 0L;
        }
        if (records.size() - 1 < rank) {
            num = records.size() - 1;
        } else {
            num = rank;
        }

        return ((Record) records.get(num)).getPid();
    }

    public int getRank(RankType type, long ownerid) {
        return getRank(type).getRank(ownerid);
    }

    public long getValue(RankType type, long ownerid) {
        return getRank(type).getValue(ownerid);
    }

    public int update(RankType type, long ownerid, long value) {
        return getRank(type).update(ownerid, value, new long[0]);
    }

    public int update(RankType type, long ownerid, long value, long... ext) {
        return getRank(type).update(ownerid, value, ext);
    }

    public int minus(RankType type, long ownerid, int value) {
        return getRank(type).minus(ownerid, value);
    }

    private Rank getRank(RankType type) {
        Rank rank = this.ranks.get(type);
        if (rank == null) {
            CommLog.error("排行榜[]未注册", type);
        }
        return rank;
    }

    public int getRankSize(RankType type) {
        Rank rank = this.ranks.get(type);
        if (rank == null) {
            CommLog.error("排行榜[]未注册", type);
        }
        return rank.list.size();
    }

    public void settle(RankType type) {
        settle(type, false);
    }

    public void settle(RankType type, boolean clear) {
        try {
            Rank rank = getRank(type);
            rank.sendReward();
            if (clear) {
                rank.clear();
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void clear(RankType type) {
        getRank(type).clear();
    }

    public void clearPlayerData(Player player) {
        BM.getBM(RankRecordBO.class).delAll("owner", Long.valueOf(player.getPid()));
        Ranks types = NormalRank.class.<Ranks>getAnnotation(Ranks.class);
        byte b;
        int i;
        RankType[] arrayOfRankType;
        for (i = (arrayOfRankType = types.value()).length, b = 0; b < i; ) {
            RankType type = arrayOfRankType[b];
            ((Rank) this.ranks.get(type)).del(player.getPid());
            b++;
        }

    }

    public void clearPlayerData(Player player, RankType type) {
        ((Rank) this.ranks.get(type)).del(player.getPid());
    }

    public void clearGuildDataById(long ownerid) {
        BM.getBM(RankRecordBO.class).delAll("owner", Long.valueOf(ownerid));
        Ranks types = GuildRank.class.<Ranks>getAnnotation(Ranks.class);
        byte b;
        int i;
        RankType[] arrayOfRankType;
        for (i = (arrayOfRankType = types.value()).length, b = 0; b < i; ) {
            RankType type = arrayOfRankType[b];
            ((Rank) this.ranks.get(type)).del(ownerid);
            b++;
        }

    }
}

