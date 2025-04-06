/*      */ package javolution.xml.internal.stream;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.Reader;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.util.Map;
/*      */ import javolution.context.LogContext;
/*      */ import javolution.io.UTF8StreamReader;
/*      */ import javolution.lang.Realtime;
/*      */ import javolution.text.CharArray;
/*      */ import javolution.xml.sax.Attributes;
/*      */ import javolution.xml.stream.Location;
/*      */ import javolution.xml.stream.NamespaceContext;
/*      */ import javolution.xml.stream.XMLStreamException;
/*      */ import javolution.xml.stream.XMLStreamReader;
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
/*      */ public final class XMLStreamReaderImpl
/*      */   implements XMLStreamReader
/*      */ {
/*   43 */   static final String[] NAMES_OF_EVENTS = new String[] { "UNDEFINED", "START_ELEMENT", "END_ELEMENT", "PROCESSING_INSTRUCTIONS", "CHARACTERS", "COMMENT", "SPACE", "START_DOCUMENT", "END_DOCUMENT", "ENTITY_REFERENCE", "ATTRIBUTE", "DTD", "CDATA", "NAMESPACE", "NOTATION_DECLARATION", "ENTITY_DECLARATION" };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static final int READER_BUFFER_CAPACITY = 4096;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   CharArray _prolog;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int _readIndex;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int _readCount;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   72 */   private char[] _data = new char[8192];
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int _index;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int _depth;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private CharArray _qName;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int _prefixSep;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private CharArray _attrQName;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int _attrPrefixSep;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private CharArray _attrValue;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  112 */   private int _eventType = 7;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean _isEmpty;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   boolean _charactersPending = false;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int _start;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  132 */   private int _state = 1;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private CharArray _text;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Reader _reader;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  147 */   private final char[] _readBuffer = new char[4096];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private int _startOffset;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  158 */   private final LocationImpl _location = new LocationImpl();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  163 */   private final NamespacesImpl _namespaces = new NamespacesImpl();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  168 */   private final AttributesImpl _attributes = new AttributesImpl(this._namespaces);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  173 */   private CharArray[] _elemStack = new CharArray[16];
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String _encoding;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  183 */   private final EntitiesImpl _entities = new EntitiesImpl();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  188 */   private final UTF8StreamReader _utf8StreamReader = new UTF8StreamReader();
/*      */   
/*      */   private final XMLInputFactoryImpl _factory;
/*      */   
/*      */   private static final int STATE_CHARACTERS = 1;
/*      */   
/*      */   private static final int STATE_MARKUP = 2;
/*      */   private static final int STATE_COMMENT = 3;
/*      */   private static final int STATE_PI = 4;
/*      */   
/*      */   public XMLStreamReaderImpl() {
/*  199 */     this(null);
/*      */   }
/*      */   private static final int STATE_CDATA = 5; private static final int STATE_OPEN_TAGxREAD_ELEM_NAME = 6; private static final int STATE_OPEN_TAGxELEM_NAME_READ = 7; private static final int STATE_OPEN_TAGxREAD_ATTR_NAME = 8;
/*      */   private static final int STATE_OPEN_TAGxATTR_NAME_READ = 9;
/*      */   private static final int STATE_OPEN_TAGxEQUAL_READ = 10;
/*      */   private static final int STATE_OPEN_TAGxREAD_ATTR_VALUE_SIMPLE_QUOTE = 11;
/*      */   private static final int STATE_OPEN_TAGxREAD_ATTR_VALUE_DOUBLE_QUOTE = 12;
/*      */   private static final int STATE_OPEN_TAGxEMPTY_TAG = 13;
/*      */   private static final int STATE_CLOSE_TAGxREAD_ELEM_NAME = 14;
/*      */   private static final int STATE_CLOSE_TAGxELEM_NAME_READ = 15;
/*      */   private static final int STATE_DTD = 16;
/*      */   private static final int STATE_DTD_INTERNAL = 17;
/*      */   private final Runnable _createSeqLogic;
/*      */   private CharArray[] _seqs;
/*      */   private int _seqsIndex;
/*      */   private int _seqsCapacity;
/*      */   
/*      */   public void setInput(InputStream in) throws XMLStreamException {
/*  217 */     setInput(in, detectEncoding(in));
/*  218 */     CharArray prologEncoding = getCharacterEncodingScheme();
/*      */ 
/*      */     
/*  221 */     if (prologEncoding != null && !prologEncoding.equals(this._encoding) && (!isUTF8(prologEncoding) || !isUTF8(this._encoding))) {
/*      */ 
/*      */       
/*  224 */       int startOffset = this._readCount;
/*  225 */       reset();
/*  226 */       this._startOffset = startOffset;
/*  227 */       setInput(in, prologEncoding.toString());
/*      */     } 
/*      */   }
/*      */   
/*      */   private static boolean isUTF8(Object encoding) {
/*  232 */     return (encoding.equals("utf-8") || encoding.equals("UTF-8") || encoding.equals("ASCII") || encoding.equals("utf8") || encoding.equals("UTF8"));
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
/*      */   public void setInput(InputStream in, String encoding) throws XMLStreamException {
/*  245 */     this._encoding = encoding;
/*  246 */     if (isUTF8(encoding)) {
/*  247 */       setInput((Reader)this._utf8StreamReader.setInput(in));
/*      */     } else {
/*      */       try {
/*  250 */         setInput(new InputStreamReader(in, encoding));
/*  251 */       } catch (UnsupportedEncodingException e) {
/*  252 */         throw new XMLStreamException(e);
/*      */       } 
/*      */     } 
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
/*      */   public void setInput(Reader reader) throws XMLStreamException {
/*  267 */     if (this._reader != null)
/*  268 */       throw new IllegalStateException("Reader not closed or reset"); 
/*  269 */     this._reader = reader;
/*      */     try {
/*  271 */       int readCount = reader.read(this._readBuffer, this._startOffset, this._readBuffer.length - this._startOffset);
/*      */       
/*  273 */       this._readCount = (readCount >= 0) ? (readCount + this._startOffset) : this._startOffset;
/*      */       
/*  275 */       if (this._readCount >= 5 && this._readBuffer[0] == '<' && this._readBuffer[1] == '?' && this._readBuffer[2] == 'x' && this._readBuffer[3] == 'm' && this._readBuffer[4] == 'l' && this._readBuffer[5] == ' ') {
/*      */ 
/*      */ 
/*      */         
/*  279 */         next();
/*  280 */         this._prolog = getPIData();
/*  281 */         this._index = this._prolog.offset() + this._prolog.length();
/*  282 */         this._start = this._index;
/*  283 */         this._eventType = 7;
/*      */       } 
/*  285 */     } catch (IOException e) {
/*  286 */       throw new XMLStreamException(e);
/*      */     } 
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
/*      */   public int getDepth() {
/*  306 */     return this._depth;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CharArray getQName() {
/*  316 */     if (this._eventType != 1 && this._eventType != 2)
/*      */     {
/*  318 */       throw new IllegalStateException("Not a start element or an end element");
/*      */     }
/*  320 */     return this._qName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CharArray getQName(int depth) {
/*  331 */     if (depth > getDepth())
/*  332 */       throw new IllegalArgumentException(); 
/*  333 */     return this._elemStack[depth];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Attributes getAttributes() {
/*  343 */     if (this._eventType != 1)
/*  344 */       throw new IllegalStateException("Not a start element"); 
/*  345 */     return this._attributes;
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
/*      */   
/*      */   public void setEntities(Map<String, String> entities) {
/*  366 */     this._entities.setEntitiesMapping(entities);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/*  375 */     return "XMLStreamReader - State: " + NAMES_OF_EVENTS[this._eventType] + ", Location: " + this._location.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int next() throws XMLStreamException {
/*  383 */     if (this._eventType == 1) {
/*  384 */       if (this._isEmpty) {
/*  385 */         this._isEmpty = false;
/*  386 */         return this._eventType = 2;
/*      */       } 
/*  388 */     } else if (this._eventType == 2) {
/*  389 */       this._namespaces.pop();
/*  390 */       CharArray startElem = this._elemStack[this._depth--];
/*  391 */       this._start = this._index = startElem.offset();
/*  392 */       while (this._seqs[--this._seqsIndex] != startElem);
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     label240: while (true) {
/*  399 */       if (this._readIndex >= this._readCount && isEndOfStream())
/*  400 */         return this._eventType; 
/*  401 */       char c = this._readBuffer[this._readIndex++];
/*  402 */       if (c <= '&') {
/*  403 */         c = (c == '&') ? replaceEntity() : ((c < ' ') ? handleEndOfLine(c) : c);
/*      */       }
/*  405 */       this._data[this._index++] = c;
/*      */ 
/*      */ 
/*      */       
/*  409 */       switch (this._state) {
/*      */         
/*      */         case 1:
/*      */           while (true) {
/*      */             
/*  414 */             if (c == '<') {
/*  415 */               int length = this._index - this._start - 1;
/*  416 */               if (length > 0) {
/*  417 */                 if (this._charactersPending) {
/*  418 */                   this._text.setArray(this._data, this._text.offset(), this._text.length() + length);
/*      */                 } else {
/*      */                   
/*  421 */                   this._text = newSeq(this._start, length);
/*  422 */                   this._charactersPending = true;
/*      */                 } 
/*  424 */                 this._start = this._index - 1;
/*      */               } 
/*  426 */               this._state = 2;
/*      */               
/*      */               continue label240;
/*      */             } 
/*      */             
/*  431 */             if (this._readIndex >= this._readCount && isEndOfStream())
/*  432 */               return this._eventType; 
/*  433 */             c = this._readBuffer[this._readIndex++];
/*  434 */             if (c <= '&') {
/*  435 */               c = (c == '&') ? replaceEntity() : ((c < ' ') ? handleEndOfLine(c) : c);
/*      */             }
/*  437 */             this._data[this._index++] = c;
/*      */           } 
/*      */ 
/*      */ 
/*      */         
/*      */         case 5:
/*      */           while (true) {
/*  444 */             if (c == '>' && this._index - this._start >= 3 && this._data[this._index - 2] == ']' && this._data[this._index - 3] == ']') {
/*      */ 
/*      */               
/*  447 */               this._index -= 3;
/*  448 */               int length = this._index - this._start;
/*  449 */               if (length > 0) {
/*  450 */                 if (this._charactersPending) {
/*  451 */                   this._text.setArray(this._data, this._text.offset(), this._text.length() + length);
/*      */                 } else {
/*      */                   
/*  454 */                   this._text = newSeq(this._start, length);
/*  455 */                   this._charactersPending = true;
/*      */                 } 
/*      */               }
/*  458 */               this._start = this._index;
/*  459 */               this._state = 1;
/*      */               
/*      */               continue label240;
/*      */             } 
/*      */             
/*  464 */             if (this._readIndex >= this._readCount)
/*  465 */               reloadBuffer(); 
/*  466 */             c = this._readBuffer[this._readIndex++];
/*  467 */             if (c < ' ')
/*  468 */               c = handleEndOfLine(c); 
/*  469 */             this._data[this._index++] = c;
/*      */           } 
/*      */ 
/*      */         
/*      */         case 16:
/*  474 */           if (c == '>') {
/*  475 */             this._text = newSeq(this._start, this._index - this._start);
/*  476 */             this._index = this._start;
/*  477 */             this._state = 1;
/*  478 */             return this._eventType = 11;
/*  479 */           }  if (c == '[') {
/*  480 */             this._state = 17;
/*      */           }
/*      */           continue;
/*      */         
/*      */         case 17:
/*  485 */           if (c == ']') {
/*  486 */             this._state = 16;
/*      */           }
/*      */           continue;
/*      */         
/*      */         case 2:
/*  491 */           if (this._index - this._start == 2) {
/*  492 */             if (c == '/') {
/*  493 */               this._start = this._index -= 2;
/*  494 */               this._state = 14;
/*  495 */               this._prefixSep = -1;
/*  496 */               if (this._charactersPending) {
/*  497 */                 this._charactersPending = false;
/*  498 */                 return this._eventType = 4;
/*      */               }  continue;
/*  500 */             }  if (c == '?') {
/*  501 */               this._start = this._index -= 2;
/*  502 */               this._state = 4;
/*  503 */               if (this._charactersPending) {
/*  504 */                 this._charactersPending = false;
/*  505 */                 return this._eventType = 4;
/*      */               }  continue;
/*  507 */             }  if (c != '!') {
/*  508 */               this._data[this._start] = c;
/*  509 */               this._index = this._start + 1;
/*  510 */               this._state = 6;
/*  511 */               this._prefixSep = -1;
/*  512 */               if (this._charactersPending) {
/*  513 */                 this._charactersPending = false;
/*  514 */                 return this._eventType = 4;
/*      */               } 
/*      */             }  continue;
/*  517 */           }  if (this._index - this._start == 4 && this._data[this._start + 1] == '!' && this._data[this._start + 2] == '-' && this._data[this._start + 3] == '-') {
/*      */ 
/*      */ 
/*      */             
/*  521 */             this._start = this._index -= 4;
/*  522 */             this._state = 3;
/*  523 */             if (this._charactersPending) {
/*  524 */               this._charactersPending = false;
/*  525 */               return this._eventType = 4;
/*      */             }  continue;
/*      */           } 
/*  528 */           if (this._index - this._start == 9 && this._data[this._start + 1] == '!' && this._data[this._start + 2] == '[' && this._data[this._start + 3] == 'C' && this._data[this._start + 4] == 'D' && this._data[this._start + 5] == 'A' && this._data[this._start + 6] == 'T' && this._data[this._start + 7] == 'A' && this._data[this._start + 8] == '[') {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  537 */             this._start = this._index -= 9;
/*  538 */             this._state = 5; continue;
/*      */           } 
/*  540 */           if (this._index - this._start == 9 && this._data[this._start + 1] == '!' && this._data[this._start + 2] == 'D' && this._data[this._start + 3] == 'O' && this._data[this._start + 4] == 'C' && this._data[this._start + 5] == 'T' && this._data[this._start + 6] == 'Y' && this._data[this._start + 7] == 'P' && this._data[this._start + 8] == 'E')
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*  550 */             this._state = 16;
/*      */           }
/*      */           continue;
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case 3:
/*      */           while (true) {
/*  559 */             if (c == '>' && this._index - this._start >= 3 && this._data[this._index - 2] == '-' && this._data[this._index - 3] == '-') {
/*      */ 
/*      */               
/*  562 */               this._index -= 3;
/*  563 */               this._text = newSeq(this._start, this._index - this._start);
/*  564 */               this._state = 1;
/*  565 */               this._index = this._start;
/*  566 */               return this._eventType = 5;
/*      */             } 
/*      */ 
/*      */             
/*  570 */             if (this._readIndex >= this._readCount)
/*  571 */               reloadBuffer(); 
/*  572 */             c = this._readBuffer[this._readIndex++];
/*  573 */             if (c < ' ')
/*  574 */               c = handleEndOfLine(c); 
/*  575 */             this._data[this._index++] = c;
/*      */           } 
/*      */         
/*      */         case 4:
/*  579 */           if (c == '>' && this._index - this._start >= 2 && this._data[this._index - 2] == '?') {
/*      */             
/*  581 */             this._index -= 2;
/*  582 */             this._text = newSeq(this._start, this._index - this._start);
/*  583 */             this._state = 1;
/*  584 */             this._index = this._start;
/*  585 */             return this._eventType = 3;
/*      */           } 
/*      */           continue;
/*      */ 
/*      */         
/*      */         case 6:
/*  591 */           this._attributes.reset();
/*  592 */           this._namespaces.push();
/*      */           
/*      */           while (true) {
/*  595 */             if (c < '@') {
/*  596 */               if (c == '>') {
/*  597 */                 this._qName = newSeq(this._start, --this._index - this._start);
/*  598 */                 this._start = this._index;
/*  599 */                 this._state = 1;
/*  600 */                 processStartTag();
/*  601 */                 this._isEmpty = false;
/*  602 */                 return this._eventType = 1;
/*  603 */               }  if (c == '/') {
/*  604 */                 this._qName = newSeq(this._start, --this._index - this._start);
/*  605 */                 this._start = this._index;
/*  606 */                 this._state = 13; continue label240;
/*      */               } 
/*  608 */               if (c == ':') {
/*  609 */                 this._prefixSep = this._index - 1;
/*  610 */               } else if (c <= ' ') {
/*  611 */                 this._qName = newSeq(this._start, --this._index - this._start);
/*  612 */                 this._state = 7;
/*      */                 
/*      */                 continue label240;
/*      */               } 
/*      */             } 
/*  617 */             if (this._readIndex >= this._readCount)
/*  618 */               reloadBuffer(); 
/*  619 */             c = this._data[this._index++] = this._readBuffer[this._readIndex++];
/*      */           } 
/*      */ 
/*      */         
/*      */         case 7:
/*  624 */           if (c == '>') {
/*  625 */             this._start = --this._index;
/*  626 */             this._state = 1;
/*  627 */             processStartTag();
/*  628 */             this._isEmpty = false;
/*  629 */             return this._eventType = 1;
/*  630 */           }  if (c == '/') {
/*  631 */             this._state = 13; continue;
/*  632 */           }  if (c > ' ') {
/*  633 */             this._start = this._index - 1;
/*  634 */             this._attrPrefixSep = -1;
/*  635 */             this._state = 8;
/*      */           } 
/*      */           continue;
/*      */ 
/*      */         
/*      */         case 8:
/*      */           while (true) {
/*  642 */             if (c < '@') {
/*  643 */               if (c <= ' ') {
/*  644 */                 this._attrQName = newSeq(this._start, --this._index - this._start);
/*  645 */                 this._state = 9; continue label240;
/*      */               } 
/*  647 */               if (c == '=') {
/*  648 */                 this._attrQName = newSeq(this._start, --this._index - this._start);
/*  649 */                 this._state = 10; continue label240;
/*      */               } 
/*  651 */               if (c == ':') {
/*  652 */                 this._attrPrefixSep = this._index - 1;
/*      */               }
/*      */             } 
/*      */             
/*  656 */             if (this._readIndex >= this._readCount)
/*  657 */               reloadBuffer(); 
/*  658 */             this._data[this._index++] = c = this._readBuffer[this._readIndex++];
/*      */           } 
/*      */ 
/*      */         
/*      */         case 9:
/*  663 */           if (c == '=') {
/*  664 */             this._index--;
/*  665 */             this._state = 10; continue;
/*  666 */           }  if (c > ' ') throw new XMLStreamException("'=' expected", this._location);
/*      */           
/*      */           continue;
/*      */         
/*      */         case 10:
/*  671 */           if (c == '\'') {
/*  672 */             this._start = --this._index;
/*  673 */             this._state = 11; continue;
/*  674 */           }  if (c == '"') {
/*  675 */             this._start = --this._index;
/*  676 */             this._state = 12; continue;
/*  677 */           }  if (c > ' ') throw new XMLStreamException("Quotes expected", this._location);
/*      */           
/*      */           continue;
/*      */ 
/*      */         
/*      */         case 11:
/*      */           while (true) {
/*  684 */             if (c == '\'') {
/*  685 */               this._attrValue = newSeq(this._start, --this._index - this._start);
/*  686 */               processAttribute();
/*  687 */               this._state = 7;
/*      */               
/*      */               continue label240;
/*      */             } 
/*      */             
/*  692 */             if (this._readIndex >= this._readCount)
/*  693 */               reloadBuffer(); 
/*  694 */             c = this._readBuffer[this._readIndex++];
/*  695 */             if (c == '&')
/*  696 */               c = replaceEntity(); 
/*  697 */             this._data[this._index++] = c;
/*      */           } 
/*      */ 
/*      */ 
/*      */         
/*      */         case 12:
/*      */           while (true) {
/*  704 */             if (c == '"') {
/*  705 */               this._attrValue = newSeq(this._start, --this._index - this._start);
/*  706 */               processAttribute();
/*  707 */               this._state = 7;
/*      */               
/*      */               continue label240;
/*      */             } 
/*      */             
/*  712 */             if (this._readIndex >= this._readCount)
/*  713 */               reloadBuffer(); 
/*  714 */             c = this._readBuffer[this._readIndex++];
/*  715 */             if (c == '&')
/*  716 */               c = replaceEntity(); 
/*  717 */             this._data[this._index++] = c;
/*      */           } 
/*      */ 
/*      */         
/*      */         case 13:
/*  722 */           if (c == '>') {
/*  723 */             this._start = --this._index;
/*  724 */             this._state = 1;
/*  725 */             processStartTag();
/*  726 */             this._isEmpty = true;
/*  727 */             return this._eventType = 1;
/*      */           } 
/*  729 */           throw new XMLStreamException("'>' expected", this._location);
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case 14:
/*      */           while (true) {
/*  736 */             if (c < '@') {
/*  737 */               if (c == '>') {
/*  738 */                 this._qName = newSeq(this._start, --this._index - this._start);
/*  739 */                 this._start = this._index;
/*  740 */                 this._state = 1;
/*  741 */                 processEndTag();
/*  742 */                 return this._eventType = 2;
/*  743 */               }  if (c == ':') {
/*  744 */                 this._prefixSep = this._index - 1;
/*  745 */               } else if (c <= ' ') {
/*  746 */                 this._qName = newSeq(this._start, --this._index - this._start);
/*  747 */                 this._state = 15;
/*      */                 
/*      */                 continue label240;
/*      */               } 
/*      */             } 
/*  752 */             if (this._readIndex >= this._readCount)
/*  753 */               reloadBuffer(); 
/*  754 */             c = this._data[this._index++] = this._readBuffer[this._readIndex++];
/*      */           } 
/*      */ 
/*      */         
/*      */         case 15:
/*  759 */           if (c == '>') {
/*  760 */             this._start = --this._index;
/*  761 */             this._state = 1;
/*  762 */             processEndTag();
/*  763 */             return this._eventType = 2;
/*  764 */           }  if (c > ' ') throw new XMLStreamException("'>' expected", this._location); 
/*      */           continue;
/*      */       } 
/*      */       break;
/*      */     } 
/*  769 */     throw new XMLStreamException("State unknown: " + this._state, this._location);
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
/*      */   private void reloadBuffer() throws XMLStreamException {
/*  819 */     if (this._reader == null)
/*  820 */       throw new XMLStreamException("Input not specified"); 
/*  821 */     this._location._column += this._readIndex;
/*  822 */     this._location._charactersRead += this._readIndex;
/*  823 */     this._readIndex = 0;
/*      */     try {
/*  825 */       this._readCount = this._reader.read(this._readBuffer, 0, this._readBuffer.length);
/*  826 */       if (this._readCount <= 0 && (this._depth != 0 || this._state != 1))
/*      */       {
/*  828 */         throw new XMLStreamException("Unexpected end of document", this._location);
/*      */       }
/*  830 */     } catch (IOException e) {
/*  831 */       throw new XMLStreamException(e);
/*      */     } 
/*  833 */     while (this._index + this._readCount >= this._data.length) {
/*  834 */       increaseDataBuffer();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean isEndOfStream() throws XMLStreamException {
/*  846 */     if (this._readIndex >= this._readCount)
/*  847 */       reloadBuffer(); 
/*  848 */     if (this._readCount <= 0) {
/*      */       
/*  850 */       if (this._eventType == 8) {
/*  851 */         throw new XMLStreamException("End document has already been reached");
/*      */       }
/*  853 */       int length = this._index - this._start;
/*  854 */       if (length > 0) {
/*  855 */         if (this._charactersPending) {
/*  856 */           this._text.setArray(this._data, this._text.offset(), this._text.length() + length);
/*      */         } else {
/*      */           
/*  859 */           this._text = newSeq(this._start, length);
/*      */         } 
/*  861 */         this._start = this._index;
/*  862 */         this._eventType = 4;
/*      */       } else {
/*  864 */         this._eventType = 8;
/*      */       } 
/*  866 */       return true;
/*      */     } 
/*  868 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private char handleEndOfLine(char c) throws XMLStreamException {
/*  878 */     if (c == '\r') {
/*      */ 
/*      */       
/*  881 */       if (this._readIndex >= this._readCount)
/*  882 */         reloadBuffer(); 
/*  883 */       if (this._readIndex < this._readCount && this._readBuffer[this._readIndex] == '\n')
/*  884 */         this._readIndex++; 
/*  885 */       c = '\n';
/*      */     } 
/*  887 */     if (c == '\n')
/*  888 */     { this._location._line++;
/*  889 */       this._location._column = -this._readIndex; }
/*  890 */     else if (c == '\000') { throw new XMLStreamException("Illegal XML character U+0000", this._location); }
/*      */     
/*  892 */     return c;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private char replaceEntity() throws XMLStreamException {
/*  902 */     if (this._state == 3 || this._state == 4 || this._state == 5)
/*      */     {
/*  904 */       return '&';
/*      */     }
/*  906 */     int start = this._index;
/*  907 */     this._data[this._index++] = '&';
/*      */     while (true) {
/*  909 */       if (this._readIndex >= this._readCount)
/*  910 */         reloadBuffer(); 
/*  911 */       char c1 = this._data[this._index++] = this._readBuffer[this._readIndex++];
/*  912 */       if (c1 == ';')
/*      */         break; 
/*  914 */       if (c1 <= ' ') {
/*  915 */         throw new XMLStreamException("';' expected", this._location);
/*      */       }
/*      */     } 
/*  918 */     while (start + this._entities.getMaxLength() >= this._data.length) {
/*  919 */       increaseDataBuffer();
/*      */     }
/*      */ 
/*      */     
/*  923 */     int length = this._entities.replaceEntity(this._data, start, this._index - start);
/*      */ 
/*      */     
/*  926 */     this._index = start + length;
/*      */ 
/*      */     
/*  929 */     if (this._readIndex >= this._readCount)
/*  930 */       reloadBuffer(); 
/*  931 */     char c = this._readBuffer[this._readIndex++];
/*  932 */     return (c == '&') ? (c = replaceEntity()) : c;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void processAttribute() throws XMLStreamException {
/*  939 */     if (this._attrPrefixSep < 0) {
/*  940 */       if (isXMLNS(this._attrQName)) {
/*  941 */         this._namespaces.setPrefix(this._namespaces._defaultNsPrefix, this._attrValue);
/*      */       } else {
/*  943 */         this._attributes.addAttribute(this._attrQName, null, this._attrQName, this._attrValue);
/*      */       } 
/*      */     } else {
/*      */       
/*  947 */       int offset = this._attrQName.offset();
/*  948 */       int length = this._attrQName.length();
/*      */       
/*  950 */       CharArray prefix = newSeq(offset, this._attrPrefixSep - offset);
/*      */       
/*  952 */       CharArray localName = newSeq(this._attrPrefixSep + 1, offset + length - this._attrPrefixSep - 1);
/*      */ 
/*      */       
/*  955 */       if (isXMLNS(prefix)) {
/*  956 */         this._namespaces.setPrefix(localName, this._attrValue);
/*      */       } else {
/*  958 */         this._attributes.addAttribute(localName, prefix, this._attrQName, this._attrValue);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean isXMLNS(CharArray chars) {
/*  965 */     return (chars.length() == 5 && chars.charAt(0) == 'x' && chars.charAt(1) == 'm' && chars.charAt(2) == 'l' && chars.charAt(3) == 'n' && chars.charAt(4) == 's');
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void processEndTag() throws XMLStreamException {
/*  971 */     if (!this._qName.equals(this._elemStack[this._depth])) {
/*  972 */       throw new XMLStreamException("Unexpected end tag for " + this._qName, this._location);
/*      */     }
/*      */   }
/*      */   
/*      */   private void processStartTag() throws XMLStreamException {
/*  977 */     if (++this._depth >= this._elemStack.length) {
/*  978 */       increaseStack();
/*      */     }
/*  980 */     this._elemStack[this._depth] = this._qName;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void reset() {
/*  986 */     this._attributes.reset();
/*  987 */     this._attrPrefixSep = 0;
/*  988 */     this._attrQName = null;
/*  989 */     this._attrValue = null;
/*  990 */     this._attrQName = null;
/*  991 */     this._charactersPending = false;
/*  992 */     this._encoding = null;
/*  993 */     this._entities.reset();
/*  994 */     this._eventType = 7;
/*  995 */     this._index = 0;
/*  996 */     this._isEmpty = false;
/*  997 */     this._location.reset();
/*  998 */     this._namespaces.reset();
/*  999 */     this._prolog = null;
/* 1000 */     this._readCount = 0;
/* 1001 */     this._reader = null;
/* 1002 */     this._depth = 0;
/* 1003 */     this._readIndex = 0;
/* 1004 */     this._seqsIndex = 0;
/* 1005 */     this._start = 0;
/* 1006 */     this._startOffset = 0;
/* 1007 */     this._state = 1;
/* 1008 */     this._utf8StreamReader.reset();
/*      */ 
/*      */     
/* 1011 */     if (this._factory != null) {
/* 1012 */       this._factory.recycle(this);
/*      */     }
/*      */   }
/*      */   
/*      */   private CharArray newSeq(int offset, int length) {
/* 1017 */     CharArray seq = (this._seqsIndex < this._seqsCapacity) ? this._seqs[this._seqsIndex++] : newSeq2();
/*      */     
/* 1019 */     return seq.setArray(this._data, offset, length);
/*      */   }
/*      */   
/*      */   private CharArray newSeq2() {
/* 1023 */     this._createSeqLogic.run();
/* 1024 */     return this._seqs[this._seqsIndex++];
/*      */   }
/*      */   XMLStreamReaderImpl(XMLInputFactoryImpl factory) {
/* 1027 */     this._createSeqLogic = new Runnable()
/*      */       {
/*      */         public void run() {
/* 1030 */           if (XMLStreamReaderImpl.this._seqsCapacity >= XMLStreamReaderImpl.this._seqs.length) {
/* 1031 */             CharArray[] tmp = new CharArray[XMLStreamReaderImpl.this._seqs.length * 2];
/* 1032 */             System.arraycopy(XMLStreamReaderImpl.this._seqs, 0, tmp, 0, XMLStreamReaderImpl.this._seqs.length);
/* 1033 */             XMLStreamReaderImpl.this._seqs = tmp;
/*      */           } 
/* 1035 */           CharArray seq = new CharArray();
/* 1036 */           XMLStreamReaderImpl.this._seqs[XMLStreamReaderImpl.this._seqsCapacity++] = seq;
/*      */         }
/*      */       };
/*      */ 
/*      */     
/* 1041 */     this._seqs = new CharArray[256];
/*      */     this._factory = factory;
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
/*      */   private void increaseDataBuffer() {
/* 1054 */     char[] tmp = new char[this._data.length * 2];
/* 1055 */     LogContext.info(new Object[] { new CharArray("XMLStreamReaderImpl: Data buffer increased to " + tmp.length) });
/*      */     
/* 1057 */     System.arraycopy(this._data, 0, tmp, 0, this._data.length);
/* 1058 */     this._data = tmp;
/*      */   }
/*      */ 
/*      */   
/*      */   private void increaseStack() {
/* 1063 */     CharArray[] tmp = new CharArray[this._elemStack.length * 2];
/* 1064 */     LogContext.info(new Object[] { new CharArray("XMLStreamReaderImpl: CharArray stack increased to " + tmp.length) });
/*      */ 
/*      */     
/* 1067 */     System.arraycopy(this._elemStack, 0, tmp, 0, this._elemStack.length);
/* 1068 */     this._elemStack = tmp;
/*      */   }
/*      */ 
/*      */   
/*      */   private final class LocationImpl
/*      */     implements Location
/*      */   {
/*      */     int _column;
/*      */     
/*      */     int _line;
/*      */     int _charactersRead;
/*      */     
/*      */     private LocationImpl() {}
/*      */     
/*      */     public int getLineNumber() {
/* 1083 */       return this._line + 1;
/*      */     }
/*      */     
/*      */     public int getColumnNumber() {
/* 1087 */       return this._column + XMLStreamReaderImpl.this._readIndex;
/*      */     }
/*      */     
/*      */     public int getCharacterOffset() {
/* 1091 */       return this._charactersRead + XMLStreamReaderImpl.this._readIndex;
/*      */     }
/*      */     
/*      */     public String getPublicId() {
/* 1095 */       return null;
/*      */     }
/*      */     
/*      */     public String getSystemId() {
/* 1099 */       return null;
/*      */     }
/*      */     
/*      */     public String toString() {
/* 1103 */       return "Line " + getLineNumber() + ", Column " + getColumnNumber();
/*      */     }
/*      */     
/*      */     public void reset() {
/* 1107 */       this._line = 0;
/* 1108 */       this._column = 0;
/* 1109 */       this._charactersRead = 0;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void require(int type, CharSequence namespaceURI, CharSequence localName) throws XMLStreamException {
/* 1120 */     if (this._eventType != type) {
/* 1121 */       throw new XMLStreamException("Expected event: " + NAMES_OF_EVENTS[type] + ", found event: " + NAMES_OF_EVENTS[this._eventType]);
/*      */     }
/*      */     
/* 1124 */     if (namespaceURI != null && !getNamespaceURI().equals(namespaceURI)) {
/* 1125 */       throw new XMLStreamException("Expected namespace URI: " + namespaceURI + ", found: " + getNamespaceURI());
/*      */     }
/* 1127 */     if (localName != null && !getLocalName().equals(localName)) {
/* 1128 */       throw new XMLStreamException("Expected local name: " + localName + ", found: " + getLocalName());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public CharArray getElementText() throws XMLStreamException {
/* 1135 */     if (getEventType() != 1) throw new XMLStreamException("Parser must be on START_ELEMENT to read next text", getLocation());
/*      */ 
/*      */     
/* 1138 */     CharArray text = null;
/* 1139 */     int eventType = next();
/* 1140 */     while (eventType != 2) {
/* 1141 */       if (eventType == 4) {
/* 1142 */         if (text == null) {
/* 1143 */           text = getText();
/*      */         } else {
/* 1145 */           text.setArray(this._data, text.offset(), text.length() + getText().length());
/*      */         }
/*      */       
/* 1148 */       } else if (eventType != 3 && eventType != 5) {
/*      */ 
/*      */         
/* 1151 */         if (eventType == 8) {
/* 1152 */           throw new XMLStreamException("Unexpected end of document when reading element text content", getLocation());
/*      */         }
/*      */         
/* 1155 */         if (eventType == 1) {
/* 1156 */           throw new XMLStreamException("Element text content may not contain START_ELEMENT", getLocation());
/*      */         }
/*      */ 
/*      */         
/* 1160 */         throw new XMLStreamException("Unexpected event type " + NAMES_OF_EVENTS[eventType], getLocation());
/*      */       } 
/*      */       
/* 1163 */       eventType = next();
/*      */     } 
/* 1165 */     return (text != null) ? text : newSeq(0, 0);
/*      */   }
/*      */ 
/*      */   
/*      */   public Object getProperty(String name) throws IllegalArgumentException {
/* 1170 */     if (name.equals("javolution.xml.stream.isCoalescing"))
/* 1171 */       return Boolean.TRUE; 
/* 1172 */     if (name.equals("javolution.xml.stream.entities")) {
/* 1173 */       return this._entities.getEntitiesMapping();
/*      */     }
/* 1175 */     throw new IllegalArgumentException("Property: " + name + " not supported");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void close() throws XMLStreamException {
/* 1182 */     reset();
/*      */   }
/*      */   
/*      */   public int getAttributeCount() {
/* 1186 */     if (this._eventType != 1)
/* 1187 */       throw illegalState("Not a start element"); 
/* 1188 */     return this._attributes.getLength();
/*      */   }
/*      */   
/*      */   public CharArray getAttributeLocalName(int index) {
/* 1192 */     if (this._eventType != 1)
/* 1193 */       throw illegalState("Not a start element"); 
/* 1194 */     return this._attributes.getLocalName(index);
/*      */   }
/*      */   
/*      */   public CharArray getAttributeNamespace(int index) {
/* 1198 */     if (this._eventType != 1)
/* 1199 */       throw illegalState("Not a start element"); 
/* 1200 */     CharArray prefix = this._attributes.getPrefix(index);
/* 1201 */     return this._namespaces.getNamespaceURINullAllowed((CharSequence)prefix);
/*      */   }
/*      */   
/*      */   public CharArray getAttributePrefix(int index) {
/* 1205 */     if (this._eventType != 1)
/* 1206 */       throw illegalState("Not a start element"); 
/* 1207 */     return this._attributes.getPrefix(index);
/*      */   }
/*      */   
/*      */   public CharArray getAttributeType(int index) {
/* 1211 */     if (this._eventType != 1)
/* 1212 */       throw illegalState("Not a start element"); 
/* 1213 */     return this._attributes.getType(index);
/*      */   }
/*      */   
/*      */   public CharArray getAttributeValue(CharSequence uri, CharSequence localName) {
/* 1217 */     if (this._eventType != 1)
/* 1218 */       throw illegalState("Not a start element"); 
/* 1219 */     return (uri == null) ? this._attributes.getValue(localName) : this._attributes.getValue(uri, localName);
/*      */   }
/*      */ 
/*      */   
/*      */   public CharArray getAttributeValue(int index) {
/* 1224 */     if (this._eventType != 1)
/* 1225 */       throw illegalState("Not a start element"); 
/* 1226 */     return this._attributes.getValue(index);
/*      */   }
/*      */   
/*      */   public CharArray getCharacterEncodingScheme() {
/* 1230 */     return readPrologAttribute((CharSequence)ENCODING);
/*      */   }
/*      */   
/* 1233 */   private static final CharArray ENCODING = new CharArray("encoding");
/*      */   
/*      */   public String getEncoding() {
/* 1236 */     return this._encoding;
/*      */   }
/*      */   
/*      */   public int getEventType() {
/* 1240 */     return this._eventType;
/*      */   }
/*      */   
/*      */   public CharArray getLocalName() {
/* 1244 */     if (this._eventType != 1 && this._eventType != 2)
/*      */     {
/* 1246 */       throw illegalState("Not a start or end element"); } 
/* 1247 */     if (this._prefixSep < 0)
/* 1248 */       return this._qName; 
/* 1249 */     CharArray localName = newSeq(this._prefixSep + 1, this._qName.offset() + this._qName.length() - this._prefixSep - 1);
/*      */     
/* 1251 */     return localName;
/*      */   }
/*      */   
/*      */   public Location getLocation() {
/* 1255 */     return this._location;
/*      */   }
/*      */   
/*      */   public int getNamespaceCount() {
/* 1259 */     if (this._eventType != 1 && this._eventType != 2)
/*      */     {
/* 1261 */       throw illegalState("Not a start or end element"); } 
/* 1262 */     return this._namespaces._namespacesCount[this._depth];
/*      */   }
/*      */   
/*      */   public CharArray getNamespacePrefix(int index) {
/* 1266 */     if (this._eventType != 1 && this._eventType != 2)
/*      */     {
/* 1268 */       throw illegalState("Not a start or end element"); } 
/* 1269 */     return this._namespaces._prefixes[index];
/*      */   }
/*      */   
/*      */   public CharArray getNamespaceURI(CharSequence prefix) {
/* 1273 */     if (this._eventType != 1 && this._eventType != 2)
/*      */     {
/* 1275 */       throw illegalState("Not a start or end element"); } 
/* 1276 */     return this._namespaces.getNamespaceURI(prefix);
/*      */   }
/*      */   
/*      */   public CharArray getNamespaceURI(int index) {
/* 1280 */     if (this._eventType != 1 && this._eventType != 2)
/*      */     {
/* 1282 */       throw illegalState("Not a start or end element"); } 
/* 1283 */     return this._namespaces._namespaces[index];
/*      */   }
/*      */   
/*      */   public NamespaceContext getNamespaceContext() {
/* 1287 */     return this._namespaces;
/*      */   }
/*      */   
/*      */   public CharArray getNamespaceURI() {
/* 1291 */     return this._namespaces.getNamespaceURINullAllowed((CharSequence)getPrefix());
/*      */   }
/*      */   
/*      */   public CharArray getPrefix() {
/* 1295 */     if (this._eventType != 1 && this._eventType != 2)
/*      */     {
/* 1297 */       throw illegalState("Not a start or end element"); } 
/* 1298 */     if (this._prefixSep < 0)
/* 1299 */       return null; 
/* 1300 */     int offset = this._qName.offset();
/* 1301 */     CharArray prefix = newSeq(offset, this._prefixSep - offset);
/* 1302 */     return prefix;
/*      */   }
/*      */   
/*      */   public CharArray getPIData() {
/* 1306 */     if (this._eventType != 3)
/* 1307 */       throw illegalState("Not a processing instruction"); 
/* 1308 */     int offset = this._text.indexOf(' ') + this._text.offset() + 1;
/* 1309 */     CharArray piData = newSeq(offset, this._text.length() - offset);
/* 1310 */     return piData;
/*      */   }
/*      */   
/*      */   public CharArray getPITarget() {
/* 1314 */     if (this._eventType != 3)
/* 1315 */       throw illegalState("Not a processing instruction"); 
/* 1316 */     CharArray piTarget = newSeq(this._text.offset(), this._text.indexOf(' ') + this._text.offset());
/*      */     
/* 1318 */     return piTarget;
/*      */   }
/*      */   
/*      */   public CharArray getText() {
/* 1322 */     if (this._eventType != 4 && this._eventType != 5 && this._eventType != 11)
/*      */     {
/*      */       
/* 1325 */       throw illegalState("Not a text event"); } 
/* 1326 */     return this._text;
/*      */   }
/*      */   
/*      */   public char[] getTextCharacters() {
/* 1330 */     return getText().array();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getTextCharacters(int sourceStart, char[] target, int targetStart, int length) throws XMLStreamException {
/* 1335 */     CharArray text = getText();
/* 1336 */     int copyLength = Math.min(length, text.length());
/* 1337 */     System.arraycopy(text.array(), sourceStart + text.offset(), target, targetStart, copyLength);
/*      */     
/* 1339 */     return copyLength;
/*      */   }
/*      */   
/*      */   public int getTextLength() {
/* 1343 */     return getText().length();
/*      */   }
/*      */   
/*      */   public int getTextStart() {
/* 1347 */     return getText().offset();
/*      */   }
/*      */   
/*      */   public CharArray getVersion() {
/* 1351 */     return readPrologAttribute((CharSequence)VERSION);
/*      */   }
/*      */   
/* 1354 */   private static final CharArray VERSION = new CharArray("version");
/*      */   
/*      */   public boolean isStandalone() {
/* 1357 */     CharArray standalone = readPrologAttribute((CharSequence)STANDALONE);
/* 1358 */     return (standalone != null) ? standalone.equals("no") : true;
/*      */   }
/*      */   
/*      */   public boolean standaloneSet() {
/* 1362 */     return (readPrologAttribute((CharSequence)STANDALONE) != null);
/*      */   }
/*      */   
/* 1365 */   private static final CharArray STANDALONE = new CharArray("standalone");
/*      */   
/*      */   public boolean hasName() {
/* 1368 */     return (this._eventType == 1 || this._eventType == 2);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasNext() throws XMLStreamException {
/* 1373 */     return (this._eventType != 8);
/*      */   }
/*      */   
/*      */   public boolean hasText() {
/* 1377 */     return ((this._eventType == 4 || this._eventType == 5 || this._eventType == 11) && this._text.length() > 0);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isAttributeSpecified(int index) {
/* 1383 */     if (this._eventType != 1)
/* 1384 */       throw new IllegalStateException("Not a start element"); 
/* 1385 */     return (this._attributes.getValue(index) != null);
/*      */   }
/*      */   
/*      */   public boolean isCharacters() {
/* 1389 */     return (this._eventType == 4);
/*      */   }
/*      */   
/*      */   public boolean isEndElement() {
/* 1393 */     return (this._eventType == 2);
/*      */   }
/*      */   
/*      */   public boolean isStartElement() {
/* 1397 */     return (this._eventType == 1);
/*      */   }
/*      */   
/*      */   public boolean isWhiteSpace() {
/* 1401 */     if (isCharacters()) {
/* 1402 */       char[] chars = this._text.array();
/* 1403 */       for (int i = this._text.offset(), end = this._text.offset() + this._text.length(); i < end;) {
/* 1404 */         if (!isWhiteSpace(chars[i++]))
/* 1405 */           return false; 
/*      */       } 
/* 1407 */       return true;
/*      */     } 
/* 1409 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private static boolean isWhiteSpace(char c) {
/* 1414 */     return (c == ' ' || c == '\t' || c == '\r' || c == '\n');
/*      */   }
/*      */   
/*      */   public int nextTag() throws XMLStreamException {
/* 1418 */     int eventType = next();
/* 1419 */     while (eventType == 5 || eventType == 3 || eventType == 11 || (eventType == 4 && isWhiteSpace()))
/*      */     {
/*      */ 
/*      */       
/* 1423 */       eventType = next();
/*      */     }
/* 1425 */     if (eventType != 1 && eventType != 2)
/*      */     {
/* 1427 */       throw new XMLStreamException("Tag expected (but found " + NAMES_OF_EVENTS[this._eventType] + ")");
/*      */     }
/* 1429 */     return eventType;
/*      */   }
/*      */   
/*      */   private IllegalStateException illegalState(String msg) {
/* 1433 */     return new IllegalStateException(msg + " (" + NAMES_OF_EVENTS[this._eventType] + ")");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private String detectEncoding(InputStream input) throws XMLStreamException {
/*      */     int byte0, byte1;
/*      */     try {
/* 1441 */       byte0 = input.read();
/* 1442 */     } catch (IOException e) {
/* 1443 */       throw new XMLStreamException(e);
/*      */     } 
/* 1445 */     if (byte0 == -1)
/* 1446 */       throw new XMLStreamException("Premature End-Of-File"); 
/* 1447 */     if (byte0 == 60) {
/* 1448 */       this._readBuffer[this._startOffset++] = '<';
/* 1449 */       return "UTF-8";
/*      */     } 
/*      */     
/*      */     try {
/* 1453 */       byte1 = input.read();
/* 1454 */     } catch (IOException e) {
/* 1455 */       throw new XMLStreamException(e);
/*      */     } 
/* 1457 */     if (byte1 == -1)
/* 1458 */       throw new XMLStreamException("Premature End-Of-File"); 
/* 1459 */     if (byte0 == 0 && byte1 == 60) {
/* 1460 */       this._readBuffer[this._startOffset++] = '<';
/* 1461 */       return "UTF-16BE";
/* 1462 */     }  if (byte0 == 60 && byte1 == 0) {
/* 1463 */       this._readBuffer[this._startOffset++] = '<';
/* 1464 */       return "UTF-16LE";
/* 1465 */     }  if (byte0 == 255 && byte1 == 254)
/* 1466 */       return "UTF-16"; 
/* 1467 */     if (byte0 == 254 && byte1 == 255) {
/* 1468 */       return "UTF-16";
/*      */     }
/* 1470 */     this._readBuffer[this._startOffset++] = (char)byte0;
/* 1471 */     this._readBuffer[this._startOffset++] = (char)byte1;
/* 1472 */     return "UTF-8";
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private final CharArray readPrologAttribute(CharSequence name) {
/* 1478 */     if (this._prolog == null)
/* 1479 */       return null; 
/* 1480 */     int READ_EQUAL = 0;
/* 1481 */     int READ_QUOTE = 1;
/* 1482 */     int VALUE_SIMPLE_QUOTE = 2;
/* 1483 */     int VALUE_DOUBLE_QUOTE = 3;
/*      */     
/* 1485 */     int i = this._prolog.indexOf(name);
/* 1486 */     if (i >= 0) {
/* 1487 */       i += this._prolog.offset();
/* 1488 */       int maxIndex = this._prolog.offset() + this._prolog.length();
/* 1489 */       i += name.length();
/* 1490 */       int state = 0;
/* 1491 */       int valueOffset = 0;
/* 1492 */       while (i < maxIndex) {
/* 1493 */         char c = this._prolog.array()[i++];
/* 1494 */         switch (state) {
/*      */           case 0:
/* 1496 */             if (c == '=') {
/* 1497 */               state = 1;
/*      */             }
/*      */           
/*      */           case 1:
/* 1501 */             if (c == '"') {
/* 1502 */               state = 3;
/* 1503 */               valueOffset = i; continue;
/* 1504 */             }  if (c == '\'') {
/* 1505 */               state = 2;
/* 1506 */               valueOffset = i;
/*      */             } 
/*      */           
/*      */           case 2:
/* 1510 */             if (c == '\'') {
/* 1511 */               return newSeq(valueOffset, i - valueOffset - 1);
/*      */             }
/*      */           case 3:
/* 1514 */             if (c == '"') {
/* 1515 */               return newSeq(valueOffset, i - valueOffset - 1);
/*      */             }
/*      */         } 
/*      */       } 
/*      */     } 
/* 1520 */     return null;
/*      */   }
/*      */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/xml/internal/stream/XMLStreamReaderImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */