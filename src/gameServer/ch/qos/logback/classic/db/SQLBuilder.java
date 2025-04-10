package ch.qos.logback.classic.db;

import ch.qos.logback.classic.db.names.ColumnName;
import ch.qos.logback.classic.db.names.DBNameResolver;
import ch.qos.logback.classic.db.names.TableName;

public class SQLBuilder
{
static String buildInsertPropertiesSQL(DBNameResolver dbNameResolver) {
StringBuilder sqlBuilder = new StringBuilder("INSERT INTO ");
sqlBuilder.append(dbNameResolver.getTableName((Enum)TableName.LOGGING_EVENT_PROPERTY)).append(" (");
sqlBuilder.append(dbNameResolver.getColumnName((Enum)ColumnName.EVENT_ID)).append(", ");
sqlBuilder.append(dbNameResolver.getColumnName((Enum)ColumnName.MAPPED_KEY)).append(", ");
sqlBuilder.append(dbNameResolver.getColumnName((Enum)ColumnName.MAPPED_VALUE)).append(") ");
sqlBuilder.append("VALUES (?, ?, ?)");
return sqlBuilder.toString();
}

static String buildInsertExceptionSQL(DBNameResolver dbNameResolver) {
StringBuilder sqlBuilder = new StringBuilder("INSERT INTO ");
sqlBuilder.append(dbNameResolver.getTableName((Enum)TableName.LOGGING_EVENT_EXCEPTION)).append(" (");
sqlBuilder.append(dbNameResolver.getColumnName((Enum)ColumnName.EVENT_ID)).append(", ");
sqlBuilder.append(dbNameResolver.getColumnName((Enum)ColumnName.I)).append(", ");
sqlBuilder.append(dbNameResolver.getColumnName((Enum)ColumnName.TRACE_LINE)).append(") ");
sqlBuilder.append("VALUES (?, ?, ?)");
return sqlBuilder.toString();
}

static String buildInsertSQL(DBNameResolver dbNameResolver) {
StringBuilder sqlBuilder = new StringBuilder("INSERT INTO ");
sqlBuilder.append(dbNameResolver.getTableName((Enum)TableName.LOGGING_EVENT)).append(" (");
sqlBuilder.append(dbNameResolver.getColumnName((Enum)ColumnName.TIMESTMP)).append(", ");
sqlBuilder.append(dbNameResolver.getColumnName((Enum)ColumnName.FORMATTED_MESSAGE)).append(", ");
sqlBuilder.append(dbNameResolver.getColumnName((Enum)ColumnName.LOGGER_NAME)).append(", ");
sqlBuilder.append(dbNameResolver.getColumnName((Enum)ColumnName.LEVEL_STRING)).append(", ");
sqlBuilder.append(dbNameResolver.getColumnName((Enum)ColumnName.THREAD_NAME)).append(", ");
sqlBuilder.append(dbNameResolver.getColumnName((Enum)ColumnName.REFERENCE_FLAG)).append(", ");
sqlBuilder.append(dbNameResolver.getColumnName((Enum)ColumnName.ARG0)).append(", ");
sqlBuilder.append(dbNameResolver.getColumnName((Enum)ColumnName.ARG1)).append(", ");
sqlBuilder.append(dbNameResolver.getColumnName((Enum)ColumnName.ARG2)).append(", ");
sqlBuilder.append(dbNameResolver.getColumnName((Enum)ColumnName.ARG3)).append(", ");
sqlBuilder.append(dbNameResolver.getColumnName((Enum)ColumnName.CALLER_FILENAME)).append(", ");
sqlBuilder.append(dbNameResolver.getColumnName((Enum)ColumnName.CALLER_CLASS)).append(", ");
sqlBuilder.append(dbNameResolver.getColumnName((Enum)ColumnName.CALLER_METHOD)).append(", ");
sqlBuilder.append(dbNameResolver.getColumnName((Enum)ColumnName.CALLER_LINE)).append(") ");
sqlBuilder.append("VALUES (?, ?, ? ,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
return sqlBuilder.toString();
}
}

