/*     */ package bsh.util;
/*     */ 
/*     */ import bsh.ConsoleInterface;
/*     */ import bsh.Interpreter;
/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import java.awt.Frame;
/*     */ import java.awt.TextArea;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.event.KeyListener;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.awt.peer.TextComponentPeer;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PipedInputStream;
/*     */ import java.io.PipedOutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Reader;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AWTConsole
/*     */   extends TextArea
/*     */   implements ConsoleInterface, Runnable, KeyListener
/*     */ {
/*     */   private OutputStream outPipe;
/*     */   private InputStream inPipe;
/*     */   private InputStream in;
/*     */   private PrintStream out;
/*     */   
/*     */   public Reader getIn() {
/*  94 */     return new InputStreamReader(this.in);
/*  95 */   } public PrintStream getOut() { return this.out; } public PrintStream getErr() {
/*  96 */     return this.out;
/*     */   }
/*  98 */   private StringBuffer line = new StringBuffer();
/*     */   private String startedLine;
/* 100 */   private int textLength = 0;
/* 101 */   private Vector history = new Vector();
/* 102 */   private int histLine = 0;
/*     */   
/*     */   public AWTConsole(int rows, int cols, InputStream cin, OutputStream cout) {
/* 105 */     super(rows, cols);
/* 106 */     setFont(new Font("Monospaced", 0, 14));
/* 107 */     setEditable(false);
/* 108 */     addKeyListener(this);
/*     */     
/* 110 */     this.outPipe = cout;
/* 111 */     if (this.outPipe == null) {
/* 112 */       this.outPipe = new PipedOutputStream();
/*     */       try {
/* 114 */         this.in = new PipedInputStream((PipedOutputStream)this.outPipe);
/* 115 */       } catch (IOException e) {
/* 116 */         print("Console internal error...");
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 121 */     this.inPipe = cin;
/* 122 */     (new Thread(this)).start();
/*     */     
/* 124 */     requestFocus();
/*     */   }
/*     */   
/*     */   public void keyPressed(KeyEvent e) {
/* 128 */     type(e.getKeyCode(), e.getKeyChar(), e.getModifiers());
/* 129 */     e.consume();
/*     */   }
/*     */   
/*     */   public AWTConsole() {
/* 133 */     this(12, 80, (InputStream)null, (OutputStream)null);
/*     */   }
/*     */   public AWTConsole(InputStream in, OutputStream out) {
/* 136 */     this(12, 80, in, out);
/*     */   }
/*     */   
/*     */   public void type(int code, char ch, int modifiers) {
/* 140 */     switch (code) {
/*     */       case 8:
/* 142 */         if (this.line.length() > 0) {
/* 143 */           this.line.setLength(this.line.length() - 1);
/* 144 */           replaceRange("", this.textLength - 1, this.textLength);
/* 145 */           this.textLength--;
/*     */         } 
/*     */         return;
/*     */       case 10:
/* 149 */         enter();
/*     */         return;
/*     */       case 85:
/* 152 */         if ((modifiers & 0x2) > 0) {
/* 153 */           int len = this.line.length();
/* 154 */           replaceRange("", this.textLength - len, this.textLength);
/* 155 */           this.line.setLength(0);
/* 156 */           this.histLine = 0;
/* 157 */           this.textLength = getText().length();
/*     */         } else {
/* 159 */           doChar(ch);
/*     */         }  return;
/*     */       case 38:
/* 162 */         historyUp();
/*     */         return;
/*     */       case 40:
/* 165 */         historyDown();
/*     */         return;
/*     */       case 9:
/* 168 */         this.line.append("    ");
/* 169 */         append("    ");
/* 170 */         this.textLength += 4;
/*     */         return;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 67:
/* 179 */         if ((modifiers & 0x2) > 0) {
/* 180 */           this.line.append("^C");
/* 181 */           append("^C");
/* 182 */           this.textLength += 2;
/*     */         } else {
/* 184 */           doChar(ch);
/*     */         }  return;
/*     */     } 
/* 187 */     doChar(ch);
/*     */   }
/*     */ 
/*     */   
/*     */   private void doChar(char ch) {
/* 192 */     if (ch >= ' ' && ch <= '~') {
/* 193 */       this.line.append(ch);
/* 194 */       append(String.valueOf(ch));
/* 195 */       this.textLength++;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void enter() {
/*     */     String s;
/* 201 */     if (this.line.length() == 0) {
/* 202 */       s = ";\n";
/*     */     } else {
/* 204 */       s = this.line + "\n";
/* 205 */       this.history.addElement(this.line.toString());
/*     */     } 
/* 207 */     this.line.setLength(0);
/* 208 */     this.histLine = 0;
/* 209 */     append("\n");
/* 210 */     this.textLength = getText().length();
/* 211 */     acceptLine(s);
/*     */     
/* 213 */     setCaretPosition(this.textLength);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCaretPosition(int pos) {
/* 223 */     ((TextComponentPeer)getPeer()).setCaretPosition(pos + countNLs());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int countNLs() {
/* 232 */     String s = getText();
/* 233 */     int c = 0;
/* 234 */     for (int i = 0; i < s.length(); i++) {
/* 235 */       if (s.charAt(i) == '\n')
/* 236 */         c++; 
/* 237 */     }  return c;
/*     */   }
/*     */   
/*     */   private void historyUp() {
/* 241 */     if (this.history.size() == 0)
/*     */       return; 
/* 243 */     if (this.histLine == 0)
/* 244 */       this.startedLine = this.line.toString(); 
/* 245 */     if (this.histLine < this.history.size()) {
/* 246 */       this.histLine++;
/* 247 */       showHistoryLine();
/*     */     } 
/*     */   }
/*     */   private void historyDown() {
/* 251 */     if (this.histLine == 0) {
/*     */       return;
/*     */     }
/* 254 */     this.histLine--;
/* 255 */     showHistoryLine();
/*     */   }
/*     */   
/*     */   private void showHistoryLine() {
/*     */     String showline;
/* 260 */     if (this.histLine == 0) {
/* 261 */       showline = this.startedLine;
/*     */     } else {
/* 263 */       showline = this.history.elementAt(this.history.size() - this.histLine);
/*     */     } 
/* 265 */     replaceRange(showline, this.textLength - this.line.length(), this.textLength);
/* 266 */     this.line = new StringBuffer(showline);
/* 267 */     this.textLength = getText().length();
/*     */   }
/*     */   
/*     */   private void acceptLine(String line) {
/* 271 */     if (this.outPipe == null) {
/* 272 */       print("Console internal error...");
/*     */     } else {
/*     */       try {
/* 275 */         this.outPipe.write(line.getBytes());
/* 276 */         this.outPipe.flush();
/* 277 */       } catch (IOException e) {
/* 278 */         this.outPipe = null;
/* 279 */         throw new RuntimeException("Console pipe broken...");
/*     */       } 
/*     */     } 
/*     */   }
/*     */   public void println(Object o) {
/* 284 */     print(String.valueOf(o) + "\n");
/*     */   }
/*     */   
/*     */   public void error(Object o) {
/* 288 */     print(o, Color.red);
/*     */   }
/*     */ 
/*     */   
/*     */   public void print(Object o, Color c) {
/* 293 */     print("*** " + String.valueOf(o));
/*     */   }
/*     */   
/*     */   public synchronized void print(Object o) {
/* 297 */     append(String.valueOf(o));
/* 298 */     this.textLength = getText().length();
/*     */   }
/*     */   
/*     */   private void inPipeWatcher() throws IOException {
/* 302 */     if (this.inPipe == null) {
/* 303 */       PipedOutputStream pout = new PipedOutputStream();
/* 304 */       this.out = new PrintStream(pout);
/* 305 */       this.inPipe = new PipedInputStream(pout);
/*     */     } 
/* 307 */     byte[] ba = new byte[256];
/*     */     int read;
/* 309 */     while ((read = this.inPipe.read(ba)) != -1) {
/* 310 */       print(new String(ba, 0, read));
/*     */     }
/* 312 */     println("Console: Input closed...");
/*     */   }
/*     */   
/*     */   public void run() {
/*     */     try {
/* 317 */       inPipeWatcher();
/* 318 */     } catch (IOException e) {
/* 319 */       println("Console: I/O Error...");
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/* 324 */     AWTConsole console = new AWTConsole();
/* 325 */     final Frame f = new Frame("Bsh Console");
/* 326 */     f.add(console, "Center");
/* 327 */     f.pack();
/* 328 */     f.show();
/* 329 */     f.addWindowListener(new WindowAdapter() {
/*     */           public void windowClosing(WindowEvent e) {
/* 331 */             f.dispose();
/*     */           }
/*     */         });
/*     */     
/* 335 */     Interpreter interpreter = new Interpreter(console);
/* 336 */     interpreter.run();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 340 */     return "BeanShell AWTConsole";
/*     */   }
/*     */   
/*     */   public void keyTyped(KeyEvent e) {}
/*     */   
/*     */   public void keyReleased(KeyEvent e) {}
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/util/AWTConsole.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */