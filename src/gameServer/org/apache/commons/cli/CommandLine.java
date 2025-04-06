/*     */ package org.apache.commons.cli;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CommandLine
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  48 */   private List args = new LinkedList();
/*     */ 
/*     */   
/*  51 */   private List options = new ArrayList();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasOption(String opt) {
/*  69 */     return this.options.contains(resolveOption(opt));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasOption(char opt) {
/*  80 */     return hasOption(String.valueOf(opt));
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
/*     */   public Object getOptionObject(String opt) {
/*     */     try {
/*  93 */       return getParsedOptionValue(opt);
/*  94 */     } catch (ParseException pe) {
/*  95 */       System.err.println("Exception found converting " + opt + " to desired type: " + pe.getMessage());
/*     */       
/*  97 */       return null;
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
/*     */   public Object getParsedOptionValue(String opt) throws ParseException {
/* 112 */     String res = getOptionValue(opt);
/*     */     
/* 114 */     Option option = resolveOption(opt);
/* 115 */     if (option == null)
/*     */     {
/* 117 */       return null;
/*     */     }
/*     */     
/* 120 */     Object type = option.getType();
/*     */     
/* 122 */     return (res == null) ? null : TypeHandler.createValue(res, type);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getOptionObject(char opt) {
/* 133 */     return getOptionObject(String.valueOf(opt));
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
/*     */   public String getOptionValue(String opt) {
/* 145 */     String[] values = getOptionValues(opt);
/*     */     
/* 147 */     return (values == null) ? null : values[0];
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
/*     */   public String getOptionValue(char opt) {
/* 159 */     return getOptionValue(String.valueOf(opt));
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
/*     */   public String[] getOptionValues(String opt) {
/* 171 */     List values = new ArrayList();
/*     */     
/* 173 */     for (Iterator it = this.options.iterator(); it.hasNext(); ) {
/*     */       
/* 175 */       Option option = it.next();
/* 176 */       if (opt.equals(option.getOpt()) || opt.equals(option.getLongOpt()))
/*     */       {
/* 178 */         values.addAll(option.getValuesList());
/*     */       }
/*     */     } 
/*     */     
/* 182 */     return values.isEmpty() ? null : (String[])values.toArray((Object[])new String[values.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Option resolveOption(String opt) {
/* 193 */     opt = Util.stripLeadingHyphens(opt);
/* 194 */     for (Iterator it = this.options.iterator(); it.hasNext(); ) {
/*     */       
/* 196 */       Option option = it.next();
/* 197 */       if (opt.equals(option.getOpt()))
/*     */       {
/* 199 */         return option;
/*     */       }
/*     */       
/* 202 */       if (opt.equals(option.getLongOpt()))
/*     */       {
/* 204 */         return option;
/*     */       }
/*     */     } 
/*     */     
/* 208 */     return null;
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
/*     */   public String[] getOptionValues(char opt) {
/* 220 */     return getOptionValues(String.valueOf(opt));
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
/*     */   public String getOptionValue(String opt, String defaultValue) {
/* 234 */     String answer = getOptionValue(opt);
/*     */     
/* 236 */     return (answer != null) ? answer : defaultValue;
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
/*     */   public String getOptionValue(char opt, String defaultValue) {
/* 250 */     return getOptionValue(String.valueOf(opt), defaultValue);
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
/*     */   public Properties getOptionProperties(String opt) {
/* 268 */     Properties props = new Properties();
/*     */     
/* 270 */     for (Iterator it = this.options.iterator(); it.hasNext(); ) {
/*     */       
/* 272 */       Option option = it.next();
/*     */       
/* 274 */       if (opt.equals(option.getOpt()) || opt.equals(option.getLongOpt())) {
/*     */         
/* 276 */         List values = option.getValuesList();
/* 277 */         if (values.size() >= 2) {
/*     */ 
/*     */           
/* 280 */           props.put(values.get(0), values.get(1)); continue;
/*     */         } 
/* 282 */         if (values.size() == 1)
/*     */         {
/*     */           
/* 285 */           props.put(values.get(0), "true");
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 290 */     return props;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String[] getArgs() {
/* 300 */     String[] answer = new String[this.args.size()];
/*     */     
/* 302 */     this.args.toArray((Object[])answer);
/*     */     
/* 304 */     return answer;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List getArgList() {
/* 314 */     return this.args;
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
/*     */   void addArg(String arg) {
/* 346 */     this.args.add(arg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void addOption(Option opt) {
/* 356 */     this.options.add(opt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Iterator iterator() {
/* 367 */     return this.options.iterator();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Option[] getOptions() {
/* 377 */     Collection processed = this.options;
/*     */ 
/*     */     
/* 380 */     Option[] optionsArray = new Option[processed.size()];
/*     */ 
/*     */     
/* 383 */     return (Option[])processed.toArray((Object[])optionsArray);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/commons/cli/CommandLine.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */