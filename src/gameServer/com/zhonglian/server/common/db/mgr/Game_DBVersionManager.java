package com.zhonglian.server.common.db.mgr;

import com.zhonglian.server.common.db.DBCons;
import com.zhonglian.server.common.db.IDBConnectionFactory;
import com.zhonglian.server.common.db.version.DBVersionManager;

public class Game_DBVersionManager
extends DBVersionManager
{
private static Game_DBVersionManager instance = null;

public static Game_DBVersionManager getInstance() {
if (instance == null) {
instance = new Game_DBVersionManager();
}
return instance;
}

public IDBConnectionFactory getConn() {
return DBCons.getDBFactory();
}
}

