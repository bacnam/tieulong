package com.mchange.v2.cfg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

final class ConfigUtils {
    static final String[] NO_PATHS = new String[0];
    private static final String[] DFLT_VM_RSRC_PATHFILES = new String[]{"/com/mchange/v2/cfg/vmConfigResourcePaths.txt", "/mchange-config-resource-paths.txt"};
    private static final String[] HARDCODED_DFLT_RSRC_PATHS = new String[]{"/mchange-commons.properties", "hocon:/reference,/application,/", "/"};
    static MultiPropertiesConfig vmConfig = null;

    static MultiPropertiesConfig read(String[] paramArrayOfString, List paramList) {
        return new BasicMultiPropertiesConfig(paramArrayOfString, paramList);
    }

    public static MultiPropertiesConfig read(String[] paramArrayOfString) {
        return new BasicMultiPropertiesConfig(paramArrayOfString);
    }

    public static MultiPropertiesConfig combine(MultiPropertiesConfig[] paramArrayOfMultiPropertiesConfig) {
        return (new CombinedMultiPropertiesConfig(paramArrayOfMultiPropertiesConfig)).toBasic();
    }

    public static MultiPropertiesConfig readVmConfig(String[] paramArrayOfString1, String[] paramArrayOfString2) {
        return readVmConfig(paramArrayOfString1, paramArrayOfString2, (List) null);
    }

    static List vmCondensedPaths(String[] paramArrayOfString1, String[] paramArrayOfString2, List paramList) {
        return condensePaths(new String[][]{paramArrayOfString1, vmResourcePaths(paramList), paramArrayOfString2});
    }

    static String stringFromPathsList(List paramList) {
        StringBuffer stringBuffer = new StringBuffer(2048);
        byte b;
        int i;
        for (b = 0, i = paramList.size(); b < i; b++) {

            if (b != 0) stringBuffer.append(", ");
            stringBuffer.append(paramList.get(b));
        }
        return stringBuffer.toString();
    }

    public static MultiPropertiesConfig readVmConfig(String[] paramArrayOfString1, String[] paramArrayOfString2, List<DelayedLogItem> paramList) {
        paramArrayOfString1 = (paramArrayOfString1 == null) ? NO_PATHS : paramArrayOfString1;
        paramArrayOfString2 = (paramArrayOfString2 == null) ? NO_PATHS : paramArrayOfString2;
        List list = vmCondensedPaths(paramArrayOfString1, paramArrayOfString2, paramList);

        if (paramList != null) {
            paramList.add(new DelayedLogItem(DelayedLogItem.Level.FINER, "Reading VM config for path list " + stringFromPathsList(list)));
        }
        return read((String[]) list.toArray((Object[]) new String[list.size()]), paramList);
    }

    private static List condensePaths(String[][] paramArrayOfString) {
        HashSet<String> hashSet = new HashSet();
        ArrayList<String> arrayList = new ArrayList();
        for (int i = paramArrayOfString.length; --i >= 0; ) {
            for (int j = (paramArrayOfString[i]).length; --j >= 0; ) {

                String str = paramArrayOfString[i][j];
                if (!hashSet.contains(str)) {

                    hashSet.add(str);
                    arrayList.add(str);
                }
            }
        }
        Collections.reverse(arrayList);
        return arrayList;
    }

    private static List readResourcePathsFromResourcePathsTextFile(String paramString, List<DelayedLogItem> paramList) {
        ArrayList<String> arrayList = new ArrayList();

        BufferedReader bufferedReader = null;

        try {
            InputStream inputStream = MultiPropertiesConfig.class.getResourceAsStream(paramString);
            if (inputStream != null) {

                bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "8859_1"));
                String str;
                while ((str = bufferedReader.readLine()) != null) {

                    str = str.trim();
                    if ("".equals(str) || str.startsWith("#")) {
                        continue;
                    }
                    arrayList.add(str);
                }

                if (paramList != null) {
                    paramList.add(new DelayedLogItem(DelayedLogItem.Level.FINEST, String.format("Added paths from resource path text file at '%s'", new Object[]{paramString})));
                }
            } else if (paramList != null) {
                paramList.add(new DelayedLogItem(DelayedLogItem.Level.FINEST, String.format("Could not find resource path text file for path '%s'. Skipping.", new Object[]{paramString})));
            }

        } catch (IOException iOException) {
            iOException.printStackTrace();
        } finally {

            try {
                if (bufferedReader != null) bufferedReader.close();
            } catch (IOException iOException) {
                iOException.printStackTrace();
            }

        }
        return arrayList;
    }

    private static List readResourcePathsFromResourcePathsTextFiles(String[] paramArrayOfString, List paramList) {
        ArrayList arrayList = new ArrayList();
        byte b;
        int i;
        for (b = 0, i = paramArrayOfString.length; b < i; b++)
            arrayList.addAll(readResourcePathsFromResourcePathsTextFile(paramArrayOfString[b], paramList));
        return arrayList;
    }

    private static String[] vmResourcePaths(List paramList) {
        List list = vmResourcePathList(paramList);
        return (String[]) list.toArray((Object[]) new String[list.size()]);
    }

    private static List vmResourcePathList(List paramList) {
        List<String> list1;
        List list = readResourcePathsFromResourcePathsTextFiles(DFLT_VM_RSRC_PATHFILES, paramList);

        if (list.size() > 0) {
            list1 = list;
        } else {
            list1 = Arrays.asList(HARDCODED_DFLT_RSRC_PATHS);
        }
        return list1;
    }

    public static synchronized MultiPropertiesConfig readVmConfig() {
        return readVmConfig((List) null);
    }

    public static synchronized MultiPropertiesConfig readVmConfig(List paramList) {
        if (vmConfig == null) {

            List list = vmResourcePathList(paramList);
            vmConfig = new BasicMultiPropertiesConfig((String[]) list.toArray((Object[]) new String[list.size()]));
        }
        return vmConfig;
    }

    public static synchronized boolean foundVmConfig() {
        return (vmConfig != null);
    }

    public static void dumpByPrefix(MultiPropertiesConfig paramMultiPropertiesConfig, String paramString) {
        Properties properties = paramMultiPropertiesConfig.getPropertiesByPrefix(paramString);
        TreeMap<Object, Object> treeMap = new TreeMap<Object, Object>();
        treeMap.putAll(properties);
        for (Map.Entry<Object, Object> entry : treeMap.entrySet()) {

            System.err.println((new StringBuilder()).append(entry.getKey()).append(" --> ").append(entry.getValue()).toString());
        }
    }
}

