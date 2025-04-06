package com.zhonglian.server.common.db.version;

public interface IUpdateDBVersion {
  String getRequestVersion();
  
  String getTargetVersion();
  
  boolean run();
}


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/zhonglian/server/common/db/version/IUpdateDBVersion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */