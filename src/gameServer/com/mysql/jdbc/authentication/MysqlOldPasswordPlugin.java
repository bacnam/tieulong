package com.mysql.jdbc.authentication;

import com.mysql.jdbc.AuthenticationPlugin;
import com.mysql.jdbc.Buffer;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.StringUtils;
import com.mysql.jdbc.Util;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class MysqlOldPasswordPlugin
implements AuthenticationPlugin
{
private Properties properties;
private String password = null;

public void init(Connection conn, Properties props) throws SQLException {
this.properties = props;
}

public void destroy() {
this.password = null;
}

public String getProtocolPluginName() {
return "mysql_old_password";
}

public boolean requiresConfidentiality() {
return false;
}

public boolean isReusable() {
return true;
}

public void setAuthenticationParameters(String user, String password) {
this.password = password;
}

public boolean nextAuthenticationStep(Buffer fromServer, List<Buffer> toServer) throws SQLException {
toServer.clear();

Buffer bresp = null;

String pwd = this.password;
if (pwd == null) {
pwd = this.properties.getProperty("password");
}

bresp = new Buffer(StringUtils.getBytes((fromServer == null || pwd == null || pwd.length() == 0) ? "" : Util.newCrypt(pwd, fromServer.readString().substring(0, 8))));

bresp.setPosition(bresp.getBufLength());
int oldBufLength = bresp.getBufLength();

bresp.writeByte((byte)0);

bresp.setBufLength(oldBufLength + 1);
bresp.setPosition(0);

toServer.add(bresp);

return true;
}
}

