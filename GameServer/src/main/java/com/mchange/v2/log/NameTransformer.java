package com.mchange.v2.log;

public interface NameTransformer {
  String transformName(String paramString);

  String transformName(Class paramClass);

  String transformName();
}

