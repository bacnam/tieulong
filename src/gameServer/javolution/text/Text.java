/*      */ package javolution.text;
/*      */ 
/*      */ import java.io.PrintStream;
/*      */ import javolution.lang.MathLib;
/*      */ import javolution.lang.Realtime;
/*      */ import javolution.lang.ValueType;
/*      */ import javolution.util.FastMap;
/*      */ import javolution.util.function.Equalities;
/*      */ import javolution.xml.XMLSerializable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ @Realtime
/*      */ public final class Text
/*      */   implements CharSequence, Comparable<CharSequence>, XMLSerializable, ValueType<Text>
/*      */ {
/*      */   private static final long serialVersionUID = 1536L;
/*      */   private static final int BLOCK_SIZE = 32;
/*      */   private static final int BLOCK_MASK = -32;
/*   79 */   private static final FastMap<Text, Text> INTERN = (new FastMap()).shared();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   85 */   public static final Text EMPTY = (new Text("")).intern();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final char[] _data;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int _count;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Text _head;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Text _tail;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Text(boolean isPrimitive) {
/*  113 */     this._data = isPrimitive ? new char[32] : null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Text(String str) {
/*  123 */     this((str.length() <= 32));
/*  124 */     this._count = str.length();
/*  125 */     if (this._data != null) {
/*  126 */       str.getChars(0, this._count, this._data, 0);
/*      */     } else {
/*  128 */       int half = this._count + 32 >> 1 & 0xFFFFFFE0;
/*  129 */       this._head = new Text(str.substring(0, half));
/*  130 */       this._tail = new Text(str.substring(half, this._count));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Text valueOf(Object obj) {
/*  141 */     return (new TextBuilder()).append(obj).toText();
/*      */   }
/*      */   
/*      */   private static Text valueOf(String str) {
/*  145 */     return valueOf(str, 0, str.length());
/*      */   }
/*      */   
/*      */   private static Text valueOf(String str, int start, int end) {
/*  149 */     int length = end - start;
/*  150 */     if (length <= 32) {
/*  151 */       Text text = newPrimitive(length);
/*  152 */       str.getChars(start, end, text._data, 0);
/*  153 */       return text;
/*      */     } 
/*  155 */     int half = length + 32 >> 1 & 0xFFFFFFE0;
/*  156 */     return newComposite(valueOf(str, start, start + half), valueOf(str, start + half, end));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Text valueOf(char[] chars) {
/*  169 */     return valueOf(chars, 0, chars.length);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Text valueOf(char[] chars, int offset, int length) {
/*  184 */     if (offset < 0 || length < 0 || offset + length > chars.length)
/*  185 */       throw new IndexOutOfBoundsException(); 
/*  186 */     if (length <= 32) {
/*  187 */       Text text = newPrimitive(length);
/*  188 */       System.arraycopy(chars, offset, text._data, 0, length);
/*  189 */       return text;
/*      */     } 
/*  191 */     int half = length + 32 >> 1 & 0xFFFFFFE0;
/*  192 */     return newComposite(valueOf(chars, offset, half), valueOf(chars, offset + half, length - half));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static Text valueOf(TextBuilder tb, int start, int end) {
/*  206 */     int length = end - start;
/*  207 */     if (length <= 32) {
/*  208 */       Text text = newPrimitive(length);
/*  209 */       tb.getChars(start, end, text._data, 0);
/*  210 */       return text;
/*      */     } 
/*  212 */     int half = length + 32 >> 1 & 0xFFFFFFE0;
/*  213 */     return newComposite(valueOf(tb, start, start + half), valueOf(tb, start + half, end));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Text valueOf(boolean b) {
/*  227 */     return b ? TRUE : FALSE;
/*      */   }
/*      */   
/*  230 */   private static final Text TRUE = (new Text("true")).intern();
/*      */   
/*  232 */   private static final Text FALSE = (new Text("false")).intern();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Text valueOf(char c) {
/*  241 */     Text text = newPrimitive(1);
/*  242 */     text._data[0] = c;
/*  243 */     return text;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Text valueOf(int i) {
/*  254 */     TextBuilder tb = new TextBuilder();
/*  255 */     return tb.append(i).toText();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Text valueOf(int i, int radix) {
/*  267 */     TextBuilder tb = new TextBuilder();
/*  268 */     return tb.append(i, radix).toText();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Text valueOf(long l) {
/*  279 */     TextBuilder tb = new TextBuilder();
/*  280 */     return tb.append(l).toText();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Text valueOf(long l, int radix) {
/*  292 */     TextBuilder tb = new TextBuilder();
/*  293 */     return tb.append(l, radix).toText();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Text valueOf(float f) {
/*  304 */     TextBuilder tb = new TextBuilder();
/*  305 */     return tb.append(f).toText();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Text valueOf(double d) {
/*  316 */     TextBuilder tb = new TextBuilder();
/*  317 */     return tb.append(d).toText();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Text valueOf(double d, int digits, boolean scientific, boolean showZero) {
/*  337 */     TextBuilder tb = new TextBuilder();
/*  338 */     return tb.append(d, digits, scientific, showZero).toText();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int length() {
/*  347 */     return this._count;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Text plus(Object obj) {
/*  358 */     return concat(valueOf(obj));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Text plus(String str) {
/*  370 */     Text merge = append(str);
/*  371 */     return (merge != null) ? merge : concat(valueOf(str));
/*      */   }
/*      */   
/*      */   private Text append(String str) {
/*  375 */     int length = str.length();
/*  376 */     if (this._data == null) {
/*  377 */       Text merge = this._tail.append(str);
/*  378 */       return (merge != null) ? newComposite(this._head, merge) : null;
/*      */     } 
/*  380 */     if (this._count + length > 32)
/*  381 */       return null; 
/*  382 */     Text text = newPrimitive(this._count + length);
/*  383 */     System.arraycopy(this._data, 0, text._data, 0, this._count);
/*  384 */     str.getChars(0, length, text._data, this._count);
/*  385 */     return text;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Text concat(Text that) {
/*  402 */     int length = this._count + that._count;
/*  403 */     if (length <= 32) {
/*  404 */       Text text = newPrimitive(length);
/*  405 */       getChars(0, this._count, text._data, 0);
/*  406 */       that.getChars(0, that._count, text._data, this._count);
/*  407 */       return text;
/*      */     } 
/*      */     
/*  410 */     Text head = this;
/*  411 */     Text tail = that;
/*      */     
/*  413 */     if (head._count << 1 < tail._count && tail._data == null) {
/*      */       
/*  415 */       if (tail._head._count > tail._tail._count)
/*      */       {
/*  417 */         tail = tail.rightRotation();
/*      */       }
/*  419 */       head = head.concat(tail._head);
/*  420 */       tail = tail._tail;
/*      */     }
/*  422 */     else if (tail._count << 1 < head._count && head._data == null) {
/*      */ 
/*      */       
/*  425 */       if (head._tail._count > head._head._count)
/*      */       {
/*  427 */         head = head.leftRotation();
/*      */       }
/*  429 */       tail = head._tail.concat(tail);
/*  430 */       head = head._head;
/*      */     } 
/*  432 */     return newComposite(head, tail);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private Text rightRotation() {
/*  438 */     Text P = this._head;
/*  439 */     if (P._data != null)
/*  440 */       return this; 
/*  441 */     Text A = P._head;
/*  442 */     Text B = P._tail;
/*  443 */     Text C = this._tail;
/*  444 */     return newComposite(A, newComposite(B, C));
/*      */   }
/*      */ 
/*      */   
/*      */   private Text leftRotation() {
/*  449 */     Text Q = this._tail;
/*  450 */     if (Q._data != null)
/*  451 */       return this; 
/*  452 */     Text B = Q._head;
/*  453 */     Text C = Q._tail;
/*  454 */     Text A = this._head;
/*  455 */     return newComposite(newComposite(A, B), C);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Text subtext(int start) {
/*  467 */     return subtext(start, length());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Text insert(int index, Text txt) {
/*  481 */     return subtext(0, index).concat(txt).concat(subtext(index));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Text delete(int start, int end) {
/*  494 */     if (start > end)
/*  495 */       throw new IndexOutOfBoundsException(); 
/*  496 */     return subtext(0, start).concat(subtext(end));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Text replace(CharSequence target, CharSequence replacement) {
/*  509 */     int i = indexOf(target);
/*  510 */     return (i < 0) ? this : subtext(0, i).concat(valueOf(replacement)).concat(subtext(i + target.length()).replace(target, replacement));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Text replace(CharSet charSet, CharSequence replacement) {
/*  525 */     int i = indexOfAny(charSet);
/*  526 */     return (i < 0) ? this : subtext(0, i).concat(valueOf(replacement)).concat(subtext(i + 1).replace(charSet, replacement));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CharSequence subSequence(int start, int end) {
/*  541 */     return subtext(start, end);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int indexOf(CharSequence csq) {
/*  553 */     return indexOf(csq, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int indexOf(CharSequence csq, int fromIndex) {
/*  570 */     int csqLength = csq.length();
/*  571 */     int min = Math.max(0, fromIndex);
/*  572 */     int max = this._count - csqLength;
/*  573 */     if (csqLength == 0) return (min > max) ? -1 : min;
/*      */ 
/*      */     
/*  576 */     char c = csq.charAt(0); int i;
/*  577 */     for (i = indexOf(c, min); i >= 0 && i <= max; i = indexOf(c, ++i)) {
/*      */       
/*  579 */       boolean match = true;
/*  580 */       for (int j = 1; j < csqLength; j++) {
/*  581 */         if (charAt(i + j) != csq.charAt(j)) {
/*  582 */           match = false;
/*      */           break;
/*      */         } 
/*      */       } 
/*  586 */       if (match) return i; 
/*      */     } 
/*  588 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int lastIndexOf(CharSequence csq) {
/*  600 */     return lastIndexOf(csq, this._count);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int lastIndexOf(CharSequence csq, int fromIndex) {
/*  616 */     int csqLength = csq.length();
/*  617 */     int min = 0;
/*  618 */     int max = Math.min(fromIndex, this._count - csqLength);
/*  619 */     if (csqLength == 0) return (0 > max) ? -1 : max;
/*      */ 
/*      */     
/*  622 */     char c = csq.charAt(0); int i;
/*  623 */     for (i = lastIndexOf(c, max); i >= 0; i = lastIndexOf(c, --i)) {
/*  624 */       boolean match = true;
/*  625 */       for (int j = 1; j < csqLength; j++) {
/*  626 */         if (charAt(i + j) != csq.charAt(j)) {
/*  627 */           match = false;
/*      */           break;
/*      */         } 
/*      */       } 
/*  631 */       if (match) return i; 
/*      */     } 
/*  633 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean startsWith(CharSequence prefix) {
/*  646 */     return startsWith(prefix, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean endsWith(CharSequence suffix) {
/*  658 */     return startsWith(suffix, length() - suffix.length());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean startsWith(CharSequence prefix, int index) {
/*  670 */     int prefixLength = prefix.length();
/*  671 */     if (index >= 0 && index <= length() - prefixLength) {
/*  672 */       for (int i = 0, j = index; i < prefixLength;) {
/*  673 */         if (prefix.charAt(i++) != charAt(j++)) return false; 
/*      */       } 
/*  675 */       return true;
/*      */     } 
/*  677 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Text trim() {
/*  690 */     int first = 0;
/*  691 */     int last = length() - 1;
/*  692 */     while (first <= last && charAt(first) <= ' ') {
/*  693 */       first++;
/*      */     }
/*  695 */     while (last >= first && charAt(last) <= ' ') {
/*  696 */       last--;
/*      */     }
/*  698 */     return subtext(first, last + 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Text intern() {
/*  708 */     Text txt = (Text)INTERN.putIfAbsent(this, this);
/*  709 */     return (txt == null) ? this : txt;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean contentEquals(CharSequence csq) {
/*  721 */     if (csq.length() != this._count)
/*  722 */       return false; 
/*  723 */     for (int i = 0; i < this._count;) {
/*  724 */       if (charAt(i) != csq.charAt(i++))
/*  725 */         return false; 
/*      */     } 
/*  727 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean contentEqualsIgnoreCase(CharSequence csq) {
/*  739 */     if (this._count != csq.length())
/*  740 */       return false; 
/*  741 */     for (int i = 0; i < this._count; ) {
/*  742 */       char u1 = charAt(i);
/*  743 */       char u2 = csq.charAt(i++);
/*  744 */       if (u1 != u2) {
/*  745 */         u1 = Character.toUpperCase(u1);
/*  746 */         u2 = Character.toUpperCase(u2);
/*  747 */         if (u1 != u2 && Character.toLowerCase(u1) != Character.toLowerCase(u2))
/*      */         {
/*      */           
/*  750 */           return false;
/*      */         }
/*      */       } 
/*      */     } 
/*  754 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean equals(Object obj) {
/*  770 */     if (this == obj)
/*  771 */       return true; 
/*  772 */     if (!(obj instanceof Text))
/*  773 */       return false; 
/*  774 */     Text that = (Text)obj;
/*  775 */     if (this._count != that._count)
/*  776 */       return false; 
/*  777 */     for (int i = 0; i < this._count;) {
/*  778 */       if (charAt(i) != that.charAt(i++))
/*  779 */         return false; 
/*      */     } 
/*  781 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int hashCode() {
/*  791 */     int h = 0;
/*  792 */     int length = length();
/*  793 */     for (int i = 0; i < length;) {
/*  794 */       h = 31 * h + charAt(i++);
/*      */     }
/*  796 */     return h;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int compareTo(CharSequence csq) {
/*  809 */     return Equalities.LEXICAL.compare(this, csq);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Text toText() {
/*  819 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void printStatistics(PrintStream out) {
/*  828 */     int length = length();
/*  829 */     int leaves = getNbrOfLeaves();
/*  830 */     synchronized (out) {
/*  831 */       out.print("LENGTH: " + length());
/*  832 */       out.print(", MAX DEPTH: " + getDepth());
/*  833 */       out.print(", NBR OF BRANCHES: " + getNbrOfBranches());
/*  834 */       out.print(", NBR OF LEAVES: " + leaves);
/*  835 */       out.print(", AVG LEAVE LENGTH: " + ((length + (leaves >> 1)) / leaves));
/*      */       
/*  837 */       out.println();
/*      */     } 
/*      */   }
/*      */   
/*      */   private int getDepth() {
/*  842 */     if (this._data != null)
/*  843 */       return 0; 
/*  844 */     return MathLib.max(this._head.getDepth(), this._tail.getDepth()) + 1;
/*      */   }
/*      */   
/*      */   private int getNbrOfBranches() {
/*  848 */     return (this._data == null) ? (this._head.getNbrOfBranches() + this._tail.getNbrOfBranches() + 1) : 0;
/*      */   }
/*      */ 
/*      */   
/*      */   private int getNbrOfLeaves() {
/*  853 */     return (this._data == null) ? (this._head.getNbrOfLeaves() + this._tail.getNbrOfLeaves()) : 1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Text toLowerCase() {
/*  864 */     if (this._data == null)
/*  865 */       return newComposite(this._head.toLowerCase(), this._tail.toLowerCase()); 
/*  866 */     Text text = newPrimitive(this._count);
/*  867 */     for (int i = 0; i < this._count;) {
/*  868 */       text._data[i] = Character.toLowerCase(this._data[i++]);
/*      */     }
/*  870 */     return text;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Text toUpperCase() {
/*  880 */     if (this._data == null)
/*  881 */       return newComposite(this._head.toUpperCase(), this._tail.toUpperCase()); 
/*  882 */     Text text = newPrimitive(this._count);
/*  883 */     for (int i = 0; i < this._count;) {
/*  884 */       text._data[i] = Character.toUpperCase(this._data[i++]);
/*      */     }
/*  886 */     return text;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public char charAt(int index) {
/*  898 */     if (index >= this._count)
/*  899 */       throw new IndexOutOfBoundsException(); 
/*  900 */     return (this._data != null) ? this._data[index] : ((index < this._head._count) ? this._head.charAt(index) : this._tail.charAt(index - this._head._count));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int indexOf(char c) {
/*  914 */     return indexOf(c, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int indexOf(char c, int fromIndex) {
/*  928 */     if (this._data != null) {
/*  929 */       for (int i = MathLib.max(fromIndex, 0); i < this._count; i++) {
/*  930 */         if (this._data[i] == c)
/*  931 */           return i; 
/*      */       } 
/*  933 */       return -1;
/*      */     } 
/*  935 */     int cesure = this._head._count;
/*  936 */     if (fromIndex < cesure) {
/*  937 */       int headIndex = this._head.indexOf(c, fromIndex);
/*  938 */       if (headIndex >= 0)
/*  939 */         return headIndex; 
/*      */     } 
/*  941 */     int tailIndex = this._tail.indexOf(c, fromIndex - cesure);
/*  942 */     return (tailIndex >= 0) ? (tailIndex + cesure) : -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int lastIndexOf(char c, int fromIndex) {
/*  958 */     if (this._data != null) {
/*  959 */       for (int i = MathLib.min(fromIndex, this._count - 1); i >= 0; i--) {
/*  960 */         if (this._data[i] == c)
/*  961 */           return i; 
/*      */       } 
/*  963 */       return -1;
/*      */     } 
/*  965 */     int cesure = this._head._count;
/*  966 */     if (fromIndex >= cesure) {
/*  967 */       int tailIndex = this._tail.lastIndexOf(c, fromIndex - cesure);
/*  968 */       if (tailIndex >= 0)
/*  969 */         return tailIndex + cesure; 
/*      */     } 
/*  971 */     return this._head.lastIndexOf(c, fromIndex);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Text subtext(int start, int end) {
/*  986 */     if (this._data != null) {
/*  987 */       if (start < 0 || start > end || end > this._count)
/*  988 */         throw new IndexOutOfBoundsException(); 
/*  989 */       if (start == 0 && end == this._count)
/*  990 */         return this; 
/*  991 */       if (start == end)
/*  992 */         return EMPTY; 
/*  993 */       int length = end - start;
/*  994 */       Text text = newPrimitive(length);
/*  995 */       System.arraycopy(this._data, start, text._data, 0, length);
/*  996 */       return text;
/*      */     } 
/*  998 */     int cesure = this._head._count;
/*  999 */     if (end <= cesure)
/* 1000 */       return this._head.subtext(start, end); 
/* 1001 */     if (start >= cesure)
/* 1002 */       return this._tail.subtext(start - cesure, end - cesure); 
/* 1003 */     if (start == 0 && end == this._count) {
/* 1004 */       return this;
/*      */     }
/* 1006 */     return this._head.subtext(start, cesure).concat(this._tail.subtext(0, end - cesure));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void getChars(int start, int end, char[] dest, int destPos) {
/* 1023 */     if (this._data != null) {
/* 1024 */       if (start < 0 || end > this._count || start > end)
/* 1025 */         throw new IndexOutOfBoundsException(); 
/* 1026 */       System.arraycopy(this._data, start, dest, destPos, end - start);
/*      */     } else {
/* 1028 */       int cesure = this._head._count;
/* 1029 */       if (end <= cesure) {
/* 1030 */         this._head.getChars(start, end, dest, destPos);
/* 1031 */       } else if (start >= cesure) {
/* 1032 */         this._tail.getChars(start - cesure, end - cesure, dest, destPos);
/*      */       } else {
/* 1034 */         this._head.getChars(start, cesure, dest, destPos);
/* 1035 */         this._tail.getChars(0, end - cesure, dest, destPos + cesure - start);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1046 */     if (this._data != null) {
/* 1047 */       return new String(this._data, 0, this._count);
/*      */     }
/* 1049 */     char[] data = new char[this._count];
/* 1050 */     getChars(0, this._count, data, 0);
/* 1051 */     return new String(data, 0, this._count);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Text copy() {
/* 1057 */     if (this._data != null) {
/* 1058 */       Text text = newPrimitive(this._count);
/* 1059 */       System.arraycopy(this._data, 0, text._data, 0, this._count);
/* 1060 */       return text;
/*      */     } 
/* 1062 */     return newComposite(this._head.copy(), this._tail.copy());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Text valueOf(char c, int length) {
/* 1079 */     if (length < 0)
/* 1080 */       throw new IndexOutOfBoundsException(); 
/* 1081 */     if (length <= 32) {
/* 1082 */       Text text = newPrimitive(length);
/* 1083 */       for (int i = 0; i < length;) {
/* 1084 */         text._data[i++] = c;
/*      */       }
/* 1086 */       return text;
/*      */     } 
/* 1088 */     int middle = length >> 1;
/* 1089 */     return newComposite(valueOf(c, middle), valueOf(c, length - middle));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isBlank() {
/* 1101 */     return isBlank(0, length());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isBlank(int start, int length) {
/* 1112 */     for (; start < length; start++) {
/* 1113 */       if (charAt(start) > ' ')
/* 1114 */         return false; 
/*      */     } 
/* 1116 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Text trimStart() {
/* 1126 */     int first = 0;
/* 1127 */     int last = length() - 1;
/* 1128 */     while (first <= last && charAt(first) <= ' ') {
/* 1129 */       first++;
/*      */     }
/* 1131 */     return subtext(first, last + 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Text trimEnd() {
/* 1142 */     int first = 0;
/* 1143 */     int last = length() - 1;
/* 1144 */     while (last >= first && charAt(last) <= ' ') {
/* 1145 */       last--;
/*      */     }
/* 1147 */     return subtext(first, last + 1);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Text padLeft(int len) {
/* 1161 */     return padLeft(len, ' ');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Text padLeft(int len, char c) {
/* 1177 */     int padSize = (len <= length()) ? 0 : (len - length());
/* 1178 */     return insert(0, valueOf(c, padSize));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Text padRight(int len) {
/* 1192 */     return padRight(len, ' ');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Text padRight(int len, char c) {
/* 1208 */     int padSize = (len <= length()) ? 0 : (len - length());
/* 1209 */     return concat(valueOf(c, padSize));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int indexOfAny(CharSet charSet) {
/* 1221 */     return indexOfAny(charSet, 0, length());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int indexOfAny(CharSet charSet, int start) {
/* 1234 */     return indexOfAny(charSet, start, length() - start);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int indexOfAny(CharSet charSet, int start, int length) {
/* 1248 */     int stop = start + length;
/* 1249 */     for (int i = start; i < stop; i++) {
/* 1250 */       if (charSet.contains(charAt(i)))
/* 1251 */         return i; 
/*      */     } 
/* 1253 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int lastIndexOfAny(CharSet charSet) {
/* 1265 */     return lastIndexOfAny(charSet, 0, length());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int lastIndexOfAny(CharSet charSet, int start) {
/* 1278 */     return lastIndexOfAny(charSet, start, length() - start);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int lastIndexOfAny(CharSet charSet, int start, int length) {
/* 1292 */     for (int i = start + length; --i >= start;) {
/* 1293 */       if (charSet.contains(charAt(i)))
/* 1294 */         return i; 
/*      */     } 
/* 1296 */     return -1;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Text newPrimitive(int length) {
/* 1308 */     Text text = new Text(true);
/* 1309 */     text._count = length;
/* 1310 */     return text;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Text newComposite(Text head, Text tail) {
/* 1321 */     Text text = new Text(false);
/* 1322 */     head._count += tail._count;
/* 1323 */     text._head = head;
/* 1324 */     text._tail = tail;
/* 1325 */     return text;
/*      */   }
/*      */ 
/*      */   
/*      */   public Text value() {
/* 1330 */     return this;
/*      */   }
/*      */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/text/Text.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */