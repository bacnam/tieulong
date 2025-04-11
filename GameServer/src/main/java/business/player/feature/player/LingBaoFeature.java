package business.player.feature.player;

import business.player.Player;
import business.player.feature.Feature;
import business.player.feature.PlayerItem;
import business.player.feature.character.CharFeature;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.enums.UnlockType;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefLingBao;
import core.config.refdata.ref.RefUnlockFunction;
import core.database.game.bo.LingbaoBO;

public class LingBaoFeature
        extends Feature {
    LingbaoBO bo;

    public LingBaoFeature(Player player) {
        super(player);
    }

    public void loadDB() {
        this.bo = (LingbaoBO) BM.getBM(LingbaoBO.class).findOne("pid", Long.valueOf(this.player.getPid()));
    }

    public LingbaoBO getOrCreate() {
        LingbaoBO bo = this.bo;
        if (bo != null) {
            return bo;
        }
        synchronized (this) {
            bo = this.bo;
            if (bo != null) {
                return bo;
            }
            bo = new LingbaoBO();
            bo.setPid(this.player.getPid());
            bo.insert();
            this.bo = bo;
        }
        return bo;
    }

    public int levelUp() throws WSException {
        RefUnlockFunction.checkUnlock(this.player, UnlockType.LingBao);

        LingbaoBO bo = getOrCreate();

        int level = bo.getLevel();
        if (level >= RefDataMgr.getAll(RefLingBao.class).size() - 1) {
            throw new WSException(ErrorCode.LingBao_Full, "灵宝等级已满");
        }

        RefLingBao ref = (RefLingBao) RefDataMgr.get(RefLingBao.class, Integer.valueOf(level + 1));
        RefLingBao now_ref = (RefLingBao) RefDataMgr.get(RefLingBao.class, Integer.valueOf(level));
        int gainExp = 0;

        if (now_ref.Level != ref.Level) {
            bo.saveLevel(bo.getLevel() + 1);
            ((CharFeature) this.player.getFeature(CharFeature.class)).updateCharPower();
            return 0;
        }

        boolean check = ((PlayerItem) this.player.getFeature(PlayerItem.class)).checkAndConsume(ref.CostIdList, ref.CostCountList, ItemFlow.LingBao);
        if (!check) {
            throw new WSException(ErrorCode.LingBao_NotEnough, "材料不足");
        }
        gainExp = RefDataMgr.getFactor("LingBaoExp", 10);
        int nowExp = bo.getExp() + gainExp;
        int levelUp = 0;
        for (int i = ref.id; i <= RefDataMgr.size(RefLingBao.class); i++) {
            RefLingBao temp_ref = (RefLingBao) RefDataMgr.get(RefLingBao.class, Integer.valueOf(i - 1));
            RefLingBao next_ref = (RefLingBao) RefDataMgr.get(RefLingBao.class, Integer.valueOf(i));

            if (next_ref == null)
                break;
            long needExp = next_ref.Exp;

            if (needExp > nowExp) {
                break;
            }
            if (temp_ref.Level != next_ref.Level) {
                break;
            }
            levelUp++;
            nowExp = (int) (nowExp - needExp);
        }
        bo.saveExp(nowExp);
        bo.saveLevel(level + levelUp);

        ((CharFeature) this.player.getFeature(CharFeature.class)).updateCharPower();

        return gainExp;
    }

    public int getLevel() {
        return getOrCreate().getLevel();
    }

    public int getExp() {
        return getOrCreate().getExp();
    }
}

