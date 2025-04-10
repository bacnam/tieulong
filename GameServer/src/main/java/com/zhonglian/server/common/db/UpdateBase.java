package com.zhonglian.server.common.db;

import com.zhonglian.server.common.db.version.IUpdateDBVersion;

public class UpdateBase
implements IUpdateDBVersion
{
public String getRequestVersion() {
String name = getClass().getSimpleName();
String version = name.split("_To_")[0].replace("Update_", "");
return version.replace("_", ".");
}

public String getTargetVersion() {
String name = getClass().getSimpleName();
String version = name.split("_To_")[1];
return version.replace("_", ".");
}

public boolean run() {
return true;
}
}

