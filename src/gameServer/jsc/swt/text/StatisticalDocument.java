/*     */ package jsc.swt.text;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.Graphics;
/*     */ import javax.swing.text.AttributeSet;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.DefaultStyledDocument;
/*     */ import javax.swing.text.SimpleAttributeSet;
/*     */ import javax.swing.text.StyleConstants;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StatisticalDocument
/*     */   extends DefaultStyledDocument
/*     */ {
/*  22 */   private IntegerFormat integerFormatter = new IntegerFormat();
/*  23 */   private RealFormat realFormatter = new SigFigFormat(5);
/*     */   
/*  25 */   public SimpleAttributeSet attributes = new SimpleAttributeSet();
/*     */   public void add(String paramString) {
/*  27 */     insertString(getLength(), paramString);
/*     */   } public void add(int paramInt) {
/*  29 */     add(this.integerFormatter.format(paramInt));
/*     */   } public void add(double paramDouble) {
/*  31 */     add(this.realFormatter.format(paramDouble));
/*     */   }
/*     */   
/*     */   public void add(Component paramComponent) {
/*  35 */     SimpleAttributeSet simpleAttributeSet = new SimpleAttributeSet();
/*  36 */     StyleConstants.setComponent(simpleAttributeSet, paramComponent);
/*  37 */     insertString(getLength(), " ", simpleAttributeSet);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void add(DefaultStyledDocument paramDefaultStyledDocument) {
/*  43 */     for (byte b = 0; b < paramDefaultStyledDocument.getLength(); b++) {
/*     */       
/*     */       try {
/*  46 */         String str = paramDefaultStyledDocument.getText(b, 1);
/*  47 */         AttributeSet attributeSet = paramDefaultStyledDocument.getCharacterElement(b).getAttributes();
/*  48 */         insertString(getLength(), str, attributeSet);
/*     */       }
/*  50 */       catch (BadLocationException badLocationException) {}
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void addBreak(int paramInt) {
/*  56 */     add("\n");
/*  57 */     if (paramInt > 0) {
/*     */       
/*  59 */       int i = getFontSize();
/*  60 */       setFontSize(paramInt);
/*  61 */       add("\n");
/*  62 */       setFontSize(i);
/*     */     } 
/*     */   }
/*     */   public void addBullet() {
/*  66 */     bold("•");
/*     */   } public void addDash() {
/*  68 */     bold("–");
/*     */   }
/*     */   
/*     */   public void addDegrees() {
/*  72 */     String str = getFontFamily();
/*  73 */     int i = getFontSize();
/*  74 */     setFontFamily("Times");
/*     */     
/*  76 */     add("°");
/*  77 */     setFontFamily(str);
/*  78 */     setFontSize(i);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addDiv() {
/*  83 */     String str = getFontFamily();
/*  84 */     int i = getFontSize();
/*  85 */     setFontFamily("Times");
/*     */     
/*  87 */     add("÷");
/*  88 */     setFontFamily(str);
/*  89 */     setFontSize(i);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addDivider() {
/*  94 */     add("\n");
/*  95 */     add(new Divider(this));
/*  96 */     add("\n");
/*     */   }
/*     */ 
/*     */   
/*     */   public void addMinus() {
/* 101 */     String str = getFontFamily();
/* 102 */     int i = getFontSize();
/* 103 */     setFontFamily("Symbol");
/*     */     
/* 105 */     add("−");
/* 106 */     setFontFamily(str);
/* 107 */     setFontSize(i);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addMult() {
/* 112 */     String str = getFontFamily();
/* 113 */     int i = getFontSize();
/* 114 */     setFontFamily("Times");
/*     */     
/* 116 */     add("×");
/* 117 */     setFontFamily(str);
/* 118 */     setFontSize(i);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addPM() {
/* 123 */     String str = getFontFamily();
/* 124 */     int i = getFontSize();
/* 125 */     setFontFamily("Times");
/*     */     
/* 127 */     add("±");
/* 128 */     setFontFamily(str);
/* 129 */     setFontSize(i);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addSqrt() {
/* 134 */     String str = getFontFamily();
/* 135 */     int i = getFontSize();
/* 136 */     setFontFamily("Symbol");
/*     */     
/* 138 */     add("√");
/* 139 */     setFontFamily(str);
/* 140 */     setFontSize(i);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addSymbol(int paramInt) {
/* 145 */     String str = getFontFamily();
/* 146 */     int i = getFontSize();
/* 147 */     setFontFamily("Symbol");
/*     */     
/* 149 */     add("" + (char)paramInt);
/* 150 */     setFontFamily(str);
/* 151 */     setFontSize(i);
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
/*     */   public String getMatchString() {
/* 164 */     return getMatchString(true);
/*     */   }
/*     */   
/*     */   public String getMatchString(boolean paramBoolean) {
/* 168 */     StringBuffer stringBuffer = new StringBuffer();
/* 169 */     char c = Character.MIN_VALUE;
/* 170 */     for (byte b = 0; b < getLength(); b++) {
/*     */       
/*     */       try {
/* 173 */         String str = getText(b, 1);
/* 174 */         if (!paramBoolean || !str.equals(" ")) {
/* 175 */           AttributeSet attributeSet = getCharacterElement(b).getAttributes();
/*     */           
/* 177 */           char c1 = Character.MIN_VALUE;
/* 178 */           if (StyleConstants.isSuperscript(attributeSet)) { c1 = '^'; }
/* 179 */           else if (StyleConstants.isSubscript(attributeSet)) { c1 = '¬'; }
/*     */           
/* 181 */           if (c1 != '\000') {
/*     */             
/* 183 */             if (c != c1) stringBuffer.append(c1); 
/* 184 */             c = c1;
/*     */           }
/* 186 */           else if (c != '\000') {
/*     */             
/* 188 */             c = Character.MIN_VALUE;
/* 189 */             stringBuffer.append(':');
/*     */           } 
/*     */ 
/*     */           
/* 193 */           if (str.equals("^")) { stringBuffer.append('^'); }
/* 194 */           else if (str.equals("¬")) { stringBuffer.append('¬'); }
/* 195 */           else if (str.equals(":")) { stringBuffer.append(':'); }
/*     */           
/* 197 */           stringBuffer.append(str);
/*     */         } 
/* 199 */       } catch (BadLocationException badLocationException) {}
/*     */     } 
/* 201 */     return stringBuffer.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String greek(char paramChar) {
/* 208 */     return "" + (char)(paramChar - 65 + 913);
/*     */   }
/*     */   public void insertString(int paramInt, String paramString, AttributeSet paramAttributeSet) {
/*     */     
/* 212 */     try { super.insertString(paramInt, paramString, paramAttributeSet); } catch (BadLocationException badLocationException) {}
/*     */   }
/*     */   public void insertString(int paramInt, String paramString) {
/* 215 */     insertString(paramInt, paramString, this.attributes);
/*     */   }
/*     */   public void reset() {
/*     */     try {
/* 219 */       remove(0, getLength());
/* 220 */     } catch (BadLocationException badLocationException) {}
/*     */   }
/*     */   
/* 223 */   public void bold(String paramString) { bold(); add(paramString); unbold(); }
/* 224 */   public void bold() { StyleConstants.setBold(this.attributes, true); } public void unbold() {
/* 225 */     StyleConstants.setBold(this.attributes, false);
/*     */   }
/* 227 */   public void italic(String paramString) { italic(); add(paramString); unitalic(); }
/* 228 */   public void italic() { StyleConstants.setItalic(this.attributes, true); } public void unitalic() {
/* 229 */     StyleConstants.setItalic(this.attributes, false);
/*     */   }
/* 231 */   public void underline(String paramString) { underline(); add(paramString); ununderline(); }
/* 232 */   public void underline() { StyleConstants.setUnderline(this.attributes, true); } public void ununderline() {
/* 233 */     StyleConstants.setUnderline(this.attributes, false);
/*     */   }
/* 235 */   public void subscript(String paramString) { subscript(); add(paramString); unsubscript(); }
/* 236 */   public void subscript() { StyleConstants.setSubscript(this.attributes, true); } public void unsubscript() {
/* 237 */     StyleConstants.setSubscript(this.attributes, false);
/*     */   }
/* 239 */   public void superscript(String paramString) { superscript(); add(paramString); unsuperscript(); }
/* 240 */   public void superscript() { StyleConstants.setSuperscript(this.attributes, true); } public void unsuperscript() {
/* 241 */     StyleConstants.setSuperscript(this.attributes, false);
/*     */   }
/* 243 */   public void setAlignmentLeft() { setAlignment(0); }
/* 244 */   public void setAlignmentCentre() { setAlignment(1); }
/* 245 */   public void setAlignmentRight() { setAlignment(2); } public void setAlignment(int paramInt) {
/* 246 */     setAlignment(paramInt, 0, getLength());
/*     */   }
/*     */   public void setAlignment(int paramInt1, int paramInt2, int paramInt3) {
/* 249 */     StyleConstants.setAlignment(this.attributes, paramInt1);
/* 250 */     setParagraphAttributes(paramInt2, paramInt3, this.attributes, false);
/*     */   }
/*     */   
/* 253 */   public void setFontSize(int paramInt) { StyleConstants.setFontSize(this.attributes, paramInt); } public int getFontSize() {
/* 254 */     return StyleConstants.getFontSize(this.attributes);
/*     */   }
/* 256 */   public void setFontFamily(String paramString) { StyleConstants.setFontFamily(this.attributes, paramString); } public String getFontFamily() {
/* 257 */     return StyleConstants.getFontFamily(this.attributes);
/*     */   } public Font getFont() {
/* 259 */     return getFont(this.attributes);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRealFormat(RealFormat paramRealFormat) {
/* 267 */     this.realFormatter = paramRealFormat;
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
/*     */   class Divider
/*     */     extends Component
/*     */   {
/*     */     private final StatisticalDocument this$0;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Divider(StatisticalDocument this$0) {
/* 302 */       this.this$0 = this$0;
/*     */     } public void paint(Graphics param1Graphics) {
/* 304 */       param1Graphics.drawLine(0, 0, getWidth(), 0);
/*     */     }
/*     */     
/*     */     public Dimension getMaximumSize() {
/* 308 */       Dimension dimension = new Dimension(getSize());
/* 309 */       dimension.width = 32767;
/* 310 */       return dimension;
/*     */     }
/*     */ 
/*     */     
/*     */     public Dimension getSize() {
/* 315 */       Dimension dimension = new Dimension(1, 1);
/* 316 */       setSize(dimension);
/* 317 */       return dimension;
/*     */     }
/* 319 */     public Dimension getMinimumSize() { return getSize(); } public Dimension getPreferredSize() {
/* 320 */       return getSize();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/jsc/swt/text/StatisticalDocument.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */