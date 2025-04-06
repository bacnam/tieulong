/*     */ package bsh.engine;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import javax.script.ScriptEngine;
/*     */ import javax.script.ScriptEngineFactory;
/*     */ 
/*     */ 
/*     */ public class BshScriptEngineFactory
/*     */   implements ScriptEngineFactory
/*     */ {
/*  12 */   final List<String> extensions = Arrays.asList(new String[] { "bsh", "java" });
/*     */   
/*  14 */   final List<String> mimeTypes = Arrays.asList(new String[] { "application/x-beanshell", "application/x-bsh", "application/x-java-source" });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  20 */   final List<String> names = Arrays.asList(new String[] { "beanshell", "bsh", "java" });
/*     */   
/*     */   public String getEngineName() {
/*  23 */     return "BeanShell Engine";
/*     */   }
/*     */   
/*     */   public String getEngineVersion() {
/*  27 */     return "1.0";
/*     */   }
/*     */   
/*     */   public List<String> getExtensions() {
/*  31 */     return this.extensions;
/*     */   }
/*     */   
/*     */   public List<String> getMimeTypes() {
/*  35 */     return this.mimeTypes;
/*     */   }
/*     */   
/*     */   public List<String> getNames() {
/*  39 */     return this.names;
/*     */   }
/*     */   
/*     */   public String getLanguageName() {
/*  43 */     return "BeanShell";
/*     */   }
/*     */   
/*     */   public String getLanguageVersion() {
/*  47 */     return "2.0b5";
/*     */   }
/*     */   
/*     */   public Object getParameter(String param) {
/*  51 */     if (param.equals("javax.script.engine"))
/*  52 */       return getEngineName(); 
/*  53 */     if (param.equals("javax.script.engine_version"))
/*  54 */       return getEngineVersion(); 
/*  55 */     if (param.equals("javax.script.name"))
/*  56 */       return getEngineName(); 
/*  57 */     if (param.equals("javax.script.language"))
/*  58 */       return getLanguageName(); 
/*  59 */     if (param.equals("javax.script.language_version"))
/*  60 */       return getLanguageVersion(); 
/*  61 */     if (param.equals("THREADING")) {
/*  62 */       return "MULTITHREADED";
/*     */     }
/*  64 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMethodCallSyntax(String objectName, String methodName, String... args) {
/*  74 */     StringBuffer sb = new StringBuffer();
/*  75 */     if (objectName != null)
/*  76 */       sb.append(objectName + "."); 
/*  77 */     sb.append(methodName + "(");
/*  78 */     if (args.length > 0)
/*  79 */       sb.append(" "); 
/*  80 */     for (int i = 0; i < args.length; i++) {
/*  81 */       sb.append(((args[i] == null) ? "null" : args[i]) + ((i < args.length - 1) ? ", " : " "));
/*     */     }
/*  83 */     sb.append(")");
/*  84 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public String getOutputStatement(String message) {
/*  88 */     return "print( \"" + message + "\" );";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getProgram(String... statements) {
/*  93 */     StringBuffer sb = new StringBuffer();
/*  94 */     for (int i = 0; i < statements.length; i++) {
/*     */       
/*  96 */       sb.append(statements[i]);
/*  97 */       if (!statements[i].endsWith(";"))
/*  98 */         sb.append(";"); 
/*  99 */       sb.append("\n");
/*     */     } 
/* 101 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ScriptEngine getScriptEngine() {
/* 110 */     return new BshScriptEngine();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/engine/BshScriptEngineFactory.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */