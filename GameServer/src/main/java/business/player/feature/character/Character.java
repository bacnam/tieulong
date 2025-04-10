package business.player.feature.character;

import business.global.rank.RankManager;
import business.player.Player;
import business.player.RobotManager;
import com.zhonglian.server.common.enums.CharacterType;
import com.zhonglian.server.common.enums.DressType;
import com.zhonglian.server.common.enums.EquipPos;
import com.zhonglian.server.common.enums.RankType;
import com.zhonglian.server.common.utils.CommTime;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefCharacter;
import core.database.game.bo.CharacterBO;
import core.database.game.bo.DressBO;
import core.network.proto.DressInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Character
{
Player player;
private CharacterBO bo;
private Map<EquipPos, Equip> equips;
private Map<DressType, DressBO> dresses;
Integer power;

Character(Player player, CharacterBO bo) {
this.power = null; this.player = player; this.bo = bo; this.equips = new HashMap<>(); this.dresses = new HashMap<>();
}
public void removeDress(DressBO bo) { this.dresses.remove(DressType.values()[bo.getType()]); }
public int getCharId() { return this.bo.getCharId(); } public Equip unEquip(EquipPos equipPos) { Equip equip = this.equips.remove(equipPos); if (equip != null) equip.saveOwner(null, EquipPos.None);  return equip; } public Equip getEquip(EquipPos pos) { return this.equips.get(pos); } public List<Equip> getEquips() { return new ArrayList<>(this.equips.values()); } public void equip(Equip equip, EquipPos pos) { this.equips.put(pos, equip); } public int getPower() { if (this.power == null) {
this.power = Integer.valueOf((new CharAttrCalculator(this)).getPower());
}
return this.power.intValue(); }
public DressBO getDress(DressType type) { return this.dresses.get(type); }
public List<DressBO> getDresses() { return new ArrayList<>(this.dresses.values()); }
public List<DressInfo> getAllDressInfo() { List<DressInfo> list = new ArrayList<>(); DressFeature feature = (DressFeature)this.player.getFeature(DressFeature.class); feature.checkDressList(new ArrayList<>(this.dresses.values())); for (DressBO bo : this.dresses.values()) list.add(new DressInfo(bo));  return list; }
public void activeDress(DressType type, DressBO bo) { if (bo.getEquipTime() == 0) bo.saveEquipTime(CommTime.nowSecond());  bo.saveCharId(getCharId()); this.dresses.put(type, bo); } public void unEquipDress(DressType type) { DressBO bo = this.dresses.remove(type); if (bo != null) { bo.saveCharId(0); this.player.pushProto("dressinfo", new DressInfo(bo)); }  } public CharacterBO getBo() { return this.bo; } public void setBo(CharacterBO bo) { this.bo = bo; } public CharAttrCalculator getAttr() { return new CharAttrCalculator(this); }

public void onAttrChanged() {
this.power = Integer.valueOf((new CharAttrCalculator(this)).getPower());
updateRank(this.power.intValue());
}

void updateRank(int power) {
RefCharacter ref = (RefCharacter)RefDataMgr.get(RefCharacter.class, Integer.valueOf(getCharId()));
RankType ranktype = null;
switch (ref.Type) {
case gumu:
ranktype = RankType.GuMuPower;
break;
case tianlong:
ranktype = RankType.TianLongPower;
break;
case xiaoyao:
ranktype = RankType.XiaoYaoPower;
break;
} 

if (!RobotManager.getInstance().isRobot(this.player.getPid())) {
RankManager.getInstance().update(ranktype, this.player.getPid(), power);
}
((CharFeature)this.player.getFeature(CharFeature.class)).updatePower();
}
}

