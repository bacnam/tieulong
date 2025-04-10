package business.player.feature.store;

import business.player.Player;
import business.player.feature.Feature;
import com.zhonglian.server.common.db.BM;
import com.zhonglian.server.common.enums.StoreType;
import com.zhonglian.server.common.utils.CommTime;
import core.config.refdata.ref.RefStoreRefresh;
import core.database.game.bo.StoreRecordBO;

public class StoreRecord
extends Feature
{
public StoreRecordBO storeRecord;

public StoreRecord(Player owner) {
super(owner);
}

public void loadDB() {
this.storeRecord = (StoreRecordBO)BM.getBM(StoreRecordBO.class).findOne("pid", Long.valueOf(this.player.getPid()));
}

public StoreRecordBO getOrCreate() {
StoreRecordBO record = this.storeRecord;
if (record != null) {
return record;
}
synchronized (this) {
record = this.storeRecord;
if (record != null) {
return record;
}
record = new StoreRecordBO();
record.setPid(this.player.getPid()); byte b; int i; StoreType[] arrayOfStoreType;
for (i = (arrayOfStoreType = StoreType.values()).length, b = 0; b < i; ) { StoreType storeType = arrayOfStoreType[b];
record.setFreeRefreshTimes(storeType.ordinal(), ((Integer)RefStoreRefresh.FreeStoreTimes.get(storeType)).intValue()); b++; }

record.insert();
this.storeRecord = record;
} 
return record;
}

public void dailyRefresh() {
try {
StoreRecordBO record = getOrCreate();
record.setPaidRefreshTimesAll(0);
record.setBuyTimesAll(0); byte b; int i; StoreType[] arrayOfStoreType;
for (i = (arrayOfStoreType = StoreType.values()).length, b = 0; b < i; ) { StoreType storeType = arrayOfStoreType[b];
record.setFreeRefreshTimes(storeType.ordinal(), ((Integer)RefStoreRefresh.FreeStoreTimes.get(storeType)).intValue()); b++; }

record.saveAll();
} catch (Exception e) {

e.printStackTrace();
} 
}

public int getFreeRefreshTimes(StoreType storeType) {
return getOrCreate().getFreeRefreshTimes(storeType.ordinal());
}

public int getPaidRefreshTimes(StoreType storeType) {
return getOrCreate().getPaidRefreshTimes(storeType.ordinal());
}

public int getBuyTimes(StoreType storeType) {
return getOrCreate().getBuyTimes(storeType.ordinal());
}

public int getFlushIconByIndex(StoreType storeType) {
return getOrCreate().getFlushIcon(storeType.ordinal());
}

public void doAutoRefresh(StoreType storeType) {
int index = storeType.ordinal();
if (index <= StoreType.None.ordinal()) {
return;
}
StoreRecordBO record = getOrCreate();

record.saveFlushIcon(index, 1);

record.saveLastRefreshTime(index, CommTime.nowSecond());
}

public void doHandleRefresh(StoreType storeType, boolean useFreeRefresh) {
int index = storeType.ordinal();
if (index <= StoreType.None.ordinal()) {
return;
}
StoreRecordBO record = getOrCreate();

if (useFreeRefresh) {
record.saveFreeRefreshTimes(index, getFreeRefreshTimes(storeType) - 1);
} else {
record.savePaidRefreshTimes(index, getPaidRefreshTimes(storeType) + 1);
} 

record.saveLastRefreshTime(index, CommTime.nowSecond());
}

public void doBuyGoods(StoreType storeType) {
int index = storeType.ordinal();
if (index <= StoreType.None.ordinal()) {
return;
}
StoreRecordBO record = getOrCreate();

record.saveBuyTimes(index, getBuyTimes(storeType) + 1);
}

public void doSaveFlushIcon(StoreType storeType, int value) {
getOrCreate().saveFlushIcon(storeType.ordinal(), value);
}
}

