/*     */ package javolution.xml.internal.stream;
/*     */ 
/*     */ import java.io.OutputStream;
/*     */ import java.io.Writer;
/*     */ import javolution.util.FastTable;
/*     */ import javolution.xml.stream.XMLOutputFactory;
/*     */ import javolution.xml.stream.XMLStreamException;
/*     */ import javolution.xml.stream.XMLStreamWriter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class XMLOutputFactoryImpl
/*     */   implements XMLOutputFactory
/*     */ {
/*  24 */   private Boolean _automaticEmptyElements = Boolean.FALSE;
/*     */ 
/*     */   
/*     */   private String _indentation;
/*     */ 
/*     */   
/*  30 */   private Boolean _isRepairingNamespaces = Boolean.FALSE;
/*     */ 
/*     */   
/*  33 */   private String _lineSeparator = "\n";
/*     */ 
/*     */   
/*  36 */   private Boolean _noEmptyElementTag = Boolean.FALSE;
/*     */ 
/*     */   
/*  39 */   private String _repairingPrefix = "ns";
/*     */   
/*  41 */   private FastTable<XMLStreamWriterImpl> _recycled = (new FastTable()).shared();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public XMLStreamWriterImpl createXMLStreamWriter(OutputStream stream) throws XMLStreamException {
/*  47 */     XMLStreamWriterImpl xmlWriter = newWriter();
/*  48 */     xmlWriter.setOutput(stream);
/*  49 */     return xmlWriter;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public XMLStreamWriterImpl createXMLStreamWriter(OutputStream stream, String encoding) throws XMLStreamException {
/*  55 */     if (encoding == null || encoding.equals("UTF-8") || encoding.equals("utf-8"))
/*     */     {
/*  57 */       return createXMLStreamWriter(stream); } 
/*  58 */     XMLStreamWriterImpl xmlWriter = newWriter();
/*  59 */     xmlWriter.setOutput(stream, encoding);
/*  60 */     return xmlWriter;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public XMLStreamWriterImpl createXMLStreamWriter(Writer writer) throws XMLStreamException {
/*  66 */     XMLStreamWriterImpl xmlWriter = newWriter();
/*  67 */     xmlWriter.setOutput(writer);
/*  68 */     return xmlWriter;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getProperty(String name) throws IllegalArgumentException {
/*  73 */     if (name.equals("javolution.xml.stream.isRepairingNamespaces"))
/*  74 */       return this._isRepairingNamespaces; 
/*  75 */     if (name.equals("javolution.xml.stream.repairingPrefix"))
/*  76 */       return this._repairingPrefix; 
/*  77 */     if (name.equals("javolution.xml.stream.automaticEmptyElements"))
/*  78 */       return this._automaticEmptyElements; 
/*  79 */     if (name.equals("javolution.xml.stream.noEmptyElementTag"))
/*  80 */       return this._noEmptyElementTag; 
/*  81 */     if (name.equals("javolution.xml.stream.indentation"))
/*  82 */       return this._indentation; 
/*  83 */     if (name.equals("javolution.xml.stream.lineSeparator")) {
/*  84 */       return this._lineSeparator;
/*     */     }
/*  86 */     throw new IllegalArgumentException("Property: " + name + " not supported");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPropertySupported(String name) {
/*  93 */     return (name.equals("javolution.xml.stream.isRepairingNamespaces") || name.equals("javolution.xml.stream.repairingPrefix") || name.equals("javolution.xml.stream.automaticEmptyElements") || name.equals("javolution.xml.stream.noEmptyElementTag") || name.equals("javolution.xml.stream.indentation") || name.equals("javolution.xml.stream.lineSeparator"));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setProperty(String name, Object value) throws IllegalArgumentException {
/* 103 */     if (name.equals("javolution.xml.stream.isRepairingNamespaces")) {
/* 104 */       this._isRepairingNamespaces = (Boolean)value;
/* 105 */     } else if (name.equals("javolution.xml.stream.repairingPrefix")) {
/* 106 */       this._repairingPrefix = (String)value;
/* 107 */     } else if (name.equals("javolution.xml.stream.automaticEmptyElements")) {
/* 108 */       this._automaticEmptyElements = (Boolean)value;
/* 109 */     } else if (name.equals("javolution.xml.stream.noEmptyElementTag")) {
/* 110 */       this._noEmptyElementTag = (Boolean)value;
/* 111 */     } else if (name.equals("javolution.xml.stream.indentation")) {
/* 112 */       this._indentation = (String)value;
/* 113 */     } else if (name.equals("javolution.xml.stream.lineSeparator")) {
/* 114 */       this._lineSeparator = (String)value;
/*     */     } else {
/* 116 */       throw new IllegalArgumentException("Property: " + name + " not supported");
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void recycle(XMLStreamWriterImpl xmlWriter) {
/* 125 */     this._recycled.addLast(xmlWriter);
/*     */   }
/*     */   
/*     */   private XMLStreamWriterImpl newWriter() {
/* 129 */     XMLStreamWriterImpl xmlWriter = (XMLStreamWriterImpl)this._recycled.pollLast();
/* 130 */     if (xmlWriter == null) xmlWriter = new XMLStreamWriterImpl(this); 
/* 131 */     xmlWriter.setRepairingNamespaces(this._isRepairingNamespaces.booleanValue());
/* 132 */     xmlWriter.setRepairingPrefix(this._repairingPrefix);
/* 133 */     xmlWriter.setIndentation(this._indentation);
/* 134 */     xmlWriter.setLineSeparator(this._lineSeparator);
/* 135 */     xmlWriter.setAutomaticEmptyElements(this._automaticEmptyElements.booleanValue());
/*     */     
/* 137 */     xmlWriter.setNoEmptyElementTag(this._noEmptyElementTag.booleanValue());
/* 138 */     return xmlWriter;
/*     */   }
/*     */ 
/*     */   
/*     */   public XMLOutputFactory clone() {
/*     */     try {
/* 144 */       XMLOutputFactoryImpl clone = (XMLOutputFactoryImpl)super.clone();
/* 145 */       clone._recycled = (new FastTable()).shared();
/* 146 */       return clone;
/* 147 */     } catch (CloneNotSupportedException e) {
/* 148 */       throw new Error();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/xml/internal/stream/XMLOutputFactoryImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */