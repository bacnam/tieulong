package com.mchange.v2.log;

public class PackageNames
implements NameTransformer
{
public String transformName(String paramString) {
return null;
}

public String transformName(Class paramClass) {
String str = paramClass.getName();
int i = str.lastIndexOf('.');
if (i <= 0) {
return "";
}
return str.substring(0, i);
}

public String transformName() {
return null;
}
}

