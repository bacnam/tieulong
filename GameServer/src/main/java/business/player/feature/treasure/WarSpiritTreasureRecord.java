package business.player.feature.treasure;

import business.player.Player;
import business.player.PlayerMgr;
import com.zhonglian.server.common.db.BM;
import core.config.refdata.RefDataMgr;
import core.config.refdata.ref.RefWarSpirit;
import core.database.game.bo.WarSpiritTreasureRecordBO;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class WarSpiritTreasureRecord
{
private static WarSpiritTreasureRecord instance = new WarSpiritTreasureRecord();

public static WarSpiritTreasureRecord getInstance() {
return instance;
}

List<WarSpiritTreasureRecordBO> records = new LinkedList<>();

int recordSize = RefDataMgr.getFactor("WarspiritTreasureSize", 3);

public void init() {
List<WarSpiritTreasureRecordBO> list = BM.getBM(WarSpiritTreasureRecordBO.class).findAllBySort("time", false, this.recordSize);
this.records = list;
}

public void add(WarSpiritTreasureRecordBO bo) {
synchronized (this) {
if (this.records.size() >= this.recordSize) {
WarSpiritTreasureRecordBO remove = this.records.remove(0);
remove.del();
} 
this.records.add(bo);
for (Player player : PlayerMgr.getInstance().getOnlinePlayers()) {
player.pushProto("newWarspiritTreasure", new WarSpiritRecordInfo(bo));
}
} 
}

public List<WarSpiritRecordInfo> getRecords() {
List<WarSpiritRecordInfo> list = new ArrayList<>();
for (WarSpiritTreasureRecordBO bo : this.records) {
list.add(new WarSpiritRecordInfo(bo));
}

return list;
}

public static class WarSpiritRecordInfo {
String name;
String warspiritName;

public WarSpiritRecordInfo(WarSpiritTreasureRecordBO bo) {
Player player = PlayerMgr.getInstance().getPlayer(bo.getPid());
RefWarSpirit ref = (RefWarSpirit)RefDataMgr.get(RefWarSpirit.class, Integer.valueOf(bo.getSpiritId()));
this.name = player.getName();
this.warspiritName = ref.Name;
}
}
}

