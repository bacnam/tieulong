/*     */ package com.mchange.v2.net;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import java.net.NetworkInterface;
/*     */ import java.net.SocketException;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.Collections;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
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
/*     */ public class LocalHostManager
/*     */ {
/*     */   Set localAddresses;
/*     */   Set knownGoodNames;
/*     */   Set knownBadNames;
/*     */   
/*     */   public synchronized void update() throws SocketException {
/*  49 */     HashSet<?> hashSet = new HashSet();
/*  50 */     Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
/*  51 */     while (enumeration.hasMoreElements()) {
/*     */       
/*  53 */       NetworkInterface networkInterface = enumeration.nextElement();
/*  54 */       Enumeration<InetAddress> enumeration1 = networkInterface.getInetAddresses();
/*  55 */       while (enumeration1.hasMoreElements())
/*  56 */         hashSet.add(enumeration1.nextElement()); 
/*     */     } 
/*  58 */     this.localAddresses = Collections.unmodifiableSet(hashSet);
/*  59 */     this.knownGoodNames = new HashSet();
/*  60 */     this.knownBadNames = new HashSet();
/*     */   }
/*     */   
/*     */   public synchronized Set getLocalAddresses() {
/*  64 */     return this.localAddresses;
/*     */   }
/*     */   public synchronized boolean isLocalAddress(InetAddress paramInetAddress) {
/*  67 */     return this.localAddresses.contains(paramInetAddress);
/*     */   }
/*     */   
/*     */   public synchronized boolean isLocalHostName(String paramString) {
/*  71 */     if (this.knownGoodNames.contains(paramString))
/*  72 */       return true; 
/*  73 */     if (this.knownGoodNames.contains(paramString)) {
/*  74 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/*  79 */       InetAddress inetAddress = InetAddress.getByName(paramString);
/*  80 */       if (this.localAddresses.contains(inetAddress)) {
/*     */         
/*  82 */         this.knownGoodNames.add(paramString);
/*  83 */         return true;
/*     */       } 
/*     */ 
/*     */       
/*  87 */       this.knownBadNames.add(paramString);
/*  88 */       return false;
/*     */     
/*     */     }
/*  91 */     catch (UnknownHostException unknownHostException) {
/*     */       
/*  93 */       this.knownBadNames.add(paramString);
/*  94 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public LocalHostManager() throws SocketException {
/* 102 */     update();
/*     */   }
/*     */ 
/*     */   
/*     */   public static void main(String[] paramArrayOfString) {
/*     */     try {
/* 108 */       LocalHostManager localHostManager = new LocalHostManager();
/* 109 */       System.out.println(localHostManager.getLocalAddresses());
/*     */     }
/* 111 */     catch (Exception exception) {
/* 112 */       exception.printStackTrace();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/net/LocalHostManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */