package com.zhonglian.server.common.data.ref;

import com.zhonglian.server.common.data.RefContainer;

public abstract class RefBase
{
public abstract boolean Assert();

public abstract boolean AssertAll(RefContainer<?> paramRefContainer);

public String toString() {
return super.toString();
}

public String[] getOptionFields() {
return new String[0]; } public boolean isFieldRequired(String filed) {
byte b;
int i;
String[] arrayOfString;
for (i = (arrayOfString = getOptionFields()).length, b = 0; b < i; ) { String f = arrayOfString[b];
if (f.equals(filed))
return false; 
b++; }

return true;
}
}

