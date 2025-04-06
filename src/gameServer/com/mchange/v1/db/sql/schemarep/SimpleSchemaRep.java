package com.mchange.v1.db.sql.schemarep;

import java.util.Set;

public interface SimpleSchemaRep {
  Set getTableNames();
  
  TableRep tableRepForName(String paramString);
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/db/sql/schemarep/SimpleSchemaRep.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */