/*    */ package org.apache.commons.cli;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
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
/*    */ public class MissingOptionException
/*    */   extends ParseException
/*    */ {
/*    */   private List missingOptions;
/*    */   
/*    */   public MissingOptionException(String message) {
/* 42 */     super(message);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MissingOptionException(List missingOptions) {
/* 54 */     this(createMessage(missingOptions));
/* 55 */     this.missingOptions = missingOptions;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public List getMissingOptions() {
/* 66 */     return this.missingOptions;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static String createMessage(List missingOptions) {
/* 77 */     StringBuffer buff = new StringBuffer("Missing required option");
/* 78 */     buff.append((missingOptions.size() == 1) ? "" : "s");
/* 79 */     buff.append(": ");
/*    */     
/* 81 */     Iterator it = missingOptions.iterator();
/* 82 */     while (it.hasNext()) {
/*    */       
/* 84 */       buff.append(it.next());
/* 85 */       if (it.hasNext())
/*    */       {
/* 87 */         buff.append(", ");
/*    */       }
/*    */     } 
/*    */     
/* 91 */     return buff.toString();
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/org/apache/commons/cli/MissingOptionException.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */