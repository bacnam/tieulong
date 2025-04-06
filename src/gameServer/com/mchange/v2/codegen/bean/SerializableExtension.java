/*     */ package com.mchange.v2.codegen.bean;
/*     */ 
/*     */ import com.mchange.v2.codegen.IndentedWriter;
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ public class SerializableExtension
/*     */   implements GeneratorExtension
/*     */ {
/*     */   Set transientProperties;
/*     */   Map transientPropertyInitializers;
/*     */   
/*     */   public SerializableExtension(Set paramSet, Map paramMap) {
/*  61 */     this.transientProperties = paramSet;
/*  62 */     this.transientPropertyInitializers = paramMap;
/*     */   }
/*     */   
/*     */   public SerializableExtension() {
/*  66 */     this(Collections.EMPTY_SET, null);
/*     */   }
/*     */   
/*     */   public Collection extraGeneralImports() {
/*  70 */     return Collections.EMPTY_SET;
/*     */   }
/*     */   
/*     */   public Collection extraSpecificImports() {
/*  74 */     HashSet<String> hashSet = new HashSet();
/*  75 */     hashSet.add("java.io.IOException");
/*  76 */     hashSet.add("java.io.Serializable");
/*  77 */     hashSet.add("java.io.ObjectOutputStream");
/*  78 */     hashSet.add("java.io.ObjectInputStream");
/*  79 */     return hashSet;
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection extraInterfaceNames() {
/*  84 */     HashSet<String> hashSet = new HashSet();
/*  85 */     hashSet.add("Serializable");
/*  86 */     return hashSet;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void generate(ClassInfo paramClassInfo, Class paramClass, Property[] paramArrayOfProperty, Class[] paramArrayOfClass, IndentedWriter paramIndentedWriter) throws IOException {
/*  92 */     paramIndentedWriter.println("private static final long serialVersionUID = 1;");
/*  93 */     paramIndentedWriter.println("private static final short VERSION = 0x0001;");
/*  94 */     paramIndentedWriter.println();
/*  95 */     paramIndentedWriter.println("private void writeObject( ObjectOutputStream oos ) throws IOException");
/*  96 */     paramIndentedWriter.println("{");
/*  97 */     paramIndentedWriter.upIndent();
/*     */     
/*  99 */     paramIndentedWriter.println("oos.writeShort( VERSION );"); byte b;
/*     */     int i;
/* 101 */     for (b = 0, i = paramArrayOfProperty.length; b < i; b++) {
/*     */       
/* 103 */       Property property = paramArrayOfProperty[b];
/* 104 */       if (!this.transientProperties.contains(property.getName())) {
/*     */         
/* 106 */         Class<byte> clazz = paramArrayOfClass[b];
/* 107 */         if (clazz != null && clazz.isPrimitive()) {
/*     */           
/* 109 */           if (clazz == byte.class) {
/* 110 */             paramIndentedWriter.println("oos.writeByte(" + property.getName() + ");");
/* 111 */           } else if (clazz == char.class) {
/* 112 */             paramIndentedWriter.println("oos.writeChar(" + property.getName() + ");");
/* 113 */           } else if (clazz == short.class) {
/* 114 */             paramIndentedWriter.println("oos.writeShort(" + property.getName() + ");");
/* 115 */           } else if (clazz == int.class) {
/* 116 */             paramIndentedWriter.println("oos.writeInt(" + property.getName() + ");");
/* 117 */           } else if (clazz == boolean.class) {
/* 118 */             paramIndentedWriter.println("oos.writeBoolean(" + property.getName() + ");");
/* 119 */           } else if (clazz == long.class) {
/* 120 */             paramIndentedWriter.println("oos.writeLong(" + property.getName() + ");");
/* 121 */           } else if (clazz == float.class) {
/* 122 */             paramIndentedWriter.println("oos.writeFloat(" + property.getName() + ");");
/* 123 */           } else if (clazz == double.class) {
/* 124 */             paramIndentedWriter.println("oos.writeDouble(" + property.getName() + ");");
/*     */           } 
/*     */         } else {
/* 127 */           writeStoreObject(property, clazz, paramIndentedWriter);
/*     */         } 
/*     */       } 
/* 130 */     }  generateExtraSerWriteStatements(paramClassInfo, paramClass, paramArrayOfProperty, paramArrayOfClass, paramIndentedWriter);
/* 131 */     paramIndentedWriter.downIndent();
/* 132 */     paramIndentedWriter.println("}");
/* 133 */     paramIndentedWriter.println();
/*     */     
/* 135 */     paramIndentedWriter.println("private void readObject( ObjectInputStream ois ) throws IOException, ClassNotFoundException");
/* 136 */     paramIndentedWriter.println("{");
/* 137 */     paramIndentedWriter.upIndent();
/* 138 */     paramIndentedWriter.println("short version = ois.readShort();");
/* 139 */     paramIndentedWriter.println("switch (version)");
/* 140 */     paramIndentedWriter.println("{");
/* 141 */     paramIndentedWriter.upIndent();
/*     */     
/* 143 */     paramIndentedWriter.println("case VERSION:");
/* 144 */     paramIndentedWriter.upIndent();
/* 145 */     for (b = 0, i = paramArrayOfProperty.length; b < i; b++) {
/*     */       
/* 147 */       Property property = paramArrayOfProperty[b];
/* 148 */       if (!this.transientProperties.contains(property.getName())) {
/*     */         
/* 150 */         Class<byte> clazz = paramArrayOfClass[b];
/* 151 */         if (clazz != null && clazz.isPrimitive()) {
/*     */           
/* 153 */           if (clazz == byte.class) {
/* 154 */             paramIndentedWriter.println("this." + property.getName() + " = ois.readByte();");
/* 155 */           } else if (clazz == char.class) {
/* 156 */             paramIndentedWriter.println("this." + property.getName() + " = ois.readChar();");
/* 157 */           } else if (clazz == short.class) {
/* 158 */             paramIndentedWriter.println("this." + property.getName() + " = ois.readShort();");
/* 159 */           } else if (clazz == int.class) {
/* 160 */             paramIndentedWriter.println("this." + property.getName() + " = ois.readInt();");
/* 161 */           } else if (clazz == boolean.class) {
/* 162 */             paramIndentedWriter.println("this." + property.getName() + " = ois.readBoolean();");
/* 163 */           } else if (clazz == long.class) {
/* 164 */             paramIndentedWriter.println("this." + property.getName() + " = ois.readLong();");
/* 165 */           } else if (clazz == float.class) {
/* 166 */             paramIndentedWriter.println("this." + property.getName() + " = ois.readFloat();");
/* 167 */           } else if (clazz == double.class) {
/* 168 */             paramIndentedWriter.println("this." + property.getName() + " = ois.readDouble();");
/*     */           } 
/*     */         } else {
/* 171 */           writeUnstoreObject(property, clazz, paramIndentedWriter);
/*     */         } 
/*     */       } else {
/*     */         
/* 175 */         String str = (String)this.transientPropertyInitializers.get(property.getName());
/* 176 */         if (str != null)
/* 177 */           paramIndentedWriter.println("this." + property.getName() + " = " + str + ';'); 
/*     */       } 
/*     */     } 
/* 180 */     generateExtraSerInitializers(paramClassInfo, paramClass, paramArrayOfProperty, paramArrayOfClass, paramIndentedWriter);
/* 181 */     paramIndentedWriter.println("break;");
/* 182 */     paramIndentedWriter.downIndent();
/* 183 */     paramIndentedWriter.println("default:");
/* 184 */     paramIndentedWriter.upIndent();
/* 185 */     paramIndentedWriter.println("throw new IOException(\"Unsupported Serialized Version: \" + version);");
/* 186 */     paramIndentedWriter.downIndent();
/*     */     
/* 188 */     paramIndentedWriter.downIndent();
/* 189 */     paramIndentedWriter.println("}");
/*     */     
/* 191 */     paramIndentedWriter.downIndent();
/* 192 */     paramIndentedWriter.println("}");
/*     */   }
/*     */ 
/*     */   
/*     */   protected void writeStoreObject(Property paramProperty, Class paramClass, IndentedWriter paramIndentedWriter) throws IOException {
/* 197 */     paramIndentedWriter.println("oos.writeObject( " + paramProperty.getName() + " );");
/*     */   }
/*     */ 
/*     */   
/*     */   protected void writeUnstoreObject(Property paramProperty, Class paramClass, IndentedWriter paramIndentedWriter) throws IOException {
/* 202 */     paramIndentedWriter.println("this." + paramProperty.getName() + " = (" + paramProperty.getSimpleTypeName() + ") ois.readObject();");
/*     */   }
/*     */   
/*     */   protected void generateExtraSerWriteStatements(ClassInfo paramClassInfo, Class paramClass, Property[] paramArrayOfProperty, Class[] paramArrayOfClass, IndentedWriter paramIndentedWriter) throws IOException {}
/*     */   
/*     */   protected void generateExtraSerInitializers(ClassInfo paramClassInfo, Class paramClass, Property[] paramArrayOfProperty, Class[] paramArrayOfClass, IndentedWriter paramIndentedWriter) throws IOException {}
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/codegen/bean/SerializableExtension.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */