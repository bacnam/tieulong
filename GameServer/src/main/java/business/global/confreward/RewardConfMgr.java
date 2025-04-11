package business.global.confreward;

import business.player.Player;
import business.player.item.Reward;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.utils.CommString;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.common.utils.Maps;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefUniformItem;
import core.database.game.bo.ConfRewardBO;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RewardConfMgr {
    private static RewardConfMgr instance = null;
    public Map<Integer, ConfRewardBO> confRewardMap;

    public RewardConfMgr() {
        this.confRewardMap = Maps.newConcurrentMap();
    }

    public static RewardConfMgr getInstance() {
        if (instance == null) {
            instance = new RewardConfMgr();
        }
        return instance;
    }

    public void init() {
        this.confRewardMap.clear();
        for (ConfRewardBO bo : BM.getBM(ConfRewardBO.class).findAll()) {
            this.confRewardMap.put(Integer.valueOf(bo.getRewardID()), bo);
        }
    }

    public ConfRewardBO getConfRewardBO(int rewardUniformId) {
        return this.confRewardMap.get(Integer.valueOf(rewardUniformId));
    }

    public String getConfRewardName(int rewardUniformId) {
        ConfRewardBO bo = getConfRewardBO(rewardUniformId);
        if (bo != null) {
            return bo.getItemsName();
        }
        return "";
    }

    public Reward buyConfReward(int rewardUniformId, int count) {
        ConfRewardBO bo = this.confRewardMap.get(Integer.valueOf(rewardUniformId));
        if (bo == null) {
            return new Reward();
        }
        List<Integer> idList = CommString.getIntegerList(bo.getItemID(), ";");
        List<Integer> countList = (List<Integer>) CommString.getIntegerList(bo.getItemCount(), ";").stream().map(x -> Integer.valueOf(x.intValue() * paramInt)).collect(Collectors.toList());
        return new Reward(idList, countList);
    }

    public boolean gmConfRewardBO(Player player, int rewardUniformId, int price) throws WSException {
        if (RefDataMgr.get(RefUniformItem.class, Integer.valueOf(rewardUniformId)) != null) {
            throw new WSException(ErrorCode.InvalidParam, "非法的参数:rewardId=%s，rewardId不能为UniformItem表的ID", new Object[]{Integer.valueOf(rewardUniformId)});
        }
        ConfRewardBO bo = getConfRewardBO(rewardUniformId);
        if (bo != null) {
            throw new WSException(ErrorCode.InvalidParam, "非法的参数:rewardId=%s，礼包已存在", new Object[]{Integer.valueOf(rewardUniformId)});
        }
        bo = new ConfRewardBO();
        bo.setRewardID(rewardUniformId);
        bo.setPrice(price);
        bo.setCreateTime(CommTime.nowSecond());
        bo.setName("测试礼包");
        bo.setRewardDescribe("测试礼包");
        bo.setIconID("Gem_Earth");
        bo.setItemID("11001;11002");
        bo.setItemCount("1;2");
        bo.insert_sync();
        this.confRewardMap.put(Integer.valueOf(rewardUniformId), bo);
        player.pushProto("RequestConfRewardInfo", "");
        return true;
    }
}

