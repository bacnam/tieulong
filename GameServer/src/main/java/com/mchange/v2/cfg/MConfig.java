package com.mchange.v2.cfg;

import com.mchange.v1.cachedstore.CachedStore;
import com.mchange.v1.cachedstore.CachedStoreException;
import com.mchange.v1.cachedstore.CachedStoreFactory;
import com.mchange.v1.cachedstore.CachedStoreUtils;
import com.mchange.v1.util.ArrayUtils;
import com.mchange.v2.log.MLevel;
import com.mchange.v2.log.MLog;
import com.mchange.v2.log.MLogger;

import java.util.*;

public final class MConfig {
    static final CachedStore cache = CachedStoreUtils.synchronizedCachedStore((CachedStore) CachedStoreFactory.createNoCleanupCachedStore(new CSManager()));
    private static final MLogger logger = MLog.getLogger(MConfig.class);
    private static final Map<DelayedLogItem.Level, MLevel> levelMap;

    static {
        try {
            HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
            for (DelayedLogItem.Level level : DelayedLogItem.Level.values())
                hashMap.put(level, MLevel.class.getField(level.toString()).get(null));
            levelMap = Collections.unmodifiableMap(hashMap);
        } catch (RuntimeException runtimeException) {

            runtimeException.printStackTrace();
            throw runtimeException;
        } catch (Exception exception) {

            exception.printStackTrace();
            throw new RuntimeException(exception);
        }
    }

    public static MultiPropertiesConfig readVmConfig(String[] paramArrayOfString1, String[] paramArrayOfString2) {
        try {
            return (MultiPropertiesConfig) cache.find(new PathsKey(paramArrayOfString1, paramArrayOfString2));
        } catch (CachedStoreException cachedStoreException) {
            throw new RuntimeException(cachedStoreException);
        }
    }

    public static MultiPropertiesConfig readVmConfig() {
        return readVmConfig(ConfigUtils.NO_PATHS, ConfigUtils.NO_PATHS);
    }

    public static MultiPropertiesConfig readConfig(String[] paramArrayOfString) {
        try {
            return (MultiPropertiesConfig) cache.find(new PathsKey(paramArrayOfString));
        } catch (CachedStoreException cachedStoreException) {
            throw new RuntimeException(cachedStoreException);
        }
    }

    public static MultiPropertiesConfig combine(MultiPropertiesConfig[] paramArrayOfMultiPropertiesConfig) {
        return ConfigUtils.combine(paramArrayOfMultiPropertiesConfig);
    }

    public static void dumpToLogger(List<DelayedLogItem> paramList, MLogger paramMLogger) {
        for (DelayedLogItem delayedLogItem : paramList) dumpToLogger(delayedLogItem, paramMLogger);
    }

    public static void dumpToLogger(DelayedLogItem paramDelayedLogItem, MLogger paramMLogger) {
        paramMLogger.log(levelMap.get(paramDelayedLogItem.getLevel()), paramDelayedLogItem.getText(), paramDelayedLogItem.getException());
    }

    private static final class PathsKey {
        String[] paths;
        List delayedLogItems;

        PathsKey(String[] param1ArrayOfString1, String[] param1ArrayOfString2) {
            this.delayedLogItems = new ArrayList();

            List list = ConfigUtils.vmCondensedPaths(param1ArrayOfString1, param1ArrayOfString2, this.delayedLogItems);
            this.paths = (String[]) list.toArray((Object[]) new String[list.size()]);
        }

        PathsKey(String[] param1ArrayOfString) {
            this.delayedLogItems = Collections.emptyList();
            this.paths = param1ArrayOfString;
        }

        public boolean equals(Object param1Object) {
            if (param1Object instanceof PathsKey) {
                return Arrays.equals((Object[]) this.paths, (Object[]) ((PathsKey) param1Object).paths);
            }
            return false;
        }

        public int hashCode() {
            return ArrayUtils.hashArray((Object[]) this.paths);
        }
    }

    private static class CSManager implements CachedStore.Manager {
        private CSManager() {
        }

        public boolean isDirty(Object param1Object1, Object param1Object2) throws Exception {
            return false;
        }

        public Object recreateFromKey(Object param1Object) throws Exception {
            MConfig.PathsKey pathsKey = (MConfig.PathsKey) param1Object;

            ArrayList<DelayedLogItem> arrayList = new ArrayList();
            arrayList.addAll(pathsKey.delayedLogItems);
            MultiPropertiesConfig multiPropertiesConfig = ConfigUtils.read(pathsKey.paths, arrayList);
            MConfig.dumpToLogger(arrayList, MConfig.logger);
            return multiPropertiesConfig;
        }
    }
}

