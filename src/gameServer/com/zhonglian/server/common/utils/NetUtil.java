/*     */ package com.zhonglian.server.common.utils;
/*     */ 
/*     */ import BaseCommon.CommLog;
/*     */ import java.net.InetAddress;
/*     */ import java.net.NetworkInterface;
/*     */ import java.net.SocketException;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.Enumeration;
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
/*     */ public class NetUtil
/*     */ {
/*     */   public static String getHostName() {
/*     */     try {
/*  25 */       InetAddress ia = InetAddress.getLocalHost();
/*  26 */       return ia.getHostName();
/*  27 */     } catch (UnknownHostException ex) {
/*  28 */       CommLog.error(NetUtil.class.getName(), ex);
/*     */       
/*  30 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int getHostIpInteger() {
/*  39 */     InetAddress ip = getHostIpAddr();
/*  40 */     if (ip == null) {
/*  41 */       return 0;
/*     */     }
/*     */     
/*  44 */     return ipAddressToInt(ip);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int ipAddressToInt(InetAddress addr) {
/*  54 */     byte[] ip_arr = addr.getAddress();
/*     */     
/*  56 */     int res = ip_arr[3] & 0xFF | (ip_arr[2] & 0xFF) << 8 | (ip_arr[1] & 0xFF) << 16 | (ip_arr[0] & 0xFF) << 24;
/*  57 */     return res;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static InetAddress getHostIpAddr() {
/*     */     try {
/*  68 */       Enumeration<NetworkInterface> niEnums = NetworkInterface.getNetworkInterfaces();
/*  69 */       while (niEnums.hasMoreElements()) {
/*  70 */         NetworkInterface ni = niEnums.nextElement();
/*  71 */         if (ni.isLoopback() || ni.isPointToPoint() || ni.isVirtual() || !ni.isUp()) {
/*     */           continue;
/*     */         }
/*  74 */         Enumeration<InetAddress> addrs = ni.getInetAddresses();
/*     */         
/*  76 */         while (addrs.hasMoreElements()) {
/*  77 */           InetAddress ip = addrs.nextElement();
/*  78 */           if (ip != null && ip instanceof java.net.Inet4Address && !ip.isAnyLocalAddress() && !ip.isMulticastAddress()) {
/*  79 */             return ip;
/*     */           }
/*     */         }
/*     */       
/*     */       }
/*     */     
/*  85 */     } catch (SocketException ex) {
/*  86 */       CommLog.error(NetUtil.class.getName(), ex);
/*     */     } 
/*     */     
/*  89 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] ipAddressStringToRaw(String ipString) {
/*  99 */     String[] ipSegs = ipString.split("\\.");
/* 100 */     if (ipSegs.length != 4) {
/* 101 */       return null;
/*     */     }
/* 103 */     byte[] targets = new byte[4];
/* 104 */     targets[0] = (byte)(Integer.parseInt(ipSegs[0]) & 0xFF);
/* 105 */     targets[1] = (byte)(Integer.parseInt(ipSegs[1]) & 0xFF);
/* 106 */     targets[2] = (byte)(Integer.parseInt(ipSegs[2]) & 0xFF);
/* 107 */     targets[3] = (byte)(Integer.parseInt(ipSegs[3]) & 0xFF);
/*     */     
/* 109 */     return targets;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int ipAddressStringToInt(String ipString) {
/* 119 */     byte[] targets = ipAddressStringToRaw(ipString);
/* 120 */     if (targets == null) {
/* 121 */       return 0;
/*     */     }
/*     */     
/* 124 */     int res = targets[3] & 0xFF | (targets[2] & 0xFF) << 8 | (targets[1] & 0xFF) << 16 | (targets[0] & 0xFF) << 24;
/* 125 */     return res;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static InetAddress ipAddressStringToInetAddr(String ipString) throws UnknownHostException {
/* 136 */     byte[] targets = ipAddressStringToRaw(ipString);
/* 137 */     if (targets == null) {
/* 138 */       throw new UnknownHostException(ipString);
/*     */     }
/*     */     
/* 141 */     return InetAddress.getByAddress(targets);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String intToIpAddressString(int ip) {
/* 151 */     int[] targets = new int[4];
/* 152 */     targets[3] = ip & 0xFF;
/* 153 */     targets[2] = ip >> 8 & 0xFF;
/* 154 */     targets[1] = ip >> 16 & 0xFF;
/* 155 */     targets[0] = ip >> 24 & 0xFF;
/*     */     
/* 157 */     return String.format("%d.%d.%d.%d", new Object[] { Integer.valueOf(targets[0]), Integer.valueOf(targets[1]), Integer.valueOf(targets[2]), Integer.valueOf(targets[3]) });
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/utils/NetUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */