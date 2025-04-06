/*     */ package com.mchange.v2.codegen.bean;
/*     */ 
/*     */ import com.mchange.v2.codegen.CodegenUtils;
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import java.lang.reflect.Modifier;
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
/*     */ public class InnerBeanPropertyBeanGenerator
/*     */   extends SimplePropertyBeanGenerator
/*     */ {
/*     */   String innerBeanClassName;
/*  46 */   int inner_bean_member_modifiers = 4;
/*     */   
/*  48 */   int inner_bean_accessor_modifiers = 4;
/*  49 */   int inner_bean_replacer_modifiers = 4;
/*     */   
/*  51 */   String innerBeanInitializationExpression = null;
/*     */   
/*     */   public void setInnerBeanClassName(String paramString) {
/*  54 */     this.innerBeanClassName = paramString;
/*     */   }
/*     */   public String getInnerBeanClassName() {
/*  57 */     return this.innerBeanClassName;
/*     */   }
/*     */   private String defaultInnerBeanInitializationExpression() {
/*  60 */     return "new " + this.innerBeanClassName + "()";
/*     */   }
/*     */   private String findInnerBeanClassName() {
/*  63 */     return (this.innerBeanClassName == null) ? "InnerBean" : this.innerBeanClassName;
/*     */   }
/*     */   private String findInnerBeanInitializationExpression() {
/*  66 */     return (this.innerBeanInitializationExpression == null) ? defaultInnerBeanInitializationExpression() : this.innerBeanInitializationExpression;
/*     */   }
/*     */   
/*     */   private int findInnerClassModifiers() {
/*  70 */     int i = 8;
/*  71 */     if (Modifier.isPublic(this.inner_bean_accessor_modifiers) || Modifier.isPublic(this.inner_bean_replacer_modifiers)) {
/*  72 */       i |= 0x1;
/*  73 */     } else if (Modifier.isProtected(this.inner_bean_accessor_modifiers) || Modifier.isProtected(this.inner_bean_replacer_modifiers)) {
/*  74 */       i |= 0x4;
/*  75 */     } else if (Modifier.isPrivate(this.inner_bean_accessor_modifiers) && Modifier.isPrivate(this.inner_bean_replacer_modifiers)) {
/*  76 */       i |= 0x2;
/*     */     } 
/*  78 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeSyntheticInnerBeanClass() throws IOException {
/*  85 */     int i = this.props.length;
/*  86 */     Property[] arrayOfProperty = new Property[i];
/*  87 */     for (byte b = 0; b < i; b++) {
/*     */       
/*  89 */       arrayOfProperty[b] = new SimplePropertyMask(this.props[b])
/*     */         {
/*     */           public int getVariableModifiers() {
/*  92 */             return 130;
/*     */           }
/*     */         };
/*     */     } 
/*  96 */     WrapperClassInfo wrapperClassInfo = new WrapperClassInfo(this.info)
/*     */       {
/*     */         public String getClassName() {
/*  99 */           return "InnerBean";
/*     */         }
/*     */         public int getModifiers() {
/* 102 */           return InnerBeanPropertyBeanGenerator.this.findInnerClassModifiers();
/*     */         }
/*     */       };
/* 105 */     createInnerGenerator().generate(wrapperClassInfo, arrayOfProperty, (Writer)this.iw);
/*     */   }
/*     */ 
/*     */   
/*     */   protected PropertyBeanGenerator createInnerGenerator() {
/* 110 */     SimplePropertyBeanGenerator simplePropertyBeanGenerator = new SimplePropertyBeanGenerator();
/* 111 */     simplePropertyBeanGenerator.setInner(true);
/* 112 */     simplePropertyBeanGenerator.addExtension(new SerializableExtension());
/* 113 */     CloneableExtension cloneableExtension = new CloneableExtension();
/* 114 */     cloneableExtension.setExceptionSwallowing(true);
/* 115 */     simplePropertyBeanGenerator.addExtension(cloneableExtension);
/* 116 */     return simplePropertyBeanGenerator;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void writeOtherVariables() throws IOException {
/* 121 */     this.iw.println(CodegenUtils.getModifierString(this.inner_bean_member_modifiers) + ' ' + findInnerBeanClassName() + " innerBean = " + findInnerBeanInitializationExpression() + ';');
/*     */     
/* 123 */     this.iw.println();
/* 124 */     this.iw.println(CodegenUtils.getModifierString(this.inner_bean_accessor_modifiers) + ' ' + findInnerBeanClassName() + " accessInnerBean()");
/*     */     
/* 126 */     this.iw.println("{ return innerBean; }");
/*     */   }
/*     */ 
/*     */   
/*     */   protected void writeOtherFunctions() throws IOException {
/* 131 */     this.iw.print(CodegenUtils.getModifierString(this.inner_bean_replacer_modifiers) + ' ' + findInnerBeanClassName() + " replaceInnerBean( " + findInnerBeanClassName() + " innerBean )");
/*     */     
/* 133 */     if (constrainedProperties()) {
/* 134 */       this.iw.println(" throws PropertyVetoException");
/*     */     } else {
/* 136 */       this.iw.println();
/* 137 */     }  this.iw.println("{");
/* 138 */     this.iw.upIndent();
/* 139 */     this.iw.println("beforeReplaceInnerBean();");
/* 140 */     this.iw.println("this.innerBean = innerBean;");
/* 141 */     this.iw.println("afterReplaceInnerBean();");
/* 142 */     this.iw.downIndent();
/* 143 */     this.iw.println("}");
/* 144 */     this.iw.println();
/*     */     
/* 146 */     boolean bool = Modifier.isAbstract(this.info.getModifiers());
/* 147 */     this.iw.print("protected ");
/* 148 */     if (bool)
/* 149 */       this.iw.print("abstract "); 
/* 150 */     this.iw.print("void beforeReplaceInnerBean()");
/* 151 */     if (constrainedProperties())
/* 152 */       this.iw.print(" throws PropertyVetoException"); 
/* 153 */     if (bool) {
/* 154 */       this.iw.println(';');
/*     */     } else {
/* 156 */       this.iw.println(" {} //hook method for subclasses");
/* 157 */     }  this.iw.println();
/*     */     
/* 159 */     this.iw.print("protected ");
/* 160 */     if (bool)
/* 161 */       this.iw.print("abstract "); 
/* 162 */     this.iw.print("void afterReplaceInnerBean()");
/* 163 */     if (bool) {
/* 164 */       this.iw.println(';');
/*     */     } else {
/* 166 */       this.iw.println(" {} //hook method for subclasses");
/* 167 */     }  this.iw.println();
/*     */     
/* 169 */     BeangenUtils.writeExplicitDefaultConstructor(1, this.info, this.iw);
/* 170 */     this.iw.println();
/* 171 */     this.iw.println("public " + this.info.getClassName() + "(" + findInnerBeanClassName() + " innerBean)");
/* 172 */     this.iw.println("{ this.innerBean = innerBean; }");
/*     */   }
/*     */ 
/*     */   
/*     */   protected void writeOtherClasses() throws IOException {
/* 177 */     if (this.innerBeanClassName == null) {
/* 178 */       writeSyntheticInnerBeanClass();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void writePropertyVariable(Property paramProperty) throws IOException {}
/*     */   
/*     */   protected void writePropertyGetter(Property paramProperty, Class paramClass) throws IOException {
/* 186 */     String str1 = paramProperty.getSimpleTypeName();
/* 187 */     String str2 = "boolean".equals(str1) ? "is" : "get";
/* 188 */     String str3 = str2 + BeangenUtils.capitalize(paramProperty.getName());
/* 189 */     this.iw.print(CodegenUtils.getModifierString(paramProperty.getGetterModifiers()));
/* 190 */     this.iw.println(' ' + paramProperty.getSimpleTypeName() + ' ' + str3 + "()");
/* 191 */     this.iw.println('{');
/* 192 */     this.iw.upIndent();
/* 193 */     this.iw.println(str1 + ' ' + paramProperty.getName() + " = innerBean." + str3 + "();");
/* 194 */     String str4 = getGetterDefensiveCopyExpression(paramProperty, paramClass);
/* 195 */     if (str4 == null) str4 = paramProperty.getName(); 
/* 196 */     this.iw.println("return " + str4 + ';');
/* 197 */     this.iw.downIndent();
/* 198 */     this.iw.println('}');
/*     */   }
/*     */ 
/*     */   
/*     */   protected void writePropertySetter(Property paramProperty, Class paramClass) throws IOException {
/* 203 */     String str1 = paramProperty.getSimpleTypeName();
/* 204 */     String str2 = "boolean".equals(str1) ? "is" : "get";
/*     */     
/* 206 */     String str3 = getSetterDefensiveCopyExpression(paramProperty, paramClass);
/* 207 */     if (str3 == null) str3 = paramProperty.getName(); 
/* 208 */     String str4 = "innerBean." + str2 + BeangenUtils.capitalize(paramProperty.getName()) + "()";
/* 209 */     String str5 = "innerBean.set" + BeangenUtils.capitalize(paramProperty.getName()) + "( " + str3 + " );";
/* 210 */     BeangenUtils.writePropertySetterWithGetExpressionSetStatement(paramProperty, str4, str5, this.iw);
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/codegen/bean/InnerBeanPropertyBeanGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */