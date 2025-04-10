package com.mysql.jdbc.authentication;

import com.mysql.jdbc.AuthenticationPlugin;
import com.mysql.jdbc.Buffer;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.StringUtils;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class MysqlClearPasswordPlugin
implements AuthenticationPlugin
{
private String password = null;

public void init(Connection conn, Properties props) throws SQLException {}

public void destroy() {
this.password = null;
}

public String getProtocolPluginName() {
return "mysql_clear_password";
}

public boolean requiresConfidentiality() {
return true;
}

public boolean isReusable() {
return true;
}

public void setAuthenticationParameters(String user, String password) {
this.password = password;
}

public boolean nextAuthenticationStep(Buffer fromServer, List<Buffer> toServer) throws SQLException {
toServer.clear();

Buffer bresp = new Buffer(StringUtils.getBytes((this.password != null) ? this.password : ""));

bresp.setPosition(bresp.getBufLength());
int oldBufLength = bresp.getBufLength();

bresp.writeByte((byte)0);

bresp.setBufLength(oldBufLength + 1);
bresp.setPosition(0);

toServer.add(bresp);
return true;
}
}

