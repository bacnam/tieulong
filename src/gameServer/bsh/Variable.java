/*     */ package bsh;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class Variable implements Serializable {
/*     */   static final int DECLARATION = 0;
/*     */   static final int ASSIGNMENT = 1;
/*     */   String name;
/*   8 */   Class type = null;
/*     */   
/*     */   String typeDescriptor;
/*     */   Object value;
/*     */   Modifiers modifiers;
/*     */   LHS lhs;
/*     */   
/*     */   Variable(String name, Class type, LHS lhs) {
/*  16 */     this.name = name;
/*  17 */     this.lhs = lhs;
/*  18 */     this.type = type;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   Variable(String name, Object value, Modifiers modifiers) throws UtilEvalError {
/*  24 */     this(name, (Class)null, value, modifiers);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Variable(String name, String typeDescriptor, Object value, Modifiers modifiers) throws UtilEvalError {
/*  35 */     this(name, (Class)null, value, modifiers);
/*  36 */     this.typeDescriptor = typeDescriptor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Variable(String name, Class type, Object value, Modifiers modifiers) throws UtilEvalError {
/*  46 */     this.name = name;
/*  47 */     this.type = type;
/*  48 */     this.modifiers = modifiers;
/*  49 */     setValue(value, 0);
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
/*     */   public void setValue(Object value, int context) throws UtilEvalError {
/*  63 */     if (hasModifier("final") && this.value != null) {
/*  64 */       throw new UtilEvalError("Final variable, can't re-assign.");
/*     */     }
/*  66 */     if (value == null) {
/*  67 */       value = Primitive.getDefaultValue(this.type);
/*     */     }
/*  69 */     if (this.lhs != null) {
/*     */       
/*  71 */       this.lhs.assign(Primitive.unwrap(value), false);
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  77 */     if (this.type != null) {
/*  78 */       value = Types.castObject(value, this.type, (context == 0) ? 0 : 1);
/*     */     }
/*     */ 
/*     */     
/*  82 */     this.value = value;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Object getValue() throws UtilEvalError {
/*  93 */     if (this.lhs != null) {
/*  94 */       return (this.type == null) ? this.lhs.getValue() : Primitive.wrap(this.lhs.getValue(), this.type);
/*     */     }
/*     */     
/*  97 */     return this.value;
/*     */   }
/*     */   
/*     */   public Class getType() {
/* 101 */     return this.type;
/*     */   } public String getTypeDescriptor() {
/* 103 */     return this.typeDescriptor;
/*     */   } public Modifiers getModifiers() {
/* 105 */     return this.modifiers;
/*     */   } public String getName() {
/* 107 */     return this.name;
/*     */   }
/*     */   public boolean hasModifier(String name) {
/* 110 */     return (this.modifiers != null && this.modifiers.hasModifier(name));
/*     */   }
/*     */   
/*     */   public String toString() {
/* 114 */     return "Variable: " + super.toString() + " " + this.name + ", type:" + this.type + ", value:" + this.value + ", lhs = " + this.lhs;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/bsh/Variable.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */