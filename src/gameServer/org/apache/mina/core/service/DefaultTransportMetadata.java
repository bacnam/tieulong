/*     */ package org.apache.mina.core.service;
/*     */ 
/*     */ import java.net.SocketAddress;
/*     */ import java.util.Collections;
/*     */ import java.util.Set;
/*     */ import org.apache.mina.core.session.IoSessionConfig;
/*     */ import org.apache.mina.util.IdentityHashSet;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultTransportMetadata
/*     */   implements TransportMetadata
/*     */ {
/*     */   private final String providerName;
/*     */   private final String name;
/*     */   private final boolean connectionless;
/*     */   private final boolean fragmentation;
/*     */   private final Class<? extends SocketAddress> addressType;
/*     */   private final Class<? extends IoSessionConfig> sessionConfigType;
/*     */   private final Set<Class<? extends Object>> envelopeTypes;
/*     */   
/*     */   public DefaultTransportMetadata(String providerName, String name, boolean connectionless, boolean fragmentation, Class<? extends SocketAddress> addressType, Class<? extends IoSessionConfig> sessionConfigType, Class<?>... envelopeTypes) {
/*  54 */     if (providerName == null) {
/*  55 */       throw new IllegalArgumentException("providerName");
/*     */     }
/*  57 */     if (name == null) {
/*  58 */       throw new IllegalArgumentException("name");
/*     */     }
/*     */     
/*  61 */     providerName = providerName.trim().toLowerCase();
/*  62 */     if (providerName.length() == 0) {
/*  63 */       throw new IllegalArgumentException("providerName is empty.");
/*     */     }
/*  65 */     name = name.trim().toLowerCase();
/*  66 */     if (name.length() == 0) {
/*  67 */       throw new IllegalArgumentException("name is empty.");
/*     */     }
/*     */     
/*  70 */     if (addressType == null) {
/*  71 */       throw new IllegalArgumentException("addressType");
/*     */     }
/*     */     
/*  74 */     if (envelopeTypes == null) {
/*  75 */       throw new IllegalArgumentException("envelopeTypes");
/*     */     }
/*     */     
/*  78 */     if (envelopeTypes.length == 0) {
/*  79 */       throw new IllegalArgumentException("envelopeTypes is empty.");
/*     */     }
/*     */     
/*  82 */     if (sessionConfigType == null) {
/*  83 */       throw new IllegalArgumentException("sessionConfigType");
/*     */     }
/*     */     
/*  86 */     this.providerName = providerName;
/*  87 */     this.name = name;
/*  88 */     this.connectionless = connectionless;
/*  89 */     this.fragmentation = fragmentation;
/*  90 */     this.addressType = addressType;
/*  91 */     this.sessionConfigType = sessionConfigType;
/*     */     
/*  93 */     IdentityHashSet<Class<? extends Object>> identityHashSet = new IdentityHashSet();
/*  94 */     for (Class<? extends Object> c : envelopeTypes) {
/*  95 */       identityHashSet.add(c);
/*     */     }
/*  97 */     this.envelopeTypes = Collections.unmodifiableSet((Set<? extends Class<? extends Object>>)identityHashSet);
/*     */   }
/*     */   
/*     */   public Class<? extends SocketAddress> getAddressType() {
/* 101 */     return this.addressType;
/*     */   }
/*     */   
/*     */   public Set<Class<? extends Object>> getEnvelopeTypes() {
/* 105 */     return this.envelopeTypes;
/*     */   }
/*     */   
/*     */   public Class<? extends IoSessionConfig> getSessionConfigType() {
/* 109 */     return this.sessionConfigType;
/*     */   }
/*     */   
/*     */   public String getProviderName() {
/* 113 */     return this.providerName;
/*     */   }
/*     */   
/*     */   public String getName() {
/* 117 */     return this.name;
/*     */   }
/*     */   
/*     */   public boolean isConnectionless() {
/* 121 */     return this.connectionless;
/*     */   }
/*     */   
/*     */   public boolean hasFragmentation() {
/* 125 */     return this.fragmentation;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 130 */     return this.name;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/service/DefaultTransportMetadata.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */