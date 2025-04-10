package com.zhonglian.server.common.db.version;

public interface IUpdateDBVersion {
  String getRequestVersion();

  String getTargetVersion();

  boolean run();
}

