package business.player.feature.treasure;

import business.player.Player;
import business.player.feature.Feature;
import business.player.item.Reward;
import business.player.item.UniformItem;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.utils.CommMath;
import com.zhonglian.server.common.utils.CommTime;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefTreasureWarspirit;
import core.config.refdata.ref.RefUniformItem;
import core.config.refdata.ref.RefWarSpirit;
import core.database.game.bo.PlayerFindTreasureBO;
import core.database.game.bo.WarSpiritTreasureRecordBO;
import java.util.ArrayList;

public class WarSpiritTreasureFeature extends Feature {
    public PlayerFindTreasureBO findTreasure;

    public WarSpiritTreasureFeature(Player player) {
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

    public RefTreasureWarspirit selectRef(int level) {
        for (RefTreasureWarspirit ref : RefDataMgr.getAll(RefTreasureWarspirit.class).values()) {
            if (ref.LevelRange.within(level)) {
                return ref;
            }
        }

        return null;
    }

    public Reward findTen() {
        RefTreasureWarspirit ref = this.selectRef(this.player.getLv());
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
        }

        int index = CommMath.getRandomIndexByRate(ref.LeastTenWeightList);
        int uniformID = ref.LeastIdList.get(index);
        int count = ref.LeastTenCountList.get(index);
        reward.add(uniformID, count);
        return reward;
    }

    public Reward find() {
        RefTreasureWarspirit ref = this.selectRef(this.player.getLv());
        int total = 0;
        total = this.getOrCreate().getWarspiritTotal();
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
        this.getOrCreate().saveWarspiritTotal(total + 1);
        if (this.getOrCreate().getWarspiritTotal() == 10) {
            this.getOrCreate().saveWarspiritTotal(0);
        }

        return new Reward(uniformID, count);
    }

    public Reward findTreasure(int times) {
        Reward reward = null;
        if (times == 1) {
            reward = this.find();
        }

        if (times == 10) {
            reward = this.findTen();
        }

        if (reward != null) {
            for (UniformItem item : reward) {
                RefUniformItem ref = RefDataMgr.get(RefUniformItem.class, item.getUniformId());
                RefWarSpirit refwarspirit = RefDataMgr.get(RefWarSpirit.class, ref.ItemID);
                if (refwarspirit != null) {
                    WarSpiritTreasureRecordBO bo = new WarSpiritTreasureRecordBO();
                    bo.savePid(this.player.getPid());
                    bo.setSpiritId(refwarspirit.id);
                    bo.setTime(CommTime.nowSecond());
                    bo.insert();
                    WarSpiritTreasureRecord.getInstance().add(bo);
                }
            }
        }

        return reward;
    }
}
