package business.player.feature.pvp;

import business.player.Player;
import business.player.feature.Feature;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.common.utils.Random;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefVIP;
import core.database.game.bo.StealGoldBO;
import core.database.game.bo.StealGoldNewsBO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StealGoldFeature extends Feature {
    public static final int MAX_FIGHTERS = 4;
    private StealGoldBO bo;
    private List<StealGoldNewsBO> news;
    private List<Long> fighters;
    private List<Integer> extmoney;
    public StealGoldFeature(Player player) {
        super(player);

        this.news = new ArrayList<>();

        this.fighters = new ArrayList<>();
        this.extmoney = new ArrayList<>();
    }

    public void loadDB() {
        this.bo = (StealGoldBO) BM.getBM(StealGoldBO.class).findOne("pid", Long.valueOf(getPid()));
        this.news = BM.getBM(StealGoldNewsBO.class).findAll("pid", Long.valueOf(getPid()));
        if (this.bo == null) {
            this.bo = new StealGoldBO();
            this.bo.setPid(getPid());
            this.bo.insert();
        }
        this.fighters = this.bo.getFightersAll();
    }

    public int addTimes() {
        this.bo.saveTimes(this.bo.getTimes() + 1);
        return this.bo.getTimes();
    }

    public int addTimes(int value) {
        this.bo.saveTimes(this.bo.getTimes() + value);
        return this.bo.getTimes();
    }

    public int getTimes() {
        return this.bo.getTimes();
    }

    public boolean checkTimes() {
        return (((RefVIP) RefDataMgr.get(RefVIP.class, Integer.valueOf(this.player.getVipLevel()))).StealGold >= this.bo.getTimes());
    }

    public List<Long> getList() {
        for (Iterator<Long> iterator = this.fighters.iterator(); iterator.hasNext(); ) {
            long i = ((Long) iterator.next()).longValue();
            if (i == 0L) {
                search();

                break;
            }
        }

        return this.fighters;
    }

    public List<Integer> getMoneyList() {
        return this.extmoney;
    }

    public void search() {
        List<Long> enemies = ((DroiyanFeature) this.player.getFeature(DroiyanFeature.class)).getEnemies();
        List<Long> players = ((DroiyanFeature) this.player.getFeature(DroiyanFeature.class)).ranOpponent(this.player.getPid(), 5);
        List<Long> ranplayers = new ArrayList<>();
        ranplayers.addAll(enemies);
        ranplayers.removeAll(players);
        ranplayers.addAll(players);
        for (int i = 0; i < 4; i++) {
            this.fighters.set(i, ranplayers.get(i));
            if (this.extmoney.size() != 4) {
                this.extmoney.add(Integer.valueOf(Random.nextInt(200, -100)));
            } else {
                this.extmoney.set(i, Integer.valueOf(Random.nextInt(200, -100)));
            }
        }
    }

    public void addNews(long stealpid, int money) {
        StealGoldNewsBO newbo = new StealGoldNewsBO();
        newbo.setPid(this.player.getPid());
        newbo.setAtkid(stealpid);
        newbo.setMoney(money);
        newbo.setTime(CommTime.nowSecond());
        newbo.insert();
        this.news.add(newbo);
    }

    public List<StealGoldNewsBO> getNews() {
        return this.news;
    }

    public void dailyRefresh() {
        try {
            search();
            this.bo.saveTimes(0);
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}

