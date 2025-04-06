/*     */ package com.mchange.v2.c3p0.impl;
/*     */ 
/*     */ import com.mchange.v2.c3p0.C3P0Registry;
/*     */ import com.mchange.v2.c3p0.cfg.C3P0Config;
/*     */ import com.mchange.v2.naming.JavaBeanReferenceMaker;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.beans.PropertyChangeSupport;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.sql.SQLFeatureNotSupportedException;
/*     */ import java.util.Properties;
/*     */ import java.util.logging.Logger;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.Reference;
/*     */ import javax.naming.Referenceable;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class DriverManagerDataSourceBase
/*     */   extends IdentityTokenResolvable
/*     */   implements Referenceable, Serializable
/*     */ {
/*  40 */   protected PropertyChangeSupport pcs = new PropertyChangeSupport(this); protected String description;
/*     */   
/*     */   protected PropertyChangeSupport getPropertyChangeSupport() {
/*  43 */     return this.pcs;
/*     */   }
/*  45 */   protected String driverClass = C3P0Config.initializeStringPropertyVar("driverClass", C3P0Defaults.driverClass());
/*  46 */   protected String factoryClassLocation = C3P0Config.initializeStringPropertyVar("factoryClassLocation", C3P0Defaults.factoryClassLocation());
/*  47 */   protected boolean forceUseNamedDriverClass = C3P0Config.initializeBooleanPropertyVar("forceUseNamedDriverClass", C3P0Defaults.forceUseNamedDriverClass());
/*     */   private volatile String identityToken;
/*  49 */   protected String jdbcUrl = C3P0Config.initializeStringPropertyVar("jdbcUrl", C3P0Defaults.jdbcUrl());
/*  50 */   protected Properties properties = new AuthMaskingProperties(); private static final long serialVersionUID = 1L;
/*     */   
/*     */   public synchronized String getDescription() {
/*  53 */     return this.description;
/*     */   }
/*     */   private static final short VERSION = 1;
/*     */   public synchronized void setDescription(String description) {
/*  57 */     this.description = description;
/*     */   }
/*     */   
/*     */   public synchronized String getDriverClass() {
/*  61 */     return this.driverClass;
/*     */   }
/*     */   
/*     */   public synchronized void setDriverClass(String driverClass) {
/*  65 */     String oldVal = this.driverClass;
/*  66 */     this.driverClass = driverClass;
/*  67 */     if (!eqOrBothNull(oldVal, driverClass))
/*  68 */       this.pcs.firePropertyChange("driverClass", oldVal, driverClass); 
/*     */   }
/*     */   
/*     */   public synchronized String getFactoryClassLocation() {
/*  72 */     return this.factoryClassLocation;
/*     */   }
/*     */   
/*     */   public synchronized void setFactoryClassLocation(String factoryClassLocation) {
/*  76 */     this.factoryClassLocation = factoryClassLocation;
/*     */   }
/*     */   
/*     */   public synchronized boolean isForceUseNamedDriverClass() {
/*  80 */     return this.forceUseNamedDriverClass;
/*     */   }
/*     */   
/*     */   public synchronized void setForceUseNamedDriverClass(boolean forceUseNamedDriverClass) {
/*  84 */     this.forceUseNamedDriverClass = forceUseNamedDriverClass;
/*     */   }
/*     */   
/*     */   public String getIdentityToken() {
/*  88 */     return this.identityToken;
/*     */   }
/*     */   
/*     */   public void setIdentityToken(String identityToken) {
/*  92 */     String oldVal = this.identityToken;
/*  93 */     this.identityToken = identityToken;
/*  94 */     if (!eqOrBothNull(oldVal, identityToken))
/*  95 */       this.pcs.firePropertyChange("identityToken", oldVal, identityToken); 
/*     */   }
/*     */   
/*     */   public synchronized String getJdbcUrl() {
/*  99 */     return this.jdbcUrl;
/*     */   }
/*     */   
/*     */   public synchronized void setJdbcUrl(String jdbcUrl) {
/* 103 */     this.jdbcUrl = jdbcUrl;
/*     */   }
/*     */   
/*     */   public synchronized Properties getProperties() {
/* 107 */     return AuthMaskingProperties.fromAnyProperties(this.properties);
/*     */   }
/*     */   
/*     */   public synchronized void setProperties(Properties properties) {
/* 111 */     Properties oldVal = this.properties;
/* 112 */     this.properties = AuthMaskingProperties.fromAnyProperties(properties);
/* 113 */     if (!eqOrBothNull(oldVal, properties))
/* 114 */       this.pcs.firePropertyChange("properties", oldVal, properties); 
/*     */   }
/*     */   
/*     */   public void addPropertyChangeListener(PropertyChangeListener pcl) {
/* 118 */     this.pcs.addPropertyChangeListener(pcl);
/*     */   }
/*     */   public void addPropertyChangeListener(String propName, PropertyChangeListener pcl) {
/* 121 */     this.pcs.addPropertyChangeListener(propName, pcl);
/*     */   }
/*     */   public void removePropertyChangeListener(PropertyChangeListener pcl) {
/* 124 */     this.pcs.removePropertyChangeListener(pcl);
/*     */   }
/*     */   public void removePropertyChangeListener(String propName, PropertyChangeListener pcl) {
/* 127 */     this.pcs.removePropertyChangeListener(propName, pcl);
/*     */   }
/*     */   public PropertyChangeListener[] getPropertyChangeListeners() {
/* 130 */     return this.pcs.getPropertyChangeListeners();
/*     */   }
/*     */   private boolean eqOrBothNull(Object a, Object b) {
/* 133 */     return (a == b || (a != null && a.equals(b)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream oos) throws IOException {
/* 143 */     oos.writeShort(1);
/* 144 */     oos.writeObject(this.description);
/* 145 */     oos.writeObject(this.driverClass);
/* 146 */     oos.writeObject(this.factoryClassLocation);
/* 147 */     oos.writeBoolean(this.forceUseNamedDriverClass);
/* 148 */     oos.writeObject(this.identityToken);
/* 149 */     oos.writeObject(this.jdbcUrl);
/* 150 */     oos.writeObject(this.properties);
/*     */   }
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
/* 155 */     short version = ois.readShort();
/* 156 */     switch (version) {
/*     */       
/*     */       case 1:
/* 159 */         this.description = (String)ois.readObject();
/* 160 */         this.driverClass = (String)ois.readObject();
/* 161 */         this.factoryClassLocation = (String)ois.readObject();
/* 162 */         this.forceUseNamedDriverClass = ois.readBoolean();
/* 163 */         this.identityToken = (String)ois.readObject();
/* 164 */         this.jdbcUrl = (String)ois.readObject();
/* 165 */         this.properties = (Properties)ois.readObject();
/* 166 */         this.pcs = new PropertyChangeSupport(this);
/*     */         return;
/*     */     } 
/* 169 */     throw new IOException("Unsupported Serialized Version: " + version);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Logger getParentLogger() throws SQLFeatureNotSupportedException {
/* 175 */     return C3P0ImplUtils.PARENT_LOGGER;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 179 */     StringBuffer sb = new StringBuffer();
/* 180 */     sb.append(super.toString());
/* 181 */     sb.append(" [ ");
/* 182 */     sb.append("description -> " + this.description);
/* 183 */     sb.append(", ");
/* 184 */     sb.append("driverClass -> " + this.driverClass);
/* 185 */     sb.append(", ");
/* 186 */     sb.append("factoryClassLocation -> " + this.factoryClassLocation);
/* 187 */     sb.append(", ");
/* 188 */     sb.append("forceUseNamedDriverClass -> " + this.forceUseNamedDriverClass);
/* 189 */     sb.append(", ");
/* 190 */     sb.append("identityToken -> " + this.identityToken);
/* 191 */     sb.append(", ");
/* 192 */     sb.append("jdbcUrl -> " + this.jdbcUrl);
/* 193 */     sb.append(", ");
/* 194 */     sb.append("properties -> " + this.properties);
/*     */     
/* 196 */     String extraToStringInfo = extraToStringInfo();
/* 197 */     if (extraToStringInfo != null)
/* 198 */       sb.append(extraToStringInfo); 
/* 199 */     sb.append(" ]");
/* 200 */     return sb.toString();
/*     */   }
/*     */   
/*     */   protected String extraToStringInfo() {
/* 204 */     return null;
/*     */   }
/* 206 */   static final JavaBeanReferenceMaker referenceMaker = new JavaBeanReferenceMaker();
/*     */ 
/*     */   
/*     */   static {
/* 210 */     referenceMaker.setFactoryClassName("com.mchange.v2.c3p0.impl.C3P0JavaBeanObjectFactory");
/* 211 */     referenceMaker.addReferenceProperty("description");
/* 212 */     referenceMaker.addReferenceProperty("driverClass");
/* 213 */     referenceMaker.addReferenceProperty("factoryClassLocation");
/* 214 */     referenceMaker.addReferenceProperty("forceUseNamedDriverClass");
/* 215 */     referenceMaker.addReferenceProperty("identityToken");
/* 216 */     referenceMaker.addReferenceProperty("jdbcUrl");
/* 217 */     referenceMaker.addReferenceProperty("properties");
/*     */   }
/*     */ 
/*     */   
/*     */   public Reference getReference() throws NamingException {
/* 222 */     return referenceMaker.createReference(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DriverManagerDataSourceBase(boolean autoregister) {
/* 230 */     if (autoregister) {
/*     */       
/* 232 */       this.identityToken = C3P0ImplUtils.allocateIdentityToken(this);
/* 233 */       C3P0Registry.reregister(this);
/*     */     } 
/*     */   }
/*     */   
/*     */   private DriverManagerDataSourceBase() {}
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/impl/DriverManagerDataSourceBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */