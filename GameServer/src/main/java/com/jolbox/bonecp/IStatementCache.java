package com.jolbox.bonecp;

public interface IStatementCache {
  StatementHandle get(String paramString);

  int size();

  void clear();

  StatementHandle get(String paramString, int paramInt1, int paramInt2, int paramInt3);

  StatementHandle get(String paramString, int paramInt1, int paramInt2);

  StatementHandle get(String paramString, int paramInt);

  StatementHandle get(String paramString, int[] paramArrayOfint);

  StatementHandle get(String paramString, String[] paramArrayOfString);

  String calculateCacheKey(String paramString, String[] paramArrayOfString);

  String calculateCacheKey(String paramString, int[] paramArrayOfint);

  String calculateCacheKey(String paramString, int paramInt);

  String calculateCacheKey(String paramString, int paramInt1, int paramInt2);

  String calculateCacheKey(String paramString, int paramInt1, int paramInt2, int paramInt3);

  void checkForProperClosure();

  void putIfAbsent(String paramString, StatementHandle paramStatementHandle);
}

