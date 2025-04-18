package com.mchange.v1.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class CleanupUtils
{
public static void attemptClose(Statement paramStatement) {
try {
if (paramStatement != null) paramStatement.close(); 
} catch (SQLException sQLException) {
sQLException.printStackTrace();
} 
}
public static void attemptClose(Connection paramConnection) {
try {
if (paramConnection != null) paramConnection.close(); 
} catch (SQLException sQLException) {
sQLException.printStackTrace();
} 
}
public static void attemptRollback(Connection paramConnection) {
try {
if (paramConnection != null) paramConnection.rollback(); 
} catch (SQLException sQLException) {
sQLException.printStackTrace();
} 
}
}

