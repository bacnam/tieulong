package core.server;

import com.zhonglian.server.common.db.BM;
import core.database.world.bo.ServerConfigBO;
import java.util.HashMap;
import java.util.Map;

public class DynamicConfig {
    private static DynamicConfig instance = new DynamicConfig();
    private Map<String, ServerConfigBO> confs = new HashMap<>();

    private DynamicConfig() {
    }

    public static void init() {
        for (ServerConfigBO bo : BM.getBM(ServerConfigBO.class).findAll()) {
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
        return bo == null ? null : bo.getValue();
    }

    public static String get(String key, String defaultvalue) {
        ServerConfigBO bo = instance.confs.get(key);
        return bo == null ? defaultvalue : bo.getValue();
    }

    public static boolean get(String key, boolean defaultvalue) {
        ServerConfigBO bo = instance.confs.get(key);
        return bo == null ? defaultvalue : Boolean.valueOf(bo.getValue());
    }

    public static int get(String key, int defaultvalue) {
        ServerConfigBO bo = instance.confs.get(key);
        return bo == null ? defaultvalue : Integer.valueOf(bo.getValue());
    }

    public static Map<String, ServerConfigBO> getAllConfig() {
        return new HashMap<>(instance.confs);
    }
}
