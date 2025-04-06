package com.mchange.v1.db.sql.schemarep;

public interface ColumnRep {
  String getColumnName();
  
  int getColumnType();
  
  int[] getColumnSize();
  
  boolean acceptsNulls();
  
  Object getDefaultValue();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/db/sql/schemarep/ColumnRep.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */