package com.mchange.v1.xmlprops;

import com.mchange.v1.xml.ResourceEntityResolver;
import com.mchange.v1.xml.StdErrErrorHandler;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;

public class DomXmlPropsParser
{
static final String XMLPROPS_NAMESPACE_URI = "http:
static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

static {
factory.setNamespaceAware(true);
factory.setValidating(true);
}

public Properties parseXmlProps(InputStream paramInputStream) throws XmlPropsException {
return parseXmlProps(new InputSource(paramInputStream), (EntityResolver)new ResourceEntityResolver(getClass()), (ErrorHandler)new StdErrErrorHandler());
}

private Properties parseXmlProps(InputSource paramInputSource, EntityResolver paramEntityResolver, ErrorHandler paramErrorHandler) throws XmlPropsException {
try {
Properties properties = new Properties();

DocumentBuilder documentBuilder = factory.newDocumentBuilder();
documentBuilder.setEntityResolver(paramEntityResolver);
documentBuilder.setErrorHandler(paramErrorHandler);

Document document = documentBuilder.parse(paramInputSource);
Element element = document.getDocumentElement();

NodeList nodeList = element.getElementsByTagName("property"); byte b; int i;
for (b = 0, i = nodeList.getLength(); b < i; b++) {

Element element1 = (Element)nodeList.item(b);

String str = element1.getAttribute("name");
StringBuffer stringBuffer = new StringBuffer();
NodeList nodeList1 = element1.getChildNodes(); byte b1; int j;
for (b1 = 0, j = nodeList1.getLength(); b1 < j; b1++) {

Node node = nodeList1.item(b1);
if (node.getNodeType() == 3) {
stringBuffer.append(node.getNodeValue());
}
} 

properties.put(str, stringBuffer.toString());
} 

return properties;
}
catch (Exception exception) {

exception.printStackTrace();
throw new XmlPropsException(exception);
} 
}

public static void main(String[] paramArrayOfString) {
try {
BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(paramArrayOfString[0]));
DomXmlPropsParser domXmlPropsParser = new DomXmlPropsParser();
Properties properties = domXmlPropsParser.parseXmlProps(bufferedInputStream);
for (String str1 : properties.keySet())
{

String str2 = properties.getProperty(str1);
System.err.println(str1 + '=' + str2);
}

} catch (Exception exception) {
exception.printStackTrace();
} 
}
}

