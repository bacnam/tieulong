package com.mysql.jdbc;

import java.sql.SQLException;
import java.util.List;

public interface AuthenticationPlugin extends Extension {
    String getProtocolPluginName();

    boolean requiresConfidentiality();

    boolean isReusable();

    void setAuthenticationParameters(String paramString1, String paramString2);

    boolean nextAuthenticationStep(Buffer paramBuffer, List<Buffer> paramList) throws SQLException;
}

