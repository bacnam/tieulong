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

