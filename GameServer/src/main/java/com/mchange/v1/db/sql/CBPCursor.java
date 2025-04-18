package com.mchange.v1.db.sql;

import java.sql.ResultSet;

public abstract class CBPCursor
extends SimpleCursor
{
ConnectionBundle returnMe;
ConnectionBundlePool home;

public CBPCursor(ResultSet paramResultSet, ConnectionBundle paramConnectionBundle, ConnectionBundlePool paramConnectionBundlePool) {
super(paramResultSet);
this.returnMe = paramConnectionBundle;
this.home = paramConnectionBundlePool;
}

public void close() throws Exception {
try {
super.close();
} finally {
this.home.checkinBundle(this.returnMe);
} 
}
}

