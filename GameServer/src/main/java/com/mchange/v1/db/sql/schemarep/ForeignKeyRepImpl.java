package com.mchange.v1.db.sql.schemarep;

import com.mchange.v1.util.ListUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ForeignKeyRepImpl
implements ForeignKeyRep
{
List locColNames;
String refTableName;
List refColNames;

public ForeignKeyRepImpl(List<?> paramList1, String paramString, List<?> paramList2) {
this.locColNames = Collections.unmodifiableList(new ArrayList(paramList1));
this.refTableName = paramString;
this.refColNames = Collections.unmodifiableList(new ArrayList(paramList2));
}

public List getLocalColumnNames() {
return this.locColNames;
}
public String getReferencedTableName() {
return this.refTableName;
}
public List getReferencedColumnNames() {
return this.refColNames;
}

public boolean equals(Object paramObject) {
if (paramObject == null || getClass() != paramObject.getClass()) {
return false;
}
ForeignKeyRepImpl foreignKeyRepImpl = (ForeignKeyRepImpl)paramObject;
return (ListUtils.equivalent(this.locColNames, foreignKeyRepImpl.locColNames) && this.refTableName.equals(foreignKeyRepImpl.refTableName) && ListUtils.equivalent(this.refColNames, foreignKeyRepImpl.refColNames));
}

public int hashCode() {
return ListUtils.hashContents(this.locColNames) ^ this.refTableName.hashCode() ^ ListUtils.hashContents(this.refColNames);
}
}

