package jsc;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.File;
import java.util.StringTokenizer;
import java.util.Vector;

public class Utilities {
    public static void beep() {
        Toolkit.getDefaultToolkit().beep();
    }

    public static String deleteChar(char paramChar, String paramString) {
        Character character = new Character(paramChar);
        return deleteChars(character.toString(), paramString);
    }

    public static String deleteChars(String paramString1, String paramString2) {
        StringTokenizer stringTokenizer = new StringTokenizer(paramString2, paramString1);
        StringBuffer stringBuffer = new StringBuffer(paramString2.length());
        for (; stringTokenizer.hasMoreTokens(); stringBuffer.append(stringTokenizer.nextToken())) ;
        return stringBuffer.toString();
    }

    public static int keyCodeToDigit(int paramInt) {
        switch (paramInt) {
            case 49:
                return 1;
            case 50:
                return 2;
            case 51:
                return 3;
            case 52:
                return 4;
            case 53:
                return 5;
            case 54:
                return 6;
            case 55:
                return 7;
            case 56:
                return 8;
            case 57:
                return 9;
            case 48:
                return 0;
        }
        return -1;
    }

    public static File getParentDirectory() {
        FileSystemView fileSystemView = FileSystemView.getFileSystemView();
        return fileSystemView.getParentDirectory(fileSystemView.getHomeDirectory());
    }

    public static char getSuperscriptChar(int paramInt) {
        switch (paramInt) {

            case 1:
                return '¹';
            case 2:
                return '²';
            case 3:
                return '³';
            case 4:
                return '⁴';
            case 5:
                return '⁵';
            case 6:
                return '⁶';
            case 7:
                return '⁷';
            case 8:
                return '⁸';
            case 9:
                return '⁹';
            case 0:
                return '⁰';
        }
        throw new IllegalArgumentException("Invalid superscript value " + paramInt);
    }

    public static File getUserDirectory() {
        return new File(System.getProperty("user.dir"));
    }

    public static boolean isVersionOK() {
        String str = System.getProperty("java.version");
        if (str.compareTo("1.4.0") < 0) {

            System.out.println("WARNING: This program was designed to run with a version 1.4.0 (or higher) Virtual Machine.");

            return false;
        }

        return true;
    }

    public static boolean isVersionOK(Component paramComponent) {
        String str = System.getProperty("java.version");
        if (str.compareTo("1.4.0") < 0) {

            JOptionPane.showMessageDialog(paramComponent, "WARNING: This program was designed to run with a version 1.4.0 (or higher) Virtual Machine.", "Java version warning", 2);

            return false;
        }

        return true;
    }

    public static int lengthOf(int paramInt) {
        return Integer.toString(paramInt).length();
    }

    public static void setNativeLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception exception) {
        }
    }

    public static void tile(Graphics paramGraphics, Component paramComponent, ImageIcon paramImageIcon) {
        Rectangle rectangle = paramComponent.getBounds();
        int i = paramImageIcon.getIconWidth();
        int j = paramImageIcon.getIconHeight();
        if (i <= 0 || j <= 0)
            return;
        for (int k = 0; k < rectangle.width; k += i) {
            for (int m = 0; m < rectangle.height; m += j) {
                paramImageIcon.paintIcon(paramComponent, paramGraphics, k, m);
            }
        }
    }

    public static String toString(String[] paramArrayOfString) {
        return toString(paramArrayOfString, ", ");
    }

    public static String toString(String[] paramArrayOfString, String paramString) {
        StringBuffer stringBuffer = new StringBuffer(100);
        byte b;
        for (b = 0; b < paramArrayOfString.length - 1; b++) {
            stringBuffer.append(paramArrayOfString[b]);
            stringBuffer.append(paramString);
        }
        stringBuffer.append(paramArrayOfString[b]);
        return stringBuffer.toString();
    }

    public static Vector toVector(String[] paramArrayOfString) {
        Vector vector = new Vector();
        for (byte b = 0; b < paramArrayOfString.length; ) {
            vector.add(paramArrayOfString[b]);
            b++;
        }
        return vector;
    }

    static class Test {
        public static void main(String[] param1ArrayOfString) {
            int[] arrayOfInt = {0, 0, 1, 2, -1, 9, 10, -10, 99, 100, -100, 101, 1000, 99999};
            for (byte b = 0; b < arrayOfInt.length; b++)
                System.out.println("Length of " + arrayOfInt[b] + " is " + Utilities.lengthOf(arrayOfInt[b]));
        }
    }
}

