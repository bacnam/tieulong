/*     */ package com.zhonglian.server.common.utils;
/*     */ import BaseCommon.CommLog;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.Closeable;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.RandomAccessFile;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ 
/*     */ public class CommFile {
/*     */   public static void close(Closeable... closeables) {
/*     */     byte b;
/*     */     int i;
/*     */     Closeable[] arrayOfCloseable;
/*  31 */     for (i = (arrayOfCloseable = closeables).length, b = 0; b < i; ) { Closeable c = arrayOfCloseable[b];
/*     */       try {
/*  33 */         if (c != null) {
/*  34 */           c.close();
/*     */         }
/*  36 */       } catch (IOException e) {
/*  37 */         CommLog.error(NetUtil.class.getName(), e);
/*     */       } 
/*     */       b++; }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String bufferedReader(String path) throws IOException {
/*  50 */     File file = new File(path);
/*  51 */     if (!file.exists() || file.isDirectory()) {
/*  52 */       throw new FileNotFoundException("file not exist:" + path);
/*     */     }
/*     */     
/*  55 */     BufferedReader br = new BufferedReader(new FileReader(file));
/*  56 */     StringBuilder sb = new StringBuilder();
/*     */     
/*  58 */     String line = null;
/*  59 */     while ((line = br.readLine()) != null) {
/*  60 */       sb.append(line).append("\n");
/*     */     }
/*  62 */     br.close();
/*  63 */     return sb.toString();
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
/*     */   public static String BufferedReaderEncode(String path, String code) throws IOException {
/*  76 */     File file = new File(path);
/*  77 */     if (!file.exists() || file.isDirectory()) {
/*  78 */       throw new FileNotFoundException();
/*     */     }
/*     */     
/*  81 */     FileInputStream fr = new FileInputStream(path);
/*  82 */     BufferedReader br = new BufferedReader(new InputStreamReader(fr, code));
/*     */     
/*  84 */     StringBuilder sb = new StringBuilder();
/*  85 */     String temp = br.readLine();
/*     */     
/*  87 */     while (temp != null) {
/*  88 */       sb.append(temp).append(" ");
/*  89 */       temp = br.readLine();
/*     */     } 
/*  91 */     return sb.toString();
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
/*     */   public static Map<String, Map<String, String>> GetTable(String path, int fieldLine, int contentLine) {
/* 105 */     Map<String, Map<String, String>> table = new HashMap<>();
/*     */     
/* 107 */     File file = new File(path);
/* 108 */     if (!file.exists() || file.isDirectory() || fieldLine >= contentLine || fieldLine <= 0 || contentLine <= 0) {
/* 109 */       CommLog.error("file not exist:{} ", path);
/* 110 */       return table;
/*     */     } 
/*     */     
/* 113 */     Charset cs = Charset.forName("utf-8");
/* 114 */     BufferedReader br = null;
/*     */     try {
/* 116 */       FileInputStream fr = new FileInputStream(file);
/* 117 */       br = new BufferedReader(new InputStreamReader(fr, cs));
/* 118 */     } catch (FileNotFoundException e) {
/* 119 */       CommLog.error(null, e);
/*     */     } 
/* 121 */     if (br == null) {
/* 122 */       CommLog.error("Refdata readfailed:{} ", path);
/* 123 */       return table;
/*     */     } 
/*     */     
/* 126 */     String temp = null;
/*     */     
/* 128 */     String[] fields = null;
/* 129 */     int curLine = 0;
/*     */     do {
/*     */       try {
/* 132 */         temp = br.readLine();
/* 133 */         if (temp == null) {
/*     */           break;
/*     */         }
/*     */         
/* 137 */         temp = temp.trim();
/* 138 */         curLine++;
/*     */         
/* 140 */         if (!temp.equals(""))
/*     */         {
/*     */ 
/*     */           
/* 144 */           if (fieldLine == curLine) {
/*     */             
/* 146 */             String[] values = temp.split("\t");
/* 147 */             if (values.length <= 0) {
/* 148 */               close(new Closeable[] { br });
/* 149 */               CommLog.error("ParseTable fieldCnt <= 0, path:{}", path);
/* 150 */               return table;
/*     */             } 
/*     */             
/* 153 */             fields = new String[values.length];
/* 154 */             for (int index = 0; index < values.length; index++) {
/* 155 */               fields[index] = values[index].trim();
/*     */             }
/* 157 */           } else if (curLine >= contentLine) {
/* 158 */             if (fields == null) {
/* 159 */               close(new Closeable[] { br });
/* 160 */               CommLog.error("ParseTable fieldCnt null(fieldLine: {}), path:{}", Integer.valueOf(fieldLine), path);
/* 161 */               return table;
/*     */             } 
/*     */ 
/*     */             
/* 165 */             String[] values = temp.split("\t");
/* 166 */             if (fields.length < values.length)
/* 167 */             { CommLog.error("解析表失败 [{}]头部字段数({})<值字段数({}) 无法解析, content:{}, path:{}", new Object[] { file.getName(), Integer.valueOf(fields.length), Integer.valueOf(values.length), temp });
/*     */                }
/*     */             
/*     */             else
/*     */             
/* 172 */             { Map<String, String> lineValue = new ConcurrentHashMap<>();
/* 173 */               for (int index = 0; index < fields.length; index++) {
/* 174 */                 if (values.length > index) {
/* 175 */                   lineValue.put(fields[index], values[index].trim());
/*     */                 } else {
/* 177 */                   lineValue.put(fields[index], "");
/*     */                 } 
/*     */               } 
/*     */ 
/*     */               
/* 182 */               String key = values[0];
/* 183 */               if (table.containsKey(key)) {
/* 184 */                 CommLog.error("键值重复: {}, at line:{}, path:{}", new Object[] { key, Integer.valueOf(curLine), path });
/*     */               }
/* 186 */               table.put(key, lineValue); } 
/*     */           }  } 
/* 188 */       } catch (IOException e) {
/* 189 */         CommLog.error(null, e);
/*     */       } 
/* 191 */     } while (temp != null);
/*     */     
/* 193 */     close(new Closeable[] { br });
/*     */     
/* 195 */     return table;
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
/*     */   public static List<Map<String, String>> GetRowSet(String path, int fieldLine, int contentLine) {
/* 209 */     List<Map<String, String>> lines = new ArrayList<>();
/* 210 */     Map<String, Map<String, String>> table = new ConcurrentHashMap<>();
/*     */     
/* 212 */     File file = new File(path);
/* 213 */     if (!file.exists() || file.isDirectory() || fieldLine >= contentLine || fieldLine <= 0 || contentLine <= 0) {
/* 214 */       return lines;
/*     */     }
/*     */     
/* 217 */     Charset cs = Charset.forName("utf-8");
/* 218 */     BufferedReader br = null;
/*     */     try {
/* 220 */       FileInputStream fr = new FileInputStream(file);
/* 221 */       br = new BufferedReader(new InputStreamReader(fr, cs));
/* 222 */     } catch (FileNotFoundException e) {
/* 223 */       CommLog.error(null, e);
/*     */     } 
/* 225 */     if (br == null) {
/* 226 */       return lines;
/*     */     }
/*     */     
/* 229 */     String temp = null;
/*     */     
/* 231 */     String[] fields = null;
/* 232 */     int curLine = 0;
/*     */     do {
/*     */       try {
/* 235 */         temp = br.readLine();
/* 236 */         if (temp == null) {
/*     */           break;
/*     */         }
/*     */         
/* 240 */         temp = temp.trim();
/* 241 */         curLine++;
/*     */         
/* 243 */         if (!temp.equals(""))
/*     */         {
/*     */ 
/*     */           
/* 247 */           if (fieldLine == curLine) {
/*     */             
/* 249 */             String[] values = temp.split("\t");
/* 250 */             if (values.length <= 0) {
/* 251 */               close(new Closeable[] { br });
/* 252 */               CommLog.error("ParseTable fieldCnt <= 0, path:{}", path);
/* 253 */               return lines;
/*     */             } 
/*     */             
/* 256 */             fields = new String[values.length];
/* 257 */             for (int index = 0; index < values.length; index++) {
/* 258 */               fields[index] = values[index].trim();
/*     */             }
/* 260 */           } else if (curLine >= contentLine) {
/* 261 */             if (fields == null) {
/* 262 */               close(new Closeable[] { br });
/* 263 */               CommLog.error("ParseTable fieldCnt null(fieldLine: {}), path:{}", Integer.valueOf(fieldLine), path);
/* 264 */               return lines;
/*     */             } 
/*     */ 
/*     */             
/* 268 */             String[] values = temp.split("\t");
/* 269 */             if (fields.length < values.length)
/* 270 */             { CommLog.error("解析表失败 代码字段({}) < 配表字段({}), content:{}, path:{}", new Object[] { Integer.valueOf(fields.length), Integer.valueOf(values.length), temp, path });
/*     */                }
/*     */             
/*     */             else
/*     */             
/* 275 */             { Map<String, String> lineValue = new ConcurrentHashMap<>();
/* 276 */               for (int index = 0; index < fields.length; index++) {
/* 277 */                 if (values.length > index) {
/* 278 */                   lineValue.put(fields[index], values[index].trim());
/*     */                 } else {
/* 280 */                   lineValue.put(fields[index], "");
/*     */                 } 
/*     */               } 
/*     */ 
/*     */               
/* 285 */               String key = values[0];
/* 286 */               if (table.containsKey(key)) {
/* 287 */                 CommLog.error("键值重复: {}, at line:{}, path:{}", new Object[] { key, Integer.valueOf(curLine), path });
/*     */               }
/* 289 */               table.put(key, lineValue);
/* 290 */               lines.add(lineValue); } 
/*     */           }  } 
/* 292 */       } catch (IOException e) {
/* 293 */         CommLog.error(null, e);
/*     */       } 
/* 295 */     } while (temp != null);
/*     */     
/* 297 */     close(new Closeable[] { br });
/*     */     
/* 299 */     return lines;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void getFileListByPath(String dirPath, List<File> fileList, String postfix) {
/* 310 */     File dir = new File(dirPath);
/* 311 */     File[] files = dir.listFiles();
/*     */     
/* 313 */     if (files == null || files.length == 0) {
/*     */       return;
/*     */     }
/*     */     
/* 317 */     List<File> subDirFileList = new ArrayList<>();
/* 318 */     List<File> curDirFileList = new ArrayList<>(); byte b; int i;
/*     */     File[] arrayOfFile1;
/* 320 */     for (i = (arrayOfFile1 = files).length, b = 0; b < i; ) { File file = arrayOfFile1[b];
/* 321 */       if (file.isDirectory()) {
/* 322 */         getFileListByPath(file.getAbsolutePath(), subDirFileList, postfix);
/*     */       }
/* 324 */       else if (postfix.isEmpty() || file.getName().toLowerCase().endsWith("." + postfix.trim())) {
/*     */ 
/*     */ 
/*     */         
/* 328 */         curDirFileList.add(file);
/*     */       } 
/*     */       
/*     */       b++; }
/*     */     
/* 333 */     fileList.addAll(subDirFileList);
/* 334 */     fileList.addAll(curDirFileList);
/*     */   }
/*     */   
/*     */   public static void getLinesFromFile(List<String> methodList, String strFilePath) {
/* 338 */     File file = null;
/* 339 */     BufferedReader br = null;
/* 340 */     String strLine = null;
/*     */     try {
/* 342 */       file = new File(strFilePath);
/* 343 */       br = new BufferedReader(new FileReader(file));
/* 344 */       while ((strLine = br.readLine()) != null) {
/* 345 */         if (!strLine.trim().isEmpty()) {
/* 346 */           methodList.add(strLine.trim());
/*     */         }
/*     */       } 
/* 349 */     } catch (FileNotFoundException e) {
/* 350 */       CommLog.error(null, e);
/* 351 */     } catch (IOException e) {
/* 352 */       CommLog.error(null, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static CharBuffer getFileData(String filePath) {
/* 363 */     CharBuffer data = null;
/*     */     
/* 365 */     File file = new File(filePath);
/* 366 */     if (!file.exists())
/* 367 */       return null; 
/* 368 */     FileInputStream fin = null;
/* 369 */     InputStreamReader isReader = null;
/* 370 */     BufferedReader bufReader = null;
/*     */     
/*     */     try {
/* 373 */       fin = new FileInputStream(file);
/* 374 */       isReader = new InputStreamReader(fin, "UTF-8");
/* 375 */       bufReader = new BufferedReader(isReader);
/*     */       
/* 377 */       data = CharBuffer.allocate((int)file.length());
/* 378 */       int count = bufReader.read(data);
/* 379 */       data.limit(count);
/* 380 */     } catch (Exception e) {
/* 381 */       return null;
/*     */     } finally {
/* 383 */       if (fin != null) {
/*     */         try {
/* 385 */           fin.close();
/* 386 */         } catch (IOException iOException) {}
/*     */       }
/*     */       
/* 389 */       if (isReader != null) {
/*     */         try {
/* 391 */           isReader.close();
/* 392 */         } catch (IOException iOException) {}
/*     */       }
/*     */       
/* 395 */       if (bufReader != null) {
/*     */         try {
/* 397 */           bufReader.close();
/* 398 */         } catch (IOException iOException) {}
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 403 */     return data;
/*     */   }
/*     */   
/*     */   public static String getTextFromFile(String strFilePath) {
/* 407 */     CharBuffer buf = getFileData(strFilePath);
/* 408 */     if (buf != null) {
/* 409 */       String res = new String(buf.array());
/* 410 */       return res.trim();
/*     */     } 
/* 412 */     return null;
/*     */   }
/*     */   
/*     */   public static Set<String> getLineSetsFromFile(String strFilePath) {
/* 416 */     Set<String> rowsets = new HashSet<>();
/* 417 */     List<String> lines = getLinesFromFile(strFilePath);
/* 418 */     for (String line : lines) {
/* 419 */       rowsets.add(line);
/*     */     }
/*     */     
/* 422 */     return rowsets;
/*     */   }
/*     */   
/*     */   public static List<String> getLinesFromFile(String strFilePath) {
/* 426 */     File file = null;
/* 427 */     BufferedReader br = null;
/* 428 */     String strLine = null;
/* 429 */     List<String> rowsets = new LinkedList<>();
/*     */     try {
/* 431 */       file = new File(strFilePath);
/* 432 */       br = new BufferedReader(new FileReader(file));
/* 433 */       while ((strLine = br.readLine()) != null) {
/* 434 */         if (!strLine.trim().isEmpty()) {
/* 435 */           rowsets.add(strLine.trim());
/*     */         }
/*     */       } 
/* 438 */     } catch (FileNotFoundException e) {
/* 439 */       CommLog.error("getLinesFromFile", e);
/* 440 */     } catch (IOException e) {
/* 441 */       CommLog.error("getLinesFromFile", e);
/*     */     } finally {
/* 443 */       if (br != null) {
/*     */         try {
/* 445 */           br.close();
/*     */         }
/* 447 */         catch (Exception e) {
/* 448 */           CommLog.error("getLinesFromFile", e);
/*     */         } 
/*     */       }
/*     */     } 
/* 452 */     return rowsets;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void Copy(String from, String to) throws IOException {
/* 463 */     RandomAccessFile fromFile = null;
/* 464 */     RandomAccessFile toFile = null;
/*     */     
/*     */     try {
/* 467 */       String currentDir = System.getProperty("user.dir");
/* 468 */       CommLog.info("Current dir using System:" + currentDir);
/*     */       
/* 470 */       fromFile = new RandomAccessFile(from, "r");
/* 471 */       FileChannel fromChannel = fromFile.getChannel();
/*     */       
/* 473 */       toFile = new RandomAccessFile(to, "rw");
/* 474 */       FileChannel toChannel = toFile.getChannel();
/*     */       
/* 476 */       long size = fromChannel.size();
/*     */       
/* 478 */       fromChannel.transferTo(0L, size, toChannel);
/*     */     } finally {
/*     */       
/* 481 */       if (fromFile != null) {
/* 482 */         fromFile.close();
/*     */       }
/* 484 */       if (toFile != null) {
/* 485 */         toFile.close();
/*     */       }
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
/*     */   public static void Write(String path, byte[] content) throws IOException {
/* 498 */     Write(path, new String(content));
/*     */   }
/*     */   
/*     */   public static void Write(String path, String content) throws IOException {
/* 502 */     File f = new File(path);
/*     */     
/* 504 */     FileOutputStream fos = new FileOutputStream(f);
/* 505 */     OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
/* 506 */     osw.write(content);
/* 507 */     osw.flush();
/* 508 */     osw.close();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/utils/CommFile.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */