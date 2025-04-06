/*     */ package bsh.servlet;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Reader;
/*     */ import java.net.URL;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
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
/*     */ public class SimpleTemplate
/*     */ {
/*     */   StringBuffer buff;
/*  39 */   static String NO_TEMPLATE = "NO_TEMPLATE";
/*  40 */   static Map templateData = new HashMap<Object, Object>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static boolean cacheTemplates = true;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static SimpleTemplate getTemplate(String file) {
/*  55 */     String templateText = (String)templateData.get(file);
/*     */     
/*  57 */     if (templateText == null || !cacheTemplates) {
/*     */       try {
/*  59 */         FileReader fr = new FileReader(file);
/*  60 */         templateText = getStringFromStream(fr);
/*  61 */         templateData.put(file, templateText);
/*  62 */       } catch (IOException e) {
/*     */         
/*  64 */         templateData.put(file, NO_TEMPLATE);
/*     */       }
/*     */     
/*     */     }
/*  68 */     else if (templateText.equals(NO_TEMPLATE)) {
/*  69 */       return null;
/*     */     } 
/*  71 */     if (templateText == null) {
/*  72 */       return null;
/*     */     }
/*  74 */     return new SimpleTemplate(templateText);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getStringFromStream(InputStream ins) throws IOException {
/*  80 */     return getStringFromStream(new InputStreamReader(ins));
/*     */   }
/*     */   
/*     */   public static String getStringFromStream(Reader reader) throws IOException {
/*  84 */     StringBuffer sb = new StringBuffer();
/*  85 */     BufferedReader br = new BufferedReader(reader);
/*     */     String line;
/*  87 */     while ((line = br.readLine()) != null) {
/*  88 */       sb.append(line + "\n");
/*     */     }
/*  90 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public SimpleTemplate(String template) {
/*  94 */     init(template);
/*     */   }
/*     */   
/*     */   public SimpleTemplate(Reader reader) throws IOException {
/*  98 */     String template = getStringFromStream(reader);
/*  99 */     init(template);
/*     */   }
/*     */ 
/*     */   
/*     */   public SimpleTemplate(URL url) throws IOException {
/* 104 */     String template = getStringFromStream(url.openStream());
/* 105 */     init(template);
/*     */   }
/*     */   
/*     */   private void init(String s) {
/* 109 */     this.buff = new StringBuffer(s);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void replace(String param, String value) {
/*     */     int[] range;
/* 117 */     while ((range = findTemplate(param)) != null) {
/* 118 */       this.buff.replace(range[0], range[1], value);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   int[] findTemplate(String name) {
/* 127 */     String text = this.buff.toString();
/* 128 */     int len = text.length();
/*     */     
/* 130 */     int start = 0;
/*     */     
/* 132 */     while (start < len) {
/*     */ 
/*     */       
/* 135 */       int cstart = text.indexOf("<!--", start);
/* 136 */       if (cstart == -1)
/* 137 */         return null; 
/* 138 */       int cend = text.indexOf("-->", cstart);
/* 139 */       if (cend == -1)
/* 140 */         return null; 
/* 141 */       cend += "-->".length();
/*     */ 
/*     */       
/* 144 */       int tstart = text.indexOf("TEMPLATE-", cstart);
/* 145 */       if (tstart == -1) {
/* 146 */         start = cend;
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 151 */       if (tstart > cend) {
/* 152 */         start = cend;
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 157 */       int pstart = tstart + "TEMPLATE-".length();
/*     */       
/* 159 */       int pend = len;
/* 160 */       for (pend = pstart; pend < len; pend++) {
/* 161 */         char c = text.charAt(pend);
/* 162 */         if (c == ' ' || c == '\t' || c == '-')
/*     */           break; 
/*     */       } 
/* 165 */       if (pend >= len) {
/* 166 */         return null;
/*     */       }
/* 168 */       String param = text.substring(pstart, pend);
/*     */ 
/*     */       
/* 171 */       if (param.equals(name)) {
/* 172 */         return new int[] { cstart, cend };
/*     */       }
/*     */ 
/*     */       
/* 176 */       start = cend;
/*     */     } 
/*     */ 
/*     */     
/* 180 */     return null;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 184 */     return this.buff.toString();
/*     */   }
/*     */   
/*     */   public void write(PrintWriter out) {
/* 188 */     out.println(toString());
/*     */   }
/*     */   
/*     */   public void write(PrintStream out) {
/* 192 */     out.println(toString());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] args) throws IOException {
/* 200 */     String filename = args[0];
/* 201 */     String param = args[1];
/* 202 */     String value = args[2];
/*     */     
/* 204 */     FileReader fr = new FileReader(filename);
/* 205 */     String templateText = getStringFromStream(fr);
/* 206 */     SimpleTemplate template = new SimpleTemplate(templateText);
/*     */     
/* 208 */     template.replace(param, value);
/* 209 */     template.write(System.out);
/*     */   }
/*     */   
/*     */   public static void setCacheTemplates(boolean b) {
/* 213 */     cacheTemplates = b;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/servlet/SimpleTemplate.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */