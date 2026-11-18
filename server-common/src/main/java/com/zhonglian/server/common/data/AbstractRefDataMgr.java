package com.zhonglian.server.common.data;

import BaseCommon.CommClass;
import BaseCommon.CommLog;
import com.zhonglian.server.common.data.ref.RefBase;
import com.zhonglian.server.common.data.ref.RefFactor;
import com.zhonglian.server.common.data.ref.RefGeneral;
import com.zhonglian.server.common.data.ref.matcher.NumberRange;
import com.zhonglian.server.common.utils.CommFile;
import com.zhonglian.server.common.utils.CommTime;
import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractRefDataMgr {
    private static AbstractRefDataMgr instance = null;
    protected Map<Class<? extends RefBase>, RefContainer<?>> refs = new ConcurrentHashMap<>();
    private List<String> dulError = new ArrayList<>();
    private boolean assertPassed = false;

    protected AbstractRefDataMgr() {
        instance = this;
    }

    public static AbstractRefDataMgr getInstance() {
        return instance;
    }

    public static <T extends RefBase> T get(Class<T> clazz, Object key) {
        T ret = null;
        if (instance.refs.containsKey(clazz)) {
            ret = (T)instance.refs.get(clazz).get(key);
        }

        if (ret == null) {
            CommLog.warn("BaseRefDataMgr get clazz:{}, key:{} failed!!", clazz.getSimpleName(), key.toString());
        }

        return ret;
    }

    public static <T extends RefBase> T getOrLast(Class<T> clazz, Object key) {
        T ret = null;
        if (instance.refs.containsKey(clazz)) {
            ret = (T)instance.refs.get(clazz).get(key);
        }

        return ret == null ? getAll(clazz).last() : ret;
    }

    public static <T extends RefBase> RefContainer<T> getAll(Class<T> clazz) {
        if (!instance.refs.containsKey(clazz)) {
            instance.refs.put(clazz, new RefContainer());
        }

        return (RefContainer<T>)instance.refs.get(clazz);
    }

    public static <T extends RefBase> int size(Class<T> clazz) {
        if (!instance.refs.containsKey(clazz)) {
            instance.refs.put(clazz, new RefContainer());
        }

        return instance.refs.get(clazz).size();
    }

    public static int getFactor(String name) {
        try {
            RefFactor ref = get(RefFactor.class, name);
            return ref != null ? (int)ref.Value : 0;
        } catch (Exception var2) {
            return 0;
        }
    }

    public static int getFactor(String name, int defaultValue) {
        try {
            RefFactor ref = get(RefFactor.class, name);
            return ref != null ? (int)ref.Value : defaultValue;
        } catch (Exception var3) {
            return defaultValue;
        }
    }

    public static double getFactor(String name, double defaultValue) {
        try {
            RefFactor ref = get(RefFactor.class, name);
            return ref != null ? ref.Value : defaultValue;
        } catch (Exception var4) {
            return defaultValue;
        }
    }

    public static String getGeneral(String name, String defaultValue) {
        try {
            RefGeneral ref = get(RefGeneral.class, name);
            return ref != null ? ref.Value : defaultValue;
        } catch (Exception var3) {
            return defaultValue;
        }
    }

    public static List<String> getGeneral(String name) {
        List<String> list = new ArrayList<>();
        RefGeneral ref = get(RefGeneral.class, name);

        try {
            if (ref != null) {
                String[] lists = ref.Value.split(";");
                return Arrays.asList(lists);
            } else {
                return list;
            }
        } catch (Exception var4) {
            if (ref == null) {
                CommLog.error("getGeneral name:{}, null", name);
            } else {
                CommLog.error("getGeneral name:{}, value:{} parse List failed", name, ref.Value);
            }

            return list;
        }
    }

    public void reload() {
        CommLog.info("=======开始加载配表");
        this.dulError.clear();
        this.checkDirects();
        this.renameRefFilesToLowcase();
        this.load(RefBase.class);
        this.onCustomLoad();
        this.assertPassed = this.assertRefs();
        CommLog.info("=======配表加载结束");
    }

    public boolean assertRefs() {
        long begin = CommTime.nowMS();
        CommLog.info("进行配表检测...");
        boolean rtn = true;

        for (Entry<Class<? extends RefBase>, RefContainer<?>> pair : this.refs.entrySet()) {
            for (RefBase ref : pair.getValue().values()) {
                try {
                    if (!ref.Assert()) {
                        CommLog.error("[{}] 配表自校验发现问题:{}", ref.getClass().getSimpleName(), CommClass.getClassPropertyInfos(ref));
                        rtn = false;
                    }
                } catch (Throwable var11) {
                    CommLog.error("[{}] 配表自校验发现问题:{},异常:", ref.getClass().getSimpleName(), CommClass.getClassPropertyInfos(ref), var11);
                    rtn = false;
                }
            }

            try {
                RefBase base = pair.getKey().newInstance();
                if (!base.AssertAll(pair.getValue())) {
                    CommLog.error("[{}] 配表全局校验发现问题", pair.getKey().getSimpleName());
                    rtn = false;
                }
            } catch (Throwable var10) {
                CommLog.error("[{}] 配表全局校验发现问题, 异常:", pair.getKey().getSimpleName(), var10);
                rtn = false;
            }
        }

        try {
            if (!this.assertAll()) {
                CommLog.error("配表总检不通过");
                rtn = false;
            }
        } catch (Throwable var9) {
            CommLog.error("refdata自校验不通过", var9);
            rtn = false;
        }

        CommLog.info("配表检测完毕，用时:{}ms", CommTime.nowMS() - begin);
        return rtn;
    }

    protected abstract boolean assertAll();

    public boolean isAssertPassed() {
        return this.assertPassed;
    }

    public <T extends RefBase> void load(Class<T> clazz) {
        for (Class<?> cs : CommClass.getAllClassByInterface(clazz, clazz.getPackage().getName())) {
            RefBase refdata = null;

            try {
                refdata = (RefBase)CommClass.forName(cs.getName()).newInstance();
            } catch (Exception var7) {
                CommLog.error("onAutoLoad occured error:{}", var7.getMessage());
            }

            if (refdata != null) {
                RefContainer<?> data = this.refs.get(cs);
                if (data == null) {
                    data = new RefContainer();
                    this.refs.put((Class<? extends RefBase>)refdata.getClass(), data);
                }

                this.loadByClazz((Class<? extends RefBase>)refdata.getClass(), data);
            }
        }
    }

    protected abstract void onCustomLoad();

    public abstract String getRefPath();

    public boolean checkDirects() {
        File path = new File(this.getRefPath());
        if (!path.exists() || !path.isDirectory()) {
            CommLog.error("【【【Refdata文件夹地址(" + path.getAbsolutePath() + ")配置错误！！！】】】");
            System.exit(-1);
        }

        return true;
    }

    public boolean renameRefFilesToLowcase() {
        File path = new File(this.getRefPath());
        File[] files = path.listFiles();

        for (File refFile : files) {
            if (refFile.isFile() && refFile.getName().toLowerCase().endsWith(".txt")) {
                File lowcaseFile = new File(refFile.getParentFile(), refFile.getName().toLowerCase());
                if (!lowcaseFile.exists()) {
                    refFile.renameTo(lowcaseFile);
                }
            }
        }

        return true;
    }

    public <T extends RefBase> void loadByClazz(Class<? extends RefBase> clazz, RefContainer<T> data) {
        String name = clazz.getSimpleName();
        String path = String.format("%s%c%s.txt", this.getRefPath(), File.separatorChar, name.substring(3).toLowerCase());
        Map<String, Map<String, String>> table = CommFile.GetTable(path, 2, 3);
        Field keysField = null;

        Object keyvalue;
        for (Field f : clazz.getFields()) {
            RefField annotation = f.getAnnotation(RefField.class);
            if (annotation != null && annotation.iskey()) {
                if (keysField != null) {
                    CommLog.error("[{}]配置了多个键值({},{})", clazz.getSimpleName(), f.getName(), keysField.getName());
                    System.exit(1);
                } else {
                    keysField = f;
                }
            }
        }

        if (keysField == null) {
            CommLog.error("[{}]未配置键值", clazz.getSimpleName());
            System.exit(1);
        }

        for (Map<String, String> lineValue : table.values()) {
            String keysrtvalue = lineValue.get(keysField.getName());
            if (keysrtvalue == null) {
                CommLog.error("[{}]txt配表上缺少键值[{}]的值配置", clazz.getName(), keysField.getName());
                System.exit(1);
            }

            keyvalue = this.parseCreateObj(keysField.getType(), keysrtvalue, clazz.getSimpleName(), keysField.getName());
            if (keyvalue == null) {
                CommLog.error("[{}]txt配表上缺少键值[{}]值[{}]解析失败", clazz.getName(), keysField.getName(), keysrtvalue);
            } else {
                T ref = (T)data.get(keyvalue);
                if (ref == null) {
                    try {
                        ref = (T)clazz.newInstance();
                    } catch (Exception var14) {
                        CommLog.error("[{}]创建实例失败", (Throwable)var14);
                        System.exit(1);
                    }

                    data.put(keyvalue, ref);
                }

                try {
                    this.loadLineValue(ref, lineValue);
                } catch (Exception var13) {
                    CommLog.error("[{}]解析配置时发生错误", clazz.getName(), var13);
                }
            }
        }
    }

    public <T extends RefBase> void loadLineValue(T obj, Map<String, String> lineValue) throws Exception {
        String parsingTable = obj.getClass().getSimpleName();
        Field[] fields = obj.getClass().getDeclaredFields();

        for (Field field : fields) {
            int modifiers = field.getModifiers();
            if (!Modifier.isStatic(modifiers) && !Modifier.isFinal(modifiers)) {
                RefField annotation = field.getAnnotation(RefField.class);
                if (annotation == null || annotation.isfield()) {
                    String varName = field.getName();
                    String varValue = lineValue.get(varName);
                    Class<?> typeClass = field.getType();
                    Object fieldValue;
                    if (List.class.isAssignableFrom(typeClass)) {
                        List<Object> listValue = null;
                        if (typeClass.isAssignableFrom(List.class)) {
                            listValue = new ArrayList<>();
                        } else {
                            listValue = (List<Object>)typeClass.newInstance();
                        }

                        if (varValue != null && !varValue.equals("")) {
                            Type gt = field.getGenericType();
                            ParameterizedType pt = (ParameterizedType)gt;
                            Class<?> clazz = (Class<?>)pt.getActualTypeArguments()[0];
                            String[] valueList = varValue.split(";");
                            int length = valueList.length;

                            for (int index = 0; index < length; index++) {
                                listValue.add(this.parseCreateObj(clazz, valueList[index], parsingTable, varName));
                            }
                        }

                        fieldValue = listValue;
                    } else if (typeClass.isArray()) {
                        Class<Object[]> typeListClass = (Class<Object[]>)field.getType();
                        Class<Object> subTypeClass = (Class<Object>)typeClass.getComponentType();
                        Object[] listValuex = (Object[])Array.newInstance(subTypeClass, 0);
                        if (varValue != null && !varValue.equals("")) {
                            String[] valueList = varValue.split(";");
                            int length = valueList.length;
                            listValuex = (Object[])Array.newInstance(subTypeClass, length);

                            for (int index = 0; index < length; index++) {
                                listValuex[index] = this.parseCreateObj(typeListClass.getComponentType(), valueList[index], parsingTable, varName);
                            }
                        }

                        fieldValue = listValuex;
                    } else if (varValue != null) {
                        fieldValue = this.parseCreateObj(typeClass, varValue, parsingTable, varName);
                    } else {
                        fieldValue = null;
                    }

                    if (fieldValue != null) {
                        field.set(obj, fieldValue);
                    }
                }
            }
        }
    }

    private Object parseCreateObj(Class<?> field, String value, String tbName, String fieldName) {
        try {
            if (field == int.class || field == Integer.class) {
                int index = value.indexOf(".");
                if (-1 != index) {
                    value = value.substring(0, index);
                }

                if (value.equals("")) {
                    return 0;
                }

                int ret = Integer.valueOf(value);
                return ret;
            }

            if (field == long.class || field == Long.class) {
                if (value.equals("")) {
                    return 0L;
                }

                return Long.valueOf(value);
            }

            if (field == float.class || field == Float.class) {
                if (value.equals("")) {
                    return 0.0F;
                }

                return Float.valueOf(value);
            }

            if (field == double.class || field == Double.class) {
                if (value.equals("")) {
                    return 0.0F;
                }

                return Double.valueOf(value);
            }

            if (field == boolean.class || field == Boolean.class) {
                try {
                    int v = Integer.valueOf(value);
                    if (v != 0) {
                        return true;
                    }

                    return false;
                } catch (Exception var7) {
                    return Boolean.valueOf(value);
                }
            }

            if (field == NumberRange.class) {
                return NumberRange.parse(value);
            }

            if (field == String.class) {
                return value;
            }

            if (field.isEnum()) {
                if (value.isEmpty()) {
                    value = "None";
                }

                return Enum.valueOf((Class) field, value);
            }
        } catch (Exception var8) {
            this.log("BaseRefDataMgr 解析值失败 [%s] field:[%s] type:[%s] value:[%s] ", tbName, fieldName, field.getSimpleName(), value);
        }

        return null;
    }

    private void log(String format, Object... param) {
        String err = String.format(format, param);
        if (!this.dulError.contains(err)) {
            CommLog.error(err);
            this.dulError.add(err);
        }
    }
}
