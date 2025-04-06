/*     */ package javolution.lang;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import javolution.context.LogContext;
/*     */ import javolution.context.SecurityContext;
/*     */ import javolution.osgi.internal.OSGiServices;
/*     */ import javolution.text.TextContext;
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
/*     */ public abstract class Configurable<T>
/*     */ {
/* 115 */   public static SecurityContext.Permission<Configurable<?>> RECONFIGURE_PERMISSION = new SecurityContext.Permission(Configurable.class, "reconfigure");
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
/*     */   private String name;
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
/* 143 */   private final SecurityContext.Permission<Configurable<T>> reconfigurePermission = new SecurityContext.Permission(Configurable.class, "reconfigure", this);
/*     */   public Configurable() {
/* 145 */     String name = getName();
/* 146 */     T defaultValue = getDefault();
/* 147 */     if (name != null) {
/*     */       try {
/* 149 */         String property = System.getProperty(name);
/* 150 */         if (property != null) {
/* 151 */           defaultValue = parse(property);
/* 152 */           LogContext.debug(new Object[] { name, ", System Properties Value: ", defaultValue });
/*     */         }
/*     */       
/* 155 */       } catch (SecurityException securityError) {}
/*     */     }
/*     */ 
/*     */     
/* 159 */     this.name = name;
/* 160 */     this.value = initialized(defaultValue);
/* 161 */     Object[] listeners = OSGiServices.getConfigurableListeners();
/* 162 */     for (Object listener : listeners) {
/* 163 */       ((Listener)listener).configurableInitialized(this, this.value);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private volatile T value;
/*     */   
/*     */   public T get() {
/* 171 */     return this.value;
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
/*     */   public String getName() {
/* 186 */     if (this.name != null)
/* 187 */       return this.name; 
/* 188 */     Class<?> thisClass = getClass();
/* 189 */     Class<?> enclosingClass = thisClass.getEnclosingClass();
/* 190 */     String fieldName = null;
/* 191 */     for (Field field : enclosingClass.getFields()) {
/* 192 */       if (field.getType().isAssignableFrom(thisClass)) {
/* 193 */         if (fieldName != null) {
/* 194 */           throw new UnsupportedOperationException("Multiple configurables static fields in the same classrequires the Configurable.getName() method to be overriden.");
/*     */         }
/*     */         
/* 197 */         fieldName = field.getName();
/*     */       } 
/*     */     } 
/* 200 */     return (fieldName != null) ? (enclosingClass.getName() + "#" + fieldName) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SecurityContext.Permission<Configurable<T>> getReconfigurePermission() {
/* 208 */     return this.reconfigurePermission;
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
/*     */   public T reconfigure(T newValue) {
/* 225 */     SecurityContext.check(this.reconfigurePermission);
/* 226 */     synchronized (this) {
/* 227 */       T oldValue = this.value;
/* 228 */       this.value = reconfigured(oldValue, newValue);
/* 229 */       Object[] listeners = OSGiServices.getConfigurableListeners();
/* 230 */       for (Object listener : listeners) {
/* 231 */         ((Listener)listener).configurableReconfigured(this, oldValue, this.value);
/*     */       }
/*     */       
/* 234 */       return this.value;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract T getDefault();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected T initialized(T value) {
/* 253 */     return value;
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
/*     */   protected T parse(String str) {
/* 265 */     Class<? extends T> type = (Class)getDefault().getClass();
/* 266 */     return (T)TextContext.getFormat(type).parse(str);
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
/*     */   protected T reconfigured(T oldValue, T newValue) {
/* 281 */     return newValue;
/*     */   }
/*     */   
/*     */   public static interface Listener {
/*     */     <T> void configurableInitialized(Configurable<T> param1Configurable, T param1T);
/*     */     
/*     */     <T> void configurableReconfigured(Configurable<T> param1Configurable, T param1T1, T param1T2);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/lang/Configurable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */