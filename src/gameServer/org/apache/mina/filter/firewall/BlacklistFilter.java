/*     */ package org.apache.mina.filter.firewall;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.SocketAddress;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import org.apache.mina.core.filterchain.IoFilter;
/*     */ import org.apache.mina.core.filterchain.IoFilterAdapter;
/*     */ import org.apache.mina.core.session.IdleStatus;
/*     */ import org.apache.mina.core.session.IoSession;
/*     */ import org.apache.mina.core.write.WriteRequest;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
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
/*     */ public class BlacklistFilter
/*     */   extends IoFilterAdapter
/*     */ {
/*  45 */   private final List<Subnet> blacklist = new CopyOnWriteArrayList<Subnet>();
/*     */   
/*  47 */   private static final Logger LOGGER = LoggerFactory.getLogger(BlacklistFilter.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBlacklist(InetAddress[] addresses) {
/*  57 */     if (addresses == null) {
/*  58 */       throw new IllegalArgumentException("addresses");
/*     */     }
/*     */     
/*  61 */     this.blacklist.clear();
/*     */     
/*  63 */     for (int i = 0; i < addresses.length; i++) {
/*  64 */       InetAddress addr = addresses[i];
/*  65 */       block(addr);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSubnetBlacklist(Subnet[] subnets) {
/*  77 */     if (subnets == null) {
/*  78 */       throw new IllegalArgumentException("Subnets must not be null");
/*     */     }
/*     */     
/*  81 */     this.blacklist.clear();
/*     */     
/*  83 */     for (Subnet subnet : subnets) {
/*  84 */       block(subnet);
/*     */     }
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
/*     */ 
/*     */   
/*     */   public void setBlacklist(Iterable<InetAddress> addresses) {
/*  99 */     if (addresses == null) {
/* 100 */       throw new IllegalArgumentException("addresses");
/*     */     }
/*     */     
/* 103 */     this.blacklist.clear();
/*     */     
/* 105 */     for (InetAddress address : addresses) {
/* 106 */       block(address);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSubnetBlacklist(Iterable<Subnet> subnets) {
/* 118 */     if (subnets == null) {
/* 119 */       throw new IllegalArgumentException("Subnets must not be null");
/*     */     }
/*     */     
/* 122 */     this.blacklist.clear();
/*     */     
/* 124 */     for (Subnet subnet : subnets) {
/* 125 */       block(subnet);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void block(InetAddress address) {
/* 133 */     if (address == null) {
/* 134 */       throw new IllegalArgumentException("Adress to block can not be null");
/*     */     }
/*     */     
/* 137 */     block(new Subnet(address, 32));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void block(Subnet subnet) {
/* 144 */     if (subnet == null) {
/* 145 */       throw new IllegalArgumentException("Subnet can not be null");
/*     */     }
/*     */     
/* 148 */     this.blacklist.add(subnet);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void unblock(InetAddress address) {
/* 155 */     if (address == null) {
/* 156 */       throw new IllegalArgumentException("Adress to unblock can not be null");
/*     */     }
/*     */     
/* 159 */     unblock(new Subnet(address, 32));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void unblock(Subnet subnet) {
/* 166 */     if (subnet == null) {
/* 167 */       throw new IllegalArgumentException("Subnet can not be null");
/*     */     }
/*     */     
/* 170 */     this.blacklist.remove(subnet);
/*     */   }
/*     */ 
/*     */   
/*     */   public void sessionCreated(IoFilter.NextFilter nextFilter, IoSession session) {
/* 175 */     if (!isBlocked(session)) {
/*     */       
/* 177 */       nextFilter.sessionCreated(session);
/*     */     } else {
/* 179 */       blockSession(session);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void sessionOpened(IoFilter.NextFilter nextFilter, IoSession session) throws Exception {
/* 185 */     if (!isBlocked(session)) {
/*     */       
/* 187 */       nextFilter.sessionOpened(session);
/*     */     } else {
/* 189 */       blockSession(session);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void sessionClosed(IoFilter.NextFilter nextFilter, IoSession session) throws Exception {
/* 195 */     if (!isBlocked(session)) {
/*     */       
/* 197 */       nextFilter.sessionClosed(session);
/*     */     } else {
/* 199 */       blockSession(session);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void sessionIdle(IoFilter.NextFilter nextFilter, IoSession session, IdleStatus status) throws Exception {
/* 205 */     if (!isBlocked(session)) {
/*     */       
/* 207 */       nextFilter.sessionIdle(session, status);
/*     */     } else {
/* 209 */       blockSession(session);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void messageReceived(IoFilter.NextFilter nextFilter, IoSession session, Object message) {
/* 215 */     if (!isBlocked(session)) {
/*     */       
/* 217 */       nextFilter.messageReceived(session, message);
/*     */     } else {
/* 219 */       blockSession(session);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void messageSent(IoFilter.NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
/* 225 */     if (!isBlocked(session)) {
/*     */       
/* 227 */       nextFilter.messageSent(session, writeRequest);
/*     */     } else {
/* 229 */       blockSession(session);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void blockSession(IoSession session) {
/* 234 */     LOGGER.warn("Remote address in the blacklist; closing.");
/* 235 */     session.close(true);
/*     */   }
/*     */   
/*     */   private boolean isBlocked(IoSession session) {
/* 239 */     SocketAddress remoteAddress = session.getRemoteAddress();
/*     */     
/* 241 */     if (remoteAddress instanceof InetSocketAddress) {
/* 242 */       InetAddress address = ((InetSocketAddress)remoteAddress).getAddress();
/*     */ 
/*     */       
/* 245 */       for (Subnet subnet : this.blacklist) {
/* 246 */         if (subnet.inSubnet(address)) {
/* 247 */           return true;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 252 */     return false;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/firewall/BlacklistFilter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */