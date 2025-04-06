/*     */ package org.apache.commons.cli;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
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
/*     */ public class PosixParser
/*     */   extends Parser
/*     */ {
/*  35 */   private List tokens = new ArrayList();
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean eatTheRest;
/*     */ 
/*     */ 
/*     */   
/*     */   private Option currentOption;
/*     */ 
/*     */ 
/*     */   
/*     */   private Options options;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void init() {
/*  53 */     this.eatTheRest = false;
/*  54 */     this.tokens.clear();
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
/*     */   protected String[] flatten(Options options, String[] arguments, boolean stopAtNonOption) {
/*  97 */     init();
/*  98 */     this.options = options;
/*     */ 
/*     */     
/* 101 */     Iterator iter = Arrays.<String>asList(arguments).iterator();
/*     */ 
/*     */     
/* 104 */     while (iter.hasNext()) {
/*     */ 
/*     */       
/* 107 */       String token = iter.next();
/*     */ 
/*     */       
/* 110 */       if (token.startsWith("--")) {
/*     */         
/* 112 */         int pos = token.indexOf('=');
/* 113 */         String opt = (pos == -1) ? token : token.substring(0, pos);
/*     */         
/* 115 */         if (!options.hasOption(opt))
/*     */         {
/* 117 */           processNonOptionToken(token, stopAtNonOption);
/*     */         }
/*     */         else
/*     */         {
/* 121 */           this.currentOption = options.getOption(opt);
/*     */           
/* 123 */           this.tokens.add(opt);
/* 124 */           if (pos != -1)
/*     */           {
/* 126 */             this.tokens.add(token.substring(pos + 1));
/*     */           
/*     */           }
/*     */         }
/*     */       
/*     */       }
/* 132 */       else if ("-".equals(token)) {
/*     */         
/* 134 */         this.tokens.add(token);
/*     */       }
/* 136 */       else if (token.startsWith("-")) {
/*     */         
/* 138 */         if (token.length() == 2 || options.hasOption(token))
/*     */         {
/* 140 */           processOptionToken(token, stopAtNonOption);
/*     */         
/*     */         }
/*     */         else
/*     */         {
/* 145 */           burstToken(token, stopAtNonOption);
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 150 */         processNonOptionToken(token, stopAtNonOption);
/*     */       } 
/*     */       
/* 153 */       gobble(iter);
/*     */     } 
/*     */     
/* 156 */     return (String[])this.tokens.toArray((Object[])new String[this.tokens.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void gobble(Iterator iter) {
/* 166 */     if (this.eatTheRest)
/*     */     {
/* 168 */       while (iter.hasNext())
/*     */       {
/* 170 */         this.tokens.add(iter.next());
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
/*     */   
/*     */   private void processNonOptionToken(String value, boolean stopAtNonOption) {
/* 184 */     if (stopAtNonOption && (this.currentOption == null || !this.currentOption.hasArg())) {
/*     */       
/* 186 */       this.eatTheRest = true;
/* 187 */       this.tokens.add("--");
/*     */     } 
/*     */     
/* 190 */     this.tokens.add(value);
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
/*     */   private void processOptionToken(String token, boolean stopAtNonOption) {
/* 207 */     if (stopAtNonOption && !this.options.hasOption(token))
/*     */     {
/* 209 */       this.eatTheRest = true;
/*     */     }
/*     */     
/* 212 */     if (this.options.hasOption(token))
/*     */     {
/* 214 */       this.currentOption = this.options.getOption(token);
/*     */     }
/*     */     
/* 217 */     this.tokens.add(token);
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
/*     */   protected void burstToken(String token, boolean stopAtNonOption) {
/* 248 */     for (int i = 1; i < token.length(); i++) {
/*     */       
/* 250 */       String ch = String.valueOf(token.charAt(i));
/*     */       
/* 252 */       if (this.options.hasOption(ch)) {
/*     */         
/* 254 */         this.tokens.add("-" + ch);
/* 255 */         this.currentOption = this.options.getOption(ch);
/*     */         
/* 257 */         if (this.currentOption.hasArg() && token.length() != i + 1) {
/*     */           
/* 259 */           this.tokens.add(token.substring(i + 1));
/*     */           
/*     */           break;
/*     */         } 
/*     */       } else {
/* 264 */         if (stopAtNonOption) {
/*     */           
/* 266 */           processNonOptionToken(token.substring(i), true);
/*     */           
/*     */           break;
/*     */         } 
/*     */         
/* 271 */         this.tokens.add(token);
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/commons/cli/PosixParser.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */