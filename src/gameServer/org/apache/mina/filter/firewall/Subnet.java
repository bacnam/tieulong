/*     */ package org.apache.mina.filter.firewall;
/*     */ 
/*     */ import java.net.InetAddress;
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
/*     */ public class Subnet
/*     */ {
/*     */   private static final int IP_MASK_V4 = -2147483648;
/*     */   private static final long IP_MASK_V6 = -9223372036854775808L;
/*     */   private static final int BYTE_MASK = 255;
/*     */   private InetAddress subnet;
/*     */   private int subnetInt;
/*     */   private long subnetLong;
/*     */   private long subnetMask;
/*     */   private int suffix;
/*     */   
/*     */   public Subnet(InetAddress subnet, int mask) {
/*  61 */     if (subnet == null) {
/*  62 */       throw new IllegalArgumentException("Subnet address can not be null");
/*     */     }
/*     */     
/*  65 */     if (!(subnet instanceof java.net.Inet4Address) && !(subnet instanceof java.net.Inet6Address)) {
/*  66 */       throw new IllegalArgumentException("Only IPv4 and IPV6 supported");
/*     */     }
/*     */     
/*  69 */     if (subnet instanceof java.net.Inet4Address) {
/*     */       
/*  71 */       if (mask < 0 || mask > 32) {
/*  72 */         throw new IllegalArgumentException("Mask has to be an integer between 0 and 32 for an IPV4 address");
/*     */       }
/*  74 */       this.subnet = subnet;
/*  75 */       this.subnetInt = toInt(subnet);
/*  76 */       this.suffix = mask;
/*     */ 
/*     */       
/*  79 */       this.subnetMask = (Integer.MIN_VALUE >> mask - 1);
/*     */     }
/*     */     else {
/*     */       
/*  83 */       if (mask < 0 || mask > 128) {
/*  84 */         throw new IllegalArgumentException("Mask has to be an integer between 0 and 128 for an IPV6 address");
/*     */       }
/*  86 */       this.subnet = subnet;
/*  87 */       this.subnetLong = toLong(subnet);
/*  88 */       this.suffix = mask;
/*     */ 
/*     */       
/*  91 */       this.subnetMask = Long.MIN_VALUE >> mask - 1;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int toInt(InetAddress inetAddress) {
/* 100 */     byte[] address = inetAddress.getAddress();
/* 101 */     int result = 0;
/*     */     
/* 103 */     for (int i = 0; i < address.length; i++) {
/* 104 */       result <<= 8;
/* 105 */       result |= address[i] & 0xFF;
/*     */     } 
/*     */     
/* 108 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long toLong(InetAddress inetAddress) {
/* 115 */     byte[] address = inetAddress.getAddress();
/* 116 */     long result = 0L;
/*     */     
/* 118 */     for (int i = 0; i < address.length; i++) {
/* 119 */       result <<= 8L;
/* 120 */       result |= (address[i] & 0xFF);
/*     */     } 
/*     */     
/* 123 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private long toSubnet(InetAddress address) {
/* 134 */     if (address instanceof java.net.Inet4Address) {
/* 135 */       return (toInt(address) & (int)this.subnetMask);
/*     */     }
/* 137 */     return toLong(address) & this.subnetMask;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean inSubnet(InetAddress address) {
/* 147 */     if (address.isAnyLocalAddress()) {
/* 148 */       return true;
/*     */     }
/*     */     
/* 151 */     if (address instanceof java.net.Inet4Address) {
/* 152 */       return ((int)toSubnet(address) == this.subnetInt);
/*     */     }
/* 154 */     return (toSubnet(address) == this.subnetLong);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 163 */     return this.subnet.getHostAddress() + "/" + this.suffix;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 168 */     if (!(obj instanceof Subnet)) {
/* 169 */       return false;
/*     */     }
/*     */     
/* 172 */     Subnet other = (Subnet)obj;
/*     */     
/* 174 */     return (other.subnetInt == this.subnetInt && other.suffix == this.suffix);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/firewall/Subnet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */