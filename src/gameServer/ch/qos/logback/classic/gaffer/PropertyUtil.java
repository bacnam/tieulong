/*    */ package ch.qos.logback.classic.gaffer;
/*    */ import groovy.lang.Closure;
/*    */ import groovy.lang.GroovyObject;
/*    */ import groovy.lang.MetaClass;
/*    */ import java.beans.Introspector;
/*    */ import java.lang.ref.SoftReference;
/*    */ import org.codehaus.groovy.reflection.ClassInfo;
/*    */ import org.codehaus.groovy.runtime.BytecodeInterface8;
/*    */ import org.codehaus.groovy.runtime.GStringImpl;
/*    */ import org.codehaus.groovy.runtime.GeneratedClosure;
/*    */ import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
/*    */ import org.codehaus.groovy.runtime.callsite.CallSite;
/*    */ import org.codehaus.groovy.runtime.callsite.CallSiteArray;
/*    */ import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;
/*    */ import org.codehaus.groovy.runtime.typehandling.ShortTypeHandling;
/*    */ 
/*    */ public class PropertyUtil implements GroovyObject {
/*    */   public PropertyUtil() {
/*    */     CallSite[] arrayOfCallSite = $getCallSiteArray();
/*    */     MetaClass metaClass = $getStaticMetaClass();
/*    */     this.metaClass = metaClass;
/*    */   }
/*    */   
/*    */   public static boolean hasAdderMethod(Object obj, String name) {
/* 25 */     CallSite[] arrayOfCallSite = $getCallSiteArray(); String addMethod = null; if (!__$stMC && !BytecodeInterface8.disabledStandardMetaClass()) { GStringImpl gStringImpl1 = new GStringImpl(new Object[] { upperCaseFirstLetter(name) }, new String[] { "add", "" }); addMethod = ShortTypeHandling.castToString(gStringImpl1);
/* 26 */       return DefaultTypeTransformation.booleanUnbox(arrayOfCallSite[1].call(arrayOfCallSite[2].callGetProperty(obj), obj, addMethod)); }  GStringImpl gStringImpl = new GStringImpl(new Object[] { arrayOfCallSite[0].callStatic(PropertyUtil.class, name) }new String[] { "add", "" }); addMethod = ShortTypeHandling.castToString(gStringImpl); return DefaultTypeTransformation.booleanUnbox(arrayOfCallSite[1].call(arrayOfCallSite[2].callGetProperty(obj), obj, addMethod));
/*    */   }
/*    */   
/*    */   public static NestingType nestingType(Object obj, String name) {
/* 30 */     CallSite[] arrayOfCallSite = $getCallSiteArray(); Object decapitalizedName = arrayOfCallSite[3].call(Introspector.class, name);
/* 31 */     if (DefaultTypeTransformation.booleanUnbox(arrayOfCallSite[4].call(obj, decapitalizedName))) {
/* 32 */       return (NestingType)ShortTypeHandling.castToEnum(arrayOfCallSite[5].callGetProperty(NestingType.class), NestingType.class);
/*    */     }
/* 34 */     if (DefaultTypeTransformation.booleanUnbox(arrayOfCallSite[6].callStatic(PropertyUtil.class, obj, name))) {
/* 35 */       return (NestingType)ShortTypeHandling.castToEnum(arrayOfCallSite[7].callGetProperty(NestingType.class), NestingType.class);
/*    */     }
/* 37 */     return (NestingType)ShortTypeHandling.castToEnum(arrayOfCallSite[8].callGetProperty(NestingType.class), NestingType.class);
/*    */   }
/*    */   
/*    */   public static void attach(NestingType nestingType, Object component, Object subComponent, String name) {
/* 41 */     CallSite[] arrayOfCallSite = $getCallSiteArray(); NestingType nestingType1 = nestingType;
/* 42 */     if (ScriptBytecodeAdapter.isCase(nestingType1, arrayOfCallSite[9].callGetProperty(NestingType.class))) {
/* 43 */       Object object1 = arrayOfCallSite[10].call(Introspector.class, name); name = ShortTypeHandling.castToString(object1);
/* 44 */       Object object2 = subComponent; ScriptBytecodeAdapter.setProperty(object2, null, component, ShortTypeHandling.castToString(new GStringImpl(new Object[] { name }, new String[] { "", "" })));
/*    */     }
/* 46 */     else if (ScriptBytecodeAdapter.isCase(nestingType1, arrayOfCallSite[11].callGetProperty(NestingType.class))) {
/* 47 */       String firstUpperName = ShortTypeHandling.castToString(arrayOfCallSite[12].call(PropertyUtil.class, name));
/* 48 */       ScriptBytecodeAdapter.invokeMethodN(PropertyUtil.class, component, ShortTypeHandling.castToString(new GStringImpl(new Object[] { firstUpperName }, new String[] { "add", "" })), new Object[] { subComponent });
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public static String transformFirstLetter(String s, Closure closure) {
/* 54 */     CallSite[] arrayOfCallSite = $getCallSiteArray(); if (BytecodeInterface8.isOrigInt() && BytecodeInterface8.isOrigZ() && !__$stMC && !BytecodeInterface8.disabledStandardMetaClass()) { if ((ScriptBytecodeAdapter.compareEqual(s, null) || ScriptBytecodeAdapter.compareEqual(arrayOfCallSite[14].call(s), Integer.valueOf(0))))
/* 55 */         return s;  } else if ((ScriptBytecodeAdapter.compareEqual(s, null) || ScriptBytecodeAdapter.compareEqual(arrayOfCallSite[13].call(s), Integer.valueOf(0)))) { return s; }
/*    */     
/* 57 */     String firstLetter = ShortTypeHandling.castToString(arrayOfCallSite[15].callConstructor(String.class, arrayOfCallSite[16].call(s, Integer.valueOf(0))));
/*    */     
/* 59 */     String modifiedFistLetter = ShortTypeHandling.castToString(arrayOfCallSite[17].call(closure, firstLetter));
/*    */     
/* 61 */     if (ScriptBytecodeAdapter.compareEqual(arrayOfCallSite[18].call(s), Integer.valueOf(1))) {
/* 62 */       return modifiedFistLetter;
/*    */     }
/* 64 */     return ShortTypeHandling.castToString(arrayOfCallSite[19].call(modifiedFistLetter, arrayOfCallSite[20].call(s, Integer.valueOf(1))));
/*    */   }
/*    */   
/*    */   public static String upperCaseFirstLetter(String s)
/*    */   {
/* 69 */     CallSite[] arrayOfCallSite = $getCallSiteArray(); return ShortTypeHandling.castToString(arrayOfCallSite[21].callStatic(PropertyUtil.class, s, new _upperCaseFirstLetter_closure1(PropertyUtil.class, PropertyUtil.class))); } class _upperCaseFirstLetter_closure1 extends Closure implements GeneratedClosure { public Object doCall(String it) { CallSite[] arrayOfCallSite = $getCallSiteArray(); return arrayOfCallSite[0].call(it); }
/*    */ 
/*    */     
/*    */     public _upperCaseFirstLetter_closure1(Object _outerInstance, Object _thisObject) {
/*    */       super(_outerInstance, _thisObject);
/*    */     }
/*    */     
/*    */     public Object call(String it) {
/*    */       CallSite[] arrayOfCallSite = $getCallSiteArray();
/*    */       return (!__$stMC && !BytecodeInterface8.disabledStandardMetaClass()) ? doCall(it) : arrayOfCallSite[1].callCurrent((GroovyObject)this, it);
/*    */     } }
/*    */ 
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/ch/qos/logback/classic/gaffer/PropertyUtil.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */