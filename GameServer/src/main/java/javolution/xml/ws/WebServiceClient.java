package javolution.xml.ws;

import javolution.io.AppendableWriter;
import javolution.io.UTF8StreamWriter;
import javolution.text.Text;
import javolution.text.TextBuilder;
import javolution.xml.XMLObjectReader;
import javolution.xml.XMLObjectWriter;
import javolution.xml.stream.XMLStreamException;
import javolution.xml.stream.XMLStreamReader;
import javolution.xml.stream.XMLStreamWriter;

import java.io.IOException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

private final TextBuilder _buffer = new TextBuilder();
private final AppendableWriter _out = new AppendableWriter();
private final XMLObjectWriter _writer = new XMLObjectWriter();
private final UTF8StreamWriter _utf8Writer = new UTF8StreamWriter();
private final XMLObjectReader _reader = new XMLObjectReader();

private static final CharSequence csq(Object string) {
    return (string instanceof CharSequence) ? (CharSequence) string : (CharSequence) Text.valueOf(string);
}

protected abstract void writeRequest(XMLObjectWriter paramXMLObjectWriter) throws XMLStreamException;

protected void readResponse(XMLObjectReader in) throws XMLStreamException {
    XMLStreamReader xml = in.getStreamReader();
    while (xml.hasNext()) {
        int i, n;
        switch (xml.next()) {
            case 7:
                System.out.println("Start Document");
                continue;
            case 8:
                System.out.println("End Document.");
                continue;
            case 1:
                System.out.println("Start Element: " + xml.getLocalName() + "(" + xml.getNamespaceURI() + ")");

                for (i = 0, n = xml.getAttributeCount(); i < n; i++) {
                    System.out.println("   Attribute: " + xml.getAttributeLocalName(i) + "(" + xml.getAttributeNamespace(i) + "), Value: " + xml.getAttributeValue(i));
                }
                continue;

            case 2:
                if (xml.getLocalName().equals("Body") && xml.getNamespaceURI().equals("http:
                return;
        }
        System.out.println("End Element: " + xml.getLocalName() + "(" + xml.getNamespaceURI() + ")");
        continue;

        case 4:
            System.out.println("Characters: " + xml.getText());
            continue;
        case 12:
            System.out.println("CDATA: " + xml.getText());
            continue;
        case 5:
            System.out.println("Comment: " + xml.getText());
            continue;
        case 6:
            System.out.println("Space");
            continue;
    }
    System.out.println(xml);
} 
}

public abstract class WebServiceClient {
    public static final String ENVELOPE_PREFIX = "env";
    public static final String ENVELOPE_URI = "http:
    Object _url;

    {
        this._reader.close();
        this._writer.reset();
        this._out.reset();
        this._buffer.clear();
        this._utf8Writer.reset();
        this._reader.reset();
    }

        public WebServiceClient setAddress(String address) {
        try {
            this._url = new URL(address);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Malformed URL: " + address);
        }
        return this;
    } finally

public void invoke() throws IOException, XMLStreamException {
        try {
            this._out.setOutput((Appendable) this._buffer);
            this._writer.setOutput((Writer) this._out);
            XMLStreamWriter xmlOut = this._writer.getStreamWriter();
            xmlOut.setPrefix(csq("env"), csq("http:
                    xmlOut.writeStartElement(csq("http:
                            xmlOut.writeNamespace(csq("env"), csq("http:
                                    xmlOut.writeStartElement(csq("http:
                                            xmlOut.writeEndElement();
            xmlOut.writeStartElement(csq("http:
                    writeRequest(this._writer);
            this._writer.close();

            if (this._url == null) {
                throw new IOException("URL not set");
            }
            HttpURLConnection http = (HttpURLConnection) ((URL) this._url).openConnection();

            http.setRequestProperty("Content-Length", String.valueOf(this._buffer.length()));

            http.setRequestProperty("Content-Type", "text/xml; charset=utf-8");

            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.setDoInput(true);
            this._utf8Writer.setOutput(http.getOutputStream());

            this._utf8Writer.append((CharSequence) this._buffer);
            this._utf8Writer.close();

            this._reader.setInput(http.getInputStream());

            XMLStreamReader xmlIn = this._reader.getStreamReader();
            while (xmlIn.hasNext()) {
                if ( xmlIn.next() == 1 && xmlIn.getLocalName().equals("Body") && xmlIn.getNamespaceURI().equals("http:

                        xmlIn.next();
                readResponse(this._reader);

                break;
            }
        }
    }
}
}

