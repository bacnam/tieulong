package com.zhonglian.server.common.db.version;

import com.zhonglian.server.common.db.IDBConnectionFactory;

public interface ICreateDBTable {
  boolean run(IDBConnectionFactory paramIDBConnectionFactory);
}

