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
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public abstract class IApp {
    public void init(String[] args) throws EvalError {
        String configfile = "conf";
        System.out.println("启动参数:" + Arrays.toString((Object[])args));
        if (args != null && args.length > 0) {
            configfile = args[0];
        }

        this.beforeInit(configfile);
        this.outputVersion();
        BaseServerInit.initBaseServer();
        if (!this.initBase()) {
            CommLog.error("初始化基础服务失败, 退出服务器启动");
            System.exit(-1);
        }

        if (!this.initLogic()) {
            CommLog.error("初始化逻辑组建失败, 退出服务器启动");
            System.exit(-1);
        }

        this.afterInit();
        if (!this.startNet()) {
            CommLog.error("启动网络通信失败，退出服务器启动");
            System.exit(-1);
        }

        CommLog.info("[服务器启动完毕]!");
    }

    protected abstract void beforeInit(String var1);

    protected abstract boolean initBase();

    protected abstract boolean startNet();

    protected abstract boolean initLogic();

    protected abstract void afterInit();

    protected abstract void start();

    protected void loadBasicConfig(String configdir, String logback) {
        String firstdir = configdir + "/first.properties";
        if (!CommProperties.load(firstdir, false)) {
            System.err.println("first配置文件[" + firstdir + "]读取失败，服务器退出!");
            System.exit(-1);
        }

        String logbackdir = configdir + File.separator + logback;
        System.setProperty("logback.configurationFile", logbackdir);
        System.setProperty("logback.configurationFile_bak", logbackdir);
        CommLog.initLog();
        Config.setStartTime(CommTime.nowSecond());
    }

    protected void loadRemoteConfig(String serverType, String serverId, String location, String configfile) {
        String url = System.getProperty("downConfUrl");
        if (url != null && !url.trim().isEmpty()) {
            url = "http://" + url + "/gm/gmNotice!loadConfig?" + serverType + "=" + serverId + "&" + "world_id" + "=" + Integer.getInteger("world_sid", 0);
            CommLog.info("从远端[{}]下载配置", url);

            try {
                JsonObject root = new JsonParser().parse(new String(HttpUtil.GetAll(url))).getAsJsonObject();
                String template = CommFile.bufferedReader(location + "/template/" + configfile);

                for (Entry<String, String> properties : this.toMapValues(root).entrySet()) {
                    template = template.replaceAll("\\{" + properties.getKey() + "\\}", properties.getValue());
                }

                CommFile.Write(location + "/" + configfile, template);
                CommLog.info("download and write config ok");
            } catch (Exception var10) {
                CommLog.error("写入下载来的配置文件失败 ,退出程序,url：[{}],文件:[{}] ", url, configfile, var10);
                System.exit(-1);
            }
        }

        CommProperties.load(location + "/" + configfile);
    }

    public Map<String, String> toMapValues(JsonObject root) {
        Map<String, String> map = new HashMap<>();

        for (Entry<String, JsonElement> element : root.entrySet()) {
            String name = element.getKey();
            JsonObject jsonObject = element.getValue().getAsJsonObject();

            for (Entry<String, JsonElement> property : jsonObject.entrySet()) {
                JsonElement value = property.getValue();
                if (value != null && !value.isJsonNull()) {
                    map.put(name + "\\." + property.getKey(), value.getAsString());
                }
            }
        }

        return map;
    }

    private void outputVersion() {
        String version = this.outputVersion("kernel", BaseServerInit.class);
        if (!version.equals(this.outputVersion("Common", IApp.class))) {
            CommLog.error("BaseServer的包和Common的Jar包不一致! 运维注意拷贝Server的jar包的时候要拷贝Lib包的内容");
            System.exit(-1);
        }

        if (!version.equals(this.outputVersion("Server", this.getClass()))) {
            CommLog.error("BaseServer的包和Server的Jar包不一致! 运维注意拷贝Server的jar包的时候要拷贝Lib包的内容");
            System.exit(-1);
        }

        CommLog.info("----------------version.txt--------------------");

        String[] var5;
        for (String line : var5 = version.split("\n")) {
            CommLog.info(line);
        }

        CommLog.info("-----------------------------------------------");
    }

    private String outputVersion(String jarPackage, Class<?> rootclass) {
        StringBuilder stringBuilder = new StringBuilder();

        try {
            try (InputStream res = rootclass.getResourceAsStream("version.txt")) {
                if (res != null) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(res));
                    String line = null;

                    while ((line = br.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }

                    return stringBuilder.toString();
                }
            }

            return "Release Only!";
        } catch (Exception var17) {
            CommLog.error("IApp.outputVersion", (Throwable)var17);
            return stringBuilder.toString();
        }
    }
}
