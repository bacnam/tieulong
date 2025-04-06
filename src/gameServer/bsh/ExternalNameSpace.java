/*     */ package bsh;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ 
/*     */ public class ExternalNameSpace
/*     */   extends NameSpace
/*     */ {
/*     */   private Map externalMap;
/*     */   
/*     */   public ExternalNameSpace() {
/*  51 */     this(null, "External Map Namespace", null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExternalNameSpace(NameSpace parent, String name, Map<Object, Object> externalMap) {
/*  58 */     super(parent, name);
/*     */     
/*  60 */     if (externalMap == null) {
/*  61 */       externalMap = new HashMap<Object, Object>();
/*     */     }
/*  63 */     this.externalMap = externalMap;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map getMap() {
/*  70 */     return this.externalMap;
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
/*     */   public void setMap(Map map) {
/*  82 */     this.externalMap = null;
/*  83 */     clear();
/*  84 */     this.externalMap = map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void unsetVariable(String name) {
/*  91 */     super.unsetVariable(name);
/*  92 */     this.externalMap.remove(name);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getVariableNames() {
/* 100 */     Set nameSet = new HashSet();
/* 101 */     String[] nsNames = super.getVariableNames();
/* 102 */     nameSet.addAll(Arrays.asList(nsNames));
/* 103 */     nameSet.addAll(this.externalMap.keySet());
/* 104 */     return (String[])nameSet.toArray((Object[])new String[0]);
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
/*     */   protected Variable getVariableImpl(String name, boolean recurse) throws UtilEvalError {
/*     */     Variable var;
/* 126 */     Object value = this.externalMap.get(name);
/*     */ 
/*     */     
/* 129 */     if (value == null) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 134 */       super.unsetVariable(name);
/*     */ 
/*     */       
/* 137 */       var = super.getVariableImpl(name, recurse);
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 142 */       Variable localVar = super.getVariableImpl(name, false);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 147 */       if (localVar == null) {
/* 148 */         var = createVariable(name, null, value, null);
/*     */       } else {
/* 150 */         var = localVar;
/*     */       } 
/*     */     } 
/* 153 */     return var;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Variable createVariable(String name, Class type, Object value, Modifiers mods) {
/* 159 */     LHS lhs = new LHS(this.externalMap, name);
/*     */ 
/*     */     
/*     */     try {
/* 163 */       lhs.assign(value, false);
/* 164 */     } catch (UtilEvalError e) {
/* 165 */       throw new InterpreterError(e.toString());
/*     */     } 
/* 167 */     return new Variable(name, type, lhs);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 176 */     super.clear();
/* 177 */     this.externalMap.clear();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/ExternalNameSpace.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */