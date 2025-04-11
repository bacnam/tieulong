package com.mchange.v2.c3p0.management;

import java.sql.SQLException;
import java.util.Set;

public interface C3P0RegistryManagerMBean {
    String[] getAllIdentityTokens();

    Set getAllIdentityTokenized();

    Set getAllPooledDataSources();

    int getAllIdentityTokenCount();

    int getAllIdentityTokenizedCount();

    int getAllPooledDataSourcesCount();

    String[] getAllIdentityTokenizedStringified();

    String[] getAllPooledDataSourcesStringified();

    int getNumPooledDataSources() throws SQLException;

    int getNumPoolsAllDataSources() throws SQLException;

    String getC3p0Version();
}

