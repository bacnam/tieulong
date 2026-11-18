package business.global.rank;

import BaseCommon.CommClass;
import BaseCommon.CommLog;
import business.global.rank.catagere.GuildRank;
import business.global.rank.catagere.NormalRank;
import business.player.Player;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.enums.RankType;
import core.database.game.bo.RankRecordBO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RankManager {
    private static RankManager instance = new RankManager();
    private Map<RankType, Rank> ranks = new HashMap<>();

    public static RankManager getInstance() {
        return instance;
    }

    private RankManager() {
    }

    public void init() {
        for (Class<?> clz : CommClass.getClasses(NormalRank.class.getPackage().getName())) {
            try {
                Class<? extends Rank> clazz = (Class<? extends Rank>)clz;
                Ranks types = clz.getAnnotation(Ranks.class);
                if (types == null || types.value().length == 0) {
                    CommLog.error("[{}]的类型没有相关排行榜类型，请检查", clz.getSimpleName());
                    System.exit(0);
                }

                RankType[] var9;
                for (RankType type : var9 = types.value()) {
                    if (this.ranks.containsKey(type)) {
                        String preClass = this.ranks.get(type).getClass().getSimpleName();
                        CommLog.error("[{},{}]重复定义[{}]类型的排行榜", preClass, clazz.getSimpleName());
                        System.exit(0);
                    }

                    Rank instance = clazz.getConstructor(RankType.class).newInstance(type);
                    this.ranks.put(type, instance);
                }
            } catch (Exception var11) {
                CommLog.error("排行榜[{}]初始化失败", clz.getSimpleName(), var11);
                System.exit(0);
            }
        }

        for (RankRecordBO recordBO : BM.getBM(RankRecordBO.class).findAll()) {
            RankType type = RankType.values()[recordBO.getType()];
            Rank rank = this.ranks.get(type);
            Record record = new Record(recordBO);
            rank.map.put(recordBO.getOwner(), record);
        }

        for (Rank rank : this.ranks.values()) {
            rank.resort();
        }
    }

    public List<Record> getRankList(RankType type, int size) {
        List<Record> list = this.getRank(type).list;
        List<Record> records;
        if (list.size() > size + 1) {
            records = list.subList(0, size + 1);
        } else {
            records = new ArrayList<>(list);
        }

        return records;
    }

    public long getPlayerId(RankType type, int rank) {
        List<Record> records = this.getRank(type).list;
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

    public int getRank(RankType type, long ownerid) {
        return this.getRank(type).getRank(ownerid);
    }

    public long getValue(RankType type, long ownerid) {
        return this.getRank(type).getValue(ownerid);
    }

    public int update(RankType type, long ownerid, long value) {
        return this.getRank(type).update(ownerid, value);
    }

    public int update(RankType type, long ownerid, long value, long... ext) {
        return this.getRank(type).update(ownerid, value, ext);
    }

    public int minus(RankType type, long ownerid, int value) {
        return this.getRank(type).minus(ownerid, value);
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
        this.settle(type, false);
    }

    public void settle(RankType type, boolean clear) {
        try {
            Rank rank = this.getRank(type);
            rank.sendReward();
            if (clear) {
                rank.clear();
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        }
    }

    public void clear(RankType type) {
        this.getRank(type).clear();
    }

    public void clearPlayerData(Player player) {
        BM.getBM(RankRecordBO.class).delAll("owner", player.getPid());
        Ranks types = NormalRank.class.getAnnotation(Ranks.class);

        RankType[] var6;
        for (RankType type : var6 = types.value()) {
            this.ranks.get(type).del(player.getPid());
        }
    }

    public void clearPlayerData(Player player, RankType type) {
        this.ranks.get(type).del(player.getPid());
    }

    public void clearGuildDataById(long ownerid) {
        BM.getBM(RankRecordBO.class).delAll("owner", ownerid);
        Ranks types = GuildRank.class.getAnnotation(Ranks.class);

        RankType[] var7;
        for (RankType type : var7 = types.value()) {
            this.ranks.get(type).del(ownerid);
        }
    }
}
