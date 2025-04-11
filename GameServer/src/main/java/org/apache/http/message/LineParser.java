package org.apache.http.message;

import org.apache.http.*;
import org.apache.http.util.CharArrayBuffer;

public interface LineParser {
    ProtocolVersion parseProtocolVersion(CharArrayBuffer paramCharArrayBuffer, ParserCursor paramParserCursor) throws ParseException;

    boolean hasProtocolVersion(CharArrayBuffer paramCharArrayBuffer, ParserCursor paramParserCursor);

    RequestLine parseRequestLine(CharArrayBuffer paramCharArrayBuffer, ParserCursor paramParserCursor) throws ParseException;

    StatusLine parseStatusLine(CharArrayBuffer paramCharArrayBuffer, ParserCursor paramParserCursor) throws ParseException;

    Header parseHeader(CharArrayBuffer paramCharArrayBuffer) throws ParseException;
}

