package com.mchange.v1.db.sql.schemarep;

public interface ColumnRep {
  String getColumnName();

  int getColumnType();

  int[] getColumnSize();

  boolean acceptsNulls();

  Object getDefaultValue();
}

