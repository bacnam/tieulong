/*     */ package com.mchange.v2.c3p0;
/*     */ 
/*     */ import com.mchange.v2.c3p0.cfg.C3P0Config;
/*     */ import com.mchange.v2.c3p0.impl.DriverManagerDataSourceBase;
/*     */ import com.mchange.v2.log.MLevel;
/*     */ import com.mchange.v2.log.MLog;
/*     */ import com.mchange.v2.log.MLogger;
/*     */ import com.mchange.v2.sql.SqlUtils;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.sql.Connection;
/*     */ import java.sql.Driver;
/*     */ import java.sql.DriverManager;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Properties;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class DriverManagerDataSource
/*     */   extends DriverManagerDataSourceBase
/*     */   implements DataSource
/*     */ {
/*  63 */   static final MLogger logger = MLog.getLogger(DriverManagerDataSource.class);
/*     */   
/*     */   Driver driver;
/*     */   
/*     */   static {
/*     */     try {
/*  69 */       Class.forName("java.sql.DriverManager");
/*  70 */     } catch (Exception e) {
/*     */       
/*  72 */       String msg = "Could not load the DriverManager class?!?";
/*  73 */       if (logger.isLoggable(MLevel.SEVERE))
/*  74 */         logger.log(MLevel.SEVERE, msg); 
/*  75 */       throw new InternalError(msg);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   boolean driver_class_loaded = false;
/*     */   
/*     */   private static final long serialVersionUID = 1L;
/*     */   private static final short VERSION = 1;
/*     */   
/*     */   public DriverManagerDataSource() {
/*  86 */     this(true);
/*     */   }
/*     */   
/*     */   public DriverManagerDataSource(boolean autoregister) {
/*  90 */     super(autoregister);
/*     */     
/*  92 */     setUpPropertyListeners();
/*     */     
/*  94 */     String user = C3P0Config.initializeStringPropertyVar("user", null);
/*  95 */     String password = C3P0Config.initializeStringPropertyVar("password", null);
/*     */     
/*  97 */     if (user != null) {
/*  98 */       setUser(user);
/*     */     }
/* 100 */     if (password != null) {
/* 101 */       setPassword(password);
/*     */     }
/*     */   }
/*     */   
/*     */   private void setUpPropertyListeners() {
/* 106 */     PropertyChangeListener driverClassListener = new PropertyChangeListener()
/*     */       {
/*     */         public void propertyChange(PropertyChangeEvent evt)
/*     */         {
/* 110 */           if ("driverClass".equals(evt.getPropertyName())) {
/*     */             
/* 112 */             DriverManagerDataSource.this.setDriverClassLoaded(false);
/*     */ 
/*     */ 
/*     */             
/* 116 */             if (DriverManagerDataSource.this.driverClass != null && DriverManagerDataSource.this.driverClass.trim().length() == 0)
/* 117 */               DriverManagerDataSource.this.driverClass = null; 
/*     */           } 
/*     */         }
/*     */       };
/* 121 */     addPropertyChangeListener(driverClassListener);
/*     */   }
/*     */   
/*     */   private synchronized boolean isDriverClassLoaded() {
/* 125 */     return this.driver_class_loaded;
/*     */   }
/*     */   
/*     */   private synchronized void setDriverClassLoaded(boolean dcl) {
/* 129 */     this.driver_class_loaded = dcl;
/* 130 */     if (!this.driver_class_loaded) clearDriver();
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   private synchronized void ensureDriverLoaded() throws SQLException {
/*     */     try {
/* 137 */       if (!isDriverClassLoaded())
/*     */       {
/* 139 */         if (this.driverClass != null)
/* 140 */           Class.forName(this.driverClass); 
/* 141 */         setDriverClassLoaded(true);
/*     */       }
/*     */     
/* 144 */     } catch (ClassNotFoundException e) {
/*     */       
/* 146 */       if (logger.isLoggable(MLevel.WARNING)) {
/* 147 */         logger.log(MLevel.WARNING, "Could not load driverClass " + this.driverClass, e);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Connection getConnection() throws SQLException {
/* 156 */     ensureDriverLoaded();
/*     */     
/* 158 */     Connection out = driver().connect(this.jdbcUrl, this.properties);
/* 159 */     if (out == null) {
/* 160 */       throw new SQLException("Apparently, jdbc URL '" + this.jdbcUrl + "' is not valid for the underlying " + "driver [" + driver() + "].");
/*     */     }
/* 162 */     return out;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Connection getConnection(String username, String password) throws SQLException {
/* 170 */     ensureDriverLoaded();
/*     */     
/* 172 */     Connection out = driver().connect(this.jdbcUrl, overrideProps(username, password));
/* 173 */     if (out == null) {
/* 174 */       throw new SQLException("Apparently, jdbc URL '" + this.jdbcUrl + "' is not valid for the underlying " + "driver [" + driver() + "].");
/*     */     }
/* 176 */     return out;
/*     */   }
/*     */   
/*     */   public PrintWriter getLogWriter() throws SQLException {
/* 180 */     return DriverManager.getLogWriter();
/*     */   }
/*     */   public void setLogWriter(PrintWriter out) throws SQLException {
/* 183 */     DriverManager.setLogWriter(out);
/*     */   }
/*     */   public int getLoginTimeout() throws SQLException {
/* 186 */     return DriverManager.getLoginTimeout();
/*     */   }
/*     */   public void setLoginTimeout(int seconds) throws SQLException {
/* 189 */     DriverManager.setLoginTimeout(seconds);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setJdbcUrl(String jdbcUrl) {
/* 196 */     super.setJdbcUrl(jdbcUrl);
/* 197 */     clearDriver();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setUser(String user) {
/* 203 */     String oldUser = getUser();
/* 204 */     if (!eqOrBothNull(user, oldUser)) {
/*     */       
/* 206 */       if (user != null) {
/* 207 */         this.properties.put("user", user);
/*     */       } else {
/* 209 */         this.properties.remove("user");
/*     */       } 
/* 211 */       this.pcs.firePropertyChange("user", oldUser, user);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized String getUser() {
/* 220 */     return this.properties.getProperty("user");
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void setPassword(String password) {
/* 225 */     String oldPass = getPassword();
/* 226 */     if (!eqOrBothNull(password, oldPass)) {
/*     */       
/* 228 */       if (password != null) {
/* 229 */         this.properties.put("password", password);
/*     */       } else {
/* 231 */         this.properties.remove("password");
/*     */       } 
/* 233 */       this.pcs.firePropertyChange("password", oldPass, password);
/*     */     } 
/*     */   }
/*     */   
/*     */   public synchronized String getPassword() {
/* 238 */     return this.properties.getProperty("password");
/*     */   }
/*     */   
/*     */   private final Properties overrideProps(String user, String password) {
/* 242 */     Properties overriding = (Properties)this.properties.clone();
/*     */     
/* 244 */     if (user != null) {
/* 245 */       overriding.put("user", user);
/*     */     } else {
/* 247 */       overriding.remove("user");
/*     */     } 
/* 249 */     if (password != null) {
/* 250 */       overriding.put("password", password);
/*     */     } else {
/* 252 */       overriding.remove("password");
/*     */     } 
/* 254 */     return overriding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private synchronized Driver driver() throws SQLException {
/* 265 */     if (this.driver == null)
/*     */     {
/* 267 */       if (this.driverClass != null && this.forceUseNamedDriverClass) {
/*     */         
/* 269 */         if (logger.isLoggable(MLevel.FINER)) {
/* 270 */           logger.finer("Circumventing DriverManager and instantiating driver class '" + this.driverClass + "' directly. (forceUseNamedDriverClass = " + this.forceUseNamedDriverClass + ")");
/*     */         }
/*     */         try {
/* 273 */           this.driver = (Driver)Class.forName(this.driverClass).newInstance();
/* 274 */         } catch (Exception e) {
/* 275 */           SqlUtils.toSQLException("Cannot instantiate specified JDBC driver. Exception while initializing named, forced-to-use driver class'" + this.driverClass + "'", e);
/*     */         } 
/*     */       } else {
/* 278 */         this.driver = DriverManager.getDriver(this.jdbcUrl);
/*     */       }  } 
/* 280 */     return this.driver;
/*     */   }
/*     */   
/*     */   private synchronized void clearDriver() {
/* 284 */     this.driver = null;
/*     */   }
/*     */   private static boolean eqOrBothNull(Object a, Object b) {
/* 287 */     return (a == b || (a != null && a.equals(b)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream oos) throws IOException {
/* 295 */     oos.writeShort(1);
/*     */   }
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
/* 300 */     short version = ois.readShort();
/* 301 */     switch (version) {
/*     */       
/*     */       case 1:
/* 304 */         setUpPropertyListeners();
/*     */         return;
/*     */     } 
/* 307 */     throw new IOException("Unsupported Serialized Version: " + version);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isWrapperFor(Class<?> iface) throws SQLException {
/* 314 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T unwrap(Class<T> iface) throws SQLException {
/* 319 */     throw new SQLException(this + " is not a Wrapper for " + iface.getName());
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/DriverManagerDataSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */