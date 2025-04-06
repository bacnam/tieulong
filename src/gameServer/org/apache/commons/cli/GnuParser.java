/*     */ package org.apache.commons.cli;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GnuParser
/*     */   extends Parser
/*     */ {
/*     */   protected String[] flatten(Options options, String[] arguments, boolean stopAtNonOption) {
/*  51 */     List tokens = new ArrayList();
/*     */     
/*  53 */     boolean eatTheRest = false;
/*     */     
/*  55 */     for (int i = 0; i < arguments.length; i++) {
/*     */       
/*  57 */       String arg = arguments[i];
/*     */       
/*  59 */       if ("--".equals(arg)) {
/*     */         
/*  61 */         eatTheRest = true;
/*  62 */         tokens.add("--");
/*     */       }
/*  64 */       else if ("-".equals(arg)) {
/*     */         
/*  66 */         tokens.add("-");
/*     */       }
/*  68 */       else if (arg.startsWith("-")) {
/*     */         
/*  70 */         String opt = Util.stripLeadingHyphens(arg);
/*     */         
/*  72 */         if (options.hasOption(opt))
/*     */         {
/*  74 */           tokens.add(arg);
/*     */ 
/*     */         
/*     */         }
/*  78 */         else if (opt.indexOf('=') != -1 && options.hasOption(opt.substring(0, opt.indexOf('='))))
/*     */         {
/*     */           
/*  81 */           tokens.add(arg.substring(0, arg.indexOf('=')));
/*  82 */           tokens.add(arg.substring(arg.indexOf('=') + 1));
/*     */         }
/*  84 */         else if (options.hasOption(arg.substring(0, 2)))
/*     */         {
/*     */           
/*  87 */           tokens.add(arg.substring(0, 2));
/*  88 */           tokens.add(arg.substring(2));
/*     */         }
/*     */         else
/*     */         {
/*  92 */           eatTheRest = stopAtNonOption;
/*  93 */           tokens.add(arg);
/*     */         }
/*     */       
/*     */       }
/*     */       else {
/*     */         
/*  99 */         tokens.add(arg);
/*     */       } 
/*     */       
/* 102 */       if (eatTheRest)
/*     */       {
/* 104 */         for (; ++i < arguments.length; i++)
/*     */         {
/* 106 */           tokens.add(arguments[i]);
/*     */         }
/*     */       }
/*     */     } 
/*     */     
/* 111 */     return tokens.<String>toArray(new String[tokens.size()]);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/commons/cli/GnuParser.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */