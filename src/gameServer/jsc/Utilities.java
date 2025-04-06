/*     */ package jsc;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Toolkit;
/*     */ import java.io.File;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.Vector;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.filechooser.FileSystemView;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Utilities
/*     */ {
/*     */   public static void beep() {
/*  24 */     Toolkit.getDefaultToolkit().beep();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String deleteChar(char paramChar, String paramString) {
/*  37 */     Character character = new Character(paramChar);
/*  38 */     return deleteChars(character.toString(), paramString);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String deleteChars(String paramString1, String paramString2) {
/*  52 */     StringTokenizer stringTokenizer = new StringTokenizer(paramString2, paramString1);
/*  53 */     StringBuffer stringBuffer = new StringBuffer(paramString2.length());
/*  54 */     for (; stringTokenizer.hasMoreTokens(); stringBuffer.append(stringTokenizer.nextToken()));
/*  55 */     return stringBuffer.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int keyCodeToDigit(int paramInt) {
/*  68 */     switch (paramInt) {
/*     */       case 49:
/*  70 */         return 1;
/*  71 */       case 50: return 2;
/*  72 */       case 51: return 3;
/*  73 */       case 52: return 4;
/*  74 */       case 53: return 5;
/*  75 */       case 54: return 6;
/*  76 */       case 55: return 7;
/*  77 */       case 56: return 8;
/*  78 */       case 57: return 9;
/*  79 */       case 48: return 0;
/*  80 */     }  return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static File getParentDirectory() {
/*  93 */     FileSystemView fileSystemView = FileSystemView.getFileSystemView();
/*  94 */     return fileSystemView.getParentDirectory(fileSystemView.getHomeDirectory());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static char getSuperscriptChar(int paramInt) {
/* 109 */     switch (paramInt) {
/*     */       
/*     */       case 1:
/* 112 */         return '¹';
/* 113 */       case 2: return '²';
/* 114 */       case 3: return '³';
/* 115 */       case 4: return '⁴';
/* 116 */       case 5: return '⁵';
/* 117 */       case 6: return '⁶';
/* 118 */       case 7: return '⁷';
/* 119 */       case 8: return '⁸';
/* 120 */       case 9: return '⁹';
/* 121 */       case 0: return '⁰';
/*     */     } 
/* 123 */     throw new IllegalArgumentException("Invalid superscript value " + paramInt);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static File getUserDirectory() {
/* 156 */     return new File(System.getProperty("user.dir"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isVersionOK() {
/* 170 */     String str = System.getProperty("java.version");
/* 171 */     if (str.compareTo("1.4.0") < 0) {
/*     */       
/* 173 */       System.out.println("WARNING: This program was designed to run with a version 1.4.0 (or higher) Virtual Machine.");
/*     */       
/* 175 */       return false;
/*     */     } 
/*     */     
/* 178 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isVersionOK(Component paramComponent) {
/* 192 */     String str = System.getProperty("java.version");
/* 193 */     if (str.compareTo("1.4.0") < 0) {
/*     */       
/* 195 */       JOptionPane.showMessageDialog(paramComponent, "WARNING: This program was designed to run with a version 1.4.0 (or higher) Virtual Machine.", "Java version warning", 2);
/*     */ 
/*     */ 
/*     */       
/* 199 */       return false;
/*     */     } 
/*     */     
/* 202 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int lengthOf(int paramInt) {
/* 219 */     return Integer.toString(paramInt).length();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setNativeLookAndFeel() {
/*     */     try {
/* 229 */       UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
/*     */     }
/* 231 */     catch (Exception exception) {}
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void tile(Graphics paramGraphics, Component paramComponent, ImageIcon paramImageIcon) {
/* 246 */     Rectangle rectangle = paramComponent.getBounds();
/* 247 */     int i = paramImageIcon.getIconWidth();
/* 248 */     int j = paramImageIcon.getIconHeight();
/* 249 */     if (i <= 0 || j <= 0)
/* 250 */       return;  for (int k = 0; k < rectangle.width; k += i) {
/* 251 */       for (int m = 0; m < rectangle.height; m += j) {
/* 252 */         paramImageIcon.paintIcon(paramComponent, paramGraphics, k, m);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toString(String[] paramArrayOfString) {
/* 264 */     return toString(paramArrayOfString, ", ");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String toString(String[] paramArrayOfString, String paramString) {
/* 278 */     StringBuffer stringBuffer = new StringBuffer(100);
/*     */     byte b;
/* 280 */     for (b = 0; b < paramArrayOfString.length - 1; b++) {
/* 281 */       stringBuffer.append(paramArrayOfString[b]); stringBuffer.append(paramString);
/* 282 */     }  stringBuffer.append(paramArrayOfString[b]);
/* 283 */     return stringBuffer.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Vector toVector(String[] paramArrayOfString) {
/* 294 */     Vector vector = new Vector();
/* 295 */     for (byte b = 0; b < paramArrayOfString.length; ) { vector.add(paramArrayOfString[b]); b++; }
/* 296 */      return vector;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class Test
/*     */   {
/*     */     public static void main(String[] param1ArrayOfString) {
/* 324 */       int[] arrayOfInt = { 0, 0, 1, 2, -1, 9, 10, -10, 99, 100, -100, 101, 1000, 99999 };
/* 325 */       for (byte b = 0; b < arrayOfInt.length; b++)
/* 326 */         System.out.println("Length of " + arrayOfInt[b] + " is " + Utilities.lengthOf(arrayOfInt[b])); 
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/Utilities.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */