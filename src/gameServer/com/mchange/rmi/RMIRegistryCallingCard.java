/*     */ package com.mchange.rmi;
/*     */ 
/*     */ import com.mchange.io.UnsupportedVersionException;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.net.MalformedURLException;
/*     */ import java.rmi.Naming;
/*     */ import java.rmi.NotBoundException;
/*     */ import java.rmi.Remote;
/*     */ import java.rmi.RemoteException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RMIRegistryCallingCard
/*     */   implements CallingCard, Serializable
/*     */ {
/*     */   transient String url;
/*     */   static final long serialVersionUID = 1L;
/*  46 */   transient Remote cached = null;
/*     */   
/*     */   private static final short VERSION = 1;
/*     */   
/*     */   public RMIRegistryCallingCard(String paramString1, int paramInt, String paramString2) {
/*  51 */     this.url = "//" + paramString1.toLowerCase() + ':' + paramInt + '/' + paramString2;
/*     */   }
/*     */   public RMIRegistryCallingCard(String paramString1, String paramString2) {
/*  54 */     this(paramString1, 1099, paramString2);
/*     */   }
/*     */   public boolean equals(Object paramObject) {
/*  57 */     return (paramObject instanceof RMIRegistryCallingCard && this.url.equals(((RMIRegistryCallingCard)paramObject).url));
/*     */   }
/*     */   public int hashCode() {
/*  60 */     return this.url.hashCode();
/*     */   }
/*     */   
/*     */   public Remote findRemote() throws ServiceUnavailableException, RemoteException {
/*  64 */     if (this.cached instanceof Checkable) {
/*     */       
/*     */       try {
/*     */         
/*  68 */         ((Checkable)this.cached).check();
/*  69 */         return this.cached;
/*     */       }
/*  71 */       catch (RemoteException remoteException) {
/*     */         
/*  73 */         this.cached = null;
/*  74 */         return findRemote();
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/*  81 */       Remote remote = Naming.lookup(this.url);
/*  82 */       if (remote instanceof Checkable)
/*  83 */         this.cached = remote; 
/*  84 */       return remote;
/*     */     }
/*  86 */     catch (NotBoundException notBoundException) {
/*  87 */       throw new ServiceUnavailableException("Object Not Bound: " + this.url);
/*  88 */     } catch (MalformedURLException malformedURLException) {
/*  89 */       throw new ServiceUnavailableException("Uh oh. Bad url. It never will be available: " + this.url);
/*     */     } 
/*     */   }
/*     */   
/*     */   public String toString() {
/*  94 */     return super.toString() + " [" + this.url + "];";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException {
/* 102 */     paramObjectOutputStream.writeShort(1);
/*     */     
/* 104 */     paramObjectOutputStream.writeUTF(this.url);
/*     */   }
/*     */ 
/*     */   
/*     */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException {
/* 109 */     short s = paramObjectInputStream.readShort();
/* 110 */     switch (s) {
/*     */       
/*     */       case 1:
/* 113 */         this.url = paramObjectInputStream.readUTF();
/*     */         return;
/*     */     } 
/* 116 */     throw new UnsupportedVersionException(getClass().getName() + "; Bad version: " + s);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/rmi/RMIRegistryCallingCard.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */