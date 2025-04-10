package com.zhonglian.server.common.db;

import BaseCommon.CommLog;
import BaseTask.AsynTask.AsyncCallBackTaskBase;
import BaseTask.AsynTask.AsyncTaskBase;
import BaseTask.AsynTask.AsyncTaskManager;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class BaseBO
implements IBaseBO, Comparable<BaseBO>
{
public static AtomicInteger AsynTaskCnt = new AtomicInteger(0);

public static int getAsynTaskCnt() {
return AsynTaskCnt.get();
}

public IDBConnectionFactory getConnectionFactory() {
return DBCons.getDBFactory();
}

public void insert(AsynCallBack_Insert _callback) {
if (getId() == 0L) {
setId(BM.getBM(getClass()).getNextId());
}
AsyncTaskManager.getDefaultMultQueue().regAsynTask(new AsynCall_Insert(this), _callback, getAsynTaskTag());
}

public boolean insert_sync() {
if (getId() == 0L) {
setId(BM.getBM(getClass()).getNextId());
}
return SQLExecutor.InsertBO(getConnectionFactory(), this, getInsertValueBytes());
}

public void insert() {
if (getId() == 0L) {
setId(BM.getBM(getClass()).getNextId());
AsyncTaskManager.getDefaultMultQueue().regAsynTask(new AsynCall_Insert(this), null, getAsynTaskTag());
} else {
CommLog.error("[{}:{}]数据已经存在，进行saveall操作", getTableName(), Long.valueOf(getId()));
AsyncTaskManager.getDefaultMultQueue().regAsynTask(new AsynRun_UpdateAll(), null, getAsynTaskTag());
} 
}

public void del() {
AsyncTaskManager.getDefaultMultQueue().regAsynTask(new AsynCall_Del(this), null, getAsynTaskTag());
}

protected void saveField(String field, Object newValue) {
AsyncTaskManager.getDefaultMultQueue().regAsynTask(new AsynRun_UpdateField(field, newValue), null, getAsynTaskTag());
}

protected void saveFieldBytes(String field, byte[] newValue) {
AsyncTaskManager.getDefaultMultQueue().regAsynTask(new AsynRun_UpdateFieldBytes(field, newValue), null, getAsynTaskTag());
}

public void saveAll() {
if (getId() == 0L) {
CommLog.error("[{}]数据还未创建，进行insert操作", getTableName());
setId(BM.getBM(getClass()).getNextId());
AsyncTaskManager.getDefaultMultQueue().regAsynTask(new AsynCall_Insert(this), null, getAsynTaskTag());
} else {
AsyncTaskManager.getDefaultMultQueue().regAsynTask(new AsynRun_UpdateAll(), null, getAsynTaskTag());
} 
}

public static abstract class AsynCallBack_Insert
extends AsyncCallBackTaskBase<Boolean> {
protected BaseBO _bo;

public AsynCallBack_Insert(BaseBO _bo) {
this._bo = _bo;
}
}

public class AsynCall_Insert
implements AsyncTaskBase<Boolean> {
BaseBO bo;

public AsynCall_Insert(BaseBO _bo) {
BaseBO.AsynTaskCnt.getAndIncrement();
this.bo = _bo;
}

public Boolean doAsynTask() {
BaseBO.AsynTaskCnt.getAndDecrement();
return Boolean.valueOf(SQLExecutor.InsertBO(BaseBO.this.getConnectionFactory(), this.bo, this.bo.getInsertValueBytes()));
}
}

public class AsynCall_Del
implements AsyncTaskBase<Integer> {
BaseBO _bo;

public AsynCall_Del(BaseBO _bo) {
BaseBO.AsynTaskCnt.getAndIncrement();
this._bo = _bo;
}

public Integer doAsynTask() {
Conditions cond = new Conditions();
cond.setTablesName(this._bo.getTableName());
cond.addAndEquals("`id`", Long.valueOf(this._bo.getId()));

BaseBO.AsynTaskCnt.getAndDecrement();
return Integer.valueOf(SQLExecutor.delByCondition(BaseBO.this.getConnectionFactory(), cond));
}
}

public class AsynRun_UpdateField
implements AsyncTaskBase<Object> {
private final String _field;
private final Object _newValue;

public AsynRun_UpdateField(String _field, Object _newValue) {
this._field = _field;
this._newValue = _newValue;
BaseBO.AsynTaskCnt.getAndIncrement();
}

public Object doAsynTask() {
Conditions cond = new Conditions();
cond.setTablesName(BaseBO.this.getTableName());
cond.addAndEquals("`id`", Long.valueOf(BaseBO.this.getId()));

Object val = this._newValue;
if (this._newValue instanceof Boolean) {
boolean tmp = ((Boolean)this._newValue).booleanValue();
val = Integer.valueOf(tmp ? 1 : 0);
} 
if (this._newValue instanceof String) {
val = ((String)this._newValue).replace("'", "''");
}
String sql = String.format("`%s` = '%s'", new Object[] { this._field, val });

BaseBO.AsynTaskCnt.getAndDecrement();
SQLExecutor.updateByCondition(BaseBO.this.getConnectionFactory(), cond, sql);
return null;
}
}

public class AsynRun_UpdateFieldBytes
implements AsyncTaskBase<Object> {
private final String _field;
private final byte[] _newValue;

public AsynRun_UpdateFieldBytes(String _field, byte[] _newValue) {
this._field = _field;
this._newValue = _newValue;
BaseBO.AsynTaskCnt.getAndIncrement();
}

public Object doAsynTask() {
Conditions cond = new Conditions();
cond.setTablesName(BaseBO.this.getTableName());
cond.addAndEquals("`id`", Long.valueOf(BaseBO.this.getId()));

String sql = String.format("`%s` = ?", new Object[] { this._field });
ArrayList<byte[]> _binaryList = (ArrayList)new ArrayList<>();
_binaryList.add(this._newValue);

BaseBO.AsynTaskCnt.getAndDecrement();
SQLExecutor.updateByCondition(BaseBO.this.getConnectionFactory(), cond, sql, _binaryList);
return null;
}
}

public void saveAll_sync() {
Conditions cond = new Conditions();
cond.setTablesName(getTableName());
cond.addAndEquals("`id`", Long.valueOf(getId()));
SQLExecutor.updateByCondition(getConnectionFactory(), cond, getUpdateKeyValue(), getInsertValueBytes());
}

public class AsynRun_UpdateAll
implements AsyncTaskBase<Object>
{
public AsynRun_UpdateAll() {
BaseBO.AsynTaskCnt.getAndIncrement();
}

public Object doAsynTask() {
BaseBO.AsynTaskCnt.getAndDecrement();
BaseBO.this.saveAll_sync();
return null;
}
}

public int compareTo(BaseBO o) {
return (int)(getId() - o.getId());
}

public abstract long getAsynTaskTag();

protected abstract String getUpdateKeyValue();

protected abstract ArrayList<byte[]> getInsertValueBytes();
}

