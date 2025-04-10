package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface BalanceStrategy extends Extension {
  ConnectionImpl pickConnection(LoadBalancingConnectionProxy paramLoadBalancingConnectionProxy, List<String> paramList, Map<String, ConnectionImpl> paramMap, long[] paramArrayOflong, int paramInt) throws SQLException;
}

