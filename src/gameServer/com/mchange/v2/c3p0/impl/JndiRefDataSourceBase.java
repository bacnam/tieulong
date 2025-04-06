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
/*     */ import java.util.Hashtable;
/*     */ import java.util.logging.Logger;
/*     */ import javax.naming.Name;
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
/*     */ public class JndiRefDataSourceBase
/*     */   extends IdentityTokenResolvable
/*     */   implements Referenceable, Serializable
/*     */ {
/*  43 */   protected PropertyChangeSupport pcs = new PropertyChangeSupport(this);
/*     */   
/*     */   protected PropertyChangeSupport getPropertyChangeSupport() {
/*  46 */     return this.pcs;
/*  47 */   } protected VetoableChangeSupport vcs = new VetoableChangeSupport(this);
/*     */   
/*     */   protected VetoableChangeSupport getVetoableChangeSupport() {
/*  50 */     return this.vcs;
/*     */   }
/*  52 */   private boolean caching = true; private String factoryClassLocation = C3P0Config.initializeStringPropertyVar("factoryClassLocation", C3P0Defaults.factoryClassLocation()); private volatile String identityToken; private Hashtable jndiEnv;
/*     */   private Object jndiName;
/*     */   private static final long serialVersionUID = 1L;
/*     */   private static final short VERSION = 1;
/*     */   
/*     */   public boolean isCaching() {
/*  58 */     return this.caching;
/*     */   }
/*     */   
/*     */   public void setCaching(boolean caching) {
/*  62 */     boolean oldVal = this.caching;
/*  63 */     this.caching = caching;
/*  64 */     if (oldVal != caching)
/*  65 */       this.pcs.firePropertyChange("caching", oldVal, caching); 
/*     */   }
/*     */   
/*     */   public String getFactoryClassLocation() {
/*  69 */     return this.factoryClassLocation;
/*     */   }
/*     */   
/*     */   public void setFactoryClassLocation(String factoryClassLocation) {
/*  73 */     String oldVal = this.factoryClassLocation;
/*  74 */     this.factoryClassLocation = factoryClassLocation;
/*  75 */     if (!eqOrBothNull(oldVal, factoryClassLocation))
/*  76 */       this.pcs.firePropertyChange("factoryClassLocation", oldVal, factoryClassLocation); 
/*     */   }
/*     */   
/*     */   public String getIdentityToken() {
/*  80 */     return this.identityToken;
/*     */   }
/*     */   
/*     */   public void setIdentityToken(String identityToken) {
/*  84 */     String oldVal = this.identityToken;
/*  85 */     this.identityToken = identityToken;
/*  86 */     if (!eqOrBothNull(oldVal, identityToken))
/*  87 */       this.pcs.firePropertyChange("identityToken", oldVal, identityToken); 
/*     */   }
/*     */   
/*     */   public Hashtable getJndiEnv() {
/*  91 */     return (this.jndiEnv != null) ? (Hashtable)this.jndiEnv.clone() : null;
/*     */   }
/*     */   
/*     */   public void setJndiEnv(Hashtable jndiEnv) {
/*  95 */     Hashtable oldVal = this.jndiEnv;
/*  96 */     this.jndiEnv = (jndiEnv != null) ? (Hashtable)jndiEnv.clone() : null;
/*  97 */     if (!eqOrBothNull(oldVal, jndiEnv))
/*  98 */       this.pcs.firePropertyChange("jndiEnv", oldVal, jndiEnv); 
/*     */   }
/*     */   
/*     */   public Object getJndiName() {
/* 102 */     return (this.jndiName instanceof Name) ? ((Name)this.jndiName).clone() : this.jndiName;
/*     */   }
/*     */   
/*     */   public void setJndiName(Object jndiName) throws PropertyVetoException {
/* 106 */     Object oldVal = this.jndiName;
/* 107 */     if (!eqOrBothNull(oldVal, jndiName))
/* 108 */       this.vcs.fireVetoableChange("jndiName", oldVal, jndiName); 
/* 109 */     this.jndiName = (jndiName instanceof Name) ? ((Name)jndiName).clone() : jndiName;
/* 110 */     if (!eqOrBothNull(oldVal, jndiName))
/* 111 */       this.pcs.firePropertyChange("jndiName", oldVal, jndiName); 
/*     */   }
/*     */   
/*     */   public void addPropertyChangeListener(PropertyChangeListener pcl) {
/* 115 */     this.pcs.addPropertyChangeListener(pcl);
/*     */   }
/*     */   public void addPropertyChangeListener(String propName, PropertyChangeListener pcl) {
/* 118 */     this.pcs.addPropertyChangeListener(propName, pcl);
/*     */   }
/*     */   public void removePropertyChangeListener(PropertyChangeListener pcl) {
/* 121 */     this.pcs.removePropertyChangeListener(pcl);
/*     */   }
/*     */   public void removePropertyChangeListener(String propName, PropertyChangeListener pcl) {
/* 124 */     this.pcs.removePropertyChangeListener(propName, pcl);
/*     */   }
/*     */   public PropertyChangeListener[] getPropertyChangeListeners() {
/* 127 */     return this.pcs.getPropertyChangeListeners();
/*     */   }
/*     */   public void addVetoableChangeListener(VetoableChangeListener vcl) {
/* 130 */     this.vcs.addVetoableChangeListener(vcl);
/*     */   }
/*     */   public void removeVetoableChangeListener(VetoableChangeListener vcl) {
/* 133 */     this.vcs.removeVetoableChangeListener(vcl);
/*     */   }
/*     */   public VetoableChangeListener[] getVetoableChangeListeners() {
/* 136 */     return this.vcs.getVetoableChangeListeners();
/*     */   }
/*     */   private boolean eqOrBothNull(Object a, Object b) {
/* 139 */     return (a == b || (a != null && a.equals(b)));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream oos) throws IOException {
/* 149 */     oos.writeShort(1);
/* 150 */     oos.writeBoolean(this.caching);
/* 151 */     oos.writeObject(this.factoryClassLocation);
/* 152 */     oos.writeObject(this.identityToken);
/* 153 */     oos.writeObject(this.jndiEnv);
/*     */ 
/*     */     
/*     */     try {
/* 157 */       SerializableUtils.toByteArray(this.jndiName);
/* 158 */       oos.writeObject(this.jndiName);
/*     */     }
/* 160 */     catch (NotSerializableException nse) {
/*     */ 
/*     */       
/*     */       try {
/* 164 */         ReferenceIndirector referenceIndirector = new ReferenceIndirector();
/* 165 */         oos.writeObject(referenceIndirector.indirectForm(this.jndiName));
/*     */       }
/* 167 */       catch (IOException indirectionIOException) {
/* 168 */         throw indirectionIOException;
/* 169 */       } catch (Exception indirectionOtherException) {
/* 170 */         throw new IOException("Problem indirectly serializing jndiName: " + indirectionOtherException.toString());
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
/*     */     Object o;
/* 176 */     short version = ois.readShort();
/* 177 */     switch (version) {
/*     */       
/*     */       case 1:
/* 180 */         this.caching = ois.readBoolean();
/* 181 */         this.factoryClassLocation = (String)ois.readObject();
/* 182 */         this.identityToken = (String)ois.readObject();
/* 183 */         this.jndiEnv = (Hashtable)ois.readObject();
/*     */ 
/*     */         
/* 186 */         o = ois.readObject();
/* 187 */         if (o instanceof IndirectlySerialized) o = ((IndirectlySerialized)o).getObject(); 
/* 188 */         this.jndiName = o;
/*     */         
/* 190 */         this.pcs = new PropertyChangeSupport(this);
/* 191 */         this.vcs = new VetoableChangeSupport(this);
/*     */         return;
/*     */     } 
/* 194 */     throw new IOException("Unsupported Serialized Version: " + version);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Logger getParentLogger() throws SQLFeatureNotSupportedException {
/* 200 */     return C3P0ImplUtils.PARENT_LOGGER;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 204 */     StringBuffer sb = new StringBuffer();
/* 205 */     sb.append(super.toString());
/* 206 */     sb.append(" [ ");
/* 207 */     sb.append("caching -> " + this.caching);
/* 208 */     sb.append(", ");
/* 209 */     sb.append("factoryClassLocation -> " + this.factoryClassLocation);
/* 210 */     sb.append(", ");
/* 211 */     sb.append("identityToken -> " + this.identityToken);
/* 212 */     sb.append(", ");
/* 213 */     sb.append("jndiEnv -> " + this.jndiEnv);
/* 214 */     sb.append(", ");
/* 215 */     sb.append("jndiName -> " + this.jndiName);
/*     */     
/* 217 */     String extraToStringInfo = extraToStringInfo();
/* 218 */     if (extraToStringInfo != null)
/* 219 */       sb.append(extraToStringInfo); 
/* 220 */     sb.append(" ]");
/* 221 */     return sb.toString();
/*     */   }
/*     */   
/*     */   protected String extraToStringInfo() {
/* 225 */     return null;
/*     */   }
/* 227 */   static final JavaBeanReferenceMaker referenceMaker = new JavaBeanReferenceMaker();
/*     */ 
/*     */   
/*     */   static {
/* 231 */     referenceMaker.setFactoryClassName("com.mchange.v2.c3p0.impl.C3P0JavaBeanObjectFactory");
/* 232 */     referenceMaker.addReferenceProperty("caching");
/* 233 */     referenceMaker.addReferenceProperty("factoryClassLocation");
/* 234 */     referenceMaker.addReferenceProperty("identityToken");
/* 235 */     referenceMaker.addReferenceProperty("jndiEnv");
/* 236 */     referenceMaker.addReferenceProperty("jndiName");
/*     */   }
/*     */ 
/*     */   
/*     */   public Reference getReference() throws NamingException {
/* 241 */     return referenceMaker.createReference(this);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JndiRefDataSourceBase(boolean autoregister) {
/* 249 */     if (autoregister) {
/*     */       
/* 251 */       this.identityToken = C3P0ImplUtils.allocateIdentityToken(this);
/* 252 */       C3P0Registry.reregister(this);
/*     */     } 
/*     */   }
/*     */   
/*     */   private JndiRefDataSourceBase() {}
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/impl/JndiRefDataSourceBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */