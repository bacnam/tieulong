package com.mchange.v1.db.sql.xmlpropsschema;

import com.mchange.v1.xmlprops.DomXmlPropsParser;
import com.mchange.v1.xmlprops.XmlPropsException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.StringTokenizer;

public class XmlPropsSchemaHelper
{
Properties props;

public XmlPropsSchemaHelper(InputStream paramInputStream) throws XmlPropsException {
DomXmlPropsParser domXmlPropsParser = new DomXmlPropsParser();
this.props = domXmlPropsParser.parseXmlProps(paramInputStream);
}

public PreparedStatement prepareXmlStatement(Connection paramConnection, String paramString) throws SQLException {
return paramConnection.prepareStatement(getKey(paramString));
}
public void executeViaStatement(Statement paramStatement, String paramString) throws SQLException {
paramStatement.executeUpdate(getKey(paramString));
}

public StringTokenizer getItems(String paramString) {
String str = getKey(paramString);
return new StringTokenizer(str, ", \t\r\n");
}

public String getKey(String paramString) {
return this.props.getProperty(paramString).trim();
}
}

