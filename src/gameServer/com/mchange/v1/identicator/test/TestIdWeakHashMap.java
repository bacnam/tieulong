/*    */ package com.mchange.v1.identicator.test;
/*    */ 
/*    */ import com.mchange.v1.identicator.IdWeakHashMap;
/*    */ import com.mchange.v1.identicator.Identicator;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class TestIdWeakHashMap
/*    */ {
/* 44 */   static final Identicator id = new Identicator()
/*    */     {
/*    */       public boolean identical(Object param1Object1, Object param1Object2) {
/* 47 */         return (((String)param1Object1).charAt(0) == ((String)param1Object2).charAt(0));
/*    */       }
/*    */       public int hash(Object param1Object) {
/* 50 */         return ((String)param1Object).charAt(0);
/*    */       }
/*    */     };
/* 53 */   static final Map weak = (Map)new IdWeakHashMap(id);
/*    */ 
/*    */   
/*    */   public static void main(String[] paramArrayOfString) {
/* 57 */     doAdds();
/* 58 */     System.gc();
/* 59 */     show();
/* 60 */     setRemoveHi();
/* 61 */     System.gc();
/* 62 */     show();
/*    */   }
/*    */ 
/*    */   
/*    */   static void setRemoveHi() {
/* 67 */     String str = new String("bye");
/* 68 */     weak.put(str, "");
/* 69 */     Set set = weak.keySet();
/* 70 */     set.remove("hi");
/* 71 */     show();
/*    */   }
/*    */ 
/*    */   
/*    */   static void doAdds() {
/* 76 */     String str1 = "hi";
/* 77 */     String str2 = new String("hello");
/* 78 */     String str3 = new String("yoohoo");
/* 79 */     String str4 = new String("poop");
/*    */     
/* 81 */     weak.put(str1, "");
/* 82 */     weak.put(str2, "");
/* 83 */     weak.put(str3, "");
/* 84 */     weak.put(str4, "");
/*    */     
/* 86 */     show();
/*    */   }
/*    */ 
/*    */   
/*    */   static void show() {
/* 91 */     System.out.println("elements:");
/* 92 */     for (Iterator<String> iterator = weak.keySet().iterator(); iterator.hasNext();) {
/* 93 */       System.out.println("\t" + iterator.next());
/*    */     }
/* 95 */     System.out.println("size: " + weak.size());
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v1/identicator/test/TestIdWeakHashMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */