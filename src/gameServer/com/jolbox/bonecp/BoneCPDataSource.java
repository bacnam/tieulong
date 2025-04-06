/*     */ package com.jolbox.bonecp;
/*     */ 
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.collect.MapMaker;
/*     */ import java.io.PrintWriter;
/*     */ import java.lang.reflect.Field;
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.concurrent.locks.ReadWriteLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.Name;
/*     */ import javax.naming.RefAddr;
/*     */ import javax.naming.Reference;
/*     */ import javax.naming.spi.ObjectFactory;
/*     */ import javax.sql.DataSource;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
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
/*     */ public class BoneCPDataSource
/*     */   extends BoneCPConfig
/*     */   implements DataSource, ObjectFactory
/*     */ {
/*     */   private static final long serialVersionUID = -1561804548443209469L;
/*  52 */   private PrintWriter logWriter = null;
/*     */   
/*  54 */   private volatile transient BoneCP pool = null;
/*     */   
/*  56 */   private ReadWriteLock rwl = new ReentrantReadWriteLock();
/*     */   
/*     */   private String driverClass;
/*     */   
/*  60 */   private static final Logger logger = LoggerFactory.getLogger(BoneCPDataSource.class);
/*     */ 
/*     */ 
/*     */   
/*  64 */   private Map<UsernamePassword, BoneCPDataSource> multiDataSource = (new MapMaker()).makeComputingMap(new Function<UsernamePassword, BoneCPDataSource>()
/*     */       {
/*     */         public BoneCPDataSource apply(UsernamePassword key)
/*     */         {
/*  68 */           BoneCPDataSource ds = null;
/*  69 */           ds = new BoneCPDataSource(BoneCPDataSource.this.getConfig());
/*     */           
/*  71 */           ds.setUsername(key.getUsername());
/*  72 */           ds.setPassword(key.getPassword());
/*     */           
/*  74 */           return ds;
/*     */         }
/*     */       });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BoneCPDataSource() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BoneCPDataSource(BoneCPConfig config) {
/*  93 */     Field[] fields = BoneCPConfig.class.getDeclaredFields();
/*  94 */     for (Field field : fields) {
/*     */       try {
/*  96 */         field.setAccessible(true);
/*  97 */         field.set(this, field.get(config));
/*  98 */       } catch (Exception e) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Connection getConnection() throws SQLException {
/* 111 */     if (this.pool == null) {
/* 112 */       maybeInit();
/*     */     }
/* 114 */     return this.pool.getConnection();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {
/* 122 */     if (this.pool != null) {
/* 123 */       this.pool.shutdown();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void maybeInit() throws SQLException {
/* 133 */     this.rwl.readLock().lock();
/* 134 */     if (this.pool == null) {
/* 135 */       this.rwl.readLock().unlock();
/* 136 */       this.rwl.writeLock().lock();
/* 137 */       if (this.pool == null) {
/*     */         try {
/* 139 */           if (getDriverClass() != null) {
/* 140 */             loadClass(getDriverClass());
/*     */           }
/*     */         }
/* 143 */         catch (ClassNotFoundException e) {
/* 144 */           throw new SQLException(PoolUtil.stringifyException(e));
/*     */         } 
/*     */ 
/*     */         
/* 148 */         logger.debug(toString());
/*     */         
/* 150 */         this.pool = new BoneCP(this);
/*     */       } 
/*     */       
/* 153 */       this.rwl.writeLock().unlock();
/*     */     } else {
/* 155 */       this.rwl.readLock().unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Connection getConnection(String username, String password) throws SQLException {
/* 167 */     return ((BoneCPDataSource)this.multiDataSource.get(new UsernamePassword(username, password))).getConnection();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PrintWriter getLogWriter() throws SQLException {
/* 175 */     return this.logWriter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getLoginTimeout() throws SQLException {
/* 185 */     throw new UnsupportedOperationException("getLoginTimeout is unsupported.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLogWriter(PrintWriter out) throws SQLException {
/* 193 */     this.logWriter = out;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLoginTimeout(int seconds) throws SQLException {
/* 203 */     throw new UnsupportedOperationException("setLoginTimeout is unsupported.");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isWrapperFor(Class<?> arg0) throws SQLException {
/* 214 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object unwrap(Class arg0) throws SQLException {
/* 226 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDriverClass() {
/* 236 */     return this.driverClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDriverClass(String driverClass) {
/* 246 */     this.driverClass = driverClass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTotalLeased() {
/* 256 */     return this.pool.getTotalLeased();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BoneCPConfig getConfig() {
/* 264 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getObjectInstance(Object object, Name name, Context context, Hashtable<?, ?> table) throws Exception {
/* 274 */     Reference ref = (Reference)object;
/* 275 */     Enumeration<RefAddr> addrs = ref.getAll();
/* 276 */     Properties props = new Properties();
/* 277 */     while (addrs.hasMoreElements()) {
/* 278 */       RefAddr addr = addrs.nextElement();
/* 279 */       if (addr.getType().equals("driverClassName")) {
/* 280 */         Class.forName((String)addr.getContent()); continue;
/*     */       } 
/* 282 */       props.put(addr.getType(), addr.getContent());
/*     */     } 
/*     */     
/* 285 */     BoneCPConfig config = new BoneCPConfig(props);
/*     */     
/* 287 */     return new BoneCPDataSource(config);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/jolbox/bonecp/BoneCPDataSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */