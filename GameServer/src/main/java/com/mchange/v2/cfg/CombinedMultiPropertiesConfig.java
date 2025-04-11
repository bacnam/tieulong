package com.mchange.v2.cfg;

import java.util.*;

class CombinedMultiPropertiesConfig
        extends MultiPropertiesConfig {
    MultiPropertiesConfig[] configs;
    String[] resourcePaths;
    List parseMessages;

    CombinedMultiPropertiesConfig(MultiPropertiesConfig[] paramArrayOfMultiPropertiesConfig) {
        this.configs = paramArrayOfMultiPropertiesConfig;

        LinkedList<String> linkedList = new LinkedList();

        for (int i = paramArrayOfMultiPropertiesConfig.length - 1; i >= 0; i--) {

            String[] arrayOfString = paramArrayOfMultiPropertiesConfig[i].getPropertiesResourcePaths();
            for (int k = arrayOfString.length - 1; k >= 0; k--) {

                String str = arrayOfString[k];
                if (!linkedList.contains(str))
                    linkedList.add(0, str);
            }
        }
        this.resourcePaths = linkedList.<String>toArray(new String[linkedList.size()]);

        LinkedList<?> linkedList1 = new LinkedList();
        byte b;
        int j;
        for (b = 0, j = paramArrayOfMultiPropertiesConfig.length; b < j; b++)
            linkedList1.addAll(paramArrayOfMultiPropertiesConfig[b].getDelayedLogItems());
        this.parseMessages = Collections.unmodifiableList(linkedList1);
    }

    private Map getPropsByResourcePaths() {
        HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
        byte b;
        int i;
        for (b = 0, i = this.resourcePaths.length; b < i; b++) {

            String str = this.resourcePaths[b];
            hashMap.put(str, getPropertiesByResourcePath(str));
        }
        return Collections.unmodifiableMap(hashMap);
    }

    public BasicMultiPropertiesConfig toBasic() {
        String[] arrayOfString = getPropertiesResourcePaths();
        Map map = getPropsByResourcePaths();
        List list = getDelayedLogItems();

        return new BasicMultiPropertiesConfig(arrayOfString, map, list);
    }

    public String[] getPropertiesResourcePaths() {
        return (String[]) this.resourcePaths.clone();
    }

    public Properties getPropertiesByResourcePath(String paramString) {
        Properties properties = new Properties();
        byte b;
        int i;
        for (b = 0, i = this.configs.length; b < i; b++) {

            MultiPropertiesConfig multiPropertiesConfig = this.configs[b];
            Properties properties1 = multiPropertiesConfig.getPropertiesByResourcePath(paramString);
            if (properties1 != null) properties.putAll(properties1);
        }
        return (properties.size() > 0) ? properties : null;
    }

    public Properties getPropertiesByPrefix(String paramString) {
        LinkedList<Map.Entry<Object, Object>> linkedList = new LinkedList();
        for (int i = this.configs.length - 1; i >= 0; i--) {

            MultiPropertiesConfig multiPropertiesConfig = this.configs[i];
            Properties properties1 = multiPropertiesConfig.getPropertiesByPrefix(paramString);
            if (properties1 != null)
                linkedList.addAll(0, properties1.entrySet());
        }
        if (linkedList.size() == 0) {
            return null;
        }

        Properties properties = new Properties();
        for (Map.Entry<Object, Object> entry : linkedList) {

            properties.put(entry.getKey(), entry.getValue());
        }
        return properties;
    }

    public String getProperty(String paramString) {
        for (int i = this.configs.length - 1; i >= 0; i--) {

            MultiPropertiesConfig multiPropertiesConfig = this.configs[i];
            String str = multiPropertiesConfig.getProperty(paramString);
            if (str != null)
                return str;
        }
        return null;
    }

    public List getDelayedLogItems() {
        return this.parseMessages;
    }
}

