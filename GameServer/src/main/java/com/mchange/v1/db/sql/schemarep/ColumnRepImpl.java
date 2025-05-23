package com.mchange.v1.db.sql.schemarep;

import com.mchange.lang.ArrayUtils;
import java.util.Arrays;

public class ColumnRepImpl
implements ColumnRep
{
String colName;
int col_type;
int[] colSize;
boolean accepts_nulls;
Object defaultValue;

public ColumnRepImpl(String paramString, int paramInt) {
this(paramString, paramInt, null);
}
public ColumnRepImpl(String paramString, int paramInt, int[] paramArrayOfint) {
this(paramString, paramInt, paramArrayOfint, false, null);
}

public ColumnRepImpl(String paramString, int paramInt, int[] paramArrayOfint, boolean paramBoolean, Object paramObject) {
this.colName = paramString;
this.col_type = paramInt;
this.colSize = paramArrayOfint;
this.accepts_nulls = paramBoolean;
this.defaultValue = paramObject;
}

public String getColumnName() {
return this.colName;
}
public int getColumnType() {
return this.col_type;
}
public int[] getColumnSize() {
return this.colSize;
}
public boolean acceptsNulls() {
return this.accepts_nulls;
}
public Object getDefaultValue() {
return this.defaultValue;
}

public boolean equals(Object paramObject) {
if (paramObject == null || getClass() != paramObject.getClass()) {
return false;
}
ColumnRepImpl columnRepImpl = (ColumnRepImpl)paramObject;
if (!this.colName.equals(columnRepImpl.colName) || this.col_type != columnRepImpl.col_type || this.accepts_nulls != columnRepImpl.accepts_nulls)
{

return false;
}
if (this.colSize != columnRepImpl.colSize && !Arrays.equals(this.colSize, columnRepImpl.colSize)) {
return false;
}
if (this.defaultValue != columnRepImpl.defaultValue && this.defaultValue != null && !this.defaultValue.equals(columnRepImpl.defaultValue))
{

return false;
}
return true;
}

public int hashCode() {
int i = this.colName.hashCode() ^ this.col_type;

if (!this.accepts_nulls) i ^= 0xFFFFFFFF;

if (this.colSize != null) {
i ^= ArrayUtils.hashAll(this.colSize);
}
if (this.defaultValue != null) {
i ^= this.defaultValue.hashCode();
}
return i;
}
}

