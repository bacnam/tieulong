package com.mchange.v1.db.sql.schemarep;

import java.util.Set;

public interface SimpleSchemaRep {
  Set getTableNames();

  TableRep tableRepForName(String paramString);
}

