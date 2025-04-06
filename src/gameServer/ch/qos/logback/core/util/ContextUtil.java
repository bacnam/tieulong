/*     */ package ch.qos.logback.core.util;
/*     */ 
/*     */ import ch.qos.logback.core.Context;
/*     */ import ch.qos.logback.core.spi.ContextAwareBase;
/*     */ import java.net.InetAddress;
/*     */ import java.net.NetworkInterface;
/*     */ import java.net.SocketException;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Properties;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ContextUtil
/*     */   extends ContextAwareBase
/*     */ {
/*     */   public ContextUtil(Context context) {
/*  32 */     setContext(context);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getLocalHostName() throws UnknownHostException, SocketException {
/*     */     try {
/*  38 */       InetAddress localhost = InetAddress.getLocalHost();
/*  39 */       return localhost.getHostName();
/*  40 */     } catch (UnknownHostException e) {
/*  41 */       return getLocalAddressAsString();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static String getLocalAddressAsString() throws UnknownHostException, SocketException {
/*  47 */     Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
/*     */     
/*  49 */     while (interfaces != null && interfaces.hasMoreElements()) {
/*  50 */       Enumeration<InetAddress> addresses = ((NetworkInterface)interfaces.nextElement()).getInetAddresses();
/*     */       
/*  52 */       while (addresses != null && addresses.hasMoreElements()) {
/*  53 */         InetAddress address = addresses.nextElement();
/*  54 */         if (acceptableAddress(address)) {
/*  55 */           return address.getHostAddress();
/*     */         }
/*     */       } 
/*     */     } 
/*  59 */     throw new UnknownHostException();
/*     */   }
/*     */   
/*     */   private static boolean acceptableAddress(InetAddress address) {
/*  63 */     return (address != null && !address.isLoopbackAddress() && !address.isAnyLocalAddress() && !address.isLinkLocalAddress());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addHostNameAsProperty() {
/*     */     try {
/*  74 */       String localhostName = getLocalHostName();
/*  75 */       this.context.putProperty("HOSTNAME", localhostName);
/*  76 */     } catch (UnknownHostException e) {
/*  77 */       addError("Failed to get local hostname", e);
/*  78 */     } catch (SocketException e) {
/*  79 */       addError("Failed to get local hostname", e);
/*  80 */     } catch (SecurityException e) {
/*  81 */       addError("Failed to get local hostname", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addProperties(Properties props) {
/*  86 */     if (props == null) {
/*     */       return;
/*     */     }
/*  89 */     Iterator<String> i = props.keySet().iterator();
/*  90 */     while (i.hasNext()) {
/*  91 */       String key = i.next();
/*  92 */       this.context.putProperty(key, props.getProperty(key));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addGroovyPackages(List<String> frameworkPackages) {
/*  99 */     addFrameworkPackage(frameworkPackages, "org.codehaus.groovy.runtime");
/*     */   }
/*     */   
/*     */   public void addFrameworkPackage(List<String> frameworkPackages, String packageName) {
/* 103 */     if (!frameworkPackages.contains(packageName))
/* 104 */       frameworkPackages.add(packageName); 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/ch/qos/logback/core/util/ContextUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */