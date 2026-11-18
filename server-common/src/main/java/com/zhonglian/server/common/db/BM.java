package com.zhonglian.server.common.db;

import BaseCommon.CommLog;
import BaseTask.AsynTask.AsyncCallBackTaskBase;
import BaseTask.AsynTask.AsyncTaskBase;
import BaseTask.AsynTask.AsyncTaskManager;
import com.zhonglian.server.common.Config;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class BM<T extends IBaseBO> {
    private static Map<Class<? extends IBaseBO>, BM<?>> map = new ConcurrentHashMap<>();
    private String tableName;
    private T boInctance;
    private final AtomicLong _nextId = new AtomicLong(-1L);
    private final AtomicLong begin_id = new AtomicLong(0L);
    private static final int MAX_ID_RANGE = 100;

    public static <M extends IBaseBO> BM<M> getBM(Class<M> classType) {
        BM<M> boInfo = (BM<M>)map.get(classType);
        if (boInfo == null) {
            map.put(classType, boInfo = new BM<>(classType));
        }

        return boInfo;
    }

    private IDBConnectionFactory getConnectionFactory() {
        return DBCons.getDBFactory();
    }

    public BM(Class<T> classType) {
        try {
            this.boInctance = classType.newInstance();
        } catch (IllegalAccessException | InstantiationException var3) {
        }

        this.tableName = this.boInctance.getTableName();
    }

    public synchronized long getNextId() {
        long newId = this._nextId.getAndIncrement();
        if (newId < this.begin_id.get() || newId >= this.begin_id.get() + 100L) {
            long curMax = this.getCurMaxId();
            curMax = Math.max(curMax, this._nextId.get());
            this._nextId.set(curMax + 1L);
            this.begin_id.set(curMax + 1L);
            newId = this._nextId.getAndIncrement();
        }

        return newId;
    }

    private long getCurMaxId() {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        Connection conn = null;

        long var8;
        try {
            conn = this.getConnectionFactory().getConnection();
            String sql = String.format("SELECT MAX(id) FROM %s", this.tableName);
            pstm = conn.prepareStatement(sql);
            rs = pstm.executeQuery();
            long maxId = 0L;
            if (rs.next()) {
                maxId = rs.getLong(1);
            }

            if (maxId != 0L) {
                return maxId;
            }

            var8 = Config.getInitialID();
        } catch (SQLException var12) {
            CommLog.error(var12.getMessage(), (Throwable)var12);
            return 0L;
        } finally {
            SQLUtil.close(rs, pstm, conn);
        }

        return var8;
    }

    public void delAll() {
        Conditions cond = new Conditions();
        cond.setTablesName(this.tableName);
        SQLExecutor.delByCondition(this.getConnectionFactory(), cond);
    }

    public void delAll(String key, Object value) {
        Conditions cond = new Conditions();
        cond.addAndEquals(String.format("`%s`", key), value);
        cond.setTablesName(this.tableName);
        SQLExecutor.delByCondition(this.getConnectionFactory(), cond);
    }

    public void delAll(String key, Object value, AsyncCallBackTaskBase<Integer> _callBackObj) {
        AsyncTaskManager.getDefaultMultQueue().regAsynTask(new BM.AsynCall_DelAll(key, value), _callBackObj);
    }

    public void delAll(String key, Object value, AsyncCallBackTaskBase<Integer> _callBackObj, int cid) {
        AsyncTaskManager.getDefaultMultQueue().regAsynTask(new BM.AsynCall_DelAll(key, value), _callBackObj, cid);
    }

    public List<T> findAll() {
        Conditions cond = new Conditions();
        return SQLExecutor.getListByCondition(this.getConnectionFactory(), cond, this.boInctance);
    }

    public List<T> findAll(String key, Object value) {
        Conditions cond = new Conditions();
        cond.addAndEquals(String.format("`%s`", key), value);
        return SQLExecutor.getListByCondition(this.getConnectionFactory(), cond, this.boInctance);
    }

    public List<T> findAll(HashMap<String, Object> conditions) {
        Conditions cond = new Conditions();

        for (String key : conditions.keySet()) {
            cond.addAndEquals(String.format("`%s`", key), conditions.get(key));
        }

        return SQLExecutor.getListByCondition(this.getConnectionFactory(), cond, this.boInctance);
    }

    public void findAll(String key, Object value, AsyncCallBackTaskBase<List<T>> _callBackObj) {
        AsyncTaskManager.getDefaultMultQueue().regAsynTask(new BM.AsynCall_FindAll(key, value), _callBackObj);
    }

    public void findAll(String key, Object value, AsyncCallBackTaskBase<List<T>> _callBackObj, int cid) {
        AsyncTaskManager.getDefaultMultQueue().regAsynTask(new BM.AsynCall_FindAll(key, value), _callBackObj, cid);
    }

    public List<T> findAllBySort(String sortKey, boolean asc, int limit) {
        Conditions cond = new Conditions();
        cond.setTablesName(this.tableName);
        cond.setLimit(limit);
        cond.addOrderItemsInfo(sortKey);
        cond.setSortType(asc ? "ASC" : "DESC");
        return SQLExecutor.getListByCondition(this.getConnectionFactory(), cond, this.boInctance);
    }

    public int Count() {
        return SQLExecutor.executeQueryCount("select count(1) from " + this.tableName, this.getConnectionFactory());
    }

    public T findOne(HashMap<String, Object> conditions) {
        List<T> bos = this.findAll(conditions);
        T tmp = null;
        if (bos != null && !bos.isEmpty()) {
            tmp = bos.get(0);
        }

        return tmp;
    }

    public T findOne(String key, Object value) {
        List<T> bos = this.findAll(key, value);
        T tmp = null;
        if (bos != null && !bos.isEmpty()) {
            tmp = bos.get(0);
        }

        return tmp;
    }

    public <M extends IBaseBO> void findOne(String key, Object value, AsyncCallBackTaskBase<M> _callBackObj) {
        AsyncTaskManager.getDefaultMultQueue().regAsynTask(this.new AsynCall_Find<M>(key, value), _callBackObj);
    }

    public <M extends IBaseBO> void findOne(String key, Object value, AsyncCallBackTaskBase<M> _callBackObj, int cid) {
        AsyncTaskManager.getDefaultMultQueue().regAsynTask(this.new AsynCall_Find<M>(key, value), _callBackObj, cid);
    }

    public class AsynCall_DelAll implements AsyncTaskBase<Integer> {
        private final String key;
        private final Object value;

        public AsynCall_DelAll(String key, Object value) {
            this.key = key;
            this.value = value;
        }

        public Integer doAsynTask() {
            Conditions cond = new Conditions();
            cond.addAndEquals(String.format("`%s`", this.key), this.value);
            cond.setTablesName(BM.this.tableName);
            int res = SQLExecutor.delByCondition(BM.this.getConnectionFactory(), cond);
            return res;
        }
    }

    public class AsynCall_Find<M extends IBaseBO> implements AsyncTaskBase<M> {
        private final String key;
        private final Object value;

        public AsynCall_Find(String key, Object value) {
            this.key = key;
            this.value = value;
        }

        public M doAsynTask() {
            return (M)BM.this.findOne(this.key, this.value);
        }
    }

    public class AsynCall_FindAll<M extends IBaseBO> implements AsyncTaskBase<List<M>> {
        private final String key;
        private final Object value;

        public AsynCall_FindAll(String key, Object value) {
            this.key = key;
            this.value = value;
        }

        public List<M> doAsynTask() {
            return (List<M>) (List<?>) BM.this.findAll(this.key, this.value);
        }
    }
}
