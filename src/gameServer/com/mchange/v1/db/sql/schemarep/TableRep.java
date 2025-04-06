package com.mchange.v1.db.sql.schemarep;

import java.util.Iterator;
import java.util.Set;

public interface TableRep {
  String getTableName();
  
  Iterator getColumnNames();
  
  ColumnRep columnRepForName(String paramString);
  
  Set getPrimaryKeyColumnNames();
  
  Set getForeignKeyReps();
  
  Set getUniquenessConstraintReps();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/db/sql/schemarep/TableRep.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */