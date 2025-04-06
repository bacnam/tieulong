/*     */ package bsh.engine;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.script.ScriptContext;
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
/*     */ public class ScriptContextEngineView
/*     */   implements Map<String, Object>
/*     */ {
/*     */   ScriptContext context;
/*     */   
/*     */   public ScriptContextEngineView(ScriptContext context) {
/*  27 */     this.context = context;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int size() {
/*  36 */     return totalKeySet().size();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/*  44 */     return (totalKeySet().size() == 0);
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
/*     */   public boolean containsKey(Object key) {
/*  64 */     return (this.context.getAttribute((String)key) != null);
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
/*     */   public boolean containsValue(Object value) {
/*  87 */     Set values = totalValueSet();
/*  88 */     return values.contains(value);
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
/*     */   public Object get(Object key) {
/* 109 */     return this.context.getAttribute((String)key);
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
/*     */   public Object put(String key, Object value) {
/* 134 */     Object oldValue = this.context.getAttribute(key, 100);
/*     */     
/* 136 */     this.context.setAttribute(key, value, 100);
/* 137 */     return oldValue;
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
/*     */   public void putAll(Map<? extends String, ? extends Object> t) {
/* 158 */     this.context.getBindings(100).putAll(t);
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
/*     */   public Object remove(Object okey) {
/* 188 */     String key = (String)okey;
/* 189 */     Object oldValue = this.context.getAttribute(key, 100);
/*     */     
/* 191 */     this.context.removeAttribute(key, 100);
/* 192 */     return oldValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clear() {
/* 202 */     this.context.getBindings(100).clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set keySet() {
/* 213 */     return totalKeySet();
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
/*     */   public Collection values() {
/* 225 */     return totalValueSet();
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
/*     */   public Set<Map.Entry<String, Object>> entrySet() {
/* 245 */     throw new Error("unimplemented");
/*     */   }
/*     */ 
/*     */   
/*     */   private Set totalKeySet() {
/* 250 */     Set<String> keys = new HashSet();
/* 251 */     List<Integer> scopes = this.context.getScopes();
/* 252 */     for (Iterator<Integer> i$ = scopes.iterator(); i$.hasNext(); ) { int i = ((Integer)i$.next()).intValue();
/* 253 */       keys.addAll(this.context.getBindings(i).keySet()); }
/*     */     
/* 255 */     return Collections.unmodifiableSet(keys);
/*     */   }
/*     */   
/*     */   private Set totalValueSet() {
/* 259 */     Set<?> values = new HashSet();
/* 260 */     List<Integer> scopes = this.context.getScopes();
/* 261 */     for (Iterator<Integer> i$ = scopes.iterator(); i$.hasNext(); ) { int i = ((Integer)i$.next()).intValue();
/* 262 */       values.addAll(this.context.getBindings(i).values()); }
/*     */     
/* 264 */     return Collections.unmodifiableSet(values);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/engine/ScriptContextEngineView.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */