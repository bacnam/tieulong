package com.mchange.v1.db.sql.schemarep;

import java.util.List;

public interface ForeignKeyRep {
  List getLocalColumnNames();
  
  String getReferencedTableName();
  
  List getReferencedColumnNames();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/db/sql/schemarep/ForeignKeyRep.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */