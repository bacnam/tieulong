/*     */ package javolution.context;
/*     */ 
/*     */ import javolution.osgi.internal.OSGiServices;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class SecurityContext
/*     */   extends AbstractContext
/*     */ {
/*     */   public static class Permission<T>
/*     */   {
/*  53 */     public static final Permission<Object> ALL = new Permission(null);
/*     */ 
/*     */     
/*     */     private final Class<? super T> category;
/*     */ 
/*     */     
/*     */     private final String action;
/*     */ 
/*     */     
/*     */     private final T instance;
/*     */ 
/*     */     
/*     */     public Permission(Class<? super T> category) {
/*  66 */       this(category, null, null);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Permission(Class<? super T> category, String action) {
/*  74 */       this(category, action, null);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Permission(Class<? super T> category, String action, T instance) {
/*  82 */       this.category = category;
/*  83 */       this.action = action;
/*  84 */       this.instance = instance;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Class<? super T> getCategory() {
/*  91 */       return this.category;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String getAction() {
/*  98 */       return this.action;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public T getInstance() {
/* 105 */       return this.instance;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean implies(Permission<?> that) {
/* 118 */       if (this.category == null)
/* 119 */         return true; 
/* 120 */       if (!this.category.isAssignableFrom(that.category))
/* 121 */         return false; 
/* 122 */       if (this.action == null)
/* 123 */         return true; 
/* 124 */       if (!this.action.equals(that.action))
/* 125 */         return false; 
/* 126 */       if (this.instance == null)
/* 127 */         return true; 
/* 128 */       if (!this.instance.equals(that.instance))
/* 129 */         return false; 
/* 130 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 135 */       if (this.category == null)
/* 136 */         return "All permissions"; 
/* 137 */       if (this.action == null)
/* 138 */         return "Permission for any action on " + this.category.getName(); 
/* 139 */       if (this.instance == null)
/* 140 */         return "Permission for " + this.action + " on " + this.category.getName(); 
/* 141 */       return "Permission for " + this.action + " on instance " + this.instance + " of " + this.category.getName();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/* 147 */       if (obj == this)
/* 148 */         return true; 
/* 149 */       if (!(obj instanceof Permission))
/* 150 */         return false; 
/* 151 */       Permission<?> that = (Permission)obj;
/* 152 */       if (this.category == null && that.category != null)
/* 153 */         return false; 
/* 154 */       if (this.category != null && !this.category.equals(that.category))
/* 155 */         return false; 
/* 156 */       if (this.action == null && that.action != null)
/* 157 */         return false; 
/* 158 */       if (this.action != null && !this.action.equals(that.action))
/* 159 */         return false; 
/* 160 */       if (this.instance == null && that.instance != null)
/* 161 */         return false; 
/* 162 */       if (this.instance != null && !this.instance.equals(that.instance))
/* 163 */         return false; 
/* 164 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 169 */       return ((this.category != null) ? this.category.hashCode() : 0) + ((this.action != null) ? this.action.hashCode() : 0) + ((this.instance != null) ? this.instance.hashCode() : 0);
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
/*     */   public static SecurityContext enter() {
/* 186 */     return (SecurityContext)currentSecurityContext().enterInner();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void check(Permission<?> permission) {
/* 196 */     if (!currentSecurityContext().isGranted(permission)) {
/* 197 */       throw new SecurityException(permission + " is not granted.");
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
/*     */   public abstract boolean isGranted(Permission<?> paramPermission);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void grant(Permission<?> paramPermission, Object paramObject);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract void revoke(Permission<?> paramPermission, Object paramObject);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void grant(Permission<?> permission) {
/* 234 */     grant(permission, (Object)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void revoke(Permission<?> permission) {
/* 244 */     revoke(permission, (Object)null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static SecurityContext currentSecurityContext() {
/* 251 */     SecurityContext ctx = current(SecurityContext.class);
/* 252 */     if (ctx != null)
/* 253 */       return ctx; 
/* 254 */     return OSGiServices.getSecurityContext();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/context/SecurityContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */