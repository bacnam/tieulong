/*     */ package com.mchange.util.impl;
/*     */ 
/*     */ import com.mchange.util.CommandLineParser;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CommandLineParserImpl
/*     */   implements CommandLineParser
/*     */ {
/*     */   String[] argv;
/*     */   String[] validSwitches;
/*     */   String[] reqSwitches;
/*     */   String[] argSwitches;
/*     */   char switch_char;
/*     */   
/*     */   public CommandLineParserImpl(String[] paramArrayOfString1, String[] paramArrayOfString2, String[] paramArrayOfString3, String[] paramArrayOfString4, char paramChar) {
/*  58 */     this.argv = paramArrayOfString1;
/*  59 */     this.validSwitches = (paramArrayOfString2 == null) ? new String[0] : paramArrayOfString2;
/*  60 */     this.reqSwitches = (paramArrayOfString3 == null) ? new String[0] : paramArrayOfString3;
/*  61 */     this.argSwitches = (paramArrayOfString4 == null) ? new String[0] : paramArrayOfString4;
/*  62 */     this.switch_char = paramChar;
/*     */   }
/*     */   
/*     */   public CommandLineParserImpl(String[] paramArrayOfString1, String[] paramArrayOfString2, String[] paramArrayOfString3, String[] paramArrayOfString4) {
/*  66 */     this(paramArrayOfString1, paramArrayOfString2, paramArrayOfString3, paramArrayOfString4, '-');
/*     */   }
/*     */   
/*     */   public boolean checkSwitch(String paramString) {
/*  70 */     for (byte b = 0; b < this.argv.length; b++) {
/*  71 */       if (this.argv[b].charAt(0) == this.switch_char && this.argv[b].equals(this.switch_char + paramString))
/*  72 */         return true; 
/*  73 */     }  return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String findSwitchArg(String paramString) {
/*  78 */     for (byte b = 0; b < this.argv.length - 1; b++) {
/*  79 */       if (this.argv[b].charAt(0) == this.switch_char && this.argv[b].equals(this.switch_char + paramString))
/*  80 */         return (this.argv[b + 1].charAt(0) == this.switch_char) ? null : this.argv[b + 1]; 
/*  81 */     }  return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean checkArgv() {
/*  91 */     return (checkValidSwitches() && checkRequiredSwitches() && checkSwitchArgSyntax());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   boolean checkValidSwitches() {
/*  97 */     for (byte b = 0; b < this.argv.length; b++) {
/*  98 */       if (this.argv[b].charAt(0) == this.switch_char) {
/*     */         
/* 100 */         byte b1 = 0; while (true) { if (b1 < this.validSwitches.length)
/* 101 */           { if (this.argv[b].equals(this.switch_char + this.validSwitches[b1]))
/* 102 */               break;  b1++; continue; }  return false; } 
/*     */       } 
/* 104 */     }  return true;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean checkRequiredSwitches() {
/* 109 */     for (int i = this.reqSwitches.length; --i >= 0;) {
/* 110 */       if (!checkSwitch(this.reqSwitches[i])) return false; 
/* 111 */     }  return true;
/*     */   }
/*     */ 
/*     */   
/*     */   boolean checkSwitchArgSyntax() {
/* 116 */     for (int i = this.argSwitches.length; --i >= 0;) {
/*     */       
/* 118 */       if (checkSwitch(this.argSwitches[i])) {
/*     */         
/* 120 */         String str = findSwitchArg(this.argSwitches[i]);
/* 121 */         if (str == null || str.charAt(0) == this.switch_char)
/* 122 */           return false; 
/*     */       } 
/*     */     } 
/* 125 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int findLastSwitched() {
/* 130 */     for (int i = this.argv.length; --i >= 0;) {
/* 131 */       if (this.argv[i].charAt(0) == this.switch_char)
/* 132 */         return i; 
/* 133 */     }  return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public String[] findUnswitchedArgs() {
/* 138 */     String[] arrayOfString1 = new String[this.argv.length];
/* 139 */     byte b1 = 0;
/* 140 */     for (byte b2 = 0; b2 < this.argv.length; b2++) {
/*     */       
/* 142 */       if (this.argv[b2].charAt(0) == this.switch_char)
/* 143 */       { if (contains(this.argv[b2].substring(1), this.argSwitches)) b2++;  }
/* 144 */       else { arrayOfString1[b1++] = this.argv[b2]; }
/*     */     
/* 146 */     }  String[] arrayOfString2 = new String[b1];
/* 147 */     System.arraycopy(arrayOfString1, 0, arrayOfString2, 0, b1);
/* 148 */     return arrayOfString2;
/*     */   }
/*     */ 
/*     */   
/*     */   private static boolean contains(String paramString, String[] paramArrayOfString) {
/* 153 */     for (int i = paramArrayOfString.length; --i >= 0;) {
/* 154 */       if (paramArrayOfString[i].equals(paramString)) return true; 
/* 155 */     }  return false;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/util/impl/CommandLineParserImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */