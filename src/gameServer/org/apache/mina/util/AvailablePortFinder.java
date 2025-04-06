/*     */ package org.apache.mina.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.DatagramSocket;
/*     */ import java.net.ServerSocket;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AvailablePortFinder
/*     */ {
/*     */   public static final int MIN_PORT_NUMBER = 1;
/*     */   public static final int MAX_PORT_NUMBER = 49151;
/*     */   
/*     */   public static Set<Integer> getAvailablePorts() {
/*  61 */     return getAvailablePorts(1, 49151);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getNextAvailable() {
/*  70 */     ServerSocket serverSocket = null;
/*     */ 
/*     */     
/*     */     try {
/*  74 */       serverSocket = new ServerSocket(0);
/*  75 */       int port = serverSocket.getLocalPort();
/*     */ 
/*     */       
/*  78 */       serverSocket.close();
/*     */       
/*  80 */       return port;
/*  81 */     } catch (IOException ioe) {
/*  82 */       throw new NoSuchElementException(ioe.getMessage());
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getNextAvailable(int fromPort) {
/*  93 */     if (fromPort < 1 || fromPort > 49151) {
/*  94 */       throw new IllegalArgumentException("Invalid start port: " + fromPort);
/*     */     }
/*     */     
/*  97 */     for (int i = fromPort; i <= 49151; i++) {
/*  98 */       if (available(i)) {
/*  99 */         return i;
/*     */       }
/*     */     } 
/*     */     
/* 103 */     throw new NoSuchElementException("Could not find an available port above " + fromPort);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean available(int port) {
/* 112 */     if (port < 1 || port > 49151) {
/* 113 */       throw new IllegalArgumentException("Invalid start port: " + port);
/*     */     }
/*     */     
/* 116 */     ServerSocket ss = null;
/* 117 */     DatagramSocket ds = null;
/*     */     
/*     */     try {
/* 120 */       ss = new ServerSocket(port);
/* 121 */       ss.setReuseAddress(true);
/* 122 */       ds = new DatagramSocket(port);
/* 123 */       ds.setReuseAddress(true);
/* 124 */       return true;
/* 125 */     } catch (IOException e) {
/*     */     
/*     */     } finally {
/* 128 */       if (ds != null) {
/* 129 */         ds.close();
/*     */       }
/*     */       
/* 132 */       if (ss != null) {
/*     */         try {
/* 134 */           ss.close();
/* 135 */         } catch (IOException e) {}
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 141 */     return false;
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
/*     */   public static Set<Integer> getAvailablePorts(int fromPort, int toPort) {
/* 153 */     if (fromPort < 1 || toPort > 49151 || fromPort > toPort) {
/* 154 */       throw new IllegalArgumentException("Invalid port range: " + fromPort + " ~ " + toPort);
/*     */     }
/*     */     
/* 157 */     Set<Integer> result = new TreeSet<Integer>();
/*     */     
/* 159 */     for (int i = fromPort; i <= toPort; i++) {
/* 160 */       ServerSocket s = null;
/*     */     }
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
/* 178 */     return result;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/util/AvailablePortFinder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */