/*     */ package jsc.swt.datatable;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.StreamTokenizer;
/*     */ import java.text.DecimalFormat;
/*     */ import java.text.NumberFormat;
/*     */ import java.text.ParsePosition;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.Locale;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DataFile
/*     */ {
/*     */   public static final String EOL = "\r\n";
/*     */   static final char FDD = '\t';
/*     */   public static final char FSDC = '"';
/*     */   public static final String FSDS = "\"";
/*     */   
/*     */   static String classToString(Class paramClass) {
/*  76 */     if (paramClass == Double.class) return "Double"; 
/*  77 */     if (paramClass == Integer.class) return "Integer"; 
/*  78 */     return "String";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static Class stringToClass(String paramString) {
/*  84 */     if (paramString.equals("Double")) return Double.class; 
/*  85 */     if (paramString.equals("Integer")) return Integer.class; 
/*  86 */     return String.class;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DecimalFormat getIntegerFormat() {
/*  97 */     NumberFormat numberFormat = NumberFormat.getInstance(Locale.UK);
/*  98 */     DecimalFormat decimalFormat = (DecimalFormat)numberFormat;
/*  99 */     decimalFormat.applyPattern("##########");
/* 100 */     return decimalFormat;
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
/*     */   public static DecimalFormat getRealFormat() {
/* 113 */     NumberFormat numberFormat = NumberFormat.getInstance(Locale.UK);
/* 114 */     DecimalFormat decimalFormat = (DecimalFormat)numberFormat;
/* 115 */     decimalFormat.applyPattern("0.###############E0");
/* 116 */     return decimalFormat;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static DataMatrix read(File paramFile) {
/* 161 */     FileReader fileReader = null;
/*     */     
/*     */     try { int i, j;
/*     */       String str1, str2;
/* 165 */       fileReader = new FileReader(paramFile);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 178 */       BufferedReader bufferedReader = new BufferedReader(fileReader);
/* 179 */       StreamTokenizer streamTokenizer = new StreamTokenizer(bufferedReader);
/* 180 */       streamTokenizer.eolIsSignificant(false);
/* 181 */       streamTokenizer.quoteChar(34);
/* 182 */       streamTokenizer.parseNumbers();
/* 183 */       streamTokenizer.slashStarComments(true);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 192 */       if (streamTokenizer.nextToken() == 34)
/* 193 */       { str1 = streamTokenizer.sval; }
/* 194 */       else { return null; }
/* 195 */        if (streamTokenizer.nextToken() == 34)
/* 196 */       { str2 = streamTokenizer.sval; }
/* 197 */       else { return null; }
/*     */ 
/*     */       
/* 200 */       if (streamTokenizer.nextToken() == -2)
/* 201 */       { i = (int)streamTokenizer.nval; }
/* 202 */       else { return null; }
/*     */       
/* 204 */       if (streamTokenizer.nextToken() == -2)
/* 205 */       { j = (int)streamTokenizer.nval; }
/* 206 */       else { return null; }
/*     */ 
/*     */       
/* 209 */       DataMatrix dataMatrix = new DataMatrix(i, j, str1, stringToClass(str2));
/*     */ 
/*     */       
/* 212 */       DecimalFormat decimalFormat1 = getRealFormat();
/* 213 */       DecimalFormat decimalFormat2 = getIntegerFormat();
/*     */ 
/*     */ 
/*     */       
/* 217 */       ParsePosition parsePosition = new ParsePosition(0);
/* 218 */       for (byte b = 0; b < j; b++) {
/*     */         String str;
/*     */         
/* 221 */         if (streamTokenizer.nextToken() == 34)
/* 222 */         { dataMatrix.setColumnName(b, streamTokenizer.sval); }
/* 223 */         else { return null; }
/*     */         
/* 225 */         if (streamTokenizer.nextToken() == 34)
/* 226 */         { str = streamTokenizer.sval; }
/* 227 */         else { return null; }
/*     */ 
/*     */         
/* 230 */         if (str.equals("Double")) {
/*     */           
/* 232 */           dataMatrix.setColumnClass(b, Double.class); while (true) {
/*     */             int k;
/* 234 */             if (streamTokenizer.nextToken() == -2)
/* 235 */             { k = (int)streamTokenizer.nval; }
/* 236 */             else { streamTokenizer.pushBack(); break; }
/*     */             
/* 238 */             if (streamTokenizer.nextToken() == 34) {
/*     */               
/* 240 */               parsePosition.setIndex(0);
/* 241 */               Number number = decimalFormat1.parse(streamTokenizer.sval, parsePosition);
/* 242 */               if (number == null) return null; 
/* 243 */               dataMatrix.setValueAt(new Double(number.doubleValue()), k, b); continue;
/*     */             } 
/* 245 */             return null;
/*     */           }
/*     */         
/*     */         }
/* 249 */         else if (str.equals("Integer")) {
/*     */           
/* 251 */           dataMatrix.setColumnClass(b, Integer.class); while (true) {
/*     */             int k;
/* 253 */             if (streamTokenizer.nextToken() == -2)
/* 254 */             { k = (int)streamTokenizer.nval; }
/* 255 */             else { streamTokenizer.pushBack();
/*     */               
/*     */               break; }
/*     */             
/* 259 */             if (streamTokenizer.nextToken() == 34) {
/*     */               
/* 261 */               parsePosition.setIndex(0);
/* 262 */               Number number = decimalFormat2.parse(streamTokenizer.sval, parsePosition);
/* 263 */               if (number == null) return null; 
/* 264 */               dataMatrix.setValueAt(new Integer(number.intValue()), k, b); continue;
/*     */             } 
/* 266 */             return null;
/*     */           }
/*     */         
/*     */         }
/*     */         else {
/*     */           
/* 272 */           dataMatrix.setColumnClass(b, String.class); while (true) {
/*     */             int k;
/* 274 */             if (streamTokenizer.nextToken() == -2)
/* 275 */             { k = (int)streamTokenizer.nval; }
/* 276 */             else { streamTokenizer.pushBack(); break; }
/*     */             
/* 278 */             if (streamTokenizer.nextToken() == 34) {
/* 279 */               dataMatrix.setValueAt(streamTokenizer.sval, k, b); continue;
/* 280 */             }  return null;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 285 */       return dataMatrix; }
/*     */     
/* 287 */     catch (IOException iOException) {  }
/* 288 */     finally { try { if (fileReader != null) fileReader.close();  } catch (IOException iOException) {} }
/* 289 */      return null;
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
/*     */ 
/*     */   
/*     */   public static DataMatrix readTextFile(File paramFile, boolean paramBoolean, String paramString1, String paramString2) {
/* 323 */     FileReader fileReader = null;
/*     */ 
/*     */     
/* 326 */     try { fileReader = new FileReader(paramFile);
/* 327 */       int i = (int)paramFile.length();
/* 328 */       char[] arrayOfChar = new char[i];
/* 329 */       int j = 0;
/* 330 */       while (j < i)
/* 331 */         j += fileReader.read(arrayOfChar, j, i - j); 
/* 332 */       String str = new String(arrayOfChar);
/*     */       
/* 334 */       return new DataMatrix(str, paramBoolean, "\n\r", paramString1, paramString2);
/*     */ 
/*     */       
/*     */        }
/*     */     
/* 339 */     catch (IOException iOException) {  }
/* 340 */     finally { try { if (fileReader != null) fileReader.close();  } catch (IOException iOException) {} }
/* 341 */      return null;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean write(File paramFile, DataTable paramDataTable) {
/* 382 */     int i = paramDataTable.getColumnCount();
/* 383 */     int j = paramDataTable.getRowCount();
/* 384 */     FileWriter fileWriter = null;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 393 */     try { fileWriter = new FileWriter(paramFile);
/* 394 */       DecimalFormat decimalFormat1 = getRealFormat();
/* 395 */       DecimalFormat decimalFormat2 = getIntegerFormat();
/*     */ 
/*     */       
/* 398 */       Calendar calendar = Calendar.getInstance();
/* 399 */       Date date = calendar.getTime();
/* 400 */       String str = "/* File created by DataFile Version 1.0 on " + date.toString() + " */" + "\r\n";
/* 401 */       fileWriter.write(str, 0, str.length());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 408 */       str = '"' + paramDataTable.getDefaultNamePrefix() + '"' + '\t' + '"' + classToString(paramDataTable.getDefaultColumnClass()) + '"' + "\r\n";
/*     */       
/* 410 */       fileWriter.write(str, 0, str.length());
/*     */ 
/*     */ 
/*     */       
/* 414 */       str = decimalFormat2.format(j) + '\t' + decimalFormat2.format(i) + "\r\n";
/* 415 */       fileWriter.write(str, 0, str.length());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 426 */       for (byte b = 0; b < i; b++) {
/*     */         
/* 428 */         Class clazz = paramDataTable.getColumnClass(b);
/* 429 */         String str1 = '"' + paramDataTable.getColumnName(b) + '"' + '\t' + '"' + classToString(clazz) + '"' + "\r\n";
/* 430 */         fileWriter.write(str1, 0, str1.length());
/*     */         
/* 432 */         if (clazz == Double.class) {
/*     */ 
/*     */ 
/*     */           
/* 436 */           for (byte b1 = 0; b1 < j; b1++) {
/*     */             
/* 438 */             Object object = paramDataTable.getValueAt(b1, b);
/* 439 */             if (object instanceof Double)
/*     */             {
/* 441 */               Double double_ = (Double)object;
/* 442 */               str = decimalFormat2.format(b1) + '\t' + '"' + decimalFormat1.format(double_.doubleValue()) + '"' + "\r\n";
/* 443 */               fileWriter.write(str, 0, str.length());
/*     */             }
/*     */           
/*     */           } 
/* 447 */         } else if (clazz == Integer.class) {
/*     */ 
/*     */ 
/*     */           
/* 451 */           for (byte b1 = 0; b1 < j; b1++)
/*     */           {
/* 453 */             Object object = paramDataTable.getValueAt(b1, b);
/* 454 */             if (object instanceof Integer)
/*     */             {
/* 456 */               Integer integer = (Integer)object;
/* 457 */               str = decimalFormat2.format(b1) + '\t' + '"' + decimalFormat2.format(integer.intValue()) + '"' + "\r\n";
/* 458 */               fileWriter.write(str, 0, str.length());
/*     */             }
/*     */           
/*     */           }
/*     */         
/*     */         }
/*     */         else {
/*     */           
/* 466 */           for (byte b1 = 0; b1 < j; b1++) {
/*     */             
/* 468 */             str1 = (String)paramDataTable.getValueAt(b1, b);
/* 469 */             if (str1 != null) {
/*     */               
/* 471 */               str = decimalFormat2.format(b1) + '\t' + '"' + str1 + '"' + "\r\n";
/* 472 */               fileWriter.write(str, 0, str.length());
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/* 477 */       return true; }
/*     */     
/* 479 */     catch (IOException iOException) {  }
/* 480 */     finally { try { if (fileWriter != null) fileWriter.close();  } catch (IOException iOException) {} }
/* 481 */      return false;
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
/*     */   public static boolean writeTextFile(File paramFile, DataTable paramDataTable, boolean paramBoolean, String paramString) {
/* 499 */     FileWriter fileWriter = null;
/*     */ 
/*     */     
/* 502 */     try { fileWriter = new FileWriter(paramFile);
/* 503 */       StringBuffer stringBuffer = paramDataTable.getDataAsStringBuffer(true, paramBoolean, paramString);
/* 504 */       String str = stringBuffer.toString();
/* 505 */       fileWriter.write(str, 0, str.length());
/*     */ 
/*     */       
/* 508 */       return true; }
/*     */     
/* 510 */     catch (IOException iOException) {  }
/* 511 */     finally { try { if (fileWriter != null) fileWriter.close();  } catch (IOException iOException) {} }
/* 512 */      return false;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/datatable/DataFile.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */