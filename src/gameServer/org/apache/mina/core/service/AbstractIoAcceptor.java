/*     */ package org.apache.mina.core.service;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.SocketAddress;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Executor;
/*     */ import org.apache.mina.core.RuntimeIoException;
/*     */ import org.apache.mina.core.session.IoSessionConfig;
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
/*     */ public abstract class AbstractIoAcceptor
/*     */   extends AbstractIoService
/*     */   implements IoAcceptor
/*     */ {
/*  45 */   private final List<SocketAddress> defaultLocalAddresses = new ArrayList<SocketAddress>();
/*     */   
/*  47 */   private final List<SocketAddress> unmodifiableDefaultLocalAddresses = Collections.unmodifiableList(this.defaultLocalAddresses);
/*     */ 
/*     */   
/*  50 */   private final Set<SocketAddress> boundAddresses = new HashSet<SocketAddress>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean disconnectOnUnbind = true;
/*     */ 
/*     */ 
/*     */   
/*  59 */   protected final Object bindLock = new Object();
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
/*     */   protected AbstractIoAcceptor(IoSessionConfig sessionConfig, Executor executor) {
/*  76 */     super(sessionConfig, executor);
/*  77 */     this.defaultLocalAddresses.add(null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SocketAddress getLocalAddress() {
/*  84 */     Set<SocketAddress> localAddresses = getLocalAddresses();
/*  85 */     if (localAddresses.isEmpty()) {
/*  86 */       return null;
/*     */     }
/*     */     
/*  89 */     return localAddresses.iterator().next();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final Set<SocketAddress> getLocalAddresses() {
/*  96 */     Set<SocketAddress> localAddresses = new HashSet<SocketAddress>();
/*     */     
/*  98 */     synchronized (this.boundAddresses) {
/*  99 */       localAddresses.addAll(this.boundAddresses);
/*     */     } 
/*     */     
/* 102 */     return localAddresses;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SocketAddress getDefaultLocalAddress() {
/* 109 */     if (this.defaultLocalAddresses.isEmpty()) {
/* 110 */       return null;
/*     */     }
/* 112 */     return this.defaultLocalAddresses.iterator().next();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setDefaultLocalAddress(SocketAddress localAddress) {
/* 119 */     setDefaultLocalAddresses(localAddress, new SocketAddress[0]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final List<SocketAddress> getDefaultLocalAddresses() {
/* 126 */     return this.unmodifiableDefaultLocalAddresses;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setDefaultLocalAddresses(List<? extends SocketAddress> localAddresses) {
/* 134 */     if (localAddresses == null) {
/* 135 */       throw new IllegalArgumentException("localAddresses");
/*     */     }
/* 137 */     setDefaultLocalAddresses(localAddresses);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setDefaultLocalAddresses(Iterable<? extends SocketAddress> localAddresses) {
/* 144 */     if (localAddresses == null) {
/* 145 */       throw new IllegalArgumentException("localAddresses");
/*     */     }
/*     */     
/* 148 */     synchronized (this.bindLock) {
/* 149 */       synchronized (this.boundAddresses) {
/* 150 */         if (!this.boundAddresses.isEmpty()) {
/* 151 */           throw new IllegalStateException("localAddress can't be set while the acceptor is bound.");
/*     */         }
/*     */         
/* 154 */         Collection<SocketAddress> newLocalAddresses = new ArrayList<SocketAddress>();
/*     */         
/* 156 */         for (SocketAddress a : localAddresses) {
/* 157 */           checkAddressType(a);
/* 158 */           newLocalAddresses.add(a);
/*     */         } 
/*     */         
/* 161 */         if (newLocalAddresses.isEmpty()) {
/* 162 */           throw new IllegalArgumentException("empty localAddresses");
/*     */         }
/*     */         
/* 165 */         this.defaultLocalAddresses.clear();
/* 166 */         this.defaultLocalAddresses.addAll(newLocalAddresses);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setDefaultLocalAddresses(SocketAddress firstLocalAddress, SocketAddress... otherLocalAddresses) {
/* 176 */     if (otherLocalAddresses == null) {
/* 177 */       otherLocalAddresses = new SocketAddress[0];
/*     */     }
/*     */     
/* 180 */     Collection<SocketAddress> newLocalAddresses = new ArrayList<SocketAddress>(otherLocalAddresses.length + 1);
/*     */     
/* 182 */     newLocalAddresses.add(firstLocalAddress);
/* 183 */     for (SocketAddress a : otherLocalAddresses) {
/* 184 */       newLocalAddresses.add(a);
/*     */     }
/*     */     
/* 187 */     setDefaultLocalAddresses(newLocalAddresses);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final boolean isCloseOnDeactivation() {
/* 194 */     return this.disconnectOnUnbind;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void setCloseOnDeactivation(boolean disconnectClientsOnUnbind) {
/* 201 */     this.disconnectOnUnbind = disconnectClientsOnUnbind;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void bind() throws IOException {
/* 208 */     bind(getDefaultLocalAddresses());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void bind(SocketAddress localAddress) throws IOException {
/* 215 */     if (localAddress == null) {
/* 216 */       throw new IllegalArgumentException("localAddress");
/*     */     }
/*     */     
/* 219 */     List<SocketAddress> localAddresses = new ArrayList<SocketAddress>(1);
/* 220 */     localAddresses.add(localAddress);
/* 221 */     bind(localAddresses);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void bind(SocketAddress... addresses) throws IOException {
/* 228 */     if (addresses == null || addresses.length == 0) {
/* 229 */       bind(getDefaultLocalAddresses());
/*     */       
/*     */       return;
/*     */     } 
/* 233 */     List<SocketAddress> localAddresses = new ArrayList<SocketAddress>(2);
/*     */     
/* 235 */     for (SocketAddress address : addresses) {
/* 236 */       localAddresses.add(address);
/*     */     }
/*     */     
/* 239 */     bind(localAddresses);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void bind(SocketAddress firstLocalAddress, SocketAddress... addresses) throws IOException {
/* 246 */     if (firstLocalAddress == null) {
/* 247 */       bind(getDefaultLocalAddresses());
/*     */     }
/*     */     
/* 250 */     if (addresses == null || addresses.length == 0) {
/* 251 */       bind(getDefaultLocalAddresses());
/*     */       
/*     */       return;
/*     */     } 
/* 255 */     List<SocketAddress> localAddresses = new ArrayList<SocketAddress>(2);
/* 256 */     localAddresses.add(firstLocalAddress);
/*     */     
/* 258 */     for (SocketAddress address : addresses) {
/* 259 */       localAddresses.add(address);
/*     */     }
/*     */     
/* 262 */     bind(localAddresses);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void bind(Iterable<? extends SocketAddress> localAddresses) throws IOException {
/* 269 */     if (isDisposing()) {
/* 270 */       throw new IllegalStateException("Already disposed.");
/*     */     }
/*     */     
/* 273 */     if (localAddresses == null) {
/* 274 */       throw new IllegalArgumentException("localAddresses");
/*     */     }
/*     */     
/* 277 */     List<SocketAddress> localAddressesCopy = new ArrayList<SocketAddress>();
/*     */     
/* 279 */     for (SocketAddress a : localAddresses) {
/* 280 */       checkAddressType(a);
/* 281 */       localAddressesCopy.add(a);
/*     */     } 
/*     */     
/* 284 */     if (localAddressesCopy.isEmpty()) {
/* 285 */       throw new IllegalArgumentException("localAddresses is empty.");
/*     */     }
/*     */     
/* 288 */     boolean activate = false;
/* 289 */     synchronized (this.bindLock) {
/* 290 */       synchronized (this.boundAddresses) {
/* 291 */         if (this.boundAddresses.isEmpty()) {
/* 292 */           activate = true;
/*     */         }
/*     */       } 
/*     */       
/* 296 */       if (getHandler() == null) {
/* 297 */         throw new IllegalStateException("handler is not set.");
/*     */       }
/*     */       
/*     */       try {
/* 301 */         Set<SocketAddress> addresses = bindInternal(localAddressesCopy);
/*     */         
/* 303 */         synchronized (this.boundAddresses) {
/* 304 */           this.boundAddresses.addAll(addresses);
/*     */         } 
/* 306 */       } catch (IOException e) {
/* 307 */         throw e;
/* 308 */       } catch (RuntimeException e) {
/* 309 */         throw e;
/* 310 */       } catch (Exception e) {
/* 311 */         throw new RuntimeIoException("Failed to bind to: " + getLocalAddresses(), e);
/*     */       } 
/*     */     } 
/*     */     
/* 315 */     if (activate) {
/* 316 */       getListeners().fireServiceActivated();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void unbind() {
/* 324 */     unbind(getLocalAddresses());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void unbind(SocketAddress localAddress) {
/* 331 */     if (localAddress == null) {
/* 332 */       throw new IllegalArgumentException("localAddress");
/*     */     }
/*     */     
/* 335 */     List<SocketAddress> localAddresses = new ArrayList<SocketAddress>(1);
/* 336 */     localAddresses.add(localAddress);
/* 337 */     unbind(localAddresses);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void unbind(SocketAddress firstLocalAddress, SocketAddress... otherLocalAddresses) {
/* 344 */     if (firstLocalAddress == null) {
/* 345 */       throw new IllegalArgumentException("firstLocalAddress");
/*     */     }
/* 347 */     if (otherLocalAddresses == null) {
/* 348 */       throw new IllegalArgumentException("otherLocalAddresses");
/*     */     }
/*     */     
/* 351 */     List<SocketAddress> localAddresses = new ArrayList<SocketAddress>();
/* 352 */     localAddresses.add(firstLocalAddress);
/* 353 */     Collections.addAll(localAddresses, otherLocalAddresses);
/* 354 */     unbind(localAddresses);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public final void unbind(Iterable<? extends SocketAddress> localAddresses) {
/* 361 */     if (localAddresses == null) {
/* 362 */       throw new IllegalArgumentException("localAddresses");
/*     */     }
/*     */     
/* 365 */     boolean deactivate = false;
/* 366 */     synchronized (this.bindLock) {
/* 367 */       synchronized (this.boundAddresses) {
/* 368 */         if (this.boundAddresses.isEmpty()) {
/*     */           return;
/*     */         }
/*     */         
/* 372 */         List<SocketAddress> localAddressesCopy = new ArrayList<SocketAddress>();
/* 373 */         int specifiedAddressCount = 0;
/*     */         
/* 375 */         for (SocketAddress a : localAddresses) {
/* 376 */           specifiedAddressCount++;
/*     */           
/* 378 */           if (a != null && this.boundAddresses.contains(a)) {
/* 379 */             localAddressesCopy.add(a);
/*     */           }
/*     */         } 
/*     */         
/* 383 */         if (specifiedAddressCount == 0) {
/* 384 */           throw new IllegalArgumentException("localAddresses is empty.");
/*     */         }
/*     */         
/* 387 */         if (!localAddressesCopy.isEmpty()) {
/*     */           try {
/* 389 */             unbind0(localAddressesCopy);
/* 390 */           } catch (RuntimeException e) {
/* 391 */             throw e;
/* 392 */           } catch (Exception e) {
/* 393 */             throw new RuntimeIoException("Failed to unbind from: " + getLocalAddresses(), e);
/*     */           } 
/*     */           
/* 396 */           this.boundAddresses.removeAll(localAddressesCopy);
/*     */           
/* 398 */           if (this.boundAddresses.isEmpty()) {
/* 399 */             deactivate = true;
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 405 */     if (deactivate) {
/* 406 */       getListeners().fireServiceDeactivated();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract Set<SocketAddress> bindInternal(List<? extends SocketAddress> paramList) throws Exception;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void unbind0(List<? extends SocketAddress> paramList) throws Exception;
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 423 */     TransportMetadata m = getTransportMetadata();
/* 424 */     return '(' + m.getProviderName() + ' ' + m.getName() + " acceptor: " + (isActive() ? ("localAddress(es): " + getLocalAddresses() + ", managedSessionCount: " + getManagedSessionCount()) : "not bound") + ')';
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void checkAddressType(SocketAddress a) {
/* 434 */     if (a != null && !getTransportMetadata().getAddressType().isAssignableFrom(a.getClass()))
/* 435 */       throw new IllegalArgumentException("localAddress type: " + a.getClass().getSimpleName() + " (expected: " + getTransportMetadata().getAddressType().getSimpleName() + ")"); 
/*     */   }
/*     */   
/*     */   public static class AcceptorOperationFuture
/*     */     extends AbstractIoService.ServiceOperationFuture
/*     */   {
/*     */     private final List<SocketAddress> localAddresses;
/*     */     
/*     */     public AcceptorOperationFuture(List<? extends SocketAddress> localAddresses) {
/* 444 */       this.localAddresses = new ArrayList<SocketAddress>(localAddresses);
/*     */     }
/*     */     
/*     */     public final List<SocketAddress> getLocalAddresses() {
/* 448 */       return Collections.unmodifiableList(this.localAddresses);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 455 */       StringBuilder sb = new StringBuilder();
/*     */       
/* 457 */       sb.append("Acceptor operation : ");
/*     */       
/* 459 */       if (this.localAddresses != null) {
/* 460 */         boolean isFirst = true;
/*     */         
/* 462 */         for (SocketAddress address : this.localAddresses) {
/* 463 */           if (isFirst) {
/* 464 */             isFirst = false;
/*     */           } else {
/* 466 */             sb.append(", ");
/*     */           } 
/*     */           
/* 469 */           sb.append(address);
/*     */         } 
/*     */       } 
/* 472 */       return sb.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/core/service/AbstractIoAcceptor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */