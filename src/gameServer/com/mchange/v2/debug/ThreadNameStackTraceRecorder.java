/*     */ package com.mchange.v2.debug;
/*     */ 
/*     */ import com.mchange.lang.ThrowableUtils;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ThreadNameStackTraceRecorder
/*     */ {
/*  44 */   static final String NL = System.getProperty("line.separator", "\r\n");
/*     */   
/*  46 */   Set set = new HashSet();
/*     */   
/*     */   String dumpHeader;
/*     */   String stackTraceHeader;
/*     */   
/*     */   public ThreadNameStackTraceRecorder(String paramString) {
/*  52 */     this(paramString, "Debug Stack Trace.");
/*     */   }
/*     */   
/*     */   public ThreadNameStackTraceRecorder(String paramString1, String paramString2) {
/*  56 */     this.dumpHeader = paramString1;
/*  57 */     this.stackTraceHeader = paramString2;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized Object record() {
/*  62 */     Record record = new Record(this.stackTraceHeader);
/*  63 */     this.set.add(record);
/*  64 */     return record;
/*     */   }
/*     */   
/*     */   public synchronized void remove(Object paramObject) {
/*  68 */     this.set.remove(paramObject);
/*     */   }
/*     */   public synchronized int size() {
/*  71 */     return this.set.size();
/*     */   }
/*     */   public synchronized String getDump() {
/*  74 */     return getDump(null);
/*     */   }
/*     */   
/*     */   public synchronized String getDump(String paramString) {
/*  78 */     SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yyyy HH:mm:ss.SSSS");
/*     */     
/*  80 */     StringBuffer stringBuffer = new StringBuffer(2047);
/*  81 */     stringBuffer.append(NL);
/*  82 */     stringBuffer.append("----------------------------------------------------");
/*  83 */     stringBuffer.append(NL);
/*  84 */     stringBuffer.append(this.dumpHeader);
/*  85 */     stringBuffer.append(NL);
/*  86 */     if (paramString != null) {
/*     */       
/*  88 */       stringBuffer.append(paramString);
/*  89 */       stringBuffer.append(NL);
/*     */     } 
/*  91 */     boolean bool = true;
/*  92 */     for (Iterator<Record> iterator = this.set.iterator(); iterator.hasNext(); ) {
/*     */       
/*  94 */       if (bool) {
/*  95 */         bool = false;
/*     */       } else {
/*     */         
/*  98 */         stringBuffer.append("---");
/*  99 */         stringBuffer.append(NL);
/*     */       } 
/*     */       
/* 102 */       Record record = iterator.next();
/* 103 */       stringBuffer.append(simpleDateFormat.format(new Date(record.time)));
/* 104 */       stringBuffer.append(" --> Thread Name: ");
/* 105 */       stringBuffer.append(record.threadName);
/* 106 */       stringBuffer.append(NL);
/* 107 */       stringBuffer.append("Stack Trace: ");
/* 108 */       stringBuffer.append(ThrowableUtils.extractStackTrace(record.stackTrace));
/*     */     } 
/* 110 */     stringBuffer.append("----------------------------------------------------");
/* 111 */     stringBuffer.append(NL);
/* 112 */     return stringBuffer.toString();
/*     */   }
/*     */   
/*     */   private static final class Record
/*     */     implements Comparable
/*     */   {
/*     */     long time;
/*     */     String threadName;
/*     */     Throwable stackTrace;
/*     */     
/*     */     Record(String param1String) {
/* 123 */       this.time = System.currentTimeMillis();
/* 124 */       this.threadName = Thread.currentThread().getName();
/* 125 */       this.stackTrace = new Exception(param1String);
/*     */     }
/*     */ 
/*     */     
/*     */     public int compareTo(Object param1Object) {
/* 130 */       Record record = (Record)param1Object;
/* 131 */       if (this.time > record.time)
/* 132 */         return 1; 
/* 133 */       if (this.time < record.time) {
/* 134 */         return -1;
/*     */       }
/*     */       
/* 137 */       int i = System.identityHashCode(this);
/* 138 */       int j = System.identityHashCode(record);
/* 139 */       if (i > j)
/* 140 */         return 1; 
/* 141 */       if (i < j)
/* 142 */         return -1; 
/* 143 */       return 0;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/debug/ThreadNameStackTraceRecorder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */