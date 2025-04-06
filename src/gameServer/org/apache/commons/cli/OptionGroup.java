/*     */ package org.apache.commons.cli;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
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
/*     */ public class OptionGroup
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  37 */   private Map optionMap = new HashMap();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String selected;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean required;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OptionGroup addOption(Option option) {
/*  55 */     this.optionMap.put(option.getKey(), option);
/*     */     
/*  57 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection getNames() {
/*  67 */     return this.optionMap.keySet();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection getOptions() {
/*  76 */     return this.optionMap.values();
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
/*     */   public void setSelected(Option option) throws AlreadySelectedException {
/*  91 */     if (this.selected == null || this.selected.equals(option.getOpt())) {
/*     */       
/*  93 */       this.selected = option.getOpt();
/*     */     }
/*     */     else {
/*     */       
/*  97 */       throw new AlreadySelectedException(this, option);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSelected() {
/* 106 */     return this.selected;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRequired(boolean required) {
/* 114 */     this.required = required;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRequired() {
/* 124 */     return this.required;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 134 */     StringBuffer buff = new StringBuffer();
/*     */     
/* 136 */     Iterator iter = getOptions().iterator();
/*     */     
/* 138 */     buff.append("[");
/*     */     
/* 140 */     while (iter.hasNext()) {
/*     */       
/* 142 */       Option option = iter.next();
/*     */       
/* 144 */       if (option.getOpt() != null) {
/*     */         
/* 146 */         buff.append("-");
/* 147 */         buff.append(option.getOpt());
/*     */       }
/*     */       else {
/*     */         
/* 151 */         buff.append("--");
/* 152 */         buff.append(option.getLongOpt());
/*     */       } 
/*     */       
/* 155 */       buff.append(" ");
/* 156 */       buff.append(option.getDescription());
/*     */       
/* 158 */       if (iter.hasNext())
/*     */       {
/* 160 */         buff.append(", ");
/*     */       }
/*     */     } 
/*     */     
/* 164 */     buff.append("]");
/*     */     
/* 166 */     return buff.toString();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/commons/cli/OptionGroup.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */