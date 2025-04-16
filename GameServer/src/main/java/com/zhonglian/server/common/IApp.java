package com.zhonglian.server.common;

import BaseCommon.CommLog;
import BaseServer.BaseServerInit;
import bsh.EvalError;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zhonglian.server.common.utils.CommFile;
import com.zhonglian.server.common.utils.CommProperties;
import com.zhonglian.server.common.utils.CommTime;
import com.zhonglian.server.http.client.HttpUtil;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class IApp {
    public void init(String[] args) throws EvalError {
        String configfile = "conf";
        System.out.println("启动参数:" + Arrays.toString((Object[]) args));
        if (args != null && args.length > 0) {
            configfile = args[0];
        }

        beforeInit(configfile);

        outputVersion();

        BaseServerInit.initBaseServer();
        if (!initBase()) {
            CommLog.error("初始化基础服务失败, 退出服务器启动");
            System.exit(-1);
        }
        if (!initLogic()) {
            CommLog.error("初始化逻辑组建失败, 退出服务器启动");
            System.exit(-1);
        }
        afterInit();

        if (!startNet()) {
            CommLog.error("启动网络通信失败，退出服务器启动");
            System.exit(-1);
        }
        CommLog.info("[服务器启动完毕]!");
    }

    protected abstract void beforeInit(String paramString);

    protected abstract boolean initBase();

    protected abstract boolean startNet();

    protected abstract boolean initLogic();

    protected abstract void afterInit();

    protected abstract void start();

    protected void loadBasicConfig(String configdir, String logback) {
        String firstdir = String.valueOf(configdir) + "/first.properties";
        if (!CommProperties.load(firstdir, false)) {
            System.err.println("first配置文件[" + firstdir + "]读取失败，服务器退出!");
            System.exit(-1);
        }

        String logbackdir = String.valueOf(configdir) + File.separator + logback;
        System.setProperty("logback.configurationFile", logbackdir);
        System.setProperty("logback.configurationFile_bak", logbackdir);
        CommLog.initLog();

        Config.setStartTime(CommTime.nowSecond());
    }

    protected void loadRemoteConfig(String serverType, String serverId, String location, String configfile) {
        String url = System.getProperty("downConfUrl");
        if (url != null && !url.trim().isEmpty()) {
            url = "http:";
            CommLog.info("从远端[{}]下载配置", url);
            try {
                JsonObject root = (new JsonParser()).parse(new String(HttpUtil.GetAll(url))).getAsJsonObject();
                String template = CommFile.bufferedReader(String.valueOf(location) + "/template/" + configfile);
                for (Map.Entry<String, String> properties : toMapValues(root).entrySet()) {
                    template = template.replaceAll("\\{" + (String) properties.getKey() + "\\}", properties.getValue());
                }
                CommFile.Write(String.valueOf(location) + "/" + configfile, template);
                CommLog.info("download and write config ok");
            } catch (Exception e) {
                CommLog.error("写入下载来的配置文件失败 ,退出程序,url：[{}],文件:[{}] ", new Object[]{url, configfile, e});
                System.exit(-1);
            }
        }

        CommProperties.load(String.valueOf(location) + "/" + configfile);
    }

    public Map<String, String> toMapValues(JsonObject root) {
        Map<String, String> map = new HashMap<>();
        for (Map.Entry<String, JsonElement> element : (Iterable<Map.Entry<String, JsonElement>>) root.entrySet()) {
            String name = element.getKey();
            JsonObject jsonObject = ((JsonElement) element.getValue()).getAsJsonObject();
            for (Map.Entry<String, JsonElement> property : (Iterable<Map.Entry<String, JsonElement>>) jsonObject.entrySet()) {
                JsonElement value = property.getValue();
                if (value != null && !value.isJsonNull()) {
                    map.put(String.valueOf(name) + "\\." + (String) property.getKey(), value.getAsString());
                }
            }
        }
        return map;
    }

    private void outputVersion() {
        String version = outputVersion("kernel", BaseServerInit.class);
        if (!version.equals(outputVersion("Common", IApp.class))) {
            CommLog.error("BaseServer的包和Common的Jar包不一致! 运维注意拷贝Server的jar包的时候要拷贝Lib包的内容");
            System.exit(-1);
        }
        if (!version.equals(outputVersion("Server", getClass()))) {
            CommLog.error("BaseServer的包和Server的Jar包不一致! 运维注意拷贝Server的jar包的时候要拷贝Lib包的内容");
            System.exit(-1);
        }
        CommLog.info("----------------version.txt--------------------");
        byte b;
        int i;
        String[] arrayOfString;
        for (i = (arrayOfString = version.split("\n")).length, b = 0; b < i; ) {
            String line = arrayOfString[b];
            CommLog.info(line);
            b++;
        }

        CommLog.info("-----------------------------------------------");
    }

    private String outputVersion(String jarPackage, Class<?> rootclass) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            Exception exception2, exception1 = null;

        } catch (Exception e) {
            CommLog.error("IApp.outputVersion", e);
        }
        return stringBuilder.toString();
    }
}

