/*    */ package com.zhonglian.server.common.utils;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.FileWriter;
/*    */ import java.text.SimpleDateFormat;
/*    */ import java.util.Date;
/*    */ import java.util.Map;
/*    */ import java.util.TreeMap;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BinLogAnalysiser
/*    */ {
/* 15 */   private static Map<String, Map<Long, Long>> analysie = new TreeMap<>();
/* 16 */   private static int timestamp = 0;
/*    */   
/*    */   public static void main(String[] args) throws Exception {
/* 19 */     File root = new File("./");
/* 20 */     long count = 0L;
/* 21 */     long filecount = 0L; byte b; int i; File[] arrayOfFile;
/* 22 */     for (i = (arrayOfFile = root.listFiles()).length, b = 0; b < i; ) { File file = arrayOfFile[b];
/* 23 */       if (file.getName().endsWith(".txt")) {
/*    */         Exception exception4;
/*    */         
/* 26 */         filecount++;
/* 27 */         System.out.println("analysie file:" + file.getName() + "   count:" + filecount);
/* 28 */         Exception exception3 = null;
/*    */       } 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/*    */       b++; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 40 */     System.out.println("analysise over, total:" + count);
/* 41 */     System.out.println("start output to analysie.out...");
/*    */     
/* 43 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
/* 44 */     Exception exception1 = null, exception2 = null; try { FileWriter output = new FileWriter(new File("analysie.out"));
/*    */       
/* 46 */       try { for (Map.Entry<String, Map<Long, Long>> pair : analysie.entrySet()) {
/* 47 */           int amount = ((Map)pair.getValue()).values().stream().mapToInt(x -> x.intValue()).sum();
/* 48 */           StringBuilder head = new StringBuilder();
/* 49 */           StringBuilder line = new StringBuilder();
/* 50 */           head.append(pair.getKey()).append('\t');
/* 51 */           line.append(amount).append('\t');
/* 52 */           for (Map.Entry<Long, Long> entry : (Iterable<Map.Entry<Long, Long>>)((Map)pair.getValue()).entrySet()) {
/* 53 */             head.append(sdf.format(new Date(((Long)entry.getKey()).longValue() * 1000L))).append("\t");
/* 54 */             line.append(entry.getValue()).append("\t");
/* 55 */             amount = (int)(amount + ((Long)entry.getValue()).longValue());
/*    */           } 
/* 57 */           head.append('\n');
/* 58 */           line.append('\n');
/* 59 */           output.write(head.toString());
/* 60 */           output.write(line.toString());
/*    */         }  }
/* 62 */       finally { if (output != null) output.close();  }  } finally { exception2 = null; if (exception1 == null) { exception1 = exception2; } else if (exception1 != exception2) { exception1.addSuppressed(exception2); }
/*    */        }
/*    */   
/*    */   }
/*    */   private static void record(String line) {
/* 67 */     String start = null;
/* 68 */     if (line.startsWith("SET TIMESTAMP=")) {
/* 69 */       timestamp = Integer.parseInt(line.replace("SET TIMESTAMP=", "").replace("/*!*/;", "")); return;
/*    */     } 
/* 71 */     if (line.startsWith("update")) {
/* 72 */       String[] split = line.split(" ", 3);
/* 73 */       start = String.valueOf(split[1].replace("`", "").replace(",", "")) + ' ' + split[0];
/* 74 */     } else if (line.startsWith("insert")) {
/* 75 */       String[] split = line.split(" ", 4);
/* 76 */       start = String.valueOf(split[2].replace("(`id`", "").replace("(`ID`", "").replace("`", "").replace(",", "")) + ' ' + split[0];
/* 77 */     } else if (line.startsWith("delete")) {
/* 78 */       String[] split = line.split(" ", 4);
/* 79 */       start = String.valueOf(split[2].replace("`", "")) + ' ' + split[0];
/*    */     } 
/* 81 */     if (start == null) {
/*    */       return;
/*    */     }
/* 84 */     Map<Long, Long> detail = analysie.get(start);
/* 85 */     if (detail == null) {
/* 86 */       analysie.put(start, detail = new TreeMap<>());
/*    */     }
/* 88 */     long time = (timestamp / 180 * 180);
/* 89 */     Long before = detail.get(Long.valueOf(time));
/* 90 */     detail.put(Long.valueOf(time), Long.valueOf((before == null) ? 1L : (before.longValue() + 1L)));
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/utils/BinLogAnalysiser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */