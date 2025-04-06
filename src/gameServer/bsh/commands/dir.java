/*     */ package bsh.commands;
/*     */ 
/*     */ import bsh.CallStack;
/*     */ import bsh.Interpreter;
/*     */ import bsh.StringUtil;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.Date;
/*     */ import java.util.GregorianCalendar;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class dir
/*     */ {
/*  19 */   static final String[] months = new String[] { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
/*     */ 
/*     */   
/*     */   public static String usage() {
/*  23 */     return "usage: dir( String dir )\n       dir()";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void invoke(Interpreter env, CallStack callstack) {
/*  31 */     String str = ".";
/*  32 */     invoke(env, callstack, str);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void invoke(Interpreter env, CallStack callstack, String str) {
/*     */     File file;
/*     */     String path;
/*     */     try {
/*  44 */       path = env.pathToFile(str).getAbsolutePath();
/*  45 */       file = env.pathToFile(str);
/*  46 */     } catch (IOException e) {
/*  47 */       env.println("error reading path: " + e);
/*     */       
/*     */       return;
/*     */     } 
/*  51 */     if (!file.exists() || !file.canRead()) {
/*  52 */       env.println("Can't read " + file);
/*     */       return;
/*     */     } 
/*  55 */     if (!file.isDirectory()) {
/*  56 */       env.println("'" + str + "' is not a directory");
/*     */     }
/*     */     
/*  59 */     String[] files = file.list();
/*  60 */     files = StringUtil.bubbleSort(files);
/*     */     
/*  62 */     for (int i = 0; i < files.length; i++) {
/*  63 */       File f = new File(path + File.separator + files[i]);
/*  64 */       StringBuffer sb = new StringBuffer();
/*  65 */       sb.append(f.canRead() ? "r" : "-");
/*  66 */       sb.append(f.canWrite() ? "w" : "-");
/*  67 */       sb.append("_");
/*  68 */       sb.append(" ");
/*     */       
/*  70 */       Date d = new Date(f.lastModified());
/*  71 */       GregorianCalendar c = new GregorianCalendar();
/*  72 */       c.setTime(d);
/*  73 */       int day = c.get(5);
/*  74 */       sb.append(months[c.get(2)] + " " + day);
/*  75 */       if (day < 10) {
/*  76 */         sb.append(" ");
/*     */       }
/*  78 */       sb.append(" ");
/*     */ 
/*     */       
/*  81 */       int fieldlen = 8;
/*  82 */       StringBuffer len = new StringBuffer();
/*  83 */       for (int j = 0; j < fieldlen; j++)
/*  84 */         len.append(" "); 
/*  85 */       len.insert(0, f.length());
/*  86 */       len.setLength(fieldlen);
/*     */       
/*  88 */       int si = len.toString().indexOf(" ");
/*  89 */       if (si != -1) {
/*  90 */         String pad = len.toString().substring(si);
/*  91 */         len.setLength(si);
/*  92 */         len.insert(0, pad);
/*     */       } 
/*     */       
/*  95 */       sb.append(len.toString());
/*     */       
/*  97 */       sb.append(" " + f.getName());
/*  98 */       if (f.isDirectory()) {
/*  99 */         sb.append("/");
/*     */       }
/* 101 */       env.println(sb.toString());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/commands/dir.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */