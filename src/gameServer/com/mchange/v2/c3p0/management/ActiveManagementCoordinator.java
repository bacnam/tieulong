/*     */ package com.mchange.v2.c3p0.management;
/*     */ 
/*     */ import com.mchange.v2.c3p0.PooledDataSource;
/*     */ import com.mchange.v2.c3p0.cfg.C3P0Config;
/*     */ import com.mchange.v2.log.MLevel;
/*     */ import com.mchange.v2.log.MLog;
/*     */ import com.mchange.v2.log.MLogger;
/*     */ import java.lang.management.ManagementFactory;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.ObjectName;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ActiveManagementCoordinator
/*     */   implements ManagementCoordinator
/*     */ {
/*     */   public static final String C3P0_REGISTRY_NAME_KEY = "com.mchange.v2.c3p0.management.RegistryName";
/*     */   private static final String C3P0_REGISTRY_NAME_PFX = "com.mchange.v2.c3p0:type=C3P0Registry";
/*  53 */   static final MLogger logger = MLog.getLogger(ActiveManagementCoordinator.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  61 */   MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
/*  62 */   String regName = getRegistryName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void attemptManageC3P0Registry() {
/*     */     try {
/*  70 */       ObjectName name = new ObjectName(this.regName);
/*  71 */       C3P0RegistryManager mbean = new C3P0RegistryManager();
/*     */       
/*  73 */       if (this.mbs.isRegistered(name)) {
/*     */         
/*  75 */         if (logger.isLoggable(MLevel.WARNING))
/*     */         {
/*  77 */           logger.warning("A C3P0Registry mbean is already registered. This probably means that an application using c3p0 was undeployed, but not all PooledDataSources were closed prior to undeployment. This may lead to resource leaks over time. Please take care to close all PooledDataSources.");
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  83 */         this.mbs.unregisterMBean(name);
/*     */       } 
/*  85 */       this.mbs.registerMBean(mbean, name);
/*     */     }
/*  87 */     catch (Exception e) {
/*     */       
/*  89 */       if (logger.isLoggable(MLevel.WARNING)) {
/*  90 */         logger.log(MLevel.WARNING, "Failed to set up C3P0RegistryManager mBean. [c3p0 will still function normally, but management via JMX may not be possible.]", e);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void attemptUnmanageC3P0Registry() {
/*     */     try {
/* 101 */       ObjectName name = new ObjectName(this.regName);
/* 102 */       if (this.mbs.isRegistered(name)) {
/*     */         
/* 104 */         this.mbs.unregisterMBean(name);
/* 105 */         if (logger.isLoggable(MLevel.FINER)) {
/* 106 */           logger.log(MLevel.FINER, "C3P0Registry mbean unregistered.");
/*     */         }
/* 108 */       } else if (logger.isLoggable(MLevel.FINE)) {
/* 109 */         logger.fine("The C3P0Registry mbean was not found in the registry, so could not be unregistered.");
/*     */       } 
/* 111 */     } catch (Exception e) {
/*     */       
/* 113 */       if (logger.isLoggable(MLevel.WARNING)) {
/* 114 */         logger.log(MLevel.WARNING, "An Exception occurred while trying to unregister the C3P0RegistryManager mBean." + e);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void attemptManagePooledDataSource(PooledDataSource pds) {
/* 122 */     String name = getPdsObjectNameStr(pds);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 131 */       DynamicPooledDataSourceManagerMBean mbean = new DynamicPooledDataSourceManagerMBean(pds, name, this.mbs);
/*     */     }
/* 133 */     catch (Exception e) {
/*     */       
/* 135 */       if (logger.isLoggable(MLevel.WARNING)) {
/* 136 */         logger.log(MLevel.WARNING, "Failed to set up a PooledDataSourceManager mBean. [" + name + "] " + "[c3p0 will still functioning normally, but management via JMX may not be possible.]", e);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void attemptUnmanagePooledDataSource(PooledDataSource pds) {
/* 146 */     String nameStr = getPdsObjectNameStr(pds);
/*     */     
/*     */     try {
/* 149 */       ObjectName name = new ObjectName(nameStr);
/* 150 */       if (this.mbs.isRegistered(name)) {
/*     */         
/* 152 */         this.mbs.unregisterMBean(name);
/* 153 */         if (logger.isLoggable(MLevel.FINER)) {
/* 154 */           logger.log(MLevel.FINER, "MBean: " + nameStr + " unregistered.");
/*     */         }
/*     */       }
/* 157 */       else if (logger.isLoggable(MLevel.FINE)) {
/* 158 */         logger.fine("The mbean " + nameStr + " was not found in the registry, so could not be unregistered.");
/*     */       } 
/* 160 */     } catch (Exception e) {
/*     */       
/* 162 */       if (logger.isLoggable(MLevel.WARNING)) {
/* 163 */         logger.log(MLevel.WARNING, "An Exception occurred while unregistering mBean. [" + nameStr + "] " + e);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static String getPdsObjectNameStr(PooledDataSource pds) {
/* 171 */     String dataSourceName = pds.getDataSourceName();
/* 172 */     String out = "com.mchange.v2.c3p0:type=PooledDataSource,identityToken=" + pds.getIdentityToken();
/* 173 */     if (dataSourceName != null)
/* 174 */       out = out + ",name=" + dataSourceName; 
/* 175 */     return out;
/*     */   }
/*     */ 
/*     */   
/*     */   private static String getRegistryName() {
/* 180 */     String name = C3P0Config.getMultiPropertiesConfig().getProperty("com.mchange.v2.c3p0.management.RegistryName");
/* 181 */     if (name == null) {
/* 182 */       name = "com.mchange.v2.c3p0:type=C3P0Registry";
/*     */     } else {
/* 184 */       name = "com.mchange.v2.c3p0:type=C3P0Registry,name=" + name;
/* 185 */     }  return name;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/management/ActiveManagementCoordinator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */