/*    */ package com.mchange.v1.db.sql.xmlpropsschema;
/*    */ 
/*    */ import com.mchange.v1.xmlprops.DomXmlPropsParser;
/*    */ import com.mchange.v1.xmlprops.XmlPropsException;
/*    */ import java.io.InputStream;
/*    */ import java.sql.Connection;
/*    */ import java.sql.PreparedStatement;
/*    */ import java.sql.SQLException;
/*    */ import java.sql.Statement;
/*    */ import java.util.Properties;
/*    */ import java.util.StringTokenizer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class XmlPropsSchemaHelper
/*    */ {
/*    */   Properties props;
/*    */   
/*    */   public XmlPropsSchemaHelper(InputStream paramInputStream) throws XmlPropsException {
/* 52 */     DomXmlPropsParser domXmlPropsParser = new DomXmlPropsParser();
/* 53 */     this.props = domXmlPropsParser.parseXmlProps(paramInputStream);
/*    */   }
/*    */ 
/*    */   
/*    */   public PreparedStatement prepareXmlStatement(Connection paramConnection, String paramString) throws SQLException {
/* 58 */     return paramConnection.prepareStatement(getKey(paramString));
/*    */   }
/*    */   public void executeViaStatement(Statement paramStatement, String paramString) throws SQLException {
/* 61 */     paramStatement.executeUpdate(getKey(paramString));
/*    */   }
/*    */   
/*    */   public StringTokenizer getItems(String paramString) {
/* 65 */     String str = getKey(paramString);
/* 66 */     return new StringTokenizer(str, ", \t\r\n");
/*    */   }
/*    */   
/*    */   public String getKey(String paramString) {
/* 70 */     return this.props.getProperty(paramString).trim();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/db/sql/xmlpropsschema/XmlPropsSchemaHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */