/*     */ package bsh;
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
/*     */ public class ParseException
/*     */   extends EvalError
/*     */ {
/*  51 */   String sourceFile = "<unknown>"; protected boolean specialConstructor; public Token currentToken;
/*     */   public int[][] expectedTokenSequences;
/*     */   public String[] tokenImage;
/*     */   protected String eol;
/*     */   
/*     */   public void setErrorSourceFile(String file) {
/*  57 */     this.sourceFile = file;
/*     */   }
/*     */   
/*     */   public String getErrorSourceFile() {
/*  61 */     return this.sourceFile;
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
/*     */   public ParseException(Token currentTokenVal, int[][] expectedTokenSequencesVal, String[] tokenImageVal) {
/*  84 */     this();
/*     */     
/*  86 */     this.specialConstructor = true;
/*  87 */     this.currentToken = currentTokenVal;
/*  88 */     this.expectedTokenSequences = expectedTokenSequencesVal;
/*  89 */     this.tokenImage = tokenImageVal;
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
/*     */   public ParseException() {
/* 104 */     this("");
/*     */     
/* 106 */     this.specialConstructor = false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ParseException(String message) {
/* 112 */     super(message, null, null);
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
/* 215 */     this.eol = System.getProperty("line.separator", "\n");
/*     */     this.specialConstructor = false;
/*     */   }
/*     */   
/*     */   public String getMessage() {
/*     */     return getMessage(false);
/*     */   }
/*     */   
/* 223 */   protected String add_escapes(String str) { StringBuffer retval = new StringBuffer();
/*     */     
/* 225 */     for (int i = 0; i < str.length(); i++) {
/* 226 */       char ch; switch (str.charAt(i)) {
/*     */         case '\000':
/*     */           break;
/*     */         
/*     */         case '\b':
/* 231 */           retval.append("\\b");
/*     */           break;
/*     */         case '\t':
/* 234 */           retval.append("\\t");
/*     */           break;
/*     */         case '\n':
/* 237 */           retval.append("\\n");
/*     */           break;
/*     */         case '\f':
/* 240 */           retval.append("\\f");
/*     */           break;
/*     */         case '\r':
/* 243 */           retval.append("\\r");
/*     */           break;
/*     */         case '"':
/* 246 */           retval.append("\\\"");
/*     */           break;
/*     */         case '\'':
/* 249 */           retval.append("\\'");
/*     */           break;
/*     */         case '\\':
/* 252 */           retval.append("\\\\");
/*     */           break;
/*     */         default:
/* 255 */           if ((ch = str.charAt(i)) < ' ' || ch > '~') {
/* 256 */             String s = "0000" + Integer.toString(ch, 16);
/* 257 */             retval.append("\\u" + s.substring(s.length() - 4, s.length())); break;
/*     */           } 
/* 259 */           retval.append(ch);
/*     */           break;
/*     */       } 
/*     */     
/*     */     } 
/* 264 */     return retval.toString(); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getErrorLineNumber() {
/* 271 */     return this.currentToken.next.beginLine;
/*     */   } public String getMessage(boolean debug) { if (!this.specialConstructor) return super.getMessage();  String expected = ""; int maxSize = 0; for (int i = 0; i < this.expectedTokenSequences.length; i++) { if (maxSize < (this.expectedTokenSequences[i]).length)
/*     */         maxSize = (this.expectedTokenSequences[i]).length;  for (int k = 0; k < (this.expectedTokenSequences[i]).length; k++)
/*     */         expected = expected + this.tokenImage[this.expectedTokenSequences[i][k]] + " ";  if (this.expectedTokenSequences[i][(this.expectedTokenSequences[i]).length - 1] != 0)
/*     */         expected = expected + "...";  expected = expected + this.eol + "    "; }  String retval = "In file: " + this.sourceFile + " Encountered \""; Token tok = this.currentToken.next; for (int j = 0; j < maxSize; j++) { if (j != 0)
/* 276 */         retval = retval + " ";  if (tok.kind == 0) { retval = retval + this.tokenImage[0]; break; }  retval = retval + add_escapes(tok.image); tok = tok.next; }  retval = retval + "\" at line " + this.currentToken.next.beginLine + ", column " + this.currentToken.next.beginColumn + "." + this.eol; if (debug) { if (this.expectedTokenSequences.length == 1) { retval = retval + "Was expecting:" + this.eol + "    "; } else { retval = retval + "Was expecting one of:" + this.eol + "    "; }  retval = retval + expected; }  return retval; } public String getErrorText() { int maxSize = 0;
/* 277 */     for (int i = 0; i < this.expectedTokenSequences.length; i++) {
/* 278 */       if (maxSize < (this.expectedTokenSequences[i]).length) {
/* 279 */         maxSize = (this.expectedTokenSequences[i]).length;
/*     */       }
/*     */     } 
/* 282 */     String retval = "";
/* 283 */     Token tok = this.currentToken.next;
/* 284 */     for (int j = 0; j < maxSize; j++) {
/*     */       
/* 286 */       if (j != 0) retval = retval + " "; 
/* 287 */       if (tok.kind == 0) {
/* 288 */         retval = retval + this.tokenImage[0];
/*     */         break;
/*     */       } 
/* 291 */       retval = retval + add_escapes(tok.image);
/* 292 */       tok = tok.next;
/*     */     } 
/*     */     
/* 295 */     return retval; }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 299 */     return getMessage();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/ParseException.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */