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
private Map<Integer, WarSpirit> warSpirits;
private WarSpirit warSpiritNow;

public WarSpiritFeature(Player owner) {
super(owner);

this.warSpirits = new HashMap<>();

this.warSpiritNow = null;
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

public void loadDB() {
List<WarSpiritBO> bos = BM.getBM(WarSpiritBO.class).findAll("pid", Long.valueOf(this.player.getPid()));
for (WarSpiritBO bo : bos) {
WarSpirit spirit = new WarSpirit(this.player, bo);
this.warSpirits.put(Integer.valueOf(bo.getSpiritId()), spirit);
if (bo.getIsSelected()) {
this.warSpiritNow = spirit;
}
} 
}

public WarSpirit getWarSpirit(int id) {
return this.warSpirits.get(Integer.valueOf(id));
}

public int unlockWarSpirit(int selected) throws WSException {
if (this.warSpirits.containsKey(Integer.valueOf(selected))) {
throw new WSException(ErrorCode.Player_AlreadyExist, "该战灵[%s]已解锁", new Object[] { Integer.valueOf(selected) });
}
WarSpiritBO bo = new WarSpiritBO();
bo.setPid(this.player.getPid());
bo.setSpiritId(selected);
bo.insert();
WarSpirit sp = new WarSpirit(this.player, bo);
this.warSpirits.put(Integer.valueOf(sp.getSpiritId()), sp);

if (this.player.getPlayerBO().getWarspiritLv() <= 0) {
this.player.getPlayerBO().saveWarspiritLv(((RefPlayerInit)RefDataMgr.get(RefPlayerInit.class, Integer.valueOf(0))).WarspiritLv);
this.player.pushProto("initWarspiritLv", Integer.valueOf(this.player.getPlayerBO().getWarspiritLv()));
} 

this.player.pushProto("newWarSpirit", new WarSpiritInfo(sp));
return bo.getSpiritId();
}

public List<WarSpiritInfo> getAllInfo() {
List<WarSpiritInfo> list = new ArrayList<>();
this.warSpirits.values().forEach(spirit -> paramList.add(new WarSpiritInfo(spirit)));

return list;
}

public int getPower() {
if (this.warSpiritNow != null) {
return this.warSpiritNow.getPower();
}
return 0;
}

public void updatePower() {
if (this.warSpiritNow != null) {
this.warSpiritNow.onAttrChanged();
}
}

public PrizeType getType() {
return PrizeType.Warspirit;
}

public boolean check(int id, int count, IItemFilter<Equip> filter) {
return false;
}

public Equip consume(int id, int count, ItemFlow reason, IItemFilter<Equip> filter) {
return null;
}

public int gain(int id, int count, ItemFlow reason) {
return 0;
}
}

