package com.mchange.v2.sql.filter;

import com.mchange.v1.lang.ClassUtils;
import com.mchange.v2.codegen.intfc.DelegatorGenerator;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.sql.DataSource;

public final class RecreatePackage
{
static final Class[] intfcs = new Class[] { Connection.class, ResultSet.class, DatabaseMetaData.class, Statement.class, PreparedStatement.class, CallableStatement.class, DataSource.class };

public static void main(String[] paramArrayOfString) {
try {
DelegatorGenerator delegatorGenerator = new DelegatorGenerator();
String str1 = RecreatePackage.class.getName();
String str2 = str1.substring(0, str1.lastIndexOf('.'));
for (byte b = 0; b < intfcs.length; b++)
{
Class clazz = intfcs[b];
String str3 = ClassUtils.simpleClassName(clazz);
String str4 = "Filter" + str3;
String str5 = "SynchronizedFilter" + str3;

BufferedWriter bufferedWriter = null;

try {
bufferedWriter = new BufferedWriter(new FileWriter(str4 + ".java"));
delegatorGenerator.setMethodModifiers(1);
delegatorGenerator.writeDelegator(clazz, str2 + '.' + str4, bufferedWriter);
System.err.println(str4);

}
finally {}

}

}
catch (Exception exception) {
exception.printStackTrace();
} 
}
}

