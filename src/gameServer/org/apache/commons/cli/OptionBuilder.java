/*     */ package org.apache.commons.cli;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class OptionBuilder
/*     */ {
/*     */   private static String longopt;
/*     */   private static String description;
/*     */   private static String argName;
/*     */   private static boolean required;
/*  46 */   private static int numberOfArgs = -1;
/*     */ 
/*     */   
/*     */   private static Object type;
/*     */ 
/*     */   
/*     */   private static boolean optionalArg;
/*     */ 
/*     */   
/*     */   private static char valuesep;
/*     */ 
/*     */   
/*  58 */   private static OptionBuilder instance = new OptionBuilder();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void reset() {
/*  73 */     description = null;
/*  74 */     argName = "arg";
/*  75 */     longopt = null;
/*  76 */     type = null;
/*  77 */     required = false;
/*  78 */     numberOfArgs = -1;
/*     */ 
/*     */ 
/*     */     
/*  82 */     optionalArg = false;
/*  83 */     valuesep = Character.MIN_VALUE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static OptionBuilder withLongOpt(String newLongopt) {
/*  94 */     longopt = newLongopt;
/*     */     
/*  96 */     return instance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static OptionBuilder hasArg() {
/* 106 */     numberOfArgs = 1;
/*     */     
/* 108 */     return instance;
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
/*     */   public static OptionBuilder hasArg(boolean hasArg) {
/* 120 */     numberOfArgs = hasArg ? 1 : -1;
/*     */     
/* 122 */     return instance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static OptionBuilder withArgName(String name) {
/* 133 */     argName = name;
/*     */     
/* 135 */     return instance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static OptionBuilder isRequired() {
/* 145 */     required = true;
/*     */     
/* 147 */     return instance;
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
/*     */   public static OptionBuilder withValueSeparator(char sep) {
/* 170 */     valuesep = sep;
/*     */     
/* 172 */     return instance;
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
/*     */   public static OptionBuilder withValueSeparator() {
/* 193 */     valuesep = '=';
/*     */     
/* 195 */     return instance;
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
/*     */   public static OptionBuilder isRequired(boolean newRequired) {
/* 207 */     required = newRequired;
/*     */     
/* 209 */     return instance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static OptionBuilder hasArgs() {
/* 219 */     numberOfArgs = -2;
/*     */     
/* 221 */     return instance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static OptionBuilder hasArgs(int num) {
/* 232 */     numberOfArgs = num;
/*     */     
/* 234 */     return instance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static OptionBuilder hasOptionalArg() {
/* 244 */     numberOfArgs = 1;
/* 245 */     optionalArg = true;
/*     */     
/* 247 */     return instance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static OptionBuilder hasOptionalArgs() {
/* 257 */     numberOfArgs = -2;
/* 258 */     optionalArg = true;
/*     */     
/* 260 */     return instance;
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
/*     */   public static OptionBuilder hasOptionalArgs(int numArgs) {
/* 272 */     numberOfArgs = numArgs;
/* 273 */     optionalArg = true;
/*     */     
/* 275 */     return instance;
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
/*     */   public static OptionBuilder withType(Object newType) {
/* 287 */     type = newType;
/*     */     
/* 289 */     return instance;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static OptionBuilder withDescription(String newDescription) {
/* 300 */     description = newDescription;
/*     */     
/* 302 */     return instance;
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
/*     */   public static Option create(char opt) throws IllegalArgumentException {
/* 316 */     return create(String.valueOf(opt));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Option create() throws IllegalArgumentException {
/* 327 */     if (longopt == null) {
/*     */       
/* 329 */       reset();
/* 330 */       throw new IllegalArgumentException("must specify longopt");
/*     */     } 
/*     */     
/* 333 */     return create((String)null);
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
/*     */   public static Option create(String opt) throws IllegalArgumentException {
/* 348 */     Option option = null;
/*     */     
/*     */     try {
/* 351 */       option = new Option(opt, description);
/*     */ 
/*     */       
/* 354 */       option.setLongOpt(longopt);
/* 355 */       option.setRequired(required);
/* 356 */       option.setOptionalArg(optionalArg);
/* 357 */       option.setArgs(numberOfArgs);
/* 358 */       option.setType(type);
/* 359 */       option.setValueSeparator(valuesep);
/* 360 */       option.setArgName(argName);
/*     */     } finally {
/*     */       
/* 363 */       reset();
/*     */     } 
/*     */ 
/*     */     
/* 367 */     return option;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/commons/cli/OptionBuilder.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */