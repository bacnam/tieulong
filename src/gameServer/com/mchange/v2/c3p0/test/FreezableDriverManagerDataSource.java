/*     */ package com.mchange.v2.c3p0.test;
/*     */ 
/*     */ import com.mchange.v2.c3p0.cfg.C3P0Config;
/*     */ import com.mchange.v2.c3p0.impl.DriverManagerDataSourceBase;
/*     */ import com.mchange.v2.log.MLevel;
/*     */ import com.mchange.v2.log.MLog;
/*     */ import com.mchange.v2.log.MLogger;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.io.File;
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
/*     */ public final class FreezableDriverManagerDataSource
/*     */   extends DriverManagerDataSourceBase
/*     */   implements DataSource
/*     */ {
/*  63 */   static final MLogger logger = MLog.getLogger(FreezableDriverManagerDataSource.class);
/*     */   
/*  65 */   static final File FREEZE_FILE = new File("/tmp/c3p0_freeze_file");
/*     */   
/*     */   Driver driver;
/*     */   
/*     */   boolean driver_class_loaded = false;
/*     */   private static final long serialVersionUID = 1L;
/*     */   private static final short VERSION = 1;
/*     */   
/*     */   public FreezableDriverManagerDataSource() {
/*  74 */     this(true);
/*     */   }
/*     */   
/*     */   public FreezableDriverManagerDataSource(boolean autoregister) {
/*  78 */     super(autoregister);
/*     */     
/*  80 */     setUpPropertyListeners();
/*     */     
/*  82 */     String user = C3P0Config.initializeStringPropertyVar("user", null);
/*  83 */     String password = C3P0Config.initializeStringPropertyVar("password", null);
/*     */     
/*  85 */     if (user != null) {
/*  86 */       setUser(user);
/*     */     }
/*  88 */     if (password != null) {
/*  89 */       setPassword(password);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void waitNoFreezeFile() throws SQLException {
/*     */     try {
/*  98 */       while (FREEZE_FILE.exists())
/*     */       {
/* 100 */         Thread.sleep(1000L);
/*     */       }
/*     */     }
/* 103 */     catch (InterruptedException e) {
/*     */       
/* 105 */       logger.log(MLevel.WARNING, "Frozen cxn acquire interrupted.", e);
/* 106 */       throw new SQLException(e.toString());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void setUpPropertyListeners() {
/* 112 */     PropertyChangeListener driverClassListener = new PropertyChangeListener()
/*     */       {
/*     */         public void propertyChange(PropertyChangeEvent evt)
/*     */         {
/* 116 */           Object val = evt.getNewValue();
/* 117 */           if ("driverClass".equals(evt.getPropertyName()))
/* 118 */             FreezableDriverManagerDataSource.this.setDriverClassLoaded(false); 
/*     */         }
/*     */       };
/* 121 */     addPropertyChangeListener(driverClassListener);
/*     */   }
/*     */   
/*     */   private synchronized boolean isDriverClassLoaded() {
/* 125 */     return this.driver_class_loaded;
/*     */   }
/*     */   private synchronized void setDriverClassLoaded(boolean dcl) {
/* 128 */     this.driver_class_loaded = dcl;
/*     */   }
/*     */ 
/*     */   
/*     */   private void ensureDriverLoaded() throws SQLException {
/*     */     try {
/* 134 */       if (!isDriverClassLoaded())
/*     */       {
/* 136 */         if (this.driverClass != null)
/* 137 */           Class.forName(this.driverClass); 
/* 138 */         setDriverClassLoaded(true);
/*     */       }
/*     */     
/* 141 */     } catch (ClassNotFoundException e) {
/*     */       
/* 143 */       if (logger.isLoggable(MLevel.WARNING)) {
/* 144 */         logger.log(MLevel.WARNING, "Could not load driverClass " + this.driverClass, e);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Connection getConnection() throws SQLException {
/* 154 */     ensureDriverLoaded();
/*     */     
/* 156 */     waitNoFreezeFile();
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
/*     */   
/*     */   public Connection getConnection(String username, String password) throws SQLException {
/* 171 */     ensureDriverLoaded();
/*     */     
/* 173 */     waitNoFreezeFile();
/*     */     
/* 175 */     Connection out = driver().connect(this.jdbcUrl, overrideProps(username, password));
/* 176 */     if (out == null) {
/* 177 */       throw new SQLException("Apparently, jdbc URL '" + this.jdbcUrl + "' is not valid for the underlying " + "driver [" + driver() + "].");
/*     */     }
/* 179 */     return out;
/*     */   }
/*     */   
/*     */   public PrintWriter getLogWriter() throws SQLException {
/* 183 */     return DriverManager.getLogWriter();
/*     */   }
/*     */   public void setLogWriter(PrintWriter out) throws SQLException {
/* 186 */     DriverManager.setLogWriter(out);
/*     */   }
/*     */   public int getLoginTimeout() throws SQLException {
/* 189 */     return DriverManager.getLoginTimeout();
/*     */   }
/*     */   public void setLoginTimeout(int seconds) throws SQLException {
/* 192 */     DriverManager.setLoginTimeout(seconds);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setJdbcUrl(String jdbcUrl) {
/* 199 */     super.setJdbcUrl(jdbcUrl);
/* 200 */     clearDriver();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setUser(String user) {
/* 206 */     String oldUser = getUser();
/* 207 */     if (!eqOrBothNull(user, oldUser)) {
/*     */       
/* 209 */       if (user != null) {
/* 210 */         this.properties.put("user", user);
/*     */       } else {
/* 212 */         this.properties.remove("user");
/*     */       } 
/* 214 */       this.pcs.firePropertyChange("user", oldUser, user);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized String getUser() {
/* 223 */     return this.properties.getProperty("user");
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void setPassword(String password) {
/* 228 */     String oldPass = getPassword();
/* 229 */     if (!eqOrBothNull(password, oldPass)) {
/*     */       
/* 231 */       if (password != null) {
/* 232 */         this.properties.put("password", password);
/*     */       } else {
/* 234 */         this.properties.remove("password");
/*     */       } 
/* 236 */       this.pcs.firePropertyChange("password", oldPass, password);
/*     */     } 
/*     */   }
/*     */   
/*     */   public synchronized String getPassword() {
/* 241 */     return this.properties.getProperty("password");
/*     */   }
/*     */   
/*     */   private final Properties overrideProps(String user, String password) {
/* 245 */     Properties overriding = (Properties)this.properties.clone();
/*     */     
/* 247 */     if (user != null) {
/* 248 */       overriding.put("user", user);
/*     */     } else {
/* 250 */       overriding.remove("user");
/*     */     } 
/* 252 */     if (password != null) {
/* 253 */       overriding.put("password", password);
/*     */     } else {
/* 255 */       overriding.remove("password");
/*     */     } 
/* 257 */     return overriding;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private synchronized Driver driver() throws SQLException {
/* 263 */     if (this.driver == null)
/* 264 */       this.driver = DriverManager.getDriver(this.jdbcUrl); 
/* 265 */     return this.driver;
/*     */   }
/*     */   
/*     */   private synchronized void clearDriver() {
/* 269 */     this.driver = null;
/*     */   }
/*     */   private static boolean eqOrBothNull(Object a, Object b) {
/* 272 */     return (a == b || (a != null && a.equals(b)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream oos) throws IOException {
/* 280 */     oos.writeShort(1);
/*     */   }
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
/* 285 */     short version = ois.readShort();
/* 286 */     switch (version) {
/*     */       
/*     */       case 1:
/* 289 */         setUpPropertyListeners();
/*     */         return;
/*     */     } 
/* 292 */     throw new IOException("Unsupported Serialized Version: " + version);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isWrapperFor(Class<?> iface) throws SQLException {
/* 300 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T unwrap(Class<T> iface) throws SQLException {
/* 305 */     throw new SQLException(this + " is not a Wrapper for " + iface.getName());
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/test/FreezableDriverManagerDataSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */