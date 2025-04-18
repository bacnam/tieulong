package com.mchange.v1.db.sql.schemarep;

import java.util.List;

public interface ForeignKeyRep {
  List getLocalColumnNames();

  String getReferencedTableName();

  List getReferencedColumnNames();
}

