package com.mchange.v2.cfg;

import com.mchange.v3.hocon.HoconPropertiesConfigSource;

import java.io.FileNotFoundException;
import java.util.*;

final class BasicMultiPropertiesConfig
        extends MultiPropertiesConfig {
    static final BasicMultiPropertiesConfig EMPTY = new BasicMultiPropertiesConfig();
    private static final String HOCON_CFG_CNAME = "com.typesafe.config.Config";
    private static final int HOCON_PFX_LEN = 6;
    String[] rps;
    Map propsByResourcePaths;
    Map propsByPrefixes;
    List parseMessages;
    Properties propsByKey;

    public BasicMultiPropertiesConfig(String[] paramArrayOfString) {
        this(paramArrayOfString, (List) null);
    }

    BasicMultiPropertiesConfig(String[] paramArrayOfString, List paramList) {
        firstInit(paramArrayOfString, paramList);
        finishInit(paramList);
    }

    public BasicMultiPropertiesConfig(String paramString, Properties paramProperties) {
        this(new String[]{paramString}, resourcePathToPropertiesMap(paramString, paramProperties), Collections.emptyList());
    }

    BasicMultiPropertiesConfig(String[] paramArrayOfString, Map paramMap, List paramList) {
        this.rps = paramArrayOfString;
        this.propsByResourcePaths = paramMap;

        ArrayList arrayList = new ArrayList();
        arrayList.addAll(paramList);
        finishInit(arrayList);

        this.parseMessages = arrayList;
    }

    private BasicMultiPropertiesConfig() {
        this.rps = new String[0];
        Map<?, ?> map1 = Collections.emptyMap();
        Map<?, ?> map2 = Collections.emptyMap();

        List<?> list = Collections.emptyList();

        Properties properties = new Properties();
    }

    static boolean isHoconPath(String paramString) {
        return (paramString.length() > 6 && paramString.substring(0, 6).toLowerCase().equals("hocon:"));
    }

    private static PropertiesConfigSource configSource(String paramString) throws Exception {
        boolean bool = isHoconPath(paramString);

        if (!bool && !paramString.startsWith("/")) {
            throw new IllegalArgumentException(String.format("Resource identifier '%s' is neither an absolute resource path nor a HOCON path. (Resource paths should be specified beginning with '/' or 'hocon:/')", new Object[]{paramString}));
        }
        if (bool) {

            try {

                Class.forName("com.typesafe.config.Config");
                return (PropertiesConfigSource) new HoconPropertiesConfigSource();
            } catch (ClassNotFoundException classNotFoundException) {

                int i = paramString.lastIndexOf('#');
                String str = (i > 0) ? paramString.substring(6, i) : paramString.substring(6);
                if (BasicMultiPropertiesConfig.class.getResource(str) == null) {
                    throw new FileNotFoundException(String.format("HOCON lib (typesafe-config) is not available. Also, no resource available at '%s' for HOCON identifier '%s'.", new Object[]{str, paramString}));
                }
                throw new Exception(String.format("Could not decode HOCON resource '%s', even though the resource exists, because HOCON lib (typesafe-config) is not available.", new Object[]{paramString}), classNotFoundException);
            }
        }
        if ("/".equals(paramString)) {
            return new SystemPropertiesConfigSource();
        }
        return new BasicPropertiesConfigSource();
    }

    private static Map resourcePathToPropertiesMap(String paramString, Properties paramProperties) {
        HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
        hashMap.put(paramString, paramProperties);
        return hashMap;
    }

    private static void dumpToSysErr(List paramList) {
        for (Object object : paramList) {
            System.err.println(object);
        }
    }

    private static String extractPrefix(String paramString) {
        int i = paramString.lastIndexOf('.');
        if (i < 0) {

            if ("".equals(paramString)) {
                return null;
            }
            return "";
        }

        return paramString.substring(0, i);
    }

    private static Properties findProps(String paramString, Map paramMap) {
        return (Properties) paramMap.get(paramString);
    }

    private static Properties extractPropsByKey(String[] paramArrayOfString, Map paramMap, List<DelayedLogItem> paramList) {
        Properties properties = new Properties();
        byte b;
        int i;
        for (b = 0, i = paramArrayOfString.length; b < i; b++) {

            String str = paramArrayOfString[b];
            Properties properties1 = findProps(str, paramMap);
            if (properties1 == null) {

                paramList.add(new DelayedLogItem(DelayedLogItem.Level.WARNING, BasicMultiPropertiesConfig.class.getName() + ".extractPropsByKey(): Could not find loaded properties for resource path: " + str));
            } else {

                for (String str1 : properties1.keySet()) {

                    if (!(str1 instanceof String)) {

                        String str4 = BasicMultiPropertiesConfig.class.getName() + ": " + "Properties object found at resource path " + ("/".equals(str) ? "[system properties]" : ("'" + str + "'")) + "' contains a key that is not a String: " + str1 + "; Skipping...";

                        paramList.add(new DelayedLogItem(DelayedLogItem.Level.WARNING, str4));
                        continue;
                    }
                    Object object = properties1.get(str1);
                    if (object != null && !(object instanceof String)) {

                        String str4 = BasicMultiPropertiesConfig.class.getName() + ": " + "Properties object found at resource path " + ("/".equals(str) ? "[system properties]" : ("'" + str + "'")) + " contains a value that is not a String: " + object + "; Skipping...";

                        paramList.add(new DelayedLogItem(DelayedLogItem.Level.WARNING, str4));

                        continue;
                    }
                    String str2 = str1;
                    String str3 = (String) object;
                    properties.put(str2, str3);
                }
            }
        }
        return properties;
    }

    private static Map extractPrefixMapFromRsrcPathMap(String[] paramArrayOfString, Map paramMap, List<DelayedLogItem> paramList) {
        HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
        byte b;
        int i;
        for (b = 0, i = paramArrayOfString.length; b < i; b++) {

            String str = paramArrayOfString[b];
            Properties properties = findProps(str, paramMap);
            if (properties == null) {

                String str1 = BasicMultiPropertiesConfig.class.getName() + ".extractPrefixMapFromRsrcPathMap(): Could not find loaded properties for resource path: " + str;

                paramList.add(new DelayedLogItem(DelayedLogItem.Level.WARNING, str1));
            } else {

                for (String str1 : properties.keySet()) {

                    if (!(str1 instanceof String)) {

                        String str4 = BasicMultiPropertiesConfig.class.getName() + ": " + "Properties object found at resource path " + ("/".equals(str) ? "[system properties]" : ("'" + str + "'")) + "' contains a key that is not a String: " + str1 + "; Skipping...";

                        paramList.add(new DelayedLogItem(DelayedLogItem.Level.WARNING, str4));

                        continue;
                    }
                    String str2 = str1;
                    String str3 = extractPrefix(str2);
                    while (str3 != null) {

                        Properties properties1 = (Properties) hashMap.get(str3);
                        if (properties1 == null) {

                            properties1 = new Properties();
                            hashMap.put(str3, properties1);
                        }
                        properties1.put(str2, properties.get(str2));

                        str3 = extractPrefix(str3);
                    }
                }
            }
        }
        return hashMap;
    }

    private void firstInit(String[] paramArrayOfString, List<DelayedLogItem> paramList) {
        boolean bool = false;
        if (paramList == null) {

            paramList = new ArrayList();
            bool = true;
        }

        HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
        ArrayList<String> arrayList = new ArrayList();
        byte b;
        int i;
        for (b = 0, i = paramArrayOfString.length; b < i; b++) {

            String str = paramArrayOfString[b];

            try {
                PropertiesConfigSource propertiesConfigSource = configSource(str);
                PropertiesConfigSource.Parse parse = propertiesConfigSource.propertiesFromSource(str);
                hashMap.put(str, parse.getProperties());
                arrayList.add(str);
                paramList.addAll(parse.getDelayedLogItems());
            } catch (FileNotFoundException fileNotFoundException) {
                paramList.add(new DelayedLogItem(DelayedLogItem.Level.FINE, String.format("The configuration file for resource identifier '%s' could not be found. Skipping.", new Object[]{str}), fileNotFoundException));
            } catch (Exception exception) {
                paramList.add(new DelayedLogItem(DelayedLogItem.Level.WARNING, String.format("An Exception occurred while trying to read configuration data at resource identifier '%s'.", new Object[]{str}), exception));
            }
        }
        this.rps = arrayList.<String>toArray(new String[arrayList.size()]);
        this.propsByResourcePaths = Collections.unmodifiableMap(hashMap);
        this.parseMessages = Collections.unmodifiableList(paramList);

        if (bool) {
            dumpToSysErr(paramList);
        }
    }

    private void finishInit(List paramList) {
        boolean bool = false;
        if (paramList == null) {

            paramList = new ArrayList();
            bool = true;
        }

        this.propsByPrefixes = Collections.unmodifiableMap(extractPrefixMapFromRsrcPathMap(this.rps, this.propsByResourcePaths, paramList));
        this.propsByKey = extractPropsByKey(this.rps, this.propsByResourcePaths, paramList);

        if (bool)
            dumpToSysErr(paramList);
    }

    public List getDelayedLogItems() {
        return this.parseMessages;
    }

    public String[] getPropertiesResourcePaths() {
        return (String[]) this.rps.clone();
    }

    public Properties getPropertiesByResourcePath(String paramString) {
        Properties properties = (Properties) this.propsByResourcePaths.get(paramString);
        return (properties == null) ? new Properties() : properties;
    }

    public Properties getPropertiesByPrefix(String paramString) {
        Properties properties = (Properties) this.propsByPrefixes.get(paramString);
        return (properties == null) ? new Properties() : properties;
    }

    public String getProperty(String paramString) {
        return this.propsByKey.getProperty(paramString);
    }

    public String dump() {
        return String.format("[ propertiesByResourcePaths -> %s, propertiesByPrefixes -> %s ]", new Object[]{this.propsByResourcePaths, this.propsByPrefixes});
    }

    public String toString() {
        return super.toString() + " " + dump();
    }

    static final class SystemPropertiesConfigSource implements PropertiesConfigSource {
        public PropertiesConfigSource.Parse propertiesFromSource(String param1String) throws FileNotFoundException, Exception {
            if ("/".equals(param1String)) {
                return new PropertiesConfigSource.Parse((Properties) System.getProperties().clone(), Collections.emptyList());
            }
            throw new Exception(String.format("Unexpected identifier for System properties: '%s'", new Object[]{param1String}));
        }
    }
}

