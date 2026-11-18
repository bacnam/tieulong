package business.player.feature.character;

import business.player.Player;
import business.player.feature.Feature;
import business.player.item.IItemFilter;
import business.player.item.IUniItemContainer;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.common.enums.UnlockType;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefPlayerInit;
import core.config.refdata.ref.RefUnlockFunction;
import core.database.game.bo.WarSpiritBO;
import core.network.proto.WarSpiritInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WarSpiritFeature extends Feature implements IUniItemContainer<Equip> {
    private Map<Integer, WarSpirit> warSpirits = new HashMap<>();
    private WarSpirit warSpiritNow = null;

    public WarSpiritFeature(Player owner) {
        super(owner);
    }

    public WarSpirit getWarSpiritNow() {
        return this.warSpiritNow;
    }

    public void setWarSpiritNow(WarSpirit warSpiritNow) throws WSException {
        UnlockType type = UnlockType.valueOf("WarSpirit");
        RefUnlockFunction.checkUnlock(this.player, type);
        if (this.warSpiritNow != null && this.warSpiritNow != warSpiritNow) {
            this.warSpiritNow.getBo().saveIsSelected(false);
        }

        this.warSpiritNow = warSpiritNow;
        this.warSpiritNow.getBo().saveIsSelected(true);
    }

    @Override
    public void loadDB() {
        for (WarSpiritBO bo : BM.getBM(WarSpiritBO.class).findAll("pid", this.player.getPid())) {
            WarSpirit spirit = new WarSpirit(this.player, bo);
            this.warSpirits.put(bo.getSpiritId(), spirit);
            if (bo.getIsSelected()) {
                this.warSpiritNow = spirit;
            }
        }
    }

    public WarSpirit getWarSpirit(int id) {
        return this.warSpirits.get(id);
    }

    public int unlockWarSpirit(int selected) throws WSException {
        if (this.warSpirits.containsKey(selected)) {
            throw new WSException(ErrorCode.Player_AlreadyExist, "该战灵[%s]已解锁", selected);
        } else {
            WarSpiritBO bo = new WarSpiritBO();
            bo.setPid(this.player.getPid());
            bo.setSpiritId(selected);
            bo.insert();
            WarSpirit sp = new WarSpirit(this.player, bo);
            this.warSpirits.put(sp.getSpiritId(), sp);
            if (this.player.getPlayerBO().getWarspiritLv() <= 0) {
                this.player.getPlayerBO().saveWarspiritLv(RefDataMgr.get(RefPlayerInit.class, 0).WarspiritLv);
                this.player.pushProto("initWarspiritLv", this.player.getPlayerBO().getWarspiritLv());
            }

            this.player.pushProto("newWarSpirit", new WarSpiritInfo(sp));
            return bo.getSpiritId();
        }
    }

    public List<WarSpiritInfo> getAllInfo() {
        List<WarSpiritInfo> list = new ArrayList<>();
        this.warSpirits.values().forEach(spirit -> list.add(new WarSpiritInfo(spirit)));
        return list;
    }

    public int getPower() {
        return this.warSpiritNow != null ? this.warSpiritNow.getPower() : 0;
    }

    public void updatePower() {
        if (this.warSpiritNow != null) {
            this.warSpiritNow.onAttrChanged();
        }
    }

    @Override
    public PrizeType getType() {
        return PrizeType.Warspirit;
    }

    @Override
    public boolean check(int id, int count, IItemFilter<Equip> filter) {
        return false;
    }

    public Equip consume(int id, int count, ItemFlow reason, IItemFilter<Equip> filter) {
        return null;
    }

    @Override
    public int gain(int id, int count, ItemFlow reason) {
        return 0;
    }
}
