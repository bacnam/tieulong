/*     */ package bsh.util;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.net.Socket;
/*     */ import java.util.StringTokenizer;
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
/*     */ 
/*     */ class HttpdConnection
/*     */   extends Thread
/*     */ {
/*     */   Socket client;
/*     */   BufferedReader in;
/*     */   OutputStream out;
/*     */   PrintStream pout;
/*     */   boolean isHttp1;
/*     */   
/*     */   HttpdConnection(Socket client) {
/*  89 */     this.client = client;
/*  90 */     setPriority(4);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void run() {
/*     */     try {
/*  97 */       this.in = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
/*     */       
/*  99 */       this.out = this.client.getOutputStream();
/* 100 */       this.pout = new PrintStream(this.out);
/*     */       
/* 102 */       String request = this.in.readLine();
/* 103 */       if (request == null) {
/* 104 */         error(400, "Empty Request");
/*     */       }
/* 106 */       if (request.toLowerCase().indexOf("http/1.") != -1) {
/*     */         String s;
/*     */         
/* 109 */         while (!(s = this.in.readLine()).equals("") && s != null);
/*     */ 
/*     */         
/* 112 */         this.isHttp1 = true;
/*     */       } 
/*     */       
/* 115 */       StringTokenizer st = new StringTokenizer(request);
/* 116 */       if (st.countTokens() < 2) {
/* 117 */         error(400, "Bad Request");
/*     */       } else {
/*     */         
/* 120 */         String command = st.nextToken();
/* 121 */         if (command.equals("GET")) {
/* 122 */           serveFile(st.nextToken());
/*     */         } else {
/* 124 */           error(400, "Bad Request");
/*     */         } 
/*     */       } 
/* 127 */       this.client.close();
/*     */     }
/* 129 */     catch (IOException e) {
/*     */       
/* 131 */       System.out.println("I/O error " + e);
/*     */       
/*     */       try {
/* 134 */         this.client.close();
/*     */       }
/* 136 */       catch (Exception e2) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void serveFile(String file) throws FileNotFoundException, IOException {
/* 144 */     if (file.equals("/")) {
/* 145 */       file = "/remote/remote.html";
/*     */     }
/* 147 */     if (file.startsWith("/remote/")) {
/* 148 */       file = "/bsh/util/lib/" + file.substring(8);
/*     */     }
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
/* 164 */     if (file.startsWith("/java")) {
/* 165 */       error(404, "Object Not Found");
/*     */     } else {
/*     */       try {
/* 168 */         System.out.println("sending file: " + file);
/* 169 */         sendFileData(file);
/* 170 */       } catch (FileNotFoundException e) {
/* 171 */         error(404, "Object Not Found");
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void sendFileData(String file) throws IOException, FileNotFoundException {
/* 181 */     InputStream fis = getClass().getResourceAsStream(file);
/* 182 */     if (fis == null)
/* 183 */       throw new FileNotFoundException(file); 
/* 184 */     byte[] data = new byte[fis.available()];
/*     */     
/* 186 */     if (this.isHttp1) {
/*     */       
/* 188 */       this.pout.println("HTTP/1.0 200 Document follows");
/*     */       
/* 190 */       this.pout.println("Content-length: " + data.length);
/*     */       
/* 192 */       if (file.endsWith(".gif")) {
/* 193 */         this.pout.println("Content-type: image/gif");
/*     */       }
/* 195 */       else if (file.endsWith(".html") || file.endsWith(".htm")) {
/* 196 */         this.pout.println("Content-Type: text/html");
/*     */       } else {
/* 198 */         this.pout.println("Content-Type: application/octet-stream");
/*     */       } 
/* 200 */       this.pout.println();
/*     */     } 
/*     */     
/* 203 */     int bytesread = 0;
/*     */     
/*     */     while (true) {
/* 206 */       bytesread = fis.read(data);
/* 207 */       if (bytesread > 0)
/* 208 */         this.pout.write(data, 0, bytesread); 
/* 209 */       if (bytesread == -1) {
/* 210 */         this.pout.flush();
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   } private void error(int num, String s) {
/* 215 */     s = "<html><h1>" + s + "</h1></html>";
/* 216 */     if (this.isHttp1) {
/*     */       
/* 218 */       this.pout.println("HTTP/1.0 " + num + " " + s);
/* 219 */       this.pout.println("Content-type: text/html");
/* 220 */       this.pout.println("Content-length: " + s.length() + "\n");
/*     */     } 
/*     */     
/* 223 */     this.pout.println(s);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/util/HttpdConnection.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */