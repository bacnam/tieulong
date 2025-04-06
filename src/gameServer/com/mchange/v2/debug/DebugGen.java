/*     */ package com.mchange.v2.debug;
/*     */ 
/*     */ import com.mchange.v1.io.WriterUtils;
/*     */ import com.mchange.v1.lang.BooleanUtils;
/*     */ import com.mchange.v1.util.SetUtils;
/*     */ import com.mchange.v1.util.StringTokenizerUtils;
/*     */ import com.mchange.v2.cmdline.CommandLineUtils;
/*     */ import com.mchange.v2.cmdline.ParsedCommandLine;
/*     */ import com.mchange.v2.io.DirectoryDescentUtils;
/*     */ import com.mchange.v2.io.FileIterator;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.util.HashSet;
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
/*     */ public final class DebugGen
/*     */   implements DebugConstants
/*     */ {
/*  50 */   static final String[] VALID = new String[] { "codebase", "packages", "trace", "debug", "recursive", "javac", "noclobber", "classname", "skipdirs", "outputbase" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  64 */   static final String[] REQUIRED = new String[] { "codebase", "packages", "trace", "debug" };
/*     */ 
/*     */   
/*  67 */   static final String[] ARGS = new String[] { "codebase", "packages", "trace", "debug", "classname", "outputbase" };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  74 */   static final String EOL = System.getProperty("line.separator");
/*     */ 
/*     */   
/*     */   static int trace_level;
/*     */   
/*     */   static boolean debug;
/*     */   
/*     */   static boolean recursive;
/*     */   
/*     */   static String classname;
/*     */   
/*     */   static boolean clobber;
/*     */   
/*     */   static Set skipDirs;
/*     */ 
/*     */   
/*     */   public static final synchronized void main(String[] paramArrayOfString) {
/*     */     try {
/*  92 */       ParsedCommandLine parsedCommandLine = CommandLineUtils.parse(paramArrayOfString, "--", VALID, REQUIRED, ARGS);
/*     */ 
/*     */       
/*  95 */       String str1 = parsedCommandLine.getSwitchArg("codebase");
/*  96 */       str1 = platify(str1);
/*  97 */       if (!str1.endsWith(File.separator)) {
/*  98 */         str1 = str1 + File.separator;
/*     */       }
/*     */       
/* 101 */       String str2 = parsedCommandLine.getSwitchArg("outputbase");
/* 102 */       if (str2 != null) {
/*     */         
/* 104 */         str2 = platify(str2);
/* 105 */         if (!str2.endsWith(File.separator)) {
/* 106 */           str2 = str2 + File.separator;
/*     */         }
/*     */       } else {
/* 109 */         str2 = str1;
/*     */       } 
/* 111 */       File file = new File(str2);
/*     */       
/* 113 */       if (file.exists()) {
/*     */         
/* 115 */         if (!file.isDirectory())
/*     */         {
/*     */           
/* 118 */           System.exit(-1);
/*     */         }
/* 120 */         else if (!file.canWrite())
/*     */         {
/* 122 */           System.err.println("Output Base '" + file.getPath() + "' is not writable!");
/* 123 */           System.exit(-1);
/*     */         }
/*     */       
/* 126 */       } else if (!file.mkdirs()) {
/*     */         
/* 128 */         System.err.println("Output Base directory '" + file.getPath() + "' does not exist, and could not be created!");
/* 129 */         System.exit(-1);
/*     */       } 
/*     */ 
/*     */       
/* 133 */       String[] arrayOfString = StringTokenizerUtils.tokenizeToArray(parsedCommandLine.getSwitchArg("packages"), ", \t");
/* 134 */       File[] arrayOfFile = new File[arrayOfString.length]; int i;
/* 135 */       for (byte b = 0; b < i; b++) {
/* 136 */         arrayOfFile[b] = new File(str1 + sepify(arrayOfString[b]));
/*     */       }
/*     */       
/* 139 */       trace_level = Integer.parseInt(parsedCommandLine.getSwitchArg("trace"));
/*     */ 
/*     */       
/* 142 */       debug = BooleanUtils.parseBoolean(parsedCommandLine.getSwitchArg("debug"));
/*     */ 
/*     */       
/* 145 */       classname = parsedCommandLine.getSwitchArg("classname");
/* 146 */       if (classname == null) {
/* 147 */         classname = "Debug";
/*     */       }
/*     */       
/* 150 */       recursive = parsedCommandLine.includesSwitch("recursive");
/*     */ 
/*     */       
/* 153 */       clobber = !parsedCommandLine.includesSwitch("noclobber");
/*     */ 
/*     */       
/* 156 */       String str3 = parsedCommandLine.getSwitchArg("skipdirs");
/* 157 */       if (str3 != null) {
/*     */         
/* 159 */         String[] arrayOfString1 = StringTokenizerUtils.tokenizeToArray(str3, ", \t");
/* 160 */         skipDirs = SetUtils.setFromArray((Object[])arrayOfString1);
/*     */       }
/*     */       else {
/*     */         
/* 164 */         skipDirs = new HashSet();
/* 165 */         skipDirs.add("CVS");
/*     */       } 
/*     */       
/* 168 */       if (parsedCommandLine.includesSwitch("javac"))
/* 169 */         System.err.println("autorecompilation of packages not yet implemented."); 
/*     */       int j;
/* 171 */       for (i = 0, j = arrayOfFile.length; i < j; i++) {
/*     */         
/* 173 */         if (recursive) {
/*     */           
/* 175 */           if (!recursivePrecheckPackages(str1, arrayOfFile[i], str2, file))
/*     */           {
/* 177 */             System.err.println("One or more of the specifies packages could not be processed. Aborting. No files have been modified.");
/*     */ 
/*     */             
/* 180 */             System.exit(-1);
/*     */           
/*     */           }
/*     */         
/*     */         }
/* 185 */         else if (!precheckPackage(str1, arrayOfFile[i], str2, file)) {
/*     */           
/* 187 */           System.err.println("One or more of the specifies packages could not be processed. Aborting. No files have been modified.");
/*     */ 
/*     */           
/* 190 */           System.exit(-1);
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/* 195 */       for (i = 0, j = arrayOfFile.length; i < j; i++) {
/*     */         
/* 197 */         if (recursive) {
/* 198 */           recursiveWriteDebugFiles(str1, arrayOfFile[i], str2, file);
/*     */         } else {
/* 200 */           writeDebugFile(str2, arrayOfFile[i]);
/*     */         } 
/*     */       } 
/* 203 */     } catch (Exception exception) {
/*     */       
/* 205 */       exception.printStackTrace();
/* 206 */       System.err.println();
/* 207 */       usage();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static void usage() {
/* 213 */     System.err.println("java " + DebugGen.class.getName() + " \\");
/* 214 */     System.err.println("\t--codebase=<directory under which packages live> \\  (no default)");
/* 215 */     System.err.println("\t--packages=<comma separated list of packages>    \\  (no default)");
/* 216 */     System.err.println("\t--debug=<true|false>                             \\  (no default)");
/* 217 */     System.err.println("\t--trace=<an int between 0 and 10>                \\  (no default)");
/* 218 */     System.err.println("\t--outputdir=<directory under which to generate>  \\  (defaults to same dir as codebase)");
/* 219 */     System.err.println("\t--recursive                                      \\  (no args)");
/* 220 */     System.err.println("\t--noclobber                                      \\  (no args)");
/* 221 */     System.err.println("\t--classname=<class to generate>                  \\  (defaults to Debug)");
/* 222 */     System.err.println("\t--skipdirs=<directories that should be skipped>  \\  (defaults to CVS)");
/*     */   }
/*     */ 
/*     */   
/*     */   private static String ify(String paramString, char paramChar1, char paramChar2) {
/* 227 */     if (paramChar1 == paramChar2) return paramString;
/*     */     
/* 229 */     StringBuffer stringBuffer = new StringBuffer(paramString); byte b; int i;
/* 230 */     for (b = 0, i = stringBuffer.length(); b < i; b++) {
/* 231 */       if (stringBuffer.charAt(b) == paramChar1)
/* 232 */         stringBuffer.setCharAt(b, paramChar2); 
/* 233 */     }  return stringBuffer.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static String platify(String paramString) {
/* 239 */     String str = ify(paramString, '/', File.separatorChar);
/* 240 */     str = ify(str, '\\', File.separatorChar);
/* 241 */     str = ify(str, ':', File.separatorChar);
/* 242 */     return str;
/*     */   }
/*     */   
/*     */   private static String dottify(String paramString) {
/* 246 */     return ify(paramString, File.separatorChar, '.');
/*     */   }
/*     */   private static String sepify(String paramString) {
/* 249 */     return ify(paramString, '.', File.separatorChar);
/*     */   }
/*     */   
/*     */   private static boolean recursivePrecheckPackages(String paramString1, File paramFile1, String paramString2, File paramFile2) throws IOException {
/* 253 */     FileIterator fileIterator = DirectoryDescentUtils.depthFirstEagerDescent(paramFile1);
/* 254 */     while (fileIterator.hasNext()) {
/*     */       
/* 256 */       File file1 = fileIterator.nextFile();
/* 257 */       if (!file1.isDirectory() || skipDirs.contains(file1.getName())) {
/*     */         continue;
/*     */       }
/* 260 */       File file2 = outputDir(paramString1, file1, paramString2, paramFile2);
/* 261 */       if (!file2.exists() && !file2.mkdirs()) {
/*     */         
/* 263 */         System.err.println("Required output dir: '" + file2 + "' does not exist, and could not be created.");
/*     */         
/* 265 */         return false;
/*     */       } 
/* 267 */       if (!precheckOutputPackageDir(file2))
/* 268 */         return false; 
/*     */     } 
/* 270 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static File outputDir(String paramString1, File paramFile1, String paramString2, File paramFile2) {
/* 276 */     if (paramString1.equals(paramString2)) {
/* 277 */       return paramFile1;
/*     */     }
/* 279 */     String str = paramFile1.getPath();
/* 280 */     if (!str.startsWith(paramString1)) {
/*     */       
/* 282 */       System.err.println(DebugGen.class.getName() + ": program bug. Source package path '" + str + "' does not begin with codebase '" + paramString1 + "'.");
/*     */       
/* 284 */       System.exit(-1);
/*     */     } 
/* 286 */     return new File(paramFile2, str.substring(paramString1.length()));
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean precheckPackage(String paramString1, File paramFile1, String paramString2, File paramFile2) throws IOException {
/* 291 */     return precheckOutputPackageDir(outputDir(paramString1, paramFile1, paramString2, paramFile2));
/*     */   }
/*     */   
/*     */   private static boolean precheckOutputPackageDir(File paramFile) throws IOException {
/* 295 */     File file = new File(paramFile, classname + ".java");
/* 296 */     if (!paramFile.canWrite()) {
/*     */       
/* 298 */       System.err.println("File '" + file.getPath() + "' is not writable.");
/* 299 */       return false;
/*     */     } 
/* 301 */     if (!clobber && file.exists()) {
/*     */       
/* 303 */       System.err.println("File '" + file.getPath() + "' exists, and we are in noclobber mode.");
/* 304 */       return false;
/*     */     } 
/*     */     
/* 307 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   private static void recursiveWriteDebugFiles(String paramString1, File paramFile1, String paramString2, File paramFile2) throws IOException {
/* 312 */     FileIterator fileIterator = DirectoryDescentUtils.depthFirstEagerDescent(outputDir(paramString1, paramFile1, paramString2, paramFile2));
/* 313 */     while (fileIterator.hasNext()) {
/*     */       
/* 315 */       File file = fileIterator.nextFile();
/*     */       
/* 317 */       if (!file.isDirectory() || skipDirs.contains(file.getName())) {
/*     */         continue;
/*     */       }
/* 320 */       writeDebugFile(paramString2, file);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void writeDebugFile(String paramString, File paramFile) throws IOException {
/* 328 */     File file = new File(paramFile, classname + ".java");
/* 329 */     String str = dottify(paramFile.getPath().substring(paramString.length()));
/* 330 */     System.err.println("Writing file: " + file.getPath());
/* 331 */     OutputStreamWriter outputStreamWriter = null;
/*     */     
/*     */     try {
/* 334 */       outputStreamWriter = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(file)), "UTF8");
/*     */ 
/*     */       
/* 337 */       outputStreamWriter.write("/********************************************************************" + EOL);
/* 338 */       outputStreamWriter.write(" * This class generated by " + DebugGen.class.getName() + EOL);
/* 339 */       outputStreamWriter.write(" * and will probably be overwritten by the same! Edit at" + EOL);
/* 340 */       outputStreamWriter.write(" * YOUR PERIL!!! Hahahahaha." + EOL);
/* 341 */       outputStreamWriter.write(" ********************************************************************/" + EOL);
/* 342 */       outputStreamWriter.write(EOL);
/* 343 */       outputStreamWriter.write("package " + str + ';' + EOL);
/* 344 */       outputStreamWriter.write(EOL);
/* 345 */       outputStreamWriter.write("import com.mchange.v2.debug.DebugConstants;" + EOL);
/* 346 */       outputStreamWriter.write(EOL);
/* 347 */       outputStreamWriter.write("final class " + classname + " implements DebugConstants" + EOL);
/* 348 */       outputStreamWriter.write("{" + EOL);
/* 349 */       outputStreamWriter.write("\tfinal static boolean DEBUG = " + debug + ';' + EOL);
/* 350 */       outputStreamWriter.write("\tfinal static int     TRACE = " + traceStr(trace_level) + ';' + EOL);
/* 351 */       outputStreamWriter.write(EOL);
/* 352 */       outputStreamWriter.write("\tprivate " + classname + "()" + EOL);
/* 353 */       outputStreamWriter.write("\t{}" + EOL);
/* 354 */       outputStreamWriter.write("}" + EOL);
/* 355 */       outputStreamWriter.write(EOL);
/* 356 */       outputStreamWriter.flush();
/*     */     } finally {
/*     */       
/* 359 */       WriterUtils.attemptClose(outputStreamWriter);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static String traceStr(int paramInt) {
/* 364 */     if (paramInt == 0)
/* 365 */       return "TRACE_NONE"; 
/* 366 */     if (paramInt == 5)
/* 367 */       return "TRACE_MED"; 
/* 368 */     if (paramInt == 10) {
/* 369 */       return "TRACE_MAX";
/*     */     }
/* 371 */     return String.valueOf(paramInt);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/debug/DebugGen.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */