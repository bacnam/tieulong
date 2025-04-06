/*     */ package bsh;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintWriter;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.Socket;
/*     */ import java.net.URL;
/*     */ import java.net.URLEncoder;
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
/*     */ public class Remote
/*     */ {
/*     */   public static void main(String[] args) throws Exception {
/*  50 */     if (args.length < 2) {
/*  51 */       System.out.println("usage: Remote URL(http|bsh) file [ file ] ... ");
/*     */       
/*  53 */       System.exit(1);
/*     */     } 
/*  55 */     String url = args[0];
/*  56 */     String text = getFile(args[1]);
/*  57 */     int ret = eval(url, text);
/*  58 */     System.exit(ret);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int eval(String url, String text) throws IOException {
/*  68 */     String returnValue = null;
/*  69 */     if (url.startsWith("http:")) {
/*  70 */       returnValue = doHttp(url, text);
/*  71 */     } else if (url.startsWith("bsh:")) {
/*  72 */       returnValue = doBsh(url, text);
/*     */     } else {
/*  74 */       throw new IOException("Unrecognized URL type.Scheme must be http:// or bsh://");
/*     */     } 
/*     */     
/*     */     try {
/*  78 */       return Integer.parseInt(returnValue);
/*  79 */     } catch (Exception e) {
/*     */       
/*  81 */       return 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String doBsh(String url, String text) {
/*  89 */     String host = "";
/*  90 */     String port = "";
/*  91 */     String returnValue = "-1";
/*  92 */     String orgURL = url;
/*     */ 
/*     */     
/*     */     try {
/*  96 */       url = url.substring(6);
/*     */       
/*  98 */       int index = url.indexOf(":");
/*  99 */       host = url.substring(0, index);
/* 100 */       port = url.substring(index + 1, url.length());
/* 101 */     } catch (Exception ex) {
/* 102 */       System.err.println("Bad URL: " + orgURL + ": " + ex);
/* 103 */       return returnValue;
/*     */     } 
/*     */     
/*     */     try {
/* 107 */       System.out.println("Connecting to host : " + host + " at port : " + port);
/*     */       
/* 109 */       Socket s = new Socket(host, Integer.parseInt(port) + 1);
/*     */       
/* 111 */       OutputStream out = s.getOutputStream();
/* 112 */       InputStream in = s.getInputStream();
/*     */       
/* 114 */       sendLine(text, out);
/*     */       
/* 116 */       BufferedReader bin = new BufferedReader(new InputStreamReader(in));
/*     */       
/*     */       String line;
/* 119 */       while ((line = bin.readLine()) != null) {
/* 120 */         System.out.println(line);
/*     */       }
/*     */       
/* 123 */       returnValue = "1";
/* 124 */       return returnValue;
/* 125 */     } catch (Exception ex) {
/* 126 */       System.err.println("Error communicating with server: " + ex);
/* 127 */       return returnValue;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void sendLine(String line, OutputStream outPipe) throws IOException {
/* 134 */     outPipe.write(line.getBytes());
/* 135 */     outPipe.flush();
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
/*     */   static String doHttp(String postURL, String text) {
/* 148 */     String returnValue = null;
/* 149 */     StringBuffer sb = new StringBuffer();
/* 150 */     sb.append("bsh.client=Remote");
/* 151 */     sb.append("&bsh.script=");
/* 152 */     sb.append(URLEncoder.encode(text));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 161 */     String formData = sb.toString();
/*     */     
/*     */     try {
/* 164 */       URL url = new URL(postURL);
/* 165 */       HttpURLConnection urlcon = (HttpURLConnection)url.openConnection();
/*     */       
/* 167 */       urlcon.setRequestMethod("POST");
/* 168 */       urlcon.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
/*     */       
/* 170 */       urlcon.setDoOutput(true);
/* 171 */       urlcon.setDoInput(true);
/* 172 */       PrintWriter pout = new PrintWriter(new OutputStreamWriter(urlcon.getOutputStream(), "8859_1"), true);
/*     */       
/* 174 */       pout.print(formData);
/* 175 */       pout.flush();
/*     */ 
/*     */       
/* 178 */       int rc = urlcon.getResponseCode();
/* 179 */       if (rc != 200) {
/* 180 */         System.out.println("Error, HTTP response: " + rc);
/*     */       }
/* 182 */       returnValue = urlcon.getHeaderField("Bsh-Return");
/*     */       
/* 184 */       BufferedReader bin = new BufferedReader(new InputStreamReader(urlcon.getInputStream()));
/*     */       
/*     */       String line;
/* 187 */       while ((line = bin.readLine()) != null) {
/* 188 */         System.out.println(line);
/*     */       }
/* 190 */       System.out.println("Return Value: " + returnValue);
/*     */     }
/* 192 */     catch (MalformedURLException e) {
/* 193 */       System.out.println(e);
/* 194 */     } catch (IOException e2) {
/* 195 */       System.out.println(e2);
/*     */     } 
/*     */     
/* 198 */     return returnValue;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String getFile(String name) throws FileNotFoundException, IOException {
/* 207 */     StringBuffer sb = new StringBuffer();
/* 208 */     BufferedReader bin = new BufferedReader(new FileReader(name));
/*     */     String line;
/* 210 */     while ((line = bin.readLine()) != null)
/* 211 */       sb.append(line).append("\n"); 
/* 212 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/Remote.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */