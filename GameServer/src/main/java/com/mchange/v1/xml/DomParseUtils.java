package com.mchange.v1.xml;

import com.mchange.v1.util.DebugUtils;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public final class DomParseUtils
{
static final boolean DEBUG = true;

public static String allTextFromUniqueChild(Element paramElement, String paramString) throws DOMException {
return allTextFromUniqueChild(paramElement, paramString, false);
}

public static String allTextFromUniqueChild(Element paramElement, String paramString, boolean paramBoolean) throws DOMException {
Element element = uniqueChildByTagName(paramElement, paramString);
if (element == null) {
return null;
}
return allTextFromElement(element, paramBoolean);
}

public static Element uniqueChild(Element paramElement, String paramString) throws DOMException {
return uniqueChildByTagName(paramElement, paramString);
}

public static Element uniqueChildByTagName(Element paramElement, String paramString) throws DOMException {
NodeList nodeList = paramElement.getElementsByTagName(paramString);
int i = nodeList.getLength();

DebugUtils.myAssert((i <= 1), "There is more than one (" + i + ") child with tag name: " + paramString + "!!!");

return (i == 1) ? (Element)nodeList.item(0) : null;
}

public static String allText(Element paramElement) throws DOMException {
return allTextFromElement(paramElement);
}
public static String allText(Element paramElement, boolean paramBoolean) throws DOMException {
return allTextFromElement(paramElement, paramBoolean);
}

public static String allTextFromElement(Element paramElement) throws DOMException {
return allTextFromElement(paramElement, false);
}

public static String allTextFromElement(Element paramElement, boolean paramBoolean) throws DOMException {
StringBuffer stringBuffer = new StringBuffer();
NodeList nodeList = paramElement.getChildNodes(); byte b; int i;
for (b = 0, i = nodeList.getLength(); b < i; b++) {

Node node = nodeList.item(b);
if (node instanceof org.w3c.dom.Text)
stringBuffer.append(node.getNodeValue()); 
} 
String str = stringBuffer.toString();
return paramBoolean ? str.trim() : str;
}

public static String[] allTextFromImmediateChildElements(Element paramElement, String paramString) throws DOMException {
return allTextFromImmediateChildElements(paramElement, paramString, false);
}

public static String[] allTextFromImmediateChildElements(Element paramElement, String paramString, boolean paramBoolean) throws DOMException {
NodeList nodeList = immediateChildElementsByTagName(paramElement, paramString);
int i = nodeList.getLength();
String[] arrayOfString = new String[i];
for (byte b = 0; b < i; b++)
arrayOfString[b] = allText((Element)nodeList.item(b), paramBoolean); 
return arrayOfString;
}

public static NodeList immediateChildElementsByTagName(Element paramElement, String paramString) throws DOMException {
return getImmediateChildElementsByTagName(paramElement, paramString);
}

public static NodeList getImmediateChildElementsByTagName(Element paramElement, String paramString) throws DOMException {
final ArrayList<Node> nodes = new ArrayList();
for (Node node = paramElement.getFirstChild(); node != null; node = node.getNextSibling()) {
if (node instanceof Element && ((Element)node).getTagName().equals(paramString))
arrayList.add(node); 
}  return new NodeList()
{
public int getLength() {
return nodes.size();
}
public Node item(int param1Int) {
return nodes.get(param1Int);
}
};
}

public static String allTextFromUniqueImmediateChild(Element paramElement, String paramString) throws DOMException {
Element element = uniqueImmediateChildByTagName(paramElement, paramString);
if (element == null)
return null; 
return allTextFromElement(element);
}

public static Element uniqueImmediateChild(Element paramElement, String paramString) throws DOMException {
return uniqueImmediateChildByTagName(paramElement, paramString);
}

public static Element uniqueImmediateChildByTagName(Element paramElement, String paramString) throws DOMException {
NodeList nodeList = getImmediateChildElementsByTagName(paramElement, paramString);
int i = nodeList.getLength();

DebugUtils.myAssert((i <= 1), "There is more than one (" + i + ") child with tag name: " + paramString + "!!!");

return (i == 1) ? (Element)nodeList.item(0) : null;
}

public static String attrValFromElement(Element paramElement, String paramString) throws DOMException {
Attr attr = paramElement.getAttributeNode(paramString);
return (attr == null) ? null : attr.getValue();
}
}

