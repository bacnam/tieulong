/*     */ package com.mchange.v2.c3p0.util;
/*     */ 
/*     */ import com.mchange.v2.c3p0.C3P0ProxyConnection;
/*     */ import com.mchange.v2.sql.SqlUtils;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Random;
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
/*     */ public final class TestUtils
/*     */ {
/*     */   private static final Method OBJECT_EQUALS;
/*     */   private static final Method IDENTITY_HASHCODE;
/*     */   private static final Method IPCFP;
/*     */   
/*     */   static {
/*     */     try {
/*  56 */       OBJECT_EQUALS = Object.class.getMethod("equals", new Class[] { Object.class });
/*  57 */       IDENTITY_HASHCODE = System.class.getMethod("identityHashCode", new Class[] { Object.class });
/*     */ 
/*     */       
/*  60 */       IPCFP = TestUtils.class.getMethod("isPhysicalConnectionForProxy", new Class[] { Connection.class, C3P0ProxyConnection.class });
/*     */     }
/*  62 */     catch (Exception e) {
/*     */       
/*  64 */       e.printStackTrace();
/*  65 */       throw new RuntimeException("Huh? Can't reflectively get ahold of expected methods?");
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
/*     */   public static boolean samePhysicalConnection(C3P0ProxyConnection con1, C3P0ProxyConnection con2) throws SQLException {
/*     */     try {
/*  78 */       Object out = con1.rawConnectionOperation(IPCFP, null, new Object[] { C3P0ProxyConnection.RAW_CONNECTION, con2 });
/*  79 */       return ((Boolean)out).booleanValue();
/*     */     }
/*  81 */     catch (Exception e) {
/*     */       
/*  83 */       e.printStackTrace();
/*  84 */       throw SqlUtils.toSQLException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isPhysicalConnectionForProxy(Connection physicalConnection, C3P0ProxyConnection proxy) throws SQLException {
/*     */     try {
/*  92 */       Object out = proxy.rawConnectionOperation(OBJECT_EQUALS, physicalConnection, new Object[] { C3P0ProxyConnection.RAW_CONNECTION });
/*  93 */       return ((Boolean)out).booleanValue();
/*     */     }
/*  95 */     catch (Exception e) {
/*     */       
/*  97 */       e.printStackTrace();
/*  98 */       throw SqlUtils.toSQLException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static int physicalConnectionIdentityHashCode(C3P0ProxyConnection conn) throws SQLException {
/*     */     try {
/* 106 */       Object out = conn.rawConnectionOperation(IDENTITY_HASHCODE, null, new Object[] { C3P0ProxyConnection.RAW_CONNECTION });
/* 107 */       return ((Integer)out).intValue();
/*     */     }
/* 109 */     catch (Exception e) {
/*     */       
/* 111 */       e.printStackTrace();
/* 112 */       throw SqlUtils.toSQLException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static DataSource unreliableCommitDataSource(DataSource ds) throws Exception {
/* 119 */     return (DataSource)Proxy.newProxyInstance(TestUtils.class.getClassLoader(), new Class[] { DataSource.class }, new StupidDataSourceInvocationHandler(ds));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static class StupidDataSourceInvocationHandler
/*     */     implements InvocationHandler
/*     */   {
/*     */     DataSource ds;
/*     */ 
/*     */     
/* 130 */     Random r = new Random();
/*     */     
/*     */     StupidDataSourceInvocationHandler(DataSource ds) {
/* 133 */       this.ds = ds;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
/* 138 */       if ("getConnection".equals(method.getName())) {
/*     */         
/* 140 */         Connection conn = (Connection)method.invoke(this.ds, args);
/* 141 */         return Proxy.newProxyInstance(TestUtils.class.getClassLoader(), new Class[] { Connection.class }, new TestUtils.StupidConnectionInvocationHandler(conn));
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 146 */       return method.invoke(this.ds, args);
/*     */     }
/*     */   }
/*     */   
/*     */   static class StupidConnectionInvocationHandler
/*     */     implements InvocationHandler {
/*     */     Connection conn;
/* 153 */     Random r = new Random();
/*     */     
/*     */     boolean invalid = false;
/*     */     
/*     */     StupidConnectionInvocationHandler(Connection conn) {
/* 158 */       this.conn = conn;
/*     */     }
/*     */ 
/*     */     
/*     */     public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
/* 163 */       if ("close".equals(method.getName())) {
/*     */         
/* 165 */         if (this.invalid) {
/*     */           
/* 167 */           (new Exception("Duplicate close() called on Connection!!!")).printStackTrace();
/*     */         
/*     */         }
/*     */         else {
/*     */           
/* 172 */           this.invalid = true;
/*     */         } 
/* 174 */         return null;
/*     */       } 
/* 176 */       if (this.invalid)
/* 177 */         throw new SQLException("Connection closed -- cannot " + method.getName()); 
/* 178 */       if ("commit".equals(method.getName()) && this.r.nextInt(100) == 0) {
/*     */         
/* 180 */         this.conn.rollback();
/* 181 */         throw new SQLException("Random commit exception!!!");
/*     */       } 
/* 183 */       if (this.r.nextInt(200) == 0) {
/*     */         
/* 185 */         this.conn.rollback();
/* 186 */         this.conn.close();
/* 187 */         throw new SQLException("Random Fatal Exception Occurred!!!");
/*     */       } 
/*     */       
/* 190 */       return method.invoke(this.conn, args);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/util/TestUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */