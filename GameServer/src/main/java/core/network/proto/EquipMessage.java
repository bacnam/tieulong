package core.network.proto;

import com.zhonglian.server.common.enums.EquipPos;
import core.database.game.bo.EquipBO;
import java.util.List;

public class EquipMessage
{
int equipId;
long sid;
List<Integer> attrs;
int charId;
EquipPos pos;

public EquipMessage(EquipBO bo) {
this.equipId = bo.getEquipId();
this.sid = bo.getId();
this.attrs = bo.getAttrAll();
this.pos = EquipPos.values()[bo.getPos()];
this.charId = bo.getCharId();
}
}

