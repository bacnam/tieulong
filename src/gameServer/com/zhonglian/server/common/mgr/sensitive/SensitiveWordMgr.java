/*     */ package com.zhonglian.server.common.mgr.sensitive;
/*     */ 
/*     */ import BaseCommon.CommLog;
/*     */ import com.zhonglian.server.common.utils.CommFile;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SensitiveWordMgr
/*     */ {
/*  27 */   private Map sensitiveWordMap = new ConcurrentHashMap<>();
/*     */   
/*     */   private static final int minMatchTYpe = 1;
/*     */   
/*     */   private static SensitiveWordMgr _instance;
/*     */   
/*     */   public static SensitiveWordMgr getInstance() {
/*  34 */     if (_instance == null) {
/*  35 */       _instance = new SensitiveWordMgr();
/*     */     }
/*  37 */     return _instance;
/*     */   }
/*     */   
/*  40 */   String sensitiveWordFileName = "Keywords.txt";
/*     */   
/*     */   public void init(String filename) {
/*  43 */     this.sensitiveWordFileName = filename;
/*     */   }
/*     */   
/*     */   public void reload() {
/*  47 */     Set<String> words = CommFile.getLineSetsFromFile(this.sensitiveWordFileName);
/*     */     
/*  49 */     buildWordsHashMap(words);
/*     */     
/*  51 */     CommLog.debug("total {} sensitive words loaded, and build {} words", Integer.valueOf(words.size()), Integer.valueOf(this.sensitiveWordMap.size()));
/*     */   }
/*     */   
/*     */   public int getSensitiveWordCount() {
/*  55 */     return this.sensitiveWordMap.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public Set getSensitiveWords() {
/*  60 */     return this.sensitiveWordMap.keySet();
/*     */   }
/*     */ 
/*     */   
/*     */   private void buildWordsHashMap(Set<String> keyWordSet) {
/*  65 */     this.sensitiveWordMap = new ConcurrentHashMap<>(keyWordSet.size());
/*     */ 
/*     */ 
/*     */     
/*  69 */     for (String key : keyWordSet) {
/*  70 */       Map<Character, Map<String, String>> nowMap = this.sensitiveWordMap;
/*  71 */       for (int i = 0; i < key.length(); i++) {
/*  72 */         Map<String, String> map; char keyChar = key.charAt(i);
/*  73 */         Object wordMap = nowMap.get(Character.valueOf(keyChar));
/*     */         
/*  75 */         if (wordMap != null) {
/*  76 */           nowMap = (Map)wordMap;
/*     */         } else {
/*  78 */           Map<String, String> newWorMap = new ConcurrentHashMap<>();
/*  79 */           newWorMap.put("isEnd", "0");
/*  80 */           nowMap.put(Character.valueOf(keyChar), newWorMap);
/*  81 */           map = newWorMap;
/*     */         } 
/*     */         
/*  84 */         if (i == key.length() - 1) {
/*  85 */           map.put("isEnd", "1");
/*     */         }
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
/*     */   public boolean isContainsSensitiveWord(String txt) {
/* 100 */     return isContainsSensitiveWord(txt, 1);
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
/*     */   public boolean isContainsSensitiveWord(String txt, int matchType) {
/* 116 */     Pattern pName = Pattern.compile("[~`!@#$%\\^&\\*()-+_=<>?\\\\\\.:'\"{}\\[\\]]");
/* 117 */     Matcher matcherName = pName.matcher(txt);
/* 118 */     if (matcherName.find()) {
/* 119 */       return false;
/*     */     }
/*     */     
/* 122 */     boolean flag = false;
/* 123 */     for (int i = 0; i < txt.length(); i++) {
/* 124 */       int matchFlag = CheckSensitiveWord(txt, i, matchType);
/* 125 */       if (matchFlag > 0) {
/* 126 */         flag = true;
/*     */       }
/*     */     } 
/* 129 */     return flag;
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
/*     */   public Set<String> getSensitiveWord(String txt, int matchType) {
/* 145 */     Set<String> sensitiveWordList = new HashSet<>();
/*     */     
/* 147 */     for (int i = 0; i < txt.length(); i++) {
/* 148 */       int length = CheckSensitiveWord(txt, i, matchType);
/* 149 */       if (length > 0) {
/* 150 */         sensitiveWordList.add(txt.substring(i, i + length));
/* 151 */         i = i + length - 1;
/*     */       } 
/*     */     } 
/*     */     
/* 155 */     return sensitiveWordList;
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
/*     */   public String replaceSensitiveWord(String txt, int matchType, String replaceChar) {
/* 173 */     String resultTxt = txt;
/* 174 */     Set<String> set = getSensitiveWord(txt, matchType);
/* 175 */     Iterator<String> iterator = set.iterator();
/* 176 */     String word = null;
/* 177 */     String replaceString = null;
/* 178 */     while (iterator.hasNext()) {
/* 179 */       word = iterator.next();
/* 180 */       replaceString = getReplaceChars(replaceChar, word.length());
/* 181 */       resultTxt = resultTxt.replaceAll(word, replaceString);
/*     */     } 
/*     */     
/* 184 */     return resultTxt;
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
/*     */   private String getReplaceChars(String replaceChar, int length) {
/* 198 */     String resultReplace = replaceChar;
/* 199 */     for (int i = 1; i < length; i++) {
/* 200 */       resultReplace = String.valueOf(resultReplace) + replaceChar;
/*     */     }
/*     */     
/* 203 */     return resultReplace;
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
/*     */   public int CheckSensitiveWord(String txt, int beginIndex, int matchType) {
/* 219 */     boolean flag = false;
/* 220 */     int matchFlag = 0;
/* 221 */     char word = Character.MIN_VALUE;
/* 222 */     Map nowMap = this.sensitiveWordMap;
/* 223 */     for (int i = beginIndex; i < txt.length(); ) {
/* 224 */       word = txt.charAt(i);
/* 225 */       nowMap = (Map)nowMap.get(Character.valueOf(word));
/* 226 */       if (nowMap != null) {
/* 227 */         matchFlag++;
/* 228 */         if ("1".equals(nowMap.get("isEnd"))) {
/* 229 */           flag = true;
/* 230 */           if (1 == matchType) {
/*     */             break;
/*     */           }
/*     */         } 
/*     */         i++;
/*     */       } 
/*     */       break;
/*     */     } 
/* 238 */     if (matchFlag < 1 || !flag) {
/* 239 */       matchFlag = 0;
/*     */     }
/* 241 */     return matchFlag;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/mgr/sensitive/SensitiveWordMgr.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */