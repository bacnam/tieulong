/*     */ package org.apache.commons.cli;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Options
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  51 */   private Map shortOpts = new HashMap();
/*     */ 
/*     */   
/*  54 */   private Map longOpts = new HashMap();
/*     */ 
/*     */   
/*  57 */   private List requiredOpts = new ArrayList();
/*     */ 
/*     */   
/*  60 */   private Map optionGroups = new HashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Options addOptionGroup(OptionGroup group) {
/*  70 */     Iterator options = group.getOptions().iterator();
/*     */     
/*  72 */     if (group.isRequired())
/*     */     {
/*  74 */       this.requiredOpts.add(group);
/*     */     }
/*     */     
/*  77 */     while (options.hasNext()) {
/*     */       
/*  79 */       Option option = options.next();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  84 */       option.setRequired(false);
/*  85 */       addOption(option);
/*     */       
/*  87 */       this.optionGroups.put(option.getKey(), group);
/*     */     } 
/*     */     
/*  90 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Collection getOptionGroups() {
/* 100 */     return new HashSet(this.optionGroups.values());
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
/*     */   public Options addOption(String opt, boolean hasArg, String description) {
/* 114 */     addOption(opt, null, hasArg, description);
/*     */     
/* 116 */     return this;
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
/*     */   public Options addOption(String opt, String longOpt, boolean hasArg, String description) {
/* 131 */     addOption(new Option(opt, longOpt, hasArg, description));
/*     */     
/* 133 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Options addOption(Option opt) {
/* 144 */     String key = opt.getKey();
/*     */ 
/*     */     
/* 147 */     if (opt.hasLongOpt())
/*     */     {
/* 149 */       this.longOpts.put(opt.getLongOpt(), opt);
/*     */     }
/*     */ 
/*     */     
/* 153 */     if (opt.isRequired()) {
/*     */       
/* 155 */       if (this.requiredOpts.contains(key))
/*     */       {
/* 157 */         this.requiredOpts.remove(this.requiredOpts.indexOf(key));
/*     */       }
/* 159 */       this.requiredOpts.add(key);
/*     */     } 
/*     */     
/* 162 */     this.shortOpts.put(key, opt);
/*     */     
/* 164 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection getOptions() {
/* 174 */     return Collections.unmodifiableCollection(helpOptions());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   List helpOptions() {
/* 184 */     return new ArrayList(this.shortOpts.values());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List getRequiredOptions() {
/* 194 */     return this.requiredOpts;
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
/*     */   public Option getOption(String opt) {
/* 206 */     opt = Util.stripLeadingHyphens(opt);
/*     */     
/* 208 */     if (this.shortOpts.containsKey(opt))
/*     */     {
/* 210 */       return (Option)this.shortOpts.get(opt);
/*     */     }
/*     */     
/* 213 */     return (Option)this.longOpts.get(opt);
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
/*     */   public boolean hasOption(String opt) {
/* 225 */     opt = Util.stripLeadingHyphens(opt);
/*     */     
/* 227 */     return (this.shortOpts.containsKey(opt) || this.longOpts.containsKey(opt));
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
/*     */   public OptionGroup getOptionGroup(Option opt) {
/* 239 */     return (OptionGroup)this.optionGroups.get(opt.getKey());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 249 */     StringBuffer buf = new StringBuffer();
/*     */     
/* 251 */     buf.append("[ Options: [ short ");
/* 252 */     buf.append(this.shortOpts.toString());
/* 253 */     buf.append(" ] [ long ");
/* 254 */     buf.append(this.longOpts);
/* 255 */     buf.append(" ]");
/*     */     
/* 257 */     return buf.toString();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/commons/cli/Options.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */