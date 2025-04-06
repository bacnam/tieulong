/*     */ package com.mchange.v2.c3p0.management;
/*     */ 
/*     */ import com.mchange.v1.lang.ClassUtils;
/*     */ import com.mchange.v2.c3p0.AbstractComboPooledDataSource;
/*     */ import com.mchange.v2.c3p0.DriverManagerDataSource;
/*     */ import com.mchange.v2.c3p0.PooledDataSource;
/*     */ import com.mchange.v2.c3p0.WrapperConnectionPoolDataSource;
/*     */ import com.mchange.v2.c3p0.impl.AbstractPoolBackedDataSource;
/*     */ import com.mchange.v2.lang.ObjectUtils;
/*     */ import com.mchange.v2.log.MLevel;
/*     */ import com.mchange.v2.log.MLog;
/*     */ import com.mchange.v2.log.MLogger;
/*     */ import com.mchange.v2.management.ManagementUtils;
/*     */ import java.beans.BeanInfo;
/*     */ import java.beans.IntrospectionException;
/*     */ import java.beans.Introspector;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ import javax.management.Attribute;
/*     */ import javax.management.AttributeList;
/*     */ import javax.management.AttributeNotFoundException;
/*     */ import javax.management.DynamicMBean;
/*     */ import javax.management.IntrospectionException;
/*     */ import javax.management.InvalidAttributeValueException;
/*     */ import javax.management.MBeanAttributeInfo;
/*     */ import javax.management.MBeanConstructorInfo;
/*     */ import javax.management.MBeanException;
/*     */ import javax.management.MBeanInfo;
/*     */ import javax.management.MBeanOperationInfo;
/*     */ import javax.management.MBeanParameterInfo;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.ObjectName;
/*     */ import javax.management.ReflectionException;
/*     */ import javax.sql.ConnectionPoolDataSource;
/*     */ import javax.sql.DataSource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DynamicPooledDataSourceManagerMBean
/*     */   implements DynamicMBean
/*     */ {
/*  78 */   static final MLogger logger = MLog.getLogger(DynamicPooledDataSourceManagerMBean.class);
/*     */   
/*     */   static final Set HIDE_PROPS;
/*     */   
/*     */   static final Set HIDE_OPS;
/*     */   static final Set FORCE_OPS;
/*     */   static final Set FORCE_READ_ONLY_PROPS;
/*     */   static final MBeanOperationInfo[] OP_INFS;
/*     */   
/*     */   static {
/*  88 */     Set<String> hpTmp = new HashSet();
/*  89 */     hpTmp.add("connectionPoolDataSource");
/*  90 */     hpTmp.add("nestedDataSource");
/*  91 */     hpTmp.add("reference");
/*  92 */     hpTmp.add("connection");
/*  93 */     hpTmp.add("password");
/*  94 */     hpTmp.add("pooledConnection");
/*  95 */     hpTmp.add("properties");
/*  96 */     hpTmp.add("logWriter");
/*  97 */     hpTmp.add("lastAcquisitionFailureDefaultUser");
/*  98 */     hpTmp.add("lastCheckoutFailureDefaultUser");
/*  99 */     hpTmp.add("lastCheckinFailureDefaultUser");
/* 100 */     hpTmp.add("lastIdleTestFailureDefaultUser");
/* 101 */     hpTmp.add("lastConnectionTestFailureDefaultUser");
/* 102 */     HIDE_PROPS = Collections.unmodifiableSet(hpTmp);
/*     */     
/* 104 */     Class[] userPassArgs = { String.class, String.class };
/* 105 */     Set<Method> hoTmp = new HashSet();
/*     */     
/*     */     try {
/* 108 */       hoTmp.add(PooledDataSource.class.getMethod("close", new Class[] { boolean.class }));
/* 109 */       hoTmp.add(PooledDataSource.class.getMethod("getConnection", userPassArgs));
/*     */       
/* 111 */       hoTmp.add(PooledDataSource.class.getMethod("getLastAcquisitionFailure", userPassArgs));
/* 112 */       hoTmp.add(PooledDataSource.class.getMethod("getLastCheckinFailure", userPassArgs));
/* 113 */       hoTmp.add(PooledDataSource.class.getMethod("getLastCheckoutFailure", userPassArgs));
/* 114 */       hoTmp.add(PooledDataSource.class.getMethod("getLastIdleTestFailure", userPassArgs));
/* 115 */       hoTmp.add(PooledDataSource.class.getMethod("getLastConnectionTestFailure", userPassArgs));
/*     */     }
/* 117 */     catch (Exception e) {
/*     */       
/* 119 */       logger.log(MLevel.WARNING, "Tried to hide an operation from being exposed by mbean, but failed to find the operation!", e);
/*     */     } 
/* 121 */     HIDE_OPS = Collections.unmodifiableSet(hoTmp);
/*     */     
/* 123 */     Set<String> fropTmp = new HashSet();
/* 124 */     fropTmp.add("identityToken");
/* 125 */     FORCE_READ_ONLY_PROPS = Collections.unmodifiableSet(fropTmp);
/*     */     
/* 127 */     Set<?> foTmp = new HashSet();
/* 128 */     FORCE_OPS = Collections.unmodifiableSet(foTmp);
/*     */ 
/*     */     
/* 131 */     OP_INFS = extractOpInfs();
/*     */   }
/* 133 */   MBeanInfo info = null;
/*     */   
/*     */   PooledDataSource pds;
/*     */   
/*     */   String mbeanName;
/*     */   
/*     */   MBeanServer mbs;
/*     */   
/*     */   ConnectionPoolDataSource cpds;
/*     */   DataSource unpooledDataSource;
/*     */   Map pdsAttrInfos;
/*     */   Map cpdsAttrInfos;
/*     */   Map unpooledDataSourceAttrInfos;
/*     */   
/* 147 */   PropertyChangeListener pcl = new PropertyChangeListener()
/*     */     {
/*     */       public void propertyChange(PropertyChangeEvent evt)
/*     */       {
/* 151 */         String propName = evt.getPropertyName();
/* 152 */         Object val = evt.getNewValue();
/*     */         
/* 154 */         if ("nestedDataSource".equals(propName) || "connectionPoolDataSource".equals(propName) || "dataSourceName".equals(propName)) {
/* 155 */           DynamicPooledDataSourceManagerMBean.this.reinitialize();
/*     */         }
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   public DynamicPooledDataSourceManagerMBean(PooledDataSource pds, String mbeanName, MBeanServer mbs) throws Exception {
/* 162 */     this.pds = pds;
/* 163 */     this.mbeanName = mbeanName;
/* 164 */     this.mbs = mbs;
/*     */     
/* 166 */     if (pds instanceof AbstractComboPooledDataSource) {
/* 167 */       ((AbstractComboPooledDataSource)pds).addPropertyChangeListener(this.pcl);
/* 168 */     } else if (pds instanceof AbstractPoolBackedDataSource) {
/* 169 */       ((AbstractPoolBackedDataSource)pds).addPropertyChangeListener(this.pcl);
/*     */     } else {
/* 171 */       logger.warning(this + "managing an unexpected PooledDataSource. Only top-level attributes will be available. PooledDataSource: " + pds);
/*     */     } 
/* 173 */     Exception e = reinitialize();
/* 174 */     if (e != null) {
/* 175 */       throw e;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private synchronized Exception reinitialize() {
/*     */     try {
/* 184 */       if (!(this.pds instanceof AbstractComboPooledDataSource) && this.pds instanceof AbstractPoolBackedDataSource) {
/*     */         
/* 186 */         if (this.cpds instanceof WrapperConnectionPoolDataSource) {
/* 187 */           ((WrapperConnectionPoolDataSource)this.cpds).removePropertyChangeListener(this.pcl);
/*     */         }
/*     */ 
/*     */         
/* 191 */         this.cpds = null;
/* 192 */         this.unpooledDataSource = null;
/*     */         
/* 194 */         this.cpds = ((AbstractPoolBackedDataSource)this.pds).getConnectionPoolDataSource();
/*     */         
/* 196 */         if (this.cpds instanceof WrapperConnectionPoolDataSource) {
/*     */           
/* 198 */           this.unpooledDataSource = ((WrapperConnectionPoolDataSource)this.cpds).getNestedDataSource();
/* 199 */           ((WrapperConnectionPoolDataSource)this.cpds).addPropertyChangeListener(this.pcl);
/*     */         } 
/*     */       } 
/*     */       
/* 203 */       this.pdsAttrInfos = extractAttributeInfos(this.pds);
/* 204 */       this.cpdsAttrInfos = extractAttributeInfos(this.cpds);
/* 205 */       this.unpooledDataSourceAttrInfos = extractAttributeInfos(this.unpooledDataSource);
/*     */       
/* 207 */       Set allAttrNames = new HashSet();
/* 208 */       allAttrNames.addAll(this.pdsAttrInfos.keySet());
/* 209 */       allAttrNames.addAll(this.cpdsAttrInfos.keySet());
/* 210 */       allAttrNames.addAll(this.unpooledDataSourceAttrInfos.keySet());
/*     */       
/* 212 */       Set<Object> allAttrs = new HashSet();
/* 213 */       for (Iterator<String> ii = allAttrNames.iterator(); ii.hasNext(); ) {
/*     */         
/* 215 */         String name = ii.next();
/*     */         
/* 217 */         Object attrInfo = this.pdsAttrInfos.get(name);
/* 218 */         if (attrInfo == null)
/* 219 */           attrInfo = this.cpdsAttrInfos.get(name); 
/* 220 */         if (attrInfo == null)
/* 221 */           attrInfo = this.unpooledDataSourceAttrInfos.get(name); 
/* 222 */         allAttrs.add(attrInfo);
/*     */       } 
/*     */       
/* 225 */       String className = getClass().getName();
/* 226 */       MBeanAttributeInfo[] attrInfos = allAttrs.<MBeanAttributeInfo>toArray(new MBeanAttributeInfo[allAttrs.size()]);
/* 227 */       Class[] ctorArgClasses = { PooledDataSource.class, String.class, MBeanServer.class };
/* 228 */       MBeanConstructorInfo[] constrInfos = { new MBeanConstructorInfo("Constructor from PooledDataSource", getClass().getConstructor(ctorArgClasses)) };
/*     */       
/* 230 */       this.info = new MBeanInfo(getClass().getName(), "An MBean to monitor and manage a PooledDataSource", attrInfos, constrInfos, OP_INFS, null);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 241 */         ObjectName oname = ObjectName.getInstance(this.mbeanName);
/* 242 */         if (this.mbs.isRegistered(oname)) {
/*     */           
/* 244 */           this.mbs.unregisterMBean(oname);
/* 245 */           if (logger.isLoggable(MLevel.FINER)) {
/* 246 */             logger.log(MLevel.FINER, "MBean: " + oname.toString() + " unregistered, in order to be reregistered after update.");
/*     */           }
/*     */         } 
/*     */         
/* 250 */         this.mbeanName = ActiveManagementCoordinator.getPdsObjectNameStr(this.pds);
/* 251 */         oname = ObjectName.getInstance(this.mbeanName);
/* 252 */         this.mbs.registerMBean(this, oname);
/* 253 */         if (logger.isLoggable(MLevel.FINER)) {
/* 254 */           logger.log(MLevel.FINER, "MBean: " + oname.toString() + " registered.");
/*     */         }
/* 256 */         return null;
/*     */       }
/* 258 */       catch (Exception e) {
/*     */         
/* 260 */         if (logger.isLoggable(MLevel.WARNING)) {
/* 261 */           logger.log(MLevel.WARNING, "An Exception occurred while registering/reregistering mbean " + this.mbeanName + ". MBean may not be registered, or may not work properly.", e);
/*     */         }
/*     */ 
/*     */         
/* 265 */         return e;
/*     */       }
/*     */     
/* 268 */     } catch (NoSuchMethodException e) {
/*     */       
/* 270 */       if (logger.isLoggable(MLevel.SEVERE)) {
/* 271 */         logger.log(MLevel.SEVERE, "Huh? We can't find our own constructor?? The one we're in?", e);
/*     */       }
/*     */       
/* 274 */       return e;
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
/*     */   private static MBeanOperationInfo[] extractOpInfs() {
/* 287 */     MBeanParameterInfo user = new MBeanParameterInfo("user", "java.lang.String", "The database username of a pool-owner.");
/* 288 */     MBeanParameterInfo pwd = new MBeanParameterInfo("password", "java.lang.String", "The database password of a pool-owner.");
/* 289 */     MBeanParameterInfo[] userPass = { user, pwd };
/* 290 */     MBeanParameterInfo[] empty = new MBeanParameterInfo[0];
/*     */     
/* 292 */     Method[] meths = PooledDataSource.class.getMethods();
/* 293 */     Set<MBeanOperationInfo> attrInfos = new TreeSet(ManagementUtils.OP_INFO_COMPARATOR);
/*     */     
/* 295 */     for (int i = 0; i < meths.length; i++) {
/*     */       MBeanParameterInfo[] pi; MBeanOperationInfo opi;
/* 297 */       Method meth = meths[i];
/* 298 */       if (HIDE_OPS.contains(meth)) {
/*     */         continue;
/*     */       }
/* 301 */       String mname = meth.getName();
/* 302 */       Class[] params = meth.getParameterTypes();
/*     */       
/* 304 */       if (!FORCE_OPS.contains(mname)) {
/*     */ 
/*     */         
/* 307 */         if (mname.startsWith("set") && params.length == 1)
/*     */           continue; 
/* 309 */         if ((mname.startsWith("get") || mname.startsWith("is")) && params.length == 0) {
/*     */           continue;
/*     */         }
/*     */       } 
/* 313 */       Class<?> retType = meth.getReturnType();
/* 314 */       int impact = (retType == void.class) ? 1 : 0;
/*     */       
/* 316 */       if (params.length == 2 && params[0] == String.class && params[1] == String.class) {
/* 317 */         pi = userPass;
/* 318 */       } else if (params.length == 0) {
/* 319 */         pi = empty;
/*     */       } else {
/* 321 */         pi = null;
/*     */       } 
/*     */       
/* 324 */       if (pi != null) {
/* 325 */         opi = new MBeanOperationInfo(mname, null, pi, retType.getName(), impact);
/*     */ 
/*     */       
/*     */       }
/*     */       else {
/*     */ 
/*     */ 
/*     */         
/* 333 */         opi = new MBeanOperationInfo(meth.toString(), meth);
/*     */       } 
/*     */ 
/*     */       
/* 337 */       attrInfos.add(opi);
/*     */       continue;
/*     */     } 
/* 340 */     return attrInfos.<MBeanOperationInfo>toArray(new MBeanOperationInfo[attrInfos.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Object getAttribute(String attr) throws AttributeNotFoundException, MBeanException, ReflectionException {
/*     */     try {
/* 347 */       AttrRec rec = attrRecForAttribute(attr);
/* 348 */       if (rec == null) {
/* 349 */         throw new AttributeNotFoundException(attr);
/*     */       }
/*     */       
/* 352 */       MBeanAttributeInfo ai = rec.attrInfo;
/* 353 */       if (!ai.isReadable()) {
/* 354 */         throw new IllegalArgumentException(attr + " not readable.");
/*     */       }
/*     */       
/* 357 */       String name = ai.getName();
/* 358 */       String pfx = ai.isIs() ? "is" : "get";
/* 359 */       String mname = pfx + Character.toUpperCase(name.charAt(0)) + name.substring(1);
/* 360 */       Object target = rec.target;
/* 361 */       Method m = target.getClass().getMethod(mname, null);
/* 362 */       return m.invoke(target, null);
/*     */ 
/*     */     
/*     */     }
/* 366 */     catch (Exception e) {
/*     */       
/* 368 */       if (logger.isLoggable(MLevel.WARNING))
/* 369 */         logger.log(MLevel.WARNING, "Failed to get requested attribute: " + attr, e); 
/* 370 */       throw new MBeanException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized AttributeList getAttributes(String[] attrs) {
/* 376 */     AttributeList al = new AttributeList();
/* 377 */     for (int i = 0, len = attrs.length; i < len; i++) {
/*     */       
/* 379 */       String attr = attrs[i];
/*     */       
/*     */       try {
/* 382 */         Object val = getAttribute(attr);
/* 383 */         al.add(new Attribute(attr, val));
/*     */       }
/* 385 */       catch (Exception e) {
/*     */         
/* 387 */         if (logger.isLoggable(MLevel.WARNING))
/* 388 */           logger.log(MLevel.WARNING, "Failed to get requested attribute (for list): " + attr, e); 
/*     */       } 
/*     */     } 
/* 391 */     return al;
/*     */   }
/*     */ 
/*     */   
/*     */   private AttrRec attrRecForAttribute(String attr) {
/* 396 */     assert Thread.holdsLock(this);
/*     */     
/* 398 */     if (this.pdsAttrInfos.containsKey(attr))
/* 399 */       return new AttrRec(this.pds, (MBeanAttributeInfo)this.pdsAttrInfos.get(attr)); 
/* 400 */     if (this.cpdsAttrInfos.containsKey(attr))
/* 401 */       return new AttrRec(this.cpds, (MBeanAttributeInfo)this.cpdsAttrInfos.get(attr)); 
/* 402 */     if (this.unpooledDataSourceAttrInfos.containsKey(attr)) {
/* 403 */       return new AttrRec(this.unpooledDataSource, (MBeanAttributeInfo)this.unpooledDataSourceAttrInfos.get(attr));
/*     */     }
/* 405 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized MBeanInfo getMBeanInfo() {
/* 410 */     if (this.info == null)
/* 411 */       reinitialize(); 
/* 412 */     return this.info;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Object invoke(String operation, Object[] paramVals, String[] signature) throws MBeanException, ReflectionException {
/*     */     try {
/* 419 */       int slen = signature.length;
/* 420 */       Class[] paramTypes = new Class[slen];
/* 421 */       for (int i = 0; i < slen; i++) {
/* 422 */         paramTypes[i] = ClassUtils.forName(signature[i]);
/*     */       }
/*     */       
/* 425 */       Method m = this.pds.getClass().getMethod(operation, paramTypes);
/* 426 */       return m.invoke(this.pds, paramVals);
/*     */     }
/* 428 */     catch (NoSuchMethodException e) {
/*     */ 
/*     */       
/*     */       try {
/*     */ 
/*     */ 
/*     */         
/* 435 */         boolean two = false;
/* 436 */         if (signature.length == 0 && (operation.startsWith("get") || (two = operation.startsWith("is")))) {
/*     */           
/* 438 */           int i = two ? 2 : 3;
/* 439 */           String attr = Character.toLowerCase(operation.charAt(i)) + operation.substring(i + 1);
/* 440 */           return getAttribute(attr);
/*     */         } 
/* 442 */         if (signature.length == 1 && operation.startsWith("set")) {
/*     */           
/* 444 */           setAttribute(new Attribute(Character.toLowerCase(operation.charAt(3)) + operation.substring(4), paramVals[0]));
/* 445 */           return null;
/*     */         } 
/*     */         
/* 448 */         throw new MBeanException(e);
/*     */       }
/* 450 */       catch (Exception e2) {
/* 451 */         throw new MBeanException(e2);
/*     */       } 
/* 453 */     } catch (Exception e) {
/* 454 */       throw new MBeanException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void setAttribute(Attribute attrObj) throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {
/*     */     try {
/* 461 */       String attr = attrObj.getName();
/*     */       
/* 463 */       Object curVal = getAttribute(attr);
/* 464 */       Object newVal = attrObj.getValue();
/*     */       
/* 466 */       if (!ObjectUtils.eqOrBothNull(curVal, newVal))
/*     */       {
/* 468 */         if (attr == "factoryClassLocation") {
/*     */           
/* 470 */           if (this.pds instanceof AbstractComboPooledDataSource) {
/*     */             
/* 472 */             ((AbstractComboPooledDataSource)this.pds).setFactoryClassLocation((String)newVal);
/*     */             return;
/*     */           } 
/* 475 */           if (this.pds instanceof AbstractPoolBackedDataSource) {
/*     */             
/* 477 */             String strval = (String)newVal;
/* 478 */             AbstractPoolBackedDataSource apbds = (AbstractPoolBackedDataSource)this.pds;
/* 479 */             apbds.setFactoryClassLocation(strval);
/* 480 */             ConnectionPoolDataSource checkDs1 = apbds.getConnectionPoolDataSource();
/* 481 */             if (checkDs1 instanceof WrapperConnectionPoolDataSource) {
/*     */               
/* 483 */               WrapperConnectionPoolDataSource wcheckDs1 = (WrapperConnectionPoolDataSource)checkDs1;
/* 484 */               wcheckDs1.setFactoryClassLocation(strval);
/* 485 */               DataSource checkDs2 = wcheckDs1.getNestedDataSource();
/* 486 */               if (checkDs2 instanceof DriverManagerDataSource) {
/* 487 */                 ((DriverManagerDataSource)checkDs2).setFactoryClassLocation(strval);
/*     */               }
/*     */             } 
/*     */ 
/*     */             
/*     */             return;
/*     */           } 
/*     */         } 
/*     */ 
/*     */         
/* 497 */         AttrRec rec = attrRecForAttribute(attr);
/* 498 */         if (rec == null) {
/* 499 */           throw new AttributeNotFoundException(attr);
/*     */         }
/*     */         
/* 502 */         MBeanAttributeInfo ai = rec.attrInfo;
/* 503 */         if (!ai.isWritable()) {
/* 504 */           throw new IllegalArgumentException(attr + " not writable.");
/*     */         }
/*     */         
/* 507 */         Class attrType = ClassUtils.forName(rec.attrInfo.getType());
/* 508 */         String name = ai.getName();
/* 509 */         String pfx = "set";
/* 510 */         String mname = pfx + Character.toUpperCase(name.charAt(0)) + name.substring(1);
/* 511 */         Object target = rec.target;
/* 512 */         Method m = target.getClass().getMethod(mname, new Class[] { attrType });
/* 513 */         m.invoke(target, new Object[] { newVal });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 519 */         if (target != this.pds)
/*     */         {
/* 521 */           if (this.pds instanceof AbstractPoolBackedDataSource) {
/* 522 */             ((AbstractPoolBackedDataSource)this.pds).resetPoolManager(false);
/* 523 */           } else if (logger.isLoggable(MLevel.WARNING)) {
/* 524 */             logger.warning("MBean set a nested ConnectionPoolDataSource or DataSource parameter on an unknown PooledDataSource type. Could not reset the pool manager, so the changes may not take effect. c3p0 may need to be updated for PooledDataSource type " + this.pds.getClass() + ".");
/*     */           
/*     */           }
/*     */         
/*     */         }
/*     */       }
/*     */     
/*     */     }
/* 532 */     catch (Exception e) {
/*     */       
/* 534 */       if (logger.isLoggable(MLevel.WARNING))
/* 535 */         logger.log(MLevel.WARNING, "Failed to set requested attribute: " + attrObj, e); 
/* 536 */       throw new MBeanException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized AttributeList setAttributes(AttributeList al) {
/* 542 */     AttributeList out = new AttributeList();
/* 543 */     for (int i = 0, len = al.size(); i < len; i++) {
/*     */       
/* 545 */       Attribute attrObj = (Attribute)al.get(i);
/*     */ 
/*     */       
/*     */       try {
/* 549 */         setAttribute(attrObj);
/* 550 */         out.add(attrObj);
/*     */       }
/* 552 */       catch (Exception e) {
/*     */         
/* 554 */         if (logger.isLoggable(MLevel.WARNING))
/* 555 */           logger.log(MLevel.WARNING, "Failed to set requested attribute (from list): " + attrObj, e); 
/*     */       } 
/*     */     } 
/* 558 */     return out;
/*     */   }
/*     */ 
/*     */   
/*     */   private static Map extractAttributeInfos(Object bean) {
/* 563 */     if (bean != null) {
/*     */       
/*     */       try {
/*     */         
/* 567 */         Map<Object, Object> out = new HashMap<Object, Object>();
/* 568 */         BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass(), Object.class);
/* 569 */         PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
/*     */         
/* 571 */         for (int i = 0, len = pds.length; i < len; i++) {
/*     */           
/* 573 */           PropertyDescriptor pd = pds[i];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 580 */           String name = pd.getName();
/*     */           
/* 582 */           if (!HIDE_PROPS.contains(name)) {
/*     */ 
/*     */             
/* 585 */             String desc = getDescription(name);
/* 586 */             Method getter = pd.getReadMethod();
/* 587 */             Method setter = pd.getWriteMethod();
/*     */             
/* 589 */             if (FORCE_READ_ONLY_PROPS.contains(name)) {
/* 590 */               setter = null;
/*     */             }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             try {
/* 603 */               out.put(name, new MBeanAttributeInfo(name, desc, getter, setter));
/*     */             }
/* 605 */             catch (IntrospectionException e) {
/*     */               
/* 607 */               if (logger.isLoggable(MLevel.WARNING))
/* 608 */                 logger.log(MLevel.WARNING, "IntrospectionException while setting up MBean attribute '" + name + "'", e); 
/*     */             } 
/*     */           } 
/*     */         } 
/* 612 */         return Collections.synchronizedMap(out);
/*     */       }
/* 614 */       catch (IntrospectionException e) {
/*     */         
/* 616 */         if (logger.isLoggable(MLevel.WARNING))
/* 617 */           logger.log(MLevel.WARNING, "IntrospectionException while setting up MBean attributes for " + bean, e); 
/* 618 */         return Collections.EMPTY_MAP;
/*     */       } 
/*     */     }
/*     */     
/* 622 */     return Collections.EMPTY_MAP;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static String getDescription(String attrName) {
/* 628 */     return null;
/*     */   }
/*     */   
/*     */   private static class AttrRec
/*     */   {
/*     */     Object target;
/*     */     MBeanAttributeInfo attrInfo;
/*     */     
/*     */     AttrRec(Object target, MBeanAttributeInfo attrInfo) {
/* 637 */       this.target = target;
/* 638 */       this.attrInfo = attrInfo;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/management/DynamicPooledDataSourceManagerMBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */