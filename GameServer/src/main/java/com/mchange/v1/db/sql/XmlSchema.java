package com.mchange.v1.db.sql;

import org.xml.sax.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class XmlSchema
        implements Schema {
    private static final int CREATE = 0;
    private static final int DROP = 1;
    List createStmts;
    List dropStmts;
    Map appMap;

    public XmlSchema(URL paramURL) throws SAXException, IOException, ParserConfigurationException {
        parse(paramURL.openStream());
    }

    public XmlSchema(InputStream paramInputStream) throws SAXException, IOException, ParserConfigurationException {
        parse(paramInputStream);
    }

    public XmlSchema() {
    }

    public static void main(String[] paramArrayOfString) {
        try {
            XmlSchema xmlSchema = new XmlSchema(XmlSchema.class.getResource("/com/mchange/v1/hjug/hjugschema.xml"));

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void parse(InputStream paramInputStream) throws SAXException, IOException, ParserConfigurationException {
        this.createStmts = new ArrayList();
        this.dropStmts = new ArrayList();
        this.appMap = new HashMap<Object, Object>();

        InputSource inputSource = new InputSource();
        inputSource.setByteStream(paramInputStream);

        inputSource.setSystemId(XmlSchema.class.getResource("schema.dtd").toExternalForm());

        SAXParser sAXParser = SAXParserFactory.newInstance().newSAXParser();
        MySaxHandler mySaxHandler = new MySaxHandler();

        sAXParser.parse(inputSource, mySaxHandler);
    }

    private void doStatementList(List paramList, Connection paramConnection) throws SQLException {
        if (paramList != null) {

            Statement statement = null;

            try {
                statement = paramConnection.createStatement();
                for (Iterator<String> iterator = paramList.iterator(); iterator.hasNext(); )
                    statement.executeUpdate(iterator.next());
                paramConnection.commit();
            } catch (SQLException sQLException) {

                ConnectionUtils.attemptRollback(paramConnection);
                sQLException.fillInStackTrace();
                throw sQLException;
            } finally {

                StatementUtils.attemptClose(statement);
            }
        }
    }

    public String getStatementText(String paramString1, String paramString2) {
        SqlApp sqlApp = (SqlApp) this.appMap.get(paramString1);
        String str = null;
        if (sqlApp != null)
            str = sqlApp.getStatementText(paramString2);
        return str;
    }

    public void createSchema(Connection paramConnection) throws SQLException {
        doStatementList(this.createStmts, paramConnection);
    }

    public void dropSchema(Connection paramConnection) throws SQLException {
        doStatementList(this.dropStmts, paramConnection);
    }

    class MySaxHandler
            extends HandlerBase {
        int state = -1;
        boolean in_statement = false;
        boolean in_comment = false;
        StringBuffer charBuff = null;
        XmlSchema.SqlApp currentApp = null;
        String currentStmtName = null;

        public void startElement(String param1String, AttributeList param1AttributeList) {
            if (param1String.equals("create")) {
                this.state = 0;
            } else if (param1String.equals("drop")) {
                this.state = 1;
            } else if (param1String.equals("statement")) {

                this.in_statement = true;
                this.charBuff = new StringBuffer();
                if (this.currentApp != null) {
                    byte b;
                    int i;
                    for (b = 0, i = param1AttributeList.getLength(); b < i; b++) {

                        String str = param1AttributeList.getName(b);
                        if (str.equals("name")) {

                            this.currentStmtName = param1AttributeList.getValue(b);

                            break;
                        }
                    }
                }
            } else if (param1String.equals("comment")) {
                this.in_comment = true;
            } else if (param1String.equals("application")) {
                byte b;
                int i;
                for (b = 0, i = param1AttributeList.getLength(); b < i; b++) {

                    String str = param1AttributeList.getName(b);
                    if (str.equals("name")) {

                        String str1 = param1AttributeList.getValue(b);
                        this.currentApp = (XmlSchema.SqlApp) XmlSchema.this.appMap.get(str1);
                        if (this.currentApp == null) {

                            this.currentApp = new XmlSchema.SqlApp();
                            XmlSchema.this.appMap.put(str1.intern(), this.currentApp);
                        }
                        break;
                    }
                }
            }
        }

        public void characters(char[] param1ArrayOfchar, int param1Int1, int param1Int2) throws SAXException {
            if (!this.in_comment) {
                if (this.in_statement) {
                    this.charBuff.append(param1ArrayOfchar, param1Int1, param1Int2);
                }
            }
        }

        public void endElement(String param1String) {
            if (param1String.equals("statement")) {

                String str = this.charBuff.toString().trim();
                if (this.state == 0) {
                    XmlSchema.this.createStmts.add(str);
                } else if (this.state == 1) {
                    XmlSchema.this.dropStmts.add(str);
                } else if (this.currentApp != null && this.currentStmtName != null) {
                    this.currentApp.setStatementText(this.currentStmtName, str);
                }
            } else if (param1String.equals("create") || param1String.equals("drop")) {
                this.state = -1;
            } else if (param1String.equals("comment")) {
                this.in_comment = false;
            } else if (param1String.equals("application")) {
                this.currentApp = null;
            }
        }

        public void warning(SAXParseException param1SAXParseException) {
            System.err.println("[Warning] " + param1SAXParseException.getMessage());
        }

        public void error(SAXParseException param1SAXParseException) {
            System.err.println("[Error] " + param1SAXParseException.getMessage());
        }

        public void fatalError(SAXParseException param1SAXParseException) throws SAXException {
            System.err.println("[Fatal Error] " + param1SAXParseException.getMessage());

            throw param1SAXParseException;
        }
    }

    class SqlApp {
        Map stmtMap = new HashMap<Object, Object>();

        public void setStatementText(String param1String1, String param1String2) {
            this.stmtMap.put(param1String1, param1String2);
        }

        public String getStatementText(String param1String) {
            return (String) this.stmtMap.get(param1String);
        }
    }
}

