/*     */ package ch.qos.logback.classic.gaffer;
/*     */ 
/*     */ import ch.qos.logback.core.joran.spi.NoAutoStartUtil;
/*     */ import ch.qos.logback.core.spi.ContextAwareBase;
/*     */ import groovy.lang.Closure;
/*     */ import groovy.lang.GroovyObject;
/*     */ import groovy.lang.MetaClass;
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import org.codehaus.groovy.reflection.ClassInfo;
/*     */ import org.codehaus.groovy.runtime.BytecodeInterface8;
/*     */ import org.codehaus.groovy.runtime.GStringImpl;
/*     */ import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
/*     */ import org.codehaus.groovy.runtime.callsite.CallSite;
/*     */ import org.codehaus.groovy.runtime.callsite.CallSiteArray;
/*     */ import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;
/*     */ import org.codehaus.groovy.runtime.typehandling.ShortTypeHandling;
/*     */ 
/*     */ public class ComponentDelegate
/*     */   extends ContextAwareBase
/*     */   implements GroovyObject
/*     */ {
/*     */   private final Object component;
/*     */   private final List fieldsToCascade;
/*     */   
/*     */   public ComponentDelegate(Object component) {
/*  28 */     CallSite[] arrayOfCallSite = $getCallSiteArray(); List list = ScriptBytecodeAdapter.createList(new Object[0]); MetaClass metaClass = $getStaticMetaClass();
/*     */ 
/*     */     
/*  31 */     Object object = SYNTHETIC_LOCAL_VARIABLE_1;
/*     */   }
/*     */   public String getLabel() {
/*  34 */     CallSite[] arrayOfCallSite = $getCallSiteArray(); return "component";
/*     */   } public String getLabelFistLetterInUpperCase() {
/*  36 */     CallSite[] arrayOfCallSite = $getCallSiteArray(); return (!__$stMC && !BytecodeInterface8.disabledStandardMetaClass()) ? ShortTypeHandling.castToString(arrayOfCallSite[6].call(arrayOfCallSite[7].call(arrayOfCallSite[8].call(getLabel(), Integer.valueOf(0))), arrayOfCallSite[9].call(getLabel(), Integer.valueOf(1)))) : ShortTypeHandling.castToString(arrayOfCallSite[0].call(arrayOfCallSite[1].call(arrayOfCallSite[2].call(arrayOfCallSite[3].callCurrent(this), Integer.valueOf(0))), arrayOfCallSite[4].call(arrayOfCallSite[5].callCurrent(this), Integer.valueOf(1))));
/*     */   }
/*     */   
/*  39 */   public void methodMissing(String name, Object args) { CallSite[] arrayOfCallSite = $getCallSiteArray(); NestingType nestingType = (NestingType)ShortTypeHandling.castToEnum(arrayOfCallSite[10].call(PropertyUtil.class, this.component, name), NestingType.class);
/*  40 */     if (ScriptBytecodeAdapter.compareEqual(nestingType, arrayOfCallSite[11].callGetProperty(NestingType.class))) {
/*  41 */       arrayOfCallSite[12].callCurrent(this, new GStringImpl(new Object[] { arrayOfCallSite[13].callCurrent(this), arrayOfCallSite[14].callCurrent(this), arrayOfCallSite[15].callGetProperty(arrayOfCallSite[16].call(this.component)), name }, new String[] { "", " ", " of type [", "] has no appplicable [", "] property " }));
/*     */       
/*     */       return;
/*     */     } 
/*  45 */     String subComponentName = null;
/*  46 */     Class clazz = null;
/*  47 */     Closure closure = null;
/*  48 */     Object object = arrayOfCallSite[17].callCurrent(this, args); subComponentName = ShortTypeHandling.castToString(arrayOfCallSite[18].call(object, Integer.valueOf(0))); clazz = ShortTypeHandling.castToClass(arrayOfCallSite[19].call(object, Integer.valueOf(1))); closure = (Closure)ScriptBytecodeAdapter.castToType(arrayOfCallSite[20].call(object, Integer.valueOf(2)), Closure.class);
/*  49 */     if (BytecodeInterface8.isOrigZ() && !__$stMC && !BytecodeInterface8.disabledStandardMetaClass()) { if (ScriptBytecodeAdapter.compareNotEqual(clazz, null))
/*  50 */       { Object subComponent = arrayOfCallSite[38].call(clazz);
/*  51 */         if ((DefaultTypeTransformation.booleanUnbox(subComponentName) && DefaultTypeTransformation.booleanUnbox(arrayOfCallSite[39].call(subComponent, name)))) {
/*  52 */           String str = subComponentName; ScriptBytecodeAdapter.setProperty(str, null, subComponent, "name");
/*     */         } 
/*  54 */         if (subComponent instanceof ch.qos.logback.core.spi.ContextAware) {
/*  55 */           Object object1 = arrayOfCallSite[40].callGroovyObjectGetProperty(this); ScriptBytecodeAdapter.setProperty(object1, null, subComponent, "context");
/*     */         } 
/*  57 */         if (DefaultTypeTransformation.booleanUnbox(closure)) {
/*  58 */           ComponentDelegate subDelegate = (ComponentDelegate)ScriptBytecodeAdapter.castToType(arrayOfCallSite[41].callConstructor(ComponentDelegate.class, subComponent), ComponentDelegate.class);
/*     */           
/*  60 */           arrayOfCallSite[42].callCurrent(this, subDelegate);
/*  61 */           Object object1 = arrayOfCallSite[43].callGroovyObjectGetProperty(this); ScriptBytecodeAdapter.setGroovyObjectProperty(object1, ComponentDelegate.class, subDelegate, "context");
/*  62 */           arrayOfCallSite[44].callCurrent(this, subComponent);
/*  63 */           ComponentDelegate componentDelegate1 = subDelegate; ScriptBytecodeAdapter.setGroovyObjectProperty(componentDelegate1, ComponentDelegate.class, (GroovyObject)closure, "delegate");
/*  64 */           Object object2 = arrayOfCallSite[45].callGetProperty(Closure.class); ScriptBytecodeAdapter.setGroovyObjectProperty(object2, ComponentDelegate.class, (GroovyObject)closure, "resolveStrategy");
/*  65 */           arrayOfCallSite[46].call(closure);
/*     */         } 
/*  67 */         if ((subComponent instanceof ch.qos.logback.core.spi.LifeCycle && DefaultTypeTransformation.booleanUnbox(arrayOfCallSite[47].call(NoAutoStartUtil.class, subComponent)))) {
/*  68 */           arrayOfCallSite[48].call(subComponent);
/*     */         }
/*  70 */         arrayOfCallSite[49].call(PropertyUtil.class, nestingType, this.component, subComponent, name); }
/*     */       else
/*  72 */       { arrayOfCallSite[50].callCurrent(this, new GStringImpl(new Object[] { name, getLabel(), getComponentName(), arrayOfCallSite[51].callGetProperty(arrayOfCallSite[52].call(this.component)) }, new String[] { "No 'class' argument specified for [", "] in ", " ", " of type [", "]" })); }  return; }  if (ScriptBytecodeAdapter.compareNotEqual(clazz, null)) { Object subComponent = arrayOfCallSite[21].call(clazz); if ((DefaultTypeTransformation.booleanUnbox(subComponentName) && DefaultTypeTransformation.booleanUnbox(arrayOfCallSite[22].call(subComponent, name)))) { String str = subComponentName; ScriptBytecodeAdapter.setProperty(str, null, subComponent, "name"); }  if (subComponent instanceof ch.qos.logback.core.spi.ContextAware) { Object object1 = arrayOfCallSite[23].callGroovyObjectGetProperty(this); ScriptBytecodeAdapter.setProperty(object1, null, subComponent, "context"); }  if (DefaultTypeTransformation.booleanUnbox(closure)) { ComponentDelegate subDelegate = (ComponentDelegate)ScriptBytecodeAdapter.castToType(arrayOfCallSite[24].callConstructor(ComponentDelegate.class, subComponent), ComponentDelegate.class); arrayOfCallSite[25].callCurrent(this, subDelegate); Object object1 = arrayOfCallSite[26].callGroovyObjectGetProperty(this); ScriptBytecodeAdapter.setGroovyObjectProperty(object1, ComponentDelegate.class, subDelegate, "context"); arrayOfCallSite[27].callCurrent(this, subComponent); ComponentDelegate componentDelegate1 = subDelegate; ScriptBytecodeAdapter.setGroovyObjectProperty(componentDelegate1, ComponentDelegate.class, (GroovyObject)closure, "delegate"); Object object2 = arrayOfCallSite[28].callGetProperty(Closure.class); ScriptBytecodeAdapter.setGroovyObjectProperty(object2, ComponentDelegate.class, (GroovyObject)closure, "resolveStrategy"); arrayOfCallSite[29].call(closure); }  if ((subComponent instanceof ch.qos.logback.core.spi.LifeCycle && DefaultTypeTransformation.booleanUnbox(arrayOfCallSite[30].call(NoAutoStartUtil.class, subComponent)))) arrayOfCallSite[31].call(subComponent);  arrayOfCallSite[32].call(PropertyUtil.class, nestingType, this.component, subComponent, name); } else { arrayOfCallSite[33].callCurrent(this, new GStringImpl(new Object[] { name, arrayOfCallSite[34].callCurrent(this), arrayOfCallSite[35].callCurrent(this), arrayOfCallSite[36].callGetProperty(arrayOfCallSite[37].call(this.component)) }, new String[] { "No 'class' argument specified for [", "] in ", " ", " of type [", "]" })); }
/*     */      } public void cascadeFields(ComponentDelegate subDelegate) {
/*     */     CallSite[] arrayOfCallSite;
/*     */     String k;
/*     */     Iterator iterator;
/*  77 */     for (arrayOfCallSite = $getCallSiteArray(), k = null, iterator = (Iterator)ScriptBytecodeAdapter.castToType(arrayOfCallSite[53].call(this.fieldsToCascade), Iterator.class); iterator.hasNext(); ) { k = ShortTypeHandling.castToString(iterator.next());
/*  78 */       Object object = ScriptBytecodeAdapter.getGroovyObjectProperty(ComponentDelegate.class, this, ShortTypeHandling.castToString(new GStringImpl(new Object[] { k }, new String[] { "", "" }))); ScriptBytecodeAdapter.setProperty(object, null, arrayOfCallSite[54].callGroovyObjectGetProperty(subDelegate), ShortTypeHandling.castToString(new GStringImpl(new Object[] { k }, new String[] { "", "" }))); }
/*     */   
/*     */   }
/*     */   
/*     */   public void injectParent(Object subComponent) {
/*  83 */     CallSite[] arrayOfCallSite = $getCallSiteArray(); if (DefaultTypeTransformation.booleanUnbox(arrayOfCallSite[55].call(subComponent, "parent"))) {
/*  84 */       Object object = this.component; ScriptBytecodeAdapter.setProperty(object, null, subComponent, "parent");
/*     */     } 
/*     */   }
/*     */   
/*     */   public void propertyMissing(String name, Object value) {
/*  89 */     CallSite[] arrayOfCallSite = $getCallSiteArray(); NestingType nestingType = (NestingType)ShortTypeHandling.castToEnum(arrayOfCallSite[56].call(PropertyUtil.class, this.component, name), NestingType.class);
/*  90 */     if (ScriptBytecodeAdapter.compareEqual(nestingType, arrayOfCallSite[57].callGetProperty(NestingType.class))) {
/*  91 */       arrayOfCallSite[58].callCurrent(this, new GStringImpl(new Object[] { arrayOfCallSite[59].callCurrent(this), arrayOfCallSite[60].callCurrent(this), arrayOfCallSite[61].callGetProperty(arrayOfCallSite[62].call(this.component)), name }, new String[] { "", " ", " of type [", "] has no appplicable [", "] property " }));
/*     */       return;
/*     */     } 
/*  94 */     arrayOfCallSite[63].call(PropertyUtil.class, nestingType, this.component, value, name);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object analyzeArgs(Object... args) {
/*  99 */     CallSite[] arrayOfCallSite = $getCallSiteArray(); String name = null;
/* 100 */     Class clazz = null;
/* 101 */     Closure closure = null;
/*     */     
/* 103 */     if (ScriptBytecodeAdapter.compareGreaterThan(arrayOfCallSite[64].call(args), Integer.valueOf(3))) {
/* 104 */       arrayOfCallSite[65].callCurrent(this, new GStringImpl(new Object[] { args }, new String[] { "At most 3 arguments allowed but you passed ", "" }));
/* 105 */       return ScriptBytecodeAdapter.createList(new Object[] { name, clazz, closure });
/*     */     } 
/*     */     
/* 108 */     if (!__$stMC && !BytecodeInterface8.disabledStandardMetaClass()) { if (BytecodeInterface8.objectArrayGet(args, Integer.valueOf(-1).intValue()) instanceof Closure)
/* 109 */       { Object object = BytecodeInterface8.objectArrayGet(args, Integer.valueOf(-1).intValue()); closure = (Closure)ScriptBytecodeAdapter.castToType(object, Closure.class);
/* 110 */         args = (Object[])ScriptBytecodeAdapter.castToType(arrayOfCallSite[70].call(args, BytecodeInterface8.objectArrayGet(args, Integer.valueOf(-1).intValue())), Object[].class); arrayOfCallSite[70].call(args, BytecodeInterface8.objectArrayGet(args, Integer.valueOf(-1).intValue())); }  } else if (arrayOfCallSite[66].call(args, Integer.valueOf(-1)) instanceof Closure) { Object object = arrayOfCallSite[67].call(args, Integer.valueOf(-1)); closure = (Closure)ScriptBytecodeAdapter.castToType(object, Closure.class); args = (Object[])ScriptBytecodeAdapter.castToType(arrayOfCallSite[68].call(args, arrayOfCallSite[69].call(args, Integer.valueOf(-1))), Object[].class); arrayOfCallSite[68].call(args, arrayOfCallSite[69].call(args, Integer.valueOf(-1))); }
/*     */ 
/*     */     
/* 113 */     if (BytecodeInterface8.isOrigInt() && BytecodeInterface8.isOrigZ() && !__$stMC && !BytecodeInterface8.disabledStandardMetaClass()) { if (ScriptBytecodeAdapter.compareEqual(arrayOfCallSite[74].call(args), Integer.valueOf(1)))
/* 114 */       { Object object = arrayOfCallSite[75].callCurrent(this, BytecodeInterface8.objectArrayGet(args, 0)); clazz = ShortTypeHandling.castToClass(object); }  } else if (ScriptBytecodeAdapter.compareEqual(arrayOfCallSite[71].call(args), Integer.valueOf(1))) { Object object = arrayOfCallSite[72].callCurrent(this, arrayOfCallSite[73].call(args, Integer.valueOf(0))); clazz = ShortTypeHandling.castToClass(object); }
/*     */ 
/*     */     
/* 117 */     if (BytecodeInterface8.isOrigInt() && BytecodeInterface8.isOrigZ() && !__$stMC && !BytecodeInterface8.disabledStandardMetaClass()) { if (ScriptBytecodeAdapter.compareEqual(arrayOfCallSite[81].call(args), Integer.valueOf(2))) {
/* 118 */         Object object1 = arrayOfCallSite[82].callCurrent(this, BytecodeInterface8.objectArrayGet(args, 0)); name = ShortTypeHandling.castToString(object1);
/* 119 */         Object object2 = arrayOfCallSite[83].callCurrent(this, BytecodeInterface8.objectArrayGet(args, 1)); clazz = ShortTypeHandling.castToClass(object2);
/*     */       } 
/*     */       
/* 122 */       return ScriptBytecodeAdapter.createList(new Object[] { name, clazz, closure }); }  if (ScriptBytecodeAdapter.compareEqual(arrayOfCallSite[76].call(args), Integer.valueOf(2))) { Object object1 = arrayOfCallSite[77].callCurrent(this, arrayOfCallSite[78].call(args, Integer.valueOf(0))); name = ShortTypeHandling.castToString(object1); Object object2 = arrayOfCallSite[79].callCurrent(this, arrayOfCallSite[80].call(args, Integer.valueOf(1))); clazz = ShortTypeHandling.castToClass(object2); }  return ScriptBytecodeAdapter.createList(new Object[] { name, clazz, closure });
/*     */   }
/*     */   
/*     */   public Class parseClassArgument(Object arg) {
/* 126 */     CallSite[] arrayOfCallSite = $getCallSiteArray(); if (arg instanceof Class)
/* 127 */       return ShortTypeHandling.castToClass(arg); 
/* 128 */     if (arg instanceof String) {
/* 129 */       return Class.forName(ShortTypeHandling.castToString(arg));
/*     */     }
/* 131 */     arrayOfCallSite[84].callCurrent(this, new GStringImpl(new Object[] { arrayOfCallSite[85].callGetProperty(arrayOfCallSite[86].call(arg)) }, new String[] { "Unexpected argument type ", "" }));
/* 132 */     return ShortTypeHandling.castToClass(null);
/*     */   }
/*     */ 
/*     */   
/*     */   public String parseNameArgument(Object arg) {
/* 137 */     CallSite[] arrayOfCallSite = $getCallSiteArray(); if (arg instanceof String) {
/* 138 */       return ShortTypeHandling.castToString(arg);
/*     */     }
/* 140 */     arrayOfCallSite[87].callCurrent(this, "With 2 or 3 arguments, the first argument must be the component name, i.e of type string");
/* 141 */     return ShortTypeHandling.castToString(null);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getComponentName() {
/* 146 */     CallSite[] arrayOfCallSite = $getCallSiteArray(); if (DefaultTypeTransformation.booleanUnbox(arrayOfCallSite[88].call(this.component, "name"))) {
/* 147 */       return ShortTypeHandling.castToString(new GStringImpl(new Object[] { arrayOfCallSite[89].callGetProperty(this.component) }, new String[] { "[", "]" }));
/*     */     }
/* 149 */     return "";
/*     */   }
/*     */   
/*     */   public final Object getComponent() {
/*     */     return this.component;
/*     */   }
/*     */   
/*     */   public final List getFieldsToCascade() {
/*     */     return this.fieldsToCascade;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/ch/qos/logback/classic/gaffer/ComponentDelegate.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */