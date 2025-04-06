/*     */ package com.mchange.v2.c3p0.impl;
/*     */ 
/*     */ import com.mchange.v2.c3p0.C3P0Registry;
/*     */ import com.mchange.v2.c3p0.cfg.C3P0Config;
/*     */ import com.mchange.v2.naming.JavaBeanReferenceMaker;
/*     */ import com.mchange.v2.naming.ReferenceIndirector;
/*     */ import com.mchange.v2.ser.IndirectlySerialized;
/*     */ import com.mchange.v2.ser.SerializableUtils;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.beans.PropertyChangeSupport;
/*     */ import java.beans.PropertyVetoException;
/*     */ import java.beans.VetoableChangeListener;
/*     */ import java.beans.VetoableChangeSupport;
/*     */ import java.io.IOException;
/*     */ import java.io.NotSerializableException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.sql.SQLFeatureNotSupportedException;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Logger;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.Reference;
/*     */ import javax.naming.Referenceable;
/*     */ import javax.sql.ConnectionPoolDataSource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PoolBackedDataSourceBase
/*     */   extends IdentityTokenResolvable
/*     */   implements Referenceable, Serializable
/*     */ {
/*  45 */   protected PropertyChangeSupport pcs = new PropertyChangeSupport(this);
/*     */   
/*     */   protected PropertyChangeSupport getPropertyChangeSupport() {
/*  48 */     return this.pcs;
/*  49 */   } protected VetoableChangeSupport vcs = new VetoableChangeSupport(this); private ConnectionPoolDataSource connectionPoolDataSource;
/*     */   
/*     */   protected VetoableChangeSupport getVetoableChangeSupport() {
/*  52 */     return this.vcs;
/*     */   }
/*  54 */   private String dataSourceName = C3P0Config.initializeStringPropertyVar("dataSourceName", C3P0Defaults.dataSourceName());
/*  55 */   private Map extensions = C3P0Config.initializeExtensions();
/*  56 */   private String factoryClassLocation = C3P0Config.initializeStringPropertyVar("factoryClassLocation", C3P0Defaults.factoryClassLocation());
/*     */   private volatile String identityToken;
/*  58 */   private int numHelperThreads = C3P0Config.initializeIntPropertyVar("numHelperThreads", C3P0Defaults.numHelperThreads()); private static final long serialVersionUID = 1L; private static final short VERSION = 1;
/*     */   
/*     */   public synchronized ConnectionPoolDataSource getConnectionPoolDataSource() {
/*  61 */     return this.connectionPoolDataSource;
/*     */   }
/*     */   
/*     */   public synchronized void setConnectionPoolDataSource(ConnectionPoolDataSource connectionPoolDataSource) throws PropertyVetoException {
/*  65 */     ConnectionPoolDataSource oldVal = this.connectionPoolDataSource;
/*  66 */     if (!eqOrBothNull(oldVal, connectionPoolDataSource))
/*  67 */       this.vcs.fireVetoableChange("connectionPoolDataSource", oldVal, connectionPoolDataSource); 
/*  68 */     this.connectionPoolDataSource = connectionPoolDataSource;
/*  69 */     if (!eqOrBothNull(oldVal, connectionPoolDataSource))
/*  70 */       this.pcs.firePropertyChange("connectionPoolDataSource", oldVal, connectionPoolDataSource); 
/*     */   }
/*     */   
/*     */   public synchronized String getDataSourceName() {
/*  74 */     return this.dataSourceName;
/*     */   }
/*     */   
/*     */   public synchronized void setDataSourceName(String dataSourceName) {
/*  78 */     String oldVal = this.dataSourceName;
/*  79 */     this.dataSourceName = dataSourceName;
/*  80 */     if (!eqOrBothNull(oldVal, dataSourceName))
/*  81 */       this.pcs.firePropertyChange("dataSourceName", oldVal, dataSourceName); 
/*     */   }
/*     */   
/*     */   public synchronized Map getExtensions() {
/*  85 */     return Collections.unmodifiableMap(this.extensions);
/*     */   }
/*     */   
/*     */   public synchronized void setExtensions(Map<?, ?> extensions) {
/*  89 */     this.extensions = Collections.unmodifiableMap(extensions);
/*     */   }
/*     */   
/*     */   public synchronized String getFactoryClassLocation() {
/*  93 */     return this.factoryClassLocation;
/*     */   }
/*     */   
/*     */   public synchronized void setFactoryClassLocation(String factoryClassLocation) {
/*  97 */     this.factoryClassLocation = factoryClassLocation;
/*     */   }
/*     */   
/*     */   public String getIdentityToken() {
/* 101 */     return this.identityToken;
/*     */   }
/*     */   
/*     */   public void setIdentityToken(String identityToken) {
/* 105 */     String oldVal = this.identityToken;
/* 106 */     this.identityToken = identityToken;
/* 107 */     if (!eqOrBothNull(oldVal, identityToken))
/* 108 */       this.pcs.firePropertyChange("identityToken", oldVal, identityToken); 
/*     */   }
/*     */   
/*     */   public synchronized int getNumHelperThreads() {
/* 112 */     return this.numHelperThreads;
/*     */   }
/*     */   
/*     */   public synchronized void setNumHelperThreads(int numHelperThreads) {
/* 116 */     int oldVal = this.numHelperThreads;
/* 117 */     this.numHelperThreads = numHelperThreads;
/* 118 */     if (oldVal != numHelperThreads)
/* 119 */       this.pcs.firePropertyChange("numHelperThreads", oldVal, numHelperThreads); 
/*     */   }
/*     */   
/*     */   public void addPropertyChangeListener(PropertyChangeListener pcl) {
/* 123 */     this.pcs.addPropertyChangeListener(pcl);
/*     */   }
/*     */   public void addPropertyChangeListener(String propName, PropertyChangeListener pcl) {
/* 126 */     this.pcs.addPropertyChangeListener(propName, pcl);
/*     */   }
/*     */   public void removePropertyChangeListener(PropertyChangeListener pcl) {
/* 129 */     this.pcs.removePropertyChangeListener(pcl);
/*     */   }
/*     */   public void removePropertyChangeListener(String propName, PropertyChangeListener pcl) {
/* 132 */     this.pcs.removePropertyChangeListener(propName, pcl);
/*     */   }
/*     */   public PropertyChangeListener[] getPropertyChangeListeners() {
/* 135 */     return this.pcs.getPropertyChangeListeners();
/*     */   }
/*     */   public void addVetoableChangeListener(VetoableChangeListener vcl) {
/* 138 */     this.vcs.addVetoableChangeListener(vcl);
/*     */   }
/*     */   public void removeVetoableChangeListener(VetoableChangeListener vcl) {
/* 141 */     this.vcs.removeVetoableChangeListener(vcl);
/*     */   }
/*     */   public VetoableChangeListener[] getVetoableChangeListeners() {
/* 144 */     return this.vcs.getVetoableChangeListeners();
/*     */   }
/*     */   private boolean eqOrBothNull(Object a, Object b) {
/* 147 */     return (a == b || (a != null && a.equals(b)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream oos) throws IOException {
/* 157 */     oos.writeShort(1);
/*     */ 
/*     */     
/*     */     try {
/* 161 */       SerializableUtils.toByteArray(this.connectionPoolDataSource);
/* 162 */       oos.writeObject(this.connectionPoolDataSource);
/*     */     }
/* 164 */     catch (NotSerializableException nse) {
/*     */ 
/*     */       
/*     */       try {
/* 168 */         ReferenceIndirector referenceIndirector = new ReferenceIndirector();
/* 169 */         oos.writeObject(referenceIndirector.indirectForm(this.connectionPoolDataSource));
/*     */       }
/* 171 */       catch (IOException indirectionIOException) {
/* 172 */         throw indirectionIOException;
/* 173 */       } catch (Exception indirectionOtherException) {
/* 174 */         throw new IOException("Problem indirectly serializing connectionPoolDataSource: " + indirectionOtherException.toString());
/*     */       } 
/* 176 */     }  oos.writeObject(this.dataSourceName);
/*     */ 
/*     */     
/*     */     try {
/* 180 */       SerializableUtils.toByteArray(this.extensions);
/* 181 */       oos.writeObject(this.extensions);
/*     */     }
/* 183 */     catch (NotSerializableException nse) {
/*     */ 
/*     */       
/*     */       try {
/* 187 */         ReferenceIndirector referenceIndirector = new ReferenceIndirector();
/* 188 */         oos.writeObject(referenceIndirector.indirectForm(this.extensions));
/*     */       }
/* 190 */       catch (IOException indirectionIOException) {
/* 191 */         throw indirectionIOException;
/* 192 */       } catch (Exception indirectionOtherException) {
/* 193 */         throw new IOException("Problem indirectly serializing extensions: " + indirectionOtherException.toString());
/*     */       } 
/* 195 */     }  oos.writeObject(this.factoryClassLocation);
/* 196 */     oos.writeObject(this.identityToken);
/* 197 */     oos.writeInt(this.numHelperThreads);
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
/*     */     Object o;
/* 202 */     short version = ois.readShort();
/* 203 */     switch (version) {
/*     */ 
/*     */ 
/*     */       
/*     */       case 1:
/* 208 */         o = ois.readObject();
/* 209 */         if (o instanceof IndirectlySerialized) o = ((IndirectlySerialized)o).getObject(); 
/* 210 */         this.connectionPoolDataSource = (ConnectionPoolDataSource)o;
/*     */         
/* 212 */         this.dataSourceName = (String)ois.readObject();
/*     */ 
/*     */         
/* 215 */         o = ois.readObject();
/* 216 */         if (o instanceof IndirectlySerialized) o = ((IndirectlySerialized)o).getObject(); 
/* 217 */         this.extensions = (Map)o;
/*     */         
/* 219 */         this.factoryClassLocation = (String)ois.readObject();
/* 220 */         this.identityToken = (String)ois.readObject();
/* 221 */         this.numHelperThreads = ois.readInt();
/* 222 */         this.pcs = new PropertyChangeSupport(this);
/* 223 */         this.vcs = new VetoableChangeSupport(this);
/*     */         return;
/*     */     } 
/* 226 */     throw new IOException("Unsupported Serialized Version: " + version);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Logger getParentLogger() throws SQLFeatureNotSupportedException {
/* 232 */     return C3P0ImplUtils.PARENT_LOGGER;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 236 */     StringBuffer sb = new StringBuffer();
/* 237 */     sb.append(super.toString());
/* 238 */     sb.append(" [ ");
/* 239 */     sb.append("connectionPoolDataSource -> " + this.connectionPoolDataSource);
/* 240 */     sb.append(", ");
/* 241 */     sb.append("dataSourceName -> " + this.dataSourceName);
/* 242 */     sb.append(", ");
/* 243 */     sb.append("extensions -> " + this.extensions);
/* 244 */     sb.append(", ");
/* 245 */     sb.append("factoryClassLocation -> " + this.factoryClassLocation);
/* 246 */     sb.append(", ");
/* 247 */     sb.append("identityToken -> " + this.identityToken);
/* 248 */     sb.append(", ");
/* 249 */     sb.append("numHelperThreads -> " + this.numHelperThreads);
/*     */     
/* 251 */     String extraToStringInfo = extraToStringInfo();
/* 252 */     if (extraToStringInfo != null)
/* 253 */       sb.append(extraToStringInfo); 
/* 254 */     sb.append(" ]");
/* 255 */     return sb.toString();
/*     */   }
/*     */   
/*     */   protected String extraToStringInfo() {
/* 259 */     return null;
/*     */   }
/* 261 */   static final JavaBeanReferenceMaker referenceMaker = new JavaBeanReferenceMaker();
/*     */ 
/*     */   
/*     */   static {
/* 265 */     referenceMaker.setFactoryClassName("com.mchange.v2.c3p0.impl.C3P0JavaBeanObjectFactory");
/* 266 */     referenceMaker.addReferenceProperty("connectionPoolDataSource");
/* 267 */     referenceMaker.addReferenceProperty("dataSourceName");
/* 268 */     referenceMaker.addReferenceProperty("extensions");
/* 269 */     referenceMaker.addReferenceProperty("factoryClassLocation");
/* 270 */     referenceMaker.addReferenceProperty("identityToken");
/* 271 */     referenceMaker.addReferenceProperty("numHelperThreads");
/*     */   }
/*     */ 
/*     */   
/*     */   public Reference getReference() throws NamingException {
/* 276 */     return referenceMaker.createReference(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PoolBackedDataSourceBase(boolean autoregister) {
/* 284 */     if (autoregister) {
/*     */       
/* 286 */       this.identityToken = C3P0ImplUtils.allocateIdentityToken(this);
/* 287 */       C3P0Registry.reregister(this);
/*     */     } 
/*     */   }
/*     */   
/*     */   private PoolBackedDataSourceBase() {}
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/impl/PoolBackedDataSourceBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */