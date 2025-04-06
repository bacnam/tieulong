/*     */ package bsh.servlet;
/*     */ import bsh.EvalError;
/*     */ import bsh.Interpreter;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringReader;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.http.HttpServlet;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ 
/*     */ public class BshServlet extends HttpServlet {
/*  16 */   static String exampleScript = "print(\"hello!\");";
/*     */   static String bshVersion;
/*     */   
/*     */   static String getBshVersion() {
/*  20 */     if (bshVersion != null) {
/*  21 */       return bshVersion;
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
/*  32 */     Interpreter bsh = new Interpreter();
/*     */     try {
/*  34 */       bsh.eval(new InputStreamReader(BshServlet.class.getResource("getVersion.bsh").openStream()));
/*     */       
/*  36 */       bshVersion = (String)bsh.eval("getVersion()");
/*  37 */     } catch (Exception e) {
/*  38 */       bshVersion = "BeanShell: unknown version";
/*     */     } 
/*     */     
/*  41 */     return bshVersion;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
/*  48 */     String script = request.getParameter("bsh.script");
/*  49 */     String client = request.getParameter("bsh.client");
/*  50 */     String output = request.getParameter("bsh.servlet.output");
/*  51 */     String captureOutErr = request.getParameter("bsh.servlet.captureOutErr");
/*     */     
/*  53 */     boolean capture = false;
/*  54 */     if (captureOutErr != null && captureOutErr.equalsIgnoreCase("true")) {
/*  55 */       capture = true;
/*     */     }
/*  57 */     Object scriptResult = null;
/*  58 */     Exception scriptError = null;
/*  59 */     StringBuffer scriptOutput = new StringBuffer();
/*  60 */     if (script != null) {
/*     */       try {
/*  62 */         scriptResult = evalScript(script, scriptOutput, capture, request, response);
/*     */       }
/*  64 */       catch (Exception e) {
/*  65 */         scriptError = e;
/*     */       } 
/*     */     }
/*     */     
/*  69 */     response.setHeader("Bsh-Return", String.valueOf(scriptResult));
/*     */     
/*  71 */     if ((output != null && output.equalsIgnoreCase("raw")) || (client != null && client.equals("Remote"))) {
/*     */       
/*  73 */       sendRaw(request, response, scriptError, scriptResult, scriptOutput);
/*     */     } else {
/*     */       
/*  76 */       sendHTML(request, response, script, scriptError, scriptResult, scriptOutput, capture);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void sendHTML(HttpServletRequest request, HttpServletResponse response, String script, Exception scriptError, Object scriptResult, StringBuffer scriptOutput, boolean capture) throws IOException {
/*  87 */     SimpleTemplate st = new SimpleTemplate(BshServlet.class.getResource("page.template"));
/*     */     
/*  89 */     st.replace("version", getBshVersion());
/*     */ 
/*     */ 
/*     */     
/*  93 */     String requestURI = request.getRequestURI();
/*     */     
/*  95 */     st.replace("servletURL", requestURI);
/*  96 */     if (script != null) {
/*  97 */       st.replace("script", script);
/*     */     } else {
/*  99 */       st.replace("script", exampleScript);
/* 100 */     }  if (capture) {
/* 101 */       st.replace("captureOutErr", "CHECKED");
/*     */     } else {
/* 103 */       st.replace("captureOutErr", "");
/* 104 */     }  if (script != null) {
/* 105 */       st.replace("scriptResult", formatScriptResultHTML(script, scriptResult, scriptError, scriptOutput));
/*     */     }
/*     */ 
/*     */     
/* 109 */     response.setContentType("text/html");
/* 110 */     PrintWriter out = response.getWriter();
/* 111 */     st.write(out);
/* 112 */     out.flush();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void sendRaw(HttpServletRequest request, HttpServletResponse response, Exception scriptError, Object scriptResult, StringBuffer scriptOutput) throws IOException {
/* 120 */     response.setContentType("text/plain");
/* 121 */     PrintWriter out = response.getWriter();
/* 122 */     if (scriptError != null) {
/* 123 */       out.println("Script Error:\n" + scriptError);
/*     */     } else {
/* 125 */       out.println(scriptOutput.toString());
/* 126 */     }  out.flush();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String formatScriptResultHTML(String script, Object result, Exception error, StringBuffer scriptOutput) throws IOException {
/*     */     SimpleTemplate tmplt;
/* 138 */     if (error != null) {
/*     */       String errString;
/* 140 */       tmplt = new SimpleTemplate(getClass().getResource("error.template"));
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 145 */       if (error instanceof EvalError) {
/*     */         
/* 147 */         int lineNo = ((EvalError)error).getErrorLineNumber();
/* 148 */         String msg = error.getMessage();
/* 149 */         int contextLines = 4;
/* 150 */         errString = escape(msg);
/* 151 */         if (lineNo > -1) {
/* 152 */           errString = errString + "<hr>" + showScriptContextHTML(script, lineNo, contextLines);
/*     */         }
/*     */       } else {
/* 155 */         errString = escape(error.toString());
/*     */       } 
/* 157 */       tmplt.replace("error", errString);
/*     */     } else {
/* 159 */       tmplt = new SimpleTemplate(getClass().getResource("result.template"));
/*     */       
/* 161 */       tmplt.replace("value", escape(String.valueOf(result)));
/* 162 */       tmplt.replace("output", escape(scriptOutput.toString()));
/*     */     } 
/*     */     
/* 165 */     return tmplt.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   String showScriptContextHTML(String s, int lineNo, int context) {
/* 174 */     StringBuffer sb = new StringBuffer();
/* 175 */     BufferedReader br = new BufferedReader(new StringReader(s));
/*     */     
/* 177 */     int beginLine = Math.max(1, lineNo - context);
/* 178 */     int endLine = lineNo + context;
/* 179 */     for (int i = 1; i <= lineNo + context + 1; i++) {
/*     */       
/* 181 */       if (i < beginLine) {
/*     */         
/*     */         try {
/* 184 */           br.readLine();
/* 185 */         } catch (IOException e) {
/* 186 */           throw new RuntimeException(e.toString());
/*     */         } 
/*     */       } else {
/*     */         String line;
/* 190 */         if (i > endLine) {
/*     */           break;
/*     */         }
/*     */         
/*     */         try {
/* 195 */           line = br.readLine();
/* 196 */         } catch (IOException e) {
/* 197 */           throw new RuntimeException(e.toString());
/*     */         } 
/*     */         
/* 200 */         if (line == null)
/*     */           break; 
/* 202 */         if (i == lineNo) {
/* 203 */           sb.append("<font color=\"red\">" + i + ": " + line + "</font><br/>");
/*     */         } else {
/* 205 */           sb.append(i + ": " + line + "<br/>");
/*     */         } 
/*     */       } 
/* 208 */     }  return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
/* 214 */     doGet(request, response);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Object evalScript(String script, StringBuffer scriptOutput, boolean captureOutErr, HttpServletRequest request, HttpServletResponse response) throws EvalError {
/* 223 */     ByteArrayOutputStream baos = new ByteArrayOutputStream();
/* 224 */     PrintStream pout = new PrintStream(baos);
/*     */ 
/*     */ 
/*     */     
/* 228 */     Interpreter bsh = new Interpreter(null, pout, pout, false);
/*     */ 
/*     */     
/* 231 */     bsh.set("bsh.httpServletRequest", request);
/* 232 */     bsh.set("bsh.httpServletResponse", response);
/*     */ 
/*     */     
/* 235 */     Object result = null;
/* 236 */     String error = null;
/* 237 */     PrintStream sout = System.out;
/* 238 */     PrintStream serr = System.err;
/* 239 */     if (captureOutErr) {
/* 240 */       System.setOut(pout);
/* 241 */       System.setErr(pout);
/*     */     } 
/*     */     
/*     */     try {
/* 245 */       result = bsh.eval(script);
/*     */     } finally {
/* 247 */       if (captureOutErr) {
/* 248 */         System.setOut(sout);
/* 249 */         System.setErr(serr);
/*     */       } 
/*     */     } 
/* 252 */     pout.flush();
/* 253 */     scriptOutput.append(baos.toString());
/* 254 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String escape(String value) {
/* 262 */     String search = "&<>";
/* 263 */     String[] replace = { "&amp;", "&lt;", "&gt;" };
/*     */     
/* 265 */     StringBuffer buf = new StringBuffer();
/*     */     
/* 267 */     for (int i = 0; i < value.length(); i++) {
/*     */       
/* 269 */       char c = value.charAt(i);
/* 270 */       int pos = search.indexOf(c);
/* 271 */       if (pos < 0) {
/* 272 */         buf.append(c);
/*     */       } else {
/* 274 */         buf.append(replace[pos]);
/*     */       } 
/*     */     } 
/* 277 */     return buf.toString();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/servlet/BshServlet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */