package com.mysql.jdbc;

class Collation
{
public int index;
public String collationName;
public String charsetName;
public String javaCharsetName;

public Collation(int index, String collationName, String charsetName) {
this.index = index;
this.collationName = collationName;
this.charsetName = charsetName;
this.javaCharsetName = CharsetMapping.MYSQL_TO_JAVA_CHARSET_MAP.get(charsetName);
}

public Collation(int index, String collationName, String charsetName, String javaCharsetName) {
this.index = index;
this.collationName = collationName;
this.charsetName = charsetName;
this.javaCharsetName = javaCharsetName;
}

public String toString() {
StringBuffer asString = new StringBuffer();
asString.append("[");
asString.append("index=");
asString.append(this.index);
asString.append(",collationName=");
asString.append(this.collationName);
asString.append(",charsetName=");
asString.append(this.charsetName);
asString.append(",javaCharsetName=");
asString.append(this.javaCharsetName);
asString.append("]");
return asString.toString();
}
}

