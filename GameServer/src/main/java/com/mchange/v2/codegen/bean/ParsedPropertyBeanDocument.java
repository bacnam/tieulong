package com.mchange.v2.codegen.bean;

import com.mchange.v1.xml.DomParseUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ParsedPropertyBeanDocument {
    static final String[] EMPTY_SA = new String[0];

    String packageName;
    int class_modifiers;
    String className;
    String superclassName;
    String[] interfaceNames = EMPTY_SA;
    String[] generalImports = EMPTY_SA;
    String[] specificImports = EMPTY_SA;

    Property[] properties;

    public ParsedPropertyBeanDocument(Document paramDocument) {
        Element element1 = paramDocument.getDocumentElement();
        this.packageName = DomParseUtils.allTextFromUniqueChild(element1, "package");
        Element element2 = DomParseUtils.uniqueImmediateChild(element1, "modifiers");
        if (element2 != null) {
            this.class_modifiers = parseModifiers(element2);
        } else {
            this.class_modifiers = 1;
        }
        Element element3 = DomParseUtils.uniqueChild(element1, "imports");
        if (element3 != null) {

            this.generalImports = DomParseUtils.allTextFromImmediateChildElements(element3, "general");
            this.specificImports = DomParseUtils.allTextFromImmediateChildElements(element3, "specific");
        }
        this.className = DomParseUtils.allTextFromUniqueChild(element1, "output-class");
        this.superclassName = DomParseUtils.allTextFromUniqueChild(element1, "extends");

        Element element4 = DomParseUtils.uniqueChild(element1, "implements");
        if (element4 != null)
            this.interfaceNames = DomParseUtils.allTextFromImmediateChildElements(element4, "interface");
        Element element5 = DomParseUtils.uniqueChild(element1, "properties");
        this.properties = findProperties(element5);
    }

    private static int modifiersThroughParentElem(Element paramElement, String paramString, int paramInt) {
        Element element = DomParseUtils.uniqueChild(paramElement, paramString);
        if (element != null) {

            Element element1 = DomParseUtils.uniqueChild(element, "modifiers");
            if (element1 != null) {
                return parseModifiers(element1);
            }
            return paramInt;
        }

        return paramInt;
    }

    private static int parseModifiers(Element paramElement) {
        int i = 0;
        String[] arrayOfString = DomParseUtils.allTextFromImmediateChildElements(paramElement, "modifier", true);
        byte b;
        int j;
        for (b = 0, j = arrayOfString.length; b < j; b++) {

            String str = arrayOfString[b];
            if ("public".equals(str)) {
                i |= 0x1;
            } else if ("protected".equals(str)) {
                i |= 0x4;
            } else if ("private".equals(str)) {
                i |= 0x2;
            } else if ("final".equals(str)) {
                i |= 0x10;
            } else if ("abstract".equals(str)) {
                i |= 0x400;
            } else if ("static".equals(str)) {
                i |= 0x8;
            } else if ("synchronized".equals(str)) {
                i |= 0x20;
            } else if ("volatile".equals(str)) {
                i |= 0x40;
            } else if ("transient".equals(str)) {
                i |= 0x80;
            } else if ("strictfp".equals(str)) {
                i |= 0x800;
            } else if ("native".equals(str)) {
                i |= 0x100;
            } else if ("interface".equals(str)) {
                i |= 0x200;
            } else {
                throw new IllegalArgumentException("Bad modifier: " + str);
            }

        }
        return i;
    }

    public ClassInfo getClassInfo() {
        return new ClassInfo() {
            public String getPackageName() {
                return ParsedPropertyBeanDocument.this.packageName;
            }

            public int getModifiers() {
                return ParsedPropertyBeanDocument.this.class_modifiers;
            }

            public String getClassName() {
                return ParsedPropertyBeanDocument.this.className;
            }

            public String getSuperclassName() {
                return ParsedPropertyBeanDocument.this.superclassName;
            }

            public String[] getInterfaceNames() {
                return ParsedPropertyBeanDocument.this.interfaceNames;
            }

            public String[] getGeneralImports() {
                return ParsedPropertyBeanDocument.this.generalImports;
            }

            public String[] getSpecificImports() {
                return ParsedPropertyBeanDocument.this.specificImports;
            }
        };
    }

    public Property[] getProperties() {
        return (Property[]) this.properties.clone();
    }

    private Property[] findProperties(Element paramElement) {
        NodeList nodeList = DomParseUtils.immediateChildElementsByTagName(paramElement, "property");
        int i = nodeList.getLength();
        Property[] arrayOfProperty = new Property[i];
        for (byte b = 0; b < i; b++) {

            Element element1 = (Element) nodeList.item(b);

            int j = modifiersThroughParentElem(element1, "variable", 2);
            String str1 = DomParseUtils.allTextFromUniqueChild(element1, "name", true);
            String str2 = DomParseUtils.allTextFromUniqueChild(element1, "type", true);
            String str3 = DomParseUtils.allTextFromUniqueChild(element1, "defensive-copy", true);
            String str4 = DomParseUtils.allTextFromUniqueChild(element1, "default-value", true);
            int k = modifiersThroughParentElem(element1, "getter", 1);
            int m = modifiersThroughParentElem(element1, "setter", 1);
            Element element2 = DomParseUtils.uniqueChild(element1, "read-only");
            boolean bool1 = (element2 != null) ? true : false;
            Element element3 = DomParseUtils.uniqueChild(element1, "bound");
            boolean bool2 = (element3 != null) ? true : false;
            Element element4 = DomParseUtils.uniqueChild(element1, "constrained");
            boolean bool3 = (element4 != null) ? true : false;
            arrayOfProperty[b] = new SimpleProperty(j, str1, str2, str3, str4, k, m, bool1, bool2, bool3);
        }

        return arrayOfProperty;
    }
}

