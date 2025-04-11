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

public class WarSpiritTreasureFeature
        extends Feature {
    public PlayerFindTreasureBO findTreasure;

    public WarSpiritTreasureFeature(Player player) {
        super(player);
    }

    public void loadDB() {
        this.findTreasure = (PlayerFindTreasureBO) BM.getBM(PlayerFindTreasureBO.class).findOne("pid", Long.valueOf(this.player.getPid()));
    }

    public PlayerFindTreasureBO getOrCreate() {
        PlayerFindTreasureBO bo = this.findTreasure;
        if (bo != null) {
            return bo;
        }
        synchronized (this) {
            bo = this.findTreasure;
            if (bo != null) {
                return bo;
            }
            bo = new PlayerFindTreasureBO();
            bo.setPid(this.player.getPid());
            bo.setTotal(0);
            bo.insert();
            this.findTreasure = bo;
        }
        return bo;
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
        RefTreasureWarspirit ref = selectRef(this.player.getLv());

        ArrayList<Integer> weightList = ref.FixedTenWeightList;
        ArrayList<Integer> idList = ref.FixedTenIdList;
        ArrayList<Integer> countList = ref.FixedTenCountList;
        Reward reward = null;
        for (int i = 0; i < 9; i++) {
            int j = CommMath.getRandomIndexByRate(weightList);
            int k = ((Integer) idList.get(j)).intValue();
            int m = ((Integer) countList.get(j)).intValue();
            if (reward != null) {
                reward.add(k, m);
            } else {
                reward = new Reward(k, m);
            }
        }

        int index = CommMath.getRandomIndexByRate(ref.LeastTenWeightList);
        int uniformID = ((Integer) ref.LeastIdList.get(index)).intValue();
        int count = ((Integer) ref.LeastTenCountList.get(index)).intValue();

        reward.add(uniformID, count);

        return reward;
    }

    public Reward find() {
        ArrayList<Integer> weightList, idList, countList;
        RefTreasureWarspirit ref = selectRef(this.player.getLv());

        int total = 0;
        total = getOrCreate().getWarspiritTotal();

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
        int uniformID = ((Integer) idList.get(index)).intValue();
        int count = ((Integer) countList.get(index)).intValue();
        getOrCreate().saveWarspiritTotal(total + 1);
        if (getOrCreate().getWarspiritTotal() == 10) {
            getOrCreate().saveWarspiritTotal(0);
        }
        Reward reward = new Reward(uniformID, count);

        return reward;
    }

    public Reward findTreasure(int times) {
        Reward reward = null;
        if (times == 1) {
            reward = find();
        }
        if (times == 10) {
            reward = findTen();
        }
        if (reward != null) {
            for (UniformItem item : reward) {
                RefUniformItem ref = (RefUniformItem) RefDataMgr.get(RefUniformItem.class, Integer.valueOf(item.getUniformId()));
                RefWarSpirit refwarspirit = (RefWarSpirit) RefDataMgr.get(RefWarSpirit.class, Integer.valueOf(ref.ItemID));
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

