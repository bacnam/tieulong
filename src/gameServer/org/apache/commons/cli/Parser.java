/*     */ package org.apache.commons.cli;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Parser
/*     */   implements CommandLineParser
/*     */ {
/*     */   protected CommandLine cmd;
/*     */   private Options options;
/*     */   private List requiredOptions;
/*     */   
/*     */   protected void setOptions(Options options) {
/*  47 */     this.options = options;
/*  48 */     this.requiredOptions = new ArrayList(options.getRequiredOptions());
/*     */   }
/*     */ 
/*     */   
/*     */   protected Options getOptions() {
/*  53 */     return this.options;
/*     */   }
/*     */ 
/*     */   
/*     */   protected List getRequiredOptions() {
/*  58 */     return this.requiredOptions;
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
/*     */   protected abstract String[] flatten(Options paramOptions, String[] paramArrayOfString, boolean paramBoolean);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CommandLine parse(Options options, String[] arguments) throws ParseException {
/*  85 */     return parse(options, arguments, null, false);
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
/*     */   public CommandLine parse(Options options, String[] arguments, Properties properties) throws ParseException {
/* 102 */     return parse(options, arguments, properties, false);
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
/*     */   public CommandLine parse(Options options, String[] arguments, boolean stopAtNonOption) throws ParseException {
/* 119 */     return parse(options, arguments, null, stopAtNonOption);
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
/*     */   public CommandLine parse(Options options, String[] arguments, Properties properties, boolean stopAtNonOption) throws ParseException {
/* 143 */     for (Iterator it = options.helpOptions().iterator(); it.hasNext(); ) {
/*     */       
/* 145 */       Option opt = it.next();
/* 146 */       opt.clearValues();
/*     */     } 
/*     */ 
/*     */     
/* 150 */     setOptions(options);
/*     */     
/* 152 */     this.cmd = new CommandLine();
/*     */     
/* 154 */     boolean eatTheRest = false;
/*     */     
/* 156 */     if (arguments == null)
/*     */     {
/* 158 */       arguments = new String[0];
/*     */     }
/*     */     
/* 161 */     List tokenList = Arrays.asList(flatten(getOptions(), arguments, stopAtNonOption));
/*     */     
/* 163 */     ListIterator iterator = tokenList.listIterator();
/*     */ 
/*     */     
/* 166 */     while (iterator.hasNext()) {
/*     */       
/* 168 */       String t = iterator.next();
/*     */ 
/*     */       
/* 171 */       if ("--".equals(t)) {
/*     */         
/* 173 */         eatTheRest = true;
/*     */ 
/*     */       
/*     */       }
/* 177 */       else if ("-".equals(t)) {
/*     */         
/* 179 */         if (stopAtNonOption)
/*     */         {
/* 181 */           eatTheRest = true;
/*     */         }
/*     */         else
/*     */         {
/* 185 */           this.cmd.addArg(t);
/*     */         
/*     */         }
/*     */       
/*     */       }
/* 190 */       else if (t.startsWith("-")) {
/*     */         
/* 192 */         if (stopAtNonOption && !getOptions().hasOption(t))
/*     */         {
/* 194 */           eatTheRest = true;
/* 195 */           this.cmd.addArg(t);
/*     */         }
/*     */         else
/*     */         {
/* 199 */           processOption(t, iterator);
/*     */         
/*     */         }
/*     */       
/*     */       }
/*     */       else {
/*     */         
/* 206 */         this.cmd.addArg(t);
/*     */         
/* 208 */         if (stopAtNonOption)
/*     */         {
/* 210 */           eatTheRest = true;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 215 */       if (eatTheRest)
/*     */       {
/* 217 */         while (iterator.hasNext()) {
/*     */           
/* 219 */           String str = iterator.next();
/*     */ 
/*     */           
/* 222 */           if (!"--".equals(str))
/*     */           {
/* 224 */             this.cmd.addArg(str);
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 230 */     processProperties(properties);
/* 231 */     checkRequiredOptions();
/*     */     
/* 233 */     return this.cmd;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void processProperties(Properties properties) {
/* 243 */     if (properties == null) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 248 */     for (Enumeration e = properties.propertyNames(); e.hasMoreElements(); ) {
/*     */       
/* 250 */       String option = e.nextElement().toString();
/*     */       
/* 252 */       if (!this.cmd.hasOption(option)) {
/*     */         
/* 254 */         Option opt = getOptions().getOption(option);
/*     */ 
/*     */         
/* 257 */         String value = properties.getProperty(option);
/*     */         
/* 259 */         if (opt.hasArg()) {
/*     */           
/* 261 */           if (opt.getValues() == null || (opt.getValues()).length == 0) {
/*     */             
/*     */             try {
/*     */               
/* 265 */               opt.addValueForProcessing(value);
/*     */             }
/* 267 */             catch (RuntimeException exp) {}
/*     */ 
/*     */           
/*     */           }
/*     */         
/*     */         }
/* 273 */         else if (!"yes".equalsIgnoreCase(value) && !"true".equalsIgnoreCase(value) && !"1".equalsIgnoreCase(value)) {
/*     */           break;
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 282 */         this.cmd.addOption(opt);
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
/*     */   
/*     */   protected void checkRequiredOptions() throws MissingOptionException {
/* 297 */     if (!getRequiredOptions().isEmpty())
/*     */     {
/* 299 */       throw new MissingOptionException(getRequiredOptions());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void processArgs(Option opt, ListIterator iter) throws ParseException {
/* 318 */     while (iter.hasNext()) {
/*     */       
/* 320 */       String str = iter.next();
/*     */ 
/*     */       
/* 323 */       if (getOptions().hasOption(str) && str.startsWith("-")) {
/*     */         
/* 325 */         iter.previous();
/*     */ 
/*     */         
/*     */         break;
/*     */       } 
/*     */       
/*     */       try {
/* 332 */         opt.addValueForProcessing(Util.stripLeadingAndTrailingQuotes(str));
/*     */       }
/* 334 */       catch (RuntimeException exp) {
/*     */         
/* 336 */         iter.previous();
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 341 */     if (opt.getValues() == null && !opt.hasOptionalArg())
/*     */     {
/* 343 */       throw new MissingArgumentException(opt);
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
/*     */ 
/*     */   
/*     */   protected void processOption(String arg, ListIterator iter) throws ParseException {
/* 358 */     boolean hasOption = getOptions().hasOption(arg);
/*     */ 
/*     */     
/* 361 */     if (!hasOption)
/*     */     {
/* 363 */       throw new UnrecognizedOptionException("Unrecognized option: " + arg, arg);
/*     */     }
/*     */ 
/*     */     
/* 367 */     Option opt = (Option)getOptions().getOption(arg).clone();
/*     */ 
/*     */ 
/*     */     
/* 371 */     if (opt.isRequired())
/*     */     {
/* 373 */       getRequiredOptions().remove(opt.getKey());
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 378 */     if (getOptions().getOptionGroup(opt) != null) {
/*     */       
/* 380 */       OptionGroup group = getOptions().getOptionGroup(opt);
/*     */       
/* 382 */       if (group.isRequired())
/*     */       {
/* 384 */         getRequiredOptions().remove(group);
/*     */       }
/*     */       
/* 387 */       group.setSelected(opt);
/*     */     } 
/*     */ 
/*     */     
/* 391 */     if (opt.hasArg())
/*     */     {
/* 393 */       processArgs(opt, iter);
/*     */     }
/*     */ 
/*     */     
/* 397 */     this.cmd.addOption(opt);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/commons/cli/Parser.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */