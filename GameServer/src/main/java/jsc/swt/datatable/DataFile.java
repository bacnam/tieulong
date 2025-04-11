package jsc.swt.datatable;

import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DataFile {
    public static final String EOL = "\r\n";
    public static final char FSDC = '"';
    public static final String FSDS = "\"";
    static final char FDD = '\t';

    static String classToString(Class paramClass) {
        if (paramClass == Double.class) return "Double";
        if (paramClass == Integer.class) return "Integer";
        return "String";
    }

    static Class stringToClass(String paramString) {
        if (paramString.equals("Double")) return Double.class;
        if (paramString.equals("Integer")) return Integer.class;
        return String.class;
    }

    public static DecimalFormat getIntegerFormat() {
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.UK);
        DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
        decimalFormat.applyPattern("##########");
        return decimalFormat;
    }

    public static DecimalFormat getRealFormat() {
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.UK);
        DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
        decimalFormat.applyPattern("0.###############E0");
        return decimalFormat;
    }

    public static DataMatrix read(File paramFile) {
        FileReader fileReader = null;

        try {
            int i, j;
            String str1, str2;
            fileReader = new FileReader(paramFile);

            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StreamTokenizer streamTokenizer = new StreamTokenizer(bufferedReader);
            streamTokenizer.eolIsSignificant(false);
            streamTokenizer.quoteChar(34);
            streamTokenizer.parseNumbers();
            streamTokenizer.slashStarComments(true);

            if (streamTokenizer.nextToken() == 34) {
                str1 = streamTokenizer.sval;
            } else {
                return null;
            }
            if (streamTokenizer.nextToken() == 34) {
                str2 = streamTokenizer.sval;
            } else {
                return null;
            }

            if (streamTokenizer.nextToken() == -2) {
                i = (int) streamTokenizer.nval;
            } else {
                return null;
            }

            if (streamTokenizer.nextToken() == -2) {
                j = (int) streamTokenizer.nval;
            } else {
                return null;
            }

            DataMatrix dataMatrix = new DataMatrix(i, j, str1, stringToClass(str2));

            DecimalFormat decimalFormat1 = getRealFormat();
            DecimalFormat decimalFormat2 = getIntegerFormat();

            ParsePosition parsePosition = new ParsePosition(0);
            for (byte b = 0; b < j; b++) {
                String str;

                if (streamTokenizer.nextToken() == 34) {
                    dataMatrix.setColumnName(b, streamTokenizer.sval);
                } else {
                    return null;
                }

                if (streamTokenizer.nextToken() == 34) {
                    str = streamTokenizer.sval;
                } else {
                    return null;
                }

                if (str.equals("Double")) {

                    dataMatrix.setColumnClass(b, Double.class);
                    while (true) {
                        int k;
                        if (streamTokenizer.nextToken() == -2) {
                            k = (int) streamTokenizer.nval;
                        } else {
                            streamTokenizer.pushBack();
                            break;
                        }

                        if (streamTokenizer.nextToken() == 34) {

                            parsePosition.setIndex(0);
                            Number number = decimalFormat1.parse(streamTokenizer.sval, parsePosition);
                            if (number == null) return null;
                            dataMatrix.setValueAt(new Double(number.doubleValue()), k, b);
                            continue;
                        }
                        return null;
                    }

                } else if (str.equals("Integer")) {

                    dataMatrix.setColumnClass(b, Integer.class);
                    while (true) {
                        int k;
                        if (streamTokenizer.nextToken() == -2) {
                            k = (int) streamTokenizer.nval;
                        } else {
                            streamTokenizer.pushBack();

                            break;
                        }

                        if (streamTokenizer.nextToken() == 34) {

                            parsePosition.setIndex(0);
                            Number number = decimalFormat2.parse(streamTokenizer.sval, parsePosition);
                            if (number == null) return null;
                            dataMatrix.setValueAt(new Integer(number.intValue()), k, b);
                            continue;
                        }
                        return null;
                    }

                } else {

                    dataMatrix.setColumnClass(b, String.class);
                    while (true) {
                        int k;
                        if (streamTokenizer.nextToken() == -2) {
                            k = (int) streamTokenizer.nval;
                        } else {
                            streamTokenizer.pushBack();
                            break;
                        }

                        if (streamTokenizer.nextToken() == 34) {
                            dataMatrix.setValueAt(streamTokenizer.sval, k, b);
                            continue;
                        }
                        return null;
                    }
                }
            }

            return dataMatrix;
        } catch (IOException iOException) {
        } finally {
            try {
                if (fileReader != null) fileReader.close();
            } catch (IOException iOException) {
            }
        }
        return null;
    }

    public static DataMatrix readTextFile(File paramFile, boolean paramBoolean, String paramString1, String paramString2) {
        FileReader fileReader = null;

        try {
            fileReader = new FileReader(paramFile);
            int i = (int) paramFile.length();
            char[] arrayOfChar = new char[i];
            int j = 0;
            while (j < i)
                j += fileReader.read(arrayOfChar, j, i - j);
            String str = new String(arrayOfChar);

            return new DataMatrix(str, paramBoolean, "\n\r", paramString1, paramString2);

        } catch (IOException iOException) {
        } finally {
            try {
                if (fileReader != null) fileReader.close();
            } catch (IOException iOException) {
            }
        }
        return null;
    }

    public static boolean write(File paramFile, DataTable paramDataTable) {
        int i = paramDataTable.getColumnCount();
        int j = paramDataTable.getRowCount();
        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(paramFile);
            DecimalFormat decimalFormat1 = getRealFormat();
            DecimalFormat decimalFormat2 = getIntegerFormat();

            Calendar calendar = Calendar.getInstance();
            Date date = calendar.getTime();
            String str = "" + "\r\n";
            fileWriter.write(str, 0, str.length());

            str = '"' + paramDataTable.getDefaultNamePrefix() + '"' + '\t' + '"' + classToString(paramDataTable.getDefaultColumnClass()) + '"' + "\r\n";

            fileWriter.write(str, 0, str.length());

            str = decimalFormat2.format(j) + '\t' + decimalFormat2.format(i) + "\r\n";
            fileWriter.write(str, 0, str.length());

            for (byte b = 0; b < i; b++) {

                Class clazz = paramDataTable.getColumnClass(b);
                String str1 = '"' + paramDataTable.getColumnName(b) + '"' + '\t' + '"' + classToString(clazz) + '"' + "\r\n";
                fileWriter.write(str1, 0, str1.length());

                if (clazz == Double.class) {

                    for (byte b1 = 0; b1 < j; b1++) {

                        Object object = paramDataTable.getValueAt(b1, b);
                        if (object instanceof Double) {
                            Double double_ = (Double) object;
                            str = decimalFormat2.format(b1) + '\t' + '"' + decimalFormat1.format(double_.doubleValue()) + '"' + "\r\n";
                            fileWriter.write(str, 0, str.length());
                        }

                    }
                } else if (clazz == Integer.class) {

                    for (byte b1 = 0; b1 < j; b1++) {
                        Object object = paramDataTable.getValueAt(b1, b);
                        if (object instanceof Integer) {
                            Integer integer = (Integer) object;
                            str = decimalFormat2.format(b1) + '\t' + '"' + decimalFormat2.format(integer.intValue()) + '"' + "\r\n";
                            fileWriter.write(str, 0, str.length());
                        }

                    }

                } else {

                    for (byte b1 = 0; b1 < j; b1++) {

                        str1 = (String) paramDataTable.getValueAt(b1, b);
                        if (str1 != null) {

                            str = decimalFormat2.format(b1) + '\t' + '"' + str1 + '"' + "\r\n";
                            fileWriter.write(str, 0, str.length());
                        }
                    }
                }
            }
            return true;
        } catch (IOException iOException) {
        } finally {
            try {
                if (fileWriter != null) fileWriter.close();
            } catch (IOException iOException) {
            }
        }
        return false;
    }

    public static boolean writeTextFile(File paramFile, DataTable paramDataTable, boolean paramBoolean, String paramString) {
        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(paramFile);
            StringBuffer stringBuffer = paramDataTable.getDataAsStringBuffer(true, paramBoolean, paramString);
            String str = stringBuffer.toString();
            fileWriter.write(str, 0, str.length());

            return true;
        } catch (IOException iOException) {
        } finally {
            try {
                if (fileWriter != null) fileWriter.close();
            } catch (IOException iOException) {
            }
        }
        return false;
    }
}

