package business.player.feature.record;

import business.player.Player;
import business.player.feature.Feature;
import com.google.common.collect.Lists;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.utils.CommTime;
import core.database.game.bo.VipRecordBO;
import java.util.List;
import java.util.stream.Collectors;

public class VipRecord
extends Feature
{
public VipRecordBO record;

public VipRecord(Player owner) {
super(owner);
}

public void loadDB() {
this.record = (VipRecordBO)BM.getBM(VipRecordBO.class).findOne("pid", Long.valueOf(this.player.getPid()));
}

public VipRecordBO getOrCreate() {
VipRecordBO bo = this.record;
if (bo != null) {
return bo;
}
synchronized (this) {
bo = this.record;
if (bo != null) {
return bo;
}
bo = new VipRecordBO();
bo.setPid(this.player.getPid());
bo.insert();
this.record = bo;
} 
return bo;
}

public List<Boolean> getFetchGiftStatusList() {
List<Integer> fetchList = Lists.newArrayList(getOrCreate().getLastFetchPrivateTimeAll());
return (List<Boolean>)fetchList.stream().map(x -> (x.intValue() > 0) ? Boolean.valueOf(true) : Boolean.valueOf(false))

.collect(Collectors.toList());
}

public void setLastFetchPrivateTime(int level) {
VipRecordBO bo = getOrCreate();
bo.saveLastFetchPrivateTime(level, CommTime.nowSecond());
}

public int getLastFetchPrivateTime(int level) {
return getOrCreate().getLastFetchPrivateTime(level);
}
}

