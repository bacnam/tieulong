/*     */ package org.apache.commons.cli;
/*     */ 
/*     */ import java.io.Serializable;
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
/*     */ public class Option
/*     */   implements Cloneable, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   public static final int UNINITIALIZED = -1;
/*     */   public static final int UNLIMITED_VALUES = -2;
/*     */   private String opt;
/*     */   private String longOpt;
/*  56 */   private String argName = "arg";
/*     */ 
/*     */   
/*     */   private String description;
/*     */ 
/*     */   
/*     */   private boolean required;
/*     */ 
/*     */   
/*     */   private boolean optionalArg;
/*     */ 
/*     */   
/*  68 */   private int numberOfArgs = -1;
/*     */ 
/*     */   
/*     */   private Object type;
/*     */ 
/*     */   
/*  74 */   private List values = new ArrayList();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private char valuesep;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Option(String opt, String description) throws IllegalArgumentException {
/*  90 */     this(opt, null, false, description);
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
/*     */   public Option(String opt, boolean hasArg, String description) throws IllegalArgumentException {
/* 105 */     this(opt, null, hasArg, description);
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
/*     */   public Option(String opt, String longOpt, boolean hasArg, String description) throws IllegalArgumentException {
/* 123 */     OptionValidator.validateOption(opt);
/*     */     
/* 125 */     this.opt = opt;
/* 126 */     this.longOpt = longOpt;
/*     */ 
/*     */     
/* 129 */     if (hasArg)
/*     */     {
/* 131 */       this.numberOfArgs = 1;
/*     */     }
/*     */     
/* 134 */     this.description = description;
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
/*     */   public int getId() {
/* 146 */     return getKey().charAt(0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String getKey() {
/* 157 */     if (this.opt == null)
/*     */     {
/* 159 */       return this.longOpt;
/*     */     }
/*     */     
/* 162 */     return this.opt;
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
/*     */   public String getOpt() {
/* 177 */     return this.opt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getType() {
/* 187 */     return this.type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setType(Object type) {
/* 197 */     this.type = type;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getLongOpt() {
/* 207 */     return this.longOpt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLongOpt(String longOpt) {
/* 217 */     this.longOpt = longOpt;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOptionalArg(boolean optionalArg) {
/* 228 */     this.optionalArg = optionalArg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasOptionalArg() {
/* 236 */     return this.optionalArg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasLongOpt() {
/* 246 */     return (this.longOpt != null);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasArg() {
/* 256 */     return (this.numberOfArgs > 0 || this.numberOfArgs == -2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDescription() {
/* 266 */     return this.description;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDescription(String description) {
/* 277 */     this.description = description;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isRequired() {
/* 287 */     return this.required;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRequired(boolean required) {
/* 297 */     this.required = required;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setArgName(String argName) {
/* 307 */     this.argName = argName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getArgName() {
/* 317 */     return this.argName;
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
/*     */   public boolean hasArgName() {
/* 329 */     return (this.argName != null && this.argName.length() > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasArgs() {
/* 339 */     return (this.numberOfArgs > 1 || this.numberOfArgs == -2);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setArgs(int num) {
/* 349 */     this.numberOfArgs = num;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setValueSeparator(char sep) {
/* 360 */     this.valuesep = sep;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public char getValueSeparator() {
/* 370 */     return this.valuesep;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasValueSeparator() {
/* 381 */     return (this.valuesep > '\000');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getArgs() {
/* 391 */     return this.numberOfArgs;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void addValueForProcessing(String value) {
/* 401 */     switch (this.numberOfArgs) {
/*     */       
/*     */       case -1:
/* 404 */         throw new RuntimeException("NO_ARGS_ALLOWED");
/*     */     } 
/*     */     
/* 407 */     processValue(value);
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
/*     */   private void processValue(String value) {
/* 425 */     if (hasValueSeparator()) {
/*     */ 
/*     */       
/* 428 */       char sep = getValueSeparator();
/*     */ 
/*     */       
/* 431 */       int index = value.indexOf(sep);
/*     */ 
/*     */       
/* 434 */       while (index != -1) {
/*     */ 
/*     */         
/* 437 */         if (this.values.size() == this.numberOfArgs - 1) {
/*     */           break;
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 443 */         add(value.substring(0, index));
/*     */ 
/*     */         
/* 446 */         value = value.substring(index + 1);
/*     */ 
/*     */         
/* 449 */         index = value.indexOf(sep);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 454 */     add(value);
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
/*     */   private void add(String value) {
/* 468 */     if (this.numberOfArgs > 0 && this.values.size() > this.numberOfArgs - 1)
/*     */     {
/* 470 */       throw new RuntimeException("Cannot add value, list full.");
/*     */     }
/*     */ 
/*     */     
/* 474 */     this.values.add(value);
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
/*     */   public String getValue() {
/* 486 */     return hasNoValues() ? null : this.values.get(0);
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
/*     */   public String getValue(int index) throws IndexOutOfBoundsException {
/* 503 */     return hasNoValues() ? null : this.values.get(index);
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
/*     */   public String getValue(String defaultValue) {
/* 518 */     String value = getValue();
/*     */     
/* 520 */     return (value != null) ? value : defaultValue;
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
/*     */   public String[] getValues() {
/* 532 */     return hasNoValues() ? null : (String[])this.values.toArray((Object[])new String[this.values.size()]);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List getValuesList() {
/* 541 */     return this.values;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 551 */     StringBuffer buf = (new StringBuffer()).append("[ option: ");
/*     */     
/* 553 */     buf.append(this.opt);
/*     */     
/* 555 */     if (this.longOpt != null)
/*     */     {
/* 557 */       buf.append(" ").append(this.longOpt);
/*     */     }
/*     */     
/* 560 */     buf.append(" ");
/*     */     
/* 562 */     if (hasArgs()) {
/*     */       
/* 564 */       buf.append("[ARG...]");
/*     */     }
/* 566 */     else if (hasArg()) {
/*     */       
/* 568 */       buf.append(" [ARG]");
/*     */     } 
/*     */     
/* 571 */     buf.append(" :: ").append(this.description);
/*     */     
/* 573 */     if (this.type != null)
/*     */     {
/* 575 */       buf.append(" :: ").append(this.type);
/*     */     }
/*     */     
/* 578 */     buf.append(" ]");
/*     */     
/* 580 */     return buf.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean hasNoValues() {
/* 590 */     return this.values.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 595 */     if (this == o)
/*     */     {
/* 597 */       return true;
/*     */     }
/* 599 */     if (o == null || getClass() != o.getClass())
/*     */     {
/* 601 */       return false;
/*     */     }
/*     */     
/* 604 */     Option option = (Option)o;
/*     */ 
/*     */     
/* 607 */     if ((this.opt != null) ? !this.opt.equals(option.opt) : (option.opt != null))
/*     */     {
/* 609 */       return false;
/*     */     }
/* 611 */     if ((this.longOpt != null) ? !this.longOpt.equals(option.longOpt) : (option.longOpt != null))
/*     */     {
/* 613 */       return false;
/*     */     }
/*     */     
/* 616 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 622 */     int result = (this.opt != null) ? this.opt.hashCode() : 0;
/* 623 */     result = 31 * result + ((this.longOpt != null) ? this.longOpt.hashCode() : 0);
/* 624 */     return result;
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
/*     */   public Object clone() {
/*     */     try {
/* 641 */       Option option = (Option)super.clone();
/* 642 */       option.values = new ArrayList(this.values);
/* 643 */       return option;
/*     */     }
/* 645 */     catch (CloneNotSupportedException cnse) {
/*     */       
/* 647 */       throw new RuntimeException("A CloneNotSupportedException was thrown: " + cnse.getMessage());
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
/*     */   void clearValues() {
/* 659 */     this.values.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean addValue(String value) {
/* 670 */     throw new UnsupportedOperationException("The addValue method is not intended for client use. Subclasses should use the addValueForProcessing method instead. ");
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/commons/cli/Option.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */