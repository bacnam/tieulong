/*    */ package org.apache.mina.transport.vmpipe;
/*    */ 
/*    */ import java.net.SocketAddress;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class VmPipeAddress
/*    */   extends SocketAddress
/*    */   implements Comparable<VmPipeAddress>
/*    */ {
/*    */   private static final long serialVersionUID = 3257844376976830515L;
/*    */   private final int port;
/*    */   
/*    */   public VmPipeAddress(int port) {
/* 38 */     this.port = port;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int getPort() {
/* 45 */     return this.port;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 50 */     return this.port;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 55 */     if (o == null) {
/* 56 */       return false;
/*    */     }
/* 58 */     if (this == o) {
/* 59 */       return true;
/*    */     }
/* 61 */     if (o instanceof VmPipeAddress) {
/* 62 */       VmPipeAddress that = (VmPipeAddress)o;
/* 63 */       return (this.port == that.port);
/*    */     } 
/*    */     
/* 66 */     return false;
/*    */   }
/*    */   
/*    */   public int compareTo(VmPipeAddress o) {
/* 70 */     return this.port - o.port;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 75 */     if (this.port >= 0) {
/* 76 */       return "vm:server:" + this.port;
/*    */     }
/*    */     
/* 79 */     return "vm:client:" + -this.port;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/transport/vmpipe/VmPipeAddress.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */