package com.zhonglian.server.common.db;

import BaseCommon.CommLog;
import BaseTask.AsynTask.AsyncCallBackTaskBase;
import BaseTask.AsynTask.AsyncTaskBase;
import BaseTask.AsynTask.AsyncTaskManager;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class BaseBO implements IBaseBO, Comparable<BaseBO> {
    public static AtomicInteger AsynTaskCnt = new AtomicInteger(0);

    public abstract long getAsynTaskTag();

    public static int getAsynTaskCnt() {
        return AsynTaskCnt.get();
    }

    @Override
    public IDBConnectionFactory getConnectionFactory() {
        return DBCons.getDBFactory();
    }

    public void insert(BaseBO.AsynCallBack_Insert _callback) {
        if (this.getId() == 0L) {
            this.setId(BM.getBM(this.getClass()).getNextId());
        }

        AsyncTaskManager.getDefaultMultQueue().regAsynTask(new BaseBO.AsynCall_Insert(this), _callback, this.getAsynTaskTag());
    }

    public boolean insert_sync() {
        if (this.getId() == 0L) {
            this.setId(BM.getBM(this.getClass()).getNextId());
        }

        return SQLExecutor.InsertBO(this.getConnectionFactory(), this, this.getInsertValueBytes());
    }

    public void insert() {
        if (this.getId() == 0L) {
            this.setId(BM.getBM(this.getClass()).getNextId());
            AsyncTaskManager.getDefaultMultQueue().regAsynTask(new BaseBO.AsynCall_Insert(this), null, this.getAsynTaskTag());
        } else {
            CommLog.error("[{}:{}]数据已经存在，进行saveall操作", this.getTableName(), this.getId());
            AsyncTaskManager.getDefaultMultQueue().regAsynTask(new BaseBO.AsynRun_UpdateAll(), null, this.getAsynTaskTag());
        }
    }

    public void del() {
        AsyncTaskManager.getDefaultMultQueue().regAsynTask(new BaseBO.AsynCall_Del(this), null, this.getAsynTaskTag());
    }

    protected void saveField(String field, Object newValue) {
        AsyncTaskManager.getDefaultMultQueue().regAsynTask(new BaseBO.AsynRun_UpdateField(field, newValue), null, this.getAsynTaskTag());
    }

    protected void saveFieldBytes(String field, byte[] newValue) {
        AsyncTaskManager.getDefaultMultQueue().regAsynTask(new BaseBO.AsynRun_UpdateFieldBytes(field, newValue), null, this.getAsynTaskTag());
    }

    public void saveAll() {
        if (this.getId() == 0L) {
            CommLog.error("[{}]数据还未创建，进行insert操作", this.getTableName());
            this.setId(BM.getBM(this.getClass()).getNextId());
            AsyncTaskManager.getDefaultMultQueue().regAsynTask(new BaseBO.AsynCall_Insert(this), null, this.getAsynTaskTag());
        } else {
            AsyncTaskManager.getDefaultMultQueue().regAsynTask(new BaseBO.AsynRun_UpdateAll(), null, this.getAsynTaskTag());
        }
    }

    public void saveAll_sync() {
        Conditions cond = new Conditions();
        cond.setTablesName(this.getTableName());
        cond.addAndEquals("`id`", this.getId());
        SQLExecutor.updateByCondition(this.getConnectionFactory(), cond, this.getUpdateKeyValue(), this.getInsertValueBytes());
    }

    protected abstract String getUpdateKeyValue();

    protected abstract ArrayList<byte[]> getInsertValueBytes();

    public int compareTo(BaseBO o) {
        return (int)(this.getId() - o.getId());
    }

    public abstract static class AsynCallBack_Insert extends AsyncCallBackTaskBase<Boolean> {
        protected BaseBO _bo;

        public AsynCallBack_Insert(BaseBO _bo) {
            this._bo = _bo;
        }
    }

    public class AsynCall_Del implements AsyncTaskBase<Integer> {
        BaseBO _bo;

        public AsynCall_Del(BaseBO _bo) {
            BaseBO.AsynTaskCnt.getAndIncrement();
            this._bo = _bo;
        }

        public Integer doAsynTask() {
            Conditions cond = new Conditions();
            cond.setTablesName(this._bo.getTableName());
            cond.addAndEquals("`id`", this._bo.getId());
            BaseBO.AsynTaskCnt.getAndDecrement();
            return SQLExecutor.delByCondition(BaseBO.this.getConnectionFactory(), cond);
        }
    }

    public class AsynCall_Insert implements AsyncTaskBase<Boolean> {
        BaseBO bo;

        public AsynCall_Insert(BaseBO _bo) {
            BaseBO.AsynTaskCnt.getAndIncrement();
            this.bo = _bo;
        }

        public Boolean doAsynTask() {
            BaseBO.AsynTaskCnt.getAndDecrement();
            return SQLExecutor.InsertBO(BaseBO.this.getConnectionFactory(), this.bo, this.bo.getInsertValueBytes());
        }
    }

    public class AsynRun_UpdateAll implements AsyncTaskBase<Object> {
        public AsynRun_UpdateAll() {
            BaseBO.AsynTaskCnt.getAndIncrement();
        }

        @Override
        public Object doAsynTask() {
            BaseBO.AsynTaskCnt.getAndDecrement();
            BaseBO.this.saveAll_sync();
            return null;
        }
    }

    public class AsynRun_UpdateField implements AsyncTaskBase<Object> {
        private final String _field;
        private final Object _newValue;

        public AsynRun_UpdateField(String _field, Object _newValue) {
            this._field = _field;
            this._newValue = _newValue;
            BaseBO.AsynTaskCnt.getAndIncrement();
        }

        @Override
        public Object doAsynTask() {
            Conditions cond = new Conditions();
            cond.setTablesName(BaseBO.this.getTableName());
            cond.addAndEquals("`id`", BaseBO.this.getId());
            Object val = this._newValue;
            if (this._newValue instanceof Boolean) {
                boolean tmp = (Boolean)this._newValue;
                val = tmp ? 1 : 0;
            }

            if (this._newValue instanceof String) {
                val = ((String)this._newValue).replace("'", "''");
            }

            String sql = String.format("`%s` = '%s'", this._field, val);
            BaseBO.AsynTaskCnt.getAndDecrement();
            SQLExecutor.updateByCondition(BaseBO.this.getConnectionFactory(), cond, sql);
            return null;
        }
    }

    public class AsynRun_UpdateFieldBytes implements AsyncTaskBase<Object> {
        private final String _field;
        private final byte[] _newValue;

        public AsynRun_UpdateFieldBytes(String _field, byte[] _newValue) {
            this._field = _field;
            this._newValue = _newValue;
            BaseBO.AsynTaskCnt.getAndIncrement();
        }

        @Override
        public Object doAsynTask() {
            Conditions cond = new Conditions();
            cond.setTablesName(BaseBO.this.getTableName());
            cond.addAndEquals("`id`", BaseBO.this.getId());
            String sql = String.format("`%s` = ?", this._field);
            ArrayList<byte[]> _binaryList = new ArrayList<>();
            _binaryList.add(this._newValue);
            BaseBO.AsynTaskCnt.getAndDecrement();
            SQLExecutor.updateByCondition(BaseBO.this.getConnectionFactory(), cond, sql, _binaryList);
            return null;
        }
    }
}
