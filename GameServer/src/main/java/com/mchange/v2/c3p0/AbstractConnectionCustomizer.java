package com.mchange.v2.c3p0;

import java.sql.Connection;
import java.util.Map;

public abstract class AbstractConnectionCustomizer
implements ConnectionCustomizer
{
protected Map extensionsForToken(String parentDataSourceIdentityToken) {
return C3P0Registry.extensionsForToken(parentDataSourceIdentityToken);
}

public void onAcquire(Connection c, String parentDataSourceIdentityToken) throws Exception {}

public void onDestroy(Connection c, String parentDataSourceIdentityToken) throws Exception {}

public void onCheckOut(Connection c, String parentDataSourceIdentityToken) throws Exception {}

public void onCheckIn(Connection c, String parentDataSourceIdentityToken) throws Exception {}
}

