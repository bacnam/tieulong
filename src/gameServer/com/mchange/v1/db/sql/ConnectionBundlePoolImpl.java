/*     */ package com.mchange.v1.db.sql;
/*     */ 
/*     */ import com.mchange.v1.util.AbstractResourcePool;
/*     */ import com.mchange.v1.util.BrokenObjectException;
/*     */ import com.mchange.v1.util.UnexpectedException;
/*     */ import java.sql.Connection;
/*     */ import java.sql.DriverManager;
/*     */ import java.sql.SQLException;
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
/*     */ public abstract class ConnectionBundlePoolImpl
/*     */   extends AbstractResourcePool
/*     */   implements ConnectionBundlePool
/*     */ {
/*     */   String jdbcUrl;
/*     */   String username;
/*     */   String pwd;
/*     */   
/*     */   public ConnectionBundlePoolImpl(String paramString1, String paramString2, String paramString3, int paramInt1, int paramInt2, int paramInt3) throws SQLException {
/*  51 */     super(paramInt1, paramInt2, paramInt3);
/*  52 */     init(paramString1, paramString2, paramString3);
/*     */   }
/*     */   
/*     */   protected ConnectionBundlePoolImpl(int paramInt1, int paramInt2, int paramInt3) {
/*  56 */     super(paramInt1, paramInt2, paramInt3);
/*     */   }
/*     */   
/*     */   protected void init(String paramString1, String paramString2, String paramString3) throws SQLException {
/*  60 */     this.jdbcUrl = paramString1;
/*  61 */     this.username = paramString2;
/*  62 */     this.pwd = paramString3; try {
/*  63 */       init();
/*  64 */     } catch (SQLException sQLException) {
/*  65 */       throw sQLException;
/*  66 */     } catch (Exception exception) {
/*  67 */       throw new UnexpectedException(exception, "Unexpected exception while initializing ConnectionBundlePool");
/*     */     } 
/*     */   }
/*     */   
/*     */   public ConnectionBundle checkoutBundle() throws SQLException, BrokenObjectException, InterruptedException {
/*     */     try {
/*  73 */       return (ConnectionBundle)checkoutResource();
/*  74 */     } catch (BrokenObjectException brokenObjectException) {
/*  75 */       throw brokenObjectException;
/*  76 */     } catch (InterruptedException interruptedException) {
/*  77 */       throw interruptedException;
/*  78 */     } catch (SQLException sQLException) {
/*  79 */       throw sQLException;
/*  80 */     } catch (Exception exception) {
/*  81 */       throw new UnexpectedException(exception, "Unexpected exception while checking out ConnectionBundle");
/*     */     } 
/*     */   }
/*     */   public void checkinBundle(ConnectionBundle paramConnectionBundle) throws BrokenObjectException {
/*  85 */     checkinResource(paramConnectionBundle);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws SQLException {
/*     */     try {
/*  91 */       super.close();
/*  92 */     } catch (SQLException sQLException) {
/*  93 */       throw sQLException;
/*  94 */     } catch (Exception exception) {
/*  95 */       throw new UnexpectedException(exception, "Unexpected exception while closing pool.");
/*     */     } 
/*     */   }
/*     */   
/*     */   protected Object acquireResource() throws Exception {
/* 100 */     Connection connection = DriverManager.getConnection(this.jdbcUrl, this.username, this.pwd);
/* 101 */     setConnectionOptions(connection);
/* 102 */     return new ConnectionBundleImpl(connection);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void refurbishResource(Object paramObject) throws BrokenObjectException {
/*     */     boolean bool;
/*     */     try {
/* 110 */       Connection connection = ((ConnectionBundle)paramObject).getConnection();
/* 111 */       connection.rollback();
/* 112 */       bool = connection.isClosed();
/* 113 */       setConnectionOptions(connection);
/*     */     }
/* 115 */     catch (SQLException sQLException) {
/* 116 */       bool = true;
/* 117 */     }  if (bool) throw new BrokenObjectException(paramObject);
/*     */   
/*     */   }
/*     */   
/*     */   protected void destroyResource(Object paramObject) throws Exception {
/* 122 */     ((ConnectionBundle)paramObject).close();
/*     */   }
/*     */   
/*     */   protected abstract void setConnectionOptions(Connection paramConnection) throws SQLException;
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/db/sql/ConnectionBundlePoolImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */