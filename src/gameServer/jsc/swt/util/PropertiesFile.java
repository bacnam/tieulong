/*     */ package jsc.swt.util;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
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
/*     */ public class PropertiesFile
/*     */   extends Properties
/*     */ {
/*  24 */   String fileName = "";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertiesFile() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public PropertiesFile(String paramString) {
/*  43 */     this.fileName = paramString;
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
/*     */   public PropertiesFile(String paramString, Properties paramProperties) {
/*  58 */     super(paramProperties);
/*  59 */     this.fileName = paramString;
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
/*     */   public boolean getBoolProperty(String paramString) {
/*  72 */     return getBoolProperty(paramString, false);
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
/*     */   public boolean getBoolProperty(String paramString, boolean paramBoolean) {
/*  86 */     String str = getProperty(paramString);
/*  87 */     if (str == null) return paramBoolean; 
/*  88 */     return str.equals("TRUE");
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
/*     */   public double getDoubleProperty(String paramString) {
/* 103 */     return getDoubleProperty(paramString, 0.0D);
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
/*     */   public double getDoubleProperty(String paramString, double paramDouble) {
/* 118 */     String str = getProperty(paramString); 
/* 119 */     try { return (new Double(str)).doubleValue(); }
/* 120 */     catch (NumberFormatException numberFormatException) { return paramDouble; }
/*     */   
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
/*     */   public int getIntProperty(String paramString) {
/* 134 */     return getIntProperty(paramString, 0);
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
/*     */   public int getIntProperty(String paramString, int paramInt) {
/* 149 */     String str = getProperty(paramString); 
/* 150 */     try { return (new Integer(str)).intValue(); }
/* 151 */     catch (NumberFormatException numberFormatException) { return paramInt; }
/*     */   
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
/*     */   public long getLongProperty(String paramString) {
/* 165 */     return getLongProperty(paramString, 0L);
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
/*     */   public long getLongProperty(String paramString, long paramLong) {
/* 180 */     String str = getProperty(paramString); 
/* 181 */     try { return (new Long(str)).longValue(); }
/* 182 */     catch (NumberFormatException numberFormatException) { return paramLong; }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean load() {
/*     */     try {
/* 194 */       DataInputStream dataInputStream = new DataInputStream(new FileInputStream(this.fileName));
/* 195 */       load(dataInputStream);
/* 196 */       dataInputStream.close();
/* 197 */       return true;
/*     */     } catch (IOException iOException) {
/*     */       
/* 200 */       return false;
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
/*     */   public boolean load(String paramString) {
/* 213 */     this.fileName = paramString;
/* 214 */     return load();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean save(String paramString) {
/*     */     try {
/* 226 */       DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(this.fileName));
/* 227 */       store(dataOutputStream, paramString);
/* 228 */       dataOutputStream.close();
/* 229 */       return true;
/*     */     } catch (IOException iOException) {
/* 231 */       return false;
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
/*     */   public boolean save(String paramString1, String paramString2) {
/* 245 */     this.fileName = paramString1;
/* 246 */     return save(paramString2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProperty(String paramString, boolean paramBoolean) {
/* 257 */     String str = "FALSE";
/* 258 */     if (paramBoolean) str = "TRUE"; 
/* 259 */     setProperty(paramString, str);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProperty(String paramString, int paramInt) {
/* 269 */     setProperty(paramString, Integer.toString(paramInt));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProperty(String paramString, long paramLong) {
/* 279 */     setProperty(paramString, Long.toString(paramLong));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProperty(String paramString, double paramDouble) {
/* 289 */     setProperty(paramString, Double.toString(paramDouble));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) {
/* 299 */       PropertiesFile propertiesFile1 = new PropertiesFile();
/* 300 */       propertiesFile1.setProperty("Boolean", true);
/* 301 */       propertiesFile1.setProperty("Int", -1);
/* 302 */       propertiesFile1.setProperty("Long", -1);
/* 303 */       propertiesFile1.setProperty("Double", Math.E);
/* 304 */       PropertiesFile propertiesFile2 = new PropertiesFile("C:\\WINDOWS\\TEST.PRO", propertiesFile1);
/* 305 */       propertiesFile2.load();
/* 306 */       System.out.println(propertiesFile2.getBoolProperty("Boolean"));
/* 307 */       System.out.println(propertiesFile2.getIntProperty("Int"));
/* 308 */       System.out.println(propertiesFile2.getLongProperty("Long"));
/* 309 */       System.out.println(propertiesFile2.getDoubleProperty("Double"));
/* 310 */       propertiesFile2.setProperty("Boolean", true);
/* 311 */       propertiesFile2.setProperty("Int", 42);
/* 312 */       propertiesFile2.setProperty("Long", 666);
/* 313 */       propertiesFile2.setProperty("Double", Math.PI);
/* 314 */       propertiesFile2.save("Test properties");
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/util/PropertiesFile.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */