/*     */ package com.mchange.v2.c3p0.impl;
/*     */ 
/*     */ import com.mchange.lang.ByteUtils;
/*     */ import com.mchange.v1.identicator.Identicator;
/*     */ import com.mchange.v1.identicator.IdentityHashCodeIdenticator;
/*     */ import com.mchange.v2.c3p0.cfg.C3P0Config;
/*     */ import com.mchange.v2.encounter.EncounterCounter;
/*     */ import com.mchange.v2.encounter.EncounterUtils;
/*     */ import com.mchange.v2.log.MLevel;
/*     */ import com.mchange.v2.log.MLog;
/*     */ import com.mchange.v2.log.MLogger;
/*     */ import com.mchange.v2.log.jdk14logging.ForwardingLogger;
/*     */ import com.mchange.v2.ser.SerializableUtils;
/*     */ import com.mchange.v2.sql.SqlUtils;
/*     */ import com.mchange.v2.uid.UidUtils;
/*     */ import java.beans.BeanInfo;
/*     */ import java.beans.Introspector;
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.sql.Connection;
/*     */ import java.sql.SQLException;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Logger;
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
/*     */ public final class C3P0ImplUtils
/*     */ {
/*     */   private static final boolean CONDITIONAL_LONG_TOKENS = false;
/*  71 */   static final MLogger logger = MLog.getLogger(C3P0ImplUtils.class);
/*     */   
/*  73 */   public static final DbAuth NULL_AUTH = new DbAuth(null, null);
/*     */   
/*  75 */   public static final Object[] NOARGS = new Object[0];
/*     */   
/*  77 */   public static final Logger PARENT_LOGGER = (Logger)new ForwardingLogger(MLog.getLogger("com.mchange.v2.c3p0"), null);
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
/* 101 */   private static final EncounterCounter ID_TOKEN_COUNTER = createEncounterCounter();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final String VMID_PROPKEY = "com.mchange.v2.c3p0.VMID";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String VMID_PFX;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static EncounterCounter createEncounterCounter() {
/* 117 */     return EncounterUtils.syncWrap(EncounterUtils.createWeak((Identicator)IdentityHashCodeIdenticator.INSTANCE));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/* 124 */     String vmid = C3P0Config.getPropsFileConfigProperty("com.mchange.v2.c3p0.VMID");
/* 125 */     if (vmid == null || (vmid = vmid.trim()).equals("") || vmid.equals("AUTO")) {
/* 126 */       VMID_PFX = UidUtils.VM_ID + '|';
/* 127 */     } else if (vmid.equals("NONE")) {
/* 128 */       VMID_PFX = "";
/*     */     } else {
/* 130 */       VMID_PFX = vmid + "|";
/*     */     } 
/*     */   }
/*     */   
/* 134 */   static String connectionTesterClassName = null;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final String HASM_HEADER = "HexAsciiSerializedMap";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String allocateIdentityToken(Object o) {
/* 145 */     if (o == null) {
/* 146 */       return null;
/*     */     }
/*     */     
/* 149 */     String shortIdToken = Integer.toString(System.identityHashCode(o), 16);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 155 */     StringBuffer sb = new StringBuffer(128);
/* 156 */     sb.append(VMID_PFX); long count;
/* 157 */     if (ID_TOKEN_COUNTER != null && (count = ID_TOKEN_COUNTER.encounter(shortIdToken)) > 0L) {
/*     */       
/* 159 */       sb.append(shortIdToken);
/* 160 */       sb.append('#');
/* 161 */       sb.append(count);
/*     */     } else {
/*     */       
/* 164 */       sb.append(shortIdToken);
/*     */     } 
/* 166 */     String out = sb.toString().intern();
/*     */     
/* 168 */     return out;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DbAuth findAuth(Object o) throws SQLException {
/* 175 */     if (o == null) {
/* 176 */       return NULL_AUTH;
/*     */     }
/* 178 */     String user = null;
/* 179 */     String password = null;
/*     */     
/* 181 */     String overrideDefaultUser = null;
/* 182 */     String overrideDefaultPassword = null;
/*     */ 
/*     */     
/*     */     try {
/* 186 */       BeanInfo bi = Introspector.getBeanInfo(o.getClass());
/* 187 */       PropertyDescriptor[] pds = bi.getPropertyDescriptors();
/* 188 */       for (int i = 0, len = pds.length; i < len; i++) {
/*     */         
/* 190 */         PropertyDescriptor pd = pds[i];
/* 191 */         Class<?> propCl = pd.getPropertyType();
/* 192 */         String propName = pd.getName();
/* 193 */         if (propCl == String.class) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 199 */           Method readMethod = pd.getReadMethod();
/* 200 */           if (readMethod != null) {
/*     */             
/* 202 */             Object propVal = readMethod.invoke(o, NOARGS);
/* 203 */             String value = (String)propVal;
/* 204 */             if ("user".equals(propName)) {
/* 205 */               user = value;
/* 206 */             } else if ("password".equals(propName)) {
/* 207 */               password = value;
/* 208 */             } else if ("overrideDefaultUser".equals(propName)) {
/* 209 */               overrideDefaultUser = value;
/* 210 */             } else if ("overrideDefaultPassword".equals(propName)) {
/* 211 */               overrideDefaultPassword = value;
/*     */             } 
/*     */           } 
/*     */         } 
/* 215 */       }  if (overrideDefaultUser != null)
/* 216 */         return new DbAuth(overrideDefaultUser, overrideDefaultPassword); 
/* 217 */       if (user != null) {
/* 218 */         return new DbAuth(user, password);
/*     */       }
/* 220 */       return NULL_AUTH;
/*     */     }
/* 222 */     catch (Exception e) {
/*     */       
/* 224 */       if (logger.isLoggable(MLevel.FINE))
/* 225 */         logger.log(MLevel.FINE, "An exception occurred while trying to extract the default authentification info from a bean.", e); 
/* 226 */       throw SqlUtils.toSQLException(e);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static void resetTxnState(Connection pCon, boolean forceIgnoreUnresolvedTransactions, boolean autoCommitOnClose, boolean txnKnownResolved) throws SQLException {
/* 245 */     if (!forceIgnoreUnresolvedTransactions && !pCon.getAutoCommit()) {
/*     */       
/* 247 */       if (!autoCommitOnClose && !txnKnownResolved)
/*     */       {
/*     */         
/* 250 */         pCon.rollback();
/*     */       }
/* 252 */       pCon.setAutoCommit(true);
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
/*     */   public static boolean supportsMethod(Object target, String mname, Class[] argTypes) {
/*     */     try {
/* 286 */       return (target.getClass().getMethod(mname, argTypes) != null);
/* 287 */     } catch (NoSuchMethodException e) {
/* 288 */       return false;
/* 289 */     } catch (SecurityException e) {
/*     */       
/* 291 */       if (logger.isLoggable(MLevel.FINE)) {
/* 292 */         logger.log(MLevel.FINE, "We were denied access in a check of whether " + target + " supports method " + mname + ". Prob means external clients have no access, returning false.", e);
/*     */       }
/*     */ 
/*     */       
/* 296 */       return false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String createUserOverridesAsString(Map userOverrides) throws IOException {
/* 304 */     StringBuffer sb = new StringBuffer();
/* 305 */     sb.append("HexAsciiSerializedMap");
/* 306 */     sb.append('[');
/* 307 */     sb.append(ByteUtils.toHexAscii(SerializableUtils.toByteArray(userOverrides)));
/* 308 */     sb.append(']');
/* 309 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public static Map parseUserOverridesAsString(String userOverridesAsString) throws IOException, ClassNotFoundException {
/* 314 */     if (userOverridesAsString != null) {
/*     */       
/* 316 */       String hexAscii = userOverridesAsString.substring("HexAsciiSerializedMap".length() + 1, userOverridesAsString.length() - 1);
/* 317 */       byte[] serBytes = ByteUtils.fromHexAscii(hexAscii);
/* 318 */       return Collections.unmodifiableMap((Map<?, ?>)SerializableUtils.fromByteArray(serBytes));
/*     */     } 
/*     */     
/* 321 */     return Collections.EMPTY_MAP;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void assertCompileTimePresenceOfJdbc4_Jdk17Api(NewProxyConnection npc) throws SQLException {
/* 329 */     npc.getNetworkTimeout();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/impl/C3P0ImplUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */