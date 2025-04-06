/*     */ package com.mchange.v2.naming;
/*     */ 
/*     */ import com.mchange.v2.log.MLevel;
/*     */ import com.mchange.v2.log.MLog;
/*     */ import com.mchange.v2.log.MLogger;
/*     */ import com.mchange.v2.ser.IndirectlySerialized;
/*     */ import com.mchange.v2.ser.Indirector;
/*     */ import java.io.IOException;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.util.Hashtable;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.InitialContext;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ReferenceIndirector
/*     */   implements Indirector
/*     */ {
/*  55 */   static final MLogger logger = MLog.getLogger(ReferenceIndirector.class);
/*     */   
/*     */   Name name;
/*     */   Name contextName;
/*     */   Hashtable environmentProperties;
/*     */   
/*     */   public Name getName() {
/*  62 */     return this.name;
/*     */   }
/*     */   public void setName(Name paramName) {
/*  65 */     this.name = paramName;
/*     */   }
/*     */   public Name getNameContextName() {
/*  68 */     return this.contextName;
/*     */   }
/*     */   public void setNameContextName(Name paramName) {
/*  71 */     this.contextName = paramName;
/*     */   }
/*     */   public Hashtable getEnvironmentProperties() {
/*  74 */     return this.environmentProperties;
/*     */   }
/*     */   public void setEnvironmentProperties(Hashtable paramHashtable) {
/*  77 */     this.environmentProperties = paramHashtable;
/*     */   }
/*     */   
/*     */   public IndirectlySerialized indirectForm(Object paramObject) throws Exception {
/*  81 */     Reference reference = ((Referenceable)paramObject).getReference();
/*  82 */     return new ReferenceSerialized(reference, this.name, this.contextName, this.environmentProperties);
/*     */   }
/*     */ 
/*     */   
/*     */   private static class ReferenceSerialized
/*     */     implements IndirectlySerialized
/*     */   {
/*     */     Reference reference;
/*     */     
/*     */     Name name;
/*     */     
/*     */     Name contextName;
/*     */     Hashtable env;
/*     */     
/*     */     ReferenceSerialized(Reference param1Reference, Name param1Name1, Name param1Name2, Hashtable param1Hashtable) {
/*  97 */       this.reference = param1Reference;
/*  98 */       this.name = param1Name1;
/*  99 */       this.contextName = param1Name2;
/* 100 */       this.env = param1Hashtable;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Object getObject() throws ClassNotFoundException, IOException {
/*     */       try {
/*     */         InitialContext initialContext;
/* 109 */         if (this.env == null) {
/* 110 */           initialContext = new InitialContext();
/*     */         } else {
/* 112 */           initialContext = new InitialContext(this.env);
/*     */         } 
/* 114 */         Context context = null;
/* 115 */         if (this.contextName != null) {
/* 116 */           context = (Context)initialContext.lookup(this.contextName);
/*     */         }
/* 118 */         return ReferenceableUtils.referenceToObject(this.reference, this.name, context, this.env);
/*     */       }
/* 120 */       catch (NamingException namingException) {
/*     */ 
/*     */         
/* 123 */         if (ReferenceIndirector.logger.isLoggable(MLevel.WARNING))
/* 124 */           ReferenceIndirector.logger.log(MLevel.WARNING, "Failed to acquire the Context necessary to lookup an Object.", namingException); 
/* 125 */         throw new InvalidObjectException("Failed to acquire the Context necessary to lookup an Object: " + namingException.toString());
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/naming/ReferenceIndirector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */