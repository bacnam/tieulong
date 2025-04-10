package business.player.feature.character;

import business.global.notice.NoticeMgr;
import business.player.Player;
import business.player.feature.Feature;
import business.player.item.IItemFilter;
import business.player.item.IUniItemContainer;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.enums.ConstEnum;
import com.zhonglian.server.common.enums.DressType;
import com.zhonglian.server.common.enums.PrizeType;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.logger.flow.ItemFlow;
import com.zhonglian.server.websocket.def.ErrorCode;
import com.zhonglian.server.websocket.exception.WSException;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefDress;
import core.database.game.bo.DressBO;
import core.network.client2game.handler.chars.DressOn;
import core.network.proto.DressInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DressFeature
extends Feature
implements IUniItemContainer<DressBO>
{
public final Map<Long, DressBO> dressMap = new HashMap<>();

public DressFeature(Player owner) {
super(owner);
}

public PrizeType getType() {
return PrizeType.Dress;
}

public boolean check(int itemId, int count, IItemFilter<DressBO> filter) {
throw new UnsupportedOperationException();
}

public DressBO consume(int itemId, int count, ItemFlow reason, IItemFilter<DressBO> filter) {
DressBO bo = getDressByDressId(itemId);
if (bo != null) {
removeDress(bo);
this.player.pushProto("delDress", new DressInfo(bo));
} 
return bo;
}

public int gain(int dressId, int count, ItemFlow reason) {
RefDress ref = (RefDress)RefDataMgr.get(RefDress.class, Integer.valueOf(dressId));
if (ref == null) {
return 0;
}
int gained = 0;
for (int i = 0; i < count; i++) {

DressBO bo = new DressBO();
bo.setPid(this.player.getPid());
bo.setDressId(dressId);
bo.setType(ref.Type.ordinal());
bo.setActiveTime(CommTime.nowSecond());
bo.insert();
gain(bo, ref);
gained++;
} 
return gained;
}

public void loadDB() {
List<DressBO> boList = BM.getBM(DressBO.class).findAll("pid", Long.valueOf(this.player.getPid()));
for (DressBO bo : boList) {
if (!onlycheck(bo)) {
bo.del();
continue;
} 
this.dressMap.put(Long.valueOf(bo.getId()), bo);
} 
}

public List<DressBO> getAllDress() {
return new ArrayList<>(this.dressMap.values());
}

public List<DressInfo> getAllDressInfo() {
List<DressInfo> list = new ArrayList<>();
checkDressList(getAllDress());
for (DressBO bo : getAllDress()) {
list.add(new DressInfo(bo));
}
return list;
}

public DressBO getDress(long sid) {
return this.dressMap.get(Long.valueOf(sid));
}

public DressBO getDressByDressId(int dressId) {
for (DressBO bo : this.dressMap.values()) {
if (bo.getDressId() == dressId && checkDress(bo)) {
return bo;
}
} 

return null;
}

public void gain(DressBO bo, RefDress ref) {
this.dressMap.put(Long.valueOf(bo.getId()), bo);

this.player.pushProto("addDress", new DressInfo(bo));

NoticeMgr.getInstance().sendMarquee(ConstEnum.UniverseMessageType.DressActive, new String[] { this.player.getName(), ref.Name });
}

public int gainAndEquip(int dressId, int count, ItemFlow reason) throws WSException {
RefDress ref = (RefDress)RefDataMgr.get(RefDress.class, Integer.valueOf(dressId));
if (ref == null) {
return 0;
}
int gained = 0;
for (int i = 0; i < count; i++) {
if (getDressByDressId(dressId) != null) {
DressBO bo = getDressByDressId(dressId);
if (bo.getEquipTime() != 0) {
bo.saveEquipTime(bo.getEquipTime() + ref.TimeLimit);

this.player.pushProto("addDress", new DressInfo(bo));
} 
} else {

DressBO bo = new DressBO();
bo.setPid(this.player.getPid());
bo.setDressId(dressId);
bo.setType(ref.Type.ordinal());
bo.setActiveTime(CommTime.nowSecond());
bo.insert();
gainAndEquip(bo, ref);
gained++;
} 
} 
return gained;
}

public void gainAndEquip(DressBO bo, RefDress ref) throws WSException {
this.dressMap.put(Long.valueOf(bo.getId()), bo);

this.player.pushProto("addDress", new DressInfo(bo));

NoticeMgr.getInstance().sendMarquee(ConstEnum.UniverseMessageType.DressActive, new String[] { this.player.getName(), ref.Name });

Active(bo.getId(), ref.Type, ref.CharId, ((CharFeature)this.player.getFeature(CharFeature.class)).getCharacter(ref.CharId));
}

public void consume(DressBO bo) {
this.dressMap.remove(Long.valueOf(bo.getId()));
bo.del();
this.player.pushProto("delDress", new DressInfo(bo));
}

public void Active(long equipSid, DressType type, int charId, Character character) throws WSException {
DressBO dressbo = getDress(equipSid);
if (dressbo == null) {
throw new WSException(ErrorCode.Dress_NotFound, "时装[%s]不存在", new Object[] { Long.valueOf(equipSid) });
}
RefDress ref = (RefDress)RefDataMgr.get(RefDress.class, Integer.valueOf(dressbo.getDressId()));
if (ref.CharId != 0 && ref.CharId != charId) {
throw new WSException(ErrorCode.Equip_NotBelong, "时装[%s]不能装备在角色%s上", new Object[] { Integer.valueOf(dressbo.getDressId()), Integer.valueOf(charId) });
}

if (!checkDress(dressbo)) {
throw new WSException(ErrorCode.Dress_OverTime, "时装超时");
}

Character preowner = ((CharFeature)this.player.getFeature(CharFeature.class)).getCharacter(dressbo.getCharId());
if (preowner != null) {
preowner.unEquipDress(type);
this.player.pushProto("dresson", new DressOn.DressNotify(preowner.getCharId(), type, 0L));
} 
character.activeDress(type, dressbo);
DressOn.DressNotify notify = new DressOn.DressNotify(character.getCharId(), type, equipSid);
this.player.pushProto("dresson", notify);
this.player.pushProto("dressinfo", new DressInfo(dressbo));
((CharFeature)this.player.getFeature(CharFeature.class)).updateCharPower();
}

public boolean checkDress(DressBO dressbo) {
boolean check = onlycheck(dressbo);
if (!check) {
removeDress(dressbo);
}
return check;
}

public int checkDressList(List<DressBO> list) {
List<DressBO> tmp_list = new ArrayList<>();
for (DressBO bo : list) {
if (!onlycheck(bo)) {
tmp_list.add(bo);
}
} 
for (DressBO bo : tmp_list) {
removeDress(bo);
}

return tmp_list.size();
}

private void removeDress(DressBO bo) {
this.dressMap.remove(Long.valueOf(bo.getId()));
if (bo.getCharId() != 0) {
Character character = ((CharFeature)this.player.getFeature(CharFeature.class)).getCharacter(bo.getCharId());
character.removeDress(bo);
} 
bo.del();
}

private boolean onlycheck(DressBO dressbo) {
RefDress ref = (RefDress)RefDataMgr.get(RefDress.class, Integer.valueOf(dressbo.getDressId()));
if (ref == null) {
return false;
}
if (ref.TimeLimit <= 0 || dressbo.getEquipTime() == 0) {
return true;
}

return (CommTime.nowSecond() - dressbo.getEquipTime() < ref.TimeLimit);
}
}

