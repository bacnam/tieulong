package business.global.guild;

import com.zhonglian.server.common.db.DBCons;
import com.zhonglian.server.common.db.SQLExecutor;
import com.zhonglian.server.common.enums.GuildRankType;
import com.zhonglian.server.common.utils.CommTime;
import core.database.game.bo.GuildRankRecordBO;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GuildRank
{
private GuildRankType type;
List<GuildRecord> list;
Map<Long, GuildRecord> map;
private Comparator<GuildRankRecordBO> comparator;

public GuildRank(GuildRankType type, Comparator<GuildRankRecordBO> comparator) {
this.type = type;
this.comparator = comparator;
this.list = new ArrayList<>();
this.list.add(null);
this.map = new HashMap<>();
}

void resort() {
for (GuildRecord record : this.map.values()) {
if (record.recordBO.getValue() != 0L) {
this.list.add(record);
}
} 
this.list.sort((left, right) -> (left == null) ? -1 : ((right == null) ? 1 : this.comparator.compare(left.recordBO, right.recordBO)));

for (int i = 1; i < this.list.size(); i++) {
((GuildRecord)this.list.get(i)).rank = i;
}
}

public int getRank(long ownerid) {
GuildRecord record = this.map.get(Long.valueOf(ownerid));
return (record == null) ? 0 : record.rank;
}

public int update(long ownerid, long value, long... ext) {
if (filter(ownerid)) {
return 0;
}
synchronized (this) {
GuildRecord record = this.map.get(Long.valueOf(ownerid));
if (record == null) {
GuildRankRecordBO recordBO = createRecordBO(ownerid, value);
this.map.put(Long.valueOf(ownerid), record = new GuildRecord(recordBO));
record.rank = 0;
} 

if (record.rank != 0) {
return setValue(record, value);
}
return insert(record);
} 
}

private int setValue(GuildRecord record, long value) {
if (record.recordBO.getValue() == value) {
return record.rank;
}
GuildRankRecordBO recordBO = record.copy();
recordBO.setValue(value);

int pos = getInsertPos(recordBO);
record.saveValue(value);
if (record.rank == pos || record.rank == pos - 1) {
return pos;
}
if (record.rank > pos) {

for (int i = record.rank; i > pos; i--) {
GuildRecord r = this.list.get(i - 1);
this.list.set(i, r);
r.rank = i;
} 
this.list.set(pos, record);
record.rank = pos;
} else {

for (int i = record.rank; i < pos - 1; i++) {
GuildRecord r = this.list.get(i + 1);
this.list.set(i, r);
r.rank = i;
} 
this.list.set(pos - 1, record);
record.rank = pos - 1;
} 
return record.rank;
}

public long getValue(long ownerid) {
GuildRecord record = this.map.get(Long.valueOf(ownerid));
if (record != null) {
return record.recordBO.getValue();
}
return 0L;
}

private int insert(GuildRecord record) {
int pos = getInsertPos(record.recordBO);
this.list.add(pos, record);
record.rank = pos;

for (int i = pos + 1; i < this.list.size(); i++) {
((GuildRecord)this.list.get(i)).rank = i;
}
return pos;
}

protected GuildRankRecordBO createRecordBO(long ownerid, long value) {
GuildRankRecordBO recordBO = new GuildRankRecordBO();
recordBO.setType(this.type.ordinal());
recordBO.setOwner(ownerid);
recordBO.setValue(value);
recordBO.setUpdateTime(CommTime.nowSecond());
recordBO.insert();
return recordBO;
}

private int getInsertPos(GuildRankRecordBO recordBo) {
int left = 0, right = this.list.size();
while (right - left > 1) {
int m = (left + right) / 2;
GuildRankRecordBO mid = ((GuildRecord)this.list.get(m)).recordBO;
if (this.comparator.compare(mid, recordBo) < 0) {
left = m; continue;
}  if (this.comparator.compare(mid, recordBo) > 0) {
right = m;

continue;
} 
left = right = m;
} 

return left + 1;
}

public void clear() {
int deleteTime = CommTime.nowSecond() - 604800;

String delsql = String.format("DELETE from rank_record where value=0 and type=%d and updateTime<%d;", new Object[] { Integer.valueOf(this.type.ordinal()), Integer.valueOf(deleteTime) });
SQLExecutor.execute(delsql, DBCons.getDBFactory());

String updatesql = String.format("UPDATE rank_record SET value=0, updateTime=%d where type=%d;", new Object[] { Integer.valueOf(CommTime.nowSecond()), Integer.valueOf(this.type.ordinal()) });
SQLExecutor.executeUpdate(updatesql, DBCons.getDBFactory());

this.list.clear();
this.list.add(null);
List<GuildRecord> todel = new ArrayList<>();
for (GuildRecord record : this.map.values()) {
if (record.recordBO.getUpdateTime() < deleteTime) {
todel.add(record);
continue;
} 
record.rank = 0;
record.recordBO.setValue(0L);
} 
for (GuildRecord record : todel) {
this.map.remove(Long.valueOf(record.recordBO.getOwner()));
}
}

public int add(long ownerid, long value) {
GuildRecord record = this.map.get(Long.valueOf(ownerid));
if (record == null) {
GuildRankRecordBO recordBO = createRecordBO(ownerid, value);
this.map.put(Long.valueOf(ownerid), record = new GuildRecord(recordBO));
record.rank = 0;
} 

if (record.rank != 0) {
return setValue(record, record.getValue() + value);
}
return insert(record);
}

public int minus(long ownerid, int value) {
GuildRecord record = this.map.get(Long.valueOf(ownerid));
if (record == null) {
return 0;
}

return setValue(record, Math.max(0L, record.getValue() - value));
}

synchronized void del(long ownerid) {
GuildRecord record = this.map.get(Long.valueOf(ownerid));
if (record != null) {
this.map.remove(Long.valueOf(ownerid));
this.list.remove(record);
for (int i = record.rank; i < this.list.size(); i++) {
if (this.list.get(i) != null)
((GuildRecord)this.list.get(i)).rank = i; 
} 
} 
}

protected abstract boolean filter(long paramLong);
}

