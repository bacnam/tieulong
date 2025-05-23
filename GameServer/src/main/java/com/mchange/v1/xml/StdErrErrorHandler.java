package com.mchange.v1.xml;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class StdErrErrorHandler
implements ErrorHandler
{
public void warning(SAXParseException paramSAXParseException) {
System.err.println("[Warning]");
showExceptionInformation(paramSAXParseException);
paramSAXParseException.printStackTrace();
}

public void error(SAXParseException paramSAXParseException) {
System.err.println("[Error]");
showExceptionInformation(paramSAXParseException);
paramSAXParseException.printStackTrace();
}

public void fatalError(SAXParseException paramSAXParseException) throws SAXException {
System.err.println("[Fatal Error]");
showExceptionInformation(paramSAXParseException);
paramSAXParseException.printStackTrace();
throw paramSAXParseException;
}

private void showExceptionInformation(SAXParseException paramSAXParseException) {
System.err.println("[\tLine Number: " + paramSAXParseException.getLineNumber() + ']');
System.err.println("[\tColumn Number: " + paramSAXParseException.getColumnNumber() + ']');
System.err.println("[\tPublic ID: " + paramSAXParseException.getPublicId() + ']');
System.err.println("[\tSystem ID: " + paramSAXParseException.getSystemId() + ']');
}
}

