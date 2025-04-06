/*     */ package jsc.swt.util;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Vector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IniFiles
/*     */ {
/*  46 */   String inifile = "";
/*  47 */   Vector INIFILE = new Vector();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IniFiles(String paramString) {
/*  58 */     this.inifile = paramString;
/*     */   }
/*     */   
/*     */   String bis(String paramString1, String paramString2) {
/*  62 */     if (paramString1.indexOf(paramString2) != -1)
/*  63 */       return paramString1.substring(0, paramString1.indexOf(paramString2)); 
/*  64 */     return "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getBoolValue(String paramString1, String paramString2) {
/*  75 */     String str = getValue(paramString1, paramString2);
/*  76 */     return str.equals("TRUE");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getIntValue(String paramString1, String paramString2) {
/*  87 */     String str = getValue(paramString1, paramString2);
/*  88 */     if (str.equals("")) str = "-1"; 
/*  89 */     return (new Integer(str)).intValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public long getLongValue(String paramString1, String paramString2) {
/* 100 */     String str = getValue(paramString1, paramString2);
/* 101 */     if (str.equals("")) str = "-1"; 
/* 102 */     return (new Long(str)).longValue();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getValue(String paramString1, String paramString2) {
/* 113 */     for (byte b = 0; b < this.INIFILE.size(); b++) {
/* 114 */       oberkey oberkey = this.INIFILE.elementAt(b);
/* 115 */       if (oberkey.key.equals(paramString1)) {
/* 116 */         for (byte b1 = 0; b1 < oberkey.UB.size(); b1++) {
/* 117 */           unterkey unterkey = oberkey.UB.elementAt(b1);
/* 118 */           if (unterkey.key.equals(paramString2)) {
/* 119 */             return unterkey.value;
/*     */           }
/*     */         } 
/* 122 */         return "";
/*     */       } 
/*     */     } 
/* 125 */     return "";
/*     */   }
/*     */   
/*     */   String hinter(String paramString1, String paramString2) {
/* 129 */     if (paramString1.indexOf(paramString2) != -1)
/* 130 */       return paramString1.substring(paramString1.indexOf(paramString2) + paramString2.length()); 
/* 131 */     return "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean loadIni() {
/* 141 */     this.INIFILE = new Vector();
/* 142 */     String str = "";
/*     */ 
/*     */ 
/*     */     
/* 146 */     try { BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(this.inifile)));
/*     */       while (true) {
/* 148 */         str = bufferedReader.readLine();
/* 149 */         if (str != null) {
/* 150 */           str = str.trim();
/* 151 */           if (!str.equals("") && !str.startsWith(";"))
/*     */           {
/* 153 */             if (str.startsWith("[")) {
/* 154 */               str = hinter(str, "[");
/* 155 */               str = bis(str, "]");
/* 156 */               oberkey oberkey = new oberkey(this, str);
/* 157 */               this.INIFILE.addElement(oberkey);
/*     */             } else {
/* 159 */               String str1 = bis(str, "=");
/* 160 */               String str2 = hinter(str, "=");
/* 161 */               unterkey unterkey = new unterkey(this, str1, str2);
/* 162 */               ((oberkey)this.INIFILE.lastElement()).UB.addElement(unterkey);
/*     */             } 
/*     */           }
/*     */         } 
/* 166 */         if (str == null) {
/* 167 */           bufferedReader.close();
/* 168 */           return true;
/*     */         } 
/*     */       }  }
/* 171 */     catch (Exception exception) { return false; }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean loadIni(String paramString) {
/* 182 */     this.inifile = paramString;
/* 183 */     return loadIni();
/*     */   }
/*     */ 
/*     */   
/*     */   public void saveIni() {
/*     */     try {
/* 189 */       PrintStream printStream = new PrintStream(new FileOutputStream(this.inifile));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 197 */       for (byte b = 0; b < this.INIFILE.size(); b++) {
/* 198 */         oberkey oberkey = this.INIFILE.elementAt(b);
/* 199 */         printStream.print("[" + oberkey.key + "]" + '\r' + '\n');
/* 200 */         for (byte b1 = 0; b1 < oberkey.UB.size(); b1++) {
/* 201 */           unterkey unterkey = oberkey.UB.elementAt(b1);
/* 202 */           printStream.print(unterkey.key + "=" + unterkey.value + '\r' + '\n');
/*     */         } 
/* 204 */         printStream.print("\r\n");
/*     */       } 
/* 206 */       printStream.close();
/* 207 */     } catch (IOException iOException) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void saveIni(String paramString) {
/* 217 */     this.inifile = paramString;
/* 218 */     saveIni();
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
/*     */   public void setValue(String paramString1, String paramString2, boolean paramBoolean) {
/* 230 */     String str = "FALSE";
/* 231 */     if (paramBoolean) str = "TRUE"; 
/* 232 */     setValue(paramString1, paramString2, str);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(String paramString1, String paramString2, int paramInt) {
/* 243 */     setValue(paramString1, paramString2, "" + paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValue(String paramString1, String paramString2, long paramLong) {
/* 254 */     setValue(paramString1, paramString2, "" + paramLong);
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
/*     */   public void setValue(String paramString1, String paramString2, String paramString3) {
/* 266 */     for (byte b = 0; b < this.INIFILE.size(); b++) {
/* 267 */       oberkey oberkey1 = this.INIFILE.elementAt(b);
/* 268 */       if (oberkey1.key.equals(paramString1)) {
/* 269 */         for (byte b1 = 0; b1 < oberkey1.UB.size(); b1++) {
/* 270 */           unterkey unterkey2 = oberkey1.UB.elementAt(b1);
/* 271 */           if (unterkey2.key.equals(paramString2)) {
/* 272 */             unterkey2.value = paramString3;
/*     */             return;
/*     */           } 
/*     */         } 
/* 276 */         unterkey unterkey1 = new unterkey(this, paramString2, paramString3);
/* 277 */         oberkey1.UB.addElement(unterkey1);
/*     */         return;
/*     */       } 
/*     */     } 
/* 281 */     oberkey oberkey = new oberkey(this, paramString1);
/* 282 */     unterkey unterkey = new unterkey(this, paramString2, paramString3);
/* 283 */     oberkey.UB.addElement(unterkey);
/* 284 */     this.INIFILE.addElement(oberkey);
/*     */   }
/*     */   
/*     */   class unterkey { String key;
/*     */     
/*     */     public unterkey(IniFiles this$0, String param1String1, String param1String2) {
/* 290 */       this.this$0 = this$0; this.key = ""; this.value = "";
/* 291 */       this.key = param1String1;
/* 292 */       this.value = param1String2;
/*     */     }
/*     */     String value; private final IniFiles this$0; }
/*     */   class oberkey { String key;
/*     */     Vector UB;
/*     */     private final IniFiles this$0;
/*     */     
/*     */     public oberkey(IniFiles this$0, String param1String) {
/* 300 */       this.this$0 = this$0; this.key = ""; this.UB = new Vector();
/* 301 */       this.key = param1String;
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) {
/* 311 */       IniFiles iniFiles = new IniFiles("C:\\WINDOWS\\SUSTATS.INI");
/* 312 */       iniFiles.loadIni();
/* 313 */       System.out.println(iniFiles.getValue("HELP", "Browser"));
/* 314 */       System.out.println(iniFiles.getValue("HELP", "SUSPath"));
/* 315 */       iniFiles.setValue("TEST", "Score", 42);
/* 316 */       iniFiles.saveIni();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/util/IniFiles.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */