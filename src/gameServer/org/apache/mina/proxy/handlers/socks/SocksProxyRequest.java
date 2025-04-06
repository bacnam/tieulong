/*     */ package org.apache.mina.proxy.handlers.socks;
/*     */ 
/*     */ import java.net.InetSocketAddress;
/*     */ import org.apache.mina.proxy.handlers.ProxyRequest;
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
/*     */ public class SocksProxyRequest
/*     */   extends ProxyRequest
/*     */ {
/*     */   private byte protocolVersion;
/*     */   private byte commandCode;
/*     */   private String userName;
/*     */   private String password;
/*     */   private String host;
/*     */   private int port;
/*     */   private String serviceKerberosName;
/*     */   
/*     */   public SocksProxyRequest(byte protocolVersion, byte commandCode, InetSocketAddress endpointAddress, String userName) {
/*  78 */     super(endpointAddress);
/*  79 */     this.protocolVersion = protocolVersion;
/*  80 */     this.commandCode = commandCode;
/*  81 */     this.userName = userName;
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
/*     */   public SocksProxyRequest(byte commandCode, String host, int port, String userName) {
/*  93 */     this.protocolVersion = 4;
/*  94 */     this.commandCode = commandCode;
/*  95 */     this.userName = userName;
/*  96 */     this.host = host;
/*  97 */     this.port = port;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getIpAddress() {
/* 108 */     if (getEndpointAddress() == null) {
/* 109 */       return SocksProxyConstants.FAKE_IP;
/*     */     }
/*     */     
/* 112 */     return getEndpointAddress().getAddress().getAddress();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getPort() {
/* 121 */     byte[] port = new byte[2];
/* 122 */     int p = (getEndpointAddress() == null) ? this.port : getEndpointAddress().getPort();
/* 123 */     port[1] = (byte)p;
/* 124 */     port[0] = (byte)(p >> 8);
/* 125 */     return port;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte getCommandCode() {
/* 134 */     return this.commandCode;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public byte getProtocolVersion() {
/* 143 */     return this.protocolVersion;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUserName() {
/* 152 */     return this.userName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final synchronized String getHost() {
/* 161 */     if (this.host == null) {
/* 162 */       InetSocketAddress adr = getEndpointAddress();
/*     */       
/* 164 */       if (adr != null && !adr.isUnresolved()) {
/* 165 */         this.host = getEndpointAddress().getHostName();
/*     */       }
/*     */     } 
/*     */     
/* 169 */     return this.host;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPassword() {
/* 178 */     return this.password;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPassword(String password) {
/* 187 */     this.password = password;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getServiceKerberosName() {
/* 196 */     return this.serviceKerberosName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setServiceKerberosName(String serviceKerberosName) {
/* 205 */     this.serviceKerberosName = serviceKerberosName;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/proxy/handlers/socks/SocksProxyRequest.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */