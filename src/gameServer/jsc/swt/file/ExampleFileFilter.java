/*     */ package jsc.swt.file;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import javax.swing.filechooser.FileFilter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExampleFileFilter
/*     */   extends FileFilter
/*     */ {
/*  62 */   private static String TYPE_UNKNOWN = "Type Unknown";
/*  63 */   private static String HIDDEN_FILE = "Hidden File";
/*     */   
/*  65 */   private Hashtable filters = null;
/*  66 */   private String description = null;
/*  67 */   private String fullDescription = null;
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean useExtensionsInDescription = true;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExampleFileFilter() {
/*  77 */     this.filters = new Hashtable();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ExampleFileFilter(String paramString) {
/*  87 */     this(paramString, (String)null);
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
/*     */   public ExampleFileFilter(String paramString1, String paramString2) {
/* 100 */     this();
/* 101 */     if (paramString1 != null) addExtension(paramString1); 
/* 102 */     if (paramString2 != null) setDescription(paramString2);
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
/*     */   public ExampleFileFilter(String[] paramArrayOfString) {
/* 115 */     this(paramArrayOfString, (String)null);
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
/*     */   public ExampleFileFilter(String[] paramArrayOfString, String paramString) {
/* 127 */     this();
/* 128 */     for (byte b = 0; b < paramArrayOfString.length; b++)
/*     */     {
/* 130 */       addExtension(paramArrayOfString[b]);
/*     */     }
/* 132 */     if (paramString != null) setDescription(paramString);
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
/*     */   public boolean accept(File paramFile) {
/* 145 */     if (paramFile != null) {
/* 146 */       if (paramFile.isDirectory()) {
/* 147 */         return true;
/*     */       }
/* 149 */       String str = getExtension(paramFile);
/* 150 */       if (str != null && this.filters.get(getExtension(paramFile)) != null) {
/* 151 */         return true;
/*     */       }
/*     */     } 
/* 154 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getExtension(File paramFile) {
/* 164 */     if (paramFile != null) {
/* 165 */       String str = paramFile.getName();
/* 166 */       int i = str.lastIndexOf('.');
/* 167 */       if (i > 0 && i < str.length() - 1) {
/* 168 */         return str.substring(i + 1).toLowerCase();
/*     */       }
/*     */     } 
/* 171 */     return null;
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
/*     */   public void addExtension(String paramString) {
/* 187 */     if (this.filters == null) {
/* 188 */       this.filters = new Hashtable(5);
/*     */     }
/* 190 */     this.filters.put(paramString.toLowerCase(), this);
/* 191 */     this.fullDescription = null;
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
/*     */   public String getDescription() {
/* 205 */     if (this.fullDescription == null) {
/* 206 */       if (this.description == null || isExtensionListInDescription()) {
/* 207 */         this.fullDescription = (this.description == null) ? "(" : (this.description + " (");
/*     */         
/* 209 */         Enumeration enumeration = this.filters.keys();
/* 210 */         if (enumeration != null) {
/* 211 */           this.fullDescription += "." + (String)enumeration.nextElement();
/* 212 */           while (enumeration.hasMoreElements()) {
/* 213 */             this.fullDescription += ", " + (String)enumeration.nextElement();
/*     */           }
/*     */         } 
/* 216 */         this.fullDescription += ")";
/*     */       } else {
/* 218 */         this.fullDescription = this.description;
/*     */       } 
/*     */     }
/* 221 */     return this.fullDescription;
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
/*     */   public void setDescription(String paramString) {
/* 233 */     this.description = paramString;
/* 234 */     this.fullDescription = null;
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
/*     */   public void setExtensionListInDescription(boolean paramBoolean) {
/* 249 */     this.useExtensionsInDescription = paramBoolean;
/* 250 */     this.fullDescription = null;
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
/*     */   public boolean isExtensionListInDescription() {
/* 265 */     return this.useExtensionsInDescription;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/file/ExampleFileFilter.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */