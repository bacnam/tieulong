/*     */ package com.mchange.v2.codegen.bean;
/*     */ 
/*     */ import com.mchange.v1.xml.DomParseUtils;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.NodeList;
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
/*     */ public class ParsedPropertyBeanDocument
/*     */ {
/*  44 */   static final String[] EMPTY_SA = new String[0];
/*     */   
/*     */   String packageName;
/*     */   int class_modifiers;
/*     */   String className;
/*     */   String superclassName;
/*  50 */   String[] interfaceNames = EMPTY_SA;
/*  51 */   String[] generalImports = EMPTY_SA;
/*  52 */   String[] specificImports = EMPTY_SA;
/*     */   
/*     */   Property[] properties;
/*     */   
/*     */   public ParsedPropertyBeanDocument(Document paramDocument) {
/*  57 */     Element element1 = paramDocument.getDocumentElement();
/*  58 */     this.packageName = DomParseUtils.allTextFromUniqueChild(element1, "package");
/*  59 */     Element element2 = DomParseUtils.uniqueImmediateChild(element1, "modifiers");
/*  60 */     if (element2 != null) {
/*  61 */       this.class_modifiers = parseModifiers(element2);
/*     */     } else {
/*  63 */       this.class_modifiers = 1;
/*     */     } 
/*  65 */     Element element3 = DomParseUtils.uniqueChild(element1, "imports");
/*  66 */     if (element3 != null) {
/*     */       
/*  68 */       this.generalImports = DomParseUtils.allTextFromImmediateChildElements(element3, "general");
/*  69 */       this.specificImports = DomParseUtils.allTextFromImmediateChildElements(element3, "specific");
/*     */     } 
/*  71 */     this.className = DomParseUtils.allTextFromUniqueChild(element1, "output-class");
/*  72 */     this.superclassName = DomParseUtils.allTextFromUniqueChild(element1, "extends");
/*     */     
/*  74 */     Element element4 = DomParseUtils.uniqueChild(element1, "implements");
/*  75 */     if (element4 != null)
/*  76 */       this.interfaceNames = DomParseUtils.allTextFromImmediateChildElements(element4, "interface"); 
/*  77 */     Element element5 = DomParseUtils.uniqueChild(element1, "properties");
/*  78 */     this.properties = findProperties(element5);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ClassInfo getClassInfo() {
/*  84 */     return new ClassInfo()
/*     */       {
/*     */         public String getPackageName() {
/*  87 */           return ParsedPropertyBeanDocument.this.packageName;
/*     */         }
/*     */         public int getModifiers() {
/*  90 */           return ParsedPropertyBeanDocument.this.class_modifiers;
/*     */         }
/*     */         public String getClassName() {
/*  93 */           return ParsedPropertyBeanDocument.this.className;
/*     */         }
/*     */         public String getSuperclassName() {
/*  96 */           return ParsedPropertyBeanDocument.this.superclassName;
/*     */         }
/*     */         public String[] getInterfaceNames() {
/*  99 */           return ParsedPropertyBeanDocument.this.interfaceNames;
/*     */         }
/*     */         public String[] getGeneralImports() {
/* 102 */           return ParsedPropertyBeanDocument.this.generalImports;
/*     */         }
/*     */         public String[] getSpecificImports() {
/* 105 */           return ParsedPropertyBeanDocument.this.specificImports;
/*     */         }
/*     */       };
/*     */   }
/*     */   public Property[] getProperties() {
/* 110 */     return (Property[])this.properties.clone();
/*     */   }
/*     */   
/*     */   private Property[] findProperties(Element paramElement) {
/* 114 */     NodeList nodeList = DomParseUtils.immediateChildElementsByTagName(paramElement, "property");
/* 115 */     int i = nodeList.getLength();
/* 116 */     Property[] arrayOfProperty = new Property[i];
/* 117 */     for (byte b = 0; b < i; b++) {
/*     */       
/* 119 */       Element element1 = (Element)nodeList.item(b);
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
/* 132 */       int j = modifiersThroughParentElem(element1, "variable", 2);
/* 133 */       String str1 = DomParseUtils.allTextFromUniqueChild(element1, "name", true);
/* 134 */       String str2 = DomParseUtils.allTextFromUniqueChild(element1, "type", true);
/* 135 */       String str3 = DomParseUtils.allTextFromUniqueChild(element1, "defensive-copy", true);
/* 136 */       String str4 = DomParseUtils.allTextFromUniqueChild(element1, "default-value", true);
/* 137 */       int k = modifiersThroughParentElem(element1, "getter", 1);
/* 138 */       int m = modifiersThroughParentElem(element1, "setter", 1);
/* 139 */       Element element2 = DomParseUtils.uniqueChild(element1, "read-only");
/* 140 */       boolean bool1 = (element2 != null) ? true : false;
/* 141 */       Element element3 = DomParseUtils.uniqueChild(element1, "bound");
/* 142 */       boolean bool2 = (element3 != null) ? true : false;
/* 143 */       Element element4 = DomParseUtils.uniqueChild(element1, "constrained");
/* 144 */       boolean bool3 = (element4 != null) ? true : false;
/* 145 */       arrayOfProperty[b] = new SimpleProperty(j, str1, str2, str3, str4, k, m, bool1, bool2, bool3);
/*     */     } 
/*     */ 
/*     */     
/* 149 */     return arrayOfProperty;
/*     */   }
/*     */ 
/*     */   
/*     */   private static int modifiersThroughParentElem(Element paramElement, String paramString, int paramInt) {
/* 154 */     Element element = DomParseUtils.uniqueChild(paramElement, paramString);
/* 155 */     if (element != null) {
/*     */       
/* 157 */       Element element1 = DomParseUtils.uniqueChild(element, "modifiers");
/* 158 */       if (element1 != null) {
/* 159 */         return parseModifiers(element1);
/*     */       }
/* 161 */       return paramInt;
/*     */     } 
/*     */     
/* 164 */     return paramInt;
/*     */   }
/*     */ 
/*     */   
/*     */   private static int parseModifiers(Element paramElement) {
/* 169 */     int i = 0;
/* 170 */     String[] arrayOfString = DomParseUtils.allTextFromImmediateChildElements(paramElement, "modifier", true); byte b; int j;
/* 171 */     for (b = 0, j = arrayOfString.length; b < j; b++) {
/*     */       
/* 173 */       String str = arrayOfString[b];
/* 174 */       if ("public".equals(str)) { i |= 0x1; }
/* 175 */       else if ("protected".equals(str)) { i |= 0x4; }
/* 176 */       else if ("private".equals(str)) { i |= 0x2; }
/* 177 */       else if ("final".equals(str)) { i |= 0x10; }
/* 178 */       else if ("abstract".equals(str)) { i |= 0x400; }
/* 179 */       else if ("static".equals(str)) { i |= 0x8; }
/* 180 */       else if ("synchronized".equals(str)) { i |= 0x20; }
/* 181 */       else if ("volatile".equals(str)) { i |= 0x40; }
/* 182 */       else if ("transient".equals(str)) { i |= 0x80; }
/* 183 */       else if ("strictfp".equals(str)) { i |= 0x800; }
/* 184 */       else if ("native".equals(str)) { i |= 0x100; }
/* 185 */       else if ("interface".equals(str)) { i |= 0x200; }
/* 186 */       else { throw new IllegalArgumentException("Bad modifier: " + str); }
/*     */     
/* 188 */     }  return i;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/codegen/bean/ParsedPropertyBeanDocument.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */