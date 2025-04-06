/*     */ package org.apache.mina.filter.logging;
/*     */ 
/*     */ import java.net.InetSocketAddress;
/*     */ import java.util.Arrays;
/*     */ import java.util.EnumSet;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.apache.mina.core.filterchain.IoFilterEvent;
/*     */ import org.apache.mina.core.session.AttributeKey;
/*     */ import org.apache.mina.core.session.IoSession;
/*     */ import org.apache.mina.filter.util.CommonEventFilter;
/*     */ import org.slf4j.MDC;
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
/*     */ public class MdcInjectionFilter
/*     */   extends CommonEventFilter
/*     */ {
/*     */   public enum MdcKey
/*     */   {
/*  76 */     handlerClass, remoteAddress, localAddress, remoteIp, remotePort, localIp, localPort;
/*     */   }
/*     */ 
/*     */   
/*  80 */   private static final AttributeKey CONTEXT_KEY = new AttributeKey(MdcInjectionFilter.class, "context");
/*     */   
/*  82 */   private ThreadLocal<Integer> callDepth = new ThreadLocal<Integer>()
/*     */     {
/*     */       protected Integer initialValue() {
/*  85 */         return Integer.valueOf(0);
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private EnumSet<MdcKey> mdcKeys;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MdcInjectionFilter(EnumSet<MdcKey> keys) {
/*  99 */     this.mdcKeys = keys.clone();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MdcInjectionFilter(MdcKey... keys) {
/* 110 */     Set<MdcKey> keySet = new HashSet<MdcKey>(Arrays.asList(keys));
/* 111 */     this.mdcKeys = EnumSet.copyOf(keySet);
/*     */   }
/*     */   
/*     */   public MdcInjectionFilter() {
/* 115 */     this.mdcKeys = EnumSet.allOf(MdcKey.class);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void filter(IoFilterEvent event) throws Exception {
/* 122 */     int currentCallDepth = ((Integer)this.callDepth.get()).intValue();
/* 123 */     this.callDepth.set(Integer.valueOf(currentCallDepth + 1));
/* 124 */     Map<String, String> context = getAndFillContext(event.getSession());
/*     */     
/* 126 */     if (currentCallDepth == 0)
/*     */     {
/* 128 */       for (Map.Entry<String, String> e : context.entrySet()) {
/* 129 */         MDC.put(e.getKey(), e.getValue());
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     try {
/* 135 */       event.fire();
/*     */     } finally {
/* 137 */       if (currentCallDepth == 0) {
/*     */         
/* 139 */         for (String key : context.keySet()) {
/* 140 */           MDC.remove(key);
/*     */         }
/* 142 */         this.callDepth.remove();
/*     */       } else {
/* 144 */         this.callDepth.set(Integer.valueOf(currentCallDepth));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private Map<String, String> getAndFillContext(IoSession session) {
/* 150 */     Map<String, String> context = getContext(session);
/* 151 */     if (context.isEmpty()) {
/* 152 */       fillContext(session, context);
/*     */     }
/* 154 */     return context;
/*     */   }
/*     */ 
/*     */   
/*     */   private static Map<String, String> getContext(IoSession session) {
/* 159 */     Map<String, String> context = (Map<String, String>)session.getAttribute(CONTEXT_KEY);
/* 160 */     if (context == null) {
/* 161 */       context = new ConcurrentHashMap<String, String>();
/* 162 */       session.setAttribute(CONTEXT_KEY, context);
/*     */     } 
/* 164 */     return context;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void fillContext(IoSession session, Map<String, String> context) {
/* 174 */     if (this.mdcKeys.contains(MdcKey.handlerClass)) {
/* 175 */       context.put(MdcKey.handlerClass.name(), session.getHandler().getClass().getName());
/*     */     }
/* 177 */     if (this.mdcKeys.contains(MdcKey.remoteAddress)) {
/* 178 */       context.put(MdcKey.remoteAddress.name(), session.getRemoteAddress().toString());
/*     */     }
/* 180 */     if (this.mdcKeys.contains(MdcKey.localAddress)) {
/* 181 */       context.put(MdcKey.localAddress.name(), session.getLocalAddress().toString());
/*     */     }
/* 183 */     if (session.getTransportMetadata().getAddressType() == InetSocketAddress.class) {
/* 184 */       InetSocketAddress remoteAddress = (InetSocketAddress)session.getRemoteAddress();
/* 185 */       InetSocketAddress localAddress = (InetSocketAddress)session.getLocalAddress();
/*     */       
/* 187 */       if (this.mdcKeys.contains(MdcKey.remoteIp)) {
/* 188 */         context.put(MdcKey.remoteIp.name(), remoteAddress.getAddress().getHostAddress());
/*     */       }
/* 190 */       if (this.mdcKeys.contains(MdcKey.remotePort)) {
/* 191 */         context.put(MdcKey.remotePort.name(), String.valueOf(remoteAddress.getPort()));
/*     */       }
/* 193 */       if (this.mdcKeys.contains(MdcKey.localIp)) {
/* 194 */         context.put(MdcKey.localIp.name(), localAddress.getAddress().getHostAddress());
/*     */       }
/* 196 */       if (this.mdcKeys.contains(MdcKey.localPort)) {
/* 197 */         context.put(MdcKey.localPort.name(), String.valueOf(localAddress.getPort()));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String getProperty(IoSession session, String key) {
/* 203 */     if (key == null) {
/* 204 */       throw new IllegalArgumentException("key should not be null");
/*     */     }
/*     */     
/* 207 */     Map<String, String> context = getContext(session);
/* 208 */     String answer = context.get(key);
/* 209 */     if (answer != null) {
/* 210 */       return answer;
/*     */     }
/*     */     
/* 213 */     return MDC.get(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setProperty(IoSession session, String key, String value) {
/* 224 */     if (key == null) {
/* 225 */       throw new IllegalArgumentException("key should not be null");
/*     */     }
/* 227 */     if (value == null) {
/* 228 */       removeProperty(session, key);
/*     */     }
/* 230 */     Map<String, String> context = getContext(session);
/* 231 */     context.put(key, value);
/* 232 */     MDC.put(key, value);
/*     */   }
/*     */   
/*     */   public static void removeProperty(IoSession session, String key) {
/* 236 */     if (key == null) {
/* 237 */       throw new IllegalArgumentException("key should not be null");
/*     */     }
/* 239 */     Map<String, String> context = getContext(session);
/* 240 */     context.remove(key);
/* 241 */     MDC.remove(key);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/mina/filter/logging/MdcInjectionFilter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */