package core.network.proto;

import business.player.feature.character.WarSpirit;

public class WarSpiritInfo
{
public long sid;
public int spiritId;
public int skill;
public boolean selected;
public int lv;
public int talent;
public int star;

public WarSpiritInfo() {}

public WarSpiritInfo(WarSpirit warspirit) {
if (warspirit != null) {
this.sid = warspirit.getBo().getId();
this.spiritId = warspirit.getSpiritId();
this.skill = warspirit.getBo().getSkill();
this.selected = warspirit.getBo().getIsSelected();
this.lv = warspirit.getPlayer().getPlayerBO().getWarspiritLv();
this.talent = warspirit.getPlayer().getPlayerBO().getWarspiritTalent();
this.star = warspirit.getBo().getStar();
} 
}

public long getSid() {
return this.sid;
}

public void setSid(long sid) {
this.sid = sid;
}

public int getSpiritId() {
return this.spiritId;
}

public void setSpiritId(int spiritId) {
this.spiritId = spiritId;
}

public int getSkill() {
return this.skill;
}

public void setSkill(int skill) {
this.skill = skill;
}
}

