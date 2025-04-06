/*     */ package bsh;
/*     */ 
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.Vector;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StringUtil
/*     */ {
/*     */   public static String[] split(String s, String delim) {
/*  41 */     Vector<String> v = new Vector();
/*  42 */     StringTokenizer st = new StringTokenizer(s, delim);
/*  43 */     while (st.hasMoreTokens())
/*  44 */       v.addElement(st.nextToken()); 
/*  45 */     String[] sa = new String[v.size()];
/*  46 */     v.copyInto((Object[])sa);
/*  47 */     return sa;
/*     */   }
/*     */   
/*     */   public static String[] bubbleSort(String[] in) {
/*  51 */     Vector<String> v = new Vector();
/*  52 */     for (int i = 0; i < in.length; i++) {
/*  53 */       v.addElement(in[i]);
/*     */     }
/*  55 */     int n = v.size();
/*  56 */     boolean swap = true;
/*  57 */     while (swap) {
/*  58 */       swap = false;
/*  59 */       for (int j = 0; j < n - 1; j++) {
/*  60 */         if (((String)v.elementAt(j)).compareTo(v.elementAt(j + 1)) > 0) {
/*     */           
/*  62 */           String tmp = v.elementAt(j + 1);
/*  63 */           v.removeElementAt(j + 1);
/*  64 */           v.insertElementAt(tmp, j);
/*  65 */           swap = true;
/*     */         } 
/*     */       } 
/*     */     } 
/*  69 */     String[] out = new String[n];
/*  70 */     v.copyInto((Object[])out);
/*  71 */     return out;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String maxCommonPrefix(String one, String two) {
/*  76 */     int i = 0;
/*  77 */     while (one.regionMatches(0, two, 0, i))
/*  78 */       i++; 
/*  79 */     return one.substring(0, i - 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public static String methodString(String name, Class[] types) {
/*  84 */     StringBuffer sb = new StringBuffer(name + "(");
/*  85 */     if (types.length > 0)
/*  86 */       sb.append(" "); 
/*  87 */     for (int i = 0; i < types.length; i++) {
/*     */       
/*  89 */       Class c = types[i];
/*  90 */       sb.append(((c == null) ? "null" : c.getName()) + ((i < types.length - 1) ? ", " : " "));
/*     */     } 
/*     */     
/*  93 */     sb.append(")");
/*  94 */     return sb.toString();
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
/*     */   public static String normalizeClassName(Class type) {
/* 120 */     return Reflect.normalizeClassName(type);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/StringUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */