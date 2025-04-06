/*      */ package com.mchange.v2.c3p0.codegen;
/*      */ 
/*      */ import com.mchange.v2.c3p0.C3P0ProxyConnection;
/*      */ import com.mchange.v2.c3p0.C3P0ProxyStatement;
/*      */ import com.mchange.v2.c3p0.impl.ProxyResultSetDetachable;
/*      */ import com.mchange.v2.codegen.CodegenUtils;
/*      */ import com.mchange.v2.codegen.IndentedWriter;
/*      */ import com.mchange.v2.codegen.intfc.DelegatorGenerator;
/*      */ import java.io.BufferedWriter;
/*      */ import java.io.File;
/*      */ import java.io.FileWriter;
/*      */ import java.io.IOException;
/*      */ import java.io.Writer;
/*      */ import java.lang.reflect.Method;
/*      */ import java.sql.CallableStatement;
/*      */ import java.sql.Connection;
/*      */ import java.sql.DatabaseMetaData;
/*      */ import java.sql.PreparedStatement;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.Statement;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class JdbcProxyGenerator
/*      */   extends DelegatorGenerator
/*      */ {
/*      */   private static final boolean PREMATURE_DETACH_DEBUG = false;
/*      */   
/*      */   JdbcProxyGenerator() {
/*   53 */     setGenerateInnerSetter(false);
/*   54 */     setGenerateInnerGetter(false);
/*   55 */     setGenerateNoArgConstructor(false);
/*   56 */     setGenerateWrappingConstructor(true);
/*   57 */     setClassModifiers(17);
/*   58 */     setMethodModifiers(17);
/*      */     
/*   60 */     setWrappingConstructorModifiers(0);
/*      */   }
/*      */ 
/*      */   
/*      */   static final class NewProxyMetaDataGenerator
/*      */     extends JdbcProxyGenerator
/*      */   {
/*      */     String getInnerTypeName() {
/*   68 */       return "DatabaseMetaData";
/*      */     }
/*      */     
/*      */     protected void generateDelegateCode(Class intfcl, String genclass, Method method, IndentedWriter iw) throws IOException {
/*   72 */       String mname = method.getName();
/*   73 */       if (JdbcProxyGenerator.jdbc4WrapperMethod(mname)) {
/*      */         
/*   75 */         JdbcProxyGenerator.generateWrapperDelegateCode(intfcl, genclass, method, iw);
/*      */         
/*      */         return;
/*      */       } 
/*   79 */       Class<?> retType = method.getReturnType();
/*      */       
/*   81 */       if (ResultSet.class.isAssignableFrom(retType)) {
/*      */         
/*   83 */         iw.println("ResultSet innerResultSet = inner." + CodegenUtils.methodCall(method) + ";");
/*   84 */         iw.println("if (innerResultSet == null) return null;");
/*   85 */         iw.println("return new NewProxyResultSet( innerResultSet, parentPooledConnection, inner, this );");
/*      */       }
/*   87 */       else if (mname.equals("getConnection")) {
/*      */         
/*   89 */         iw.println("return this.proxyCon;");
/*      */       } else {
/*      */         
/*   92 */         super.generateDelegateCode(intfcl, genclass, method, iw);
/*      */       } 
/*      */     }
/*      */     
/*      */     protected void generatePreDelegateCode(Class intfcl, String genclass, Method method, IndentedWriter iw) throws IOException {
/*   97 */       if ((method.getExceptionTypes()).length > 0) {
/*   98 */         super.generatePreDelegateCode(intfcl, genclass, method, iw);
/*      */       }
/*      */     }
/*      */     
/*      */     protected void generatePostDelegateCode(Class intfcl, String genclass, Method method, IndentedWriter iw) throws IOException {
/*  103 */       if ((method.getExceptionTypes()).length > 0) {
/*  104 */         super.generatePostDelegateCode(intfcl, genclass, method, iw);
/*      */       }
/*      */     }
/*      */     
/*      */     protected void generateExtraDeclarations(Class intfcl, String genclass, IndentedWriter iw) throws IOException {
/*  109 */       super.generateExtraDeclarations(intfcl, genclass, iw);
/*  110 */       iw.println();
/*  111 */       iw.println("NewProxyConnection proxyCon;");
/*  112 */       iw.println();
/*  113 */       iw.print(CodegenUtils.fqcnLastElement(genclass));
/*  114 */       iw.println("( " + CodegenUtils.simpleClassName(intfcl) + " inner, NewPooledConnection parentPooledConnection, NewProxyConnection proxyCon )");
/*  115 */       iw.println("{");
/*  116 */       iw.upIndent();
/*  117 */       iw.println("this( inner, parentPooledConnection );");
/*  118 */       iw.println("this.proxyCon = proxyCon;");
/*  119 */       iw.downIndent();
/*  120 */       iw.println("}");
/*      */     }
/*      */   }
/*      */   
/*      */   static final class NewProxyResultSetGenerator
/*      */     extends JdbcProxyGenerator {
/*      */     String getInnerTypeName() {
/*  127 */       return "ResultSet";
/*      */     }
/*      */ 
/*      */     
/*      */     protected void generateDelegateCode(Class intfcl, String genclass, Method method, IndentedWriter iw) throws IOException {
/*  132 */       String mname = method.getName();
/*  133 */       if (JdbcProxyGenerator.jdbc4WrapperMethod(mname)) {
/*      */         
/*  135 */         JdbcProxyGenerator.generateWrapperDelegateCode(intfcl, genclass, method, iw);
/*      */         
/*      */         return;
/*      */       } 
/*  139 */       Class<?> retType = method.getReturnType();
/*      */       
/*  141 */       iw.println("if (proxyConn != null) proxyConn.maybeDirtyTransaction();");
/*  142 */       iw.println();
/*      */       
/*  144 */       if (mname.equals("close")) {
/*      */         
/*  146 */         iw.println("if (! this.isDetached())");
/*  147 */         iw.println("{");
/*  148 */         iw.upIndent();
/*      */         
/*  150 */         iw.println("if (creator instanceof Statement)");
/*  151 */         iw.upIndent();
/*  152 */         iw.println("parentPooledConnection.markInactiveResultSetForStatement( (Statement) creator, inner );");
/*  153 */         iw.downIndent();
/*  154 */         iw.println("else if (creator instanceof DatabaseMetaData)");
/*  155 */         iw.upIndent();
/*  156 */         iw.println("parentPooledConnection.markInactiveMetaDataResultSet( inner );");
/*  157 */         iw.downIndent();
/*  158 */         iw.println("else if (creator instanceof Connection)");
/*  159 */         iw.upIndent();
/*  160 */         iw.println("parentPooledConnection.markInactiveRawConnectionResultSet( inner );");
/*  161 */         iw.downIndent();
/*  162 */         iw.println("else throw new InternalError(\"Must be Statement or DatabaseMetaData -- Bad Creator: \" + creator);");
/*      */         
/*  164 */         iw.println("if (creatorProxy instanceof ProxyResultSetDetachable) ((ProxyResultSetDetachable) creatorProxy).detachProxyResultSet( this );");
/*      */         
/*  166 */         iw.println("this.detach();");
/*  167 */         iw.println("inner.close();");
/*  168 */         iw.println("this.inner = null;");
/*      */         
/*  170 */         iw.downIndent();
/*  171 */         iw.println("}");
/*      */       }
/*  173 */       else if (mname.equals("getStatement")) {
/*      */         
/*  175 */         iw.println("if (creator instanceof Statement)");
/*  176 */         iw.upIndent();
/*  177 */         iw.println("return (Statement) creatorProxy;");
/*  178 */         iw.downIndent();
/*  179 */         iw.println("else if (creator instanceof DatabaseMetaData)");
/*  180 */         iw.upIndent();
/*  181 */         iw.println("return null;");
/*  182 */         iw.downIndent();
/*  183 */         iw.println("else throw new InternalError(\"Must be Statement or DatabaseMetaData -- Bad Creator: \" + creator);");
/*      */       }
/*  185 */       else if (mname.equals("isClosed")) {
/*      */         
/*  187 */         iw.println("return this.isDetached();");
/*      */       } else {
/*      */         
/*  190 */         super.generateDelegateCode(intfcl, genclass, method, iw);
/*      */       } 
/*      */     }
/*      */     
/*      */     protected void generateExtraDeclarations(Class intfcl, String genclass, IndentedWriter iw) throws IOException {
/*  195 */       super.generateExtraDeclarations(intfcl, genclass, iw);
/*  196 */       iw.println();
/*  197 */       iw.println("Object creator;");
/*  198 */       iw.println("Object creatorProxy;");
/*  199 */       iw.println("NewProxyConnection proxyConn;");
/*  200 */       iw.println();
/*  201 */       iw.print(CodegenUtils.fqcnLastElement(genclass));
/*  202 */       iw.println("( " + CodegenUtils.simpleClassName(intfcl) + " inner, NewPooledConnection parentPooledConnection, Object c, Object cProxy )");
/*  203 */       iw.println("{");
/*  204 */       iw.upIndent();
/*  205 */       iw.println("this( inner, parentPooledConnection );");
/*  206 */       iw.println("this.creator      = c;");
/*  207 */       iw.println("this.creatorProxy = cProxy;");
/*  208 */       iw.println("if (creatorProxy instanceof NewProxyConnection) this.proxyConn = (NewProxyConnection) cProxy;");
/*  209 */       iw.downIndent();
/*  210 */       iw.println("}");
/*      */     }
/*      */ 
/*      */     
/*      */     protected void generatePreDelegateCode(Class intfcl, String genclass, Method method, IndentedWriter iw) throws IOException {
/*  215 */       super.generatePreDelegateCode(intfcl, genclass, method, iw);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   static final class NewProxyAnyStatementGenerator
/*      */     extends JdbcProxyGenerator
/*      */   {
/*      */     private static final boolean CONCURRENT_ACCESS_DEBUG = false;
/*      */ 
/*      */     
/*      */     NewProxyAnyStatementGenerator() {
/*  227 */       setExtraInterfaces(new Class[] { C3P0ProxyStatement.class, ProxyResultSetDetachable.class });
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     String getInnerTypeName() {
/*      */       return "Statement";
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected void generateDelegateCode(Class intfcl, String genclass, Method method, IndentedWriter iw) throws IOException {
/*  241 */       String mname = method.getName();
/*  242 */       if (JdbcProxyGenerator.jdbc4WrapperMethod(mname)) {
/*      */         
/*  244 */         JdbcProxyGenerator.generateWrapperDelegateCode(intfcl, genclass, method, iw);
/*      */         
/*      */         return;
/*      */       } 
/*  248 */       Class<?> retType = method.getReturnType();
/*      */       
/*  250 */       iw.println("maybeDirtyTransaction();");
/*  251 */       iw.println();
/*      */       
/*  253 */       if (ResultSet.class.isAssignableFrom(retType)) {
/*      */         
/*  255 */         iw.println("ResultSet innerResultSet = inner." + CodegenUtils.methodCall(method) + ";");
/*  256 */         iw.println("if (innerResultSet == null) return null;");
/*  257 */         iw.println("parentPooledConnection.markActiveResultSetForStatement( inner, innerResultSet );");
/*  258 */         iw.println("NewProxyResultSet out = new NewProxyResultSet( innerResultSet, parentPooledConnection, inner, this );");
/*  259 */         iw.println("synchronized ( myProxyResultSets ) { myProxyResultSets.add( out ); }");
/*  260 */         iw.println("return out;");
/*      */       }
/*  262 */       else if (mname.equals("getConnection")) {
/*      */         
/*  264 */         iw.println("if (! this.isDetached())");
/*  265 */         iw.upIndent();
/*  266 */         iw.println("return creatorProxy;");
/*  267 */         iw.downIndent();
/*  268 */         iw.println("else");
/*  269 */         iw.upIndent();
/*  270 */         iw.println("throw new SQLException(\"You cannot operate on a closed Statement!\");");
/*  271 */         iw.downIndent();
/*      */       }
/*  273 */       else if (mname.equals("close")) {
/*      */         
/*  275 */         iw.println("if (! this.isDetached())");
/*  276 */         iw.println("{");
/*  277 */         iw.upIndent();
/*      */         
/*  279 */         iw.println("synchronized ( myProxyResultSets )");
/*  280 */         iw.println("{");
/*  281 */         iw.upIndent();
/*  282 */         iw.println("for( Iterator ii = myProxyResultSets.iterator(); ii.hasNext(); )");
/*  283 */         iw.println("{");
/*  284 */         iw.upIndent();
/*  285 */         iw.println("ResultSet closeMe = (ResultSet) ii.next();");
/*  286 */         iw.println("ii.remove();");
/*  287 */         iw.println();
/*  288 */         iw.println("try { closeMe.close(); }");
/*  289 */         iw.println("catch (SQLException e)");
/*  290 */         iw.println("{");
/*  291 */         iw.upIndent();
/*  292 */         iw.println("if (logger.isLoggable( MLevel.WARNING ))");
/*  293 */         iw.upIndent();
/*  294 */         iw.println("logger.log( MLevel.WARNING, \"Exception on close of apparently orphaned ResultSet.\", e);");
/*  295 */         iw.downIndent();
/*  296 */         iw.downIndent();
/*  297 */         iw.println("}");
/*  298 */         iw.println("if (logger.isLoggable( MLevel.FINE ))");
/*  299 */         iw.upIndent();
/*  300 */         iw.println("logger.log( MLevel.FINE, this + \" closed orphaned ResultSet: \" +closeMe);");
/*  301 */         iw.downIndent();
/*  302 */         iw.downIndent();
/*  303 */         iw.println("}");
/*  304 */         iw.downIndent();
/*  305 */         iw.println("}");
/*  306 */         iw.println();
/*  307 */         iw.println("if ( is_cached )");
/*  308 */         iw.upIndent();
/*  309 */         iw.println("parentPooledConnection.checkinStatement( inner );");
/*  310 */         iw.downIndent();
/*  311 */         iw.println("else");
/*  312 */         iw.println("{");
/*  313 */         iw.upIndent();
/*  314 */         iw.println("parentPooledConnection.markInactiveUncachedStatement( inner );");
/*      */         
/*  316 */         iw.println("try{ inner.close(); }");
/*  317 */         iw.println("catch (Exception e )");
/*  318 */         iw.println("{");
/*  319 */         iw.upIndent();
/*      */         
/*  321 */         iw.println("if (logger.isLoggable( MLevel.WARNING ))");
/*  322 */         iw.upIndent();
/*  323 */         iw.println("logger.log( MLevel.WARNING, \"Exception on close of inner statement.\", e);");
/*  324 */         iw.downIndent();
/*      */         
/*  326 */         iw.println("SQLException sqle = SqlUtils.toSQLException( e );");
/*  327 */         iw.println("throw sqle;");
/*  328 */         iw.downIndent();
/*  329 */         iw.println("}");
/*  330 */         iw.downIndent();
/*  331 */         iw.println("}");
/*      */         
/*  333 */         iw.println();
/*  334 */         iw.println("this.detach();");
/*  335 */         iw.println("this.inner = null;");
/*  336 */         iw.println("this.creatorProxy = null;");
/*      */         
/*  338 */         iw.downIndent();
/*  339 */         iw.println("}");
/*      */       }
/*  341 */       else if (mname.equals("isClosed")) {
/*      */         
/*  343 */         iw.println("return this.isDetached();");
/*      */       } else {
/*      */         
/*  346 */         super.generateDelegateCode(intfcl, genclass, method, iw);
/*      */       } 
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
/*      */     protected void generatePreDelegateCode(Class intfcl, String genclass, Method method, IndentedWriter iw) throws IOException {
/*  372 */       super.generatePreDelegateCode(intfcl, genclass, method, iw);
/*      */     }
/*      */ 
/*      */     
/*      */     protected void generatePostDelegateCode(Class intfcl, String genclass, Method method, IndentedWriter iw) throws IOException {
/*  377 */       super.generatePostDelegateCode(intfcl, genclass, method, iw);
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
/*      */     protected void generateExtraDeclarations(Class intfcl, String genclass, IndentedWriter iw) throws IOException {
/*  394 */       super.generateExtraDeclarations(intfcl, genclass, iw);
/*  395 */       iw.println();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  407 */       iw.println("boolean is_cached;");
/*  408 */       iw.println("NewProxyConnection creatorProxy;");
/*  409 */       iw.println();
/*  410 */       iw.println("// Although formally unnecessary, we sync access to myProxyResultSets on");
/*  411 */       iw.println("// that set's own lock, in case clients (illegally but not uncommonly) close()");
/*  412 */       iw.println("// the Statement from a Thread other than the one they use in general");
/*  413 */       iw.println("// with the Statement");
/*  414 */       iw.println("HashSet myProxyResultSets = new HashSet();");
/*  415 */       iw.println();
/*  416 */       iw.println("public void detachProxyResultSet( ResultSet prs )");
/*  417 */       iw.println("{");
/*  418 */       iw.upIndent();
/*      */       
/*  420 */       iw.println("synchronized (myProxyResultSets) { myProxyResultSets.remove( prs ); }");
/*  421 */       iw.downIndent();
/*  422 */       iw.println("}");
/*  423 */       iw.println();
/*  424 */       iw.print(CodegenUtils.fqcnLastElement(genclass));
/*  425 */       iw.println("( " + CodegenUtils.simpleClassName(intfcl) + " inner, NewPooledConnection parentPooledConnection, boolean cached, NewProxyConnection cProxy )");
/*      */       
/*  427 */       iw.println("{");
/*  428 */       iw.upIndent();
/*  429 */       iw.println("this( inner, parentPooledConnection );");
/*  430 */       iw.println("this.is_cached = cached;");
/*  431 */       iw.println("this.creatorProxy = cProxy;");
/*  432 */       iw.downIndent();
/*  433 */       iw.println("}");
/*  434 */       iw.println();
/*  435 */       iw.println("public Object rawStatementOperation(Method m, Object target, Object[] args) throws IllegalAccessException, InvocationTargetException, SQLException");
/*      */       
/*  437 */       iw.println("{");
/*  438 */       iw.upIndent();
/*  439 */       iw.println("maybeDirtyTransaction();");
/*  440 */       iw.println();
/*  441 */       iw.println("if (target == C3P0ProxyStatement.RAW_STATEMENT) target = inner;");
/*  442 */       iw.println("for (int i = 0, len = args.length; i < len; ++i)");
/*  443 */       iw.upIndent();
/*  444 */       iw.println("if (args[i] == C3P0ProxyStatement.RAW_STATEMENT) args[i] = inner;");
/*  445 */       iw.downIndent();
/*  446 */       iw.println("Object out = m.invoke(target, args);");
/*  447 */       iw.println("if (out instanceof ResultSet)");
/*  448 */       iw.println("{");
/*  449 */       iw.upIndent();
/*  450 */       iw.println("ResultSet innerResultSet = (ResultSet) out;");
/*  451 */       iw.println("parentPooledConnection.markActiveResultSetForStatement( inner, innerResultSet );");
/*  452 */       iw.println("out = new NewProxyResultSet( innerResultSet, parentPooledConnection, inner, this );");
/*  453 */       iw.downIndent();
/*  454 */       iw.println("}");
/*  455 */       iw.println();
/*  456 */       iw.println("return out;");
/*  457 */       iw.downIndent();
/*  458 */       iw.println("}");
/*  459 */       iw.println();
/*  460 */       iw.println("void maybeDirtyTransaction()");
/*  461 */       iw.println("{ if (creatorProxy != null) creatorProxy.maybeDirtyTransaction(); }");
/*      */     }
/*      */ 
/*      */     
/*      */     protected void generateExtraImports(IndentedWriter iw) throws IOException {
/*  466 */       super.generateExtraImports(iw);
/*  467 */       iw.println("import java.lang.reflect.InvocationTargetException;");
/*  468 */       iw.println("import java.util.HashSet;");
/*  469 */       iw.println("import java.util.Iterator;");
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
/*      */   static final class NewProxyConnectionGenerator
/*      */     extends JdbcProxyGenerator
/*      */   {
/*      */     NewProxyConnectionGenerator() {
/*  497 */       setMethodModifiers(33);
/*  498 */       setExtraInterfaces(new Class[] { C3P0ProxyConnection.class });
/*      */     }
/*      */ 
/*      */     
/*      */     protected void generateDelegateCode(Class intfcl, String genclass, Method method, IndentedWriter iw) throws IOException {
/*  503 */       String mname = method.getName();
/*  504 */       if (JdbcProxyGenerator.jdbc4WrapperMethod(mname)) {
/*      */         
/*  506 */         JdbcProxyGenerator.generateWrapperDelegateCode(intfcl, genclass, method, iw);
/*      */         
/*      */         return;
/*      */       } 
/*  510 */       if (mname.equals("createStatement")) {
/*      */         
/*  512 */         iw.println("txn_known_resolved = false;");
/*  513 */         iw.println();
/*  514 */         iw.println("Statement innerStmt = inner." + CodegenUtils.methodCall(method) + ";");
/*  515 */         iw.println("parentPooledConnection.markActiveUncachedStatement( innerStmt );");
/*  516 */         iw.println("return new NewProxyStatement( innerStmt, parentPooledConnection, false, this );");
/*      */       }
/*  518 */       else if (mname.equals("prepareStatement")) {
/*      */         
/*  520 */         iw.println("txn_known_resolved = false;");
/*  521 */         iw.println();
/*  522 */         iw.println("PreparedStatement innerStmt;");
/*  523 */         iw.println();
/*  524 */         iw.println("if ( parentPooledConnection.isStatementCaching() )");
/*  525 */         iw.println("{");
/*  526 */         iw.upIndent();
/*      */         
/*  528 */         iw.println("try");
/*  529 */         iw.println("{");
/*  530 */         iw.upIndent();
/*      */         
/*  532 */         generateFindMethodAndArgs(method, iw);
/*  533 */         iw.println("innerStmt = (PreparedStatement) parentPooledConnection.checkoutStatement( method, args );");
/*  534 */         iw.println("return new NewProxyPreparedStatement( innerStmt, parentPooledConnection, true, this );");
/*      */         
/*  536 */         iw.downIndent();
/*  537 */         iw.println("}");
/*  538 */         iw.println("catch (ResourceClosedException e)");
/*  539 */         iw.println("{");
/*  540 */         iw.upIndent();
/*      */         
/*  542 */         iw.println("if ( logger.isLoggable( MLevel.FINE ) )");
/*  543 */         iw.upIndent();
/*  544 */         iw.println("logger.log( MLevel.FINE, \"A Connection tried to prepare a Statement via a Statement cache that is already closed. This can happen -- rarely -- if a DataSource is closed or reset() while Connections are checked-out and in use.\", e );");
/*      */ 
/*      */         
/*  547 */         iw.downIndent();
/*      */ 
/*      */         
/*  550 */         iw.println("innerStmt = inner." + CodegenUtils.methodCall(method) + ";");
/*  551 */         iw.println("parentPooledConnection.markActiveUncachedStatement( innerStmt );");
/*  552 */         iw.println("return new NewProxyPreparedStatement( innerStmt, parentPooledConnection, false, this );");
/*      */         
/*  554 */         iw.downIndent();
/*  555 */         iw.println("}");
/*      */         
/*  557 */         iw.downIndent();
/*  558 */         iw.println("}");
/*  559 */         iw.println("else");
/*  560 */         iw.println("{");
/*  561 */         iw.upIndent();
/*      */ 
/*      */         
/*  564 */         iw.println("innerStmt = inner." + CodegenUtils.methodCall(method) + ";");
/*  565 */         iw.println("parentPooledConnection.markActiveUncachedStatement( innerStmt );");
/*  566 */         iw.println("return new NewProxyPreparedStatement( innerStmt, parentPooledConnection, false, this );");
/*      */         
/*  568 */         iw.downIndent();
/*  569 */         iw.println("}");
/*      */       
/*      */       }
/*  572 */       else if (mname.equals("prepareCall")) {
/*      */         
/*  574 */         iw.println("txn_known_resolved = false;");
/*  575 */         iw.println();
/*  576 */         iw.println("CallableStatement innerStmt;");
/*  577 */         iw.println();
/*  578 */         iw.println("if ( parentPooledConnection.isStatementCaching() )");
/*  579 */         iw.println("{");
/*  580 */         iw.upIndent();
/*      */         
/*  582 */         iw.println("try");
/*  583 */         iw.println("{");
/*  584 */         iw.upIndent();
/*      */         
/*  586 */         generateFindMethodAndArgs(method, iw);
/*  587 */         iw.println("innerStmt = (CallableStatement) parentPooledConnection.checkoutStatement( method, args );");
/*  588 */         iw.println("return new NewProxyCallableStatement( innerStmt, parentPooledConnection, true, this );");
/*      */         
/*  590 */         iw.downIndent();
/*  591 */         iw.println("}");
/*  592 */         iw.println("catch (ResourceClosedException e)");
/*  593 */         iw.println("{");
/*  594 */         iw.upIndent();
/*      */         
/*  596 */         iw.println("if ( logger.isLoggable( MLevel.FINE ) )");
/*  597 */         iw.upIndent();
/*  598 */         iw.println("logger.log( MLevel.FINE, \"A Connection tried to prepare a CallableStatement via a Statement cache that is already closed. This can happen -- rarely -- if a DataSource is closed or reset() while Connections are checked-out and in use.\", e );");
/*      */ 
/*      */         
/*  601 */         iw.downIndent();
/*      */ 
/*      */         
/*  604 */         iw.println("innerStmt = inner." + CodegenUtils.methodCall(method) + ";");
/*  605 */         iw.println("parentPooledConnection.markActiveUncachedStatement( innerStmt );");
/*  606 */         iw.println("return new NewProxyCallableStatement( innerStmt, parentPooledConnection, false, this );");
/*      */         
/*  608 */         iw.downIndent();
/*  609 */         iw.println("}");
/*      */         
/*  611 */         iw.downIndent();
/*  612 */         iw.println("}");
/*  613 */         iw.println("else");
/*  614 */         iw.println("{");
/*  615 */         iw.upIndent();
/*      */ 
/*      */         
/*  618 */         iw.println("innerStmt = inner." + CodegenUtils.methodCall(method) + ";");
/*  619 */         iw.println("parentPooledConnection.markActiveUncachedStatement( innerStmt );");
/*  620 */         iw.println("return new NewProxyCallableStatement( innerStmt, parentPooledConnection, false, this );");
/*      */         
/*  622 */         iw.downIndent();
/*  623 */         iw.println("}");
/*      */       
/*      */       }
/*  626 */       else if (mname.equals("getMetaData")) {
/*      */         
/*  628 */         iw.println("txn_known_resolved = false;");
/*  629 */         iw.println();
/*  630 */         iw.println("if (this.metaData == null)");
/*  631 */         iw.println("{");
/*  632 */         iw.upIndent();
/*  633 */         iw.println("DatabaseMetaData innerMetaData = inner." + CodegenUtils.methodCall(method) + ";");
/*  634 */         iw.println("this.metaData = new NewProxyDatabaseMetaData( innerMetaData, parentPooledConnection, this );");
/*  635 */         iw.downIndent();
/*  636 */         iw.println("}");
/*  637 */         iw.println("return this.metaData;");
/*      */       }
/*  639 */       else if (mname.equals("setTransactionIsolation")) {
/*      */ 
/*      */ 
/*      */         
/*  643 */         super.generateDelegateCode(intfcl, genclass, method, iw);
/*  644 */         iw.println("parentPooledConnection.markNewTxnIsolation( " + CodegenUtils.generatedArgumentName(0) + " );");
/*      */       }
/*  646 */       else if (mname.equals("setCatalog")) {
/*      */ 
/*      */ 
/*      */         
/*  650 */         super.generateDelegateCode(intfcl, genclass, method, iw);
/*  651 */         iw.println("parentPooledConnection.markNewCatalog( " + CodegenUtils.generatedArgumentName(0) + " );");
/*      */       }
/*  653 */       else if (mname.equals("setHoldability")) {
/*      */ 
/*      */ 
/*      */         
/*  657 */         super.generateDelegateCode(intfcl, genclass, method, iw);
/*  658 */         iw.println("parentPooledConnection.markNewHoldability( " + CodegenUtils.generatedArgumentName(0) + " );");
/*      */       }
/*  660 */       else if (mname.equals("setReadOnly")) {
/*      */ 
/*      */ 
/*      */         
/*  664 */         super.generateDelegateCode(intfcl, genclass, method, iw);
/*  665 */         iw.println("parentPooledConnection.markNewReadOnly( " + CodegenUtils.generatedArgumentName(0) + " );");
/*      */       }
/*  667 */       else if (mname.equals("setTypeMap")) {
/*      */ 
/*      */ 
/*      */         
/*  671 */         super.generateDelegateCode(intfcl, genclass, method, iw);
/*  672 */         iw.println("parentPooledConnection.markNewTypeMap( " + CodegenUtils.generatedArgumentName(0) + " );");
/*      */       }
/*  674 */       else if (mname.equals("getWarnings") || mname.equals("clearWarnings")) {
/*      */ 
/*      */ 
/*      */         
/*  678 */         super.generateDelegateCode(intfcl, genclass, method, iw);
/*      */       }
/*  680 */       else if (mname.equals("close")) {
/*      */         
/*  682 */         iw.println("if (! this.isDetached())");
/*  683 */         iw.println("{");
/*  684 */         iw.upIndent();
/*  685 */         iw.println("NewPooledConnection npc = parentPooledConnection;");
/*  686 */         iw.println("this.detach();");
/*  687 */         iw.println("npc.markClosedProxyConnection( this, txn_known_resolved );");
/*  688 */         iw.println("this.inner = null;");
/*  689 */         iw.downIndent();
/*  690 */         iw.println("}");
/*  691 */         iw.println("else if (Debug.DEBUG && logger.isLoggable( MLevel.FINE ))");
/*  692 */         iw.println("{");
/*  693 */         iw.upIndent();
/*  694 */         iw.println("logger.log( MLevel.FINE, this + \": close() called more than once.\" );");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  705 */         iw.downIndent();
/*  706 */         iw.println("}");
/*      */       }
/*  708 */       else if (mname.equals("isClosed")) {
/*      */         
/*  710 */         iw.println("return this.isDetached();");
/*      */       }
/*  712 */       else if (mname.equals("isValid")) {
/*      */         
/*  714 */         iw.println("if (this.isDetached()) return false;");
/*      */         
/*  716 */         super.generateDelegateCode(intfcl, genclass, method, iw);
/*      */       }
/*      */       else {
/*      */         
/*  720 */         boolean known_resolved = (mname.equals("commit") || mname.equals("rollback") || mname.equals("setAutoCommit"));
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  725 */         if (!known_resolved) {
/*      */           
/*  727 */           iw.println("txn_known_resolved = false;");
/*  728 */           iw.println();
/*      */         } 
/*  730 */         super.generateDelegateCode(intfcl, genclass, method, iw);
/*  731 */         if (known_resolved) {
/*      */           
/*  733 */           iw.println();
/*  734 */           iw.println("txn_known_resolved = true;");
/*      */         } 
/*      */       } 
/*      */     } String getInnerTypeName() {
/*      */       return "Connection";
/*      */     }
/*      */     protected void generateExtraDeclarations(Class intfcl, String genclass, IndentedWriter iw) throws IOException {
/*  741 */       iw.println("boolean txn_known_resolved = true;");
/*  742 */       iw.println();
/*  743 */       iw.println("DatabaseMetaData metaData = null;");
/*  744 */       iw.println();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  760 */       iw.println("public Object rawConnectionOperation(Method m, Object target, Object[] args)");
/*  761 */       iw.upIndent();
/*  762 */       iw.println("throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, SQLException");
/*  763 */       iw.downIndent();
/*  764 */       iw.println("{");
/*  765 */       iw.upIndent();
/*  766 */       iw.println("maybeDirtyTransaction();");
/*  767 */       iw.println();
/*  768 */       iw.println("if (inner == null)");
/*  769 */       iw.upIndent();
/*  770 */       iw.println("throw new SQLException(\"You cannot operate on a closed Connection!\");");
/*  771 */       iw.downIndent();
/*      */       
/*  773 */       iw.println("if ( target == C3P0ProxyConnection.RAW_CONNECTION)");
/*  774 */       iw.upIndent();
/*  775 */       iw.println("target = inner;");
/*  776 */       iw.downIndent();
/*      */       
/*  778 */       iw.println("for (int i = 0, len = args.length; i < len; ++i)");
/*  779 */       iw.upIndent();
/*  780 */       iw.println("if (args[i] == C3P0ProxyConnection.RAW_CONNECTION)");
/*  781 */       iw.upIndent();
/*  782 */       iw.println("args[i] = inner;");
/*  783 */       iw.downIndent();
/*  784 */       iw.downIndent();
/*      */       
/*  786 */       iw.println("Object out = m.invoke( target, args );");
/*  787 */       iw.println();
/*  788 */       iw.println("// we never cache Statements generated by an operation on the raw Connection");
/*  789 */       iw.println("if (out instanceof CallableStatement)");
/*  790 */       iw.println("{");
/*  791 */       iw.upIndent();
/*  792 */       iw.println("CallableStatement innerStmt = (CallableStatement) out;");
/*  793 */       iw.println("parentPooledConnection.markActiveUncachedStatement( innerStmt );");
/*  794 */       iw.println("out = new NewProxyCallableStatement( innerStmt, parentPooledConnection, false, this );");
/*  795 */       iw.downIndent();
/*  796 */       iw.println("}");
/*  797 */       iw.println("else if (out instanceof PreparedStatement)");
/*  798 */       iw.println("{");
/*  799 */       iw.upIndent();
/*  800 */       iw.println("PreparedStatement innerStmt = (PreparedStatement) out;");
/*  801 */       iw.println("parentPooledConnection.markActiveUncachedStatement( innerStmt );");
/*  802 */       iw.println("out = new NewProxyPreparedStatement( innerStmt, parentPooledConnection, false, this );");
/*  803 */       iw.downIndent();
/*  804 */       iw.println("}");
/*  805 */       iw.println("else if (out instanceof Statement)");
/*  806 */       iw.println("{");
/*  807 */       iw.upIndent();
/*  808 */       iw.println("Statement innerStmt = (Statement) out;");
/*  809 */       iw.println("parentPooledConnection.markActiveUncachedStatement( innerStmt );");
/*  810 */       iw.println("out = new NewProxyStatement( innerStmt, parentPooledConnection, false, this );");
/*  811 */       iw.downIndent();
/*  812 */       iw.println("}");
/*  813 */       iw.println("else if (out instanceof ResultSet)");
/*  814 */       iw.println("{");
/*  815 */       iw.upIndent();
/*  816 */       iw.println("ResultSet innerRs = (ResultSet) out;");
/*  817 */       iw.println("parentPooledConnection.markActiveRawConnectionResultSet( innerRs );");
/*  818 */       iw.println("out = new NewProxyResultSet( innerRs, parentPooledConnection, inner, this );");
/*  819 */       iw.downIndent();
/*  820 */       iw.println("}");
/*  821 */       iw.println("else if (out instanceof DatabaseMetaData)");
/*  822 */       iw.upIndent();
/*  823 */       iw.println("out = new NewProxyDatabaseMetaData( (DatabaseMetaData) out, parentPooledConnection );");
/*  824 */       iw.downIndent();
/*  825 */       iw.println("return out;");
/*  826 */       iw.downIndent();
/*  827 */       iw.println("}");
/*  828 */       iw.println();
/*  829 */       iw.println("synchronized void maybeDirtyTransaction()");
/*  830 */       iw.println("{ txn_known_resolved = false; }");
/*      */       
/*  832 */       super.generateExtraDeclarations(intfcl, genclass, iw);
/*      */     }
/*      */ 
/*      */     
/*      */     void generateFindMethodAndArgs(Method method, IndentedWriter iw) throws IOException {
/*  837 */       iw.println("Class[] argTypes = ");
/*  838 */       iw.println("{");
/*  839 */       iw.upIndent();
/*      */       
/*  841 */       Class[] argTypes = method.getParameterTypes(); int i, len;
/*  842 */       for (i = 0, len = argTypes.length; i < len; i++) {
/*      */         
/*  844 */         if (i != 0) iw.println(","); 
/*  845 */         iw.print(CodegenUtils.simpleClassName(argTypes[i]) + ".class");
/*      */       } 
/*  847 */       iw.println();
/*  848 */       iw.downIndent();
/*  849 */       iw.println("};");
/*  850 */       iw.println("Method method = Connection.class.getMethod( \"" + method.getName() + "\" , argTypes );");
/*  851 */       iw.println();
/*  852 */       iw.println("Object[] args = ");
/*  853 */       iw.println("{");
/*  854 */       iw.upIndent();
/*      */       
/*  856 */       for (i = 0, len = argTypes.length; i < len; i++) {
/*      */         
/*  858 */         if (i != 0) iw.println(","); 
/*  859 */         String argName = CodegenUtils.generatedArgumentName(i);
/*  860 */         Class<boolean> argType = argTypes[i];
/*  861 */         if (argType.isPrimitive()) {
/*      */           
/*  863 */           if (argType == boolean.class) {
/*  864 */             iw.print("Boolean.valueOf( " + argName + " )");
/*  865 */           } else if (argType == byte.class) {
/*  866 */             iw.print("new Byte( " + argName + " )");
/*  867 */           } else if (argType == char.class) {
/*  868 */             iw.print("new Character( " + argName + " )");
/*  869 */           } else if (argType == short.class) {
/*  870 */             iw.print("new Short( " + argName + " )");
/*  871 */           } else if (argType == int.class) {
/*  872 */             iw.print("new Integer( " + argName + " )");
/*  873 */           } else if (argType == long.class) {
/*  874 */             iw.print("new Long( " + argName + " )");
/*  875 */           } else if (argType == float.class) {
/*  876 */             iw.print("new Float( " + argName + " )");
/*  877 */           } else if (argType == double.class) {
/*  878 */             iw.print("new Double( " + argName + " )");
/*      */           } 
/*      */         } else {
/*  881 */           iw.print(argName);
/*      */         } 
/*      */       } 
/*  884 */       iw.downIndent();
/*  885 */       iw.println("};");
/*      */     }
/*      */ 
/*      */     
/*      */     protected void generateExtraImports(IndentedWriter iw) throws IOException {
/*  890 */       super.generateExtraImports(iw);
/*  891 */       iw.println("import java.lang.reflect.InvocationTargetException;");
/*  892 */       iw.println("import com.mchange.v2.util.ResourceClosedException;");
/*      */     }
/*      */ 
/*      */     
/*      */     protected void generatePreDelegateCode(Class intfcl, String genclass, Method method, IndentedWriter iw) throws IOException {
/*  897 */       if ("setClientInfo".equals(method.getName())) {
/*      */         
/*  899 */         iw.println("try");
/*  900 */         iw.println("{");
/*  901 */         iw.upIndent();
/*      */         
/*  903 */         super.generatePreDelegateCode(intfcl, genclass, method, iw);
/*      */       }
/*      */       else {
/*      */         
/*  907 */         super.generatePreDelegateCode(intfcl, genclass, method, iw);
/*      */       } 
/*      */     }
/*      */     
/*      */     protected void generatePostDelegateCode(Class intfcl, String genclass, Method method, IndentedWriter iw) throws IOException {
/*  912 */       if ("setClientInfo".equals(method.getName())) {
/*      */         
/*  914 */         super.generatePostDelegateCode(intfcl, genclass, method, iw);
/*      */         
/*  916 */         iw.downIndent();
/*  917 */         iw.println("}");
/*  918 */         iw.println("catch (Exception e)");
/*  919 */         iw.println("{ throw SqlUtils.toSQLClientInfoException( e ); }");
/*      */       } else {
/*      */         
/*  922 */         super.generatePostDelegateCode(intfcl, genclass, method, iw);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected void generateDelegateCode(Class intfcl, String genclass, Method method, IndentedWriter iw) throws IOException {
/*  929 */     super.generateDelegateCode(intfcl, genclass, method, iw);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void generatePreDelegateCode(Class intfcl, String genclass, Method method, IndentedWriter iw) throws IOException {
/*  934 */     if (!jdbc4WrapperMethod(method.getName())) {
/*  935 */       generateTryOpener(iw);
/*      */     }
/*      */   }
/*      */   
/*      */   protected void generatePostDelegateCode(Class intfcl, String genclass, Method method, IndentedWriter iw) throws IOException {
/*  940 */     if (!jdbc4WrapperMethod(method.getName())) {
/*  941 */       generateTryCloserAndCatch(intfcl, genclass, method, iw);
/*      */     }
/*      */   }
/*      */   
/*      */   void generateTryOpener(IndentedWriter iw) throws IOException {
/*  946 */     iw.println("try");
/*  947 */     iw.println("{");
/*  948 */     iw.upIndent();
/*      */   }
/*      */ 
/*      */   
/*      */   void generateTryCloserAndCatch(Class intfcl, String genclass, Method method, IndentedWriter iw) throws IOException {
/*  953 */     iw.downIndent();
/*  954 */     iw.println("}");
/*  955 */     iw.println("catch (NullPointerException exc)");
/*  956 */     iw.println("{");
/*  957 */     iw.upIndent();
/*  958 */     iw.println("if ( this.isDetached() )");
/*  959 */     iw.println("{");
/*  960 */     iw.upIndent();
/*      */ 
/*      */     
/*  963 */     if ("close".equals(method.getName())) {
/*      */       
/*  965 */       iw.println("if (Debug.DEBUG && logger.isLoggable( MLevel.FINE ))");
/*  966 */       iw.println("{");
/*  967 */       iw.upIndent();
/*  968 */       iw.println("logger.log( MLevel.FINE, this + \": close() called more than once.\" );");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  979 */       iw.downIndent();
/*  980 */       iw.println("}");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  993 */       iw.println("throw SqlUtils.toSQLException(\"You can't operate on a closed " + getInnerTypeName() + "!!!\", exc);");
/*      */     } 
/*  995 */     iw.downIndent();
/*  996 */     iw.println("}");
/*  997 */     iw.println("else throw exc;");
/*  998 */     iw.downIndent();
/*  999 */     iw.println("}");
/* 1000 */     iw.println("catch (Exception exc)");
/* 1001 */     iw.println("{");
/* 1002 */     iw.upIndent();
/* 1003 */     iw.println("if (! this.isDetached())");
/* 1004 */     iw.println("{");
/* 1005 */     iw.upIndent();
/*      */     
/* 1007 */     iw.println("throw parentPooledConnection.handleThrowable( exc );");
/* 1008 */     iw.downIndent();
/* 1009 */     iw.println("}");
/* 1010 */     iw.println("else throw SqlUtils.toSQLException( exc );");
/* 1011 */     iw.downIndent();
/* 1012 */     iw.println("}");
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
/*      */   protected void generateExtraDeclarations(Class intfcl, String genclass, IndentedWriter iw) throws IOException {
/* 1027 */     iw.println("private final static MLogger logger = MLog.getLogger( \"" + genclass + "\" );");
/* 1028 */     iw.println();
/*      */     
/* 1030 */     iw.println("volatile NewPooledConnection parentPooledConnection;");
/* 1031 */     iw.println();
/*      */     
/* 1033 */     iw.println("ConnectionEventListener cel = new ConnectionEventListener()");
/* 1034 */     iw.println("{");
/* 1035 */     iw.upIndent();
/*      */     
/* 1037 */     iw.println("public void connectionErrorOccurred(ConnectionEvent evt)");
/* 1038 */     iw.println("{ /* DON'T detach()... IGNORE -- this could be an ordinary error. Leave it to the PooledConnection to test, but leave proxies intact */ }");
/*      */ 
/*      */     
/* 1041 */     iw.println();
/* 1042 */     iw.println("public void connectionClosed(ConnectionEvent evt)");
/* 1043 */     iw.println("{ detach(); }");
/*      */     
/* 1045 */     iw.downIndent();
/* 1046 */     iw.println("};");
/* 1047 */     iw.println();
/*      */     
/* 1049 */     iw.println("void attach( NewPooledConnection parentPooledConnection )");
/* 1050 */     iw.println("{");
/* 1051 */     iw.upIndent();
/*      */     
/* 1053 */     iw.println("this.parentPooledConnection = parentPooledConnection;");
/* 1054 */     iw.println("parentPooledConnection.addConnectionEventListener( cel );");
/* 1055 */     iw.downIndent();
/* 1056 */     iw.println("}");
/* 1057 */     iw.println();
/* 1058 */     iw.println("private void detach()");
/* 1059 */     iw.println("{");
/* 1060 */     iw.upIndent();
/*      */ 
/*      */     
/* 1063 */     writeDetachBody(iw);
/*      */     
/* 1065 */     iw.downIndent();
/* 1066 */     iw.println("}");
/* 1067 */     iw.println();
/* 1068 */     iw.print(CodegenUtils.fqcnLastElement(genclass));
/* 1069 */     iw.println("( " + CodegenUtils.simpleClassName(intfcl) + " inner, NewPooledConnection parentPooledConnection )");
/* 1070 */     iw.println("{");
/* 1071 */     iw.upIndent();
/* 1072 */     iw.println("this( inner );");
/* 1073 */     iw.println("attach( parentPooledConnection );");
/* 1074 */     generateExtraConstructorCode(intfcl, genclass, iw);
/* 1075 */     iw.downIndent();
/* 1076 */     iw.println("}");
/* 1077 */     iw.println();
/* 1078 */     iw.println("boolean isDetached()");
/* 1079 */     iw.println("{ return (this.parentPooledConnection == null); }");
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
/*      */ 
/*      */ 
/*      */   
/*      */   protected void writeDetachBody(IndentedWriter iw) throws IOException {
/* 1117 */     iw.println("parentPooledConnection.removeConnectionEventListener( cel );");
/* 1118 */     iw.println("parentPooledConnection = null;");
/*      */   }
/*      */ 
/*      */   
/*      */   protected void generateExtraImports(IndentedWriter iw) throws IOException {
/* 1123 */     iw.println("import java.sql.*;");
/* 1124 */     iw.println("import javax.sql.*;");
/* 1125 */     iw.println("import com.mchange.v2.log.*;");
/* 1126 */     iw.println("import java.lang.reflect.Method;");
/* 1127 */     iw.println("import com.mchange.v2.sql.SqlUtils;");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   void generateExtraConstructorCode(Class intfcl, String genclass, IndentedWriter iw) throws IOException {}
/*      */ 
/*      */   
/*      */   private static void generateWrapperDelegateCode(Class intfcl, String genclass, Method method, IndentedWriter iw) throws IOException {
/* 1136 */     String mname = method.getName();
/* 1137 */     if ("isWrapperFor".equals(mname)) {
/*      */ 
/*      */       
/* 1140 */       String wrappedIntfc = intfcl.getName() + ".class";
/* 1141 */       String wrappedClass = "inner.getClass()";
/* 1142 */       iw.println("return ( " + wrappedIntfc + "== a || a.isAssignableFrom( " + wrappedClass + " ) );");
/*      */     }
/* 1144 */     else if ("unwrap".equals(mname)) {
/*      */       
/* 1146 */       iw.println("if (this.isWrapperFor( a )) return inner;");
/* 1147 */       iw.println("else throw new SQLException( this + \" is not a wrapper for \" + a.getName());");
/*      */     } 
/*      */   }
/*      */   
/*      */   private static boolean jdbc4WrapperMethod(String mname) {
/* 1152 */     return ("unwrap".equals(mname) || "isWrapperFor".equals(mname));
/*      */   }
/*      */ 
/*      */   
/*      */   public static void main(String[] argv) {
/*      */     try {
/* 1158 */       if (argv.length != 1) {
/*      */         
/* 1160 */         System.err.println("java " + JdbcProxyGenerator.class.getName() + " <source-root-directory>");
/*      */         
/*      */         return;
/*      */       } 
/* 1164 */       File srcroot = new File(argv[0]);
/* 1165 */       if (!srcroot.exists() || !srcroot.canWrite()) {
/*      */         
/* 1167 */         System.err.println(JdbcProxyGenerator.class.getName() + " -- sourceroot: " + argv[0] + " must exist and be writable");
/*      */         
/*      */         return;
/*      */       } 
/* 1171 */       DelegatorGenerator mdgen = new NewProxyMetaDataGenerator();
/* 1172 */       DelegatorGenerator rsgen = new NewProxyResultSetGenerator();
/* 1173 */       DelegatorGenerator stgen = new NewProxyAnyStatementGenerator();
/* 1174 */       DelegatorGenerator cngen = new NewProxyConnectionGenerator();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1190 */       genclass(cngen, Connection.class, "com.mchange.v2.c3p0.impl.NewProxyConnection", srcroot);
/* 1191 */       genclass(stgen, Statement.class, "com.mchange.v2.c3p0.impl.NewProxyStatement", srcroot);
/*      */ 
/*      */       
/* 1194 */       genclass(stgen, PreparedStatement.class, "com.mchange.v2.c3p0.impl.NewProxyPreparedStatement", srcroot);
/* 1195 */       genclass(stgen, CallableStatement.class, "com.mchange.v2.c3p0.impl.NewProxyCallableStatement", srcroot);
/* 1196 */       genclass(rsgen, ResultSet.class, "com.mchange.v2.c3p0.impl.NewProxyResultSet", srcroot);
/* 1197 */       genclass(mdgen, DatabaseMetaData.class, "com.mchange.v2.c3p0.impl.NewProxyDatabaseMetaData", srcroot);
/*      */     }
/* 1199 */     catch (Exception e) {
/* 1200 */       e.printStackTrace();
/*      */     } 
/*      */   }
/*      */   
/*      */   static void genclass(DelegatorGenerator dg, Class intfcl, String fqcn, File srcroot) throws IOException {
/* 1205 */     File genDir = new File(srcroot, dirForFqcn(fqcn));
/* 1206 */     if (!genDir.exists()) {
/*      */       
/* 1208 */       System.err.println(JdbcProxyGenerator.class.getName() + " -- creating directory: " + genDir.getAbsolutePath());
/* 1209 */       genDir.mkdirs();
/*      */     } 
/* 1211 */     String fileName = CodegenUtils.fqcnLastElement(fqcn) + ".java";
/* 1212 */     Writer w = null;
/*      */     
/*      */     try {
/* 1215 */       w = new BufferedWriter(new FileWriter(new File(genDir, fileName)));
/* 1216 */       dg.writeDelegator(intfcl, fqcn, w);
/* 1217 */       w.flush();
/* 1218 */       System.err.println("Generated " + fileName);
/*      */     } finally {
/*      */       
/*      */       try {
/* 1222 */         if (w != null) w.close(); 
/* 1223 */       } catch (Exception e) {
/* 1224 */         e.printStackTrace();
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   static String dirForFqcn(String fqcn) {
/* 1230 */     int last_dot = fqcn.lastIndexOf('.');
/* 1231 */     StringBuffer sb = new StringBuffer(fqcn.substring(0, last_dot + 1));
/* 1232 */     for (int i = 0, len = sb.length(); i < len; i++) {
/* 1233 */       if (sb.charAt(i) == '.')
/* 1234 */         sb.setCharAt(i, '/'); 
/* 1235 */     }  return sb.toString();
/*      */   }
/*      */   
/*      */   abstract String getInnerTypeName();
/*      */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/codegen/JdbcProxyGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */