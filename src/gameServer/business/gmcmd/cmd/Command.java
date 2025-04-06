/*     */ package business.gmcmd.cmd;
/*     */ 
/*     */ import business.player.Player;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Parameter;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ public class Command
/*     */ {
/*     */   private String name;
/*     */   private String comment;
/*     */   private Method cmd;
/*     */   private Object excuter;
/*     */   
/*     */   public Command(business.gmcmd.annotation.Command annotation, Method m, Object excuter) {
/*  19 */     this.name = annotation.command().trim();
/*  20 */     if (this.name.isEmpty()) {
/*  21 */       this.name = m.getName().toLowerCase();
/*     */     }
/*  23 */     this.comment = annotation.comment();
/*  24 */     this.cmd = m;
/*  25 */     this.excuter = excuter;
/*     */   }
/*     */   
/*     */   public String run(Player player, String[] args) throws Exception {
/*  29 */     Object[] params = parseParams(player, (Object[])args);
/*     */     
/*  31 */     if (this.cmd.getReturnType() == null) {
/*  32 */       this.cmd.invoke(this.excuter, params);
/*  33 */       return "command[" + this.name + "] run success";
/*     */     } 
/*  35 */     Object rtn = this.cmd.invoke(this.excuter, params);
/*  36 */     if (rtn == null) {
/*  37 */       return "command[" + this.name + "] run success";
/*     */     }
/*  39 */     return rtn.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private Object[] parseParams(Player player, Object[] args) throws Exception {
/*  44 */     Class[] type = this.cmd.getParameterTypes();
/*  45 */     int parameCount = this.cmd.getParameterCount(); byte b; int j; Class[] arrayOfClass1;
/*  46 */     for (j = (arrayOfClass1 = type).length, b = 0; b < j; ) { Class<?> clazz = arrayOfClass1[b];
/*  47 */       if (clazz == Player.class) {
/*  48 */         parameCount--;
/*     */         break;
/*     */       } 
/*     */       b++; }
/*     */     
/*  53 */     if (args.length != parameCount) {
/*  54 */       throw new Exception("[" + this.name + "] needs " + type.length + " params but provide " + args.length + " params");
/*     */     }
/*     */     
/*  57 */     Object[] newArgs = new Object[type.length];
/*  58 */     if (parameCount < type.length) {
/*  59 */       int index = 0;
/*  60 */       for (int k = 0; k < type.length; k++) {
/*  61 */         if (type[k] == Player.class) {
/*  62 */           newArgs[k] = player;
/*     */         } else {
/*     */           
/*  65 */           newArgs[k] = args[index];
/*  66 */           index++;
/*     */         } 
/*     */       } 
/*     */     } 
/*  70 */     Object[] params = new Object[this.cmd.getParameterCount()];
/*  71 */     for (int i = 0; i < type.length; i++) {
/*  72 */       params[i] = parseParam(type[i], newArgs[i]);
/*     */     }
/*     */     
/*  75 */     return params;
/*     */   }
/*     */ 
/*     */   
/*     */   private Object parseParam(Class<?> clazz, Object object) throws Exception {
/*  80 */     if (clazz == Player.class) {
/*  81 */       return object;
/*     */     }
/*  83 */     String param = (String)object;
/*  84 */     if (List.class.isAssignableFrom(clazz)) {
/*     */       List<Object> list;
/*  86 */       if (clazz.isAssignableFrom(List.class)) {
/*  87 */         list = new ArrayList();
/*     */       } else {
/*  89 */         list = (List<Object>)clazz.newInstance();
/*     */       } 
/*  91 */       Type gt = clazz.getGenericSuperclass();
/*  92 */       ParameterizedType pt = (ParameterizedType)gt;
/*  93 */       Class<?> ptClass = (Class)pt.getActualTypeArguments()[0];
/*  94 */       String[] subparams = param.split(";"); byte b; int i; String[] arrayOfString1;
/*  95 */       for (i = (arrayOfString1 = subparams).length, b = 0; b < i; ) { String p = arrayOfString1[b];
/*  96 */         list.add(parseParam(ptClass, p)); b++; }
/*     */       
/*  98 */       return list;
/*  99 */     }  if (clazz.isArray()) {
/* 100 */       String[] subparams = param.split(";");
/* 101 */       Object[] array = new Object[subparams.length];
/* 102 */       Class<Object[]> ptClass = (Class)clazz.getComponentType();
/* 103 */       for (int i = 0; i < subparams.length; i++) {
/* 104 */         array[i] = parseParam(ptClass, subparams[i]);
/*     */       }
/* 106 */       return array;
/* 107 */     }  if (clazz == int.class || clazz == Integer.class)
/* 108 */       return Integer.valueOf(Integer.parseInt(param)); 
/* 109 */     if (clazz == long.class || clazz == Long.class)
/* 110 */       return Long.valueOf(Long.parseLong(param)); 
/* 111 */     if (clazz == float.class || clazz == Float.class)
/* 112 */       return Float.valueOf(param); 
/* 113 */     if (clazz == double.class || clazz == Double.class)
/* 114 */       return Double.valueOf(param); 
/* 115 */     if (clazz == boolean.class || clazz == Boolean.class)
/* 116 */       return Boolean.valueOf(param); 
/* 117 */     if (clazz == String.class)
/* 118 */       return param; 
/* 119 */     if (clazz.isEnum()) {
/* 120 */       return Enum.valueOf(clazz, param);
/*     */     }
/* 122 */     throw new Exception("can not instance " + clazz.getSimpleName() + " define for command:" + this.name);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 127 */     return String.valueOf(this.name) + " " + getParamString() + " " + this.comment;
/*     */   }
/*     */   
/*     */   private String getParamString() {
/* 131 */     StringBuilder stringBuilder = new StringBuilder(); byte b; int i; Parameter[] arrayOfParameter;
/* 132 */     for (i = (arrayOfParameter = this.cmd.getParameters()).length, b = 0; b < i; ) { Parameter parameter = arrayOfParameter[b];
/* 133 */       if (parameter.getType() != Player.class)
/*     */       {
/*     */         
/* 136 */         stringBuilder.append(parameter.getType().getSimpleName().toLowerCase()).append(" "); }  b++; }
/*     */     
/* 138 */     return stringBuilder.toString();
/*     */   }
/*     */   
/*     */   public String getName() {
/* 142 */     return this.name;
/*     */   }
/*     */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/business/gmcmd/cmd/Command.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */