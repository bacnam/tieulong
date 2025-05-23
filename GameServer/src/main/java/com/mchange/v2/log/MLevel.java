package com.mchange.v2.log;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class MLevel
{
public static final MLevel ALL;
public static final MLevel CONFIG;
public static final MLevel FINE;
public static final MLevel FINER;
public static final MLevel FINEST;
public static final MLevel INFO;
public static final MLevel OFF;
public static final MLevel SEVERE;
public static final MLevel WARNING;
private static final Map integersToMLevels;
private static final Map namesToMLevels;
private static final int ALL_INTVAL = -2147483648;
private static final int CONFIG_INTVAL = 700;
private static final int FINE_INTVAL = 500;
private static final int FINER_INTVAL = 400;
private static final int FINEST_INTVAL = 300;
private static final int INFO_INTVAL = 800;
private static final int OFF_INTVAL = 2147483647;
private static final int SEVERE_INTVAL = 1000;
private static final int WARNING_INTVAL = 900;
Object level;
int intval;
String lvlstring;

public static MLevel fromIntValue(int paramInt) {
return (MLevel)integersToMLevels.get(new Integer(paramInt));
}
public static MLevel fromSeverity(String paramString) {
return (MLevel)namesToMLevels.get(paramString);
}

static {
Class clazz;
boolean bool;
MLevel mLevel1, mLevel2, mLevel3, mLevel4, mLevel5, mLevel6, mLevel7, mLevel8, mLevel9;
try {
clazz = Class.forName("java.util.logging.Level");
bool = true;
}
catch (ClassNotFoundException classNotFoundException) {

clazz = null;
bool = false;
} 

try {
mLevel1 = new MLevel(bool ? clazz.getField("ALL").get(null) : null, -2147483648, "ALL");
mLevel2 = new MLevel(bool ? clazz.getField("CONFIG").get(null) : null, 700, "CONFIG");
mLevel3 = new MLevel(bool ? clazz.getField("FINE").get(null) : null, 500, "FINE");
mLevel4 = new MLevel(bool ? clazz.getField("FINER").get(null) : null, 400, "FINER");
mLevel5 = new MLevel(bool ? clazz.getField("FINEST").get(null) : null, 300, "FINEST");
mLevel6 = new MLevel(bool ? clazz.getField("INFO").get(null) : null, 800, "INFO");
mLevel7 = new MLevel(bool ? clazz.getField("OFF").get(null) : null, 2147483647, "OFF");
mLevel8 = new MLevel(bool ? clazz.getField("SEVERE").get(null) : null, 1000, "SEVERE");
mLevel9 = new MLevel(bool ? clazz.getField("WARNING").get(null) : null, 900, "WARNING");
}
catch (Exception exception) {

exception.printStackTrace();
throw new InternalError("Huh? java.util.logging.Level is here, but not its expected public fields?");
} 

ALL = mLevel1;
CONFIG = mLevel2;
FINE = mLevel3;
FINER = mLevel4;
FINEST = mLevel5;
INFO = mLevel6;
OFF = mLevel7;
SEVERE = mLevel8;
WARNING = mLevel9;

HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
hashMap.put(new Integer(mLevel1.intValue()), mLevel1);
hashMap.put(new Integer(mLevel2.intValue()), mLevel2);
hashMap.put(new Integer(mLevel3.intValue()), mLevel3);
hashMap.put(new Integer(mLevel4.intValue()), mLevel4);
hashMap.put(new Integer(mLevel5.intValue()), mLevel5);
hashMap.put(new Integer(mLevel6.intValue()), mLevel6);
hashMap.put(new Integer(mLevel7.intValue()), mLevel7);
hashMap.put(new Integer(mLevel8.intValue()), mLevel8);
hashMap.put(new Integer(mLevel9.intValue()), mLevel9);

integersToMLevels = Collections.unmodifiableMap(hashMap);

hashMap = new HashMap<Object, Object>();
hashMap.put(mLevel1.getSeverity(), mLevel1);
hashMap.put(mLevel2.getSeverity(), mLevel2);
hashMap.put(mLevel3.getSeverity(), mLevel3);
hashMap.put(mLevel4.getSeverity(), mLevel4);
hashMap.put(mLevel5.getSeverity(), mLevel5);
hashMap.put(mLevel6.getSeverity(), mLevel6);
hashMap.put(mLevel7.getSeverity(), mLevel7);
hashMap.put(mLevel8.getSeverity(), mLevel8);
hashMap.put(mLevel9.getSeverity(), mLevel9);

namesToMLevels = Collections.unmodifiableMap(hashMap);
}

public int intValue() {
return this.intval;
}
public Object asJdk14Level() {
return this.level;
}
public String getSeverity() {
return this.lvlstring;
}
public String toString() {
return getClass().getName() + getLineHeader();
}
public String getLineHeader() {
return "[" + this.lvlstring + ']';
}
public boolean isLoggable(MLevel paramMLevel) {
return (this.intval >= paramMLevel.intval);
}

private MLevel(Object paramObject, int paramInt, String paramString) {
this.level = paramObject;
this.intval = paramInt;
this.lvlstring = paramString;
}
}

