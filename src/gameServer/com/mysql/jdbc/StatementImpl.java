/*      */ package com.mysql.jdbc;
/*      */ 
/*      */ import com.mysql.jdbc.exceptions.MySQLStatementCancelledException;
/*      */ import com.mysql.jdbc.exceptions.MySQLTimeoutException;
/*      */ import com.mysql.jdbc.log.LogUtils;
/*      */ import com.mysql.jdbc.profiler.ProfilerEvent;
/*      */ import com.mysql.jdbc.profiler.ProfilerEventHandler;
/*      */ import java.io.InputStream;
/*      */ import java.math.BigInteger;
/*      */ import java.sql.BatchUpdateException;
/*      */ import java.sql.Connection;
/*      */ import java.sql.DriverManager;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.SQLWarning;
/*      */ import java.sql.Statement;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Calendar;
/*      */ import java.util.Collections;
/*      */ import java.util.Enumeration;
/*      */ import java.util.GregorianCalendar;
/*      */ import java.util.HashSet;
/*      */ import java.util.List;
/*      */ import java.util.Properties;
/*      */ import java.util.Set;
/*      */ import java.util.TimerTask;
/*      */ import java.util.concurrent.atomic.AtomicBoolean;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class StatementImpl
/*      */   implements Statement
/*      */ {
/*      */   protected static final String PING_MARKER = "/* ping */";
/*      */   
/*      */   class CancelTask
/*      */     extends TimerTask
/*      */   {
/*   78 */     long connectionId = 0L;
/*   79 */     String origHost = "";
/*   80 */     SQLException caughtWhileCancelling = null;
/*      */     StatementImpl toCancel;
/*   82 */     Properties origConnProps = null;
/*   83 */     String origConnURL = "";
/*      */     
/*      */     CancelTask(StatementImpl cancellee) throws SQLException {
/*   86 */       this.connectionId = cancellee.connectionId;
/*   87 */       this.origHost = StatementImpl.this.connection.getHost();
/*   88 */       this.toCancel = cancellee;
/*   89 */       this.origConnProps = new Properties();
/*      */       
/*   91 */       Properties props = StatementImpl.this.connection.getProperties();
/*      */       
/*   93 */       Enumeration<?> keys = props.propertyNames();
/*      */       
/*   95 */       while (keys.hasMoreElements()) {
/*   96 */         String key = keys.nextElement().toString();
/*   97 */         this.origConnProps.setProperty(key, props.getProperty(key));
/*      */       } 
/*      */       
/*  100 */       this.origConnURL = StatementImpl.this.connection.getURL();
/*      */     }
/*      */ 
/*      */     
/*      */     public void run() {
/*  105 */       Thread cancelThread = new Thread()
/*      */         {
/*      */           public void run() {
/*  108 */             if (StatementImpl.this.connection.getQueryTimeoutKillsConnection()) {
/*      */               try {
/*  110 */                 StatementImpl.CancelTask.this.toCancel.wasCancelled = true;
/*  111 */                 StatementImpl.CancelTask.this.toCancel.wasCancelledByTimeout = true;
/*  112 */                 StatementImpl.this.connection.realClose(false, false, true, (Throwable)new MySQLStatementCancelledException(Messages.getString("Statement.ConnectionKilledDueToTimeout")));
/*      */               }
/*  114 */               catch (NullPointerException npe) {
/*      */               
/*  116 */               } catch (SQLException sqlEx) {
/*  117 */                 StatementImpl.CancelTask.this.caughtWhileCancelling = sqlEx;
/*      */               } 
/*      */             } else {
/*  120 */               Connection cancelConn = null;
/*  121 */               Statement cancelStmt = null;
/*      */               
/*      */               try {
/*  124 */                 synchronized (StatementImpl.this.cancelTimeoutMutex) {
/*  125 */                   if (StatementImpl.CancelTask.this.origConnURL.equals(StatementImpl.this.connection.getURL())) {
/*      */                     
/*  127 */                     cancelConn = StatementImpl.this.connection.duplicate();
/*  128 */                     cancelStmt = cancelConn.createStatement();
/*  129 */                     cancelStmt.execute("KILL QUERY " + StatementImpl.CancelTask.this.connectionId);
/*      */                   } else {
/*      */                     try {
/*  132 */                       cancelConn = (Connection)DriverManager.getConnection(StatementImpl.CancelTask.this.origConnURL, StatementImpl.CancelTask.this.origConnProps);
/*  133 */                       cancelStmt = cancelConn.createStatement();
/*  134 */                       cancelStmt.execute("KILL QUERY " + StatementImpl.CancelTask.this.connectionId);
/*  135 */                     } catch (NullPointerException npe) {}
/*      */                   } 
/*      */ 
/*      */                   
/*  139 */                   StatementImpl.CancelTask.this.toCancel.wasCancelled = true;
/*  140 */                   StatementImpl.CancelTask.this.toCancel.wasCancelledByTimeout = true;
/*      */                 } 
/*  142 */               } catch (SQLException sqlEx) {
/*  143 */                 StatementImpl.CancelTask.this.caughtWhileCancelling = sqlEx;
/*  144 */               } catch (NullPointerException npe) {
/*      */ 
/*      */ 
/*      */ 
/*      */               
/*      */               }
/*      */               finally {
/*      */                 
/*  152 */                 if (cancelStmt != null) {
/*      */                   try {
/*  154 */                     cancelStmt.close();
/*  155 */                   } catch (SQLException sqlEx) {
/*  156 */                     throw new RuntimeException(sqlEx.toString());
/*      */                   } 
/*      */                 }
/*      */                 
/*  160 */                 if (cancelConn != null) {
/*      */                   try {
/*  162 */                     cancelConn.close();
/*  163 */                   } catch (SQLException sqlEx) {
/*  164 */                     throw new RuntimeException(sqlEx.toString());
/*      */                   } 
/*      */                 }
/*      */                 
/*  168 */                 StatementImpl.CancelTask.this.toCancel = null;
/*  169 */                 StatementImpl.CancelTask.this.origConnProps = null;
/*  170 */                 StatementImpl.CancelTask.this.origConnURL = null;
/*      */               } 
/*      */             } 
/*      */           }
/*      */         };
/*      */       
/*  176 */       cancelThread.start();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  183 */   protected Object cancelTimeoutMutex = new Object();
/*      */ 
/*      */   
/*  186 */   static int statementCounter = 1;
/*      */ 
/*      */   
/*      */   public static final byte USES_VARIABLES_FALSE = 0;
/*      */   
/*      */   public static final byte USES_VARIABLES_TRUE = 1;
/*      */   
/*      */   public static final byte USES_VARIABLES_UNKNOWN = -1;
/*      */   
/*      */   protected boolean wasCancelled = false;
/*      */   
/*      */   protected boolean wasCancelledByTimeout = false;
/*      */   
/*      */   protected List<Object> batchedArgs;
/*      */   
/*  201 */   protected SingleByteCharsetConverter charConverter = null;
/*      */ 
/*      */   
/*  204 */   protected String charEncoding = null;
/*      */ 
/*      */   
/*  207 */   protected volatile MySQLConnection connection = null;
/*      */   
/*  209 */   protected long connectionId = 0L;
/*      */ 
/*      */   
/*  212 */   protected String currentCatalog = null;
/*      */ 
/*      */   
/*      */   protected boolean doEscapeProcessing = true;
/*      */ 
/*      */   
/*  218 */   protected ProfilerEventHandler eventSink = null;
/*      */ 
/*      */   
/*  221 */   private int fetchSize = 0;
/*      */ 
/*      */   
/*      */   protected boolean isClosed = false;
/*      */ 
/*      */   
/*  227 */   protected long lastInsertId = -1L;
/*      */ 
/*      */   
/*  230 */   protected int maxFieldSize = MysqlIO.getMaxBuf();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  236 */   protected int maxRows = -1;
/*      */ 
/*      */   
/*      */   protected boolean maxRowsChanged = false;
/*      */ 
/*      */   
/*  242 */   protected Set<ResultSetInternalMethods> openResults = new HashSet<ResultSetInternalMethods>();
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean pedantic = false;
/*      */ 
/*      */ 
/*      */   
/*      */   protected String pointOfOrigin;
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean profileSQL = false;
/*      */ 
/*      */   
/*  257 */   protected ResultSetInternalMethods results = null;
/*      */   
/*  259 */   protected ResultSetInternalMethods generatedKeysResults = null;
/*      */ 
/*      */   
/*  262 */   protected int resultSetConcurrency = 0;
/*      */ 
/*      */   
/*  265 */   protected int resultSetType = 0;
/*      */ 
/*      */   
/*      */   protected int statementId;
/*      */ 
/*      */   
/*  271 */   protected int timeoutInMillis = 0;
/*      */ 
/*      */   
/*  274 */   protected long updateCount = -1L;
/*      */ 
/*      */   
/*      */   protected boolean useUsageAdvisor = false;
/*      */ 
/*      */   
/*  280 */   protected SQLWarning warningChain = null;
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean clearWarningsCalled = false;
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean holdResultsOpenOverClose = false;
/*      */ 
/*      */   
/*  291 */   protected ArrayList<ResultSetRow> batchedGeneratedKeys = null;
/*      */   
/*      */   protected boolean retrieveGeneratedKeys = false;
/*      */   
/*      */   protected boolean continueBatchOnError = false;
/*      */   
/*  297 */   protected PingTarget pingTarget = null;
/*      */ 
/*      */   
/*      */   protected boolean useLegacyDatetimeCode;
/*      */ 
/*      */   
/*      */   private ExceptionInterceptor exceptionInterceptor;
/*      */   
/*      */   protected boolean lastQueryIsOnDupKeyUpdate = false;
/*      */   
/*  307 */   protected final AtomicBoolean statementExecuting = new AtomicBoolean(false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int originalResultSetType;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int originalFetchSize;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isPoolable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private InputStream localInfileInputStream;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected final boolean version5013OrNewer;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean closeOnCompletion;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addBatch(String sql) throws SQLException {
/*  398 */     synchronized (checkClosed()) {
/*  399 */       if (this.batchedArgs == null) {
/*  400 */         this.batchedArgs = new ArrayList();
/*      */       }
/*      */       
/*  403 */       if (sql != null) {
/*  404 */         this.batchedArgs.add(sql);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Object> getBatchedArgs() {
/*  416 */     return (this.batchedArgs == null) ? null : Collections.<Object>unmodifiableList(this.batchedArgs);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void cancel() throws SQLException {
/*  425 */     if (!this.statementExecuting.get()) {
/*      */       return;
/*      */     }
/*      */     
/*  429 */     if (!this.isClosed && this.connection != null && this.connection.versionMeetsMinimum(5, 0, 0)) {
/*      */ 
/*      */       
/*  432 */       Connection cancelConn = null;
/*  433 */       Statement cancelStmt = null;
/*      */       
/*      */       try {
/*  436 */         cancelConn = this.connection.duplicate();
/*  437 */         cancelStmt = cancelConn.createStatement();
/*  438 */         cancelStmt.execute("KILL QUERY " + this.connection.getIO().getThreadId());
/*      */         
/*  440 */         this.wasCancelled = true;
/*      */       } finally {
/*  442 */         if (cancelStmt != null) {
/*  443 */           cancelStmt.close();
/*      */         }
/*      */         
/*  446 */         if (cancelConn != null) {
/*  447 */           cancelConn.close();
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected MySQLConnection checkClosed() throws SQLException {
/*  463 */     MySQLConnection c = this.connection;
/*      */     
/*  465 */     if (c == null) {
/*  466 */       throw SQLError.createSQLException(Messages.getString("Statement.49"), "08003", getExceptionInterceptor());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  471 */     return c;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void checkForDml(String sql, char firstStatementChar) throws SQLException {
/*  488 */     if (firstStatementChar == 'I' || firstStatementChar == 'U' || firstStatementChar == 'D' || firstStatementChar == 'A' || firstStatementChar == 'C' || firstStatementChar == 'T' || firstStatementChar == 'R') {
/*      */ 
/*      */ 
/*      */       
/*  492 */       String noCommentSql = StringUtils.stripComments(sql, "'\"", "'\"", true, false, true, true);
/*      */ 
/*      */       
/*  495 */       if (StringUtils.startsWithIgnoreCaseAndWs(noCommentSql, "INSERT") || StringUtils.startsWithIgnoreCaseAndWs(noCommentSql, "UPDATE") || StringUtils.startsWithIgnoreCaseAndWs(noCommentSql, "DELETE") || StringUtils.startsWithIgnoreCaseAndWs(noCommentSql, "DROP") || StringUtils.startsWithIgnoreCaseAndWs(noCommentSql, "CREATE") || StringUtils.startsWithIgnoreCaseAndWs(noCommentSql, "ALTER") || StringUtils.startsWithIgnoreCaseAndWs(noCommentSql, "TRUNCATE") || StringUtils.startsWithIgnoreCaseAndWs(noCommentSql, "RENAME"))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  504 */         throw SQLError.createSQLException(Messages.getString("Statement.57"), "S1009", getExceptionInterceptor());
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void checkNullOrEmptyQuery(String sql) throws SQLException {
/*  521 */     if (sql == null) {
/*  522 */       throw SQLError.createSQLException(Messages.getString("Statement.59"), "S1009", getExceptionInterceptor());
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  527 */     if (sql.length() == 0) {
/*  528 */       throw SQLError.createSQLException(Messages.getString("Statement.61"), "S1009", getExceptionInterceptor());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void clearBatch() throws SQLException {
/*  543 */     synchronized (checkClosed()) {
/*  544 */       if (this.batchedArgs != null) {
/*  545 */         this.batchedArgs.clear();
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void clearWarnings() throws SQLException {
/*  558 */     synchronized (checkClosed()) {
/*  559 */       this.clearWarningsCalled = true;
/*  560 */       this.warningChain = null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void close() throws SQLException {
/*      */     try {
/*  581 */       synchronized (checkClosed()) {
/*  582 */         realClose(true, true);
/*      */       } 
/*  584 */     } catch (SQLException sqlEx) {
/*  585 */       if ("08003".equals(sqlEx.getSQLState())) {
/*      */         return;
/*      */       }
/*      */       
/*  589 */       throw sqlEx;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void closeAllOpenResults() throws SQLException {
/*  597 */     synchronized (checkClosed()) {
/*  598 */       if (this.openResults != null) {
/*  599 */         for (ResultSetInternalMethods element : this.openResults) {
/*      */           try {
/*  601 */             element.realClose(false);
/*  602 */           } catch (SQLException sqlEx) {
/*  603 */             AssertionFailedException.shouldNotHappen(sqlEx);
/*      */           } 
/*      */         } 
/*      */         
/*  607 */         this.openResults.clear();
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void removeOpenResultSet(ResultSet rs) {
/*      */     try {
/*  614 */       synchronized (checkClosed()) {
/*  615 */         if (this.openResults != null) {
/*  616 */           this.openResults.remove(rs);
/*      */         }
/*      */       } 
/*  619 */     } catch (SQLException e) {}
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public int getOpenResultSetCount() {
/*      */     try {
/*  626 */       synchronized (checkClosed()) {
/*  627 */         if (this.openResults != null) {
/*  628 */           return this.openResults.size();
/*      */         }
/*      */         
/*  631 */         return 0;
/*      */       } 
/*  633 */     } catch (SQLException e) {
/*      */ 
/*      */       
/*  636 */       return 0;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ResultSetInternalMethods createResultSetUsingServerFetch(String sql) throws SQLException {
/*  646 */     synchronized (checkClosed()) {
/*  647 */       PreparedStatement pStmt = this.connection.prepareStatement(sql, this.resultSetType, this.resultSetConcurrency);
/*      */ 
/*      */       
/*  650 */       pStmt.setFetchSize(this.fetchSize);
/*      */       
/*  652 */       if (this.maxRows > -1) {
/*  653 */         pStmt.setMaxRows(this.maxRows);
/*      */       }
/*      */       
/*  656 */       statementBegins();
/*      */       
/*  658 */       pStmt.execute();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  664 */       ResultSetInternalMethods rs = ((StatementImpl)pStmt).getResultSetInternal();
/*      */ 
/*      */       
/*  667 */       rs.setStatementUsedForFetchingRows((PreparedStatement)pStmt);
/*      */ 
/*      */       
/*  670 */       this.results = rs;
/*      */       
/*  672 */       return rs;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean createStreamingResultSet() {
/*      */     try {
/*  685 */       synchronized (checkClosed()) {
/*  686 */         return (this.resultSetType == 1003 && this.resultSetConcurrency == 1007 && this.fetchSize == Integer.MIN_VALUE);
/*      */       }
/*      */     
/*  689 */     } catch (SQLException e) {
/*      */ 
/*      */       
/*  692 */       return false;
/*      */     } 
/*      */   }
/*      */   
/*  696 */   public StatementImpl(MySQLConnection c, String catalog) throws SQLException { this.originalResultSetType = 0;
/*  697 */     this.originalFetchSize = 0;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2932 */     this.isPoolable = true; if (c == null || c.isClosed()) throw SQLError.createSQLException(Messages.getString("Statement.0"), "08003", null);  this.connection = c; this.connectionId = this.connection.getId(); this.exceptionInterceptor = this.connection.getExceptionInterceptor(); this.currentCatalog = catalog; this.pedantic = this.connection.getPedantic(); this.continueBatchOnError = this.connection.getContinueBatchOnError(); this.useLegacyDatetimeCode = this.connection.getUseLegacyDatetimeCode(); if (!this.connection.getDontTrackOpenResources()) this.connection.registerStatement(this);  if (this.connection != null) { this.maxFieldSize = this.connection.getMaxAllowedPacket(); int defaultFetchSize = this.connection.getDefaultFetchSize(); if (defaultFetchSize != 0) setFetchSize(defaultFetchSize);  if (this.connection.getUseUnicode()) { this.charEncoding = this.connection.getEncoding(); this.charConverter = this.connection.getCharsetConverter(this.charEncoding); }  boolean profiling = (this.connection.getProfileSql() || this.connection.getUseUsageAdvisor() || this.connection.getLogSlowQueries()); if (this.connection.getAutoGenerateTestcaseScript() || profiling) this.statementId = statementCounter++;  if (profiling) { this.pointOfOrigin = LogUtils.findCallingClassAndMethod(new Throwable()); this.profileSQL = this.connection.getProfileSql(); this.useUsageAdvisor = this.connection.getUseUsageAdvisor(); this.eventSink = ProfilerEventHandlerFactory.getInstance(this.connection); }  int maxRowsConn = this.connection.getMaxRows(); if (maxRowsConn != -1) setMaxRows(maxRowsConn);  this.holdResultsOpenOverClose = this.connection.getHoldResultsOpenOverStatementClose(); }  this.version5013OrNewer = this.connection.versionMeetsMinimum(5, 0, 13); }
/*      */   public void enableStreamingResults() throws SQLException { synchronized (checkClosed()) { this.originalResultSetType = this.resultSetType; this.originalFetchSize = this.fetchSize; setFetchSize(-2147483648); setResultSetType(1003); }  }
/*      */   public void disableStreamingResults() throws SQLException { synchronized (checkClosed()) { if (this.fetchSize == Integer.MIN_VALUE && this.resultSetType == 1003) { setFetchSize(this.originalFetchSize); setResultSetType(this.originalResultSetType); }  }  }
/* 2935 */   public boolean execute(String sql) throws SQLException { return execute(sql, false); } private boolean execute(String sql, boolean returnGeneratedKeys) throws SQLException { MySQLConnection locallyScopedConn = checkClosed(); synchronized (locallyScopedConn) { this.retrieveGeneratedKeys = returnGeneratedKeys; this.lastQueryIsOnDupKeyUpdate = false; if (returnGeneratedKeys) this.lastQueryIsOnDupKeyUpdate = containsOnDuplicateKeyInString(sql);  resetCancelledState(); checkNullOrEmptyQuery(sql); checkClosed(); char firstNonWsChar = StringUtils.firstAlphaCharUc(sql, findStartOfStatement(sql)); boolean isSelect = true; if (firstNonWsChar != 'S') { isSelect = false; if (locallyScopedConn.isReadOnly()) throw SQLError.createSQLException(Messages.getString("Statement.27") + Messages.getString("Statement.28"), "S1009", getExceptionInterceptor());  }  boolean doStreaming = createStreamingResultSet(); try { if (doStreaming && locallyScopedConn.getNetTimeoutForStreamingResults() > 0) executeSimpleNonQuery(locallyScopedConn, "SET net_write_timeout=" + locallyScopedConn.getNetTimeoutForStreamingResults());  if (this.doEscapeProcessing) { Object escapedSqlResult = EscapeProcessor.escapeSQL(sql, locallyScopedConn.serverSupportsConvertFn(), locallyScopedConn); if (escapedSqlResult instanceof String) { sql = (String)escapedSqlResult; } else { sql = ((EscapeProcessorResult)escapedSqlResult).escapedSql; }  }  if (!locallyScopedConn.getHoldResultsOpenOverStatementClose()) { if (this.results != null) this.results.realClose(false);  if (this.generatedKeysResults != null) this.generatedKeysResults.realClose(false);  closeAllOpenResults(); }  if (sql.charAt(0) == '/' && sql.startsWith("/* ping */")) { doPingInstead(); return true; }  CachedResultSetMetaData cachedMetaData = null; ResultSetInternalMethods rs = null; this.batchedGeneratedKeys = null; if (useServerFetch()) { rs = createResultSetUsingServerFetch(sql); } else { CancelTask timeoutTask = null; String oldCatalog = null; try { if (locallyScopedConn.getEnableQueryTimeouts() && this.timeoutInMillis != 0 && locallyScopedConn.versionMeetsMinimum(5, 0, 0)) { timeoutTask = new CancelTask(this); locallyScopedConn.getCancelTimer().schedule(timeoutTask, this.timeoutInMillis); }  if (!locallyScopedConn.getCatalog().equals(this.currentCatalog)) { oldCatalog = locallyScopedConn.getCatalog(); locallyScopedConn.setCatalog(this.currentCatalog); }  Field[] cachedFields = null; if (locallyScopedConn.getCacheResultSetMetadata()) { cachedMetaData = locallyScopedConn.getCachedMetaData(sql); if (cachedMetaData != null) cachedFields = cachedMetaData.fields;  }  if (locallyScopedConn.useMaxRows()) { int rowLimit = -1; if (isSelect) { if (StringUtils.indexOfIgnoreCase(sql, "LIMIT") != -1) { rowLimit = this.maxRows; } else if (this.maxRows <= 0) { executeSimpleNonQuery(locallyScopedConn, "SET SQL_SELECT_LIMIT=DEFAULT"); } else { executeSimpleNonQuery(locallyScopedConn, "SET SQL_SELECT_LIMIT=" + this.maxRows); }  } else { executeSimpleNonQuery(locallyScopedConn, "SET SQL_SELECT_LIMIT=DEFAULT"); }  statementBegins(); rs = locallyScopedConn.execSQL(this, sql, rowLimit, (Buffer)null, this.resultSetType, this.resultSetConcurrency, doStreaming, this.currentCatalog, cachedFields); } else { statementBegins(); rs = locallyScopedConn.execSQL(this, sql, -1, (Buffer)null, this.resultSetType, this.resultSetConcurrency, doStreaming, this.currentCatalog, cachedFields); }  if (timeoutTask != null) { if (timeoutTask.caughtWhileCancelling != null) throw timeoutTask.caughtWhileCancelling;  timeoutTask.cancel(); timeoutTask = null; }  synchronized (this.cancelTimeoutMutex) { if (this.wasCancelled) { MySQLStatementCancelledException mySQLStatementCancelledException; SQLException cause = null; if (this.wasCancelledByTimeout) { MySQLTimeoutException mySQLTimeoutException = new MySQLTimeoutException(); } else { mySQLStatementCancelledException = new MySQLStatementCancelledException(); }  resetCancelledState(); throw mySQLStatementCancelledException; }  }  } finally { if (timeoutTask != null) { timeoutTask.cancel(); locallyScopedConn.getCancelTimer().purge(); }  if (oldCatalog != null) locallyScopedConn.setCatalog(oldCatalog);  }  }  if (rs != null) { this.lastInsertId = rs.getUpdateID(); this.results = rs; rs.setFirstCharOfQuery(firstNonWsChar); if (rs.reallyResult()) if (cachedMetaData != null) { locallyScopedConn.initializeResultsMetadataFromCache(sql, cachedMetaData, this.results); } else if (this.connection.getCacheResultSetMetadata()) { locallyScopedConn.initializeResultsMetadataFromCache(sql, (CachedResultSetMetaData)null, this.results); }   }  return (rs != null && rs.reallyResult()); } finally { this.statementExecuting.set(false); }  }  } protected void statementBegins() { this.clearWarningsCalled = false; this.statementExecuting.set(true); } protected void resetCancelledState() throws SQLException { synchronized (checkClosed()) { if (this.cancelTimeoutMutex == null) return;  synchronized (this.cancelTimeoutMutex) { this.wasCancelled = false; this.wasCancelledByTimeout = false; }  }  } public boolean execute(String sql, int returnGeneratedKeys) throws SQLException { if (returnGeneratedKeys == 1) { checkClosed(); MySQLConnection locallyScopedConn = this.connection; synchronized (locallyScopedConn) { boolean readInfoMsgState = this.connection.isReadInfoMsgEnabled(); locallyScopedConn.setReadInfoMsgEnabled(true); try { return execute(sql, true); } finally { locallyScopedConn.setReadInfoMsgEnabled(readInfoMsgState); }  }  }  return execute(sql); } public boolean execute(String sql, int[] generatedKeyIndices) throws SQLException { MySQLConnection locallyScopedConn = checkClosed(); synchronized (locallyScopedConn) { if (generatedKeyIndices != null && generatedKeyIndices.length > 0) { this.retrieveGeneratedKeys = true; boolean readInfoMsgState = locallyScopedConn.isReadInfoMsgEnabled(); locallyScopedConn.setReadInfoMsgEnabled(true); try { return execute(sql, true); } finally { locallyScopedConn.setReadInfoMsgEnabled(readInfoMsgState); }  }  return execute(sql); }  } public boolean execute(String sql, String[] generatedKeyNames) throws SQLException { MySQLConnection locallyScopedConn = checkClosed(); synchronized (locallyScopedConn) { if (generatedKeyNames != null && generatedKeyNames.length > 0) { this.retrieveGeneratedKeys = true; boolean readInfoMsgState = this.connection.isReadInfoMsgEnabled(); locallyScopedConn.setReadInfoMsgEnabled(true); try { return execute(sql, true); } finally { locallyScopedConn.setReadInfoMsgEnabled(readInfoMsgState); }  }  return execute(sql); }  } public int[] executeBatch() throws SQLException { MySQLConnection locallyScopedConn = checkClosed(); synchronized (locallyScopedConn) { if (locallyScopedConn.isReadOnly()) throw SQLError.createSQLException(Messages.getString("Statement.34") + Messages.getString("Statement.35"), "S1009", getExceptionInterceptor());  if (!locallyScopedConn.getHoldResultsOpenOverStatementClose()) { if (this.results != null) this.results.realClose(false);  if (this.generatedKeysResults != null) this.generatedKeysResults.realClose(false);  closeAllOpenResults(); }  if (this.batchedArgs == null || this.batchedArgs.size() == 0) return new int[0];  int individualStatementTimeout = this.timeoutInMillis; this.timeoutInMillis = 0; CancelTask timeoutTask = null; try { resetCancelledState(); statementBegins(); try { this.retrieveGeneratedKeys = true; int[] updateCounts = null; if (this.batchedArgs != null) { int nbrCommands = this.batchedArgs.size(); this.batchedGeneratedKeys = new ArrayList<ResultSetRow>(this.batchedArgs.size()); boolean multiQueriesEnabled = locallyScopedConn.getAllowMultiQueries(); if (locallyScopedConn.versionMeetsMinimum(4, 1, 1) && (multiQueriesEnabled || (locallyScopedConn.getRewriteBatchedStatements() && nbrCommands > 4))) return executeBatchUsingMultiQueries(multiQueriesEnabled, nbrCommands, individualStatementTimeout);  if (locallyScopedConn.getEnableQueryTimeouts() && individualStatementTimeout != 0 && locallyScopedConn.versionMeetsMinimum(5, 0, 0)) { timeoutTask = new CancelTask(this); locallyScopedConn.getCancelTimer().schedule(timeoutTask, individualStatementTimeout); }  updateCounts = new int[nbrCommands]; for (int i = 0; i < nbrCommands; i++) updateCounts[i] = -3;  SQLException sqlEx = null; int commandIndex = 0; for (commandIndex = 0; commandIndex < nbrCommands; commandIndex++) { try { String sql = (String)this.batchedArgs.get(commandIndex); updateCounts[commandIndex] = executeUpdate(sql, true, true); getBatchedGeneratedKeys(containsOnDuplicateKeyInString(sql) ? 1 : 0); } catch (SQLException ex) { updateCounts[commandIndex] = -3; if (this.continueBatchOnError && !(ex instanceof MySQLTimeoutException) && !(ex instanceof MySQLStatementCancelledException) && !hasDeadlockOrTimeoutRolledBackTx(ex)) { sqlEx = ex; } else { int[] newUpdateCounts = new int[commandIndex]; if (hasDeadlockOrTimeoutRolledBackTx(ex)) { for (int j = 0; j < newUpdateCounts.length; j++) newUpdateCounts[j] = -3;  } else { System.arraycopy(updateCounts, 0, newUpdateCounts, 0, commandIndex); }  throw new BatchUpdateException(ex.getMessage(), ex.getSQLState(), ex.getErrorCode(), newUpdateCounts); }  }  }  if (sqlEx != null) throw new BatchUpdateException(sqlEx.getMessage(), sqlEx.getSQLState(), sqlEx.getErrorCode(), updateCounts);  }  if (timeoutTask != null) { if (timeoutTask.caughtWhileCancelling != null) throw timeoutTask.caughtWhileCancelling;  timeoutTask.cancel(); locallyScopedConn.getCancelTimer().purge(); timeoutTask = null; }  return (updateCounts != null) ? updateCounts : new int[0]; } finally { this.statementExecuting.set(false); }  } finally { if (timeoutTask != null) { timeoutTask.cancel(); locallyScopedConn.getCancelTimer().purge(); }  resetCancelledState(); this.timeoutInMillis = individualStatementTimeout; clearBatch(); }  }  } protected final boolean hasDeadlockOrTimeoutRolledBackTx(SQLException ex) { int vendorCode = ex.getErrorCode(); switch (vendorCode) { case 1206: case 1213: return true;case 1205: return !this.version5013OrNewer; }  return false; } private int[] executeBatchUsingMultiQueries(boolean multiQueriesEnabled, int nbrCommands, int individualStatementTimeout) throws SQLException { MySQLConnection locallyScopedConn = checkClosed(); synchronized (locallyScopedConn) { if (!multiQueriesEnabled) locallyScopedConn.getIO().enableMultiQueries();  Statement batchStmt = null; CancelTask timeoutTask = null; try { int[] updateCounts = new int[nbrCommands]; for (int i = 0; i < nbrCommands; i++) updateCounts[i] = -3;  int commandIndex = 0; StringBuffer queryBuf = new StringBuffer(); batchStmt = locallyScopedConn.createStatement(); if (locallyScopedConn.getEnableQueryTimeouts() && individualStatementTimeout != 0 && locallyScopedConn.versionMeetsMinimum(5, 0, 0)) { timeoutTask = new CancelTask((StatementImpl)batchStmt); locallyScopedConn.getCancelTimer().schedule(timeoutTask, individualStatementTimeout); }  int counter = 0; int numberOfBytesPerChar = 1; String connectionEncoding = locallyScopedConn.getEncoding(); if (StringUtils.startsWithIgnoreCase(connectionEncoding, "utf")) { numberOfBytesPerChar = 3; } else if (CharsetMapping.isMultibyteCharset(connectionEncoding)) { numberOfBytesPerChar = 2; }  int escapeAdjust = 1; batchStmt.setEscapeProcessing(this.doEscapeProcessing); if (this.doEscapeProcessing) escapeAdjust = 2;  SQLException sqlEx = null; int argumentSetsInBatchSoFar = 0; for (commandIndex = 0; commandIndex < nbrCommands; commandIndex++) { String nextQuery = (String)this.batchedArgs.get(commandIndex); if (((queryBuf.length() + nextQuery.length()) * numberOfBytesPerChar + 1 + 4) * escapeAdjust + 32 > this.connection.getMaxAllowedPacket()) { try { batchStmt.execute(queryBuf.toString(), 1); } catch (SQLException ex) { sqlEx = handleExceptionForBatch(commandIndex, argumentSetsInBatchSoFar, updateCounts, ex); }  counter = processMultiCountsAndKeys((StatementImpl)batchStmt, counter, updateCounts); queryBuf = new StringBuffer(); argumentSetsInBatchSoFar = 0; }  queryBuf.append(nextQuery); queryBuf.append(";"); argumentSetsInBatchSoFar++; }  if (queryBuf.length() > 0) { try { batchStmt.execute(queryBuf.toString(), 1); } catch (SQLException ex) { sqlEx = handleExceptionForBatch(commandIndex - 1, argumentSetsInBatchSoFar, updateCounts, ex); }  counter = processMultiCountsAndKeys((StatementImpl)batchStmt, counter, updateCounts); }  if (timeoutTask != null) { if (timeoutTask.caughtWhileCancelling != null) throw timeoutTask.caughtWhileCancelling;  timeoutTask.cancel(); locallyScopedConn.getCancelTimer().purge(); timeoutTask = null; }  if (sqlEx != null) throw new BatchUpdateException(sqlEx.getMessage(), sqlEx.getSQLState(), sqlEx.getErrorCode(), updateCounts);  return (updateCounts != null) ? updateCounts : new int[0]; } finally { if (timeoutTask != null) { timeoutTask.cancel(); locallyScopedConn.getCancelTimer().purge(); }  resetCancelledState(); try { if (batchStmt != null) batchStmt.close();  } finally { if (!multiQueriesEnabled) locallyScopedConn.getIO().disableMultiQueries();  }  }  }  } protected int processMultiCountsAndKeys(StatementImpl batchedStatement, int updateCountCounter, int[] updateCounts) throws SQLException { synchronized (checkClosed()) { updateCounts[updateCountCounter++] = batchedStatement.getUpdateCount(); boolean doGenKeys = (this.batchedGeneratedKeys != null); byte[][] row = (byte[][])null; if (doGenKeys) { long generatedKey = batchedStatement.getLastInsertID(); row = new byte[1][]; row[0] = StringUtils.getBytes(Long.toString(generatedKey)); this.batchedGeneratedKeys.add(new ByteArrayRow(row, getExceptionInterceptor())); }  while (batchedStatement.getMoreResults() || batchedStatement.getUpdateCount() != -1) { updateCounts[updateCountCounter++] = batchedStatement.getUpdateCount(); if (doGenKeys) { long generatedKey = batchedStatement.getLastInsertID(); row = new byte[1][]; row[0] = StringUtils.getBytes(Long.toString(generatedKey)); this.batchedGeneratedKeys.add(new ByteArrayRow(row, getExceptionInterceptor())); }  }  return updateCountCounter; }  } protected SQLException handleExceptionForBatch(int endOfBatchIndex, int numValuesPerBatch, int[] updateCounts, SQLException ex) throws BatchUpdateException { SQLException sqlEx; for (int j = endOfBatchIndex; j > endOfBatchIndex - numValuesPerBatch; j--) updateCounts[j] = -3;  if (this.continueBatchOnError && !(ex instanceof MySQLTimeoutException) && !(ex instanceof MySQLStatementCancelledException) && !hasDeadlockOrTimeoutRolledBackTx(ex)) { sqlEx = ex; } else { int[] newUpdateCounts = new int[endOfBatchIndex]; System.arraycopy(updateCounts, 0, newUpdateCounts, 0, endOfBatchIndex); BatchUpdateException batchException = new BatchUpdateException(ex.getMessage(), ex.getSQLState(), ex.getErrorCode(), newUpdateCounts); batchException.initCause(ex); throw batchException; }  return sqlEx; } public ResultSet executeQuery(String sql) throws SQLException { synchronized (checkClosed()) { MySQLConnection locallyScopedConn = this.connection; this.retrieveGeneratedKeys = false; resetCancelledState(); checkNullOrEmptyQuery(sql); boolean doStreaming = createStreamingResultSet(); if (doStreaming && this.connection.getNetTimeoutForStreamingResults() > 0) executeSimpleNonQuery(locallyScopedConn, "SET net_write_timeout=" + this.connection.getNetTimeoutForStreamingResults());  if (this.doEscapeProcessing) { Object escapedSqlResult = EscapeProcessor.escapeSQL(sql, locallyScopedConn.serverSupportsConvertFn(), this.connection); if (escapedSqlResult instanceof String) { sql = (String)escapedSqlResult; } else { sql = ((EscapeProcessorResult)escapedSqlResult).escapedSql; }  }  char firstStatementChar = StringUtils.firstNonWsCharUc(sql, findStartOfStatement(sql)); if (sql.charAt(0) == '/' && sql.startsWith("/* ping */")) { doPingInstead(); return this.results; }  checkForDml(sql, firstStatementChar); if (!locallyScopedConn.getHoldResultsOpenOverStatementClose()) { if (this.results != null) this.results.realClose(false);  if (this.generatedKeysResults != null) this.generatedKeysResults.realClose(false);  closeAllOpenResults(); }  CachedResultSetMetaData cachedMetaData = null; if (useServerFetch()) { this.results = createResultSetUsingServerFetch(sql); return this.results; }  CancelTask timeoutTask = null; String oldCatalog = null; try { if (locallyScopedConn.getEnableQueryTimeouts() && this.timeoutInMillis != 0 && locallyScopedConn.versionMeetsMinimum(5, 0, 0)) { timeoutTask = new CancelTask(this); locallyScopedConn.getCancelTimer().schedule(timeoutTask, this.timeoutInMillis); }  if (!locallyScopedConn.getCatalog().equals(this.currentCatalog)) { oldCatalog = locallyScopedConn.getCatalog(); locallyScopedConn.setCatalog(this.currentCatalog); }  Field[] cachedFields = null; if (locallyScopedConn.getCacheResultSetMetadata()) { cachedMetaData = locallyScopedConn.getCachedMetaData(sql); if (cachedMetaData != null) cachedFields = cachedMetaData.fields;  }  if (locallyScopedConn.useMaxRows()) { if (StringUtils.indexOfIgnoreCase(sql, "LIMIT") != -1) { this.results = locallyScopedConn.execSQL(this, sql, this.maxRows, (Buffer)null, this.resultSetType, this.resultSetConcurrency, doStreaming, this.currentCatalog, cachedFields); } else { if (this.maxRows <= 0) { executeSimpleNonQuery(locallyScopedConn, "SET SQL_SELECT_LIMIT=DEFAULT"); } else { executeSimpleNonQuery(locallyScopedConn, "SET SQL_SELECT_LIMIT=" + this.maxRows); }  statementBegins(); this.results = locallyScopedConn.execSQL(this, sql, -1, (Buffer)null, this.resultSetType, this.resultSetConcurrency, doStreaming, this.currentCatalog, cachedFields); if (oldCatalog != null) locallyScopedConn.setCatalog(oldCatalog);  }  } else { statementBegins(); this.results = locallyScopedConn.execSQL(this, sql, -1, (Buffer)null, this.resultSetType, this.resultSetConcurrency, doStreaming, this.currentCatalog, cachedFields); }  if (timeoutTask != null) { if (timeoutTask.caughtWhileCancelling != null) throw timeoutTask.caughtWhileCancelling;  timeoutTask.cancel(); locallyScopedConn.getCancelTimer().purge(); timeoutTask = null; }  synchronized (this.cancelTimeoutMutex) { if (this.wasCancelled) { MySQLStatementCancelledException mySQLStatementCancelledException; SQLException cause = null; if (this.wasCancelledByTimeout) { MySQLTimeoutException mySQLTimeoutException = new MySQLTimeoutException(); } else { mySQLStatementCancelledException = new MySQLStatementCancelledException(); }  resetCancelledState(); throw mySQLStatementCancelledException; }  }  } finally { this.statementExecuting.set(false); if (timeoutTask != null) { timeoutTask.cancel(); locallyScopedConn.getCancelTimer().purge(); }  if (oldCatalog != null) locallyScopedConn.setCatalog(oldCatalog);  }  this.lastInsertId = this.results.getUpdateID(); if (cachedMetaData != null) { locallyScopedConn.initializeResultsMetadataFromCache(sql, cachedMetaData, this.results); } else if (this.connection.getCacheResultSetMetadata()) { locallyScopedConn.initializeResultsMetadataFromCache(sql, (CachedResultSetMetaData)null, this.results); }  return this.results; }  } protected void doPingInstead() throws SQLException { synchronized (checkClosed()) { if (this.pingTarget != null) { this.pingTarget.doPing(); } else { this.connection.ping(); }  ResultSetInternalMethods fakeSelectOneResultSet = generatePingResultSet(); this.results = fakeSelectOneResultSet; }  } protected ResultSetInternalMethods generatePingResultSet() throws SQLException { synchronized (checkClosed()) { Field[] fields = { new Field(null, "1", -5, 1) }; ArrayList<ResultSetRow> rows = new ArrayList<ResultSetRow>(); byte[] colVal = { 49 }; rows.add(new ByteArrayRow(new byte[][] { colVal }, getExceptionInterceptor())); return (ResultSetInternalMethods)DatabaseMetaData.buildResultSet(fields, rows, this.connection); }  } protected void executeSimpleNonQuery(MySQLConnection c, String nonQuery) throws SQLException { c.execSQL(this, nonQuery, -1, (Buffer)null, 1003, 1007, false, this.currentCatalog, (Field[])null, false).close(); } public int executeUpdate(String sql) throws SQLException { return executeUpdate(sql, false, false); } protected int executeUpdate(String sql, boolean isBatch, boolean returnGeneratedKeys) throws SQLException { synchronized (checkClosed()) { MySQLConnection locallyScopedConn = this.connection; char firstStatementChar = StringUtils.firstAlphaCharUc(sql, findStartOfStatement(sql)); ResultSetInternalMethods rs = null; this.retrieveGeneratedKeys = returnGeneratedKeys; resetCancelledState(); checkNullOrEmptyQuery(sql); if (this.doEscapeProcessing) { Object escapedSqlResult = EscapeProcessor.escapeSQL(sql, this.connection.serverSupportsConvertFn(), this.connection); if (escapedSqlResult instanceof String) { sql = (String)escapedSqlResult; } else { sql = ((EscapeProcessorResult)escapedSqlResult).escapedSql; }  }  if (locallyScopedConn.isReadOnly(false)) throw SQLError.createSQLException(Messages.getString("Statement.42") + Messages.getString("Statement.43"), "S1009", getExceptionInterceptor());  if (StringUtils.startsWithIgnoreCaseAndWs(sql, "select")) throw SQLError.createSQLException(Messages.getString("Statement.46"), "01S03", getExceptionInterceptor());  if (!locallyScopedConn.getHoldResultsOpenOverStatementClose()) { if (this.results != null) this.results.realClose(false);  if (this.generatedKeysResults != null) this.generatedKeysResults.realClose(false);  closeAllOpenResults(); }  CancelTask timeoutTask = null; String oldCatalog = null; try { if (locallyScopedConn.getEnableQueryTimeouts() && this.timeoutInMillis != 0 && locallyScopedConn.versionMeetsMinimum(5, 0, 0)) { timeoutTask = new CancelTask(this); locallyScopedConn.getCancelTimer().schedule(timeoutTask, this.timeoutInMillis); }  if (!locallyScopedConn.getCatalog().equals(this.currentCatalog)) { oldCatalog = locallyScopedConn.getCatalog(); locallyScopedConn.setCatalog(this.currentCatalog); }  if (locallyScopedConn.useMaxRows()) executeSimpleNonQuery(locallyScopedConn, "SET SQL_SELECT_LIMIT=DEFAULT");  statementBegins(); rs = locallyScopedConn.execSQL(this, sql, -1, (Buffer)null, 1003, 1007, false, this.currentCatalog, (Field[])null, isBatch); if (timeoutTask != null) { if (timeoutTask.caughtWhileCancelling != null) throw timeoutTask.caughtWhileCancelling;  timeoutTask.cancel(); locallyScopedConn.getCancelTimer().purge(); timeoutTask = null; }  synchronized (this.cancelTimeoutMutex) { if (this.wasCancelled) { MySQLStatementCancelledException mySQLStatementCancelledException; SQLException cause = null; if (this.wasCancelledByTimeout) { MySQLTimeoutException mySQLTimeoutException = new MySQLTimeoutException(); } else { mySQLStatementCancelledException = new MySQLStatementCancelledException(); }  resetCancelledState(); throw mySQLStatementCancelledException; }  }  } finally { if (timeoutTask != null) { timeoutTask.cancel(); locallyScopedConn.getCancelTimer().purge(); }  if (oldCatalog != null) locallyScopedConn.setCatalog(oldCatalog);  if (!isBatch) this.statementExecuting.set(false);  }  this.results = rs; rs.setFirstCharOfQuery(firstStatementChar); this.updateCount = rs.getUpdateCount(); int truncatedUpdateCount = 0; if (this.updateCount > 2147483647L) { truncatedUpdateCount = Integer.MAX_VALUE; } else { truncatedUpdateCount = (int)this.updateCount; }  this.lastInsertId = rs.getUpdateID(); return truncatedUpdateCount; }  } public int executeUpdate(String sql, int returnGeneratedKeys) throws SQLException { synchronized (checkClosed()) { if (returnGeneratedKeys == 1) { MySQLConnection locallyScopedConn = this.connection; boolean readInfoMsgState = locallyScopedConn.isReadInfoMsgEnabled(); locallyScopedConn.setReadInfoMsgEnabled(true); try { return executeUpdate(sql, false, true); } finally { locallyScopedConn.setReadInfoMsgEnabled(readInfoMsgState); }  }  return executeUpdate(sql); }  } public int executeUpdate(String sql, int[] generatedKeyIndices) throws SQLException { synchronized (checkClosed()) { if (generatedKeyIndices != null && generatedKeyIndices.length > 0) { checkClosed(); MySQLConnection locallyScopedConn = this.connection; boolean readInfoMsgState = locallyScopedConn.isReadInfoMsgEnabled(); locallyScopedConn.setReadInfoMsgEnabled(true); try { return executeUpdate(sql, false, true); } finally { locallyScopedConn.setReadInfoMsgEnabled(readInfoMsgState); }  }  return executeUpdate(sql); }  } public int executeUpdate(String sql, String[] generatedKeyNames) throws SQLException { synchronized (checkClosed()) { if (generatedKeyNames != null && generatedKeyNames.length > 0) { MySQLConnection locallyScopedConn = this.connection; boolean readInfoMsgState = this.connection.isReadInfoMsgEnabled(); locallyScopedConn.setReadInfoMsgEnabled(true); try { return executeUpdate(sql, false, true); } finally { locallyScopedConn.setReadInfoMsgEnabled(readInfoMsgState); }  }  return executeUpdate(sql); }  } protected Calendar getCalendarInstanceForSessionOrNew() throws SQLException { synchronized (checkClosed()) { if (this.connection != null) return this.connection.getCalendarInstanceForSessionOrNew();  return new GregorianCalendar(); }  } public Connection getConnection() throws SQLException { synchronized (checkClosed()) { return this.connection; }  } public int getFetchDirection() throws SQLException { return 1000; } public int getFetchSize() throws SQLException { synchronized (checkClosed()) { return this.fetchSize; }  } public ResultSet getGeneratedKeys() throws SQLException { synchronized (checkClosed()) { if (!this.retrieveGeneratedKeys) throw SQLError.createSQLException(Messages.getString("Statement.GeneratedKeysNotRequested"), "S1009", getExceptionInterceptor());  if (this.batchedGeneratedKeys == null) { if (this.lastQueryIsOnDupKeyUpdate) return getGeneratedKeysInternal(1);  return getGeneratedKeysInternal(); }  Field[] fields = new Field[1]; fields[0] = new Field("", "GENERATED_KEY", -5, 17); fields[0].setConnection(this.connection); this.generatedKeysResults = ResultSetImpl.getInstance(this.currentCatalog, fields, new RowDataStatic(this.batchedGeneratedKeys), this.connection, this, false); return this.generatedKeysResults; }  } protected ResultSet getGeneratedKeysInternal() throws SQLException { int numKeys = getUpdateCount(); return getGeneratedKeysInternal(numKeys); } protected ResultSet getGeneratedKeysInternal(int numKeys) throws SQLException { synchronized (checkClosed()) { Field[] fields = new Field[1]; fields[0] = new Field("", "GENERATED_KEY", -5, 17); fields[0].setConnection(this.connection); fields[0].setUseOldNameMetadata(true); ArrayList<ResultSetRow> rowSet = new ArrayList<ResultSetRow>(); long beginAt = getLastInsertID(); if (beginAt < 0L) fields[0].setUnsigned();  if (this.results != null) { String serverInfo = this.results.getServerInfo(); if (numKeys > 0 && this.results.getFirstCharOfQuery() == 'R' && serverInfo != null && serverInfo.length() > 0) numKeys = getRecordCountFromInfo(serverInfo);  if (beginAt != 0L && numKeys > 0) for (int i = 0; i < numKeys; i++) { byte[][] row = new byte[1][]; if (beginAt > 0L) { row[0] = StringUtils.getBytes(Long.toString(beginAt)); } else { byte[] asBytes = new byte[8]; asBytes[7] = (byte)(int)(beginAt & 0xFFL); asBytes[6] = (byte)(int)(beginAt >>> 8L); asBytes[5] = (byte)(int)(beginAt >>> 16L); asBytes[4] = (byte)(int)(beginAt >>> 24L); asBytes[3] = (byte)(int)(beginAt >>> 32L); asBytes[2] = (byte)(int)(beginAt >>> 40L); asBytes[1] = (byte)(int)(beginAt >>> 48L); asBytes[0] = (byte)(int)(beginAt >>> 56L); BigInteger val = new BigInteger(1, asBytes); row[0] = val.toString().getBytes(); }  rowSet.add(new ByteArrayRow(row, getExceptionInterceptor())); beginAt += this.connection.getAutoIncrementIncrement(); }   }  ResultSetImpl gkRs = ResultSetImpl.getInstance(this.currentCatalog, fields, new RowDataStatic(rowSet), this.connection, this, false); this.openResults.add(gkRs); return gkRs; }  } public boolean isPoolable() throws SQLException { return this.isPoolable; }
/*      */   protected int getId() { return this.statementId; }
/*      */   public long getLastInsertID() { try { synchronized (checkClosed()) { return this.lastInsertId; }  } catch (SQLException e) { throw new RuntimeException(e); }  }
/*      */   public long getLongUpdateCount() { try { synchronized (checkClosed()) { if (this.results == null) return -1L;  if (this.results.reallyResult()) return -1L;  return this.updateCount; }  } catch (SQLException e) { throw new RuntimeException(e); }  }
/* 2939 */   public int getMaxFieldSize() throws SQLException { synchronized (checkClosed()) { return this.maxFieldSize; }  } public int getMaxRows() throws SQLException { synchronized (checkClosed()) { if (this.maxRows <= 0) return 0;  return this.maxRows; }  } public boolean getMoreResults() throws SQLException { return getMoreResults(1); } public boolean getMoreResults(int current) throws SQLException { synchronized (checkClosed()) { if (this.results == null) return false;  boolean streamingMode = createStreamingResultSet(); if (streamingMode && this.results.reallyResult()) while (this.results.next());  ResultSetInternalMethods nextResultSet = this.results.getNextResultSet(); switch (current) { case 1: if (this.results != null) { if (!streamingMode) this.results.close();  this.results.clearNextResult(); }  break;case 3: if (this.results != null) { if (!streamingMode) this.results.close();  this.results.clearNextResult(); }  closeAllOpenResults(); break;case 2: if (!this.connection.getDontTrackOpenResources()) this.openResults.add(this.results);  this.results.clearNextResult(); break;default: throw SQLError.createSQLException(Messages.getString("Statement.19"), "S1009", getExceptionInterceptor()); }  this.results = nextResultSet; if (this.results == null) { this.updateCount = -1L; this.lastInsertId = -1L; } else if (this.results.reallyResult()) { this.updateCount = -1L; this.lastInsertId = -1L; } else { this.updateCount = this.results.getUpdateCount(); this.lastInsertId = this.results.getUpdateID(); }  return (this.results != null && this.results.reallyResult()); }  } public int getQueryTimeout() throws SQLException { synchronized (checkClosed()) { return this.timeoutInMillis / 1000; }  } private int getRecordCountFromInfo(String serverInfo) { StringBuffer recordsBuf = new StringBuffer(); int recordsCount = 0; int duplicatesCount = 0; char c = Character.MIN_VALUE; int length = serverInfo.length(); int i = 0; for (; i < length; i++) { c = serverInfo.charAt(i); if (Character.isDigit(c)) break;  }  recordsBuf.append(c); i++; for (; i < length; i++) { c = serverInfo.charAt(i); if (!Character.isDigit(c)) break;  recordsBuf.append(c); }  recordsCount = Integer.parseInt(recordsBuf.toString()); StringBuffer duplicatesBuf = new StringBuffer(); for (; i < length; i++) { c = serverInfo.charAt(i); if (Character.isDigit(c)) break;  }  duplicatesBuf.append(c); i++; for (; i < length; i++) { c = serverInfo.charAt(i); if (!Character.isDigit(c)) break;  duplicatesBuf.append(c); }  duplicatesCount = Integer.parseInt(duplicatesBuf.toString()); return recordsCount - duplicatesCount; } public ResultSet getResultSet() throws SQLException { synchronized (checkClosed()) { return (this.results != null && this.results.reallyResult()) ? this.results : null; }  } public int getResultSetConcurrency() throws SQLException { synchronized (checkClosed()) { return this.resultSetConcurrency; }  } public int getResultSetHoldability() throws SQLException { return 1; } protected ResultSetInternalMethods getResultSetInternal() { try { synchronized (checkClosed()) { return this.results; }  } catch (SQLException e) { return this.results; }  } public int getResultSetType() throws SQLException { synchronized (checkClosed()) { return this.resultSetType; }  } public int getUpdateCount() throws SQLException { synchronized (checkClosed()) { if (this.results == null) return -1;  if (this.results.reallyResult()) return -1;  int truncatedUpdateCount = 0; if (this.results.getUpdateCount() > 2147483647L) { truncatedUpdateCount = Integer.MAX_VALUE; } else { truncatedUpdateCount = (int)this.results.getUpdateCount(); }  return truncatedUpdateCount; }  } public SQLWarning getWarnings() throws SQLException { synchronized (checkClosed()) { if (this.clearWarningsCalled) return null;  if (this.connection.versionMeetsMinimum(4, 1, 0)) { SQLWarning pendingWarningsFromServer = SQLError.convertShowWarningsToSQLWarnings(this.connection); if (this.warningChain != null) { this.warningChain.setNextWarning(pendingWarningsFromServer); } else { this.warningChain = pendingWarningsFromServer; }  return this.warningChain; }  return this.warningChain; }  } protected void realClose(boolean calledExplicitly, boolean closeOpenResults) throws SQLException { MySQLConnection locallyScopedConn; try { locallyScopedConn = checkClosed(); } catch (SQLException sqlEx) { return; }  synchronized (locallyScopedConn) { if (this.useUsageAdvisor && !calledExplicitly) { String message = Messages.getString("Statement.63") + Messages.getString("Statement.64"); this.eventSink.consumeEvent(new ProfilerEvent((byte)0, "", this.currentCatalog, this.connectionId, getId(), -1, System.currentTimeMillis(), 0L, Constants.MILLIS_I18N, null, this.pointOfOrigin, message)); }  if (closeOpenResults) closeOpenResults = !this.holdResultsOpenOverClose;  if (closeOpenResults) { if (this.results != null) try { this.results.close(); } catch (Exception ex) {}  if (this.generatedKeysResults != null) try { this.generatedKeysResults.close(); } catch (Exception ex) {}  closeAllOpenResults(); }  if (this.connection != null) { if (this.maxRowsChanged) this.connection.unsetMaxRows(this);  if (!this.connection.getDontTrackOpenResources()) this.connection.unregisterStatement(this);  }  this.isClosed = true; this.results = null; this.generatedKeysResults = null; this.connection = null; this.warningChain = null; this.openResults = null; this.batchedGeneratedKeys = null; this.localInfileInputStream = null; this.pingTarget = null; }  } public void setCursorName(String name) throws SQLException {} public void setEscapeProcessing(boolean enable) throws SQLException { synchronized (checkClosed()) { this.doEscapeProcessing = enable; }  } public void setFetchDirection(int direction) throws SQLException { switch (direction) { case 1000: case 1001: case 1002: return; }  throw SQLError.createSQLException(Messages.getString("Statement.5"), "S1009", getExceptionInterceptor()); } public void setFetchSize(int rows) throws SQLException { synchronized (checkClosed()) { if ((rows < 0 && rows != Integer.MIN_VALUE) || (this.maxRows != 0 && this.maxRows != -1 && rows > getMaxRows())) throw SQLError.createSQLException(Messages.getString("Statement.7"), "S1009", getExceptionInterceptor());  this.fetchSize = rows; }  } public void setHoldResultsOpenOverClose(boolean holdResultsOpenOverClose) { try { synchronized (checkClosed()) { this.holdResultsOpenOverClose = holdResultsOpenOverClose; }  } catch (SQLException e) {} } public void setMaxFieldSize(int max) throws SQLException { synchronized (checkClosed()) { if (max < 0) throw SQLError.createSQLException(Messages.getString("Statement.11"), "S1009", getExceptionInterceptor());  int maxBuf = (this.connection != null) ? this.connection.getMaxAllowedPacket() : MysqlIO.getMaxBuf(); if (max > maxBuf) throw SQLError.createSQLException(Messages.getString("Statement.13", new Object[] { Long.valueOf(maxBuf) }), "S1009", getExceptionInterceptor());  this.maxFieldSize = max; }  } public void setMaxRows(int max) throws SQLException { synchronized (checkClosed()) { if (max > 50000000 || max < 0) throw SQLError.createSQLException(Messages.getString("Statement.15") + max + " > " + 50000000 + ".", "S1009", getExceptionInterceptor());  if (max == 0) max = -1;  this.maxRows = max; this.maxRowsChanged = true; if (this.maxRows == -1) { this.connection.unsetMaxRows(this); this.maxRowsChanged = false; } else { this.connection.maxRowsChanged(this); }  }  } public void setQueryTimeout(int seconds) throws SQLException { synchronized (checkClosed()) { if (seconds < 0) throw SQLError.createSQLException(Messages.getString("Statement.21"), "S1009", getExceptionInterceptor());  this.timeoutInMillis = seconds * 1000; }  } void setResultSetConcurrency(int concurrencyFlag) { try { synchronized (checkClosed()) { this.resultSetConcurrency = concurrencyFlag; }  } catch (SQLException e) {} } void setResultSetType(int typeFlag) { try { synchronized (checkClosed()) { this.resultSetType = typeFlag; }  } catch (SQLException e) {} } protected void getBatchedGeneratedKeys(Statement batchedStatement) throws SQLException { synchronized (checkClosed()) { if (this.retrieveGeneratedKeys) { ResultSet rs = null; try { rs = batchedStatement.getGeneratedKeys(); while (rs.next()) { this.batchedGeneratedKeys.add(new ByteArrayRow(new byte[][] { rs.getBytes(1) }, getExceptionInterceptor())); }  } finally { if (rs != null) rs.close();  }  }  }  } protected void getBatchedGeneratedKeys(int maxKeys) throws SQLException { synchronized (checkClosed()) { if (this.retrieveGeneratedKeys) { ResultSet rs = null; try { if (maxKeys == 0) { rs = getGeneratedKeysInternal(); } else { rs = getGeneratedKeysInternal(maxKeys); }  while (rs.next()) { this.batchedGeneratedKeys.add(new ByteArrayRow(new byte[][] { rs.getBytes(1) }, getExceptionInterceptor())); }  } finally { if (rs != null) rs.close();  }  }  }  } private boolean useServerFetch() throws SQLException { synchronized (checkClosed()) { return (this.connection.isCursorFetchEnabled() && this.fetchSize > 0 && this.resultSetConcurrency == 1007 && this.resultSetType == 1003); }  } public boolean isClosed() throws SQLException { try { synchronized (checkClosed()) { return this.isClosed; }  } catch (SQLException sqlEx) { if ("08003".equals(sqlEx.getSQLState())) return true;  throw sqlEx; }  } public void setPoolable(boolean poolable) throws SQLException { this.isPoolable = poolable; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isWrapperFor(Class<?> iface) throws SQLException {
/* 2958 */     checkClosed();
/*      */ 
/*      */ 
/*      */     
/* 2962 */     return iface.isInstance(this);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object unwrap(Class<?> iface) throws SQLException {
/*      */     try {
/* 2983 */       return Util.cast(iface, this);
/* 2984 */     } catch (ClassCastException cce) {
/* 2985 */       throw SQLError.createSQLException("Unable to unwrap to " + iface.toString(), "S1009", getExceptionInterceptor());
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected int findStartOfStatement(String sql) {
/* 2991 */     int statementStartPos = 0;
/*      */     
/* 2993 */     if (StringUtils.startsWithIgnoreCaseAndWs(sql, "/*")) {
/* 2994 */       statementStartPos = sql.indexOf("*/");
/*      */       
/* 2996 */       if (statementStartPos == -1) {
/* 2997 */         statementStartPos = 0;
/*      */       } else {
/* 2999 */         statementStartPos += 2;
/*      */       } 
/* 3001 */     } else if (StringUtils.startsWithIgnoreCaseAndWs(sql, "--") || StringUtils.startsWithIgnoreCaseAndWs(sql, "#")) {
/*      */       
/* 3003 */       statementStartPos = sql.indexOf('\n');
/*      */       
/* 3005 */       if (statementStartPos == -1) {
/* 3006 */         statementStartPos = sql.indexOf('\r');
/*      */         
/* 3008 */         if (statementStartPos == -1) {
/* 3009 */           statementStartPos = 0;
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 3014 */     return statementStartPos;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public InputStream getLocalInfileInputStream() {
/* 3022 */     return this.localInfileInputStream;
/*      */   }
/*      */   
/*      */   public void setLocalInfileInputStream(InputStream stream) {
/* 3026 */     this.localInfileInputStream = stream;
/*      */   }
/*      */   
/*      */   public void setPingTarget(PingTarget pingTarget) {
/* 3030 */     this.pingTarget = pingTarget;
/*      */   }
/*      */   
/*      */   public ExceptionInterceptor getExceptionInterceptor() {
/* 3034 */     return this.exceptionInterceptor;
/*      */   }
/*      */   
/*      */   protected boolean containsOnDuplicateKeyInString(String sql) {
/* 3038 */     return (getOnDuplicateKeyLocation(sql) != -1);
/*      */   }
/*      */   
/*      */   protected int getOnDuplicateKeyLocation(String sql) {
/* 3042 */     return StringUtils.indexOfIgnoreCaseRespectMarker(0, sql, "ON DUPLICATE KEY UPDATE ", "\"'`", "\"'`", !this.connection.isNoBackslashEscapesSet());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void closeOnCompletion() throws SQLException {
/* 3049 */     synchronized (checkClosed()) {
/* 3050 */       this.closeOnCompletion = true;
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean isCloseOnCompletion() throws SQLException {
/* 3055 */     synchronized (checkClosed()) {
/* 3056 */       return this.closeOnCompletion;
/*      */     } 
/*      */   }
/*      */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mysql/jdbc/StatementImpl.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */