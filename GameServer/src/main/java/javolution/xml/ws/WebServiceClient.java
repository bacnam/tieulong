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
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public abstract class WebServiceClient {

    public static final String ENVELOPE_PREFIX = "env";
    public static final String ENVELOPE_URI = "http://schemas.xmlsoap.org/soap/envelope/";

    private final TextBuilder _buffer = new TextBuilder();
    private final AppendableWriter _out = new AppendableWriter();
    private final XMLObjectWriter _writer = new XMLObjectWriter();
    private final UTF8StreamWriter _utf8Writer = new UTF8StreamWriter();
    private final XMLObjectReader _reader = new XMLObjectReader();

    private URL _url;

    /** Thiết lập endpoint */
    public WebServiceClient setAddress(String address) {
        try {
            this._url = new URL(address);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Malformed URL: " + address);
        }
        return this;
    }

    /** Hàm abstract để client override – nội dung request */
    protected abstract void writeRequest(XMLObjectWriter writer) throws XMLStreamException;

    /** Hàm override tùy chọn – đọc phản hồi */
    protected void readResponse(XMLObjectReader reader) throws XMLStreamException {
        XMLStreamReader xml = reader.getStreamReader();
        while (xml.hasNext()) {
            int type = xml.next();
            switch (type) {
                case XMLStreamReader.START_ELEMENT:
                    System.out.println("Start Element: " + xml.getLocalName());
                    break;
                case XMLStreamReader.END_ELEMENT:
                    System.out.println("End Element: " + xml.getLocalName());
                    break;
                case XMLStreamReader.CHARACTERS:
                    System.out.println("Text: " + xml.getText());
                    break;
            }
        }
    }

    /** Gửi và xử lý SOAP request */
    public void invoke() throws IOException, XMLStreamException {
        // 1. Viết SOAP envelope vào buffer
        _out.setOutput(_buffer);
        _writer.setOutput((Writer) _out);
        XMLStreamWriter xmlOut = _writer.getStreamWriter();

        xmlOut.setPrefix(ENVELOPE_PREFIX, ENVELOPE_URI);
        xmlOut.writeStartElement(ENVELOPE_URI, "Envelope");
        xmlOut.writeNamespace(ENVELOPE_PREFIX, ENVELOPE_URI);

        xmlOut.writeStartElement(ENVELOPE_URI, "Header");
        xmlOut.writeEndElement(); // Header

        xmlOut.writeStartElement(ENVELOPE_URI, "Body");
        writeRequest(_writer);
        xmlOut.writeEndElement(); // Body

        xmlOut.writeEndElement(); // Envelope
        _writer.close();

        // 2. Gửi HTTP POST
        if (_url == null) throw new IOException("URL not set");

        HttpURLConnection conn = (HttpURLConnection) _url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
        conn.setRequestProperty("Content-Length", String.valueOf(_buffer.length()));

        _utf8Writer.setOutput(conn.getOutputStream());
        _utf8Writer.append(_buffer);
        _utf8Writer.close();

        // 3. Nhận phản hồi
        _reader.setInput(conn.getInputStream());

        XMLStreamReader xmlIn = _reader.getStreamReader();
        while (xmlIn.hasNext()) {
            if (xmlIn.next() == XMLStreamReader.START_ELEMENT &&
                    "Body".equals(xmlIn.getLocalName()) &&
                    ENVELOPE_URI.equals(xmlIn.getNamespaceURI())) {
                xmlIn.next(); // Tiến tới phần nội dung thật sự
                readResponse(_reader);
                break;
            }
        }

        // 4. Reset
        _reader.close();
        _writer.reset();
        _out.reset();
        _buffer.clear();
        _utf8Writer.reset();
    }

    /** Hỗ trợ chuỗi an toàn */
    private static CharSequence csq(Object str) {
        return (str instanceof CharSequence) ? (CharSequence) str : Text.valueOf(str);
    }
}
