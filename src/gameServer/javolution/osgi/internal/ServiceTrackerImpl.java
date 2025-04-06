/*    */ package javolution.osgi.internal;
/*    */ 
/*    */ import org.osgi.framework.BundleContext;
/*    */ import org.osgi.util.tracker.ServiceTracker;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ServiceTrackerImpl<C>
/*    */ {
/*    */   private volatile ServiceTracker<C, C> tracker;
/*    */   private final Class<C> type;
/*    */   private final Class<? extends C> defaultImplClass;
/*    */   private C defaultImpl;
/*    */   
/*    */   public ServiceTrackerImpl(Class<C> type, Class<? extends C> defaultImplClass) {
/* 27 */     this.defaultImplClass = defaultImplClass;
/* 28 */     this.type = type;
/*    */   }
/*    */ 
/*    */   
/*    */   public void activate(BundleContext bc) {
/* 33 */     ServiceTracker<C, C> trk = new ServiceTracker(bc, this.type, null);
/* 34 */     trk.open();
/* 35 */     this.tracker = trk;
/*    */   }
/*    */ 
/*    */   
/*    */   public void deactivate(BundleContext bc) {
/* 40 */     this.tracker.close();
/* 41 */     this.tracker = null;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object[] getServices() {
/* 46 */     ServiceTracker<C, C> trk = this.tracker;
/* 47 */     if (trk != null) {
/* 48 */       Object[] services = trk.getServices();
/* 49 */       if (services != null) return services; 
/*    */     } 
/* 51 */     synchronized (this) {
/* 52 */       if (this.defaultImpl == null) {
/*    */         try {
/* 54 */           this.defaultImpl = this.defaultImplClass.newInstance();
/* 55 */         } catch (Throwable error) {
/* 56 */           throw new RuntimeException(error);
/*    */         } 
/*    */       }
/*    */     } 
/* 60 */     return new Object[] { this.defaultImpl };
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/osgi/internal/ServiceTrackerImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */