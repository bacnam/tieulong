package com.mchange.v2.codegen.bean;

import com.mchange.v1.lang.ClassUtils;
import com.mchange.v2.codegen.CodegenUtils;
import com.mchange.v2.codegen.IndentedWriter;
import com.mchange.v2.log.MLevel;
import com.mchange.v2.log.MLog;
import com.mchange.v2.log.MLogger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

public class SimplePropertyBeanGenerator
        implements PropertyBeanGenerator {
    private static final MLogger logger = MLog.getLogger(SimplePropertyBeanGenerator.class);
    protected ClassInfo info;
    protected Property[] props;
    protected IndentedWriter iw;
    protected Set generalImports;
    protected Set specificImports;
    protected Set interfaceNames;
    protected Class superclassType;
    protected List interfaceTypes;
    protected Class[] propertyTypes;
    protected List generatorExtensions = new ArrayList();
    private boolean inner = false;
    private int java_version = 140;
    private boolean force_unmodifiable = false;
    private String generatorName = getClass().getName();

    public static void main(String[] paramArrayOfString) {
        try {
            SimpleClassInfo simpleClassInfo = new SimpleClassInfo("test", 1, paramArrayOfString[0], null, null, new String[]{"java.awt"}, null);

            Property[] arrayOfProperty = {new SimpleProperty("number", "int", null, "7", false, true, false), new SimpleProperty("fpNumber", "float", null, null, true, true, false), new SimpleProperty("location", "Point", "new Point( location.x, location.y )", "new Point( 0, 0 )", false, true, true)};

            FileWriter fileWriter = new FileWriter(paramArrayOfString[0] + ".java");
            SimplePropertyBeanGenerator simplePropertyBeanGenerator = new SimplePropertyBeanGenerator();
            simplePropertyBeanGenerator.addExtension(new SerializableExtension());
            simplePropertyBeanGenerator.generate(simpleClassInfo, arrayOfProperty, fileWriter);
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public synchronized boolean isInner() {
        return this.inner;
    }

    public synchronized void setInner(boolean paramBoolean) {
        this.inner = paramBoolean;
    }

    public synchronized int getJavaVersion() {
        return this.java_version;
    }

    public synchronized void setJavaVersion(int paramInt) {
        this.java_version = paramInt;
    }

    public synchronized String getGeneratorName() {
        return this.generatorName;
    }

    public synchronized void setGeneratorName(String paramString) {
        this.generatorName = paramString;
    }

    public synchronized boolean isForceUnmodifiable() {
        return this.force_unmodifiable;
    }

    public synchronized void setForceUnmodifiable(boolean paramBoolean) {
        this.force_unmodifiable = paramBoolean;
    }

    public synchronized void addExtension(GeneratorExtension paramGeneratorExtension) {
        this.generatorExtensions.add(paramGeneratorExtension);
    }

    public synchronized void removeExtension(GeneratorExtension paramGeneratorExtension) {
        this.generatorExtensions.remove(paramGeneratorExtension);
    }

    public synchronized void generate(ClassInfo paramClassInfo, Property[] paramArrayOfProperty, Writer paramWriter) throws IOException {
        this.info = paramClassInfo;
        this.props = paramArrayOfProperty;
        Arrays.sort(paramArrayOfProperty, BeangenUtils.PROPERTY_COMPARATOR);
        this.iw = (paramWriter instanceof IndentedWriter) ? (IndentedWriter) paramWriter : new IndentedWriter(paramWriter);

        this.generalImports = new TreeSet();
        if (paramClassInfo.getGeneralImports() != null) {
            this.generalImports.addAll(Arrays.asList(paramClassInfo.getGeneralImports()));
        }
        this.specificImports = new TreeSet();
        if (paramClassInfo.getSpecificImports() != null) {
            this.specificImports.addAll(Arrays.asList(paramClassInfo.getSpecificImports()));
        }
        this.interfaceNames = new TreeSet();
        if (paramClassInfo.getInterfaceNames() != null) {
            this.interfaceNames.addAll(Arrays.asList(paramClassInfo.getInterfaceNames()));
        }
        addInternalImports();
        addInternalInterfaces();

        resolveTypes();

        if (!this.inner) {

            writeHeader();
            this.iw.println();
        }
        generateClassJavaDocComment();
        writeClassDeclaration();
        this.iw.println('{');
        this.iw.upIndent();

        writeCoreBody();

        this.iw.downIndent();
        this.iw.println('}');
    }

    protected void resolveTypes() {
        String[] arrayOfString1 = (String[]) this.generalImports.toArray((Object[]) new String[this.generalImports.size()]);
        String[] arrayOfString2 = (String[]) this.specificImports.toArray((Object[]) new String[this.specificImports.size()]);

        if (this.info.getSuperclassName() != null) {

            try {
                this.superclassType = ClassUtils.forName(this.info.getSuperclassName(), arrayOfString1, arrayOfString2);
            } catch (Exception exception) {

                if (logger.isLoggable(MLevel.WARNING)) {
                    logger.warning(getClass().getName() + " could not resolve superclass '" + this.info.getSuperclassName() + "'.");
                }
                this.superclassType = null;
            }
        }

        this.interfaceTypes = new ArrayList(this.interfaceNames.size());
        for (String str : this.interfaceNames) {

            try {
                this.interfaceTypes.add(ClassUtils.forName(str, arrayOfString1, arrayOfString2));
            } catch (Exception exception) {

                if (logger.isLoggable(MLevel.WARNING)) {
                    logger.warning(getClass().getName() + " could not resolve interface '" + str + "'.");
                }
                this.interfaceTypes.add(null);
            }
        }

        this.propertyTypes = new Class[this.props.length];
        byte b;
        int i;
        for (b = 0, i = this.props.length; b < i; b++) {

            String str = this.props[b].getSimpleTypeName();
            try {
                this.propertyTypes[b] = ClassUtils.forName(str, arrayOfString1, arrayOfString2);
            } catch (Exception exception) {

                if (logger.isLoggable(MLevel.WARNING)) {
                    logger.log(MLevel.WARNING, getClass().getName() + " could not resolve property type '" + str + "'.", exception);
                }
                this.propertyTypes[b] = null;
            }
        }
    }

    protected void addInternalImports() {
        if (boundProperties()) {

            this.specificImports.add("java.beans.PropertyChangeEvent");
            this.specificImports.add("java.beans.PropertyChangeSupport");
            this.specificImports.add("java.beans.PropertyChangeListener");
        }
        if (constrainedProperties()) {

            this.specificImports.add("java.beans.PropertyChangeEvent");
            this.specificImports.add("java.beans.PropertyVetoException");
            this.specificImports.add("java.beans.VetoableChangeSupport");
            this.specificImports.add("java.beans.VetoableChangeListener");
        }

        for (GeneratorExtension generatorExtension : this.generatorExtensions) {

            this.specificImports.addAll(generatorExtension.extraSpecificImports());
            this.generalImports.addAll(generatorExtension.extraGeneralImports());
        }
    }

    protected void addInternalInterfaces() {
        for (GeneratorExtension generatorExtension : this.generatorExtensions) {

            this.interfaceNames.addAll(generatorExtension.extraInterfaceNames());
        }
    }

    protected void writeCoreBody() throws IOException {
        writeJavaBeansChangeSupport();
        writePropertyVariables();
        writeOtherVariables();
        this.iw.println();

        writeGetterSetterPairs();
        if (boundProperties()) {

            this.iw.println();
            writeBoundPropertyEventSourceMethods();
        }
        if (constrainedProperties()) {

            this.iw.println();
            writeConstrainedPropertyEventSourceMethods();
        }
        writeInternalUtilityFunctions();
        writeOtherFunctions();

        writeOtherClasses();

        String[] arrayOfString1 = (String[]) this.interfaceNames.toArray((Object[]) new String[this.interfaceNames.size()]);
        String[] arrayOfString2 = (String[]) this.generalImports.toArray((Object[]) new String[this.generalImports.size()]);
        String[] arrayOfString3 = (String[]) this.specificImports.toArray((Object[]) new String[this.specificImports.size()]);
        SimpleClassInfo simpleClassInfo = new SimpleClassInfo(this.info.getPackageName(), this.info.getModifiers(), this.info.getClassName(), this.info.getSuperclassName(), arrayOfString1, arrayOfString2, arrayOfString3);

        for (GeneratorExtension generatorExtension : this.generatorExtensions) {

            this.iw.println();
            generatorExtension.generate(simpleClassInfo, this.superclassType, this.props, this.propertyTypes, this.iw);
        }
    }

    protected void writeInternalUtilityFunctions() throws IOException {
        this.iw.println("private boolean eqOrBothNull( Object a, Object b )");
        this.iw.println("{");
        this.iw.upIndent();

        this.iw.println("return");
        this.iw.upIndent();
        this.iw.println("a == b ||");
        this.iw.println("(a != null && a.equals(b));");
        this.iw.downIndent();

        this.iw.downIndent();
        this.iw.println("}");
    }

    protected void writeConstrainedPropertyEventSourceMethods() throws IOException {
        this.iw.println("public void addVetoableChangeListener( VetoableChangeListener vcl )");
        this.iw.println("{ vcs.addVetoableChangeListener( vcl ); }");
        this.iw.println();

        this.iw.println("public void removeVetoableChangeListener( VetoableChangeListener vcl )");
        this.iw.println("{ vcs.removeVetoableChangeListener( vcl ); }");
        this.iw.println();

        if (this.java_version >= 140) {

            this.iw.println("public VetoableChangeListener[] getVetoableChangeListeners()");
            this.iw.println("{ return vcs.getVetoableChangeListeners(); }");
        }
    }

    protected void writeBoundPropertyEventSourceMethods() throws IOException {
        this.iw.println("public void addPropertyChangeListener( PropertyChangeListener pcl )");
        this.iw.println("{ pcs.addPropertyChangeListener( pcl ); }");
        this.iw.println();

        this.iw.println("public void addPropertyChangeListener( String propName, PropertyChangeListener pcl )");
        this.iw.println("{ pcs.addPropertyChangeListener( propName, pcl ); }");
        this.iw.println();

        this.iw.println("public void removePropertyChangeListener( PropertyChangeListener pcl )");
        this.iw.println("{ pcs.removePropertyChangeListener( pcl ); }");
        this.iw.println();

        this.iw.println("public void removePropertyChangeListener( String propName, PropertyChangeListener pcl )");
        this.iw.println("{ pcs.removePropertyChangeListener( propName, pcl ); }");
        this.iw.println();

        if (this.java_version >= 140) {

            this.iw.println("public PropertyChangeListener[] getPropertyChangeListeners()");
            this.iw.println("{ return pcs.getPropertyChangeListeners(); }");
        }
    }

    protected void writeJavaBeansChangeSupport() throws IOException {
        if (boundProperties()) {

            this.iw.println("protected PropertyChangeSupport pcs = new PropertyChangeSupport( this );");
            this.iw.println();
            this.iw.println("protected PropertyChangeSupport getPropertyChangeSupport()");
            this.iw.println("{ return pcs; }");
        }

        if (constrainedProperties()) {

            this.iw.println("protected VetoableChangeSupport vcs = new VetoableChangeSupport( this );");
            this.iw.println();
            this.iw.println("protected VetoableChangeSupport getVetoableChangeSupport()");
            this.iw.println("{ return vcs; }");
        }
    }

    protected void writeOtherVariables() throws IOException {
    }

    protected void writeOtherFunctions() throws IOException {
    }

    protected void writeOtherClasses() throws IOException {
    }

    protected void writePropertyVariables() throws IOException {
        byte b;
        int i;
        for (b = 0, i = this.props.length; b < i; b++) {
            writePropertyVariable(this.props[b]);
        }
    }

    protected void writePropertyVariable(Property paramProperty) throws IOException {
        BeangenUtils.writePropertyVariable(paramProperty, this.iw);
    }

    protected void writePropertyMembers() throws IOException {
        throw new InternalError("writePropertyMembers() deprecated and removed. please us writePropertyVariables().");
    }

    protected void writePropertyMember(Property paramProperty) throws IOException {
        throw new InternalError("writePropertyMember() deprecated and removed. please us writePropertyVariable().");
    }

    protected void writeGetterSetterPairs() throws IOException {
        byte b;
        int i;
        for (b = 0, i = this.props.length; b < i; b++) {

            writeGetterSetterPair(this.props[b], this.propertyTypes[b]);
            if (b != i - 1) this.iw.println();

        }
    }

    protected void writeGetterSetterPair(Property paramProperty, Class paramClass) throws IOException {
        writePropertyGetter(paramProperty, paramClass);

        if (!paramProperty.isReadOnly() && !this.force_unmodifiable) {

            this.iw.println();
            writePropertySetter(paramProperty, paramClass);
        }
    }

    protected void writePropertyGetter(Property paramProperty, Class paramClass) throws IOException {
        BeangenUtils.writePropertyGetter(paramProperty, getGetterDefensiveCopyExpression(paramProperty, paramClass), this.iw);
    }

    protected void writePropertySetter(Property paramProperty, Class paramClass) throws IOException {
        BeangenUtils.writePropertySetter(paramProperty, getSetterDefensiveCopyExpression(paramProperty, paramClass), this.iw);
    }

    protected String getGetterDefensiveCopyExpression(Property paramProperty, Class paramClass) {
        return paramProperty.getDefensiveCopyExpression();
    }

    protected String getSetterDefensiveCopyExpression(Property paramProperty, Class paramClass) {
        return paramProperty.getDefensiveCopyExpression();
    }

    protected String getConstructorDefensiveCopyExpression(Property paramProperty, Class paramClass) {
        return paramProperty.getDefensiveCopyExpression();
    }

    protected void writeHeader() throws IOException {
        writeBannerComments();
        this.iw.println();
        this.iw.println("package " + this.info.getPackageName() + ';');
        this.iw.println();
        writeImports();
    }

    protected void writeBannerComments() throws IOException {
        this.iw.println(" * " + new Date());
        this.iw.println(" * DO NOT HAND EDIT!");
        this.iw.println(" */");
    }

    protected void generateClassJavaDocComment() throws IOException {
        this.iw.println(" */");
    }

    protected void writeImports() throws IOException {
        Iterator<String> iterator;
        for (iterator = this.generalImports.iterator(); iterator.hasNext(); )
            this.iw.println("import " + iterator.next() + ".*;");
        for (iterator = this.specificImports.iterator(); iterator.hasNext(); ) {
            this.iw.println("import " + iterator.next() + ";");
        }
    }

    protected void writeClassDeclaration() throws IOException {
        this.iw.print(CodegenUtils.getModifierString(this.info.getModifiers()) + " class " + this.info.getClassName());
        String str = this.info.getSuperclassName();
        if (str != null)
            this.iw.print(" extends " + str);
        if (this.interfaceNames.size() > 0) {

            this.iw.print(" implements ");
            boolean bool = true;
            for (Iterator<String> iterator = this.interfaceNames.iterator(); iterator.hasNext(); ) {

                if (bool) {
                    bool = false;
                } else {
                    this.iw.print(", ");
                }
                this.iw.print(iterator.next());
            }
        }
        this.iw.println();
    }

    boolean boundProperties() {
        return BeangenUtils.hasBoundProperties(this.props);
    }

    boolean constrainedProperties() {
        return BeangenUtils.hasConstrainedProperties(this.props);
    }
}

