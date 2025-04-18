package com.mchange.v1.db.sql.schemarep;

import com.mchange.v1.util.ListUtils;
import com.mchange.v1.util.MapUtils;
import com.mchange.v1.util.SetUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TableRepImpl
implements TableRep
{
String tableName;
List colNameList;
Map namesToColReps;
Set primaryKeyColNames;
Set foreignKeyReps;
Set uniqConstrReps;

public TableRepImpl(String paramString, List<ColumnRep> paramList, Collection<?> paramCollection1, Collection<?> paramCollection2, Collection<?> paramCollection3) {
this.tableName = paramString;
ArrayList<String> arrayList = new ArrayList();
HashMap<Object, Object> hashMap = new HashMap<Object, Object>(); byte b; int i;
for (b = 0, i = paramList.size(); b < i; b++) {

ColumnRep columnRep = paramList.get(b);
String str = columnRep.getColumnName();
arrayList.add(str);
hashMap.put(str, columnRep);
} 
this.colNameList = Collections.unmodifiableList(arrayList);
this.namesToColReps = Collections.unmodifiableMap(hashMap);
this.primaryKeyColNames = (paramCollection1 == null) ? Collections.EMPTY_SET : Collections.unmodifiableSet(new HashSet(paramCollection1));

this.foreignKeyReps = (paramCollection2 == null) ? Collections.EMPTY_SET : Collections.unmodifiableSet(new HashSet(paramCollection2));

this.uniqConstrReps = (paramCollection3 == null) ? Collections.EMPTY_SET : Collections.unmodifiableSet(new HashSet(paramCollection3));
}

public String getTableName() {
return this.tableName;
}
public Iterator getColumnNames() {
return this.colNameList.iterator();
}
public ColumnRep columnRepForName(String paramString) {
return (ColumnRep)this.namesToColReps.get(paramString);
}
public Set getPrimaryKeyColumnNames() {
return this.primaryKeyColNames;
}
public Set getForeignKeyReps() {
return this.foreignKeyReps;
}
public Set getUniquenessConstraintReps() {
return this.uniqConstrReps;
}

public boolean equals(Object paramObject) {
if (paramObject == null || getClass() != paramObject.getClass()) {
return false;
}
TableRepImpl tableRepImpl = (TableRepImpl)paramObject;
return (this.tableName.equals(tableRepImpl.tableName) && ListUtils.equivalent(this.colNameList, tableRepImpl.colNameList) && MapUtils.equivalentDisregardingSort(this.namesToColReps, tableRepImpl.namesToColReps) && SetUtils.equivalentDisregardingSort(this.primaryKeyColNames, tableRepImpl.primaryKeyColNames) && SetUtils.equivalentDisregardingSort(this.foreignKeyReps, tableRepImpl.foreignKeyReps) && SetUtils.equivalentDisregardingSort(this.uniqConstrReps, tableRepImpl.uniqConstrReps));
}

public int hashCode() {
return this.tableName.hashCode() ^ ListUtils.hashContents(this.colNameList) ^ MapUtils.hashContentsDisregardingSort(this.namesToColReps) ^ SetUtils.hashContentsDisregardingSort(this.primaryKeyColNames) ^ SetUtils.hashContentsDisregardingSort(this.foreignKeyReps) ^ SetUtils.hashContentsDisregardingSort(this.uniqConstrReps);
}
}

