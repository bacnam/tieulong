/*      */ package com.mysql.jdbc;
/*      */ 
/*      */ import com.mysql.jdbc.exceptions.MySQLStatementCancelledException;
/*      */ import com.mysql.jdbc.exceptions.MySQLTimeoutException;
/*      */ import com.mysql.jdbc.profiler.ProfilerEvent;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Reader;
/*      */ import java.io.StringReader;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.math.BigDecimal;
/*      */ import java.math.BigInteger;
/*      */ import java.net.URL;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.CharBuffer;
/*      */ import java.nio.charset.Charset;
/*      */ import java.nio.charset.CharsetEncoder;
/*      */ import java.sql.Array;
/*      */ import java.sql.BatchUpdateException;
/*      */ import java.sql.Blob;
/*      */ import java.sql.Clob;
/*      */ import java.sql.DatabaseMetaData;
/*      */ import java.sql.Date;
/*      */ import java.sql.ParameterMetaData;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.Ref;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.ResultSetMetaData;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.Statement;
/*      */ import java.sql.Time;
/*      */ import java.sql.Timestamp;
/*      */ import java.text.DateFormat;
/*      */ import java.text.ParsePosition;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Calendar;
/*      */ import java.util.Date;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.TimeZone;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class PreparedStatement
/*      */   extends StatementImpl
/*      */   implements PreparedStatement
/*      */ {
/*      */   private static final Constructor<?> JDBC_4_PSTMT_2_ARG_CTOR;
/*      */   private static final Constructor<?> JDBC_4_PSTMT_3_ARG_CTOR;
/*      */   private static final Constructor<?> JDBC_4_PSTMT_4_ARG_CTOR;
/*      */   
/*      */   static {
/*   96 */     if (Util.isJdbc4()) {
/*      */       try {
/*   98 */         JDBC_4_PSTMT_2_ARG_CTOR = Class.forName("com.mysql.jdbc.JDBC4PreparedStatement").getConstructor(new Class[] { MySQLConnection.class, String.class });
/*      */ 
/*      */ 
/*      */         
/*  102 */         JDBC_4_PSTMT_3_ARG_CTOR = Class.forName("com.mysql.jdbc.JDBC4PreparedStatement").getConstructor(new Class[] { MySQLConnection.class, String.class, String.class });
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  107 */         JDBC_4_PSTMT_4_ARG_CTOR = Class.forName("com.mysql.jdbc.JDBC4PreparedStatement").getConstructor(new Class[] { MySQLConnection.class, String.class, String.class, ParseInfo.class });
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*  112 */       catch (SecurityException e) {
/*  113 */         throw new RuntimeException(e);
/*  114 */       } catch (NoSuchMethodException e) {
/*  115 */         throw new RuntimeException(e);
/*  116 */       } catch (ClassNotFoundException e) {
/*  117 */         throw new RuntimeException(e);
/*      */       } 
/*      */     } else {
/*  120 */       JDBC_4_PSTMT_2_ARG_CTOR = null;
/*  121 */       JDBC_4_PSTMT_3_ARG_CTOR = null;
/*  122 */       JDBC_4_PSTMT_4_ARG_CTOR = null;
/*      */     } 
/*      */   }
/*      */   
/*      */   public class BatchParams {
/*  127 */     public boolean[] isNull = null;
/*      */     
/*  129 */     public boolean[] isStream = null;
/*      */     
/*  131 */     public InputStream[] parameterStreams = null;
/*      */     
/*  133 */     public byte[][] parameterStrings = (byte[][])null;
/*      */     
/*  135 */     public int[] streamLengths = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     BatchParams(byte[][] strings, InputStream[] streams, boolean[] isStreamFlags, int[] lengths, boolean[] isNullFlags) {
/*  142 */       this.parameterStrings = new byte[strings.length][];
/*  143 */       this.parameterStreams = new InputStream[streams.length];
/*  144 */       this.isStream = new boolean[isStreamFlags.length];
/*  145 */       this.streamLengths = new int[lengths.length];
/*  146 */       this.isNull = new boolean[isNullFlags.length];
/*  147 */       System.arraycopy(strings, 0, this.parameterStrings, 0, strings.length);
/*      */       
/*  149 */       System.arraycopy(streams, 0, this.parameterStreams, 0, streams.length);
/*      */       
/*  151 */       System.arraycopy(isStreamFlags, 0, this.isStream, 0, isStreamFlags.length);
/*      */       
/*  153 */       System.arraycopy(lengths, 0, this.streamLengths, 0, lengths.length);
/*  154 */       System.arraycopy(isNullFlags, 0, this.isNull, 0, isNullFlags.length);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   class EndPoint
/*      */   {
/*      */     int begin;
/*      */     
/*      */     int end;
/*      */     
/*      */     EndPoint(int b, int e) {
/*  166 */       this.begin = b;
/*  167 */       this.end = e;
/*      */     }
/*      */   }
/*      */   
/*      */   class ParseInfo {
/*  172 */     char firstStmtChar = Character.MIN_VALUE;
/*      */     
/*      */     boolean foundLimitClause = false;
/*      */     
/*      */     boolean foundLoadData = false;
/*      */     
/*  178 */     long lastUsed = 0L;
/*      */     
/*  180 */     int statementLength = 0;
/*      */     
/*  182 */     int statementStartPos = 0;
/*      */     
/*      */     boolean canRewriteAsMultiValueInsert = false;
/*      */     
/*  186 */     byte[][] staticSql = (byte[][])null;
/*      */     
/*      */     boolean isOnDuplicateKeyUpdate = false;
/*      */     
/*  190 */     int locationOfOnDuplicateKeyUpdate = -1;
/*      */ 
/*      */     
/*      */     String valuesClause;
/*      */     
/*      */     boolean parametersInDuplicateKeyClause = false;
/*      */     
/*      */     private ParseInfo batchHead;
/*      */     
/*      */     private ParseInfo batchValues;
/*      */     
/*      */     private ParseInfo batchODKUClause;
/*      */ 
/*      */     
/*      */     ParseInfo(String sql, MySQLConnection conn, DatabaseMetaData dbmd, String encoding, SingleByteCharsetConverter converter) throws SQLException {
/*  205 */       this(sql, conn, dbmd, encoding, converter, true);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ParseInfo(String sql, MySQLConnection conn, DatabaseMetaData dbmd, String encoding, SingleByteCharsetConverter converter, boolean buildRewriteInfo) throws SQLException {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: putfield this$0 : Lcom/mysql/jdbc/PreparedStatement;
/*      */       //   5: aload_0
/*      */       //   6: invokespecial <init> : ()V
/*      */       //   9: aload_0
/*      */       //   10: iconst_0
/*      */       //   11: putfield firstStmtChar : C
/*      */       //   14: aload_0
/*      */       //   15: iconst_0
/*      */       //   16: putfield foundLimitClause : Z
/*      */       //   19: aload_0
/*      */       //   20: iconst_0
/*      */       //   21: putfield foundLoadData : Z
/*      */       //   24: aload_0
/*      */       //   25: lconst_0
/*      */       //   26: putfield lastUsed : J
/*      */       //   29: aload_0
/*      */       //   30: iconst_0
/*      */       //   31: putfield statementLength : I
/*      */       //   34: aload_0
/*      */       //   35: iconst_0
/*      */       //   36: putfield statementStartPos : I
/*      */       //   39: aload_0
/*      */       //   40: iconst_0
/*      */       //   41: putfield canRewriteAsMultiValueInsert : Z
/*      */       //   44: aload_0
/*      */       //   45: aconst_null
/*      */       //   46: checkcast [[B
/*      */       //   49: putfield staticSql : [[B
/*      */       //   52: aload_0
/*      */       //   53: iconst_0
/*      */       //   54: putfield isOnDuplicateKeyUpdate : Z
/*      */       //   57: aload_0
/*      */       //   58: iconst_m1
/*      */       //   59: putfield locationOfOnDuplicateKeyUpdate : I
/*      */       //   62: aload_0
/*      */       //   63: iconst_0
/*      */       //   64: putfield parametersInDuplicateKeyClause : Z
/*      */       //   67: aload_2
/*      */       //   68: ifnonnull -> 86
/*      */       //   71: ldc 'PreparedStatement.61'
/*      */       //   73: invokestatic getString : (Ljava/lang/String;)Ljava/lang/String;
/*      */       //   76: ldc 'S1009'
/*      */       //   78: aload_1
/*      */       //   79: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
/*      */       //   82: invokestatic createSQLException : (Ljava/lang/String;Ljava/lang/String;Lcom/mysql/jdbc/ExceptionInterceptor;)Ljava/sql/SQLException;
/*      */       //   85: athrow
/*      */       //   86: aload_0
/*      */       //   87: aload_1
/*      */       //   88: aload_2
/*      */       //   89: invokevirtual getOnDuplicateKeyLocation : (Ljava/lang/String;)I
/*      */       //   92: putfield locationOfOnDuplicateKeyUpdate : I
/*      */       //   95: aload_0
/*      */       //   96: aload_0
/*      */       //   97: getfield locationOfOnDuplicateKeyUpdate : I
/*      */       //   100: iconst_m1
/*      */       //   101: if_icmpeq -> 108
/*      */       //   104: iconst_1
/*      */       //   105: goto -> 109
/*      */       //   108: iconst_0
/*      */       //   109: putfield isOnDuplicateKeyUpdate : Z
/*      */       //   112: aload_0
/*      */       //   113: invokestatic currentTimeMillis : ()J
/*      */       //   116: putfield lastUsed : J
/*      */       //   119: aload #4
/*      */       //   121: invokeinterface getIdentifierQuoteString : ()Ljava/lang/String;
/*      */       //   126: astore #8
/*      */       //   128: iconst_0
/*      */       //   129: istore #9
/*      */       //   131: aload #8
/*      */       //   133: ifnull -> 162
/*      */       //   136: aload #8
/*      */       //   138: ldc ' '
/*      */       //   140: invokevirtual equals : (Ljava/lang/Object;)Z
/*      */       //   143: ifne -> 162
/*      */       //   146: aload #8
/*      */       //   148: invokevirtual length : ()I
/*      */       //   151: ifle -> 162
/*      */       //   154: aload #8
/*      */       //   156: iconst_0
/*      */       //   157: invokevirtual charAt : (I)C
/*      */       //   160: istore #9
/*      */       //   162: aload_0
/*      */       //   163: aload_2
/*      */       //   164: invokevirtual length : ()I
/*      */       //   167: putfield statementLength : I
/*      */       //   170: new java/util/ArrayList
/*      */       //   173: dup
/*      */       //   174: invokespecial <init> : ()V
/*      */       //   177: astore #10
/*      */       //   179: iconst_0
/*      */       //   180: istore #11
/*      */       //   182: iconst_0
/*      */       //   183: istore #12
/*      */       //   185: iconst_0
/*      */       //   186: istore #13
/*      */       //   188: iconst_0
/*      */       //   189: istore #14
/*      */       //   191: aload_0
/*      */       //   192: getfield statementLength : I
/*      */       //   195: iconst_5
/*      */       //   196: isub
/*      */       //   197: istore #16
/*      */       //   199: aload_0
/*      */       //   200: iconst_0
/*      */       //   201: putfield foundLimitClause : Z
/*      */       //   204: aload_1
/*      */       //   205: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
/*      */       //   208: invokeinterface isNoBackslashEscapesSet : ()Z
/*      */       //   213: istore #17
/*      */       //   215: aload_0
/*      */       //   216: aload_1
/*      */       //   217: aload_2
/*      */       //   218: invokevirtual findStartOfStatement : (Ljava/lang/String;)I
/*      */       //   221: putfield statementStartPos : I
/*      */       //   224: aload_0
/*      */       //   225: getfield statementStartPos : I
/*      */       //   228: istore #15
/*      */       //   230: iload #15
/*      */       //   232: aload_0
/*      */       //   233: getfield statementLength : I
/*      */       //   236: if_icmpge -> 951
/*      */       //   239: aload_2
/*      */       //   240: iload #15
/*      */       //   242: invokevirtual charAt : (I)C
/*      */       //   245: istore #18
/*      */       //   247: aload_0
/*      */       //   248: getfield firstStmtChar : C
/*      */       //   251: ifne -> 271
/*      */       //   254: iload #18
/*      */       //   256: invokestatic isLetter : (C)Z
/*      */       //   259: ifeq -> 271
/*      */       //   262: aload_0
/*      */       //   263: iload #18
/*      */       //   265: invokestatic toUpperCase : (C)C
/*      */       //   268: putfield firstStmtChar : C
/*      */       //   271: iload #17
/*      */       //   273: ifne -> 300
/*      */       //   276: iload #18
/*      */       //   278: bipush #92
/*      */       //   280: if_icmpne -> 300
/*      */       //   283: iload #15
/*      */       //   285: aload_0
/*      */       //   286: getfield statementLength : I
/*      */       //   289: iconst_1
/*      */       //   290: isub
/*      */       //   291: if_icmpge -> 300
/*      */       //   294: iinc #15, 1
/*      */       //   297: goto -> 945
/*      */       //   300: iload #11
/*      */       //   302: ifne -> 332
/*      */       //   305: iload #9
/*      */       //   307: ifeq -> 332
/*      */       //   310: iload #18
/*      */       //   312: iload #9
/*      */       //   314: if_icmpne -> 332
/*      */       //   317: iload #13
/*      */       //   319: ifne -> 326
/*      */       //   322: iconst_1
/*      */       //   323: goto -> 327
/*      */       //   326: iconst_0
/*      */       //   327: istore #13
/*      */       //   329: goto -> 680
/*      */       //   332: iload #13
/*      */       //   334: ifne -> 680
/*      */       //   337: iload #11
/*      */       //   339: ifeq -> 450
/*      */       //   342: iload #18
/*      */       //   344: bipush #39
/*      */       //   346: if_icmpeq -> 356
/*      */       //   349: iload #18
/*      */       //   351: bipush #34
/*      */       //   353: if_icmpne -> 411
/*      */       //   356: iload #18
/*      */       //   358: iload #12
/*      */       //   360: if_icmpne -> 411
/*      */       //   363: iload #15
/*      */       //   365: aload_0
/*      */       //   366: getfield statementLength : I
/*      */       //   369: iconst_1
/*      */       //   370: isub
/*      */       //   371: if_icmpge -> 393
/*      */       //   374: aload_2
/*      */       //   375: iload #15
/*      */       //   377: iconst_1
/*      */       //   378: iadd
/*      */       //   379: invokevirtual charAt : (I)C
/*      */       //   382: iload #12
/*      */       //   384: if_icmpne -> 393
/*      */       //   387: iinc #15, 1
/*      */       //   390: goto -> 945
/*      */       //   393: iload #11
/*      */       //   395: ifne -> 402
/*      */       //   398: iconst_1
/*      */       //   399: goto -> 403
/*      */       //   402: iconst_0
/*      */       //   403: istore #11
/*      */       //   405: iconst_0
/*      */       //   406: istore #12
/*      */       //   408: goto -> 680
/*      */       //   411: iload #18
/*      */       //   413: bipush #39
/*      */       //   415: if_icmpeq -> 425
/*      */       //   418: iload #18
/*      */       //   420: bipush #34
/*      */       //   422: if_icmpne -> 680
/*      */       //   425: iload #18
/*      */       //   427: iload #12
/*      */       //   429: if_icmpne -> 680
/*      */       //   432: iload #11
/*      */       //   434: ifne -> 441
/*      */       //   437: iconst_1
/*      */       //   438: goto -> 442
/*      */       //   441: iconst_0
/*      */       //   442: istore #11
/*      */       //   444: iconst_0
/*      */       //   445: istore #12
/*      */       //   447: goto -> 680
/*      */       //   450: iload #18
/*      */       //   452: bipush #35
/*      */       //   454: if_icmpeq -> 488
/*      */       //   457: iload #18
/*      */       //   459: bipush #45
/*      */       //   461: if_icmpne -> 534
/*      */       //   464: iload #15
/*      */       //   466: iconst_1
/*      */       //   467: iadd
/*      */       //   468: aload_0
/*      */       //   469: getfield statementLength : I
/*      */       //   472: if_icmpge -> 534
/*      */       //   475: aload_2
/*      */       //   476: iload #15
/*      */       //   478: iconst_1
/*      */       //   479: iadd
/*      */       //   480: invokevirtual charAt : (I)C
/*      */       //   483: bipush #45
/*      */       //   485: if_icmpne -> 534
/*      */       //   488: aload_0
/*      */       //   489: getfield statementLength : I
/*      */       //   492: iconst_1
/*      */       //   493: isub
/*      */       //   494: istore #19
/*      */       //   496: iload #15
/*      */       //   498: iload #19
/*      */       //   500: if_icmpge -> 945
/*      */       //   503: aload_2
/*      */       //   504: iload #15
/*      */       //   506: invokevirtual charAt : (I)C
/*      */       //   509: istore #18
/*      */       //   511: iload #18
/*      */       //   513: bipush #13
/*      */       //   515: if_icmpeq -> 945
/*      */       //   518: iload #18
/*      */       //   520: bipush #10
/*      */       //   522: if_icmpne -> 528
/*      */       //   525: goto -> 945
/*      */       //   528: iinc #15, 1
/*      */       //   531: goto -> 496
/*      */       //   534: iload #18
/*      */       //   536: bipush #47
/*      */       //   538: if_icmpne -> 659
/*      */       //   541: iload #15
/*      */       //   543: iconst_1
/*      */       //   544: iadd
/*      */       //   545: aload_0
/*      */       //   546: getfield statementLength : I
/*      */       //   549: if_icmpge -> 659
/*      */       //   552: aload_2
/*      */       //   553: iload #15
/*      */       //   555: iconst_1
/*      */       //   556: iadd
/*      */       //   557: invokevirtual charAt : (I)C
/*      */       //   560: istore #19
/*      */       //   562: iload #19
/*      */       //   564: bipush #42
/*      */       //   566: if_icmpne -> 656
/*      */       //   569: iinc #15, 2
/*      */       //   572: iload #15
/*      */       //   574: istore #20
/*      */       //   576: iload #20
/*      */       //   578: aload_0
/*      */       //   579: getfield statementLength : I
/*      */       //   582: if_icmpge -> 656
/*      */       //   585: iinc #15, 1
/*      */       //   588: aload_2
/*      */       //   589: iload #20
/*      */       //   591: invokevirtual charAt : (I)C
/*      */       //   594: istore #19
/*      */       //   596: iload #19
/*      */       //   598: bipush #42
/*      */       //   600: if_icmpne -> 650
/*      */       //   603: iload #20
/*      */       //   605: iconst_1
/*      */       //   606: iadd
/*      */       //   607: aload_0
/*      */       //   608: getfield statementLength : I
/*      */       //   611: if_icmpge -> 650
/*      */       //   614: aload_2
/*      */       //   615: iload #20
/*      */       //   617: iconst_1
/*      */       //   618: iadd
/*      */       //   619: invokevirtual charAt : (I)C
/*      */       //   622: bipush #47
/*      */       //   624: if_icmpne -> 650
/*      */       //   627: iinc #15, 1
/*      */       //   630: iload #15
/*      */       //   632: aload_0
/*      */       //   633: getfield statementLength : I
/*      */       //   636: if_icmpge -> 656
/*      */       //   639: aload_2
/*      */       //   640: iload #15
/*      */       //   642: invokevirtual charAt : (I)C
/*      */       //   645: istore #18
/*      */       //   647: goto -> 656
/*      */       //   650: iinc #20, 1
/*      */       //   653: goto -> 576
/*      */       //   656: goto -> 680
/*      */       //   659: iload #18
/*      */       //   661: bipush #39
/*      */       //   663: if_icmpeq -> 673
/*      */       //   666: iload #18
/*      */       //   668: bipush #34
/*      */       //   670: if_icmpne -> 680
/*      */       //   673: iconst_1
/*      */       //   674: istore #11
/*      */       //   676: iload #18
/*      */       //   678: istore #12
/*      */       //   680: iload #18
/*      */       //   682: bipush #63
/*      */       //   684: if_icmpne -> 743
/*      */       //   687: iload #11
/*      */       //   689: ifne -> 743
/*      */       //   692: iload #13
/*      */       //   694: ifne -> 743
/*      */       //   697: aload #10
/*      */       //   699: iconst_2
/*      */       //   700: newarray int
/*      */       //   702: dup
/*      */       //   703: iconst_0
/*      */       //   704: iload #14
/*      */       //   706: iastore
/*      */       //   707: dup
/*      */       //   708: iconst_1
/*      */       //   709: iload #15
/*      */       //   711: iastore
/*      */       //   712: invokevirtual add : (Ljava/lang/Object;)Z
/*      */       //   715: pop
/*      */       //   716: iload #15
/*      */       //   718: iconst_1
/*      */       //   719: iadd
/*      */       //   720: istore #14
/*      */       //   722: aload_0
/*      */       //   723: getfield isOnDuplicateKeyUpdate : Z
/*      */       //   726: ifeq -> 743
/*      */       //   729: iload #15
/*      */       //   731: aload_0
/*      */       //   732: getfield locationOfOnDuplicateKeyUpdate : I
/*      */       //   735: if_icmple -> 743
/*      */       //   738: aload_0
/*      */       //   739: iconst_1
/*      */       //   740: putfield parametersInDuplicateKeyClause : Z
/*      */       //   743: iload #11
/*      */       //   745: ifne -> 945
/*      */       //   748: iload #13
/*      */       //   750: ifne -> 945
/*      */       //   753: iload #15
/*      */       //   755: iload #16
/*      */       //   757: if_icmpge -> 945
/*      */       //   760: iload #18
/*      */       //   762: bipush #76
/*      */       //   764: if_icmpeq -> 774
/*      */       //   767: iload #18
/*      */       //   769: bipush #108
/*      */       //   771: if_icmpne -> 945
/*      */       //   774: aload_2
/*      */       //   775: iload #15
/*      */       //   777: iconst_1
/*      */       //   778: iadd
/*      */       //   779: invokevirtual charAt : (I)C
/*      */       //   782: istore #19
/*      */       //   784: iload #19
/*      */       //   786: bipush #73
/*      */       //   788: if_icmpeq -> 798
/*      */       //   791: iload #19
/*      */       //   793: bipush #105
/*      */       //   795: if_icmpne -> 945
/*      */       //   798: aload_2
/*      */       //   799: iload #15
/*      */       //   801: iconst_2
/*      */       //   802: iadd
/*      */       //   803: invokevirtual charAt : (I)C
/*      */       //   806: istore #20
/*      */       //   808: iload #20
/*      */       //   810: bipush #77
/*      */       //   812: if_icmpeq -> 822
/*      */       //   815: iload #20
/*      */       //   817: bipush #109
/*      */       //   819: if_icmpne -> 945
/*      */       //   822: aload_2
/*      */       //   823: iload #15
/*      */       //   825: iconst_3
/*      */       //   826: iadd
/*      */       //   827: invokevirtual charAt : (I)C
/*      */       //   830: istore #21
/*      */       //   832: iload #21
/*      */       //   834: bipush #73
/*      */       //   836: if_icmpeq -> 846
/*      */       //   839: iload #21
/*      */       //   841: bipush #105
/*      */       //   843: if_icmpne -> 945
/*      */       //   846: aload_2
/*      */       //   847: iload #15
/*      */       //   849: iconst_4
/*      */       //   850: iadd
/*      */       //   851: invokevirtual charAt : (I)C
/*      */       //   854: istore #22
/*      */       //   856: iload #22
/*      */       //   858: bipush #84
/*      */       //   860: if_icmpeq -> 870
/*      */       //   863: iload #22
/*      */       //   865: bipush #116
/*      */       //   867: if_icmpne -> 945
/*      */       //   870: iconst_0
/*      */       //   871: istore #23
/*      */       //   873: iconst_0
/*      */       //   874: istore #24
/*      */       //   876: iload #15
/*      */       //   878: aload_0
/*      */       //   879: getfield statementStartPos : I
/*      */       //   882: if_icmple -> 902
/*      */       //   885: aload_2
/*      */       //   886: iload #15
/*      */       //   888: iconst_1
/*      */       //   889: isub
/*      */       //   890: invokevirtual charAt : (I)C
/*      */       //   893: invokestatic isValidIdChar : (C)Z
/*      */       //   896: ifeq -> 902
/*      */       //   899: iconst_1
/*      */       //   900: istore #23
/*      */       //   902: iload #15
/*      */       //   904: iconst_5
/*      */       //   905: iadd
/*      */       //   906: aload_0
/*      */       //   907: getfield statementLength : I
/*      */       //   910: if_icmpge -> 930
/*      */       //   913: aload_2
/*      */       //   914: iload #15
/*      */       //   916: iconst_5
/*      */       //   917: iadd
/*      */       //   918: invokevirtual charAt : (I)C
/*      */       //   921: invokestatic isValidIdChar : (C)Z
/*      */       //   924: ifeq -> 930
/*      */       //   927: iconst_1
/*      */       //   928: istore #24
/*      */       //   930: iload #23
/*      */       //   932: ifne -> 945
/*      */       //   935: iload #24
/*      */       //   937: ifne -> 945
/*      */       //   940: aload_0
/*      */       //   941: iconst_1
/*      */       //   942: putfield foundLimitClause : Z
/*      */       //   945: iinc #15, 1
/*      */       //   948: goto -> 230
/*      */       //   951: aload_0
/*      */       //   952: getfield firstStmtChar : C
/*      */       //   955: bipush #76
/*      */       //   957: if_icmpne -> 985
/*      */       //   960: aload_2
/*      */       //   961: ldc 'LOAD DATA'
/*      */       //   963: invokestatic startsWithIgnoreCaseAndWs : (Ljava/lang/String;Ljava/lang/String;)Z
/*      */       //   966: ifeq -> 977
/*      */       //   969: aload_0
/*      */       //   970: iconst_1
/*      */       //   971: putfield foundLoadData : Z
/*      */       //   974: goto -> 990
/*      */       //   977: aload_0
/*      */       //   978: iconst_0
/*      */       //   979: putfield foundLoadData : Z
/*      */       //   982: goto -> 990
/*      */       //   985: aload_0
/*      */       //   986: iconst_0
/*      */       //   987: putfield foundLoadData : Z
/*      */       //   990: aload #10
/*      */       //   992: iconst_2
/*      */       //   993: newarray int
/*      */       //   995: dup
/*      */       //   996: iconst_0
/*      */       //   997: iload #14
/*      */       //   999: iastore
/*      */       //   1000: dup
/*      */       //   1001: iconst_1
/*      */       //   1002: aload_0
/*      */       //   1003: getfield statementLength : I
/*      */       //   1006: iastore
/*      */       //   1007: invokevirtual add : (Ljava/lang/Object;)Z
/*      */       //   1010: pop
/*      */       //   1011: aload_0
/*      */       //   1012: aload #10
/*      */       //   1014: invokevirtual size : ()I
/*      */       //   1017: anewarray [B
/*      */       //   1020: putfield staticSql : [[B
/*      */       //   1023: aload_2
/*      */       //   1024: invokevirtual toCharArray : ()[C
/*      */       //   1027: astore #18
/*      */       //   1029: iconst_0
/*      */       //   1030: istore #15
/*      */       //   1032: iload #15
/*      */       //   1034: aload_0
/*      */       //   1035: getfield staticSql : [[B
/*      */       //   1038: arraylength
/*      */       //   1039: if_icmpge -> 1271
/*      */       //   1042: aload #10
/*      */       //   1044: iload #15
/*      */       //   1046: invokevirtual get : (I)Ljava/lang/Object;
/*      */       //   1049: checkcast [I
/*      */       //   1052: astore #19
/*      */       //   1054: aload #19
/*      */       //   1056: iconst_1
/*      */       //   1057: iaload
/*      */       //   1058: istore #20
/*      */       //   1060: aload #19
/*      */       //   1062: iconst_0
/*      */       //   1063: iaload
/*      */       //   1064: istore #21
/*      */       //   1066: iload #20
/*      */       //   1068: iload #21
/*      */       //   1070: isub
/*      */       //   1071: istore #22
/*      */       //   1073: aload_0
/*      */       //   1074: getfield foundLoadData : Z
/*      */       //   1077: ifeq -> 1110
/*      */       //   1080: new java/lang/String
/*      */       //   1083: dup
/*      */       //   1084: aload #18
/*      */       //   1086: iload #21
/*      */       //   1088: iload #22
/*      */       //   1090: invokespecial <init> : ([CII)V
/*      */       //   1093: astore #23
/*      */       //   1095: aload_0
/*      */       //   1096: getfield staticSql : [[B
/*      */       //   1099: iload #15
/*      */       //   1101: aload #23
/*      */       //   1103: invokestatic getBytes : (Ljava/lang/String;)[B
/*      */       //   1106: aastore
/*      */       //   1107: goto -> 1265
/*      */       //   1110: aload #5
/*      */       //   1112: ifnonnull -> 1164
/*      */       //   1115: iload #22
/*      */       //   1117: newarray byte
/*      */       //   1119: astore #23
/*      */       //   1121: iconst_0
/*      */       //   1122: istore #24
/*      */       //   1124: iload #24
/*      */       //   1126: iload #22
/*      */       //   1128: if_icmpge -> 1152
/*      */       //   1131: aload #23
/*      */       //   1133: iload #24
/*      */       //   1135: aload_2
/*      */       //   1136: iload #21
/*      */       //   1138: iload #24
/*      */       //   1140: iadd
/*      */       //   1141: invokevirtual charAt : (I)C
/*      */       //   1144: i2b
/*      */       //   1145: bastore
/*      */       //   1146: iinc #24, 1
/*      */       //   1149: goto -> 1124
/*      */       //   1152: aload_0
/*      */       //   1153: getfield staticSql : [[B
/*      */       //   1156: iload #15
/*      */       //   1158: aload #23
/*      */       //   1160: aastore
/*      */       //   1161: goto -> 1265
/*      */       //   1164: aload #6
/*      */       //   1166: ifnull -> 1213
/*      */       //   1169: aload_0
/*      */       //   1170: getfield staticSql : [[B
/*      */       //   1173: iload #15
/*      */       //   1175: aload_2
/*      */       //   1176: aload #6
/*      */       //   1178: aload #5
/*      */       //   1180: aload_1
/*      */       //   1181: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
/*      */       //   1184: invokeinterface getServerCharacterEncoding : ()Ljava/lang/String;
/*      */       //   1189: iload #21
/*      */       //   1191: iload #22
/*      */       //   1193: aload_1
/*      */       //   1194: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
/*      */       //   1197: invokeinterface parserKnowsUnicode : ()Z
/*      */       //   1202: aload_1
/*      */       //   1203: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
/*      */       //   1206: invokestatic getBytes : (Ljava/lang/String;Lcom/mysql/jdbc/SingleByteCharsetConverter;Ljava/lang/String;Ljava/lang/String;IIZLcom/mysql/jdbc/ExceptionInterceptor;)[B
/*      */       //   1209: aastore
/*      */       //   1210: goto -> 1265
/*      */       //   1213: new java/lang/String
/*      */       //   1216: dup
/*      */       //   1217: aload #18
/*      */       //   1219: iload #21
/*      */       //   1221: iload #22
/*      */       //   1223: invokespecial <init> : ([CII)V
/*      */       //   1226: astore #23
/*      */       //   1228: aload_0
/*      */       //   1229: getfield staticSql : [[B
/*      */       //   1232: iload #15
/*      */       //   1234: aload #23
/*      */       //   1236: aload #5
/*      */       //   1238: aload_1
/*      */       //   1239: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
/*      */       //   1242: invokeinterface getServerCharacterEncoding : ()Ljava/lang/String;
/*      */       //   1247: aload_1
/*      */       //   1248: getfield connection : Lcom/mysql/jdbc/MySQLConnection;
/*      */       //   1251: invokeinterface parserKnowsUnicode : ()Z
/*      */       //   1256: aload_3
/*      */       //   1257: aload_1
/*      */       //   1258: invokevirtual getExceptionInterceptor : ()Lcom/mysql/jdbc/ExceptionInterceptor;
/*      */       //   1261: invokestatic getBytes : (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZLcom/mysql/jdbc/MySQLConnection;Lcom/mysql/jdbc/ExceptionInterceptor;)[B
/*      */       //   1264: aastore
/*      */       //   1265: iinc #15, 1
/*      */       //   1268: goto -> 1032
/*      */       //   1271: goto -> 1315
/*      */       //   1274: astore #8
/*      */       //   1276: new java/sql/SQLException
/*      */       //   1279: dup
/*      */       //   1280: new java/lang/StringBuilder
/*      */       //   1283: dup
/*      */       //   1284: invokespecial <init> : ()V
/*      */       //   1287: ldc 'Parse error for '
/*      */       //   1289: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */       //   1292: aload_2
/*      */       //   1293: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */       //   1296: invokevirtual toString : ()Ljava/lang/String;
/*      */       //   1299: invokespecial <init> : (Ljava/lang/String;)V
/*      */       //   1302: astore #9
/*      */       //   1304: aload #9
/*      */       //   1306: aload #8
/*      */       //   1308: invokevirtual initCause : (Ljava/lang/Throwable;)Ljava/lang/Throwable;
/*      */       //   1311: pop
/*      */       //   1312: aload #9
/*      */       //   1314: athrow
/*      */       //   1315: iload #7
/*      */       //   1317: ifeq -> 1383
/*      */       //   1320: aload_0
/*      */       //   1321: aload_2
/*      */       //   1322: aload_0
/*      */       //   1323: getfield isOnDuplicateKeyUpdate : Z
/*      */       //   1326: aload_0
/*      */       //   1327: getfield locationOfOnDuplicateKeyUpdate : I
/*      */       //   1330: aload_0
/*      */       //   1331: getfield statementStartPos : I
/*      */       //   1334: invokestatic canRewrite : (Ljava/lang/String;ZII)Z
/*      */       //   1337: ifeq -> 1351
/*      */       //   1340: aload_0
/*      */       //   1341: getfield parametersInDuplicateKeyClause : Z
/*      */       //   1344: ifne -> 1351
/*      */       //   1347: iconst_1
/*      */       //   1348: goto -> 1352
/*      */       //   1351: iconst_0
/*      */       //   1352: putfield canRewriteAsMultiValueInsert : Z
/*      */       //   1355: aload_0
/*      */       //   1356: getfield canRewriteAsMultiValueInsert : Z
/*      */       //   1359: ifeq -> 1383
/*      */       //   1362: aload_3
/*      */       //   1363: invokeinterface getRewriteBatchedStatements : ()Z
/*      */       //   1368: ifeq -> 1383
/*      */       //   1371: aload_0
/*      */       //   1372: aload_2
/*      */       //   1373: aload_3
/*      */       //   1374: aload #4
/*      */       //   1376: aload #5
/*      */       //   1378: aload #6
/*      */       //   1380: invokespecial buildRewriteBatchedParams : (Ljava/lang/String;Lcom/mysql/jdbc/MySQLConnection;Ljava/sql/DatabaseMetaData;Ljava/lang/String;Lcom/mysql/jdbc/SingleByteCharsetConverter;)V
/*      */       //   1383: return
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #210	-> 0
/*      */       //   #172	-> 9
/*      */       //   #174	-> 14
/*      */       //   #176	-> 19
/*      */       //   #178	-> 24
/*      */       //   #180	-> 29
/*      */       //   #182	-> 34
/*      */       //   #184	-> 39
/*      */       //   #186	-> 44
/*      */       //   #188	-> 52
/*      */       //   #190	-> 57
/*      */       //   #194	-> 62
/*      */       //   #212	-> 67
/*      */       //   #213	-> 71
/*      */       //   #218	-> 86
/*      */       //   #219	-> 95
/*      */       //   #221	-> 112
/*      */       //   #223	-> 119
/*      */       //   #225	-> 128
/*      */       //   #227	-> 131
/*      */       //   #230	-> 154
/*      */       //   #233	-> 162
/*      */       //   #235	-> 170
/*      */       //   #236	-> 179
/*      */       //   #237	-> 182
/*      */       //   #238	-> 185
/*      */       //   #239	-> 188
/*      */       //   #242	-> 191
/*      */       //   #244	-> 199
/*      */       //   #246	-> 204
/*      */       //   #252	-> 215
/*      */       //   #254	-> 224
/*      */       //   #255	-> 239
/*      */       //   #257	-> 247
/*      */       //   #260	-> 262
/*      */       //   #263	-> 271
/*      */       //   #265	-> 294
/*      */       //   #266	-> 297
/*      */       //   #271	-> 300
/*      */       //   #273	-> 317
/*      */       //   #274	-> 332
/*      */       //   #277	-> 337
/*      */       //   #278	-> 342
/*      */       //   #279	-> 363
/*      */       //   #280	-> 387
/*      */       //   #281	-> 390
/*      */       //   #284	-> 393
/*      */       //   #285	-> 405
/*      */       //   #286	-> 411
/*      */       //   #287	-> 432
/*      */       //   #288	-> 444
/*      */       //   #291	-> 450
/*      */       //   #296	-> 488
/*      */       //   #298	-> 496
/*      */       //   #299	-> 503
/*      */       //   #301	-> 511
/*      */       //   #302	-> 525
/*      */       //   #298	-> 528
/*      */       //   #307	-> 534
/*      */       //   #309	-> 552
/*      */       //   #311	-> 562
/*      */       //   #312	-> 569
/*      */       //   #314	-> 572
/*      */       //   #315	-> 585
/*      */       //   #316	-> 588
/*      */       //   #318	-> 596
/*      */       //   #319	-> 614
/*      */       //   #320	-> 627
/*      */       //   #322	-> 630
/*      */       //   #323	-> 639
/*      */       //   #314	-> 650
/*      */       //   #331	-> 656
/*      */       //   #332	-> 673
/*      */       //   #333	-> 676
/*      */       //   #338	-> 680
/*      */       //   #339	-> 697
/*      */       //   #340	-> 716
/*      */       //   #342	-> 722
/*      */       //   #343	-> 738
/*      */       //   #347	-> 743
/*      */       //   #348	-> 760
/*      */       //   #349	-> 774
/*      */       //   #351	-> 784
/*      */       //   #352	-> 798
/*      */       //   #354	-> 808
/*      */       //   #355	-> 822
/*      */       //   #357	-> 832
/*      */       //   #358	-> 846
/*      */       //   #360	-> 856
/*      */       //   #362	-> 870
/*      */       //   #363	-> 873
/*      */       //   #364	-> 876
/*      */       //   #365	-> 899
/*      */       //   #367	-> 902
/*      */       //   #368	-> 927
/*      */       //   #370	-> 930
/*      */       //   #371	-> 940
/*      */       //   #254	-> 945
/*      */       //   #381	-> 951
/*      */       //   #382	-> 960
/*      */       //   #383	-> 969
/*      */       //   #385	-> 977
/*      */       //   #388	-> 985
/*      */       //   #391	-> 990
/*      */       //   #392	-> 1011
/*      */       //   #393	-> 1023
/*      */       //   #395	-> 1029
/*      */       //   #396	-> 1042
/*      */       //   #397	-> 1054
/*      */       //   #398	-> 1060
/*      */       //   #399	-> 1066
/*      */       //   #401	-> 1073
/*      */       //   #402	-> 1080
/*      */       //   #403	-> 1095
/*      */       //   #404	-> 1107
/*      */       //   #405	-> 1115
/*      */       //   #407	-> 1121
/*      */       //   #408	-> 1131
/*      */       //   #407	-> 1146
/*      */       //   #411	-> 1152
/*      */       //   #412	-> 1161
/*      */       //   #413	-> 1164
/*      */       //   #414	-> 1169
/*      */       //   #419	-> 1213
/*      */       //   #421	-> 1228
/*      */       //   #395	-> 1265
/*      */       //   #433	-> 1271
/*      */       //   #428	-> 1274
/*      */       //   #429	-> 1276
/*      */       //   #430	-> 1304
/*      */       //   #432	-> 1312
/*      */       //   #436	-> 1315
/*      */       //   #437	-> 1320
/*      */       //   #442	-> 1355
/*      */       //   #444	-> 1371
/*      */       //   #448	-> 1383
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   496	38	19	endOfStmt	I
/*      */       //   576	80	20	j	I
/*      */       //   562	94	19	cNext	C
/*      */       //   873	72	23	hasPreviosIdChar	Z
/*      */       //   876	69	24	hasFollowingIdChar	Z
/*      */       //   856	89	22	posT	C
/*      */       //   832	113	21	posI2	C
/*      */       //   808	137	20	posM	C
/*      */       //   784	161	19	posI1	C
/*      */       //   247	698	18	c	C
/*      */       //   1095	12	23	temp	Ljava/lang/String;
/*      */       //   1124	28	24	j	I
/*      */       //   1121	40	23	buf	[B
/*      */       //   1228	37	23	temp	Ljava/lang/String;
/*      */       //   1054	211	19	ep	[I
/*      */       //   1060	205	20	end	I
/*      */       //   1066	199	21	begin	I
/*      */       //   1073	192	22	len	I
/*      */       //   128	1143	8	quotedIdentifierString	Ljava/lang/String;
/*      */       //   131	1140	9	quotedIdentifierChar	C
/*      */       //   179	1092	10	endpointList	Ljava/util/ArrayList;
/*      */       //   182	1089	11	inQuotes	Z
/*      */       //   185	1086	12	quoteChar	C
/*      */       //   188	1083	13	inQuotedId	Z
/*      */       //   191	1080	14	lastParmEnd	I
/*      */       //   230	1041	15	i	I
/*      */       //   199	1072	16	stopLookingForLimitClause	I
/*      */       //   215	1056	17	noBackslashEscapes	Z
/*      */       //   1029	242	18	asCharArray	[C
/*      */       //   1304	11	9	sqlEx	Ljava/sql/SQLException;
/*      */       //   1276	39	8	oobEx	Ljava/lang/StringIndexOutOfBoundsException;
/*      */       //   0	1384	0	this	Lcom/mysql/jdbc/PreparedStatement$ParseInfo;
/*      */       //   0	1384	2	sql	Ljava/lang/String;
/*      */       //   0	1384	3	conn	Lcom/mysql/jdbc/MySQLConnection;
/*      */       //   0	1384	4	dbmd	Ljava/sql/DatabaseMetaData;
/*      */       //   0	1384	5	encoding	Ljava/lang/String;
/*      */       //   0	1384	6	converter	Lcom/mysql/jdbc/SingleByteCharsetConverter;
/*      */       //   0	1384	7	buildRewriteInfo	Z
/*      */       // Local variable type table:
/*      */       //   start	length	slot	name	signature
/*      */       //   179	1092	10	endpointList	Ljava/util/ArrayList<[I>;
/*      */       // Exception table:
/*      */       //   from	to	target	type
/*      */       //   67	1271	1274	java/lang/StringIndexOutOfBoundsException
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void buildRewriteBatchedParams(String sql, MySQLConnection conn, DatabaseMetaData metadata, String encoding, SingleByteCharsetConverter converter) throws SQLException {
/*  459 */       this.valuesClause = extractValuesClause(sql);
/*  460 */       String odkuClause = this.isOnDuplicateKeyUpdate ? sql.substring(this.locationOfOnDuplicateKeyUpdate) : null;
/*      */ 
/*      */       
/*  463 */       String headSql = null;
/*      */       
/*  465 */       if (this.isOnDuplicateKeyUpdate) {
/*  466 */         headSql = sql.substring(0, this.locationOfOnDuplicateKeyUpdate);
/*      */       } else {
/*  468 */         headSql = sql;
/*      */       } 
/*      */       
/*  471 */       this.batchHead = new ParseInfo(headSql, conn, metadata, encoding, converter, false);
/*      */       
/*  473 */       this.batchValues = new ParseInfo("," + this.valuesClause, conn, metadata, encoding, converter, false);
/*      */       
/*  475 */       this.batchODKUClause = null;
/*      */       
/*  477 */       if (odkuClause != null && odkuClause.length() > 0) {
/*  478 */         this.batchODKUClause = new ParseInfo("," + this.valuesClause + " " + odkuClause, conn, metadata, encoding, converter, false);
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private String extractValuesClause(String sql) throws SQLException {
/*  485 */       String quoteCharStr = PreparedStatement.this.connection.getMetaData().getIdentifierQuoteString();
/*      */ 
/*      */       
/*  488 */       int indexOfValues = -1;
/*  489 */       int valuesSearchStart = this.statementStartPos;
/*      */       
/*  491 */       while (indexOfValues == -1) {
/*  492 */         if (quoteCharStr.length() > 0) {
/*  493 */           indexOfValues = StringUtils.indexOfIgnoreCaseRespectQuotes(valuesSearchStart, PreparedStatement.this.originalSql, "VALUES", quoteCharStr.charAt(0), false);
/*      */         }
/*      */         else {
/*      */           
/*  497 */           indexOfValues = StringUtils.indexOfIgnoreCase(valuesSearchStart, PreparedStatement.this.originalSql, "VALUES");
/*      */         } 
/*      */ 
/*      */ 
/*      */         
/*  502 */         if (indexOfValues > 0) {
/*      */           
/*  504 */           char c = PreparedStatement.this.originalSql.charAt(indexOfValues - 1);
/*  505 */           if (!Character.isWhitespace(c) && c != ')' && c != '`') {
/*  506 */             valuesSearchStart = indexOfValues + 6;
/*  507 */             indexOfValues = -1;
/*      */             continue;
/*      */           } 
/*  510 */           c = PreparedStatement.this.originalSql.charAt(indexOfValues + 6);
/*  511 */           if (!Character.isWhitespace(c) && c != '(') {
/*  512 */             valuesSearchStart = indexOfValues + 6;
/*  513 */             indexOfValues = -1;
/*      */           } 
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  521 */       if (indexOfValues == -1) {
/*  522 */         return null;
/*      */       }
/*      */       
/*  525 */       int indexOfFirstParen = sql.indexOf('(', indexOfValues + 6);
/*      */       
/*  527 */       if (indexOfFirstParen == -1) {
/*  528 */         return null;
/*      */       }
/*      */       
/*  531 */       int endOfValuesClause = sql.lastIndexOf(')');
/*      */       
/*  533 */       if (endOfValuesClause == -1) {
/*  534 */         return null;
/*      */       }
/*      */       
/*  537 */       if (this.isOnDuplicateKeyUpdate) {
/*  538 */         endOfValuesClause = this.locationOfOnDuplicateKeyUpdate - 1;
/*      */       }
/*      */       
/*  541 */       return sql.substring(indexOfFirstParen, endOfValuesClause + 1);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     synchronized ParseInfo getParseInfoForBatch(int numBatch) {
/*  548 */       PreparedStatement.AppendingBatchVisitor apv = new PreparedStatement.AppendingBatchVisitor();
/*  549 */       buildInfoForBatch(numBatch, apv);
/*      */       
/*  551 */       ParseInfo batchParseInfo = new ParseInfo(apv.getStaticSqlStrings(), this.firstStmtChar, this.foundLimitClause, this.foundLoadData, this.isOnDuplicateKeyUpdate, this.locationOfOnDuplicateKeyUpdate, this.statementLength, this.statementStartPos);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  557 */       return batchParseInfo;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     String getSqlForBatch(int numBatch) throws UnsupportedEncodingException {
/*  566 */       ParseInfo batchInfo = getParseInfoForBatch(numBatch);
/*      */       
/*  568 */       return getSqlForBatch(batchInfo);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     String getSqlForBatch(ParseInfo batchInfo) throws UnsupportedEncodingException {
/*  575 */       int size = 0;
/*  576 */       byte[][] sqlStrings = batchInfo.staticSql;
/*  577 */       int sqlStringsLength = sqlStrings.length;
/*      */       
/*  579 */       for (int i = 0; i < sqlStringsLength; i++) {
/*  580 */         size += (sqlStrings[i]).length;
/*  581 */         size++;
/*      */       } 
/*      */       
/*  584 */       StringBuffer buf = new StringBuffer(size);
/*      */       
/*  586 */       for (int j = 0; j < sqlStringsLength - 1; j++) {
/*  587 */         buf.append(StringUtils.toString(sqlStrings[j], PreparedStatement.this.charEncoding));
/*  588 */         buf.append("?");
/*      */       } 
/*      */       
/*  591 */       buf.append(StringUtils.toString(sqlStrings[sqlStringsLength - 1]));
/*      */       
/*  593 */       return buf.toString();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void buildInfoForBatch(int numBatch, PreparedStatement.BatchVisitor visitor) {
/*  605 */       byte[][] headStaticSql = this.batchHead.staticSql;
/*  606 */       int headStaticSqlLength = headStaticSql.length;
/*      */       
/*  608 */       if (headStaticSqlLength > 1) {
/*  609 */         for (int j = 0; j < headStaticSqlLength - 1; j++) {
/*  610 */           visitor.append(headStaticSql[j]).increment();
/*      */         }
/*      */       }
/*      */ 
/*      */       
/*  615 */       byte[] endOfHead = headStaticSql[headStaticSqlLength - 1];
/*  616 */       byte[][] valuesStaticSql = this.batchValues.staticSql;
/*  617 */       byte[] beginOfValues = valuesStaticSql[0];
/*      */       
/*  619 */       visitor.merge(endOfHead, beginOfValues).increment();
/*      */       
/*  621 */       int numValueRepeats = numBatch - 1;
/*      */       
/*  623 */       if (this.batchODKUClause != null) {
/*  624 */         numValueRepeats--;
/*      */       }
/*      */       
/*  627 */       int valuesStaticSqlLength = valuesStaticSql.length;
/*  628 */       byte[] endOfValues = valuesStaticSql[valuesStaticSqlLength - 1];
/*      */       
/*  630 */       for (int i = 0; i < numValueRepeats; i++) {
/*  631 */         for (int j = 1; j < valuesStaticSqlLength - 1; j++) {
/*  632 */           visitor.append(valuesStaticSql[j]).increment();
/*      */         }
/*  634 */         visitor.merge(endOfValues, beginOfValues).increment();
/*      */       } 
/*      */       
/*  637 */       if (this.batchODKUClause != null) {
/*  638 */         byte[][] batchOdkuStaticSql = this.batchODKUClause.staticSql;
/*  639 */         byte[] beginOfOdku = batchOdkuStaticSql[0];
/*  640 */         visitor.decrement().merge(endOfValues, beginOfOdku).increment();
/*      */         
/*  642 */         int batchOdkuStaticSqlLength = batchOdkuStaticSql.length;
/*      */         
/*  644 */         if (numBatch > 1) {
/*  645 */           for (int j = 1; j < batchOdkuStaticSqlLength; j++) {
/*  646 */             visitor.append(batchOdkuStaticSql[j]).increment();
/*      */           }
/*      */         } else {
/*      */           
/*  650 */           visitor.decrement().append(batchOdkuStaticSql[batchOdkuStaticSqlLength - 1]);
/*      */         }
/*      */       
/*      */       } else {
/*      */         
/*  655 */         visitor.decrement().append(this.staticSql[this.staticSql.length - 1]);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private ParseInfo(byte[][] staticSql, char firstStmtChar, boolean foundLimitClause, boolean foundLoadData, boolean isOnDuplicateKeyUpdate, int locationOfOnDuplicateKeyUpdate, int statementLength, int statementStartPos) {
/*  664 */       this.firstStmtChar = firstStmtChar;
/*  665 */       this.foundLimitClause = foundLimitClause;
/*  666 */       this.foundLoadData = foundLoadData;
/*  667 */       this.isOnDuplicateKeyUpdate = isOnDuplicateKeyUpdate;
/*  668 */       this.locationOfOnDuplicateKeyUpdate = locationOfOnDuplicateKeyUpdate;
/*  669 */       this.statementLength = statementLength;
/*  670 */       this.statementStartPos = statementStartPos;
/*  671 */       this.staticSql = staticSql;
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
/*      */   class AppendingBatchVisitor
/*      */     implements BatchVisitor
/*      */   {
/*  686 */     LinkedList<byte[]> statementComponents = (LinkedList)new LinkedList<byte>();
/*      */     
/*      */     public PreparedStatement.BatchVisitor append(byte[] values) {
/*  689 */       this.statementComponents.addLast(values);
/*      */       
/*  691 */       return this;
/*      */     }
/*      */ 
/*      */     
/*      */     public PreparedStatement.BatchVisitor increment() {
/*  696 */       return this;
/*      */     }
/*      */     
/*      */     public PreparedStatement.BatchVisitor decrement() {
/*  700 */       this.statementComponents.removeLast();
/*      */       
/*  702 */       return this;
/*      */     }
/*      */     
/*      */     public PreparedStatement.BatchVisitor merge(byte[] front, byte[] back) {
/*  706 */       int mergedLength = front.length + back.length;
/*  707 */       byte[] merged = new byte[mergedLength];
/*  708 */       System.arraycopy(front, 0, merged, 0, front.length);
/*  709 */       System.arraycopy(back, 0, merged, front.length, back.length);
/*  710 */       this.statementComponents.addLast(merged);
/*  711 */       return this;
/*      */     }
/*      */     
/*      */     public byte[][] getStaticSqlStrings() {
/*  715 */       byte[][] asBytes = new byte[this.statementComponents.size()][];
/*  716 */       this.statementComponents.toArray(asBytes);
/*      */       
/*  718 */       return asBytes;
/*      */     }
/*      */     
/*      */     public String toString() {
/*  722 */       StringBuffer buf = new StringBuffer();
/*  723 */       Iterator<byte[]> iter = (Iterator)this.statementComponents.iterator();
/*  724 */       while (iter.hasNext()) {
/*  725 */         buf.append(StringUtils.toString(iter.next()));
/*      */       }
/*      */       
/*  728 */       return buf.toString();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*  733 */   private static final byte[] HEX_DIGITS = new byte[] { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70 };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static int readFully(Reader reader, char[] buf, int length) throws IOException {
/*  756 */     int numCharsRead = 0;
/*      */     
/*  758 */     while (numCharsRead < length) {
/*  759 */       int count = reader.read(buf, numCharsRead, length - numCharsRead);
/*      */       
/*  761 */       if (count < 0) {
/*      */         break;
/*      */       }
/*      */       
/*  765 */       numCharsRead += count;
/*      */     } 
/*      */     
/*  768 */     return numCharsRead;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean batchHasPlainStatements = false;
/*      */ 
/*      */ 
/*      */   
/*  779 */   private DatabaseMetaData dbmd = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  785 */   protected char firstCharOfStmt = Character.MIN_VALUE;
/*      */ 
/*      */   
/*      */   protected boolean hasLimitClause = false;
/*      */ 
/*      */   
/*      */   protected boolean isLoadDataQuery = false;
/*      */   
/*  793 */   protected boolean[] isNull = null;
/*      */   
/*  795 */   private boolean[] isStream = null;
/*      */   
/*  797 */   protected int numberOfExecutions = 0;
/*      */ 
/*      */   
/*  800 */   protected String originalSql = null;
/*      */ 
/*      */   
/*      */   protected int parameterCount;
/*      */   
/*      */   protected MysqlParameterMetadata parameterMetaData;
/*      */   
/*  807 */   private InputStream[] parameterStreams = null;
/*      */   
/*  809 */   private byte[][] parameterValues = (byte[][])null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  815 */   protected int[] parameterTypes = null;
/*      */   
/*      */   protected ParseInfo parseInfo;
/*      */   
/*      */   private ResultSetMetaData pstmtResultMetaData;
/*      */   
/*  821 */   private byte[][] staticSqlStrings = (byte[][])null;
/*      */   
/*  823 */   private byte[] streamConvertBuf = null;
/*      */   
/*  825 */   private int[] streamLengths = null;
/*      */   
/*  827 */   private SimpleDateFormat tsdf = null;
/*      */ 
/*      */   
/*      */   protected boolean useTrueBoolean = false;
/*      */ 
/*      */   
/*      */   protected boolean usingAnsiMode;
/*      */ 
/*      */   
/*      */   protected String batchedValuesClause;
/*      */   
/*      */   private boolean doPingInstead;
/*      */   
/*      */   private SimpleDateFormat ddf;
/*      */   
/*      */   private SimpleDateFormat tdf;
/*      */   
/*      */   private boolean compensateForOnDuplicateKeyUpdate = false;
/*      */   
/*      */   private CharsetEncoder charsetEncoder;
/*      */   
/*  848 */   protected int batchCommandIndex = -1;
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean serverSupportsFracSecs;
/*      */ 
/*      */ 
/*      */   
/*      */   protected int rewrittenBatchSize;
/*      */ 
/*      */ 
/*      */   
/*      */   protected static PreparedStatement getInstance(MySQLConnection conn, String catalog) throws SQLException {
/*  861 */     if (!Util.isJdbc4()) {
/*  862 */       return new PreparedStatement(conn, catalog);
/*      */     }
/*      */     
/*  865 */     return (PreparedStatement)Util.handleNewInstance(JDBC_4_PSTMT_2_ARG_CTOR, new Object[] { conn, catalog }, conn.getExceptionInterceptor());
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
/*      */   protected static PreparedStatement getInstance(MySQLConnection conn, String sql, String catalog) throws SQLException {
/*  878 */     if (!Util.isJdbc4()) {
/*  879 */       return new PreparedStatement(conn, sql, catalog);
/*      */     }
/*      */     
/*  882 */     return (PreparedStatement)Util.handleNewInstance(JDBC_4_PSTMT_3_ARG_CTOR, new Object[] { conn, sql, catalog }, conn.getExceptionInterceptor());
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
/*      */   protected static PreparedStatement getInstance(MySQLConnection conn, String sql, String catalog, ParseInfo cachedParseInfo) throws SQLException {
/*  895 */     if (!Util.isJdbc4()) {
/*  896 */       return new PreparedStatement(conn, sql, catalog, cachedParseInfo);
/*      */     }
/*      */     
/*  899 */     return (PreparedStatement)Util.handleNewInstance(JDBC_4_PSTMT_4_ARG_CTOR, new Object[] { conn, sql, catalog, cachedParseInfo }, conn.getExceptionInterceptor());
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
/*      */   public PreparedStatement(MySQLConnection conn, String catalog) throws SQLException
/*      */   {
/*  917 */     super(conn, catalog);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2636 */     this.rewrittenBatchSize = 0; detectFractionalSecondsSupport(); this.compensateForOnDuplicateKeyUpdate = this.connection.getCompensateOnDuplicateKeyUpdateCounts(); } protected void detectFractionalSecondsSupport() throws SQLException { this.serverSupportsFracSecs = (this.connection != null && this.connection.versionMeetsMinimum(5, 6, 4)); } public PreparedStatement(MySQLConnection conn, String sql, String catalog) throws SQLException { super(conn, catalog); this.rewrittenBatchSize = 0; if (sql == null) throw SQLError.createSQLException(Messages.getString("PreparedStatement.0"), "S1009", getExceptionInterceptor());  detectFractionalSecondsSupport(); this.originalSql = sql; if (this.originalSql.startsWith("/* ping */")) { this.doPingInstead = true; } else { this.doPingInstead = false; }  this.dbmd = this.connection.getMetaData(); this.useTrueBoolean = this.connection.versionMeetsMinimum(3, 21, 23); this.parseInfo = new ParseInfo(sql, this.connection, this.dbmd, this.charEncoding, this.charConverter); initializeFromParseInfo(); this.compensateForOnDuplicateKeyUpdate = this.connection.getCompensateOnDuplicateKeyUpdateCounts(); if (conn.getRequiresEscapingEncoder()) this.charsetEncoder = Charset.forName(conn.getEncoding()).newEncoder();  } public PreparedStatement(MySQLConnection conn, String sql, String catalog, ParseInfo cachedParseInfo) throws SQLException { super(conn, catalog); this.rewrittenBatchSize = 0; if (sql == null) throw SQLError.createSQLException(Messages.getString("PreparedStatement.1"), "S1009", getExceptionInterceptor());  detectFractionalSecondsSupport(); this.originalSql = sql; this.dbmd = this.connection.getMetaData(); this.useTrueBoolean = this.connection.versionMeetsMinimum(3, 21, 23); this.parseInfo = cachedParseInfo; this.usingAnsiMode = !this.connection.useAnsiQuotedIdentifiers(); initializeFromParseInfo(); this.compensateForOnDuplicateKeyUpdate = this.connection.getCompensateOnDuplicateKeyUpdateCounts(); if (conn.getRequiresEscapingEncoder()) this.charsetEncoder = Charset.forName(conn.getEncoding()).newEncoder();  }
/*      */   public void addBatch() throws SQLException { synchronized (checkClosed()) { if (this.batchedArgs == null) this.batchedArgs = new ArrayList();  for (int i = 0; i < this.parameterValues.length; i++) checkAllParametersSet(this.parameterValues[i], this.parameterStreams[i], i);  this.batchedArgs.add(new BatchParams(this.parameterValues, this.parameterStreams, this.isStream, this.streamLengths, this.isNull)); }  }
/*      */   public void addBatch(String sql) throws SQLException { synchronized (checkClosed()) { this.batchHasPlainStatements = true; super.addBatch(sql); }  }
/* 2639 */   protected String asSql() throws SQLException { return asSql(false); } protected String asSql(boolean quoteStreamsAndUnknowns) throws SQLException { synchronized (checkClosed()) { StringBuffer buf = new StringBuffer(); try { int realParameterCount = this.parameterCount + getParameterIndexOffset(); Object batchArg = null; if (this.batchCommandIndex != -1) batchArg = this.batchedArgs.get(this.batchCommandIndex);  for (int i = 0; i < realParameterCount; i++) { if (this.charEncoding != null) { buf.append(StringUtils.toString(this.staticSqlStrings[i], this.charEncoding)); } else { buf.append(StringUtils.toString(this.staticSqlStrings[i])); }  byte[] val = null; if (batchArg != null && batchArg instanceof String) { buf.append((String)batchArg); } else { if (this.batchCommandIndex == -1) { val = this.parameterValues[i]; } else { val = ((BatchParams)batchArg).parameterStrings[i]; }  boolean isStreamParam = false; if (this.batchCommandIndex == -1) { isStreamParam = this.isStream[i]; } else { isStreamParam = ((BatchParams)batchArg).isStream[i]; }  if (val == null && !isStreamParam) { if (quoteStreamsAndUnknowns) buf.append("'");  buf.append("** NOT SPECIFIED **"); if (quoteStreamsAndUnknowns) buf.append("'");  } else if (isStreamParam) { if (quoteStreamsAndUnknowns) buf.append("'");  buf.append("** STREAM DATA **"); if (quoteStreamsAndUnknowns) buf.append("'");  } else if (this.charConverter != null) { buf.append(this.charConverter.toString(val)); } else if (this.charEncoding != null) { buf.append(new String(val, this.charEncoding)); } else { buf.append(StringUtils.toAsciiString(val)); }  }  }  if (this.charEncoding != null) { buf.append(StringUtils.toString(this.staticSqlStrings[this.parameterCount + getParameterIndexOffset()], this.charEncoding)); } else { buf.append(StringUtils.toAsciiString(this.staticSqlStrings[this.parameterCount + getParameterIndexOffset()])); }  } catch (UnsupportedEncodingException uue) { throw new RuntimeException(Messages.getString("PreparedStatement.32") + this.charEncoding + Messages.getString("PreparedStatement.33")); }  return buf.toString(); }  } public void clearBatch() throws SQLException { synchronized (checkClosed()) { this.batchHasPlainStatements = false; super.clearBatch(); }  } public void clearParameters() throws SQLException { synchronized (checkClosed()) { for (int i = 0; i < this.parameterValues.length; i++) { this.parameterValues[i] = null; this.parameterStreams[i] = null; this.isStream[i] = false; this.isNull[i] = false; this.parameterTypes[i] = 0; }  }  } private final void escapeblockFast(byte[] buf, Buffer packet, int size) throws SQLException { int lastwritten = 0; for (int i = 0; i < size; i++) { byte b = buf[i]; if (b == 0) { if (i > lastwritten) packet.writeBytesNoNull(buf, lastwritten, i - lastwritten);  packet.writeByte((byte)92); packet.writeByte((byte)48); lastwritten = i + 1; } else if (b == 92 || b == 39 || (!this.usingAnsiMode && b == 34)) { if (i > lastwritten) packet.writeBytesNoNull(buf, lastwritten, i - lastwritten);  packet.writeByte((byte)92); lastwritten = i; }  }  if (lastwritten < size) packet.writeBytesNoNull(buf, lastwritten, size - lastwritten);  } private final void escapeblockFast(byte[] buf, ByteArrayOutputStream bytesOut, int size) { int lastwritten = 0; for (int i = 0; i < size; i++) { byte b = buf[i]; if (b == 0) { if (i > lastwritten) bytesOut.write(buf, lastwritten, i - lastwritten);  bytesOut.write(92); bytesOut.write(48); lastwritten = i + 1; } else if (b == 92 || b == 39 || (!this.usingAnsiMode && b == 34)) { if (i > lastwritten) bytesOut.write(buf, lastwritten, i - lastwritten);  bytesOut.write(92); lastwritten = i; }  }  if (lastwritten < size) bytesOut.write(buf, lastwritten, size - lastwritten);  } protected boolean checkReadOnlySafeStatement() throws SQLException { synchronized (checkClosed()) { return (!this.connection.isReadOnly() || this.firstCharOfStmt == 'S'); }  } public boolean execute() throws SQLException { synchronized (checkClosed()) { MySQLConnection locallyScopedConn = this.connection; if (!checkReadOnlySafeStatement()) throw SQLError.createSQLException(Messages.getString("PreparedStatement.20") + Messages.getString("PreparedStatement.21"), "S1009", getExceptionInterceptor());  ResultSetInternalMethods rs = null; CachedResultSetMetaData cachedMetadata = null; this.lastQueryIsOnDupKeyUpdate = false; if (this.retrieveGeneratedKeys) this.lastQueryIsOnDupKeyUpdate = containsOnDuplicateKeyUpdateInSQL();  boolean doStreaming = createStreamingResultSet(); clearWarnings(); if (doStreaming && this.connection.getNetTimeoutForStreamingResults() > 0) executeSimpleNonQuery(locallyScopedConn, "SET net_write_timeout=" + this.connection.getNetTimeoutForStreamingResults());  this.batchedGeneratedKeys = null; Buffer sendPacket = fillSendPacket(); String oldCatalog = null; if (!locallyScopedConn.getCatalog().equals(this.currentCatalog)) { oldCatalog = locallyScopedConn.getCatalog(); locallyScopedConn.setCatalog(this.currentCatalog); }  if (locallyScopedConn.getCacheResultSetMetadata()) cachedMetadata = locallyScopedConn.getCachedMetaData(this.originalSql);  Field[] metadataFromCache = null; if (cachedMetadata != null) metadataFromCache = cachedMetadata.fields;  boolean oldInfoMsgState = false; if (this.retrieveGeneratedKeys) { oldInfoMsgState = locallyScopedConn.isReadInfoMsgEnabled(); locallyScopedConn.setReadInfoMsgEnabled(true); }  if (locallyScopedConn.useMaxRows()) { int rowLimit = -1; if (this.firstCharOfStmt == 'S') { if (this.hasLimitClause) { rowLimit = this.maxRows; } else if (this.maxRows <= 0) { executeSimpleNonQuery(locallyScopedConn, "SET SQL_SELECT_LIMIT=DEFAULT"); } else { executeSimpleNonQuery(locallyScopedConn, "SET SQL_SELECT_LIMIT=" + this.maxRows); }  } else { executeSimpleNonQuery(locallyScopedConn, "SET SQL_SELECT_LIMIT=DEFAULT"); }  rs = executeInternal(rowLimit, sendPacket, doStreaming, (this.firstCharOfStmt == 'S'), metadataFromCache, false); } else { rs = executeInternal(-1, sendPacket, doStreaming, (this.firstCharOfStmt == 'S'), metadataFromCache, false); }  if (cachedMetadata != null) { locallyScopedConn.initializeResultsMetadataFromCache(this.originalSql, cachedMetadata, this.results); } else if (rs.reallyResult() && locallyScopedConn.getCacheResultSetMetadata()) { locallyScopedConn.initializeResultsMetadataFromCache(this.originalSql, (CachedResultSetMetaData)null, rs); }  if (this.retrieveGeneratedKeys) { locallyScopedConn.setReadInfoMsgEnabled(oldInfoMsgState); rs.setFirstCharOfQuery(this.firstCharOfStmt); }  if (oldCatalog != null) locallyScopedConn.setCatalog(oldCatalog);  if (rs != null) { this.lastInsertId = rs.getUpdateID(); this.results = rs; }  return (rs != null && rs.reallyResult()); }  } public int[] executeBatch() throws SQLException { synchronized (checkClosed()) { if (this.connection.isReadOnly()) throw new SQLException(Messages.getString("PreparedStatement.25") + Messages.getString("PreparedStatement.26"), "S1009");  if (this.batchedArgs == null || this.batchedArgs.size() == 0) return new int[0];  int batchTimeout = this.timeoutInMillis; this.timeoutInMillis = 0; resetCancelledState(); try { statementBegins(); clearWarnings(); if (!this.batchHasPlainStatements && this.connection.getRewriteBatchedStatements()) { if (canRewriteAsMultiValueInsertAtSqlLevel()) return executeBatchedInserts(batchTimeout);  if (this.connection.versionMeetsMinimum(4, 1, 0) && !this.batchHasPlainStatements && this.batchedArgs != null && this.batchedArgs.size() > 3) return executePreparedBatchAsMultiStatement(batchTimeout);  }  return executeBatchSerially(batchTimeout); } finally { this.statementExecuting.set(false); clearBatch(); }  }  } public boolean canRewriteAsMultiValueInsertAtSqlLevel() throws SQLException { return this.parseInfo.canRewriteAsMultiValueInsert; } protected int getLocationOfOnDuplicateKeyUpdate() throws SQLException { return this.parseInfo.locationOfOnDuplicateKeyUpdate; } protected int[] executePreparedBatchAsMultiStatement(int batchTimeout) throws SQLException { synchronized (checkClosed()) { if (this.batchedValuesClause == null) this.batchedValuesClause = this.originalSql + ";";  MySQLConnection locallyScopedConn = this.connection; boolean multiQueriesEnabled = locallyScopedConn.getAllowMultiQueries(); StatementImpl.CancelTask timeoutTask = null; try { clearWarnings(); int numBatchedArgs = this.batchedArgs.size(); if (this.retrieveGeneratedKeys) this.batchedGeneratedKeys = new ArrayList<ResultSetRow>(numBatchedArgs);  int numValuesPerBatch = computeBatchSize(numBatchedArgs); if (numBatchedArgs < numValuesPerBatch) numValuesPerBatch = numBatchedArgs;  PreparedStatement batchedStatement = null; int batchedParamIndex = 1; int numberToExecuteAsMultiValue = 0; int batchCounter = 0; int updateCountCounter = 0; } finally { if (timeoutTask != null) { timeoutTask.cancel(); locallyScopedConn.getCancelTimer().purge(); }  resetCancelledState(); if (!multiQueriesEnabled) locallyScopedConn.getIO().disableMultiQueries();  clearBatch(); }  }  } private String generateMultiStatementForBatch(int numBatches) throws SQLException { synchronized (checkClosed()) { StringBuffer newStatementSql = new StringBuffer((this.originalSql.length() + 1) * numBatches); newStatementSql.append(this.originalSql); for (int i = 0; i < numBatches - 1; i++) { newStatementSql.append(';'); newStatementSql.append(this.originalSql); }  return newStatementSql.toString(); }  } protected int[] executeBatchedInserts(int batchTimeout) throws SQLException { synchronized (checkClosed()) { String valuesClause = getValuesClause(); MySQLConnection locallyScopedConn = this.connection; if (valuesClause == null) return executeBatchSerially(batchTimeout);  int numBatchedArgs = this.batchedArgs.size(); if (this.retrieveGeneratedKeys) this.batchedGeneratedKeys = new ArrayList<ResultSetRow>(numBatchedArgs);  int numValuesPerBatch = computeBatchSize(numBatchedArgs); if (numBatchedArgs < numValuesPerBatch) numValuesPerBatch = numBatchedArgs;  PreparedStatement batchedStatement = null; int batchedParamIndex = 1; int updateCountRunningTotal = 0; int numberToExecuteAsMultiValue = 0; int batchCounter = 0; StatementImpl.CancelTask timeoutTask = null; SQLException sqlEx = null; int[] updateCounts = new int[numBatchedArgs]; for (int i = 0; i < this.batchedArgs.size(); i++) updateCounts[i] = 1;  try {  } finally { if (timeoutTask != null) { timeoutTask.cancel(); locallyScopedConn.getCancelTimer().purge(); }  resetCancelledState(); }  }  } public int getRewrittenBatchSize() { return this.rewrittenBatchSize; }
/*      */   protected String getValuesClause() throws SQLException { return this.parseInfo.valuesClause; }
/*      */   protected int computeBatchSize(int numBatchedArgs) throws SQLException { synchronized (checkClosed()) { long[] combinedValues = computeMaxParameterSetSizeAndBatchSize(numBatchedArgs); long maxSizeOfParameterSet = combinedValues[0]; long sizeOfEntireBatch = combinedValues[1]; int maxAllowedPacket = this.connection.getMaxAllowedPacket(); if (sizeOfEntireBatch < (maxAllowedPacket - this.originalSql.length())) return numBatchedArgs;  return (int)Math.max(1L, (maxAllowedPacket - this.originalSql.length()) / maxSizeOfParameterSet); }  }
/*      */   protected long[] computeMaxParameterSetSizeAndBatchSize(int numBatchedArgs) throws SQLException { synchronized (checkClosed()) { long sizeOfEntireBatch = 0L; long maxSizeOfParameterSet = 0L; for (int i = 0; i < numBatchedArgs; i++) { BatchParams paramArg = (BatchParams)this.batchedArgs.get(i); boolean[] isNullBatch = paramArg.isNull; boolean[] isStreamBatch = paramArg.isStream; long sizeOfParameterSet = 0L; for (int j = 0; j < isNullBatch.length; j++) { if (!isNullBatch[j]) { if (isStreamBatch[j]) { int streamLength = paramArg.streamLengths[j]; if (streamLength != -1) { sizeOfParameterSet += (streamLength * 2); } else { int paramLength = (paramArg.parameterStrings[j]).length; sizeOfParameterSet += paramLength; }  } else { sizeOfParameterSet += (paramArg.parameterStrings[j]).length; }  } else { sizeOfParameterSet += 4L; }  }  if (getValuesClause() != null) { sizeOfParameterSet += (getValuesClause().length() + 1); } else { sizeOfParameterSet += (this.originalSql.length() + 1); }  sizeOfEntireBatch += sizeOfParameterSet; if (sizeOfParameterSet > maxSizeOfParameterSet) maxSizeOfParameterSet = sizeOfParameterSet;  }  (new long[2])[0] = maxSizeOfParameterSet; (new long[2])[1] = sizeOfEntireBatch; return new long[2]; }  }
/* 2643 */   protected int[] executeBatchSerially(int batchTimeout) throws SQLException { synchronized (checkClosed()) { MySQLConnection locallyScopedConn = this.connection; if (locallyScopedConn == null) checkClosed();  int[] updateCounts = null; if (this.batchedArgs != null) { int nbrCommands = this.batchedArgs.size(); updateCounts = new int[nbrCommands]; for (int i = 0; i < nbrCommands; i++) updateCounts[i] = -3;  SQLException sqlEx = null; StatementImpl.CancelTask timeoutTask = null; try { if (locallyScopedConn.getEnableQueryTimeouts() && batchTimeout != 0 && locallyScopedConn.versionMeetsMinimum(5, 0, 0)) { timeoutTask = new StatementImpl.CancelTask(this, this); locallyScopedConn.getCancelTimer().schedule(timeoutTask, batchTimeout); }  if (this.retrieveGeneratedKeys) this.batchedGeneratedKeys = new ArrayList<ResultSetRow>(nbrCommands);  for (this.batchCommandIndex = 0; this.batchCommandIndex < nbrCommands; this.batchCommandIndex++) { Object arg = this.batchedArgs.get(this.batchCommandIndex); if (arg instanceof String) { updateCounts[this.batchCommandIndex] = executeUpdate((String)arg); } else { BatchParams paramArg = (BatchParams)arg; try { updateCounts[this.batchCommandIndex] = executeUpdate(paramArg.parameterStrings, paramArg.parameterStreams, paramArg.isStream, paramArg.streamLengths, paramArg.isNull, true); if (this.retrieveGeneratedKeys) ResultSet rs = null;  } catch (SQLException ex) { updateCounts[this.batchCommandIndex] = -3; if (this.continueBatchOnError && !(ex instanceof MySQLTimeoutException) && !(ex instanceof MySQLStatementCancelledException) && !hasDeadlockOrTimeoutRolledBackTx(ex)) { sqlEx = ex; } else { int[] newUpdateCounts = new int[this.batchCommandIndex]; System.arraycopy(updateCounts, 0, newUpdateCounts, 0, this.batchCommandIndex); SQLException batchUpdateException = new BatchUpdateException(ex.getMessage(), ex.getSQLState(), ex.getErrorCode(), newUpdateCounts); batchUpdateException.initCause(ex); throw batchUpdateException; }  }  }  }  if (sqlEx != null) { SQLException batchUpdateException = new BatchUpdateException(sqlEx.getMessage(), sqlEx.getSQLState(), sqlEx.getErrorCode(), updateCounts); batchUpdateException.initCause(sqlEx); throw batchUpdateException; }  } catch (NullPointerException npe) { try { checkClosed(); } catch (SQLException connectionClosedEx) { updateCounts[this.batchCommandIndex] = -3; int[] newUpdateCounts = new int[this.batchCommandIndex]; System.arraycopy(updateCounts, 0, newUpdateCounts, 0, this.batchCommandIndex); throw new BatchUpdateException(connectionClosedEx.getMessage(), connectionClosedEx.getSQLState(), connectionClosedEx.getErrorCode(), newUpdateCounts); }  throw npe; } finally { this.batchCommandIndex = -1; if (timeoutTask != null) { timeoutTask.cancel(); locallyScopedConn.getCancelTimer().purge(); }  resetCancelledState(); }  }  return (updateCounts != null) ? updateCounts : new int[0]; }  } public String getDateTime(String pattern) { SimpleDateFormat sdf = new SimpleDateFormat(pattern); return sdf.format(new Date()); } protected ResultSetInternalMethods executeInternal(int maxRowsToRetrieve, Buffer sendPacket, boolean createStreamingResultSet, boolean queryIsSelectOnly, Field[] metadataFromCache, boolean isBatch) throws SQLException { synchronized (checkClosed()) { ResultSetInternalMethods rs; resetCancelledState(); MySQLConnection locallyScopedConnection = this.connection; this.numberOfExecutions++; if (this.doPingInstead) { doPingInstead(); return this.results; }  StatementImpl.CancelTask timeoutTask = null; try { if (locallyScopedConnection.getEnableQueryTimeouts() && this.timeoutInMillis != 0 && locallyScopedConnection.versionMeetsMinimum(5, 0, 0)) { timeoutTask = new StatementImpl.CancelTask(this, this); locallyScopedConnection.getCancelTimer().schedule(timeoutTask, this.timeoutInMillis); }  if (!isBatch) statementBegins();  rs = locallyScopedConnection.execSQL(this, (String)null, maxRowsToRetrieve, sendPacket, this.resultSetType, this.resultSetConcurrency, createStreamingResultSet, this.currentCatalog, metadataFromCache, isBatch); if (timeoutTask != null) { timeoutTask.cancel(); locallyScopedConnection.getCancelTimer().purge(); if (timeoutTask.caughtWhileCancelling != null) throw timeoutTask.caughtWhileCancelling;  timeoutTask = null; }  synchronized (this.cancelTimeoutMutex) { if (this.wasCancelled) { MySQLStatementCancelledException mySQLStatementCancelledException; SQLException cause = null; if (this.wasCancelledByTimeout) { MySQLTimeoutException mySQLTimeoutException = new MySQLTimeoutException(); } else { mySQLStatementCancelledException = new MySQLStatementCancelledException(); }  resetCancelledState(); throw mySQLStatementCancelledException; }  }  } finally { if (!isBatch) this.statementExecuting.set(false);  if (timeoutTask != null) { timeoutTask.cancel(); locallyScopedConnection.getCancelTimer().purge(); }  }  return rs; }  } public ResultSet executeQuery() throws SQLException { synchronized (checkClosed()) { MySQLConnection locallyScopedConn = this.connection; checkForDml(this.originalSql, this.firstCharOfStmt); CachedResultSetMetaData cachedMetadata = null; clearWarnings(); boolean doStreaming = createStreamingResultSet(); this.batchedGeneratedKeys = null; if (doStreaming && this.connection.getNetTimeoutForStreamingResults() > 0) { Statement stmt = null; try { stmt = this.connection.createStatement(); ((StatementImpl)stmt).executeSimpleNonQuery(this.connection, "SET net_write_timeout=" + this.connection.getNetTimeoutForStreamingResults()); } finally { if (stmt != null) stmt.close();  }  }  Buffer sendPacket = fillSendPacket(); if (!this.connection.getHoldResultsOpenOverStatementClose() && !this.holdResultsOpenOverClose) { if (this.results != null) this.results.realClose(false);  if (this.generatedKeysResults != null) this.generatedKeysResults.realClose(false);  closeAllOpenResults(); }  String oldCatalog = null; if (!locallyScopedConn.getCatalog().equals(this.currentCatalog)) { oldCatalog = locallyScopedConn.getCatalog(); locallyScopedConn.setCatalog(this.currentCatalog); }  if (locallyScopedConn.getCacheResultSetMetadata()) cachedMetadata = locallyScopedConn.getCachedMetaData(this.originalSql);  Field[] metadataFromCache = null; if (cachedMetadata != null) metadataFromCache = cachedMetadata.fields;  if (locallyScopedConn.useMaxRows()) { if (this.hasLimitClause) { this.results = executeInternal(this.maxRows, sendPacket, createStreamingResultSet(), true, metadataFromCache, false); } else { if (this.maxRows <= 0) { executeSimpleNonQuery(locallyScopedConn, "SET SQL_SELECT_LIMIT=DEFAULT"); } else { executeSimpleNonQuery(locallyScopedConn, "SET SQL_SELECT_LIMIT=" + this.maxRows); }  this.results = executeInternal(-1, sendPacket, doStreaming, true, metadataFromCache, false); if (oldCatalog != null) this.connection.setCatalog(oldCatalog);  }  } else { this.results = executeInternal(-1, sendPacket, doStreaming, true, metadataFromCache, false); }  if (oldCatalog != null) locallyScopedConn.setCatalog(oldCatalog);  if (cachedMetadata != null) { locallyScopedConn.initializeResultsMetadataFromCache(this.originalSql, cachedMetadata, this.results); } else if (locallyScopedConn.getCacheResultSetMetadata()) { locallyScopedConn.initializeResultsMetadataFromCache(this.originalSql, (CachedResultSetMetaData)null, this.results); }  this.lastInsertId = this.results.getUpdateID(); return this.results; }  } public int executeUpdate() throws SQLException { return executeUpdate(true, false); } protected int executeUpdate(boolean clearBatchedGeneratedKeysAndWarnings, boolean isBatch) throws SQLException { synchronized (checkClosed()) { if (clearBatchedGeneratedKeysAndWarnings) { clearWarnings(); this.batchedGeneratedKeys = null; }  return executeUpdate(this.parameterValues, this.parameterStreams, this.isStream, this.streamLengths, this.isNull, isBatch); }  } protected int executeUpdate(byte[][] batchedParameterStrings, InputStream[] batchedParameterStreams, boolean[] batchedIsStream, int[] batchedStreamLengths, boolean[] batchedIsNull, boolean isReallyBatch) throws SQLException { synchronized (checkClosed()) { MySQLConnection locallyScopedConn = this.connection; if (locallyScopedConn.isReadOnly()) throw SQLError.createSQLException(Messages.getString("PreparedStatement.34") + Messages.getString("PreparedStatement.35"), "S1009", getExceptionInterceptor());  if (this.firstCharOfStmt == 'S' && isSelectQuery()) throw SQLError.createSQLException(Messages.getString("PreparedStatement.37"), "01S03", getExceptionInterceptor());  if (!locallyScopedConn.getHoldResultsOpenOverStatementClose()) { if (this.results != null) this.results.realClose(false);  if (this.generatedKeysResults != null) this.generatedKeysResults.realClose(false);  closeAllOpenResults(); }  ResultSetInternalMethods rs = null; Buffer sendPacket = fillSendPacket(batchedParameterStrings, batchedParameterStreams, batchedIsStream, batchedStreamLengths); String oldCatalog = null; if (!locallyScopedConn.getCatalog().equals(this.currentCatalog)) { oldCatalog = locallyScopedConn.getCatalog(); locallyScopedConn.setCatalog(this.currentCatalog); }  if (locallyScopedConn.useMaxRows()) executeSimpleNonQuery(locallyScopedConn, "SET SQL_SELECT_LIMIT=DEFAULT");  boolean oldInfoMsgState = false; if (this.retrieveGeneratedKeys) { oldInfoMsgState = locallyScopedConn.isReadInfoMsgEnabled(); locallyScopedConn.setReadInfoMsgEnabled(true); }  rs = executeInternal(-1, sendPacket, false, false, (Field[])null, isReallyBatch); if (this.retrieveGeneratedKeys) { locallyScopedConn.setReadInfoMsgEnabled(oldInfoMsgState); rs.setFirstCharOfQuery(this.firstCharOfStmt); }  if (oldCatalog != null) locallyScopedConn.setCatalog(oldCatalog);  this.results = rs; this.updateCount = rs.getUpdateCount(); if (containsOnDuplicateKeyUpdateInSQL() && this.compensateForOnDuplicateKeyUpdate) if (this.updateCount == 2L || this.updateCount == 0L) this.updateCount = 1L;   int truncatedUpdateCount = 0; if (this.updateCount > 2147483647L) { truncatedUpdateCount = Integer.MAX_VALUE; } else { truncatedUpdateCount = (int)this.updateCount; }  this.lastInsertId = rs.getUpdateID(); return truncatedUpdateCount; }  } protected boolean containsOnDuplicateKeyUpdateInSQL() { return this.parseInfo.isOnDuplicateKeyUpdate; } protected Buffer fillSendPacket() throws SQLException { synchronized (checkClosed()) { return fillSendPacket(this.parameterValues, this.parameterStreams, this.isStream, this.streamLengths); }  } protected Buffer fillSendPacket(byte[][] batchedParameterStrings, InputStream[] batchedParameterStreams, boolean[] batchedIsStream, int[] batchedStreamLengths) throws SQLException { synchronized (checkClosed()) { Buffer sendPacket = this.connection.getIO().getSharedSendPacket(); sendPacket.clear(); sendPacket.writeByte((byte)3); boolean useStreamLengths = this.connection.getUseStreamLengthsInPrepStmts(); int ensurePacketSize = 0; String statementComment = this.connection.getStatementComment(); byte[] commentAsBytes = null; if (statementComment != null) { if (this.charConverter != null) { commentAsBytes = this.charConverter.toBytes(statementComment); } else { commentAsBytes = StringUtils.getBytes(statementComment, this.charConverter, this.charEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), getExceptionInterceptor()); }  ensurePacketSize += commentAsBytes.length; ensurePacketSize += 6; }  int i; for (i = 0; i < batchedParameterStrings.length; i++) { if (batchedIsStream[i] && useStreamLengths) ensurePacketSize += batchedStreamLengths[i];  }  if (ensurePacketSize != 0) sendPacket.ensureCapacity(ensurePacketSize);  if (commentAsBytes != null) { sendPacket.writeBytesNoNull(Constants.SLASH_STAR_SPACE_AS_BYTES); sendPacket.writeBytesNoNull(commentAsBytes); sendPacket.writeBytesNoNull(Constants.SPACE_STAR_SLASH_SPACE_AS_BYTES); }  for (i = 0; i < batchedParameterStrings.length; i++) { checkAllParametersSet(batchedParameterStrings[i], batchedParameterStreams[i], i); sendPacket.writeBytesNoNull(this.staticSqlStrings[i]); if (batchedIsStream[i]) { streamToBytes(sendPacket, batchedParameterStreams[i], true, batchedStreamLengths[i], useStreamLengths); } else { sendPacket.writeBytesNoNull(batchedParameterStrings[i]); }  }  sendPacket.writeBytesNoNull(this.staticSqlStrings[batchedParameterStrings.length]); return sendPacket; }  } private void checkAllParametersSet(byte[] parameterString, InputStream parameterStream, int columnIndex) throws SQLException { if (parameterString == null && parameterStream == null) throw SQLError.createSQLException(Messages.getString("PreparedStatement.40") + (columnIndex + 1), "07001", getExceptionInterceptor());  } protected PreparedStatement prepareBatchedInsertSQL(MySQLConnection localConn, int numBatches) throws SQLException { synchronized (checkClosed()) { PreparedStatement pstmt = new PreparedStatement(localConn, "Rewritten batch of: " + this.originalSql, this.currentCatalog, this.parseInfo.getParseInfoForBatch(numBatches)); pstmt.setRetrieveGeneratedKeys(this.retrieveGeneratedKeys); pstmt.rewrittenBatchSize = numBatches; return pstmt; }  } protected void setRetrieveGeneratedKeys(boolean flag) throws SQLException { synchronized (checkClosed()) { this.retrieveGeneratedKeys = flag; }  } public String getNonRewrittenSql() throws SQLException { synchronized (checkClosed()) {
/* 2644 */       int indexOfBatch = this.originalSql.indexOf(" of: ");
/*      */       
/* 2646 */       if (indexOfBatch != -1) {
/* 2647 */         return this.originalSql.substring(indexOfBatch + 5);
/*      */       }
/*      */       
/* 2650 */       return this.originalSql;
/*      */     }  }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public byte[] getBytesRepresentation(int parameterIndex) throws SQLException {
/* 2668 */     synchronized (checkClosed()) {
/* 2669 */       if (this.isStream[parameterIndex]) {
/* 2670 */         return streamToBytes(this.parameterStreams[parameterIndex], false, this.streamLengths[parameterIndex], this.connection.getUseStreamLengthsInPrepStmts());
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 2675 */       byte[] parameterVal = this.parameterValues[parameterIndex];
/*      */       
/* 2677 */       if (parameterVal == null) {
/* 2678 */         return null;
/*      */       }
/*      */       
/* 2681 */       if (parameterVal[0] == 39 && parameterVal[parameterVal.length - 1] == 39) {
/*      */         
/* 2683 */         byte[] valNoQuotes = new byte[parameterVal.length - 2];
/* 2684 */         System.arraycopy(parameterVal, 1, valNoQuotes, 0, parameterVal.length - 2);
/*      */ 
/*      */         
/* 2687 */         return valNoQuotes;
/*      */       } 
/*      */       
/* 2690 */       return parameterVal;
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
/*      */   protected byte[] getBytesRepresentationForBatch(int parameterIndex, int commandIndex) throws SQLException {
/* 2703 */     synchronized (checkClosed()) {
/* 2704 */       Object batchedArg = this.batchedArgs.get(commandIndex);
/* 2705 */       if (batchedArg instanceof String) {
/*      */         try {
/* 2707 */           return StringUtils.getBytes((String)batchedArg, this.charEncoding);
/*      */         }
/* 2709 */         catch (UnsupportedEncodingException uue) {
/* 2710 */           throw new RuntimeException(Messages.getString("PreparedStatement.32") + this.charEncoding + Messages.getString("PreparedStatement.33"));
/*      */         } 
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2717 */       BatchParams params = (BatchParams)batchedArg;
/* 2718 */       if (params.isStream[parameterIndex]) {
/* 2719 */         return streamToBytes(params.parameterStreams[parameterIndex], false, params.streamLengths[parameterIndex], this.connection.getUseStreamLengthsInPrepStmts());
/*      */       }
/*      */       
/* 2722 */       byte[] parameterVal = params.parameterStrings[parameterIndex];
/* 2723 */       if (parameterVal == null) {
/* 2724 */         return null;
/*      */       }
/* 2726 */       if (parameterVal[0] == 39 && parameterVal[parameterVal.length - 1] == 39) {
/*      */         
/* 2728 */         byte[] valNoQuotes = new byte[parameterVal.length - 2];
/* 2729 */         System.arraycopy(parameterVal, 1, valNoQuotes, 0, parameterVal.length - 2);
/*      */ 
/*      */         
/* 2732 */         return valNoQuotes;
/*      */       } 
/*      */       
/* 2735 */       return parameterVal;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final String getDateTimePattern(String dt, boolean toTime) throws Exception {
/* 2746 */     int dtLength = (dt != null) ? dt.length() : 0;
/*      */     
/* 2748 */     if (dtLength >= 8 && dtLength <= 10) {
/* 2749 */       int dashCount = 0;
/* 2750 */       boolean isDateOnly = true;
/*      */       
/* 2752 */       for (int k = 0; k < dtLength; k++) {
/* 2753 */         char c = dt.charAt(k);
/*      */         
/* 2755 */         if (!Character.isDigit(c) && c != '-') {
/* 2756 */           isDateOnly = false;
/*      */           
/*      */           break;
/*      */         } 
/*      */         
/* 2761 */         if (c == '-') {
/* 2762 */           dashCount++;
/*      */         }
/*      */       } 
/*      */       
/* 2766 */       if (isDateOnly && dashCount == 2) {
/* 2767 */         return "yyyy-MM-dd";
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2774 */     boolean colonsOnly = true;
/*      */     
/* 2776 */     for (int i = 0; i < dtLength; i++) {
/* 2777 */       char c = dt.charAt(i);
/*      */       
/* 2779 */       if (!Character.isDigit(c) && c != ':') {
/* 2780 */         colonsOnly = false;
/*      */         
/*      */         break;
/*      */       } 
/*      */     } 
/*      */     
/* 2786 */     if (colonsOnly) {
/* 2787 */       return "HH:mm:ss";
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2796 */     StringReader reader = new StringReader(dt + " ");
/* 2797 */     ArrayList<Object[]> vec = new ArrayList();
/* 2798 */     ArrayList<Object[]> vecRemovelist = new ArrayList();
/* 2799 */     Object[] nv = new Object[3];
/*      */     
/* 2801 */     nv[0] = Character.valueOf('y');
/* 2802 */     nv[1] = new StringBuffer();
/* 2803 */     nv[2] = Integer.valueOf(0);
/* 2804 */     vec.add(nv);
/*      */     
/* 2806 */     if (toTime) {
/* 2807 */       nv = new Object[3];
/* 2808 */       nv[0] = Character.valueOf('h');
/* 2809 */       nv[1] = new StringBuffer();
/* 2810 */       nv[2] = Integer.valueOf(0);
/* 2811 */       vec.add(nv);
/*      */     } 
/*      */     int z;
/* 2814 */     while ((z = reader.read()) != -1) {
/* 2815 */       char separator = (char)z;
/* 2816 */       int maxvecs = vec.size();
/*      */       
/* 2818 */       for (int count = 0; count < maxvecs; count++) {
/* 2819 */         Object[] arrayOfObject = vec.get(count);
/* 2820 */         int n = ((Integer)arrayOfObject[2]).intValue();
/* 2821 */         char c = getSuccessor(((Character)arrayOfObject[0]).charValue(), n);
/*      */         
/* 2823 */         if (!Character.isLetterOrDigit(separator)) {
/* 2824 */           if (c == ((Character)arrayOfObject[0]).charValue() && c != 'S') {
/* 2825 */             vecRemovelist.add(arrayOfObject);
/*      */           } else {
/* 2827 */             ((StringBuffer)arrayOfObject[1]).append(separator);
/*      */             
/* 2829 */             if (c == 'X' || c == 'Y') {
/* 2830 */               arrayOfObject[2] = Integer.valueOf(4);
/*      */             }
/*      */           } 
/*      */         } else {
/* 2834 */           if (c == 'X') {
/* 2835 */             c = 'y';
/* 2836 */             nv = new Object[3];
/* 2837 */             nv[1] = (new StringBuffer(((StringBuffer)arrayOfObject[1]).toString())).append('M');
/*      */             
/* 2839 */             nv[0] = Character.valueOf('M');
/* 2840 */             nv[2] = Integer.valueOf(1);
/* 2841 */             vec.add(nv);
/* 2842 */           } else if (c == 'Y') {
/* 2843 */             c = 'M';
/* 2844 */             nv = new Object[3];
/* 2845 */             nv[1] = (new StringBuffer(((StringBuffer)arrayOfObject[1]).toString())).append('d');
/*      */             
/* 2847 */             nv[0] = Character.valueOf('d');
/* 2848 */             nv[2] = Integer.valueOf(1);
/* 2849 */             vec.add(nv);
/*      */           } 
/*      */           
/* 2852 */           ((StringBuffer)arrayOfObject[1]).append(c);
/*      */           
/* 2854 */           if (c == ((Character)arrayOfObject[0]).charValue()) {
/* 2855 */             arrayOfObject[2] = Integer.valueOf(n + 1);
/*      */           } else {
/* 2857 */             arrayOfObject[0] = Character.valueOf(c);
/* 2858 */             arrayOfObject[2] = Integer.valueOf(1);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/* 2863 */       int k = vecRemovelist.size();
/*      */       
/* 2865 */       for (int m = 0; m < k; m++) {
/* 2866 */         Object[] arrayOfObject = vecRemovelist.get(m);
/* 2867 */         vec.remove(arrayOfObject);
/*      */       } 
/*      */       
/* 2870 */       vecRemovelist.clear();
/*      */     } 
/*      */     
/* 2873 */     int size = vec.size();
/*      */     int j;
/* 2875 */     for (j = 0; j < size; j++) {
/* 2876 */       Object[] arrayOfObject = vec.get(j);
/* 2877 */       char c = ((Character)arrayOfObject[0]).charValue();
/* 2878 */       int n = ((Integer)arrayOfObject[2]).intValue();
/*      */       
/* 2880 */       boolean bk = (getSuccessor(c, n) != c);
/* 2881 */       boolean atEnd = ((c == 's' || c == 'm' || (c == 'h' && toTime)) && bk);
/* 2882 */       boolean finishesAtDate = (bk && c == 'd' && !toTime);
/* 2883 */       boolean containsEnd = (((StringBuffer)arrayOfObject[1]).toString().indexOf('W') != -1);
/*      */ 
/*      */       
/* 2886 */       if ((!atEnd && !finishesAtDate) || containsEnd) {
/* 2887 */         vecRemovelist.add(arrayOfObject);
/*      */       }
/*      */     } 
/*      */     
/* 2891 */     size = vecRemovelist.size();
/*      */     
/* 2893 */     for (j = 0; j < size; j++) {
/* 2894 */       vec.remove(vecRemovelist.get(j));
/*      */     }
/*      */     
/* 2897 */     vecRemovelist.clear();
/* 2898 */     Object[] v = vec.get(0);
/*      */     
/* 2900 */     StringBuffer format = (StringBuffer)v[1];
/* 2901 */     format.setLength(format.length() - 1);
/*      */     
/* 2903 */     return format.toString();
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
/*      */   public ResultSetMetaData getMetaData() throws SQLException {
/* 2918 */     synchronized (checkClosed()) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2930 */       if (!isSelectQuery()) {
/* 2931 */         return null;
/*      */       }
/*      */       
/* 2934 */       PreparedStatement mdStmt = null;
/* 2935 */       ResultSet mdRs = null;
/*      */       
/* 2937 */       if (this.pstmtResultMetaData == null) {
/*      */         try {
/* 2939 */           mdStmt = new PreparedStatement(this.connection, this.originalSql, this.currentCatalog, this.parseInfo);
/*      */ 
/*      */           
/* 2942 */           mdStmt.setMaxRows(1);
/*      */           
/* 2944 */           int paramCount = this.parameterValues.length;
/*      */           
/* 2946 */           for (int i = 1; i <= paramCount; i++) {
/* 2947 */             mdStmt.setString(i, "");
/*      */           }
/*      */           
/* 2950 */           boolean hadResults = mdStmt.execute();
/*      */           
/* 2952 */           if (hadResults) {
/* 2953 */             mdRs = mdStmt.getResultSet();
/*      */             
/* 2955 */             this.pstmtResultMetaData = mdRs.getMetaData();
/*      */           } else {
/* 2957 */             this.pstmtResultMetaData = new ResultSetMetaData(new Field[0], this.connection.getUseOldAliasMetadataBehavior(), getExceptionInterceptor());
/*      */           }
/*      */         
/*      */         } finally {
/*      */           
/* 2962 */           SQLException sqlExRethrow = null;
/*      */           
/* 2964 */           if (mdRs != null) {
/*      */             try {
/* 2966 */               mdRs.close();
/* 2967 */             } catch (SQLException sqlEx) {
/* 2968 */               sqlExRethrow = sqlEx;
/*      */             } 
/*      */             
/* 2971 */             mdRs = null;
/*      */           } 
/*      */           
/* 2974 */           if (mdStmt != null) {
/*      */             try {
/* 2976 */               mdStmt.close();
/* 2977 */             } catch (SQLException sqlEx) {
/* 2978 */               sqlExRethrow = sqlEx;
/*      */             } 
/*      */             
/* 2981 */             mdStmt = null;
/*      */           } 
/*      */           
/* 2984 */           if (sqlExRethrow != null) {
/* 2985 */             throw sqlExRethrow;
/*      */           }
/*      */         } 
/*      */       }
/*      */       
/* 2990 */       return this.pstmtResultMetaData;
/*      */     } 
/*      */   }
/*      */   
/*      */   protected boolean isSelectQuery() throws SQLException {
/* 2995 */     synchronized (checkClosed()) {
/* 2996 */       return StringUtils.startsWithIgnoreCaseAndWs(StringUtils.stripComments(this.originalSql, "'\"", "'\"", true, false, true, true), "SELECT");
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
/*      */   public ParameterMetaData getParameterMetaData() throws SQLException {
/* 3008 */     synchronized (checkClosed()) {
/* 3009 */       if (this.parameterMetaData == null) {
/* 3010 */         if (this.connection.getGenerateSimpleParameterMetadata()) {
/* 3011 */           this.parameterMetaData = new MysqlParameterMetadata(this.parameterCount);
/*      */         } else {
/* 3013 */           this.parameterMetaData = new MysqlParameterMetadata(null, this.parameterCount, getExceptionInterceptor());
/*      */         } 
/*      */       }
/*      */ 
/*      */       
/* 3018 */       return this.parameterMetaData;
/*      */     } 
/*      */   }
/*      */   
/*      */   ParseInfo getParseInfo() {
/* 3023 */     return this.parseInfo;
/*      */   }
/*      */   
/*      */   private final char getSuccessor(char c, int n) {
/* 3027 */     return (c == 'y' && n == 2) ? 'X' : ((c == 'y' && n < 4) ? 'y' : ((c == 'y') ? 'M' : ((c == 'M' && n == 2) ? 'Y' : ((c == 'M' && n < 3) ? 'M' : ((c == 'M') ? 'd' : ((c == 'd' && n < 2) ? 'd' : ((c == 'd') ? 'H' : ((c == 'H' && n < 2) ? 'H' : ((c == 'H') ? 'm' : ((c == 'm' && n < 2) ? 'm' : ((c == 'm') ? 's' : ((c == 's' && n < 2) ? 's' : 'W'))))))))))));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final void hexEscapeBlock(byte[] buf, Buffer packet, int size) throws SQLException {
/* 3053 */     for (int i = 0; i < size; i++) {
/* 3054 */       byte b = buf[i];
/* 3055 */       int lowBits = (b & 0xFF) / 16;
/* 3056 */       int highBits = (b & 0xFF) % 16;
/*      */       
/* 3058 */       packet.writeByte(HEX_DIGITS[lowBits]);
/* 3059 */       packet.writeByte(HEX_DIGITS[highBits]);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void initializeFromParseInfo() throws SQLException {
/* 3064 */     synchronized (checkClosed()) {
/* 3065 */       this.staticSqlStrings = this.parseInfo.staticSql;
/* 3066 */       this.hasLimitClause = this.parseInfo.foundLimitClause;
/* 3067 */       this.isLoadDataQuery = this.parseInfo.foundLoadData;
/* 3068 */       this.firstCharOfStmt = this.parseInfo.firstStmtChar;
/*      */       
/* 3070 */       this.parameterCount = this.staticSqlStrings.length - 1;
/*      */       
/* 3072 */       this.parameterValues = new byte[this.parameterCount][];
/* 3073 */       this.parameterStreams = new InputStream[this.parameterCount];
/* 3074 */       this.isStream = new boolean[this.parameterCount];
/* 3075 */       this.streamLengths = new int[this.parameterCount];
/* 3076 */       this.isNull = new boolean[this.parameterCount];
/* 3077 */       this.parameterTypes = new int[this.parameterCount];
/*      */       
/* 3079 */       clearParameters();
/*      */       
/* 3081 */       for (int j = 0; j < this.parameterCount; j++) {
/* 3082 */         this.isStream[j] = false;
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   boolean isNull(int paramIndex) throws SQLException {
/* 3088 */     synchronized (checkClosed()) {
/* 3089 */       return this.isNull[paramIndex];
/*      */     } 
/*      */   }
/*      */   
/*      */   private final int readblock(InputStream i, byte[] b) throws SQLException {
/*      */     try {
/* 3095 */       return i.read(b);
/* 3096 */     } catch (Throwable ex) {
/* 3097 */       SQLException sqlEx = SQLError.createSQLException(Messages.getString("PreparedStatement.56") + ex.getClass().getName(), "S1000", getExceptionInterceptor());
/*      */       
/* 3099 */       sqlEx.initCause(ex);
/*      */       
/* 3101 */       throw sqlEx;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private final int readblock(InputStream i, byte[] b, int length) throws SQLException {
/*      */     try {
/* 3108 */       int lengthToRead = length;
/*      */       
/* 3110 */       if (lengthToRead > b.length) {
/* 3111 */         lengthToRead = b.length;
/*      */       }
/*      */       
/* 3114 */       return i.read(b, 0, lengthToRead);
/* 3115 */     } catch (Throwable ex) {
/* 3116 */       SQLException sqlEx = SQLError.createSQLException(Messages.getString("PreparedStatement.56") + ex.getClass().getName(), "S1000", getExceptionInterceptor());
/*      */       
/* 3118 */       sqlEx.initCause(ex);
/*      */       
/* 3120 */       throw sqlEx;
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
/*      */   protected void realClose(boolean calledExplicitly, boolean closeOpenResults) throws SQLException {
/*      */     MySQLConnection locallyScopedConn;
/*      */     try {
/* 3138 */       locallyScopedConn = checkClosed();
/* 3139 */     } catch (SQLException sqlEx) {
/*      */       return;
/*      */     } 
/*      */     
/* 3143 */     synchronized (locallyScopedConn) {
/*      */       
/* 3145 */       if (this.useUsageAdvisor && 
/* 3146 */         this.numberOfExecutions <= 1) {
/* 3147 */         String message = Messages.getString("PreparedStatement.43");
/*      */         
/* 3149 */         this.eventSink.consumeEvent(new ProfilerEvent((byte)0, "", this.currentCatalog, this.connectionId, getId(), -1, System.currentTimeMillis(), 0L, Constants.MILLIS_I18N, null, this.pointOfOrigin, message));
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3158 */       super.realClose(calledExplicitly, closeOpenResults);
/*      */       
/* 3160 */       this.dbmd = null;
/* 3161 */       this.originalSql = null;
/* 3162 */       this.staticSqlStrings = (byte[][])null;
/* 3163 */       this.parameterValues = (byte[][])null;
/* 3164 */       this.parameterStreams = null;
/* 3165 */       this.isStream = null;
/* 3166 */       this.streamLengths = null;
/* 3167 */       this.isNull = null;
/* 3168 */       this.streamConvertBuf = null;
/* 3169 */       this.parameterTypes = null;
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
/*      */   public void setArray(int i, Array x) throws SQLException {
/* 3187 */     throw SQLError.notImplemented();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
/* 3214 */     if (x == null) {
/* 3215 */       setNull(parameterIndex, 12);
/*      */     } else {
/* 3217 */       setBinaryStream(parameterIndex, x, length);
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
/*      */   public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
/* 3235 */     if (x == null) {
/* 3236 */       setNull(parameterIndex, 3);
/*      */     } else {
/* 3238 */       setInternal(parameterIndex, StringUtils.fixDecimalExponent(StringUtils.consistentToString(x)));
/*      */ 
/*      */       
/* 3241 */       this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = 3;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
/* 3267 */     synchronized (checkClosed()) {
/* 3268 */       if (x == null) {
/* 3269 */         setNull(parameterIndex, -2);
/*      */       } else {
/* 3271 */         int parameterIndexOffset = getParameterIndexOffset();
/*      */         
/* 3273 */         if (parameterIndex < 1 || parameterIndex > this.staticSqlStrings.length)
/*      */         {
/* 3275 */           throw SQLError.createSQLException(Messages.getString("PreparedStatement.2") + parameterIndex + Messages.getString("PreparedStatement.3") + this.staticSqlStrings.length + Messages.getString("PreparedStatement.4"), "S1009", getExceptionInterceptor());
/*      */         }
/*      */ 
/*      */ 
/*      */         
/* 3280 */         if (parameterIndexOffset == -1 && parameterIndex == 1) {
/* 3281 */           throw SQLError.createSQLException("Can't set IN parameter for return value of stored function call.", "S1009", getExceptionInterceptor());
/*      */         }
/*      */ 
/*      */ 
/*      */         
/* 3286 */         this.parameterStreams[parameterIndex - 1 + parameterIndexOffset] = x;
/* 3287 */         this.isStream[parameterIndex - 1 + parameterIndexOffset] = true;
/* 3288 */         this.streamLengths[parameterIndex - 1 + parameterIndexOffset] = length;
/* 3289 */         this.isNull[parameterIndex - 1 + parameterIndexOffset] = false;
/* 3290 */         this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = 2004;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
/* 3297 */     setBinaryStream(parameterIndex, inputStream, (int)length);
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
/*      */   public void setBlob(int i, Blob x) throws SQLException {
/* 3312 */     if (x == null) {
/* 3313 */       setNull(i, 2004);
/*      */     } else {
/* 3315 */       ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
/*      */       
/* 3317 */       bytesOut.write(39);
/* 3318 */       escapeblockFast(x.getBytes(1L, (int)x.length()), bytesOut, (int)x.length());
/*      */       
/* 3320 */       bytesOut.write(39);
/*      */       
/* 3322 */       setInternal(i, bytesOut.toByteArray());
/*      */       
/* 3324 */       this.parameterTypes[i - 1 + getParameterIndexOffset()] = 2004;
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
/*      */   public void setBoolean(int parameterIndex, boolean x) throws SQLException {
/* 3341 */     if (this.useTrueBoolean) {
/* 3342 */       setInternal(parameterIndex, x ? "1" : "0");
/*      */     } else {
/* 3344 */       setInternal(parameterIndex, x ? "'t'" : "'f'");
/*      */       
/* 3346 */       this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = 16;
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
/*      */   public void setByte(int parameterIndex, byte x) throws SQLException {
/* 3363 */     setInternal(parameterIndex, String.valueOf(x));
/*      */     
/* 3365 */     this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = -6;
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
/*      */   public void setBytes(int parameterIndex, byte[] x) throws SQLException {
/* 3382 */     setBytes(parameterIndex, x, true, true);
/*      */     
/* 3384 */     if (x != null) {
/* 3385 */       this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = -2;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setBytes(int parameterIndex, byte[] x, boolean checkForIntroducer, boolean escapeForMBChars) throws SQLException {
/* 3392 */     synchronized (checkClosed()) {
/* 3393 */       if (x == null) {
/* 3394 */         setNull(parameterIndex, -2);
/*      */       } else {
/* 3396 */         String connectionEncoding = this.connection.getEncoding();
/*      */         
/*      */         try {
/* 3399 */           if (this.connection.isNoBackslashEscapesSet() || (escapeForMBChars && this.connection.getUseUnicode() && connectionEncoding != null && CharsetMapping.isMultibyteCharset(connectionEncoding))) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 3407 */             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(x.length * 2 + 3);
/*      */             
/* 3409 */             byteArrayOutputStream.write(120);
/* 3410 */             byteArrayOutputStream.write(39);
/*      */             
/* 3412 */             for (int j = 0; j < x.length; j++) {
/* 3413 */               int lowBits = (x[j] & 0xFF) / 16;
/* 3414 */               int highBits = (x[j] & 0xFF) % 16;
/*      */               
/* 3416 */               byteArrayOutputStream.write(HEX_DIGITS[lowBits]);
/* 3417 */               byteArrayOutputStream.write(HEX_DIGITS[highBits]);
/*      */             } 
/*      */             
/* 3420 */             byteArrayOutputStream.write(39);
/*      */             
/* 3422 */             setInternal(parameterIndex, byteArrayOutputStream.toByteArray());
/*      */             
/*      */             return;
/*      */           } 
/* 3426 */         } catch (SQLException ex) {
/* 3427 */           throw ex;
/* 3428 */         } catch (RuntimeException ex) {
/* 3429 */           SQLException sqlEx = SQLError.createSQLException(ex.toString(), "S1009", (ExceptionInterceptor)null);
/* 3430 */           sqlEx.initCause(ex);
/* 3431 */           throw sqlEx;
/*      */         } 
/*      */ 
/*      */         
/* 3435 */         int numBytes = x.length;
/*      */         
/* 3437 */         int pad = 2;
/*      */         
/* 3439 */         boolean needsIntroducer = (checkForIntroducer && this.connection.versionMeetsMinimum(4, 1, 0));
/*      */ 
/*      */         
/* 3442 */         if (needsIntroducer) {
/* 3443 */           pad += 7;
/*      */         }
/*      */         
/* 3446 */         ByteArrayOutputStream bOut = new ByteArrayOutputStream(numBytes + pad);
/*      */ 
/*      */         
/* 3449 */         if (needsIntroducer) {
/* 3450 */           bOut.write(95);
/* 3451 */           bOut.write(98);
/* 3452 */           bOut.write(105);
/* 3453 */           bOut.write(110);
/* 3454 */           bOut.write(97);
/* 3455 */           bOut.write(114);
/* 3456 */           bOut.write(121);
/*      */         } 
/* 3458 */         bOut.write(39);
/*      */         
/* 3460 */         for (int i = 0; i < numBytes; i++) {
/* 3461 */           byte b = x[i];
/*      */           
/* 3463 */           switch (b) {
/*      */             case 0:
/* 3465 */               bOut.write(92);
/* 3466 */               bOut.write(48);
/*      */               break;
/*      */ 
/*      */             
/*      */             case 10:
/* 3471 */               bOut.write(92);
/* 3472 */               bOut.write(110);
/*      */               break;
/*      */ 
/*      */             
/*      */             case 13:
/* 3477 */               bOut.write(92);
/* 3478 */               bOut.write(114);
/*      */               break;
/*      */ 
/*      */             
/*      */             case 92:
/* 3483 */               bOut.write(92);
/* 3484 */               bOut.write(92);
/*      */               break;
/*      */ 
/*      */             
/*      */             case 39:
/* 3489 */               bOut.write(92);
/* 3490 */               bOut.write(39);
/*      */               break;
/*      */ 
/*      */             
/*      */             case 34:
/* 3495 */               bOut.write(92);
/* 3496 */               bOut.write(34);
/*      */               break;
/*      */ 
/*      */             
/*      */             case 26:
/* 3501 */               bOut.write(92);
/* 3502 */               bOut.write(90);
/*      */               break;
/*      */ 
/*      */             
/*      */             default:
/* 3507 */               bOut.write(b);
/*      */               break;
/*      */           } 
/*      */         } 
/* 3511 */         bOut.write(39);
/*      */         
/* 3513 */         setInternal(parameterIndex, bOut.toByteArray());
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
/*      */ 
/*      */   
/*      */   protected void setBytesNoEscape(int parameterIndex, byte[] parameterAsBytes) throws SQLException {
/* 3532 */     byte[] parameterWithQuotes = new byte[parameterAsBytes.length + 2];
/* 3533 */     parameterWithQuotes[0] = 39;
/* 3534 */     System.arraycopy(parameterAsBytes, 0, parameterWithQuotes, 1, parameterAsBytes.length);
/*      */     
/* 3536 */     parameterWithQuotes[parameterAsBytes.length + 1] = 39;
/*      */     
/* 3538 */     setInternal(parameterIndex, parameterWithQuotes);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void setBytesNoEscapeNoQuotes(int parameterIndex, byte[] parameterAsBytes) throws SQLException {
/* 3543 */     setInternal(parameterIndex, parameterAsBytes);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
/* 3570 */     synchronized (checkClosed()) {
/*      */       try {
/* 3572 */         if (reader == null) {
/* 3573 */           setNull(parameterIndex, -1);
/*      */         } else {
/* 3575 */           char[] c = null;
/* 3576 */           int len = 0;
/*      */           
/* 3578 */           boolean useLength = this.connection.getUseStreamLengthsInPrepStmts();
/*      */ 
/*      */           
/* 3581 */           String forcedEncoding = this.connection.getClobCharacterEncoding();
/*      */           
/* 3583 */           if (useLength && length != -1) {
/* 3584 */             c = new char[length];
/*      */             
/* 3586 */             int numCharsRead = readFully(reader, c, length);
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 3591 */             if (forcedEncoding == null) {
/* 3592 */               setString(parameterIndex, new String(c, 0, numCharsRead));
/*      */             } else {
/*      */               try {
/* 3595 */                 setBytes(parameterIndex, StringUtils.getBytes(new String(c, 0, numCharsRead), forcedEncoding));
/*      */               
/*      */               }
/* 3598 */               catch (UnsupportedEncodingException uee) {
/* 3599 */                 throw SQLError.createSQLException("Unsupported character encoding " + forcedEncoding, "S1009", getExceptionInterceptor());
/*      */               } 
/*      */             } 
/*      */           } else {
/*      */             
/* 3604 */             c = new char[4096];
/*      */             
/* 3606 */             StringBuffer buf = new StringBuffer();
/*      */             
/* 3608 */             while ((len = reader.read(c)) != -1) {
/* 3609 */               buf.append(c, 0, len);
/*      */             }
/*      */             
/* 3612 */             if (forcedEncoding == null) {
/* 3613 */               setString(parameterIndex, buf.toString());
/*      */             } else {
/*      */               try {
/* 3616 */                 setBytes(parameterIndex, StringUtils.getBytes(buf.toString(), forcedEncoding));
/*      */               }
/* 3618 */               catch (UnsupportedEncodingException uee) {
/* 3619 */                 throw SQLError.createSQLException("Unsupported character encoding " + forcedEncoding, "S1009", getExceptionInterceptor());
/*      */               } 
/*      */             } 
/*      */           } 
/*      */ 
/*      */           
/* 3625 */           this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = 2005;
/*      */         } 
/* 3627 */       } catch (IOException ioEx) {
/* 3628 */         throw SQLError.createSQLException(ioEx.toString(), "S1000", getExceptionInterceptor());
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
/*      */   
/*      */   public void setClob(int i, Clob x) throws SQLException {
/* 3646 */     synchronized (checkClosed()) {
/* 3647 */       if (x == null) {
/* 3648 */         setNull(i, 2005);
/*      */       } else {
/*      */         
/* 3651 */         String forcedEncoding = this.connection.getClobCharacterEncoding();
/*      */         
/* 3653 */         if (forcedEncoding == null) {
/* 3654 */           setString(i, x.getSubString(1L, (int)x.length()));
/*      */         } else {
/*      */           try {
/* 3657 */             setBytes(i, StringUtils.getBytes(x.getSubString(1L, (int)x.length()), forcedEncoding));
/*      */           }
/* 3659 */           catch (UnsupportedEncodingException uee) {
/* 3660 */             throw SQLError.createSQLException("Unsupported character encoding " + forcedEncoding, "S1009", getExceptionInterceptor());
/*      */           } 
/*      */         } 
/*      */ 
/*      */         
/* 3665 */         this.parameterTypes[i - 1 + getParameterIndexOffset()] = 2005;
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
/*      */ 
/*      */   
/*      */   public void setDate(int parameterIndex, Date x) throws SQLException {
/* 3684 */     setDate(parameterIndex, x, (Calendar)null);
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
/*      */   public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
/* 3703 */     if (x == null) {
/* 3704 */       setNull(parameterIndex, 91);
/*      */     } else {
/* 3706 */       checkClosed();
/*      */       
/* 3708 */       if (!this.useLegacyDatetimeCode) {
/* 3709 */         newSetDateInternal(parameterIndex, x, cal);
/*      */       }
/*      */       else {
/*      */         
/* 3713 */         SimpleDateFormat dateFormatter = new SimpleDateFormat("''yyyy-MM-dd''", Locale.US);
/*      */         
/* 3715 */         setInternal(parameterIndex, dateFormatter.format(x));
/*      */         
/* 3717 */         this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = 91;
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
/*      */   
/*      */   public void setDouble(int parameterIndex, double x) throws SQLException {
/* 3735 */     synchronized (checkClosed()) {
/* 3736 */       if (!this.connection.getAllowNanAndInf() && (x == Double.POSITIVE_INFINITY || x == Double.NEGATIVE_INFINITY || Double.isNaN(x)))
/*      */       {
/*      */         
/* 3739 */         throw SQLError.createSQLException("'" + x + "' is not a valid numeric or approximate numeric value", "S1009", getExceptionInterceptor());
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3745 */       setInternal(parameterIndex, StringUtils.fixDecimalExponent(String.valueOf(x)));
/*      */ 
/*      */       
/* 3748 */       this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = 8;
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
/*      */   public void setFloat(int parameterIndex, float x) throws SQLException {
/* 3765 */     setInternal(parameterIndex, StringUtils.fixDecimalExponent(String.valueOf(x)));
/*      */ 
/*      */     
/* 3768 */     this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = 6;
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
/*      */   public void setInt(int parameterIndex, int x) throws SQLException {
/* 3784 */     setInternal(parameterIndex, String.valueOf(x));
/*      */     
/* 3786 */     this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = 4;
/*      */   }
/*      */ 
/*      */   
/*      */   protected final void setInternal(int paramIndex, byte[] val) throws SQLException {
/* 3791 */     synchronized (checkClosed()) {
/*      */       
/* 3793 */       int parameterIndexOffset = getParameterIndexOffset();
/*      */       
/* 3795 */       checkBounds(paramIndex, parameterIndexOffset);
/*      */       
/* 3797 */       this.isStream[paramIndex - 1 + parameterIndexOffset] = false;
/* 3798 */       this.isNull[paramIndex - 1 + parameterIndexOffset] = false;
/* 3799 */       this.parameterStreams[paramIndex - 1 + parameterIndexOffset] = null;
/* 3800 */       this.parameterValues[paramIndex - 1 + parameterIndexOffset] = val;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void checkBounds(int paramIndex, int parameterIndexOffset) throws SQLException {
/* 3806 */     synchronized (checkClosed()) {
/* 3807 */       if (paramIndex < 1) {
/* 3808 */         throw SQLError.createSQLException(Messages.getString("PreparedStatement.49") + paramIndex + Messages.getString("PreparedStatement.50"), "S1009", getExceptionInterceptor());
/*      */       }
/*      */ 
/*      */       
/* 3812 */       if (paramIndex > this.parameterCount) {
/* 3813 */         throw SQLError.createSQLException(Messages.getString("PreparedStatement.51") + paramIndex + Messages.getString("PreparedStatement.52") + this.parameterValues.length + Messages.getString("PreparedStatement.53"), "S1009", getExceptionInterceptor());
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 3818 */       if (parameterIndexOffset == -1 && paramIndex == 1) {
/* 3819 */         throw SQLError.createSQLException("Can't set IN parameter for return value of stored function call.", "S1009", getExceptionInterceptor());
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected final void setInternal(int paramIndex, String val) throws SQLException {
/* 3827 */     synchronized (checkClosed()) {
/*      */       
/* 3829 */       byte[] parameterAsBytes = null;
/*      */       
/* 3831 */       if (this.charConverter != null) {
/* 3832 */         parameterAsBytes = this.charConverter.toBytes(val);
/*      */       } else {
/* 3834 */         parameterAsBytes = StringUtils.getBytes(val, this.charConverter, this.charEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), getExceptionInterceptor());
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3840 */       setInternal(paramIndex, parameterAsBytes);
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
/*      */   public void setLong(int parameterIndex, long x) throws SQLException {
/* 3857 */     setInternal(parameterIndex, String.valueOf(x));
/*      */     
/* 3859 */     this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = -5;
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
/*      */   public void setNull(int parameterIndex, int sqlType) throws SQLException {
/* 3879 */     synchronized (checkClosed()) {
/* 3880 */       setInternal(parameterIndex, "null");
/* 3881 */       this.isNull[parameterIndex - 1 + getParameterIndexOffset()] = true;
/*      */       
/* 3883 */       this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = 0;
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
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNull(int parameterIndex, int sqlType, String arg) throws SQLException {
/* 3906 */     setNull(parameterIndex, sqlType);
/*      */     
/* 3908 */     this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = 0;
/*      */   }
/*      */ 
/*      */   
/*      */   private void setNumericObject(int parameterIndex, Object parameterObj, int targetSqlType, int scale) throws SQLException {
/*      */     Number parameterAsNum;
/* 3914 */     if (parameterObj instanceof Boolean) {
/* 3915 */       parameterAsNum = ((Boolean)parameterObj).booleanValue() ? Integer.valueOf(1) : Integer.valueOf(0);
/*      */     
/*      */     }
/* 3918 */     else if (parameterObj instanceof String) {
/* 3919 */       boolean parameterAsBoolean; switch (targetSqlType) {
/*      */         case -7:
/* 3921 */           if ("1".equals(parameterObj) || "0".equals(parameterObj)) {
/*      */             
/* 3923 */             Number number = Integer.valueOf((String)parameterObj); break;
/*      */           } 
/* 3925 */           parameterAsBoolean = "true".equalsIgnoreCase((String)parameterObj);
/*      */ 
/*      */           
/* 3928 */           parameterAsNum = parameterAsBoolean ? Integer.valueOf(1) : Integer.valueOf(0);
/*      */           break;
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case -6:
/*      */         case 4:
/*      */         case 5:
/* 3937 */           parameterAsNum = Integer.valueOf((String)parameterObj);
/*      */           break;
/*      */ 
/*      */ 
/*      */         
/*      */         case -5:
/* 3943 */           parameterAsNum = Long.valueOf((String)parameterObj);
/*      */           break;
/*      */ 
/*      */ 
/*      */         
/*      */         case 7:
/* 3949 */           parameterAsNum = Float.valueOf((String)parameterObj);
/*      */           break;
/*      */ 
/*      */ 
/*      */         
/*      */         case 6:
/*      */         case 8:
/* 3956 */           parameterAsNum = Double.valueOf((String)parameterObj);
/*      */           break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         default:
/* 3964 */           parameterAsNum = new BigDecimal((String)parameterObj);
/*      */           break;
/*      */       } 
/*      */     } else {
/* 3968 */       parameterAsNum = (Number)parameterObj;
/*      */     } 
/*      */     
/* 3971 */     switch (targetSqlType) {
/*      */       case -7:
/*      */       case -6:
/*      */       case 4:
/*      */       case 5:
/* 3976 */         setInt(parameterIndex, parameterAsNum.intValue());
/*      */         break;
/*      */ 
/*      */       
/*      */       case -5:
/* 3981 */         setLong(parameterIndex, parameterAsNum.longValue());
/*      */         break;
/*      */ 
/*      */       
/*      */       case 7:
/* 3986 */         setFloat(parameterIndex, parameterAsNum.floatValue());
/*      */         break;
/*      */ 
/*      */       
/*      */       case 6:
/*      */       case 8:
/* 3992 */         setDouble(parameterIndex, parameterAsNum.doubleValue());
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 2:
/*      */       case 3:
/* 3999 */         if (parameterAsNum instanceof BigDecimal) {
/* 4000 */           BigDecimal scaledBigDecimal = null;
/*      */           
/*      */           try {
/* 4003 */             scaledBigDecimal = ((BigDecimal)parameterAsNum).setScale(scale);
/*      */           }
/* 4005 */           catch (ArithmeticException ex) {
/*      */             try {
/* 4007 */               scaledBigDecimal = ((BigDecimal)parameterAsNum).setScale(scale, 4);
/*      */             
/*      */             }
/* 4010 */             catch (ArithmeticException arEx) {
/* 4011 */               throw SQLError.createSQLException("Can't set scale of '" + scale + "' for DECIMAL argument '" + parameterAsNum + "'", "S1009", getExceptionInterceptor());
/*      */             } 
/*      */           } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 4020 */           setBigDecimal(parameterIndex, scaledBigDecimal); break;
/* 4021 */         }  if (parameterAsNum instanceof BigInteger) {
/* 4022 */           setBigDecimal(parameterIndex, new BigDecimal((BigInteger)parameterAsNum, scale));
/*      */ 
/*      */           
/*      */           break;
/*      */         } 
/*      */         
/* 4028 */         setBigDecimal(parameterIndex, new BigDecimal(parameterAsNum.doubleValue()));
/*      */         break;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setObject(int parameterIndex, Object parameterObj) throws SQLException {
/* 4039 */     synchronized (checkClosed()) {
/* 4040 */       if (parameterObj == null) {
/* 4041 */         setNull(parameterIndex, 1111);
/*      */       }
/* 4043 */       else if (parameterObj instanceof Byte) {
/* 4044 */         setInt(parameterIndex, ((Byte)parameterObj).intValue());
/* 4045 */       } else if (parameterObj instanceof String) {
/* 4046 */         setString(parameterIndex, (String)parameterObj);
/* 4047 */       } else if (parameterObj instanceof BigDecimal) {
/* 4048 */         setBigDecimal(parameterIndex, (BigDecimal)parameterObj);
/* 4049 */       } else if (parameterObj instanceof Short) {
/* 4050 */         setShort(parameterIndex, ((Short)parameterObj).shortValue());
/* 4051 */       } else if (parameterObj instanceof Integer) {
/* 4052 */         setInt(parameterIndex, ((Integer)parameterObj).intValue());
/* 4053 */       } else if (parameterObj instanceof Long) {
/* 4054 */         setLong(parameterIndex, ((Long)parameterObj).longValue());
/* 4055 */       } else if (parameterObj instanceof Float) {
/* 4056 */         setFloat(parameterIndex, ((Float)parameterObj).floatValue());
/* 4057 */       } else if (parameterObj instanceof Double) {
/* 4058 */         setDouble(parameterIndex, ((Double)parameterObj).doubleValue());
/* 4059 */       } else if (parameterObj instanceof byte[]) {
/* 4060 */         setBytes(parameterIndex, (byte[])parameterObj);
/* 4061 */       } else if (parameterObj instanceof Date) {
/* 4062 */         setDate(parameterIndex, (Date)parameterObj);
/* 4063 */       } else if (parameterObj instanceof Time) {
/* 4064 */         setTime(parameterIndex, (Time)parameterObj);
/* 4065 */       } else if (parameterObj instanceof Timestamp) {
/* 4066 */         setTimestamp(parameterIndex, (Timestamp)parameterObj);
/* 4067 */       } else if (parameterObj instanceof Boolean) {
/* 4068 */         setBoolean(parameterIndex, ((Boolean)parameterObj).booleanValue());
/*      */       }
/* 4070 */       else if (parameterObj instanceof InputStream) {
/* 4071 */         setBinaryStream(parameterIndex, (InputStream)parameterObj, -1);
/* 4072 */       } else if (parameterObj instanceof Blob) {
/* 4073 */         setBlob(parameterIndex, (Blob)parameterObj);
/* 4074 */       } else if (parameterObj instanceof Clob) {
/* 4075 */         setClob(parameterIndex, (Clob)parameterObj);
/* 4076 */       } else if (this.connection.getTreatUtilDateAsTimestamp() && parameterObj instanceof Date) {
/*      */         
/* 4078 */         setTimestamp(parameterIndex, new Timestamp(((Date)parameterObj).getTime()));
/*      */       }
/* 4080 */       else if (parameterObj instanceof BigInteger) {
/* 4081 */         setString(parameterIndex, parameterObj.toString());
/*      */       } else {
/* 4083 */         setSerializableObject(parameterIndex, parameterObj);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setObject(int parameterIndex, Object parameterObj, int targetSqlType) throws SQLException {
/* 4105 */     if (!(parameterObj instanceof BigDecimal)) {
/* 4106 */       setObject(parameterIndex, parameterObj, targetSqlType, 0);
/*      */     } else {
/* 4108 */       setObject(parameterIndex, parameterObj, targetSqlType, ((BigDecimal)parameterObj).scale());
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setObject(int parameterIndex, Object parameterObj, int targetSqlType, int scale) throws SQLException {
/* 4144 */     synchronized (checkClosed()) {
/* 4145 */       if (parameterObj == null) {
/* 4146 */         setNull(parameterIndex, 1111);
/*      */       } else {
/*      */         try {
/* 4149 */           Date parameterAsDate; switch (targetSqlType) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*      */             case 16:
/* 4169 */               if (parameterObj instanceof Boolean) {
/* 4170 */                 setBoolean(parameterIndex, ((Boolean)parameterObj).booleanValue());
/*      */                 break;
/*      */               } 
/* 4173 */               if (parameterObj instanceof String) {
/* 4174 */                 setBoolean(parameterIndex, ("true".equalsIgnoreCase((String)parameterObj) || !"0".equalsIgnoreCase((String)parameterObj)));
/*      */                 
/*      */                 break;
/*      */               } 
/* 4178 */               if (parameterObj instanceof Number) {
/* 4179 */                 int intValue = ((Number)parameterObj).intValue();
/*      */                 
/* 4181 */                 setBoolean(parameterIndex, (intValue != 0));
/*      */                 
/*      */                 break;
/*      */               } 
/* 4185 */               throw SQLError.createSQLException("No conversion from " + parameterObj.getClass().getName() + " to Types.BOOLEAN possible.", "S1009", getExceptionInterceptor());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*      */             case -7:
/*      */             case -6:
/*      */             case -5:
/*      */             case 2:
/*      */             case 3:
/*      */             case 4:
/*      */             case 5:
/*      */             case 6:
/*      */             case 7:
/*      */             case 8:
/* 4201 */               setNumericObject(parameterIndex, parameterObj, targetSqlType, scale);
/*      */               break;
/*      */ 
/*      */             
/*      */             case -1:
/*      */             case 1:
/*      */             case 12:
/* 4208 */               if (parameterObj instanceof BigDecimal) {
/* 4209 */                 setString(parameterIndex, StringUtils.fixDecimalExponent(StringUtils.consistentToString((BigDecimal)parameterObj)));
/*      */ 
/*      */                 
/*      */                 break;
/*      */               } 
/*      */               
/* 4215 */               setString(parameterIndex, parameterObj.toString());
/*      */               break;
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*      */             case 2005:
/* 4222 */               if (parameterObj instanceof Clob) {
/* 4223 */                 setClob(parameterIndex, (Clob)parameterObj); break;
/*      */               } 
/* 4225 */               setString(parameterIndex, parameterObj.toString());
/*      */               break;
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*      */             case -4:
/*      */             case -3:
/*      */             case -2:
/*      */             case 2004:
/* 4235 */               if (parameterObj instanceof byte[]) {
/* 4236 */                 setBytes(parameterIndex, (byte[])parameterObj); break;
/* 4237 */               }  if (parameterObj instanceof Blob) {
/* 4238 */                 setBlob(parameterIndex, (Blob)parameterObj); break;
/*      */               } 
/* 4240 */               setBytes(parameterIndex, StringUtils.getBytes(parameterObj.toString(), this.charConverter, this.charEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), getExceptionInterceptor()));
/*      */               break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*      */             case 91:
/*      */             case 93:
/* 4254 */               if (parameterObj instanceof String) {
/* 4255 */                 ParsePosition pp = new ParsePosition(0);
/* 4256 */                 DateFormat sdf = new SimpleDateFormat(getDateTimePattern((String)parameterObj, false), Locale.US);
/*      */                 
/* 4258 */                 parameterAsDate = sdf.parse((String)parameterObj, pp);
/*      */               } else {
/* 4260 */                 parameterAsDate = (Date)parameterObj;
/*      */               } 
/*      */               
/* 4263 */               switch (targetSqlType) {
/*      */                 
/*      */                 case 91:
/* 4266 */                   if (parameterAsDate instanceof Date) {
/* 4267 */                     setDate(parameterIndex, (Date)parameterAsDate);
/*      */                     break;
/*      */                   } 
/* 4270 */                   setDate(parameterIndex, new Date(parameterAsDate.getTime()));
/*      */                   break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/*      */                 case 93:
/* 4278 */                   if (parameterAsDate instanceof Timestamp) {
/* 4279 */                     setTimestamp(parameterIndex, (Timestamp)parameterAsDate);
/*      */                     break;
/*      */                   } 
/* 4282 */                   setTimestamp(parameterIndex, new Timestamp(parameterAsDate.getTime()));
/*      */                   break;
/*      */               } 
/*      */ 
/*      */ 
/*      */ 
/*      */               
/*      */               break;
/*      */ 
/*      */ 
/*      */             
/*      */             case 92:
/* 4294 */               if (parameterObj instanceof String) {
/* 4295 */                 DateFormat sdf = new SimpleDateFormat(getDateTimePattern((String)parameterObj, true), Locale.US);
/*      */                 
/* 4297 */                 setTime(parameterIndex, new Time(sdf.parse((String)parameterObj).getTime())); break;
/*      */               } 
/* 4299 */               if (parameterObj instanceof Timestamp) {
/* 4300 */                 Timestamp xT = (Timestamp)parameterObj;
/* 4301 */                 setTime(parameterIndex, new Time(xT.getTime())); break;
/*      */               } 
/* 4303 */               setTime(parameterIndex, (Time)parameterObj);
/*      */               break;
/*      */ 
/*      */ 
/*      */             
/*      */             case 1111:
/* 4309 */               setSerializableObject(parameterIndex, parameterObj);
/*      */               break;
/*      */ 
/*      */             
/*      */             default:
/* 4314 */               throw SQLError.createSQLException(Messages.getString("PreparedStatement.16"), "S1000", getExceptionInterceptor());
/*      */           } 
/*      */ 
/*      */         
/* 4318 */         } catch (Exception ex) {
/* 4319 */           if (ex instanceof SQLException) {
/* 4320 */             throw (SQLException)ex;
/*      */           }
/*      */           
/* 4323 */           SQLException sqlEx = SQLError.createSQLException(Messages.getString("PreparedStatement.17") + parameterObj.getClass().toString() + Messages.getString("PreparedStatement.18") + ex.getClass().getName() + Messages.getString("PreparedStatement.19") + ex.getMessage(), "S1000", getExceptionInterceptor());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 4331 */           sqlEx.initCause(ex);
/*      */           
/* 4333 */           throw sqlEx;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected int setOneBatchedParameterSet(PreparedStatement batchedStatement, int batchedParamIndex, Object paramSet) throws SQLException {
/* 4342 */     BatchParams paramArg = (BatchParams)paramSet;
/*      */     
/* 4344 */     boolean[] isNullBatch = paramArg.isNull;
/* 4345 */     boolean[] isStreamBatch = paramArg.isStream;
/*      */     
/* 4347 */     for (int j = 0; j < isNullBatch.length; j++) {
/* 4348 */       if (isNullBatch[j]) {
/* 4349 */         batchedStatement.setNull(batchedParamIndex++, 0);
/*      */       }
/* 4351 */       else if (isStreamBatch[j]) {
/* 4352 */         batchedStatement.setBinaryStream(batchedParamIndex++, paramArg.parameterStreams[j], paramArg.streamLengths[j]);
/*      */       }
/*      */       else {
/*      */         
/* 4356 */         ((PreparedStatement)batchedStatement).setBytesNoEscapeNoQuotes(batchedParamIndex++, paramArg.parameterStrings[j]);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 4363 */     return batchedParamIndex;
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
/*      */   public void setRef(int i, Ref x) throws SQLException {
/* 4380 */     throw SQLError.notImplemented();
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
/*      */   private final void setSerializableObject(int parameterIndex, Object parameterObj) throws SQLException {
/*      */     try {
/* 4399 */       ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
/* 4400 */       ObjectOutputStream objectOut = new ObjectOutputStream(bytesOut);
/* 4401 */       objectOut.writeObject(parameterObj);
/* 4402 */       objectOut.flush();
/* 4403 */       objectOut.close();
/* 4404 */       bytesOut.flush();
/* 4405 */       bytesOut.close();
/*      */       
/* 4407 */       byte[] buf = bytesOut.toByteArray();
/* 4408 */       ByteArrayInputStream bytesIn = new ByteArrayInputStream(buf);
/* 4409 */       setBinaryStream(parameterIndex, bytesIn, buf.length);
/* 4410 */       this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = -2;
/* 4411 */     } catch (Exception ex) {
/* 4412 */       SQLException sqlEx = SQLError.createSQLException(Messages.getString("PreparedStatement.54") + ex.getClass().getName(), "S1009", getExceptionInterceptor());
/*      */ 
/*      */       
/* 4415 */       sqlEx.initCause(ex);
/*      */       
/* 4417 */       throw sqlEx;
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
/*      */   public void setShort(int parameterIndex, short x) throws SQLException {
/* 4434 */     setInternal(parameterIndex, String.valueOf(x));
/*      */     
/* 4436 */     this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = 5;
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
/*      */   public void setString(int parameterIndex, String x) throws SQLException {
/* 4453 */     synchronized (checkClosed()) {
/*      */       
/* 4455 */       if (x == null) {
/* 4456 */         setNull(parameterIndex, 1);
/*      */       } else {
/* 4458 */         checkClosed();
/*      */         
/* 4460 */         int stringLength = x.length();
/*      */         
/* 4462 */         if (this.connection.isNoBackslashEscapesSet()) {
/*      */ 
/*      */           
/* 4465 */           boolean needsHexEscape = isEscapeNeededForString(x, stringLength);
/*      */ 
/*      */           
/* 4468 */           if (!needsHexEscape) {
/* 4469 */             byte[] arrayOfByte = null;
/*      */             
/* 4471 */             StringBuffer quotedString = new StringBuffer(x.length() + 2);
/* 4472 */             quotedString.append('\'');
/* 4473 */             quotedString.append(x);
/* 4474 */             quotedString.append('\'');
/*      */             
/* 4476 */             if (!this.isLoadDataQuery) {
/* 4477 */               arrayOfByte = StringUtils.getBytes(quotedString.toString(), this.charConverter, this.charEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), getExceptionInterceptor());
/*      */             
/*      */             }
/*      */             else {
/*      */ 
/*      */               
/* 4483 */               arrayOfByte = StringUtils.getBytes(quotedString.toString());
/*      */             } 
/*      */             
/* 4486 */             setInternal(parameterIndex, arrayOfByte);
/*      */           } else {
/* 4488 */             byte[] arrayOfByte = null;
/*      */             
/* 4490 */             if (!this.isLoadDataQuery) {
/* 4491 */               arrayOfByte = StringUtils.getBytes(x, this.charConverter, this.charEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), getExceptionInterceptor());
/*      */             
/*      */             }
/*      */             else {
/*      */ 
/*      */               
/* 4497 */               arrayOfByte = StringUtils.getBytes(x);
/*      */             } 
/*      */             
/* 4500 */             setBytes(parameterIndex, arrayOfByte);
/*      */           } 
/*      */           
/*      */           return;
/*      */         } 
/*      */         
/* 4506 */         String parameterAsString = x;
/* 4507 */         boolean needsQuoted = true;
/*      */         
/* 4509 */         if (this.isLoadDataQuery || isEscapeNeededForString(x, stringLength)) {
/* 4510 */           needsQuoted = false;
/*      */           
/* 4512 */           StringBuffer buf = new StringBuffer((int)(x.length() * 1.1D));
/*      */           
/* 4514 */           buf.append('\'');
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 4523 */           for (int i = 0; i < stringLength; i++) {
/* 4524 */             char c = x.charAt(i);
/*      */             
/* 4526 */             switch (c) {
/*      */               case '\000':
/* 4528 */                 buf.append('\\');
/* 4529 */                 buf.append('0');
/*      */                 break;
/*      */ 
/*      */               
/*      */               case '\n':
/* 4534 */                 buf.append('\\');
/* 4535 */                 buf.append('n');
/*      */                 break;
/*      */ 
/*      */               
/*      */               case '\r':
/* 4540 */                 buf.append('\\');
/* 4541 */                 buf.append('r');
/*      */                 break;
/*      */ 
/*      */               
/*      */               case '\\':
/* 4546 */                 buf.append('\\');
/* 4547 */                 buf.append('\\');
/*      */                 break;
/*      */ 
/*      */               
/*      */               case '\'':
/* 4552 */                 buf.append('\\');
/* 4553 */                 buf.append('\'');
/*      */                 break;
/*      */ 
/*      */               
/*      */               case '"':
/* 4558 */                 if (this.usingAnsiMode) {
/* 4559 */                   buf.append('\\');
/*      */                 }
/*      */                 
/* 4562 */                 buf.append('"');
/*      */                 break;
/*      */ 
/*      */               
/*      */               case '\032':
/* 4567 */                 buf.append('\\');
/* 4568 */                 buf.append('Z');
/*      */                 break;
/*      */ 
/*      */ 
/*      */               
/*      */               case '¥':
/*      */               case '₩':
/* 4575 */                 if (this.charsetEncoder != null) {
/* 4576 */                   CharBuffer cbuf = CharBuffer.allocate(1);
/* 4577 */                   ByteBuffer bbuf = ByteBuffer.allocate(1);
/* 4578 */                   cbuf.put(c);
/* 4579 */                   cbuf.position(0);
/* 4580 */                   this.charsetEncoder.encode(cbuf, bbuf, true);
/* 4581 */                   if (bbuf.get(0) == 92) {
/* 4582 */                     buf.append('\\');
/*      */                   }
/*      */                 } 
/*      */ 
/*      */               
/*      */               default:
/* 4588 */                 buf.append(c);
/*      */                 break;
/*      */             } 
/*      */           } 
/* 4592 */           buf.append('\'');
/*      */           
/* 4594 */           parameterAsString = buf.toString();
/*      */         } 
/*      */         
/* 4597 */         byte[] parameterAsBytes = null;
/*      */         
/* 4599 */         if (!this.isLoadDataQuery) {
/* 4600 */           if (needsQuoted) {
/* 4601 */             parameterAsBytes = StringUtils.getBytesWrapped(parameterAsString, '\'', '\'', this.charConverter, this.charEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), getExceptionInterceptor());
/*      */           
/*      */           }
/*      */           else {
/*      */             
/* 4606 */             parameterAsBytes = StringUtils.getBytes(parameterAsString, this.charConverter, this.charEncoding, this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), getExceptionInterceptor());
/*      */           
/*      */           }
/*      */         
/*      */         }
/*      */         else {
/*      */           
/* 4613 */           parameterAsBytes = StringUtils.getBytes(parameterAsString);
/*      */         } 
/*      */         
/* 4616 */         setInternal(parameterIndex, parameterAsBytes);
/*      */         
/* 4618 */         this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = 12;
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private boolean isEscapeNeededForString(String x, int stringLength) {
/* 4624 */     boolean needsHexEscape = false;
/*      */     
/* 4626 */     for (int i = 0; i < stringLength; i++) {
/* 4627 */       char c = x.charAt(i);
/*      */       
/* 4629 */       switch (c) {
/*      */         
/*      */         case '\000':
/* 4632 */           needsHexEscape = true;
/*      */           break;
/*      */         
/*      */         case '\n':
/* 4636 */           needsHexEscape = true;
/*      */           break;
/*      */ 
/*      */         
/*      */         case '\r':
/* 4641 */           needsHexEscape = true;
/*      */           break;
/*      */         
/*      */         case '\\':
/* 4645 */           needsHexEscape = true;
/*      */           break;
/*      */ 
/*      */         
/*      */         case '\'':
/* 4650 */           needsHexEscape = true;
/*      */           break;
/*      */ 
/*      */         
/*      */         case '"':
/* 4655 */           needsHexEscape = true;
/*      */           break;
/*      */ 
/*      */         
/*      */         case '\032':
/* 4660 */           needsHexEscape = true;
/*      */           break;
/*      */       } 
/*      */       
/* 4664 */       if (needsHexEscape) {
/*      */         break;
/*      */       }
/*      */     } 
/* 4668 */     return needsHexEscape;
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
/*      */   public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
/* 4687 */     setTimeInternal(parameterIndex, x, cal, cal.getTimeZone(), true);
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
/*      */   public void setTime(int parameterIndex, Time x) throws SQLException {
/* 4704 */     setTimeInternal(parameterIndex, x, (Calendar)null, Util.getDefaultTimeZone(), false);
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
/*      */   
/*      */   private void setTimeInternal(int parameterIndex, Time x, Calendar targetCalendar, TimeZone tz, boolean rollForward) throws SQLException {
/* 4725 */     synchronized (checkClosed()) {
/* 4726 */       if (x == null) {
/* 4727 */         setNull(parameterIndex, 92);
/*      */       } else {
/* 4729 */         checkClosed();
/*      */         
/* 4731 */         if (!this.useLegacyDatetimeCode) {
/* 4732 */           newSetTimeInternal(parameterIndex, x, targetCalendar);
/*      */         } else {
/* 4734 */           Calendar sessionCalendar = getCalendarInstanceForSessionOrNew();
/*      */           
/* 4736 */           synchronized (sessionCalendar) {
/* 4737 */             x = TimeUtil.changeTimezone(this.connection, sessionCalendar, targetCalendar, x, tz, this.connection.getServerTimezoneTZ(), rollForward);
/*      */           } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 4744 */           setInternal(parameterIndex, "'" + x.toString() + "'");
/*      */         } 
/*      */         
/* 4747 */         this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = 92;
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
/* 4768 */     setTimestampInternal(parameterIndex, x, cal, cal.getTimeZone(), true);
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
/*      */   public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
/* 4785 */     setTimestampInternal(parameterIndex, x, (Calendar)null, Util.getDefaultTimeZone(), false);
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
/*      */   private void setTimestampInternal(int parameterIndex, Timestamp x, Calendar targetCalendar, TimeZone tz, boolean rollForward) throws SQLException {
/* 4805 */     synchronized (checkClosed()) {
/* 4806 */       if (x == null) {
/* 4807 */         setNull(parameterIndex, 93);
/*      */       } else {
/* 4809 */         checkClosed();
/*      */         
/* 4811 */         if (!this.useLegacyDatetimeCode) {
/* 4812 */           newSetTimestampInternal(parameterIndex, x, targetCalendar);
/*      */         } else {
/* 4814 */           Calendar sessionCalendar = this.connection.getUseJDBCCompliantTimezoneShift() ? this.connection.getUtcCalendar() : getCalendarInstanceForSessionOrNew();
/*      */ 
/*      */ 
/*      */           
/* 4818 */           synchronized (sessionCalendar) {
/* 4819 */             x = TimeUtil.changeTimezone(this.connection, sessionCalendar, targetCalendar, x, tz, this.connection.getServerTimezoneTZ(), rollForward);
/*      */           } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 4826 */           if (this.connection.getUseSSPSCompatibleTimezoneShift()) {
/* 4827 */             doSSPSCompatibleTimezoneShift(parameterIndex, x, sessionCalendar);
/*      */           } else {
/* 4829 */             synchronized (this) {
/* 4830 */               if (this.tsdf == null) {
/* 4831 */                 this.tsdf = new SimpleDateFormat("''yyyy-MM-dd HH:mm:ss", Locale.US);
/*      */               }
/*      */               
/* 4834 */               StringBuffer buf = new StringBuffer();
/* 4835 */               buf.append(this.tsdf.format(x));
/*      */               
/* 4837 */               if (this.serverSupportsFracSecs) {
/* 4838 */                 int nanos = x.getNanos();
/*      */                 
/* 4840 */                 if (nanos != 0) {
/* 4841 */                   buf.append('.');
/* 4842 */                   buf.append(TimeUtil.formatNanos(nanos, this.serverSupportsFracSecs));
/*      */                 } 
/*      */               } 
/*      */               
/* 4846 */               buf.append('\'');
/*      */               
/* 4848 */               setInternal(parameterIndex, buf.toString());
/*      */             } 
/*      */           } 
/*      */         } 
/*      */ 
/*      */         
/* 4854 */         this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = 93;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void newSetTimestampInternal(int parameterIndex, Timestamp x, Calendar targetCalendar) throws SQLException {
/* 4861 */     synchronized (checkClosed()) {
/* 4862 */       if (this.tsdf == null) {
/* 4863 */         this.tsdf = new SimpleDateFormat("''yyyy-MM-dd HH:mm:ss", Locale.US);
/*      */       }
/*      */       
/* 4866 */       String timestampString = null;
/*      */       
/* 4868 */       if (targetCalendar != null) {
/* 4869 */         targetCalendar.setTime(x);
/* 4870 */         this.tsdf.setTimeZone(targetCalendar.getTimeZone());
/*      */         
/* 4872 */         timestampString = this.tsdf.format(x);
/*      */       } else {
/* 4874 */         this.tsdf.setTimeZone(this.connection.getServerTimezoneTZ());
/* 4875 */         timestampString = this.tsdf.format(x);
/*      */       } 
/*      */       
/* 4878 */       StringBuffer buf = new StringBuffer();
/* 4879 */       buf.append(timestampString);
/* 4880 */       buf.append('.');
/* 4881 */       buf.append(TimeUtil.formatNanos(x.getNanos(), this.serverSupportsFracSecs));
/* 4882 */       buf.append('\'');
/*      */       
/* 4884 */       setInternal(parameterIndex, buf.toString());
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void newSetTimeInternal(int parameterIndex, Time x, Calendar targetCalendar) throws SQLException {
/* 4890 */     synchronized (checkClosed()) {
/* 4891 */       if (this.tdf == null) {
/* 4892 */         this.tdf = new SimpleDateFormat("''HH:mm:ss''", Locale.US);
/*      */       }
/*      */ 
/*      */       
/* 4896 */       String timeString = null;
/*      */       
/* 4898 */       if (targetCalendar != null) {
/* 4899 */         targetCalendar.setTime(x);
/* 4900 */         this.tdf.setTimeZone(targetCalendar.getTimeZone());
/*      */         
/* 4902 */         timeString = this.tdf.format(x);
/*      */       } else {
/* 4904 */         this.tdf.setTimeZone(this.connection.getServerTimezoneTZ());
/* 4905 */         timeString = this.tdf.format(x);
/*      */       } 
/*      */       
/* 4908 */       setInternal(parameterIndex, timeString);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void newSetDateInternal(int parameterIndex, Date x, Calendar targetCalendar) throws SQLException {
/* 4914 */     synchronized (checkClosed()) {
/* 4915 */       if (this.ddf == null) {
/* 4916 */         this.ddf = new SimpleDateFormat("''yyyy-MM-dd''", Locale.US);
/*      */       }
/*      */       
/* 4919 */       String timeString = null;
/*      */       
/* 4921 */       if (targetCalendar != null) {
/* 4922 */         targetCalendar.setTime(x);
/* 4923 */         this.ddf.setTimeZone(targetCalendar.getTimeZone());
/*      */         
/* 4925 */         timeString = this.ddf.format(x);
/*      */       } else {
/* 4927 */         this.ddf.setTimeZone(this.connection.getServerTimezoneTZ());
/* 4928 */         timeString = this.ddf.format(x);
/*      */       } 
/*      */       
/* 4931 */       setInternal(parameterIndex, timeString);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void doSSPSCompatibleTimezoneShift(int parameterIndex, Timestamp x, Calendar sessionCalendar) throws SQLException {
/* 4936 */     synchronized (checkClosed()) {
/* 4937 */       Calendar sessionCalendar2 = this.connection.getUseJDBCCompliantTimezoneShift() ? this.connection.getUtcCalendar() : getCalendarInstanceForSessionOrNew();
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 4942 */       synchronized (sessionCalendar2) {
/* 4943 */         Date oldTime = sessionCalendar2.getTime();
/*      */         
/*      */         try {
/* 4946 */           sessionCalendar2.setTime(x);
/*      */           
/* 4948 */           int year = sessionCalendar2.get(1);
/* 4949 */           int month = sessionCalendar2.get(2) + 1;
/* 4950 */           int date = sessionCalendar2.get(5);
/*      */           
/* 4952 */           int hour = sessionCalendar2.get(11);
/* 4953 */           int minute = sessionCalendar2.get(12);
/* 4954 */           int seconds = sessionCalendar2.get(13);
/*      */           
/* 4956 */           StringBuffer tsBuf = new StringBuffer();
/*      */           
/* 4958 */           tsBuf.append('\'');
/* 4959 */           tsBuf.append(year);
/*      */           
/* 4961 */           tsBuf.append("-");
/*      */           
/* 4963 */           if (month < 10) {
/* 4964 */             tsBuf.append('0');
/*      */           }
/*      */           
/* 4967 */           tsBuf.append(month);
/*      */           
/* 4969 */           tsBuf.append('-');
/*      */           
/* 4971 */           if (date < 10) {
/* 4972 */             tsBuf.append('0');
/*      */           }
/*      */           
/* 4975 */           tsBuf.append(date);
/*      */           
/* 4977 */           tsBuf.append(' ');
/*      */           
/* 4979 */           if (hour < 10) {
/* 4980 */             tsBuf.append('0');
/*      */           }
/*      */           
/* 4983 */           tsBuf.append(hour);
/*      */           
/* 4985 */           tsBuf.append(':');
/*      */           
/* 4987 */           if (minute < 10) {
/* 4988 */             tsBuf.append('0');
/*      */           }
/*      */           
/* 4991 */           tsBuf.append(minute);
/*      */           
/* 4993 */           tsBuf.append(':');
/*      */           
/* 4995 */           if (seconds < 10) {
/* 4996 */             tsBuf.append('0');
/*      */           }
/*      */           
/* 4999 */           tsBuf.append(seconds);
/*      */           
/* 5001 */           tsBuf.append('.');
/* 5002 */           tsBuf.append(TimeUtil.formatNanos(x.getNanos(), this.serverSupportsFracSecs));
/* 5003 */           tsBuf.append('\'');
/*      */           
/* 5005 */           setInternal(parameterIndex, tsBuf.toString());
/*      */         } finally {
/*      */           
/* 5008 */           sessionCalendar.setTime(oldTime);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
/* 5040 */     if (x == null) {
/* 5041 */       setNull(parameterIndex, 12);
/*      */     } else {
/* 5043 */       setBinaryStream(parameterIndex, x, length);
/*      */       
/* 5045 */       this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = 2005;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setURL(int parameterIndex, URL arg) throws SQLException {
/* 5053 */     if (arg != null) {
/* 5054 */       setString(parameterIndex, arg.toString());
/*      */       
/* 5056 */       this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = 70;
/*      */     } else {
/* 5058 */       setNull(parameterIndex, 1);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private final void streamToBytes(Buffer packet, InputStream in, boolean escape, int streamLength, boolean useLength) throws SQLException {
/* 5065 */     synchronized (checkClosed()) {
/*      */       try {
/* 5067 */         if (this.streamConvertBuf == null) {
/* 5068 */           this.streamConvertBuf = new byte[4096];
/*      */         }
/*      */         
/* 5071 */         String connectionEncoding = this.connection.getEncoding();
/*      */         
/* 5073 */         boolean hexEscape = false;
/*      */         
/*      */         try {
/* 5076 */           if (this.connection.isNoBackslashEscapesSet() || (this.connection.getUseUnicode() && connectionEncoding != null && CharsetMapping.isMultibyteCharset(connectionEncoding) && !this.connection.parserKnowsUnicode()))
/*      */           {
/*      */ 
/*      */ 
/*      */             
/* 5081 */             hexEscape = true;
/*      */           }
/* 5083 */         } catch (RuntimeException ex) {
/* 5084 */           SQLException sqlEx = SQLError.createSQLException(ex.toString(), "S1009", (ExceptionInterceptor)null);
/* 5085 */           sqlEx.initCause(ex);
/* 5086 */           throw sqlEx;
/*      */         } 
/*      */         
/* 5089 */         if (streamLength == -1) {
/* 5090 */           useLength = false;
/*      */         }
/*      */         
/* 5093 */         int bc = -1;
/*      */         
/* 5095 */         if (useLength) {
/* 5096 */           bc = readblock(in, this.streamConvertBuf, streamLength);
/*      */         } else {
/* 5098 */           bc = readblock(in, this.streamConvertBuf);
/*      */         } 
/*      */         
/* 5101 */         int lengthLeftToRead = streamLength - bc;
/*      */         
/* 5103 */         if (hexEscape) {
/* 5104 */           packet.writeStringNoNull("x");
/* 5105 */         } else if (this.connection.getIO().versionMeetsMinimum(4, 1, 0)) {
/* 5106 */           packet.writeStringNoNull("_binary");
/*      */         } 
/*      */         
/* 5109 */         if (escape) {
/* 5110 */           packet.writeByte((byte)39);
/*      */         }
/*      */         
/* 5113 */         while (bc > 0) {
/* 5114 */           if (hexEscape) {
/* 5115 */             hexEscapeBlock(this.streamConvertBuf, packet, bc);
/* 5116 */           } else if (escape) {
/* 5117 */             escapeblockFast(this.streamConvertBuf, packet, bc);
/*      */           } else {
/* 5119 */             packet.writeBytesNoNull(this.streamConvertBuf, 0, bc);
/*      */           } 
/*      */           
/* 5122 */           if (useLength) {
/* 5123 */             bc = readblock(in, this.streamConvertBuf, lengthLeftToRead);
/*      */             
/* 5125 */             if (bc > 0)
/* 5126 */               lengthLeftToRead -= bc; 
/*      */             continue;
/*      */           } 
/* 5129 */           bc = readblock(in, this.streamConvertBuf);
/*      */         } 
/*      */ 
/*      */         
/* 5133 */         if (escape) {
/* 5134 */           packet.writeByte((byte)39);
/*      */         }
/*      */       } finally {
/* 5137 */         if (this.connection.getAutoClosePStmtStreams()) {
/*      */           try {
/* 5139 */             in.close();
/* 5140 */           } catch (IOException ioEx) {}
/*      */ 
/*      */ 
/*      */           
/* 5144 */           in = null;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private final byte[] streamToBytes(InputStream in, boolean escape, int streamLength, boolean useLength) throws SQLException {
/* 5152 */     synchronized (checkClosed()) {
/*      */       try {
/* 5154 */         if (this.streamConvertBuf == null) {
/* 5155 */           this.streamConvertBuf = new byte[4096];
/*      */         }
/* 5157 */         if (streamLength == -1) {
/* 5158 */           useLength = false;
/*      */         }
/*      */         
/* 5161 */         ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
/*      */         
/* 5163 */         int bc = -1;
/*      */         
/* 5165 */         if (useLength) {
/* 5166 */           bc = readblock(in, this.streamConvertBuf, streamLength);
/*      */         } else {
/* 5168 */           bc = readblock(in, this.streamConvertBuf);
/*      */         } 
/*      */         
/* 5171 */         int lengthLeftToRead = streamLength - bc;
/*      */         
/* 5173 */         if (escape) {
/* 5174 */           if (this.connection.versionMeetsMinimum(4, 1, 0)) {
/* 5175 */             bytesOut.write(95);
/* 5176 */             bytesOut.write(98);
/* 5177 */             bytesOut.write(105);
/* 5178 */             bytesOut.write(110);
/* 5179 */             bytesOut.write(97);
/* 5180 */             bytesOut.write(114);
/* 5181 */             bytesOut.write(121);
/*      */           } 
/*      */           
/* 5184 */           bytesOut.write(39);
/*      */         } 
/*      */         
/* 5187 */         while (bc > 0) {
/* 5188 */           if (escape) {
/* 5189 */             escapeblockFast(this.streamConvertBuf, bytesOut, bc);
/*      */           } else {
/* 5191 */             bytesOut.write(this.streamConvertBuf, 0, bc);
/*      */           } 
/*      */           
/* 5194 */           if (useLength) {
/* 5195 */             bc = readblock(in, this.streamConvertBuf, lengthLeftToRead);
/*      */             
/* 5197 */             if (bc > 0)
/* 5198 */               lengthLeftToRead -= bc; 
/*      */             continue;
/*      */           } 
/* 5201 */           bc = readblock(in, this.streamConvertBuf);
/*      */         } 
/*      */ 
/*      */         
/* 5205 */         if (escape) {
/* 5206 */           bytesOut.write(39);
/*      */         }
/*      */         
/* 5209 */         return bytesOut.toByteArray();
/*      */       } finally {
/* 5211 */         if (this.connection.getAutoClosePStmtStreams()) {
/*      */           try {
/* 5213 */             in.close();
/* 5214 */           } catch (IOException ioEx) {}
/*      */ 
/*      */ 
/*      */           
/* 5218 */           in = null;
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
/*      */   public String toString() {
/* 5230 */     StringBuffer buf = new StringBuffer();
/* 5231 */     buf.append(super.toString());
/* 5232 */     buf.append(": ");
/*      */     
/*      */     try {
/* 5235 */       buf.append(asSql());
/* 5236 */     } catch (SQLException sqlEx) {
/* 5237 */       buf.append("EXCEPTION: " + sqlEx.toString());
/*      */     } 
/*      */     
/* 5240 */     return buf.toString();
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
/*      */   protected int getParameterIndexOffset() {
/* 5252 */     return 0;
/*      */   }
/*      */   
/*      */   public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
/* 5256 */     setAsciiStream(parameterIndex, x, -1);
/*      */   }
/*      */   
/*      */   public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
/* 5260 */     setAsciiStream(parameterIndex, x, (int)length);
/* 5261 */     this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = 2005;
/*      */   }
/*      */   
/*      */   public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
/* 5265 */     setBinaryStream(parameterIndex, x, -1);
/*      */   }
/*      */   
/*      */   public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
/* 5269 */     setBinaryStream(parameterIndex, x, (int)length);
/*      */   }
/*      */   
/*      */   public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
/* 5273 */     setBinaryStream(parameterIndex, inputStream);
/*      */   }
/*      */   
/*      */   public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
/* 5277 */     setCharacterStream(parameterIndex, reader, -1);
/*      */   }
/*      */   
/*      */   public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
/* 5281 */     setCharacterStream(parameterIndex, reader, (int)length);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setClob(int parameterIndex, Reader reader) throws SQLException {
/* 5286 */     setCharacterStream(parameterIndex, reader);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
/* 5292 */     setCharacterStream(parameterIndex, reader, length);
/*      */   }
/*      */   
/*      */   public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
/* 5296 */     setNCharacterStream(parameterIndex, value, -1L);
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
/*      */   public void setNString(int parameterIndex, String x) throws SQLException {
/* 5314 */     synchronized (checkClosed()) {
/* 5315 */       if (this.charEncoding.equalsIgnoreCase("UTF-8") || this.charEncoding.equalsIgnoreCase("utf8")) {
/*      */         
/* 5317 */         setString(parameterIndex, x);
/*      */         
/*      */         return;
/*      */       } 
/*      */       
/* 5322 */       if (x == null) {
/* 5323 */         setNull(parameterIndex, 1);
/*      */       } else {
/* 5325 */         int stringLength = x.length();
/*      */ 
/*      */ 
/*      */         
/* 5329 */         StringBuffer buf = new StringBuffer((int)(x.length() * 1.1D + 4.0D));
/* 5330 */         buf.append("_utf8");
/* 5331 */         buf.append('\'');
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 5340 */         for (int i = 0; i < stringLength; i++) {
/* 5341 */           char c = x.charAt(i);
/*      */           
/* 5343 */           switch (c) {
/*      */             case '\000':
/* 5345 */               buf.append('\\');
/* 5346 */               buf.append('0');
/*      */               break;
/*      */ 
/*      */             
/*      */             case '\n':
/* 5351 */               buf.append('\\');
/* 5352 */               buf.append('n');
/*      */               break;
/*      */ 
/*      */             
/*      */             case '\r':
/* 5357 */               buf.append('\\');
/* 5358 */               buf.append('r');
/*      */               break;
/*      */ 
/*      */             
/*      */             case '\\':
/* 5363 */               buf.append('\\');
/* 5364 */               buf.append('\\');
/*      */               break;
/*      */ 
/*      */             
/*      */             case '\'':
/* 5369 */               buf.append('\\');
/* 5370 */               buf.append('\'');
/*      */               break;
/*      */ 
/*      */             
/*      */             case '"':
/* 5375 */               if (this.usingAnsiMode) {
/* 5376 */                 buf.append('\\');
/*      */               }
/*      */               
/* 5379 */               buf.append('"');
/*      */               break;
/*      */ 
/*      */             
/*      */             case '\032':
/* 5384 */               buf.append('\\');
/* 5385 */               buf.append('Z');
/*      */               break;
/*      */ 
/*      */             
/*      */             default:
/* 5390 */               buf.append(c);
/*      */               break;
/*      */           } 
/*      */         } 
/* 5394 */         buf.append('\'');
/*      */         
/* 5396 */         String parameterAsString = buf.toString();
/*      */         
/* 5398 */         byte[] parameterAsBytes = null;
/*      */         
/* 5400 */         if (!this.isLoadDataQuery) {
/* 5401 */           parameterAsBytes = StringUtils.getBytes(parameterAsString, this.connection.getCharsetConverter("UTF-8"), "UTF-8", this.connection.getServerCharacterEncoding(), this.connection.parserKnowsUnicode(), getExceptionInterceptor());
/*      */         
/*      */         }
/*      */         else {
/*      */ 
/*      */           
/* 5407 */           parameterAsBytes = StringUtils.getBytes(parameterAsString);
/*      */         } 
/*      */         
/* 5410 */         setInternal(parameterIndex, parameterAsBytes);
/*      */         
/* 5412 */         this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = -9;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setNCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
/* 5441 */     synchronized (checkClosed()) {
/*      */       try {
/* 5443 */         if (reader == null) {
/* 5444 */           setNull(parameterIndex, -1);
/*      */         } else {
/*      */           
/* 5447 */           char[] c = null;
/* 5448 */           int len = 0;
/*      */           
/* 5450 */           boolean useLength = this.connection.getUseStreamLengthsInPrepStmts();
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 5455 */           if (useLength && length != -1L) {
/* 5456 */             c = new char[(int)length];
/*      */             
/* 5458 */             int numCharsRead = readFully(reader, c, (int)length);
/*      */ 
/*      */ 
/*      */             
/* 5462 */             setNString(parameterIndex, new String(c, 0, numCharsRead));
/*      */           } else {
/*      */             
/* 5465 */             c = new char[4096];
/*      */             
/* 5467 */             StringBuffer buf = new StringBuffer();
/*      */             
/* 5469 */             while ((len = reader.read(c)) != -1) {
/* 5470 */               buf.append(c, 0, len);
/*      */             }
/*      */             
/* 5473 */             setNString(parameterIndex, buf.toString());
/*      */           } 
/*      */           
/* 5476 */           this.parameterTypes[parameterIndex - 1 + getParameterIndexOffset()] = 2011;
/*      */         } 
/* 5478 */       } catch (IOException ioEx) {
/* 5479 */         throw SQLError.createSQLException(ioEx.toString(), "S1000", getExceptionInterceptor());
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void setNClob(int parameterIndex, Reader reader) throws SQLException {
/* 5486 */     setNCharacterStream(parameterIndex, reader);
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
/*      */   public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
/* 5504 */     if (reader == null) {
/* 5505 */       setNull(parameterIndex, -1);
/*      */     } else {
/* 5507 */       setNCharacterStream(parameterIndex, reader, length);
/*      */     } 
/*      */   }
/*      */   
/*      */   public ParameterBindings getParameterBindings() throws SQLException {
/* 5512 */     synchronized (checkClosed()) {
/* 5513 */       return new EmulatedPreparedStatementBindings();
/*      */     } 
/*      */   }
/*      */   
/*      */   class EmulatedPreparedStatementBindings
/*      */     implements ParameterBindings {
/*      */     private ResultSetImpl bindingsAsRs;
/*      */     private boolean[] parameterIsNull;
/*      */     
/*      */     EmulatedPreparedStatementBindings() throws SQLException {
/* 5523 */       List<ResultSetRow> rows = new ArrayList<ResultSetRow>();
/* 5524 */       this.parameterIsNull = new boolean[PreparedStatement.this.parameterCount];
/* 5525 */       System.arraycopy(PreparedStatement.this.isNull, 0, this.parameterIsNull, 0, PreparedStatement.this.parameterCount);
/*      */ 
/*      */       
/* 5528 */       byte[][] rowData = new byte[PreparedStatement.this.parameterCount][];
/* 5529 */       Field[] typeMetadata = new Field[PreparedStatement.this.parameterCount];
/*      */       
/* 5531 */       for (int i = 0; i < PreparedStatement.this.parameterCount; i++) {
/* 5532 */         if (PreparedStatement.this.batchCommandIndex == -1) {
/* 5533 */           rowData[i] = PreparedStatement.this.getBytesRepresentation(i);
/*      */         } else {
/* 5535 */           rowData[i] = PreparedStatement.this.getBytesRepresentationForBatch(i, PreparedStatement.this.batchCommandIndex);
/*      */         } 
/* 5537 */         int charsetIndex = 0;
/*      */         
/* 5539 */         if (PreparedStatement.this.parameterTypes[i] == -2 || PreparedStatement.this.parameterTypes[i] == 2004) {
/*      */           
/* 5541 */           charsetIndex = 63;
/*      */         } else {
/*      */           try {
/* 5544 */             String mysqlEncodingName = CharsetMapping.getMysqlEncodingForJavaEncoding(PreparedStatement.this.connection.getEncoding(), PreparedStatement.this.connection);
/*      */ 
/*      */             
/* 5547 */             charsetIndex = CharsetMapping.getCharsetIndexForMysqlEncodingName(mysqlEncodingName);
/*      */           }
/* 5549 */           catch (SQLException ex) {
/* 5550 */             throw ex;
/* 5551 */           } catch (RuntimeException ex) {
/* 5552 */             SQLException sqlEx = SQLError.createSQLException(ex.toString(), "S1009", (ExceptionInterceptor)null);
/* 5553 */             sqlEx.initCause(ex);
/* 5554 */             throw sqlEx;
/*      */           } 
/*      */         } 
/*      */         
/* 5558 */         Field parameterMetadata = new Field(null, "parameter_" + (i + 1), charsetIndex, PreparedStatement.this.parameterTypes[i], (rowData[i]).length);
/*      */ 
/*      */         
/* 5561 */         parameterMetadata.setConnection(PreparedStatement.this.connection);
/* 5562 */         typeMetadata[i] = parameterMetadata;
/*      */       } 
/*      */       
/* 5565 */       rows.add(new ByteArrayRow(rowData, PreparedStatement.this.getExceptionInterceptor()));
/*      */       
/* 5567 */       this.bindingsAsRs = new ResultSetImpl(PreparedStatement.this.connection.getCatalog(), typeMetadata, new RowDataStatic(rows), PreparedStatement.this.connection, null);
/*      */       
/* 5569 */       this.bindingsAsRs.next();
/*      */     }
/*      */     
/*      */     public Array getArray(int parameterIndex) throws SQLException {
/* 5573 */       return this.bindingsAsRs.getArray(parameterIndex);
/*      */     }
/*      */ 
/*      */     
/*      */     public InputStream getAsciiStream(int parameterIndex) throws SQLException {
/* 5578 */       return this.bindingsAsRs.getAsciiStream(parameterIndex);
/*      */     }
/*      */     
/*      */     public BigDecimal getBigDecimal(int parameterIndex) throws SQLException {
/* 5582 */       return this.bindingsAsRs.getBigDecimal(parameterIndex);
/*      */     }
/*      */ 
/*      */     
/*      */     public InputStream getBinaryStream(int parameterIndex) throws SQLException {
/* 5587 */       return this.bindingsAsRs.getBinaryStream(parameterIndex);
/*      */     }
/*      */     
/*      */     public Blob getBlob(int parameterIndex) throws SQLException {
/* 5591 */       return this.bindingsAsRs.getBlob(parameterIndex);
/*      */     }
/*      */     
/*      */     public boolean getBoolean(int parameterIndex) throws SQLException {
/* 5595 */       return this.bindingsAsRs.getBoolean(parameterIndex);
/*      */     }
/*      */     
/*      */     public byte getByte(int parameterIndex) throws SQLException {
/* 5599 */       return this.bindingsAsRs.getByte(parameterIndex);
/*      */     }
/*      */     
/*      */     public byte[] getBytes(int parameterIndex) throws SQLException {
/* 5603 */       return this.bindingsAsRs.getBytes(parameterIndex);
/*      */     }
/*      */ 
/*      */     
/*      */     public Reader getCharacterStream(int parameterIndex) throws SQLException {
/* 5608 */       return this.bindingsAsRs.getCharacterStream(parameterIndex);
/*      */     }
/*      */     
/*      */     public Clob getClob(int parameterIndex) throws SQLException {
/* 5612 */       return this.bindingsAsRs.getClob(parameterIndex);
/*      */     }
/*      */     
/*      */     public Date getDate(int parameterIndex) throws SQLException {
/* 5616 */       return this.bindingsAsRs.getDate(parameterIndex);
/*      */     }
/*      */     
/*      */     public double getDouble(int parameterIndex) throws SQLException {
/* 5620 */       return this.bindingsAsRs.getDouble(parameterIndex);
/*      */     }
/*      */     
/*      */     public float getFloat(int parameterIndex) throws SQLException {
/* 5624 */       return this.bindingsAsRs.getFloat(parameterIndex);
/*      */     }
/*      */     
/*      */     public int getInt(int parameterIndex) throws SQLException {
/* 5628 */       return this.bindingsAsRs.getInt(parameterIndex);
/*      */     }
/*      */     
/*      */     public long getLong(int parameterIndex) throws SQLException {
/* 5632 */       return this.bindingsAsRs.getLong(parameterIndex);
/*      */     }
/*      */ 
/*      */     
/*      */     public Reader getNCharacterStream(int parameterIndex) throws SQLException {
/* 5637 */       return this.bindingsAsRs.getCharacterStream(parameterIndex);
/*      */     }
/*      */     
/*      */     public Reader getNClob(int parameterIndex) throws SQLException {
/* 5641 */       return this.bindingsAsRs.getCharacterStream(parameterIndex);
/*      */     }
/*      */     
/*      */     public Object getObject(int parameterIndex) throws SQLException {
/* 5645 */       PreparedStatement.this.checkBounds(parameterIndex, 0);
/*      */       
/* 5647 */       if (this.parameterIsNull[parameterIndex - 1]) {
/* 5648 */         return null;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 5655 */       switch (PreparedStatement.this.parameterTypes[parameterIndex - 1]) {
/*      */         case -6:
/* 5657 */           return Byte.valueOf(getByte(parameterIndex));
/*      */         case 5:
/* 5659 */           return Short.valueOf(getShort(parameterIndex));
/*      */         case 4:
/* 5661 */           return Integer.valueOf(getInt(parameterIndex));
/*      */         case -5:
/* 5663 */           return Long.valueOf(getLong(parameterIndex));
/*      */         case 6:
/* 5665 */           return Float.valueOf(getFloat(parameterIndex));
/*      */         case 8:
/* 5667 */           return Double.valueOf(getDouble(parameterIndex));
/*      */       } 
/* 5669 */       return this.bindingsAsRs.getObject(parameterIndex);
/*      */     }
/*      */ 
/*      */     
/*      */     public Ref getRef(int parameterIndex) throws SQLException {
/* 5674 */       return this.bindingsAsRs.getRef(parameterIndex);
/*      */     }
/*      */     
/*      */     public short getShort(int parameterIndex) throws SQLException {
/* 5678 */       return this.bindingsAsRs.getShort(parameterIndex);
/*      */     }
/*      */     
/*      */     public String getString(int parameterIndex) throws SQLException {
/* 5682 */       return this.bindingsAsRs.getString(parameterIndex);
/*      */     }
/*      */     
/*      */     public Time getTime(int parameterIndex) throws SQLException {
/* 5686 */       return this.bindingsAsRs.getTime(parameterIndex);
/*      */     }
/*      */     
/*      */     public Timestamp getTimestamp(int parameterIndex) throws SQLException {
/* 5690 */       return this.bindingsAsRs.getTimestamp(parameterIndex);
/*      */     }
/*      */     
/*      */     public URL getURL(int parameterIndex) throws SQLException {
/* 5694 */       return this.bindingsAsRs.getURL(parameterIndex);
/*      */     }
/*      */     
/*      */     public boolean isNull(int parameterIndex) throws SQLException {
/* 5698 */       PreparedStatement.this.checkBounds(parameterIndex, 0);
/*      */       
/* 5700 */       return this.parameterIsNull[parameterIndex - 1];
/*      */     }
/*      */   }
/*      */   
/*      */   public String getPreparedSql() {
/*      */     try {
/* 5706 */       synchronized (checkClosed()) {
/* 5707 */         if (this.rewrittenBatchSize == 0) {
/* 5708 */           return this.originalSql;
/*      */         }
/*      */         
/*      */         try {
/* 5712 */           return this.parseInfo.getSqlForBatch(this.parseInfo);
/* 5713 */         } catch (UnsupportedEncodingException e) {
/* 5714 */           throw new RuntimeException(e);
/*      */         } 
/*      */       } 
/* 5717 */     } catch (SQLException e) {
/* 5718 */       throw new RuntimeException(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   public int getUpdateCount() throws SQLException {
/* 5723 */     int count = super.getUpdateCount();
/*      */     
/* 5725 */     if (containsOnDuplicateKeyUpdateInSQL() && this.compensateForOnDuplicateKeyUpdate)
/*      */     {
/* 5727 */       if (count == 2 || count == 0) {
/* 5728 */         count = 1;
/*      */       }
/*      */     }
/*      */     
/* 5732 */     return count;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected static boolean canRewrite(String sql, boolean isOnDuplicateKeyUpdate, int locationOfOnDuplicateKeyUpdate, int statementStartPos) {
/* 5739 */     boolean rewritableOdku = true;
/*      */     
/* 5741 */     if (isOnDuplicateKeyUpdate) {
/* 5742 */       int updateClausePos = StringUtils.indexOfIgnoreCase(locationOfOnDuplicateKeyUpdate, sql, " UPDATE ");
/*      */ 
/*      */       
/* 5745 */       if (updateClausePos != -1) {
/* 5746 */         rewritableOdku = (StringUtils.indexOfIgnoreCaseRespectMarker(updateClausePos, sql, "LAST_INSERT_ID", "\"'`", "\"'`", false) == -1);
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 5753 */     return (StringUtils.startsWithIgnoreCaseAndWs(sql, "INSERT", statementStartPos) && StringUtils.indexOfIgnoreCaseRespectMarker(statementStartPos, sql, "SELECT", "\"'`", "\"'`", false) == -1 && rewritableOdku);
/*      */   }
/*      */   
/*      */   static interface BatchVisitor {
/*      */     BatchVisitor increment();
/*      */     
/*      */     BatchVisitor decrement();
/*      */     
/*      */     BatchVisitor append(byte[] param1ArrayOfbyte);
/*      */     
/*      */     BatchVisitor merge(byte[] param1ArrayOfbyte1, byte[] param1ArrayOfbyte2);
/*      */   }
/*      */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mysql/jdbc/PreparedStatement.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */