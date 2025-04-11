package jsc.swt.util;

import java.io.*;
import java.util.Properties;

public class PropertiesFile
        extends Properties {
    String fileName = "";

    public PropertiesFile() {
    }

    public PropertiesFile(String paramString) {
        this.fileName = paramString;
    }

    public PropertiesFile(String paramString, Properties paramProperties) {
        super(paramProperties);
        this.fileName = paramString;
    }

    public boolean getBoolProperty(String paramString) {
        return getBoolProperty(paramString, false);
    }

    public boolean getBoolProperty(String paramString, boolean paramBoolean) {
        String str = getProperty(paramString);
        if (str == null) return paramBoolean;
        return str.equals("TRUE");
    }

    public double getDoubleProperty(String paramString) {
        return getDoubleProperty(paramString, 0.0D);
    }

    public double getDoubleProperty(String paramString, double paramDouble) {
        String str = getProperty(paramString);
        try {
            return (new Double(str)).doubleValue();
        } catch (NumberFormatException numberFormatException) {
            return paramDouble;
        }

    }

    public int getIntProperty(String paramString) {
        return getIntProperty(paramString, 0);
    }

    public int getIntProperty(String paramString, int paramInt) {
        String str = getProperty(paramString);
        try {
            return (new Integer(str)).intValue();
        } catch (NumberFormatException numberFormatException) {
            return paramInt;
        }

    }

    public long getLongProperty(String paramString) {
        return getLongProperty(paramString, 0L);
    }

    public long getLongProperty(String paramString, long paramLong) {
        String str = getProperty(paramString);
        try {
            return (new Long(str)).longValue();
        } catch (NumberFormatException numberFormatException) {
            return paramLong;
        }

    }

    public boolean load() {
        try {
            DataInputStream dataInputStream = new DataInputStream(new FileInputStream(this.fileName));
            load(dataInputStream);
            dataInputStream.close();
            return true;
        } catch (IOException iOException) {

            return false;
        }
    }

    public boolean load(String paramString) {
        this.fileName = paramString;
        return load();
    }

    public boolean save(String paramString) {
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(this.fileName));
            store(dataOutputStream, paramString);
            dataOutputStream.close();
            return true;
        } catch (IOException iOException) {
            return false;
        }
    }

    public boolean save(String paramString1, String paramString2) {
        this.fileName = paramString1;
        return save(paramString2);
    }

    public void setProperty(String paramString, boolean paramBoolean) {
        String str = "FALSE";
        if (paramBoolean) str = "TRUE";
        setProperty(paramString, str);
    }

    public void setProperty(String paramString, int paramInt) {
        setProperty(paramString, Integer.toString(paramInt));
    }

    public void setProperty(String paramString, long paramLong) {
        setProperty(paramString, Long.toString(paramLong));
    }

    public void setProperty(String paramString, double paramDouble) {
        setProperty(paramString, Double.toString(paramDouble));
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            PropertiesFile propertiesFile1 = new PropertiesFile();
            propertiesFile1.setProperty("Boolean", true);
            propertiesFile1.setProperty("Int", -1);
            propertiesFile1.setProperty("Long", -1);
            propertiesFile1.setProperty("Double", Math.E);
            PropertiesFile propertiesFile2 = new PropertiesFile("C:\\WINDOWS\\TEST.PRO", propertiesFile1);
            propertiesFile2.load();
            System.out.println(propertiesFile2.getBoolProperty("Boolean"));
            System.out.println(propertiesFile2.getIntProperty("Int"));
            System.out.println(propertiesFile2.getLongProperty("Long"));
            System.out.println(propertiesFile2.getDoubleProperty("Double"));
            propertiesFile2.setProperty("Boolean", true);
            propertiesFile2.setProperty("Int", 42);
            propertiesFile2.setProperty("Long", 666);
            propertiesFile2.setProperty("Double", Math.PI);
            propertiesFile2.save("Test properties");
        }
    }
}

