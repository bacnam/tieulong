package ch.qos.logback.classic.db.names;

public interface DBNameResolver {
    <N extends Enum<?>> String getTableName(N paramN);

    <N extends Enum<?>> String getColumnName(N paramN);
}

