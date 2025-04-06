/*     */ package bsh.util;
/*     */ 
/*     */ import bsh.EvalError;
/*     */ import bsh.Interpreter;
/*     */ import bsh.InterpreterError;
/*     */ import bsh.Primitive;
/*     */ import bsh.TargetError;
/*     */ import bsh.This;
/*     */ import java.util.Vector;
/*     */ import org.apache.bsf.BSFDeclaredBean;
/*     */ import org.apache.bsf.BSFException;
/*     */ import org.apache.bsf.BSFManager;
/*     */ import org.apache.bsf.util.BSFEngineImpl;
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
/*     */ public class BeanShellBSFEngine
/*     */   extends BSFEngineImpl
/*     */ {
/*     */   Interpreter interpreter;
/*     */   boolean installedApplyMethod;
/*     */   static final String bsfApplyMethod = "_bsfApply( _bsfNames, _bsfArgs, _bsfText ) {for(i=0;i<_bsfNames.length;i++)this.namespace.setVariable(_bsfNames[i], _bsfArgs[i],false);return this.interpreter.eval(_bsfText, this.namespace);}";
/*     */   
/*     */   public void initialize(BSFManager mgr, String lang, Vector<BSFDeclaredBean> declaredBeans) throws BSFException {
/*  41 */     super.initialize(mgr, lang, declaredBeans);
/*     */     
/*  43 */     this.interpreter = new Interpreter();
/*     */ 
/*     */     
/*     */     try {
/*  47 */       this.interpreter.set("bsf", mgr);
/*  48 */     } catch (EvalError e) {
/*  49 */       throw new BSFException("bsh internal error: " + e.toString());
/*     */     } 
/*     */     
/*  52 */     for (int i = 0; i < declaredBeans.size(); i++) {
/*     */       
/*  54 */       BSFDeclaredBean bean = declaredBeans.get(i);
/*  55 */       declareBean(bean);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDebug(boolean debug) {
/*  61 */     Interpreter.DEBUG = debug;
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
/*     */   public Object call(Object object, String name, Object[] args) throws BSFException {
/*  76 */     if (object == null) {
/*     */       try {
/*  78 */         object = this.interpreter.get("global");
/*  79 */       } catch (EvalError e) {
/*  80 */         throw new BSFException("bsh internal error: " + e.toString());
/*     */       } 
/*     */     }
/*  83 */     if (object instanceof This) {
/*     */       
/*     */       try {
/*  86 */         Object value = ((This)object).invokeMethod(name, args);
/*  87 */         return Primitive.unwrap(value);
/*  88 */       } catch (InterpreterError e) {
/*     */         
/*  90 */         throw new BSFException("BeanShell interpreter internal error: " + e);
/*     */       }
/*  92 */       catch (TargetError e2) {
/*     */         
/*  94 */         throw new BSFException("The application script threw an exception: " + e2.getTarget());
/*     */       
/*     */       }
/*  97 */       catch (EvalError e3) {
/*     */         
/*  99 */         throw new BSFException("BeanShell script error: " + e3);
/*     */       } 
/*     */     }
/* 102 */     throw new BSFException("Cannot invoke method: " + name + ". Object: " + object + " is not a BeanShell scripted object.");
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object apply(String source, int lineNo, int columnNo, Object funcBody, Vector namesVec, Vector argsVec) throws BSFException {
/* 135 */     if (namesVec.size() != argsVec.size())
/* 136 */       throw new BSFException("number of params/names mismatch"); 
/* 137 */     if (!(funcBody instanceof String)) {
/* 138 */       throw new BSFException("apply: functino body must be a string");
/*     */     }
/* 140 */     String[] names = new String[namesVec.size()];
/* 141 */     namesVec.copyInto((Object[])names);
/* 142 */     Object[] args = new Object[argsVec.size()];
/* 143 */     argsVec.copyInto(args);
/*     */ 
/*     */     
/*     */     try {
/* 147 */       if (!this.installedApplyMethod) {
/*     */         
/* 149 */         this.interpreter.eval("_bsfApply( _bsfNames, _bsfArgs, _bsfText ) {for(i=0;i<_bsfNames.length;i++)this.namespace.setVariable(_bsfNames[i], _bsfArgs[i],false);return this.interpreter.eval(_bsfText, this.namespace);}");
/* 150 */         this.installedApplyMethod = true;
/*     */       } 
/*     */       
/* 153 */       This global = (This)this.interpreter.get("global");
/* 154 */       Object value = global.invokeMethod("_bsfApply", new Object[] { names, args, funcBody });
/*     */       
/* 156 */       return Primitive.unwrap(value);
/*     */     }
/* 158 */     catch (InterpreterError e) {
/*     */       
/* 160 */       throw new BSFException("BeanShell interpreter internal error: " + e + sourceInfo(source, lineNo, columnNo));
/*     */     
/*     */     }
/* 163 */     catch (TargetError e2) {
/*     */       
/* 165 */       throw new BSFException("The application script threw an exception: " + e2.getTarget() + sourceInfo(source, lineNo, columnNo));
/*     */ 
/*     */     
/*     */     }
/* 169 */     catch (EvalError e3) {
/*     */       
/* 171 */       throw new BSFException("BeanShell script error: " + e3 + sourceInfo(source, lineNo, columnNo));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object eval(String source, int lineNo, int columnNo, Object expr) throws BSFException {
/* 181 */     if (!(expr instanceof String)) {
/* 182 */       throw new BSFException("BeanShell expression must be a string");
/*     */     }
/*     */     try {
/* 185 */       return this.interpreter.eval((String)expr);
/* 186 */     } catch (InterpreterError e) {
/*     */       
/* 188 */       throw new BSFException("BeanShell interpreter internal error: " + e + sourceInfo(source, lineNo, columnNo));
/*     */     
/*     */     }
/* 191 */     catch (TargetError e2) {
/*     */       
/* 193 */       throw new BSFException("The application script threw an exception: " + e2.getTarget() + sourceInfo(source, lineNo, columnNo));
/*     */ 
/*     */     
/*     */     }
/* 197 */     catch (EvalError e3) {
/*     */       
/* 199 */       throw new BSFException("BeanShell script error: " + e3 + sourceInfo(source, lineNo, columnNo));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void exec(String source, int lineNo, int columnNo, Object script) throws BSFException {
/* 209 */     eval(source, lineNo, columnNo, script);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void declareBean(BSFDeclaredBean bean) throws BSFException {
/*     */     try {
/* 243 */       this.interpreter.set(bean.name, bean.bean);
/* 244 */     } catch (EvalError e) {
/* 245 */       throw new BSFException("error declaring bean: " + bean.name + " : " + e.toString());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void undeclareBean(BSFDeclaredBean bean) throws BSFException {
/*     */     try {
/* 254 */       this.interpreter.unset(bean.name);
/* 255 */     } catch (EvalError e) {
/* 256 */       throw new BSFException("bsh internal error: " + e.toString());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void terminate() {}
/*     */ 
/*     */   
/*     */   private String sourceInfo(String source, int lineNo, int columnNo) {
/* 265 */     return " BSF info: " + source + " at line: " + lineNo + " column: columnNo";
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/util/BeanShellBSFEngine.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */