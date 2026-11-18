package business.player.feature.treasure;

import business.global.activity.ActivityMgr;
import business.global.notice.NoticeMgr;
import business.player.Player;
import business.player.feature.Feature;
import business.player.item.Reward;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.common.enums.Quality;
import com.zhonglian.server.common.enums.RankType;
import com.zhonglian.server.common.enums.ConstEnum.FindTreasureType;
import com.zhonglian.server.common.enums.ConstEnum.UniverseMessageType;
import com.zhonglian.server.common.utils.CommMath;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefTreasure;
import core.config.refdata.ref.RefUniformItem;
import core.database.game.bo.PlayerFindTreasureBO;
import java.util.ArrayList;

public class FindTreasureFeature extends Feature {
    public PlayerFindTreasureBO findTreasure;

    public FindTreasureFeature(Player player) {
        super(player);
    }

    @Override
    public void loadDB() {
        this.findTreasure = BM.getBM(PlayerFindTreasureBO.class).findOne("pid", this.player.getPid());
    }

    public PlayerFindTreasureBO getOrCreate() {
        PlayerFindTreasureBO bo = this.findTreasure;
        if (bo != null) {
            return bo;
        } else {
            synchronized (this) {
                bo = this.findTreasure;
                if (bo != null) {
                    return bo;
                } else {
                    bo = new PlayerFindTreasureBO();
                    bo.setPid(this.player.getPid());
                    bo.setTotal(0);
                    bo.insert();
                    this.findTreasure = bo;
                    return bo;
                }
            }
        }
    }

    public RefTreasure selectRef(int level) {
        for (RefTreasure ref : RefDataMgr.getAll(RefTreasure.class).values()) {
            if (ref.LevelRange.within(level)) {
                return ref;
            }
        }

        return null;
    }

    public Reward findTen() {
        PlayerFindTreasureBO findTreasure = this.getOrCreate();
        findTreasure.saveTentimes(this.findTreasure.getTentimes() + 1);
        RefTreasure ref = this.selectRef(this.player.getLv());
        ArrayList<Integer> weightList = ref.FixedTenWeightList;
        ArrayList<Integer> idList = ref.FixedTenIdList;
        ArrayList<Integer> countList = ref.FixedTenCountList;
        Reward reward = null;

        for (int i = 0; i < 9; i++) {
            int index = CommMath.getRandomIndexByRate(weightList);
            int uniformID = idList.get(index);
            int count = countList.get(index);
            if (reward != null) {
                reward.add(uniformID, count);
            } else {
                reward = new Reward(uniformID, count);
            }

            RefUniformItem refUniform = RefDataMgr.get(RefUniformItem.class, uniformID);
            if (refUniform.Quality == Quality.Red.ordinal() && refUniform.Type == PrizeType.Equip) {
                NoticeMgr.getInstance().sendMarquee(UniverseMessageType.FindTreasure, this.player.getName(), refUniform.Name);
            }
        }

        int indexx = CommMath.getRandomIndexByRate(ref.LeastTenWeightList);
        int uniformIDx = ref.LeastIdList.get(indexx);
        int countx = ref.LeastTenCountList.get(indexx);
        RefUniformItem refUniform = RefDataMgr.get(RefUniformItem.class, uniformIDx);
        if (refUniform.Quality == Quality.Red.ordinal() && refUniform.Type == PrizeType.Equip) {
            NoticeMgr.getInstance().sendMarquee(UniverseMessageType.FindTreasure, this.player.getName(), refUniform.Name);
        }

        reward.add(uniformIDx, countx);
        reward.combine(this.getExtReward(10));
        return reward;
    }

    public Reward find() {
        PlayerFindTreasureBO findTreasure = this.getOrCreate();
        findTreasure.saveTimes(this.findTreasure.getTimes() + 1);
        RefTreasure ref = this.selectRef(this.player.getLv());
        int total = 0;
        total = this.getOrCreate().getTotal();
        ArrayList<Integer> weightList;
        ArrayList<Integer> idList;
        ArrayList<Integer> countList;
        if (total >= 9) {
            weightList = ref.LeastTenWeightList;
            idList = ref.LeastIdList;
            countList = ref.LeastTenCountList;
        } else {
            weightList = ref.NormalWeightList;
            idList = ref.NormalIdList;
            countList = ref.NormalCountList;
        }

        int index = CommMath.getRandomIndexByRate(weightList);
        int uniformID = idList.get(index);
        int count = countList.get(index);
        this.getOrCreate().saveTotal(total + 1);
        if (this.getOrCreate().getTotal() == 10) {
            this.getOrCreate().saveTotal(0);
        }

        Reward reward = new Reward(uniformID, count);
        reward.combine(this.getExtReward(1));
        RefUniformItem refUniform = RefDataMgr.get(RefUniformItem.class, uniformID);
        if (refUniform.Quality == Quality.Red.ordinal() && refUniform.Type == PrizeType.Equip) {
            NoticeMgr.getInstance().sendMarquee(UniverseMessageType.FindTreasure, this.player.getName(), refUniform.Name);
        }

        return reward;
    }

    public Reward findTreasure(int times) {
        ActivityMgr.updateWorldRank(this.player, times, RankType.WorldTreasure);
        if (times == 1) {
            return this.find();
        } else {
            return times == 10 ? this.findTen() : null;
        }
    }

    public Reward getExtReward(int times) {
        RefTreasure ref = this.selectRef(this.player.getLv());
        Reward reward = new Reward();

        for (int i = 0; i < times; i++) {
            reward.add(ref.UniformId, ref.UniformCount);
        }

        return reward;
    }

    public int getLeftTimes(FindTreasureType find) {
        if (find == FindTreasureType.single) {
            int times = RefDataMgr.getFactor("findTreasureTimes", 20);
            return times < 0 ? times : times - this.getOrCreate().getTimes();
        } else if (find == FindTreasureType.Ten) {
            int times = RefDataMgr.getFactor("findTreasureTentimes", 2);
            return times < 0 ? times : RefDataMgr.getFactor("findTreasureTentimes", 2) - this.getOrCreate().getTentimes();
        } else {
            return 0;
        }
    }
}
