package com.mchange.v2.c3p0.codegen;

import com.mchange.v1.xml.DomParseUtils;
import com.mchange.v2.codegen.IndentedWriter;
import com.mchange.v2.codegen.bean.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.*;

public class BeangenDataSourceGenerator {
    public static void main(String[] argv) {
        try {
            if (argv.length != 2) {

                System.err.println("java " + BeangenDataSourceGenerator.class.getName() + " <infile.xml> <OutputFile.java>");

                return;
            }

            File outFile = new File(argv[1]);
            File parentDir = outFile.getParentFile();
            if (!parentDir.exists()) {

                System.err.println("Warning: making parent directory: " + parentDir);
                parentDir.mkdirs();
            }

            DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = fact.newDocumentBuilder();
            Document doc = db.parse(new File(argv[0]));
            ParsedPropertyBeanDocument parsed = new ParsedPropertyBeanDocument(doc);
            Writer w = new BufferedWriter(new FileWriter(outFile));

            SimplePropertyBeanGenerator gen = new SimplePropertyBeanGenerator();
            gen.setGeneratorName(BeangenDataSourceGenerator.class.getName());

            IndirectingSerializableExtension idse = new IndirectingSerializableExtension("com.mchange.v2.naming.ReferenceIndirector") {

                protected void generateExtraSerInitializers(ClassInfo info, Class superclassType, Property[] props, Class[] propTypes, IndentedWriter iw) throws IOException {
                    if (BeangenUtils.hasBoundProperties(props))
                        iw.println("this.pcs = new PropertyChangeSupport( this );");
                    if (BeangenUtils.hasConstrainedProperties(props))
                        iw.println("this.vcs = new VetoableChangeSupport( this );");
                }
            };
            gen.addExtension((GeneratorExtension) idse);

            gen.addExtension(new C3P0ImplUtilsParentLoggerGeneratorExtension());

            PropsToStringGeneratorExtension tsge = new PropsToStringGeneratorExtension();
            tsge.setExcludePropertyNames(Arrays.asList(new String[]{"userOverridesAsString", "overrideDefaultUser", "overrideDefaultPassword"}));
            gen.addExtension((GeneratorExtension) tsge);

            PropertyReferenceableExtension prex = new PropertyReferenceableExtension();
            prex.setUseExplicitReferenceProperties(true);

            prex.setFactoryClassName("com.mchange.v2.c3p0.impl.C3P0JavaBeanObjectFactory");
            gen.addExtension((GeneratorExtension) prex);

            BooleanInitIdentityTokenConstructortorGeneratorExtension biitcge = new BooleanInitIdentityTokenConstructortorGeneratorExtension();
            gen.addExtension(biitcge);

            if (parsed.getClassInfo().getClassName().equals("WrapperConnectionPoolDataSourceBase")) {
                gen.addExtension(new WcpdsExtrasGeneratorExtension());
            }
            if (unmodifiableShadow(doc)) {
                gen.addExtension(new UnmodifiableShadowGeneratorExtension());
            }

            gen.generate(parsed.getClassInfo(), parsed.getProperties(), w);

            w.flush();
            w.close();

            System.err.println("Processed: " + argv[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean unmodifiableShadow(Document doc) {
        Element docElem = doc.getDocumentElement();
        return (DomParseUtils.uniqueChild(docElem, "unmodifiable-shadow") != null);
    }

    static class BooleanInitIdentityTokenConstructortorGeneratorExtension implements GeneratorExtension {
        public Collection extraGeneralImports() {
            return Collections.EMPTY_SET;
        }

        public Collection extraSpecificImports() {
            Set<String> out = new HashSet();
            out.add("com.mchange.v2.c3p0.C3P0Registry");
            return out;
        }

        public Collection extraInterfaceNames() {
            return Collections.EMPTY_SET;
        }

        public void generate(ClassInfo info, Class superclassType, Property[] props, Class[] propTypes, IndentedWriter iw) throws IOException {
            BeangenUtils.writeExplicitDefaultConstructor(2, info, iw);
            iw.println();
            iw.println("public " + info.getClassName() + "( boolean autoregister )");
            iw.println("{");
            iw.upIndent();
            iw.println("if (autoregister)");
            iw.println("{");
            iw.upIndent();
            iw.println("this.identityToken = C3P0ImplUtils.allocateIdentityToken( this );");
            iw.println("C3P0Registry.reregister( this );");
            iw.downIndent();
            iw.println("}");

            iw.downIndent();
            iw.println("}");
        }
    }

    static class WcpdsExtrasGeneratorExtension implements GeneratorExtension {
        public Collection extraGeneralImports() {
            return Collections.EMPTY_SET;
        }

        public Collection extraSpecificImports() {
            Set<String> out = new HashSet();
            out.add("com.mchange.v2.c3p0.ConnectionCustomizer");
            out.add("javax.sql.PooledConnection");
            out.add("java.sql.SQLException");
            return out;
        }

        public Collection extraInterfaceNames() {
            return Collections.EMPTY_SET;
        }

        public void generate(ClassInfo info, Class superclassType, Property[] props, Class[] propTypes, IndentedWriter iw) throws IOException {
            iw.println("protected abstract PooledConnection getPooledConnection( ConnectionCustomizer cc, String idt) throws SQLException;");

            iw.println("protected abstract PooledConnection getPooledConnection(String user, String password, ConnectionCustomizer cc, String idt) throws SQLException;");
        }
    }

    static class UnmodifiableShadowGeneratorExtension
            implements GeneratorExtension {
        BeanExtractingGeneratorExtension bege;
        CompleteConstructorGeneratorExtension ccge;

        UnmodifiableShadowGeneratorExtension() {
            this.bege = new BeanExtractingGeneratorExtension();
            this.bege.setExtractMethodModifiers(2);
            this.bege.setConstructorModifiers(1);

            this.ccge = new CompleteConstructorGeneratorExtension();
        }

        public Collection extraGeneralImports() {
            Set out = new HashSet();
            out.addAll(this.bege.extraGeneralImports());
            out.addAll(this.ccge.extraGeneralImports());
            return out;
        }

        public Collection extraSpecificImports() {
            Set out = new HashSet();
            out.addAll(this.bege.extraSpecificImports());
            out.addAll(this.ccge.extraSpecificImports());
            return out;
        }

        public Collection extraInterfaceNames() {
            return Collections.EMPTY_SET;
        }

        public void generate(ClassInfo info, Class superclassType, Property[] props, Class[] propTypes, IndentedWriter iw) throws IOException {
            SimpleClassInfo simpleClassInfo = new SimpleClassInfo(info.getPackageName(), 9, "UnmodifiableShadow", info.getSuperclassName(), info.getInterfaceNames(), info.getGeneralImports(), info.getSpecificImports());

            SimplePropertyBeanGenerator innerGen = new SimplePropertyBeanGenerator();
            innerGen.setInner(true);
            innerGen.setForceUnmodifiable(true);
            innerGen.addExtension((GeneratorExtension) this.bege);
            innerGen.addExtension((GeneratorExtension) this.ccge);
            innerGen.generate((ClassInfo) simpleClassInfo, props, (Writer) iw);
        }
    }
}

