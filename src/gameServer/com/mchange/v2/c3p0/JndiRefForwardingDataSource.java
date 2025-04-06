/*     */ package com.mchange.v2.c3p0;
/*     */ 
/*     */ import com.mchange.v2.c3p0.impl.JndiRefDataSourceBase;
/*     */ import com.mchange.v2.log.MLevel;
/*     */ import com.mchange.v2.log.MLog;
/*     */ import com.mchange.v2.log.MLogger;
/*     */ import com.mchange.v2.sql.SqlUtils;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.beans.PropertyVetoException;
/*     */ import java.beans.VetoableChangeListener;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Hashtable;
/*     */ import javax.naming.InitialContext;
/*     */ import javax.naming.Name;
/*     */ import javax.naming.NamingException;
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
/*     */ final class JndiRefForwardingDataSource
/*     */   extends JndiRefDataSourceBase
/*     */   implements DataSource
/*     */ {
/*  61 */   static final MLogger logger = MLog.getLogger(JndiRefForwardingDataSource.class);
/*     */   transient DataSource cachedInner;
/*     */   private static final long serialVersionUID = 1L;
/*     */   private static final short VERSION = 1;
/*     */   
/*     */   public JndiRefForwardingDataSource() {
/*  67 */     this(true);
/*     */   }
/*     */   
/*     */   public JndiRefForwardingDataSource(boolean autoregister) {
/*  71 */     super(autoregister);
/*  72 */     setUpPropertyListeners();
/*     */   }
/*     */ 
/*     */   
/*     */   private void setUpPropertyListeners() {
/*  77 */     VetoableChangeListener l = new VetoableChangeListener()
/*     */       {
/*     */         public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException
/*     */         {
/*  81 */           Object val = evt.getNewValue();
/*  82 */           if ("jndiName".equals(evt.getPropertyName()))
/*     */           {
/*  84 */             if (!(val instanceof Name) && !(val instanceof String))
/*  85 */               throw new PropertyVetoException("jndiName must be a String or a javax.naming.Name", evt); 
/*     */           }
/*     */         }
/*     */       };
/*  89 */     addVetoableChangeListener(l);
/*     */     
/*  91 */     PropertyChangeListener pcl = new PropertyChangeListener()
/*     */       {
/*     */         public void propertyChange(PropertyChangeEvent evt) {
/*  94 */           JndiRefForwardingDataSource.this.cachedInner = null; }
/*     */       };
/*  96 */     addPropertyChangeListener(pcl);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private DataSource dereference() throws SQLException {
/* 102 */     Object jndiName = getJndiName();
/* 103 */     Hashtable<?, ?> jndiEnv = getJndiEnv();
/*     */     
/*     */     try {
/*     */       InitialContext ctx;
/* 107 */       if (jndiEnv != null) {
/* 108 */         ctx = new InitialContext(jndiEnv);
/*     */       } else {
/* 110 */         ctx = new InitialContext();
/* 111 */       }  if (jndiName instanceof String)
/* 112 */         return (DataSource)ctx.lookup((String)jndiName); 
/* 113 */       if (jndiName instanceof Name) {
/* 114 */         return (DataSource)ctx.lookup((Name)jndiName);
/*     */       }
/* 116 */       throw new SQLException("Could not find ConnectionPoolDataSource with JNDI name: " + jndiName);
/*     */     
/*     */     }
/* 119 */     catch (NamingException e) {
/*     */ 
/*     */       
/* 122 */       if (logger.isLoggable(MLevel.WARNING))
/* 123 */         logger.log(MLevel.WARNING, "An Exception occurred while trying to look up a target DataSource via JNDI!", e); 
/* 124 */       throw SqlUtils.toSQLException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private synchronized DataSource inner() throws SQLException {
/* 130 */     if (this.cachedInner != null) {
/* 131 */       return this.cachedInner;
/*     */     }
/*     */     
/* 134 */     DataSource out = dereference();
/* 135 */     if (isCaching())
/* 136 */       this.cachedInner = out; 
/* 137 */     return out;
/*     */   }
/*     */ 
/*     */   
/*     */   public Connection getConnection() throws SQLException {
/* 142 */     return inner().getConnection();
/*     */   }
/*     */   public Connection getConnection(String username, String password) throws SQLException {
/* 145 */     return inner().getConnection(username, password);
/*     */   }
/*     */   public PrintWriter getLogWriter() throws SQLException {
/* 148 */     return inner().getLogWriter();
/*     */   }
/*     */   public void setLogWriter(PrintWriter out) throws SQLException {
/* 151 */     inner().setLogWriter(out);
/*     */   }
/*     */   public int getLoginTimeout() throws SQLException {
/* 154 */     return inner().getLoginTimeout();
/*     */   }
/*     */   public void setLoginTimeout(int seconds) throws SQLException {
/* 157 */     inner().setLoginTimeout(seconds);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream oos) throws IOException {
/* 165 */     oos.writeShort(1);
/*     */   }
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
/* 170 */     short version = ois.readShort();
/* 171 */     switch (version) {
/*     */       
/*     */       case 1:
/* 174 */         setUpPropertyListeners();
/*     */         return;
/*     */     } 
/* 177 */     throw new IOException("Unsupported Serialized Version: " + version);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isWrapperFor(Class<?> iface) throws SQLException {
/* 184 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T unwrap(Class<T> iface) throws SQLException {
/* 189 */     throw new SQLException(this + " is not a Wrapper for " + iface.getName());
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/JndiRefForwardingDataSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */