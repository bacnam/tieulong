package com.mchange.v1.db.sql;

import com.mchange.v1.util.BrokenObjectException;
import com.mchange.v1.util.ClosableResource;
import java.sql.SQLException;

public interface ConnectionBundlePool extends ClosableResource {
  ConnectionBundle checkoutBundle() throws SQLException, InterruptedException, BrokenObjectException;
  
  void checkinBundle(ConnectionBundle paramConnectionBundle) throws SQLException, BrokenObjectException;
  
  void close() throws SQLException;
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/db/sql/ConnectionBundlePool.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */