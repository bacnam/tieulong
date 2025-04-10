package business.global.guild;

import com.zhonglian.server.common.utils.CommTime;
import core.database.game.bo.GuildRankRecordBO;

public class GuildRecord
{
int rank;
GuildRankRecordBO recordBO;

public GuildRecord(GuildRankRecordBO recordBO) {
this.recordBO = recordBO;
this.rank = 0;
}

public long saveValue(long value) {
this.recordBO.saveValue(value);
this.recordBO.saveUpdateTime(CommTime.nowSecond());
return this.recordBO.getValue();
}

public GuildRankRecordBO copy() {
GuildRankRecordBO recordBO = new GuildRankRecordBO();
recordBO.setType(recordBO.getType());
recordBO.setOwner(recordBO.getOwner());
recordBO.setValue(recordBO.getValue());
recordBO.setExt1(recordBO.getExt1());
recordBO.setExt2(recordBO.getExt2());
recordBO.setExt3(recordBO.getExt3());
recordBO.setExt4(recordBO.getExt4());
recordBO.setExt5(recordBO.getExt5());
recordBO.setUpdateTime(CommTime.nowSecond());
return recordBO;
}

public long getPid() {
return this.recordBO.getOwner();
}

public int getRank() {
return this.rank;
}

public long getValue() {
return this.recordBO.getValue();
}
}

