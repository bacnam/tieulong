/*     */ package org.apache.commons.cli;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.net.URL;
/*     */ import java.util.Date;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PatternOptionBuilder
/*     */ {
/*  58 */   public static final Class STRING_VALUE = String.class;
/*     */ 
/*     */   
/*  61 */   public static final Class OBJECT_VALUE = Object.class;
/*     */ 
/*     */   
/*  64 */   public static final Class NUMBER_VALUE = Number.class;
/*     */ 
/*     */   
/*  67 */   public static final Class DATE_VALUE = Date.class;
/*     */ 
/*     */   
/*  70 */   public static final Class CLASS_VALUE = Class.class;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  77 */   public static final Class EXISTING_FILE_VALUE = FileInputStream.class;
/*     */ 
/*     */   
/*  80 */   public static final Class FILE_VALUE = File.class;
/*     */ 
/*     */   
/*  83 */   public static final Class FILES_VALUE = (array$Ljava$io$File == null) ? (array$Ljava$io$File = class$("[Ljava.io.File;")) : array$Ljava$io$File;
/*     */ 
/*     */   
/*  86 */   public static final Class URL_VALUE = URL.class;
/*     */ 
/*     */ 
/*     */   
/*     */   static Class array$Ljava$io$File;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Object getValueClass(char ch) {
/*  96 */     switch (ch) {
/*     */       
/*     */       case '@':
/*  99 */         return OBJECT_VALUE;
/*     */       case ':':
/* 101 */         return STRING_VALUE;
/*     */       case '%':
/* 103 */         return NUMBER_VALUE;
/*     */       case '+':
/* 105 */         return CLASS_VALUE;
/*     */       case '#':
/* 107 */         return DATE_VALUE;
/*     */       case '<':
/* 109 */         return EXISTING_FILE_VALUE;
/*     */       case '>':
/* 111 */         return FILE_VALUE;
/*     */       case '*':
/* 113 */         return FILES_VALUE;
/*     */       case '/':
/* 115 */         return URL_VALUE;
/*     */     } 
/*     */     
/* 118 */     return null;
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
/*     */   public static boolean isValueCode(char ch) {
/* 130 */     return (ch == '@' || ch == ':' || ch == '%' || ch == '+' || ch == '#' || ch == '<' || ch == '>' || ch == '*' || ch == '/' || ch == '!');
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
/*     */   public static Options parsePattern(String pattern) {
/* 150 */     char opt = ' ';
/* 151 */     boolean required = false;
/* 152 */     Object type = null;
/*     */     
/* 154 */     Options options = new Options();
/*     */     
/* 156 */     for (int i = 0; i < pattern.length(); i++) {
/*     */       
/* 158 */       char ch = pattern.charAt(i);
/*     */ 
/*     */ 
/*     */       
/* 162 */       if (!isValueCode(ch)) {
/*     */         
/* 164 */         if (opt != ' ') {
/*     */           
/* 166 */           OptionBuilder.hasArg((type != null));
/* 167 */           OptionBuilder.isRequired(required);
/* 168 */           OptionBuilder.withType(type);
/*     */ 
/*     */           
/* 171 */           options.addOption(OptionBuilder.create(opt));
/* 172 */           required = false;
/* 173 */           type = null;
/* 174 */           opt = ' ';
/*     */         } 
/*     */         
/* 177 */         opt = ch;
/*     */       }
/* 179 */       else if (ch == '!') {
/*     */         
/* 181 */         required = true;
/*     */       }
/*     */       else {
/*     */         
/* 185 */         type = getValueClass(ch);
/*     */       } 
/*     */     } 
/*     */     
/* 189 */     if (opt != ' ') {
/*     */       
/* 191 */       OptionBuilder.hasArg((type != null));
/* 192 */       OptionBuilder.isRequired(required);
/* 193 */       OptionBuilder.withType(type);
/*     */ 
/*     */       
/* 196 */       options.addOption(OptionBuilder.create(opt));
/*     */     } 
/*     */     
/* 199 */     return options;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/commons/cli/PatternOptionBuilder.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */