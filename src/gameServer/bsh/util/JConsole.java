/*     */ package bsh.util;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Cursor;
/*     */ import java.awt.Font;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.KeyEvent;
/*     */ import java.awt.event.KeyListener;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.MouseListener;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PipedInputStream;
/*     */ import java.io.PipedOutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Reader;
/*     */ import java.util.Vector;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.JPopupMenu;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTextPane;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.text.AttributeSet;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.DefaultStyledDocument;
/*     */ import javax.swing.text.MutableAttributeSet;
/*     */ import javax.swing.text.SimpleAttributeSet;
/*     */ import javax.swing.text.StyleConstants;
/*     */ import javax.swing.text.StyledDocument;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JConsole
/*     */   extends JScrollPane
/*     */   implements GUIConsoleInterface, Runnable, KeyListener, MouseListener, ActionListener, PropertyChangeListener
/*     */ {
/*     */   private static final String CUT = "Cut";
/*     */   private static final String COPY = "Copy";
/*     */   private static final String PASTE = "Paste";
/*     */   private OutputStream outPipe;
/*     */   private InputStream inPipe;
/*     */   private InputStream in;
/*     */   private PrintStream out;
/*     */   
/*     */   public InputStream getInputStream() {
/*  76 */     return this.in;
/*  77 */   } public Reader getIn() { return new InputStreamReader(this.in); }
/*  78 */   public PrintStream getOut() { return this.out; } public PrintStream getErr() {
/*  79 */     return this.out;
/*     */   }
/*  81 */   private int cmdStart = 0;
/*  82 */   private Vector history = new Vector();
/*     */   private String startedLine;
/*  84 */   private int histLine = 0;
/*     */   
/*     */   private JPopupMenu menu;
/*     */   
/*     */   private JTextPane text;
/*     */   private DefaultStyledDocument doc;
/*     */   NameCompletion nameCompletion;
/*  91 */   final int SHOW_AMBIG_MAX = 10;
/*     */   
/*     */   private boolean gotUp = true;
/*     */   String ZEROS;
/*     */   
/*     */   public JConsole() {
/*  97 */     this((InputStream)null, (OutputStream)null);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void requestFocus() {
/* 166 */     super.requestFocus();
/* 167 */     this.text.requestFocus();
/*     */   }
/*     */   
/*     */   public void keyPressed(KeyEvent e) {
/* 171 */     type(e);
/* 172 */     this.gotUp = false;
/*     */   }
/*     */   
/*     */   public void keyTyped(KeyEvent e) {
/* 176 */     type(e);
/*     */   }
/*     */   
/*     */   public void keyReleased(KeyEvent e) {
/* 180 */     this.gotUp = true;
/* 181 */     type(e);
/*     */   }
/*     */   
/*     */   private synchronized void type(KeyEvent e) {
/* 185 */     switch (e.getKeyCode()) {
/*     */       
/*     */       case 10:
/* 188 */         if (e.getID() == 401 && 
/* 189 */           this.gotUp) {
/* 190 */           enter();
/* 191 */           resetCommandStart();
/* 192 */           this.text.setCaretPosition(this.cmdStart);
/*     */         } 
/*     */         
/* 195 */         e.consume();
/* 196 */         this.text.repaint();
/*     */ 
/*     */       
/*     */       case 38:
/* 200 */         if (e.getID() == 401) {
/* 201 */           historyUp();
/*     */         }
/* 203 */         e.consume();
/*     */ 
/*     */       
/*     */       case 40:
/* 207 */         if (e.getID() == 401) {
/* 208 */           historyDown();
/*     */         }
/* 210 */         e.consume();
/*     */ 
/*     */       
/*     */       case 8:
/*     */       case 37:
/*     */       case 127:
/* 216 */         if (this.text.getCaretPosition() <= this.cmdStart)
/*     */         {
/*     */           
/* 219 */           e.consume();
/*     */         }
/*     */ 
/*     */       
/*     */       case 39:
/* 224 */         forceCaretMoveToStart();
/*     */ 
/*     */       
/*     */       case 36:
/* 228 */         this.text.setCaretPosition(this.cmdStart);
/* 229 */         e.consume();
/*     */ 
/*     */       
/*     */       case 85:
/* 233 */         if ((e.getModifiers() & 0x2) > 0) {
/* 234 */           replaceRange("", this.cmdStart, textLength());
/* 235 */           this.histLine = 0;
/* 236 */           e.consume();
/*     */         } 
/*     */ 
/*     */ 
/*     */       
/*     */       case 16:
/*     */       case 17:
/*     */       case 18:
/*     */       case 19:
/*     */       case 20:
/*     */       case 27:
/*     */       case 112:
/*     */       case 113:
/*     */       case 114:
/*     */       case 115:
/*     */       case 116:
/*     */       case 117:
/*     */       case 118:
/*     */       case 119:
/*     */       case 120:
/*     */       case 121:
/*     */       case 122:
/*     */       case 123:
/*     */       case 145:
/*     */       case 154:
/*     */       case 155:
/*     */       case 157:
/*     */         return;
/*     */ 
/*     */ 
/*     */       
/*     */       case 67:
/* 268 */         if (this.text.getSelectedText() == null) {
/* 269 */           if ((e.getModifiers() & 0x2) > 0 && e.getID() == 401)
/*     */           {
/* 271 */             append("^C");
/*     */           }
/* 273 */           e.consume();
/*     */         } 
/*     */ 
/*     */       
/*     */       case 9:
/* 278 */         if (e.getID() == 402) {
/* 279 */           String part = this.text.getText().substring(this.cmdStart);
/* 280 */           doCommandCompletion(part);
/*     */         } 
/* 282 */         e.consume();
/*     */     } 
/*     */ 
/*     */     
/* 286 */     if ((e.getModifiers() & 0xE) == 0)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 292 */       forceCaretMoveToEnd();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 299 */     if (e.paramString().indexOf("Backspace") != -1)
/*     */     {
/* 301 */       if (this.text.getCaretPosition() <= this.cmdStart) {
/* 302 */         e.consume();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void doCommandCompletion(String part) {
/* 312 */     if (this.nameCompletion == null) {
/*     */       return;
/*     */     }
/* 315 */     int i = part.length() - 1;
/*     */ 
/*     */ 
/*     */     
/* 319 */     while (i >= 0 && (Character.isJavaIdentifierPart(part.charAt(i)) || part.charAt(i) == '.'))
/*     */     {
/*     */ 
/*     */       
/* 323 */       i--;
/*     */     }
/* 325 */     part = part.substring(i + 1);
/*     */     
/* 327 */     if (part.length() < 2) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 333 */     String[] complete = this.nameCompletion.completeName(part);
/* 334 */     if (complete.length == 0) {
/* 335 */       Toolkit.getDefaultToolkit().beep();
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 340 */     if (complete.length == 1 && !complete.equals(part)) {
/* 341 */       String append = complete[0].substring(part.length());
/* 342 */       append(append);
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 348 */     String line = this.text.getText();
/* 349 */     String command = line.substring(this.cmdStart);
/*     */     
/* 351 */     for (i = this.cmdStart; line.charAt(i) != '\n' && i > 0; i--);
/* 352 */     String prompt = line.substring(i + 1, this.cmdStart);
/*     */ 
/*     */     
/* 355 */     StringBuffer sb = new StringBuffer("\n");
/* 356 */     for (i = 0; i < complete.length && i < 10; i++)
/* 357 */       sb.append(complete[i] + "\n"); 
/* 358 */     if (i == 10) {
/* 359 */       sb.append("...\n");
/*     */     }
/* 361 */     print(sb, Color.gray);
/* 362 */     print(prompt);
/* 363 */     append(command);
/*     */   }
/*     */   
/*     */   private void resetCommandStart() {
/* 367 */     this.cmdStart = textLength();
/*     */   }
/*     */   
/*     */   private void append(String string) {
/* 371 */     int slen = textLength();
/* 372 */     this.text.select(slen, slen);
/* 373 */     this.text.replaceSelection(string);
/*     */   }
/*     */   
/*     */   private String replaceRange(Object s, int start, int end) {
/* 377 */     String st = s.toString();
/* 378 */     this.text.select(start, end);
/* 379 */     this.text.replaceSelection(st);
/*     */     
/* 381 */     return st;
/*     */   }
/*     */   
/*     */   private void forceCaretMoveToEnd() {
/* 385 */     if (this.text.getCaretPosition() < this.cmdStart)
/*     */     {
/* 387 */       this.text.setCaretPosition(textLength());
/*     */     }
/* 389 */     this.text.repaint();
/*     */   }
/*     */   
/*     */   private void forceCaretMoveToStart() {
/* 393 */     if (this.text.getCaretPosition() < this.cmdStart);
/*     */ 
/*     */     
/* 396 */     this.text.repaint();
/*     */   }
/*     */ 
/*     */   
/*     */   private void enter() {
/* 401 */     String s = getCmd();
/*     */     
/* 403 */     if (s.length() == 0) {
/* 404 */       s = ";\n";
/*     */     } else {
/* 406 */       this.history.addElement(s);
/* 407 */       s = s + "\n";
/*     */     } 
/*     */     
/* 410 */     append("\n");
/* 411 */     this.histLine = 0;
/* 412 */     acceptLine(s);
/* 413 */     this.text.repaint();
/*     */   }
/*     */   
/*     */   private String getCmd() {
/* 417 */     String s = "";
/*     */     try {
/* 419 */       s = this.text.getText(this.cmdStart, textLength() - this.cmdStart);
/* 420 */     } catch (BadLocationException e) {
/*     */       
/* 422 */       System.out.println("Internal JConsole Error: " + e);
/*     */     } 
/* 424 */     return s;
/*     */   }
/*     */   
/*     */   private void historyUp() {
/* 428 */     if (this.history.size() == 0)
/*     */       return; 
/* 430 */     if (this.histLine == 0)
/* 431 */       this.startedLine = getCmd(); 
/* 432 */     if (this.histLine < this.history.size()) {
/* 433 */       this.histLine++;
/* 434 */       showHistoryLine();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void historyDown() {
/* 439 */     if (this.histLine == 0) {
/*     */       return;
/*     */     }
/* 442 */     this.histLine--;
/* 443 */     showHistoryLine();
/*     */   }
/*     */   
/*     */   private void showHistoryLine() {
/*     */     String showline;
/* 448 */     if (this.histLine == 0) {
/* 449 */       showline = this.startedLine;
/*     */     } else {
/* 451 */       showline = this.history.elementAt(this.history.size() - this.histLine);
/*     */     } 
/* 453 */     replaceRange(showline, this.cmdStart, textLength());
/* 454 */     this.text.setCaretPosition(textLength());
/* 455 */     this.text.repaint();
/*     */   }
/*     */   
/* 458 */   public JConsole(InputStream cin, OutputStream cout) { this.ZEROS = "000"; this.text = new JTextPane(this.doc = new DefaultStyledDocument()) {
/*     */         public void cut() { if (JConsole.this.text.getCaretPosition() < JConsole.this.cmdStart) { copy(); } else { super.cut(); }  } public void paste() { JConsole.this.forceCaretMoveToEnd(); super.paste(); }
/*     */       }; Font font = new Font("Monospaced", 0, 14); this.text.setText(""); this.text.setFont(font); this.text.setMargin(new Insets(7, 5, 7, 5)); this.text.addKeyListener(this); setViewportView(this.text); this.menu = new JPopupMenu("JConsole\tMenu"); this.menu.add(new JMenuItem("Cut")).addActionListener(this); this.menu.add(new JMenuItem("Copy")).addActionListener(this); this.menu.add(new JMenuItem("Paste")).addActionListener(this); this.text.addMouseListener(this); UIManager.addPropertyChangeListener(this); this.outPipe = cout; if (this.outPipe == null) { this.outPipe = new PipedOutputStream(); try { this.in = new PipedInputStream((PipedOutputStream)this.outPipe); } catch (IOException e) { print("Console internal\terror (1)...", Color.red); }  }
/*     */      this.inPipe = cin; if (this.inPipe == null) { PipedOutputStream pout = new PipedOutputStream(); this.out = new PrintStream(pout); try { this.inPipe = new BlockingPipedInputStream(pout); }
/*     */       catch (IOException e) { print("Console internal error: " + e); }
/*     */        }
/* 464 */      (new Thread(this)).start(); requestFocus(); } private void acceptLine(String line) { StringBuffer buf = new StringBuffer();
/* 465 */     int lineLength = line.length();
/* 466 */     for (int i = 0; i < lineLength; i++) {
/* 467 */       String val = Integer.toString(line.charAt(i), 16);
/* 468 */       val = this.ZEROS.substring(0, 4 - val.length()) + val;
/* 469 */       buf.append("\\u" + val);
/*     */     } 
/* 471 */     line = buf.toString();
/*     */ 
/*     */     
/* 474 */     if (this.outPipe == null) {
/* 475 */       print("Console internal\terror: cannot output ...", Color.red);
/*     */     } else {
/*     */       try {
/* 478 */         this.outPipe.write(line.getBytes());
/* 479 */         this.outPipe.flush();
/* 480 */       } catch (IOException e) {
/* 481 */         this.outPipe = null;
/* 482 */         throw new RuntimeException("Console pipe broken...");
/*     */       } 
/*     */     }  }
/*     */ 
/*     */   
/*     */   public void println(Object o) {
/* 488 */     print(String.valueOf(o) + "\n");
/* 489 */     this.text.repaint();
/*     */   }
/*     */   
/*     */   public void print(final Object o) {
/* 493 */     invokeAndWait(new Runnable() {
/*     */           public void run() {
/* 495 */             JConsole.this.append(String.valueOf(o));
/* 496 */             JConsole.this.resetCommandStart();
/* 497 */             JConsole.this.text.setCaretPosition(JConsole.this.cmdStart);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void println() {
/* 506 */     print("\n");
/* 507 */     this.text.repaint();
/*     */   }
/*     */   
/*     */   public void error(Object o) {
/* 511 */     print(o, Color.red);
/*     */   }
/*     */   
/*     */   public void println(Icon icon) {
/* 515 */     print(icon);
/* 516 */     println();
/* 517 */     this.text.repaint();
/*     */   }
/*     */   
/*     */   public void print(final Icon icon) {
/* 521 */     if (icon == null) {
/*     */       return;
/*     */     }
/* 524 */     invokeAndWait(new Runnable() {
/*     */           public void run() {
/* 526 */             JConsole.this.text.insertIcon(icon);
/* 527 */             JConsole.this.resetCommandStart();
/* 528 */             JConsole.this.text.setCaretPosition(JConsole.this.cmdStart);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public void print(Object s, Font font) {
/* 534 */     print(s, font, (Color)null);
/*     */   }
/*     */   
/*     */   public void print(Object s, Color color) {
/* 538 */     print(s, (Font)null, color);
/*     */   }
/*     */   
/*     */   public void print(final Object o, final Font font, final Color color) {
/* 542 */     invokeAndWait(new Runnable() {
/*     */           public void run() {
/* 544 */             AttributeSet old = JConsole.this.getStyle();
/* 545 */             JConsole.this.setStyle(font, color);
/* 546 */             JConsole.this.append(String.valueOf(o));
/* 547 */             JConsole.this.resetCommandStart();
/* 548 */             JConsole.this.text.setCaretPosition(JConsole.this.cmdStart);
/* 549 */             JConsole.this.setStyle(old, true);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void print(Object s, String fontFamilyName, int size, Color color) {
/* 561 */     print(s, fontFamilyName, size, color, false, false, false);
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
/*     */   public void print(final Object o, final String fontFamilyName, final int size, final Color color, final boolean bold, final boolean italic, final boolean underline) {
/* 574 */     invokeAndWait(new Runnable() {
/*     */           public void run() {
/* 576 */             AttributeSet old = JConsole.this.getStyle();
/* 577 */             JConsole.this.setStyle(fontFamilyName, size, color, bold, italic, underline);
/* 578 */             JConsole.this.append(String.valueOf(o));
/* 579 */             JConsole.this.resetCommandStart();
/* 580 */             JConsole.this.text.setCaretPosition(JConsole.this.cmdStart);
/* 581 */             JConsole.this.setStyle(old, true);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   private AttributeSet setStyle(Font font) {
/* 587 */     return setStyle(font, (Color)null);
/*     */   }
/*     */   
/*     */   private AttributeSet setStyle(Color color) {
/* 591 */     return setStyle((Font)null, color);
/*     */   }
/*     */ 
/*     */   
/*     */   private AttributeSet setStyle(Font font, Color color) {
/* 596 */     if (font != null) {
/* 597 */       return setStyle(font.getFamily(), font.getSize(), color, font.isBold(), font.isItalic(), StyleConstants.isUnderline(getStyle()));
/*     */     }
/*     */ 
/*     */     
/* 601 */     return setStyle((String)null, -1, color);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private AttributeSet setStyle(String fontFamilyName, int size, Color color) {
/* 607 */     MutableAttributeSet attr = new SimpleAttributeSet();
/* 608 */     if (color != null)
/* 609 */       StyleConstants.setForeground(attr, color); 
/* 610 */     if (fontFamilyName != null)
/* 611 */       StyleConstants.setFontFamily(attr, fontFamilyName); 
/* 612 */     if (size != -1) {
/* 613 */       StyleConstants.setFontSize(attr, size);
/*     */     }
/* 615 */     setStyle(attr);
/*     */     
/* 617 */     return getStyle();
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
/*     */   private AttributeSet setStyle(String fontFamilyName, int size, Color color, boolean bold, boolean italic, boolean underline) {
/* 629 */     MutableAttributeSet attr = new SimpleAttributeSet();
/* 630 */     if (color != null)
/* 631 */       StyleConstants.setForeground(attr, color); 
/* 632 */     if (fontFamilyName != null)
/* 633 */       StyleConstants.setFontFamily(attr, fontFamilyName); 
/* 634 */     if (size != -1)
/* 635 */       StyleConstants.setFontSize(attr, size); 
/* 636 */     StyleConstants.setBold(attr, bold);
/* 637 */     StyleConstants.setItalic(attr, italic);
/* 638 */     StyleConstants.setUnderline(attr, underline);
/*     */     
/* 640 */     setStyle(attr);
/*     */     
/* 642 */     return getStyle();
/*     */   }
/*     */   
/*     */   private void setStyle(AttributeSet attributes) {
/* 646 */     setStyle(attributes, false);
/*     */   }
/*     */   
/*     */   private void setStyle(AttributeSet attributes, boolean overWrite) {
/* 650 */     this.text.setCharacterAttributes(attributes, overWrite);
/*     */   }
/*     */   
/*     */   private AttributeSet getStyle() {
/* 654 */     return this.text.getCharacterAttributes();
/*     */   }
/*     */   
/*     */   public void setFont(Font font) {
/* 658 */     super.setFont(font);
/*     */     
/* 660 */     if (this.text != null)
/* 661 */       this.text.setFont(font); 
/*     */   }
/*     */   
/*     */   private void inPipeWatcher() throws IOException {
/* 665 */     byte[] ba = new byte[256];
/*     */     int read;
/* 667 */     while ((read = this.inPipe.read(ba)) != -1) {
/* 668 */       print(new String(ba, 0, read));
/*     */     }
/*     */ 
/*     */     
/* 672 */     println("Console: Input\tclosed...");
/*     */   }
/*     */   
/*     */   public void run() {
/*     */     try {
/* 677 */       inPipeWatcher();
/* 678 */     } catch (IOException e) {
/* 679 */       print("Console: I/O Error: " + e + "\n", Color.red);
/*     */     } 
/*     */   }
/*     */   
/*     */   public String toString() {
/* 684 */     return "BeanShell console";
/*     */   }
/*     */ 
/*     */   
/*     */   public void mouseClicked(MouseEvent event) {}
/*     */ 
/*     */   
/*     */   public void mousePressed(MouseEvent event) {
/* 692 */     if (event.isPopupTrigger()) {
/* 693 */       this.menu.show((Component)event.getSource(), event.getX(), event.getY());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void mouseReleased(MouseEvent event) {
/* 699 */     if (event.isPopupTrigger()) {
/* 700 */       this.menu.show((Component)event.getSource(), event.getX(), event.getY());
/*     */     }
/*     */     
/* 703 */     this.text.repaint();
/*     */   }
/*     */ 
/*     */   
/*     */   public void mouseEntered(MouseEvent event) {}
/*     */   
/*     */   public void mouseExited(MouseEvent event) {}
/*     */   
/*     */   public void propertyChange(PropertyChangeEvent event) {
/* 712 */     if (event.getPropertyName().equals("lookAndFeel")) {
/* 713 */       SwingUtilities.updateComponentTreeUI(this.menu);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void actionPerformed(ActionEvent event) {
/* 719 */     String cmd = event.getActionCommand();
/* 720 */     if (cmd.equals("Cut")) {
/* 721 */       this.text.cut();
/* 722 */     } else if (cmd.equals("Copy")) {
/* 723 */       this.text.copy();
/* 724 */     } else if (cmd.equals("Paste")) {
/* 725 */       this.text.paste();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void invokeAndWait(Runnable run) {
/* 733 */     if (!SwingUtilities.isEventDispatchThread()) {
/*     */       try {
/* 735 */         SwingUtilities.invokeAndWait(run);
/* 736 */       } catch (Exception e) {
/*     */         
/* 738 */         e.printStackTrace();
/*     */       } 
/*     */     } else {
/* 741 */       run.run();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class BlockingPipedInputStream
/*     */     extends PipedInputStream
/*     */   {
/*     */     boolean closed;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public BlockingPipedInputStream(PipedOutputStream pout) throws IOException {
/* 762 */       super(pout);
/*     */     }
/*     */     public synchronized int read() throws IOException {
/* 765 */       if (this.closed) {
/* 766 */         throw new IOException("stream closed");
/*     */       }
/* 768 */       while (this.in < 0) {
/* 769 */         notifyAll();
/*     */         try {
/* 771 */           wait(750L);
/* 772 */         } catch (InterruptedException e) {
/* 773 */           throw new InterruptedIOException();
/*     */         } 
/*     */       } 
/*     */       
/* 777 */       int ret = this.buffer[this.out++] & 0xFF;
/* 778 */       if (this.out >= this.buffer.length)
/* 779 */         this.out = 0; 
/* 780 */       if (this.in == this.out)
/* 781 */         this.in = -1; 
/* 782 */       return ret;
/*     */     }
/*     */     public void close() throws IOException {
/* 785 */       this.closed = true;
/* 786 */       super.close();
/*     */     }
/*     */   }
/*     */   
/*     */   public void setNameCompletion(NameCompletion nc) {
/* 791 */     this.nameCompletion = nc;
/*     */   }
/*     */   
/*     */   public void setWaitFeedback(boolean on) {
/* 795 */     if (on) {
/* 796 */       setCursor(Cursor.getPredefinedCursor(3));
/*     */     } else {
/* 798 */       setCursor(Cursor.getPredefinedCursor(0));
/*     */     } 
/*     */   } private int textLength() {
/* 801 */     return this.text.getDocument().getLength();
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/util/JConsole.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */