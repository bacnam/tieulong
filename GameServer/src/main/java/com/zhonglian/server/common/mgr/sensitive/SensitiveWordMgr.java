package com.zhonglian.server.common.mgr.sensitive;

import BaseCommon.CommLog;
import com.zhonglian.server.common.utils.CommFile;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SensitiveWordMgr
{
private Map sensitiveWordMap = new ConcurrentHashMap<>();

private static final int minMatchTYpe = 1;

private static SensitiveWordMgr _instance;

public static SensitiveWordMgr getInstance() {
if (_instance == null) {
_instance = new SensitiveWordMgr();
}
return _instance;
}

String sensitiveWordFileName = "Keywords.txt";

public void init(String filename) {
this.sensitiveWordFileName = filename;
}

public void reload() {
Set<String> words = CommFile.getLineSetsFromFile(this.sensitiveWordFileName);

buildWordsHashMap(words);

CommLog.debug("total {} sensitive words loaded, and build {} words", Integer.valueOf(words.size()), Integer.valueOf(this.sensitiveWordMap.size()));
}

public int getSensitiveWordCount() {
return this.sensitiveWordMap.size();
}

public Set getSensitiveWords() {
return this.sensitiveWordMap.keySet();
}

private void buildWordsHashMap(Set<String> keyWordSet) {
this.sensitiveWordMap = new ConcurrentHashMap<>(keyWordSet.size());

for (String key : keyWordSet) {
Map<Character, Map<String, String>> nowMap = this.sensitiveWordMap;
for (int i = 0; i < key.length(); i++) {
Map<String, String> map; char keyChar = key.charAt(i);
Object wordMap = nowMap.get(Character.valueOf(keyChar));

if (wordMap != null) {
nowMap = (Map)wordMap;
} else {
Map<String, String> newWorMap = new ConcurrentHashMap<>();
newWorMap.put("isEnd", "0");
nowMap.put(Character.valueOf(keyChar), newWorMap);
map = newWorMap;
} 

if (i == key.length() - 1) {
map.put("isEnd", "1");
}
} 
} 
}

public boolean isContainsSensitiveWord(String txt) {
return isContainsSensitiveWord(txt, 1);
}

public boolean isContainsSensitiveWord(String txt, int matchType) {
Pattern pName = Pattern.compile("[~`!@#$%\\^&\\*()-+_=<>?\\\\\\.:'\"{}\\[\\]]");
Matcher matcherName = pName.matcher(txt);
if (matcherName.find()) {
return false;
}

boolean flag = false;
for (int i = 0; i < txt.length(); i++) {
int matchFlag = CheckSensitiveWord(txt, i, matchType);
if (matchFlag > 0) {
flag = true;
}
} 
return flag;
}

public Set<String> getSensitiveWord(String txt, int matchType) {
Set<String> sensitiveWordList = new HashSet<>();

for (int i = 0; i < txt.length(); i++) {
int length = CheckSensitiveWord(txt, i, matchType);
if (length > 0) {
sensitiveWordList.add(txt.substring(i, i + length));
i = i + length - 1;
} 
} 

return sensitiveWordList;
}

public String replaceSensitiveWord(String txt, int matchType, String replaceChar) {
String resultTxt = txt;
Set<String> set = getSensitiveWord(txt, matchType);
Iterator<String> iterator = set.iterator();
String word = null;
String replaceString = null;
while (iterator.hasNext()) {
word = iterator.next();
replaceString = getReplaceChars(replaceChar, word.length());
resultTxt = resultTxt.replaceAll(word, replaceString);
} 

return resultTxt;
}

private String getReplaceChars(String replaceChar, int length) {
String resultReplace = replaceChar;
for (int i = 1; i < length; i++) {
resultReplace = String.valueOf(resultReplace) + replaceChar;
}

return resultReplace;
}

public int CheckSensitiveWord(String txt, int beginIndex, int matchType) {
boolean flag = false;
int matchFlag = 0;
char word = Character.MIN_VALUE;
Map nowMap = this.sensitiveWordMap;
for (int i = beginIndex; i < txt.length(); ) {
word = txt.charAt(i);
nowMap = (Map)nowMap.get(Character.valueOf(word));
if (nowMap != null) {
matchFlag++;
if ("1".equals(nowMap.get("isEnd"))) {
flag = true;
if (1 == matchType) {
break;
}
} 
i++;
} 
break;
} 
if (matchFlag < 1 || !flag) {
matchFlag = 0;
}
return matchFlag;
}
}

