/*     */ package com.zhonglian.server.common.utils;
/*     */ 
/*     */ import bsh.EvalError;
/*     */ import bsh.Interpreter;
/*     */ import bsh.NameSpace;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileFilter;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Reader;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
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
/*     */ public class CommCmdString
/*     */ {
/*     */   private final Interpreter it;
/*     */   private Thread workThread;
/*  31 */   private String defaultImports = "import com.mg.server.common.conf.*; import com.mg.server.common.utils.*; import com.mg.server.common.mgr.*; import com.mg.server.common.db.*; import com.mg.server.common.db.game.*; import com.mg.server.common.db.game.bo.*; import com.mg.server.common.db.game.create.*; import com.mg.server.common.db.log.*; import com.mg.server.common.db.log.create.*; import com.mg.server.common.data.ref.*; import com.mg.server.utils.*; import com.mg.server.utils.collections.*; import com.mg.server.common.async.*;import com.mg.server.framework.*; import com.mg.server.framework.telnet.*; import com.mg.server.framework.session.*; import com.mg.server.framework.protocol.*;";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  37 */   private String scriptPath = "";
/*     */   
/*     */   public CommCmdString(boolean interactive) {
/*  40 */     BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
/*  41 */     this.it = new Interpreter(br, System.out, System.err, interactive);
/*     */   }
/*     */   
/*     */   public CommCmdString(Reader in, PrintStream out, PrintStream err, boolean interactive, NameSpace namespace) {
/*  45 */     this.it = new Interpreter(in, out, err, interactive, namespace);
/*     */   }
/*     */ 
/*     */   
/*     */   public void run() {
/*     */     try {
/*  51 */       this.it.run();
/*  52 */     } catch (Throwable ex) {
/*  53 */       this.it.getErr().println(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Object source(String filename, NameSpace nameSpace) {
/*     */     try {
/*  59 */       return this.it.source(filename, nameSpace);
/*  60 */     } catch (FileNotFoundException ex) {
/*  61 */       this.it.getErr().println(ex);
/*  62 */     } catch (EvalError|IOException ex) {
/*  63 */       this.it.getErr().println(ex);
/*     */     } 
/*  65 */     return null;
/*     */   }
/*     */   
/*     */   public Object source(String filename) {
/*     */     try {
/*  70 */       return this.it.source(filename);
/*  71 */     } catch (FileNotFoundException ex) {
/*  72 */       this.it.getErr().println(ex);
/*  73 */     } catch (EvalError|IOException ex) {
/*  74 */       this.it.getErr().println(ex);
/*     */     } 
/*  76 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object eval(String statements) {
/*     */     try {
/*  82 */       String filterdCmd = statements.trim().toLowerCase();
/*  83 */       int foundExit = filterdCmd.indexOf("exit");
/*  84 */       if (foundExit >= 0) {
/*  85 */         Pattern pName = Pattern.compile("exit[\\s]?([\\s]?)[\\s]?");
/*  86 */         Matcher matcherName = pName.matcher(filterdCmd);
/*  87 */         if (matcherName.find()) {
/*  88 */           String res = "exit() not allowed!\n";
/*  89 */           return res;
/*     */         } 
/*     */       } 
/*     */       
/*  93 */       return this.it.eval(statements);
/*  94 */     } catch (LinkageError|EvalError ex) {
/*  95 */       this.it.getErr().println(ex);
/*  96 */       return ex;
/*     */     } 
/*     */   }
/*     */   
/*     */   public Object eval(String statements, NameSpace nameSpace) {
/*     */     try {
/* 102 */       return this.it.eval(statements, nameSpace);
/* 103 */     } catch (LinkageError|EvalError ex) {
/* 104 */       this.it.getErr().println(ex);
/*     */       
/* 106 */       return null;
/*     */     } 
/*     */   }
/*     */   public Reader getIn() {
/* 110 */     return this.it.getIn();
/*     */   }
/*     */   
/*     */   public PrintStream getOut() {
/* 114 */     return this.it.getOut();
/*     */   }
/*     */   
/*     */   public PrintStream getErr() {
/* 118 */     return this.it.getErr();
/*     */   }
/*     */   
/*     */   public final void println(Object o) {
/* 122 */     this.it.println(o);
/*     */   }
/*     */   
/*     */   public final void print(Object o) {
/* 126 */     this.it.print(o);
/*     */   }
/*     */   
/*     */   public Object get(String name) {
/*     */     try {
/* 131 */       return this.it.get(name);
/* 132 */     } catch (LinkageError|EvalError ex) {
/* 133 */       this.it.getErr().println(ex);
/*     */       
/* 135 */       return null;
/*     */     } 
/*     */   }
/*     */   public void set(String name, Object value) {
/*     */     try {
/* 140 */       this.it.set(name, value);
/* 141 */     } catch (LinkageError|EvalError ex) {
/* 142 */       this.it.getErr().println(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void set(String name, long value) throws EvalError {
/*     */     try {
/* 148 */       this.it.set(name, value);
/* 149 */     } catch (LinkageError|EvalError ex) {
/* 150 */       this.it.getErr().println(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void set(String name, int value) throws EvalError {
/*     */     try {
/* 156 */       this.it.set(name, value);
/* 157 */     } catch (LinkageError|EvalError ex) {
/* 158 */       this.it.getErr().println(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void set(String name, double value) throws EvalError {
/*     */     try {
/* 164 */       this.it.set(name, value);
/* 165 */     } catch (LinkageError|EvalError ex) {
/* 166 */       this.it.getErr().println(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void set(String name, float value) throws EvalError {
/*     */     try {
/* 172 */       this.it.set(name, value);
/* 173 */     } catch (LinkageError|EvalError ex) {
/* 174 */       this.it.getErr().println(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void set(String name, boolean value) throws EvalError {
/*     */     try {
/* 180 */       this.it.set(name, value);
/* 181 */     } catch (LinkageError|EvalError ex) {
/* 182 */       this.it.getErr().println(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void unset(String name) throws EvalError {
/*     */     try {
/* 188 */       this.it.unset(name);
/* 189 */     } catch (LinkageError|EvalError ex) {
/* 190 */       this.it.getErr().println(ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void redirectOutputToFile(String filename) {
/* 195 */     Interpreter.redirectOutputToFile(filename);
/*     */   }
/*     */   
/*     */   public void setStrictJava(boolean b) {
/* 199 */     this.it.setStrictJava(b);
/*     */   }
/*     */   
/*     */   public boolean getStrictJava() {
/* 203 */     return this.it.getStrictJava();
/*     */   }
/*     */   
/*     */   public String getSourceFileInfo() {
/* 207 */     return this.it.getSourceFileInfo();
/*     */   }
/*     */   
/*     */   public void setOut(PrintStream out) {
/* 211 */     this.it.setOut(out);
/*     */   }
/*     */   
/*     */   public void setErr(PrintStream err) {
/* 215 */     this.it.setErr(err);
/*     */   }
/*     */   
/*     */   public void setShowResults(boolean showResults) {
/* 219 */     this.it.setShowResults(showResults);
/*     */   }
/*     */   
/*     */   public boolean getShowResults() {
/* 223 */     return this.it.getShowResults();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDefaultImports() {
/* 228 */     return this.defaultImports;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDefaultImports(String defaultImports) {
/* 243 */     this.defaultImports = defaultImports;
/*     */   }
/*     */   
/*     */   public void setScriptPath(String scripts) {
/* 247 */     this.scriptPath = scripts;
/*     */   }
/*     */   
/*     */   public void reloadCommands() {
/*     */     try {
/* 252 */       if (this.defaultImports != null && !this.defaultImports.isEmpty()) {
/* 253 */         this.it.getOut().print("bsh: " + this.defaultImports + "\r\n");
/* 254 */         this.it.eval(this.defaultImports);
/* 255 */         this.it.set("cmdline", this);
/*     */       } 
/* 257 */       loadCommands(this.scriptPath);
/* 258 */     } catch (LinkageError|EvalError ex) {
/* 259 */       this.it.getErr().print("bsh: import defaults error:" + ex.toString() + "\r\n");
/*     */     } 
/*     */   }
/*     */   
/*     */   public void start() {
/* 264 */     stop();
/*     */     
/* 266 */     reloadCommands();
/*     */     
/* 268 */     this.workThread = new CommCmdStringThread((Runnable)this.it);
/* 269 */     this.workThread.start();
/*     */   }
/*     */   
/*     */   public int loadCommands(String path) {
/*     */     try {
/* 274 */       File dirs = new File(path);
/* 275 */       if (dirs.exists()) {
/* 276 */         File[] files = dirs.listFiles(new FileFilter()
/*     */             {
/*     */               public boolean accept(File pathname) {
/* 279 */                 return pathname.getName().endsWith(".bsh"); } });
/*     */         byte b;
/*     */         int i;
/*     */         File[] arrayOfFile1;
/* 283 */         for (i = (arrayOfFile1 = files).length, b = 0; b < i; ) { File f = arrayOfFile1[b];
/*     */           try {
/* 285 */             this.it.source(f.getPath());
/* 286 */           } catch (LinkageError|EvalError e) {
/* 287 */             this.it.getErr().println(e);
/*     */           }  b++; }
/*     */         
/* 290 */         return files.length;
/*     */       } 
/* 292 */     } catch (IOException e) {
/* 293 */       this.it.getErr().println(e);
/*     */     } 
/* 295 */     return 0;
/*     */   }
/*     */   
/*     */   public void stop() {
/* 299 */     if (this.workThread != null) {
/*     */       try {
/* 301 */         this.it.getIn().close();
/* 302 */       } catch (IOException ex) {
/* 303 */         this.it.getErr().println(ex);
/*     */       } 
/* 305 */       this.workThread = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   class CommCmdStringThread
/*     */     extends Thread {
/* 311 */     private Runnable target = null;
/*     */     
/*     */     public CommCmdStringThread(Runnable target) {
/* 314 */       super(target, "CommCmdString:bsh");
/* 315 */       this.target = target;
/*     */     }
/*     */     
/*     */     public CommCmdStringThread(Runnable target, String name) {
/* 319 */       super(target, name);
/* 320 */       this.target = target;
/*     */     }
/*     */ 
/*     */     
/*     */     public void run() {
/*     */       while (true) {
/*     */         try {
/* 327 */           if (this.target != null) {
/* 328 */             this.target.run();
/*     */           }
/*     */ 
/*     */           
/*     */           break;
/* 333 */         } catch (LinkageError ex) {
/* 334 */           CommCmdString.this.it.getErr().println(ex);
/* 335 */         } catch (Throwable ex) {
/* 336 */           CommCmdString.this.it.getErr().println(ex);
/* 337 */           throw ex;
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/utils/CommCmdString.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */