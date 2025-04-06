/*     */ package javolution.xml.ws;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import javolution.io.AppendableWriter;
/*     */ import javolution.io.UTF8StreamWriter;
/*     */ import javolution.text.Text;
/*     */ import javolution.text.TextBuilder;
/*     */ import javolution.xml.XMLObjectReader;
/*     */ import javolution.xml.XMLObjectWriter;
/*     */ import javolution.xml.stream.XMLStreamException;
/*     */ import javolution.xml.stream.XMLStreamReader;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class WebServiceClient
/*     */ {
/*     */   public static final String ENVELOPE_PREFIX = "env";
/*     */   public static final String ENVELOPE_URI = "http://schemas.xmlsoap.org/soap/envelope/";
/*     */   Object _url;
/*     */   
/*     */   public WebServiceClient setAddress(String address) {
/*     */     try {
/*  79 */       this._url = new URL(address);
/*  80 */     } catch (MalformedURLException e) {
/*  81 */       throw new IllegalArgumentException("Malformed URL: " + address);
/*     */     } 
/*  83 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void invoke() throws IOException, XMLStreamException {
/*     */     try {
/*  93 */       this._out.setOutput((Appendable)this._buffer);
/*  94 */       this._writer.setOutput((Writer)this._out);
/*  95 */       XMLStreamWriter xmlOut = this._writer.getStreamWriter();
/*  96 */       xmlOut.setPrefix(csq("env"), csq("http://schemas.xmlsoap.org/soap/envelope/"));
/*  97 */       xmlOut.writeStartElement(csq("http://schemas.xmlsoap.org/soap/envelope/"), csq("Envelope"));
/*  98 */       xmlOut.writeNamespace(csq("env"), csq("http://schemas.xmlsoap.org/soap/envelope/"));
/*  99 */       xmlOut.writeStartElement(csq("http://schemas.xmlsoap.org/soap/envelope/"), csq("Header"));
/* 100 */       xmlOut.writeEndElement();
/* 101 */       xmlOut.writeStartElement(csq("http://schemas.xmlsoap.org/soap/envelope/"), csq("Body"));
/* 102 */       writeRequest(this._writer);
/* 103 */       this._writer.close();
/*     */ 
/*     */       
/* 106 */       if (this._url == null) {
/* 107 */         throw new IOException("URL not set");
/*     */       }
/* 109 */       HttpURLConnection http = (HttpURLConnection)((URL)this._url).openConnection();
/*     */       
/* 111 */       http.setRequestProperty("Content-Length", String.valueOf(this._buffer.length()));
/*     */       
/* 113 */       http.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
/*     */       
/* 115 */       http.setRequestMethod("POST");
/* 116 */       http.setDoOutput(true);
/* 117 */       http.setDoInput(true);
/* 118 */       this._utf8Writer.setOutput(http.getOutputStream());
/*     */       
/* 120 */       this._utf8Writer.append((CharSequence)this._buffer);
/* 121 */       this._utf8Writer.close();
/*     */ 
/*     */ 
/*     */       
/* 125 */       this._reader.setInput(http.getInputStream());
/*     */       
/* 127 */       XMLStreamReader xmlIn = this._reader.getStreamReader();
/* 128 */       while (xmlIn.hasNext()) {
/* 129 */         if (xmlIn.next() == 1 && xmlIn.getLocalName().equals("Body") && xmlIn.getNamespaceURI().equals("http://schemas.xmlsoap.org/soap/envelope/")) {
/*     */ 
/*     */ 
/*     */           
/* 133 */           xmlIn.next();
/* 134 */           readResponse(this._reader);
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 140 */       this._reader.close();
/* 141 */       this._writer.reset();
/* 142 */       this._out.reset();
/* 143 */       this._buffer.clear();
/* 144 */       this._utf8Writer.reset();
/* 145 */       this._reader.reset();
/*     */     } 
/*     */   }
/*     */   
/* 149 */   private final TextBuilder _buffer = new TextBuilder();
/* 150 */   private final AppendableWriter _out = new AppendableWriter();
/* 151 */   private final XMLObjectWriter _writer = new XMLObjectWriter();
/* 152 */   private final UTF8StreamWriter _utf8Writer = new UTF8StreamWriter();
/* 153 */   private final XMLObjectReader _reader = new XMLObjectReader();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract void writeRequest(XMLObjectWriter paramXMLObjectWriter) throws XMLStreamException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void readResponse(XMLObjectReader in) throws XMLStreamException {
/* 172 */     XMLStreamReader xml = in.getStreamReader();
/* 173 */     while (xml.hasNext()) {
/* 174 */       int i, n; switch (xml.next()) {
/*     */         case 7:
/* 176 */           System.out.println("Start Document");
/*     */           continue;
/*     */         case 8:
/* 179 */           System.out.println("End Document.");
/*     */           continue;
/*     */         case 1:
/* 182 */           System.out.println("Start Element: " + xml.getLocalName() + "(" + xml.getNamespaceURI() + ")");
/*     */           
/* 184 */           for (i = 0, n = xml.getAttributeCount(); i < n; i++) {
/* 185 */             System.out.println("   Attribute: " + xml.getAttributeLocalName(i) + "(" + xml.getAttributeNamespace(i) + "), Value: " + xml.getAttributeValue(i));
/*     */           }
/*     */           continue;
/*     */ 
/*     */ 
/*     */         
/*     */         case 2:
/* 192 */           if (xml.getLocalName().equals("Body") && xml.getNamespaceURI().equals("http://schemas.xmlsoap.org/soap/envelope/")) {
/*     */             return;
/*     */           }
/* 195 */           System.out.println("End Element: " + xml.getLocalName() + "(" + xml.getNamespaceURI() + ")");
/*     */           continue;
/*     */         
/*     */         case 4:
/* 199 */           System.out.println("Characters: " + xml.getText());
/*     */           continue;
/*     */         case 12:
/* 202 */           System.out.println("CDATA: " + xml.getText());
/*     */           continue;
/*     */         case 5:
/* 205 */           System.out.println("Comment: " + xml.getText());
/*     */           continue;
/*     */         case 6:
/* 208 */           System.out.println("Space");
/*     */           continue;
/*     */       } 
/* 211 */       System.out.println(xml);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final CharSequence csq(Object string) {
/* 219 */     return (string instanceof CharSequence) ? (CharSequence)string : (CharSequence)Text.valueOf(string);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/javolution/xml/ws/WebServiceClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */