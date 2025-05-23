package com.mchange.v1.db.sql;

import com.mchange.util.impl.CommandLineParserImpl;
import com.mchange.v1.util.CleanupUtils;
import java.sql.Connection;
import java.sql.DriverManager;

public class SchemaManager
{
static final String[] VALID = new String[] { "create", "drop" };

public static void main(String[] paramArrayOfString) {
Connection connection = null;

try {
CommandLineParserImpl commandLineParserImpl = new CommandLineParserImpl(paramArrayOfString, VALID, null, null);
boolean bool = commandLineParserImpl.checkSwitch("create");

if (!commandLineParserImpl.checkArgv()) usage(); 
if (!(bool ^ commandLineParserImpl.checkSwitch("drop"))) usage();

String[] arrayOfString = commandLineParserImpl.findUnswitchedArgs();

if (arrayOfString.length == 2) {
connection = DriverManager.getConnection(arrayOfString[0]);
} else if (arrayOfString.length == 4) {
connection = DriverManager.getConnection(arrayOfString[0], arrayOfString[1], arrayOfString[2]);
} else {
usage();
} 
connection.setAutoCommit(false);

Schema schema = (Schema)Class.forName(arrayOfString[arrayOfString.length - 1]).newInstance();
if (bool)
{
schema.createSchema(connection);
System.out.println("Schema created.");
}
else
{
schema.dropSchema(connection);
System.out.println("Schema dropped.");
}

} catch (Exception exception) {
exception.printStackTrace();
} finally {
CleanupUtils.attemptClose(connection);
} 
}

static void usage() {
System.err.println("java -Djdbc.drivers=<driverclass> com.mchange.v1.db.sql.SchemaManager [-create | -drop] <jdbc_url> [<user> <password>] <schemaclass>");

System.exit(-1);
}
}

