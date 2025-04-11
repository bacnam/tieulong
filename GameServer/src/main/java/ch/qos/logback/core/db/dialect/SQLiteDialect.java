package ch.qos.logback.core.db.dialect;

public class SQLiteDialect
        implements SQLDialect {
    public static final String SELECT_CURRVAL = "SELECT last_insert_rowid();";

    public String getSelectInsertId() {
        return "SELECT last_insert_rowid();";
    }
}

