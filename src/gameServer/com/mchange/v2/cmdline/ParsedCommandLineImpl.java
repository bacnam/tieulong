/*     */ package com.mchange.v2.cmdline;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ParsedCommandLineImpl
/*     */   implements ParsedCommandLine
/*     */ {
/*     */   String[] argv;
/*     */   String switchPrefix;
/*     */   String[] unswitchedArgs;
/*  49 */   HashMap foundSwitches = new HashMap<Object, Object>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   ParsedCommandLineImpl(String[] paramArrayOfString1, String paramString, String[] paramArrayOfString2, String[] paramArrayOfString3, String[] paramArrayOfString4) throws BadCommandLineException {
/*  58 */     this.argv = paramArrayOfString1;
/*  59 */     this.switchPrefix = paramString;
/*     */     
/*  61 */     LinkedList<String> linkedList = new LinkedList();
/*  62 */     int i = paramString.length();
/*     */     byte b;
/*  64 */     for (b = 0; b < paramArrayOfString1.length; b++) {
/*     */       
/*  66 */       if (paramArrayOfString1[b].startsWith(paramString)) {
/*     */         
/*  68 */         String str1 = paramArrayOfString1[b].substring(i);
/*  69 */         String str2 = null;
/*     */         
/*  71 */         int j = str1.indexOf('=');
/*  72 */         if (j >= 0) {
/*     */           
/*  74 */           str2 = str1.substring(j + 1);
/*  75 */           str1 = str1.substring(0, j);
/*     */         }
/*  77 */         else if (contains(str1, paramArrayOfString4)) {
/*     */           
/*  79 */           if (b < paramArrayOfString1.length - 1 && !paramArrayOfString1[b + 1].startsWith(paramString)) {
/*  80 */             str2 = paramArrayOfString1[++b];
/*     */           }
/*     */         } 
/*  83 */         if (paramArrayOfString2 != null && !contains(str1, paramArrayOfString2))
/*  84 */           throw new UnexpectedSwitchException("Unexpected Switch: " + str1, str1); 
/*  85 */         if (paramArrayOfString4 != null && str2 != null && !contains(str1, paramArrayOfString4)) {
/*  86 */           throw new UnexpectedSwitchArgumentException("Switch \"" + str1 + "\" should not have an " + "argument. Argument \"" + str2 + "\" found.", str1, str2);
/*     */         }
/*     */ 
/*     */         
/*  90 */         this.foundSwitches.put(str1, str2);
/*     */       } else {
/*     */         
/*  93 */         linkedList.add(paramArrayOfString1[b]);
/*     */       } 
/*     */     } 
/*  96 */     if (paramArrayOfString3 != null)
/*     */     {
/*  98 */       for (b = 0; b < paramArrayOfString3.length; b++) {
/*  99 */         if (!this.foundSwitches.containsKey(paramArrayOfString3[b])) {
/* 100 */           throw new MissingSwitchException("Required switch \"" + paramArrayOfString3[b] + "\" not found.", paramArrayOfString3[b]);
/*     */         }
/*     */       } 
/*     */     }
/* 104 */     this.unswitchedArgs = new String[linkedList.size()];
/* 105 */     linkedList.toArray(this.unswitchedArgs);
/*     */   }
/*     */   
/*     */   public String getSwitchPrefix() {
/* 109 */     return this.switchPrefix;
/*     */   }
/*     */   public String[] getRawArgs() {
/* 112 */     return (String[])this.argv.clone();
/*     */   }
/*     */   public boolean includesSwitch(String paramString) {
/* 115 */     return this.foundSwitches.containsKey(paramString);
/*     */   }
/*     */   public String getSwitchArg(String paramString) {
/* 118 */     return (String)this.foundSwitches.get(paramString);
/*     */   }
/*     */   public String[] getUnswitchedArgs() {
/* 121 */     return (String[])this.unswitchedArgs.clone();
/*     */   }
/*     */   
/*     */   private static boolean contains(String paramString, String[] paramArrayOfString) {
/* 125 */     for (int i = paramArrayOfString.length; --i >= 0;) {
/* 126 */       if (paramArrayOfString[i].equals(paramString)) return true; 
/* 127 */     }  return false;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/cmdline/ParsedCommandLineImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */