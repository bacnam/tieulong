/*     */ package com.mchange.v2.codegen.bean;
/*     */ 
/*     */ import com.mchange.v1.lang.ClassUtils;
/*     */ import com.mchange.v2.codegen.CodegenUtils;
/*     */ import com.mchange.v2.codegen.IndentedWriter;
/*     */ import com.mchange.v2.log.MLevel;
/*     */ import com.mchange.v2.log.MLog;
/*     */ import com.mchange.v2.log.MLogger;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Date;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
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
/*     */ public class SimplePropertyBeanGenerator
/*     */   implements PropertyBeanGenerator
/*     */ {
/*  48 */   private static final MLogger logger = MLog.getLogger(SimplePropertyBeanGenerator.class);
/*     */   
/*     */   private boolean inner = false;
/*  51 */   private int java_version = 140;
/*     */   private boolean force_unmodifiable = false;
/*  53 */   private String generatorName = getClass().getName();
/*     */   
/*     */   protected ClassInfo info;
/*     */   
/*     */   protected Property[] props;
/*     */   
/*     */   protected IndentedWriter iw;
/*     */   
/*     */   protected Set generalImports;
/*     */   
/*     */   protected Set specificImports;
/*     */   protected Set interfaceNames;
/*     */   protected Class superclassType;
/*     */   protected List interfaceTypes;
/*     */   protected Class[] propertyTypes;
/*  68 */   protected List generatorExtensions = new ArrayList();
/*     */   
/*     */   public synchronized void setInner(boolean paramBoolean) {
/*  71 */     this.inner = paramBoolean;
/*     */   }
/*     */   public synchronized boolean isInner() {
/*  74 */     return this.inner;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void setJavaVersion(int paramInt) {
/*  80 */     this.java_version = paramInt;
/*     */   }
/*     */   public synchronized int getJavaVersion() {
/*  83 */     return this.java_version;
/*     */   }
/*     */   public synchronized void setGeneratorName(String paramString) {
/*  86 */     this.generatorName = paramString;
/*     */   }
/*     */   public synchronized String getGeneratorName() {
/*  89 */     return this.generatorName;
/*     */   }
/*     */   public synchronized void setForceUnmodifiable(boolean paramBoolean) {
/*  92 */     this.force_unmodifiable = paramBoolean;
/*     */   }
/*     */   public synchronized boolean isForceUnmodifiable() {
/*  95 */     return this.force_unmodifiable;
/*     */   }
/*     */   public synchronized void addExtension(GeneratorExtension paramGeneratorExtension) {
/*  98 */     this.generatorExtensions.add(paramGeneratorExtension);
/*     */   }
/*     */   public synchronized void removeExtension(GeneratorExtension paramGeneratorExtension) {
/* 101 */     this.generatorExtensions.remove(paramGeneratorExtension);
/*     */   }
/*     */   
/*     */   public synchronized void generate(ClassInfo paramClassInfo, Property[] paramArrayOfProperty, Writer paramWriter) throws IOException {
/* 105 */     this.info = paramClassInfo;
/* 106 */     this.props = paramArrayOfProperty;
/* 107 */     Arrays.sort(paramArrayOfProperty, BeangenUtils.PROPERTY_COMPARATOR);
/* 108 */     this.iw = (paramWriter instanceof IndentedWriter) ? (IndentedWriter)paramWriter : new IndentedWriter(paramWriter);
/*     */     
/* 110 */     this.generalImports = new TreeSet();
/* 111 */     if (paramClassInfo.getGeneralImports() != null) {
/* 112 */       this.generalImports.addAll(Arrays.asList(paramClassInfo.getGeneralImports()));
/*     */     }
/* 114 */     this.specificImports = new TreeSet();
/* 115 */     if (paramClassInfo.getSpecificImports() != null) {
/* 116 */       this.specificImports.addAll(Arrays.asList(paramClassInfo.getSpecificImports()));
/*     */     }
/* 118 */     this.interfaceNames = new TreeSet();
/* 119 */     if (paramClassInfo.getInterfaceNames() != null) {
/* 120 */       this.interfaceNames.addAll(Arrays.asList(paramClassInfo.getInterfaceNames()));
/*     */     }
/* 122 */     addInternalImports();
/* 123 */     addInternalInterfaces();
/*     */     
/* 125 */     resolveTypes();
/*     */     
/* 127 */     if (!this.inner) {
/*     */       
/* 129 */       writeHeader();
/* 130 */       this.iw.println();
/*     */     } 
/* 132 */     generateClassJavaDocComment();
/* 133 */     writeClassDeclaration();
/* 134 */     this.iw.println('{');
/* 135 */     this.iw.upIndent();
/*     */     
/* 137 */     writeCoreBody();
/*     */     
/* 139 */     this.iw.downIndent();
/* 140 */     this.iw.println('}');
/*     */   }
/*     */ 
/*     */   
/*     */   protected void resolveTypes() {
/* 145 */     String[] arrayOfString1 = (String[])this.generalImports.toArray((Object[])new String[this.generalImports.size()]);
/* 146 */     String[] arrayOfString2 = (String[])this.specificImports.toArray((Object[])new String[this.specificImports.size()]);
/*     */     
/* 148 */     if (this.info.getSuperclassName() != null) {
/*     */       
/*     */       try {
/* 151 */         this.superclassType = ClassUtils.forName(this.info.getSuperclassName(), arrayOfString1, arrayOfString2);
/* 152 */       } catch (Exception exception) {
/*     */ 
/*     */ 
/*     */         
/* 156 */         if (logger.isLoggable(MLevel.WARNING)) {
/* 157 */           logger.warning(getClass().getName() + " could not resolve superclass '" + this.info.getSuperclassName() + "'.");
/*     */         }
/* 159 */         this.superclassType = null;
/*     */       } 
/*     */     }
/*     */     
/* 163 */     this.interfaceTypes = new ArrayList(this.interfaceNames.size());
/* 164 */     for (String str : this.interfaceNames) {
/*     */ 
/*     */       
/*     */       try {
/* 168 */         this.interfaceTypes.add(ClassUtils.forName(str, arrayOfString1, arrayOfString2));
/* 169 */       } catch (Exception exception) {
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 174 */         if (logger.isLoggable(MLevel.WARNING)) {
/* 175 */           logger.warning(getClass().getName() + " could not resolve interface '" + str + "'.");
/*     */         }
/* 177 */         this.interfaceTypes.add(null);
/*     */       } 
/*     */     } 
/*     */     
/* 181 */     this.propertyTypes = new Class[this.props.length]; byte b; int i;
/* 182 */     for (b = 0, i = this.props.length; b < i; b++) {
/*     */       
/* 184 */       String str = this.props[b].getSimpleTypeName();
/*     */       try {
/* 186 */         this.propertyTypes[b] = ClassUtils.forName(str, arrayOfString1, arrayOfString2);
/* 187 */       } catch (Exception exception) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 193 */         if (logger.isLoggable(MLevel.WARNING)) {
/* 194 */           logger.log(MLevel.WARNING, getClass().getName() + " could not resolve property type '" + str + "'.", exception);
/*     */         }
/* 196 */         this.propertyTypes[b] = null;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addInternalImports() {
/* 203 */     if (boundProperties()) {
/*     */       
/* 205 */       this.specificImports.add("java.beans.PropertyChangeEvent");
/* 206 */       this.specificImports.add("java.beans.PropertyChangeSupport");
/* 207 */       this.specificImports.add("java.beans.PropertyChangeListener");
/*     */     } 
/* 209 */     if (constrainedProperties()) {
/*     */       
/* 211 */       this.specificImports.add("java.beans.PropertyChangeEvent");
/* 212 */       this.specificImports.add("java.beans.PropertyVetoException");
/* 213 */       this.specificImports.add("java.beans.VetoableChangeSupport");
/* 214 */       this.specificImports.add("java.beans.VetoableChangeListener");
/*     */     } 
/*     */     
/* 217 */     for (GeneratorExtension generatorExtension : this.generatorExtensions) {
/*     */ 
/*     */       
/* 220 */       this.specificImports.addAll(generatorExtension.extraSpecificImports());
/* 221 */       this.generalImports.addAll(generatorExtension.extraGeneralImports());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addInternalInterfaces() {
/* 227 */     for (GeneratorExtension generatorExtension : this.generatorExtensions)
/*     */     {
/*     */       
/* 230 */       this.interfaceNames.addAll(generatorExtension.extraInterfaceNames());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void writeCoreBody() throws IOException {
/* 236 */     writeJavaBeansChangeSupport();
/* 237 */     writePropertyVariables();
/* 238 */     writeOtherVariables();
/* 239 */     this.iw.println();
/*     */     
/* 241 */     writeGetterSetterPairs();
/* 242 */     if (boundProperties()) {
/*     */       
/* 244 */       this.iw.println();
/* 245 */       writeBoundPropertyEventSourceMethods();
/*     */     } 
/* 247 */     if (constrainedProperties()) {
/*     */       
/* 249 */       this.iw.println();
/* 250 */       writeConstrainedPropertyEventSourceMethods();
/*     */     } 
/* 252 */     writeInternalUtilityFunctions();
/* 253 */     writeOtherFunctions();
/*     */     
/* 255 */     writeOtherClasses();
/*     */     
/* 257 */     String[] arrayOfString1 = (String[])this.interfaceNames.toArray((Object[])new String[this.interfaceNames.size()]);
/* 258 */     String[] arrayOfString2 = (String[])this.generalImports.toArray((Object[])new String[this.generalImports.size()]);
/* 259 */     String[] arrayOfString3 = (String[])this.specificImports.toArray((Object[])new String[this.specificImports.size()]);
/* 260 */     SimpleClassInfo simpleClassInfo = new SimpleClassInfo(this.info.getPackageName(), this.info.getModifiers(), this.info.getClassName(), this.info.getSuperclassName(), arrayOfString1, arrayOfString2, arrayOfString3);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 267 */     for (GeneratorExtension generatorExtension : this.generatorExtensions) {
/*     */ 
/*     */       
/* 270 */       this.iw.println();
/* 271 */       generatorExtension.generate(simpleClassInfo, this.superclassType, this.props, this.propertyTypes, this.iw);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void writeInternalUtilityFunctions() throws IOException {
/* 277 */     this.iw.println("private boolean eqOrBothNull( Object a, Object b )");
/* 278 */     this.iw.println("{");
/* 279 */     this.iw.upIndent();
/*     */     
/* 281 */     this.iw.println("return");
/* 282 */     this.iw.upIndent();
/* 283 */     this.iw.println("a == b ||");
/* 284 */     this.iw.println("(a != null && a.equals(b));");
/* 285 */     this.iw.downIndent();
/*     */     
/* 287 */     this.iw.downIndent();
/* 288 */     this.iw.println("}");
/*     */   }
/*     */ 
/*     */   
/*     */   protected void writeConstrainedPropertyEventSourceMethods() throws IOException {
/* 293 */     this.iw.println("public void addVetoableChangeListener( VetoableChangeListener vcl )");
/* 294 */     this.iw.println("{ vcs.addVetoableChangeListener( vcl ); }");
/* 295 */     this.iw.println();
/*     */     
/* 297 */     this.iw.println("public void removeVetoableChangeListener( VetoableChangeListener vcl )");
/* 298 */     this.iw.println("{ vcs.removeVetoableChangeListener( vcl ); }");
/* 299 */     this.iw.println();
/*     */     
/* 301 */     if (this.java_version >= 140) {
/*     */       
/* 303 */       this.iw.println("public VetoableChangeListener[] getVetoableChangeListeners()");
/* 304 */       this.iw.println("{ return vcs.getVetoableChangeListeners(); }");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void writeBoundPropertyEventSourceMethods() throws IOException {
/* 310 */     this.iw.println("public void addPropertyChangeListener( PropertyChangeListener pcl )");
/* 311 */     this.iw.println("{ pcs.addPropertyChangeListener( pcl ); }");
/* 312 */     this.iw.println();
/*     */     
/* 314 */     this.iw.println("public void addPropertyChangeListener( String propName, PropertyChangeListener pcl )");
/* 315 */     this.iw.println("{ pcs.addPropertyChangeListener( propName, pcl ); }");
/* 316 */     this.iw.println();
/*     */     
/* 318 */     this.iw.println("public void removePropertyChangeListener( PropertyChangeListener pcl )");
/* 319 */     this.iw.println("{ pcs.removePropertyChangeListener( pcl ); }");
/* 320 */     this.iw.println();
/*     */     
/* 322 */     this.iw.println("public void removePropertyChangeListener( String propName, PropertyChangeListener pcl )");
/* 323 */     this.iw.println("{ pcs.removePropertyChangeListener( propName, pcl ); }");
/* 324 */     this.iw.println();
/*     */     
/* 326 */     if (this.java_version >= 140) {
/*     */       
/* 328 */       this.iw.println("public PropertyChangeListener[] getPropertyChangeListeners()");
/* 329 */       this.iw.println("{ return pcs.getPropertyChangeListeners(); }");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void writeJavaBeansChangeSupport() throws IOException {
/* 335 */     if (boundProperties()) {
/*     */       
/* 337 */       this.iw.println("protected PropertyChangeSupport pcs = new PropertyChangeSupport( this );");
/* 338 */       this.iw.println();
/* 339 */       this.iw.println("protected PropertyChangeSupport getPropertyChangeSupport()");
/* 340 */       this.iw.println("{ return pcs; }");
/*     */     } 
/*     */     
/* 343 */     if (constrainedProperties()) {
/*     */       
/* 345 */       this.iw.println("protected VetoableChangeSupport vcs = new VetoableChangeSupport( this );");
/* 346 */       this.iw.println();
/* 347 */       this.iw.println("protected VetoableChangeSupport getVetoableChangeSupport()");
/* 348 */       this.iw.println("{ return vcs; }");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void writeOtherVariables() throws IOException {}
/*     */ 
/*     */   
/*     */   protected void writeOtherFunctions() throws IOException {}
/*     */   
/*     */   protected void writeOtherClasses() throws IOException {}
/*     */   
/*     */   protected void writePropertyVariables() throws IOException {
/*     */     byte b;
/*     */     int i;
/* 363 */     for (b = 0, i = this.props.length; b < i; b++) {
/* 364 */       writePropertyVariable(this.props[b]);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void writePropertyVariable(Property paramProperty) throws IOException {
/* 369 */     BeangenUtils.writePropertyVariable(paramProperty, this.iw);
/*     */   }
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
/*     */   protected void writePropertyMembers() throws IOException {
/* 382 */     throw new InternalError("writePropertyMembers() deprecated and removed. please us writePropertyVariables().");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writePropertyMember(Property paramProperty) throws IOException {
/* 388 */     throw new InternalError("writePropertyMember() deprecated and removed. please us writePropertyVariable().");
/*     */   } protected void writeGetterSetterPairs() throws IOException {
/*     */     byte b;
/*     */     int i;
/* 392 */     for (b = 0, i = this.props.length; b < i; b++) {
/*     */       
/* 394 */       writeGetterSetterPair(this.props[b], this.propertyTypes[b]);
/* 395 */       if (b != i - 1) this.iw.println();
/*     */     
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void writeGetterSetterPair(Property paramProperty, Class paramClass) throws IOException {
/* 401 */     writePropertyGetter(paramProperty, paramClass);
/*     */     
/* 403 */     if (!paramProperty.isReadOnly() && !this.force_unmodifiable) {
/*     */       
/* 405 */       this.iw.println();
/* 406 */       writePropertySetter(paramProperty, paramClass);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void writePropertyGetter(Property paramProperty, Class paramClass) throws IOException {
/* 412 */     BeangenUtils.writePropertyGetter(paramProperty, getGetterDefensiveCopyExpression(paramProperty, paramClass), this.iw);
/*     */   }
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
/*     */   protected void writePropertySetter(Property paramProperty, Class paramClass) throws IOException {
/* 428 */     BeangenUtils.writePropertySetter(paramProperty, getSetterDefensiveCopyExpression(paramProperty, paramClass), this.iw);
/*     */   }
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
/*     */   protected String getGetterDefensiveCopyExpression(Property paramProperty, Class paramClass) {
/* 510 */     return paramProperty.getDefensiveCopyExpression();
/*     */   }
/*     */   protected String getSetterDefensiveCopyExpression(Property paramProperty, Class paramClass) {
/* 513 */     return paramProperty.getDefensiveCopyExpression();
/*     */   }
/*     */   protected String getConstructorDefensiveCopyExpression(Property paramProperty, Class paramClass) {
/* 516 */     return paramProperty.getDefensiveCopyExpression();
/*     */   }
/*     */   
/*     */   protected void writeHeader() throws IOException {
/* 520 */     writeBannerComments();
/* 521 */     this.iw.println();
/* 522 */     this.iw.println("package " + this.info.getPackageName() + ';');
/* 523 */     this.iw.println();
/* 524 */     writeImports();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void writeBannerComments() throws IOException {
/* 529 */     this.iw.println("/*");
/* 530 */     this.iw.println(" * This class autogenerated by " + this.generatorName + '.');
/* 531 */     this.iw.println(" * " + new Date());
/* 532 */     this.iw.println(" * DO NOT HAND EDIT!");
/* 533 */     this.iw.println(" */");
/*     */   }
/*     */ 
/*     */   
/*     */   protected void generateClassJavaDocComment() throws IOException {
/* 538 */     this.iw.println("/**");
/* 539 */     this.iw.println(" * This class was generated by " + this.generatorName + ".");
/* 540 */     this.iw.println(" */");
/*     */   }
/*     */ 
/*     */   
/*     */   protected void writeImports() throws IOException {
/*     */     Iterator<String> iterator;
/* 546 */     for (iterator = this.generalImports.iterator(); iterator.hasNext();)
/* 547 */       this.iw.println("import " + iterator.next() + ".*;"); 
/* 548 */     for (iterator = this.specificImports.iterator(); iterator.hasNext();) {
/* 549 */       this.iw.println("import " + iterator.next() + ";");
/*     */     }
/*     */   }
/*     */   
/*     */   protected void writeClassDeclaration() throws IOException {
/* 554 */     this.iw.print(CodegenUtils.getModifierString(this.info.getModifiers()) + " class " + this.info.getClassName());
/* 555 */     String str = this.info.getSuperclassName();
/* 556 */     if (str != null)
/* 557 */       this.iw.print(" extends " + str); 
/* 558 */     if (this.interfaceNames.size() > 0) {
/*     */       
/* 560 */       this.iw.print(" implements ");
/* 561 */       boolean bool = true;
/* 562 */       for (Iterator<String> iterator = this.interfaceNames.iterator(); iterator.hasNext(); ) {
/*     */         
/* 564 */         if (bool) {
/* 565 */           bool = false;
/*     */         } else {
/* 567 */           this.iw.print(", ");
/*     */         } 
/* 569 */         this.iw.print(iterator.next());
/*     */       } 
/*     */     } 
/* 572 */     this.iw.println();
/*     */   }
/*     */   
/*     */   boolean boundProperties() {
/* 576 */     return BeangenUtils.hasBoundProperties(this.props);
/*     */   }
/*     */   boolean constrainedProperties() {
/* 579 */     return BeangenUtils.hasConstrainedProperties(this.props);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void main(String[] paramArrayOfString) {
/*     */     try {
/* 585 */       SimpleClassInfo simpleClassInfo = new SimpleClassInfo("test", 1, paramArrayOfString[0], null, null, new String[] { "java.awt" }, null);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 593 */       Property[] arrayOfProperty = { new SimpleProperty("number", "int", null, "7", false, true, false), new SimpleProperty("fpNumber", "float", null, null, true, true, false), new SimpleProperty("location", "Point", "new Point( location.x, location.y )", "new Point( 0, 0 )", false, true, true) };
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
/* 621 */       FileWriter fileWriter = new FileWriter(paramArrayOfString[0] + ".java");
/* 622 */       SimplePropertyBeanGenerator simplePropertyBeanGenerator = new SimplePropertyBeanGenerator();
/* 623 */       simplePropertyBeanGenerator.addExtension(new SerializableExtension());
/* 624 */       simplePropertyBeanGenerator.generate(simpleClassInfo, arrayOfProperty, fileWriter);
/* 625 */       fileWriter.flush();
/* 626 */       fileWriter.close();
/*     */     }
/* 628 */     catch (Exception exception) {
/* 629 */       exception.printStackTrace();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/codegen/bean/SimplePropertyBeanGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */