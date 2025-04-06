/*     */ package com.mchange.v1.db.sql;
/*     */ 
/*     */ import com.mchange.io.UnsupportedVersionException;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Serializable;
/*     */ import java.sql.Connection;
/*     */ import java.sql.DriverManager;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.SQLFeatureNotSupportedException;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Properties;
/*     */ import java.util.logging.Logger;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.Name;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.Reference;
/*     */ import javax.naming.Referenceable;
/*     */ import javax.naming.StringRefAddr;
/*     */ import javax.naming.spi.ObjectFactory;
/*     */ import javax.sql.DataSource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DriverManagerDataSource
/*     */   implements DataSource, Serializable, Referenceable
/*     */ {
/*  51 */   static final String REF_FACTORY_NAME = DmdsObjectFactory.class.getName();
/*     */   static final String REF_JDBC_URL = "jdbcUrl";
/*     */   static final String REF_DFLT_USER = "dfltUser";
/*     */   static final String REF_DFLT_PWD = "dfltPassword";
/*     */   String jdbcUrl;
/*     */   String dfltUser;
/*     */   String dfltPassword;
/*     */   static final long serialVersionUID = 1L;
/*     */   private static final short VERSION = 1;
/*     */   
/*     */   public DriverManagerDataSource(String paramString1, String paramString2, String paramString3) {
/*  62 */     this.jdbcUrl = paramString1;
/*  63 */     this.dfltUser = paramString2;
/*  64 */     this.dfltPassword = paramString3;
/*     */   }
/*     */   
/*     */   public DriverManagerDataSource(String paramString) {
/*  68 */     this(paramString, null, null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Connection getConnection() throws SQLException {
/*  74 */     return DriverManager.getConnection(this.jdbcUrl, createProps(null, null));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Connection getConnection(String paramString1, String paramString2) throws SQLException {
/*  81 */     return DriverManager.getConnection(this.jdbcUrl, createProps(paramString1, paramString2));
/*     */   }
/*     */   
/*     */   public PrintWriter getLogWriter() throws SQLException {
/*  85 */     return DriverManager.getLogWriter();
/*     */   }
/*     */   public void setLogWriter(PrintWriter paramPrintWriter) throws SQLException {
/*  88 */     DriverManager.setLogWriter(paramPrintWriter);
/*     */   }
/*     */   public int getLoginTimeout() throws SQLException {
/*  91 */     return DriverManager.getLoginTimeout();
/*     */   }
/*     */   public void setLoginTimeout(int paramInt) throws SQLException {
/*  94 */     DriverManager.setLoginTimeout(paramInt);
/*     */   }
/*     */   
/*     */   public boolean isWrapperFor(Class<?> paramClass) throws SQLException {
/*  98 */     return false;
/*     */   }
/*     */   public <T> T unwrap(Class<T> paramClass) throws SQLException {
/* 101 */     throw new SQLException(getClass().getName() + " is not a wrapper for an object implementing any interface.");
/*     */   }
/*     */ 
/*     */   
/*     */   public Reference getReference() throws NamingException {
/* 106 */     Reference reference = new Reference(getClass().getName(), REF_FACTORY_NAME, null);
/*     */ 
/*     */     
/* 109 */     reference.add(new StringRefAddr("jdbcUrl", this.jdbcUrl));
/* 110 */     reference.add(new StringRefAddr("dfltUser", this.dfltUser));
/* 111 */     reference.add(new StringRefAddr("dfltPassword", this.dfltPassword));
/*     */     
/* 113 */     return reference;
/*     */   }
/*     */ 
/*     */   
/*     */   public Logger getParentLogger() throws SQLFeatureNotSupportedException {
/* 118 */     throw new SQLFeatureNotSupportedException("javax.sql.DataSource.getParentLogger() is not currently supported by " + getClass().getName());
/*     */   }
/*     */   
/*     */   private Properties createProps(String paramString1, String paramString2) {
/* 122 */     Properties properties = new Properties();
/* 123 */     if (paramString1 != null) {
/*     */       
/* 125 */       properties.put("user", paramString1);
/* 126 */       properties.put("password", paramString2);
/*     */     }
/* 128 */     else if (this.dfltUser != null) {
/*     */       
/* 130 */       properties.put("user", this.dfltUser);
/* 131 */       properties.put("password", this.dfltPassword);
/*     */     } 
/* 133 */     return properties;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
/* 142 */     paramObjectOutputStream.writeShort(1);
/* 143 */     paramObjectOutputStream.writeUTF(this.jdbcUrl);
/* 144 */     paramObjectOutputStream.writeUTF(this.dfltUser);
/* 145 */     paramObjectOutputStream.writeUTF(this.dfltPassword);
/*     */   }
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
/* 150 */     short s = paramObjectInputStream.readShort();
/* 151 */     switch (s) {
/*     */       
/*     */       case 1:
/* 154 */         this.jdbcUrl = paramObjectInputStream.readUTF();
/* 155 */         this.dfltUser = paramObjectInputStream.readUTF();
/* 156 */         this.dfltPassword = paramObjectInputStream.readUTF();
/*     */         return;
/*     */     } 
/* 159 */     throw new UnsupportedVersionException(this, s);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class DmdsObjectFactory
/*     */     implements ObjectFactory
/*     */   {
/*     */     public Object getObjectInstance(Object param1Object, Name param1Name, Context param1Context, Hashtable param1Hashtable) throws Exception {
/* 170 */       String str = DriverManagerDataSource.class.getName(); Reference reference;
/* 171 */       if (param1Object instanceof Reference && (reference = (Reference)param1Object).getClassName().equals(str))
/*     */       {
/*     */         
/* 174 */         return new DriverManagerDataSource((String)reference.get("jdbcUrl").getContent(), (String)reference.get("dfltUser").getContent(), (String)reference.get("dfltPassword").getContent());
/*     */       }
/*     */ 
/*     */ 
/*     */       
/* 179 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/db/sql/DriverManagerDataSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */