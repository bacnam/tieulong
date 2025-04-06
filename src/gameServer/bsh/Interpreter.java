/*      */ package bsh;
/*      */ 
/*      */ import java.io.BufferedReader;
/*      */ import java.io.File;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.FileReader;
/*      */ import java.io.FilterInputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.io.Reader;
/*      */ import java.io.Serializable;
/*      */ import java.io.StringReader;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Method;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Interpreter
/*      */   implements Runnable, ConsoleInterface, Serializable
/*      */ {
/*      */   public static final String VERSION = "2.0b5";
/*      */   public static boolean DEBUG;
/*      */   public static boolean TRACE;
/*      */   public static boolean LOCALSCOPING;
/*      */   static transient PrintStream debug;
/*  111 */   static String systemLineSeparator = "\n"; static This sharedObject;
/*      */   
/*      */   static {
/*  114 */     staticInit();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean strictJava = false;
/*      */ 
/*      */ 
/*      */   
/*      */   transient Parser parser;
/*      */ 
/*      */ 
/*      */   
/*      */   NameSpace globalNameSpace;
/*      */ 
/*      */ 
/*      */   
/*      */   transient Reader in;
/*      */ 
/*      */ 
/*      */   
/*      */   transient PrintStream out;
/*      */ 
/*      */ 
/*      */   
/*      */   transient PrintStream err;
/*      */ 
/*      */ 
/*      */   
/*      */   ConsoleInterface console;
/*      */ 
/*      */ 
/*      */   
/*      */   Interpreter parent;
/*      */ 
/*      */ 
/*      */   
/*      */   String sourceFileInfo;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean exitOnEOF = true;
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean evalOnly;
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean interactive;
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean showResults;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Interpreter(Reader in, PrintStream out, PrintStream err, boolean interactive, NameSpace namespace, Interpreter parent, String sourceFileInfo) {
/*  175 */     this.parser = new Parser(in);
/*  176 */     long t1 = System.currentTimeMillis();
/*  177 */     this.in = in;
/*  178 */     this.out = out;
/*  179 */     this.err = err;
/*  180 */     this.interactive = interactive;
/*  181 */     debug = err;
/*  182 */     this.parent = parent;
/*  183 */     if (parent != null)
/*  184 */       setStrictJava(parent.getStrictJava()); 
/*  185 */     this.sourceFileInfo = sourceFileInfo;
/*      */     
/*  187 */     BshClassManager bcm = BshClassManager.createClassManager(this);
/*  188 */     if (namespace == null) {
/*  189 */       this.globalNameSpace = new NameSpace(bcm, "global");
/*      */     } else {
/*  191 */       this.globalNameSpace = namespace;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  200 */     if (!(getu("bsh") instanceof This)) {
/*  201 */       initRootSystemObject();
/*      */     }
/*  203 */     if (interactive) {
/*  204 */       loadRCFiles();
/*      */     }
/*  206 */     long t2 = System.currentTimeMillis();
/*  207 */     if (DEBUG) {
/*  208 */       debug("Time to initialize interpreter: " + (t2 - t1));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Interpreter(Reader in, PrintStream out, PrintStream err, boolean interactive, NameSpace namespace) {
/*  215 */     this(in, out, err, interactive, namespace, null, null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Interpreter(Reader in, PrintStream out, PrintStream err, boolean interactive) {
/*  221 */     this(in, out, err, interactive, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Interpreter(ConsoleInterface console, NameSpace globalNameSpace) {
/*  230 */     this(console.getIn(), console.getOut(), console.getErr(), true, globalNameSpace);
/*      */ 
/*      */     
/*  233 */     setConsole(console);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Interpreter(ConsoleInterface console) {
/*  241 */     this(console, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Interpreter() {
/*  249 */     this(new StringReader(""), System.out, System.err, false, null);
/*      */     
/*  251 */     this.evalOnly = true;
/*  252 */     setu("bsh.evalOnly", new Primitive(true));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setConsole(ConsoleInterface console) {
/*  262 */     this.console = console;
/*  263 */     setu("bsh.console", console);
/*      */     
/*  265 */     setOut(console.getOut());
/*  266 */     setErr(console.getErr());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void initRootSystemObject() {
/*  272 */     BshClassManager bcm = getClassManager();
/*      */     
/*  274 */     setu("bsh", (new NameSpace(bcm, "Bsh Object")).getThis(this));
/*      */ 
/*      */     
/*  277 */     if (sharedObject == null) {
/*  278 */       sharedObject = (new NameSpace(bcm, "Bsh Shared System Object")).getThis(this);
/*      */     }
/*      */     
/*  281 */     setu("bsh.system", sharedObject);
/*  282 */     setu("bsh.shared", sharedObject);
/*      */ 
/*      */     
/*  285 */     This helpText = (new NameSpace(bcm, "Bsh Command Help Text")).getThis(this);
/*      */     
/*  287 */     setu("bsh.help", helpText);
/*      */ 
/*      */     
/*      */     try {
/*  291 */       setu("bsh.cwd", System.getProperty("user.dir"));
/*  292 */     } catch (SecurityException e) {
/*      */       
/*  294 */       setu("bsh.cwd", ".");
/*      */     } 
/*      */ 
/*      */     
/*  298 */     setu("bsh.interactive", new Primitive(this.interactive));
/*      */     
/*  300 */     setu("bsh.evalOnly", new Primitive(this.evalOnly));
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
/*      */   public void setNameSpace(NameSpace globalNameSpace) {
/*  319 */     this.globalNameSpace = globalNameSpace;
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
/*      */   public NameSpace getNameSpace() {
/*  338 */     return this.globalNameSpace;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void main(String[] args) {
/*  346 */     if (args.length > 0) {
/*  347 */       String bshArgs[], filename = args[0];
/*      */ 
/*      */       
/*  350 */       if (args.length > 1) {
/*  351 */         bshArgs = new String[args.length - 1];
/*  352 */         System.arraycopy(args, 1, bshArgs, 0, args.length - 1);
/*      */       } else {
/*  354 */         bshArgs = new String[0];
/*      */       } 
/*  356 */       Interpreter interpreter = new Interpreter();
/*      */       
/*  358 */       interpreter.setu("bsh.args", bshArgs);
/*      */       try {
/*  360 */         Object result = interpreter.source(filename, interpreter.globalNameSpace);
/*      */         
/*  362 */         if (result instanceof Class) {
/*      */           try {
/*  364 */             invokeMain((Class)result, bshArgs);
/*  365 */           } catch (Exception e) {
/*      */             
/*  367 */             Object o = e;
/*  368 */             if (e instanceof InvocationTargetException) {
/*  369 */               o = ((InvocationTargetException)e).getTargetException();
/*      */             }
/*  371 */             System.err.println("Class: " + result + " main method threw exception:" + o);
/*      */           } 
/*      */         }
/*  374 */       } catch (FileNotFoundException e) {
/*  375 */         System.out.println("File not found: " + e);
/*  376 */       } catch (TargetError e) {
/*  377 */         System.out.println("Script threw exception: " + e);
/*  378 */         if (e.inNativeCode())
/*  379 */           e.printStackTrace(DEBUG, System.err); 
/*  380 */       } catch (EvalError e) {
/*  381 */         System.out.println("Evaluation Error: " + e);
/*  382 */       } catch (IOException e) {
/*  383 */         System.out.println("I/O Error: " + e);
/*      */       } 
/*      */     } else {
/*      */       InputStream src;
/*      */ 
/*      */ 
/*      */       
/*  390 */       if (System.getProperty("os.name").startsWith("Windows") && System.getProperty("java.version").startsWith("1.1.")) {
/*      */ 
/*      */         
/*  393 */         src = new FilterInputStream(System.in) {
/*      */             public int available() throws IOException {
/*  395 */               return 0;
/*      */             }
/*      */           };
/*      */       } else {
/*      */         
/*  400 */         src = System.in;
/*      */       } 
/*  402 */       Reader in = new CommandLineReader(new InputStreamReader(src));
/*  403 */       Interpreter interpreter = new Interpreter(in, System.out, System.err, true);
/*      */       
/*  405 */       interpreter.run();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static void invokeMain(Class clas, String[] args) throws Exception {
/*  412 */     Method main = Reflect.resolveJavaMethod(null, clas, "main", new Class[] { String[].class }, true);
/*      */ 
/*      */     
/*  415 */     if (main != null) {
/*  416 */       main.invoke(null, new Object[] { args });
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void run() {
/*  424 */     if (this.evalOnly) {
/*  425 */       throw new RuntimeException("bsh Interpreter: No stream");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  432 */     if (this.interactive) {
/*      */       try {
/*  434 */         eval("printBanner();");
/*  435 */       } catch (EvalError e) {
/*  436 */         println("BeanShell 2.0b5 - by Pat Niemeyer (pat@pat.net)");
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*  441 */     CallStack callstack = new CallStack(this.globalNameSpace);
/*      */     
/*  443 */     boolean eof = false;
/*  444 */     while (!eof) {
/*      */ 
/*      */       
/*      */       try {
/*      */         
/*  449 */         System.out.flush();
/*  450 */         System.err.flush();
/*  451 */         Thread.yield();
/*      */         
/*  453 */         if (this.interactive) {
/*  454 */           print(getBshPrompt());
/*      */         }
/*  456 */         eof = Line();
/*      */         
/*  458 */         if (get_jjtree().nodeArity() > 0) {
/*      */           
/*  460 */           SimpleNode node = (SimpleNode)get_jjtree().rootNode();
/*      */           
/*  462 */           if (DEBUG) {
/*  463 */             node.dump(">");
/*      */           }
/*  465 */           Object ret = node.eval(callstack, this);
/*      */ 
/*      */           
/*  468 */           if (callstack.depth() > 1) {
/*  469 */             throw new InterpreterError("Callstack growing: " + callstack);
/*      */           }
/*      */           
/*  472 */           if (ret instanceof ReturnControl) {
/*  473 */             ret = ((ReturnControl)ret).value;
/*      */           }
/*  475 */           if (ret != Primitive.VOID) {
/*      */             
/*  477 */             setu("$_", ret);
/*  478 */             if (this.showResults) {
/*  479 */               println("<" + ret + ">");
/*      */             }
/*      */           } 
/*      */         } 
/*  483 */       } catch (ParseException e) {
/*      */         
/*  485 */         error("Parser Error: " + e.getMessage(DEBUG));
/*  486 */         if (DEBUG)
/*  487 */           e.printStackTrace(); 
/*  488 */         if (!this.interactive) {
/*  489 */           eof = true;
/*      */         }
/*  491 */         this.parser.reInitInput(this.in);
/*      */       }
/*  493 */       catch (InterpreterError e) {
/*      */         
/*  495 */         error("Internal Error: " + e.getMessage());
/*  496 */         e.printStackTrace();
/*  497 */         if (!this.interactive) {
/*  498 */           eof = true;
/*      */         }
/*  500 */       } catch (TargetError e) {
/*      */         
/*  502 */         error("// Uncaught Exception: " + e);
/*  503 */         if (e.inNativeCode())
/*  504 */           e.printStackTrace(DEBUG, this.err); 
/*  505 */         if (!this.interactive)
/*  506 */           eof = true; 
/*  507 */         setu("$_e", e.getTarget());
/*      */       }
/*  509 */       catch (EvalError e) {
/*      */         
/*  511 */         if (this.interactive) {
/*  512 */           error("EvalError: " + e.toString());
/*      */         } else {
/*  514 */           error("EvalError: " + e.getMessage());
/*      */         } 
/*  516 */         if (DEBUG) {
/*  517 */           e.printStackTrace();
/*      */         }
/*  519 */         if (!this.interactive) {
/*  520 */           eof = true;
/*      */         }
/*  522 */       } catch (Exception e) {
/*      */         
/*  524 */         error("Unknown error: " + e);
/*  525 */         if (DEBUG)
/*  526 */           e.printStackTrace(); 
/*  527 */         if (!this.interactive) {
/*  528 */           eof = true;
/*      */         }
/*  530 */       } catch (TokenMgrError e) {
/*      */         
/*  532 */         error("Error parsing input: " + e);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  539 */         this.parser.reInitTokenInput(this.in);
/*      */         
/*  541 */         if (!this.interactive) {
/*  542 */           eof = true;
/*      */         }
/*      */       } finally {
/*      */         
/*  546 */         get_jjtree().reset();
/*      */         
/*  548 */         if (callstack.depth() > 1) {
/*  549 */           callstack.clear();
/*  550 */           callstack.push(this.globalNameSpace);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/*  555 */     if (this.interactive && this.exitOnEOF) {
/*  556 */       System.exit(0);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object source(String filename, NameSpace nameSpace) throws FileNotFoundException, IOException, EvalError {
/*  567 */     File file = pathToFile(filename);
/*  568 */     if (DEBUG) debug("Sourcing file: " + file); 
/*  569 */     Reader sourceIn = new BufferedReader(new FileReader(file));
/*      */     try {
/*  571 */       return eval(sourceIn, nameSpace, filename);
/*      */     } finally {
/*  573 */       sourceIn.close();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object source(String filename) throws FileNotFoundException, IOException, EvalError {
/*  584 */     return source(filename, this.globalNameSpace);
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
/*      */   public Object eval(Reader in, NameSpace nameSpace, String sourceFileInfo) throws EvalError {
/*  615 */     Object retVal = null;
/*  616 */     if (DEBUG) debug("eval: nameSpace = " + nameSpace);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  623 */     Interpreter localInterpreter = new Interpreter(in, this.out, this.err, false, nameSpace, this, sourceFileInfo);
/*      */ 
/*      */ 
/*      */     
/*  627 */     CallStack callstack = new CallStack(nameSpace);
/*      */     
/*  629 */     boolean eof = false;
/*  630 */     while (!eof) {
/*      */       
/*  632 */       SimpleNode node = null;
/*      */ 
/*      */       
/*  635 */       try { eof = localInterpreter.Line();
/*  636 */         if (localInterpreter.get_jjtree().nodeArity() > 0)
/*      */         
/*  638 */         { node = (SimpleNode)localInterpreter.get_jjtree().rootNode();
/*      */           
/*  640 */           node.setSourceFile(sourceFileInfo);
/*      */           
/*  642 */           if (TRACE) {
/*  643 */             println("// " + node.getText());
/*      */           }
/*  645 */           retVal = node.eval(callstack, localInterpreter);
/*      */ 
/*      */           
/*  648 */           if (callstack.depth() > 1) {
/*  649 */             throw new InterpreterError("Callstack growing: " + callstack);
/*      */           }
/*      */           
/*  652 */           if (retVal instanceof ReturnControl)
/*  653 */           { retVal = ((ReturnControl)retVal).value;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  703 */             localInterpreter.get_jjtree().reset();
/*      */ 
/*      */             
/*  706 */             if (callstack.depth() > 1)
/*  707 */             { callstack.clear();
/*  708 */               callstack.push(nameSpace); }  break; }  if (localInterpreter.showResults && retVal != Primitive.VOID) println("<" + retVal + ">");  }  } catch (ParseException e) { if (DEBUG) error(e.getMessage(DEBUG));  e.setErrorSourceFile(sourceFileInfo); throw e; } catch (InterpreterError e) { e.printStackTrace(); throw new EvalError("Sourced file: " + sourceFileInfo + " internal Error: " + e.getMessage(), node, callstack); } catch (TargetError e) { if (e.getNode() == null) e.setNode(node);  e.reThrow("Sourced file: " + sourceFileInfo); } catch (EvalError e) { if (DEBUG) e.printStackTrace();  if (e.getNode() == null) e.setNode(node);  e.reThrow("Sourced file: " + sourceFileInfo); } catch (Exception e) { if (DEBUG) e.printStackTrace();  throw new EvalError("Sourced file: " + sourceFileInfo + " unknown error: " + e.getMessage(), node, callstack); } catch (TokenMgrError e) { throw new EvalError("Sourced file: " + sourceFileInfo + " Token Parsing Error: " + e.getMessage(), node, callstack); } finally { localInterpreter.get_jjtree().reset(); if (callstack.depth() > 1) { callstack.clear(); callstack.push(nameSpace); }
/*      */          }
/*      */     
/*      */     } 
/*  712 */     return Primitive.unwrap(retVal);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object eval(Reader in) throws EvalError {
/*  720 */     return eval(in, this.globalNameSpace, "eval stream");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object eval(String statements) throws EvalError {
/*  727 */     if (DEBUG) debug("eval(String): " + statements); 
/*  728 */     return eval(statements, this.globalNameSpace);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object eval(String statements, NameSpace nameSpace) throws EvalError {
/*  738 */     String s = statements.endsWith(";") ? statements : (statements + ";");
/*  739 */     return eval(new StringReader(s), nameSpace, "inline evaluation of: ``" + showEvalString(s) + "''");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private String showEvalString(String s) {
/*  745 */     s = s.replace('\n', ' ');
/*  746 */     s = s.replace('\r', ' ');
/*  747 */     if (s.length() > 80)
/*  748 */       s = s.substring(0, 80) + " . . . "; 
/*  749 */     return s;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public final void error(Object o) {
/*  760 */     if (this.console != null) {
/*  761 */       this.console.error("// Error: " + o + "\n");
/*      */     } else {
/*  763 */       this.err.println("// Error: " + o);
/*  764 */       this.err.flush();
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
/*      */   public Reader getIn() {
/*  777 */     return this.in;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public PrintStream getOut() {
/*  783 */     return this.out;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public PrintStream getErr() {
/*  789 */     return this.err;
/*      */   }
/*      */   
/*      */   public final void println(Object o) {
/*  793 */     print(String.valueOf(o) + systemLineSeparator);
/*      */   }
/*      */ 
/*      */   
/*      */   public final void print(Object o) {
/*  798 */     if (this.console != null) {
/*  799 */       this.console.print(o);
/*      */     } else {
/*  801 */       this.out.print(o);
/*  802 */       this.out.flush();
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
/*      */   public static final void debug(String s) {
/*  814 */     if (DEBUG) {
/*  815 */       debug.println("// Debug: " + s);
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
/*      */   public Object get(String name) throws EvalError {
/*      */     try {
/*  830 */       Object ret = this.globalNameSpace.get(name, this);
/*  831 */       return Primitive.unwrap(ret);
/*  832 */     } catch (UtilEvalError e) {
/*  833 */       throw e.toEvalError(SimpleNode.JAVACODE, new CallStack());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Object getu(String name) {
/*      */     try {
/*  842 */       return get(name);
/*  843 */     } catch (EvalError e) {
/*  844 */       throw new InterpreterError("set: " + e);
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
/*      */   public void set(String name, Object value) throws EvalError {
/*  856 */     if (value == null) {
/*  857 */       value = Primitive.NULL;
/*      */     }
/*  859 */     CallStack callstack = new CallStack();
/*      */     try {
/*  861 */       if (Name.isCompound(name))
/*      */       
/*  863 */       { LHS lhs = this.globalNameSpace.getNameResolver(name).toLHS(callstack, this);
/*      */         
/*  865 */         lhs.assign(value, false); }
/*      */       else
/*  867 */       { this.globalNameSpace.setVariable(name, value, false); } 
/*  868 */     } catch (UtilEvalError e) {
/*  869 */       throw e.toEvalError(SimpleNode.JAVACODE, callstack);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setu(String name, Object value) {
/*      */     try {
/*  878 */       set(name, value);
/*  879 */     } catch (EvalError e) {
/*  880 */       throw new InterpreterError("set: " + e);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void set(String name, long value) throws EvalError {
/*  885 */     set(name, new Primitive(value));
/*      */   }
/*      */   public void set(String name, int value) throws EvalError {
/*  888 */     set(name, new Primitive(value));
/*      */   }
/*      */   public void set(String name, double value) throws EvalError {
/*  891 */     set(name, new Primitive(value));
/*      */   }
/*      */   public void set(String name, float value) throws EvalError {
/*  894 */     set(name, new Primitive(value));
/*      */   }
/*      */   public void set(String name, boolean value) throws EvalError {
/*  897 */     set(name, new Primitive(value));
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
/*      */   public void unset(String name) throws EvalError {
/*  911 */     CallStack callstack = new CallStack();
/*      */     try {
/*  913 */       LHS lhs = this.globalNameSpace.getNameResolver(name).toLHS(callstack, this);
/*      */ 
/*      */       
/*  916 */       if (lhs.type != 0) {
/*  917 */         throw new EvalError("Can't unset, not a variable: " + name, SimpleNode.JAVACODE, new CallStack());
/*      */       }
/*      */ 
/*      */       
/*  921 */       lhs.nameSpace.unsetVariable(name);
/*  922 */     } catch (UtilEvalError e) {
/*  923 */       throw new EvalError(e.getMessage(), SimpleNode.JAVACODE, new CallStack());
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object getInterface(Class interf) throws EvalError {
/*      */     try {
/*  983 */       return this.globalNameSpace.getThis(this).getInterface(interf);
/*  984 */     } catch (UtilEvalError e) {
/*  985 */       throw e.toEvalError(SimpleNode.JAVACODE, new CallStack());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private JJTParserState get_jjtree() {
/*  992 */     return this.parser.jjtree;
/*      */   }
/*      */   
/*      */   private JavaCharStream get_jj_input_stream() {
/*  996 */     return this.parser.jj_input_stream;
/*      */   }
/*      */   
/*      */   private boolean Line() throws ParseException {
/* 1000 */     return this.parser.Line();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   void loadRCFiles() {
/*      */     try {
/* 1007 */       String rcfile = System.getProperty("user.home") + File.separator + ".bshrc";
/*      */ 
/*      */       
/* 1010 */       source(rcfile, this.globalNameSpace);
/* 1011 */     } catch (Exception e) {
/*      */       
/* 1013 */       if (DEBUG) debug("Could not find rc file: " + e);
/*      */     
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public File pathToFile(String fileName) throws IOException {
/* 1024 */     File file = new File(fileName);
/*      */ 
/*      */     
/* 1027 */     if (!file.isAbsolute()) {
/* 1028 */       String cwd = (String)getu("bsh.cwd");
/* 1029 */       file = new File(cwd + File.separator + fileName);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1034 */     return new File(file.getCanonicalPath());
/*      */   }
/*      */ 
/*      */   
/*      */   public static void redirectOutputToFile(String filename) {
/*      */     try {
/* 1040 */       PrintStream pout = new PrintStream(new FileOutputStream(filename));
/*      */       
/* 1042 */       System.setOut(pout);
/* 1043 */       System.setErr(pout);
/* 1044 */     } catch (IOException e) {
/* 1045 */       System.err.println("Can't redirect output to file: " + filename);
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
/*      */   public void setClassLoader(ClassLoader externalCL) {
/* 1072 */     getClassManager().setClassLoader(externalCL);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BshClassManager getClassManager() {
/* 1082 */     return getNameSpace().getClassManager();
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
/*      */   public void setStrictJava(boolean b) {
/* 1098 */     this.strictJava = b;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getStrictJava() {
/* 1105 */     return this.strictJava;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void staticInit() {
/*      */     try {
/* 1116 */       systemLineSeparator = System.getProperty("line.separator");
/* 1117 */       debug = System.err;
/* 1118 */       DEBUG = Boolean.getBoolean("debug");
/* 1119 */       TRACE = Boolean.getBoolean("trace");
/* 1120 */       LOCALSCOPING = Boolean.getBoolean("localscoping");
/* 1121 */       String outfilename = System.getProperty("outfile");
/* 1122 */       if (outfilename != null)
/* 1123 */         redirectOutputToFile(outfilename); 
/* 1124 */     } catch (SecurityException e) {
/* 1125 */       System.err.println("Could not init static:" + e);
/* 1126 */     } catch (Exception e) {
/* 1127 */       System.err.println("Could not init static(2):" + e);
/* 1128 */     } catch (Throwable e) {
/* 1129 */       System.err.println("Could not init static(3):" + e);
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
/*      */   public String getSourceFileInfo() {
/* 1142 */     if (this.sourceFileInfo != null) {
/* 1143 */       return this.sourceFileInfo;
/*      */     }
/* 1145 */     return "<unknown source>";
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
/*      */   public Interpreter getParent() {
/* 1158 */     return this.parent;
/*      */   }
/*      */   
/*      */   public void setOut(PrintStream out) {
/* 1162 */     this.out = out;
/*      */   }
/*      */   public void setErr(PrintStream err) {
/* 1165 */     this.err = err;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
/* 1175 */     stream.defaultReadObject();
/*      */ 
/*      */     
/* 1178 */     if (this.console != null) {
/* 1179 */       setOut(this.console.getOut());
/* 1180 */       setErr(this.console.getErr());
/*      */     } else {
/* 1182 */       setOut(System.out);
/* 1183 */       setErr(System.err);
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
/*      */   private String getBshPrompt() {
/*      */     try {
/* 1196 */       return (String)eval("getBshPrompt()");
/* 1197 */     } catch (Exception e) {
/* 1198 */       return "bsh % ";
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
/*      */   public void setExitOnEOF(boolean value) {
/* 1214 */     this.exitOnEOF = value;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setShowResults(boolean showResults) {
/* 1224 */     this.showResults = showResults;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getShowResults() {
/* 1232 */     return this.showResults;
/*      */   }
/*      */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/Interpreter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */