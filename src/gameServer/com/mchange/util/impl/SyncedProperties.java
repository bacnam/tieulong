/*     */ package com.mchange.util.impl;
/*     */ 
/*     */ import com.mchange.io.InputStreamUtils;
/*     */ import com.mchange.io.OutputStreamUtils;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.Enumeration;
/*     */ import java.util.LinkedList;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SyncedProperties
/*     */ {
/*  45 */   private static final String[] SA_TEMPLATE = new String[0];
/*     */   
/*     */   private static final byte H_START_BYTE = 35;
/*     */   private static final byte[] H_LF_BYTES;
/*     */   private static final String ASCII = "8859_1";
/*     */   
/*     */   static {
/*     */     try {
/*  53 */       H_LF_BYTES = System.getProperty("line.separator", "\r\n").getBytes("8859_1");
/*  54 */     } catch (UnsupportedEncodingException unsupportedEncodingException) {
/*  55 */       throw new InternalError("Encoding 8859_1 not supported ?!?");
/*     */     } 
/*     */   }
/*     */   Properties props;
/*     */   byte[] headerBytes;
/*     */   File file;
/*  61 */   long last_mod = -1L;
/*     */   
/*     */   public SyncedProperties(File paramFile, String paramString) throws IOException {
/*  64 */     this(paramFile, makeHeaderBytes(paramString));
/*     */   }
/*     */   public SyncedProperties(File paramFile, String[] paramArrayOfString) throws IOException {
/*  67 */     this(paramFile, makeHeaderBytes(paramArrayOfString));
/*     */   }
/*     */   public SyncedProperties(File paramFile) throws IOException {
/*  70 */     this(paramFile, (byte[])null);
/*     */   }
/*     */   
/*     */   private SyncedProperties(File paramFile, byte[] paramArrayOfbyte) throws IOException {
/*  74 */     if (paramFile.exists()) {
/*     */       
/*  76 */       if (!paramFile.isFile())
/*  77 */         throw new IOException(paramFile.getPath() + ": Properties file can't be a directory or special file!"); 
/*  78 */       if (paramArrayOfbyte == null) {
/*     */ 
/*     */ 
/*     */         
/*  82 */         BufferedReader bufferedReader = null;
/*     */         
/*     */         try {
/*  85 */           bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(paramFile)));
/*     */           
/*  87 */           LinkedList<String> linkedList = new LinkedList();
/*  88 */           String str = bufferedReader.readLine();
/*  89 */           while (str.trim().equals(""))
/*  90 */             str = bufferedReader.readLine(); 
/*  91 */           while (str.charAt(0) == '#')
/*  92 */             linkedList.add(str.substring(1).trim()); 
/*  93 */           paramArrayOfbyte = makeHeaderBytes(linkedList.<String>toArray(SA_TEMPLATE));
/*     */         } finally {
/*     */           
/*  96 */           if (bufferedReader != null) bufferedReader.close();
/*     */         
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 113 */     if (!paramFile.canWrite())
/* 114 */       throw new IOException("Can't write to file " + paramFile.getPath()); 
/* 115 */     this.props = new Properties();
/* 116 */     this.headerBytes = paramArrayOfbyte;
/* 117 */     this.file = paramFile;
/* 118 */     ensureUpToDate();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized String getProperty(String paramString) throws IOException {
/* 123 */     ensureUpToDate();
/* 124 */     return this.props.getProperty(paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized String getProperty(String paramString1, String paramString2) throws IOException {
/* 129 */     String str = this.props.getProperty(paramString1);
/* 130 */     return (str == null) ? paramString2 : str;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void put(String paramString1, String paramString2) throws IOException {
/* 135 */     ensureUpToDate();
/* 136 */     this.props.put(paramString1, paramString2);
/* 137 */     rewritePropsFile();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void remove(String paramString) throws IOException {
/* 142 */     ensureUpToDate();
/* 143 */     this.props.remove(paramString);
/* 144 */     rewritePropsFile();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void clear() throws IOException {
/* 149 */     ensureUpToDate();
/* 150 */     this.props.clear();
/* 151 */     rewritePropsFile();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean contains(String paramString) throws IOException {
/* 156 */     ensureUpToDate();
/* 157 */     return this.props.contains(paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean containsKey(String paramString) throws IOException {
/* 162 */     ensureUpToDate();
/* 163 */     return this.props.containsKey(paramString);
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized Enumeration elements() throws IOException {
/* 168 */     ensureUpToDate();
/* 169 */     return this.props.elements();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized Enumeration keys() throws IOException {
/* 174 */     ensureUpToDate();
/* 175 */     return this.props.keys();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized int size() throws IOException {
/* 180 */     ensureUpToDate();
/* 181 */     return this.props.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized boolean isEmpty() throws IOException {
/* 186 */     ensureUpToDate();
/* 187 */     return this.props.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   private synchronized void ensureUpToDate() throws IOException {
/* 192 */     long l = this.file.lastModified();
/* 193 */     if (l > this.last_mod) {
/*     */       
/* 195 */       BufferedInputStream bufferedInputStream = null;
/*     */       
/*     */       try {
/* 198 */         bufferedInputStream = new BufferedInputStream(new FileInputStream(this.file));
/* 199 */         this.props.clear();
/* 200 */         this.props.load(bufferedInputStream);
/* 201 */         this.last_mod = l;
/*     */       } finally {
/*     */         
/* 204 */         InputStreamUtils.attemptClose(bufferedInputStream);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private synchronized void rewritePropsFile() throws IOException {
/* 210 */     BufferedOutputStream bufferedOutputStream = null;
/*     */     
/*     */     try {
/* 213 */       bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(this.file));
/* 214 */       if (this.headerBytes != null) bufferedOutputStream.write(this.headerBytes); 
/* 215 */       this.props.store(bufferedOutputStream, (String)null);
/* 216 */       bufferedOutputStream.flush();
/* 217 */       this.last_mod = this.file.lastModified();
/*     */     } finally {
/*     */       
/* 220 */       OutputStreamUtils.attemptClose(bufferedOutputStream);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static byte[] makeHeaderBytes(String[] paramArrayOfString) {
/*     */     try {
/* 227 */       ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); byte b; int i;
/* 228 */       for (b = 0, i = paramArrayOfString.length; b < i; b++) {
/*     */         
/* 230 */         byteArrayOutputStream.write(35);
/* 231 */         byteArrayOutputStream.write(paramArrayOfString[b].getBytes());
/* 232 */         byteArrayOutputStream.write(H_LF_BYTES);
/*     */       } 
/* 234 */       return byteArrayOutputStream.toByteArray();
/*     */     }
/* 236 */     catch (IOException iOException) {
/* 237 */       throw new InternalError("IOException working with ByteArrayOutputStream?!?");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static byte[] makeHeaderBytes(String paramString) {
/*     */     try {
/* 244 */       ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
/* 245 */       byteArrayOutputStream.write(35);
/* 246 */       byteArrayOutputStream.write(paramString.getBytes());
/* 247 */       byteArrayOutputStream.write(H_LF_BYTES);
/* 248 */       return byteArrayOutputStream.toByteArray();
/*     */     }
/* 250 */     catch (IOException iOException) {
/* 251 */       throw new InternalError("IOException working with ByteArrayOutputStream?!?");
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/util/impl/SyncedProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */