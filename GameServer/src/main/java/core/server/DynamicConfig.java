package core.server;

import com.zhonglian.server.common.db.BM;
import core.database.game.bo.ServerConfigBO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamicConfig {
    private static DynamicConfig instance = new DynamicConfig();

    private Map<String, ServerConfigBO> confs = new HashMap<>();

    public static void init() {
        List<ServerConfigBO> bos = BM.getBM(ServerConfigBO.class).findAll();
        for (ServerConfigBO bo : bos) {
            instance.confs.put(bo.getKey(), bo);
        }
    }

    public static void set(String key, String value) {
        ServerConfigBO bo = instance.confs.get(key);
        if (bo == null) {
            bo = new ServerConfigBO();
            bo.setKey(key);
            bo.setValue(value);
            bo.insert();
            instance.confs.put(key, bo);
        }
        bo.saveValue(value);
    }

    public static String get(String key) {
        ServerConfigBO bo = instance.confs.get(key);
        if (bo == null) {
            return null;
        }
        return bo.getValue();
    }

    public static String get(String key, String defaultvalue) {
        ServerConfigBO bo = instance.confs.get(key);
        if (bo == null) {
            return defaultvalue;
        }
        return bo.getValue();
    }

    public static boolean get(String key, boolean defaultvalue) {
        ServerConfigBO bo = instance.confs.get(key);
        if (bo == null) {
            return defaultvalue;
        }
        return Boolean.valueOf(bo.getValue()).booleanValue();
    }

    public static int get(String key, int defaultvalue) {
        ServerConfigBO bo = instance.confs.get(key);
        if (bo == null) {
            return defaultvalue;
        }
        return Integer.valueOf(bo.getValue()).intValue();
    }

    public static Map<String, ServerConfigBO> getAllConfig() {
        return new HashMap<>(instance.confs);
    }
}

