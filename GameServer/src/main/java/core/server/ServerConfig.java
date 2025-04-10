package core.server;

import BaseCommon.CommLog;
import com.zhonglian.server.common.Config;
import com.zhonglian.server.common.utils.CommString;
import com.zhonglian.server.common.utils.CommTime;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ServerConfig
extends Config
{
private static List<Integer> serverMergedIds = new ArrayList<>();
static {
serverMergedIds.addAll(CommString.getIntegerList(System.getProperty("SERVER_MIDList", ServerID()), ";"));
}

public static List<Integer> SERVER_MIDList() {
return serverMergedIds;
}

public static int AAY_AppId() {
return 10294;
}

public static String AAY_SecretKey() {
return System.getProperty("AAY_SecretKey", "2767c8cf315a4e8ebd50e5f9bb52fd3a");
}

private static int loginkey = CommTime.nowSecond();

public static int refreshLoginKey() {
return loginkey = CommTime.nowSecond();
}

public static int getLoginKey() {
return loginkey;
}

public static String BattleMapPath() {
return System.getProperty("GameServer.Maps");
}

public static void checkMapPath() {
File path = new File(BattleMapPath());
if (!path.exists() || !path.isDirectory()) {
CommLog.error("【【【Map文件夹地址(" + path.getAbsolutePath() + ")配置错误！！！】】】");
System.exit(-1);
} 
}
}

