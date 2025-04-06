/*    */ package com.mchange.v2.c3p0.management;
/*    */ 
/*    */ import com.mchange.v2.c3p0.C3P0Registry;
/*    */ import java.sql.SQLException;
/*    */ import java.util.Iterator;
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
/*    */ 
/*    */ 
/*    */ public class C3P0RegistryManager
/*    */   implements C3P0RegistryManagerMBean
/*    */ {
/*    */   public String[] getAllIdentityTokens() {
/* 47 */     Set tokens = C3P0Registry.allIdentityTokens();
/* 48 */     return (String[])tokens.toArray((Object[])new String[tokens.size()]);
/*    */   }
/*    */   
/*    */   public Set getAllIdentityTokenized() {
/* 52 */     return C3P0Registry.allIdentityTokenized();
/*    */   }
/*    */   public Set getAllPooledDataSources() {
/* 55 */     return C3P0Registry.allPooledDataSources();
/*    */   }
/*    */   public int getAllIdentityTokenCount() {
/* 58 */     return C3P0Registry.allIdentityTokens().size();
/*    */   }
/*    */   public int getAllIdentityTokenizedCount() {
/* 61 */     return C3P0Registry.allIdentityTokenized().size();
/*    */   }
/*    */   public int getAllPooledDataSourcesCount() {
/* 64 */     return C3P0Registry.allPooledDataSources().size();
/*    */   }
/*    */   public String[] getAllIdentityTokenizedStringified() {
/* 67 */     return stringifySet(C3P0Registry.allIdentityTokenized());
/*    */   }
/*    */   public String[] getAllPooledDataSourcesStringified() {
/* 70 */     return stringifySet(C3P0Registry.allPooledDataSources());
/*    */   }
/*    */   public int getNumPooledDataSources() throws SQLException {
/* 73 */     return C3P0Registry.getNumPooledDataSources();
/*    */   }
/*    */   public int getNumPoolsAllDataSources() throws SQLException {
/* 76 */     return C3P0Registry.getNumPoolsAllDataSources();
/*    */   }
/*    */   public String getC3p0Version() {
/* 79 */     return "0.9.5-pre7";
/*    */   }
/*    */   
/*    */   private String[] stringifySet(Set s) {
/* 83 */     String[] out = new String[s.size()];
/* 84 */     int i = 0;
/* 85 */     for (Iterator<E> ii = s.iterator(); ii.hasNext();)
/* 86 */       out[i++] = ii.next().toString(); 
/* 87 */     return out;
/*    */   }
/*    */ }


/* Location:              /Users/bacnam/Projects/TieuLongProject/gameserver/gameServer.jar!/com/mchange/v2/c3p0/management/C3P0RegistryManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */