package com.mchange.v1.db.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

public class WeakHashPSManager
        implements PSManager {
    WeakHashMap wmap = new WeakHashMap<Object, Object>();

    public PreparedStatement getPS(Connection paramConnection, String paramString) {
        Map map = (Map) this.wmap.get(paramConnection);
        return (map == null) ? null : (PreparedStatement) map.get(paramString);
    }

    public void putPS(Connection paramConnection, String paramString, PreparedStatement paramPreparedStatement) {
        Map<Object, Object> map = (Map) this.wmap.get(paramConnection);
        if (map == null) {

            map = new HashMap<Object, Object>();
            this.wmap.put(paramConnection, map);
        }
        map.put(paramString, paramPreparedStatement);
    }
}

