/*     */ package com.mchange.v2.c3p0.codegen;
/*     */ 
/*     */ import com.mchange.v1.xml.DomParseUtils;
/*     */ import com.mchange.v2.codegen.IndentedWriter;
/*     */ import com.mchange.v2.codegen.bean.BeanExtractingGeneratorExtension;
/*     */ import com.mchange.v2.codegen.bean.BeangenUtils;
/*     */ import com.mchange.v2.codegen.bean.ClassInfo;
/*     */ import com.mchange.v2.codegen.bean.CompleteConstructorGeneratorExtension;
/*     */ import com.mchange.v2.codegen.bean.GeneratorExtension;
/*     */ import com.mchange.v2.codegen.bean.IndirectingSerializableExtension;
/*     */ import com.mchange.v2.codegen.bean.ParsedPropertyBeanDocument;
/*     */ import com.mchange.v2.codegen.bean.Property;
/*     */ import com.mchange.v2.codegen.bean.PropertyReferenceableExtension;
/*     */ import com.mchange.v2.codegen.bean.PropsToStringGeneratorExtension;
/*     */ import com.mchange.v2.codegen.bean.SimpleClassInfo;
/*     */ import com.mchange.v2.codegen.bean.SimplePropertyBeanGenerator;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
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
/*     */ public class BeangenDataSourceGenerator
/*     */ {
/*     */   public static void main(String[] argv) {
/*     */     try {
/*  55 */       if (argv.length != 2) {
/*     */         
/*  57 */         System.err.println("java " + BeangenDataSourceGenerator.class.getName() + " <infile.xml> <OutputFile.java>");
/*     */ 
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/*  63 */       File outFile = new File(argv[1]);
/*  64 */       File parentDir = outFile.getParentFile();
/*  65 */       if (!parentDir.exists()) {
/*     */         
/*  67 */         System.err.println("Warning: making parent directory: " + parentDir);
/*  68 */         parentDir.mkdirs();
/*     */       } 
/*     */       
/*  71 */       DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
/*  72 */       DocumentBuilder db = fact.newDocumentBuilder();
/*  73 */       Document doc = db.parse(new File(argv[0]));
/*  74 */       ParsedPropertyBeanDocument parsed = new ParsedPropertyBeanDocument(doc);
/*  75 */       Writer w = new BufferedWriter(new FileWriter(outFile));
/*     */       
/*  77 */       SimplePropertyBeanGenerator gen = new SimplePropertyBeanGenerator();
/*  78 */       gen.setGeneratorName(BeangenDataSourceGenerator.class.getName());
/*     */ 
/*     */       
/*  81 */       IndirectingSerializableExtension idse = new IndirectingSerializableExtension("com.mchange.v2.naming.ReferenceIndirector")
/*     */         {
/*     */           
/*     */           protected void generateExtraSerInitializers(ClassInfo info, Class superclassType, Property[] props, Class[] propTypes, IndentedWriter iw) throws IOException
/*     */           {
/*  86 */             if (BeangenUtils.hasBoundProperties(props))
/*  87 */               iw.println("this.pcs = new PropertyChangeSupport( this );"); 
/*  88 */             if (BeangenUtils.hasConstrainedProperties(props))
/*  89 */               iw.println("this.vcs = new VetoableChangeSupport( this );"); 
/*     */           }
/*     */         };
/*  92 */       gen.addExtension((GeneratorExtension)idse);
/*     */       
/*  94 */       gen.addExtension(new C3P0ImplUtilsParentLoggerGeneratorExtension());
/*     */       
/*  96 */       PropsToStringGeneratorExtension tsge = new PropsToStringGeneratorExtension();
/*  97 */       tsge.setExcludePropertyNames(Arrays.asList(new String[] { "userOverridesAsString", "overrideDefaultUser", "overrideDefaultPassword" }));
/*  98 */       gen.addExtension((GeneratorExtension)tsge);
/*     */       
/* 100 */       PropertyReferenceableExtension prex = new PropertyReferenceableExtension();
/* 101 */       prex.setUseExplicitReferenceProperties(true);
/*     */ 
/*     */       
/* 104 */       prex.setFactoryClassName("com.mchange.v2.c3p0.impl.C3P0JavaBeanObjectFactory");
/* 105 */       gen.addExtension((GeneratorExtension)prex);
/*     */       
/* 107 */       BooleanInitIdentityTokenConstructortorGeneratorExtension biitcge = new BooleanInitIdentityTokenConstructortorGeneratorExtension();
/* 108 */       gen.addExtension(biitcge);
/*     */       
/* 110 */       if (parsed.getClassInfo().getClassName().equals("WrapperConnectionPoolDataSourceBase")) {
/* 111 */         gen.addExtension(new WcpdsExtrasGeneratorExtension());
/*     */       }
/* 113 */       if (unmodifiableShadow(doc)) {
/* 114 */         gen.addExtension(new UnmodifiableShadowGeneratorExtension());
/*     */       }
/*     */       
/* 117 */       gen.generate(parsed.getClassInfo(), parsed.getProperties(), w);
/*     */       
/* 119 */       w.flush();
/* 120 */       w.close();
/*     */       
/* 122 */       System.err.println("Processed: " + argv[0]);
/*     */     }
/* 124 */     catch (Exception e) {
/* 125 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   private static boolean unmodifiableShadow(Document doc) {
/* 130 */     Element docElem = doc.getDocumentElement();
/* 131 */     return (DomParseUtils.uniqueChild(docElem, "unmodifiable-shadow") != null);
/*     */   }
/*     */   
/*     */   static class BooleanInitIdentityTokenConstructortorGeneratorExtension implements GeneratorExtension {
/*     */     public Collection extraGeneralImports() {
/* 136 */       return Collections.EMPTY_SET;
/*     */     }
/*     */     
/*     */     public Collection extraSpecificImports() {
/* 140 */       Set<String> out = new HashSet();
/* 141 */       out.add("com.mchange.v2.c3p0.C3P0Registry");
/* 142 */       return out;
/*     */     }
/*     */     public Collection extraInterfaceNames() {
/* 145 */       return Collections.EMPTY_SET;
/*     */     }
/*     */ 
/*     */     
/*     */     public void generate(ClassInfo info, Class superclassType, Property[] props, Class[] propTypes, IndentedWriter iw) throws IOException {
/* 150 */       BeangenUtils.writeExplicitDefaultConstructor(2, info, iw);
/* 151 */       iw.println();
/* 152 */       iw.println("public " + info.getClassName() + "( boolean autoregister )");
/* 153 */       iw.println("{");
/* 154 */       iw.upIndent();
/* 155 */       iw.println("if (autoregister)");
/* 156 */       iw.println("{");
/* 157 */       iw.upIndent();
/* 158 */       iw.println("this.identityToken = C3P0ImplUtils.allocateIdentityToken( this );");
/* 159 */       iw.println("C3P0Registry.reregister( this );");
/* 160 */       iw.downIndent();
/* 161 */       iw.println("}");
/*     */       
/* 163 */       iw.downIndent();
/* 164 */       iw.println("}");
/*     */     }
/*     */   }
/*     */   
/*     */   static class WcpdsExtrasGeneratorExtension implements GeneratorExtension {
/*     */     public Collection extraGeneralImports() {
/* 170 */       return Collections.EMPTY_SET;
/*     */     }
/*     */     
/*     */     public Collection extraSpecificImports() {
/* 174 */       Set<String> out = new HashSet();
/* 175 */       out.add("com.mchange.v2.c3p0.ConnectionCustomizer");
/* 176 */       out.add("javax.sql.PooledConnection");
/* 177 */       out.add("java.sql.SQLException");
/* 178 */       return out;
/*     */     }
/*     */     public Collection extraInterfaceNames() {
/* 181 */       return Collections.EMPTY_SET;
/*     */     }
/*     */ 
/*     */     
/*     */     public void generate(ClassInfo info, Class superclassType, Property[] props, Class[] propTypes, IndentedWriter iw) throws IOException {
/* 186 */       iw.println("protected abstract PooledConnection getPooledConnection( ConnectionCustomizer cc, String idt) throws SQLException;");
/*     */       
/* 188 */       iw.println("protected abstract PooledConnection getPooledConnection(String user, String password, ConnectionCustomizer cc, String idt) throws SQLException;");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static class UnmodifiableShadowGeneratorExtension
/*     */     implements GeneratorExtension
/*     */   {
/*     */     BeanExtractingGeneratorExtension bege;
/*     */     CompleteConstructorGeneratorExtension ccge;
/*     */     
/*     */     UnmodifiableShadowGeneratorExtension() {
/* 200 */       this.bege = new BeanExtractingGeneratorExtension();
/* 201 */       this.bege.setExtractMethodModifiers(2);
/* 202 */       this.bege.setConstructorModifiers(1);
/*     */       
/* 204 */       this.ccge = new CompleteConstructorGeneratorExtension();
/*     */     }
/*     */ 
/*     */     
/*     */     public Collection extraGeneralImports() {
/* 209 */       Set out = new HashSet();
/* 210 */       out.addAll(this.bege.extraGeneralImports());
/* 211 */       out.addAll(this.ccge.extraGeneralImports());
/* 212 */       return out;
/*     */     }
/*     */ 
/*     */     
/*     */     public Collection extraSpecificImports() {
/* 217 */       Set out = new HashSet();
/* 218 */       out.addAll(this.bege.extraSpecificImports());
/* 219 */       out.addAll(this.ccge.extraSpecificImports());
/* 220 */       return out;
/*     */     }
/*     */     public Collection extraInterfaceNames() {
/* 223 */       return Collections.EMPTY_SET;
/*     */     }
/*     */ 
/*     */     
/*     */     public void generate(ClassInfo info, Class superclassType, Property[] props, Class[] propTypes, IndentedWriter iw) throws IOException {
/* 228 */       SimpleClassInfo simpleClassInfo = new SimpleClassInfo(info.getPackageName(), 9, "UnmodifiableShadow", info.getSuperclassName(), info.getInterfaceNames(), info.getGeneralImports(), info.getSpecificImports());
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 236 */       SimplePropertyBeanGenerator innerGen = new SimplePropertyBeanGenerator();
/* 237 */       innerGen.setInner(true);
/* 238 */       innerGen.setForceUnmodifiable(true);
/* 239 */       innerGen.addExtension((GeneratorExtension)this.bege);
/* 240 */       innerGen.addExtension((GeneratorExtension)this.ccge);
/* 241 */       innerGen.generate((ClassInfo)simpleClassInfo, props, (Writer)iw);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/codegen/BeangenDataSourceGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */