/*     */ package bsh.engine;
/*     */ import bsh.EvalError;
/*     */ import bsh.ExternalNameSpace;
/*     */ import bsh.Interpreter;
/*     */ import bsh.InterpreterError;
/*     */ import bsh.NameSpace;
/*     */ import bsh.ParseException;
/*     */ import bsh.TargetError;
/*     */ import bsh.This;
/*     */ import bsh.UtilEvalError;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ import java.util.Map;
/*     */ import javax.script.CompiledScript;
/*     */ import javax.script.ScriptContext;
/*     */ import javax.script.ScriptException;
/*     */ 
/*     */ public class BshScriptEngine extends AbstractScriptEngine implements Compilable, Invocable {
/*     */   static final String engineNameSpaceKey = "org_beanshell_engine_namespace";
/*     */   
/*     */   public BshScriptEngine() {
/*  24 */     this(null);
/*     */   }
/*     */   private BshScriptEngineFactory factory; private Interpreter interpreter;
/*     */   
/*     */   public BshScriptEngine(BshScriptEngineFactory factory) {
/*  29 */     this.factory = factory;
/*  30 */     getInterpreter();
/*     */   }
/*     */ 
/*     */   
/*     */   protected Interpreter getInterpreter() {
/*  35 */     if (this.interpreter == null) {
/*  36 */       this.interpreter = new Interpreter();
/*  37 */       this.interpreter.setNameSpace(null);
/*     */     } 
/*     */     
/*  40 */     return this.interpreter;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object eval(String script, ScriptContext scriptContext) throws ScriptException {
/*  46 */     return evalSource(script, scriptContext);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object eval(Reader reader, ScriptContext scriptContext) throws ScriptException {
/*  52 */     return evalSource(reader, scriptContext);
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
/*     */   private Object evalSource(Object source, ScriptContext scriptContext) throws ScriptException {
/*  64 */     NameSpace contextNameSpace = getEngineNameSpace(scriptContext);
/*  65 */     Interpreter bsh = getInterpreter();
/*  66 */     bsh.setNameSpace(contextNameSpace);
/*     */ 
/*     */     
/*  69 */     bsh.setOut(new PrintStream(new WriterOutputStream(scriptContext.getWriter())));
/*     */     
/*  71 */     bsh.setErr(new PrintStream(new WriterOutputStream(scriptContext.getErrorWriter())));
/*     */ 
/*     */     
/*     */     try {
/*  75 */       if (source instanceof Reader) {
/*  76 */         return bsh.eval((Reader)source);
/*     */       }
/*  78 */       return bsh.eval((String)source);
/*  79 */     } catch (ParseException e) {
/*     */       
/*  81 */       throw new ScriptException(e.toString(), e.getErrorSourceFile(), e.getErrorLineNumber());
/*     */     }
/*  83 */     catch (TargetError e) {
/*     */ 
/*     */       
/*  86 */       ScriptException se = new ScriptException(e.toString(), e.getErrorSourceFile(), e.getErrorLineNumber());
/*     */       
/*  88 */       se.initCause(e.getTarget());
/*  89 */       throw se;
/*  90 */     } catch (EvalError e) {
/*     */       
/*  92 */       throw new ScriptException(e.toString(), e.getErrorSourceFile(), e.getErrorLineNumber());
/*     */     }
/*  94 */     catch (InterpreterError e) {
/*     */       
/*  96 */       throw new ScriptException(e.toString());
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
/*     */   private static NameSpace getEngineNameSpace(ScriptContext scriptContext) {
/*     */     ExternalNameSpace externalNameSpace;
/* 109 */     NameSpace ns = (NameSpace)scriptContext.getAttribute("org_beanshell_engine_namespace", 100);
/*     */ 
/*     */     
/* 112 */     if (ns == null) {
/*     */ 
/*     */       
/* 115 */       Map engineView = new ScriptContextEngineView(scriptContext);
/* 116 */       externalNameSpace = new ExternalNameSpace(null, "javax_script_context", engineView);
/*     */ 
/*     */       
/* 119 */       scriptContext.setAttribute("org_beanshell_engine_namespace", externalNameSpace, 100);
/*     */     } 
/*     */     
/* 122 */     return (NameSpace)externalNameSpace;
/*     */   }
/*     */ 
/*     */   
/*     */   public Bindings createBindings() {
/* 127 */     return new SimpleBindings();
/*     */   }
/*     */ 
/*     */   
/*     */   public ScriptEngineFactory getFactory() {
/* 132 */     if (this.factory == null)
/* 133 */       this.factory = new BshScriptEngineFactory(); 
/* 134 */     return this.factory;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompiledScript compile(String script) throws ScriptException {
/* 154 */     return compile(new StringReader(script));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompiledScript compile(Reader script) throws ScriptException {
/* 175 */     throw new Error("unimplemented");
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
/*     */   public Object invoke(Object thiz, String name, Object... args) throws ScriptException, NoSuchMethodException {
/* 203 */     if (!(thiz instanceof This)) {
/* 204 */       throw new ScriptException("Illegal objec type: " + thiz.getClass());
/*     */     }
/* 206 */     This bshObject = (This)thiz;
/*     */     
/*     */     try {
/* 209 */       return bshObject.invokeMethod(name, args);
/* 210 */     } catch (ParseException e) {
/*     */       
/* 212 */       throw new ScriptException(e.toString(), e.getErrorSourceFile(), e.getErrorLineNumber());
/*     */     }
/* 214 */     catch (TargetError e) {
/*     */ 
/*     */       
/* 217 */       ScriptException se = new ScriptException(e.toString(), e.getErrorSourceFile(), e.getErrorLineNumber());
/*     */       
/* 219 */       se.initCause(e.getTarget());
/* 220 */       throw se;
/* 221 */     } catch (EvalError e) {
/*     */       
/* 223 */       throw new ScriptException(e.toString(), e.getErrorSourceFile(), e.getErrorLineNumber());
/*     */     }
/* 225 */     catch (InterpreterError e) {
/*     */       
/* 227 */       throw new ScriptException(e.toString());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object invoke(String name, Object... args) throws ScriptException, NoSuchMethodException {
/* 248 */     return invoke(getGlobal(), name, args);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T getInterface(Class<T> clasz) {
/*     */     try {
/* 269 */       return (T)getGlobal().getInterface(clasz);
/* 270 */     } catch (UtilEvalError utilEvalError) {
/* 271 */       utilEvalError.printStackTrace();
/* 272 */       return null;
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
/*     */   public <T> T getInterface(Object thiz, Class<T> clasz) {
/* 296 */     if (!(thiz instanceof This)) {
/* 297 */       throw new IllegalArgumentException("invalid object type: " + thiz.getClass());
/*     */     }
/*     */     
/*     */     try {
/* 301 */       This bshThis = (This)thiz;
/* 302 */       return (T)bshThis.getInterface(clasz);
/* 303 */     } catch (UtilEvalError utilEvalError) {
/* 304 */       utilEvalError.printStackTrace(System.err);
/* 305 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private This getGlobal() {
/* 312 */     return getEngineNameSpace(getContext()).getThis(getInterpreter());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   class WriterOutputStream
/*     */     extends OutputStream
/*     */   {
/*     */     Writer writer;
/*     */ 
/*     */     
/*     */     WriterOutputStream(Writer writer) {
/* 324 */       this.writer = writer;
/*     */     }
/*     */ 
/*     */     
/*     */     public void write(int b) throws IOException {
/* 329 */       this.writer.write(b);
/*     */     }
/*     */ 
/*     */     
/*     */     public void flush() throws IOException {
/* 334 */       this.writer.flush();
/*     */     }
/*     */ 
/*     */     
/*     */     public void close() throws IOException {
/* 339 */       this.writer.close();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/engine/BshScriptEngine.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */